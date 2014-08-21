package com.asi.ext.api.service.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class Product {

    @JsonProperty("ExternalProductId")
    private String             externalProductId;
    @JsonProperty("Name")
    private String             name;
    @JsonProperty("Description")
    private String             description;
    @JsonProperty("Summary")
    private String             summary;
    @JsonProperty("AsiProdNo")
    private String             asiProdNo;
    @JsonProperty("ProductType")
    private String             productType;
    @JsonProperty("StatusCode")
    private String             statusCode;
    @JsonProperty("ProductInventoryLink")
    private String             productInventoryLink;
    @JsonProperty("ProductDataSheet")
    private String             productDataSheet;
    @JsonProperty("ShipperBillsBy")
    private String             shipperBillsBy;
    @JsonProperty("ProductBreakoutBy")
    private String             productBreakoutBy;
    @JsonProperty("BreakoutByPrice")
    private String             breakOutByPrice;
    @JsonProperty("LineNames")
    private List<String>       lineNames               = null;
    @JsonProperty("Catalogs")
    private List<Catalog>      catalogs                = null;
    @JsonProperty("DistributorOnly")
    private String             distributorOnly         = null;
    @JsonProperty("DistributorOnlyComments")
    private String             distributorOnlyComments = null;
    @JsonProperty("ProductDisclaimer")
    private String             productDisclaimer       = null;
    @JsonProperty("AdditionalProductInfo")
    private String             additionalProductInfo   = null;
    @JsonProperty("AdditionalShippingInfo")
    private String             additionalShippingInfo  = null;
    @JsonProperty("PriceConfirmedThru")
    private String             priceConfirmedThru      = null;
    @JsonProperty("CanOrderLessThanMimimum")
    private String             canOrderLessThanMimimum = null;
    @JsonProperty("Availability")
    private List<Availability> availability;

    /**
     * @return the availability
     */
	public List<Availability> getAvailability() {
        return availability;
    }

    /**
     * @param availability
     *            the availability to set
     */
	public void setAvailability(List<Availability> availability) {
        this.availability = availability;
    }

    public String getDistributorOnlyComments() {
        return distributorOnlyComments;
    }

    public void setDistributorOnlyComments(String distributorOnlyComments) {
        this.distributorOnlyComments = distributorOnlyComments;
    }

    public String getProductDisclaimer() {
        return productDisclaimer;
    }

    public void setProductDisclaimer(String productDisclaimer) {
        this.productDisclaimer = productDisclaimer;
    }

    public String getAdditionalProductInfo() {
        return additionalProductInfo;
    }

    public void setAdditionalProductInfo(String additionalProductInfo) {
        this.additionalProductInfo = additionalProductInfo;
    }

    public String getAdditionalShippingInfo() {
        return additionalShippingInfo;
    }

    public void setAdditionalShippingInfo(String additionalShippingInfo) {
        this.additionalShippingInfo = additionalShippingInfo;
    }

    public String getPriceConfirmedThru() {
        return priceConfirmedThru;
    }

    public void setPriceConfirmedThru(String priceConfirmedThru) {
        this.priceConfirmedThru = priceConfirmedThru;
    }

    public String getCanOrderLessThanMimimum() {
        return canOrderLessThanMimimum;
    }

    public void setCanOrderLessThanMimimum(String canOrderLessThanMimimum) {
        this.canOrderLessThanMimimum = canOrderLessThanMimimum;
    }

    public String getDistributorOnly() {
        return distributorOnly;
    }

    public void setDistributorOnly(String distributorOnly) {
        this.distributorOnly = distributorOnly;
    }

    public String getBreakOutByPrice() {
        return breakOutByPrice;
    }

    public void setBreakOutByPrice(String breakOutByPrice) {
        this.breakOutByPrice = breakOutByPrice;
    }

    public List<String> getLineNames() {
        return lineNames;
    }

    public void setLineNames(List<String> lineNames) {
        this.lineNames = lineNames;
    }

    @JsonProperty("FOBPoints")
    private List<String>          fobPoints       = null;

    @JsonProperty("ProductKeywords")
    private List<String>          productKeywords = null;
    @JsonProperty("Categories")
    private List<String>          categories      = null;
    @JsonProperty("ComplianceCerts")
    private List<String>          complianceCerts = null;
    @JsonProperty("SafetyWarnings")
    private List<String>          safetyWarnings  = null;
    @JsonProperty("Images")
    private List<Image>           images          = null;
    @JsonProperty("PriceGrids")
    private List<PriceGrid>       priceGrids      = null;
    @JsonProperty("ProductNumbers")
    private List<ProductNumber>   productNumbers  = null;
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

    public List<String> getFobPoints() {
        return fobPoints;
    }

    public void setFobPoints(List<String> fobPoints) {
        this.fobPoints = fobPoints;
    }

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

}
