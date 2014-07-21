package com.asi.ext.api.radar.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "ID", "ProductTypeCode", "OriginalProductId", "ExternalProductId", "CompanyId", "StatusCode", "Name",
        "Description", "Summary", "RushServiceFlag", "SameDayRushFlag", "AsiProdNo", "NewProductFlag", "NewProductExpirationDate",
        "FullColorProcessFlag", "VisibleForAllUsersFlag", "VirtualProductFlag", "NLevelConnectFlag", "ProductLockedFlag",
        "WorkflowStatusCode", "ChangeProductReasonCode", "WorkflowStatusStateCode", "Show1MediaIdIm", "Show1MediaIdVd",
        "IsCustomProduct", "ShipperBillsByCode", "IsShippableInPlainBox", "AddtionalShippingInfo", "ProductDataSheet",
        "ProductInventoryLink", "IsAvailableUnimprinted", "IsPersonalizationAvailable", "IsOrderLessThanMinimumAllowed",
        "Disclaimer", "PublishDate", "LastPublishedDate", "LocationCode", "IsWIP", "IsProductNumberBreakout",
        "IsPriceBreakoutFlag", "DistributorComments", "AdditionalInfo", "IncludeAppOfferList", "ProductConfigurations",
        "ProductKeywords", "SelectedProductCategories", "SelectedLineNames", "SelectedComplianceCerts", "SelectedSafetyWarnings",
        "ProductMediaItems", "ProductMediaCitations", "PriceGrids", "ProductNumbers", "Relationships", "PriceConfirmationDate" })
public class Product {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ProductConfigurations[] getProductConfigurations() {
        return productConfigurations;
    }

