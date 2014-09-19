package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)

@XmlSeeAlso({ValueWrapper.class})
public class Configurations {

    @JsonProperty("Criteria")
    private String criteria;
    @JsonProperty("OptionName")
    private String optionName;
    //@JsonProperty("Value")
   // private Object value;
    @JsonProperty("Value")
    @XmlElement(name="Value")
    private BaseValue value;
       
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
	public BaseValue getValue() {
		return value;
	}
    @JsonProperty("Value")
	public void setValue(BaseValue value) {
		this.value = value;
	}



}
