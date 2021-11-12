package com.onboarding.services.service;

import com.onboarding.services.model.*;
import org.apache.commons.codec.EncoderException;

import java.io.IOException;

public interface IServiceRegistration {

    /**
     * @param service
     * @throws IOException
     * @throws InterruptedException
     */
    public RegistrationResponse createService(ServiceConfig service) throws IOException, InterruptedException, EncoderException;

    public MessageConfig createRoute(Route route, String serviceId) throws IOException, InterruptedException;

    public void createGlobalPlugin(PluginConfig config) throws IOException, InterruptedException;

    public MessageConfig createPlugin(PluginConfig config, String serviceId) throws IOException, InterruptedException;

    public MessageConfig createConsumer(CommonConfig commonConfig, String serviceId) throws IOException, InterruptedException;

    public MessageConfig createConsumerCredential(CommonConfig commonConfig, String serviceId) throws IOException, InterruptedException;

}
