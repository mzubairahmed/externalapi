package com.asi.velocity.bean;

public class Prices {
public String getPriceGridId() {
		return priceGridId;
	}
	public void setPriceGridId(String priceGridId) {
		this.priceGridId = priceGridId;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getLowQuantity() {
		return lowQuantity;
	}
	public void setLowQuantity(String lowQuantity) {
		this.lowQuantity = lowQuantity;
	}
	public String getHighQuantity() {
		return highQuantity;
	}
	public void setHighQuantity(String highQuantity) {
		this.highQuantity = highQuantity;
	}
	public String getListPrice() {
		return ListPrice;
	}
	public void setListPrice(String ListPrice) {
		this.ListPrice = ListPrice;
	}
	public String getItemsPerUnit() {
		return itemsPerUnit;
	}
	public void setItemsPerUnit(String itemsPerUnit) {
		this.itemsPerUnit = itemsPerUnit;
	}
	public String getIsNetCostEnabled() {
		return isNetCostEnabled;
	}
	public void setIsNetCostEnabled(String isNetCostEnabled) {
		this.isNetCostEnabled = isNetCostEnabled;
	}
	public String getIsQuantityEnabled() {
		return isQuantityEnabled;
	}
	public void setIsQuantityEnabled(String isQuantityEnabled) {
		this.isQuantityEnabled = isQuantityEnabled;
	}
	public String getIsListPriceEnabled() {
		return isListPriceEnabled;
	}
	public void setIsListPriceEnabled(String isListPriceEnabled) {
		this.isListPriceEnabled = isListPriceEnabled;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getPriceUnitName() {
		return priceUnitName;
	}
	public void setPriceUnitName(String priceUnitName) {
		this.priceUnitName = priceUnitName;
	}
	public String getNetCost() {
		return netCost;
	}
	public void setNetCost(String netCost) {
		this.netCost = netCost;
	}
	public String getListPricePerPiece() {
		return listPricePerPiece;
	}
	public void setListPricePerPiece(String listPricePerPiece) {
		this.listPricePerPiece = listPricePerPiece;
	}
	public String getPriceUnitDisplayName() {
		return priceUnitDisplayName;
	}
	public void setPriceUnitDisplayName(String priceUnitDisplayName) {
		this.priceUnitDisplayName = priceUnitDisplayName;
	}
	public String getIsNullo() {
		return isNullo;
	}
	public void setIsNullo(String isNullo) {
		this.isNullo = isNullo;
	}
	public PriceUnit getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(PriceUnit priceUnit) {
		this.priceUnit = priceUnit;
	}
	public DiscountRate getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(DiscountRate discountRate) {
		this.discountRate = discountRate;
	}
private String priceGridId="0",sequenceNumber="1",lowQuantity="0";
private String highQuantity="0",ListPrice="0",itemsPerUnit="1",isNetCostEnabled="false";
private String isQuantityEnabled="false",isListPriceEnabled="false",isEnabled="false";
private String priceUnitName="",netCost="0",listPricePerPiece="";
private String priceUnitDisplayName="",isNullo="";
private PriceUnit priceUnit=new PriceUnit();
private DiscountRate discountRate=new DiscountRate();
private String Quantity="0";
public String getQuantity() {
	return Quantity;
}
public void setQuantity(String quantity) {
	Quantity = quantity;
}

}
