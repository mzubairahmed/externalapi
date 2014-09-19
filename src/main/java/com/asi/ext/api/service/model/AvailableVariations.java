package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class AvailableVariations {
	
	@JsonProperty("ParentValue")
	@XmlElement(name="ParentValue")
	private BaseValue parentValue;
	
	@JsonProperty("ChildValue")
	@XmlElement(name="ChildValue")
	private BaseValue childValue;
	
	public BaseValue getParentValue() {
		return parentValue;
	}
	
	public void setParentValue(BaseValue parentValue) {
		this.parentValue = parentValue;
	}
	
	public BaseValue getChildValue() {
		return childValue;
	}
	
	public void setChildValue(BaseValue childValue) {
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
