package com.asi.ext.api.radar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
public class PriceGrids {
    @JsonProperty("ID")
    private String               id                     = "0";
    @JsonProperty("ProductId")
    private String               productId              = "";
    @JsonProperty("IsBasePrice")
    private String               isBasePrice            = "true";
    @JsonProperty("IsQUR")
    private String               isQUR                  = "false";
    @JsonProperty("PriceGridSubTypeCode")
    private String               priceGridSubTypeCode   = "";
    @JsonProperty("UsageLevelCode")
    private String               usageLevelCode         = "";
    @JsonProperty("Description")
    private String               description            = "";
    @JsonProperty("Comment")
    private String               comment                = "";
    @JsonProperty("IsRange")
    private String               isRange                = "false";
    @JsonProperty("IsSpecial")
    private String               isSpecial              = "false";
    @JsonProperty("DisplaySequence")
    private String               displaySequence        = "1";
    @JsonProperty("IsCopy")
    private String               isCopy                 = "false";
    @JsonProperty("priceIncludes")
    private String               priceIncludes          = "";
    @JsonProperty("IsPriceIncludesEnabled")
    private String               isPriceIncludesEnabled = "false";
    @JsonProperty("ApplyOneCode")
    private String               applyOneCode           = "";
    @JsonProperty("IsApplyOneCodeEnabled")
    private String               isApplyOneCodeEnabled  = "false";
    @JsonProperty("ErrorMessage")
    private String               errorMessage           = "";
    @JsonProperty("IsUpArrowEnabled")
    private String               isUpArrowEnabled       = "false";
    @JsonProperty("IsDownArrowEnabled")
    private String               isDownArrowEnabled     = "false";
    @JsonProperty("InputMode")
    private String               inputMode              = "";
    @JsonProperty("TotalVariations")
    private String               totalVariations        = "";
    @JsonProperty("ConfigDesc")
    private String               configDesc             = "";
    @JsonProperty("DisplayProductNumber")
    private String               displayProductNumber   = "";
    @JsonProperty("AllowDeletion")
    private String               allowDeletion          = "";
    @JsonProperty("Standalone")
    private String               standalone             = "";
    @JsonProperty("IsNullo")
    private String               isNullo                = "false";
    @JsonProperty("IsDirty")
    private String               isDirty                = "false";
    @JsonProperty("Currency")
    private Currency             currency               = new Currency();
    @JsonProperty("Prices")
    private Prices[]             prices                 = {};
    @JsonProperty("PricingItems")
    private PricingItems[]       pricingItems           = {};
    @JsonProperty("DiscountRateValues")
    private DiscountRateValues[] discountRateValues     = {};

