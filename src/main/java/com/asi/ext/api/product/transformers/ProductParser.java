package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetRelationships;
import com.asi.ext.api.radar.model.CriteriaSetValuePaths;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Currency;
import com.asi.ext.api.radar.model.DiscountRate;
import com.asi.ext.api.radar.model.PriceGrids;
import com.asi.ext.api.radar.model.Prices;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.radar.model.ProductKeywords;
import com.asi.ext.api.radar.model.Relationships;
import com.asi.ext.api.radar.model.SelectedProductCategories;
import com.asi.ext.api.radar.model.SelectedSafetyWarnings;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.rest.JersyClientGet;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.PriceGridUtil;
import com.asi.ext.api.util.RestAPIProperties;

/**
 * Product parser is one of the important Class which contains the processing logic for all lookup
 * operations and Criteria creation and organization of criteria sets
 * 
 * @author Shravan, Murali Ede, Rahul K
 * @version 1.5
 * 
 */
public class ProductParser {

    private final static Logger              LOGGER                      = Logger.getLogger(ProductParser.class.getName());

    private String                                                              originWSResponse            = null;
    private String                                                              colorsWSResponse            = null;
    private String                                                              materialWSResponse          = null;
    private String                                                              currenciesWSResponse        = null;
    private String                                                              discountRatesWSResponse     = null;
    private String                                                              shapesWSResponse            = null;
    protected JsonProcessor                                                     jsonProcessorObj            = new JsonProcessor();
    private String                                                              themesWSResponse            = null;
    protected static int                                                        criteriaSetValuesID         = -2;
    // private static int currentCriteriaSetValueID=-2;
    // private ProductCriteriaSets[] prodctCriteriaSets = null;
    protected JerseyClientPost                                                  orgnCall                    = new JerseyClientPost();
    private JersyClientGet                                                      jerseyClient                = new JersyClientGet();
    private Map<String, CriteriaSetValues[]>                                    listOfSetCodeValues         = null;
    private String                                                              sizesWSResponse             = null;
    private String                                                              optionsProdWSResponse       = null;
    @SuppressWarnings("rawtypes")
    private HashMap                                                             sizeElementsResponse        = null;
    private String                                                              imprintWSResponse           = null;
    private String                                                              artworkWSResponse           = null;
    // private String imprintArtWrkWSResponse = null;
    private String                                                              safetyWarningWSResponse     = null;
    private String                                                              mediaCitationsWSResponse    = null;
    private String                                                              tradeNamesWSResponse        = null;
    private String                                                              packagingWSResponse         = null;
    private String                                                              sizesCriteriaWSResponse     = null;
    private CommonUtilities                                                     commonUtils                 = new CommonUtilities();
    private String                                                              optionsShipWSResponse       = null;
    private String                                                              optionsImprintWSResponse    = null;
    private String                                                              sizesShippingDimsWSResponse = null;
    private String                                                              additionalColorWSResponse   = null;
    private String                                                              shippingItemsWSResponse     = null;
    private String                                                              rushTimeWSResponse          = null;
    private String                                                              imprintSizeWSResponse       = null;
    private String                                                              imprintColorWSResponse      = null;
    
    private ProductCompareUtil                                                  productCompareUtil          = new ProductCompareUtil();
    
    public ProductDataStore                                                     productDataStore            = new ProductDataStore();

    protected int                                                               criteriaSetUniqueId         = -500;
    protected static int                                                        uniqPriceGrid               = -1;

