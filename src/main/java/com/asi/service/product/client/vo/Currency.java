package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Currency {
@JsonProperty("Code")
private String code="NON";
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Object getAsiSymbol() {
	return asiSymbol;
}
public void setAsiSymbol(Object asiSymbol) {
	this.asiSymbol = asiSymbol;
}
public String getIsActive() {
	return isActive;
}
public void setIsActive(String isActive) {
	this.isActive = isActive;
}
public String getIsNullo() {
	return isNullo;
}
public void setIsNullo(String isNullo) {
	this.isNullo = isNullo;
}
@JsonProperty("Name")
private String name="";
@JsonProperty("AsiSymbol")
private Object asiSymbol;
@JsonProperty("IsActive")
private String isActive="true";
@JsonProperty("IsNullo")
private String isNullo="false";
@JsonProperty("Number")
private String number;
public String getNumber() {
	return number;
}
public void setNumber(String number) {
	this.number = number;
}
public String getIsISO() {
	return isISO;
}
public void setIsISO(String isISO) {
	this.isISO = isISO;
}
public String getDisplaySequence() {
	return displaySequence;
}
public void setDisplaySequence(String displaySequence) {
	this.displaySequence = displaySequence;
}
public String getaSIDisplaySymbol() {
	return aSIDisplaySymbol;
}
public void setaSIDisplaySymbol(String aSIDisplaySymbol) {
	this.aSIDisplaySymbol = aSIDisplaySymbol;
}
public String getiSODisplaySymbol() {
	return iSODisplaySymbol;
}
public void setiSODisplaySymbol(String iSODisplaySymbol) {
	this.iSODisplaySymbol = iSODisplaySymbol;
}
@JsonProperty("IsISO")
private String isISO;
@JsonProperty("DisplaySequence")
private String displaySequence;
@JsonProperty("ASIDisplaySymbol")
private String aSIDisplaySymbol;
@JsonProperty("ISODisplaySymbol")
private String iSODisplaySymbol;
}
