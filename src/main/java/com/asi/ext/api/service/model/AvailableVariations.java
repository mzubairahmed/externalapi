package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class AvailableVariations {
	
	@JsonProperty("ParentValue")
	private Object parentValue;
	
	@JsonProperty("ChildValue")
	@XmlAnyElement(lax = true)
	private Object childValue;
	
	public Object getParentValue() {
		return parentValue;
	}
	
	public void setParentValue(Object parentValue) {
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
