package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class Value {
    @JsonProperty("Attribute")
    private String attribute;
    @JsonProperty("Value")
    private String value;
    @JsonIgnore
    private String criteriaType;
    
    public String getCriteriaType() {
		return criteriaType;
	}

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

    @Override
	public String toString() {
		return "Value [attribute=" + attribute + ", value=" + value + ", unit="
				+ unit + ", getAttribute()=" + getAttribute() + ", getValue()="
				+ getValue() + ", getUnit()=" + getUnit() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	@JsonProperty("Unit")
    private String unit;
}
