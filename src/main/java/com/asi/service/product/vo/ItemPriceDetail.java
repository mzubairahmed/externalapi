package com.asi.service.product.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product", name = "itemPrice")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ItemPrice", propOrder={"productID","priceID","priceName","priceType","priceIncludes","priceUponRequest","productNumber","firstPriceCriteria","secondPriceCriteria","isMultiPriceGrid","priceDetails"})

@JsonPropertyOrder({"productID","priceID","priceName","priceType","priceIncludes","priceUponRequest","productNumber","firstPriceCriteria","secondPriceCriteria","isMultiPriceGrid","priceDetails"})
public class ItemPriceDetail {
	@XmlEnum(String.class)
    public static enum PRICE_Type { REGL }
	@XmlElement(name="productID",required=true)
    private String productID= "";
	@XmlElement(name="priceID")
    private String priceID= "";
	@XmlElement(name="priceName")
    private String priceName= "";
	@XmlElement(name="priceType")
    private PRICE_Type priceType;
	@XmlElement(name="priceIncludes")
    private String priceIncludes= "";
	@XmlElement(name="priceUponRequest")
    private Boolean priceUponRequest =Boolean.FALSE;
	@XmlElement(name="productNumber")
    private String productNumber= "";
	@XmlElement(name="firstPriceCriteria")
    private String firstPriceCriteria= "";
	@XmlElement(name="secondPriceCriteria")
    private String secondPriceCriteria= "";	
	@XmlElement(name="isMultiPriceGrid")
    private Boolean isMultiPriceGrid= Boolean.FALSE;
	@XmlElement(name="priceDetail")
    private List<PriceDetail> priceDetails = new ArrayList<PriceDetail>();
	
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getPriceID() {
		return priceID;
	}
	public void setPriceID(String priceID) {
		this.priceID = priceID;
	}
	public String getFirstPriceCriteria() {
		return firstPriceCriteria;
	}
	public void setFirstPriceCriteria(String firstPriceCriteria) {
		this.firstPriceCriteria = firstPriceCriteria;
	}
	public String getSecondPriceCriteria() {
		return secondPriceCriteria;
	}
	public void setSecondPriceCriteria(String secondPriceCriteria) {
		this.secondPriceCriteria = secondPriceCriteria;
	}
	/**
	 * @return the priceType
	 */
	public PRICE_Type getPriceType() {
		return priceType;
	}
	/**
	 * @param priceType the priceType to set
	 */
	public void setPriceType(PRICE_Type priceType) {
		this.priceType = priceType;
	}
	/**
	 * @return the priceName
	 */
	public String getPriceName() {
		return priceName;
	}
	/**
	 * @param priceName the priceName to set
	 */
	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}
	/**
	 * @return the priceIncludes
	 */
	public String getPriceIncludes() {
		return priceIncludes;
	}
	/**
	 * @param priceIncludes the priceIncludes to set
	 */
	public void setPriceIncludes(String priceIncludes) {
		this.priceIncludes = priceIncludes;
	}


	/**
	 * @return the priceUponRequest
	 */
	public Boolean getPriceUponRequest() {
		return priceUponRequest;
	}
	/**
	 * @param priceUponRequest the priceUponRequest to set
	 */
	public void setPriceUponRequest(Boolean priceUponRequest) {
		this.priceUponRequest = priceUponRequest;
	}
	/**
	 * @return the productNumber
	 */
	public String getProductNumber() {
		return productNumber;
	}
	/**
	 * @param productNumber the productNumber to set
	 */
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	/**
	 * @return the priceDetails
	 */
	public List<PriceDetail> getPriceDetails() {
		return priceDetails;
	}
	/**
	 * @param priceDetails the priceDetails to set
	 */
	public void setPriceDetails(List<PriceDetail> priceDetails) {
		this.priceDetails = priceDetails;
	}

	/**
	 * @return the isMultiPriceGrid
	 */
	public Boolean getIsMultiPriceGrid() {
		return isMultiPriceGrid;
	}
	/**
	 * @param isMultiPriceGrid the isMultiPriceGrid to set
	 */
	public void setIsMultiPriceGrid(Boolean isMultiPriceGrid) {
		this.isMultiPriceGrid = isMultiPriceGrid;
	}
	
}
