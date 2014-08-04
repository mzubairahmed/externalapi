package com.asi.ext.api.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class Values {
	  @JsonProperty("Type")
	    private String       type;
	    @JsonProperty("Value")
	    private Object value;
	    @JsonProperty("Type")
	    public String getType() {
	        return type;
	    }

	    @JsonProperty("Type")
	    public void setType(String type) {
	        this.type = type;
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
