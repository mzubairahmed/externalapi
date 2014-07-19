package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {
	@JsonProperty("Code")
	private String code = "NON";
	@JsonProperty("Name")
	private String name = "";
	@JsonProperty("Number")
	private String number;
	@JsonProperty("ASIDisplaySymbol")
	private String aSIDisplaySymbol;
	@JsonProperty("ISODisplaySymbol")
	private String iSODisplaySymbol;
	@JsonProperty("IsISO")
	private String isISO;
	@JsonProperty("IsActive")
	private String isActive = "true";
	@JsonProperty("DisplaySequence")
	private String displaySequence;
	@JsonProperty("IsNullo")
	private String isNullo = "false";
	@JsonProperty("ASISymbol")
	private Object asiSymbol;

	public Currency() {
		// Default Constructor
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the aSIDisplaySymbol
	 */
	public String getaSIDisplaySymbol() {
		return aSIDisplaySymbol;
	}

	/**
	 * @param aSIDisplaySymbol
	 *            the aSIDisplaySymbol to set
	 */
	public void setaSIDisplaySymbol(String aSIDisplaySymbol) {
		this.aSIDisplaySymbol = aSIDisplaySymbol;
	}

	/**
	 * @return the iSODisplaySymbol
	 */
	public String getiSODisplaySymbol() {
		return iSODisplaySymbol;
	}

	/**
	 * @param iSODisplaySymbol
	 *            the iSODisplaySymbol to set
	 */
	public void setiSODisplaySymbol(String iSODisplaySymbol) {
		this.iSODisplaySymbol = iSODisplaySymbol;
	}

	/**
	 * @return the isISO
	 */
	public String getIsISO() {
		return isISO;
	}

	/**
	 * @param isISO
	 *            the isISO to set
	 */
	public void setIsISO(String isISO) {
		this.isISO = isISO;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the displaySequence
	 */
	public String getDisplaySequence() {
		return displaySequence;
	}

	/**
	 * @param displaySequence
	 *            the displaySequence to set
	 */
	public void setDisplaySequence(String displaySequence) {
		this.displaySequence = displaySequence;
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

	/**
	 * @return the asiSymbol
	 */
	public Object getAsiSymbol() {
		return asiSymbol;
	}

	/**
	 * @param asiSymbol
	 *            the asiSymbol to set
	 */
	public void setAsiSymbol(Object asiSymbol) {
		this.asiSymbol = asiSymbol;
	}

}
