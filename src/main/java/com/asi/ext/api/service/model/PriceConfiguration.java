package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class PriceConfiguration {

    @JsonProperty("Criteria")
    private String criteria;
    @JsonProperty("Value")
    private Object value;

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
