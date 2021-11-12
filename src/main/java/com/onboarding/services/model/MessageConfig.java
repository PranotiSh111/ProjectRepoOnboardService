package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageConfig {

    @JsonProperty("response")
    private String response;

    @JsonProperty("message")
    private String message;

}
