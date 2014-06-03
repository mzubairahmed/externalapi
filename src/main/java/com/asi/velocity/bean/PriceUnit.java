package com.asi.velocity.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class PriceUnit {
	@JsonProperty("ID")
private String id="3162";
private String description="",displayName="",itemsPerUnit="0",isNullo="false";

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
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

public String getItemsPerUnit() {
	return itemsPerUnit;
}

public void setItemsPerUnit(String itemsPerUnit) {
	this.itemsPerUnit = itemsPerUnit;
}

public String getIsNullo() {
	return isNullo;
}

public void setIsNullo(String isNullo) {
	this.isNullo = isNullo;
}
}
