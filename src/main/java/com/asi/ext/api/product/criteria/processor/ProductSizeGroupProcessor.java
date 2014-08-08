package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.product.transformers.JerseyClientPost;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.Product;

import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.service.model.Apparel;
import com.asi.ext.api.service.model.Capacity;
import com.asi.ext.api.service.model.ProductionTime;
import com.asi.ext.api.service.model.ShippingEstimate;
import com.asi.ext.api.service.model.Size;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.ProductParserUtil;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Value;

public class ProductSizeGroupProcessor extends SimpleCriteriaProcessor {
    private final static Logger  LOGGER                      = Logger.getLogger(ProductSizeGroupProcessor.class.getName());

    public final static String[] SIZE_GROUP_CRITERIACODES    = { "CAPS", "DIMS", "SABR", "SAHU", "SAIT", "SANS", "SAWI", "SSNM",
            "SVWT", "SOTH"                                  };

    private String               sizesWSResponse             = null;
    @SuppressWarnings("rawtypes")
    private HashMap              sizeElementsResponse        = null;
    private String               sizesCriteriaWSResponse     = null;
    private String               sizesShippingDimsWSResponse = null;
    private String               optionsProdWSResponse       = null;

    private String               companyId                   = "0";
    private String               configId                    = "0";

    private String               criteriaSetId               = "0";

    private String               productId;

    /**
     * @param companyId
     * @param productId
     * @param configId
     */
    public ProductSizeGroupProcessor(String criteriaSetId) {
        this.criteriaSetId = criteriaSetId;
    }

    public Map<String, ProductCriteriaSets> getProductCriteriaSet(Size size, ProductDetail product,
            Map<String, ProductCriteriaSets> existingCriteriaSetMap, String configId) {
        this.productId = product.getID();
        this.companyId = product.getCompanyId();
        this.configId = configId;

        String criteriaCode = findCriteriaCodeForSizeModel(size, product.getExternalProductId());

        if (CommonUtilities.isValueNull(criteriaCode)) {
            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                    ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Unable to identify the given size details");
            return existingCriteriaSetMap;
        }
        String sizeValues = ProductParserUtil.getSizeValuesFromSize(size, criteriaCode);

        if (CommonUtilities.isValueNull(sizeValues)) {
            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                    ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Unable to read the given size details");
            return existingCriteriaSetMap;
        }

        ProductCriteriaSets tempCriteriaSet = getSizeCriteriaSet(sizeValues, criteriaCode,
                existingCriteriaSetMap.get(criteriaCode), product);

        if (tempCriteriaSet != null && tempCriteriaSet.getCriteriaSetValues() != null
                && !tempCriteriaSet.getCriteriaSetValues().isEmpty()) {
            // For the reference
            productDataStore.registerSizeGroupOfProduct(criteriaCode, product.getExternalProductId());
            ProductCriteriaSets exisitingCriteriaSet = existingCriteriaSetMap.get(criteriaCode);
            if (exisitingCriteriaSet == null) {
                existingCriteriaSetMap = removeSizeRelatedCriteriaSetFromExisting(existingCriteriaSetMap);
                exisitingCriteriaSet = compareAndUpdateSizeGroup(product, tempCriteriaSet, exisitingCriteriaSet);
            } else {
                exisitingCriteriaSet = compareAndUpdateSizeGroup(product, tempCriteriaSet, exisitingCriteriaSet);
            }
            if (exisitingCriteriaSet != null) {
                existingCriteriaSetMap.put(criteriaCode, exisitingCriteriaSet);
            }
        } else {
            // for clean up
            existingCriteriaSetMap = removeSizeRelatedCriteriaSetFromExisting(existingCriteriaSetMap);
        }

        return existingCriteriaSetMap;
    }

    public boolean isSizeNull(Size size) {
        if (size == null
                || (size.getApparel() == null && size.getDimension() == null && size.getCapacity() == null
                        && size.getVolume() == null && size.getOther() == null)) {
            return true;
        } else {
            return false;
        }
    }

    private String findCriteriaCodeForSizeModel(Size size, String xid) {
        String criteriaCode = null;

        boolean sizeCodeFound = false;
        if (size.getApparel() != null && size.getApparel().getType() != null && size.getApparel().getValues() != null
                && !size.getApparel().getValues().isEmpty()) {
            criteriaCode = findCriteriaCodeFromApparalType(size.getApparel());
            if (criteriaCode != null) {
                sizeCodeFound = true;
            }
        }

        if (size.getCapacity() != null && size.getCapacity().getValues() != null && !size.getCapacity().getValues().isEmpty()) {
            if (!sizeCodeFound) {
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_CAPACITY;
                sizeCodeFound = true;
            } else {
                productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                        "Morethan one size group specified");
                return criteriaCode;
            }
        }

