package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class ServiceConfig {

    @NotNull(message = "Service name must not be null or empty")
    @NotEmpty(message = "Service name must not be null or empty")
    @JsonProperty("name")
    private String name;

    private String serviceId;

    @NotNull(message = "Service url must not be null or empty")
    @NotEmpty(message = "Service url must not be null or empty")
    @JsonProperty("url")
    private String url;

    @JsonProperty("version")
    private String version;

    @Valid
    @JsonProperty("routes")
    private List<Route> routes;

    @JsonProperty("plugins")
    private List<PluginConfig> servicePlugins;
}
