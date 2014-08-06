package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SameDayRush {

	@JsonProperty("Available")
	private String available;
	@JsonProperty("Details")
	private String details;
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
