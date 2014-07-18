package com.asi.ext.api.radar.model;

public class SetCodeValues {
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getDisplaySequence() {
		return displaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		this.displaySequence = displaySequence;
	}
	public String getIsSupplierSpecific() {
		return isSupplierSpecific;
	}
	public void setIsSupplierSpecific(String isSupplierSpecific) {
		this.isSupplierSpecific = isSupplierSpecific;
	}
	private String ID;
	private String codeValue;
	private String displaySequence;
	private String isSupplierSpecific;

}