    public ProductParser() {
    }
    
    
    /**
     * Removes unwanted elements or null elements from a product criteria set and
     * returns a organized/re-indexed array of product criteria sets.
     * 
     * @param productCriteriaSetsAry
     *            is the array need to be arranged and corrected
     * @param criteriaCode
     *            is the criteria code to check
     * @return organized and corrected product criteria set
     */
    public ProductCriteriaSets[] getCriteriaSetsBasedOnCriteriaCode(ProductCriteriaSets productCriteriaSetsAry[],
            String criteriaCode) {
        ProductCriteriaSets crntProductCriteriaSet = null;
        ArrayList<ProductCriteriaSets> productCriteriaSetsAl = new ArrayList<ProductCriteriaSets>();
        // ProductCriteriaSets[] finalProductCriteriaSets=new ProductCriteriaSets[]{};
        for (int i = 0; i < productCriteriaSetsAry.length; i++) {
            crntProductCriteriaSet = productCriteriaSetsAry[i];
            if (null != crntProductCriteriaSet && crntProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode.trim())) {
                productCriteriaSetsAl.add(crntProductCriteriaSet);
                // finalProductCriteriaSets[j]=new ProductCriteriaSets();
                // finalProductCriteriaSets[j]=crntProductCriteriaSet;
                // j++;
            }

        }
        return productCriteriaSetsAl.toArray(new ProductCriteriaSets[0]);
    }

    /**
     * Updates product media citation details using lookup service
     * 
     * @param mediaCitationId
     *            is the media citation
     * @param companyId
     *            is the Company where the product belongs
     * @return string value of media citation details
     * @throws VelocityException
     */
    public String updateProductMediaCitationDetails(String mediaCitationId, String companyId, boolean callAPI)
            throws VelocityException {
        String mediaCitationDetails = ""; // citationId:citationreferenceId
        // This lookup API needs to called all the time
        if (callAPI || mediaCitationsWSResponse == null) {
            mediaCitationsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                    .get(ApplicationConstants.MEDIA_CITATIONS_LOOKUP) + companyId);
        }
        /*
         * if (null == mediaCitationsWSResponse) {
         * mediaCitationsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
         * .get(ApplicationConstants.MEDIA_CITATIONS_LOOKUP) + companyId);
         * }
         */
        mediaCitationDetails = jsonProcessorObj.checkMediaCitationId(mediaCitationsWSResponse, mediaCitationId);
        return mediaCitationDetails;
    }

    /**
     * Process Safety Information that applies to a product,
     * using lookup service get required IDs and other required data for a Criteria set
     * 
     * @param product
     *            is the object we need to store the processed safety criteria also
     *            product contains required elements which we need to process
     * @param safteyWarningStr
     *            is safety warning criteria JSON
     * @return {@linkplain Product} with the safety warnings criteria appended
     * @throws VelocityException
     */
    public Product updateSafteyWarnings(Product product, String safteyWarningStr) throws VelocityException {
        safteyWarningStr = CommonUtilities.checkAndFixCSVValues(safteyWarningStr);
        
        if (safteyWarningStr.contains(",")) {

            String[] safteyWrngsAry = safteyWarningStr.split(",");
            List<SelectedSafetyWarnings> safetyWarnings = new ArrayList<SelectedSafetyWarnings>();
            for (int safteyCntr = 0; safteyCntr < safteyWrngsAry.length; safteyCntr++) {
                SelectedSafetyWarnings safetyWarning = new SelectedSafetyWarnings();
                safteyWarningStr = safteyWrngsAry[safteyCntr];
                if (null == safetyWarningWSResponse) {
                    safetyWarningWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.SAFETY_WARNINGS_LOOKUP));
                }
                safteyWarningStr = jsonProcessorObj.checkValueKeyPair(safetyWarningWSResponse, safteyWarningStr,
                        ApplicationConstants.CONST_STRING_NO_CAP);
                if (!CommonUtilities.isValueNull(safteyWarningStr)) {
                    safetyWarning.setCode(safteyWarningStr);
                    safetyWarning.setProductId(product.getId());
                    safetyWarning.setMarketSegmentCode(product.getSelectedSafetyWarnings()[0].getMarketSegmentCode());
                    safetyWarnings.add(safetyWarning);
                } else {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Safety warning "
                                    + safteyWrngsAry[safteyCntr]);
                }
            }
            if (safetyWarnings != null && !safetyWarnings.isEmpty()) {
                product.setSelectedSafetyWarnings(safetyWarnings.toArray(new SelectedSafetyWarnings[0]));
            } else {
                product.setSelectedSafetyWarnings(new SelectedSafetyWarnings[0]);
            }
        } else {

            if (null == safetyWarningWSResponse) {
                safetyWarningWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                        .get(ApplicationConstants.SAFETY_WARNINGS_LOOKUP));
            }
            String temp = safteyWarningStr;
            safteyWarningStr = jsonProcessorObj.checkValueKeyPair(safetyWarningWSResponse, safteyWarningStr,
                    ApplicationConstants.CONST_STRING_NO_CAP);
            if (null != safteyWarningStr && !safteyWarningStr.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                product.getSelectedSafetyWarnings()[0].setCode(safteyWarningStr);
            } else {
                product.setSelectedSafetyWarnings(new SelectedSafetyWarnings[] {});
                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Safety warning " + temp);
            }
        }
        return product;
    }

    public String getUpchargeCriteriaSetCode(ProductCriteriaSets[] productCriteriaSets, String criteriaCode, String lookupValue) {

        if (criteriaCode != null && !criteriaCode.isEmpty() && lookupValue != null && !lookupValue.isEmpty()) {
            return getCriteriaSetCodeValueId(productCriteriaSets, criteriaCode, lookupValue);
        } else {
            return criteriaCode;
        }
    }

    /**
     * Finds the CriteriaCodeValue for a give criteriaCode using lookup
     * 
     * @param productCriteriaSetsAry
     *            is the current product criteria sets
     * @param criteriaCode
     *            is the criteria code of CriteriaSetCodeValue which we need to find
     * @param lookupValue
     * @return CriteriSetCodeValue
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String getCriteriaSetCodeValueId(ProductCriteriaSets productCriteriaSetsAry[], String criteriaCode, String lookupValue) {
        String criteriaSetCodeValueId = null;
        String crntLookupValue = null;
        ProductCriteriaSets tempProductCriteriaSets = null;
        if (null != productCriteriaSetsAry) {
            for (int criteriaSetsCntr = 0; criteriaSetsCntr < productCriteriaSetsAry.length; criteriaSetsCntr++) {
                tempProductCriteriaSets = productCriteriaSetsAry[criteriaSetsCntr];

                if (null != tempProductCriteriaSets && tempProductCriteriaSets.getCriteriaCode().equalsIgnoreCase(criteriaCode)) {
                    for (int j = 0; j < tempProductCriteriaSets.getCriteriaSetValues().length; j++) {
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) {
                            Value[] value = new Value[1];
                            if (tempProductCriteriaSets.getCriteriaSetValues()[j].getValue() instanceof ArrayList) {
                                ArrayList temp = new ArrayList<>();
                                temp = (ArrayList) tempProductCriteriaSets.getCriteriaSetValues()[j].getValue();
                                LinkedHashMap<String, String> tempMap = (LinkedHashMap<String, String>) temp.get(0);
                                Value v = new Value();
                                v.setCriteriaAttributeId(String.valueOf(tempMap.get("CriteriaAttributeId")));
                                v.setUnitOfMeasureCode(String.valueOf(tempMap.get("UnitOfMeasureCode")));
                                v.setUnitValue(String.valueOf(tempMap.get("UnitValue")));
                                value[0] = v;
                            } else {
                                value = (Value[]) tempProductCriteriaSets.getCriteriaSetValues()[j].getValue();
                            }
                            String[] values = lookupValue.split("\\$");
                            try {
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                        ApplicationConstants.CONST_STRING_CAPACITY, ApplicationConstants.CONST_SIZE_GROUP_CAPACITY);
                                values[1] = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, values[1].trim());
                                if (value[0].getUnitOfMeasureCode().equalsIgnoreCase(values[1])
                                        && value[0].getUnitValue().equalsIgnoreCase(values[0])) {
                                    criteriaSetCodeValueId = tempProductCriteriaSets.getCriteriaSetValues()[j]
                                            .getCriteriaSetCodeValues()[0].getCriteriaSetValueId();
                                    break;
                                }
                            } catch (ArrayIndexOutOfBoundsException aioe) {
                            }
                        } else {
                            crntLookupValue = tempProductCriteriaSets.getCriteriaSetValues()[j].getValue().toString();
                        }
                        if (null != crntLookupValue && crntLookupValue.equals(""))
                            crntLookupValue = tempProductCriteriaSets.getCriteriaSetValues()[j].getBaseLookupValue();
                        if (null != crntLookupValue && crntLookupValue.toUpperCase().contains(lookupValue.toUpperCase().trim())) {
                            criteriaSetCodeValueId = tempProductCriteriaSets.getCriteriaSetValues()[j].getCriteriaSetCodeValues()[0]
                                    .getCriteriaSetValueId();
                            break;
                        }
                    }
                    if (null != criteriaSetCodeValueId) break;
                }
            }
        }
        return criteriaSetCodeValueId;
    }

    /**
     * Creates PriceGrid data for the given price type
     * 
     * @param existingProduct
     *            is existing product
     * @param product
     *            is the current product
     * @param price
     *            is the price
     * @param quantity
     *            is the quantity
     * @param discount
     *            is the allowed discount for the product
     * @param currencyValue
     *            is value of the given currency
     * @param isQurFlag
     *            is the QUR flag
     * @param description
     *            is the description of the price type
     * @return PriceGrid of the give price type with updated value
     * @throws VelocityException
     */
    public PriceGrids getPriceGridsByPriceType(Product existingProduct, Product product, String price, String quantity,
            String discount, String currencyValue, String isQurFlag, String description, boolean isBasePrice, int priceGridNumber)
            throws VelocityException {
        PriceGrids crntPriceGrids = null;

        if (null != price && null != quantity && null != discount) {
            String[] pricesAry = price.split("\\$");
            String[] quatityAry = quantity.split("\\$");
            String[] discountAry = discount.split("\\$");

            Currency currency = null;
            List<Prices> pricesList = new ArrayList<Prices>();

            String crntDiscountValue = "";
            String[] crntDiscountElements = new String[4];

            if (null != pricesAry[0] && !CommonUtilities.isValueNull(pricesAry[0]) && pricesAry.length <= 11) {
                // If quantities contains price unit modifier other than Piece Price then we need to relax the rule
                if (!PriceGridUtil.relaxPriceValidation(quatityAry)) {  
                if (!PriceGridParser.isPricesInDecreasingOrder(pricesAry)) {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, PriceGridUtil.getPriceGridErrorMessage(
                                        ApplicationConstants.CONST_ERROR_MSG_PRICE_INCREASE_ORDER, isBasePrice, priceGridNumber));
                }

                if (!PriceGridParser.isQuantityInIncreasingOrder(quatityAry)) {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, PriceGridUtil.getPriceGridErrorMessage(
                                        ApplicationConstants.CONST_ERROR_MSG_QUANTITY_DECREASE_ORDER, isBasePrice, priceGridNumber));
                    }
                }
                // priceGridsLst = new PriceGrids[1];
                crntPriceGrids = new PriceGrids();
                uniqPriceGrid--;
                crntPriceGrids.setId(uniqPriceGrid + "");
                crntPriceGrids.setProductId(product.getPriceGrids()[0].getProductId());

                // crntPriceGrids.setIsQUR(isQur);
                if (isBasePrice) {
                    crntPriceGrids.setPriceGridSubTypeCode(ApplicationConstants.CONST_BASE_PRICE_GRID_CODE);
                } else {
                    crntPriceGrids.setPriceGridSubTypeCode(ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE);
                }
                crntPriceGrids.setUsageLevelCode(ApplicationConstants.CONST_STRING_NONE_CAP);
                
                crntPriceGrids.setComment(product.getPriceGrids()[0].getComment());
                crntPriceGrids.setIsRange(product.getPriceGrids()[0].getIsRange());
                crntPriceGrids.setIsSpecial(product.getPriceGrids()[0].getIsSpecial());
                crntPriceGrids.setPriceIncludes(product.getPriceGrids()[0].getPriceIncludes());
                crntPriceGrids.setIsPriceIncludesEnabled(product.getPriceGrids()[0].getIsPriceIncludesEnabled());
                crntPriceGrids.setDescription(description);
                crntPriceGrids.setIsCopy(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                currency = new Currency();
                if (null == currenciesWSResponse) {
                    currenciesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.CURRENCIES_LOOKUP_URL));
                }
                currency = jsonProcessorObj.checkCurrencyValueKeyPair(product.getExternalProductId().trim(), currenciesWSResponse,
                        currencyValue);

                crntPriceGrids.setCurrency(currency);
                crntPriceGrids.setDisplaySequence(product.getPriceGrids()[0].getDisplaySequence()); // TODO : Correct this
                crntPriceGrids.setApplyOneCode(product.getPriceGrids()[0].getApplyOneCode());
                crntPriceGrids.setIsApplyOneCodeEnabled(product.getPriceGrids()[0].getIsApplyOneCodeEnabled());
                crntPriceGrids.setErrorMessage(product.getPriceGrids()[0].getErrorMessage());
                crntPriceGrids.setIsUpArrowEnabled(product.getPriceGrids()[0].getIsUpArrowEnabled());
                crntPriceGrids.setIsDownArrowEnabled(product.getPriceGrids()[0].getIsDownArrowEnabled());
                crntPriceGrids.setInputMode(product.getPriceGrids()[0].getInputMode());
                crntPriceGrids.setTotalVariations(product.getPriceGrids()[0].getTotalVariations());
                crntPriceGrids.setConfigDesc(product.getPriceGrids()[0].getConfigDesc());
                crntPriceGrids.setDisplayProductNumber(product.getPriceGrids()[0].getDisplayProductNumber());
                crntPriceGrids.setAllowDeletion(product.getPriceGrids()[0].getAllowDeletion());
                crntPriceGrids.setStandalone(product.getPriceGrids()[0].getStandalone());
                crntPriceGrids.setIsNullo(product.getPriceGrids()[0].getIsNullo());
                crntPriceGrids.setIsDirty(product.getPriceGrids()[0].getIsDirty());
                int sequenceNum = 0;

                String pricingErrors = "";
                String discountCodeErrors = "";
                for (int pricescntr = 0; pricescntr < pricesAry.length; pricescntr++) {
                    // Now we not log if both values are null
                    if (!PriceGridParser.isValidPriceSet(pricesAry[pricescntr], quatityAry[pricescntr])) {
                        pricingErrors += ", P" + String.valueOf(pricescntr + 1);
                        continue;
                    }
                    Prices crntPrices = null;
                    String crntQuantity = quatityAry[pricescntr];
                    String crntPrice = pricesAry[pricescntr];

                    String priceUnit = null;

                    String itemsPerUnit = null;
                    if (!CommonUtilities.isValueNull(crntQuantity) && crntQuantity.contains(":")) { // Contains Price Unit
                        String[] quantityElements = PriceGridUtil.getQuantityElements(crntQuantity);
                        if (quantityElements != null && quantityElements.length > 0) {
                            crntQuantity = quantityElements[0];
                        }
                        if (quantityElements != null && quantityElements.length > 1) {
                            priceUnit = quantityElements[1];
                        }
                        if (quantityElements != null && quantityElements.length > 2) {
                            itemsPerUnit = quantityElements[2];
                        }
                    } else {
                        priceUnit = ApplicationConstants.CONST_STRING_PIECE;
                    }

                    if (CommonUtilities.isValueNull(itemsPerUnit) || itemsPerUnit.equalsIgnoreCase("0")) {
                        itemsPerUnit = "1";
                    }
                    if (!CommonUtilities.isValueNull(crntQuantity) && !CommonUtilities.isInteger(crntQuantity, true)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, PriceGridUtil.getPriceGridErrorMessage(
                                "A quantity value must be an integer greater than or equal to one, given value is "
                                                + crntQuantity, isBasePrice, priceGridNumber));
                        crntQuantity = "";
                    }
                    if (!CommonUtilities.isValueNull(crntPrice) && !CommonUtilities.isValidNumber(crntPrice)) {
                        productDataStore.addErrorToBatchLogCollection(
                                product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                PriceGridUtil.getPriceGridErrorMessage("Invalid value found for list price, given value is "
                                        + crntPrice, isBasePrice, priceGridNumber));
                        crntPrice = "";
                    }

                    if (CommonUtilities.isValueNull(crntQuantity) || CommonUtilities.isValueNull(crntPrice)) {
                        continue;
                    } else {
                        crntPrices = new Prices();
                        crntPrices.setPriceGridId(crntPriceGrids.getId());

                        crntPrices.setQuantity(crntQuantity);
                        crntPrices.setListPrice(crntPrice);
                        crntPrices.setItemsPerUnit(itemsPerUnit);
                        crntPrices.setPriceUnitName(priceUnit);

                        DiscountRate discountRate = new DiscountRate();
                        if (CommonUtilities.isValueNull(discountAry[pricescntr]) && isBasePrice) {
                            discountCodeErrors += ", P" + String.valueOf(pricescntr + 1);
                            // addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            // ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                            // "Discount code required for pricing P"+String.valueOf(pricescntr + 1));
                            continue;
                        }
                        if (null == discountRatesWSResponse) {
                            discountRatesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.DISCOUNT_RATES_LOOKUP_URL));
                        }

                        crntDiscountValue = jsonProcessorObj.checkDiscountValueKeyPair(discountRatesWSResponse,
                                discountAry[pricescntr], product.getExternalProductId(), isBasePrice);
                        if (CommonUtilities.isValueNull(crntDiscountValue)) {
                            continue;
                        }
                        crntDiscountElements = crntDiscountValue.split("#");
                        for (int dscntCntr = 0; dscntCntr < crntDiscountElements.length; dscntCntr++) {
                            if (dscntCntr == 0) discountRate.setCode(crntDiscountElements[dscntCntr]);
                            if (dscntCntr == 1) discountRate.setDiscountPercent(crntDiscountElements[dscntCntr]);
                            if (dscntCntr == 2) discountRate.setIndustryDiscountCode(crntDiscountElements[dscntCntr]);
                            if (dscntCntr == 3) discountRate.setDisplayValue(crntDiscountElements[dscntCntr]);

                        }
                        crntPrices.setPriceUnit(ProductDataStore.getPriceUnit(priceUnit));
                        crntPrices.setDiscountRate(discountRate);
                        crntPrices.setSequenceNumber(++sequenceNum + "");

                        pricesList.add(crntPrices);
                    }
                }
                if (pricesList != null && !pricesList.isEmpty()) {
                    crntPriceGrids.setPrices(pricesList.toArray(new Prices[0]));
                    if (!CommonUtilities.isValueNull(pricingErrors)) {
                        pricingErrors = pricingErrors.trim().startsWith(ApplicationConstants.CONST_STRING_COMMA_SEP) ? pricingErrors
                                .trim().substring(1) : pricingErrors.trim();
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, PriceGridUtil.getPriceGridErrorMessage(
                                        "Invalid Pricing found for Pricing " + pricingErrors, isBasePrice, priceGridNumber));
                    }
                    if (!CommonUtilities.isValueNull(discountCodeErrors)) {
                        discountCodeErrors = discountCodeErrors.trim().startsWith(ApplicationConstants.CONST_STRING_COMMA_SEP) ? discountCodeErrors
                                .trim().substring(1) : discountCodeErrors.trim();
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD, PriceGridUtil.getPriceGridErrorMessage(
                                        "Discount code required for pricing " + discountCodeErrors, isBasePrice, priceGridNumber));
                    }
                } else {
                    if (!CommonUtilities.isValueNull(discountCodeErrors)) {
                        discountCodeErrors = discountCodeErrors.trim().startsWith(ApplicationConstants.CONST_STRING_COMMA_SEP) ? discountCodeErrors
                                .trim().substring(1) : discountCodeErrors.trim();
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD, PriceGridUtil.getPriceGridErrorMessage(
                                        "Discount code required for pricing " + discountCodeErrors, isBasePrice, priceGridNumber));
                    }
                    if (!isQurFlag.equalsIgnoreCase(ApplicationConstants.CONST_STRING_TRUE_SMALL)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, PriceGridUtil.getPriceGridErrorMessage(
                                        "PriceGrid discarded because all the prices are invalid", isBasePrice, priceGridNumber));
                        crntPriceGrids = null;
                    } else {
                        crntPriceGrids.setPrices(new Prices[] {});
                    }
                }

            } else if (null != isQurFlag && isQurFlag.equalsIgnoreCase(ApplicationConstants.CONST_STRING_TRUE_SMALL)) {
                crntPriceGrids = new PriceGrids();
                uniqPriceGrid--;
                crntPriceGrids.setId(uniqPriceGrid + "");
                crntPriceGrids.setProductId(product.getPriceGrids()[0].getProductId());

                // crntPriceGrids.setIsQUR(isQur);
                if (isBasePrice) {
                    crntPriceGrids.setPriceGridSubTypeCode(ApplicationConstants.CONST_BASE_PRICE_GRID_CODE);
                } else {
                    crntPriceGrids.setPriceGridSubTypeCode(ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE);
                }
                crntPriceGrids.setUsageLevelCode(ApplicationConstants.CONST_STRING_NONE_CAP);
                crntPriceGrids.setComment(product.getPriceGrids()[0].getComment());
                crntPriceGrids.setIsRange(product.getPriceGrids()[0].getIsRange());
                crntPriceGrids.setIsSpecial(product.getPriceGrids()[0].getIsSpecial());
                crntPriceGrids.setIsPriceIncludesEnabled(product.getPriceGrids()[0].getIsPriceIncludesEnabled());
                // crntPriceGrids.setDescription(product.getPriceGrids()[0]
                // .getDescription());
                crntPriceGrids.setIsCopy(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                currency = new Currency();
                if (null == currenciesWSResponse) {
                    currenciesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.CURRENCIES_LOOKUP_URL));
                }
                // currency = jsonProcessorObj.checkCurrencyValueKeyPair(currenciesWSResponse,
                // product.getPriceGrids()[0].getCurrency().getCode());
                currency = jsonProcessorObj.checkCurrencyValueKeyPair(product.getExternalProductId().trim(), currenciesWSResponse,
                        currencyValue);
                crntPriceGrids.setCurrency(currency);
                crntPriceGrids.setDisplaySequence(product.getPriceGrids()[0].getDisplaySequence());
                crntPriceGrids.setApplyOneCode(product.getPriceGrids()[0].getApplyOneCode());
                crntPriceGrids.setIsApplyOneCodeEnabled(product.getPriceGrids()[0].getIsApplyOneCodeEnabled());
                crntPriceGrids.setErrorMessage(product.getPriceGrids()[0].getErrorMessage());
                crntPriceGrids.setIsUpArrowEnabled(product.getPriceGrids()[0].getIsUpArrowEnabled());
                crntPriceGrids.setIsDownArrowEnabled(product.getPriceGrids()[0].getIsDownArrowEnabled());
                crntPriceGrids.setInputMode(product.getPriceGrids()[0].getInputMode());
                crntPriceGrids.setTotalVariations(product.getPriceGrids()[0].getTotalVariations());
                crntPriceGrids.setConfigDesc(product.getPriceGrids()[0].getConfigDesc());
                crntPriceGrids.setDisplayProductNumber(product.getPriceGrids()[0].getDisplayProductNumber());
                crntPriceGrids.setAllowDeletion(product.getPriceGrids()[0].getAllowDeletion());
                crntPriceGrids.setStandalone(product.getPriceGrids()[0].getStandalone());
                crntPriceGrids.setIsNullo(product.getPriceGrids()[0].getIsNullo());
                crntPriceGrids.setIsDirty(product.getPriceGrids()[0].getIsDirty());
                crntPriceGrids.setPrices(new Prices[] {});
            }
        }

        return crntPriceGrids;
    }

    /**
     * Add imprint criteria set to the product criteria set.
     * Also contains the logic for processing imprint criteria string to a {@linkplain ProductCriteriaSets},
     * processing of imprint criteria includes lookup service, splitting, grouping, aggregations
     * 
     * @param existingProduct
     *            is the exiting product
     * @param product
     *            is the new Product object
     * @param imprintMethods
     *            is the imprint method values in string JSON format
     * @param productCriteriaSetsAry2
     *            is the temporary criteria array used for manipulating imprint methods.
     * @param criteriaCode
     *            criteria code of imprint methods
     * @return {@link ProductCriteriaSets} which contains imprint criteria sets or the existing criteria set if no match found in
     *         Imprint methods
     * @throws VelocityException
     */
    public ProductCriteriaSets addCriteriaSetForImprint(Product existingProduct, Product product, String imprintMethods,
            ProductCriteriaSets[] productCriteriaSetsAry2, String criteriaCode) throws VelocityException {
        //ProductCriteriaSets[] crntProductCriteriaSets = productCriteriaSetsAry2;
        ProductCriteriaSets productCriteriaSet = null;

        // Value[] valueAry = null;

        // System.out.println("2. Existing setCode List:" + listOfSetCodeValues);
        if (imprintMethods.contains("|")) {
            imprintMethods = imprintMethods.replace("|", ",");
        }
        String[] imprintMethodsAry = imprintMethods.split(",");
        List<String> tempIMethdList = new ArrayList<String>();
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
            imprintMethodsAry = CommonUtilities.filterDuplicates(imprintMethodsAry);
        } else {
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
            imprintMethodsAry = CommonUtilities.filterDuplicates(imprintMethodsAry);
        } else {
            for (int impMethdCntr = 0; impMethdCntr < imprintMethodsAry.length; impMethdCntr++) {
                if (imprintMethodsAry[impMethdCntr] != null && !imprintMethodsAry[impMethdCntr].trim().equals("")
                        && !imprintMethodsAry[impMethdCntr].trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                    tempIMethdList.add(imprintMethodsAry[impMethdCntr]);
                }
            }
            imprintMethodsAry = tempIMethdList.toArray(new String[0]);
        }
        }

        criteriaSetUniqueId--;
        String criteriaSetId = String.valueOf(criteriaSetUniqueId);

        productCriteriaSet = new ProductCriteriaSets();
        productCriteriaSet.setCriteriaCode(criteriaCode);
        productCriteriaSet.setCriteriaSetId(criteriaSetId);
        productCriteriaSet.setProductId(product.getId());
        productCriteriaSet.setCompanyId(product.getCompanyId());
        
        productCriteriaSet.setConfigId(ApplicationConstants.CONST_STRING_ZERO);
        List<CriteriaSetValues> criteriaSetValueList = new ArrayList<CriteriaSetValues>();

        for (int impMethdCntr = 0; impMethdCntr < imprintMethodsAry.length; impMethdCntr++) {
            imprintMethods = imprintMethodsAry[impMethdCntr];
            if (CommonUtilities.isValueNull(imprintMethods)) {
                continue;
            }
            CriteriaSetValues criteriaSetValue = null;

            CriteriaSetCodeValues criteriaSetCodeValues = new CriteriaSetCodeValues();
            // If Product has any Size Groups

            CriteriaSetCodeValues[] criteriaSetCodeValuesAry = new CriteriaSetCodeValues[1];

            // If Product has only one Size Value
            // String[] sizeValueElements = imprintMethods.split(";");



            String actualImprntMethod = imprintMethods;
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_METHOD_CODE)) {

                if (null == imprintWSResponse) {
                    imprintWSResponse = orgnCall.getLookupsResponse(RestAPIProperties.get(ApplicationConstants.IMPRINT_LOOKUP_URL));
                }
                if (actualImprntMethod.contains("=")) {
                    imprintMethods = jsonProcessorObj.checkImprintValueKeyPair(imprintWSResponse,
                            imprintMethods.substring(0, imprintMethods.indexOf("=")));
                } else {
                    imprintMethods = jsonProcessorObj.checkImprintValueKeyPair(imprintWSResponse, actualImprntMethod);
                }

                if (imprintMethods.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP))
                    imprintMethods = jsonProcessorObj.checkImprintValueKeyPair(imprintWSResponse, "Other");
                criteriaSetCodeValues.setSetCodeValueId(imprintMethods);
            } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
                if (null == artworkWSResponse) {
                    artworkWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.IMPRINT_ARTWORK_LOOKUP_URL));
                }
                String artworkSetCodeValueId = "null";
                artworkSetCodeValueId = jsonProcessorObj.checkImprintArtWorkValueKeyPair(artworkWSResponse, imprintMethods,
                        criteriaCode);
                if (!commonUtils.isThatValidNumber(artworkSetCodeValueId)) {
                    artworkSetCodeValueId = jsonProcessorObj.checkImprintArtWorkValueKeyPair(artworkWSResponse, "Other",
                            criteriaCode);
                    criteriaSetCodeValues.setCodeValue(imprintMethods);
                }
                criteriaSetCodeValues.setSetCodeValueId(artworkSetCodeValueId);
                if (imprintMethods != null && imprintMethods.trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_OTHER)) {
                    criteriaSetCodeValues.setCodeValue(imprintMethods);
                }
                /*
                 * criteriaSetCodeValues.setSetCodeValueId(jsonProcessorObj.checkImprintArtWorkValueKeyPair(artworkWSResponse,
                 * imprintMethods, criteriaCode));
                 */
            } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY)) {
                if (null == artworkWSResponse) {
                    artworkWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.IMPRINT_ARTWORK_LOOKUP_URL));
                }// Minimum Order
                criteriaSetCodeValues.setSetCodeValueId(jsonProcessorObj.checkImprintArtWorkValueKeyPair(artworkWSResponse,
                        "Other", criteriaCode));

            }
            criteriaSetValue = new CriteriaSetValues();
            criteriaSetValue.setCriteriaSetId(criteriaSetId);

            criteriaSetValue.setId(ApplicationConstants.CONST_STRING_ZERO);
            criteriaSetValue.setCriteriaCode(criteriaCode);
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_METHOD_CODE)) {
                if (actualImprntMethod.contains("=")) {
                    criteriaSetValue.setValue(actualImprntMethod.substring(actualImprntMethod.indexOf("=") + 1,
                            actualImprntMethod.length()));
                } else {
                    criteriaSetValue.setValue(actualImprntMethod);
                }
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
            } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                criteriaSetValue.setValue(actualImprntMethod);
            } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY)) {
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);

                String[] unitValues = imprintMethods.split(":");
                Value value = new Value();
                if (unitValues.length == 2) {
                    String sizeUnit = unitValues[1];

                    if (null == sizesCriteriaWSResponse) {
                        sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                    }
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY))
                        sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, "Unit",
                                ApplicationConstants.CONST_MINIMUM_QUANTITY);

                    value.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, "Minimum Order"));
                    value.setUnitValue(unitValues[0]);
                    value.setUnitOfMeasureCode(jsonProcessorObj.getSizesElementValue("UNITS", sizeElementsResponse, sizeUnit.trim()));

                }

                criteriaSetValue.setValue(new Value[] { value });
            }
            criteriaSetValue.setCriteriaValueDetail("");
            criteriaSetCodeValues.setId(ApplicationConstants.CONST_STRING_ZERO);
            criteriaSetValuesID = criteriaSetValuesID - 1;
            criteriaSetCodeValues.setCriteriaSetValueId(criteriaSetValuesID + "");
            // criteriaSetCodeValues.setCodeValue("");
            // criteriaSetValue.setCriteriaSetId(ApplicationConstants.CONST_STRING_ZERO);
            criteriaSetCodeValuesAry[0] = criteriaSetCodeValues;
            criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValuesAry);
            criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            // criteriaSetValue.setValue(valueAry);
            criteriaSetValue.setId(criteriaSetValuesID + "");
            
            if (criteriaSetValue != null && criteriaSetValue.getCriteriaSetCodeValues() != null
                    && criteriaSetValue.getCriteriaSetCodeValues().length > 0
                    && commonUtils.isThatValidNumber(criteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId())) {

                criteriaSetValueList.add(criteriaSetValue);

                productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), criteriaCode,
                        processSourceCriteriaValueByCriteriaCode(imprintMethodsAry[impMethdCntr], criteriaCode),
                        criteriaSetValue.getId());
                // DO NOT DELETE : This line of code is added for ARTW reference in Relationship
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
                    productDataStore.updateCriteriaSetValueReferenceTable(
                            product.getExternalProductId().trim(),
                            criteriaCode,
                            processSourceCriteriaValueByCriteriaCode(imprintMethodsAry[impMethdCntr] + "__" + impMethdCntr,
                                    criteriaCode), criteriaSetValue.getId());
                }
            }

        } // End of product sizes if condition
        
        if (criteriaSetValueList != null && !criteriaSetValueList.isEmpty()) {
            productCriteriaSet.setCriteriaSetValues(criteriaSetValueList.toArray(new CriteriaSetValues[0]));
        } else {
            productCriteriaSet = null;
        }
            
        return productCriteriaSet;
    }

    /**
     * Contains logic for processing sizes of a product, processing of size includes determining the size group.
     * Size groups includes
     * <ul>
     * <li>Apparel - Hosiery/Uniform Sizes
     * <li>Apparel - Infant & Toddler
     * <li>Apparel - Dress Shirt Sizes
     * <li>Apparel - Pants
     * <li>Apparel - Bra Sizes
     * <li>Dimension
     * <li>Capacity
     * <li>Volume/Weight
     * <li>Standard & Numbered
     * <li>Other
     * </ul>
     * Based on these group we finds appropriate criteriaSetCodeValues and process the given size value and append to the existing
     * criteria set
     * 
     * @param productSizes
     *            is the sizes of product and it must be in a valid format
     * @param sourceProduct
     *            is the source product
     * @param product
     *            is the current product
     * @param sizeGroups
     *            is the size group of the give product
     * @param existingProductCriteriaSets
     *            is the existing product criteria set
     * @return {@link ProductCriteriaSets} array which includes size details
     * @throws VelocityException
     */
    public ProductCriteriaSets addCriteriaSetForSizes(String productSizes, Product sourceProduct, Product product,
            String sizeGroups, ProductCriteriaSets[] existingProductCriteriaSets) throws VelocityException {

        //ProductCriteriaSets[] crntProductCriteriaSets = existingProductCriteriaSets;
        ProductCriteriaSets productCriteriaSet = null;
        CriteriaSetValues[] criteriaSetValuesAry = null;
        // CriteriaSetValues criteriaSetValue = null;
        // CriteriaSetCodeValues[] criteriaSetCodeValuesAry = null;
        // CriteriaSetCodeValues criteriaSetCodeValues = null;
        boolean validCriteriaSet = true;
        Value[] valueAry = null;
        // boolean isExistingCriteriaSet = false;
        Value value = null;
        // String initSizeGroup = sizeGroups;
        if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_SHIPPING_WEIGHT))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_SHIPPING_DIMENSION))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_DIMENSION))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_DIMENSION;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_VOLUME_WEIGHT))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_CAPACITY))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_CAPACITY;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_INFANT_TODDLER))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_DRESS_SHIRT_SIZES))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_PANTS_SIZES))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM)) {
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM;
            productSizes = productSizes.toUpperCase();
        } else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_BRA_SIZES))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA;
        else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD_NUMBERED))
            sizeGroups = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM;
        else
            sizeGroups = ApplicationConstants.CONST_SIZE_OTHER_CODE;
        LOGGER.info("1. Existing setCode List:" + listOfSetCodeValues);

        if (null != productSizes && !productSizes.trim().equals("")) {
            // If Product has any Size Groups

           // int size = crntProductCriteriaSets.length;

            // length:9:in;width:9:in;height:9:in,length:23:cm,arc:23:in
            String[] individualSizes = productSizes.split(","); // Checking CSV
            String attribute = null;
            String sizeValue = null;
            String units = null;
            // criteriaSetCodeValuesAry = new CriteriaSetCodeValues[1];
            criteriaSetUniqueId--;
            LOGGER.info("Individual Size (All Sizes):" + individualSizes);
            String criteriaSetId = product.getProductConfigurations()[0].getProductCriteriaSets()[0].getCriteriaSetId();
            if (criteriaSetId == null || criteriaSetId.trim().isEmpty() || criteriaSetId.equalsIgnoreCase("0")
                    || criteriaSetId.trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                criteriaSetId = String.valueOf(criteriaSetUniqueId);
            }
            // if() check for updations

            String initialUnits = "";
            String tempValueElement = null;
            int valueCntr = 0;
            productCriteriaSet = new ProductCriteriaSets();
            productCriteriaSet.setCriteriaCode(sizeGroups);
            productCriteriaSet.setCriteriaSetId(criteriaSetId);
            productCriteriaSet.setProductId(product.getId());
            productCriteriaSet.setConfigId(ApplicationConstants.CONST_STRING_ZERO);

            if (individualSizes.length == 1) {
                if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                        && !CommonUtilities.isValidDimension(productSizes)) {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid format/value for Dimension ");
                    productSizes = "";
                    validCriteriaSet = false;
                } else if ((sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI) || sizeGroups
                        .equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY))
                        && !CommonUtilities.isValidCapacity(productSizes.trim())) {
                    String temp = sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY) ? "Capacity"
                            : "Volume/Weight";
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid format/value for " + temp);
                    productSizes = "";
                    validCriteriaSet = false;
                }
                if (!productSizes.isEmpty()) {
                    criteriaSetValuesAry = new CriteriaSetValues[1];
                    // If Product has only one Size Value
                    String[] sizeValueElements = productSizes.split(";");
                    valueAry = new Value[sizeValueElements.length];
                    for (int valueElementsCntr = 0; valueElementsCntr < sizeValueElements.length; valueElementsCntr++) {
                        // currentCriteriaSetValueID=criteriaSetValuesID;
                        // criteriaSetValuesID=criteriaSetValuesID-1;
                        tempValueElement = sizeValueElements[valueElementsCntr];
                        tempValueElement = tempValueElement.trim();
                        if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {
                            if (valueElementsCntr == 0)
                                tempValueElement = "Length:" + tempValueElement;
                            else if (valueElementsCntr == 1)
                                tempValueElement = "Width:" + tempValueElement;
                            else if (valueElementsCntr == 2) tempValueElement = "Height:" + tempValueElement;

                        }
                        // For Single Size Element(attribute:value:units) it will iterate once
                        if (tempValueElement.contains(":")) {
                            String[] valueElements = tempValueElement.split(":");

                            for (int sizeElemntCntr = 0; sizeElemntCntr < valueElements.length; sizeElemntCntr++) {
                                if (sizeElemntCntr == 0) {
                                    attribute = valueElements[sizeElemntCntr];
                                    attribute = attribute.trim();
                                } else if (sizeElemntCntr == 1) {
                                    sizeValue = valueElements[sizeElemntCntr];
                                } else if (sizeElemntCntr == 2) {
                                    units = valueElements[sizeElemntCntr];
                                    units = units.trim();
                                }
                            }
                        } // End of : tokens
                        LOGGER.info("Attribute:" + attribute + " Size:" + sizeValue + " Units:" + units);
                        initialUnits = units;
                        if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) initialUnits = sizeValue;
                        if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)) {
                            initialUnits = sizeValue;
                            sizeValue = attribute;
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                    ApplicationConstants.CONST_STRING_VOLUME, sizeGroups);
                            attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, attribute);
                            units = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                    sizeElementsResponse, initialUnits.trim());
                            if (units.equals("")) {
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                        ApplicationConstants.CONST_STRING_WEIGHT, sizeGroups);
                                attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, attribute);
                                units = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, initialUnits.trim());

                            }

                        } else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                                || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {

                            if (null != attribute && null != sizeValue && null != units) {

                                if (null == sizesCriteriaWSResponse) {
                                    sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                            .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                                }
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, attribute,
                                        sizeGroups);
                                // jsonProcessorObj.getSizesElementValue("CRITERIASETID",jsonProcessorObj.getSizesResponse(response,ApplicationConstants.CONST_STRING_CAPACITY,ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)),
                                // "\"")
                                attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, attribute);
                                if (units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_INCH_SHORT_SMALL)
                                        || units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_INCH_SMALL)) units = "\"";
                                if (units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FEET_SHORT_SMALL)
                                        || units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FEET_SMALL)) units = "\'";
                                units = jsonProcessorObj.getSizesElementValue("UNITS", sizeElementsResponse, units.trim());
                            }
                        } else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)
                                || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) {
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT))
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, "Unit",
                                        sizeGroups);
                            else
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                        ApplicationConstants.CONST_STRING_CAPACITY, sizeGroups);
                            if (null != sizeValue) {
                                units = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, sizeValue.trim());
                                sizeValue = attribute;
                                attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                        ApplicationConstants.CONST_STRING_CAPACITY);
                            }
                        } // End of Caps Size Groups

                        if (null != attribute && !attribute.equals("") && null != units && !units.equals("")) {
                            value = new Value();
                            value.setCriteriaAttributeId(attribute);
                            value.setUnitValue(sizeValue);
                            value.setUnitOfMeasureCode(units);
                            valueAry[valueCntr] = value;
                            valueCntr++;
                        } else {
                            validCriteriaSet = false;
                            if (CommonUtilities.isValueNull(attribute)) {
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Size Value " + attribute
                                                + " dosen't exists in lookup values");
                            } else if (CommonUtilities.isValueNull(units) && sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) {
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Invalid Unit of Measure found for Shipping Weight");
                            }
                            /*
                             * if (CommonUtilities.isValueNull(units)) {
                             * addErrorToBatchLogCollection(product.getExternalProductId(),
                             * ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Given unit " + units
                             * + " dosen't exists in lookup values");
                             * }
                             */
                            LOGGER.info(" Invalid Unit Value:" + attribute + " Units:" + units);
                        }

                    } // End of ; tokens - set multiple value components
                }
                if (validCriteriaSet) {
                    CriteriaSetCodeValues criteriaSetCodeValuesNew = new CriteriaSetCodeValues();
                    String criteriaSetValueId = "";

                    if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {
                        if (null == sizesShippingDimsWSResponse) {
                            sizesShippingDimsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZE_GROUP_SHIPPING_DIMENSION_LOOKUP));
                        }
                        criteriaSetValueId = jsonProcessorObj.checkImprintArtWorkValueKeyPair(sizesShippingDimsWSResponse, "Other",
                                ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);

                        // criteriaSetValueId=ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION_VAL_ID;
                    } else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) { // SIZE_GROUP_SHIPPING_WGHT_LOOKUP
                                                                                                                     // criteriaSetValueId=ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT_VAL_ID;
                        if (null == sizesShippingDimsWSResponse) {
                            sizesShippingDimsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZE_GROUP_SHIPPING_DIMENSION_LOOKUP));
                        }
                        criteriaSetValueId = jsonProcessorObj.checkImprintArtWorkValueKeyPair(sizesShippingDimsWSResponse, "Other",
                                ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);

                    } else
                        criteriaSetValueId = jsonProcessorObj.getSizesElementValue("CRITERIASETID", sizeElementsResponse,
                                initialUnits);
                    criteriaSetCodeValuesNew.setSetCodeValueId(criteriaSetValueId);
                    CriteriaSetValues criteriaSetValueNew = new CriteriaSetValues();
                    criteriaSetValueNew.setId(ApplicationConstants.CONST_STRING_ZERO);
                    criteriaSetValueNew.setCriteriaCode(sizeGroups);
                    if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                            || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)
                            || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION))
                        criteriaSetValueNew.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    else
                        criteriaSetValueNew.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                    criteriaSetValueNew.setCriteriaValueDetail(ApplicationConstants.CONST_STRING_NONE_SMALL);
                    criteriaSetCodeValuesNew.setId(ApplicationConstants.CONST_STRING_ZERO);
                    criteriaSetValuesID = criteriaSetValuesID - 1;
                    criteriaSetValueNew.setId(criteriaSetValuesID + "");
                    // criteriaSetValuesID=criteriaSetValuesID-1;
                    criteriaSetCodeValuesNew.setCriteriaSetValueId(criteriaSetValuesID + "");
                    // currentCriteriaSetValueID=currentCriteriaSetValueID-1;
                    // criteriaSetValueNew.setCriteriaSetId(ApplicationConstants.CONST_STRING_ZERO);
                    CriteriaSetCodeValues[] criteriaSetCodeValuesAryNew = new CriteriaSetCodeValues[1];
                    criteriaSetCodeValuesAryNew[0] = criteriaSetCodeValuesNew;
                    criteriaSetValueNew.setCriteriaSetCodeValues(criteriaSetCodeValuesAryNew);
                    criteriaSetValueNew.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);

                    criteriaSetValueNew.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValueNew.setValue(valueAry);
                    criteriaSetValueNew.setCriteriaSetId(productCriteriaSet.getCriteriaSetId());
                    // TODO : Add reference table, individualElements[0];
                    // Adding a this criteriaSet entry details to reference table, so later can be referenced easily
                    productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), sizeGroups,
                            processSourceCriteriaValueByCriteriaCode(individualSizes[0], sizeGroups), criteriaSetValueNew.getId());
                    criteriaSetValuesAry[0] = criteriaSetValueNew;
                }
            } else {
                // Multiple Size Values (,) - set multiple criteria sets
                criteriaSetValuesAry = new CriteriaSetValues[individualSizes.length];
                for (int criteriaSetValuesCntr = 0; criteriaSetValuesCntr < individualSizes.length; criteriaSetValuesCntr++) {
                    // criteriaSetValuesID=criteriaSetValuesID-1;
                    // currentCriteriaSetValueID=criteriaSetValuesID;
                    // criteriaSetValue=new CriteriaSetValues();
                    if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                            && !CommonUtilities.isValidDimension(individualSizes[criteriaSetValuesCntr])) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid format/value for Dimension ");
                        continue;
                    } else if ((sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI) || sizeGroups
                            .equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY))
                            && !CommonUtilities.isValidCapacity(individualSizes[criteriaSetValuesCntr].trim())) {
                        String temp = sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY) ? "Capacity"
                                : ApplicationConstants.CONST_STRING_VOLUME_WEIGHT;
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid format/value for " + temp);
                        continue;
                    }
                    String[] sizeValueElements = individualSizes[criteriaSetValuesCntr].split(";");
                    valueAry = new Value[sizeValueElements.length];
                    for (int valueElementsCntr = 0; valueElementsCntr < sizeValueElements.length; valueElementsCntr++) {
                        tempValueElement = sizeValueElements[valueElementsCntr];
                        // For Single Size Element(attribute:value:units) it will iterate once
                        if (tempValueElement.contains(":")) {
                            String[] valueElements = tempValueElement.split(":");

                            for (int sizeElemntCntr = 0; sizeElemntCntr < valueElements.length; sizeElemntCntr++) {
                                if (sizeElemntCntr == 0) {
                                    attribute = valueElements[sizeElemntCntr];
                                } else if (sizeElemntCntr == 1)
                                    sizeValue = valueElements[sizeElemntCntr];
                                else if (sizeElemntCntr == 2) units = valueElements[sizeElemntCntr];
                            }
                        } // End of : tokens
                        if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) initialUnits = sizeValue;
                        if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)) {
                            initialUnits = sizeValue;
                            sizeValue = attribute;
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                    ApplicationConstants.CONST_STRING_VOLUME, sizeGroups);
                            attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, attribute);
                            units = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                    sizeElementsResponse, initialUnits.trim());
                            if (units.equals("")) {
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                        ApplicationConstants.CONST_STRING_WEIGHT, sizeGroups);
                                attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, attribute);
                                units = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, initialUnits.trim());

                            }
                        } else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                                || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {

                            if (null != attribute && null != sizeValue && null != units) {

                                if (null == sizesCriteriaWSResponse) {
                                    sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                            .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                                }
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, attribute,
                                        sizeGroups);
                                // jsonProcessorObj.getSizesElementValue("CRITERIASETID",jsonProcessorObj.getSizesResponse(response,ApplicationConstants.CONST_STRING_CAPACITY,ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)),
                                // "\"")
                                attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse, attribute);
                                if (units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_INCH_SHORT_SMALL)
                                        || units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_INCH_SMALL)) units = "\"";
                                if (units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FEET_SHORT_SMALL)
                                        || units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FEET_SMALL)) units = "\'";
                                units = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, units.trim());
                            }
                        } else if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) {
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                    ApplicationConstants.CONST_STRING_CAPACITY, sizeGroups);
                            if (null != sizeValue) {
                                units = jsonProcessorObj.getSizesElementValue("UNITS", sizeElementsResponse, sizeValue.trim());
                                sizeValue = attribute;
                                attribute = jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                        ApplicationConstants.CONST_STRING_CAPACITY);
                            }
                        }
                        value = new Value();
                        value.setCriteriaAttributeId(attribute);
                        value.setUnitValue(sizeValue);
                        value.setUnitOfMeasureCode(units);
                        valueAry[valueElementsCntr] = value;
                    }

                    CriteriaSetCodeValues criteriaSetCodeValuesNew = new CriteriaSetCodeValues();
                    String criteriaSetValueId = "";
                    if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {
                        if (null == sizesShippingDimsWSResponse) {
                            sizesShippingDimsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZE_GROUP_SHIPPING_DIMENSION_LOOKUP));
                        }
                        criteriaSetValueId = jsonProcessorObj.checkImprintArtWorkValueKeyPair(optionsProdWSResponse, "Other",
                                ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);

                        // criteriaSetValueId=ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION_VAL_ID;
                    } else
                        criteriaSetValueId = jsonProcessorObj.getSizesElementValue("CRITERIASETID", sizeElementsResponse,
                                initialUnits);
                    criteriaSetCodeValuesNew.setSetCodeValueId(criteriaSetValueId);
                    CriteriaSetValues criteriaSetValueNew = new CriteriaSetValues();
                    criteriaSetValueNew.setId(ApplicationConstants.CONST_STRING_ZERO);
                    criteriaSetValueNew.setCriteriaCode(sizeGroups);
                    if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                            || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)
                            || sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION))
                        criteriaSetValueNew.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    else
                        criteriaSetValueNew.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                    criteriaSetValueNew.setCriteriaValueDetail(ApplicationConstants.CONST_STRING_NONE_SMALL);
                    criteriaSetCodeValuesNew.setId(ApplicationConstants.CONST_STRING_ZERO);
                    // currentCriteriaSetValueID=currentCriteriaSetValueID-1;
                    criteriaSetValuesID = criteriaSetValuesID - 1;
                    criteriaSetCodeValuesNew.setCriteriaSetValueId(criteriaSetValuesID + "");

                    criteriaSetValueNew.setId(criteriaSetValuesID + "");
                    // criteriaSetCodeValues.setCriteriaSetValueId(criteriaSetValue.getId());
                    criteriaSetValueNew.setCriteriaSetId(ApplicationConstants.CONST_STRING_ZERO);
                    CriteriaSetCodeValues[] criteriaSetCodeValuesAryNew = new CriteriaSetCodeValues[1];
                    criteriaSetCodeValuesAryNew[0] = criteriaSetCodeValuesNew;
                    // criteriaSetCodeValuesAry[0].setCriteriaSetValueId(criteriaSetValue.getId());
                    criteriaSetValueNew.setCriteriaSetCodeValues(criteriaSetCodeValuesAryNew);
                    criteriaSetValueNew.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValueNew.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValueNew.setValue(valueAry);
                    criteriaSetValueNew.setCriteriaSetId(productCriteriaSet.getCriteriaSetId());
                    // TODO : Set ReferenceTable
                    // Adding a this criteriaSet entry details to reference table, so later can be referenced easily
                    productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), sizeGroups,
                            processSourceCriteriaValueByCriteriaCode(individualSizes[criteriaSetValuesCntr], sizeGroups),
                            criteriaSetValueNew.getId());
                    criteriaSetValuesAry[criteriaSetValuesCntr] = criteriaSetValueNew;

                }
            } // End of csv criteria Sets
            if (criteriaSetValuesAry != null) {
                List<CriteriaSetValues> filteredSetValues = new ArrayList<CriteriaSetValues>();
                for (int i = 0; i < criteriaSetValuesAry.length; i++) {
                    if (criteriaSetValuesAry[i] != null) {
                        filteredSetValues.add(criteriaSetValuesAry[i]);
                    }
                }
                productCriteriaSet.setCriteriaSetValues(filteredSetValues.toArray(new CriteriaSetValues[0]));
            }
            productCriteriaSet.setCompanyId(product.getCompanyId());
            // productCriteriaSet.setCriteriaSetValues(criteriaSetValuesAry);
            
            /*
             * else
             * Arrays.copyOf(crntProductCriteriaSets, crntProductCriteriaSets.length-1);
             */

        } // End of product sizes if condition
        
        if (productCriteriaSet != null && productCriteriaSet.getCriteriaSetValues() != null
                && productCriteriaSet.getCriteriaSetValues().length > 0) {
            // criteriaSetValuesID=criteriaSetValuesID+1;
            return productCriteriaSet;
        } else {
            return productCriteriaSet;
        }
    }

    /**
     * 
     * Modifies the criteria set as per relationship and criteria code
     * 
     * @param productConfigId
     *            is config id of the product
     * @param productCriteriaSets
     *            is the criteria set which to modify
     * @param criteriaCode
     *            is the criteria code of the criteria set
     * @return modified {@linkplain ProductCriteriaSets} or the actual one
     */
    public ProductCriteriaSets modifyCriteriaSetAsPerRelationships(String productConfigId, ProductCriteriaSets productCriteriaSets,
            String criteriaCode) {
        if (null != criteriaCode && null != productCriteriaSets) {
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_METHOD_CODE)) {
                productCriteriaSets.setCriteriaSetId(Integer.parseInt(productConfigId) - 1 + "");
                productCriteriaSets.setParentCriteriaSetId(productConfigId);
                productCriteriaSets.getCriteriaSetValues()[0].setId(Integer.parseInt(productConfigId) - 2 + "");
                productCriteriaSets.getCriteriaSetValues()[0].setCriteriaSetId(productCriteriaSets.getCriteriaSetId());
                productCriteriaSets.getCriteriaSetValues()[0].getCriteriaSetCodeValues()[0].setId(Integer.parseInt(productConfigId)
                        - 3 + "");
                productCriteriaSets.getCriteriaSetValues()[0].getCriteriaSetCodeValues()[0]
                        .setCriteriaSetValueId(productCriteriaSets.getCriteriaSetValues()[0].getId());

            } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
                productCriteriaSets.setCriteriaSetId(Integer.parseInt(productConfigId) - 4 + "");
                productCriteriaSets.setParentCriteriaSetId(productConfigId);
                productCriteriaSets.getCriteriaSetValues()[0].setId(Integer.parseInt(productConfigId) - 5 + "");
                productCriteriaSets.getCriteriaSetValues()[0].setCriteriaSetId(productCriteriaSets.getCriteriaSetId());
                productCriteriaSets.getCriteriaSetValues()[0].getCriteriaSetCodeValues()[0].setId(Integer.parseInt(productConfigId)
                        - 6 + "");
                productCriteriaSets.getCriteriaSetValues()[0].getCriteriaSetCodeValues()[0]
                        .setCriteriaSetValueId(productCriteriaSets.getCriteriaSetValues()[0].getId());
            }
        }

        return productCriteriaSets;
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
    public ProductCriteriaSets getCriteriaSetBasedOnCriteriaCode(ProductCriteriaSets productCriteriaSetsAry[], String criteriaCode) {
        ProductCriteriaSets crntProductCriteriaSet = null;

        for (int i = 0; i < productCriteriaSetsAry.length; i++) {
            crntProductCriteriaSet = productCriteriaSetsAry[i];
            if (null != crntProductCriteriaSet && crntProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode.trim()))
                break;
        }
        return crntProductCriteriaSet;
    }

    /**
     * Add a criteriaSet for a give criteria code
     * 
     * @param sourceProduct
     *            is the source product
     * @param product
     *            is the exising product
     * @param criteriaCode
     *            is the criteria code where the criteriaSet belongs
     * @param srcCriteria
     *            is the criteriaSet
     * @param description
     *            is the description of the criteriaSet
     * @return {@linkplain ProductCriteriaSets}
     * @throws VelocityException
     */
    public ProductCriteriaSets addCriteriaSetForCriteriaCode(Product sourceProduct, Product product, String criteriaCode,
            String srcCriteria, String description) throws VelocityException {
        ProductCriteriaSets crntProductCriteriaSets = new ProductCriteriaSets();
        boolean crntCriteriaCheck = false;
        String initSizeGroup = criteriaCode;
        criteriaCode = criteriaCode.trim();
        srcCriteria = srcCriteria.trim();
        boolean isCustomValue = false;
        boolean isOtherSize = false;

        if (!crntCriteriaCheck) {
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_ADDITIONAL_LOCATION))
                criteriaCode = ApplicationConstants.CONST_ADDITIONAL_LOCATION;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_ADDITIONAL_COLOR))
                criteriaCode = ApplicationConstants.CONST_ADDITIONAL_COLOR;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_DIMENSION))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_DIMENSION;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_VOLUME_WEIGHT))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_CAPACITY))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_CAPACITY;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_INFANT_TODDLER))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_DRESS_SHIRT_SIZES))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_PANTS_SIZES))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM)) {
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM;
                srcCriteria = srcCriteria.toUpperCase();
            } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_BRA_SIZES))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD_NUMBERED))
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_PRODUCT_OPTION))
                criteriaCode = ApplicationConstants.CONST_PRODUCT_OPTION;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_SHIPPING_OPTION))
                criteriaCode = ApplicationConstants.CONST_SHIPPING_OPTION;
            else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_IMPRINT_OPTION))
                criteriaCode = ApplicationConstants.CONST_IMPRINT_OPTION;

            crntProductCriteriaSets.setDescription(description);

            // CriteriaSetCodeValues chilsd1Obj = null;
            CriteriaSetValues[] criteriaSetValuesAry = {};
            CriteriaSetCodeValues[] criteriaSetCodeValues = null;
            // CriteriaSetValues criteriaSetXValue = new CriteriaSetValues();
            CriteriaSetValues tempCriteriaSetValues = null;
            String unitValue = "";
            criteriaSetUniqueId--; // Creates unique Id
            String actualCriteria = srcCriteria;
            // boolean
            // checkExistingCriteriaSetCode=jerseyClient.containsInExisting(product,criteriaCode);
            // if (listOfSetCodeValues == null) listOfSetCodeValues = jerseyClient.getAllSetCodeValueIds(sourceProduct);
            listOfSetCodeValues = new HashMap<>();
            // listOfSetCodeValues = jerseyClient.getAllSetCodeValueIds(sourceProduct); //
            CriteriaSetValues[] existingCriteriaSetCodeValues = {};
            existingCriteriaSetCodeValues = listOfSetCodeValues.get(criteriaCode);
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE) && srcCriteria.contains(",")) {
                // x1,x2|y1,y2
                String[] imprintAttributes = srcCriteria.split("\\|");
                String[] imprintSizes = imprintAttributes.length > 0 ? imprintAttributes[0].split(",") : new String[]{};
                String[] imprintLocation = imprintAttributes.length > 1 ? imprintAttributes[1].split(",") : new String[]{};
                // if the array length is not matching then make it equal
                if (imprintSizes.length != imprintLocation.length) {
                    if (imprintSizes.length > imprintLocation.length) {

                        String[] tempArray = new String[imprintSizes.length];
                        Arrays.fill(tempArray, "");
                        System.arraycopy(imprintLocation, 0, tempArray, 0, imprintLocation.length);

                        imprintLocation = tempArray;
                    } else if (imprintLocation.length > imprintSizes.length) {
                        String[] tempArray = new String[imprintLocation.length];
                        Arrays.fill(tempArray, "");
                        System.arraycopy(imprintSizes, 0, tempArray, 0, imprintSizes.length);

                        imprintSizes = tempArray;
                    }
                }
                if (imprintSizes.length == imprintLocation.length) {
                    srcCriteria = "";
                    for (int imprintCntr = 0; imprintCntr < imprintSizes.length; imprintCntr++) {
                        if (imprintCntr < imprintSizes.length - 1)
                            srcCriteria += imprintSizes[imprintCntr] + "|" + imprintLocation[imprintCntr] + ",";
                        else
                            srcCriteria += imprintSizes[imprintCntr] + "|" + imprintLocation[imprintCntr];
                    }
                }

            }

            if (srcCriteria.contains(",")) {
                isCustomValue = false;
                String[] criteriaElements; 
                if (ApplicationConstants.CONST_SHAPE_CRITERIA_CODE.equalsIgnoreCase(criteriaCode) 
                        || ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
                    criteriaElements = CommonUtilities.filterDuplicates(srcCriteria.split(","));
                } else {
                    criteriaElements = srcCriteria.split(",");
                }
                
                int elementsLength = criteriaElements.length;
                int cntr = 0;

                criteriaSetValuesAry = new CriteriaSetValues[elementsLength];
                int invalidCriteriasCntr = 0;
                for (String curntCriteria : criteriaElements) {
                    String orignalCriteriaValue = processSourceCriteriaValueByCriteriaCode(curntCriteria, criteriaCode);
                    isCustomValue = false;
                    curntCriteria = curntCriteria != null ? curntCriteria.trim() : curntCriteria;
                    actualCriteria = curntCriteria;
                    CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
                    criteriaSetValue.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
                    CriteriaSetCodeValues child1Obj = new CriteriaSetCodeValues();
                    criteriaSetCodeValues = new CriteriaSetCodeValues[1];
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)) {
                        curntCriteria = curntCriteria.toUpperCase();
                    }
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE)) {
                        if (null == originWSResponse) {
                            originWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.ORIGIN_LOOKUP_URL));
                        }
                        String temp = curntCriteria;
                        curntCriteria = jsonProcessorObj.checkValueKeyPairForOrign(originWSResponse, curntCriteria);
                        if (CommonUtilities.isValueNull(curntCriteria)) {
                            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Orgin Value " + temp
                                            + " dosen't exists in lookup values");
                        }
                        // orgn = orgnCall.getKeyValue(orgn); // orn key value ,
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_COLORS_CRITERIA_CODE)) {

                        if (null == colorsWSResponse) {
                            colorsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.COLORS_LOOKUP_URL));
                        }
                        if (curntCriteria.contains("=")) {
                            curntCriteria = curntCriteria.substring(0, curntCriteria.indexOf("="));
                        }
                        curntCriteria = jsonProcessorObj.checkColorValueKeyPair(colorsWSResponse, curntCriteria);

                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE)) {
                        if (null == materialWSResponse) {
                            materialWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.MATERIALS_LOOKUP_URL));
                        }
                        if (curntCriteria.contains("=")) {
                            curntCriteria = curntCriteria.substring(0, curntCriteria.indexOf("="));
                        }
                        curntCriteria = jsonProcessorObj.checkMaterialValueKeyPair(materialWSResponse, curntCriteria);

                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE)) {
                        // curntCriteria=
                        if (null == shapesWSResponse) {
                            shapesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.PRODUCT_SHAPES_LOOKUP_URL));
                        }
                        String temp = curntCriteria;
                        curntCriteria = jsonProcessorObj.checkShapeValueKeyPair(shapesWSResponse, curntCriteria);
                        if (CommonUtilities.isValueNull(curntCriteria)) {
                            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Shape Value " + temp
                                            + " dosen't exists in lookup values");
                        }
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_THEME_CRITERIA_CODE)) {

                        if (null == themesWSResponse) {
                            themesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.PRODUCT_THEMES_URL));
                        }
                        String temp = curntCriteria;
                        curntCriteria = jsonProcessorObj.checkThemeValueKeyPair(themesWSResponse, curntCriteria);
                        if (CommonUtilities.isValueNull(curntCriteria)) {
                            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Theme Value " + temp
                                            + " dosen't exists in lookup values");
                        }
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_TRADE_NAME_CODE)) {
                        curntCriteria = curntCriteria.trim();
                        tradeNamesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.PRODUCT_TRADENAMES_LOOKUP)
                                + CommonUtilities.getURLEncodedValue(curntCriteria));

                        String temp = curntCriteria;
                        curntCriteria = jsonProcessorObj.checkValueKeyPair(tradeNamesWSResponse, curntCriteria,
                                ApplicationConstants.CONST_TRADE_NAME_CODE);

                        if (CommonUtilities.isValueNull(curntCriteria)) {
                            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Tradename Value " + temp
                                            + " dosen't exists in lookup values");
                        }
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                        // Imprint Size and Location
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)) {
                            if (null == imprintColorWSResponse) {
                                imprintColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.IMPRINT_COLOR_LOOKUP));
                            }
                            curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(imprintColorWSResponse, "Other",
                                    ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
                        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                            if (null == imprintSizeWSResponse) {
                                imprintSizeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.IMPRINT_SIZE_LOOKUP));
                            }
                            curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(imprintSizeWSResponse, "Other",
                                    ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE);
                        }
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE)) {
                        // Packaging
                        JsonProcessor.isCustomValue = false;
                        if (null == packagingWSResponse) {
                            packagingWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.PACKAGING_LOOKUP));
                        }

                        curntCriteria = jsonProcessorObj.checkValueKeyPair(packagingWSResponse, curntCriteria,
                                ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
                        if (JsonProcessor.isCustomValue) {
                            isCustomValue = true;
                        }
                        JsonProcessor.isCustomValue = false;
                        // curntCriteria="";
                        // TODO : PCKG
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE)) {
                        unitValue = curntCriteria;
                        // PRODUCT_SAMPLE_LOOKUP
                        if (null == additionalColorWSResponse) {
                            additionalColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP));
                        }
                        curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(additionalColorWSResponse,
                                "Product Sample", ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);

                        // curntCriteria=ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE_VAL_ID;
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)) {
                        // Production Time
                        if (CommonUtilities.isValidProductionTime(curntCriteria)) {
                            unitValue = curntCriteria;
                            // PRODUCTION_TIME_LOOKUP
                            if (null == rushTimeWSResponse) {
                                rushTimeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.RUSH_TIME_LOOKUP));
                            }
                            curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(rushTimeWSResponse, "Other",
                                    ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
                        } else {
                            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                    ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid value found for Production Time "
                                            + curntCriteria);
                            curntCriteria = ApplicationConstants.CONST_STRING_NULL_CAP;
                        }

                        // curntCriteria=ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE_VAL_ID;
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ADDITIONAL_LOCATION)) {
                        unitValue = curntCriteria;
                        // ADDITIONAL_LOCATION_LOOKUP
                        if (null == additionalColorWSResponse) {
                            additionalColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP));
                        }
                        curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(additionalColorWSResponse, "Other",
                                ApplicationConstants.CONST_ADDITIONAL_LOCATION);

                        // curntCriteria=ApplicationConstants.CONST_ADDITIONAL_LOCATION_VAL_ID;
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ADDITIONAL_COLOR)) {
                        unitValue = curntCriteria;
                        if (null == additionalColorWSResponse) {
                            additionalColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP));
                        }
                        curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(additionalColorWSResponse, "Other",
                                ApplicationConstants.CONST_ADDITIONAL_COLOR);
                        // curntCriteria=ApplicationConstants.CONST_ADDITIONAL_COLOR_VAL_ID;
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)) {
                        // Rush Time
                        unitValue = curntCriteria;
                        if (null == rushTimeWSResponse) {
                            rushTimeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.RUSH_TIME_LOOKUP));
                        }
                        curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(rushTimeWSResponse, "Other",
                                ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);

                        // curntCriteria=ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE_VAL_ID;
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                        // Shipping Items
                        unitValue = curntCriteria;
                        // PRODUCT_SHIPPING_ITEM_LOOKUP
                        if (null == shippingItemsWSResponse) {
                            shippingItemsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.PRODUCT_SHIPPING_ITEM_LOOKUP));
                        }
                        curntCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(shippingItemsWSResponse, "Other",
                                ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);

                        // curntCriteria=ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE_VAL_ID;
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
                        // Option Type and Names
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)) {

                            if (null == optionsProdWSResponse) {
                                optionsProdWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.OPTION_PRODUCT_LOOKUP));
                            }
                            curntCriteria = jsonProcessorObj.checkOptionsKeyValueKeyPair(optionsProdWSResponse, curntCriteria,
                                    ApplicationConstants.CONST_PRODUCT_OPTION);
                        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)) {

                            if (null == optionsShipWSResponse) {
                                optionsShipWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.OPTION_SHIPPING_LOOKUP));
                            }
                            curntCriteria = jsonProcessorObj.checkOptionsKeyValueKeyPair(optionsShipWSResponse, curntCriteria,
                                    ApplicationConstants.CONST_SHIPPING_OPTION);
                        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {

                            if (null == optionsImprintWSResponse) {
                                optionsImprintWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.OPTION_IMPRINT_LOOKUP));
                            }
                            curntCriteria = jsonProcessorObj.checkOptionsKeyValueKeyPair(optionsImprintWSResponse, curntCriteria,
                                    ApplicationConstants.CONST_IMPRINT_OPTION);
                        }
                    } else if (initSizeGroup.contains(ApplicationConstants.CONST_STRING_APPAREL)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)) {
                        String tempCriteria = curntCriteria;
                        if (null == sizesWSResponse) {
                            sizesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZES_LOOKUP_URL));
                        }
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)
                                && !(curntCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_CUSTOM) || curntCriteria
                                        .equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD))) {
                            curntCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse,
                                    ApplicationConstants.CONST_STRING_OTHER_SIZES, criteriaCode);
                            isCustomValue = true;
                        } else {
                            curntCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse, curntCriteria, criteriaCode);
                        }
                        if (curntCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE)) {
                                isOtherSize = true;
                                curntCriteria = jsonProcessorObj.checkOtherSizesKeyValuePair(sizesWSResponse,
                                        ApplicationConstants.CONST_STRING_OTHER, criteriaCode);
                                isCustomValue = true;
                            }
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)) {
                                isOtherSize = true;
                                if (tempCriteria.toLowerCase().contains("month")) {
                                    tempCriteria=tempCriteria.toLowerCase();
                                    tempCriteria=tempCriteria.substring(0,tempCriteria.indexOf("m"));
                                    unitValue = "months";
                                    actualCriteria = tempCriteria.trim();
                                } else if (tempCriteria.toLowerCase().contains("t")) {
                                    tempCriteria=tempCriteria.toLowerCase();
                                    tempCriteria=tempCriteria.substring(0,tempCriteria.indexOf("t"));
                                    unitValue= "T";
                                    actualCriteria = tempCriteria.trim();
                                }      
                            }
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM)) {
                                curntCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse,
                                        ApplicationConstants.CONST_STRING_STANDARD_NUMBERED_OTHER, criteriaCode);
                                isCustomValue = true;
                            }
                        }
                    }
                    tempCriteriaSetValues = jerseyClient.IsExistingCriteriaCode(existingCriteriaSetCodeValues, curntCriteria,
                            sourceProduct);

                    if (null != tempCriteriaSetValues && !crntCriteriaCheck) {
                        crntCriteriaCheck = true;
                        criteriaSetValuesAry[cntr] = tempCriteriaSetValues;
                        cntr++;
                    } else if (null != curntCriteria
                            && !curntCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                        crntCriteriaCheck = true;
                        criteriaSetValuesID = criteriaSetValuesID - 1;
                        child1Obj.setSetCodeValueId(curntCriteria);
                        // child1Obj.setCodeValue(criteriaCode); // "PRCL"
                        child1Obj.setCriteriaSetValueId(criteriaSetValuesID + "");
                        criteriaSetCodeValues[0] = child1Obj;
                        child1Obj = null;
                        criteriaSetValue.setId(criteriaSetValuesID + "");

                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                            Value value = new Value();
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                                if (null == sizesCriteriaWSResponse) {
                                    sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                            .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                                }
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, "Unit",
                                        criteriaCode);
                                if (unitValue.contains(":")) {
                                    String[] unitValueAry = unitValue.split(":");
                                    String unitsCode = unitValueAry[1];
                                    unitValue = unitValueAry[0];
                                    String temp = unitsCode;
                                    unitsCode = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                            sizeElementsResponse, unitsCode.trim());
                                    if (CommonUtilities.isValueNull(unitsCode)) { // Fix for unit other than in Lookup
                                        unitsCode = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                                sizeElementsResponse, ApplicationConstants.CONST_STRING_OTHER);
                                        if (criteriaSetValue.getCriteriaSetCodeValues() != null && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
                                            criteriaSetValue.getCriteriaSetCodeValues()[0].setCodeValue(temp);
                                            value.setUnitOfMeasureCode(unitsCode);
                                        }
                                    } else {
                                        value.setUnitOfMeasureCode(unitsCode);
                                    }

                                    value.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                            unitValue.trim()));
                                    value.setUnitValue(unitValue);
                                    value.setUnitOfMeasureCode(unitsCode);
                                    Value[] valueAry = new Value[1];
                                    valueAry[0] = value;
                                    criteriaSetValue.setValue(valueAry);
                                }
                            } else {
                                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)
                                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE))
                                    value.setCriteriaAttributeId("14");
                                else
                                    value.setCriteriaAttributeId("13");
                                value.setUnitValue(unitValue);
                                value.setUnitOfMeasureCode("BUSI");
                                Value[] valueAry = new Value[1];
                                valueAry[0] = value;
                                criteriaSetValue.setValue(valueAry);
                                // criteriaSetValue.setFormatValue(unitValue+" DAYS");
                            }
                        } else if (actualCriteria != null
                                && actualCriteria.contains("=")
                                && (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE) || criteriaCode
                                        .equalsIgnoreCase(ApplicationConstants.CONST_COLORS_CRITERIA_CODE))) {
                            criteriaSetValue.setValue(actualCriteria.substring(actualCriteria.indexOf("=") + 1,
                                    actualCriteria.length()));
                        } else if (isOtherSize) {
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)) {
                                if (null == sizesCriteriaWSResponse) {
                                    sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                            .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                                }
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, "Unit",
                                        criteriaCode);
                                Value valueObj = new Value();
                                // String[] unitValueAry=new String[1];
                                // String unitsCode = unitValueAry[1];
                                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)
                                        && !CommonUtilities.isValueNull(unitValue)) {
                                    String temp = jsonProcessorObj.getSizesElementValue("Units", sizeElementsResponse,
                                            unitValue.trim());
                                    valueObj.setUnitOfMeasureCode(CommonUtilities.isValueNull(temp) ? "" : temp);
                                }
                                unitValue = (unitValue == null || unitValue.trim().isEmpty()) ? actualCriteria : unitValue;

                                valueObj.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                        unitValue.trim()));
                                valueObj.setUnitValue(actualCriteria);

                                Value[] valueAry = new Value[1];
                                valueAry[0] = valueObj;
                                criteriaSetValue.setValue(valueAry);
                            }
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)
                                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE)) {
                                String[] unitValueAry = null;
                                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE))
                                    unitValueAry = new String[] { ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_NECK,
                                            ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_SLVS };
                                else
                                    unitValueAry = new String[] { ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_WAIST,
                                            ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_INSEAM };
                                Value valueObj = null;
                                Value[] valueAry = new Value[unitValueAry.length];
                                String[] untValueFnlAry = new String[unitValueAry.length];
                                String validUnit = null;
                                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE))
                                    validUnit = CommonUtilities.getStringWithBrackets(actualCriteria);
                                else {
                                    if (actualCriteria.contains("x"))
                                        validUnit = actualCriteria.substring(actualCriteria.indexOf("x") + 1,
                                                actualCriteria.length());

                                }

                                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)) {
                                    if (validUnit.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FALSE_SMALL)) {
                                        untValueFnlAry = new String[] { actualCriteria, ApplicationConstants.CONST_STRING_EMPTY };
                                    } else
                                        untValueFnlAry = new String[] { actualCriteria.substring(0, actualCriteria.indexOf("(")),
                                                validUnit };
                                } else {
                                    if (actualCriteria.contains("x"))
                                        untValueFnlAry = new String[] { actualCriteria.substring(0, actualCriteria.indexOf("x")),
                                                validUnit };
                                    else
                                        untValueFnlAry = new String[] { actualCriteria, ApplicationConstants.CONST_STRING_EMPTY };
                                }

                                for (int untValCntr = 0; untValCntr < unitValueAry.length; untValCntr++) {
                                    valueObj = new Value();
                                    if (null == sizesCriteriaWSResponse) {
                                        sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                                .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                                    }
                                    sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                            unitValueAry[untValCntr], criteriaCode);
                                    valueObj.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID",
                                            sizeElementsResponse, unitValueAry[untValCntr]));
                                    valueObj.setUnitValue(untValueFnlAry[untValCntr].trim());
                                    if (!valueObj.getUnitValue().equalsIgnoreCase(ApplicationConstants.CONST_STRING_EMPTY)) {
                                        valueAry[untValCntr] = valueObj;
                                    } else
                                        valueAry = Arrays.copyOf(valueAry, valueAry.length - 1);
                                }
                                criteriaSetValue.setValue(valueAry);
                            }

                            isOtherSize = false;
                        } else {
                            criteriaSetValue.setValue(actualCriteria);
                        }

                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)) {
                            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_COLOR);
                        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE)) {
                            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                        } else if (isCustomValue) {
                            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                            isCustomValue = false;
                        } else
                            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE)) {
                            criteriaSetValue.setCriteriaValueDetail(actualCriteria);

                        } else
                            criteriaSetValue
                                    .setCriteriaValueDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                                            .getCriteriaSetValues()[0].getCriteriaValueDetail());
                        criteriaSetValue.setIsSubset(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                                .getCriteriaSetValues()[0].getIsSubset());
                        criteriaSetValue.setIsSetValueMeasurement(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                                .getCriteriaSetValues()[0].getIsSetValueMeasurement());

                        criteriaSetValue.setCriteriaSetId(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                                .getCriteriaSetValues()[0].getCriteriaSetId());

                        criteriaSetValue.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
                        criteriaSetValue.setCriteriaCode(criteriaCode);
                        if (actualCriteria.equals("null|null")) {
                            actualCriteria = "";
                        }

                        /*
                         * if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)
                         * || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                         * criteriaSetValue.setFormatValue(actualCriteria);
                         * criteriaSetValue.setCriteriaSetCodeValues(new CriteriaSetCodeValues[] {});
                         * } else
                         * criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
                         */
                        
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                            criteriaSetValue.setFormatValue(actualCriteria);
                            criteriaSetValue.setCriteriaSetCodeValues(new CriteriaSetCodeValues[] {});
                        } else
                            criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
                        /*
                         * criteriaSetValue
                         * .setCriteriaCode(product.getProductConfigurations()[0]
                         * .getProductCriteriaSets()[0] .getCriteriaSetValues()[0]
                         * .getCriteriaCode());
                         */
                        // CriteriaSetValues[] criteriaSetValuesAry = new
                        // CriteriaSetValues[1];
                        if (criteriaSetValuesAry.length > cntr) {
                            criteriaSetValuesAry[cntr] = criteriaSetValue;
                            // Adding a this criteriaSet entry details to reference table, so later can be referenced easily
                            if (isOptionGroup(criteriaCode) && !CommonUtilities.isValueNull(description)) {
                                String temp = processOptionNameForPriceCriteria(criteriaCode, description, orignalCriteriaValue);
                                productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId(), criteriaCode,
                                        temp, criteriaSetValue.getId());
                            } else {
                                productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId(), criteriaCode,
                                        orignalCriteriaValue, criteriaSetValue.getId());
                            }
                        }
                        criteriaSetValue = null;
                        // child1Obj.setChildCriteriaSetCodeValues(null);
                        // child1Obj.setCodeValueDetail(null);
                        cntr++;
                    } else {
                        // crntCriteriaCheck = false;
                        invalidCriteriasCntr++;
                        criteriaSetValuesAry = Arrays.copyOf(criteriaSetValuesAry, criteriaSetValuesAry.length - 1);
                        LOGGER.info("Criteria value " + actualCriteria + " is Invalid Name found in " + criteriaCode + " Type");
                    }
                    //

                    // child1ObjAry[cntr] = criteriaSetValue;

                }
                if (invalidCriteriasCntr == criteriaElements.length) crntCriteriaCheck = false;
            } else {
                String orignalCriteriaValue = processSourceCriteriaValueByCriteriaCode(srcCriteria, criteriaCode);
                srcCriteria = srcCriteria.trim();
                CriteriaSetValues criteriaSetValue = new CriteriaSetValues();

                CriteriaSetCodeValues child1Obj = new CriteriaSetCodeValues();
                criteriaSetCodeValues = new CriteriaSetCodeValues[1];
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)) {
                    srcCriteria = srcCriteria.toUpperCase();
                }
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE)) {
                    if (null == originWSResponse) {
                        originWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.ORIGIN_LOOKUP_URL));
                    }
                    String temp = srcCriteria;
                    srcCriteria = jsonProcessorObj.checkValueKeyPairForOrign(originWSResponse, srcCriteria);
                    if (CommonUtilities.isValueNull(srcCriteria)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Orgin Value " + temp
                                        + " dosen't exists in lookup values");
                    }
                    // orgn = orgnCall.getKeyValue(orgn); // orn key value ,
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_COLORS_CRITERIA_CODE)) {

                    if (null == colorsWSResponse) {
                        colorsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.COLORS_LOOKUP_URL));
                    }
                    if (srcCriteria.contains("=")) {
                        srcCriteria = srcCriteria.substring(0, srcCriteria.indexOf("="));
                    }
                    srcCriteria = jsonProcessorObj.checkColorValueKeyPair(colorsWSResponse, srcCriteria);

                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE)) {
                    if (null == materialWSResponse) {
                        materialWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.MATERIALS_LOOKUP_URL));
                    }
                    if (srcCriteria.contains("=")) {
                        srcCriteria = srcCriteria.substring(0, srcCriteria.indexOf("="));
                    }
                    srcCriteria = jsonProcessorObj.checkMaterialValueKeyPair(materialWSResponse, srcCriteria);

                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE)) {
                    // curntCriteria=
                    if (null == shapesWSResponse) {
                        shapesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.PRODUCT_SHAPES_LOOKUP_URL));
                    }
                    String temp = srcCriteria;
                    srcCriteria = jsonProcessorObj.checkShapeValueKeyPair(shapesWSResponse, srcCriteria);
                    if (CommonUtilities.isValueNull(srcCriteria)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Shape Value " + temp
                                        + " dosen't exists in lookup values");
                    }
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_THEME_CRITERIA_CODE)) {

                    if (null == themesWSResponse) {
                        themesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.PRODUCT_THEMES_URL));
                    }
                    String temp = srcCriteria;
                    srcCriteria = jsonProcessorObj.checkThemeValueKeyPair(themesWSResponse, srcCriteria);
                    if (CommonUtilities.isValueNull(srcCriteria)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Theme Value " + temp
                                        + " dosen't exists in lookup values");
                    }
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_TRADE_NAME_CODE)) {
                    tradeNamesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.PRODUCT_TRADENAMES_LOOKUP) + CommonUtilities.getURLEncodedValue(srcCriteria));
                    String temp = srcCriteria;
                    srcCriteria = jsonProcessorObj.checkValueKeyPair(tradeNamesWSResponse, srcCriteria,
                            ApplicationConstants.CONST_TRADE_NAME_CODE);
                    if (CommonUtilities.isValueNull(srcCriteria)) {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Tradename Value " + temp
                                        + " dosen't exists in lookup values");
                    }
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                    // srcCriteria="";
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)) {
                        if (null == imprintColorWSResponse) {
                            imprintColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.IMPRINT_COLOR_LOOKUP));
                        }
                        srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(imprintColorWSResponse, "Other",
                                ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                        if (null == imprintSizeWSResponse) {
                            imprintSizeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.IMPRINT_SIZE_LOOKUP));
                        }
                        srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(imprintSizeWSResponse, "Other",
                                ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE);
                    }

                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)) {

                    sizesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties.get(ApplicationConstants.SIZES_LOOKUP_URL));

                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)
                            && !(srcCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_CUSTOM) || srcCriteria
                                    .equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD))) {
                        srcCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse,
                                ApplicationConstants.CONST_STRING_OTHER_SIZES, criteriaCode);
                        isCustomValue = true;
                    } else {
                        srcCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse, srcCriteria, criteriaCode);
                    }
                    // srcCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse,
                    // ApplicationConstants.CONST_STRING_OTHER_SIZES, criteriaCode);

                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE)) {
                    // Packaging
                    JsonProcessor.isCustomValue = false;
                    if (null == packagingWSResponse) {
                        packagingWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.PACKAGING_LOOKUP));
                    }
                    srcCriteria = jsonProcessorObj.checkValueKeyPair(packagingWSResponse, srcCriteria,
                            ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
                    if (JsonProcessor.isCustomValue) {
                        isCustomValue = true;
                    }
                    JsonProcessor.isCustomValue = false;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE)) {
                    unitValue = srcCriteria;
                    if (null == additionalColorWSResponse) {
                        additionalColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP));
                    }
                    srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(additionalColorWSResponse, "Product Sample",
                            ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);

                    // srcCriteria=ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE_VAL_ID;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)) {
                    // Production Time
                    if (CommonUtilities.isValidProductionTime(srcCriteria)) {
                        unitValue = srcCriteria;
                        if (null == rushTimeWSResponse) {
                            rushTimeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.RUSH_TIME_LOOKUP));
                        }
                        srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(rushTimeWSResponse, "Other",
                                ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
                    } else {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid value found for Production Time "
                                        + srcCriteria);
                        srcCriteria = ApplicationConstants.CONST_STRING_NULL_CAP;
                    }

                    // srcCriteria=ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE_VAL_ID;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ADDITIONAL_LOCATION)) {
                    unitValue = srcCriteria;
                    if (null == additionalColorWSResponse) {
                        additionalColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP));
                    }
                    srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(additionalColorWSResponse, "Other",
                            ApplicationConstants.CONST_ADDITIONAL_LOCATION);

                    // srcCriteria=ApplicationConstants.CONST_ADDITIONAL_LOCATION_VAL_ID;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ADDITIONAL_COLOR)) {
                    unitValue = srcCriteria;
                    if (null == additionalColorWSResponse) {
                        additionalColorWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP));
                    }
                    srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(additionalColorWSResponse, "Other",
                            ApplicationConstants.CONST_ADDITIONAL_COLOR);

                    // srcCriteria=ApplicationConstants.CONST_ADDITIONAL_COLOR_VAL_ID;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)) {
                    // Rush Time
                    unitValue = srcCriteria;
                    if (null == rushTimeWSResponse) {
                        rushTimeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.RUSH_TIME_LOOKUP));
                    }
                    srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(rushTimeWSResponse, "Other",
                            ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);

                    // srcCriteria=ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE_VAL_ID;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                    // Shipping Items
                    unitValue = srcCriteria;
                    if (null == shippingItemsWSResponse) {
                        shippingItemsWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.PRODUCT_SHIPPING_ITEM_LOOKUP));
                    }
                    srcCriteria = jsonProcessorObj.checkImprintArtWorkValueKeyPair(shippingItemsWSResponse, "Other",
                            ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);

                    // srcCriteria=ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE_VAL_ID;
                } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
                    // Option Type and Names

                    // Option Type and Names
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)) {

                        if (null == optionsProdWSResponse) {
                            optionsProdWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.OPTION_PRODUCT_LOOKUP));
                        }
                        srcCriteria = jsonProcessorObj.checkOptionsKeyValueKeyPair(optionsProdWSResponse, srcCriteria,
                                ApplicationConstants.CONST_PRODUCT_OPTION);
                    }
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)) {

                        if (null == optionsShipWSResponse) {
                            optionsShipWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.OPTION_SHIPPING_LOOKUP));
                        }
                        srcCriteria = jsonProcessorObj.checkOptionsKeyValueKeyPair(optionsShipWSResponse, srcCriteria,
                                ApplicationConstants.CONST_SHIPPING_OPTION);
                    }
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {

                        if (null == optionsImprintWSResponse) {
                            optionsImprintWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.OPTION_IMPRINT_LOOKUP));
                        }
                        srcCriteria = jsonProcessorObj.checkOptionsKeyValueKeyPair(optionsImprintWSResponse, srcCriteria,
                                ApplicationConstants.CONST_IMPRINT_OPTION);
                    }

                } else if (initSizeGroup.contains(ApplicationConstants.CONST_STRING_APPAREL)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)) {
                    String tempCriteria = srcCriteria;
                    if (null == sizesWSResponse) {
                        sizesWSResponse = orgnCall.getLookupsResponse(RestAPIProperties.get(ApplicationConstants.SIZES_LOOKUP_URL));
                    }
                    srcCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse, srcCriteria, criteriaCode);
                    if (srcCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE)) {
                            isOtherSize = true;
                        }
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)) {
                            isOtherSize = true;
                            if (tempCriteria.toLowerCase().contains("month")) {
                                tempCriteria=tempCriteria.toLowerCase();
                                tempCriteria=tempCriteria.substring(0,tempCriteria.indexOf("m"));
                                unitValue = "months";
                                actualCriteria = tempCriteria.trim();
                            } else if (tempCriteria.toLowerCase().contains("t")) {
                                tempCriteria=tempCriteria.toLowerCase();
                                tempCriteria=tempCriteria.substring(0,tempCriteria.indexOf("t"));
                                unitValue= "T";
                                actualCriteria = tempCriteria;
                            }  
                        }
                        srcCriteria = jsonProcessorObj.checkOtherSizesKeyValuePair(sizesWSResponse,
                                ApplicationConstants.CONST_STRING_OTHER, criteriaCode);
                        isCustomValue = true;

                    }
                    if (srcCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
                            && criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM)) {
                        srcCriteria = jsonProcessorObj.checkSizesKeyValuePair(sizesWSResponse,
                                ApplicationConstants.CONST_STRING_STANDARD_NUMBERED_OTHER, criteriaCode);
                        isCustomValue = true;
                    }
                }
                if (null != jerseyClient.IsExistingCriteriaCode(existingCriteriaSetCodeValues, srcCriteria, sourceProduct)) {
                    crntCriteriaCheck = true;
                    criteriaSetValuesAry = new CriteriaSetValues[1];
                    criteriaSetValuesAry[0] = jerseyClient.IsExistingCriteriaCode(existingCriteriaSetCodeValues, srcCriteria,
                            sourceProduct);
                } else if (null != srcCriteria && !srcCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                    crntCriteriaCheck = true;
                    criteriaSetValuesID = criteriaSetValuesID - 1;
                    child1Obj.setSetCodeValueId(srcCriteria);
                    // child1Obj.setCodeValue(criteriaCode); // "PRCL"
                    child1Obj.setCriteriaSetValueId(criteriaSetValuesID + "");
                    criteriaSetCodeValues[0] = child1Obj;
                    criteriaSetValue.setId(criteriaSetValuesID + "");

                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE)) {
                        criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_COLOR);
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE)) {
                        criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    } else if (isCustomValue) {
                        criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                        isCustomValue = false;
                    } else
                        criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE)) {
                        criteriaSetValue.setCriteriaValueDetail(actualCriteria);
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE)) {
                        criteriaSetValue.setCriteriaValueDetail(ApplicationConstants.CONST_STRING_NONE_SMALL);
                    } else
                        criteriaSetValue.setCriteriaValueDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                                .getCriteriaSetValues()[0].getCriteriaValueDetail());
                    criteriaSetValue.setIsSubset(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getIsSubset());
                    criteriaSetValue.setIsSetValueMeasurement(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getIsSetValueMeasurement());
                    criteriaSetValue.setCriteriaSetId(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getCriteriaSetId());
                    criteriaSetValue.setCriteriaSetId(String.valueOf(criteriaSetUniqueId)); // Changed TODO
                    criteriaSetValue.setCriteriaCode(criteriaCode);
                    if (actualCriteria != null
                            && actualCriteria.contains("=")
                            && (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE) || criteriaCode
                                    .equalsIgnoreCase(ApplicationConstants.CONST_COLORS_CRITERIA_CODE))) {
                        criteriaSetValue
                                .setValue(actualCriteria.substring(actualCriteria.indexOf("=") + 1, actualCriteria.length()));
                    } else {
                        criteriaSetValue.setValue(actualCriteria);
                    }

                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                        // criteriaSetValue.setFormatValue(actualCriteria);
                        criteriaSetValue.setValue(actualCriteria);
                        criteriaSetValue.setCriteriaSetCodeValues(new CriteriaSetCodeValues[] {});
                    } else
                        criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
                    /*
                     * if(criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE))
                     * {
                     * // criteriaSetValue.setFormatValue(actualCriteria);
                     * }
                     * else
                     */
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                        Value value = new Value();
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, "Unit", criteriaCode);
                            if (unitValue.contains(":")) {
                                String[] unitValueAry = unitValue.split(":");
                                String unitsCode = unitValueAry[1];
                                unitValue = unitValueAry[0];
                                String temp = unitsCode;
                                unitsCode = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, unitsCode.trim());

                                if (CommonUtilities.isValueNull(unitsCode)) { // Fix for unit other than in Lookup
                                    unitsCode = jsonProcessorObj.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                            sizeElementsResponse, ApplicationConstants.CONST_STRING_OTHER);
                                    if (criteriaSetValue.getCriteriaSetCodeValues() != null && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
                                        //criteriaSetValue.getCriteriaSetCodeValues()[0].setCodeValue(temp);
                                        value.setUnitOfMeasureCode(unitsCode);
                                        criteriaSetValue.setCriteriaValueDetail(temp);
                                    }
                                } else {
                                    value.setUnitOfMeasureCode(unitsCode);
                                }

                                value.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                        unitValue.trim()));
                                value.setUnitValue(unitValue);
                                
                                Value[] valueAry = new Value[1];
                                valueAry[0] = value;
                                criteriaSetValue.setValue(valueAry);
                            }
                        } else {
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE))
                                value.setCriteriaAttributeId("14");
                            else
                                value.setCriteriaAttributeId("13");
                            value.setUnitValue(unitValue);
                            value.setUnitOfMeasureCode("BUSI");
                            Value[] valueAry = new Value[1];
                            valueAry[0] = value;
                            criteriaSetValue.setValue(valueAry);
                            // criteriaSetValue.setFormatValue(unitValue+" DAYS");
                        }
                    }
                    if (isOtherSize) {
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)) {
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse, "Unit", criteriaCode);
                            Value valueObj = new Value();
                            // String[] unitValueAry=new String[1];
                            // String unitsCode = unitValueAry[1];
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)
                                    && !CommonUtilities.isValueNull(unitValue)) {
                                String temp = jsonProcessorObj
                                        .getSizesElementValue("Units", sizeElementsResponse, unitValue.trim());
                                valueObj.setUnitOfMeasureCode(CommonUtilities.isValueNull(temp) ? "" : temp);
                            }
                            unitValue = (unitValue == null || unitValue.trim().isEmpty()) ? actualCriteria : unitValue;

                            valueObj.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                    unitValue.trim()));
                            valueObj.setUnitValue(actualCriteria);

                            Value[] valueAry = new Value[1];
                            valueAry[0] = valueObj;
                            criteriaSetValue.setValue(valueAry);
                        }
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)
                                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE)) {
                            String[] unitValueAry = null;
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE))
                                unitValueAry = new String[] { ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_NECK,
                                        ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_SLVS };
                            else
                                unitValueAry = new String[] { ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_WAIST,
                                        ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_INSEAM };

                            Value valueObj = null;
                            Value[] valueAry = new Value[unitValueAry.length];
                            String[] untValueFnlAry = new String[unitValueAry.length];
                            String validUnit = CommonUtilities.getStringWithBrackets(actualCriteria);
                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE))
                                validUnit = CommonUtilities.getStringWithBrackets(actualCriteria);
                            else {
                                if (actualCriteria.contains("x"))
                                    validUnit = actualCriteria.substring(actualCriteria.indexOf("x") + 1, actualCriteria.length());

                            }

                            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)) {
                                if (validUnit.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FALSE_SMALL)) {
                                    untValueFnlAry = new String[] { actualCriteria, ApplicationConstants.CONST_STRING_EMPTY };
                                } else
                                    untValueFnlAry = new String[] { actualCriteria.substring(0, actualCriteria.indexOf("(")),
                                            validUnit };
                            } else {
                                if (actualCriteria.contains("x"))
                                    untValueFnlAry = new String[] { actualCriteria.substring(0, actualCriteria.indexOf("x")),
                                            validUnit };
                                else
                                    untValueFnlAry = new String[] { actualCriteria, ApplicationConstants.CONST_STRING_EMPTY };
                            }
                            for (int untValCntr = 0; untValCntr < unitValueAry.length; untValCntr++) {
                                valueObj = new Value();
                                sizeElementsResponse = jsonProcessorObj.getSizesResponse(sizesCriteriaWSResponse,
                                        unitValueAry[untValCntr], criteriaCode);
                                valueObj.setCriteriaAttributeId(jsonProcessorObj.getSizesElementValue("ID", sizeElementsResponse,
                                        unitValueAry[untValCntr]));
                                valueObj.setUnitValue(untValueFnlAry[untValCntr].trim());
                                if (!valueObj.getUnitValue().equalsIgnoreCase(ApplicationConstants.CONST_STRING_EMPTY)) {
                                    valueAry[untValCntr] = valueObj;
                                } else
                                    valueAry = Arrays.copyOf(valueAry, valueAry.length - 1);
                            }
                            criteriaSetValue.setValue(valueAry);
                        }
                        isOtherSize = false;
                    }
                    criteriaSetValue.setCriteriaCode(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getCriteriaCode());
                    criteriaSetValue.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
                    criteriaSetValuesAry = new CriteriaSetValues[1];
                    criteriaSetValuesAry[0] = criteriaSetValue;
                    // Adding a this criteriaSet entry details to reference table, so later can be referenced easily
                    if (isOptionGroup(criteriaCode) && !CommonUtilities.isValueNull(description)) {
                        String temp = processOptionNameForPriceCriteria(criteriaCode, description, orignalCriteriaValue);
                        productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId(), criteriaCode, temp,
                                criteriaSetValue.getId());
                    } else {
                        productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId(), criteriaCode,
                                orignalCriteriaValue, criteriaSetValue.getId());
                    }/*
                      * updateCriteriaSetValueReferenceTable(product.getExternalProductId(), criteriaCode, orignalCriteriaValue,
                      * criteriaSetValue.getId());
                      */
                } else {
                    // System.out.println("Criteria Code is Wrong");
                    LOGGER.info("Criteria Code " + actualCriteria + " is Invalid Name found in " + criteriaCode + " Type");
                    crntCriteriaCheck = false;
                }
            }
            // child1ObjAry[0] = child1Obj;
            if (!crntCriteriaCheck) {
                crntProductCriteriaSets = null;
            } else {
                crntProductCriteriaSets.setCriteriaSetValues(criteriaSetValuesAry);

                crntProductCriteriaSets.getCriteriaSetValues()[0].setCriteriaCode(criteriaCode);
                crntProductCriteriaSets.setCompanyId(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getCompanyId());
                crntProductCriteriaSets.setCriteriaCode(criteriaCode);

                crntProductCriteriaSets.setCriteriaSetId(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getCriteriaSetId());

                crntProductCriteriaSets.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
                crntProductCriteriaSets.setProductId(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getProductId());
                crntProductCriteriaSets
                        .setConfigId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getConfigId());
                // crntProductCriteriaSets.setDescription(product
                // .getProductConfigurations()[0].getProductCriteriaSets()[0]
                // .getDescription());
                crntProductCriteriaSets.setIsBase(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getIsBase());
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
                    crntProductCriteriaSets.setIsRequiredForOrder(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getIsRequiredForOrder());
                } else
                    crntProductCriteriaSets.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                crntProductCriteriaSets
                        .setIsMultipleChoiceAllowed(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                                .getIsMultipleChoiceAllowed());
                crntProductCriteriaSets.setIsTemplate(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getIsTemplate());
                crntProductCriteriaSets.setCriteriaDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getCriteriaDetail());
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE))
                    crntProductCriteriaSets.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_TRUE_SMALL);
                else
                    crntProductCriteriaSets.setIsDefaultConfiguration(product.getProductConfigurations()[0]
                            .getProductCriteriaSets()[0].getIsDefaultConfiguration());
                crntProductCriteriaSets.setIsBrokenOutOn(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getIsBrokenOutOn());

            }
        }
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
            crntProductCriteriaSets.setDescription("");
            crntProductCriteriaSets.setCriteriaDetail(description);
        }
        return crntProductCriteriaSets;

    }

    public Relationships createImprintArtworkRelationShip(ProductCriteriaSets[] productCriteriaSets, Product product,
            String imprintMethod, String artwork, boolean isArtwork, String relationshipId) {

        ProductCriteriaSets immdCriteriaSet = getCriteriaSetBasedOnCriteriaCode(productCriteriaSets,
                ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
        ProductCriteriaSets artwCriteriaSet = null;
        ProductCriteriaSets minOrderCriteriaSet = null;
        int criteriaSetValuePathId = -1;

        String[] imprintMethods = imprintMethod.split(","); // Repeatable imprint method value should split by $ delimiter
        String[] artworks = artwork.split("\\|"); //

        Relationships individualRelationShip = new Relationships();
        individualRelationShip.setId(relationshipId);
        if (isArtwork) {
            individualRelationShip.setName("Imprint Method x Artwork");
            artwCriteriaSet = getCriteriaSetBasedOnCriteriaCode(productCriteriaSets, ApplicationConstants.CONST_ARTWORK_CODE);
        } else {
            individualRelationShip.setName("Imprint Method x Min Order");
            minOrderCriteriaSet = getCriteriaSetBasedOnCriteriaCode(productCriteriaSets,
                    ApplicationConstants.CONST_MINIMUM_QUANTITY);
        }
        individualRelationShip.setProductId(product.getId());
        individualRelationShip.setParentCriteriaSetId(immdCriteriaSet.getCriteriaSetId());

        CriteriaSetRelationships[] criteriaSetRelationshipsAry = new CriteriaSetRelationships[2];

        criteriaSetRelationshipsAry[0] = new CriteriaSetRelationships();
        criteriaSetRelationshipsAry[0].setIsParent("true");
        criteriaSetRelationshipsAry[0].setRelationshipId(relationshipId);
        criteriaSetRelationshipsAry[0].setProductId(product.getId());
        criteriaSetRelationshipsAry[0].setCriteriaSetId(immdCriteriaSet.getCriteriaSetId());

        criteriaSetRelationshipsAry[1] = new CriteriaSetRelationships();
        criteriaSetRelationshipsAry[1].setProductId(product.getId());
        criteriaSetRelationshipsAry[1].setRelationshipId(relationshipId);
        if (isArtwork) {
            criteriaSetRelationshipsAry[1].setCriteriaSetId(artwCriteriaSet.getCriteriaSetId());
        } else {
            criteriaSetRelationshipsAry[1].setCriteriaSetId(minOrderCriteriaSet.getCriteriaSetId());
        }
        criteriaSetRelationshipsAry[1].setIsParent("false");

        individualRelationShip.setCriteriaSetRelationships(criteriaSetRelationshipsAry);

        List<CriteriaSetValuePaths> criteriaSetValuePathCollection = new ArrayList<CriteriaSetValuePaths>();
        if (isArtwork) {
            for (int i = 0; i < imprintMethods.length; i++) {
                if (/* artworks.length > i && */artworks.length > i && artworks[i] != null && !artworks[i].isEmpty()
                        && !artworks[i].trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)) {
                    String tempImprntMthd = imprintMethods[i];
                    if (tempImprntMthd != null && tempImprntMthd.contains("=")) {
                        tempImprntMthd = tempImprntMthd.substring(tempImprntMthd.indexOf("=") + 1, tempImprntMthd.length());
                    }
                    CriteriaSetValues imprintCriteriaValue = findByCriteriaValue(tempImprntMthd,
                            immdCriteriaSet.getCriteriaSetValues(), ApplicationConstants.CONST_IMPRINT_METHOD_CODE);

                    if (imprintCriteriaValue != null) {
                        String[] artworkArray = artworks[i].split(",");
                        for (int j = 0; j < artworkArray.length; j++) {
                            
                            String  artworkCriteriaValue = findCriteriaSetValueForArtwork(product, artworkArray[j]);
                            if (artworkCriteriaValue != null) {
                                CriteriaSetValuePaths criteriaSetValuePathForIMMD = new CriteriaSetValuePaths();
                                criteriaSetValuePathForIMMD.setRelationshipId(relationshipId);
                                criteriaSetValuePathForIMMD.setIsParent(ApplicationConstants.CONST_STRING_TRUE_SMALL);
                                criteriaSetValuePathForIMMD.setProductId(product.getId());
                                criteriaSetValuePathForIMMD.setCriteriaSetValueId(imprintCriteriaValue.getId());
                                criteriaSetValuePathForIMMD.setId(String.valueOf(criteriaSetValuePathId));

                                criteriaSetValuePathCollection.add(criteriaSetValuePathForIMMD);

                                CriteriaSetValuePaths criteriaSetValuePathForARTW = new CriteriaSetValuePaths();
                                criteriaSetValuePathForARTW.setRelationshipId(relationshipId);
                                criteriaSetValuePathForARTW.setIsParent(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                                criteriaSetValuePathForARTW.setProductId(product.getId());
                                criteriaSetValuePathForARTW.setCriteriaSetValueId(artworkCriteriaValue);
                                criteriaSetValuePathForARTW.setId(String.valueOf(criteriaSetValuePathId));

                                criteriaSetValuePathCollection.add(criteriaSetValuePathForARTW);
                                criteriaSetValuePathId--;
                            }
                        }
                    }
                }
            }
        } else {
            // Min-Order Relationship

            for (int i = 0; i < imprintMethods.length; i++) {
                if (artworks[i] != null && !artworks[i].isEmpty()
                        && !artworks[i].trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)) {
                    String tempImprntMthd = imprintMethods[i];
                    if (tempImprntMthd != null && tempImprntMthd.contains("=")) {
                        tempImprntMthd = tempImprntMthd.substring(tempImprntMthd.indexOf("=") + 1, tempImprntMthd.length());
                    }
                    CriteriaSetValues imprintCriteriaValue = findByCriteriaValue(tempImprntMthd,
                            immdCriteriaSet.getCriteriaSetValues(), ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
                    if (imprintCriteriaValue != null) {
                        String[] artworkArray = artworks[i].split(",");
                        for (int j = 0; j < artworkArray.length; j++) {
                            CriteriaSetValues artworkCriteriaValue = findByCriteriaValue(artworkArray[j],
                                    minOrderCriteriaSet.getCriteriaSetValues(), ApplicationConstants.CONST_MINIMUM_QUANTITY);
                            if (artworkCriteriaValue != null) {
                                CriteriaSetValuePaths criteriaSetValuePathForIMMD = new CriteriaSetValuePaths();
                                criteriaSetValuePathForIMMD.setRelationshipId(relationshipId);
                                criteriaSetValuePathForIMMD.setIsParent(ApplicationConstants.CONST_STRING_TRUE_SMALL);
                                criteriaSetValuePathForIMMD.setProductId(product.getId());
                                criteriaSetValuePathForIMMD.setCriteriaSetValueId(imprintCriteriaValue.getId());
                                criteriaSetValuePathForIMMD.setId(String.valueOf(criteriaSetValuePathId));

                                criteriaSetValuePathCollection.add(criteriaSetValuePathForIMMD);

                                CriteriaSetValuePaths criteriaSetValuePathForMINO = new CriteriaSetValuePaths();
                                criteriaSetValuePathForMINO.setRelationshipId(relationshipId);
                                criteriaSetValuePathForMINO.setIsParent(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                                criteriaSetValuePathForMINO.setProductId(product.getId());
                                criteriaSetValuePathForMINO.setCriteriaSetValueId(artworkCriteriaValue.getId());
                                criteriaSetValuePathForMINO.setId(String.valueOf(criteriaSetValuePathId));

                                criteriaSetValuePathCollection.add(criteriaSetValuePathForMINO);
                                criteriaSetValuePathId--;

                            }
                        }
                    }
                }
            }
        }
        if (criteriaSetValuePathCollection != null && !criteriaSetValuePathCollection.isEmpty()) {
            individualRelationShip.setCriteriaSetValuePaths(criteriaSetValuePathCollection.toArray(new CriteriaSetValuePaths[0]));
        } else {
            individualRelationShip = null;
        }

        return individualRelationShip;
    }

    @SuppressWarnings("unchecked")
    private CriteriaSetValues findByCriteriaValue(String criteriaValue, CriteriaSetValues[] values, String criteriaCode) {
        if (criteriaValue != null && !criteriaValue.isEmpty()) {
            for (int i = 0; i < values.length; i++) {
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY)) {

                    // Value v = (Value) values[i].getValue();
                    ArrayList<LinkedHashMap<String, String>> temp = new ArrayList<>();
                    LinkedHashMap<String, String> tempMap = null;
                    if (values[i].getValue() instanceof ArrayList) {
                        temp = (ArrayList<LinkedHashMap<String, String>>) values[i].getValue();
                        tempMap = (LinkedHashMap<String, String>) temp.get(0);
                        if (tempMap != null && tempMap.size() > 0) {
                            String[] tempValueAry = criteriaValue.split(":");
                            if (tempValueAry.length > 1
                                    && tempMap.get("UnitValue").equalsIgnoreCase(tempValueAry[0])
                                    && tempMap.get("UnitOfMeasureCode").equalsIgnoreCase(
                                            jsonProcessorObj.getSizesElementValue("UNITS", sizeElementsResponse,
                                                    tempValueAry[1].trim()))) {
                                return values[i];
                            }
                        }
                    } else {
                        Value[] v = (Value[]) values[i].getValue();
                        if (v != null && v.length > 0) {
                            String[] tempValueAry = criteriaValue.split(":");
                            if (tempValueAry.length > 1
                                    && v[0].getUnitValue().equalsIgnoreCase(tempValueAry[0])
                                    && v[0].getUnitOfMeasureCode().equalsIgnoreCase(
                                            jsonProcessorObj.getSizesElementValue("UNITS", sizeElementsResponse,
                                                    tempValueAry[1].trim()))) {
                                return values[i];
                            }
                        }
                    }

                } else {
                    if (values[i] != null && values[i].getValue() != null
                            && String.valueOf(values[i].getValue()).equalsIgnoreCase(criteriaValue)) {
                        return values[i];
                    }
                }
            }
        }
        return null;
    }

    private String findCriteriaSetValueForArtwork(Product product, String artworkName) {
        String criteriaSetValueId = productDataStore.findCriteriaSetValueIdForValue(product.getExternalProductId().trim(),
                ApplicationConstants.CONST_ARTWORK_CODE, String.valueOf(artworkName).trim());
        return criteriaSetValueId;
    }
    public ProductKeywords[] comapreAndUpdateKeywords(ProductKeywords[] currentProductKeywords,
            ProductKeywords[] existingProductKeywords) {

        if (existingProductKeywords != null && existingProductKeywords.length > 0) {
            for (int currentPrdKeywdCount = 0; currentPrdKeywdCount < currentProductKeywords.length; currentPrdKeywdCount++) {
                for (int extPrdKeywdCount = 0; extPrdKeywdCount < existingProductKeywords.length; extPrdKeywdCount++) {
                    if (currentProductKeywords[currentPrdKeywdCount].getValue().equalsIgnoreCase(
                            existingProductKeywords[extPrdKeywdCount].getValue())
                            && currentProductKeywords[currentPrdKeywdCount].getTypeCode().equalsIgnoreCase(
                                    existingProductKeywords[extPrdKeywdCount].getTypeCode())) {
                        currentProductKeywords[currentPrdKeywdCount] = existingProductKeywords[extPrdKeywdCount];
                        break;
                    }
                }
            }
            return currentProductKeywords;
        } else {
            return currentProductKeywords;
        }
    }

    public SelectedProductCategories[] compareAndUpdateSelectedProductCategories(
            SelectedProductCategories[] currentSelectedPrdtCatg, SelectedProductCategories[] extSelectedPrdtCatg) {

        if (extSelectedPrdtCatg != null && extSelectedPrdtCatg.length > 0) {
            for (int crntSelPrdCtgCount = 0; crntSelPrdCtgCount < currentSelectedPrdtCatg.length; crntSelPrdCtgCount++) {
                for (int extSelPrdCtgCount = 0; extSelPrdCtgCount < extSelectedPrdtCatg.length; extSelPrdCtgCount++) {
                    if (currentSelectedPrdtCatg[crntSelPrdCtgCount].getCode().equalsIgnoreCase(
                            extSelectedPrdtCatg[extSelPrdCtgCount].getCode())) {
                        currentSelectedPrdtCatg[crntSelPrdCtgCount] = extSelectedPrdtCatg[extSelPrdCtgCount];
                    }
                }
            }
            return currentSelectedPrdtCatg;
        } else {
            return currentSelectedPrdtCatg;
        }
    }

    static String elementIndex = null;

    public Relationships compareAndUpdateRelationship(Relationships[] existingRelationships, Relationships newRelationship,
            String relationshipType, ProductCriteriaSets[] productCriteriaSetsAry) {

        if (existingRelationships != null && existingRelationships.length > 0) {
            
            Relationships matchedRelationShip = getRelationshipBasedOnType(existingRelationships, relationshipType,
                    productCriteriaSetsAry);
            if (matchedRelationShip == null) {
                return newRelationship;
            }

            CriteriaSetRelationships newParentRelationShip = getParentCriteriaSetRelationship(newRelationship
                    .getCriteriaSetRelationships());
            CriteriaSetRelationships existingParentSetRelationships = getParentCriteriaSetRelationship(matchedRelationShip
                    .getCriteriaSetRelationships());

            if (newParentRelationShip == null) { // TODO : add more conditions to validate relationship later
                return null;
            }
            newRelationship.setId(matchedRelationShip.getId());

            if (existingParentSetRelationships == null) {
                return newRelationship;
            }
            if (newParentRelationShip.getCriteriaSetId().equalsIgnoreCase(existingParentSetRelationships.getCriteriaSetId())) {
                newRelationship.setId(matchedRelationShip.getId());
                newRelationship.setCriteriaSetRelationships(matchedRelationShip.getCriteriaSetRelationships());
                List<String> processedCriteriaSetValuePathIds = new ArrayList<>();
                // Now check criteria set value paths
                List<CriteriaSetValuePaths> criteriaSetValuePathsList = new ArrayList<CriteriaSetValuePaths>();
                for (int i = 0; i < newRelationship.getCriteriaSetValuePaths().length
                        && newRelationship.getCriteriaSetValuePaths() != null; i++) {

                    if (newRelationship.getCriteriaSetValuePaths()[i] != null) {

                        CriteriaSetValuePaths currentCrtValuePathParent = null;
                        CriteriaSetValuePaths currentCrtValuePathChild = null;
                        if (newRelationship.getCriteriaSetValuePaths()[i] != null
                                && newRelationship.getCriteriaSetValuePaths()[i].getIsParent().equalsIgnoreCase(
                                        ApplicationConstants.CONST_STRING_TRUE_SMALL)) {
                            currentCrtValuePathParent = newRelationship.getCriteriaSetValuePaths()[i];

                            currentCrtValuePathChild = getCriteriaSetValuePathById(newRelationship.getCriteriaSetValuePaths(),
                                    currentCrtValuePathParent.getId(), ApplicationConstants.CONST_STRING_FALSE_SMALL);
                        } else {
                            currentCrtValuePathChild = newRelationship.getCriteriaSetValuePaths()[i];
                            currentCrtValuePathParent = getCriteriaSetValuePathById(newRelationship.getCriteriaSetValuePaths(),
                                    currentCrtValuePathChild.getId(), ApplicationConstants.CONST_STRING_TRUE_SMALL);
                        }
                        CriteriaSetValuePaths extCrtValuePathParent = null;
                        CriteriaSetValuePaths extCrtValuePathChild = null;
                        if (currentCrtValuePathParent != null && currentCrtValuePathChild != null) {
                            currentCrtValuePathParent.setRelationshipId(newRelationship.getId());
                            currentCrtValuePathChild.setRelationshipId(newRelationship.getId());
                            extCrtValuePathParent = getCriteriaSetValuePathFromExistingList(
                                    matchedRelationShip.getCriteriaSetValuePaths(),
                                    currentCrtValuePathParent.getCriteriaSetValueId(), ApplicationConstants.CONST_STRING_TRUE_SMALL);

                            extCrtValuePathChild = getCriteriaSetValuePathFromExistingList(
                                    matchedRelationShip.getCriteriaSetValuePaths(),
                                    currentCrtValuePathChild.getCriteriaSetValueId(), ApplicationConstants.CONST_STRING_FALSE_SMALL);

                            if (extCrtValuePathParent != null && extCrtValuePathChild != null
                                    && extCrtValuePathParent.getId().equalsIgnoreCase(extCrtValuePathChild.getId())) {
                                criteriaSetValuePathsList.add(extCrtValuePathParent);
                                criteriaSetValuePathsList.add(extCrtValuePathChild);
                                int matchedExtPrntIndex = ArrayUtils.indexOf(matchedRelationShip.getCriteriaSetValuePaths(),
                                        extCrtValuePathParent);
                                matchedRelationShip.getCriteriaSetValuePaths()[matchedExtPrntIndex] = null; // Remove the element
                                                                                                            // from the array, other
                                                                                                            // it will conflict
                            } else {
                                criteriaSetValuePathsList.add(currentCrtValuePathParent);
                                criteriaSetValuePathsList.add(currentCrtValuePathChild);
                            }
                            newRelationship.getCriteriaSetValuePaths()[i] = null;
                            int index = ArrayUtils.indexOf(newRelationship.getCriteriaSetValuePaths(), currentCrtValuePathChild);
                            newRelationship.getCriteriaSetValuePaths()[index] = null;
                        }
                    }
                }

                if (criteriaSetValuePathsList.size() > 1) {
                    newRelationship.setCriteriaSetValuePaths(criteriaSetValuePathsList.toArray(new CriteriaSetValuePaths[0]));
                    
                } else {
                    // Invalid Relationship set
                    return null;
                }
            }
            return newRelationship;
        } else {
            return newRelationship;
        }
        // Iterate over each existing CriteriaSetRelationships
    }

     private Relationships getRelationshipBasedOnType(Relationships[] relationships, String type,
            ProductCriteriaSets[] productCriteriaSets) {
        if (type.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
            return productCompareUtil.getRelationshipBasedOnCriteriaCodes(relationships,
                    ApplicationConstants.CONST_IMPRINT_METHOD_CODE, ApplicationConstants.CONST_ARTWORK_CODE, productCriteriaSets);
        } else if (type.equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY)) {
            return productCompareUtil.getRelationshipBasedOnCriteriaCodes(relationships,
                    ApplicationConstants.CONST_IMPRINT_METHOD_CODE, ApplicationConstants.CONST_MINIMUM_QUANTITY,
                    productCriteriaSets);
        } else {
            return null;
        }
    }

    private CriteriaSetRelationships getParentCriteriaSetRelationship(CriteriaSetRelationships[] relationships) {
        for (int i = 0; i < relationships.length; i++) {
            if (relationships[i] != null
                    && relationships[i].getIsParent().equalsIgnoreCase(ApplicationConstants.CONST_STRING_TRUE_CAP)) {
                return relationships[i];
            }
        }
        return null;
    }

    private CriteriaSetValuePaths getCriteriaSetValuePathFromExistingList(CriteriaSetValuePaths[] exiCriteriaSetValuePaths,
            String criteriaSetValueId, String isParent) {
        if (exiCriteriaSetValuePaths != null && exiCriteriaSetValuePaths.length > 0) {
            for (int i = 0; i < exiCriteriaSetValuePaths.length; i++) {
                if (exiCriteriaSetValuePaths[i] != null
                        && exiCriteriaSetValuePaths[i].getCriteriaSetValueId().equalsIgnoreCase(criteriaSetValueId)
                        && exiCriteriaSetValuePaths[i].getIsParent().equalsIgnoreCase(isParent)) {
                    return exiCriteriaSetValuePaths[i];
                }
            }
            return null;
        } else {
            return null;
        }
    }

    private CriteriaSetValuePaths getCriteriaSetValuePathById(CriteriaSetValuePaths[] exiCriteriaSetValuePaths, String elementId,
            String isParent) {
        if (exiCriteriaSetValuePaths != null && exiCriteriaSetValuePaths.length > 0) {
            for (int i = 0; i < exiCriteriaSetValuePaths.length; i++) {
                if (exiCriteriaSetValuePaths[i] != null && exiCriteriaSetValuePaths[i].getId().equalsIgnoreCase(elementId)
                        && exiCriteriaSetValuePaths[i].getIsParent().equalsIgnoreCase(isParent)) {
                    elementIndex = String.valueOf(i);
                    return exiCriteriaSetValuePaths[i];
                }
            }
            return null;
        } else {
            return null;
        }
    }


    protected static boolean isOptionGroup(String criteriaCode) {
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
            return true;
        } else {
            return false;
        }
    }

    public static String processOptionNameForPriceCriteria(String criteriaCode, String optName, String optValue) {
        return optName + ":" + optValue;
    }

    public String processSourceCriteriaValueByCriteriaCode(String sourceValue, String criteriaCode) {
        if (!CommonUtilities.isValueNull(sourceValue)) {
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                if (sourceValue.contains("\\|")) {
                    
                }
                sourceValue = sourceValue.replace("|", "-").trim();                    
                return sourceValue;
            } else if (sourceValue.contains("=")) {
                String elements[] = sourceValue.split("=");
                return elements.length > 1 ? elements[1] : elements[0];
            } else {
                return sourceValue.trim();
            }
        } else {
            return sourceValue;
        }
    }
    
    public static List<String> getValuesFromCriteriaValues(CriteriaSetValues[] criteriaSetValues)  {
        List<String> tempValues = new ArrayList<String>();
        if (criteriaSetValues != null) {
            for (int i = 0; i < criteriaSetValues.length; i++) {
                if (criteriaSetValues[i] != null && criteriaSetValues[i].getValue() instanceof String) {
                    tempValues.add((String) criteriaSetValues[i].getValue());
                }
            }
        }
        return tempValues;
    }
    
    public HashMap<String, Object> updateProductCriteriaSetArray(ProductCriteriaSets[] currentProductCriteriaSets, Integer counter,
            String criteriaCode, Product extProduct) {
        
        if (criteriaCode != null && extProduct != null) {
            ProductCriteriaSets[] productCriteriaSets = getCriteriaSetsBasedOnCriteriaCode(
                    extProduct.getProductConfigurations()[0].getProductCriteriaSets(), criteriaCode);
            if (productCriteriaSets != null && productCriteriaSets.length > 0) {
                productCompareUtil.registerExitingCriteriaValuesToReferenceTable(extProduct.getExternalProductId(), criteriaCode,
                        productCriteriaSets);
                int tempCount = 0;
                for (int i = 0; i < productCriteriaSets.length; i++) {
                    currentProductCriteriaSets[counter++] = productCriteriaSets[i]; // counter value will be updated by java
                                                                                    // reference
                    tempCount++;                    
                }
                
                if (tempCount > 1) { // then we need resize array
                    currentProductCriteriaSets = Arrays.copyOf(currentProductCriteriaSets, currentProductCriteriaSets.length
                            + tempCount - 1);
                }
            }
        }
        HashMap<String, Object> prdCrtMap = new HashMap<>();
        prdCrtMap.put("CriteriaSet", currentProductCriteriaSets);
        prdCrtMap.put("Counter", counter);
        
        return prdCrtMap;
    }
    
    public HashMap<String, Object> updateProductOptionCriteriaSets(ProductCriteriaSets[] currentProductCriteriaSets, Integer count,
            Product extProduct) {
        HashMap<String, Object> tempMap = new HashMap<>();
        tempMap.put("CriteriaSet", currentProductCriteriaSets);
        tempMap.put("Counter", count);
        if (extProduct != null) {
            tempMap = updateProductCriteriaSetArray(currentProductCriteriaSets, count, ApplicationConstants.CONST_PRODUCT_OPTION,
                    extProduct);
            tempMap = updateProductCriteriaSetArray((ProductCriteriaSets[]) tempMap.get("CriteriaSet"),
                    (Integer) tempMap.get("Counter"), ApplicationConstants.CONST_SHIPPING_OPTION, extProduct);
            tempMap = updateProductCriteriaSetArray((ProductCriteriaSets[]) tempMap.get("CriteriaSet"),
                    (Integer) tempMap.get("Counter"), ApplicationConstants.CONST_IMPRINT_OPTION, extProduct);
        }
        return tempMap;
    }
}
