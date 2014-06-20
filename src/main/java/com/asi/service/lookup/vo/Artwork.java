package com.asi.service.lookup.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name = "artworksList")
@XmlType(propOrder={"id","codeValue"})
@JsonPropertyOrder({"id","codeValue"})

public class Artwork {
	@XmlElement(nillable=true, name="id")
	private String id;
	@XmlElement(nillable=true, name="codeValue")
	private String codeValue;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
}
