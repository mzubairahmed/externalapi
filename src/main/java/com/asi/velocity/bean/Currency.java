package com.asi.velocity.bean;


public class Currency {
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
private String name="";
private Object asiSymbol;
private String isActive="true";
private String isNullo="false";

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
private String isISO;
private String displaySequence;
private String aSIDisplaySymbol;
private String iSODisplaySymbol;
}
