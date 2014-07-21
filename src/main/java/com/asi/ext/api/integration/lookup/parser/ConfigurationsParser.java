package com.asi.ext.api.integration.lookup.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductConfigurations;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Value;
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

	private final static Logger _LOGGER = Logger
			.getLogger(ConfigurationsParser.class.getName());
	private HashMap<String, HashMap<String, String>> criteriaSet = new HashMap<>();
	private int productCriteriaSetCntr = -1;
	private int newCriteriaSetCodeValueCntr = -112;
	private int newCriteriaSetValuesCntr = -1;

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
		com.asi.service.product.client.vo.ProductCriteriaSets imprintProductCriteriaSet = null;
		List<com.asi.service.product.client.vo.ProductCriteriaSets> productCriteriaSetsAry = new ArrayList<>();
		if (null != productConfiguration) {
			productCriteriaSetsAry = productConfiguration
					.getProductCriteriaSets();
			for (com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSet : productCriteriaSetsAry) {
				if (currentProductCriteriaSet.getCriteriaCode()
						.equalsIgnoreCase(criteriaCode)) {
					imprintProductCriteriaSet = currentProductCriteriaSet;
					break;
				}
			}
		}
		return imprintProductCriteriaSet;
	}

	private List<CriteriaSetValues> transformCriteriaSetValues(
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
	}

	/*
	 * private CriteriaSetValues transformCriteriaSetValues(
	 * com.asi.service.product.vo.CriteriaSetValues currentCriteriaSetValues) {
	 * CriteriaSetValues clientCriteriaSetValues=new CriteriaSetValues();
	 * clientCriteriaSetValues
	 * .setValueTypeCode(currentCriteriaSetValues.getValueTypeCode());
	 * clientCriteriaSetValues
	 * .setBaseLookupValue(currentCriteriaSetValues.getBaseLookupValue());
	 * clientCriteriaSetValues
	 * .setCriteriaCode(currentCriteriaSetValues.getCriteriaCode());
	 * clientCriteriaSetValues
	 * .setCriteriaSetId(currentCriteriaSetValues.getCriteriaSetId());
	 * clientCriteriaSetValues
	 * .setCriteriaValueDetail(currentCriteriaSetValues.getCriteriaValueDetail
	 * ()); clientCriteriaSetValues.setFormatValue(currentCriteriaSetValues.
	 * getFormatValue());
	 * clientCriteriaSetValues.setValue(currentCriteriaSetValues.getValue());
	 * clientCriteriaSetValues.setId(currentCriteriaSetValues.getId());
	 * clientCriteriaSetValues
	 * .setIsSetValueMeasurement(currentCriteriaSetValues.
	 * getIsSetValueMeasurement());
	 * clientCriteriaSetValues.setIsSubset(currentCriteriaSetValues
	 * .getIsSubset());
	 * clientCriteriaSetValues.setDisplaySequence(currentCriteriaSetValues
	 * .getDisplaySequence());
	 * clientCriteriaSetValues.setSubSets(currentCriteriaSetValues
	 * .getSubSets()); CriteriaSetCodeValues currentCriteriaSetCodeValues=new
	 * CriteriaSetCodeValues(); CriteriaSetCodeValues[]
	 * clientCriteriaSetCodeValueList=new CriteriaSetCodeValues[1];
	 * currentCriteriaSetCodeValues
	 * .setId(currentCriteriaSetValues.getCriteriaSetCodeValues()[0].getId());
	 * currentCriteriaSetCodeValues
	 * .setCodeValue(currentCriteriaSetValues.getCriteriaSetCodeValues
	 * ()[0].getCodeValue());
	 * currentCriteriaSetCodeValues.setCodeValueDetail(currentCriteriaSetValues
	 * .getCriteriaSetCodeValues()[0].getCodeValueDetail());
	 * currentCriteriaSetCodeValues
	 * .setCriteriaSetValueId(currentCriteriaSetValues
	 * .getCriteriaSetCodeValues()[0].getCriteriaSetValueId());
	 * currentCriteriaSetCodeValues
	 * .setSetCodeValueId(currentCriteriaSetValues.getCriteriaSetCodeValues
	 * ()[0].getSetCodeValueId());
	 * clientCriteriaSetCodeValueList[0]=currentCriteriaSetCodeValues;
	 * clientCriteriaSetValues
	 * .setCriteriaSetCodeValues(clientCriteriaSetCodeValueList); return
	 * clientCriteriaSetValues; }
	 */
	/*public com.asi.service.product.client.vo.Product setProductWithSizeConfigurations(
			Product srcProduct, ProductDetail currentProductDetails,
			com.asi.service.product.client.vo.Product productToUpdate,
			LookupParser lookupsParser, String groupName, String sizeValue) {
		groupName = lookupsParser.getSizeCodeByName(groupName);
		if (null != sizeValue && !sizeValue.isEmpty()) {
			com.asi.service.product.client.vo.ProductCriteriaSets existingProductCriteriaSet = getProductCriteriaSetByCodeIfExist(
					currentProductDetails.getProductConfigurations().get(0),
					groupName);
			ProductCriteriaSets clientProductCriteriaSet = new ProductCriteriaSets();
			if (null == existingProductCriteriaSet) {
				clientProductCriteriaSet
						.setCompanyId(srcProduct.getCompanyId());
				clientProductCriteriaSet.setProductId(String.valueOf(srcProduct
						.getID()));
				clientProductCriteriaSet.setConfigId("0");
				clientProductCriteriaSet.setCriteriaCode(groupName);
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
			String[] criteriaItems = sizeValue.split(",");
			boolean criteriaSetValueExist = false;
			List<CriteriaSetValues> clientCriteriaSetValuesList = new ArrayList<>();
			CriteriaSetCodeValues tempCriteriaSetCodeValues = null;
			CriteriaSetCodeValues[] tempCriteriaSetCodeValuesList = new CriteriaSetCodeValues[1];
			com.asi.service.product.client.vo.CriteriaSetValues clientCurrentCriteriaSetValues = null;
			// if(null!=clientColorsProductCriteriaSet.getCriteriaSetValues() &&
			// clientColorsProductCriteriaSet.getCriteriaSetValues().size()>0){
			if (null != existingProductCriteriaSet
					&& null != existingProductCriteriaSet
							.getCriteriaSetValues()
					&& existingProductCriteriaSet.getCriteriaSetValues().size() > 0) {
				for (com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValues : existingProductCriteriaSet
						.getCriteriaSetValues()) {
					for (String critieriaItem : criteriaItems) {

						
						 * if(currentCriteriaSetValues.getValue().toString().trim
						 * ().equalsIgnoreCase(critieriaItem.trim())){
						 * clientCurrentCriteriaSetValues=new
						 * CriteriaSetValues();
						 * //clientCurrentCriteriaSetValues=
						 * transformCriteriaSetValues(currentCriteriaSetValues);
						 * //BeanUtils.copyProperties(currentCriteriaSetValues,
						 * clientCurrentCriteriaSetValues);
						 * currentCriteriaSetValues
						 * .getCriteriaSetCodeValues()[0]
						 * .setId(String.valueOf(newCriteriaSetCodeValueCntr));
						 * clientCriteriaSetValuesList
						 * .add(currentCriteriaSetValues);
						 * //currentCriteriaSetValues=null;
						 * criteriaSetValueExist=true; break; }else
						 * if(productLookupParser
						 * .getSetValueNameByCode(currentCriteriaSetValues
						 * .getCriteriaSetCodeValues
						 * ()[0].getSetCodeValueId(),groupName
						 * ,currentCriteriaSetValues
						 * .getValue()).equalsIgnoreCase(critieriaItem.trim())){
						 * clientCurrentCriteriaSetValues=new
						 * CriteriaSetValues();
						 * //clientCurrentCriteriaSetValues=
						 * transformCriteriaSetValues(currentCriteriaSetValues);
						 * //BeanUtils.copyProperties(currentCriteriaSetValues,
						 * clientCurrentCriteriaSetValues);
						 * currentCriteriaSetValues
						 * .getCriteriaSetCodeValues()[0]
						 * .setId(String.valueOf(newCriteriaSetCodeValueCntr));
						 * clientCriteriaSetValuesList
						 * .add(currentCriteriaSetValues);
						 * //currentCriteriaSetValues=null;
						 * criteriaSetValueExist=true; break; }
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
					clientCurrentCriteriaSetValues.setCriteriaCode(groupName);
					Value valueObj = null;
					List<Value> valueList = setSizeValueItem(sizeValue,
							groupName);
					
					 * for(String critieriaItem:criteriaItems) {
					 * valueObj=setSizeValueItem(critieriaItem,groupName);
					 * valueList.add(valueObj); }
					 
					clientCurrentCriteriaSetValues.setValue(valueList);
					clientCurrentCriteriaSetValues.setValueTypeCode("LOOK");
					clientCurrentCriteriaSetValues.setIsSubset("false");
					// clientCurrentCriteriaSetValues.setFormatValue(sizeValue.trim());
					clientCurrentCriteriaSetValues
							.setIsSetValueMeasurement("false");
					tempCriteriaSetCodeValues = new CriteriaSetCodeValues();
					tempCriteriaSetCodeValues.setId(String
							.valueOf(newCriteriaSetCodeValueCntr));
					tempCriteriaSetCodeValues
							.setCriteriaSetValueId(clientCurrentCriteriaSetValues
									.getId());
					tempCriteriaSetCodeValues
							.setSetCodeValueId(productLookupParser
									.getSizesSetCodeValueId(groupName));
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
				productCriteriaSetsList = addOrUpdateProductCriteriaSetsList(
						clientProductCriteriaSet, currentProductDetails
								.getProductConfigurations().get(0)
								.getProductCriteriaSets(),
						productToUpdate.getProductConfigurations());
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
*/
/*	private List<Value> setSizeValueItem(String critieriaItem,
			String criteriaCode) {
		List<Value> valueList = new ArrayList<>();
		int valueItemCntr = 0, valueSizeCntr = 0;
		String dimensionName = "", valueNumber = "0", valueUnits = "";
		String[] valueSizeItems = critieriaItem.split(",");
		for (String valueSizeItem : valueSizeItems) {
			critieriaItem = valueSizeItem;
			valueItemCntr = 0;
			dimensionName = "";
			valueNumber = "0";
			valueUnits = "";
			if (criteriaCode.equalsIgnoreCase("SAIT")) {
				if (critieriaItem.endsWith("T")) {
					critieriaItem = critieriaItem.replace("T", ":T");
				} else {
					critieriaItem = critieriaItem.replace("months", ":months");
				}
			} else if (criteriaCode.equalsIgnoreCase("SANS")) {
				if (valueSizeCntr == 0)
					dimensionName = "Neck";
				else
					dimensionName = "Sleeve";
				valueSizeCntr++;
			} else if (criteriaCode.equalsIgnoreCase("SAWI")) {
				if (valueSizeCntr == 0)
					dimensionName = "Waist";
				else
					dimensionName = "Inseam";
				valueSizeCntr++;
			} else if (criteriaCode.equalsIgnoreCase("SVWT")) {
				if (valueSizeCntr == 0)
					dimensionName = "Volume";
				valueSizeCntr++;
			}
			String[] valueItems = critieriaItem.split(":");
			for (String currentItem : valueItems) {
				currentItem = currentItem.trim();
				if (valueItemCntr == 0) {
					if (criteriaCode.equalsIgnoreCase("DIMS")) {
						dimensionName = currentItem;
					} else if (criteriaCode.equalsIgnoreCase("SAIT")
							|| criteriaCode.equalsIgnoreCase("SABR")
							|| criteriaCode.equalsIgnoreCase("SAHU")) {
						dimensionName = "Unit";
						valueNumber = currentItem;
					} else {
						valueNumber = currentItem;
					}
				} else if (valueItemCntr == 1) {
					if (criteriaCode.equalsIgnoreCase("DIMS"))
						valueNumber = currentItem;
					else
						valueUnits = currentItem;
				} else {
					valueUnits = currentItem;
				}
				valueItemCntr++;
			}
			Value valueObj = new Value();
			if (criteriaCode.equalsIgnoreCase("CAPS")) {
				valueObj.setCriteriaAttributeId(productLookupParser
						.getCriteriaAttributeIdByDisplayName("Capacity"));
			} else {
				valueObj.setCriteriaAttributeId(productLookupParser
						.getSpecificCriteriaAttributeId(dimensionName,
								criteriaCode));
			}
			if (valueUnits.equals("in"))
				valueUnits = "\"";
			valueObj.setUnitOfMeasureCode(productLookupParser
					.getUnitsOfMeasureCodeByFormat(criteriaCode,
							valueObj.getCriteriaAttributeId(), valueUnits));
			if (valueObj.getUnitOfMeasureCode().trim().isEmpty()
					&& criteriaCode.equals("SVWT")) {
				valueObj.setCriteriaAttributeId(productLookupParser
						.getSpecificCriteriaAttributeId("Weight", criteriaCode));
				valueObj.setUnitOfMeasureCode(productLookupParser
						.getUnitsOfMeasureCodeByFormat(criteriaCode,
								valueObj.getCriteriaAttributeId(), valueUnits));
			}
			valueObj.setUnitValue(valueNumber);
			valueList.add(valueObj);
			valueSizeCntr++;
		}
		return valueList;
	}
*/
/*	public com.asi.service.product.client.vo.Product setProductWithSizeApperalConfigurations(
			Product srcProduct, ProductDetail currentProductDetails,
			com.asi.service.product.client.vo.Product productToUpdate,
			LookupParser lookupsParser, String groupName, String sizeValue) {
		groupName = lookupsParser.getSizeCodeByName(groupName);
		String setCodeValueId = "";
		if (null != sizeValue && !sizeValue.isEmpty()) {
			com.asi.service.product.client.vo.ProductCriteriaSets existingProductCriteriaSet = getProductCriteriaSetByCodeIfExist(
					currentProductDetails.getProductConfigurations().get(0),
					groupName);
			ProductCriteriaSets clientProductCriteriaSet = new ProductCriteriaSets();
			if (null == existingProductCriteriaSet) {
				clientProductCriteriaSet
						.setCompanyId(srcProduct.getCompanyId());
				clientProductCriteriaSet.setProductId(String.valueOf(srcProduct
						.getID()));
				clientProductCriteriaSet.setConfigId("0");
				clientProductCriteriaSet.setCriteriaCode(groupName);
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
			String[] criteriaItems = sizeValue.split(",");
			boolean criteriaSetValueExist = false;
			List<CriteriaSetValues> clientCriteriaSetValuesList = new ArrayList<>();

			// com.asi.service.product.client.vo.CriteriaSetValues
			// clientCurrentCriteriaSetValues=null;
			// if(null!=clientColorsProductCriteriaSet.getCriteriaSetValues() &&
			// clientColorsProductCriteriaSet.getCriteriaSetValues().size()>0){
			if (null != existingProductCriteriaSet
					&& null != existingProductCriteriaSet
							.getCriteriaSetValues()
					&& existingProductCriteriaSet.getCriteriaSetValues().size() > 0) {
				for (com.asi.service.product.client.vo.CriteriaSetValues currentCriteriaSetValues : existingProductCriteriaSet
						.getCriteriaSetValues()) {
					for (String critieriaItem : criteriaItems) {
						critieriaItem = critieriaItem.trim();
						com.asi.service.product.client.vo.CriteriaSetValues clientCurrentCriteriaSetValues = new CriteriaSetValues();

						if (currentCriteriaSetValues.getValue().toString()
								.trim().equalsIgnoreCase(critieriaItem.trim())) {
							clientCurrentCriteriaSetValues = new CriteriaSetValues();
							//
							clientCurrentCriteriaSetValues = currentCriteriaSetValues;
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
						} else if (currentCriteriaSetValues.getValue() instanceof ArrayList) {
												 productLookupParser.getValueString((ArrayList<Value>)currentCriteriaSetValues.getValue(),
														groupName);
							clientCurrentCriteriaSetValues = new CriteriaSetValues();
							//
							clientCurrentCriteriaSetValues = currentCriteriaSetValues;
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

						if (!criteriaSetValueExist) {

							clientCurrentCriteriaSetValues.setId(String
									.valueOf(newCriteriaSetValuesCntr));
							//
							clientCurrentCriteriaSetValues
									.setCriteriaSetId(clientProductCriteriaSet
											.getCriteriaSetId());
							clientCurrentCriteriaSetValues
									.setCriteriaCode(groupName);
							setCodeValueId = productLookupParser
									.getSizesApperalSetCodeValueId(
											critieriaItem, groupName);
							if (null == setCodeValueId) {
								setCodeValueId = productLookupParser
										.getSizesSetCodeValueId(groupName);
								clientCurrentCriteriaSetValues
										.setValueTypeCode("CUST");
								if (groupName.equalsIgnoreCase("SANS")) {
									critieriaItem = critieriaItem.replace(")",
											"");
									critieriaItem = critieriaItem.replace("(",
											",");
								} else if (groupName.equalsIgnoreCase("SAWI")) {
									critieriaItem = critieriaItem.replace("x",
											",");
								} else if (groupName.equalsIgnoreCase("SOTH")
										|| groupName.equalsIgnoreCase("SSNM")) {
									clientCurrentCriteriaSetValues
											.setValue(critieriaItem);
								} else {
									List<Value> valueList = setSizeValueItem(
											critieriaItem, groupName);
									clientCurrentCriteriaSetValues
											.setValue(valueList);
								}
							} else {
								clientCurrentCriteriaSetValues
										.setValueTypeCode("LOOK");
								clientCurrentCriteriaSetValues.setValue("");
							}
							clientCurrentCriteriaSetValues.setIsSubset("false");
							// clientCurrentCriteriaSetValues.setFormatValue(sizeValue.trim());
							clientCurrentCriteriaSetValues
									.setIsSetValueMeasurement("false");
							CriteriaSetCodeValues[] tempCriteriaSetCodeValuesList = new CriteriaSetCodeValues[1];
							CriteriaSetCodeValues tempCriteriaSetCodeValues = new CriteriaSetCodeValues();
							tempCriteriaSetCodeValues.setId(String
									.valueOf(newCriteriaSetCodeValueCntr));
							tempCriteriaSetCodeValues
									.setCriteriaSetValueId(clientCurrentCriteriaSetValues
											.getId());
							tempCriteriaSetCodeValues
									.setSetCodeValueId(setCodeValueId);
							tempCriteriaSetCodeValuesList[0] = tempCriteriaSetCodeValues;
							clientCurrentCriteriaSetValues
									.setCriteriaSetCodeValues(tempCriteriaSetCodeValuesList);
							clientCriteriaSetValuesList
									.add(clientCurrentCriteriaSetValues);
						}
						criteriaSetValueExist = false;
						newCriteriaSetValuesCntr--;
						newCriteriaSetCodeValueCntr--;
					}
				}
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
				productCriteriaSetsList = addOrUpdateProductCriteriaSetsList(
						clientProductCriteriaSet, currentProductDetails
								.getProductConfigurations().get(0)
								.getProductCriteriaSets(),
						productToUpdate.getProductConfigurations());
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
*/}
