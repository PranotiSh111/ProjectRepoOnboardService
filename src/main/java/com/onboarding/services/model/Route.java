package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Route {

    @JsonProperty("name")
    private String name;

    @JsonProperty("methods")
    private List<String> methods;

    @JsonProperty("paths")
    private List<String> paths;

    @JsonProperty("plugins")
    private List<PluginConfig> routePlugins;
}
