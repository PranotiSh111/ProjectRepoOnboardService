package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonConfig {

    @JsonProperty("username")
    private String user;

    @JsonProperty("password")
    private String password;

    @JsonProperty("generator")
    private String generator;

    @JsonProperty("echo_downstream")
    private Boolean echoDownstream=false;

    @JsonProperty("key")
    private String key;

    @JsonProperty("run_on_preflight")
    private Boolean runOnPreflight=true;

    private String consumerId;
}
