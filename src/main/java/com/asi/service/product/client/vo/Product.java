package com.asi.service.product.client.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;



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
	public void setProductConfigurations(
			ProductConfigurations[] productConfigurations) {
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
	public void setSelectedProductCategories(
			SelectedProductCategories[] selectedProductCategories) {
		this.selectedProductCategories = selectedProductCategories;
	}
	public SelectedComplianceCerts[] getSelectedComplianceCerts() {
		return selectedComplianceCerts;
	}
	public void setSelectedComplianceCerts(
			SelectedComplianceCerts[] selectedComplianceCerts) {
		this.selectedComplianceCerts = selectedComplianceCerts;
	}
	public SelectedSafetyWarnings[] getSelectedSafetyWarnings() {
		return selectedSafetyWarnings;
	}
	public void setSelectedSafetyWarnings(
			SelectedSafetyWarnings[] selectedSafetyWarnings) {
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
	public ProductNumbers[] getProductNumbers() {
		return productNumbers;
	}
	public void setProductNumbers(ProductNumbers[] productNumbers) {
		this.productNumbers = productNumbers;
	}
	public Boolean getIsProductNumberBreakout() {
		return isProductNumberBreakout;
	}
	public void setIsProductNumberBreakout(
			Boolean isProductNumberBreakout) {
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
	private String id="0";
	private String name="";
	@JsonProperty("DataSourceId")
	private String dataSourceId="";
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
	private String description="";
	private String summary="";
	private ProductConfigurations[] productConfigurations={};
	private String asiProdNo="";
	public String getAsiProdNo() {
		return asiProdNo;
	}
	public void setAsiProdNo(String asiProdNo) {
		this.asiProdNo = asiProdNo;
	}
	public void setProductNumberBreakout(boolean isProductNumberBreakout) {
		this.isProductNumberBreakout = isProductNumberBreakout;
	}
	private String companyId="";
	public String getExternalProductId() {
		return externalProductId;
	}
	public void setExternalProductId(String externalProductId) {
		this.externalProductId = externalProductId;
	}
	@JsonProperty("ExternalProductId")
	private String externalProductId="";
	private ProductDataSheet productDataSheet=new ProductDataSheet();	
	private String productTypeCode="";
	private String disclaimer="";
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
	public void setIsOrderLessThanMinimumAllowed(
			String isOrderLessThanMinimumAllowed) {
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
	private String changeProductReasonCode;
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
	private String RushServiceFlag;	
	private String SameDayRushFlag;
	private String NewProductFlag;
	private String FullColorProcessFlag;	
	private String VisibleForAllUsersFlag;	
	private String VirtualProductFlag;	
	@JsonProperty("NLevelConnectFlag")  
	private String NLevelConnectFlag;	
	private String ProductLockedFlag;
	private String Show1MediaIdIm;	
	private String Show1MediaIdVd;	
	private String IsCustomProduct;
	private String ShipperBillsByCode;
	private String AddtionalShippingInfo;	
	private String IsAvailableUnimprinted;	
	private String IsPersonalizationAvailable;	
	private String IsOrderLessThanMinimumAllowed;	
	private String PublishDate;			
	private String LocationCode;		
	private String IsWIP;
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
	private String isPriceBreakoutFlag="false";
	public ProductMediaItems[] productMediaItems;
	
	public String getIsPriceBreakoutFlag() {
		return isPriceBreakoutFlag;
	}
	public void setIsPriceBreakoutFlag(String isPriceBreakoutFlag) {
		this.isPriceBreakoutFlag = isPriceBreakoutFlag;
	}
	@JsonProperty("distributorComments")
	private String distributorComments="";
	@JsonProperty("additionalInfo")
	private String additionalInfo="";
	private String isShippableInPlainBox="false";
	private SelectedLineNames[] selectedLineNames={};
	private SelectedProductCategories[] selectedProductCategories={};
	@JsonProperty("SelectedComplianceCerts")
	private SelectedComplianceCerts[] selectedComplianceCerts={};
	@JsonProperty("SelectedSafetyWarnings")
	private SelectedSafetyWarnings[]	selectedSafetyWarnings={};
	private ProductKeywords[] productKeywords={};
	@JsonProperty("ProductMediaCitations")
	private ProductMediaCitations[] productMediaCitations={};
	public ProductMediaCitations[] getProductMediaCitations() {
		return productMediaCitations;
	}
	public void setProductMediaCitations(
			ProductMediaCitations[] productMediaCitations) {
		this.productMediaCitations = productMediaCitations;
	}
	private PriceGrids priceGrids[]={};
	private String priceConfirmationDate="";
	private String statusCode="";
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	private String workflowStatusCode="";
	private String childChanged="false";
	private String excludeAppOfferList="";
	private ProductNumbers[] productNumbers={};
	@JsonProperty("ProductInventoryLink")
	private ProductInventoryLink productInventoryLink=new ProductInventoryLink();
	public ProductInventoryLink getProductInventoryLink() {
		return productInventoryLink;
	}
	public void setProductInventoryLink(ProductInventoryLink productInventoryLink) {
		this.productInventoryLink = productInventoryLink;
	}
	private boolean isProductNumberBreakout=false;
	private String isNullo="false";
	private String isDirty="true";
	private String originalProductId="";
	public String getOriginalProductId() {
		return originalProductId;
	}
	public void setOriginalProductId(String originalProductId) {
		this.originalProductId = originalProductId;
	}
	
	
}
