package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class PluginConfig {

    @NotNull(message = "Plugin name must not be null or empty")
    @NotEmpty(message = "Plugin name must not be null or empty")
    @JsonProperty("name")
    private String name;

    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("config")
    private CommonConfig config;

    private JsonNode rateLimitterConfig;
}
