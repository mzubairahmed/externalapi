package com.asi.service.product.vo;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Configurations {
@JsonProperty("Color")
private String color="";
@JsonProperty("Material")
private String material="";
@JsonProperty("Origin")
private String origin="";
public String getColor() {
	return color;
}
public void setColor(String color) {
	this.color = color;
}
public String getMaterial() {
	return material;
}
public void setMaterial(String material) {
	this.material = material;
}
public String getOrigin() {
	return origin;
}
public void setOrigin(String origin) {
	this.origin = origin;
}

}

