package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="Value")
public class StringValue extends BaseValue {
	@JsonProperty("Value")
	private String value=null;
	public StringValue() {
		// TODO Auto-generated constructor stub
	}
	
	public StringValue(String value) {
		this.value = value;
	}
	@JsonProperty("Value")
	public String getValue() {
		return value;
	}
	@JsonProperty("Value")
	public void setValue(String value) {
		this.value = value;
	}
	
}
