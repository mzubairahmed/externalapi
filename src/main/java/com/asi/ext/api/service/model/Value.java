package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlAccessorType(XmlAccessType.NONE)
@JsonInclude(Include.NON_NULL)
public class Value extends BaseValue {

	@JsonProperty("Attribute")
    @XmlElement(name="attribute")
    private String attribute;

	@XmlElement(name="Value")
    @JsonProperty("Value")
    private String value;

	@JsonIgnore
    private String criteriaType;

	@JsonIgnore
    public String getCriteriaType() {
		return criteriaType;
	}

	@JsonIgnore
	public void setCriteriaType(String criteriaType) {
		this.criteriaType = criteriaType;
	}

	public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

   
    @XmlElement(name="Unit")
	@JsonProperty("Unit")
    private String unit;
}
