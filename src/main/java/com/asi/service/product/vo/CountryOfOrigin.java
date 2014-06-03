package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "origin")
@XmlType(propOrder={"id","countryOfOrigin"})
@JsonPropertyOrder({"id","countryOfOrigin"})
public class CountryOfOrigin {

	private String id;
	private String countryOfOrigin;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}
	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	
}
