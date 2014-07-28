package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.asi.ext.api.exception.AmbiguousPriceCriteriaException;
import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.radar.lookup.model.SetCodeValueJsonModel;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.PriceGrids;
import com.asi.ext.api.radar.model.Prices;
import com.asi.ext.api.radar.model.PricingItems;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductConfigurations;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.service.model.Price;
import com.asi.ext.api.service.model.PriceConfiguration;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.PriceGridUtil;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.product.client.vo.Currency;
import com.asi.service.product.client.vo.DiscountRate;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PriceUnit;
import com.asi.service.product.client.vo.ProductDetail;

public class PriceGridParser extends ProductParser {

    private final String                                            CAN_ORDER_LESS_THAN_MINIMUM    = "Can order less than minimum";

    private static ConcurrentHashMap<String, SetCodeValueJsonModel> lessThanMinimumSetCodeValueMap = null;

    private static String                                           validCriteriaValues            = "";

    // public ProductDataStore productDataStore = new ProductDataStore();

    private String getUsageLevelCode(boolean priceType) {
        return ApplicationConstants.CONST_STRING_NONE_CAP;
    }
    
    private Set<String> getPriceCriteriaCodes(List<PriceConfiguration> priceConfigs) {
        Set<String> criteriaCodes = new HashSet<String>();
        for (PriceConfiguration config : priceConfigs) {
            CriteriaInfo crtInfo = ProductDataStore.getCriteriaInfoByDescription(config.getCriteria());
            if (crtInfo != null) {
                criteriaCodes.add(crtInfo.getCode());
            }
        }
        return criteriaCodes;
    }
    
    private String getPriceGridSubTypeCode(boolean priceType, List<PriceConfiguration> priceConfigs) {
        if (priceType) { // Base price
            return ApplicationConstants.CONST_BASE_PRICE_GRID_CODE;
        } else {
            Set<String> criteriaCodes = getPriceCriteriaCodes(priceConfigs);
            if (criteriaCodes.size() > 1) {
                return ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
            } else if (criteriaCodes.size() == 1) {
                return PriceGridUtil.getPriceGridSubTypeCode(CommonUtilities.getElementFromSet(criteriaCodes, 1), priceType);
            } else {
                return ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
            }
        }
    }
    
    private Currency getCurrencyModel(String serCurrency) {
        return ProductDataStore.getCurrencyForCode(serCurrency, false);
    }
    
    private DiscountRate getDiscountRate(String discountCode) {
        return ProductDataStore.getDiscountRate(discountCode, false);
    }
    
    private PriceUnit getPriceUnit(com.asi.ext.api.service.model.PriceUnit serPunit) {
        return ProductDataStore.getPriceUnit(serPunit.getName());
    }
    
    private String getPriceUnitName(com.asi.ext.api.service.model.PriceUnit serPunit) {
        if (serPunit != null && serPunit.getName() != null) {
            return serPunit.getName();
        } else if (serPunit != null) {
            serPunit.getPriceUnitName();
        } else {
            return ApplicationConstants.CONST_STRING_PIECE;
        }
        return ApplicationConstants.CONST_STRING_PIECE;
    }
    
    private List<com.asi.service.product.client.vo.Price> getPrices(List<Price> prices, String pGridId) {
        
        List<com.asi.service.product.client.vo.Price> finalPrices = new ArrayList<com.asi.service.product.client.vo.Price>();
        for (Price serPrice : prices) {
            com.asi.service.product.client.vo.Price veloPrice = new com.asi.service.product.client.vo.Price();
            
            veloPrice.setPriceGridId(pGridId);;
            veloPrice.setSequenceNumber(serPrice.getSequence());
            veloPrice.setQuantity(serPrice.getQty());
            veloPrice.setItemsPerUnit(serPrice.getPriceUnit() != null ? Integer.parseInt(serPrice.getPriceUnit().getItemsPerUnit()) : 1);
            
            veloPrice.setListPrice(Double.parseDouble(serPrice.getListPrice()));
            
            veloPrice.setDiscountRate(getDiscountRate(serPrice.getDiscountCode()));
            
            veloPrice.setPriceUnit(getPriceUnit(serPrice.getPriceUnit()));
            
            veloPrice.setPriceUnitName(getPriceUnitName(serPrice.getPriceUnit()));
            
            finalPrices.add(veloPrice);
        }
        return finalPrices;
    }
    
    private PriceGrid getMatchingPriceGrid(List<PriceGrid> extPriceGrids, com.asi.ext.api.service.model.PriceGrid serPGrid) {
        String extPriceModelString = getPriceModelStringFromServiceModel(serPGrid.getPrices()); 
        for (PriceGrid pGrid : extPriceGrids) {
            if (serPGrid.getIsBasePrice().equals(pGrid.getIsBasePrice())) {
                String extPgString = getPriceModelString(pGrid.getPrices());
                if (extPriceModelString.equalsIgnoreCase(extPgString)) {
                    return pGrid;
                }
            }
        }
        return null; // no match found
    }
    
