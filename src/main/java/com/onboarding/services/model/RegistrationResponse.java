package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Setter
@Getter
public class RegistrationResponse {

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("status")
    private String StatusCode;

    @JsonProperty("validation_errors")
    private List<String> validationErrors;

    @JsonProperty("messageConfig")
    private List<MessageConfig> messageConfig;

}
