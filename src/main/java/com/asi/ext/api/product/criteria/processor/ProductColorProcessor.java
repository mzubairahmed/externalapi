package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Color;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductColorProcessor extends SimpleCriteriaProcessor {

    private int                 uniqueCriteriaSetId = -1;
    private String              configId            = "0";

    private final static Logger LOGGER              = Logger.getLogger(ProductColorProcessor.class.getName());

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductColorProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getProductColorCriteriaSet(List<Color> colors, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {

        if (colors == null || colors.isEmpty()) {
            return null;
        }
        this.configId = configId;

        return getCriteriaSet(getColorStringFromList(colors), existingProduct, matchedCriteriaSet, 0);
    }

    private String getColorStringFromList(List<Color> colors) {
        String finalColor = "";
        for (Color color : colors) {
            if (color != null) {
                if (CommonUtilities.isValueNull(color.getAlias())) {
                    finalColor = finalColor.isEmpty() ? color.getName() : finalColor + "," + color.getName();
                } else {
                    finalColor = finalColor.isEmpty() ? color.getName() + "=" + color.getAlias() : finalColor + ","
                            + color.getName() + "=" + color.getAlias();
                }
            }
        }
        return finalColor;
    }

    public ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet,
            int currentSetValueId) {

        if (!updateNeeded(matchedCriteriaSet, values)) {
            return null;
        }
        LOGGER.info("Started Processing of Product Colors");
        // First verify and process value to desired format

        if (!isValueIsValid(values)) {
            return null;
        }

        String[] finalValues = processValues(values);
        List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<>();

        boolean checkExistingElements = matchedCriteriaSet != null;

        HashMap<String, CriteriaSetValues> existingValueMap = new HashMap<String, CriteriaSetValues>();
        if (checkExistingElements) {
            existingValueMap = createTableForExistingSetValue(matchedCriteriaSet.getCriteriaSetValues());
        } else {
            matchedCriteriaSet = new ProductCriteriaSets();
            // Set Basic elements
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getID());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(this.configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (String value : finalValues) {
            String originalValue = value;
            int index = value.indexOf("=");

            if (index != -1) {
                value = value.substring(0, index);
                originalValue = originalValue.substring(index + 1);
            }
            String setCodeValueId = getSetCodeValueId(value);

            if (CommonUtilities.isValueNull(setCodeValueId)) {
                // LOG Batch Error
                continue;
            }
            CriteriaSetValues criteriaSetValue = null;

            if (checkExistingElements) {
                if (index == -1) {
                    criteriaSetValue = existingValueMap.get(value.toUpperCase() + "_" + setCodeValueId);
                } else {
                    criteriaSetValue = existingValueMap.get(originalValue.toUpperCase() + "_" + setCodeValueId);
                }

            }
            if (criteriaSetValue == null) {
                // If no match found in the existing list
                // Set basic properties for a criteriaSetValue
                criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                if (index != -1) {
                    criteriaSetValue.setValue(originalValue);
                } else {
                    criteriaSetValue.setValue(value);
                }
            }

            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_COLORS_CRITERIA_CODE,
                    processSourceCriteriaValueByCriteriaCode(originalValue, ApplicationConstants.CONST_COLORS_CRITERIA_CODE),
                    criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);

        LOGGER.info("Completed Processing of Product Colors");

        return matchedCriteriaSet;
    }

    protected boolean isValueIsValid(String value) {
        // For color no need to validate values
        return true;
    }

    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForProductColor(value.trim());
    }

    @Override
    protected String[] processValues(String value) {
        // Add NP Check and all
        return CommonUtilities.trimArrayValues(value.split(ApplicationConstants.CONST_STRING_COMMA_SEP));
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // if (value != null )
        return false;
    }

    private HashMap<String, CriteriaSetValues> createTableForExistingSetValue(List<CriteriaSetValues> setValues) {
        HashMap<String, CriteriaSetValues> tempHashMap = new HashMap<>();

        if (setValues != null && !setValues.isEmpty()) {
            for (CriteriaSetValues criteriaSetValue : setValues) {
                String setCodeValue = criteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(); // Check for AIOE
                tempHashMap.put(String.valueOf(criteriaSetValue.getValue()).toUpperCase() + "_" + setCodeValue, criteriaSetValue);
            }
        }

        return tempHashMap;
    }

    public boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId) {
        if (criteriaSet == null) {
            return false;
        }
        LOGGER.info("Registering existing product color values");
        if (criteriaSet.getCriteriaSetValues() != null && !criteriaSet.getCriteriaSetValues().isEmpty()) {
            for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
                if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
                    String valueToRegister = null;
                    if (CommonUtilities.isValueNull(String.valueOf(criteriaValues.getValue()))) {
                        valueToRegister = doReverseLookup(criteriaValues.getCriteriaSetCodeValues()[0].getSetCodeValueId(),
                                criteriaSet.getCriteriaCode());
                    } else {
                        valueToRegister = String.valueOf(criteriaValues.getValue());
                    }
                    updateReferenceTable(externalProductId, ApplicationConstants.CONST_COLORS_CRITERIA_CODE, valueToRegister,
                            criteriaValues);
                }
            }
        }
        LOGGER.info("Completed registering existing product color values");

        return false;
    }

}