package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prices {
	@JsonProperty("PriceGridId")
	private String priceGridId = "0";
	@JsonProperty("SequenceNumber")
	private String sequenceNumber = "1";
	@JsonProperty("LowQuantity")
	private String lowQuantity = "0";
	@JsonProperty("HighQuantity")
	private String highQuantity = "0";
	@JsonProperty("Quantity")
	private String Quantity = "0";
	@JsonProperty("ListPrice")
	private String ListPrice = "0";
	@JsonProperty("NetCost")
	private String netCost = "0";
	@JsonProperty("IsNetCostEnabled")
	private String isNetCostEnabled = "false";
	@JsonProperty("ItemsPerUnit")
	private String itemsPerUnit = "1";
	@JsonProperty("IsQuantityEnabled")
	private String isQuantityEnabled = "false";
	@JsonProperty("IsListPriceEnabled")
	private String isListPriceEnabled = "false";
	@JsonProperty("IsEnabled")
	private String isEnabled = "false";
	@JsonProperty("PriceUnitName")
	private String priceUnitName = "";
	@JsonProperty("ListPricePerPiece")
	private String listPricePerPiece = "";
	@JsonProperty("PriceUnitDisplayName")
	private String priceUnitDisplayName = "";
	@JsonProperty("IsNullo")
	private String isNullo = "";
	@JsonProperty("PriceUnit")
	private PriceUnit priceUnit = new PriceUnit();
	@JsonProperty("DiscountRate")
	private DiscountRate discountRate = new DiscountRate();

	public Prices() {
		// Defult Constructor
	}

	/**
	 * @return the priceGridId
	 */
	public String getPriceGridId() {
		return priceGridId;
	}

	/**
	 * @param priceGridId
	 *            the priceGridId to set
	 */
	public void setPriceGridId(String priceGridId) {
		this.priceGridId = priceGridId;
	}

	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the lowQuantity
	 */
	public String getLowQuantity() {
		return lowQuantity;
	}

	/**
	 * @param lowQuantity
	 *            the lowQuantity to set
	 */
	public void setLowQuantity(String lowQuantity) {
		this.lowQuantity = lowQuantity;
	}

	/**
	 * @return the highQuantity
	 */
	public String getHighQuantity() {
		return highQuantity;
	}

	/**
	 * @param highQuantity
	 *            the highQuantity to set
	 */
	public void setHighQuantity(String highQuantity) {
		this.highQuantity = highQuantity;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return Quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	/**
	 * @return the listPrice
	 */
	public String getListPrice() {
		return ListPrice;
	}

	/**
	 * @param listPrice
	 *            the listPrice to set
	 */
	public void setListPrice(String listPrice) {
		ListPrice = listPrice;
	}

	/**
	 * @return the netCost
	 */
	public String getNetCost() {
		return netCost;
	}

	/**
	 * @param netCost
	 *            the netCost to set
	 */
	public void setNetCost(String netCost) {
		this.netCost = netCost;
	}

	/**
	 * @return the isNetCostEnabled
	 */
	public String getIsNetCostEnabled() {
		return isNetCostEnabled;
	}

	/**
	 * @param isNetCostEnabled
	 *            the isNetCostEnabled to set
	 */
	public void setIsNetCostEnabled(String isNetCostEnabled) {
		this.isNetCostEnabled = isNetCostEnabled;
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
	 * @return the isQuantityEnabled
	 */
	public String getIsQuantityEnabled() {
		return isQuantityEnabled;
	}

	/**
	 * @param isQuantityEnabled
	 *            the isQuantityEnabled to set
	 */
	public void setIsQuantityEnabled(String isQuantityEnabled) {
		this.isQuantityEnabled = isQuantityEnabled;
	}

	/**
	 * @return the isListPriceEnabled
	 */
	public String getIsListPriceEnabled() {
		return isListPriceEnabled;
	}

	/**
	 * @param isListPriceEnabled
	 *            the isListPriceEnabled to set
	 */
	public void setIsListPriceEnabled(String isListPriceEnabled) {
		this.isListPriceEnabled = isListPriceEnabled;
	}

	/**
	 * @return the isEnabled
	 */
	public String getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled
	 *            the isEnabled to set
	 */
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return the priceUnitName
	 */
	public String getPriceUnitName() {
		return priceUnitName;
	}

	/**
	 * @param priceUnitName
	 *            the priceUnitName to set
	 */
	public void setPriceUnitName(String priceUnitName) {
		this.priceUnitName = priceUnitName;
	}

	/**
	 * @return the listPricePerPiece
	 */
	public String getListPricePerPiece() {
		return listPricePerPiece;
	}

	/**
	 * @param listPricePerPiece
	 *            the listPricePerPiece to set
	 */
	public void setListPricePerPiece(String listPricePerPiece) {
		this.listPricePerPiece = listPricePerPiece;
	}

	/**
	 * @return the priceUnitDisplayName
	 */
	public String getPriceUnitDisplayName() {
		return priceUnitDisplayName;
	}

	/**
	 * @param priceUnitDisplayName
	 *            the priceUnitDisplayName to set
	 */
	public void setPriceUnitDisplayName(String priceUnitDisplayName) {
		this.priceUnitDisplayName = priceUnitDisplayName;
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
	 * @return the priceUnit
	 */
	public PriceUnit getPriceUnit() {
		return priceUnit;
	}

	/**
	 * @param priceUnit
	 *            the priceUnit to set
	 */
	public void setPriceUnit(PriceUnit priceUnit) {
		this.priceUnit = priceUnit;
	}

	/**
	 * @return the discountRate
	 */
	public DiscountRate getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate
	 *            the discountRate to set
	 */
	public void setDiscountRate(DiscountRate discountRate) {
		this.discountRate = discountRate;
	}

}
