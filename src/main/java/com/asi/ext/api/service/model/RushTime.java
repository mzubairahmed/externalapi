package com.asi.ext.api.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RushTime {
	@JsonProperty("Available")
	private boolean available;

	@JsonProperty("Values")
	private List<RushTimeValue> rushTimeValues=null;

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<RushTimeValue> getRushTimeValues() {
		return rushTimeValues;
	}

	public void setRushTimeValues(List<RushTimeValue> rushTimeValues) {
		this.rushTimeValues = rushTimeValues;
	}
	
    
}
