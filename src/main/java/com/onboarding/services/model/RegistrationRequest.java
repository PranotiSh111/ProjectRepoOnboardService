package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RegistrationRequest {

    @JsonProperty("globalPlugins")
    private List<PluginConfig> globalPlugins;

    @JsonProperty("services")
    private List<ServiceConfig> services;
}
