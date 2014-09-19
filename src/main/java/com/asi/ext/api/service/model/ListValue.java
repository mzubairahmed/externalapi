package com.asi.ext.api.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


import com.fasterxml.jackson.annotation.JsonProperty;
@XmlRootElement(name="Value")
public class ListValue extends BaseValue{
	public ListValue() {
		// TODO Auto-generated constructor stub
	}
	
public ListValue(List<Value> value) {
		this.value = value;
	}

@JsonProperty("Value")
private List<Value> value;
@JsonProperty("Value")
public List<Value> getValue() {
	return value;
}
@JsonProperty("Value")
public void setValue(List<Value> value) {
	this.value = value;
}

}
