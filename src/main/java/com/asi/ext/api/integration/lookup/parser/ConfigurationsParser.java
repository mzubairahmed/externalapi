package com.asi.ext.api.integration.lookup.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.BlendMaterial;
import com.asi.ext.api.service.model.Color;
import com.asi.ext.api.service.model.Combo;
import com.asi.ext.api.service.model.Dimensions;
import com.asi.ext.api.service.model.ImprintColor;
import com.asi.ext.api.service.model.ImprintMethod;
import com.asi.ext.api.service.model.ImprintSizeLocation;
import com.asi.ext.api.service.model.Material;
import com.asi.ext.api.service.model.ProductionTime;
import com.asi.ext.api.service.model.RushTime;
import com.asi.ext.api.service.model.SameDayRush;
import com.asi.ext.api.service.model.Samples;
import com.asi.ext.api.service.model.ShippingEstimate;
import com.asi.ext.api.service.model.Size;
import com.asi.ext.api.service.model.Value;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.product.client.vo.ChildCriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductConfigurations;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.vo.Product;

public class ConfigurationsParser {
	@Autowired
	LookupParser productLookupParser;
	public LookupParser getProductLookupParser() {
		return productLookupParser;
	}

	public void setProductLookupParser(LookupParser productLookupParser) {
		this.productLookupParser = productLookupParser;
	}

	public static final String[] SIZE_GROUP_CRITERIACODES={"CAPS","DIMS","SABR","SAHU","SAIT","SANS","SAWI","SSNM","SVWT","SOTH"};
	private final static Logger _LOGGER = Logger
			.getLogger(ConfigurationsParser.class.getName());
//	private HashMap<String, HashMap<String, String>> criteriaSet = new HashMap<>();
	private int productCriteriaSetCntr = -1;
	private int newCriteriaSetCodeValueCntr = -112;
	private int newCriteriaSetValuesCntr = -1;
	private String breakoutBy=null;
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser();
	/*public String[] getPriceCriteria(ProductDetail productDetail,
			String priceGridId) {
		String[] priceCrterias = null;
		String criteriaOne = "", criteria1Value = "";
		String criteriaTwo = "", criteria2Value = "";
		String currentCriteria = "";
		if (null != productDetail && null != priceGridId) {
			priceCrterias = new String[2];
			String externalId = productDetail.getExternalProductId();
			if (criteriaSet.isEmpty() && null != externalId) {
				criteriaSet = setCriteriaSet(productDetail, externalId);
			}
			if (!criteriaSet.isEmpty() && null != productDetail.getPriceGrids()) {
				for (PriceGrid currentPricingItem : productDetail
						.getPriceGrids()) {
					if (currentPricingItem.getIsBasePrice()
							&& null != currentPricingItem.getPricingItems()) {
						for (PricingItem pricingItem : currentPricingItem
								.getPricingItems()) {
							currentCriteria = criteriaSet.get(externalId).get(
									pricingItem.getCriteriaSetValueId()
											.toString());
							if (null != currentCriteria
									&& priceGridId.toString().equalsIgnoreCase(
											pricingItem.getPriceGridId())) {
								if (criteriaOne.isEmpty()) {
									criteriaOne = currentCriteria.substring(0,
											currentCriteria.indexOf(":"));
									criteria1Value = currentCriteria
											.substring(currentCriteria
													.indexOf(":") + 1);
								} else if (criteriaTwo.isEmpty()
										&& !criteriaOne
												.equalsIgnoreCase(currentCriteria
														.substring(
																0,
																currentCriteria
																		.indexOf(":")))) {
									criteriaTwo = currentCriteria.substring(0,
											currentCriteria.indexOf(":"));
									criteria2Value = currentCriteria
											.substring(currentCriteria
													.indexOf(":") + 1);
								} else if (criteriaOne
										.equalsIgnoreCase(currentCriteria
												.substring(0, currentCriteria
														.indexOf(":")))) {
									criteria1Value += ","
											+ currentCriteria
													.substring(currentCriteria
															.indexOf(":") + 1);
								} else if (criteriaTwo
										.equalsIgnoreCase(currentCriteria
												.substring(0, currentCriteria
														.indexOf(":")))) {
									criteria2Value += ","
											+ currentCriteria
													.substring(currentCriteria
															.indexOf(":") + 1);
								}
								if (!criteriaOne.isEmpty()
										&& !criteriaTwo.isEmpty()
										&& !criteriaOne
												.equalsIgnoreCase(currentCriteria
														.substring(
																0,
																currentCriteria
																		.indexOf(":")))
										&& !criteriaTwo
												.equalsIgnoreCase(currentCriteria
														.substring(
																0,
																currentCriteria
																		.indexOf(":")))) {
									_LOGGER.info("InValid Price Criteria :"
											+ currentCriteria);
								}
							}
						}
					}
				}
			}
			criteriaSet = new HashMap<>();
			priceCrterias[0] = (!criteriaOne.isEmpty()) ? criteriaOne + ":"
					+ criteria1Value : "";
			priceCrterias[1] = (!criteriaTwo.isEmpty()) ? criteriaTwo + ":"
					+ criteria2Value : "";
		}
		return priceCrterias;
	}
*/
	/*private HashMap<String, HashMap<String, String>> setCriteriaSet(
			ProductDetail productDetails, String externalId) {
		HashMap<String, HashMap<String, String>> currentHashMap = new HashMap<>();
		HashMap<String, String> productCriteriSets = new HashMap<>();
		ArrayList<ProductConfiguration> productConfigurationList = (ArrayList<ProductConfiguration>) productDetails
				.getProductConfigurations();
		for (ProductConfiguration currentProductConfiguration : productConfigurationList) {
			for (ProductCriteriaSets currentProductCriteriSet : currentProductConfiguration
					.getProductCriteriaSets()) {
				for (CriteriaSetValues currentCriteria : currentProductCriteriSet
						.getCriteriaSetValues()) {
					if (currentCriteria.getValue() instanceof String) {
						productCriteriSets.put(currentCriteria.getId()
								.toString(), currentCriteria.getCriteriaCode()
								+ ":" + currentCriteria.getValue().toString());
					} else if (currentCriteria.getValue() instanceof ArrayList) {
						productCriteriSets.put(
								currentCriteria.getId().toString(),
								currentCriteria.getCriteriaCode()
										+ ":"
										+ productLookupParser.getValueString(
												(ArrayList<?>) currentCriteria
														.getValue(),
												currentCriteria
														.getCriteriaCode()));
					}
				}
			}
		}
		currentHashMap.put(externalId, productCriteriSets);
		return currentHashMap;
	}*/

