package com.onboarding.services.service;

import com.onboarding.services.model.PluginConfig;
import com.onboarding.services.model.Route;
import com.onboarding.services.model.ServiceConfig;

import java.io.IOException;

public interface IServiceRegistration {

    /**
     * @param service
     * @throws IOException
     * @throws InterruptedException
     */
    public void createService(ServiceConfig service) throws IOException, InterruptedException;

    public void createRoute(Route route,String serviceId) throws IOException, InterruptedException;

    public void createGlobalPlugin(PluginConfig config) throws IOException, InterruptedException;

    public void createPlugin(PluginConfig config,String serviceId) throws IOException, InterruptedException;
}
