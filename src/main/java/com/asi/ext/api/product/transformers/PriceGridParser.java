package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.exception.AmbiguousPriceCriteriaException;
import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.integration.lookup.parser.PricesParser;
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
import com.asi.ext.api.service.model.Value;
import com.asi.ext.api.service.model.Values;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.PriceCriteriaComparator;
import com.asi.ext.api.util.PriceGridUtil;
import com.asi.ext.api.util.ProductParserUtil;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.product.client.vo.Currency;
import com.asi.service.product.client.vo.DiscountRate;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PriceUnit;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductNumber;
import com.asi.service.product.client.vo.ProductNumberConfiguration;
import com.asi.service.product.client.vo.parser.UpChargeLookup;

public class PriceGridParser extends ProductParser {
	
	private final static Logger              LOGGER                      = Logger.getLogger(PriceGridParser.class.getName());

    private final String                                            CAN_ORDER_LESS_THAN_MINIMUM    = "Can order less than minimum";

    private ProductNumberCriteriaParser                             productNumberProcessor         = new ProductNumberCriteriaParser();

    private static ConcurrentHashMap<String, SetCodeValueJsonModel> lessThanMinimumSetCodeValueMap = null;
    @Autowired
    private UpChargeLookup                                          upChargeLookup                 = new UpChargeLookup();

    private static String                                           validCriteriaValues            = "";
    private LinkedList<?>                                           currenciesWSList               = null;
    public static RestTemplate                                      lookupRestTemplate;
    public ProductDataStore                                         productDataStore               = new ProductDataStore();
    protected JerseyClientPost                                      orgnCall                       = new JerseyClientPost();
    private static int                                              uniqPItemId                    = -1;

    private String getUsageLevelCode(boolean priceType, String usageLevel) {
        if (priceType) { 
            return ApplicationConstants.CONST_STRING_NONE_CAP;
        } else if (!CommonUtilities.isValueNull(usageLevel)) {
            String usageLevelCode =  ProductDataStore.getPriceGridUsageLevelCodeByName(usageLevel);
            if (usageLevelCode == null) {
                return ApplicationConstants.CONST_STRING_NONE_CAP;
            } else {
                return usageLevelCode;
            }
        }
        return ApplicationConstants.CONST_STRING_NONE_CAP;
    }

    private Set<String> getPriceCriteriaCodes(List<PriceConfiguration> priceConfigs, String xid) {
        Set<String> criteriaCodes = new HashSet<String>();
        for (PriceConfiguration config : priceConfigs) {
            if (config != null && !new PriceConfiguration().equals(config) && config.getCriteria() != null) {
                CriteriaInfo crtInfo = ProductDataStore.getCriteriaInfoByDescription(config.getCriteria(), xid);
                if (crtInfo != null) {
                    criteriaCodes.add(crtInfo.getCode());
                }
            }
        }
        return criteriaCodes;
    }

    private String getPriceGridSubTypeCode(boolean priceType, List<PriceConfiguration> priceConfigs, String pgGridSubType, String xid) {
        if (priceType) { // Base price
            return ApplicationConstants.CONST_BASE_PRICE_GRID_CODE;
        } else if (!CommonUtilities.isValueNull(pgGridSubType)) {
            String subtypeCode =  ProductDataStore.getPriceGridSubtypeCodeByName(pgGridSubType);
            if (subtypeCode == null) {
                if (priceType) { // Base price
                    return ApplicationConstants.CONST_BASE_PRICE_GRID_CODE;
                } else {
                    return ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
                }
            } else {
                return subtypeCode;
            }
        } else {
            Set<String> criteriaCodes = getPriceCriteriaCodes(priceConfigs, xid);
            if (criteriaCodes.size() > 1) {
                return ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
            } else if (criteriaCodes.size() == 1) {
                return PriceGridUtil.getPriceGridSubTypeCode(CommonUtilities.getElementFromSet(criteriaCodes, 1), priceType);
            } else {
                return ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
            }
        }
    }

    private Currency getCurrencyModel(String serCurrency, boolean getDefault) {
        return ProductDataStore.getCurrencyForCode(serCurrency, getDefault);
    }

