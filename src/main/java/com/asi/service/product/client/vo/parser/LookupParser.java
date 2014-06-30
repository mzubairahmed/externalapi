package com.asi.service.product.client.vo.parser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;
import com.asi.service.product.client.vo.SelectedProductCategory;
import com.asi.service.product.client.vo.colors.Color;
import com.asi.service.product.vo.DataSheet;
import com.asi.service.product.vo.InventoryLink;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;
import com.asi.service.product.vo.ProductConfigurations;

public class LookupParser {
	

	@Autowired LookupValuesClient lookupClient;
	
	
	
	/**
     * 
     * Gets the parsed size data (in Map format) from sizeResponse JSON
     * 
     * @param sizesWSResponse is the JSON data which contains the size details, criteria details etc.. 
     * @param attribute is the string to lookup for existence  
     * @param DimensionType is the type of dimension
     */
	@SuppressWarnings("rawtypes")
	public HashMap getSizesResponse(String DimensionType,String attribute) {
		DimensionType=DimensionType.trim();
		attribute=attribute.trim();
		HashMap returnValue = null;
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getCriteriaAttributesFromLookup(lookupClient.getLookupcriteriaAttributeURL().toString());
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (attribute.equalsIgnoreCase(currentCriteriaAttribute.get("ID")
					.toString())
					&& DimensionType.equalsIgnoreCase(currentCriteriaAttribute
							.get("CriteriaCode").toString())) {
				returnValue = currentCriteriaAttribute;
				break;
			}
		}
		return returnValue;
	}
	@SuppressWarnings("rawtypes")
	public String getCriteriaAttributeId(String DimensionType) {
		DimensionType=DimensionType.trim();
		String returnValue = null;
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getCriteriaAttributesFromLookup(lookupClient.getLookupcriteriaAttributeURL().toString());
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (DimensionType.equalsIgnoreCase(currentCriteriaAttribute
							.get("CriteriaCode").toString())) {
				returnValue = currentCriteriaAttribute.get("ID").toString();
				break;
			}
		}
		return returnValue;
	}

	private String getSizesElementValue(String criteriaCode,
			String criteriaAttributeId, String unitValue,
			String unitOfMeasureCode) {
		String elementValue="";
		String elementName="";
		String elementUnit="";
		@SuppressWarnings("rawtypes")
		HashMap sourceMap=getSizesResponse(criteriaCode, criteriaAttributeId);
		elementName=sourceMap.get("DisplayName").toString();
		if(null!=sourceMap && null!=sourceMap.get("UnitsOfMeasure") && !sourceMap.get("UnitsOfMeasure").toString().isEmpty())
		{
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> unitOfMeasures = (ArrayList<LinkedHashMap>) sourceMap.get("UnitsOfMeasure");
		for(@SuppressWarnings("rawtypes") Map codeValueGrpsMap :unitOfMeasures) {
			// LOGGER.info(codeValueGrpsMap.toString());
			if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(unitOfMeasureCode)) {
				elementUnit = (String) codeValueGrpsMap.get("Format");
			}
		}
		}
		elementValue=!elementName.isEmpty()?elementName+":"+unitValue+":"+elementUnit:"";
		return elementValue;
	}
	public String getUnitsOfMeasureCode(String criteriaCode,
			String criteriaAttributeId, String units) {
		String elementUnit="";
		@SuppressWarnings("rawtypes")
		HashMap sourceMap=getSizesResponse(criteriaCode, criteriaAttributeId);
		if(null!=sourceMap && null!=sourceMap.get("UnitsOfMeasure") && !sourceMap.get("UnitsOfMeasure").toString().isEmpty())
		{
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> unitOfMeasures = (ArrayList<LinkedHashMap>) sourceMap.get("UnitsOfMeasure");
		for(@SuppressWarnings("rawtypes") Map codeValueGrpsMap :unitOfMeasures) {
			// LOGGER.info(codeValueGrpsMap.toString());
			if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(units)) {
				elementUnit = (String) codeValueGrpsMap.get("Format");
				break;
			}
		}
		}
	//	elementValue=!elementName.isEmpty()?elementName+":"+unitValue+":"+elementUnit:"";
		return elementUnit;
	}

	public LookupValuesClient getLookupClient() {
		return lookupClient;
	}

	public void setLookupClient(LookupValuesClient lookupClient) {
		this.lookupClient = lookupClient;
	}

	@SuppressWarnings("rawtypes")
	public String getValueString(ArrayList<?> value,String criteriaCode) {
		String valueStr="";
		String currentValue="";
		LinkedHashMap curntHashMap=null;
		if(null!=value){
		for(Object currentObj:value)
		{
			curntHashMap =(LinkedHashMap)currentObj;
			if(criteriaCode.equalsIgnoreCase("DIMS")){
				currentValue=getSizesElementValue(criteriaCode,curntHashMap.get("CriteriaAttributeId").toString(),curntHashMap.get("UnitValue").toString(),curntHashMap.get("UnitOfMeasureCode").toString());
			} else {
				currentValue=getElementValue(criteriaCode,curntHashMap.get("CriteriaAttributeId").toString(),curntHashMap.get("UnitValue").toString(),curntHashMap.get("UnitOfMeasureCode").toString());
			}
			valueStr=valueStr.equals("")?currentValue:valueStr+";"+currentValue;
			currentValue="";
		}
		}
		return valueStr;
	}

	public String getElementValue(String criteriaCode,String criteriaAttributeId,String unitValue, String unitOfMeasureCode) {
		String elementUnit="";
		String elementValue="";
		@SuppressWarnings("rawtypes")
		HashMap sourceMap=getSizesResponse(criteriaCode, criteriaAttributeId);
		if(null!=sourceMap && null!=sourceMap.get("UnitsOfMeasure") && !sourceMap.get("UnitsOfMeasure").toString().isEmpty())
		{
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> unitOfMeasures = (ArrayList<LinkedHashMap>) sourceMap.get("UnitsOfMeasure");
		for(@SuppressWarnings("rawtypes") Map codeValueGrpsMap :unitOfMeasures) {
			if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(unitOfMeasureCode)) {
				elementUnit = (String) codeValueGrpsMap.get("Format");
			}
		}
		}
		elementValue=(!unitValue.isEmpty() && !elementUnit.isEmpty())?unitValue+":"+elementUnit:"";
		return elementValue;
	}

	public String getCategoryCodeByName(String crntCategory) {
		ArrayList<LinkedHashMap<String,String>> categoriesList = lookupClient.getCategoriesFromLookup(lookupClient.getLookupCategoryURL());
		String categoryStr=null;
		for(LinkedHashMap<String,String> currentCategory:categoriesList)
		{
			if(null!=currentCategory.get("Name") && currentCategory.get("Name").toString().equalsIgnoreCase(crntCategory))
			{
				categoryStr=String.valueOf(currentCategory.get("Code"));
				break;
			}
		}
		return categoryStr;
	}
	public String getCategoryNameByCode(String crntCategory) {
		ArrayList<LinkedHashMap<String,String>> categoriesList = lookupClient.getCategoriesFromLookup(lookupClient.getLookupCategoryURL());
		String categoryStr=null;
		for(LinkedHashMap<String,String> currentCategory:categoriesList)
		{
			if(null!=currentCategory.get("Code") && currentCategory.get("Code").toString().equalsIgnoreCase(crntCategory))
			{
				categoryStr=String.valueOf(currentCategory.get("Name"));
				break;
			}
		}
		return categoryStr;
	}
	public String getArtworkNameByCode(String artworkCode) {
		ArrayList<LinkedHashMap> artworkList = lookupClient.getArtworksFromLookup(lookupClient.getLookupArtworkURL());
		String artworkStr=null;
		ArrayList<LinkedHashMap> codeValueGrps =null;
		ArrayList<LinkedHashMap> setCodeValueGrps =null;
		for(LinkedHashMap currentArtwork:artworkList)
		{
			if(null!=currentArtwork.get("Code") && currentArtwork.get("Code").equals("ARTW")){
			codeValueGrps = (ArrayList<LinkedHashMap>) currentArtwork.get("CodeValueGroups");
			if(null!=codeValueGrps){
				for(LinkedHashMap codeValueGroup:codeValueGrps){
				setCodeValueGrps=(ArrayList<LinkedHashMap>) codeValueGroup.get("SetCodeValues");
				if(null!=setCodeValueGrps){
					for(LinkedHashMap setCodeValue:setCodeValueGrps){
						if(null!=setCodeValue.get("ID") && setCodeValue.get("ID").toString().equalsIgnoreCase(artworkCode))
						{
							artworkStr=String.valueOf(setCodeValue.get("CodeValue"));
							break;					
						}
					}
				}				
			}}
			}
		}
		return artworkStr;
	}
	public String getColorCodeByName(String colorName) {
		ArrayList<LinkedHashMap> colorsList = lookupClient.getColorFromLookup(lookupClient.getLookupColorURL());
		String colorCode=null;
		if(!colorName.trim().contains(" "))
		{
			colorName=colorName.trim();
			colorName="Medium "+colorName;
		}
		String colorCodeOther=null;
		ArrayList<LinkedHashMap> codeValueGrps =null;
		ArrayList<LinkedHashMap> setCodeValueGrps =null;
		for(LinkedHashMap currentArtwork:colorsList)
		{
			codeValueGrps = (ArrayList<LinkedHashMap>) currentArtwork.get("CodeValueGroups");
			if(null!=codeValueGrps){
				for(LinkedHashMap codeValueGroup:codeValueGrps){
				setCodeValueGrps=(ArrayList<LinkedHashMap>) codeValueGroup.get("SetCodeValues");
				if(null!=setCodeValueGrps){
					for(LinkedHashMap setCodeValue:setCodeValueGrps){
						if(null!=setCodeValue.get("CodeValue") && setCodeValue.get("CodeValue").toString().equalsIgnoreCase(colorName))
						{
							colorCode=String.valueOf(setCodeValue.get("ID"));
							break;
						}
						if(null!=setCodeValue.get("CodeValue") && setCodeValue.get("CodeValue").toString().equalsIgnoreCase("Unclassified/Other"))
						{
							colorCodeOther=String.valueOf(setCodeValue.get("ID"));
						}
					}
					}
				}
			}
		/*for(LinkedHashMap currentColor:colorsList){
		 	if(currentColor.get("DisplayName").toString().equalsIgnoreCase(colorName)){
				colorCode=currentColor.get("Code").toString();				
			}*/
		}
				if(null!=colorCode) colorCode=colorCodeOther;
		return colorCode;
	}
	public String getColorNameByCode(String colorCode) {
		ArrayList<LinkedHashMap> colorsList = lookupClient.getColorFromLookup(lookupClient.getLookupColorURL());
		String colorName=null;
		for(LinkedHashMap currentColor:colorsList){
			if(currentColor.get("Code").toString().equalsIgnoreCase(colorCode)){
				colorName=currentColor.get("DisplayName").toString();				
			}
		}
		return colorName;
	}
	public String getArtworkCodeByName(String artworkCode) {
		ArrayList<LinkedHashMap> artworkList = lookupClient.getArtworksFromLookup(lookupClient.getLookupArtworkURL());
		String artworkStr=null;
		String otherArtwk=null;
		ArrayList<LinkedHashMap> codeValueGrps =null;
		ArrayList<LinkedHashMap> setCodeValueGrps =null;
		for(LinkedHashMap currentArtwork:artworkList)
		{
			if(null!=currentArtwork.get("Code") && currentArtwork.get("Code").equals("ARTW")){
			codeValueGrps = (ArrayList<LinkedHashMap>) currentArtwork.get("CodeValueGroups");
			if(null!=codeValueGrps){
				for(LinkedHashMap codeValueGroup:codeValueGrps){
				setCodeValueGrps=(ArrayList<LinkedHashMap>) codeValueGroup.get("SetCodeValues");
				if(null!=setCodeValueGrps){
					for(LinkedHashMap setCodeValue:setCodeValueGrps){
						if(null!=setCodeValue.get("CodeValue") && setCodeValue.get("CodeValue").toString().equalsIgnoreCase("Other"))
						{
							otherArtwk=String.valueOf(setCodeValue.get("ID"));
						}
						if(null!=setCodeValue.get("CodeValue") && setCodeValue.get("CodeValue").toString().equalsIgnoreCase(artworkCode))
						{
							artworkStr=String.valueOf(setCodeValue.get("ID"));
							break;					
						}
					}
				}				
			}}
			}
		}
		if(null==artworkStr && null!=otherArtwk) artworkStr=otherArtwk;
		return artworkStr;
	}
	public String getImprintCodeByName(String imprintName) {
		ArrayList<LinkedHashMap> artworkList = lookupClient.getImprintFromLookup(lookupClient.getLookupImprintURL());
		String artworkStr=null;
		String otherArtwk=null;
		ArrayList<LinkedHashMap> codeValueGrps =null;
		ArrayList<LinkedHashMap> setCodeValueGrps =null;
		for(LinkedHashMap currentArtwork:artworkList)
		{
			if(null!=currentArtwork.get("CodeValue") && currentArtwork.get("CodeValue").toString().equalsIgnoreCase("Other"))
			{
				otherArtwk=String.valueOf(currentArtwork.get("ID"));
			}
			if(null!=currentArtwork.get("CodeValue") && currentArtwork.get("CodeValue").toString().equalsIgnoreCase(imprintName)){
				artworkStr=String.valueOf(currentArtwork.get("ID"));
			}
		}
		if(null==artworkStr && null!=otherArtwk) artworkStr=otherArtwk;
		return artworkStr;
	}
	public Product setProductCategory(ProductDetail productDetail, Product product) {
		List<SelectedProductCategory> productCategoriesList=productDetail.getSelectedProductCategories();
		String finalCategory="";
		int cntr=0;
		if(null!=productCategoriesList && productCategoriesList.size()>0)
		{
			for(SelectedProductCategory currentCategory:productCategoriesList)
			{
				if(cntr==0)
					finalCategory=getCategoryNameByCode(currentCategory.getCode());
				else
					finalCategory+=", "+getCategoryNameByCode(currentCategory.getCode());
				cntr++;
			}
			product.setCategory(finalCategory);
		}
		return product;		
	}

	public com.asi.service.product.client.vo.Product setProductKeyWords(
			com.asi.service.product.client.vo.Product productToUpdate, Product srcProduct) {
		String keywords=srcProduct.getKeyword();
		if(null!=keywords){
		String[] keywordlist=keywords.split(",");
		com.asi.service.product.client.vo.ProductKeywords curntProductKeyword=null;
		int keyworkCntr=0;
		com.asi.service.product.client.vo.ProductKeywords[] productKeywordsAry={};
		if(null!=keywordlist && keywordlist.length>0)
		{
			productKeywordsAry=new com.asi.service.product.client.vo.ProductKeywords[keywordlist.length];
			for(String crntKeyword:keywordlist)
			{
				curntProductKeyword=new com.asi.service.product.client.vo.ProductKeywords();
				curntProductKeyword.setId("0");
				curntProductKeyword.setMarketSegmentCode("USAALL");
				curntProductKeyword.setProductId(String.valueOf(srcProduct.getID()));
				curntProductKeyword.setTypeCode("HIDD");
				curntProductKeyword.setValue(crntKeyword);
				productKeywordsAry[keyworkCntr++]=curntProductKeyword;
				//productToUpdate.getProductKeywords().add(curntProductKeyword);
			}
			productToUpdate.setProductKeywords(productKeywordsAry);
		}
		}
		return productToUpdate;
	}

	public Product setProductServiceKeywords(ProductDetail productDetail,
			Product product) {
		List<com.asi.service.product.client.vo.ProductKeywords> productKeywordsList=productDetail.getProductKeywords();
		String finalProductKeyword="";
		int cntr=0;
		if(null!=productKeywordsList && productKeywordsList.size()>0)
		{
			for(com.asi.service.product.client.vo.ProductKeywords crntProductKeywords : productKeywordsList)
			{
				if(cntr==0)
					finalProductKeyword=crntProductKeywords.getValue();
				else
					finalProductKeyword+=", "+crntProductKeywords.getValue();
				cntr++;
			}
			product.setKeyword(finalProductKeyword);
		}
		return product;		
	}

	public Product setProductServiceDataSheet(ProductDetail productDetail,
			Product product) {
		com.asi.service.product.client.vo.ProductDataSheet crntDataSheet=productDetail.getProductDataSheet();
		if(null!=crntDataSheet){
			DataSheet productDataSheet=new DataSheet();
			productDataSheet.setProductId(Integer.valueOf(productDetail.getID()));
			productDataSheet.setCompanyId(Integer.valueOf(productDetail.getCompanyId()));
			productDataSheet.setId(Integer.valueOf(crntDataSheet.getId()));
			productDataSheet.setUrl(crntDataSheet.getUrl());
			product.setProductDataSheet(productDataSheet);
		}
		return product;
	}
	public Product setProductServiceInventoryLink(ProductDetail productDetail,
			Product product) {
		ProductInventoryLink crntInventoryLink=productDetail.getProductInventoryLink();
		{
			InventoryLink productInventoryLink=new InventoryLink();
			productInventoryLink.setProductId(Integer.valueOf(productDetail.getID()));
			productInventoryLink.setCompanyId(Integer.valueOf(productDetail.getCompanyId()));
			productInventoryLink.setId(Integer.valueOf(crntInventoryLink.getId()));
			productInventoryLink.setUrl(crntInventoryLink.getUrl());
			product.setProductInventoryLink(productInventoryLink);
		}
		return product;
	}

	public Product setProductServiceBasePriceInfo(ProductDetail productDetail,
			Product product) {
		List<com.asi.service.product.client.vo.PriceGrid> priceGridList=productDetail.getPriceGrids();
		if(null!=priceGridList && priceGridList.size()>0)
		{
			List<ItemPriceDetail> priceDetails=new ArrayList<>();
			
			PriceDetail priceDetail=null;
			List<PriceDetail> priceDetailList=new ArrayList<>();
			List<Price> pricesList=null;
			ItemPriceDetail crntPriceDetail=null;
			for(com.asi.service.product.client.vo.PriceGrid crntPriceGrid:priceGridList)
			{
				crntPriceDetail=new ItemPriceDetail();
				crntPriceDetail.setPriceID(String.valueOf(crntPriceGrid.getID()));
				crntPriceDetail.setPriceName(productDetail.getName());
				//crntPriceDetail.setPriceType(ItemPriceDetail.PRICE_Type);
				crntPriceDetail.setPriceIncludes(crntPriceGrid.getPriceIncludes());
				crntPriceDetail.setPriceUponRequest(false);
				pricesList=crntPriceGrid.getPrices();
				for(Price crntPrice:pricesList)
				{
					priceDetail=new PriceDetail();
					priceDetail.setPrice(new Double(2));
					priceDetail.setQuanty(crntPrice.getQuantity());
					priceDetail.setDiscount(crntPrice.getDiscountRate().getIndustryDiscountCode());
					priceDetail.setItemsPerUnit(crntPrice.getItemsPerUnit());
					priceDetail.setSequenceNumber(crntPrice.getSequenceNumber());
					priceDetail.setLowQuantity(crntPrice.getLowQuantity());
					priceDetail.setHighQuantity(crntPrice.getHighQuantity());
					priceDetail.setNetCost(crntPrice.getNetCost());
					priceDetail.setMaxDecimalPlaces(crntPrice.getMaxDecimalPlaces());
					priceDetail.setPriceUnitName(crntPrice.getPriceUnitName());
					priceDetail.setItemsPerUnitBy(crntPrice.getPriceUnit().getDisplayName());
					priceDetailList.add(priceDetail);
				}	
				crntPriceDetail.setPriceDetails(priceDetailList);
				priceDetails.add(crntPriceDetail);
			}
			product.setItemPrice(priceDetails);
		}
		return product;
	}

	public Product setProductConfigurations(ProductDetail productDetail,
			Product product) {
		List<ProductConfiguration> srcProductConfigList=productDetail.getProductConfigurations();
		ProductConfigurations[] targetProductConfigList={};
		ProductConfigurations currentProductConfig=null;
		com.asi.service.product.vo.ProductCriteriaSets[] productCriteriaSetsAry=null;
		com.asi.service.product.vo.ProductCriteriaSets currentProductCriteriaSets=null;
		com.asi.service.product.vo.CriteriaSetValues currentCriteriaSetValues=null;
		List<com.asi.service.product.vo.CriteriaSetValues> currentCriteriaSetValuesAry=null;
		List<CriteriaSetValues> clientCriteriaSetValuesAry=null;
		ArrayList<ProductCriteriaSets> productCriteriaSetsList=null;
		if(null!=srcProductConfigList){
			int productConfigCntr=0;
			int productCriteriaSetCntr=0;
			targetProductConfigList=new ProductConfigurations[srcProductConfigList.size()];
			for(ProductConfiguration crntProductConfiguration:srcProductConfigList)
			{
				currentProductConfig=new ProductConfigurations();
				productCriteriaSetsAry=new com.asi.service.product.vo.ProductCriteriaSets[crntProductConfiguration.getProductCriteriaSets().size()];
				productCriteriaSetsList=new ArrayList<>();
				currentProductConfig.setId(String.valueOf(crntProductConfiguration.getID()));
				currentProductConfig.setIsDefault(String.valueOf(crntProductConfiguration.getIsDefault()));
				currentProductConfig.setProductId(String.valueOf(crntProductConfiguration.getProductId()));
				productCriteriaSetsList=(ArrayList<ProductCriteriaSets>) crntProductConfiguration.getProductCriteriaSets();
				//productCriteriaSetsList=new ArrayList(Arrays.asList(crntProductConfiguration.getProductCriteriaSets()));
				for(com.asi.service.product.client.vo.ProductCriteriaSets crntProductCriteriaSet:productCriteriaSetsList){
					currentProductCriteriaSets=new com.asi.service.product.vo.ProductCriteriaSets();
						currentProductCriteriaSets.setCompanyId(crntProductCriteriaSet.getCompanyId());
						currentProductCriteriaSets.setProductId(crntProductCriteriaSet.getProductId());
						currentProductCriteriaSets.setConfigId(crntProductCriteriaSet.getConfigId());
						currentProductCriteriaSets.setCriteriaCode(crntProductCriteriaSet.getCriteriaCode());
						currentProductCriteriaSets.setCriteriaDetail(crntProductCriteriaSet.getCriteriaDetail());
						currentProductCriteriaSets.setCriteriaSetId(crntProductCriteriaSet.getCriteriaSetId());
						currentProductCriteriaSets.setParentCriteriaSetId(crntProductCriteriaSet.getParentCriteriaSetId());
						currentProductCriteriaSets.setDescription(crntProductCriteriaSet.getDescription());
						currentProductCriteriaSets.setIsBase(crntProductCriteriaSet.getIsBase());
						currentProductCriteriaSets.setIsRequiredForOrder(crntProductCriteriaSet.getIsRequiredForOrder());
						currentProductCriteriaSets.setIsMultipleChoiceAllowed(crntProductCriteriaSet.getIsMultipleChoiceAllowed());
						currentProductCriteriaSets.setIsTemplate(crntProductCriteriaSet.getIsTemplate());
						currentProductCriteriaSets.setOrderDetail(crntProductCriteriaSet.getOrderDetail());
						currentProductCriteriaSets.setIsDefaultConfiguration(crntProductCriteriaSet.getIsDefaultConfiguration());
						currentProductCriteriaSets.setDisplayProductNumber(crntProductCriteriaSet.getDisplayProductNumber());
						currentProductCriteriaSets.setDisplayOptionName(crntProductCriteriaSet.getDisplayOptionName());
						currentProductCriteriaSets.setIsBrokenOutOn(crntProductCriteriaSet.getIsBrokenOutOn());
						clientCriteriaSetValuesAry=crntProductCriteriaSet.getCriteriaSetValues();						
						currentCriteriaSetValuesAry=getCurrentCriteriaSetValues(clientCriteriaSetValuesAry);
						currentProductCriteriaSets.setCriteriaSetValues(currentCriteriaSetValuesAry);
					productCriteriaSetsAry[productCriteriaSetCntr++]=currentProductCriteriaSets;
				}
				currentProductConfig.setProductCriteriaSets(productCriteriaSetsAry);
				targetProductConfigList[productConfigCntr++]=currentProductConfig;
				//BeanUtils.copyProperties(targetProductConfigList[productConfigCntr++], crntProductConfiguration);
			}
			
		}	
		product.setProductConfigurations(targetProductConfigList);
		return product;
	}

	private List<com.asi.service.product.vo.CriteriaSetValues> getCurrentCriteriaSetValues(
			List<CriteriaSetValues> clientCriteriaSetValuesAry) {
		com.asi.service.product.vo.CriteriaSetValues currentCriteriaSetValues=null;
		List<com.asi.service.product.vo.CriteriaSetValues> currentCriteriaSetValuesAry=new ArrayList<>();
		int childCriteriaCodesCntr=0;
		//currentCriteriaSetValuesAry=new com.asi.service.product.vo.CriteriaSetValues[clientCriteriaSetValuesAry.length];
		for(CriteriaSetValues crntclientCriteriaSetValues:clientCriteriaSetValuesAry){
			currentCriteriaSetValues=new com.asi.service.product.vo.CriteriaSetValues();
			currentCriteriaSetValues.setId(crntclientCriteriaSetValues.getId());
			currentCriteriaSetValues.setBaseLookupValue(crntclientCriteriaSetValues.getBaseLookupValue());
			currentCriteriaSetValues.setCriteriaCode(crntclientCriteriaSetValues.getCriteriaCode());
			currentCriteriaSetValues.setCriteriaSetId(crntclientCriteriaSetValues.getCriteriaSetId());
			currentCriteriaSetValues.setValueTypeCode(crntclientCriteriaSetValues.getValueTypeCode());
			currentCriteriaSetValues.setCriteriaValueDetail(crntclientCriteriaSetValues.getCriteriaValueDetail());
			currentCriteriaSetValues.setIsSubset(crntclientCriteriaSetValues.getIsSubset());
			currentCriteriaSetValues.setIsSetValueMeasurement(crntclientCriteriaSetValues.getIsSetValueMeasurement());
			currentCriteriaSetValues.setProductNumber(crntclientCriteriaSetValues.getProductNumber());
			currentCriteriaSetValues.setValue(crntclientCriteriaSetValues.getValue());
			currentCriteriaSetValues.setSubSets(crntclientCriteriaSetValues.getSubSets());
			currentCriteriaSetValues.setDisplaySequence(crntclientCriteriaSetValues.getDisplaySequence());
			currentCriteriaSetValues.setCriteriaSetCodeValues(crntclientCriteriaSetValues.getCriteriaSetCodeValues());
			currentCriteriaSetValuesAry.add(currentCriteriaSetValues);
		}
		return currentCriteriaSetValuesAry;
	}

	public String getImprintNameByCode(String setCodeValueId) {
		ArrayList<LinkedHashMap> artworkList = lookupClient.getImprintFromLookup(lookupClient.getLookupImprintURL());
		String artworkStr=null;
		String otherArtwk=null;
		ArrayList<LinkedHashMap> codeValueGrps =null;
		ArrayList<LinkedHashMap> setCodeValueGrps =null;
		for(LinkedHashMap currentArtwork:artworkList)
		{
			if(null!=currentArtwork.get("ID") && currentArtwork.get("ID").toString().equalsIgnoreCase(setCodeValueId)){
				artworkStr=String.valueOf(currentArtwork.get("CodeValue"));
				break;
			}
		}
		if(null==artworkStr && null!=otherArtwk) artworkStr=otherArtwk;
		return artworkStr;
	}

	public String getMinOrderCodeByName(String methodName) {
		// TODO Auto-generated method stub
		return "750000013";
	}
	public Product setProductServiceColor(ProductDetail productDetail,
			Product product) {
		String productColor="";
		int colorCntr=0;
		List<ProductCriteriaSets> currentProductCriteriaSets=productDetail.getProductConfigurations().get(0).getProductCriteriaSets();
		for(ProductCriteriaSets currentProductCritieriaSet:currentProductCriteriaSets){
			if(currentProductCritieriaSet.getCriteriaCode().equalsIgnoreCase("PRCL")){
				for(CriteriaSetValues currentCriteriaSetValue:currentProductCritieriaSet.getCriteriaSetValues()){
					if(currentCriteriaSetValue.getValue() instanceof String && !currentCriteriaSetValue.getValue().toString().isEmpty()){
						productColor=(colorCntr==0)?currentCriteriaSetValue.getValue().toString():productColor+", "+currentCriteriaSetValue.getValue().toString();
						colorCntr++;
					}else{
						productColor=(colorCntr==0)?getColorNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()):productColor+", "+getColorNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId());
						colorCntr++;
					}
				}
			}
		}
		product.setColor(productColor);
		return product;
	}
	
	
	
	
}
