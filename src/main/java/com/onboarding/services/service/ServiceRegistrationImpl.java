package com.onboarding.services.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboarding.services.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


@Service("serviceRegistrationImpl")
public class ServiceRegistrationImpl implements IServiceRegistration {

    Logger logger = LoggerFactory.getLogger(ServiceRegistrationImpl.class);

    @Value("${KongAdminAPIURL}")
    private String kongAdminAPIUrl;

    @Override
    public void createService(ServiceConfig service) throws IOException, InterruptedException {

        logger.info("Execution started In createService() ");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.createObjectNode().put("name", service.getName())
                .put("url", service.getUrl());


        HttpClient client = HttpClient.newHttpClient();

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kongAdminAPIUrl + "/services"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(node)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createServiceRestCall : {} ", response.statusCode());

        if ((response.statusCode() == 201 || response.statusCode() == 200)
                && !service.getRoutes().isEmpty()) {
            KongResponse responseBody = mapper.readValue(response.body(), KongResponse.class);
            service.setServiceId(responseBody.getId().toString());
            for (Route route : service.getRoutes()) {
                createRoute(route, service.getServiceId());
            }

            createDefaultPlugins(service);

            createOptionalPlugins(service);
        }

    }

    private void createOptionalPlugins(ServiceConfig service)  throws IOException, InterruptedException {
        if(null != service.getServicePlugins() && !service.getServicePlugins().isEmpty()) {
            for (PluginConfig config : service.getServicePlugins()) {
                createCorrelationIdPlugin(service, config);
                createKeyAuthCredentialsAndPlugin(service, config);
            }
        }
    }

    private void createKeyAuthCredentialsAndPlugin(ServiceConfig service, PluginConfig config) throws IOException, InterruptedException {
        if (config.getName().equals("key-auth")) {
            config.setEnabled(true);
            if (null != config.getConfig()) {
                createConsumer(config.getConfig(), service.getServiceId());
                config.setRateLimitterConfig(getAuthenticationKeyConfiguration(config.getConfig()));
                createPlugin(config, service.getServiceId());
                if (null != config.getConfig().getConsumerId()) {
                    createConsumerCredential(config.getConfig(), service.getServiceId());
                }
            }
        }
    }

    private void createCorrelationIdPlugin(ServiceConfig service, PluginConfig config) throws IOException, InterruptedException {
        if (config.getName().equals("correlation-id")) {
            config.setEnabled(true);
            if (null != config.getConfig()) {
                config.setRateLimitterConfig(getCorrelationIdConfiguration(config.getConfig()));
                createPlugin(config, service.getServiceId());
            }
        }
    }

    private void createDefaultPlugins(ServiceConfig service) throws IOException, InterruptedException {

        //Rate-Limiting Plugin configuration
        PluginConfig config = new PluginConfig();
        config.setName("rate-limiting");
        config.setEnabled(true);
        config.setRateLimitterConfig(getRateLimitterConfiguration());
        createPlugin(config, service.getServiceId());

        //File Logging
        PluginConfig filePathConfig = new PluginConfig();
        filePathConfig.setName("file-log");
        filePathConfig.setEnabled(true);
        filePathConfig.setRateLimitterConfig(getFileLoggingConfiguration(service));
        createPlugin(filePathConfig, service.getServiceId());

        //CORS
        PluginConfig corsPluginConfig = new PluginConfig();
        corsPluginConfig.setName("cors");
        corsPluginConfig.setEnabled(true);
        corsPluginConfig.setRateLimitterConfig(getCORSPluginConfiguration());
        createPlugin(corsPluginConfig, service.getServiceId());

    }

    private JsonNode getCORSPluginConfiguration(){
        JsonNode config = new ObjectMapper().createObjectNode()
                .put("max_age", 3800)
                .put("preflight_continue", false);

        return config;
    }

