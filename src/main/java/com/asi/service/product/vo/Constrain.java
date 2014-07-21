package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "constrain")
@XmlType(propOrder={"allowBasePrice","allowUpcharge","flag","productNumberAssignmentAllowed","mediaAssignmentAllowed","supplierSpecific"})
@JsonPropertyOrder({"allowBasePrice","allowUpcharge","flag","productNumberAssignmentAllowed","mediaAssignmentAllowed","supplierSpecific"})

public class Constrain {
	private boolean isAllowBasePrice;
	private boolean isAllowUpcharge;
	private boolean isFlag;
	private boolean isProductNumberAssignmentAllowed;
	private boolean isMediaAssignmentAllowed;
	private boolean isSupplierSpecific;
	@XmlElement(name="AllowBasePrice")
	public boolean isAllowBasePrice() {
		return isAllowBasePrice;
	}
	@XmlElement(name="AllowUpcharge")
	public boolean isAllowUpcharge() {
		return isAllowUpcharge;
	}
	@XmlElement(name="Flag")
	public boolean isFlag() {
		return isFlag;
	}
	@XmlElement(name="ProductNumberAssignmentAllowed")
	public boolean isProductNumberAssignmentAllowed() {
		return isProductNumberAssignmentAllowed;
	}
	@XmlElement(name="MediaAssignmentAllowed")
	public boolean isMediaAssignmentAllowed() {
		return isMediaAssignmentAllowed;
	}
	@XmlElement(name="SupplierSpecific")
	public boolean isSupplierSpecific() {
		return isSupplierSpecific;
	}
	public void setAllowBasePrice(boolean isAllowBasePrice) {
		this.isAllowBasePrice = isAllowBasePrice;
	}
	public void setAllowUpcharge(boolean isAllowUpcharge) {
		this.isAllowUpcharge = isAllowUpcharge;
	}
	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}
	public void setProductNumberAssignmentAllowed(
			boolean isProductNumberAssignmentAllowed) {
		this.isProductNumberAssignmentAllowed = isProductNumberAssignmentAllowed;
	}
	public void setMediaAssignmentAllowed(boolean isMediaAssignmentAllowed) {
		this.isMediaAssignmentAllowed = isMediaAssignmentAllowed;
	}
	public void setSupplierSpecific(boolean isSupplierSpecific) {
		this.isSupplierSpecific = isSupplierSpecific;
	}
	

	
}
