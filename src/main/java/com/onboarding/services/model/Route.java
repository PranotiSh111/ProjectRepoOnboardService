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

    @NotNull(message = "endpoint name must not be null or empty")
    @NotEmpty(message = "endpoint name must not be null or empty")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "methods must not be null or empty")
    @NotEmpty(message = "methods must not be null or empty")
    @JsonProperty("methods")
    private List<String> methods;

    @NotNull(message = "endpoint paths must not be null or empty")
    @NotEmpty(message = "endpoint path must not be null or empty")
    @JsonProperty("paths")
    private List<String> paths;

    @JsonProperty("strip_path")
    private Boolean stripPath=false;
}
