package com.onboarding.services.service;

import com.onboarding.services.controller.OnBoardServiceController;
import com.onboarding.services.exception.OnBoardingServiceException;
import com.onboarding.services.model.PluginConfig;
import com.onboarding.services.model.RegistrationRequest;
import com.onboarding.services.model.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("onBoardServiceImpl")
public class OnBoardServiceImpl implements IOnBoardService {

    @Autowired
    private IServiceRegistration iserviceRegistration;

    Logger logger = LoggerFactory.getLogger(OnBoardServiceImpl.class);

    @Override
    public void onboardServices(RegistrationRequest registrationRequest) throws OnBoardingServiceException {
        try {
            logger.info("Execution started In onboardServices() ");
            for (ServiceConfig service : registrationRequest.getServices()) {
                iserviceRegistration.createService(service);
            }
            if (null != registrationRequest.getGlobalPlugins() && !registrationRequest.getGlobalPlugins().isEmpty()) {
                for (PluginConfig config : registrationRequest.getGlobalPlugins()) {
                    iserviceRegistration.createGlobalPlugin(config);
                }
            }
            logger.info("Execution completed In onboardServices() ");
        } catch (InterruptedException exception) {
            logger.error("Exception thrown while executing onboardServices() : {} ", exception);
        } catch (Exception exception) {
            logger.error("Exception thrown while executing onboardServices() : {} ", exception);
        }
    }

}
