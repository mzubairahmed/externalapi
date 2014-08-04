package com.asi.ext.api.service.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(Include.NON_NULL)
public class Values {
	  @JsonProperty("Type")
	    private String       type;
	    @JsonProperty("Value")
	    private List<Value> value;
	    @JsonProperty("Type")
	    public String getType() {
	        return type;
	    }

	    @JsonProperty("Type")
	    public void setType(String type) {
	        this.type = type;
	    }

	    @JsonProperty("Value")
	    public List<Value> getValue() {
	        return value;
	    }

	    @JsonProperty("Value")
	    public void setValue(List<Value> value) {
	        this.value = value;
	    }

}
