package com.onboarding.services.service;

import com.onboarding.services.controller.OnBoardServiceController;
import com.onboarding.services.exception.OnBoardingServiceException;
import com.onboarding.services.model.PluginConfig;
import com.onboarding.services.model.RegistrationRequest;
import com.onboarding.services.model.RegistrationResponse;
import com.onboarding.services.model.ServiceConfig;
import com.onboarding.services.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service("onBoardServiceImpl")
public class OnBoardServiceImpl implements IOnBoardService {

    @Autowired
    private IServiceRegistration iserviceRegistration;

    Logger logger = LoggerFactory.getLogger(OnBoardServiceImpl.class);

    @Override
    public RegistrationResponse onboardServices(RegistrationRequest registrationRequest) throws OnBoardingServiceException {
        RegistrationResponse response = new RegistrationResponse();
        try {
            logger.info("Execution started In onboardServices() ");
            //Validations added for registration request
            mandatoryFieldValidations(registrationRequest, response);

            if(!Utility.isNullOrEmptyList(response.getValidationErrors())){
                if (Utility.isNullOrEmptyList(registrationRequest.getServices())) {
                    for (ServiceConfig service : registrationRequest.getServices()) {
                        response = iserviceRegistration.createService(service);
                    }
                }
                if (Utility.isNullOrEmptyList(registrationRequest.getGlobalPlugins())) {
                    for (PluginConfig config : registrationRequest.getGlobalPlugins()) {
                        iserviceRegistration.createGlobalPlugin(config);
                    }
                }
            }
            logger.info("Execution completed In onboardServices() ");
        }catch(ConstraintViolationException exception){
            throw new OnBoardingServiceException(exception.getMessage());
        }catch (InterruptedException exception) {
            logger.error("Exception thrown while executing onboardServices() : {} ", exception);
        } catch (Exception exception) {
            logger.error("Exception thrown while executing onboardServices() : {} ", exception);
        }
        return response;
    }

    private void mandatoryFieldValidations(RegistrationRequest registrationRequest, RegistrationResponse response) {
        List<String> validationErrors = new ArrayList<String>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(registrationRequest);
        if (!violations.isEmpty()) {
            violations.forEach(action -> {
                validationErrors.add(action.getMessage());
            });
            response.setValidationErrors(validationErrors);
            response.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        }
    }

}
