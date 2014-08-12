
package com.asi.ext.api.radar.model;

import java.util.ArrayList;
import java.util.List;

import com.asi.service.product.client.vo.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ID",
    "ProductTypeCode",
    "OriginalProductId",
    "ExternalProductId",
    "CompanyId",
    "StatusCode",
    "Name",
    "Description",
    "Summary",
    "RushServiceFlag",
    "SameDayRushFlag",
    "AsiProdNo",
    "NewProductFlag",
    "NewProductExpirationDate",
    "FullColorProcessFlag",
    "VisibleForAllUsersFlag",
    "VirtualProductFlag",
    "NLevelConnectFlag",
    "ProductLockedFlag",
    "WorkflowStatusCode",
    "ChangeProductReasonCode",
    "WorkflowStatusStateCode",
    "Show1MediaIdIm",
    "Show1MediaIdVd",
    "IsCustomProduct",
    "ShipperBillsByCode",
    "IsShippableInPlainBox",
    "AddtionalShippingInfo",
    "ProductDataSheet",
    "ProductInventoryLink",
    "IsAvailableUnimprinted",
    "IsPersonalizationAvailable",
    "IsOrderLessThanMinimumAllowed",
    "Disclaimer",
    "PublishDate",
    "LastPublishedDate",
    "LocationCode",
    "IsWIP",
    "IsProductNumberBreakout",
    "IsPriceBreakoutFlag",
    "DistributorComments",
    "AdditionalInfo",
    "IncludeAppOfferList",
    "ProductConfigurations",
    "ProductKeywords",
    "SelectedProductCategories",
    "SelectedLineNames",
    "SelectedComplianceCerts",
    "SelectedSafetyWarnings",
    "ProductMediaItems",
    "ProductMediaCitations",
    "PriceGrids",
    "ProductNumbers",
    "Relationships",
    "PriceConfirmationDate"
})
public class ProductDetailTest {