    private JsonNode getCorrelationIdConfiguration(CommonConfig commonConfig) {

        JsonNode config = new ObjectMapper().createObjectNode()
                .put("header_name", "Kong-Request-CorrelationID")
                .put("generator",  commonConfig.getGenerator())
                .put("echo_downstream",commonConfig.getEchoDownstream());

        return config;

    }

    private JsonNode getAuthenticationKeyConfiguration(CommonConfig commonConfig) {

        JsonNode config = new ObjectMapper().createObjectNode()
                .put("run_on_preflight",commonConfig.getRunOnPreflight())
                .put("key_in_header",Boolean.TRUE);

        return config;

    }

    private JsonNode getFileLoggingConfiguration(ServiceConfig serviceConfig) {

        JsonNode config = new ObjectMapper().createObjectNode()
                .put("path", "/tmp/" + serviceConfig.getName() + ".log")
                .put("reopen", false);

        return config;

    }

    @Override
    public void createRoute(Route route, String serviceId) throws IOException, InterruptedException {

        logger.info("Execution started In createRoute() ");

        ObjectMapper mapper = new ObjectMapper();

        HttpClient client = HttpClient.newHttpClient();

        logger.info("Request Created for creating route : {} ",mapper.writeValueAsString(route));
        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kongAdminAPIUrl + "/services/" + serviceId + "/routes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(route)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createRouteRestCall : {} ", response.statusCode());

    }

    @Override
    public void createGlobalPlugin(PluginConfig config) throws IOException, InterruptedException {

        logger.info("Execution started In createGlobalPlugin() ");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonBody = mapper.createObjectNode()
                .put("name", config.getName())
                .put("enabled", config.getEnabled());

        HttpClient client = HttpClient.newHttpClient();

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kongAdminAPIUrl + "/plugins"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(jsonBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createGlobalPluginRestCall : {} ", response.statusCode());


    }

    @Override
    public void createPlugin(PluginConfig config, String serviceId) throws IOException, InterruptedException {


        logger.info("Execution started In createPlugin() ");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonBody = mapper.createObjectNode()
                .put("name", config.getName())
                .put("enabled", config.getEnabled())
                .set("config", config.getRateLimitterConfig());

        HttpClient client = HttpClient.newHttpClient();

        logger.info("Request created for create Plugin : {} ", String.valueOf(jsonBody));

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kongAdminAPIUrl + "/services/" + serviceId + "/plugins"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(jsonBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createPluginRestCall : {} ", response.statusCode());


    }

    @Override
    public void createConsumerCredential(CommonConfig config, String serviceId) throws IOException, InterruptedException {


        logger.info("Execution started In createConsumerCredential() ");

        ObjectMapper mapper = new ObjectMapper();

        HttpClient client = HttpClient.newHttpClient();
        JsonNode jsonBody = mapper.createObjectNode()
                .put("key", config.getKey());

        logger.info("Request created for create consumer credentials : {} ", String.valueOf(jsonBody));

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kongAdminAPIUrl + "/consumers/" + config.getConsumerId() + "/key-auth"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(jsonBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createConsumerCredential : {} ", response.statusCode());


    }

    private JsonNode getRateLimitterConfiguration() {

        JsonNode config = new ObjectMapper().createObjectNode()
                .put("second", 60)
                .put("hour", 10000)
                .put("policy", "local");

        return config;
    }

    @Override
    public void createConsumer(CommonConfig config,String serviceId) throws IOException, InterruptedException {

        logger.info("Execution started In createRoute() ");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonBody = mapper.createObjectNode()
                .put("username", config.getUser())
                .put("custom_id", config.getUser());

        HttpClient client = HttpClient.newHttpClient();

        logger.info("Request Created for creating consumer : {} ",mapper.writeValueAsString(jsonBody));
        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(kongAdminAPIUrl + "/consumers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(jsonBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createConsumerRestCall : {} ", response.statusCode());
        KongResponse responseBody = mapper.readValue(response.body(), KongResponse.class);
        config.setConsumerId(responseBody.getId().toString());


    }


}
