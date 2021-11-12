package com.onboarding.services.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onboarding.services.exception.OnBoardingServiceException;
import com.onboarding.services.model.RegistrationRequest;
import com.onboarding.services.model.RegistrationResponse;
import com.onboarding.services.service.IOnBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("onboard")
public class OnBoardServiceController {

    Logger logger = LoggerFactory.getLogger(OnBoardServiceController.class);

    @Autowired
    private IOnBoardService onBoardService;

    @PostMapping("/register")
    public RegistrationResponse registerServices(@RequestBody final RegistrationRequest registrationRequest) throws JsonProcessingException, OnBoardingServiceException {
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Received request : {} ", mapper.writeValueAsString(registrationRequest));
        return onBoardService.onboardServices(registrationRequest);
    }

}