    private DiscountRate getDiscountRate(String discountCode) {
        return ProductDataStore.getDiscountRate(discountCode, false);
    }

    private PriceUnit getPriceUnit(String serPunit, boolean getDefault) {
        return ProductDataStore.getPriceUnit(serPunit, getDefault);
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

    private List<com.asi.service.product.client.vo.Price> getPrices(List<Price> prices, String pGridId, String xid) {

        List<com.asi.service.product.client.vo.Price> finalPrices = new ArrayList<com.asi.service.product.client.vo.Price>();
        for (Price serPrice : prices) {
            com.asi.service.product.client.vo.Price veloPrice = new com.asi.service.product.client.vo.Price();

            veloPrice.setPriceGridId(pGridId);
            veloPrice.setSequenceNumber(serPrice.getSequence());
            veloPrice.setQuantity(serPrice.getQty());
            veloPrice.setItemsPerUnit(1);

            PriceUnit priceUnit = new PriceUnit();

            if (serPrice.getPriceUnit() == null) {
                priceUnit = getPriceUnit(ApplicationConstants.CONST_STRING_PIECE, true);
                veloPrice.setItemsPerUnit(1);
                veloPrice.setPriceUnit(priceUnit);
            } else {
                priceUnit = getPriceUnit(serPrice.getPriceUnit().getName(), false);
                if (priceUnit == null) {
                    productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                            "Invalid Price Unit given, PriceUnit : " + serPrice.getPriceUnit().getName()
                                    + ", default value (Piece) taken");
                    priceUnit = getPriceUnit(ApplicationConstants.CONST_STRING_PIECE, true);
                    veloPrice.setPriceUnit(priceUnit);
                } else if (priceUnit.getDescription().equalsIgnoreCase(ApplicationConstants.CONST_STRING_OTHER)) {
                    if (serPrice.getPriceUnit().getPriceUnitName() != null) {
                        veloPrice.setPriceUnitName(serPrice.getPriceUnit().getPriceUnitName());
                    } else {
                        veloPrice.setPriceUnitName(ApplicationConstants.CONST_STRING_OTHER);
                    }
                    veloPrice.setPriceUnit(priceUnit);
                    try {
                        veloPrice.setItemsPerUnit(Integer.parseInt(serPrice.getPriceUnit().getItemsPerUnit()));
                    } catch (NumberFormatException nfe) {
                        productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                "Invalid value found for ItemsPerUnit, ItemsPerUnit : " + serPrice.getPriceUnit().getItemsPerUnit()
                                        + ", default value (1) taken");
                    }
                } else {
                    if (priceUnit.getItemsPerUnit().equalsIgnoreCase("0") && serPrice.getPriceUnit().getItemsPerUnit() != null) {
                        try {
                            veloPrice.setItemsPerUnit(Integer.parseInt(serPrice.getPriceUnit().getItemsPerUnit()));
                            veloPrice.setPriceUnitName(serPrice.getPriceUnit().getName());
                            veloPrice.setPriceUnit(priceUnit);
                        } catch (NumberFormatException nfe) {
                            productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                    "Incorrect format for ItemsPerUnit, ItemsPerUnit : "
                                            + serPrice.getPriceUnit().getItemsPerUnit() + ", default value (1) taken");
                        }
                    } else {
                        veloPrice.setItemsPerUnit(Integer.parseInt(priceUnit.getItemsPerUnit()));
                        veloPrice.setPriceUnit(priceUnit);
                    }
                }
            }

            try {
            	veloPrice.setListPrice(Double.parseDouble(serPrice.getListPrice()));
            } catch (Exception nfe) {
            	LOGGER.error("Error occurred while parsing ListPrice for value given: " + serPrice.getListPrice());
            	veloPrice.setListPrice(0.0d);
            }

            veloPrice.setDiscountRate(getDiscountRate(serPrice.getDiscountCode()));

            // veloPrice.setPriceUnit(getPriceUnit(serPrice.getPriceUnit()));

            veloPrice.setPriceUnitName(getPriceUnitName(serPrice.getPriceUnit()));