    @JsonProperty("ID")
    private Integer ID;
    @JsonProperty("ProductTypeCode")
    private String productTypeCode;
    @JsonProperty("OriginalProductId")
    private Integer originalProductId;
    @JsonProperty("ExternalProductId")
    private String externalProductId;
    @JsonProperty("CompanyId")
    private String companyId;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Summary")
    private String summary;
    @JsonProperty("RushServiceFlag")
    private String rushServiceFlag;
    @JsonProperty("SameDayRushFlag")
    private String sameDayRushFlag;
    @JsonProperty("AsiProdNo")
    private String asiProdNo;
    @JsonProperty("NewProductFlag")
    private String newProductFlag;
    @JsonProperty("NewProductExpirationDate")
    private String newProductExpirationDate;
	@JsonProperty("FullColorProcessFlag")
    private String fullColorProcessFlag;
    @JsonProperty("VisibleForAllUsersFlag")
    private String visibleForAllUsersFlag;
    @JsonProperty("VirtualProductFlag")
    private String virtualProductFlag;
    @JsonProperty("NLevelConnectFlag")
    private String nLevelConnectFlag;
    @JsonProperty("ProductLockedFlag")
    private String productLockedFlag;
    @JsonProperty("WorkflowStatusCode")
    private String workflowStatusCode;
    @JsonProperty("ChangeProductReasonCode")
    private String changeProductReasonCode;
    @JsonProperty("WorkflowStatusStateCode")
    private String workflowStatusStateCode;
    @JsonProperty("Show1MediaIdIm")
    private Integer show1MediaIdIm;
    @JsonProperty("Show1MediaIdVd")
    private Integer show1MediaIdVd;
    @JsonProperty("IsCustomProduct")
    private Boolean isCustomProduct;
    @JsonProperty("ShipperBillsByCode")
    private String shipperBillsByCode;
    @JsonProperty("IsShippableInPlainBox")
    private Boolean isShippableInPlainBox;
    @JsonProperty("ProductDataSheet")
    private ProductDataSheet productDataSheet;
    @JsonProperty("ProductInventoryLink")
    private ProductInventoryLink productInventoryLink;
    @JsonProperty("IsAvailableUnimprinted")
    private Boolean isAvailableUnimprinted;
    @JsonProperty("IsPersonalizationAvailable")
    private Boolean isPersonalizationAvailable;
    @JsonProperty("IsOrderLessThanMinimumAllowed")
    private Boolean isOrderLessThanMinimumAllowed;
    @JsonProperty("Disclaimer")
    private String disclaimer;
    @JsonProperty("PublishDate")
    private String publishDate;
    @JsonProperty("LastPublishedDate")
    private String lastPublishedDate;
    @JsonProperty("LocationCode")
    private String locationCode;
    @JsonProperty("IsWIP")
    private Boolean isWIP;
    @JsonProperty("IsProductNumberBreakout")
    private Boolean isProductNumberBreakout;
    @JsonProperty("IsPriceBreakoutFlag")
    private Boolean isPriceBreakoutFlag;
    @JsonProperty("DistributorComments")
    private String distributorComments;
    @JsonProperty("AdditionalInfo")
    private String additionalInfo;
    @JsonProperty("IncludeAppOfferList")
    private String includeAppOfferList;
    @JsonProperty("PriceConfirmationDate")
    private String priceConfirmationDate;
	@JsonProperty("DataSourceId")
	private String dataSourceId="";
	public ProductDetailTest() {
		// TODO Auto-generated constructor stub
	}
	public String getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}    
    public String getPriceConfirmationDate() {
		return priceConfirmationDate;
	}
	public void setPriceConfirmationDate(String priceConfirmationDate) {
		this.priceConfirmationDate = priceConfirmationDate;
	}
	@JsonProperty("ProductConfigurations")
    private List<ProductConfiguration> productConfigurations = new ArrayList<ProductConfiguration>();
    @JsonProperty("ProductKeywords")
    private List<ProductKeywords> productKeywords = new ArrayList<ProductKeywords>();
    @JsonProperty("SelectedProductCategories")
    private List<SelectedProductCategory> selectedProductCategories = new ArrayList<SelectedProductCategory>();
    @JsonProperty("SelectedLineNames")
    private List<SelectedLineNames> selectedLineNames = new ArrayList<SelectedLineNames>();
    @JsonProperty("SelectedComplianceCerts")
    private List<SelectedComplianceCert> selectedComplianceCerts = new ArrayList<SelectedComplianceCert>();
    @JsonProperty("SelectedSafetyWarnings")
    private List<SelectedSafetyWarnings> selectedSafetyWarnings = new ArrayList<SelectedSafetyWarnings>();
    @JsonProperty("ProductMediaItems")
    private List<ProductMediaItems> productMediaItems = new ArrayList<ProductMediaItems>();
    @JsonProperty("ProductMediaCitations")
    private List<ProductMediaCitations> productMediaCitations = new ArrayList<ProductMediaCitations>();
    @JsonProperty("PriceGrids")
    private List<PriceGrid> priceGrids = new ArrayList<PriceGrid>();
    @JsonProperty("ProductNumbers")
    private List<ProductNumber> productNumbers = new ArrayList<ProductNumber>();
    @JsonProperty("Relationships")
    private List<Relationship> relationships = new ArrayList<Relationship>();
    
    public String getNewProductExpirationDate() {
		return newProductExpirationDate;
	}
	public void setNewProductExpirationDate(String newProductExpirationDate) {
		this.newProductExpirationDate = newProductExpirationDate;
	}
    @JsonProperty("AddtionalShippingInfo")
    public String getAdditionalShippingInfo() {
		return additionalShippingInfo;
	}
    @JsonProperty("AddtionalShippingInfo")
	public void setAdditionalShippingInfo(String additionalShippingInfo) {
		this.additionalShippingInfo = additionalShippingInfo;
	}
	@JsonProperty("AddtionalShippingInfo")
    private String additionalShippingInfo;
    
    
   // private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private String processedURL = "";
    @JsonProperty("ID")
    public Integer getID() {
        return ID;
    }

    @JsonProperty("ID")
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @JsonProperty("ProductTypeCode")
    public String getProductTypeCode() {
        return productTypeCode;
    }

    @JsonProperty("ProductTypeCode")
    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    @JsonProperty("OriginalProductId")
    public Integer getOriginalProductId() {
        return originalProductId;
    }

    @JsonProperty("OriginalProductId")
    public void setOriginalProductId(Integer originalProductId) {
        this.originalProductId = originalProductId;
    }

    @JsonProperty("ExternalProductId")
    public String getExternalProductId() {
        return externalProductId;
    }

    @JsonProperty("ExternalProductId")
    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    @JsonProperty("CompanyId")
    public String getCompanyId() {
        return companyId;
    }

    @JsonProperty("CompanyId")
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @JsonProperty("StatusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    @JsonProperty("RushServiceFlag")
    public String getRushServiceFlag() {
        return rushServiceFlag;
    }

    @JsonProperty("RushServiceFlag")
    public void setRushServiceFlag(String rushServiceFlag) {
        this.rushServiceFlag = rushServiceFlag;
    }

    @JsonProperty("SameDayRushFlag")
    public String getSameDayRushFlag() {
        return sameDayRushFlag;
    }

    @JsonProperty("SameDayRushFlag")
    public void setSameDayRushFlag(String sameDayRushFlag) {
        this.sameDayRushFlag = sameDayRushFlag;
    }

    @JsonProperty("AsiProdNo")
    public String getAsiProdNo() {
        return asiProdNo;
    }

    @JsonProperty("AsiProdNo")
    public void setAsiProdNo(String asiProdNo) {
        this.asiProdNo = asiProdNo;
    }

    @JsonProperty("NewProductFlag")
    public String getNewProductFlag() {
        return newProductFlag;
    }

    @JsonProperty("NewProductFlag")
    public void setNewProductFlag(String newProductFlag) {
        this.newProductFlag = newProductFlag;
    }

    @JsonProperty("FullColorProcessFlag")
    public String getFullColorProcessFlag() {
        return fullColorProcessFlag;
    }

    @JsonProperty("FullColorProcessFlag")
    public void setFullColorProcessFlag(String fullColorProcessFlag) {
        this.fullColorProcessFlag = fullColorProcessFlag;
    }

    @JsonProperty("VisibleForAllUsersFlag")
    public String getVisibleForAllUsersFlag() {
        return visibleForAllUsersFlag;
    }

    @JsonProperty("VisibleForAllUsersFlag")
    public void setVisibleForAllUsersFlag(String visibleForAllUsersFlag) {
        this.visibleForAllUsersFlag = visibleForAllUsersFlag;
    }

    @JsonProperty("VirtualProductFlag")
    public String getVirtualProductFlag() {
        return virtualProductFlag;
    }

    @JsonProperty("VirtualProductFlag")
    public void setVirtualProductFlag(String virtualProductFlag) {
        this.virtualProductFlag = virtualProductFlag;
    }

    @JsonProperty("NLevelConnectFlag")
    public String getNLevelConnectFlag() {
        return nLevelConnectFlag;
    }

    @JsonProperty("NLevelConnectFlag")
    public void setNLevelConnectFlag(String nLevelConnectFlag) {
        this.nLevelConnectFlag = nLevelConnectFlag;
    }

    @JsonProperty("ProductLockedFlag")
    public String getProductLockedFlag() {
        return productLockedFlag;
    }

    @JsonProperty("ProductLockedFlag")
    public void setProductLockedFlag(String productLockedFlag) {
        this.productLockedFlag = productLockedFlag;
    }

    @JsonProperty("WorkflowStatusCode")
    public String getWorkflowStatusCode() {
        return workflowStatusCode;
    }

    @JsonProperty("WorkflowStatusCode")
    public void setWorkflowStatusCode(String workflowStatusCode) {
        this.workflowStatusCode = workflowStatusCode;
    }

    @JsonProperty("ChangeProductReasonCode")
    public String getChangeProductReasonCode() {
        return changeProductReasonCode;
    }

    @JsonProperty("ChangeProductReasonCode")
    public void setChangeProductReasonCode(String changeProductReasonCode) {
        this.changeProductReasonCode = changeProductReasonCode;
    }

    @JsonProperty("WorkflowStatusStateCode")
    public String getWorkflowStatusStateCode() {
        return workflowStatusStateCode;
    }

    @JsonProperty("WorkflowStatusStateCode")
    public void setWorkflowStatusStateCode(String workflowStatusStateCode) {
        this.workflowStatusStateCode = workflowStatusStateCode;
    }

    @JsonProperty("Show1MediaIdIm")
    public Integer getShow1MediaIdIm() {
        return show1MediaIdIm;
    }

    @JsonProperty("Show1MediaIdIm")
    public void setShow1MediaIdIm(Integer show1MediaIdIm) {
        this.show1MediaIdIm = show1MediaIdIm;
    }

    @JsonProperty("Show1MediaIdVd")
    public Integer getShow1MediaIdVd() {
        return show1MediaIdVd;
    }

    @JsonProperty("Show1MediaIdVd")
    public void setShow1MediaIdVd(Integer show1MediaIdVd) {
        this.show1MediaIdVd = show1MediaIdVd;
    }

    @JsonProperty("IsCustomProduct")
    public Boolean getIsCustomProduct() {
        return isCustomProduct;
    }

    @JsonProperty("IsCustomProduct")
    public void setIsCustomProduct(Boolean isCustomProduct) {
        this.isCustomProduct = isCustomProduct;
    }

    @JsonProperty("ShipperBillsByCode")
    public String getShipperBillsByCode() {
        return shipperBillsByCode;
    }

    @JsonProperty("ShipperBillsByCode")
    public void setShipperBillsByCode(String shipperBillsByCode) {
        this.shipperBillsByCode = shipperBillsByCode;
    }

    @JsonProperty("IsShippableInPlainBox")
    public Boolean getIsShippableInPlainBox() {
        return isShippableInPlainBox;
    }

    @JsonProperty("IsShippableInPlainBox")
    public void setIsShippableInPlainBox(Boolean isShippableInPlainBox) {
        this.isShippableInPlainBox = isShippableInPlainBox;
    }

    @JsonProperty("ProductDataSheet")
    public ProductDataSheet getProductDataSheet() {
        return productDataSheet;
    }

    @JsonProperty("ProductDataSheet")
    public void setProductDataSheet(ProductDataSheet productDataSheet) {
        this.productDataSheet = productDataSheet;
    }

    @JsonProperty("ProductInventoryLink")
    public ProductInventoryLink getProductInventoryLink() {
        return productInventoryLink;
    }

    @JsonProperty("ProductInventoryLink")
    public void setProductInventoryLink(ProductInventoryLink productInventoryLink) {
        this.productInventoryLink = productInventoryLink;
    }

    @JsonProperty("IsAvailableUnimprinted")
    public Boolean getIsAvailableUnimprinted() {
        return isAvailableUnimprinted;
    }

    @JsonProperty("IsAvailableUnimprinted")
    public void setIsAvailableUnimprinted(Boolean isAvailableUnimprinted) {
        this.isAvailableUnimprinted = isAvailableUnimprinted;
    }

    @JsonProperty("IsPersonalizationAvailable")
    public Boolean getIsPersonalizationAvailable() {
        return isPersonalizationAvailable;
    }

    @JsonProperty("IsPersonalizationAvailable")
    public void setIsPersonalizationAvailable(Boolean isPersonalizationAvailable) {
        this.isPersonalizationAvailable = isPersonalizationAvailable;
    }

    @JsonProperty("IsOrderLessThanMinimumAllowed")
    public Boolean getIsOrderLessThanMinimumAllowed() {
        return isOrderLessThanMinimumAllowed;
    }

    @JsonProperty("IsOrderLessThanMinimumAllowed")
    public void setIsOrderLessThanMinimumAllowed(Boolean isOrderLessThanMinimumAllowed) {
        this.isOrderLessThanMinimumAllowed = isOrderLessThanMinimumAllowed;
    }

    @JsonProperty("Disclaimer")
    public String getDisclaimer() {
        return disclaimer;
    }

    @JsonProperty("Disclaimer")
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    @JsonProperty("PublishDate")
    public String getPublishDate() {
        return publishDate;
    }

    @JsonProperty("PublishDate")
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @JsonProperty("LastPublishedDate")
    public String getLastPublishedDate() {
        return lastPublishedDate;
    }

    @JsonProperty("LastPublishedDate")
    public void setLastPublishedDate(String lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }

    @JsonProperty("LocationCode")
    public String getLocationCode() {
        return locationCode;
    }

    @JsonProperty("LocationCode")
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @JsonProperty("IsWIP")
    public Boolean getIsWIP() {
        return isWIP;
    }

    @JsonProperty("IsWIP")
    public void setIsWIP(Boolean isWIP) {
        this.isWIP = isWIP;
    }

    @JsonProperty("IsProductNumberBreakout")
    public Boolean getIsProductNumberBreakout() {
        return isProductNumberBreakout;
    }

    @JsonProperty("IsProductNumberBreakout")
    public void setIsProductNumberBreakout(Boolean isProductNumberBreakout) {
        this.isProductNumberBreakout = isProductNumberBreakout;
    }

    @JsonProperty("IsPriceBreakoutFlag")
    public Boolean getIsPriceBreakoutFlag() {
        return isPriceBreakoutFlag;
    }

    @JsonProperty("IsPriceBreakoutFlag")
    public void setIsPriceBreakoutFlag(Boolean isPriceBreakoutFlag) {
        this.isPriceBreakoutFlag = isPriceBreakoutFlag;
    }

    @JsonProperty("DistributorComments")
    public String getDistributorComments() {
        return distributorComments;
    }

    @JsonProperty("DistributorComments")
    public void setDistributorComments(String distributorComments) {
        this.distributorComments = distributorComments;
    }

    @JsonProperty("AdditionalInfo")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @JsonProperty("AdditionalInfo")
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @JsonProperty("IncludeAppOfferList")
    public String getIncludeAppOfferList() {
        return includeAppOfferList;
    }

    @JsonProperty("IncludeAppOfferList")
    public void setIncludeAppOfferList(String includeAppOfferList) {
        this.includeAppOfferList = includeAppOfferList;
    }

    @JsonProperty("ProductConfigurations")
    public List<ProductConfiguration> getProductConfigurations() {
        return productConfigurations;
    }

    @JsonProperty("ProductConfigurations")
    public void setProductConfigurations(List<ProductConfiguration> productConfigurations) {
        this.productConfigurations = productConfigurations;
    }

    @JsonProperty("ProductKeywords")
    public List<ProductKeywords> getProductKeywords() {
        return productKeywords;
    }

    @JsonProperty("ProductKeywords")
    public void setProductKeywords(List<ProductKeywords> productKeywords) {
        this.productKeywords = productKeywords;
    }

    @JsonProperty("SelectedProductCategories")
    public List<SelectedProductCategory> getSelectedProductCategories() {
        return selectedProductCategories;
    }

    @JsonProperty("SelectedProductCategories")
    public void setSelectedProductCategories(List<SelectedProductCategory> selectedProductCategories) {
        this.selectedProductCategories = selectedProductCategories;
    }

    @JsonProperty("SelectedLineNames")
    public List<SelectedLineNames> getSelectedLineNames() {
        return selectedLineNames;
    }

    @JsonProperty("SelectedLineNames")
    public void setSelectedLineNames(List<SelectedLineNames> selectedLineNames) {
        this.selectedLineNames = selectedLineNames;
    }

    @JsonProperty("SelectedComplianceCerts")
    public List<SelectedComplianceCert> getSelectedComplianceCerts() {
        return selectedComplianceCerts;
    }

    @JsonProperty("SelectedComplianceCerts")
    public void setSelectedComplianceCerts(List<SelectedComplianceCert> selectedComplianceCerts) {
        this.selectedComplianceCerts = selectedComplianceCerts;
    }

    @JsonProperty("SelectedSafetyWarnings")
    public List<SelectedSafetyWarnings> getSelectedSafetyWarnings() {
        return selectedSafetyWarnings;
    }

    @JsonProperty("SelectedSafetyWarnings")
    public void setSelectedSafetyWarnings(List<SelectedSafetyWarnings> selectedSafetyWarnings) {
        this.selectedSafetyWarnings = selectedSafetyWarnings;
    }

    @JsonProperty("ProductMediaItems")
    public List<ProductMediaItems> getProductMediaItems() {
        return productMediaItems;
    }

    @JsonProperty("ProductMediaItems")
    public void setProductMediaItems(List<ProductMediaItems> productMediaItems) {
        this.productMediaItems = productMediaItems;
    }

    @JsonProperty("ProductMediaCitations")
    public List<ProductMediaCitations> getProductMediaCitations() {
        return productMediaCitations;
    }

    @JsonProperty("ProductMediaCitations")
    public void setProductMediaCitations(List<ProductMediaCitations> productMediaCitations) {
        this.productMediaCitations = productMediaCitations;
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

    @JsonProperty("Relationships")
    public List<Relationship> getRelationships() {
        return relationships;
    }

    @JsonProperty("Relationships")
    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

  /*  @Override
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
    public String getProcessedURL()
    {
    	
    	String seprator = ",";
    	String valueforSeprator="";
    	ArrayList<ProductMediaItems> productMediaItems = (ArrayList<ProductMediaItems>) getProductMediaItems();
    	int mediaCntr=0;
    	for(ProductMediaItems crntproductMediaItems:productMediaItems)
    	{
    		if(mediaCntr!=0)
				valueforSeprator = seprator;
    		processedURL = processedURL +crntproductMediaItems.getMedia().getUrl()+valueforSeprator;
    	}
    	Iterator<ProductMediaItems> mediaIterator=productMediaItems.iterator();
    	while(mediaIterator.hasNext())
    	{
    		
			LinkedHashMap<String, ProductMediaItems> indexMediaItem = (LinkedHashMap<String, ProductMediaItems>) mediaIterator.next();
			LinkedHashMap<String, Object> media = (LinkedHashMap<String, Object>) indexMediaItem.get("Media");
			valueforSeprator="";
			if(mediaIterator.hasNext())
				valueforSeprator = seprator;
			
			processedURL = processedURL + media.get("Url")+valueforSeprator;
    	}
    	return processedURL;
    	
    }
	
*/
}
