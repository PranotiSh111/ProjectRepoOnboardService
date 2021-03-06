package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    @JsonProperty("name")
    private String name;

    @JsonProperty("methods")
    private List<String> methods;

    @JsonProperty("paths")
    private List<String> paths;

    @JsonProperty("strip_path")
    private Boolean stripPath=false;
}
