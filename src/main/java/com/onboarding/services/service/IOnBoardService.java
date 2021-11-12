package com.onboarding.services.service;

import com.onboarding.services.exception.OnBoardingServiceException;
import com.onboarding.services.model.RegistrationRequest;
import com.onboarding.services.model.RegistrationResponse;

public interface IOnBoardService {

    public RegistrationResponse onboardServices(RegistrationRequest registrationRequest) throws OnBoardingServiceException;
}
