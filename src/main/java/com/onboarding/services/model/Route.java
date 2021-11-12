package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    @NotNull(message = "Route name must not be null or empty")
    @NotEmpty(message = "Route name must not be null or empty")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Route methods must not be null or empty")
    @NotEmpty(message = "Route methods must not be null or empty")
    @JsonProperty("methods")
    private List<String> methods;

    @NotNull(message = "Route paths must not be null or empty")
    @NotEmpty(message = "Route paths must not be null or empty")
    @JsonProperty("paths")
    private List<String> paths;

    @JsonProperty("strip_path")
    private Boolean stripPath=false;
}
