package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductConfigurations;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Relationships;
import com.asi.service.product.client.vo.Value;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.Product;

public class ProductConfigurationsParser {
	private final static Logger _LOGGER = Logger
			.getLogger(ProductConfigurationsParser.class.getName());
	@Autowired LookupParser productLookupParser;
	RelationshipParser relationshipParser  = new RelationshipParser(); 
	private HashMap<String,HashMap<String, String>> criteriaSet=new HashMap<>();
	private int productCriteriaSetCntr=-1;
	private int newCriteriaSetCodeValueCntr=-112;
	private int newCriteriaSetValuesCntr=-1;
	
	public String[] getPriceCriteria(ProductDetail productDetail,String priceGridId) {
		String[] priceCrterias=null;
		String criteriaOne="",criteria1Value="";
		String criteriaTwo="",criteria2Value="";
		String currentCriteria="";
		if(null!=productDetail && null!=priceGridId)
		{
		priceCrterias=new String[2];
		String externalId=productDetail.getExternalProductId();
		if(criteriaSet.isEmpty() && null!=externalId)
		{
			criteriaSet=setCriteriaSet(productDetail,externalId);
		}
		if(!criteriaSet.isEmpty() && null!=productDetail.getPriceGrids())
		{
			for(PriceGrid currentPricingItem:productDetail.getPriceGrids())
			{
				if(currentPricingItem.getIsBasePrice() && null!=currentPricingItem.getPricingItems())
				{
					for(PricingItem pricingItem:currentPricingItem.getPricingItems())
					{
						currentCriteria=criteriaSet.get(externalId).get(pricingItem.getCriteriaSetValueId().toString());
						if(null!=currentCriteria && priceGridId.toString().equalsIgnoreCase(pricingItem.getPriceGridId()))
						{
						if(criteriaOne.isEmpty()) {						
							criteriaOne=currentCriteria.substring(0,currentCriteria.indexOf(":"));
							criteria1Value=currentCriteria.substring(currentCriteria.indexOf(":")+1);
						}else if(criteriaTwo.isEmpty() && !criteriaOne.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":")))){
							criteriaTwo=currentCriteria.substring(0,currentCriteria.indexOf(":"));
							criteria2Value=currentCriteria.substring(currentCriteria.indexOf(":")+1);
						} else if(criteriaOne.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":"))))
						{
							criteria1Value+=","+currentCriteria.substring(currentCriteria.indexOf(":")+1);
						}
						else if(criteriaTwo.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":"))))
								{
							criteria2Value+=","+currentCriteria.substring(currentCriteria.indexOf(":")+1);
								}
						if(!criteriaOne.isEmpty() && !criteriaTwo.isEmpty() && !criteriaOne.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":"))) && !criteriaTwo.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":")))) {
							_LOGGER.info("InValid Price Criteria :"+currentCriteria);
						}
						}
					}
				}
			}
		}
		criteriaSet=new HashMap<>();
		priceCrterias[0]=(!criteriaOne.isEmpty())?criteriaOne+":"+criteria1Value:"";
		priceCrterias[1]=(!criteriaTwo.isEmpty())?criteriaTwo+":"+criteria2Value:"";
		}
		return priceCrterias;
	}
	private HashMap<String,HashMap<String, String>> setCriteriaSet(ProductDetail productDetails,String externalId)
	{
		HashMap<String,HashMap<String, String>> currentHashMap=new HashMap<>();
		HashMap<String, String> productCriteriSets=new HashMap<>();
		ArrayList<ProductConfiguration> productConfigurationList=(ArrayList<ProductConfiguration>) productDetails.getProductConfigurations();
		for(ProductConfiguration currentProductConfiguration: productConfigurationList){
			for(ProductCriteriaSets currentProductCriteriSet:currentProductConfiguration.getProductCriteriaSets()){
				for(CriteriaSetValues currentCriteria:currentProductCriteriSet.getCriteriaSetValues()){
					if(currentCriteria.getValue() instanceof String){
						productCriteriSets.put(currentCriteria.getId().toString(), currentCriteria.getCriteriaCode()+":"+currentCriteria.getValue().toString());
					}
					else if(currentCriteria.getValue() instanceof ArrayList){
						productCriteriSets.put(currentCriteria.getId().toString(), currentCriteria.getCriteriaCode()+":"+productLookupParser.getValueString((ArrayList<?>)currentCriteria.getValue(),currentCriteria.getCriteriaCode()));
					}
				}
			}
		}
		currentHashMap.put(externalId, productCriteriSets);
		return currentHashMap;
	}
	public LookupParser getProductLookupParser() {
		return productLookupParser;
	}
	public void setProductLookupParser(LookupParser productLookupParser) {
		this.productLookupParser = productLookupParser;
	}
	public ProductConfigurations[] transformProductConfiguration(
			com.asi.service.product.vo.ProductConfigurations[] productConfigurations) {
		ProductConfigurations[] productConfigAry={};
		ProductConfigurations serviceProductConfig=null;
		if(null!=productConfigurations && productConfigurations.length>0){
			int productConfigCntr=0;
			productConfigAry=new ProductConfigurations[productConfigurations.length];
			for(com.asi.service.product.vo.ProductConfigurations crntProductConfig:productConfigurations){
				serviceProductConfig=new ProductConfigurations();
				BeanUtils.copyProperties(crntProductConfig, serviceProductConfig);		
				serviceProductConfig.setProductCriteriaSets(transformProductCriteriaSets(crntProductConfig.getProductCriteriaSets()));
				productConfigAry[productConfigCntr++]=serviceProductConfig;
			}			
		}
		return productConfigAry;
	}

	private List<ProductCriteriaSets> transformProductCriteriaSets(
			com.asi.service.product.vo.ProductCriteriaSets[] productCriteriaSets) {
		List<ProductCriteriaSets> productCriteriaSetsAry=new ArrayList<>();
		ProductCriteriaSets clientProductCriteriaSets=null;
		if(null!=productCriteriaSets && productCriteriaSets.length>0){
			int criteriaSetCntr=0;
			//productCriteriaSetsAry=new ProductCriteriaSets[productCriteriaSets.length];
			for(com.asi.service.product.vo.ProductCriteriaSets currentProductCriteriaSets:productCriteriaSets){
				clientProductCriteriaSets=new ProductCriteriaSets();
				BeanUtils.copyProperties(currentProductCriteriaSets, clientProductCriteriaSets);
				clientProductCriteriaSets.setCriteriaSetValues(transformCriteriaSetValues(currentProductCriteriaSets.getCriteriaSetValues()));
				productCriteriaSetsAry.add(clientProductCriteriaSets);
			}			
		}			
		return productCriteriaSetsAry;
	}
	private List<CriteriaSetValues> transformCriteriaSetValues(
			List<com.asi.service.product.vo.CriteriaSetValues> criteriaSetValues) {
		List<CriteriaSetValues> criteriaSetValuesAry=new ArrayList<>();
		CriteriaSetValues clientCriteriaSetValues=null;
		if(null!=criteriaSetValues && criteriaSetValues.size()>0){
			//criteriaSetValuesAry=new CriteriaSetValues[criteriaSetValues.length];
			for(com.asi.service.product.vo.CriteriaSetValues currentCriteriaSetValues:criteriaSetValues){
				clientCriteriaSetValues=new CriteriaSetValues();
				BeanUtils.copyProperties(currentCriteriaSetValues, clientCriteriaSetValues);
				criteriaSetValuesAry.add(clientCriteriaSetValues);
			}
		}
		return criteriaSetValuesAry;
	}
	public com.asi.service.product.client.vo.Product setProductWithImprintDetails(
			Product currentProduct,
			com.asi.service.product.client.vo.Product velocityBean,LookupParser lookupParser,String criteriaCode) {
		com.asi.service.product.client.vo.ProductConfigurations[] productConfigAry=velocityBean.getProductConfigurations();
		if(null!=currentProduct.getProductConfigurations() && currentProduct.getProductConfigurations().length>0)
		{
			com.asi.service.product.vo.ProductConfigurations productConfiguration=currentProduct.getProductConfigurations()[0]; 
			com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSets=getProductCriteriaSetByCodeIfExist(productConfiguration,criteriaCode);
			Imprints imprintMethodsList=currentProduct.getImprints();
			List<com.asi.service.product.vo.CriteriaSetValues> criteriaSetValuesAry=new ArrayList<>();
			
			if(null!=imprintMethodsList && imprintMethodsList.getImprintMethod().size()>0)
			{
				//criteriaSetValuesAry=new com.asi.service.product.vo.CriteriaSetValues[imprintMethodsList.getImprintMethod().size()];
				if(null==imprintProductCriteriaSets){
					productConfigAry=Arrays.copyOf(productConfigAry, productConfigAry.length+1);
					imprintProductCriteriaSets=new com.asi.service.product.vo.ProductCriteriaSets();
					imprintProductCriteriaSets.setCriteriaCode(criteriaCode);
					imprintProductCriteriaSets.setCompanyId(currentProduct.getCompanyId());
					imprintProductCriteriaSets.setConfigId(productConfiguration.getId());
					imprintProductCriteriaSets.setCriteriaSetId("-1");
				}			
					// check with the combination of method name, artwork and minquantity - With existing productcriteria set collection
					criteriaSetValuesAry=checkImprintMethod(productConfiguration,imprintProductCriteriaSets,imprintMethodsList,lookupParser);
					// If exist then continue other wise prepare new criteriaset and append as a new criteria set
					// Find and Replace new Product Criteria set collection of IMMD with existing one
					imprintProductCriteriaSets.setCriteriaSetValues(criteriaSetValuesAry);		
					
				//productConfigAry=replaceOrAddIfImprintExist(productConfigAry,imprintProductCriteriaSets,criteriaCode);
			}	
			Imprints imprintIds = new Imprints();
			String methodsList="";
			String minOrderList="";
			String artworkList="";
			List<ImprintMethod> imprintMethodIdsAry=new ArrayList<>();
			int methodNameCntr=0,artworkCntr=0,minOrderCntr=0;
			for(ImprintMethod crntImprint:imprintMethodsList.getImprintMethod())
			{
				if(null!=crntImprint.getMethodName() && !crntImprint.getMethodName().isEmpty()){
				if(methodNameCntr==0){
					methodsList=crntImprint.getMethodName();
					methodNameCntr++;
				}else{
					methodsList+=","+crntImprint.getMethodName();
				}
				}
				if(null!=crntImprint.getMinimumOrder() && !crntImprint.getMinimumOrder().isEmpty()){
				if(minOrderCntr==0){
					minOrderList=crntImprint.getMinimumOrder();
					minOrderCntr++;
				}else{
					minOrderList+=","+crntImprint.getMinimumOrder();
				}
				}
				if(null!=crntImprint.getArtworkName() && !crntImprint.getArtworkName().isEmpty()){
				if(artworkCntr==0){
					artworkList=crntImprint.getArtworkName();
					artworkCntr++;
				}else{
					artworkList+=","+crntImprint.getArtworkName();
				}
				}
					}
			
			productConfigAry[0].setProductCriteriaSets(checkCriteriaSetAndCreateIfNotExist(productConfigAry[0].getProductCriteriaSets(),minOrderList,"MINO"));
			productConfigAry[0].setProductCriteriaSets(checkCriteriaSetAndCreateIfNotExist(productConfigAry[0].getProductCriteriaSets(),artworkList,"ARTW"));
			productConfigAry[0].setProductCriteriaSets(checkCriteriaSetAndCreateIfNotExist(productConfigAry[0].getProductCriteriaSets(),methodsList,"IMMD"));
			for(ImprintMethod crntImprint:imprintMethodsList.getImprintMethod())
			{
			crntImprint.setMethodName(getRespectiveId(productConfigAry[0],crntImprint.getMethodName(),"IMMD"));
			crntImprint.setMinimumOrder(getRespectiveId(productConfigAry[0],crntImprint.getMinimumOrder(),"MINO"));
			crntImprint.setArtworkName(getRespectiveId(productConfigAry[0],crntImprint.getArtworkName(),"ARTW"));
			imprintMethodIdsAry.add(crntImprint);
			}
			imprintIds.setImprintMethod(imprintMethodIdsAry);
			com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSet=getProductCriteriaSetByCodeIfExist(productConfiguration,"IMMD");
			com.asi.service.product.vo.ProductCriteriaSets artworkProductCriteriaSet=getProductCriteriaSetByCodeIfExist(productConfiguration,"ARTW");
			com.asi.service.product.vo.ProductCriteriaSets minQtyProductCriteriaSet=getProductCriteriaSetByCodeIfExist(productConfiguration,"MINO");
			
			velocityBean.setRelationships(relationshipParser.createImprintArtworkRelationShip(imprintIds.getImprintMethod(), velocityBean.getId(), imprintProductCriteriaSet.getCriteriaSetId(), artworkProductCriteriaSet.getCriteriaSetId(), minQtyProductCriteriaSet.getCriteriaSetId()));
			
			// getRelationshipBasedOnCriteriaCodes IMMD * ARTW 
			//compareAndUpdateRelationship
			
			// getRelationshipBasedOnCriteriaCodes IMMD * MINO
				//		compareAndUpdateRelationship
						
		}
		
		velocityBean.setProductConfigurations(productConfigAry);
		if(null==velocityBean.getRelationships())
		{
			velocityBean.setRelationships(new Relationships[]{});
		}
		/*List<Relationships> relationshipAry=currentProduct.getRelationships();
		for(Relationship currentRe)*/
		return velocityBean;
	}

	private List<ProductCriteriaSets> checkCriteriaSetAndCreateIfNotExist(
			List<ProductCriteriaSets> productCriteriaSets, String methodName,
			String criteriaCode) {
		List<ProductCriteriaSets> finalProductCriteriaSets=new ArrayList<>();
		Value valueObj=new Value();
		List<Value> valueList=new ArrayList<>();
		List<CriteriaSetValues> newCriteriaSetValues=null;
		ProductCriteriaSets currentProductCriteriaSet=null;
		CriteriaSetValues currentCriteriaSetValues=null;
		CriteriaSetCodeValues currentSetCodeValues=null;
		finalProductCriteriaSets=productCriteriaSets;
		boolean criteriaSetExist=false;
		String criteriaAtribId="0";
		String[] methodNamesAry={};
		//List<ProductCriteriaSets> tempFinalProductCriteriaSets=removeCurrentCriteriaSet(finalProductCriteriaSets,criteriaCode);
		for(ProductCriteriaSets currentProductCriteriaSets:productCriteriaSets){
			methodNamesAry=methodName.split(",");
		for(String currentMethodName:methodNamesAry)
		{
			if(currentProductCriteriaSets.getCriteriaCode().equalsIgnoreCase(criteriaCode)){
				//finalProductCriteriaSets=productCriteriaSets;
			//	if(criteriaCode.equalsIgnoreCase("IMMD"))	currentProductCriteriaSets=checkCriteriaSetCodeValueIfNotExist(currentProductCriteriaSets,currentMethodName,productLookupParser.getImprintCodeByName(methodName));
				//else if(criteriaCode.equalsIgnoreCase("ARTW"))	currentProductCriteriaSets=checkCriteriaSetCodeValueIfNotExist(currentProductCriteriaSets,currentMethodName,productLookupParser.getArtworkCodeByName(methodName));
				//else if(criteriaCode.equalsIgnoreCase("MINO"))	currentProductCriteriaSets=checkCriteriaSetCodeValueIfNotExist(currentProductCriteriaSets,currentMethodName,productLookupParser.getMinOrderCodeByName(methodName));
//				currentProductCriteriaSet.getCriteriaSetValues().get(0).getCriteriaSetCodeValues()[0].setCriteriaSetValueId(productLookupParser.getArtworkCodeByName(methodName));
				criteriaSetExist=true;
				break;
			}
		if(!criteriaSetExist){
			currentProductCriteriaSet=new ProductCriteriaSets();
			newCriteriaSetValues=new ArrayList<>();
			currentCriteriaSetValues=new CriteriaSetValues();
			currentCriteriaSetValues.setId(String.valueOf(-1));
			currentCriteriaSetValues.setCriteriaCode(criteriaCode);
			currentCriteriaSetValues.setCriteriaSetId(String.valueOf(productCriteriaSetCntr--));
			currentCriteriaSetValues.setValueTypeCode("LOOK");
			currentCriteriaSetValues.setDisplaySequence("0");
			currentCriteriaSetValues.setIsSubset("false");
			currentCriteriaSetValues.setValue(currentMethodName);
			currentCriteriaSetValues.setIsSetValueMeasurement("false");
			currentSetCodeValues = new CriteriaSetCodeValues();
			currentSetCodeValues.setId(String.valueOf(newCriteriaSetCodeValueCntr--));
			currentSetCodeValues.setCriteriaSetValueId(currentCriteriaSetValues.getId());
			if(criteriaCode.equalsIgnoreCase("IMMD"))
			currentSetCodeValues.setSetCodeValueId(productLookupParser.getImprintCodeByName(currentMethodName));
			else
			if(criteriaCode.equalsIgnoreCase("ARTW"))
				currentSetCodeValues.setSetCodeValueId(productLookupParser.getArtworkCodeByName(currentMethodName));
			else if(criteriaCode.equalsIgnoreCase("MINO")){
				if(currentMethodName.contains(":"))
				{
					valueObj.setUnitValue(currentMethodName.substring(0,currentMethodName.indexOf(":")));
					criteriaAtribId=productLookupParser.getCriteriaAttributeId("MINO");
					valueObj.setCriteriaAttributeId(criteriaAtribId);
					valueObj.setUnitOfMeasureCode(productLookupParser.getUnitsOfMeasureCode(criteriaCode,criteriaAtribId,currentMethodName.substring(currentMethodName.indexOf(":")+1)));
					valueList.add(valueObj);
					currentCriteriaSetValues.setValue(valueList);
				}
				currentSetCodeValues.setSetCodeValueId(productLookupParser.getMinOrderCodeByName(currentMethodName));
				//
			}
				currentCriteriaSetValues.setCriteriaSetCodeValues(new CriteriaSetCodeValues[]{currentSetCodeValues});
				newCriteriaSetValues.add(currentCriteriaSetValues);
				currentProductCriteriaSet.setCriteriaSetValues(newCriteriaSetValues);
				//finalProductCriteriaSets=Arrays.copyOf(finalProductCriteriaSets, finalProductCriteriaSets.length+1);
				finalProductCriteriaSets.add(currentProductCriteriaSet);
		}
		}
		}
			
		return finalProductCriteriaSets;
	}
	private List<ProductCriteriaSets> removeCurrentCriteriaSet(
			List<ProductCriteriaSets> tempProductCriteriaSets,
			String criteriaCode) {
		List<ProductCriteriaSets> newProductCriteriaSetList=new ArrayList<>();
		for(ProductCriteriaSets currentProductCriteriaSets:tempProductCriteriaSets)
		{
			if(!currentProductCriteriaSets.getCriteriaCode().equalsIgnoreCase(criteriaCode)){
				newProductCriteriaSetList.add(currentProductCriteriaSets);
			}
		}
		
		return newProductCriteriaSetList;
	}
	@SuppressWarnings("unchecked")
	private ProductCriteriaSets checkCriteriaSetCodeValueIfNotExist(ProductCriteriaSets productCriteriaSets,
			String methodName, String artworkCodeByName) {
		ProductCriteriaSets finalProductCriteriaSets=productCriteriaSets;
		List<CriteriaSetValues> currentCriteriaSetValueList=null;
		CriteriaSetValues currentCriteriaSetValue=null;
		CriteriaSetCodeValues currentSetCodeValues=null;
		List<LinkedHashMap> valuesList=null;
		String valueString=null;
		String criteriaAtribId="0";
		LinkedHashMap valueObj=new LinkedHashMap();
		boolean criteriaSetCodeExist=false;
		currentCriteriaSetValueList=productCriteriaSets.getCriteriaSetValues();
		for(CriteriaSetValues currentCriteriaSetValues:productCriteriaSets.getCriteriaSetValues()){
			if(currentCriteriaSetValues.getValue() instanceof String){
				if(currentCriteriaSetValues.getValue().toString().equalsIgnoreCase(methodName)){
					criteriaSetCodeExist=true;
					break;
				}
				if(artworkCodeByName.equalsIgnoreCase(currentCriteriaSetValues.getCriteriaSetCodeValues()[0].getSetCodeValueId())){
					criteriaSetCodeExist=true;
					break;
				}
			
			}else 
				if(currentCriteriaSetValues.getValue() instanceof List){
					valuesList=(List<LinkedHashMap>)currentCriteriaSetValues.getValue();
					criteriaAtribId=productLookupParser.getCriteriaAttributeId(currentCriteriaSetValues.getCriteriaCode());
					valueString=valuesList.get(0).get("UnitValue")+":"+productLookupParser.getUnitsOfMeasureCode(currentCriteriaSetValues.getCriteriaCode(),criteriaAtribId,valuesList.get(0).get("UnitOfMeasureCode").toString());
					if(methodName.equalsIgnoreCase(valueString)){
						criteriaSetCodeExist=true;
						break;
					}
				}
		}

		if(!criteriaSetCodeExist){
			currentCriteriaSetValue=new CriteriaSetValues();
			currentCriteriaSetValue.setId(String.valueOf(-1));
			currentCriteriaSetValue.setCriteriaCode(productCriteriaSets.getCriteriaCode());
			currentCriteriaSetValue.setCriteriaSetId(productCriteriaSets.getCriteriaSetId());
			currentCriteriaSetValue.setValueTypeCode("LOOK");
			currentCriteriaSetValue.setDisplaySequence("0");
			currentCriteriaSetValue.setIsSubset("false");
			currentCriteriaSetValue.setValue(methodName);
			currentCriteriaSetValue.setIsSetValueMeasurement("false");
			currentSetCodeValues = new CriteriaSetCodeValues();
			currentSetCodeValues.setId(String.valueOf(newCriteriaSetCodeValueCntr--));
			currentSetCodeValues.setCriteriaSetValueId(currentCriteriaSetValue.getId());
			if(productCriteriaSets.getCriteriaCode().equalsIgnoreCase("IMMD"))
			currentSetCodeValues.setSetCodeValueId(productLookupParser.getImprintCodeByName(methodName));
			else
			if(productCriteriaSets.getCriteriaCode().equalsIgnoreCase("ARTW"))
				currentSetCodeValues.setSetCodeValueId(productLookupParser.getArtworkCodeByName(methodName));
			else if(productCriteriaSets.getCriteriaCode().equalsIgnoreCase("MINO")){
				if(methodName.contains(":"))
				{
					valueObj.put("UnitValue",methodName.substring(0,methodName.indexOf(":")+1));
					criteriaAtribId=productLookupParser.getCriteriaAttributeId("MINO");
					valuesList=new ArrayList<>();
					valueObj.put("CriteriaAttributeId", criteriaAtribId);
					valueObj.put("UnitOfMeasureCode",productLookupParser.getUnitsOfMeasureCode(productCriteriaSets.getCriteriaCode(),criteriaAtribId,methodName.substring(methodName.indexOf(":")+1)));
					valuesList.add(valueObj);
					currentCriteriaSetValue.setValue(valuesList);
				}
				currentSetCodeValues.setSetCodeValueId(productLookupParser.getMinOrderCodeByName(methodName));
				//
			}
				currentCriteriaSetValue.setCriteriaSetCodeValues(new CriteriaSetCodeValues[]{currentSetCodeValues});
				currentCriteriaSetValueList.add(currentCriteriaSetValue);
				finalProductCriteriaSets.setCriteriaSetValues(currentCriteriaSetValueList);
		}
		return finalProductCriteriaSets;
	}
	private List<com.asi.service.product.vo.CriteriaSetValues> checkImprintMethod(com.asi.service.product.vo.ProductConfigurations productConfiguration,
			com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSets,
			Imprints imprintMethodsList,LookupParser lookupsParser) {
		List<com.asi.service.product.vo.CriteriaSetValues> srcCriteriaSetValues=imprintProductCriteriaSets.getCriteriaSetValues();
		List<com.asi.service.product.vo.CriteriaSetValues> finalCriteriaSetValues=new ArrayList<>();
		com.asi.service.product.vo.CriteriaSetValues currentCriteriaSetValues=null;
		CriteriaSetCodeValues currentSetCodeValues=null;
		// finalCriteriaSetValues=srcCriteriaSetValues;	
		 int newCriteriaSetCntr=-1;
		 int newCriteriaSetCodeValueCntr=0;
		// int displaySeq=1;
		if(null!=imprintMethodsList && imprintMethodsList.getImprintMethod().size()>0)
		{
			if(null!=imprintMethodsList && imprintMethodsList.getImprintMethod().size()>0)
			//finalCriteriaSetValues=Arrays.copyOf(finalCriteriaSetValues, finalCriteriaSetValues.length+imprintMethodsList.getImprintMethod().size());
		//	criteriaSetValuesCntr=finalCriteriaSetValues.length-1;
			for(ImprintMethod crntImprint:imprintMethodsList.getImprintMethod())
			{				
			currentCriteriaSetValues=getCriteriaSetValuesIfExist(srcCriteriaSetValues,crntImprint);
			
			if(null!=currentCriteriaSetValues){
				/*if(currentCriteriaSetValues.getId().contains("-")) { 
					finalCriteriaSetValues.add(currentCriteriaSetValues);
				}			*/
			}else{
				currentCriteriaSetValues=new com.asi.service.product.vo.CriteriaSetValues();
				currentCriteriaSetValues.setId(String.valueOf(newCriteriaSetCntr--));
				currentCriteriaSetValues.setCriteriaCode("IMMD");
				currentCriteriaSetValues.setCriteriaSetId(imprintProductCriteriaSets.getCriteriaSetId());
				currentCriteriaSetValues.setValueTypeCode("LOOK");
				currentCriteriaSetValues.setDisplaySequence("0");
				currentCriteriaSetValues.setIsSubset("false");
				currentCriteriaSetValues.setValue(crntImprint.getMethodName());
				currentCriteriaSetValues.setIsSetValueMeasurement("false");
				currentSetCodeValues=new CriteriaSetCodeValues();
				currentSetCodeValues.setId(String.valueOf(newCriteriaSetCodeValueCntr));
				currentSetCodeValues.setCriteriaSetValueId(currentCriteriaSetValues.getId());
				currentSetCodeValues.setSetCodeValueId(lookupsParser.getImprintCodeByName(crntImprint.getMethodName()));
				currentCriteriaSetValues.setCriteriaSetCodeValues(new CriteriaSetCodeValues[]{currentSetCodeValues});
				//criteriaSetValuesCntr--;
				//finalCriteriaSetValues.add(currentCriteriaSetValues);
				//if(null==finalCriteriaSetValues[criteriaSetValuesCntr]){
					/*finalCriteriaSetValues[criteriaSetValuesCntr]=currentCriteriaSetValues;
					criteriaSetValuesCntr++;*/
				//}
				//else{
					//criteriaSetValuesCntr++;
					//finalCriteriaSetValues[criteriaSetValuesCntr]=currentCriteriaSetValues;
				//}
			}
			// Artwork Processing
			
			finalCriteriaSetValues.add(currentCriteriaSetValues);
			//tempCriteriaSetCntr++;
			}
			
		}	
		return finalCriteriaSetValues;
	}
	private String getRespectiveId(
			com.asi.service.product.client.vo.ProductConfigurations productCriteriaSets,
			String criteriaValue, String criteriaCode) {
		String criteriaId=null;
		String artworkCriteriaValues=null;
		int artworkCntr=0;
		String[] artworkAry=null;
		String currentCriteriaValue="";
		List<LinkedHashMap<String,String>> valueList=null;
		com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSets = getClientProductCriteriaSetIfExist(productCriteriaSets, criteriaCode);
		if(null!=currentProductCriteriaSets && !criteriaValue.trim().isEmpty()){
		for(CriteriaSetValues currentCriteriSetValues:currentProductCriteriaSets.getCriteriaSetValues()){
				if(currentCriteriSetValues.getValue() instanceof String){
					if(criteriaCode.equalsIgnoreCase("ARTW")){ 
					//	artworkCriteriaValues=currentCriteriSetValues.getValue().toString();
						currentCriteriaValue=currentCriteriSetValues.getValue().toString();
						if(null!=currentCriteriaValue && currentCriteriaValue.trim().isEmpty())
						currentCriteriaValue=productLookupParser.getArtworkNameByCode(currentCriteriSetValues.getCriteriaSetCodeValues()[0].getSetCodeValueId());
						//artworkCriteriaValues=getArtworkLookupIds(artworkCriteriaValues);
					}else if(criteriaCode.equalsIgnoreCase("IMMD")){
						currentCriteriaValue=currentCriteriSetValues.getValue().toString();
						if(null!=currentCriteriaValue && currentCriteriaValue.trim().isEmpty())
						currentCriteriaValue=productLookupParser.getImprintNameByCode(currentCriteriSetValues.getCriteriaSetCodeValues()[0].getSetCodeValueId());
				}
				// check criteria set value id
/*				if(criteriaCode.equalsIgnoreCase("ARTW")){
					currentCriteriaValue="";
					if(null!=criteriaValue && !criteriaValue.trim().isEmpty()){
						if(criteriaValue.contains(",")){
						artworkAry=criteriaValue.split(",");
						for(String currentArtwork:artworkAry){
						if(artworkCntr==0)
							currentCriteriaValue=productLookupParser.getArtworkCodeByName(currentArtwork);
						else{
							currentCriteriaValue+=", "+productLookupParser.getArtworkCodeByName(currentArtwork);
						}
						artworkCntr++;
						}
						}else{
							currentCriteriaValue=productLookupParser.getArtworkNameByCode(currentCriteriSetValues.getCriteriaSetCodeValues()[0].getSetCodeValueId());
						}
						if(null!=currentCriteriaValue && !currentCriteriaValue.trim().isEmpty()){
							criteriaId=currentCriteriaValue;
							break;
						}
					}						
				}else*/ if(currentCriteriaValue.equalsIgnoreCase(criteriaValue)){
					criteriaId=currentCriteriSetValues.getId();
					break;
				}
				
				}else if(currentCriteriSetValues.getValue() instanceof List){
					valueList=(List<LinkedHashMap<String,String>>)currentCriteriSetValues.getValue();
					for(LinkedHashMap<String, String> valueObj:valueList){
						
						if(criteriaValue.equalsIgnoreCase(productLookupParser.getElementValue(currentCriteriSetValues.getCriteriaCode(),String.valueOf(valueObj.get("CriteriaAttributeId")),String.valueOf(valueObj.get("UnitValue")),String.valueOf(valueObj.get("UnitOfMeasureCode"))))){
							criteriaId=currentCriteriSetValues.getId();
							break;
						}						
					}
					if(null!=criteriaId)
						break;
				}
				//if()
		}		
		}
		return criteriaId;
	}
	private String getArtworkLookupIds(String artworkName)
	{
		String finalArtworkId=null;
		String[] artworkAry=null;
		int artworkCntr=0;
		if(null!=artworkName && !artworkName.trim().isEmpty()){
			if(artworkName.contains(",")){
			artworkAry=artworkName.split(",");
			for(String currentArtwork:artworkAry){
			if(artworkCntr==0)
				finalArtworkId=productLookupParser.getArtworkCodeByName(currentArtwork);
			else{
				finalArtworkId+=", "+productLookupParser.getArtworkCodeByName(currentArtwork);
			}
			artworkCntr++;
			}
			}else{
				finalArtworkId=productLookupParser.getArtworkNameByCode(artworkName);
			}
	/*		if(null!=currentCriteriaValue && !currentCriteriaValue.trim().isEmpty()){
				criteriaId=currentCriteriaValue;
				break;
			}*/
		}
		return finalArtworkId;
	}
	private com.asi.service.product.vo.CriteriaSetValues getCriteriaSetValuesIfExist(
			List<com.asi.service.product.vo.CriteriaSetValues> srcCriteriaSetValues, ImprintMethod crntImprint) {
		com.asi.service.product.vo.CriteriaSetValues imprintCriteriaSetValues=null;
		for(com.asi.service.product.vo.CriteriaSetValues currentSetValue:srcCriteriaSetValues){
			if(currentSetValue.getValue() instanceof String)
			{
			
				if(!currentSetValue.getValue().toString().trim().isEmpty() && currentSetValue.getValue().toString().equalsIgnoreCase(crntImprint.getMethodName())){
				imprintCriteriaSetValues=currentSetValue;
				break;
				}else{
					if(productLookupParser.getImprintNameByCode(currentSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()).equalsIgnoreCase(crntImprint.getMethodName()))
					{
						///currentCriteriaValue=productLookupParser.getImprintNameByCode(currentCriteriSetValues.getCriteriaSetCodeValues()[0].getSetCodeValueId());
						imprintCriteriaSetValues=currentSetValue;	
						break;
					}
					
				}
				//if()
			}
		}
		
		return imprintCriteriaSetValues;
	}
	private ProductConfigurations[] replaceOrAddIfImprintExist(
			ProductConfigurations[] productConfigAry, com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSets,String criteriaCode) {
		com.asi.service.product.client.vo.ProductCriteriaSets clientImprintProductCriteriaSet=getClientProductCriteriaSetIfExist(productConfigAry[0],criteriaCode);
		List<com.asi.service.product.client.vo.ProductCriteriaSets> clientProductCriteriaSets=productConfigAry[0].getProductCriteriaSets();
		ProductCriteriaSets productCriteriaSets=new ProductCriteriaSets();
		if(null!=clientImprintProductCriteriaSet)
		{
			// replace
			findAndReplaceImprintCriteriaSet(clientProductCriteriaSets,imprintProductCriteriaSets);
		}else{
			// add
			//clientProductCriteriaSets=Arrays.copyOf(clientProductCriteriaSets, clientProductCriteriaSets.length+1);
			productCriteriaSets=transformProductCriteriaSet(imprintProductCriteriaSets);
			clientProductCriteriaSets.add(productCriteriaSets);
		}
			
		return productConfigAry;
	}
	private void findAndReplaceImprintCriteriaSet(
			List<ProductCriteriaSets> clientProductCriteriaSets,
			com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSets) {
		int criteriaSetCntr=0;
		for(ProductCriteriaSets crntProductCriteriaSet:clientProductCriteriaSets){
			if(crntProductCriteriaSet.getCriteriaCode().equalsIgnoreCase("IMMD")){
				clientProductCriteriaSets.add(transformProductCriteriaSet(imprintProductCriteriaSets));
			}
			criteriaSetCntr++;
		}
	}
	private ProductCriteriaSets transformProductCriteriaSet(
			com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSets) {
		ProductCriteriaSets finalProductCriteriaSet=new ProductCriteriaSets();
		com.asi.service.product.client.vo.CriteriaSetValues clientCriteriaSetVallues=null;
		List<com.asi.service.product.client.vo.CriteriaSetValues> clientCriteriaSetValluesAry=new ArrayList<>();
		BeanUtils.copyProperties(imprintProductCriteriaSets, finalProductCriteriaSet);
		List<com.asi.service.product.client.vo.CriteriaSetValues> tempCriteriaList=new ArrayList<com.asi.service.product.client.vo.CriteriaSetValues>();
		//clientCriteriaSetValluesAry=new com.asi.service.product.client.vo.CriteriaSetValues[imprintProductCriteriaSets.getCriteriaSetValues().size()];
		for(com.asi.service.product.vo.CriteriaSetValues crntCriteriaSetValues:imprintProductCriteriaSets.getCriteriaSetValues())
		{
			clientCriteriaSetVallues=new com.asi.service.product.client.vo.CriteriaSetValues();
			BeanUtils.copyProperties(crntCriteriaSetValues,clientCriteriaSetVallues);
			tempCriteriaList.add(clientCriteriaSetVallues);
			finalProductCriteriaSet.setCriteriaSetValues(tempCriteriaList);
			clientCriteriaSetVallues.setCriteriaSetCodeValues(crntCriteriaSetValues.getCriteriaSetCodeValues());
			clientCriteriaSetValluesAry.add(clientCriteriaSetVallues);
		}
		finalProductCriteriaSet.setCriteriaSetValues(clientCriteriaSetValluesAry);
		return finalProductCriteriaSet;
	}
	private com.asi.service.product.vo.ProductCriteriaSets getProductCriteriaSetByCodeIfExist(
			com.asi.service.product.vo.ProductConfigurations productConfiguration,String criteriaCode) {
		com.asi.service.product.vo.ProductCriteriaSets imprintProductCriteriaSet=null;
		com.asi.service.product.vo.ProductCriteriaSets[] productCriteriaSetsAry=null;
		if(null!=productConfiguration){
			productCriteriaSetsAry=productConfiguration.getProductCriteriaSets();
			for(com.asi.service.product.vo.ProductCriteriaSets currentProductCriteriaSet:productCriteriaSetsAry){
				if(currentProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode)){
					imprintProductCriteriaSet=currentProductCriteriaSet;
					break;
				}
			}
		}
		return imprintProductCriteriaSet;
	}
	private com.asi.service.product.client.vo.ProductCriteriaSets getClientProductCriteriaSetIfExist(
			com.asi.service.product.client.vo.ProductConfigurations productConfiguration,String criteriaCode) {
		com.asi.service.product.client.vo.ProductCriteriaSets imprintProductCriteriaSet=null;
		List<com.asi.service.product.client.vo.ProductCriteriaSets> productCriteriaSetsAry=null;
		if(null!=productConfiguration){
			productCriteriaSetsAry=productConfiguration.getProductCriteriaSets();
			for(com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSet:productCriteriaSetsAry){
				if(currentProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode)){
					imprintProductCriteriaSet=currentProductCriteriaSet;
				}
			}
		}
		return imprintProductCriteriaSet;
	}
	public com.asi.service.product.client.vo.Product transformProductColors(
			String productColor) {

		return null;
	}
	public com.asi.service.product.client.vo.Product setProductWithProductConfigurations(
			Product srcProduct,
			com.asi.service.product.client.vo.Product productToUpdate,
			LookupParser lookupsParser, String string, String productColor) {
		if(null!=productColor && !productColor.isEmpty()){
		com.asi.service.product.vo.ProductCriteriaSets colorsProductCriteriaSet=getProductCriteriaSetByCodeIfExist(srcProduct.getProductConfigurations()[0],"PRCL");
		ProductCriteriaSets clientColorsProductCriteriaSet=new ProductCriteriaSets();
		if(null==colorsProductCriteriaSet){
			clientColorsProductCriteriaSet.setCompanyId(srcProduct.getCompanyId());
			clientColorsProductCriteriaSet.setProductId(String.valueOf(srcProduct.getID()));
			clientColorsProductCriteriaSet.setConfigId("0");
			clientColorsProductCriteriaSet.setCriteriaCode("PRCL");
			clientColorsProductCriteriaSet.setCriteriaSetId(String.valueOf(productCriteriaSetCntr));
			productCriteriaSetCntr--;
			clientColorsProductCriteriaSet.setCriteriaSetValues(new ArrayList<CriteriaSetValues>());
		}else{
			BeanUtils.copyProperties(colorsProductCriteriaSet, clientColorsProductCriteriaSet);
		}
		//CriteriaSetValues	
		String[] colors=productColor.split(",");
		boolean criteriaSetValueExist=false;
		List<CriteriaSetValues> clientCriteriaSetValuesList=new ArrayList<>();
		CriteriaSetValues tempCriteriaSetValues=null;
		CriteriaSetCodeValues tempCriteriaSetCodeValues=null;
		CriteriaSetCodeValues[] tempCriteriaSetCodeValuesList=new CriteriaSetCodeValues[1];
		for(String currentColor:colors)
		{
			//if(null!=clientColorsProductCriteriaSet.getCriteriaSetValues() && clientColorsProductCriteriaSet.getCriteriaSetValues().size()>0){
				for(CriteriaSetValues currentCriteriaSetValues: clientColorsProductCriteriaSet.getCriteriaSetValues()){
					if(currentCriteriaSetValues.getValue().toString().equalsIgnoreCase(currentColor)){
						clientCriteriaSetValuesList.add(currentCriteriaSetValues);
						criteriaSetValueExist=true;
					}else if(productLookupParser.getColorNameByCode(currentCriteriaSetValues.getCriteriaSetCodeValues()[0].getSetCodeValueId()).equalsIgnoreCase(currentColor)){
						clientCriteriaSetValuesList.add(currentCriteriaSetValues);
						criteriaSetValueExist=true;
					}					
				}
				if(!criteriaSetValueExist){
					tempCriteriaSetValues=new CriteriaSetValues();
					tempCriteriaSetValues.setId(String.valueOf(newCriteriaSetValuesCntr));
					newCriteriaSetValuesCntr--;
					tempCriteriaSetValues.setCriteriaSetId(clientColorsProductCriteriaSet.getCriteriaSetId());
					tempCriteriaSetValues.setCriteriaCode("PRCL");
					tempCriteriaSetValues.setValue(currentColor);
					tempCriteriaSetValues.setValueTypeCode("LOOK");
					tempCriteriaSetValues.setIsSubset("false");
					tempCriteriaSetValues.setIsSetValueMeasurement("false");
					tempCriteriaSetCodeValues=new CriteriaSetCodeValues();
					tempCriteriaSetCodeValues.setId(String.valueOf(newCriteriaSetCodeValueCntr));
					newCriteriaSetCodeValueCntr--;
					tempCriteriaSetCodeValues.setCriteriaSetValueId(tempCriteriaSetValues.getId());
					tempCriteriaSetCodeValues.setSetCodeValueId(productLookupParser.getColorCodeByName(currentColor));
					tempCriteriaSetCodeValuesList[0]=tempCriteriaSetCodeValues;
					tempCriteriaSetValues.setCriteriaSetCodeValues(tempCriteriaSetCodeValuesList);
					clientCriteriaSetValuesList.add(tempCriteriaSetValues);
				}
		//	}
		}
		clientColorsProductCriteriaSet.setCriteriaSetValues(clientCriteriaSetValuesList);
		ProductConfigurations[] productConfigList=new ProductConfigurations[1];
		List<ProductCriteriaSets> productCriteriaSetsList=new ArrayList<>();
		productCriteriaSetsList.add(clientColorsProductCriteriaSet);
		ProductConfigurations currentProductConfigurations=new ProductConfigurations();
		currentProductConfigurations.setProductId(String.valueOf(srcProduct.getID()));
		currentProductConfigurations.setIsDefault("true");
		currentProductConfigurations.setProductCriteriaSets(productCriteriaSetsList);
		productConfigList[0]=currentProductConfigurations;
		//List<ProductCriteriaSets> currentProductCriteriaSetList=productToUpdate.getProductConfigurations()[0].getProductCriteriaSets();
	//	currentProductCriteriaSetList.add(clientColorsProductCriteriaSet);
		productToUpdate.setProductConfigurations(productConfigList);//[0].setProductCriteriaSets(currentProductCriteriaSetList);
		}
		return productToUpdate;
	}
	
}
