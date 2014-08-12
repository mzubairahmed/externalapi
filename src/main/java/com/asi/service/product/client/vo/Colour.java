package com.asi.service.product.client.vo;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Colour {
	public String getCode() {
		return Code;
	}
	public void setCode(String Code) {
		this.Code = Code;
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
	public String getDisplaySequence() {
		return displaySequence;
	}
	public void setDisplaySequence(String displaySequence) {
		this.displaySequence = displaySequence;
	}
	public String getMajorCodeValueGroupCode() {
		return majorCodeValueGroupCode;
	}
	public void setMajorCodeValueGroupCode(String majorCodeValueGroupCode) {
		this.majorCodeValueGroupCode = majorCodeValueGroupCode;
	}
	public ColorHue getColorHue() {
		return colorHue;
	}
	public void setColorHue(ColorHue colorHue) {
		this.colorHue = colorHue;
	}
	public SetCodeValues getSetCodeValues() {
		return setCodeValues;
	}
	public void setSetCodeValues(SetCodeValues setCodeValues) {
		this.setCodeValues = setCodeValues;
	}
	@JsonIgnore
	private String Code;
	private String description=null;
	private String displayName=null;
	private String displaySequence;
	private String majorCodeValueGroupCode;
	private ColorHue colorHue; 
	private SetCodeValues setCodeValues; 
}
