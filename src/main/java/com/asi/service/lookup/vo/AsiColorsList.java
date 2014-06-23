package com.asi.service.lookup.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="colorGroups")
@XmlType(propOrder={
		"colorGroup"
	})
public class AsiColorsList {
	   @JsonProperty("ColorGroup")
	    private List<AsiColor> colorGroup;

	public List<AsiColor> getColorGroup() {
		return colorGroup;
	}

	public void setColorGroup(List<AsiColor> colorGroup) {
		this.colorGroup = colorGroup;
	}



}
