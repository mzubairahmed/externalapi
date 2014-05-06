package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(name = "imprintMethods")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="imprintMethods", propOrder={"methodName","minimumOrder","unit","artworkName"})

@JsonPropertyOrder({"methodName","minimumOrder","unit","artworkName"})
public class ImprintMethod {
	@XmlElement(name="methodName")
    private String methodName= "";
	@XmlElement(name="minimumOrder")
    private String minimumOrder;
	@XmlElement(name="unit")
    private String unit= "";
	@XmlElement(name="artworkName")
    private String artworkName ="";
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMinimumOrder() {
		return minimumOrder;
	}
	public void setMinimumOrder(String minimumOrder) {
		this.minimumOrder = minimumOrder;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getArtworkName() {
		return artworkName;
	}
	public void setArtworkName(String artworkName) {
		this.artworkName = artworkName;
	}

}