    private String getPriceModelString(List<com.asi.service.product.client.vo.Price> prices) {
        String pString = "";
        for (com.asi.service.product.client.vo.Price p : prices) {
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getQuantity()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getListPrice()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getDiscountRate().getIndustryDiscountCode()), "$$$");
            //final delim
            pString = CommonUtilities.appendValue(pString, "---", "");
        }
        
        
        return pString;
    }
    
    private String getPriceModelStringFromServiceModel(List<Price> prices) {
        String pString = "";
        for (Price p : prices) {
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getQty()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getListPrice()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getDiscountCode()), "$$$");
            //final delim
            pString = CommonUtilities.appendValue(pString, "---", "");
        }
        
        
        return pString;
    }
    
    public List<PriceGrid> getPriceGrids(List<com.asi.ext.api.service.model.PriceGrid> servicePriceGrids, ProductDetail product) {
        int priceGridId = -1;
        
        List<PriceGrid> finalPGrids = new ArrayList<PriceGrid>();
        for (com.asi.ext.api.service.model.PriceGrid serPGrid : servicePriceGrids) {
            PriceGrid newPGrid = new PriceGrid();
            // Basic fields
            newPGrid.setID(String.valueOf(--priceGridId));
            newPGrid.setProductId(product.getID());
            newPGrid.setIsBasePrice(serPGrid.getIsBasePrice());
            newPGrid.setDescription(serPGrid.getDescription());
            newPGrid.setDisplaySequence(serPGrid.getSequence());
            newPGrid.setPriceIncludes(serPGrid.getPriceIncludes());
            newPGrid.setIsQUR(serPGrid.getIsQUR());
            // Required fields
            newPGrid.setIsCopy(false);
            newPGrid.setIsRange(false);
            newPGrid.setIsSpecial(false);
            // Currency
            newPGrid.setCurrency(getCurrencyModel(serPGrid.getCurrency()));
            // Price Grid Type Code
            newPGrid.setUsageLevelCode(getUsageLevelCode(serPGrid.getIsBasePrice()));
            newPGrid.setPriceGridSubTypeCode(getPriceGridSubTypeCode(serPGrid.getIsBasePrice(), serPGrid.getPriceConfigurations()));
            PriceGrid extPriceGrid = getMatchingPriceGrid(product.getPriceGrids(), serPGrid);
            if (extPriceGrid != null) { 
                newPGrid.setID(extPriceGrid.getID());
            } 
            newPGrid.setPrices(getPrices(serPGrid.getPrices(), newPGrid.getID()));
            // Pricing Item configs
            finalPGrids.add(newPGrid);
        }
        return finalPGrids;
    }
    
