package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product", name="priceCriteria")
@XmlType(propOrder={"criteriaCode","value"})
@JsonPropertyOrder({"criteriaCode","value"})
public class PriceCriteria {
	private String criteriaCode;
	
	public String getCriteriaCode() {
		return criteriaCode;
	}

	public void setCriteriaCode(String criteriaCode) {
		this.criteriaCode = criteriaCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String value;
}
