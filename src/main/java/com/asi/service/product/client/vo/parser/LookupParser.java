package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;
import com.asi.service.product.client.vo.ProductKeywords;
import com.asi.service.product.client.vo.SelectedProductCategory;
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
	/*	List<ProductConfiguration> srcProductConfigList=productDetail.getProductConfigurations();
		if(null!=srcProductConfigList){
			ProductConfigurations[] targetProductConfigList=new ProductConfigurations[srcProductConfigList.size()];
		}		*/
		return product;
	}
	
	
	
	
}