        if (size.getDimension() != null && size.getDimension().getValues() != null && !size.getDimension().getValues().isEmpty()) {
            if (!sizeCodeFound) {
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_DIMENSION;
                sizeCodeFound = true;
            } else {
                productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                        "Morethan one size group specified");
                return criteriaCode;
            }
        }

        if (size.getVolume() != null && size.getVolume().getValues() != null && !size.getVolume().getValues().isEmpty()) {
            if (!sizeCodeFound) {
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI;
                sizeCodeFound = true;
            } else {
                productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                        "Morethan one size group specified");
                return criteriaCode;
            }
        }

        if (size.getOther() != null && size.getOther().getValues() != null && !size.getVolume().getValues().isEmpty()) {
            if (!sizeCodeFound) {
                criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI;
            } else {
                productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                        "Morethan one size group specified");
                return criteriaCode;
            }
        }

        return criteriaCode;
    }

    private String findCriteriaCodeFromApparalType(Apparel apparel) {
        String criteriaCode = null;
        if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_INFANT_TODDLER)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_DRESS_SHIRT_SIZES)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_PANTS_SIZES)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_BRA_SIZES)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD_NUMBERED)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM;
        }
        return criteriaCode;
    }

    private String findSizeGroupApparalType(Apparel apparel) {
        String criteriaCode = null;
        if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_INFANT_TODDLER)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_DRESS_SHIRT_SIZES)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_PANTS_SIZES)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_BRA_SIZES)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA;
        } else if (apparel.getType().equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD_NUMBERED)) {
            criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM;
        }
        return criteriaCode;
    }

    private ProductCriteriaSets getSizeCriteriaSet(String sizeValues, String sizeCriteriaCode,
            ProductCriteriaSets matchedCriteriaSet, ProductDetail product) {
        boolean checkForExisting = (matchedCriteriaSet != null);
        ProductCriteriaSets newCriteriaSet = new ProductCriteriaSets();
        if (!checkForExisting) {
            // Create new one
            newCriteriaSet = new ProductCriteriaSets();
            newCriteriaSet.setProductId(product.getID());
            newCriteriaSet.setConfigId(configId);
            newCriteriaSet.setCriteriaSetId(criteriaSetId);
            newCriteriaSet.setCriteriaCode(sizeCriteriaCode);
            newCriteriaSet.setCompanyId(companyId);
            newCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            newCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            newCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        } else {
            this.criteriaSetId = matchedCriteriaSet.getCriteriaSetId();

            newCriteriaSet.setProductId(product.getID());
            newCriteriaSet.setConfigId(configId);
            newCriteriaSet.setCriteriaSetId(criteriaSetId);
            newCriteriaSet.setCriteriaCode(sizeCriteriaCode);
            newCriteriaSet.setCompanyId(companyId);
            newCriteriaSet.setIsBase(matchedCriteriaSet.getIsBase());
            newCriteriaSet.setIsRequiredForOrder(matchedCriteriaSet.getIsRequiredForOrder());
            newCriteriaSet.setIsDefaultConfiguration(matchedCriteriaSet.getIsDefaultConfiguration());
        }

        newCriteriaSet.setCriteriaSetValues(getCriteriaSetValues(product, sizeCriteriaCode, sizeValues));

        return newCriteriaSet;
    }

    private List<CriteriaSetValues> getCriteriaSetValues(ProductDetail product, String sizeCriteriaCode, String sizeValues) {

        List<CriteriaSetValues> criteriaSetValues = new ArrayList<CriteriaSetValues>();
        // Dimensions Parsing Starts Here
        try {
            if (null != sizeCriteriaCode && !sizeCriteriaCode.trim().equals("")
                    && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                LOGGER.info("SizeGroups Transformation Starts :" + sizeCriteriaCode);
                if (!sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                        && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)
                        && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)
                        && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)
                        && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) {
                    if (!sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                            && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)
                            && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)
                            && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE)
                            && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                            && !sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM))
                        sizeCriteriaCode = ApplicationConstants.CONST_SIZE_OTHER_CODE;
                    if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM))
                        sizeValues = sizeValues.toUpperCase();
                    if (null != sizeValues && !sizeValues.trim().equals("")) {
                        criteriaSetValues = addCriteriaSetForApparals(product, sizeCriteriaCode, sizeValues);
                    }
                } else if (!sizeCriteriaCode.equals("")) {
                    String[] tmpSizeValuesAry = sizeValues.split(":");
                    if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                            && tmpSizeValuesAry.length > 3) {
                        criteriaSetValues = addCriteriaSetForSizes(sizeValues, product, sizeCriteriaCode);
                    } else if (tmpSizeValuesAry.length > 1) {
                        criteriaSetValues = addCriteriaSetForSizes(sizeValues, product, sizeCriteriaCode);
                    }

                }
                LOGGER.info("SizeGroups Transformation Ends");
            }
        } catch (Exception e) {
            LOGGER.error("Excepiton while processing SIZE CriteriaSetValues", e);
        }

        return criteriaSetValues;

    }

    public List<CriteriaSetValues> addCriteriaSetForSizes(String productSizes, ProductDetail product, String sizeCriteriaCode)
            throws VelocityException {

        List<CriteriaSetValues> criteriaSetValuesAry = new ArrayList<CriteriaSetValues>();

        Value[] valueAry = null;

        if (null != productSizes && !productSizes.trim().equals("")) {
            // If Product has any Size Groups

            // length:9:in;width:9:in;height:9:in,length:23:cm,arc:23:in
            String[] individualSizes = productSizes.split(","); // Checking CSV
            String attribute = null;
            String sizeValue = null;
            String units = null;
            // criteriaSetCodeValuesAry = new CriteriaSetCodeValues[1];
            LOGGER.info("Individual Size (All Sizes):" + individualSizes);

            String initialUnits = "";
            String tempValueElement = null;

            for (int criteriaSetValuesCntr = 0; criteriaSetValuesCntr < individualSizes.length; criteriaSetValuesCntr++) {

                if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                        && !CommonUtilities.isValidDimension(individualSizes[criteriaSetValuesCntr])) {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid format/value for Dimension ");
                    continue;
                } else if ((sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI) || sizeCriteriaCode
                        .equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY))
                        && !CommonUtilities.isValidCapacity(individualSizes[criteriaSetValuesCntr].trim())) {
                    String temp = sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY) ? "Capacity"
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
                    if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {
                        if (valueElementsCntr == 0)
                            tempValueElement = "Length:" + tempValueElement;
                        else if (valueElementsCntr == 1)
                            tempValueElement = "Width:" + tempValueElement;
                        else if (valueElementsCntr == 2) tempValueElement = "Height:" + tempValueElement;

                    }
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
                    if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY))
                        initialUnits = sizeValue;
                    if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)) {
                        initialUnits = sizeValue;
                        sizeValue = attribute;
                        if (null == sizesCriteriaWSResponse) {
                            sizesCriteriaWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                        }
                        sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse,
                                ApplicationConstants.CONST_STRING_VOLUME, sizeCriteriaCode);
                        attribute = JsonProcessor.getSizesElementValue("ID", sizeElementsResponse, attribute);
                        units = JsonProcessor.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS, sizeElementsResponse,
                                initialUnits.trim());
                        if (units.equals("")) {
                            sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse,
                                    ApplicationConstants.CONST_STRING_WEIGHT, sizeCriteriaCode);
                            attribute = JsonProcessor.getSizesElementValue("ID", sizeElementsResponse, attribute);
                            units = JsonProcessor.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                    sizeElementsResponse, initialUnits.trim());

                        }
                    } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                            || sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {

                        if (null != attribute && null != sizeValue && null != units) {

                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse, attribute,
                                    sizeCriteriaCode);

                            attribute = JsonProcessor.getSizesElementValue("ID", sizeElementsResponse, attribute);
                            if (units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_INCH_SHORT_SMALL)
                                    || units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_INCH_SMALL)) units = "\"";
                            if (units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FEET_SHORT_SMALL)
                                    || units.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FEET_SMALL)) units = "\'";
                            units = JsonProcessor.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                    sizeElementsResponse, units.trim());
                        }
                    } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)
                            || sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) {
                        if (null == sizesCriteriaWSResponse) {
                            sizesCriteriaWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                        }
                        if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) {
                            sizeElementsResponse = JsonProcessor
                                    .getSizesResponse(sizesCriteriaWSResponse, "Unit", sizeCriteriaCode);
                        } else {
                            sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse,
                                    ApplicationConstants.CONST_STRING_CAPACITY, sizeCriteriaCode);
                        }
                        if (null != sizeValue) {
                            units = JsonProcessor.getSizesElementValue("UNITS", sizeElementsResponse, sizeValue.trim());
                            sizeValue = attribute;
                            attribute = JsonProcessor.getSizesElementValue("ID", sizeElementsResponse,
                                    ApplicationConstants.CONST_STRING_CAPACITY);
                        }
                    }
                    Value value = new Value();
                    value.setCriteriaAttributeId(attribute);
                    value.setUnitValue(sizeValue);
                    value.setUnitOfMeasureCode(units);
                    valueAry[valueElementsCntr] = value;
                }

                CriteriaSetCodeValues criteriaSetCodeValuesNew = new CriteriaSetCodeValues();
                String criteriaSetValueId = "";
                if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)) {
                    if (null == sizesShippingDimsWSResponse) {
                        sizesShippingDimsWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.SIZE_GROUP_SHIPPING_DIMENSION_LOOKUP));
                    }
                    criteriaSetValueId = JsonProcessor.checkImprintArtWorkValueKeyPair(sizesShippingDimsWSResponse, "Other",
                            ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);

                    // criteriaSetValueId=ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION_VAL_ID;
                } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT)) { // SIZE_GROUP_SHIPPING_WGHT_LOOKUP
                    // criteriaSetValueId=ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT_VAL_ID;
                    if (null == sizesShippingDimsWSResponse) {
                        sizesShippingDimsWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                .get(ApplicationConstants.SIZE_GROUP_SHIPPING_DIMENSION_LOOKUP));
                    }
                    criteriaSetValueId = JsonProcessor.checkImprintArtWorkValueKeyPair(sizesShippingDimsWSResponse, "Other",
                            ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);

                } else
                    criteriaSetValueId = JsonProcessor.getSizesElementValue("CRITERIASETID", sizeElementsResponse, initialUnits);
                criteriaSetCodeValuesNew.setSetCodeValueId(criteriaSetValueId);
                CriteriaSetValues criteriaSetValueNew = new CriteriaSetValues();
                criteriaSetValueNew.setId(ApplicationConstants.CONST_STRING_ZERO);
                criteriaSetValueNew.setCriteriaCode(sizeCriteriaCode);
                if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)
                        || sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)
                        || sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION)
                        || sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT))
                    criteriaSetValueNew.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                else
                    criteriaSetValueNew.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                criteriaSetValueNew.setCriteriaValueDetail(ApplicationConstants.CONST_STRING_NONE_SMALL);
                criteriaSetCodeValuesNew.setId(ApplicationConstants.CONST_STRING_ZERO);

                criteriaSetValueNew.setId(--uniqueSetValueId + "");

                criteriaSetCodeValuesNew.setCriteriaSetValueId(uniqueSetValueId + "");

                criteriaSetValueNew.setCriteriaSetId(criteriaSetId);
                CriteriaSetCodeValues[] criteriaSetCodeValuesAryNew = new CriteriaSetCodeValues[1];
                criteriaSetCodeValuesAryNew[0] = criteriaSetCodeValuesNew;
                // criteriaSetCodeValuesAry[0].setCriteriaSetValueId(criteriaSetValue.getId());
                criteriaSetValueNew.setCriteriaSetCodeValues(criteriaSetCodeValuesAryNew);
                criteriaSetValueNew.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValueNew.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValueNew.setValue(valueAry);
                // TODO : Set ReferenceTable
                // Adding a this criteriaSet entry details to reference table, so later can be referenced easily
                productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), sizeCriteriaCode,
                        processSourceCriteriaValueByCriteriaCode(individualSizes[criteriaSetValuesCntr], sizeCriteriaCode),
                        criteriaSetValueNew.getId());
                if (criteriaSetValueNew != null) {
                    criteriaSetValuesAry.add(criteriaSetValueNew);
                }

            }

        } // End of product sizes if condition

        return criteriaSetValuesAry;

    }

    public List<CriteriaSetValues> addCriteriaSetForApparals(ProductDetail product, String criteriaCode, String srcCriteria)
            throws VelocityException {
        String initSizeGroup = criteriaCode;
        criteriaCode = criteriaCode.trim();
        srcCriteria = srcCriteria.trim();
        boolean isCustomValue = false;
        boolean isOtherSize = false;

        /*
         * if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_INFANT_TODDLER)) {
         * criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR;
         * } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_DRESS_SHIRT_SIZES)) {
         * criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE;
         * } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_PANTS_SIZES)) {
         * criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE;
         * } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM)) {
         * criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM;
         * srcCriteria = srcCriteria.toUpperCase();
         * } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_BRA_SIZES)) {
         * criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA;
         * } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD_NUMBERED)) {
         * criteriaCode = ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM;
         * }
         */

        List<CriteriaSetValues> criteriaSetValuesList = new ArrayList<CriteriaSetValues>();
        CriteriaSetCodeValues[] criteriaSetCodeValues = null;
        String unitValue = "";
        String actualCriteria = srcCriteria;

        isCustomValue = false;
        String[] criteriaElements = srcCriteria.split(",");

        int cntr = 0;

        for (String curntCriteria : criteriaElements) {
            String orignalCriteriaValue = processSourceCriteriaValueByCriteriaCode(curntCriteria, criteriaCode);
            isCustomValue = false;
            curntCriteria = curntCriteria != null ? curntCriteria.trim() : curntCriteria;
            actualCriteria = curntCriteria;
            CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
            criteriaSetValue.setCriteriaSetId(criteriaSetId);
            CriteriaSetCodeValues child1Obj = new CriteriaSetCodeValues();
            criteriaSetCodeValues = new CriteriaSetCodeValues[1];
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)) {
                curntCriteria = curntCriteria.toUpperCase();
            }
            if (initSizeGroup.contains(ApplicationConstants.CONST_STRING_APPAREL)
                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM)
                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)
                    || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)) {
                String tempCriteria = curntCriteria;
                if (null == sizesWSResponse) {
                    sizesWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                            .get(ApplicationConstants.SIZES_LOOKUP_URL));
                }
                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)
                        && !(curntCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_CUSTOM) || curntCriteria
                                .equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD))) {
                    curntCriteria = JsonProcessor.checkSizesKeyValuePair(sizesWSResponse,
                            ApplicationConstants.CONST_STRING_OTHER_SIZES, criteriaCode);
                    isCustomValue = true;
                } else {
                    curntCriteria = JsonProcessor.checkSizesKeyValuePair(sizesWSResponse, curntCriteria, criteriaCode);
                }
                if (curntCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE)) {
                        isOtherSize = true;
                        curntCriteria = JsonProcessor.checkOtherSizesKeyValuePair(sizesWSResponse,
                                ApplicationConstants.CONST_STRING_OTHER, criteriaCode);
                        isCustomValue = true;
                    }
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)) {
                        isOtherSize = true;
                        if (tempCriteria.toLowerCase().contains("month")) {
                            tempCriteria = tempCriteria.toLowerCase();
                            tempCriteria = tempCriteria.substring(0, tempCriteria.indexOf("m"));
                            unitValue = "months";
                            actualCriteria = tempCriteria.trim();
                        } else if (tempCriteria.toLowerCase().contains("t")) {
                            tempCriteria = tempCriteria.toLowerCase();
                            tempCriteria = tempCriteria.substring(0, tempCriteria.indexOf("t"));
                            unitValue = "T";
                            actualCriteria = tempCriteria.trim();
                        }
                    }
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM)) {
                        curntCriteria = JsonProcessor.checkSizesKeyValuePair(sizesWSResponse,
                                ApplicationConstants.CONST_STRING_STANDARD_NUMBERED_OTHER, criteriaCode);
                        isCustomValue = true;
                    }
                }
            }

            if (null != curntCriteria && !curntCriteria.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                uniqueSetValueId--;
                child1Obj.setSetCodeValueId(curntCriteria);
                // child1Obj.setCodeValue(criteriaCode); // "PRCL"
                child1Obj.setCriteriaSetValueId(uniqueSetValueId + "");
                criteriaSetCodeValues[0] = child1Obj;
                child1Obj = null;
                criteriaSetValue.setId(uniqueSetValueId + "");

                if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)
                        || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                    Value value = new Value();
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE)) {
                        if (null == sizesCriteriaWSResponse) {
                            sizesCriteriaWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                        }
                        sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse, "Unit", criteriaCode);
                        if (unitValue.contains(":")) {
                            String[] unitValueAry = unitValue.split(":");
                            String unitsCode = unitValueAry[1];
                            unitValue = unitValueAry[0];
                            String temp = unitsCode;
                            unitsCode = JsonProcessor.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                    sizeElementsResponse, unitsCode.trim());
                            if (CommonUtilities.isValueNull(unitsCode)) { // Fix for unit other than in Lookup
                                unitsCode = JsonProcessor.getSizesElementValue(ApplicationConstants.CONST_STRING_UNITS,
                                        sizeElementsResponse, ApplicationConstants.CONST_STRING_OTHER);
                                if (criteriaSetValue.getCriteriaSetCodeValues() != null
                                        && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
                                    criteriaSetValue.getCriteriaSetCodeValues()[0].setCodeValue(temp);
                                    value.setUnitOfMeasureCode(unitsCode);
                                }
                            } else {
                                value.setUnitOfMeasureCode(unitsCode);
                            }

                            value.setCriteriaAttributeId(JsonProcessor.getSizesElementValue("ID", sizeElementsResponse,
                                    unitValue.trim()));
                            value.setUnitValue(unitValue);
                            value.setUnitOfMeasureCode(unitsCode);
                            Value[] valueAry = new Value[1];
                            valueAry[0] = value;
                            criteriaSetValue.setValue(valueAry);
                        }
                    }
                } else if (isOtherSize) {
                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)) {
                        if (null == sizesCriteriaWSResponse) {
                            sizesCriteriaWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                    .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                        }
                        sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse, "Unit", criteriaCode);
                        Value valueObj = new Value();
                        // String[] unitValueAry=new String[1];
                        // String unitsCode = unitValueAry[1];
                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR)
                                && !CommonUtilities.isValueNull(unitValue)) {
                            String temp = JsonProcessor.getSizesElementValue("Units", sizeElementsResponse, unitValue.trim());
                            valueObj.setUnitOfMeasureCode(CommonUtilities.isValueNull(temp) ? "" : temp);
                        }
                        unitValue = (unitValue == null || unitValue.trim().isEmpty()) ? actualCriteria : unitValue;

                        valueObj.setCriteriaAttributeId(JsonProcessor.getSizesElementValue("ID", sizeElementsResponse,
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
                                validUnit = actualCriteria.substring(actualCriteria.indexOf("x") + 1, actualCriteria.length());

                        }

                        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE)) {
                            if (validUnit.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FALSE_SMALL)) {
                                untValueFnlAry = new String[] { actualCriteria, ApplicationConstants.CONST_STRING_EMPTY };
                            } else
                                untValueFnlAry = new String[] { actualCriteria.substring(0, actualCriteria.indexOf("(")), validUnit };
                        } else {
                            if (actualCriteria.contains("x"))
                                untValueFnlAry = new String[] { actualCriteria.substring(0, actualCriteria.indexOf("x")), validUnit };
                            else
                                untValueFnlAry = new String[] { actualCriteria, ApplicationConstants.CONST_STRING_EMPTY };
                        }

                        for (int untValCntr = 0; untValCntr < unitValueAry.length; untValCntr++) {
                            valueObj = new Value();
                            if (null == sizesCriteriaWSResponse) {
                                sizesCriteriaWSResponse = JerseyClientPost.getLookupsResponse(RestAPIProperties
                                        .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                            }
                            sizeElementsResponse = JsonProcessor.getSizesResponse(sizesCriteriaWSResponse,
                                    unitValueAry[untValCntr], criteriaCode);
                            valueObj.setCriteriaAttributeId(JsonProcessor.getSizesElementValue("ID", sizeElementsResponse,
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
                } else {
                    criteriaSetValue.setValue(actualCriteria);
                }

                if (isCustomValue) {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    isCustomValue = false;
                } else
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);

                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);

                criteriaSetValue.setCriteriaSetId(criteriaSetId);

                criteriaSetValue.setCriteriaSetId(criteriaSetId);
                criteriaSetValue.setCriteriaCode(criteriaCode);

                criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);

                if (criteriaSetValuesList.size() > cntr) {
                    criteriaSetValuesList.add(criteriaSetValue);
                    // Adding a this criteriaSet entry details to reference table, so later can be referenced easily
                    productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId(), criteriaCode,
                            orignalCriteriaValue, criteriaSetValue.getId());
                }
                criteriaSetValue = null;
                cntr++;
            }

        }

        return criteriaSetValuesList;

    }

    public ProductCriteriaSets compareAndUpdateSizeGroup(ProductDetail existingProduct,
            ProductCriteriaSets newlyCreatedCriteriaSet, ProductCriteriaSets existingCriteriaSet) {
        if (newlyCreatedCriteriaSet == null) {
            return null;
        }
        if (existingCriteriaSet != null) {
            Map<String, CriteriaSetValues> existingMap = createExistingSizesCollection(existingCriteriaSet.getCriteriaSetValues(),
                    existingCriteriaSet.getCriteriaCode());
            List<CriteriaSetValues> finalValues = new ArrayList<CriteriaSetValues>();

            if (!existingMap.isEmpty()) {
                for (CriteriaSetValues criteriaSetValue : newlyCreatedCriteriaSet.getCriteriaSetValues()) {
                    String key = newlyCreatedCriteriaSet.getCriteriaCode() + "_" + getKeyFromValue(criteriaSetValue.getValue());
                    if (existingMap.containsKey(key)) {
                        finalValues.add(existingMap.get(key));
                        /*
                         * findSizeValueDetails(criteriaSetValue.getCriteriaCode(), getSizeElementResponse(), existingMap.get(key),
                         * existingProduct.getExternalProductId());
                         */
                    } else {
                        criteriaSetValue.setCriteriaSetId(existingCriteriaSet.getCriteriaSetId());
                        finalValues.add(criteriaSetValue);
                    }
                }
                existingCriteriaSet.setCriteriaSetValues(finalValues);

                return existingCriteriaSet;
            } else {
                // TODO : Update few attributes of newly created set
                return syncProductCriteriaSet(newlyCreatedCriteriaSet);
            }
        } else if (existingProduct != null) {
            // Set productId and CompanyId and ConfigId;
            newlyCreatedCriteriaSet.setProductId(existingProduct.getID());
            newlyCreatedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            newlyCreatedCriteriaSet.setConfigId(this.configId);
            return newlyCreatedCriteriaSet;
        } else {
            newlyCreatedCriteriaSet.setProductId(this.productId);
            newlyCreatedCriteriaSet.setCompanyId(this.companyId);
            newlyCreatedCriteriaSet.setConfigId(this.configId);
            return newlyCreatedCriteriaSet;
        }
    }

    public Map<String, CriteriaSetValues> createExistingSizesCollection(List<CriteriaSetValues> existingCriteriaSetValues,
            String criteriaCode) {
        Map<String, CriteriaSetValues> existing = new HashMap<String, CriteriaSetValues>();
        for (CriteriaSetValues criteriaSetValue : existingCriteriaSetValues) {
            if (criteriaSetValue != null) {
                existing.put(criteriaCode + "_" + getKeyFromValue(criteriaSetValue.getValue()), criteriaSetValue);
            }
        }
        return existing;
    }

    public ProductCriteriaSets syncProductCriteriaSet(ProductCriteriaSets criteriaSet) {
        criteriaSet.setProductId(this.productId);
        criteriaSet.setCompanyId(this.companyId);
        criteriaSet.setConfigId(this.configId);

        return criteriaSet;
    }

    protected ProductCriteriaSets getProductCriteriaSetForSizeGroup(Map<String, ProductCriteriaSets> criteriaSets) {
        ProductCriteriaSets criteriaSet = null;
        for (String code : SIZE_GROUP_CRITERIACODES) {
            criteriaSet = criteriaSets.get(code);
            if (criteriaSet != null) {
                return criteriaSet;
            }
        }
        return criteriaSet;
    }

    public Map<String, ProductCriteriaSets> removeSizeRelatedCriteriaSetFromExisting(
            Map<String, ProductCriteriaSets> existingCriteriaCollection) {

        try {
            for (String code : SIZE_GROUP_CRITERIACODES) {
                if (existingCriteriaCollection.remove(code) != null) {
                    break;
                }
            }
        } catch (Exception e) {
            // Nothing to do on exception
        }
        return existingCriteriaCollection;
    }

    public ProductCriteriaSets getSizeRelatedCriteriaSetFromExisting(Map<String, ProductCriteriaSets> existingCriteriaCollection) {

        try {
            List<String> sizeCriteriaCodes = Arrays.asList(SIZE_GROUP_CRITERIACODES);
            for (ProductCriteriaSets criteriaSets : existingCriteriaCollection.values()) {
                if (criteriaSets != null) {
                    if (sizeCriteriaCodes.contains(criteriaSets.getCriteriaCode())) {
                        return criteriaSets;
                    }
                }
            }

        } catch (Exception e) {
            // Nothing to do with exception
        }
        return null;
    }

    /**
     * Finds the value of a given size element from sizeElementResponses
     * 
     * @param elementName
     *            is the type of size value like Dimension, Weight, etc...
     * @param sizeElementsResponse
     *            contains all the size related details
     * @param attribute
     *            is the value
     * @return Criteria code of the give size value
     */

    @SuppressWarnings("rawtypes")
    public String getSizesElementValue(String elementName, LinkedList<LinkedHashMap> sizeElementsResponse, String attribute) {
        attribute = attribute.trim();
        elementName = elementName.trim();
        String ElementValue = "";
        try {
            // LinkedList<LinkedHashMap> sizeElementsResponse=(LinkedList<LinkedHashMap>)jsonParser.parseToList(response);
            if (null != sizeElementsResponse) {
                Iterator<LinkedHashMap> iterator = sizeElementsResponse.iterator();
                while (iterator.hasNext()) {
                    Map sizeIndividualLookupMap = (LinkedHashMap) iterator.next();
                    if (elementName.equalsIgnoreCase("id")) {
                        if (sizeIndividualLookupMap.get("ID").toString().equals(attribute)) {
                            ElementValue = sizeIndividualLookupMap.get("DisplayName").toString();
                            break;
                        }
                    } else if (elementName.equalsIgnoreCase("units")) {

                        @SuppressWarnings({ "unchecked" })
                        LinkedList<LinkedHashMap> unitValuesList = (LinkedList<LinkedHashMap>) sizeIndividualLookupMap
                                .get("UnitsOfMeasure");
                        Iterator<LinkedHashMap> unitValuesiterator = unitValuesList.iterator();
                        while (unitValuesiterator.hasNext()) {
                            Map codeValueGrpsMap = (LinkedHashMap) unitValuesiterator.next();
                            if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(attribute)) {
                                ElementValue = (String) codeValueGrpsMap.get("Format");
                                break;
                            }
                        }
                    }
                    if (!ElementValue.isEmpty()) break;
                }
            }

        } catch (Exception Ex) {
            LOGGER.error("Exception while processing Product Size Group JSON", Ex);
        }
        return ElementValue;
    }

    @SuppressWarnings("rawtypes")
    public LinkedList<LinkedHashMap> getSizeElementResponse() {
        return ProductDataStore.getLookupResponse();
    }

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSetCodeValueId(String value) {
        // TODO Auto-generated method stub
        return ProductDataStore.getSetCodeValueIdForShippingItem(value);
    }

    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String[] processValues(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * public boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId) {
     * if (criteriaSet == null) {
     * return false;
     * }
     * LOGGER.info("Registering existing Size values of product");
     * if (criteriaSet.getCriteriaSetValues() != null && criteriaSet.getCriteriaSetValues().length > 0) {
     * for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
     * if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
     * findSizeValueDetails(criteriaSet.getCriteriaCode(), getSizeElementResponse(), criteriaValues, externalProductId);
     * }
     * }
     * }
     * LOGGER.info("Completed existing Size values of product");
     * 
     * return false;
     * }
     */
    @SuppressWarnings({ "rawtypes", "unused" })
    private void findSizeValueDetails(String criteriaCode, LinkedList<LinkedHashMap> criteriaAttributes,
            CriteriaSetValues criteriaSetValue, String externalProductId) {
        // String[] stringAry=new String[2];

        if (criteriaCode.equalsIgnoreCase("DIMS"))
            LOGGER.info("Found Size Group Dimension");
        else if (criteriaCode.equalsIgnoreCase("CAPS"))
            LOGGER.info("Found Size Group Capacity");
        else if (criteriaCode.equalsIgnoreCase("SVWT"))
            LOGGER.info("Found Size Group Volume/Weight");
        else if (criteriaCode.equalsIgnoreCase("SABR"))
            LOGGER.info("Found Size Group Apparel - Bra Sizes");
        else if (criteriaCode.equalsIgnoreCase("SAHU"))
            LOGGER.info("Found Size Group Apparel - Hosiery/Uniform Sizes");
        else if (criteriaCode.equalsIgnoreCase("SAIT"))
            LOGGER.info("Found Size Group Apparel - Infant & Toddler");
        else if (criteriaCode.equalsIgnoreCase("SANS"))
            LOGGER.info("Found Size Group Apparel - Dress Shirt Sizes");
        else if (criteriaCode.equalsIgnoreCase("SAWI"))
            LOGGER.info("Found Size Group Apparel - Pants Sizes");
        else if (criteriaCode.equalsIgnoreCase("SSNM"))
            LOGGER.info("Found Size Group Standard & Numbered");
        else if (criteriaCode.equalsIgnoreCase("SOTH")) LOGGER.info("Found Size Group Other");

        String sizeValue = "", finalSizeValue = "", delim = "";
        String sizeElementValue = "";
        // int noOfSizes=criteriaSetValueLst.size();
        int elementsCntr = 0;
        String unitOfmeasureCode = "";
        int sizeCntr = 0;
        if (criteriaSetValue.getValue() instanceof List) {
            ArrayList<?> valueList = (ArrayList<?>) criteriaSetValue.getValue();
            Iterator<?> sizeValuesItr = valueList.iterator();

            while (sizeValuesItr.hasNext()) {
                LinkedHashMap<?, ?> valueMap = (LinkedHashMap<?, ?>) sizeValuesItr.next();
                unitOfmeasureCode = getSizesElementValue("UNITS", criteriaAttributes, valueMap.get("UnitOfMeasureCode").toString());
                if (unitOfmeasureCode.equals("\"")) unitOfmeasureCode = "in";
                if (unitOfmeasureCode.equals("'")) unitOfmeasureCode = "ft";
                if (criteriaCode.equalsIgnoreCase("DIMS") || criteriaCode.equalsIgnoreCase("SDIM")) {
                    if (criteriaCode.equalsIgnoreCase("DIMS"))
                        sizeValue = getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString())
                                + ":" + valueMap.get("UnitValue") + ":" + unitOfmeasureCode;
                    else
                        sizeValue = valueMap.get("UnitValue") + ":" + unitOfmeasureCode;
                    delim = "; ";
                } else if (criteriaCode.equalsIgnoreCase("CAPS") || criteriaCode.equalsIgnoreCase("SVWT")
                        || criteriaCode.equalsIgnoreCase("SHWT")) {
                    sizeValue = valueMap.get("UnitValue") + ":" + unitOfmeasureCode;
                    delim = ": ";
                } else {
                    if (criteriaCode.equalsIgnoreCase("SAWI")) {
                        if (getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString()).equals(
                                "Waist"))
                            sizeValue = valueMap.get("UnitValue").toString();
                        else if (getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString())
                                .equals("Inseam")) sizeValue = "x" + valueMap.get("UnitValue").toString();
                    } else if (criteriaCode.equalsIgnoreCase("SAIT")) {
                        if (unitOfmeasureCode.length() == 1) {
                            sizeValue = valueMap.get("UnitValue").toString() + unitOfmeasureCode;
                        } else {
                            sizeValue = valueMap.get("UnitValue").toString() + " " + unitOfmeasureCode;
                        }
                    }
                }

                if (sizeCntr != 0) {
                    if (criteriaCode.equalsIgnoreCase("SANS")) {
                        sizeElementValue += "(" + sizeValue.trim() + ")";
                    } else {
                        sizeElementValue += delim + sizeValue;
                    }
                } else {
                    sizeElementValue += sizeValue;
                }

                sizeCntr++;
            }
            // updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE,
            // value, criteriaSetValue);
            updateReferenceTable(externalProductId, criteriaCode, sizeElementValue, criteriaSetValue);
            // criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,criteriaSetValue.getID(),sizeElementValue);
        } else {
            sizeValue = criteriaSetValue.getBaseLookupValue();
            if (null == sizeValue) {
                sizeValue = criteriaSetValue.getFormatValue();
            }
            updateReferenceTable(externalProductId, criteriaCode, sizeElementValue, criteriaSetValue);
            //
            // criteriaSetParser.addReferenceSet(externalProductId, criteriaSetValue.getCriteriaCode(), criteriaSetValue.getID(),
            // sizeValue);
            sizeElementValue += sizeValue;
            sizeCntr++;
        }
        if (elementsCntr != 0) {
            finalSizeValue = finalSizeValue + "," + sizeElementValue.trim();
        } else {
            finalSizeValue = sizeElementValue.trim();
        }
        sizeElementValue = "";
    }

    public Map<String, ProductCriteriaSets> processShippingItem(ProductDetail product,
            Map<String, ProductCriteriaSets> existingCriteriaSetMap, String configId, ShippingEstimate shippingEstimate,
            int criteriaSetId) {
        this.configId = configId;
        this.productId = product.getID();
        this.companyId = product.getCompanyId();

        if (shippingEstimate == null) {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
        }
        // Shipping Dimensions and Weight
        String[] dimnsAry = {};
        if (shippingEstimate.getDimensions() == null) {
            // Remove Previous SDIM from the criteria set
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);

        } else {
            this.criteriaSetId = String.valueOf(--criteriaSetId);
            String shippingDimensions = ProductParserUtil.getShippingDimension(shippingEstimate);

            LOGGER.info("Shipping Dimensions Transformation Starts :" + shippingDimensions);
            dimnsAry = shippingDimensions.split(";");
            if (dimnsAry.length == 3) {

                ProductCriteriaSets tempCriteriaSet = getSizeCriteriaSet(shippingDimensions,
                        ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION,
                        existingCriteriaSetMap.get(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION), product);
                // compare and update
                if (tempCriteriaSet != null && tempCriteriaSet.getCriteriaSetValues().size() > 0) {
                    tempCriteriaSet = compareAndUpdateSizeGroup(product, tempCriteriaSet,
                            existingCriteriaSetMap.get(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION));
                    existingCriteriaSetMap.put(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION, tempCriteriaSet);
                } else {
                    existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);
                }
            } else {

                // TODO : LOG ERROR
            }
            LOGGER.info("Shipping Dimensions Transformation Ends");
        }

        if (shippingEstimate.getNumberOfItems() == null) {
            // Remove Previous SDIM from the criteria set
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
        } else {
            this.criteriaSetId = String.valueOf(--criteriaSetId);
            ProductCriteriaSets tempCriteriaSet = getCriteriaSet(shippingEstimate, product,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE), criteriaSetId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE, tempCriteriaSet);
        }

        if (shippingEstimate.getWeight() == null) {
            // Remove Previous SDIM from the criteria set
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);
        } else {
            this.criteriaSetId = String.valueOf(--criteriaSetId);
            String shippingWeight = ProductParserUtil.getShippingWeight(shippingEstimate);
            ProductCriteriaSets tempCriteriaSet = getSizeCriteriaSet(shippingWeight,
                    ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT), product);
            // compare and update
            if (tempCriteriaSet != null && tempCriteriaSet.getCriteriaSetValues().size() > 0) {
                tempCriteriaSet = compareAndUpdateSizeGroup(product, tempCriteriaSet,
                        existingCriteriaSetMap.get(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT));
                existingCriteriaSetMap.put(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT, tempCriteriaSet);
            } else {
                existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);
            }
        }
        return existingCriteriaSetMap;

    }

    private ProductCriteriaSets getCriteriaSet(ShippingEstimate shippingEstimate, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int uniqueCriteriaSetId) {

        if (shippingEstimate == null || shippingEstimate.getNumberOfItems() == null) {
            return null;
        }

        LOGGER.info("Started Processing of Shipping Items of Shipping Estimate" + shippingEstimate.getNumberOfItems());
        // String[] finalValues = processValues(values);
        List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<>();

        boolean checkExistingElements = matchedCriteriaSet != null;

        HashMap<String, CriteriaSetValues> existingValueMap = new HashMap<String, CriteriaSetValues>();
        if (checkExistingElements) {
            existingValueMap = createTableForExistingSetValue(matchedCriteriaSet.getCriteriaSetValues());
        } else {
            matchedCriteriaSet = new ProductCriteriaSets();
            // Set Basic elements
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getID());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(this.configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        String setCodeValueId = getSetCodeValueId(ApplicationConstants.CONST_STRING_OTHER);
        CriteriaSetValues criteriaSetValue = null;
        Value value = getValueForShippingItem(existingProduct.getExternalProductId(), shippingEstimate.getNumberOfItems());
        if (value == null) {
            return null;
        } else {
            String key = getKeyFromValue(value);
            if (checkExistingElements) {
                criteriaSetValue = existingValueMap.get(key);
            }

            if (criteriaSetValue == null) {
                // If no match found in the existing list
                // Set basic properties for a criteriaSetValue
                criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaValueDetail(shippingEstimate.getNumberOfItems().getUnit());
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                criteriaSetValue.setValue(new Value[] { value });
            }
        }
        updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE,
                String.valueOf(ProductParserUtil.getShippingItem(shippingEstimate)), criteriaSetValue);

        finalCriteriaSetValues.add(criteriaSetValue);

        LOGGER.info("Completed Processing of Shipping Item of Shipping Estimate " + shippingEstimate);
        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);
        return matchedCriteriaSet;
    }

    private HashMap<String, CriteriaSetValues> createTableForExistingSetValue(List<CriteriaSetValues> existingCriteriaSetValues) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Started createTableForExistingSetValue(), " + System.currentTimeMillis());
        }
        HashMap<String, CriteriaSetValues> existing = new HashMap<String, CriteriaSetValues>(existingCriteriaSetValues.size());

        for (CriteriaSetValues criteriaSetValue : existingCriteriaSetValues) {
            if (criteriaSetValue != null) {
                existing.put(getKeyFromValue(criteriaSetValue.getValue()), criteriaSetValue);
            }
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Completed createTableForExistingSetValue(), " + System.currentTimeMillis());
        }
        return existing;
    }

    private Value getValueForShippingItem(String externalProductId, com.asi.ext.api.service.model.Value shippingItemValue) {
        if (!CommonUtilities.isValueNull(shippingItemValue.getValue()) || !CommonUtilities.isValueNull(shippingItemValue.getUnit())) {
            String criteriaSetAttributeId = getCriteriaSetAttributeId(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
            String unitOfMeasureCode = getUnitOfMeasureCode(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE,
                    shippingItemValue.getUnit());

            if (unitOfMeasureCode == null) {
                unitOfMeasureCode = getUnitOfMeasureCode(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE,
                        ApplicationConstants.CONST_STRING_OTHER);
            }

            if (criteriaSetAttributeId != null && unitOfMeasureCode != null) {
                Value value = new Value();
                value.setCriteriaAttributeId(criteriaSetAttributeId);
                value.setUnitOfMeasureCode(unitOfMeasureCode);
                value.setUnitValue(shippingItemValue.getValue());

                return value;
            } else {
                addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
                        "One of the required attribute not found in the Shipping Estimate lookup data ");
                return null;
            }

        } else {
            addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                    "Invalid value found for Number of Items in Shipping Estimate unit : " + shippingItemValue.getUnit()
                            + ", value : " + shippingItemValue.getValue());
        }
        return null;
    }

}
