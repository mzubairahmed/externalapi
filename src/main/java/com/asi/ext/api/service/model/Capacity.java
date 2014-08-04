package com.asi.ext.api.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Capacity {
	@JsonProperty("Values")
	private List<Value> values;

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}
}
