package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
//@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = As.PROPERTY, property = "@class")
public class PriceConfiguration {

    @JsonProperty("Criteria")
    private String criteria;
    @JsonProperty("Value")
//    @XmlElement(name="Value")
    private BaseValue value;
    
    @JsonProperty("OptionName")
    private String optionName;

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

    /**
     * @return the optionName
     */
    public String getOptionName() {
        return optionName;
    }

    /**
     * @param optionName
     *            the optionName to set
     */
    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

}