            finalPrices.add(veloPrice);
        }
        return finalPrices;
    }

    private PriceGrid getMatchingPriceGrid(List<PriceGrid> extPriceGrids, com.asi.ext.api.service.model.PriceGrid serPGrid) {
        boolean needDeepComapre = false;
        String extPriceModelString = getPriceModelStringFromServiceModel(serPGrid, serPGrid.getPrices(), needDeepComapre);
        PriceGrid firstMatch = null;
        for (PriceGrid pGrid : extPriceGrids) {
            if (serPGrid.getIsBasePrice().equals(pGrid.getIsBasePrice())) {
                String extPgString = getPriceModelString(pGrid, pGrid.getPrices(), false);
                if (extPriceModelString.equalsIgnoreCase(extPgString)) {
                    if (firstMatch != null) {
                        needDeepComapre = true;
                    }
                    firstMatch = pGrid;
                }
            }
        }
        if (needDeepComapre) {
            extPriceModelString = getPriceModelStringFromServiceModel(serPGrid, serPGrid.getPrices(), needDeepComapre);
            for (PriceGrid pGrid : extPriceGrids) {
                if (serPGrid.getIsBasePrice().equals(pGrid.getIsBasePrice())) {
                    String extPgString = getPriceModelString(pGrid, pGrid.getPrices(), needDeepComapre);
                    if (extPriceModelString.equalsIgnoreCase(extPgString)) {
                        return pGrid;
                    }
                }
            }
        } else {
            return firstMatch;
        }
        return null; // no match found
    }

    private String getPriceModelString(PriceGrid pGrid, List<com.asi.service.product.client.vo.Price> prices, boolean deepComparison) {
        String pString = pGrid.getIsBasePrice() + "###" + pGrid.getIsQUR();
        if (deepComparison) {
            pString = pGrid.getIsBasePrice() + "###" + pGrid.getDisplaySequence() + "###" + pGrid.getIsQUR() + "###"
                    + pGrid.getPriceIncludes();
        }
        for (com.asi.service.product.client.vo.Price p : prices) {
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getQuantity()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getListPrice()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getDiscountRate().getIndustryDiscountCode()), "$$$");
            // final delim
            pString = CommonUtilities.appendValue(pString, "---", "");
        }

        return pString;
    }

    private String getPriceModelStringFromServiceModel(com.asi.ext.api.service.model.PriceGrid pGrid, List<Price> prices,
            boolean deepComparison) {
        String pString = pGrid.getIsBasePrice() + "###" + pGrid.getIsQUR();
        if (deepComparison) {
            pString = pGrid.getIsBasePrice() + "###" + pGrid.getSequence() + "###" + pGrid.getIsQUR() + "###"
                    + pGrid.getPriceIncludes();
        }
        for (Price p : prices) {
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getQty()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getListPrice()), "$$$");
            pString = CommonUtilities.appendValue(pString, String.valueOf(p.getDiscountCode()), "$$$");
            // final delim
            pString = CommonUtilities.appendValue(pString, "---", "");
        }

        return pString;
    }

    public List<PriceGrid> getPriceGrids(List<com.asi.ext.api.service.model.PriceGrid> servicePriceGrids, ProductDetail product) {
        int priceGridId = -1;
        uniqPItemId = -1;

        List<ProductNumber> productNumbers = new ArrayList<ProductNumber>();
        Map<String, ProductNumber> extPnoMap = productNumberProcessor.getExistingProductNumberMapForPriceGrid(product
                .getProductNumbers());
        boolean hasProductNumber = false;
        boolean foundOnePno = false;
        int pgCounter = 0;
        boolean currencyErrorLogged = false;
        List<PriceGrid> finalPGrids = new ArrayList<PriceGrid>();
        for (com.asi.ext.api.service.model.PriceGrid serPGrid : servicePriceGrids) {
            if (new com.asi.ext.api.service.model.PriceGrid().equals(serPGrid)) {
                continue;
            }

            hasProductNumber = !CommonUtilities.isValueNull(serPGrid.getProductNumber());
            if (!foundOnePno && hasProductNumber) {
                foundOnePno = true;
            }
            pgCounter++;
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
            newPGrid.setCurrency(getCurrencyModel(serPGrid.getCurrency(), false));
            if (newPGrid.getCurrency() == null) {
                if (!currencyErrorLogged) {
                    currencyErrorLogged = true;
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                            "PriceGrid : Currency not found / Invalid currency, Default currency (USD) assigned");
                }
                newPGrid.setCurrency(getCurrencyModel(serPGrid.getCurrency(), true));
            }
            // Price Grid Type Code
            newPGrid.setUsageLevelCode(getUsageLevelCode(serPGrid.getIsBasePrice(), serPGrid.getUpchargeUsageType()));
            newPGrid.setPriceGridSubTypeCode(getPriceGridSubTypeCode(serPGrid.getIsBasePrice(), serPGrid.getPriceConfigurations(), serPGrid.getUpchargeType(),
                    product.getExternalProductId()));
            PriceGrid extPriceGrid = getMatchingPriceGrid(product.getPriceGrids(), serPGrid);
            if (extPriceGrid != null) {
                newPGrid.setID(extPriceGrid.getID());
            }
            newPGrid.setPrices(getPrices(serPGrid.getPrices(), newPGrid.getID(), product.getExternalProductId()));
            // Pricing Item configs
            Collections.sort(serPGrid.getPriceConfigurations(), new PriceCriteriaComparator());

            if (extPriceGrid != null && extPriceGrid.getPricingItems() != null) {
                newPGrid.setPricingItems(getPricingItems(serPGrid.getPriceConfigurations(), newPGrid.getID(), product.getID(),
                        newPGrid.getIsBasePrice(), extPriceGrid.getPricingItems(), product.getExternalProductId()));
            } else {
                newPGrid.setPricingItems(getPricingItems(serPGrid.getPriceConfigurations(), newPGrid.getID(), product.getID(),
                        newPGrid.getIsBasePrice(), null, product.getExternalProductId()));
            }

            if (hasProductNumber) {
                productNumbers.add(productNumberProcessor.getProductNumberForPriceGrid(newPGrid.getID(),
                        serPGrid.getProductNumber(), newPGrid.getPricingItems(), extPnoMap.get(newPGrid.getID()), product.getID()));
                product.setProductNumbers(productNumbers); // Using Java Reference by Object Principal
            }

            finalPGrids.add(newPGrid);
        }

        if (foundOnePno) {
            product.setProductNumbers(productNumbers);
        }
        return finalPGrids;
    }

    private List<PricingItem> getPricingItems(List<PriceConfiguration> priceConfigs, String pGridId, String productId,
            boolean isBasePrice, List<PricingItem> extPItem, String xid) {
        List<PricingItem> finalPricingItems = new ArrayList<PricingItem>();

        for (PriceConfiguration pConfig : priceConfigs) {
            if (pConfig != null && !new PriceConfiguration().equals(pConfig) && pConfig.getCriteria() != null) {
                CriteriaInfo criteriaInfo = ProductDataStore.getCriteriaInfoByDescription(pConfig.getCriteria(), xid);
                if (criteriaInfo == null) {
                    // TODO: LOG Validation ERROR
                    productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                            "Criteria specified for product doesn't exist");
                    continue;
                }

                String criteriaSetValueId = ProductParserUtil.getCriteriaSetValueIdBaseOnValueType(xid, criteriaInfo.getCode(),
                        pConfig.getValue());
                if (criteriaSetValueId == null) {
                    productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                            "Criteria value specified for product doesn't exist, value : " + pConfig.getValue());
                } else {
                    finalPricingItems.add(getPricingItem(criteriaSetValueId, pGridId, productId, isBasePrice, extPItem));
                }
            }
        }
        return finalPricingItems;
    }

    private PricingItem getPricingItem(String criteriaSetValueId, String pGridId, String productId, boolean isBasePrice,
            List<PricingItem> extPricingItems) {
        if (extPricingItems != null && !extPricingItems.isEmpty()) {
            for (PricingItem item : extPricingItems) {
                if (criteriaSetValueId.equalsIgnoreCase(item.getCriteriaSetValueId())
                        && pGridId.equalsIgnoreCase(item.getPriceGridId())) {
                    return item;
                }
            }
        }
        return createNewPricingItem(criteriaSetValueId, pGridId, productId, isBasePrice);
    }

    private PricingItem createNewPricingItem(String crtValueId, String pGridId, String productId, boolean isBasePrice) {
        PricingItem pItem = new PricingItem();
        pItem.setCriteriaSetValueId(crtValueId);
        pItem.setDescription("");
        pItem.setId(String.valueOf(--uniqPItemId));
        pItem.setProductId(productId);
        pItem.setPriceGridId(pGridId);
        pItem.setIsDateBased(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        pItem.setIsPrimary(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        pItem.setMarketSegmentCode(ApplicationConstants.CONST_MARKET_SEGMENT_CODE);
        if (isBasePrice) {
            pItem.setPriceGridTypeCode(ApplicationConstants.CONST_BASE_PRICE_GRID_CODE);
        } else {
            pItem.setPriceGridTypeCode(ApplicationConstants.CONST_PRICE_GRID_ITEM_CODE);
        }

        return pItem;
    }

    protected ProductCriteriaSets getLessThanMinimumCriteriaSet(Product product) throws VelocityException {

        if (lessThanMinimumSetCodeValueMap == null || lessThanMinimumSetCodeValueMap.isEmpty()) {
            @SuppressWarnings("static-access")
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
                    if (isBasePrice && !criteriaCode.equalsIgnoreCase(pricecriteriaCode)
                            && !relaxBasePriceRule(criteriaCode, pricecriteriaCode)) {
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
                            @SuppressWarnings("static-access")
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
                                pricingItems.setPriceGridTypeCode(ApplicationConstants.CONST_PRICE_GRID_ITEM_CODE);
                                pricingItems.setPriceGridId(priceGrids.getId());

                                pricingItemsList.add(pricingItems);

                            } else if (criteriaValues.size() > 1) {

                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
                                        "PriceGrid - Given Criteria value " + criteria + " doesn't exist");
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

    public com.asi.ext.api.service.model.Product setProductWithPriceGrids(ProductDetail productDetail,
            com.asi.ext.api.service.model.Product serviceProduct) {
        List<PriceGrid> pricegridsList = productDetail.getPriceGrids();
        List<com.asi.service.product.client.vo.Price> pricesList = new ArrayList<>();
        List<Price> servicePricesList = null;
        com.asi.ext.api.service.model.Price currentPrices = null;
        com.asi.ext.api.service.model.PriceUnit currentPriceUnit = null;
        List<com.asi.ext.api.service.model.PriceGrid> servicePriceGrids = null;
        com.asi.ext.api.service.model.PriceGrid currentPriceGrid = null;
        pricegridsList = productDetail.getPriceGrids();
        if (null != pricegridsList && pricegridsList.size() > 0) {
            servicePriceGrids = new ArrayList<>();
            for (PriceGrid radarPriceGrid : pricegridsList) {
                currentPriceGrid = new com.asi.ext.api.service.model.PriceGrid();
                currentPriceGrid.setIsBasePrice(radarPriceGrid.getIsBasePrice());
                currentPriceGrid.setIsQUR(radarPriceGrid.getIsQUR());
                currentPriceGrid.setDescription(radarPriceGrid.getDescription());
                currentPriceGrid.setPriceIncludes(radarPriceGrid.getPriceIncludes());
                currentPriceGrid.setSequence(radarPriceGrid.getDisplaySequence());
                //
                if (null == currenciesWSList) {
                    currenciesWSList = lookupRestTemplate.getForObject(
                            RestAPIProperties.get(ApplicationConstants.CURRENCIES_LOOKUP_URL), LinkedList.class);
                }
                currentPriceGrid.setCurrency(jsonProcessorObj.checkCurrencyValueKeyPair(
                        productDetail.getExternalProductId().trim(), currenciesWSList, radarPriceGrid.getCurrency().getCode()));
                // Product Number - TBD
                currentPriceGrid.setProductNumber(getProductNumber(radarPriceGrid.getPricingItems(),
                        productDetail.getProductNumbers()));
                if(!currentPriceGrid.getIsBasePrice()){
                	upChargeLookup.setUpchargeTypelookupAPI(RestAPIProperties.get(ApplicationConstants.PRICING_SUBTYPECODE_LOOKUP));
                    upChargeLookup.setUsageLevelLookupAPI(RestAPIProperties.get(ApplicationConstants.PRICING_USAGELEVEL_LOOKUP));
                          
                	if (upChargeLookup != null) {
                	currentPriceGrid.setUpchargeType(upChargeLookup
        					.getUpChargeType(radarPriceGrid.getPriceGridSubTypeCode()));
                	currentPriceGrid.setUpchargeUsageType(upChargeLookup
        					.getUpchargeLevel(radarPriceGrid.getUsageLevelCode()));
        		}
                
                }
                pricesList = radarPriceGrid.getPrices();
                if (null != pricesList && pricesList.size() > 0) {
                    servicePricesList = new ArrayList<>();
                    for (com.asi.service.product.client.vo.Price currentPrice : pricesList) {
                        currentPrices = new com.asi.ext.api.service.model.Price();
                        currentPrices.setSequence(currentPrice.getSequenceNumber());
                        currentPrices.setQty(currentPrice.getQuantity());
                        currentPrices.setListPrice(String.valueOf(currentPrice.getListPrice()));
                        if (isValidDiscountCode(currentPrice.getDiscountRate().getIndustryDiscountCode()))
                            currentPrices.setDiscountCode(currentPrice.getDiscountRate().getIndustryDiscountCode());
                        if (null != currentPrice.getPriceUnit()) {
                            currentPriceUnit = new com.asi.ext.api.service.model.PriceUnit();
                            if (null != currentPrice.getItemsPerUnit() && currentPrice.getItemsPerUnit() > 0)
                                currentPriceUnit.setItemsPerUnit(String.valueOf(currentPrice.getItemsPerUnit()));

                            currentPriceUnit.setName((null != currentPrice.getPriceUnit().getDisplayName() && !currentPrice
                                    .getPriceUnit().getDisplayName().isEmpty()) ? currentPrice.getPriceUnit().getDisplayName()
                                    : null);
                            if (ProductDataStore.isOtherPriceUnit(currentPrice.getPriceUnit().getDisplayName())) {
                                currentPriceUnit.setPriceUnitName(currentPrice.getPriceUnitName());
                            }
                            
                            currentPrices.setPriceUnit(currentPriceUnit);
                        }
                        servicePricesList.add(currentPrices);
                    }
                    if (servicePricesList.size() > 0)
                        currentPriceGrid.setPrices(servicePricesList);
                    else
                        currentPriceGrid.setPrices(null);
                    // currentPriceGrid.setPriceConfigurations(setPriceGridWithItsPriceCriteria(radarPriceGrid, productDetail));
                }

                currentPriceGrid.setPriceConfigurations(setPriceGridWithItsPriceCriteria(radarPriceGrid, productDetail));
                servicePriceGrids.add(currentPriceGrid);
            }
            serviceProduct.setPriceGrids(servicePriceGrids);
        }

        return serviceProduct;
    }

    private String getProductNumber(List<PricingItem> pricingItems, List<ProductNumber> productNumbers) {
        String finalProductNumber = null;
        if (null != pricingItems && pricingItems.size() > 0) {
            for (PricingItem currentPricingItem : pricingItems) {
                for (ProductNumber currentProductNumber : productNumbers) {
                    for (ProductNumberConfiguration currentProductNumberConfig : currentProductNumber
                            .getProductNumberConfigurations())
                        if (currentPricingItem.getCriteriaSetValueId().equals(
                                String.valueOf(currentProductNumberConfig.getCriteriaSetValueId()))) {
                            finalProductNumber = currentProductNumber.getValue();
                            break;
                        }
                    if (null != finalProductNumber) break;
                }
                if (null != finalProductNumber) break;
            }
        }
        return finalProductNumber;
    }

    private boolean isValidDiscountCode(String code) {
        DiscountRate currentDiscountRate = ProductDataStore.getDiscountRate(code, true);
        if (null != currentDiscountRate && currentDiscountRate.getIndustryDiscountCode().equalsIgnoreCase(code))
            return true;
        else
            return false;
    }

    private List<PriceConfiguration> setPriceGridWithItsPriceCriteria(com.asi.service.product.client.vo.PriceGrid currentPriceGrid,
            ProductDetail productDetail) {
        List<PriceConfiguration> pricingConfigurations = new ArrayList<>();
        // PriceConfiguration currentPriceConfig = null;
        PricesParser pricesParser = new PricesParser();
        // Object firstCriteria = "";
        // Object secondCriteria = "";
        // String currentCriteria = "";
        // CriteriaInfo criteriaInfo = null;
        // UpChargePriceDetails upchargePriceDetail = new
        // UpChargePriceDetails();
        // boolean checkNeeded = false;
        // boolean isCriteriaString=false;
        // / boolean setCurrency = true;
        if (currentPriceGrid != null) {
            // if (currentPriceGrid.getIsBasePrice()) {
            pricingConfigurations = pricesParser.getPricingConfigurations(productDetail.getExternalProductId(),
                    currentPriceGrid.getPricingItems());
            /*
             * } else { upchargePriceDetail =
             * pricesParser.getUpChargePriceDetails
             * (productDetail.getExternalProductId(), currentPriceGrid,
             * upChargeLookup); if (upchargePriceDetail.getUpChargeCriteria1()
             * != null && !upchargePriceDetail.getUpChargeCriteria1().isEmpty())
             * { currentPriceConfig = new PriceConfiguration(); criteriaInfo =
             * ProductDataStore
             * .getCriteriaInfoForCriteriaCode(getCriteriaCode(upchargePriceDetail
             * .getUpChargeCriteria1()));
             * currentPriceConfig.setCriteria(criteriaInfo.getDescription());
             * currentPriceConfig
             * .setValue(getCriteriaValueByCriteria(upchargePriceDetail
             * .getUpChargeCriteria1(),criteriaInfo.getCode()));
             * if(!isCriteriaString
             * )pricingConfigurations.add(currentPriceConfig); } if
             * (upchargePriceDetail.getUpChargeCriteria2() != null &&
             * !upchargePriceDetail.getUpChargeCriteria2().isEmpty()) {
             * currentPriceConfig = new PriceConfiguration(); criteriaInfo =
             * ProductDataStore
             * .getCriteriaInfoForCriteriaCode(getCriteriaCode(upchargePriceDetail
             * .getUpChargeCriteria2()));
             * currentPriceConfig.setCriteria(criteriaInfo.getDescription());
             * currentPriceConfig
             * .setValue(getCriteriaValueByCriteria(upchargePriceDetail
             * .getUpChargeCriteria2(),criteriaInfo.getCode()));
             * if(!isCriteriaString
             * )pricingConfigurations.add(currentPriceConfig); } }
             */}
        if (pricingConfigurations.size() > 0)
            return pricingConfigurations;
        else
            return null;
    }

    public Object getCriteriaCode(Object source) {
        List<Value> tempList = null;
        if (source != null && source instanceof String && !source.toString().isEmpty() && source.toString().contains(":")) {
            return source.toString().substring(0, source.toString().indexOf(":"));
        } else if (source != null && source instanceof Values) {
            return ((Values) source).getValue().get(0).getCriteriaType();
        } else if (source != null && source instanceof List<?>) {
            tempList = (List<Value>) source;
            if (tempList.size() > 0)
                return (String) tempList.get(0).getCriteriaType();
            else
                return null;
        } else {
            return source;
        }
    }

    public Object getCriteriaValueByCriteria(String source) {
        String criteriaValue = "";
        String[] valueElements = {};
        if (source != null && !source.isEmpty() && source.contains(":")) {
            criteriaValue = source.substring(source.indexOf(":") + 1);
            if (!criteriaValue.contains(":"))
                return criteriaValue;
            else {
                valueElements = criteriaValue.split(":");
                Value valueObj = new Value();
                if (valueElements.length == 2) {
                    valueObj.setValue(valueElements[0]);
                    valueObj.setUnit(valueElements[1]);
                } else {
                    valueObj.setAttribute(valueElements[0]);
                    valueObj.setValue(valueElements[1]);
                    valueObj.setUnit(valueElements[2]);
                }
                return valueObj;
            }
        }
        return null;
    }
}
