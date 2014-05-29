package com.asi.service.lookup.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.asi.service.product.vo.Constrain;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "sizes")
@XmlType(propOrder={"sizeCode","description","displayText","constrains","isSupplierSpecific"})
@JsonPropertyOrder({"sizeCode","description","displayText","constrains","isSupplierSpecific"})
public class Size {
	@XmlElement(nillable=true, name="Code")
	private String sizeCode;
	@XmlElement(nillable=true, name="description")
	private String description;
	@XmlElement(nillable=true, name="displayText")
	private String displayText;
	@XmlElement(nillable=true, name="constrains")
	private Constrain constrains;
	@XmlElement(nillable=true, name="isSupplierSpecific")
	private boolean isSupplierSpecific;

	@XmlElement(nillable=true,name="sizeList")
	private List<Size> sizeList = new ArrayList<Size>();
	
	public String getSizeCode() {
		return sizeCode;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public Constrain getConstrains() {
		return constrains;
	}
	public void setConstrains(Constrain constrains) {
		this.constrains = constrains;
	}
	public boolean isSupplierSpecific() {
		return isSupplierSpecific;
	}
	public void setSupplierSpecific(boolean isSupplierSpecific) {
		this.isSupplierSpecific = isSupplierSpecific;
	}
	
	
}
