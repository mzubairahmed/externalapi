package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableVariations {
	@JsonProperty("ParentValue")
	private String parentValue;
	@JsonProperty("ChildValue")
	private Object childValue;
	
	public String getParentValue() {
		return parentValue;
	}
	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}
	public Object getChildValue() {
		return childValue;
	}
	public void setChildValue(Object childValue) {
		this.childValue = childValue;
	}
	@Override
	public String toString() {
		return "AvailableVariations [parentValue=" + parentValue
				+ ", childValue=" + childValue + ", getParentValue()="
				+ getParentValue() + ", getChildValue()=" + getChildValue()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
