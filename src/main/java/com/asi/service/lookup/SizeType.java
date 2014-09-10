package com.asi.service.lookup;

import java.util.List;

public class SizeType {
	private String attribute = null;
	private List<String> units = null;
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attributes) {
		this.attribute = attributes;
	}
	public List<String> getUnits() {
		return units;
	}
	public void setUnits(List<String> units) {
		this.units = units;
	}
	
}
