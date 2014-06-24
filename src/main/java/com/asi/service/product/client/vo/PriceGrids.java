package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
public class PriceGrids {
@JsonProperty("ID")
private String id="0";
private String productId="",isBasePrice="true",isQUR="false",priceGridSubTypeCode="",usageLevelCode="",comment="",isRange="false",isSpecial="false";
@JsonProperty("priceIncludes")
private String priceIncludes="";
private String isPriceIncludesEnabled="false",description="",displaySequence="1",applyOneCode="",
isApplyOneCodeEnabled="false",errorMessage="",isUpArrowEnabled="false",isDownArrowEnabled="false",inputMode="",totalVariations="",configDesc="",
displayProductNumber="",allowDeletion="",standalone="",isNullo="false",isDirty="false";
private String isCopy="false";
public String getIsCopy() {
	return isCopy;
}
public void setIsCopy(String isCopy) {
	this.isCopy = isCopy;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getProductId() {
	return productId;
}
public void setProductId(String productId) {
	this.productId = productId;
}
public String getIsBasePrice() {
	return isBasePrice;
}
public void setIsBasePrice(String isBasePrice) {
	this.isBasePrice = isBasePrice;
}
public String getIsQUR() {
	return isQUR;
}
public void setIsQUR(String isQUR) {
	this.isQUR = isQUR;
}
public String getPriceGridSubTypeCode() {
	return priceGridSubTypeCode;
}
public void setPriceGridSubTypeCode(String priceGridSubTypeCode) {
	this.priceGridSubTypeCode = priceGridSubTypeCode;
}
public String getUsageLevelCode() {
	return usageLevelCode;
}
public void setUsageLevelCode(String usageLevelCode) {
	this.usageLevelCode = usageLevelCode;
}
public String getComment() {
	return comment;
}
public void setComment(String comment) {
	this.comment = comment;
}
public String getIsRange() {
	return isRange;
}
public void setIsRange(String isRange) {
	this.isRange = isRange;
}
public String getIsSpecial() {
	return isSpecial;
}
public void setIsSpecial(String isSpecial) {
	this.isSpecial = isSpecial;
}
public String getPriceIncludes() {
	return priceIncludes;
}
public void setPriceIncludes(String priceIncludes) {
	this.priceIncludes = priceIncludes;
}
public String getIsPriceIncludesEnabled() {
	return isPriceIncludesEnabled;
}
public void setIsPriceIncludesEnabled(String isPriceIncludesEnabled) {
	this.isPriceIncludesEnabled = isPriceIncludesEnabled;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public Currency getCurrency() {
	return currency;
}
public void setCurrency(Currency currency) {
	this.currency = currency;
}
public String getDisplaySequence() {
	return displaySequence;
}
public void setDisplaySequence(String displaySequence) {
	this.displaySequence = displaySequence;
}
public String getApplyOneCode() {
	return applyOneCode;
}
public void setApplyOneCode(String applyOneCode) {
	this.applyOneCode = applyOneCode;
}
public String getIsApplyOneCodeEnabled() {
	return isApplyOneCodeEnabled;
}
public void setIsApplyOneCodeEnabled(String isApplyOneCodeEnabled) {
	this.isApplyOneCodeEnabled = isApplyOneCodeEnabled;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
public String getIsUpArrowEnabled() {
	return isUpArrowEnabled;
}
public void setIsUpArrowEnabled(String isUpArrowEnabled) {
	this.isUpArrowEnabled = isUpArrowEnabled;
}
public String getIsDownArrowEnabled() {
	return isDownArrowEnabled;
}
public void setIsDownArrowEnabled(String isDownArrowEnabled) {
	this.isDownArrowEnabled = isDownArrowEnabled;
}
public String getInputMode() {
	return inputMode;
}
public void setInputMode(String inputMode) {
	this.inputMode = inputMode;
}
public String getTotalVariations() {
	return totalVariations;
}
public void setTotalVariations(String totalVariations) {
	this.totalVariations = totalVariations;
}
public String getConfigDesc() {
	return configDesc;
}
public void setConfigDesc(String configDesc) {
	this.configDesc = configDesc;
}
public String getDisplayProductNumber() {
	return displayProductNumber;
}
public void setDisplayProductNumber(String displayProductNumber) {
	this.displayProductNumber = displayProductNumber;
}
public String getAllowDeletion() {
	return allowDeletion;
}
public void setAllowDeletion(String allowDeletion) {
	this.allowDeletion = allowDeletion;
}
public String getStandalone() {
	return standalone;
}
public void setStandalone(String standalone) {
	this.standalone = standalone;
}
public String getIsNullo() {
	return isNullo;
}
public void setIsNullo(String isNullo) {
	this.isNullo = isNullo;
}
public String getIsDirty() {
	return isDirty;
}
public void setIsDirty(String isDirty) {
	this.isDirty = isDirty;
}
public DiscountRateValues[] getDiscountRateValues() {
	return discountRateValues;
}
public void setDiscountRateValues(DiscountRateValues[] discountRateValues) {
	this.discountRateValues = discountRateValues;
}
public PricingItems[] getPricingItems() {
	return pricingItems;
}
public void setPricingItems(PricingItems[] pricingItems) {
	this.pricingItems = pricingItems;
}
private Prices[] prices={};
public Prices[] getPrices() {
	return prices;
}
public void setPrices(Prices[] prices) {
	this.prices = prices;
}
private Currency currency=new Currency();
private DiscountRateValues[] discountRateValues={};
private PricingItems[] pricingItems={};
}
