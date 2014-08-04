package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlendMaterial {
    
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Percentage")
    private String percentage;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

}
