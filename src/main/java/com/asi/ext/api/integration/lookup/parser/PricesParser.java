package com.asi.ext.api.integration.lookup.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.PriceConfiguration;
import com.asi.ext.api.service.model.Value;
import com.asi.ext.api.service.model.Values;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.BasePriceDetails;
import com.asi.service.product.client.vo.DiscountList;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PricesList;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.QuantityList;
import com.asi.service.product.client.vo.UpChargePriceDetails;
import com.asi.service.product.client.vo.parser.UpChargeLookup;

public class PricesParser {
	private CriteriaSetParser criteriaSetParser = new CriteriaSetParser();
	private ArrayList<String> rushCriteriaGroup = new ArrayList<String>(
			Arrays.asList("RUSH", "PRTM", "SDRU"));
	private ArrayList<String> APPAREL_SIZE_GROUP_CRITERIACODES = new ArrayList<String>(
			Arrays.asList("SABR", "SAHU", "SAIT", "SANS", "SAWI", "SVWT"));
	CommonUtilities commonUtilities = new CommonUtilities();

	public List<Object> getPricesList(List<PriceGrid> priceGrids,
			String externalProductId) {
		ArrayList<Object> priceFinalList = null;
		PricesList pricesList = new PricesList();
		List<Price> prices = null;
		QuantityList quantityList = new QuantityList();
		int noOfPriceGrids = 0;
		DiscountList disountList = new DiscountList();

		for (PriceGrid priceGrid : priceGrids) {
			prices = priceGrid.getPrices();
			int priceCntr = 1;
			if (priceGrid.getIsBasePrice()) {
				priceFinalList = new ArrayList<Object>();
				for (Price price : prices) {
					switch (priceCntr) {
					case 1:
						pricesList.setP1((noOfPriceGrids == 0) ? price
								.getListPrice() + "" : pricesList.getP1()
								+ "||" + price.getListPrice());
						quantityList.setQ1((noOfPriceGrids == 0) ? price
								.getQuantity() + "" : quantityList.getQ1()
								+ "||" + price.getQuantity());
						disountList.setD1((noOfPriceGrids == 0) ? price
								.getDiscountRate().getIndustryDiscountCode()
								: disountList.getD1()
										+ "||"
										+ price.getDiscountRate()
												.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 2:
						pricesList.setP2(price.getListPrice() + "");
						quantityList.setQ2(price.getQuantity() + "");
						disountList.setD2(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 3:
						pricesList.setP3(price.getListPrice() + "");
						quantityList.setQ3(price.getQuantity() + "");
						disountList.setD3(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 4:
						pricesList.setP4(price.getListPrice() + "");
						quantityList.setQ4(price.getQuantity() + "");
						disountList.setD4(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 5:
						pricesList.setP5(price.getListPrice() + "");
						quantityList.setQ5(price.getQuantity() + "");
						disountList.setD5(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 6:
						pricesList.setP6(price.getListPrice() + "");
						quantityList.setQ6(price.getQuantity() + "");
						disountList.setD6(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 7:
						pricesList.setP7(price.getListPrice() + "");
						quantityList.setQ7(price.getQuantity() + "");
						disountList.setD7(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 8:
						pricesList.setP8(price.getListPrice() + "");
						quantityList.setQ8(price.getQuantity() + "");
						disountList.setD8(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 9:
						pricesList.setP9(price.getListPrice() + "");
						quantityList.setQ9(price.getQuantity() + "");
						disountList.setD9(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					case 10:
						pricesList.setP10(price.getListPrice() + "");
						quantityList.setQ10(price.getQuantity() + "");
						disountList.setD10(price.getDiscountRate()
								.getIndustryDiscountCode());
						priceCntr++;
						break;
					default:
						priceCntr = 1;
						break;
					}
				}
				priceFinalList.add(pricesList);
				priceFinalList.add(quantityList);
				priceFinalList.add(disountList);
			}
			noOfPriceGrids++;
		}
		return priceFinalList;
	}

	/*
	 * public ProcessProductsList setPricingItem(int pricingColumn,
	 * ProcessProductsList tempProduct, String[] listOfPs, String[] listOfQs,
	 * String[] listOfDs, int repeatableRow) { PricesList pricesList=new
	 * PricesList(); QuantityList quantityList=new QuantityList(); DiscountList
	 * discountList=new DiscountList(); switch (pricingColumn) { case 1:
	 * pricesList.setP1(commonUtilities.checkAndSet(listOfPs, repeatableRow));
	 * quantityList.setQ1(commonUtilities.checkAndSet(listOfQs, repeatableRow));
	 * discountList.setD1(commonUtilities.checkAndSet(listOfDs, repeatableRow));
	 * break; case 2: pricesList.setP2(commonUtilities.checkAndSet(listOfPs,
	 * repeatableRow)); quantityList.setQ2(commonUtilities.checkAndSet(listOfQs,
	 * repeatableRow)); discountList.setD2(commonUtilities.checkAndSet(listOfDs,
	 * repeatableRow)); break; case 3:
	 * pricesList.setP3(commonUtilities.checkAndSet(listOfPs, repeatableRow));
	 * quantityList.setQ3(commonUtilities.checkAndSet(listOfQs, repeatableRow));
	 * discountList.setD3(commonUtilities.checkAndSet(listOfDs, repeatableRow));
	 * break; case 4: pricesList.setP4(commonUtilities.checkAndSet(listOfPs,
	 * repeatableRow)); quantityList.setQ4(commonUtilities.checkAndSet(listOfQs,
	 * repeatableRow)); discountList.setD4(commonUtilities.checkAndSet(listOfDs,
	 * repeatableRow)); break; case 5:
	 * pricesList.setP5(commonUtilities.checkAndSet(listOfPs, repeatableRow));
	 * quantityList.setQ5(commonUtilities.checkAndSet(listOfQs, repeatableRow));
	 * discountList.setD5(commonUtilities.checkAndSet(listOfDs, repeatableRow));
	 * break; case 6: pricesList.setP6(commonUtilities.checkAndSet(listOfPs,
	 * repeatableRow)); quantityList.setQ6(commonUtilities.checkAndSet(listOfQs,
	 * repeatableRow)); discountList.setD6(commonUtilities.checkAndSet(listOfDs,
	 * repeatableRow)); break; case 7:
	 * pricesList.setP7(commonUtilities.checkAndSet(listOfPs, repeatableRow));
	 * quantityList.setQ7(commonUtilities.checkAndSet(listOfQs, repeatableRow));
	 * discountList.setD7(commonUtilities.checkAndSet(listOfDs, repeatableRow));
	 * break; case 8: pricesList.setP8(commonUtilities.checkAndSet(listOfPs,
	 * repeatableRow)); quantityList.setQ8(commonUtilities.checkAndSet(listOfQs,
	 * repeatableRow)); discountList.setD8(commonUtilities.checkAndSet(listOfDs,
	 * repeatableRow)); break; case 9:
	 * pricesList.setP9(commonUtilities.checkAndSet(listOfPs, repeatableRow));
	 * quantityList.setQ9(commonUtilities.checkAndSet(listOfQs, repeatableRow));
	 * discountList.setD9(commonUtilities.checkAndSet(listOfDs, repeatableRow));
	 * break; case 10: pricesList.setP10(commonUtilities.checkAndSet(listOfPs,
	 * repeatableRow));
	 * quantityList.setQ10(commonUtilities.checkAndSet(listOfQs,
	 * repeatableRow));
	 * discountList.setD10(commonUtilities.checkAndSet(listOfDs,
	 * repeatableRow)); break; default: break; } return tempProduct; }
	 */
	public String formatCriteriaValue(String srcString, String criteriaCode) {
		if (srcString != null) {
			if (criteriaCode != null && criteriaCode.equalsIgnoreCase("IMSZ")) {
				// srcString=srcString.replaceAll("\\|", "-");
			} else if ("FOBP".equalsIgnoreCase(criteriaCode) && srcString.contains(",")) {
//				srcString = "\"" + srcString + "\"";
			}
		}
		return srcString;
	}

	public Object[] getPriceCriteria(String externalProductId,
			List<PricingItem> pricingItems) {
		Object criteriaSet1 = "";
		Object currentObj = "";
		Object criteriaSet2 = "";
		Object[] criteriaSets = new Object[] { null, null };
		String criteriaCode = "";
		String firstCriteria = null, secondCriteria = null;
		String temp = "", temp1 = "";
		String[] criteriaItems = null;
		int criteriaCntr = 0;
		Values criteriaValues = null;
		Value currentCriteriaObj = null;
		List<Value> valuesList = new ArrayList<>();
		Values criteriaValues2 = null;
		Value currentCriteriaObj2 = null;
		List<Value> valuesList2 = new ArrayList<>();
		for (PricingItem priceItem : pricingItems) {

			if (criteriaCntr == 0) {
				temp = criteriaSetParser.findCriteriaSetValueById(
						externalProductId, priceItem.getCriteriaSetValueId());
				if (null != temp) {
					criteriaItems = temp.split("__");
					if (criteriaItems.length > 1) {
						criteriaCode = criteriaItems[0];
						firstCriteria = criteriaCode;
						criteriaSet1 = criteriaCode
								+ ":"
								+ formatCriteriaValue(criteriaItems[1],
										criteriaCode);
					}
				} else {
					if (null == criteriaValues) {
						criteriaValues = new Values();
					}

					criteriaSet1 = criteriaSetParser.findSizesCriteriaSetById(
							externalProductId,
							priceItem.getCriteriaSetValueId());
					firstCriteria = (String) getCriteriaCode(criteriaSet1);
					if (criteriaSet1 instanceof Value) {
						currentCriteriaObj = (Value) criteriaSet1;
						if (APPAREL_SIZE_GROUP_CRITERIACODES
								.contains(currentCriteriaObj.getCriteriaType())) {
							criteriaValues.setType(ProductDataStore
									.getCriteriaInfoForCriteriaCode(
											currentCriteriaObj
													.getCriteriaType())
									.getDescription().replace("Size-", "")
									.trim());
						}
						valuesList.add(currentCriteriaObj);
						criteriaValues.setValue(valuesList);
						criteriaSet1 = criteriaValues;
					}
				}
			} else {
				// criteriaSet1=temp;
				temp = criteriaSetParser.findCriteriaSetValueById(
						externalProductId, priceItem.getCriteriaSetValueId());
				if (null != temp) {
					criteriaItems = temp.split("__");
					if (criteriaItems.length > 1) {
						if (criteriaItems[0].equalsIgnoreCase(criteriaCode)) {
							criteriaSet1 = criteriaSet1
									+ ","
									+ formatCriteriaValue(criteriaItems[1],
											criteriaItems[0]);
						} else if (temp1.equals("")) {
							criteriaSet2 = criteriaItems[0]
									+ ":"
									+ formatCriteriaValue(criteriaItems[1],
											criteriaItems[0]);
							temp1 = criteriaItems[0];
						} else {
							criteriaSet2 = criteriaSet2
									+ ","
									+ formatCriteriaValue(criteriaItems[1],
											criteriaItems[0]);
						}
					}
				} else {
					if (null == criteriaValues2) {
						criteriaValues2 = new Values();
					}
					criteriaSet2 = criteriaSetParser.findSizesCriteriaSetById(
							externalProductId,
							priceItem.getCriteriaSetValueId());
					secondCriteria = (String) getCriteriaCode(criteriaSet2);
					if (firstCriteria.equalsIgnoreCase(secondCriteria)) {
						if (currentObj instanceof Value) {
							currentCriteriaObj = (Value) criteriaSet2;
							if (APPAREL_SIZE_GROUP_CRITERIACODES
									.contains(currentCriteriaObj
											.getCriteriaType())) {
								criteriaValues2.setType(ProductDataStore
										.getCriteriaInfoForCriteriaCode(
												currentCriteriaObj
														.getCriteriaType())
										.getDescription().replace("Size-", "")
										.trim());
							}
							valuesList.add(currentCriteriaObj);
							criteriaValues.setValue(valuesList);
							criteriaSet1 = criteriaValues;
						}
					} else {
						if (criteriaSet2 instanceof Value) {
							currentCriteriaObj2 = (Value) criteriaSet2;
							if (APPAREL_SIZE_GROUP_CRITERIACODES
									.contains(currentCriteriaObj2
											.getCriteriaType())) {
								criteriaValues2.setType(ProductDataStore
										.getCriteriaInfoForCriteriaCode(
												currentCriteriaObj2
														.getCriteriaType())
										.getDescription().replace("Size-", "")
										.trim());
							}
							valuesList2.add(currentCriteriaObj2);
							criteriaValues2.setValue(valuesList2);
							criteriaSet2 = criteriaValues2;
						} else
							criteriaSet2 = currentObj;
					}
				}
			}
			criteriaCntr++;
		}
		criteriaSets[0] = criteriaSet1;
		criteriaSets[1] = criteriaSet2;
		return criteriaSets;
	}

	/*
	 * public List<Value[]> getPriceSizesCriteria(String externalProductId,
	 * List<PricingItem> pricingItems) { List<Value[]> criteriaSets = new
	 * ArrayList<>(); Value[] temp; for (PricingItem priceItem : pricingItems) {
	 * // if (criteriaCntr == 0) { temp =
	 * criteriaSetParser.findSizesCriteriaSetById(externalProductId,
	 * priceItem.getCriteriaSetValueId()); if (null != temp) {
	 * criteriaSets.add(temp); } } else { // criteriaSet1=temp; temp = (Value[])
	 * criteriaSetParser.findSizesCriteriaSetById(externalProductId,
	 * priceItem.getCriteriaSetValueId()); if (null != temp) { criteriaItems =
	 * temp.split("__"); if (criteriaItems.length > 1) { if
	 * (criteriaItems[0].equalsIgnoreCase(criteriaCode)) { criteriaSet1 =
	 * criteriaSet1 + "," + formatCriteriaValue(criteriaItems[1],
	 * criteriaItems[0]); } else if (temp1.equals("")) { criteriaSet2 =
	 * criteriaItems[0] + ":" + formatCriteriaValue(criteriaItems[1],
	 * criteriaItems[0]); temp1 = criteriaItems[0]; } else { criteriaSet2 =
	 * criteriaSet2 + "," + formatCriteriaValue(criteriaItems[1],
	 * criteriaItems[0]); } } } } // criteriaCntr++; } return criteriaSets; }
	 */
	public BasePriceDetails getBasePriceDetails(String externalProductId,
			PriceGrid priceGrid, boolean setCurrency, String firstCriteria,
			String secondCriteria) {
		BasePriceDetails basePriceDetails = new BasePriceDetails();
		basePriceDetails.setBasePriceName(priceGrid.getDescription());
		basePriceDetails.setPriceIncludes(priceGrid.getPriceIncludes());
		if (priceGrid.getIsQUR()) {
			basePriceDetails.setQUR("Y");
		} else {
			basePriceDetails.setQUR("");
		}
		List<String> pricesList = new ArrayList<String>();
		List<String> quantityList = new ArrayList<String>();
		List<String> discountList = new ArrayList<String>();
		for (Price p : priceGrid.getPrices()) {
			pricesList.add(p.getListPrice() + "");
			if (null != p.getPriceUnitName() && !p.getPriceUnitName().isEmpty()
					&& !p.getPriceUnitName().equalsIgnoreCase("piece")) {
				quantityList.add(p.getQuantity() + ":" + p.getPriceUnitName()
						+ ":" + p.getItemsPerUnit());
			} else {
				quantityList.add(p.getQuantity() + "");
			}
			discountList.add(p.getDiscountRate().getIndustryDiscountCode());
		}
		basePriceDetails.setPrices(pricesList);
		basePriceDetails.setQuantities(quantityList);
		basePriceDetails.setDiscounts(discountList);
		if (setCurrency && priceGrid.getCurrency() != null) {
			basePriceDetails.setCurrency(priceGrid.getCurrency().getCode());
		}

		// TODO : Call the function
		Object[] temp = getPriceCriteria(externalProductId,
				priceGrid.getPricingItems());
		Object tempCode;
		if (temp != null && temp.length == 2 && temp[0] instanceof String) {
			if (!CommonUtilities.isValueNull(temp[0].toString())
					&& !CommonUtilities.isValueNull(firstCriteria)) {
				if (rushCriteriaGroup.contains(firstCriteria)
						&& !rushCriteriaGroup.contains(temp[0])
						&& rushCriteriaGroup.contains(temp[1])) {
					tempCode = temp[0];
					temp[0] = temp[1];
					temp[1] = tempCode;
				} else if (rushCriteriaGroup.contains(secondCriteria)
						&& rushCriteriaGroup.contains(temp[0])) {
					tempCode = temp[0];
					temp[0] = temp[1];
					temp[1] = tempCode;
				} else if (!temp[0].toString().startsWith(firstCriteria)
						&& !rushCriteriaGroup.contains(firstCriteria)) {
					tempCode = temp[0];
					temp[0] = temp[1];
					temp[1] = tempCode;
				}
			}
			if (null != temp[0])
				basePriceDetails.setBasePriceCriteria1(CommonUtilities
						.isValueNull(temp[0].toString()) ? null : temp[0]
						.toString());
			if (null != temp[1]) {
				if (temp[1] instanceof String) {
					basePriceDetails.setBasePriceCriteria2(CommonUtilities
							.isValueNull(temp[1].toString()) ? null : temp[1]
							.toString());
				} else {
					basePriceDetails.setBasePriceCriteria2(temp[1]);
				}
			}
		} else {

			basePriceDetails.setBasePriceCriteria1(temp[0]);
			if (null != temp[1] && temp[1] instanceof String
					&& !temp[1].toString().isEmpty())
				basePriceDetails.setBasePriceCriteria2(temp[1]);
			else if (null != temp[1] && temp[1] instanceof String) {

			} else if (null != temp[1]) {
				basePriceDetails.setBasePriceCriteria2(temp[1]);
			}
		}

		return basePriceDetails;
	}

	public UpChargePriceDetails getUpChargePriceDetails(
			String externalProductId, PriceGrid priceGrid,
			UpChargeLookup upChargeLookup) {

		UpChargePriceDetails upChargePriceDetails = new UpChargePriceDetails();
		upChargePriceDetails.setUpChargeName(priceGrid.getDescription());

		if (priceGrid.getIsQUR()) {
			upChargePriceDetails.setuQUR("Y");
		} else {
			upChargePriceDetails.setuQUR("");
		}
		List<String> pricesList = new ArrayList<String>();
		List<String> quantityList = new ArrayList<String>();
		List<String> discountList = new ArrayList<String>();
		for (Price p : priceGrid.getPrices()) {
			pricesList.add(p.getListPrice() + "");
			quantityList.add(p.getQuantity() + "");
			discountList.add(p.getDiscountRate().getIndustryDiscountCode());
		}
		upChargePriceDetails.setPrices(pricesList);
		upChargePriceDetails.setQuantities(quantityList);
		upChargePriceDetails.setDiscounts(discountList);

		Object[] temp = getPriceCriteria(externalProductId,
				priceGrid.getPricingItems());
		if (temp != null && temp.length == 2 && temp[0] instanceof String) {
			upChargePriceDetails.setUpChargeCriteria1(CommonUtilities
					.isValueNull(temp[0].toString()) ? null : temp[0]
					.toString());
			upChargePriceDetails.setUpChargeCriteria2(CommonUtilities
					.isValueNull(temp[1].toString()) ? null : temp[1]
					.toString());
		}

		if (upChargeLookup != null) {
			upChargePriceDetails.setUpchargeType(upChargeLookup
					.getUpChargeType(priceGrid.getPriceGridSubTypeCode()));
			upChargePriceDetails.setUpchargeLevel(upChargeLookup
					.getUpchargeLevel(priceGrid.getUsageLevelCode()));
		}
		return upChargePriceDetails;
	}

	@SuppressWarnings("unchecked")
	public Object getCriteriaCode(Object source) {
		List<Value> valuesList = null;
		if (source != null && source instanceof String
				&& !source.toString().isEmpty()
				&& source.toString().contains(":")) {
			return source.toString().substring(0,
					source.toString().indexOf(":"));
		} else if (source != null && source instanceof Values) {
			return ((Values) source).getValue().get(0).getCriteriaType();
		} else if (source != null && source instanceof Value) {
			return ((Value) source).getCriteriaType();
		} else if (null != source && source instanceof List) {
			valuesList = (List<Value>) source;
			if (null != valuesList && valuesList.size() > 0)
				return valuesList.get(0).getCriteriaType();
			else
				return null;
		} else {
			return source;
		}
	}

	public List<PriceConfiguration> getPricingConfigurations(
			String externalProductId, List<PricingItem> pricingItems) {

		PriceConfiguration criteriaSet1 = new PriceConfiguration();
		List<PriceConfiguration> criteriaSets = new ArrayList<PriceConfiguration>();
		for (PricingItem priceItem : pricingItems) {
			criteriaSet1 = getConfiguration(externalProductId,
					priceItem.getCriteriaSetValueId());
			criteriaSets.add(criteriaSet1);
		}

		return criteriaSets;
	}

	private PriceConfiguration getConfiguration(String externalProductId,
			String criteriaSetValueId) {
		Object temp = criteriaSetParser.findCriteriaSetValueById(
				externalProductId, criteriaSetValueId);
		PriceConfiguration criteriaSet1 = null;
		String[] criteriaItems = null;
		// ArrayList<String> valuesList;
		String currentCriteria = null;
		String crntCriteria=null;
		String criteriaValue=null;
		String criteriaCode;
		if (null != temp && temp instanceof String) {
			criteriaSet1 = new PriceConfiguration();
			criteriaItems = temp.toString().split("__");
			if (criteriaItems.length > 1) {
				criteriaCode = criteriaItems[0];
				// firstCriteria=criteriaCode;
				criteriaSet1.setCriteria(ProductDataStore
						.getCriteriaInfoForCriteriaCode(criteriaCode)
						.getDescription().replace("Size-", "").trim());
				criteriaValue=formatCriteriaValue(criteriaItems[1],
						criteriaCode);
				if(criteriaValue.contains(":") && criteriaCode.equals("IMMD")){
					// criteriaSet1.setValue(criteriaValue.substring(criteriaValue.indexOf(":")+1));
					criteriaSet1.setValue(CommonUtilities.getListData(criteriaValue.substring(criteriaValue.indexOf(":")+1)));
				}else{
					// criteriaSet1.setValue(criteriaValue);
					criteriaSet1.setValue(CommonUtilities.getListData(criteriaValue));
				}
			}
		} else {
			criteriaSet1 = new PriceConfiguration();
			// BUG: VELOEXTAPI-440
//			criteriaSet1.setValue(criteriaSetParser.findSizesCriteriaSetById(externalProductId, criteriaSetValueId));
			Object value = criteriaSetParser.findSizesCriteriaSetById(externalProductId, criteriaSetValueId);
			if(value instanceof java.util.List) {
				if(((java.util.List) value).size() == 1) {
					Value singleValue = (Value) ((java.util.List) value).get(0);
					value = singleValue;
				}
			}
			// firstCriteria=(String) getCriteriaCode(criteriaSet1);
			if (value instanceof Value) {
				Value currentCriteriaObj = (Value) value;
				criteriaSet1.setValue(CommonUtilities.getListData(currentCriteriaObj));
				if (APPAREL_SIZE_GROUP_CRITERIACODES
						.contains(currentCriteriaObj.getCriteriaType())) {
							crntCriteria=ProductDataStore
							.getCriteriaInfoForCriteriaCode(
									currentCriteriaObj.getCriteriaType())
									.getDescription().trim();
					if (crntCriteria.trim().startsWith("Size")
							|| crntCriteria.contains("Apparel")
							|| crntCriteria.trim().startsWith("SIZE") || currentCriteriaObj.getCriteriaType().contains(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM))
						crntCriteria = "Size";
					criteriaSet1.setCriteria(crntCriteria);					
				}else{
					crntCriteria=ProductDataStore
							.getCriteriaInfoForCriteriaCode(
									currentCriteriaObj.getCriteriaType()).getDescription();
					if (crntCriteria.trim().startsWith("Size")
							|| crntCriteria.contains("Apparel")
							|| crntCriteria.trim().startsWith("SIZE") || currentCriteriaObj.getCriteriaType().contains(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM))
						crntCriteria = "Size";
					criteriaSet1.setCriteria(crntCriteria);					
				}
				// valuesList.add(criteriaSet1);
				// criteriaValues.setValue(valuesList);
				// criteriaSet1=criteriaValues;
			} else if (value instanceof List) {
				@SuppressWarnings("unchecked")
				// List<Value> valueList = (List<Value>) criteriaSet1.getValue();
				List<Value> valueList = (List<Value>) value;
				criteriaSet1.setValue((List<Object>)value);
				for (Value currentValue : valueList) {
					currentCriteria = ProductDataStore
							.getCriteriaInfoForCriteriaCode(
									currentValue.getCriteriaType())
							.getDescription().trim();
					if (currentCriteria.trim().startsWith("Size")
							|| currentCriteria.contains("Apparel")
							|| currentCriteria.trim().startsWith("SIZE"))
						currentCriteria = "Size";
					criteriaSet1.setCriteria(currentCriteria);
				}
			}
		}
		return criteriaSet1;
	}
}
