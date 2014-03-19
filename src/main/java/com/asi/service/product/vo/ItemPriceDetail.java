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

@XmlRootElement(name = "itemPrices")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ItemPrices", propOrder={"priceName","priceType","priceIncludes","priceUponRequest","productNumber","isMultiPriceGrid","priceDetails"})

@JsonPropertyOrder({"priceName","priceType","priceIncludes","priceUponRequest","productNumber","isMultiPriceGrid","priceDetails"})
public class ItemPriceDetail {
	@XmlEnum(String.class)
    public static enum PRICE_Type { REGL }
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
	@XmlElement(name="isMultiPriceGrid")
    private Boolean isMultiPriceGrid= Boolean.FALSE;
	@XmlElement(name="priceDetail")
    private List<PriceDetail> priceDetails = new ArrayList<PriceDetail>();
    
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
