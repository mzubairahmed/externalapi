package com.asi.service.lookup.vo;

import java.util.List;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="AsiColor")
@XmlType(propOrder={
		"description",
	    "displayName",
	    "color"
	})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"Description",
    "DisplayName",
    "Color"
})
public class AsiColor {
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("Colors")
    private List<com.asi.service.lookup.vo.Colors> color;
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
	public List<com.asi.service.lookup.vo.Colors> getColor() {
		return color;
	}
	public void setColor(List<com.asi.service.lookup.vo.Colors> colors) {
		this.color = colors;
	}
    
}
