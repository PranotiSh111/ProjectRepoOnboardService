package com.onboarding.services.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.onboarding.services.model.KongResponse;
import com.onboarding.services.model.PluginConfig;
import com.onboarding.services.model.Route;
import com.onboarding.services.model.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service("serviceRegistrationImpl")
public class ServiceRegistrationImpl implements IServiceRegistration {

    Logger logger = LoggerFactory.getLogger(ServiceRegistrationImpl.class);

    @Override
    public void createService(ServiceConfig service) throws IOException, InterruptedException {

        logger.info("Execution started In createService() ");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.createObjectNode().put("name",service.getName())
                .put("url",service.getUrl());


        HttpClient client = HttpClient.newHttpClient();

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                              URI.create("http://localhost:8001/services"))
                              .header("Content-Type", "application/json")
                               .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(node)))
                                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createServiceRestCall : {} ", response.statusCode());

        if((response.statusCode() == 201 || response.statusCode() == 200 )
                && !service.getRoutes().isEmpty()) {
            KongResponse responseBody = mapper.readValue(response.body(),KongResponse.class);
            logger.info("Service Id : {} ", responseBody.getId());
            service.setServiceId(responseBody.getId().toString());
            logger.info("Service Id : {} ", service.getServiceId());
            for (Route route : service.getRoutes()) {
                createRoute(route,service.getServiceId());
            }

            PluginConfig config = new PluginConfig();
            config.setName("rate-limiting");
            config.setEnabled(true);
            config.setRateLimitterConfig(getRateLimitterConfiguration());
            createPlugin(config,service.getServiceId());

        }
    }

    @Override
    public void createRoute(Route route,String serviceId) throws IOException, InterruptedException {

        logger.info("Execution started In createRoute() ");

        ObjectMapper mapper = new ObjectMapper();

        ArrayNode pathsArrayNode = mapper.createArrayNode();
        route.getPaths().forEach((path) -> pathsArrayNode.add(path));

        ArrayNode methods =  mapper.createArrayNode();
        route.getMethods().forEach((method) -> methods.add(method));

        JsonNode jsonBody =  mapper.createObjectNode()
                .put("name",route.getName())
                .set("paths",pathsArrayNode);

        HttpClient client = HttpClient.newHttpClient();

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:8001/services/" + serviceId + "/routes"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(jsonBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createRouteRestCall : {} ", response.statusCode());

    }

    @Override
    public void createGlobalPlugin(PluginConfig config) throws IOException, InterruptedException{

        logger.info("Execution started In createGlobalPlugin() ");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonBody =  mapper.createObjectNode()
                .put("name",config.getName())
                .put("enabled",config.getEnabled());

        HttpClient client = HttpClient.newHttpClient();

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:8001/plugins"))
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

        JsonNode jsonBody =  mapper.createObjectNode()
                .put("name",config.getName())
                .put("enabled",config.getEnabled())
                .set("config",config.getRateLimitterConfig());

        HttpClient client = HttpClient.newHttpClient();

        logger.info("Request created for create Plugin : {} ",String.valueOf(jsonBody));

        // create a request
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:8001/services/" + serviceId + "/plugins"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(jsonBody)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("Response of createPluginRestCall : {} ", response.statusCode());


    }

    private JsonNode getRateLimitterConfiguration(){

        JsonNode config = new ObjectMapper().createObjectNode()
                .put("second",60)
                .put("hour",10000)
                .put("policy","local");

        return config;
    }

}
