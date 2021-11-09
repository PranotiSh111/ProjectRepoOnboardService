package com.onboarding.services.service;

import com.onboarding.services.exception.OnBoardingServiceException;
import com.onboarding.services.model.RegistrationRequest;

public interface IOnBoardService {

    public void onboardServices(RegistrationRequest registrationRequest) throws OnBoardingServiceException;
}
