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
import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.PriceGrids;
import com.asi.ext.api.radar.model.Prices;
import com.asi.ext.api.radar.model.PricingItems;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductConfigurations;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.PriceGridUtil;
import com.asi.ext.api.util.RestAPIProperties;

public class PriceGridParser extends ProductParser {

    private final String                                            CAN_ORDER_LESS_THAN_MINIMUM    = "Can order less than minimum";

    private static ConcurrentHashMap<String, SetCodeValueJsonModel> lessThanMinimumSetCodeValueMap = null;

    private static String                                           validCriteriaValues            = "";

    // public ProductDataStore productDataStore = new ProductDataStore();

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
