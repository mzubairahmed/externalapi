package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RushTimeValue {
	@JsonProperty("BusinessDays")
    private Integer businessDays;
    @JsonProperty("Details")
    private String  details;
	public Integer getBusinessDays() {
		return businessDays;
	}
	public void setBusinessDays(Integer businessDays) {
		this.businessDays = businessDays;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}

}