	public com.asi.service.product.client.vo.Product setProductWithProductConfigurations(
			Product srcProduct, ProductDetail currentProductDetails,
			com.asi.service.product.client.vo.Product productToUpdate,
			LookupParser lookupsParser, String criteriaCode,
			String criteriaValue) throws RestClientException, UnsupportedEncodingException {
		if (null != criteriaValue && !criteriaValue.isEmpty()) {
			com.asi.service.product.client.vo.ProductCriteriaSets existingProductCriteriaSet = getProductCriteriaSetByCodeIfExist(
					currentProductDetails.getProductConfigurations().get(0),
					criteriaCode);
			ProductCriteriaSets clientProductCriteriaSet = new ProductCriteriaSets();
			if (null == existingProductCriteriaSet) {
				clientProductCriteriaSet
						.setCompanyId(srcProduct.getCompanyId());
				clientProductCriteriaSet.setProductId(String.valueOf(srcProduct
						.getID()));
				clientProductCriteriaSet.setConfigId("0");
				clientProductCriteriaSet.setCriteriaCode(criteriaCode);
				clientProductCriteriaSet.setCriteriaSetId(String
						.valueOf(productCriteriaSetCntr));
				productCriteriaSetCntr--;
				clientProductCriteriaSet
						.setCriteriaSetValues(new ArrayList<CriteriaSetValues>());
			} else {
				// BeanUtils.copyProperties(colorsProductCriteriaSet,
				// clientColorsProductCriteriaSet);
				clientProductCriteriaSet
						.setCompanyId(existingProductCriteriaSet.getCompanyId());
				clientProductCriteriaSet.setProductId(String.valueOf(srcProduct
						.getID()));
				clientProductCriteriaSet.setConfigId(existingProductCriteriaSet
						.getConfigId());
				clientProductCriteriaSet
						.setCriteriaCode(existingProductCriteriaSet
								.getCriteriaCode());
				clientProductCriteriaSet
						.setCriteriaSetId(existingProductCriteriaSet
								.getCriteriaSetId());
				// clientColorsProductCriteriaSet.setCriteriaSetValues(transformCriteriaSetValues(colorsProductCriteriaSet.getCriteriaSetValues()));
			}
			// CriteriaSetValues
			String[] criteriaItems = criteriaValue.split(",");
			String customColorName="";
			boolean criteriaSetValueExist = false;
			List<CriteriaSetValues> clientCriteriaSetValuesList = new ArrayList<>();
			CriteriaSetCodeValues tempCriteriaSetCodeValues = null;
			String currentCriteriaSetValue = null;
			CriteriaSetCodeValues[] tempCriteriaSetCodeValuesList = new CriteriaSetCodeValues[1];
			com.asi.service.product.client.vo.CriteriaSetValues clientCurrentCriteriaSetValues = null;
			for (String critieriaItem : criteriaItems) {
				if(criteriaCode.equalsIgnoreCase("PRCL") && critieriaItem.contains("=")){
					customColorName=critieriaItem.substring(critieriaItem.indexOf("=")+1).trim();
					critieriaItem=critieriaItem.substring(0,critieriaItem.indexOf("=")).trim();
				}
				// if(null!=clientColorsProductCriteriaSet.getCriteriaSetValues()
				// &&
				// clientColorsProductCriteriaSet.getCriteriaSetValues().size()>0){
				if (null != existingProductCriteriaSet
						&& null != existingProductCriteriaSet
								.getCriteriaSetValues()
						&& existingProductCriteriaSet.getCriteriaSetValues()
								.size() > 0) {
					for (com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValues : existingProductCriteriaSet
							.getCriteriaSetValues()) {
						
			/*	today comment		currentCriteriaSetValue = productLookupParser
								.getSetValueNameByCode(currentCriteriaSetValues
										.getCriteriaSetCodeValues()[0]
										.getSetCodeValueId(), criteriaCode,
										currentCriteriaSetValues.getValue());*/
						if (currentCriteriaSetValues.getValue().toString()
								.trim().equalsIgnoreCase(critieriaItem.trim())) {
							clientCurrentCriteriaSetValues = new CriteriaSetValues();
							// clientCurrentCriteriaSetValues=transformCriteriaSetValues(currentCriteriaSetValues);
							// BeanUtils.copyProperties(currentCriteriaSetValues,
							// clientCurrentCriteriaSetValues);
							currentCriteriaSetValues.getCriteriaSetCodeValues()[0]
									.setId(String
											.valueOf(newCriteriaSetCodeValueCntr));
							clientCriteriaSetValuesList
									.add(currentCriteriaSetValues);
							// currentCriteriaSetValues=null;
							criteriaSetValueExist = true;
							break;
						} else if (null != currentCriteriaSetValue
								&& currentCriteriaSetValue
										.equalsIgnoreCase(critieriaItem.trim())) {
							clientCurrentCriteriaSetValues = new CriteriaSetValues();
							// clientCurrentCriteriaSetValues=transformCriteriaSetValues(currentCriteriaSetValues);
							// BeanUtils.copyProperties(currentCriteriaSetValues,
							// clientCurrentCriteriaSetValues);
							currentCriteriaSetValues.getCriteriaSetCodeValues()[0]
									.setId(String
											.valueOf(newCriteriaSetCodeValueCntr));
							clientCriteriaSetValuesList
									.add(currentCriteriaSetValues);
							// currentCriteriaSetValues=null;
							criteriaSetValueExist = true;
							break;
						}
					}
				}
				if (!criteriaSetValueExist) {
					clientCurrentCriteriaSetValues = new CriteriaSetValues();
					clientCurrentCriteriaSetValues.setId(String
							.valueOf(newCriteriaSetValuesCntr));
					//
					clientCurrentCriteriaSetValues
							.setCriteriaSetId(clientProductCriteriaSet
									.getCriteriaSetId());
					clientCurrentCriteriaSetValues
							.setCriteriaCode(criteriaCode);
					if(criteriaCode.equals("PRCL") && !customColorName.trim().isEmpty()){
						clientCurrentCriteriaSetValues.setValue(customColorName);
					}else{				
						clientCurrentCriteriaSetValues.setValue(critieriaItem.trim());
					}
					clientCurrentCriteriaSetValues.setValueTypeCode("LOOK");
					clientCurrentCriteriaSetValues.setIsSubset("false");
					clientCurrentCriteriaSetValues.setFormatValue(critieriaItem
							.trim());
					clientCurrentCriteriaSetValues
							.setIsSetValueMeasurement("false");
					tempCriteriaSetCodeValues = new CriteriaSetCodeValues();
					tempCriteriaSetCodeValues.setId(String
							.valueOf(newCriteriaSetCodeValueCntr));
					tempCriteriaSetCodeValues
							.setCriteriaSetValueId(clientCurrentCriteriaSetValues
									.getId());
				/*	today comment	tempCriteriaSetCodeValues
							.setSetCodeValueId(productLookupParser
									.getSetCodeByName(criteriaCode,
											critieriaItem.trim()));*/
					tempCriteriaSetCodeValuesList[0] = tempCriteriaSetCodeValues;
					clientCurrentCriteriaSetValues
							.setCriteriaSetCodeValues(tempCriteriaSetCodeValuesList);
					clientCriteriaSetValuesList
							.add(clientCurrentCriteriaSetValues);
				}
				criteriaSetValueExist = false;
				newCriteriaSetValuesCntr--;
				newCriteriaSetCodeValueCntr--;
				// }
			}
			clientProductCriteriaSet
					.setCriteriaSetValues(clientCriteriaSetValuesList);
			ProductConfigurations[] productConfigList = new ProductConfigurations[1];
			List<ProductCriteriaSets> productCriteriaSetsList = new ArrayList<>();
			if (null != currentProductDetails
					&& null != currentProductDetails.getProductConfigurations()
					&& currentProductDetails.getProductConfigurations().size() > 0
					&& null != currentProductDetails.getProductConfigurations()
							.get(0).getProductCriteriaSets()) {
			/*	today comment	productCriteriaSetsList = addOrUpdateProductCriteriaSetsList(
						clientProductCriteriaSet, currentProductDetails
								.getProductConfigurations().get(0)
								.getProductCriteriaSets(),
						productToUpdate.getProductConfigurations());*/
			} else {
				productCriteriaSetsList.add(clientProductCriteriaSet);
			}
			ProductConfigurations currentProductConfigurations = new ProductConfigurations();
			currentProductConfigurations.setProductId(String.valueOf(srcProduct
					.getID()));
			currentProductConfigurations.setIsDefault("true");
			currentProductConfigurations
					.setProductCriteriaSets(productCriteriaSetsList);
			productConfigList[0] = currentProductConfigurations;
			// List<ProductCriteriaSets>
			// currentProductCriteriaSetList=productToUpdate.getProductConfigurations()[0].getProductCriteriaSets();
			// currentProductCriteriaSetList.add(clientColorsProductCriteriaSet);
			productToUpdate.setProductConfigurations(productConfigList);// [0].setProductCriteriaSets(currentProductCriteriaSetList);
		}
		return productToUpdate;
	}

/*	private List<ProductCriteriaSets> addOrUpdateProductCriteriaSetsList(
			ProductCriteriaSets clientProductCriteriaSet,
			List<ProductCriteriaSets> productCriteriaSets,
			ProductConfigurations[] productConfigurations) {
		List<ProductCriteriaSets> prodToUpdateCriteriaSetsList = new ArrayList<>();
		if (null != productConfigurations && productConfigurations.length > 0) {
			prodToUpdateCriteriaSetsList = productConfigurations[0]
					.getProductCriteriaSets();
		}
		String criteriaCode = clientProductCriteriaSet.getCriteriaCode();
		// List<ProductCriteriaSets> finalProductCriteriaSets=new ArrayList<>();
		if (productCriteriaSets.size() == 0) {
			prodToUpdateCriteriaSetsList.add(clientProductCriteriaSet);
			// finalProductCriteriaSets=prodToUpdateCriteriaSetsList;
		} else {
			for (ProductCriteriaSets currentProductCriteriaSet : productCriteriaSets) {
				if (!isCriteriaExist(prodToUpdateCriteriaSetsList, criteriaCode)) {
					if (criteriaCode.equalsIgnoreCase(currentProductCriteriaSet
							.getCriteriaCode())) {
						prodToUpdateCriteriaSetsList
								.add(clientProductCriteriaSet);
					} else {
						if (!clientProductCriteriaSet.getCriteriaCode()
								.equalsIgnoreCase(
										currentProductCriteriaSet
												.getCriteriaCode()))
							prodToUpdateCriteriaSetsList
									.add(clientProductCriteriaSet);
					}
				}
			}
		}
		return prodToUpdateCriteriaSetsList;
	}

	private boolean isCriteriaExist(
			List<ProductCriteriaSets> productCriteriaSetsList,
			String criteriaCode) {
		boolean criteriaAvailable = false;
		for (ProductCriteriaSets currentProductCriteriaSets : productCriteriaSetsList) {
			if (currentProductCriteriaSets.getCriteriaCode().equalsIgnoreCase(
					criteriaCode)) {
				criteriaAvailable = true;
			}
		}
		return criteriaAvailable;
	}
*/
	private com.asi.service.product.client.vo.ProductCriteriaSets getProductCriteriaSetByCodeIfExist(
			com.asi.service.product.client.vo.ProductConfiguration productConfiguration,
			String criteriaCode) {
		String[] breakoutCritierias={"PRCL","SHAP","MTRL","CAPS","DIMS","SABR","SAHU","SAIT","SANS","SAWI","SSNM","SVWT","SOTH","PROP","IMOP","SHOP"};
		com.asi.service.product.client.vo.ProductCriteriaSets imprintProductCriteriaSet = null;
		List<com.asi.service.product.client.vo.ProductCriteriaSets> productCriteriaSetsAry = new ArrayList<>();
		if (null != productConfiguration) {
			productCriteriaSetsAry = productConfiguration
					.getProductCriteriaSets();
		
			for (com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSet : productCriteriaSetsAry) {
				if(Arrays.asList(breakoutCritierias).contains(currentProductCriteriaSet.getCriteriaCode()) && currentProductCriteriaSet.getIsBrokenOutOn().equalsIgnoreCase("true")){
					breakoutBy=ProductDataStore.getCriteriaInfoForCriteriaCode(currentProductCriteriaSet.getCriteriaCode()).getDescription();
				}
				if (currentProductCriteriaSet.getCriteriaCode()
						.equalsIgnoreCase(criteriaCode)) {
					imprintProductCriteriaSet = currentProductCriteriaSet;
					break;
				}
			}
		}
		return imprintProductCriteriaSet;
	}

/*	private List<CriteriaSetValues> transformCriteriaSetValues(
			List<com.asi.service.product.client.vo.CriteriaSetValues> list) {
		List<CriteriaSetValues> criteriaSetValuesAry = new ArrayList<>();
		// CriteriaSetValues clientCriteriaSetValues=null;
		if (null != list && list.size() > 0) {
			// criteriaSetValuesAry=new
			// CriteriaSetValues[criteriaSetValues.length];
			for (com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValues : list) {
				// clientCriteriaSetValues=new CriteriaSetValues();
				// clientCriteriaSetValues=transformCriteriaSetValues(currentCriteriaSetValues);
				// BeanUtils.copyProperties(currentCriteriaSetValues,
				// clientCriteriaSetValues);
				criteriaSetValuesAry.add(currentCriteriaSetValues);
			}
		}
		return criteriaSetValuesAry;
	}*/

