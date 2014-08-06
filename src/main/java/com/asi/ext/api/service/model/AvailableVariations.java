package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableVariations {
	@JsonProperty("ParentValue")
	private String parentValue;
	@JsonProperty("ChildValue")
	private String childValue;
	public String getParentValue() {
		return parentValue;
	}
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	public String getChildValue() {
		return childValue;
	}
	public void setChildValue(String childValue) {
		this.childValue = childValue;
	}
	
}
