package com.asi.service.product.vo;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="productDetail")
@XmlType(propOrder={    
		   "ID",
		   "asiProdNo",
		   "companyId",
		   "originalProductId",
		   "externalProductId",
		   "name",
		   "description",
		   "summary",
		   "category",
		   "keyword",
		   "itemPrice",
		   "imprints",
		   "workflowStatusCode",
		   "changeProductReasonCode",
		   "workflowStatusStateCode",
		   "statusCode",
		   "dataSourceId",
		   "productTypeCode",
		   "rushServiceFlag",
		   "sameDayRushFlag",
		   "newProductFlag",
		   "newProductExpirationDate",
		   "fullColorProcessFlag",
		   "visibleForAllUsersFlag",
		   "virtualProductFlag",
		   "nLevelConnectFlag",
		   "productLockedFlag",
		   "show1MediaIdIm",
		   "show1MediaIdVd",
		   "isCustomProduct",
		   "shipperBillsByCode",
		   "isShippableInPlainBox",
		   "productDataSheet",
		   "productInventoryLink",
		   "isAvailableUnimprinted",
		   "isPersonalizationAvailable",
		   "isOrderLessThanMinimumAllowed",
		   "priceConfirmationDate",
		   "disclaimer",
		   "publishDate",
		   "lastPublishedDate",
		   "locationCode",
		   "isWIP",
		   "isProductNumberBreakout",
		   "isPriceBreakoutFlag",
		   "includeAppOfferList",
		   "distributorComments",
		   "additionalInfo"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {
    
    public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	private Integer ID;
    
    private String productTypeCode;
    
    private Integer originalProductId;
    
    private String externalProductId;
    
    private Integer companyId;
    
    private String statusCode;
    
	private String dataSourceId="";
    
    private String name;
    
    private String description;
    
    private String summary;
    
    private String category;
    
    private String keyword;
    
    public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String rushServiceFlag;
    
    private String sameDayRushFlag;
    
    private String asiProdNo;
    
    private String newProductFlag;
    
    private String newProductExpirationDate;
    public String getNewProductExpirationDate() {
		return newProductExpirationDate;
	}

	public void setNewProductExpirationDate(String newProductExpirationDate) {
		this.newProductExpirationDate = newProductExpirationDate;
	}

	private String fullColorProcessFlag;
    
    private String visibleForAllUsersFlag;
    
    private String virtualProductFlag;
    
    private String nLevelConnectFlag;
    
    private String productLockedFlag;
    
    private String workflowStatusCode;
    
    private String changeProductReasonCode;
    
    private String workflowStatusStateCode;
    
    private Integer show1MediaIdIm;
    
    private Integer show1MediaIdVd;
    
    private Boolean isCustomProduct;
    
    private String shipperBillsByCode;
    
    private Boolean isShippableInPlainBox;
    
    private ProductDataSheet productDataSheet;
    
    private ProductInventoryLink productInventoryLink;
    
    private Boolean isAvailableUnimprinted;
    
    private Boolean isPersonalizationAvailable;
    
    private Boolean isOrderLessThanMinimumAllowed;
    
    private String priceConfirmationDate;
    
    public String getPriceConfirmationDate() {
		return priceConfirmationDate;
	}

	public void setPriceConfirmationDate(String priceConfirmationDate) {
		this.priceConfirmationDate = priceConfirmationDate;
	}

	private String disclaimer;
    
    private String publishDate;
    
    private String lastPublishedDate;
    
    private String locationCode;
    
    private Boolean isWIP;
   
    private Boolean isProductNumberBreakout;
  
    private Boolean isPriceBreakoutFlag;
   
    private String includeAppOfferList;
    
    public String getIncludeAppOfferList() {
		return includeAppOfferList;
	}

	public void setIncludeAppOfferList(String includeAppOfferList) {
		this.includeAppOfferList = includeAppOfferList;
	}

	private String distributorComments;
   
    private String additionalInfo;
    private Imprints imprints;


	public Imprints getImprints() {
		return imprints;
	}

	public void setImprints(Imprints imprints) {
		this.imprints = imprints;
	}

	private List<ItemPriceDetail> itemPrice;

	/**
	 * @return the itemPrices
	 */
	public List<ItemPriceDetail> getItemPrice() {
		return itemPrice;
	}

	/**
	 * @param itemPrices the itemPrices to set
	 */
	public void setItemPrice(List<ItemPriceDetail> itemPrice) {
		this.itemPrice = itemPrice;
	}

	/**
	 * @return the iD
	 */
	public Integer getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(Integer ID) {
		this.ID = ID;
	}

	/**
	 * @return the productTypeCode
	 */
	public String getProductTypeCode() {
		return productTypeCode;
	}

	/**
	 * @param productTypeCode the productTypeCode to set
	 */
	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	/**
	 * @return the originalProductId
	 */
	public Integer getOriginalProductId() {
		return originalProductId;
	}

	/**
	 * @param originalProductId the originalProductId to set
	 */
	public void setOriginalProductId(Integer originalProductId) {
		this.originalProductId = originalProductId;
	}

	/**
	 * @return the externalProductId
	 */
	public String getExternalProductId() {
		return externalProductId;
	}

	/**
	 * @param externalProductId the externalProductId to set
	 */
	public void setExternalProductId(String externalProductId) {
		this.externalProductId = externalProductId;
	}

	/**
	 * @return the companyId
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the rushServiceFlag
	 */
	public String getRushServiceFlag() {
		return rushServiceFlag;
	}

	/**
	 * @param rushServiceFlag the rushServiceFlag to set
	 */
	public void setRushServiceFlag(String rushServiceFlag) {
		this.rushServiceFlag = rushServiceFlag;
	}

	/**
	 * @return the sameDayRushFlag
	 */
	public String getSameDayRushFlag() {
		return sameDayRushFlag;
	}

	/**
	 * @param sameDayRushFlag the sameDayRushFlag to set
	 */
	public void setSameDayRushFlag(String sameDayRushFlag) {
		this.sameDayRushFlag = sameDayRushFlag;
	}

	/**
	 * @return the asiProdNo
	 */
	public String getAsiProdNo() {
		return asiProdNo;
	}

	/**
	 * @param asiProdNo the asiProdNo to set
	 */
	public void setAsiProdNo(String asiProdNo) {
		this.asiProdNo = asiProdNo;
	}

	/**
	 * @return the newProductFlag
	 */
	public String getNewProductFlag() {
		return newProductFlag;
	}

	/**
	 * @param newProductFlag the newProductFlag to set
	 */
	public void setNewProductFlag(String newProductFlag) {
		this.newProductFlag = newProductFlag;
	}

	/**
	 * @return the fullColorProcessFlag
	 */
	public String getFullColorProcessFlag() {
		return fullColorProcessFlag;
	}

	/**
	 * @param fullColorProcessFlag the fullColorProcessFlag to set
	 */
	public void setFullColorProcessFlag(String fullColorProcessFlag) {
		this.fullColorProcessFlag = fullColorProcessFlag;
	}

	/**
	 * @return the visibleForAllUsersFlag
	 */
	public String getVisibleForAllUsersFlag() {
		return visibleForAllUsersFlag;
	}

	/**
	 * @param visibleForAllUsersFlag the visibleForAllUsersFlag to set
	 */
	public void setVisibleForAllUsersFlag(String visibleForAllUsersFlag) {
		this.visibleForAllUsersFlag = visibleForAllUsersFlag;
	}

	/**
	 * @return the virtualProductFlag
	 */
	public String getVirtualProductFlag() {
		return virtualProductFlag;
	}

	/**
	 * @param virtualProductFlag the virtualProductFlag to set
	 */
	public void setVirtualProductFlag(String virtualProductFlag) {
		this.virtualProductFlag = virtualProductFlag;
	}

	/**
	 * @return the nLevelConnectFlag
	 */
	public String getnLevelConnectFlag() {
		return nLevelConnectFlag;
	}

	/**
	 * @param nLevelConnectFlag the nLevelConnectFlag to set
	 */
	public void setnLevelConnectFlag(String nLevelConnectFlag) {
		this.nLevelConnectFlag = nLevelConnectFlag;
	}

	/**
	 * @return the productLockedFlag
	 */
	public String getProductLockedFlag() {
		return productLockedFlag;
	}

	/**
	 * @param productLockedFlag the productLockedFlag to set
	 */
	public void setProductLockedFlag(String productLockedFlag) {
		this.productLockedFlag = productLockedFlag;
	}

	/**
	 * @return the workflowStatusCode
	 */
	public String getWorkflowStatusCode() {
		return workflowStatusCode;
	}

	/**
	 * @param workflowStatusCode the workflowStatusCode to set
	 */
	public void setWorkflowStatusCode(String workflowStatusCode) {
		this.workflowStatusCode = workflowStatusCode;
	}

	/**
	 * @return the changeProductReasonCode
	 */
	public String getChangeProductReasonCode() {
		return changeProductReasonCode;
	}

	/**
	 * @param changeProductReasonCode the changeProductReasonCode to set
	 */
	public void setChangeProductReasonCode(String changeProductReasonCode) {
		this.changeProductReasonCode = changeProductReasonCode;
	}

	/**
	 * @return the workflowStatusStateCode
	 */
	public String getWorkflowStatusStateCode() {
		return workflowStatusStateCode;
	}

	/**
	 * @param workflowStatusStateCode the workflowStatusStateCode to set
	 */
	public void setWorkflowStatusStateCode(String workflowStatusStateCode) {
		this.workflowStatusStateCode = workflowStatusStateCode;
	}

	/**
	 * @return the show1MediaIdIm
	 */
	public Integer getShow1MediaIdIm() {
		return show1MediaIdIm;
	}

	/**
	 * @param show1MediaIdIm the show1MediaIdIm to set
	 */
	public void setShow1MediaIdIm(Integer show1MediaIdIm) {
		this.show1MediaIdIm = show1MediaIdIm;
	}

	/**
	 * @return the show1MediaIdVd
	 */
	public Integer getShow1MediaIdVd() {
		return show1MediaIdVd;
	}

	/**
	 * @param show1MediaIdVd the show1MediaIdVd to set
	 */
	public void setShow1MediaIdVd(Integer show1MediaIdVd) {
		this.show1MediaIdVd = show1MediaIdVd;
	}

	/**
	 * @return the isCustomProduct
	 */
	public Boolean getIsCustomProduct() {
		return isCustomProduct;
	}

	/**
	 * @param isCustomProduct the isCustomProduct to set
	 */
	public void setIsCustomProduct(Boolean isCustomProduct) {
		this.isCustomProduct = isCustomProduct;
	}

	/**
	 * @return the shipperBillsByCode
	 */
	public String getShipperBillsByCode() {
		return shipperBillsByCode;
	}

	/**
	 * @param shipperBillsByCode the shipperBillsByCode to set
	 */
	public void setShipperBillsByCode(String shipperBillsByCode) {
		this.shipperBillsByCode = shipperBillsByCode;
	}

	/**
	 * @return the isShippableInPlainBox
	 */
	public Boolean getIsShippableInPlainBox() {
		return isShippableInPlainBox;
	}

	/**
	 * @param isShippableInPlainBox the isShippableInPlainBox to set
	 */
	public void setIsShippableInPlainBox(Boolean isShippableInPlainBox) {
		this.isShippableInPlainBox = isShippableInPlainBox;
	}

	/**
	 * @return the productDataSheet
	 */
	public ProductDataSheet getProductDataSheet() {
		return productDataSheet;
	}

	/**
	 * @param productDataSheet the productDataSheet to set
	 */
	public void setProductDataSheet(ProductDataSheet productDataSheet) {
		this.productDataSheet = productDataSheet;
	}

	/**
	 * @return the productInventoryLink
	 */
	public ProductInventoryLink getProductInventoryLink() {
		return productInventoryLink;
	}

	/**
	 * @param productInventoryLink the productInventoryLink to set
	 */
	public void setProductInventoryLink(ProductInventoryLink productInventoryLink) {
		this.productInventoryLink = productInventoryLink;
	}

	/**
	 * @return the isAvailableUnimprinted
	 */
	public Boolean getIsAvailableUnimprinted() {
		return isAvailableUnimprinted;
	}

	/**
	 * @param isAvailableUnimprinted the isAvailableUnimprinted to set
	 */
	public void setIsAvailableUnimprinted(Boolean isAvailableUnimprinted) {
		this.isAvailableUnimprinted = isAvailableUnimprinted;
	}

	/**
	 * @return the isPersonalizationAvailable
	 */
	public Boolean getIsPersonalizationAvailable() {
		return isPersonalizationAvailable;
	}

	/**
	 * @param isPersonalizationAvailable the isPersonalizationAvailable to set
	 */
	public void setIsPersonalizationAvailable(Boolean isPersonalizationAvailable) {
		this.isPersonalizationAvailable = isPersonalizationAvailable;
	}

	/**
	 * @return the isOrderLessThanMinimumAllowed
	 */
	public Boolean getIsOrderLessThanMinimumAllowed() {
		return isOrderLessThanMinimumAllowed;
	}

	/**
	 * @param isOrderLessThanMinimumAllowed the isOrderLessThanMinimumAllowed to set
	 */
	public void setIsOrderLessThanMinimumAllowed(
			Boolean isOrderLessThanMinimumAllowed) {
		this.isOrderLessThanMinimumAllowed = isOrderLessThanMinimumAllowed;
	}

	/**
	 * @return the disclaimer
	 */
	public String getDisclaimer() {
		return disclaimer;
	}

	/**
	 * @param disclaimer the disclaimer to set
	 */
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	/**
	 * @return the publishDate
	 */
	public String getPublishDate() {
		return publishDate;
	}

	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	/**
	 * @return the lastPublishedDate
	 */
	public String getLastPublishedDate() {
		return lastPublishedDate;
	}

	/**
	 * @param lastPublishedDate the lastPublishedDate to set
	 */
	public void setLastPublishedDate(String lastPublishedDate) {
		this.lastPublishedDate = lastPublishedDate;
	}

	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}

	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	/**
	 * @return the isWIP
	 */
	public Boolean getIsWIP() {
		return isWIP;
	}

	/**
	 * @param isWIP the isWIP to set
	 */
	public void setIsWIP(Boolean isWIP) {
		this.isWIP = isWIP;
	}

	/**
	 * @return the isProductNumberBreakout
	 */
	public Boolean getIsProductNumberBreakout() {
		return isProductNumberBreakout;
	}

	/**
	 * @param isProductNumberBreakout the isProductNumberBreakout to set
	 */
	public void setIsProductNumberBreakout(Boolean isProductNumberBreakout) {
		this.isProductNumberBreakout = isProductNumberBreakout;
	}

	/**
	 * @return the isPriceBreakoutFlag
	 */
	public Boolean getIsPriceBreakoutFlag() {
		return isPriceBreakoutFlag;
	}

	/**
	 * @param isPriceBreakoutFlag the isPriceBreakoutFlag to set
	 */
	public void setIsPriceBreakoutFlag(Boolean isPriceBreakoutFlag) {
		this.isPriceBreakoutFlag = isPriceBreakoutFlag;
	}

	/**
	 * @return the distributorComments
	 */
	public String getDistributorComments() {
		return distributorComments;
	}

	/**
	 * @param distributorComments the distributorComments to set
	 */
	public void setDistributorComments(String distributorComments) {
		this.distributorComments = distributorComments;
	}

	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
   
  
}
