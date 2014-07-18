package com.asi.service.product.client.vo.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;
import com.asi.service.product.client.vo.SelectedProductCategory;
import com.asi.service.product.client.vo.material.Material;
import com.asi.service.product.vo.DataSheet;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.InventoryLink;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;
import com.asi.service.product.vo.SizeDetails;

public class LookupParser {
	

	@Autowired LookupValuesClient lookupClient;
	@Autowired	ImprintParser imprintParser;
	private String[] sizeCriteriaAry={"DIMS","CAPS","SABR","SAHU","SAIT","SANS","SAWI","SSNM","SVWT","SOTH"};
	LinkedHashMap<String, String> sizeGroupDetails;
	
	private List<String> sizecodes=new ArrayList<>();
	
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
		HashMap returnValue = null;
		if(null!=DimensionType && null!=attribute){
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getCriteriaAttributesFromLookup(lookupClient.getLookupcriteriaAttributeURL().toString());
		DimensionType=DimensionType.trim();
		attribute=attribute.trim();
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
	@SuppressWarnings("rawtypes")
	public String getCriteriaAttributeIdByDisplayName(String DimensionType) {
		DimensionType=DimensionType.trim();
		String returnValue = null;
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getCriteriaAttributesFromLookup(lookupClient.getLookupcriteriaAttributeURL().toString());
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (DimensionType.equalsIgnoreCase(currentCriteriaAttribute
							.get("DisplayName").toString())) {
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
	public String getUnitsOfMeasureCodeByFormat(String criteriaCode,
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
			if (codeValueGrpsMap.get("Format").toString().equalsIgnoreCase(units)) {
				elementUnit = (String) codeValueGrpsMap.get("Code");
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
		if(elementUnit.equals("\"")) elementUnit="in";
		if(!elementUnit.isEmpty()){
			if(criteriaCode.equalsIgnoreCase("SAIT")){
				elementValue=(!unitValue.isEmpty() && !elementUnit.isEmpty())?unitValue+" "+elementUnit:"";
			}else{
				elementValue=(!unitValue.isEmpty() && !elementUnit.isEmpty())?unitValue+":"+elementUnit:"";
			}
		}else{
			elementValue=(!unitValue.isEmpty())?unitValue:"";
		}
			
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
				if(null!=colorCode) break;
				}
			}
			if(null!=colorCode) break;
		/*for(LinkedHashMap currentColor:colorsList){
		 	if(currentColor.get("DisplayName").toString().equalsIgnoreCase(colorName)){
				colorCode=currentColor.get("Code").toString();				
			}*/
		}
				if(null==colorCode) colorCode=colorCodeOther;
		return colorCode;
	}
	public String getColorNameByCode(String colorCode) {
		ArrayList<LinkedHashMap> colorsList = lookupClient.getColorFromLookup(lookupClient.getLookupColorURL());
		String colorName="";
		
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
						if(null!=setCodeValue.get("ID") && setCodeValue.get("ID").toString().equalsIgnoreCase(colorCode))
						{
							colorName=String.valueOf(setCodeValue.get("CodeValue"));
							if(!colorName.trim().contains(" "))
							{
								colorName=colorName.trim();
								colorName="Medium "+colorName;
							}
							break;
						}
					}
					}
				}
			}
		}
		return colorName;
	}
	private String getMaterialNameByCode(String setCodeValueId) {
		String materialName="";
		List<Material> materialList= new ArrayList<Material>();
		LinkedHashMap<?,?> currentHashMap=new LinkedHashMap<>();
		LinkedHashMap<?,?> setcodeValuesMap=new LinkedHashMap<>();
		ArrayList<LinkedHashMap> materialsFromService = lookupClient.getMaterialFromLookup(lookupClient.getLookupMaterialURL());
		Iterator materialsIterator=  materialsFromService.iterator();
		ArrayList setCodeValuesList = null;
		while(materialsIterator.hasNext())
		{			
			currentHashMap=(LinkedHashMap)materialsIterator.next();			
			ArrayList codeValueGroupsList = (ArrayList<?>) currentHashMap.get("CodeValueGroups");
						
				Iterator<?> codeValueGroupsIterator  = (Iterator<?>) codeValueGroupsList.iterator();
				while(codeValueGroupsIterator.hasNext())
				{
					setcodeValuesMap=(LinkedHashMap)codeValueGroupsIterator.next();
				setCodeValuesList=(ArrayList)setcodeValuesMap.get("SetCodeValues");
				Iterator setCodeValuesListIterator=setCodeValuesList.iterator();
				while(setCodeValuesListIterator.hasNext())
				{
					currentHashMap=(LinkedHashMap)setCodeValuesListIterator.next();
					if(currentHashMap.containsKey("ID") && currentHashMap.get("ID").toString().equalsIgnoreCase(setCodeValueId)){
						if(currentHashMap.containsKey("CodeValue")){
							materialName=currentHashMap.get("CodeValue").toString();
							break;
						}						
					}						
				}
				if(null!=materialName)
					break;
				}
				if(null!=materialName)
					break;
		}	
		return materialName;
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
/*			productDataSheet.setProductId(Integer.valueOf(productDetail.getID()));
			productDataSheet.setCompanyId(Integer.valueOf(productDetail.getCompanyId()));
			productDataSheet.setId(Integer.valueOf(productDetail.getID()));*/
			productDataSheet.setUrl(crntDataSheet.getUrl());
			product.setProductDataSheet(productDataSheet);
		}
		return product;
	}
	public Product setProductServiceInventoryLink(ProductDetail productDetail,
			Product product) {
		ProductInventoryLink crntInventoryLink=productDetail.getProductInventoryLink();
		if(null!=crntInventoryLink){
			
			InventoryLink productInventoryLink=new InventoryLink();
			productInventoryLink.setProductId(Integer.valueOf(productDetail.getID()));
			productInventoryLink.setCompanyId(Integer.valueOf(productDetail.getCompanyId()));
			productInventoryLink.setId((null!=crntInventoryLink.getId())?0:Integer.valueOf(crntInventoryLink.getId()));
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
	/*	List<ProductConfiguration> srcProductConfigList=productDetail.getProductConfigurations();
		Configurations[] targetProductConfigList={};
		Configurations currentProductConfig=null;
		com.asi.service.product.vo.ProductCriteriaSets[] productCriteriaSetsAry=null;
		com.asi.service.product.vo.ProductCriteriaSets currentProductCriteriaSets=null;
		com.asi.service.product.vo.CriteriaSetValues currentCriteriaSetValues=null;
		List<com.asi.service.product.vo.CriteriaSetValues> currentCriteriaSetValuesAry=null;
		List<CriteriaSetValues> clientCriteriaSetValuesAry=null;
		ArrayList<ProductCriteriaSets> productCriteriaSetsList=null;
		if(null!=srcProductConfigList){
			int productConfigCntr=0;
			int productCriteriaSetCntr=0;
			targetProductConfigList=new Configurations[srcProductConfigList.size()];
			for(ProductConfiguration crntProductConfiguration:srcProductConfigList)
			{
				currentProductConfig=new Configurations();
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
*/		return product;
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
		String actualColor="";
		String customColor="";
		int colorCntr=0;
		List<ProductCriteriaSets> currentProductCriteriaSets=productDetail.getProductConfigurations().get(0).getProductCriteriaSets();
		for(ProductCriteriaSets currentProductCritieriaSet:currentProductCriteriaSets){
			if(currentProductCritieriaSet.getCriteriaCode().equalsIgnoreCase("PRCL")){
				for(CriteriaSetValues currentCriteriaSetValue:currentProductCritieriaSet.getCriteriaSetValues()){
					actualColor=currentCriteriaSetValue.getBaseLookupValue();
					customColor=currentCriteriaSetValue.getValue().toString();							
					if(null!=actualColor && !actualColor.isEmpty()) actualColor=actualColor.replace("Medium", "").trim();
					if(currentCriteriaSetValue.getValue() instanceof String && !currentCriteriaSetValue.getValue().toString().isEmpty()){
						if(actualColor.equals(customColor)){
							productColor=(colorCntr==0)?currentCriteriaSetValue.getValue().toString():productColor+", "+currentCriteriaSetValue.getValue().toString();
						}else{
							productColor=(colorCntr==0)?actualColor+"="+currentCriteriaSetValue.getValue().toString():productColor+", "+actualColor+"="+currentCriteriaSetValue.getValue().toString();
						}
						colorCntr++;
					}else{
						if(actualColor.equals(customColor)){
							productColor=(colorCntr==0)?getColorNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()):productColor+", "+getColorNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId());
						}else{
							productColor=(colorCntr==0)?getColorNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId())+"="+customColor:productColor+", "+getColorNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()+"="+customColor);
						}
						colorCntr++;
					}
				}
			}
		}
		product.setColor(productColor);
		return product;
	}
	private String setProductServiceConfiguration(ProductDetail productDetail,
			Product product,String criteriaCode) throws RestClientException, UnsupportedEncodingException {
		String productColor="";
		int colorCntr=0;
		List<ProductCriteriaSets> currentProductCriteriaSets=productDetail.getProductConfigurations().get(0).getProductCriteriaSets();
		for(ProductCriteriaSets currentProductCritieriaSet:currentProductCriteriaSets){
			if(currentProductCritieriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode)){
				for(CriteriaSetValues currentCriteriaSetValue:currentProductCritieriaSet.getCriteriaSetValues()){
					if(currentCriteriaSetValue.getValue() instanceof String && !currentCriteriaSetValue.getValue().toString().isEmpty()){
						productColor=(colorCntr==0)?currentCriteriaSetValue.getValue().toString():productColor+", "+currentCriteriaSetValue.getValue().toString();
						colorCntr++;
					}else{
						if(currentCriteriaSetValue.getValue() instanceof String){
							if(criteriaCode.equalsIgnoreCase("TDNM")){
								productColor=(colorCntr==0)?getSetValueNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),criteriaCode,currentCriteriaSetValue.getBaseLookupValue().toString()):productColor+", "+getSetValueNameByCode((currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()),criteriaCode,currentCriteriaSetValue.getBaseLookupValue().toString());
							}else{
								productColor=(colorCntr==0)?getSetValueNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),criteriaCode,currentCriteriaSetValue.getValue().toString()):productColor+", "+getSetValueNameByCode((currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()),criteriaCode,currentCriteriaSetValue.getValue().toString());
							}
						}else{
							productColor=(colorCntr==0)?getSetValueNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),criteriaCode,""):productColor+", "+getSetValueNameByCode((currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()),criteriaCode,"");	
						}
						colorCntr++;
					}
				}
			}
			/*switch(criteriaCode){
			case "MTRL":product.setMaterial(productColor);
			case "TDNM":product.setTradeName(productColor);
			case "SHAP":product.setShape(productColor);
			case "ORGN":product.setOrigin(productColor);*/
		//}
		
		}
		return productColor;
	}
	public String getSetValueNameByCode(String setCodeValueId,
			String criteriaCode,Object searchKey) throws RestClientException, UnsupportedEncodingException {
		sizecodes.addAll(Arrays.asList(sizeCriteriaAry));
		if(null!=criteriaCode && !sizecodes.contains(criteriaCode)){
		switch (criteriaCode){
		case "PRCL":return getColorNameByCode(setCodeValueId);
		case "MTRL":return getMaterialNameByCode(setCodeValueId);
		case "TDNM":return getTradeNameByCode(setCodeValueId,searchKey);
		case "SHAP":return getShapeByCode(setCodeValueId);
		case "ORGN":return getOriginNameByCode(setCodeValueId);
		case "PCKG":return getPackageNameByCode(setCodeValueId);
		case "IMMD":return getImprintNameByCode(setCodeValueId);
		default: return null;
		}	
		}else if(sizecodes.contains(criteriaCode)){
			return getSizeValueDetails(setCodeValueId,criteriaCode,searchKey.toString());
		}
		return null;
	}
	public String getSetCodeByName(String criteriaCode,String setCodeId) { 
		switch (criteriaCode){
		case "PRCL":return getColorCodeByName(setCodeId);
		case "IMMD":return getImprintCodeByName(setCodeId);
		default: return getColorCodeByName(setCodeId);
		}
	}
		private String getSizeValueDetails(String setCodeValueId,
		                             		String criteriaCode, String customValue) {
	                                             	String returnValue=null;
	                                              	//if(criteriaCode.equalsIgnoreCase("SABR")){
			returnValue=getSizeValueById(criteriaCode,setCodeValueId,customValue);
		//}			
		return returnValue;
	}
	private String getSizeValueById(String criteriaCode, String setCodeValueId,
			String customValue) {
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getSizesFromLookup(lookupClient.getLookupSizeURL().toString());
		ArrayList<LinkedHashMap> codeValueGroups=new ArrayList<>();
		ArrayList<LinkedHashMap> setCodeValueSet=new ArrayList<>();
		String returnValue=null;
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (criteriaCode.equalsIgnoreCase(currentCriteriaAttribute
							.get("Code").toString())) {
				codeValueGroups=(ArrayList<LinkedHashMap>) currentCriteriaAttribute.get("CodeValueGroups");
				for(LinkedHashMap curretCodevalueGroup:codeValueGroups){
					setCodeValueSet=(ArrayList<LinkedHashMap>) curretCodevalueGroup.get("SetCodeValues");
					for(LinkedHashMap currentSetcodeValue:setCodeValueSet){
						if(currentSetcodeValue.get("ID").toString().equalsIgnoreCase(setCodeValueId)){
							returnValue=currentSetcodeValue.get("CodeValue").toString();
							break;
						}
					}
					if(null!=returnValue) break;
				}
			}
			if(null!=returnValue) break;
		}
		return returnValue;
	}
	public String getSizesSetCodeValueId(String criteriaCode) {
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getSizesFromLookup(lookupClient.getLookupSizeURL().toString());
		ArrayList<LinkedHashMap> codeValueGroups=new ArrayList<>();
		ArrayList<LinkedHashMap> setCodeValueSet=new ArrayList<>();
		String returnValue=null;
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (criteriaCode.equalsIgnoreCase(currentCriteriaAttribute
							.get("Code").toString())) {
				codeValueGroups=(ArrayList<LinkedHashMap>) currentCriteriaAttribute.get("CodeValueGroups");
				for(LinkedHashMap curretCodevalueGroup:codeValueGroups){
					setCodeValueSet=(ArrayList<LinkedHashMap>) curretCodevalueGroup.get("SetCodeValues");
					for(LinkedHashMap currentSetcodeValue:setCodeValueSet){
						if(currentSetcodeValue.get("CodeValue").toString().contains("Other")){
							returnValue = currentSetcodeValue.get("ID").toString();
							break;	
						}
					}
					if(null!=returnValue) break;
				}
			}
			if(null!=returnValue) break;
		}	
		return returnValue;
	}
	private String getPackageNameByCode(String setCodeValueId) {
		ArrayList<LinkedHashMap> packagesList = lookupClient.getPackagesFromLookup(lookupClient.getLookupPackageURL());
		for(LinkedHashMap currentPackage:packagesList){
			if(currentPackage.get("Key").toString().equalsIgnoreCase(setCodeValueId)){
				return currentPackage.get("Value").toString();
			}
		}
		return "";
	}
	private String getOriginNameByCode(String setCodeValueId) {
		ArrayList<LinkedHashMap> originsList = lookupClient.getOriginFromLookup(lookupClient.getOriginLookupURL());
		for(LinkedHashMap currentOrigin:originsList){
			if(currentOrigin.get("ID").toString().equalsIgnoreCase(setCodeValueId)){
				return currentOrigin.get("CodeValue").toString();
			}
		}
		return "";
	}
	private String getShapeByCode(String setCodeValueId) {
		ArrayList<LinkedHashMap> shapesList = lookupClient.getShapesFromLookup(lookupClient.getLookupShapeURL());
		for(LinkedHashMap currentShape:shapesList){
			if(currentShape.get("Key").toString().equalsIgnoreCase(setCodeValueId)){
				return currentShape.get("Value").toString();
			}
		}
		return "";
	}
	private String getTradeNameByCode(String setCodeValueId,Object srchkey) throws RestClientException, UnsupportedEncodingException {
		ArrayList<LinkedHashMap> tradeNameList = lookupClient.getTradeNameFromLookup(lookupClient.getLookupTradeNameURL(),srchkey);
		String tradeName="";
		for(LinkedHashMap currentTrade:tradeNameList){
			if(currentTrade.get("Key").toString().equalsIgnoreCase(setCodeValueId)){
				tradeName=currentTrade.get("Value").toString();
				break;
			}
		}
		return tradeName;
	}
	public Product setProductServiceWithConfigurations(
			ProductDetail productDetail, Product product) throws RestClientException, UnsupportedEncodingException {
		product=setProductServiceColor(productDetail, product);
		product.setMaterial(setProductServiceConfiguration(productDetail, product, "MTRL"));
		//origin,package,tradename,shape
		product.setOrigin(setProductServiceConfiguration(productDetail, product, "ORGN"));
		product.setPackages(setProductServiceConfiguration(productDetail, product, "PCKG"));
		product.setTradeName(setProductServiceConfiguration(productDetail, product, "TDNM"));
		product.setShape(setProductServiceConfiguration(productDetail, product, "SHAP"));
		product.setImprints(setProductServiceImprints(productDetail, product));
		product.setSize(setProductServiceSizeDetails(productDetail,product));
		return product;
	}
	private Imprints setProductServiceImprints(ProductDetail productDetail,
			Product product) {
		Imprints imprints=new Imprints();
		List<ImprintMethod> imprintMethodList=new ArrayList<>();
		
/*		String imprintMethodNames=setProductServiceConfiguration(productDetail, product, "IMMD");
		//String imprintArtworkNames=setProductServiceConfiguration(productDetail, product, "ARTW");
		//String imprintMino=setProductServiceConfiguration(productDetail, product, "MINO");
		String[] imprintMethods=imprintMethodNames.split(",");
		for(String currentImprintMethodName: imprintMethods){
			ImprintMethod currentImprintMethod=new ImprintMethod();
			currentImprintMethod.setMethodName(currentImprintMethodName);
			imprintMethodList.add(currentImprintMethod);
		}*/
		
		ProductConfiguration productConfiguration = productDetail
				.getProductConfigurations().get(0);
		ProductCriteriaSets imprintCriteriaSet = getCriteriaSetBasedOnCriteriaCode(
						productConfiguration.getProductCriteriaSets(), "IMMD");
		if(null!=imprintCriteriaSet){
			List<CriteriaSetValues> criteriaSetValues = imprintCriteriaSet
					.getCriteriaSetValues();
			for (CriteriaSetValues criteriaSetValue : criteriaSetValues) {
				imprintMethodList = imprintParser.getImprintMethodRelations(
						productDetail.getExternalProductId(),
						Integer.parseInt(criteriaSetValue.getCriteriaSetId()),
						productConfiguration.getProductCriteriaSets(),
						productDetail.getRelationships());
			}
		}
		imprints.setImprintMethod(imprintMethodList);	
		
		
		
		
		
		
		
		imprints.setImprintMethod(imprintMethodList);
		return imprints;
	}
	public ImprintParser getImprintParser() {
		return imprintParser;
	}
	public void setImprintParser(ImprintParser imprintParser) {
		this.imprintParser = imprintParser;
	}
	private SizeDetails setProductServiceSizeDetails(
			ProductDetail productDetail, Product product) throws RestClientException, UnsupportedEncodingException {
		SizeDetails sizeDetails=new SizeDetails();
		String sizeValue="";
		String sawiSize="";
		sizecodes.addAll(Arrays.asList(sizeCriteriaAry));
		int sizeCntr=0,sizeItemCntr=0;	
		List<LinkedHashMap<String,String>> valueList=new ArrayList<>();
		String criteriaCode="";
		String sizeValueItem="";
		List<ProductCriteriaSets> currentProductCriteriaSets=productDetail.getProductConfigurations().get(0).getProductCriteriaSets();
		for(ProductCriteriaSets currentProductCritieriaSet:currentProductCriteriaSets){
			if(sizecodes.contains(currentProductCritieriaSet.getCriteriaCode())){
				sizeDetails.setGroupName(getSizeGroupNameByCode(currentProductCritieriaSet.getCriteriaCode()));
				criteriaCode=currentProductCritieriaSet.getCriteriaCode();
				for(CriteriaSetValues currentCriteriaSetValue:currentProductCritieriaSet.getCriteriaSetValues()){
					if(currentCriteriaSetValue.getValue() instanceof String){
						if(!currentCriteriaSetValue.getValue().toString().isEmpty()){
								sizeValue=(sizeCntr==0)?currentCriteriaSetValue.getValue().toString():sizeValue+", "+currentCriteriaSetValue.getValue().toString();
						sizeCntr++;
					}else {
							sizeValue=(sizeCntr==0)?getSetValueNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),criteriaCode,""):sizeValue+", "+getSetValueNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),criteriaCode,"");	
						sizeCntr++;
					}
				} else if(currentCriteriaSetValue.getValue() instanceof List){	
						valueList=(List<LinkedHashMap<String,String>>)currentCriteriaSetValue.getValue();
    					for(LinkedHashMap<String, String> valueObj:valueList){
    						
    						sizeValueItem= getElementValue(criteriaCode,String.valueOf(valueObj.get("CriteriaAttributeId")),String.valueOf(valueObj.get("UnitValue")),String.valueOf(valueObj.get("UnitOfMeasureCode")));
    						if(criteriaCode.toString().equals("DIMS")){
    								sizeValueItem=getSizesResponse(criteriaCode,String.valueOf(valueObj.get("CriteriaAttributeId"))).get("DisplayName")+":"+sizeValueItem;
    						}
    						if(criteriaCode.toString().equalsIgnoreCase("SAWI")){
    							sawiSize=(sizeItemCntr==0 && sawiSize.trim().isEmpty())?sizeValueItem:sawiSize+"x"+sizeValueItem;
    						}else if(criteriaCode.toString().equalsIgnoreCase("SANS")){
    							sawiSize=(sizeItemCntr==0 && sawiSize.trim().isEmpty())?sizeValueItem:sawiSize+"("+sizeValueItem+")";
    							//sizeValue=(sizeItemCntr==0 && sizeValue.trim().isEmpty())?sizeValueItem:sizeValue+"("+sizeValueItem+")";
    						}else{
    							sizeValue=(sizeItemCntr==0 && sizeValue.trim().isEmpty())?sizeValueItem:sizeValue+","+sizeValueItem;
    						}
    								sizeItemCntr++;
    					}
						//sizeValue=(sizeCntr==0)?getSetValueNameByCode(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),criteriaCode,currentCriteriaSetValue.getValue()):sizeValue+", "+getSetValueNameByCode((currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()),criteriaCode,currentCriteriaSetValue.getValue());
						sizeCntr++;
						if(criteriaCode.equalsIgnoreCase("SAWI") || criteriaCode.equalsIgnoreCase("SANS")){
							if(null!=sawiSize && !sawiSize.trim().isEmpty()){
								if(!sizeValue.trim().isEmpty()){
									sizeValue=(sizeCntr==0)?sawiSize:sizeValue+","+sawiSize;
								}else{
									sizeValue=sawiSize;
								}
								sawiSize="";
							}
						}	
				}
					
				}
				sizeDetails.setSizeValue(sizeValue);
			}			
		}
		if(sizeDetails.getGroupName()==null){
			sizeDetails.setGroupName("No Size Group is set to this product");
			sizeDetails.setSizeValue("");
		}
		return sizeDetails;
	}
	private void setSizeGroupDetails(){
		 String[] sizecriteriaNames={"Dimension","Capacity","Apparel - Bra Sizes","Apparel - Hosiery/Uniform Sizes","Apparel - Infant & Toddler","Apparel - Dress Shirt Sizes" +
				"","Apparel - Pants Sizes","Standard & Numbered","Volume/Weight","Other Sizes"};
		 sizeGroupDetails=new LinkedHashMap<String, String>();
int sizeGroupCntr=0;
		 for(String currentSizeCode:sizeCriteriaAry){
			if(sizecriteriaNames.length>9){
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[sizeGroupCntr]);
			sizeGroupCntr++;
/*			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[1]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[2]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[3]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[4]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[5]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[6]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[7]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[8]);
			sizeGroupDetails.put(currentSizeCode, sizecriteriaNames[9]);
*/			}
		}		
	}
	private String getSizeGroupNameByCode(String criteriaCode) {
		setSizeGroupDetails();
		if(null!=criteriaCode){
			setSizeGroupDetails();
		return sizeGroupDetails.get(criteriaCode);
		}
		return null;
	}
	
	public String getSizeCodeByName(String groupName) {
		setSizeGroupDetails();
		Set<String> sizeCriteriCodes = sizeGroupDetails.keySet();
		for(String currentSizeCode:sizeCriteriCodes){
			if(sizeGroupDetails.get(currentSizeCode).equalsIgnoreCase(groupName))
			return currentSizeCode;
		}
		return null;
	}
	public String getSpecificCriteriaAttributeId(String displayName,String criteriaCode) {
		displayName=displayName.trim();
		String returnValue = null;
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getCriteriaAttributesFromLookup(lookupClient.getLookupcriteriaAttributeURL().toString());
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (displayName.equalsIgnoreCase(currentCriteriaAttribute
							.get("DisplayName").toString()) && criteriaCode.equalsIgnoreCase(currentCriteriaAttribute
									.get("CriteriaCode").toString())) {
				returnValue = currentCriteriaAttribute.get("ID").toString();
				break;
			}
		}
		return returnValue;
	}
	public String getSizesApperalSetCodeValueId(String groupName,String criteriaCode) {
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getSizesFromLookup(lookupClient.getLookupSizeURL().toString());
		ArrayList<LinkedHashMap> codeValueGroups=new ArrayList<>();
		ArrayList<LinkedHashMap> setCodeValueSet=new ArrayList<>();
		String returnValue=null;
		String otherSetCodeValue="";
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (criteriaCode.equalsIgnoreCase(currentCriteriaAttribute
							.get("Code").toString())) {
				codeValueGroups=(ArrayList<LinkedHashMap>) currentCriteriaAttribute.get("CodeValueGroups");
				for(LinkedHashMap curretCodevalueGroup:codeValueGroups){
					setCodeValueSet=(ArrayList<LinkedHashMap>) curretCodevalueGroup.get("SetCodeValues");
					for(LinkedHashMap currentSetcodeValue:setCodeValueSet){
						if(currentSetcodeValue.get("CodeValue").toString().contains("Other")){
							otherSetCodeValue = currentSetcodeValue.get("ID").toString();
						}else if(currentSetcodeValue.get("CodeValue").toString().equalsIgnoreCase(groupName)){
							returnValue = currentSetcodeValue.get("ID").toString();
							break;	
						}
					}
					if(null!=returnValue) break;
				}
			}
			if(null!=returnValue) break;
		}
		//if(criteriaCode.equalsIgnoreCase("SOTH") && null==returnValue) returnValue=otherSetCodeValue;
		return returnValue;
	}
	 /**
     * Find a criteriaSet from the productCriteria set array based on the criteria code
     * 
     * @param productCriteriaSetsAry
     *            is the array contains all criteria set of the product
     * @param criteriaCode
     *            is the criteria code of the criteriaSet to find
     * @return the matched {@linkplain ProductCriteriaSets } or null
     */
	 public ProductCriteriaSets getCriteriaSetBasedOnCriteriaCode(List<ProductCriteriaSets> productCriteriaSetsAry, String criteriaCode) {
	        for (ProductCriteriaSets currentProductCriteriaSet: productCriteriaSetsAry)
	        	{
	        		if (null != currentProductCriteriaSet && currentProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode.trim()))
	        			return currentProductCriteriaSet;
	        	}
	        return null;
	    }
	
}
