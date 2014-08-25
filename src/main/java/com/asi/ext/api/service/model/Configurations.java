package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class Configurations {

    @JsonProperty("Criteria")
    private String criteria;
    @JsonProperty("OptionName")
    private String optionName;
    @JsonProperty("Value")
    private Object value;

    public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	@JsonProperty("Criteria")
    public String getCriteria() {
        return criteria;
    }

    @JsonProperty("Criteria")
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @JsonProperty("Value")
    public Object getValue() {
        return value;
    }
    @JsonProperty("Value")
    public void setValue(Object value) {
        this.value = value;
    }


}