/*
  try {
  PriceGridParser priceGridParser = new PriceGridParser();
  
  LOGGER.info("Prices Transformation Starts :" + price);
  boolean isValidPGFound = false;
  String[] priceType = price.split("@");
  // int maxNumberOfPGrdidFound = PriceGridParser.findNumberOfPGridAvailable(priceType);
  int numberOfValidBasePriceGrids = PriceGridParser.findPGridCountByType(priceType, true);
  String[] quantityType = quantity.split("@");
  String[] discountType = discount.split("@");
  
  priceGridsLst = new PriceGrids[priceType.length];
  
  String[] actualPriceGrids = product.getPriceGrids()[0].getIsQUR().split(":");
  String actualCurrency = PriceGridParser.getCurrency(product.getPriceGrids()[0].getCurrency().getCode());
  // String pgSubType = "REGL";
  String pgUsageLevel = ApplicationConstants.CONST_STRING_NONE_CAP;
  String[] pricingSubTypeCode = product.getPriceGrids()[0].getPriceGridSubTypeCode().split("\\|");
  String[] pricingUsageLevel = product.getPriceGrids()[0].getUsageLevelCode().split("\\|");
  String[] priceCriteriaAry = product.getPriceGrids()[0].getPricingItems()[0].getCriteriaSetValueId()
  .split("\\$\\$\\$");
  
  String[] priceCriteria2Ary = product.getPriceGrids()[0].getPricingItems()[0].getDescription().split(
  "\\$\\$\\$");
  String[] temp = PriceGridParser.getValidPriceCriteriaCode(priceCriteriaAry, priceCriteria2Ary, true)
  .split("\\#\\$");
  
  String basePriceCriteria1Code = temp[0];
  String basePriceCriteria2Code = temp[1];
  Set<String> optionGroupCriteriaSet = new HashSet<>();
  if (PriceGridParser.isOptionGroup(basePriceCriteria1Code)
  | PriceGridParser.isOptionGroup(basePriceCriteria2Code)) {
  // Create combination for list option group
  optionGroupCriteriaSet = priceGridParser.getOptionGroupPriceCriterias(priceCriteriaAry,
  priceCriteria2Ary);
  }
  temp = PriceGridParser.getValidPriceCriteriaCode(priceCriteriaAry, priceCriteria2Ary, false).split(
  "\\#\\$");
  
  String upchargeCriteria1Code = temp[0];
  String upchargeCriteria2Code = temp[1];
  
  List<String> priceCriteriaValuesList = new ArrayList<String>();
  
  String[] priceIncludes = product.getPriceGrids()[0].getPriceIncludes().split(
  ApplicationConstants.PRODUCT_RPBST_ELEMENT_SPLITTER_CODE);
  boolean processPrices = true;
  String[] basePrices = product.getPriceGrids()[0].getDescription().split("\\$");
  int priceGridRowCount = 0;
  for (int priceTypeCntr = 0; priceTypeCntr < priceType.length; priceTypeCntr++) {
  PriceGrids tempPriceGrids = null;
  price = priceType[priceTypeCntr];
  quantity = quantityType[priceTypeCntr];
  if (priceTypeCntr % 2 == 0) {
  priceGridRowCount++;
  }
  String quantityChk = quantity.replaceAll(ApplicationConstants.CONST_STRING_NULL_SMALL, "");
  discount = discountType[priceTypeCntr];
  if (commonUtils.isThatValidInteger(quantityChk)) {
  try {
  tempPriceGrids = productParser.getPriceGridsByPriceType(null, product, price, quantity,
  discount, actualCurrency,
  CommonUtilities.parseBooleanValue(actualPriceGrids[priceTypeCntr]),
  basePrices[priceTypeCntr], (priceTypeCntr % 2 == 0), priceGridRowCount);
  } catch (Exception e) {
  tempPriceGrids = null;
  LOGGER.error("PriceGrid Processing failed ");
  batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
  + ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR
  + ":PriceGrid processing failed";
  
  }
  } else {
  processPrices = false;
  LOGGER.warn("Quantity Should be Integer Value");
  }
  if (null != tempPriceGrids) {
  if (!CommonUtilities.isValueNull(priceIncludes[priceTypeCntr])) {
  tempPriceGrids.setPriceIncludes(priceIncludes[priceTypeCntr]);
  } else {
  tempPriceGrids.setPriceIncludes("");
  }
  tempPriceGrids.setIsQUR(CommonUtilities.parseBooleanValue(actualPriceGrids[priceTypeCntr]));
  
  if (priceTypeCntr % 2 == 0) {
  tempPriceGrids.setPriceGridSubTypeCode(PriceGridUtil.getPriceGridSubTypeCode(
  priceCriteriaAry[priceTypeCntr], priceCriteria2Ary[priceTypeCntr], true));
  String priceGridDescription = "";
  // priceCriteriaAry[priceTypeCntr] will be in this format<criteriaCode>:<criteriaCodevalues
  // separated by ,>
  List<PricingItems> pricingItemsList = new ArrayList<PricingItems>();
  try {
  if (priceCriteriaValuesList.contains(PriceGridParser.getPriceCriteriaCombinationString(
  basePriceCriteria1Code, priceCriteriaAry[priceTypeCntr],
  basePriceCriteria2Code, priceCriteria2Ary[priceTypeCntr]))) {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
  "A PriceGrid already exists with the given Criteria "
  + CommonUtilities.getStringValue(priceCriteriaAry[priceTypeCntr]));
  continue;
  } else {
  pricingItemsList = priceGridParser.generatePricingItemsForCriteria(
  priceCriteriaAry[priceTypeCntr], product, tempPriceGrids,
  basePriceCriteria1Code, true);
  if (!pricingItemsList.isEmpty()) {
  priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
  basePriceCriteria1Code, priceCriteriaAry[priceTypeCntr],
  basePriceCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
  } else if (pricingItemsList.isEmpty()
  && numberOfValidBasePriceGrids > 2
  && ApplicationConstants.CONST_STRING_FALSE_SMALL
  .equalsIgnoreCase(tempPriceGrids.getIsQUR())
  && CommonUtilities.isValueNull(priceCriteriaAry[priceTypeCntr])) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId()
  .trim(), ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
  "PriceCriteria cannot be empty if product has multiple PriceGrid ");
  continue;
  }
  }
  if (!CommonUtilities.isValueNull(priceCriteriaAry[priceTypeCntr])
  && pricingItemsList.isEmpty()) {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
  "Invalid Base Price Criteria 1 " + priceCriteriaAry[priceTypeCntr]);
  continue;
  }
  } catch (AmbiguousPriceCriteriaException apre) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
  continue;
  }
  
  if (pricingItemsList != null && !pricingItemsList.isEmpty()
  && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  priceGridDescription = PriceGridParser.getValidCriteriaValues();
  }
  List<PricingItems> pricingItemList2 = new ArrayList<PricingItems>();
  try {
  if (!CommonUtilities.isValueNull(basePriceCriteria1Code)
  && basePriceCriteria1Code.equalsIgnoreCase(basePriceCriteria2Code)) {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
  "Invalid PriceCriteria "
  + CommonUtilities.getStringValue(priceCriteria2Ary[priceTypeCntr]));
  } else {
  pricingItemList2 = priceGridParser.generatePricingItemsForCriteria(
  priceCriteria2Ary[priceTypeCntr], product, tempPriceGrids,
  basePriceCriteria2Code, true);
  if (!pricingItemList2.isEmpty()) {
  priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
  basePriceCriteria1Code, priceCriteriaAry[priceTypeCntr],
  basePriceCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
  }
  }
  if (!CommonUtilities.isValueNull(priceCriteria2Ary[priceTypeCntr])
  && pricingItemsList.isEmpty()) {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
  "Invalid Base Price Criteria 2 " + priceCriteria2Ary[priceTypeCntr]);
  continue;
  }
  } catch (AmbiguousPriceCriteriaException apre) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
  continue;
  }
  
  if (pricingItemList2 != null && !pricingItemList2.isEmpty()
  && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  priceGridDescription = priceGridDescription.isEmpty() ? PriceGridParser
  .getValidCriteriaValues() : priceGridDescription + ", "
  + PriceGridParser.getValidCriteriaValues();
  }
  
  pricingItemsList.addAll(pricingItemList2);
  
  if (!CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  tempPriceGrids.setDescription(basePrices[priceTypeCntr]);
  } else if (!CommonUtilities.isValueNull(priceGridDescription)
  && !pricingItemsList.isEmpty()) {
  tempPriceGrids.setDescription(priceGridDescription);
  } else if (!pricingItemsList.isEmpty() && CommonUtilities.isValueNull(priceGridDescription)
  && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  tempPriceGrids.setDescription(product.getName());
  }
  
  if (!pricingItemsList.isEmpty()) {
  tempPriceGrids.setPricingItems(pricingItemsList.toArray(new PricingItems[0]));
  } else if (pricingItemsList != null && pricingItemsList.isEmpty() && isValidPGFound
  && tempPriceGrids != null) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
  "PriceGrid without a valid price criteria");
  tempPriceGrids = null;
  } else {
  tempPriceGrids.setPricingItems(new PricingItems[] {});
  }
  
  } else if (priceTypeCntr % 2 == 1) {
  if (CommonUtilities.isValueNull(upchargeCriteria1Code)) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_UP_CRT,
  "Upcharge criteria 1 is required for creating Upcharge PriceGrid");
  continue;
  }
  tempPriceGrids.setPriceGridSubTypeCode(PriceGridUtil.getPriceGridSubTypeCode(
  priceCriteriaAry[priceTypeCntr], priceCriteria2Ary[priceTypeCntr], false));
  String tempCriteriaCode = priceGridParser
  .getCriteriaCodeFromPriceCriteria(priceCriteriaAry[priceTypeCntr]);
  if (!CommonUtilities.isValueNull(basePriceCriteria1Code)
  && basePriceCriteria1Code.equalsIgnoreCase(tempCriteriaCode)) {
  if (PriceGridParser.isOptionGroup(tempCriteriaCode)) {
  if (priceGridParser.isOptionCriteriaAlreadyConfigured(
  priceCriteriaAry[priceTypeCntr], optionGroupCriteriaSet)) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId()
  .trim(), ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
  "Invalid Upcharge Price Criteria. Base Price Criteria ("
  + basePriceCriteria1Code + ") and Upcharge criteria ("
  + tempCriteriaCode + ") should be unique by value for Options");
  continue;
  }
  } else {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Base Price Criteria ("
  + basePriceCriteria1Code + ") and Upcharge criteria ("
  + upchargeCriteria1Code + ") cannot be same");
  continue;
  }
  }
  tempPriceGrids.setIsBasePrice("false");
  if (null == pricingSubTypeCodeWSResponse) {
  pricingSubTypeCodeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
  .get(ApplicationConstants.PRICING_SUBTYPECODE_LOOKUP));
  }
  String pricingSubTypeCodeValue = jsonProcessorObj.checkPricingCode(
  pricingSubTypeCodeWSResponse, pricingSubTypeCode[priceTypeCntr]);
  tempPriceGrids.setPriceGridSubTypeCode(pricingSubTypeCodeValue);
  if (null == pricingUsageLevelCodeWSResponse) {
  pricingUsageLevelCodeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
  .get(ApplicationConstants.PRICING_USAGELEVEL_LOOKUP));
  }
  String pricingUsageLevelValue = jsonProcessorObj.checkPricingCode(
  pricingUsageLevelCodeWSResponse, pricingUsageLevel[priceTypeCntr]);
  tempPriceGrids.setUsageLevelCode(pricingUsageLevelValue);
  
  // priceCriteriaAry[priceTypeCntr] will be in this format<criteriaCode>:<criteriaCodevalues
  // separated by ,>
  
  String priceGridDescription = "";
  List<PricingItems> pricingItemsList = new ArrayList<PricingItems>();
  try {
  
  pricingItemsList = priceGridParser.generatePricingItemsForCriteria(
  priceCriteriaAry[priceTypeCntr], product, tempPriceGrids,
  upchargeCriteria1Code, false);
  if (!pricingItemsList.isEmpty()) {
  priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
  upchargeCriteria1Code, priceCriteriaAry[priceTypeCntr],
  upchargeCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
  }
  }
  
  if (!CommonUtilities.isValueNull(priceCriteriaAry[priceTypeCntr])
  && pricingItemsList.isEmpty()) {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
  "Invalid UpCharge Criteria 1 " + priceCriteriaAry[priceTypeCntr]);
  continue;
  }
  } catch (AmbiguousPriceCriteriaException apre) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
  continue;
  }
  if (pricingItemsList != null && !pricingItemsList.isEmpty()
  && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  priceGridDescription = PriceGridParser.getValidCriteriaValues();
  }
  
  List<PricingItems> pricingItemList2 = new ArrayList<PricingItems>();
  ;
  try {
  
  pricingItemList2 = priceGridParser.generatePricingItemsForCriteria(
  priceCriteria2Ary[priceTypeCntr], product, tempPriceGrids,
  upchargeCriteria2Code, false);
  if (!pricingItemList2.isEmpty()) {
  priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
  upchargeCriteria1Code, priceCriteriaAry[priceTypeCntr],
  upchargeCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
  }
  }
  if (!CommonUtilities.isValueNull(priceCriteria2Ary[priceTypeCntr])
  && pricingItemsList.isEmpty()) {
  productDataStore.addErrorToBatchLogCollection(
  product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
  "Invalid UpCharge Criteria 2 " + priceCriteria2Ary[priceTypeCntr]);
  continue;
  }
  } catch (AmbiguousPriceCriteriaException apre) {
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
  continue;
  }
  
  if (pricingItemList2 != null && !pricingItemList2.isEmpty()
  && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  priceGridDescription = priceGridDescription.isEmpty() ? PriceGridParser
  .getValidCriteriaValues() : priceGridDescription + ", "
  + PriceGridParser.getValidCriteriaValues();
  }
  
  pricingItemsList.addAll(pricingItemList2);
  if (!CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  tempPriceGrids.setDescription(basePrices[priceTypeCntr]);
  } else if (!CommonUtilities.isValueNull(priceGridDescription)
  && !pricingItemsList.isEmpty()) {
  tempPriceGrids.setDescription(priceGridDescription);
  } else if (!pricingItemsList.isEmpty() && CommonUtilities.isValueNull(priceGridDescription)
  && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
  tempPriceGrids.setDescription(product.getName());
  }
  
  if (!pricingItemsList.isEmpty()) {
  tempPriceGrids.setPricingItems(pricingItemsList.toArray(new PricingItems[0]));
  } else {
  tempPriceGrids.setPricingItems(new PricingItems[] {});
  }
  }
  if (tempPriceGrids != null && CommonUtilities.isValueNull(tempPriceGrids.getDescription())) {
  tempPriceGrids.setDescription(product.getName());
  }
  }
  if (tempPriceGrids != null) {
  isValidPGFound = true;
  }
  if (processPrices)
  priceGridsLst[priceTypeCntr] = tempPriceGrids;
  else
  priceGridsLst = new PriceGrids[] {};
  }
  
  if (processPrices && null != priceGridsLst[1])
  priceGridsLst[1].setIsBasePrice(ApplicationConstants.CONST_STRING_FALSE_SMALL);
  } catch (Exception e) {
  LOGGER.error("PriceGrid processing failed", e);
  String message = String
  .format("Exception while processing PriceGrid for Product %s with Product External ProductId %s, ErrorCode  = %s",
  product.getName(), product.getExternalProductId(),
  VelocityImportExceptionCodes.PG_EXCEPTION_UNKOWN);
  productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
  ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR, message);
  }
  List<PriceGrids> filterdPriceGrids = new ArrayList<PriceGrids>();
  int priceGridSequence = 0;
  for (int priceGridcntr = 0; priceGridcntr < priceGridsLst.length; priceGridcntr++) {
  if (priceGridsLst[priceGridcntr] != null) {
  priceGridsLst[priceGridcntr].setDisplaySequence(++priceGridSequence + "");
  filterdPriceGrids.add(priceGridsLst[priceGridcntr]);
  }
  }
  
  if (isSimplifiedTemplate && existingProduct != null) {
  filterdPriceGrids.addAll(priceGridParser.getUpchargePrices(existingProduct.getPriceGrids(),
  criteriaSetValueIds));
  }
  if (filterdPriceGrids != null && filterdPriceGrids.size() > 0) {
  priceGridsLst = filterdPriceGrids.toArray(new PriceGrids[0]);
  } else {
  priceGridsLst = new PriceGrids[] {};
  }
  
  LOGGER.info("Prices Transformation End");
  }
  
  // Comparing Current PriceGrid with the existing priceGrid
  
  if (existingProduct != null && existingProduct.getPriceGrids() != null
  && existingProduct.getPriceGrids().length > 0 && priceGridsLst != null && priceGridsLst.length > 0) {
  PriceGrids[] existingPriceGrids = existingProduct.getPriceGrids();
  // PriceGrids newPriceGrid = new PriceGrids();
  
  for (int existingPGCount = 0; existingPGCount < existingPriceGrids.length; existingPGCount++) {
  PriceGrids[] currentPriceGrids = priceGridsLst;
  PriceGrids existingPGrid = existingPriceGrids[existingPGCount];
  for (int currentPGCount = 0; currentPGCount < currentPriceGrids.length; currentPGCount++) {
  PriceGrids currentPGrid = currentPriceGrids[currentPGCount];
  if (isSimplifiedTemplate
  && currentPGrid.getIsBasePrice().equalsIgnoreCase(
  ApplicationConstants.CONST_STRING_FALSE_SMALL)) {
  priceGridsLst[currentPGCount] = currentPGrid;
  continue;
  } else if (existingPGrid.getIsBasePrice().equalsIgnoreCase(currentPGrid.getIsBasePrice())
  && existingPGrid.getIsQUR().equalsIgnoreCase(currentPGrid.getIsQUR())
  && existingPGrid.getDescription().equalsIgnoreCase(currentPGrid.getDescription())) {
  // BasePriceGrid Manipulation
  
  currentPGrid.setId(existingPGrid.getId());
  currentPGrid.setDisplaySequence(existingPGrid.getDisplaySequence());
  currentPGrid.setProductId(existingPGrid.getProductId());
  // Set more elements if needed
  // Currency Manipulation
  if (currentPGrid.getCurrency() != null
  && currentPGrid.getCurrency().getCode()
  .equalsIgnoreCase(existingPGrid.getCurrency().getCode())) {
  currentPGrid.setCurrency(existingPGrid.getCurrency());
  }
  // Price[] Manipulation
  if (currentPGrid.getPrices() != null && currentPGrid.getPrices().length > 0
  && existingPGrid.getPrices() != null && existingPGrid.getPrices().length > 0) {
  Prices[] curPrices = currentPGrid.getPrices();
  Prices[] extPrices = existingPGrid.getPrices();
  // Now we need to iterate over each price and compare with existing one
  for (int extPriceCount = 0; extPriceCount < extPrices.length; extPriceCount++) {
  
  for (int curPriceCount = 0; curPriceCount < curPrices.length; curPriceCount++) {
  
  if (PriceGridParser.isPricesEqual(extPrices[extPriceCount],
  curPrices[curPriceCount])) {
  curPrices[curPriceCount] = extPrices[extPriceCount];
  break;
  }
  }
  }
  
    
    */
    protected ProductCriteriaSets getLessThanMinimumCriteriaSet(Product product) throws VelocityException {

        if (lessThanMinimumSetCodeValueMap == null || lessThanMinimumSetCodeValueMap.isEmpty()) {
            String lessThanMinWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                    .get(ApplicationConstants.LESS_THAN_MIN_LOOKUP));
            lessThanMinimumSetCodeValueMap = jsonProcessorObj.getSetCodeValuesForIndividualCriteriaCode(lessThanMinWSResponse,
                    ApplicationConstants.CONST_LESS_THAN_MIN_CRT_CODE);

            if (lessThanMinimumSetCodeValueMap == null || lessThanMinimumSetCodeValueMap.isEmpty()) {
                // TODO : ADD batch error and return null;
                return null;
            }
        }
        criteriaSetUniqueId--;
        ProductCriteriaSets productCriteriaSet = new ProductCriteriaSets();
        productCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_LESS_THAN_MIN_CRT_CODE);
        productCriteriaSet.setCompanyId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getCompanyId());
        productCriteriaSet.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
        productCriteriaSet.setProductId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getProductId());
        productCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        productCriteriaSet.setConfigId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getConfigId());
        // Find from setCodeValues of LessThanMin JSON Dataset
        String setCodeValueId = lessThanMinimumSetCodeValueMap.get(ApplicationConstants.CONST_STRING_OTHER) != null ? lessThanMinimumSetCodeValueMap
                .get(ApplicationConstants.CONST_STRING_OTHER).getId() : null;
        if (setCodeValueId != null) {
            CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
            criteriaSetValue.setCriteriaSetId(productCriteriaSet.getCriteriaSetId());
            criteriaSetValuesID = criteriaSetValuesID - 1;
            criteriaSetValue.setId(criteriaSetValuesID + "");
            criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_LESS_THAN_MIN_CRT_CODE);
            productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(),
                    ApplicationConstants.CONST_LESS_THAN_MIN_CRT_CODE, "Y", criteriaSetValue.getId());

            criteriaSetValue.setCriteriaValueDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                    .getCriteriaSetValues()[0].getCriteriaValueDetail());
            criteriaSetValue
                    .setIsSubset(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getCriteriaSetValues()[0]
                            .getIsSubset());
            criteriaSetValue.setIsSetValueMeasurement(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                    .getCriteriaSetValues()[0].getIsSetValueMeasurement());

            CriteriaSetCodeValues[] criteriaSetCodeValues = new CriteriaSetCodeValues[1];

            CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();
            criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
            criteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValuesID + "");

            criteriaSetCodeValues[0] = criteriaSetCodeValue;

            criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);

            // Value processing
            criteriaSetValue.setValue(CAN_ORDER_LESS_THAN_MINIMUM);

            productCriteriaSet.setCriteriaSetValues(new CriteriaSetValues[] { criteriaSetValue });

            return productCriteriaSet;
        } else {
            // TODO : Add batch error log about setCodeValue Id not found
            return null;
        }
    }

    public synchronized List<PricingItems> generatePricingItemsForCriteria(String priceCriteria, Product product,
            PriceGrids priceGrids, String pricecriteriaCode, boolean isBasePrice) throws AmbiguousPriceCriteriaException {
        validCriteriaValues = "";
        try {
            if (!CommonUtilities.isValueNull(priceCriteria)) {
                if (CommonUtilities.isValidPriceCriteria(priceCriteria)) {
                    String criteriaCode = priceCriteria.trim().substring(0, priceCriteria.indexOf(":"));
                    if (isBasePrice && !criteriaCode.equalsIgnoreCase(pricecriteriaCode) && !relaxBasePriceRule(criteriaCode, pricecriteriaCode)) {
                        throw new AmbiguousPriceCriteriaException("Ambiguous/Invalid Price Criteria "
                                + CommonUtilities.getStringValue(criteriaCode) + " specified for PriceGrid");
                    }

                    if (isBasePrice && !isCriteriaCodeValidForPriceGrid(criteriaCode, isBasePrice)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Criteria Code " + criteriaCode
                                        + " is not valid for Base Price");
                        return new ArrayList<>();
                    } else if (!isBasePrice && !isCriteriaCodeValidForPriceGrid(criteriaCode, isBasePrice)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Criteria Code " + criteriaCode
                                        + " is not valid for Upcharge Price");
                        return new ArrayList<>();
                    }

                    if (!isValidOptionCriteria(priceCriteria.substring(criteriaCode.length() - 1), criteriaCode)) {
                        return new ArrayList<>();
                    }
                    Set<String> criteriaValues = getBasePriceCriteriaValues(priceCriteria, criteriaCode);

                    if (criteriaValues != null && !criteriaValues.isEmpty()) {
                        List<PricingItems> pricingItemsList = new ArrayList<PricingItems>();
                        for (String criteria : criteriaValues) {
                            String criteriaSetValueId = productDataStore.findCriteriaSetValueIdForValue(product
                                    .getExternalProductId().trim(), criteriaCode, criteria);

                            if (!CommonUtilities.isValueNull(criteriaSetValueId)) {
                                PricingItems pricingItems = new PricingItems();
                                if (validCriteriaValues.isEmpty()) {
                                    validCriteriaValues = criteria;
                                } else {
                                    validCriteriaValues += ApplicationConstants.CONST_STRING_COMMA_SEP + criteria;
                                }
                                pricingItems.setId(ApplicationConstants.CONST_STRING_ZERO);
                                pricingItems.setCriteriaSetValueId(criteriaSetValueId);
                                pricingItems.setId(ApplicationConstants.CONST_STRING_ZERO);
                                pricingItems.setProductId(product.getId());
                                pricingItems.setMarketSegmentCode(ApplicationConstants.CONST_MARKET_SEGMENT_CODE);
                                pricingItems.setPriceGridTypeCode(ApplicationConstants.CONST_PRICE_GRID_TYPE_CODE);
                                pricingItems.setPriceGridId(priceGrids.getId());

                                pricingItemsList.add(pricingItems);

                            } else if (criteriaValues.size() > 1) {

                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
                                        "PriceGrid - Given Criteria value " + criteria + " dosen't exist");
                            }
                        }
                        return pricingItemsList;
                    } else {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "PriceGrid Criteria " + priceCriteria
                                        + " is not valid");
                    }
                } else {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Price Criteria " + priceCriteria);
                    return new ArrayList<>();
                }
            } else {
                return new ArrayList<>();
            }
            return new ArrayList<>();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return new ArrayList<>();
        }
    }

    private static Set<String> getBasePriceCriteriaValues(String source, String criteriaCode) {
        if (!CommonUtilities.isValueNull(source)) {
            Set<String> criteriaValues = new HashSet<String>();
            int index = source.indexOf(":");
            if (index != -1 && source.length() > index) {
                String[] temp;
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_CRITERIA_CODE_FOBP)) {
                    temp = CommonUtilities.getOriginalCSVValues(source.substring(index + 1, source.length()));
                } else {
                    temp = source.substring(index + 1, source.length()).split(ApplicationConstants.CONST_STRING_COMMA_SEP);
                }

                for (int i = 0; i < temp.length; i++) {
                    if (!CommonUtilities.isValueNull(temp[i])) {
                        criteriaValues.add(String.valueOf(temp[i]).trim());
                    }
                }
                return criteriaValues;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getValidCriteriaValues() {
        return validCriteriaValues;
    }

    public static String getValidPriceCriteriaCode(String[] priceCriterias, String[] priceCriterias2, boolean isBasePrice) {

        String priceCriteriaCode = "null#$null";

        if (priceCriterias != null && priceCriterias.length > 0) {

            if (isBasePrice && !CommonUtilities.isValueNull(priceCriterias[0])
                    && CommonUtilities.isValidPriceCriteria(priceCriterias[0])) {
                priceCriteriaCode = priceCriterias[0].trim().substring(0, priceCriterias[0].trim().indexOf(":"));
                if (PriceGridUtil.isCriteriaCodeValidForPriceGrid(priceCriteriaCode, isBasePrice)) {
                    priceCriteriaCode += "#$"
                            + (priceCriterias2[0].trim().indexOf(":") != -1 ? priceCriterias2[0].trim().substring(0,
                                    priceCriterias2[0].trim().indexOf(":")) : "null");

                    return priceCriteriaCode;
                }
            } else if (!isBasePrice && !CommonUtilities.isValueNull(priceCriterias[1])
                    && CommonUtilities.isValidPriceCriteria(priceCriterias[1])) {
                priceCriteriaCode = priceCriterias[1].trim().substring(0, priceCriterias[1].trim().indexOf(":"));
                if (PriceGridUtil.isCriteriaCodeValidForPriceGrid(priceCriteriaCode, isBasePrice)) {

                    priceCriteriaCode += "#$"
                            + (priceCriterias2[1].trim().indexOf(":") != -1 ? priceCriterias2[1].trim().substring(0,
                                    priceCriterias2[1].trim().indexOf(":")) : "null");

                    return priceCriteriaCode;
                }
            }
            // If first set dosen't have a valid Criteria set so find next valid one

            for (int i = 0; i < priceCriterias.length; i++) {

                if (((i % 2) == 0 && isBasePrice) && !CommonUtilities.isValueNull(priceCriterias[i])
                        && CommonUtilities.isValidPriceCriteria(priceCriterias[i])) {
                    priceCriteriaCode = priceCriterias[i].trim().substring(0, priceCriterias[i].trim().indexOf(":"));
                    if (PriceGridUtil.isCriteriaCodeValidForPriceGrid(priceCriteriaCode, isBasePrice)) {

                        priceCriteriaCode += "#$"
                                + (priceCriterias2[i].trim().indexOf(":") != -1 ? priceCriterias2[i].trim().substring(0,
                                        priceCriterias2[i].trim().indexOf(":")) : "null");

                        return priceCriteriaCode;
                    }
                } else if (((i % 2) != 0 && !isBasePrice) && !CommonUtilities.isValueNull(priceCriterias[0])
                        && CommonUtilities.isValidPriceCriteria(priceCriterias[i])) {
                    priceCriteriaCode = priceCriterias[i].trim().substring(0, priceCriterias[i].trim().indexOf(":"));
                    if (PriceGridUtil.isCriteriaCodeValidForPriceGrid(priceCriteriaCode, isBasePrice)) {

                        priceCriteriaCode += "#$"
                                + (priceCriterias2[i].trim().indexOf(":") != -1 ? priceCriterias2[i].trim().substring(0,
                                        priceCriterias2[i].trim().indexOf(":")) : "null");

                        return priceCriteriaCode;
                    }
                }
            }

        }
        if (!priceCriteriaCode.contains("#$")) {
            priceCriteriaCode += "#$null";
        }

        return priceCriteriaCode;
    }

    protected static String getPriceGridDescription(PriceGrids pGrids, String basePriceName) {
        return null;
    }

    public static PriceGrids createNewPriceGrid(Product product, Prices price) {
        return null;
    }

    public static PriceGrids compareAndUpdatePriceGrid(PriceGrids newPriceGrids, PriceGrids existingPriceGrid,
            Product currentProduct) {
        return null;
    }

    public static boolean isPricesInDecreasingOrder(String[] prices) {
        double lastLowerValue = 0;
        try {
            for (int i = 0; i < prices.length; i++) {
                if (i == 0) {
                    lastLowerValue = Double.parseDouble(prices[i]);
                    continue;
                }
                if (!CommonUtilities.isValueNull(prices[i])) {
                    int currentValue = Integer.parseInt(prices[i]);
                    if (currentValue < lastLowerValue) {
                        lastLowerValue = currentValue;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isQuantityInIncreasingOrder(String[] quantity) {
        int lastHigherValue = 0;
        try {
            for (int i = 0; i < quantity.length; i++) {
                String q = getQuantity(quantity[i]);
                if (i == 0) {
                    lastHigherValue = Integer.parseInt(q);
                    continue;
                }
                if (!CommonUtilities.isValueNull(q)) {
                    int currentValue = Integer.parseInt(q);
                    if (currentValue > lastHigherValue) {
                        lastHigherValue = currentValue;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    protected static String getQuantity(String quantity) {
        String[] elements = quantity.split(":");
        if (elements != null && elements.length != 0) {
            return elements[0];
        } else {
            return null;
        }
    }

    public static boolean isValidOptionCriteria(String priceCriteria, String criteriaCode) {
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
            return true;
        }
        return true;
    }

    public static boolean isValidPriceSet(String price, String quantity) {

        if (CommonUtilities.isValueNull(price) && CommonUtilities.isValueNull(quantity)) {
            return true;
        } else if (!CommonUtilities.isValueNull(price) && CommonUtilities.isValueNull(quantity)) {
            return false;
        } else if (CommonUtilities.isValueNull(price) && !CommonUtilities.isValueNull(quantity)) {
            return false;
        } else if (!CommonUtilities.isValueNull(price) && !PriceGridUtil.isValidQuantity(quantity)) {
            return false;
        }
        return true;
    }

    public static Prices findPriceByIndex(int index) {
        return null;
    }

    protected static String getPriceCriteriaCombinationString(String criteriaCode1, String criteria1, String criteriaCode2,
            String criteria2) {

        return criteriaCode1 + "_" + criteria1 + "_" + criteriaCode2 + "_" + criteria2;
    }

    public static String getCurrency(String src) {
        if (CommonUtilities.isValueNull(src)) {
            return "USD";
        } else {
            return src.trim();
        }
    }

    protected static int findNumberOfPGridAvailable(String[] prices) {
        int count = 0;
        for (int i = 0; i < prices.length; i++) {
            count = CommonUtilities.isElementsAreNull(prices[i], "$") ? count : ++count;
        }
        return count;
    }

    public static int findPGridCountByType(String[] prices, boolean isBasePrice) {
        int count = 0;
        for (int i = 0; i < prices.length; i++) {
            if (isBasePrice && i % 2 == 0) {
                count = CommonUtilities.isElementsAreNull(prices[i], "$") ? count : ++count;
            }
        }
        return count;
    }

    /**
     * Return Upcharge prices from the given price grids
     * 
     * @param exisistingPGs
     *            is collection of price grids
     * @return a collection of upcharge prices, otherwise empty list if the given collection is null or the collection dosen't have
     *         any ucharges
     */
    public List<PriceGrids> getUpchargePrices(PriceGrids[] exisistingPGs, List<String> criteriaSetValueIds) {

        ArrayList<PriceGrids> upchargePriceGrids = new ArrayList<>();
        if (exisistingPGs != null) {
            for (PriceGrids pGrid : exisistingPGs) {
                if (pGrid != null && !CommonUtilities.getBooleanValueFromString(pGrid.getIsBasePrice())) {
                    if (pGrid.getPricingItems() != null && pGrid.getPricingItems().length > 0) {
                        PricingItems[] pricingItems = getValidPricingItem(pGrid.getPricingItems(), criteriaSetValueIds).toArray(
                                new PricingItems[0]);
                        pGrid.setPricingItems(pricingItems);
                        upchargePriceGrids.add(pGrid);
                    } else {
                        upchargePriceGrids.add(pGrid);
                    }
                }
            }
        } else {
            return upchargePriceGrids;
        }
        return upchargePriceGrids;
    }

    private List<PricingItems> getValidPricingItem(PricingItems[] pricingItems, List<String> criteriaSetValueIds) {
        List<PricingItems> validPricingItems = new ArrayList<PricingItems>();

        for (PricingItems priceItem : pricingItems) {
            if (criteriaSetValueIds.contains(priceItem.getCriteriaSetValueId())) {
                validPricingItems.add(priceItem);
            }
        }
        return validPricingItems;
    }

    public Set<String> getAllCriteriaSetValueIds(ProductConfigurations productConfigurations) {
        Set<String> criteriaSetValueIds = new HashSet<String>();
        if (productConfigurations != null && productConfigurations.getProductCriteriaSets() != null
                && productConfigurations.getProductCriteriaSets().length > 0) {
            for (ProductCriteriaSets prodCriteriaSet : productConfigurations.getProductCriteriaSets()) {
                if (prodCriteriaSet != null) {
                    for (CriteriaSetValues criteriaSetValue : prodCriteriaSet.getCriteriaSetValues()) {
                        if (criteriaSetValue != null) {
                            criteriaSetValueIds.add(criteriaSetValue.getId());
                        }
                    }
                }
            }
        }
        return criteriaSetValueIds;
    }

    protected boolean processPriceGridForSimplifiedTemplate(String priceCriteria, String criteriaCode) {
        HashMap<String, String> tempMap = new HashMap<>();
        String criteriaSetValueId = "";

        if ("specialCriterias".contains(criteriaCode)) { // TODO : Change ***specialCriterias*** to actual codes
            // checking the pricing item contains an element for that match
            if (tempMap.containsKey(criteriaCode + "__" + criteriaSetValueId)) {
                String value = tempMap.get(criteriaCode + "__" + criteriaSetValueId);
                if (value != null && value.equalsIgnoreCase(priceCriteria)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isPricesEqual(Prices p1, Prices p2) {
        if (!String.valueOf(p1.getListPrice()).trim().equalsIgnoreCase(String.valueOf(p2.getListPrice()).trim())) {
            return false;
        }
        if (!String.valueOf(p1.getQuantity()).trim().equalsIgnoreCase(String.valueOf(p2.getQuantity()).trim())) {
            return false;
        }
        if (p1.getDiscountRate() != null
                && p2.getDiscountRate() != null
                && !String.valueOf(p1.getDiscountRate().getCode()).trim()
                        .equalsIgnoreCase(String.valueOf(p2.getDiscountRate().getCode().trim()))) {
            return false;
        }
        return true;
    }

    protected boolean isCriteriaCodeValidForPriceGrid(String criteriaCode, boolean isBasePrice) {
        if (CommonUtilities.isValueNull(criteriaCode)) {
            return true;
        }
        if (isBasePrice) {
            return ApplicationConstants.BASE_PRICE_CRITERIA_SET.contains(criteriaCode.trim().toUpperCase());
        } else {
            return ApplicationConstants.UPCHARGE_PRICE_CRITERIA_SET.contains(criteriaCode.trim().toUpperCase());
        }
    }

    protected boolean isPriceCriteriaValid(String criteria1, String criteria2) {
        if (isOptionGroup(criteria1) && isOptionGroup(criteria2)) {

        }
        return false;
    }

    protected Set<String> getOptionGroupPriceCriterias(String[] criteria1Array, String[] criteria2Array) {
        Set<String> optionGroups = new HashSet<>();
        int i = 0;
        try {
            for (String criteria : criteria1Array) {
                if (!CommonUtilities.isValueNull(criteria)) {
                    if (i % 2 != 0) {
                        i++;
                        continue;
                    }
                    i++;
                    String criteriaCode = criteria.trim().substring(0, criteria.indexOf(":"));
                    if (isOptionGroup(criteriaCode)) {
                        if (CommonUtilities.countSpecifiedChar(criteria, ':') > 1) {
                            String valueToPreserve = criteria.trim().substring(0, CommonUtilities.indexOfElement(criteria, ':', 2));
                            optionGroups.add(valueToPreserve);
                        } else {
                            optionGroups.add(criteriaCode);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        i = 0;
        try {
            for (String criteria : criteria2Array) {
                if (i % 2 != 0) {
                    i++;
                    continue;
                }
                i++;
                if (!CommonUtilities.isValueNull(criteria)) {
                    String criteriaCode = criteria.trim().substring(0, criteria.indexOf(":"));
                    if (isOptionGroup(criteriaCode)) {
                        if (CommonUtilities.countSpecifiedChar(criteria, ':') > 1) {
                            String valueToPreserve = criteria.trim().substring(0, CommonUtilities.indexOfElement(criteria, ':', 2));
                            optionGroups.add(valueToPreserve);
                        } else {
                            optionGroups.add(criteriaCode);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return optionGroups;
    }

    protected boolean isOptionCriteriaAlreadyConfigured(String upchargeCriteria, Set<String> optionGroupCriteriaSet) {
        if (!CommonUtilities.isValueNull(upchargeCriteria)) {
            String criteriaCode = upchargeCriteria.trim().substring(0, upchargeCriteria.indexOf(":"));
            if (isOptionGroup(criteriaCode)) {
                if (CommonUtilities.countSpecifiedChar(upchargeCriteria, ':') > 1) {
                    String valueToPreserve = upchargeCriteria.trim().substring(0,
                            CommonUtilities.indexOfElement(upchargeCriteria, ':', 2));
                    return optionGroupCriteriaSet.contains(valueToPreserve);
                } else {
                    return optionGroupCriteriaSet.contains(criteriaCode);
                }
            } else {
                return optionGroupCriteriaSet.contains(criteriaCode);
            }
        }
        return false;
    }

    protected String getCriteriaCodeFromPriceCriteria(String priceCriteria) {
        
        return PriceGridUtil.getPriceCriteriaCode(priceCriteria);
    }

    /**
     * Some of the Product Criteria need overcome the rules of base price Grid creation. This rule relaxtion determined by Criteria
     * codes
     * 
     * @param criteriaPaticipator
     * @param criteriaCodeToCheck
     * @return
     */
    private static boolean relaxBasePriceRule(String criteriaPaticipator, String criteriaCodeToCheck) {
        if (PriceGridUtil.isCriteriaInPRSGroup(criteriaPaticipator) && PriceGridUtil.isCriteriaInPRSGroup(criteriaCodeToCheck)) {
            return true;
        }
        return false;
    }
}
