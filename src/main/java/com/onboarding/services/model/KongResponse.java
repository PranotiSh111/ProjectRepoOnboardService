package com.onboarding.services.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KongResponse {

    private String id;

    private String name;

    private String userName;

    private Boolean enabled;

}
