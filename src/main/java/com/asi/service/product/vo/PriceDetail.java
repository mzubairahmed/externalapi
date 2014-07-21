package com.asi.service.product.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;



@XmlRootElement(namespace = "http://www.asicentral.com/schema/product", name="pricingDetail")
@XmlType(propOrder={"sequenceNumber","price","quanty","discount","priceUnitName","lowQuantity","highQuantity","netCost","maxDecimalPlaces","itemsPerUnit","itemsPerUnitBy"})
@JsonPropertyOrder({"sequenceNumber","price","quanty","discount","priceUnitName","lowQuantity","highQuantity","netCost","maxDecimalPlaces","itemsPerUnit","itemsPerUnitBy"})
public class PriceDetail {
 
	private Integer sequenceNumber;
	private Double price;
    private Integer quanty;
    private String discount = "";
    private String priceUnitName="";
    public String getPriceUnitName() {
		return priceUnitName;
	}
	public void setPriceUnitName(String priceUnitName) {
		this.priceUnitName = priceUnitName;
	}
	private Integer lowQuantity;
    private Integer highQuantity;
    private Double netCost;
    private int maxDecimalPlaces;
    private int itemsPerUnit;
    private String itemsPerUnitBy;

    
    /**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	/**
	 * @return the quanty
	 */
	public Integer getQuanty() {
		return quanty;
	}
	/**
	 * @param quanty the quanty to set
	 */
	public void setQuanty(Integer quanty) {
		this.quanty = quanty;
	}
	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	/**
	 * @return the lowQuantity
	 */
	public Integer getLowQuantity() {
		return lowQuantity;
	}
	/**
	 * @param lowQuantity the lowQuantity to set
	 */
	public void setLowQuantity(Integer lowQuantity) {
		this.lowQuantity = lowQuantity;
	}
	/**
	 * @return the highQuantity
	 */
	public Integer getHighQuantity() {
		return highQuantity;
	}
	/**
	 * @param highQuantity the highQuantity to set
	 */
	public void setHighQuantity(Integer highQuantity) {
		this.highQuantity = highQuantity;
	}
	/**
	 * @return the netCost
	 */
	public Double getNetCost() {
		return netCost;
	}
	/**
	 * @param netCost the netCost to set
	 */
	public void setNetCost(Double netCost) {
		this.netCost = netCost;
	}
	/**
	 * @return the maxDecimalPlaces
	 */
	public int getMaxDecimalPlaces() {
		return maxDecimalPlaces;
	}
	/**
	 * @param maxDecimalPlaces the maxDecimalPlaces to set
	 */
	public void setMaxDecimalPlaces(int maxDecimalPlaces) {
		this.maxDecimalPlaces = maxDecimalPlaces;
	}
	/**
	 * @return the itemsPerUnit
	 */
	public int getItemsPerUnit() {
		return itemsPerUnit;
	}
	/**
	 * @param itemsPerUnit the itemsPerUnit to set
	 */
	public void setItemsPerUnit(int itemsPerUnit) {
		this.itemsPerUnit = itemsPerUnit;
	}
	/**
	 * @return the itemsPerUnitBy
	 */
	public String getItemsPerUnitBy() {
		return itemsPerUnitBy;
	}
	/**
	 * @param itemsPerUnitBy the itemsPerUnitBy to set
	 */
	public void setItemsPerUnitBy(String itemsPerUnitBy) {
		this.itemsPerUnitBy = itemsPerUnitBy;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
    
}
