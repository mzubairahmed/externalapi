package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.ImprintColor;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductImprintColorProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(ProductImprintColorProcessor.class.getName());

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductImprintColorProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getImprintColorCriteriaSet(ImprintColor imprintColor, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {

        if (imprintColor == null) {
            return null;
        }
        this.configId = configId;
        if (imprintColor.getValues() == null || imprintColor.getValues().isEmpty()) {
            productDataStore.addErrorToBatchLogCollection(existingProduct.getExternalProductId(),
                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Imprint Color value collection cannot be empty");
        }
        String imprintColors = CommonUtilities.convertStringListToCSV(imprintColor.getValues());
        String typeCode = null;
        if (CommonUtilities.isValueNull(imprintColor.getType())) {
            typeCode = ApplicationConstants.CONST_VALUE_TYPE_CODE_COLOR;
        } else if (imprintColor.getType().equalsIgnoreCase(ApplicationConstants.CONST_VALUE_TYPE_CODE_COLOR)) {
            typeCode = imprintColor.getType().toUpperCase().trim();
        } else if (imprintColor.getType().equalsIgnoreCase(ApplicationConstants.CONST_VALUE_TYPE_CODE_PMS)) {
            typeCode = imprintColor.getType().toUpperCase().trim();
        } else {
            typeCode = ApplicationConstants.CONST_VALUE_TYPE_CODE_COLOR;
        }

        return getCriteriaSet(imprintColors, typeCode, existingProduct, matchedCriteriaSet, 0);
    }

    public ProductCriteriaSets getCriteriaSet(String values, String typeCode, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        if (!updateNeeded(matchedCriteriaSet, values)) {
            return null;
        }
        LOGGER.info("Started Processing of Imprint Color");
        ProductDataStore productDataStore = new ProductDataStore();
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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (String value : finalValues) {
            String setCodeValueId = getSetCodeValueId(value);

            if (CommonUtilities.isValueNull(setCodeValueId)) {
                if (CommonUtilities.isValueNull(value)) {
                    productDataStore.addErrorToBatchLogCollection(existingProduct.getExternalProductId(),
                            ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Imprint Color Value " + value
                                    + " doesn’t exist in lookup values");
                }
                continue;
            }
            CriteriaSetValues criteriaSetValue = null;

            if (checkExistingElements) {
                criteriaSetValue = existingValueMap.get(value.toUpperCase() + "_" + setCodeValueId);
            }
            if (criteriaSetValue == null) {
                // If no match found in the existing list
                // Set basic properties for a criteriaSetValue
                criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
                criteriaSetValue.setValueTypeCode(typeCode);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                criteriaSetValue.setValue(value);
            } else {
                criteriaSetValue.setValueTypeCode(typeCode);
            }
            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE,
                    value, criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);

        LOGGER.info("Completed Processing of Imprint Color");
        productDataStore = null;
        return matchedCriteriaSet;
    }

    protected boolean isValueIsValid(String value) {
        // For color no need to validate values
        return true;
    }

    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForImprintColor(value.trim());
    }

    @Override
    protected String[] processValues(String value) {
        // Add NP Check and all
        return CommonUtilities.trimArrayValues(value.split(ApplicationConstants.CONST_STRING_COMMA_SEP));
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
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
        LOGGER.info("Registering existing Imprint color values of product");
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
                    updateReferenceTable(externalProductId, ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE,
                            valueToRegister, criteriaValues);
                }
            }
        }
        LOGGER.info("Completed existing Imprint color values of product");

        return false;
    }

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

}
