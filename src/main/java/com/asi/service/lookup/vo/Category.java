package com.asi.service.lookup.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Category {
	@JsonProperty("Name")
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