	public com.asi.ext.api.service.model.Product setProductWithConfigurations(
			ProductDetail productDetail,
			com.asi.ext.api.service.model.Product serviceProduct) {
		com.asi.ext.api.service.model.ProductConfigurations serviceProductConfig=new com.asi.ext.api.service.model.ProductConfigurations(); 
		// Break out check
		 if(productDetail.getIsPriceBreakoutFlag()){
			 breakoutBy="Product Number";		 			
		 		}
		 serviceProduct.setBreakOutByPrice(String.valueOf(productDetail.getIsPriceBreakoutFlag()));
		// Product Color
		List<com.asi.service.product.client.vo.CriteriaSetValues> currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			Color currentColor;
			String crntColor;
			List<Combo> comboList=new ArrayList<>();
			Combo currentCombo=null;
			List<Color> colorsList=new ArrayList<>();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				currentColor=new Color();
				if(currentCriteriaSetValue.getCriteriaSetCodeValues().length>1){
					// Combo
					for(com.asi.service.product.client.vo.CriteriaSetCodeValues currentCriteriaSetCodeValue:currentCriteriaSetValue.getCriteriaSetCodeValues()){
						if(currentCriteriaSetCodeValue.getCodeValueDetail().equalsIgnoreCase("main")){
							currentColor.setAlias(currentCriteriaSetValue.getValue().toString());
							currentColor.setName(ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetCodeValue.getSetCodeValueId(),ApplicationConstants.CONST_COLORS_CRITERIA_CODE));
						}else{
							currentCombo=new Combo();
							currentCombo.setName(ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetCodeValue.getSetCodeValueId(),ApplicationConstants.CONST_COLORS_CRITERIA_CODE));
							currentCombo.setType(currentCriteriaSetCodeValue.getCodeValueDetail());
							comboList.add(currentCombo);
						}
					}
					currentColor.setCombos(comboList);				
					criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), currentCriteriaSetValue.getValue().toString());
				}else{
				currentColor.setAlias(currentCriteriaSetValue.getValue().toString());
				crntColor=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
						criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), crntColor);		
				currentColor.setName(crntColor);
				}
				colorsList.add(currentColor);
			}
			serviceProductConfig.setColors(colorsList);
		}
		
		// Origin
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE);
		String currentOrigin;
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			
			List<String> originsList=new ArrayList<>();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				currentOrigin=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE);
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), currentOrigin);	
				originsList.add(currentOrigin);
			}
			serviceProductConfig.setOrigins(originsList);
		}
		
		// Packaging
				currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
				if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
					String packageName="";
					List<String> packageList=new ArrayList<>();
					for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
						packageName=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
						if(packageName.equalsIgnoreCase("Custom")){
							packageName=currentCriteriaSetValue.getValue().toString();
						}
						criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), packageName);
						packageList.add(packageName);
					}
					serviceProductConfig.setPackaging(packageList);
				}
		// Shapes
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_SHAPE_CRITERIA_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			List<String> shapesList=new ArrayList<>();
		    String crntShape;
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				crntShape=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_SHAPE_CRITERIA_CODE);
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), crntShape);
				shapesList.add(crntShape);
			}
			serviceProductConfig.setShapes(shapesList);
		}
		
		// Imprint Colors
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			ImprintColor currentImprintcolor=new ImprintColor();
			String currentImprintColorValue="";
			String[] imprintColrsAry;
			List<String> imprColrValues=null;
			HashMap<String,String> imprintColorByValueTypeCode=getImprintColorsByTypeCode(currentCriteriaSetValueList,productDetail.getExternalProductId());
			@SuppressWarnings("rawtypes")
			Iterator itr = imprintColorByValueTypeCode.entrySet().iterator();
		    while (itr.hasNext()) {
		        @SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry)itr.next();
		        currentImprintcolor.setType(pairs.getKey().toString());
		       currentImprintColorValue=(pairs.getValue()==null)?"":pairs.getValue().toString();
		        if(currentImprintColorValue.contains(",")){
		        	imprColrValues=new ArrayList<>();
		        	imprintColrsAry=currentImprintColorValue.split(",");
		        	for(String crntImprintColr:imprintColrsAry){
		        		imprColrValues.add(crntImprintColr);
		        	}
		        	currentImprintcolor.setValues(imprColrValues);
		        }else{
		        	currentImprintcolor.getValues().add(currentImprintColorValue);
		        }	
		        itr.remove(); 
		    }
			serviceProductConfig.setImprintColors(currentImprintcolor);
		}
		
		// Imprint Size / Location
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			List<ImprintSizeLocation> imprintSzLnList=new ArrayList<>();
			String imprintSizeLocation="";
			ImprintSizeLocation currentImprintSizeLocation=null;
			for(CriteriaSetValues currentCriteriasetValue:currentCriteriaSetValueList){
				currentImprintSizeLocation=new ImprintSizeLocation();
				imprintSizeLocation=currentCriteriasetValue.getValue().toString();
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriasetValue.getCriteriaCode(), Integer.parseInt(currentCriteriasetValue.getId()), imprintSizeLocation);
				if(imprintSizeLocation.contains("|")){
				currentImprintSizeLocation.setSize(imprintSizeLocation.substring(0,imprintSizeLocation.indexOf("|")));
				currentImprintSizeLocation.setLocation(imprintSizeLocation.substring(imprintSizeLocation.indexOf("|")+1));
				imprintSzLnList.add(currentImprintSizeLocation);
				}
			}			
			serviceProductConfig.setImprintSizeLocations(imprintSzLnList);		
		}
		
		//Rush Time
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			List<RushTime> rushTimeList=new ArrayList<>();
			RushTime rushTime;
			for(CriteriaSetValues currentCriteriasetValue:currentCriteriaSetValueList){
				rushTime=new RushTime();
				if(currentCriteriasetValue.getValue() instanceof List){
				rushTime.setBusinessDays(Integer.parseInt(productLookupParser.getTimeText(currentCriteriasetValue.getValue())));
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriasetValue.getCriteriaCode(), Integer.parseInt(currentCriteriasetValue.getId()), productLookupParser.getTimeText(currentCriteriasetValue.getValue()));
				rushTime.setDetails(currentCriteriasetValue.getCriteriaValueDetail());
				}else{
					rushTime.setDetails("");
					rushTime.setBusinessDays(0);
				}
				
				rushTimeList.add(rushTime);
			}		
			serviceProductConfig.setRushTime(rushTimeList);
		}
		
		// Materials
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE);
		String materialName="";
		
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			Material material;
			List<Material> materialList=new ArrayList<>();
			Combo currentCombo=null;
			boolean firstElement=false;
			List<Combo> comboList=null;
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				material=new Material();
				firstElement=true;
				comboList=new ArrayList<>();
				if(currentCriteriaSetValue.getCriteriaSetCodeValues().length>1){
					// Combo
					for(com.asi.service.product.client.vo.CriteriaSetCodeValues currentCriteriaSetCodeValue:currentCriteriaSetValue.getCriteriaSetCodeValues()){
						if(firstElement){
							material.setAlias(currentCriteriaSetValue.getValue().toString());
							materialName=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetCodeValue.getSetCodeValueId(),ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE);
							material.setName(materialName);
							material.setBlendMaterials(checkAndSetMaterialBlendInfo(currentCriteriaSetCodeValue.getChildCriteriaSetCodeValues()));
							criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), materialName);
							firstElement=false;
						}else{
							currentCombo=new Combo();
							currentCombo.setName(ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetCodeValue.getSetCodeValueId(),ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE));
							currentCombo.setBlendMaterials(checkAndSetMaterialBlendInfo(currentCriteriaSetCodeValue.getChildCriteriaSetCodeValues()));
							material.setCombo(currentCombo);
						}						
					}
					
				}else{
					material.setAlias(currentCriteriaSetValue.getValue().toString());
					materialName=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE);
					material.setName(materialName);
					criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), materialName);
					material.setBlendMaterials(checkAndSetMaterialBlendInfo(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getChildCriteriaSetCodeValues()));
				}
				materialList.add(material);
		}
		serviceProductConfig.setMaterials(materialList);
		}

		// Same Day Rush
		SameDayRush sdrush=null;
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			sdrush=new SameDayRush();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
			sdrush.setAvailable("true");
			sdrush.setDetails(currentCriteriaSetValue.getCriteriaValueDetail());
			criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), currentCriteriaSetValue.getCriteriaValueDetail());
			serviceProductConfig.setSameDayRush(sdrush);
			}
		}
		// Additional Colors
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_ADDITIONAL_COLOR);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
		String additionalColor="";			
		List<String> addtnlColorsList=new ArrayList<>();
		for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
			additionalColor=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_ADDITIONAL_COLOR);
			if(null!=additionalColor && additionalColor.equalsIgnoreCase("Other")){
				additionalColor=currentCriteriaSetValue.getValue().toString();
			}
			criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), currentCriteriaSetValue.getValue().toString());
			addtnlColorsList.add(additionalColor);
			}
			serviceProductConfig.setAdditionalColors(addtnlColorsList);
		}
		// Additional Locations
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_ADDITIONAL_LOCATION);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
		String additionalLocation="";			
		List<String> addtnlLocationsList=new ArrayList<>();
		for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
			additionalLocation=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_ADDITIONAL_LOCATION);
			if(null!=additionalLocation && additionalLocation.equalsIgnoreCase("Other")){
				additionalLocation=currentCriteriaSetValue.getValue().toString();
			}
			criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), currentCriteriaSetValue.getValue().toString());
			addtnlLocationsList.add(additionalLocation);
			}
			serviceProductConfig.setAdditionalLocations(addtnlLocationsList);
		}
		// Tradenames
		
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_TRADE_NAME_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
					
		List<String> tradeNamesList=new ArrayList<>();
		for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
		/*	try {
				if(ProductDataStore.getSetCodeValueIdForProductTradeName(URLEncoder.encode(currentCriteriaSetValue.getValue().toString().trim(), "UTF-16")).equals(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()))
		*/		tradeNamesList.add(currentCriteriaSetValue.getValue().toString().trim());
		criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), currentCriteriaSetValue.getValue().toString());
		/*	} catch (UnsupportedEncodingException e) {
				_LOGGER.error("Trade Name is Invalid - Unsupported Encoding Exception");
			}*/			
			}
		_LOGGER.info(tradeNamesList);
			serviceProductConfig.setTradeNames(tradeNamesList);
		}
		// Production Time
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			ProductionTime currentProductionTime=null;
			List<ProductionTime> prodTimeList=new ArrayList<>();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
					currentProductionTime=new ProductionTime();
					if(currentCriteriaSetValue.getValue() instanceof List){
						currentProductionTime.setBusinessDays(Integer.parseInt(productLookupParser.getTimeText(currentCriteriaSetValue.getValue())));
						criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), currentCriteriaSetValue.getCriteriaCode(), Integer.parseInt(currentCriteriaSetValue.getId()), productLookupParser.getTimeText(currentCriteriaSetValue.getValue()));
					}
					currentProductionTime.setDetails(currentCriteriaSetValue.getCriteriaValueDetail());
					prodTimeList.add(currentProductionTime);
			}
			serviceProductConfig.setProductionTime(prodTimeList);
		}
		// Samples
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);
		String sampleType="";
		Samples samples=new Samples();
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			samples.setProductSampleAvailable(false);
			samples.setSpecSampleAvailable(false);
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				sampleType=productLookupParser.findSampleType(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()+"");
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE, Integer.parseInt(currentCriteriaSetValue.getId()), currentCriteriaSetValue.getValue().toString());
				if(sampleType.equalsIgnoreCase("Spec Sample")){
					samples.setSpecSampleAvailable(true);
					samples.setSpecDetails(currentCriteriaSetValue.getValue().toString());
				}else if(sampleType.equalsIgnoreCase("Product Sample")){
					samples.setProductSampleAvailable(true);
					samples.setProductSampleDetails(currentCriteriaSetValue.getValue().toString());
				}
			}
			serviceProductConfig.setSamples(samples);
		}
			
		// Imprint Methods
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
		List<ImprintMethod> imprintMethodsList=new ArrayList<>();
		String imprintMethods="";
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
					
			String currentImprintMethod="";
			int imprintCntr=0;
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				currentImprintMethod=ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
				if(currentImprintMethod.equalsIgnoreCase("Other")){
					currentImprintMethod=currentImprintMethod+":"+currentCriteriaSetValue.getValue().toString();
				}
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), ApplicationConstants.CONST_IMPRINT_METHOD_CODE, Integer.parseInt(currentCriteriaSetValue.getId()), currentImprintMethod);
				if(null!=currentImprintMethod && !currentImprintMethod.equalsIgnoreCase("Unimprinted") && !currentImprintMethod.equalsIgnoreCase("Personalization")){
				if(imprintCntr==0){
					imprintMethods=currentImprintMethod;
				}else{
					imprintMethods+="||"+currentImprintMethod;
				}
				imprintCntr++;
				}				
			}			
		}
		// Artworks
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_ARTWORK_CODE);
		
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), ApplicationConstants.CONST_ARTWORK_CODE, Integer.parseInt(currentCriteriaSetValue.getId()), ProductDataStore.reverseLookupFindAttribute(currentCriteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_ARTWORK_CODE));
			}
		}
		
		// MINO
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_MINIMUM_QUANTITY);
		
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				criteriaSetParser.addReferenceSet(productDetail.getExternalProductId(), ApplicationConstants.CONST_MINIMUM_QUANTITY, Integer.parseInt(currentCriteriaSetValue.getId()), productLookupParser.getValueInText(currentCriteriaSetValue.getValue()));
			}
		}

		imprintMethodsList=productLookupParser.setServiceImprintMethods(productDetail,imprintMethods,criteriaSetParser);
		if(null!=imprintMethodsList && imprintMethodsList.size()>0 && imprintMethodsList.get(0).getType()!=null)
		serviceProductConfig.setImprintMethods(imprintMethodsList);
		
		
		// Options
		List<ProductCriteriaSets> productCriteriaSetsList=productDetail.getProductConfigurations().get(0).getProductCriteriaSets();
		ConcurrentHashMap<String,ArrayList<String>> optionList=null;
		for(ProductCriteriaSets currentProductCriteriaSet:productCriteriaSetsList){
			optionList=productLookupParser.findOptionValueDetails(optionList,currentProductCriteriaSet.getCriteriaCode(),currentProductCriteriaSet,productDetail.getExternalProductId());
		}
		if(null!=optionList) serviceProductConfig=productLookupParser.setOptionList(serviceProductConfig,optionList);
		
		// Sizes
		serviceProductConfig.setSizes(getCriteriaSetValuesListBySizeCode(productDetail.getExternalProductId(),productDetail.getProductConfigurations().get(0),SIZE_GROUP_CRITERIACODES));
		
		// Product Numbers
		serviceProduct.setProductNumbers(productLookupParser.setSeriviceProductWithProductNumbers(productDetail));
		
		// Line Names
		serviceProduct.setLineNames(productLookupParser.setServiceProductLineNames(productDetail));
		
		// FOB Points
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_CRITERIA_CODE_FOBP);
		List<String> fobPointsList=new ArrayList<>();
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				if(null!=ProductDataStore.getSetCodeValueIdForFobPoints(currentCriteriaSetValue.getValue().toString(),productDetail.getCompanyId())){
					fobPointsList.add(currentCriteriaSetValue.getValue().toString());
				}
			}
		}		
		
		// Shipping Items
		ShippingEstimate shippingEstimate=null;
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
		List<String> shippingItemList=new ArrayList<>();
		Size shippingSize=new Size();
		List<Value> valuesList=null;
		Value shippingItemValue=new Value();
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			shippingEstimate=new ShippingEstimate();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				shippingItemValue=productLookupParser.findSizeValueListDetails(shippingItemValue, currentCriteriaSetValue.getCriteriaCode(), currentCriteriaSetValue.getValue(), productDetail.getExternalProductId()).get(0);
				shippingEstimate.setNumberOfItems(shippingItemValue);
			}
		}
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			if(null==shippingEstimate)shippingEstimate=new ShippingEstimate();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				shippingItemValue=productLookupParser.findSizeValueListDetails(shippingItemValue, currentCriteriaSetValue.getCriteriaCode(), currentCriteriaSetValue.getValue(), productDetail.getExternalProductId()).get(0);
				shippingEstimate.setWeight(shippingItemValue);
			}
			
		}		
		currentCriteriaSetValueList=getCriteriaSetValuesListByCode(productDetail.getProductConfigurations().get(0),ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);
		int sdimCntr=0;
		Dimensions  shippingDimensions;
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			shippingDimensions=new Dimensions();
			if(null==shippingEstimate)shippingEstimate=new ShippingEstimate();
			for(com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValue:currentCriteriaSetValueList){
				valuesList=productLookupParser.findSizeValueListDetails(shippingItemValue, currentCriteriaSetValue.getCriteriaCode(), currentCriteriaSetValue.getValue(), productDetail.getExternalProductId());
			for(Value currntValueObj:valuesList)
				{
				if(sdimCntr==0){
				// Length
				shippingDimensions.setLength(currntValueObj.getValue());
				shippingDimensions.setLengthUnit(currntValueObj.getUnit());				
			}else if(sdimCntr==1){
				// Width
				shippingDimensions.setWidth(currntValueObj.getValue());
				shippingDimensions.setWidthUnit(currntValueObj.getUnit());
			}else if(sdimCntr==2){
				// Height
				shippingDimensions.setHeight(currntValueObj.getValue());
				shippingDimensions.setHeightUnit(currntValueObj.getUnit());				
			}
			sdimCntr++;
				}
				}
			shippingEstimate.setDimensions(shippingDimensions);
		}
		serviceProductConfig.setShippingEstimates(shippingEstimate);
		serviceProduct.setBreakOutByPrice(breakoutBy);
		serviceProduct.setFobPoints(fobPointsList);
		serviceProduct.setProductConfigurations(serviceProductConfig);
		return serviceProduct;
	}

	private Size getCriteriaSetValuesListBySizeCode(String externalProductId,
			ProductConfiguration productConfiguration,
			String[] size) {
		List<ProductCriteriaSets> productCriteriaSetsList=productConfiguration.getProductCriteriaSets();
		List<CriteriaSetValues> productCriteriaSetValuesList=null;
		Size currentSize=new Size();
		for(ProductCriteriaSets currentProductCriteriaSets:productCriteriaSetsList){
			if(Arrays.asList(SIZE_GROUP_CRITERIACODES).contains(currentProductCriteriaSets.getCriteriaCode().toString())){
				productCriteriaSetValuesList=currentProductCriteriaSets.getCriteriaSetValues();
					currentSize=productLookupParser.findSizeValueDetails(currentSize, currentProductCriteriaSets.getCriteriaCode(), productCriteriaSetValuesList, externalProductId);
			}			
		}
		return currentSize;
	}

	private HashMap<String, String> getImprintColorsByTypeCode(
			List<CriteriaSetValues> currentCriteriaSetValueList,String xid) {
		HashMap<String,String> typeCodeColorMap=null;
		String imprintColor="";
		if(null!=currentCriteriaSetValueList && currentCriteriaSetValueList.size()>0){
			typeCodeColorMap=new HashMap<>();
			for(CriteriaSetValues currentCriteriasetValue:currentCriteriaSetValueList){
				imprintColor=ProductDataStore.reverseLookupFindAttribute(currentCriteriasetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(),ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
				
				if(null!=imprintColor && imprintColor.equalsIgnoreCase("other")){
					imprintColor=currentCriteriasetValue.getValue().toString();
				}
				criteriaSetParser.addReferenceSet(xid, currentCriteriasetValue.getCriteriaCode(), Integer.parseInt(currentCriteriasetValue.getId()), imprintColor);
			if(null==typeCodeColorMap.get(currentCriteriasetValue.getValueTypeCode())){
				typeCodeColorMap.put(currentCriteriasetValue.getValueTypeCode(), imprintColor);
			}else{
				typeCodeColorMap.put(currentCriteriasetValue.getValueTypeCode(), typeCodeColorMap.get(currentCriteriasetValue.getValueTypeCode())+","+imprintColor);
			}			
			}			
		}		
		return typeCodeColorMap;
	}

	private List<CriteriaSetValues> getCriteriaSetValuesListByCode(
			ProductConfiguration productConfiguration, String criteriaCode) {
		List<CriteriaSetValues> criteriaSetValuesList=new ArrayList<>();
		com.asi.service.product.client.vo.ProductCriteriaSets productCriteriaSets=getProductCriteriaSetByCodeIfExist(productConfiguration,criteriaCode);
		if(null!=productCriteriaSets) criteriaSetValuesList=productCriteriaSets.getCriteriaSetValues();
		return criteriaSetValuesList;
	}
	private List<BlendMaterial> checkAndSetMaterialBlendInfo(
			ChildCriteriaSetCodeValues[] childCriteriaSetCodeValues) {
		BlendMaterial blendMaterial=null;
		List<BlendMaterial> finalBlendMaterial=new ArrayList<>();
		if(null!=childCriteriaSetCodeValues && childCriteriaSetCodeValues.length>0){
			for(ChildCriteriaSetCodeValues currentSetCodeValue:childCriteriaSetCodeValues){
				blendMaterial=new BlendMaterial();
				blendMaterial.setName(ProductDataStore.reverseLookupFindAttribute(currentSetCodeValue.getChildCriteriaSetCodeValue().getSetCodeValueId(),ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE));
				blendMaterial.setPercentage(currentSetCodeValue.getChildCriteriaSetCodeValue().getCodeValue());
				finalBlendMaterial.add(blendMaterial);
			}			
		}		
		return finalBlendMaterial;
	}
	
}
