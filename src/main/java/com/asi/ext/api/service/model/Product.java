package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonProperty("ExternalProductId")
    private String                externalProductId;
    @JsonProperty("Name")
    private String                name;
    @JsonProperty("Description")
    private String                description;
    @JsonProperty("Summary")
    private String                summary;
    @JsonProperty("AsiProdNo")
    private String                asiProdNo;
    @JsonProperty("ProductType")
    private String                productType;
    @JsonProperty("StatusCode")
    private String                statusCode;
    @JsonProperty("ProductInventoryLink")
    private String                productInventoryLink;
    @JsonProperty("ProductDataSheet")
    private String                productDataSheet;
    @JsonProperty("SameDayRushOffered")
    private Boolean               sameDayRushOffered;
    @JsonProperty("ShipperBillsBy")
    private String                shipperBillsBy;
    @JsonProperty("ProductBreakoutBy")
    private String                productBreakoutBy;
    @JsonProperty("ProductKeywords")
    private List<String>          productKeywords = new ArrayList<String>();
    @JsonProperty("Categories")
    private List<String>          categories      = new ArrayList<String>();
    @JsonProperty("ComplianceCerts")
    private List<String>          complianceCerts = new ArrayList<String>();
    @JsonProperty("SafetyWarnings")
    private List<String>          safetyWarnings  = new ArrayList<String>();
    @JsonProperty("Images")
    private List<Image>           images          = new ArrayList<Image>();
    @JsonProperty("PriceGrids")
    private List<PriceGrid>       priceGrids      = new ArrayList<PriceGrid>();
    @JsonProperty("ProductNumbers")
    private List<ProductNumber>   productNumbers  = new ArrayList<ProductNumber>();
    @JsonProperty("ProductConfigurations")
    private ProductConfigurations productConfigurations;

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAsiProdNo() {
        return asiProdNo;
    }

    public void setAsiProdNo(String asiProdNo) {
        this.asiProdNo = asiProdNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProductInventoryLink() {
        return productInventoryLink;
    }

    public void setProductInventoryLink(String productInventoryLink) {
        this.productInventoryLink = productInventoryLink;
    }

    public String getProductDataSheet() {
        return productDataSheet;
    }

    public void setProductDataSheet(String productDataSheet) {
        this.productDataSheet = productDataSheet;
    }

    public Boolean getSameDayRushOffered() {
        return sameDayRushOffered;
    }

    public void setSameDayRushOffered(Boolean sameDayRushOffered) {
        this.sameDayRushOffered = sameDayRushOffered;
    }

    public String getShipperBillsBy() {
        return shipperBillsBy;
    }

    public void setShipperBillsBy(String shipperBillsBy) {
        this.shipperBillsBy = shipperBillsBy;
    }

    public List<String> getProductKeywords() {
        return productKeywords;
    }

    public void setProductKeywords(List<String> productKeywords) {
        this.productKeywords = productKeywords;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getComplianceCerts() {
        return complianceCerts;
    }

    public void setComplianceCerts(List<String> complianceCerts) {
        this.complianceCerts = complianceCerts;
    }

    public List<String> getSafetyWarnings() {
        return safetyWarnings;
    }

    public void setSafetyWarnings(List<String> safetyWarnings) {
        this.safetyWarnings = safetyWarnings;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<PriceGrid> getPriceGrids() {
        return priceGrids;
    }

    public void setPriceGrids(List<PriceGrid> priceGrids) {
        this.priceGrids = priceGrids;
    }

    public List<ProductNumber> getProductNumbers() {
        return productNumbers;
    }

    public void setProductNumbers(List<ProductNumber> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public ProductConfigurations getProductConfigurations() {
        return productConfigurations;
    }

    public void setProductConfigurations(ProductConfigurations productConfigurations) {
        this.productConfigurations = productConfigurations;
    }

    /**
     * @return the productBreakoutBy
     */
    public String getProductBreakoutBy() {
        return productBreakoutBy;
    }

    /**
     * @param productBreakoutBy
     *            the productBreakoutBy to set
     */
    public void setProductBreakoutBy(String productBreakoutBy) {
        this.productBreakoutBy = productBreakoutBy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

}
