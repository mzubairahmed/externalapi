package com.asi.service.lookup.vo;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="Color")
@XmlType(propOrder={
		"iD",
		"code",
		"description",
	    "displayName",
	    "colorHue"
	})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"ID",
	"Code",
	"Description",
    "DisplayName",
    "ColorHue"
})
public class Colors {
    @JsonProperty("ID")
    private String iD;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("Code")
    private String code;
    
    @JsonProperty("ColorHue")
    private ColorHue colorHue;
    
	public String getiD() {
		return iD;
	}
	public void setiD(String iD) {
		this.iD = iD;
	}
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
	public ColorHue getColorHue() {
		return colorHue;
	}
	public void setColorHue(ColorHue colorHue) {
		this.colorHue = colorHue;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
}
