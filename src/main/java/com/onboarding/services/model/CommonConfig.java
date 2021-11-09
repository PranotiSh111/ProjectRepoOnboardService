package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonConfig {

    @JsonProperty("user")
    private String user;

    @JsonProperty("password")
    private String password;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("downstream_yes")
    private Boolean downstreamYes;
}
