package com.asi.ext.api.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public class AvailableVariations {
	
	@JsonProperty("ParentValue")
	@XmlElement(name="ParentValue")
	private List<Object> parentValue;
	
	@JsonProperty("ChildValue")
	@XmlElement(name="ChildValue")
	private List<Object> childValue;

	public List<Object> getParentValue() {
		return parentValue;
	}

	public void setParentValue(List<Object> parentValue) {
		this.parentValue = parentValue;
	}

	public List<Object> getChildValue() {
		return childValue;
	}

	public void setChildValue(List<Object> childValue) {
		this.childValue = childValue;
	}
	
	
}
