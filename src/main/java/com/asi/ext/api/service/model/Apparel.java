package com.asi.ext.api.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Apparel {
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Values")
	private List<Values> values;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Values> getValues() {
		return values;
	}

	public void setValues(List<Values> values) {
		this.values = values;
	}

}