    public void setProductConfigurations(ProductConfigurations[] productConfigurations) {
        this.productConfigurations = productConfigurations;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public ProductDataSheet getProductDataSheet() {
        return productDataSheet;
    }

    public void setProductDataSheet(ProductDataSheet productDataSheet) {
        this.productDataSheet = productDataSheet;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getIsShippableInPlainBox() {
        return isShippableInPlainBox;
    }

    public void setIsShippableInPlainBox(String isShippableInPlainBox) {
        this.isShippableInPlainBox = isShippableInPlainBox;
    }

    public SelectedLineNames[] getSelectedLineNames() {
        return selectedLineNames;
    }

    public void setSelectedLineNames(SelectedLineNames[] selectedLineNames) {
        this.selectedLineNames = selectedLineNames;
    }

    public SelectedProductCategories[] getSelectedProductCategories() {
        return selectedProductCategories;
    }

    public void setSelectedProductCategories(SelectedProductCategories[] selectedProductCategories) {
        this.selectedProductCategories = selectedProductCategories;
    }

    public SelectedComplianceCerts[] getSelectedComplianceCerts() {
        return selectedComplianceCerts;
    }

    public void setSelectedComplianceCerts(SelectedComplianceCerts[] selectedComplianceCerts) {
        this.selectedComplianceCerts = selectedComplianceCerts;
    }

    public SelectedSafetyWarnings[] getSelectedSafetyWarnings() {
        return selectedSafetyWarnings;
    }

    public void setSelectedSafetyWarnings(SelectedSafetyWarnings[] selectedSafetyWarnings) {
        this.selectedSafetyWarnings = selectedSafetyWarnings;
    }

    public ProductKeywords[] getProductKeywords() {
        return productKeywords;
    }

    public void setProductKeywords(ProductKeywords[] productKeywords) {
        this.productKeywords = productKeywords;
    }

    public PriceGrids[] getPriceGrids() {
        return priceGrids;
    }

    public void setPriceGrids(PriceGrids[] priceGrids) {
        this.priceGrids = priceGrids;
    }

    public String getPriceConfirmationDate() {
        return priceConfirmationDate;
    }

    public void setPriceConfirmationDate(String priceConfirmationDate) {
        this.priceConfirmationDate = priceConfirmationDate;
    }

    public String getWorkflowStatusCode() {
        return workflowStatusCode;
    }

    public void setWorkflowStatusCode(String workflowStatusCode) {
        this.workflowStatusCode = workflowStatusCode;
    }

    public String getChildChanged() {
        return childChanged;
    }

    public void setChildChanged(String childChanged) {
        this.childChanged = childChanged;
    }

    public String getExcludeAppOfferList() {
        return excludeAppOfferList;
    }

    public void setExcludeAppOfferList(String excludeAppOfferList) {
        this.excludeAppOfferList = excludeAppOfferList;
    }

    public List<ProductNumbers> getProductNumbers() {
        return productNumbers;
    }

    public void setProductNumbers(List<ProductNumbers> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public Boolean getIsProductNumberBreakout() {
        return isProductNumberBreakout;
    }

    public void setIsProductNumberBreakout(Boolean isProductNumberBreakout) {
        this.isProductNumberBreakout = isProductNumberBreakout;
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

    @JsonProperty("ID")
    private String id           = "0";
    @JsonProperty("Name")
    private String name         = "";
    @JsonProperty("DataSourceId")
    private String dataSourceId = "";

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDistributorComments() {
        return distributorComments;
    }

    public void setDistributorComments(String distributorComments) {
        this.distributorComments = distributorComments;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @JsonProperty("Description")
    private String                  description           = "";
    @JsonProperty("Summary")
    private String                  summary               = "";
    @JsonProperty("ProductConfigurations")
    private ProductConfigurations[] productConfigurations = {};
    @JsonProperty("ASIProdno")
    private String                  asiProdNo             = "";

    public String getAsiProdNo() {
        return asiProdNo;
    }

    public void setAsiProdNo(String asiProdNo) {
        this.asiProdNo = asiProdNo;
    }

    public void setProductNumberBreakout(boolean isProductNumberBreakout) {
        this.isProductNumberBreakout = isProductNumberBreakout;
    }

    private String companyId = "";

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    @JsonProperty("ExternalProductId")
    private String           externalProductId = "";
    @JsonProperty("ProductDataSheet")
    private ProductDataSheet productDataSheet  = new ProductDataSheet();
    @JsonProperty("ProductTypeCode")
    private String           productTypeCode   = "";
    @JsonProperty("Disclaimer")
    private String           disclaimer        = "";

    public String getRushServiceFlag() {
        return RushServiceFlag;
    }

    public void setRushServiceFlag(String rushServiceFlag) {
        RushServiceFlag = rushServiceFlag;
    }

    public String getSameDayRushFlag() {
        return SameDayRushFlag;
    }

    public void setSameDayRushFlag(String sameDayRushFlag) {
        SameDayRushFlag = sameDayRushFlag;
    }

    public String getNewProductFlag() {
        return NewProductFlag;
    }

    public void setNewProductFlag(String newProductFlag) {
        NewProductFlag = newProductFlag;
    }

    public String getFullColorProcessFlag() {
        return FullColorProcessFlag;
    }

    public void setFullColorProcessFlag(String fullColorProcessFlag) {
        FullColorProcessFlag = fullColorProcessFlag;
    }

    public String getVisibleForAllUsersFlag() {
        return VisibleForAllUsersFlag;
    }

    public void setVisibleForAllUsersFlag(String visibleForAllUsersFlag) {
        VisibleForAllUsersFlag = visibleForAllUsersFlag;
    }

    public String getVirtualProductFlag() {
        return VirtualProductFlag;
    }

    public void setVirtualProductFlag(String virtualProductFlag) {
        VirtualProductFlag = virtualProductFlag;
    }

    public String getNLevelConnectFlag() {
        return NLevelConnectFlag;
    }

    public void setNLevelConnectFlag(String nLevelConnectFlag) {
        NLevelConnectFlag = nLevelConnectFlag;
    }

    public String getProductLockedFlag() {
        return ProductLockedFlag;
    }

    public void setProductLockedFlag(String productLockedFlag) {
        ProductLockedFlag = productLockedFlag;
    }

    public String getShow1MediaIdIm() {
        return Show1MediaIdIm;
    }

    public void setShow1MediaIdIm(String show1MediaIdIm) {
        Show1MediaIdIm = show1MediaIdIm;
    }

    public String getShow1MediaIdVd() {
        return Show1MediaIdVd;
    }

    public void setShow1MediaIdVd(String show1MediaIdVd) {
        Show1MediaIdVd = show1MediaIdVd;
    }

    public String getIsCustomProduct() {
        return IsCustomProduct;
    }

    public void setIsCustomProduct(String isCustomProduct) {
        IsCustomProduct = isCustomProduct;
    }

    public String getShipperBillsByCode() {
        return ShipperBillsByCode;
    }

    public void setShipperBillsByCode(String shipperBillsByCode) {
        ShipperBillsByCode = shipperBillsByCode;
    }

    public String getAddtionalShippingInfo() {
        return AddtionalShippingInfo;
    }

    @JsonSetter("AddtionalShippingInfo")
    public void setAddtionalShippingInfo(String addtionalShippingInfo) {
        AddtionalShippingInfo = addtionalShippingInfo;
    }

    @JsonSetter("additionalShippingInfo")
    public void setAddtionalShippingInfoAltr(String addtionalShippingInfo) {
        AddtionalShippingInfo = addtionalShippingInfo;
    }

    public String getIsAvailableUnimprinted() {
        return IsAvailableUnimprinted;
    }

    public void setIsAvailableUnimprinted(String isAvailableUnimprinted) {
        IsAvailableUnimprinted = isAvailableUnimprinted;
    }

    public String getIsPersonalizationAvailable() {
        return IsPersonalizationAvailable;
    }

    public void setIsPersonalizationAvailable(String isPersonalizationAvailable) {
        IsPersonalizationAvailable = isPersonalizationAvailable;
    }

    public String getIsOrderLessThanMinimumAllowed() {
        return IsOrderLessThanMinimumAllowed;
    }

    public void setIsOrderLessThanMinimumAllowed(String isOrderLessThanMinimumAllowed) {
        IsOrderLessThanMinimumAllowed = isOrderLessThanMinimumAllowed;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getIsWIP() {
        return IsWIP;
    }

    public void setIsWIP(String isWIP) {
        IsWIP = isWIP;
    }

    @JsonProperty("ChangeProductReasonCode")
    private String changeProductReasonCode;
    @JsonProperty("WorkflowStatusStateCode")
    private String workflowStatusStateCode;

    public String getWorkflowStatusStateCode() {
        return workflowStatusStateCode;
    }

    public void setWorkflowStatusStateCode(String workflowStatusStateCode) {
        this.workflowStatusStateCode = workflowStatusStateCode;
    }

    public String getChangeProductReasonCode() {
        return changeProductReasonCode;
    }

    public void setChangeProductReasonCode(String changeProductReasonCode) {
        this.changeProductReasonCode = changeProductReasonCode;
    }

    @JsonProperty("RushServiceFlag")
    private String          RushServiceFlag;
    @JsonProperty("SameDayRushFlag")
    private String          SameDayRushFlag;
    @JsonProperty("NewProductFlag")
    private String          NewProductFlag;
    @JsonProperty("FullColorProcessFlag")
    private String          FullColorProcessFlag;
    @JsonProperty("VisibleForAllUsersFlag")
    private String          VisibleForAllUsersFlag;
    @JsonProperty("VirtualProductFlag")
    private String          VirtualProductFlag;
    @JsonProperty("NLevelConnectFlag")
    private String          NLevelConnectFlag;
    @JsonProperty("ProductLockedFlag")
    private String          ProductLockedFlag;
    @JsonProperty("Show1MediaIdIm")
    private String          Show1MediaIdIm;
    @JsonProperty("Show1MediaIdVd")
    private String          Show1MediaIdVd;
    @JsonProperty("IsCustomProduct")
    private String          IsCustomProduct;
    @JsonProperty("ShipperBillsByCode")
    private String          ShipperBillsByCode;
    @JsonProperty("AddtionalShippingInfo")
    private String          AddtionalShippingInfo;
    @JsonProperty("IsAvailableUnimprinted")
    private String          IsAvailableUnimprinted;
    @JsonProperty("IsPersonalizationAvailable")
    private String          IsPersonalizationAvailable;
    @JsonProperty("IsOrderLessThanMinimumAllowed")
    private String          IsOrderLessThanMinimumAllowed;
    @JsonProperty("PublishDate")
    private String          PublishDate;
    @JsonProperty("LocationCode")
    private String          LocationCode;
    @JsonProperty("IsWIP")
    private String          IsWIP;
    @JsonProperty("ProofProdGroupId")
    private String          proofProdGroupId;
    @JsonProperty("ProofPageNo")
    private String          proofPageNo;
    @JsonProperty("ProofSubIssueId")
    private String          proofSubIssueId;
    @JsonProperty("Show1ProdNo")
    private String          show1ProdNo;
    @JsonProperty("Show1PriceGridId")
    private String          show1PriceGridId;
    @JsonProperty("LastPublishedDate")
    private String          lastPublishedDate;
    @JsonProperty("IncludeAppOfferList")
    private String          includeAppOfferList;

    @JsonProperty("Relationships")
    private Relationships[] relationships;

    public Relationships[] getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationships[] relationships) {
        this.relationships = relationships;
    }

    public ProductMediaItems[] getProductMediaItems() {
        return productMediaItems;
    }

    public void setProductMediaItems(ProductMediaItems[] productMediaItems) {
        this.productMediaItems = productMediaItems;
    }

    @JsonProperty("IsPriceBreakoutFlag")
    private String             isPriceBreakoutFlag = "false";
    @JsonProperty("ProductMediaItems")
    public ProductMediaItems[] productMediaItems;

    public String getIsPriceBreakoutFlag() {
        return isPriceBreakoutFlag;
    }

    public void setIsPriceBreakoutFlag(String isPriceBreakoutFlag) {
        this.isPriceBreakoutFlag = isPriceBreakoutFlag;
    }

    @JsonProperty("DistributorComments")
    private String                      distributorComments       = "";
    @JsonProperty("AdditionalInfo")
    private String                      additionalInfo            = "";
    @JsonProperty("IsShippableInPlainBox")
    private String                      isShippableInPlainBox     = "false";
    @JsonProperty("SelectedLineNames")
    private SelectedLineNames[]         selectedLineNames         = {};
    @JsonProperty("SelectedProductCategories")
    private SelectedProductCategories[] selectedProductCategories = {};
    @JsonProperty("SelectedComplianceCerts")
    private SelectedComplianceCerts[]   selectedComplianceCerts   = {};
    @JsonProperty("SelectedSafetyWarnings")
    private SelectedSafetyWarnings[]    selectedSafetyWarnings    = {};
    @JsonProperty("ProductKeywords")
    private ProductKeywords[]           productKeywords           = {};
    @JsonProperty("ProductMediaCitations")
    private ProductMediaCitations[]     productMediaCitations     = {};

    public ProductMediaCitations[] getProductMediaCitations() {
        return productMediaCitations;
    }

    public void setProductMediaCitations(ProductMediaCitations[] productMediaCitations) {
        this.productMediaCitations = productMediaCitations;
    }

    @JsonProperty("PriceGrids")
    private PriceGrids priceGrids[]          = {};
    @JsonProperty("PriceConfirmationDate")
    private String     priceConfirmationDate = "";
    @JsonProperty("StatusCode")
    private String     statusCode            = "";

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("WorkflowStatusCode")
    private String               workflowStatusCode   = "";
    @JsonProperty("ChildChanged")
    private String               childChanged         = "false";
    @JsonProperty("ExcludeAppOfferList")
    private String               excludeAppOfferList  = "";
    @JsonProperty("ProductNumbers")
    private List<ProductNumbers> productNumbers       = new ArrayList<>();
    @JsonProperty("ProductInventoryLink")
    private ProductInventoryLink productInventoryLink = new ProductInventoryLink();

    public ProductInventoryLink getProductInventoryLink() {
        return productInventoryLink;
    }

    public void setProductInventoryLink(ProductInventoryLink productInventoryLink) {
        this.productInventoryLink = productInventoryLink;
    }

    @JsonProperty("IsProductNumberBreakout")
    private boolean isProductNumberBreakout = false;
    @JsonProperty("IsNullo")
    private String  isNullo                 = "false";
    @JsonProperty("IsDirty")
    private String  isDirty                 = "true";
    @JsonProperty("OriginalProductId")
    private String  originalProductId       = "";

    public String getOriginalProductId() {
        return originalProductId;
    }

    public void setOriginalProductId(String originalProductId) {
        this.originalProductId = originalProductId;
    }

    /**
     * @return the proofProdGroupId
     */
    public String getProofProdGroupId() {
        return proofProdGroupId;
    }

    /**
     * @param proofProdGroupId
     *            the proofProdGroupId to set
     */
    public void setProofProdGroupId(String proofProdGroupId) {
        this.proofProdGroupId = proofProdGroupId;
    }

    /**
     * @return the proofPageNo
     */
    public String getProofPageNo() {
        return proofPageNo;
    }

    /**
     * @param proofPageNo
     *            the proofPageNo to set
     */
    public void setProofPageNo(String proofPageNo) {
        this.proofPageNo = proofPageNo;
    }

    /**
     * @return the proofSubIssueId
     */
    public String getProofSubIssueId() {
        return proofSubIssueId;
    }

    /**
     * @param proofSubIssueId
     *            the proofSubIssueId to set
     */
    public void setProofSubIssueId(String proofSubIssueId) {
        this.proofSubIssueId = proofSubIssueId;
    }

    /**
     * @return the show1ProdNo
     */
    public String getShow1ProdNo() {
        return show1ProdNo;
    }

    /**
     * @param show1ProdNo
     *            the show1ProdNo to set
     */
    public void setShow1ProdNo(String show1ProdNo) {
        this.show1ProdNo = show1ProdNo;
    }

    /**
     * @return the show1PriceGridId
     */
    public String getShow1PriceGridId() {
        return show1PriceGridId;
    }

    /**
     * @param show1PriceGridId
     *            the show1PriceGridId to set
     */
    public void setShow1PriceGridId(String show1PriceGridId) {
        this.show1PriceGridId = show1PriceGridId;
    }

    /**
     * @return the lastPublishedDate
     */
    public String getLastPublishedDate() {
        return lastPublishedDate;
    }

    /**
     * @param lastPublishedDate
     *            the lastPublishedDate to set
     */
    public void setLastPublishedDate(String lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }

    /**
     * @return the includeAppOfferList
     */
    public String getIncludeAppOfferList() {
        return includeAppOfferList;
    }

    /**
     * @param includeAppOfferList
     *            the includeAppOfferList to set
     */
    public void setIncludeAppOfferList(String includeAppOfferList) {
        this.includeAppOfferList = includeAppOfferList;
    }

}
