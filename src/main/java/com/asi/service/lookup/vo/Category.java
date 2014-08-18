package com.asi.service.lookup.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "categoriesList")
@XmlType(propOrder={"Name"})
@JsonPropertyOrder({"Name"})
public class Category {
	@XmlElement(nillable=true, name="Name")
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
