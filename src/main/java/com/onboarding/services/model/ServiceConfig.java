package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ServiceConfig {

    @JsonProperty("name")
    private String name;

    private String serviceId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("version")
    private String version;

    @JsonProperty("routes")
    private List<Route> routes;

    @JsonProperty("plugins")
    private List<PluginConfig> servicePlugins;
}