    public PriceGrids() {
        // Default Constructor
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return the isBasePrice
     */
    public String getIsBasePrice() {
        return isBasePrice;
    }

    /**
     * @param isBasePrice
     *            the isBasePrice to set
     */
    public void setIsBasePrice(String isBasePrice) {
        this.isBasePrice = isBasePrice;
    }

    /**
     * @return the isQUR
     */
    public String getIsQUR() {
        return isQUR;
    }

    /**
     * @param isQUR
     *            the isQUR to set
     */
    public void setIsQUR(String isQUR) {
        this.isQUR = isQUR;
    }

    /**
     * @return the priceGridSubTypeCode
     */
    public String getPriceGridSubTypeCode() {
        return priceGridSubTypeCode;
    }

    /**
     * @param priceGridSubTypeCode
     *            the priceGridSubTypeCode to set
     */
    public void setPriceGridSubTypeCode(String priceGridSubTypeCode) {
        this.priceGridSubTypeCode = priceGridSubTypeCode;
    }

    /**
     * @return the usageLevelCode
     */
    public String getUsageLevelCode() {
        return usageLevelCode;
    }

    /**
     * @param usageLevelCode
     *            the usageLevelCode to set
     */
    public void setUsageLevelCode(String usageLevelCode) {
        this.usageLevelCode = usageLevelCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     *            the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the isRange
     */
    public String getIsRange() {
        return isRange;
    }

    /**
     * @param isRange
     *            the isRange to set
     */
    public void setIsRange(String isRange) {
        this.isRange = isRange;
    }

    /**
     * @return the isSpecial
     */
    public String getIsSpecial() {
        return isSpecial;
    }

    /**
     * @param isSpecial
     *            the isSpecial to set
     */
    public void setIsSpecial(String isSpecial) {
        this.isSpecial = isSpecial;
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
     * @return the isCopy
     */
    public String getIsCopy() {
        return isCopy;
    }

    /**
     * @param isCopy
     *            the isCopy to set
     */
    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }

    /**
     * @return the priceIncludes
     */
    public String getPriceIncludes() {
        return priceIncludes;
    }

    /**
     * @param priceIncludes
     *            the priceIncludes to set
     */
    public void setPriceIncludes(String priceIncludes) {
        this.priceIncludes = priceIncludes;
    }

    /**
     * @return the isPriceIncludesEnabled
     */
    public String getIsPriceIncludesEnabled() {
        return isPriceIncludesEnabled;
    }

    /**
     * @param isPriceIncludesEnabled
     *            the isPriceIncludesEnabled to set
     */
    public void setIsPriceIncludesEnabled(String isPriceIncludesEnabled) {
        this.isPriceIncludesEnabled = isPriceIncludesEnabled;
    }

    /**
     * @return the applyOneCode
     */
    public String getApplyOneCode() {
        return applyOneCode;
    }

    /**
     * @param applyOneCode
     *            the applyOneCode to set
     */
    public void setApplyOneCode(String applyOneCode) {
        this.applyOneCode = applyOneCode;
    }

    /**
     * @return the isApplyOneCodeEnabled
     */
    public String getIsApplyOneCodeEnabled() {
        return isApplyOneCodeEnabled;
    }

    /**
     * @param isApplyOneCodeEnabled
     *            the isApplyOneCodeEnabled to set
     */
    public void setIsApplyOneCodeEnabled(String isApplyOneCodeEnabled) {
        this.isApplyOneCodeEnabled = isApplyOneCodeEnabled;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     *            the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the isUpArrowEnabled
     */
    public String getIsUpArrowEnabled() {
        return isUpArrowEnabled;
    }

    /**
     * @param isUpArrowEnabled
     *            the isUpArrowEnabled to set
     */
    public void setIsUpArrowEnabled(String isUpArrowEnabled) {
        this.isUpArrowEnabled = isUpArrowEnabled;
    }

    /**
     * @return the isDownArrowEnabled
     */
    public String getIsDownArrowEnabled() {
        return isDownArrowEnabled;
    }

    /**
     * @param isDownArrowEnabled
     *            the isDownArrowEnabled to set
     */
    public void setIsDownArrowEnabled(String isDownArrowEnabled) {
        this.isDownArrowEnabled = isDownArrowEnabled;
    }

    /**
     * @return the inputMode
     */
    public String getInputMode() {
        return inputMode;
    }

    /**
     * @param inputMode
     *            the inputMode to set
     */
    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    /**
     * @return the totalVariations
     */
    public String getTotalVariations() {
        return totalVariations;
    }

    /**
     * @param totalVariations
     *            the totalVariations to set
     */
    public void setTotalVariations(String totalVariations) {
        this.totalVariations = totalVariations;
    }

    /**
     * @return the configDesc
     */
    public String getConfigDesc() {
        return configDesc;
    }

    /**
     * @param configDesc
     *            the configDesc to set
     */
    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    /**
     * @return the displayProductNumber
     */
    public String getDisplayProductNumber() {
        return displayProductNumber;
    }

    /**
     * @param displayProductNumber
     *            the displayProductNumber to set
     */
    public void setDisplayProductNumber(String displayProductNumber) {
        this.displayProductNumber = displayProductNumber;
    }

    /**
     * @return the allowDeletion
     */
    public String getAllowDeletion() {
        return allowDeletion;
    }

    /**
     * @param allowDeletion
     *            the allowDeletion to set
     */
    public void setAllowDeletion(String allowDeletion) {
        this.allowDeletion = allowDeletion;
    }

    /**
     * @return the standalone
     */
    public String getStandalone() {
        return standalone;
    }

    /**
     * @param standalone
     *            the standalone to set
     */
    public void setStandalone(String standalone) {
        this.standalone = standalone;
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
     * @return the isDirty
     */
    public String getIsDirty() {
        return isDirty;
    }

    /**
     * @param isDirty
     *            the isDirty to set
     */
    public void setIsDirty(String isDirty) {
        this.isDirty = isDirty;
    }

    /**
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @param currency
     *            the currency to set
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * @return the prices
     */
    public Prices[] getPrices() {
        return prices;
    }

    /**
     * @param prices
     *            the prices to set
     */
    public void setPrices(Prices[] prices) {
        this.prices = prices;
    }

    /**
     * @return the discountRateValues
     */
    public DiscountRateValues[] getDiscountRateValues() {
        return discountRateValues;
    }

    /**
     * @param discountRateValues
     *            the discountRateValues to set
     */
    public void setDiscountRateValues(DiscountRateValues[] discountRateValues) {
        this.discountRateValues = discountRateValues;
    }

    /**
     * @return the pricingItems
     */
    public PricingItems[] getPricingItems() {
        return pricingItems;
    }

    /**
     * @param pricingItems
     *            the pricingItems to set
     */
    public void setPricingItems(PricingItems[] pricingItems) {
        this.pricingItems = pricingItems;
    }

}
