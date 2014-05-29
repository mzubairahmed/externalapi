package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "constrain")
@XmlType(propOrder={"isAllowBasePrice","isAllowUpcharge","isFlag","isProductNumberAssignmentAllowed","isMediaAssignmentAllowed","isSupplierSpecific"})
@JsonPropertyOrder({"isAllowBasePrice","isAllowUpcharge","isFlag","isProductNumberAssignmentAllowed","isMediaAssignmentAllowed","isSupplierSpecific"})

public class Constrain {
	private boolean isAllowBasePrice;
	private boolean isAllowUpcharge;
	private boolean isFlag;
	private boolean isProductNumberAssignmentAllowed;
	private boolean isMediaAssignmentAllowed;
	private boolean isSupplierSpecific;
	
	public boolean isSupplierSpecific() {
		return isSupplierSpecific;
	}
	public void setSupplierSpecific(boolean isSupplierSpecific) {
		this.isSupplierSpecific = isSupplierSpecific;
	}
	public boolean isAllowBasePrice() {
		return isAllowBasePrice;
	}
	public void setAllowBasePrice(boolean isAllowBasePrice) {
		this.isAllowBasePrice = isAllowBasePrice;
	}
	public boolean isAllowUpcharge() {
		return isAllowUpcharge;
	}
	public void setAllowUpcharge(boolean isAllowUpcharge) {
		this.isAllowUpcharge = isAllowUpcharge;
	}
	public boolean isFlag() {
		return isFlag;
	}
	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}
	public boolean isProductNumberAssignmentAllowed() {
		return isProductNumberAssignmentAllowed;
	}
	public void setProductNumberAssignmentAllowed(
			boolean isProductNumberAssignmentAllowed) {
		this.isProductNumberAssignmentAllowed = isProductNumberAssignmentAllowed;
	}
	public boolean isMediaAssignmentAllowed() {
		return isMediaAssignmentAllowed;
	}
	public void setMediaAssignmentAllowed(boolean isMediaAssignmentAllowed) {
		this.isMediaAssignmentAllowed = isMediaAssignmentAllowed;
	}
	
}
