package com.asi.ext.api.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Product {

    @JsonProperty("ExternalProductId")
    private String externalProductId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Summary")
    private String summary;
    @JsonProperty("AsiProdNo")
    private String asiProdNo;
    @JsonProperty("ProductType")
    private String productType;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("ProductInventoryLink")
    private String productInventoryLink;
    @JsonProperty("ProductDataSheet")
    private String productDataSheet;
    @JsonProperty("SameDayRushOffered")
    private Boolean sameDayRushOffered;
    @JsonProperty("ShipperBillsBy")
    private String shipperBillsBy;
    @JsonProperty("ProductKeywords")
    private List<String> productKeywords = new ArrayList<String>();
    @JsonProperty("Categories")
    private List<String> categories = new ArrayList<String>();
    @JsonProperty("ComplianceCerts")
    private List<String> complianceCerts = new ArrayList<String>();
    @JsonProperty("SafetyWarnings")
    private List<String> safetyWarnings = new ArrayList<String>();
    @JsonProperty("Images")
    private List<Image> images = new ArrayList<Image>();
    @JsonProperty("PriceGrids")
    private List<PriceGrid> priceGrids = new ArrayList<PriceGrid>();
    @JsonProperty("ProductNumbers")
    private List<ProductNumber> productNumbers = new ArrayList<ProductNumber>();
    @JsonProperty("ProductConfigurations")
    private ProductConfigurations productConfigurations;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ExternalProductId")
    public String getExternalProductId() {
        return externalProductId;
    }

    @JsonProperty("ExternalProductId")
    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("Summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("AsiProdNo")
    public String getAsiProdNo() {
        return asiProdNo;
    }

    @JsonProperty("AsiProdNo")
    public void setAsiProdNo(String asiProdNo) {
        this.asiProdNo = asiProdNo;
    }

    @JsonProperty("ProductType")
    public String getProductType() {
        return productType;
    }

    @JsonProperty("ProductType")
    public void setProductType(String productType) {
        this.productType = productType;
    }

    @JsonProperty("StatusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("ProductInventoryLink")
    public String getProductInventoryLink() {
        return productInventoryLink;
    }

    @JsonProperty("ProductInventoryLink")
    public void setProductInventoryLink(String productInventoryLink) {
        this.productInventoryLink = productInventoryLink;
    }

    @JsonProperty("ProductDataSheet")
    public String getProductDataSheet() {
        return productDataSheet;
    }

    @JsonProperty("ProductDataSheet")
    public void setProductDataSheet(String productDataSheet) {
        this.productDataSheet = productDataSheet;
    }

    @JsonProperty("SameDayRushOffered")
    public Boolean getSameDayRushOffered() {
        return sameDayRushOffered;
    }

    @JsonProperty("SameDayRushOffered")
    public void setSameDayRushOffered(Boolean sameDayRushOffered) {
        this.sameDayRushOffered = sameDayRushOffered;
    }

    @JsonProperty("ShipperBillsBy")
    public String getShipperBillsBy() {
        return shipperBillsBy;
    }

    @JsonProperty("ShipperBillsBy")
    public void setShipperBillsBy(String shipperBillsBy) {
        this.shipperBillsBy = shipperBillsBy;
    }

    @JsonProperty("ProductKeywords")
    public List<String> getProductKeywords() {
        return productKeywords;
    }

    @JsonProperty("ProductKeywords")
    public void setProductKeywords(List<String> productKeywords) {
        this.productKeywords = productKeywords;
    }

    @JsonProperty("Categories")
    public List<String> getCategories() {
        return categories;
    }

    @JsonProperty("Categories")
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @JsonProperty("ComplianceCerts")
    public List<String> getComplianceCerts() {
        return complianceCerts;
    }

    @JsonProperty("ComplianceCerts")
    public void setComplianceCerts(List<String> complianceCerts) {
        this.complianceCerts = complianceCerts;
    }

    @JsonProperty("SafetyWarnings")
    public List<String> getSafetyWarnings() {
        return safetyWarnings;
    }

    @JsonProperty("SafetyWarnings")
    public void setSafetyWarnings(List<String> safetyWarnings) {
        this.safetyWarnings = safetyWarnings;
    }

    @JsonProperty("Images")
    public List<Image> getImages() {
        return images;
    }

    @JsonProperty("Images")
    public void setImages(List<Image> images) {
        this.images = images;
    }

    @JsonProperty("PriceGrids")
    public List<PriceGrid> getPriceGrids() {
        return priceGrids;
    }

    @JsonProperty("PriceGrids")
    public void setPriceGrids(List<PriceGrid> priceGrids) {
        this.priceGrids = priceGrids;
    }

    @JsonProperty("ProductNumbers")
    public List<ProductNumber> getProductNumbers() {
        return productNumbers;
    }

    @JsonProperty("ProductNumbers")
    public void setProductNumbers(List<ProductNumber> productNumbers) {
        this.productNumbers = productNumbers;
    }

    @JsonProperty("ProductConfigurations")
    public ProductConfigurations getProductConfigurations() {
        return productConfigurations;
    }

    @JsonProperty("ProductConfigurations")
    public void setProductConfigurations(ProductConfigurations productConfigurations) {
        this.productConfigurations = productConfigurations;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
