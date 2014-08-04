package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtherSize {
	@JsonProperty("Values")
	private Values values;

	public Values getValues() {
		return values;
	}

	public void setValues(Values values) {
		this.values = values;
	}
}
