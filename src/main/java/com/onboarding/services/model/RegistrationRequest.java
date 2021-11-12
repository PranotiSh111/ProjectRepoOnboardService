package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Setter
@Getter
public class RegistrationRequest {

    @JsonProperty("globalPlugins")
    private List<PluginConfig> globalPlugins;

    @Valid
    @JsonProperty("services")
    private List<ServiceConfig> services;
}
