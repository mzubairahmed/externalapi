package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "imprintMethod")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"methodName","minimumOrder","artworkName"})

@JsonPropertyOrder({"methodName","minimumOrder","artworkName"})
public class ImprintMethod {
	@XmlElement(name="methodName")
    private String methodName= "";
	@XmlElement(name="minimumOrder")
    private String minimumOrder="";
	
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
	
	public String getArtworkName() {
		return artworkName;
	}
	public void setArtworkName(String artworkName) {
		this.artworkName = artworkName;
	}

}
