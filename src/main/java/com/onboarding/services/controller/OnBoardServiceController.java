package com.onboarding.services.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboarding.services.exception.OnBoardingServiceException;
import com.onboarding.services.model.RegistrationRequest;
import com.onboarding.services.service.IOnBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("onboard")
public class OnBoardServiceController {

    Logger logger = LoggerFactory.getLogger(OnBoardServiceController.class);

    @Autowired
    private IOnBoardService onBoardService;

    @PostMapping("/register")
    public void registerServices(@RequestBody final RegistrationRequest registrationRequest) throws JsonProcessingException, OnBoardingServiceException {
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Received request : {} ", mapper.writeValueAsString(registrationRequest));
        onBoardService.onboardServices(registrationRequest);
        logger.info("Request Completed Successfully");
    }
}
