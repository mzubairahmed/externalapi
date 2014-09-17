package com.asi.ext.api.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class PriceConfiguration {

    @JsonProperty("Criteria")
    private String criteria;
//    private Object value;
    
//    @JsonProperty("Value")
    private String stringValue;
    
//    @JsonProperty("Value")
    private Value value;
    
//    @JsonProperty("Value")
    private List<Value> values;
    
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

//    @JsonProperty("Value")
    public String getStringValue() {
        return stringValue;
    }

//    @JsonProperty("Value")
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
    
//    @JsonProperty("Value")
    public Value getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(Value value) {
        this.value = value;
    }

//    @JsonProperty("Value")
    public List<Value> getValues() {
        return values;
    }

//    @JsonProperty("Value")
    public void setValues(List<Value> values) {
        this.values = values;
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
