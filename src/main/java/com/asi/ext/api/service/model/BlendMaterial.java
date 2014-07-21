package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlendMaterial {
    
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Percentage")
    private String percentage;

}
