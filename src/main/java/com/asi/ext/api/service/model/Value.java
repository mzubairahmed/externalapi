package com.asi.ext.api.service.model;

public class Value {
 private String attribute;
 private String value;
 public String getAttribute() {
	return attribute;
}
public void setAttribute(String attribute) {
	this.attribute = attribute;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getUnit() {
	return unit;
}
public void setUnit(String unit) {
	this.unit = unit;
}
private String unit;
}
