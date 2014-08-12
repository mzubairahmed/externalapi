package com.asi.service.lookup.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="ColorHue")
@XmlType(propOrder={
		"code",
		"description",
	    "displayName"
	})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"Code",
	"Description",
    "DisplayName"
})
public class ColorHue {

    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("Code")
    private String code;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
}
