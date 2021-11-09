package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class RegistrationRequest {

    @JsonProperty("globalPlugins")
    private List<PluginConfig> globalPlugins;

    @Valid
    @NotNull
    @JsonProperty("services")
    private List<ServiceConfig> services;
}
