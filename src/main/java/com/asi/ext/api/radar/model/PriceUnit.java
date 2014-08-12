package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceUnit {
	@JsonProperty("ID")
	private String id = "3162";
	@JsonProperty("Description")
	private String description = "";
	@JsonProperty("DisplayName")
	private String displayName = "";
	@JsonProperty("ItemsPerUnit")
	private String itemsPerUnit = "0";
	@JsonProperty("IsNullo")
	private String isNullo = "false";

	public PriceUnit() {
		// Default Constructor
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the itemsPerUnit
	 */
	public String getItemsPerUnit() {
		return itemsPerUnit;
	}

	/**
	 * @param itemsPerUnit
	 *            the itemsPerUnit to set
	 */
	public void setItemsPerUnit(String itemsPerUnit) {
		this.itemsPerUnit = itemsPerUnit;
	}

	/**
	 * @return the isNullo
	 */
	public String getIsNullo() {
		return isNullo;
	}

	/**
	 * @param isNullo
	 *            the isNullo to set
	 */
	public void setIsNullo(String isNullo) {
		this.isNullo = isNullo;
	}

}
