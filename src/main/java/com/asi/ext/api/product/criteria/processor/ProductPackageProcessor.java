package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductPackageProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(ProductPackageProcessor.class.getName());

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductPackageProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getPackageCriteriaSet(List<String> packages, ProductDetail rdrProduct,
            ProductCriteriaSets productCriteriaSets, String configId) {
        this.configId = configId;
        if (packages != null && !packages.isEmpty()) { 
            return getCriteriaSet(packages, rdrProduct, productCriteriaSets, 0);
        } else {
            return null;
        }
    }

    protected ProductCriteriaSets getCriteriaSet(List<String> packages, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet,
            int currentSetValueId) {
      
        LOGGER.info("Started Processing of Product Packages");
        ProductDataStore productDataStore = new ProductDataStore();
        // First verify and process value to desired format

    
        //String[] finalValues = processValues(values);
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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }
        String setCodeValueForCustomTypes = getSetCodeValueId(ApplicationConstants.CONST_STRING_CUSTOM);
        for (String value : packages) {
            String setCodeValueId = getSetCodeValueId(value);

            if (CommonUtilities.isValueNull(setCodeValueId)) {
                if (CommonUtilities.isValueNull(value)) {
                    productDataStore.addErrorToBatchLogCollection(existingProduct.getExternalProductId(),
                            ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Package Value " + value
                                    + " dosen't exists in lookup values");
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
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
                // Check whether the given value is custom
                if (setCodeValueForCustomTypes == setCodeValueId) {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                } else {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                }
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                criteriaSetValue.setValue(value);
            }

            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE, value, criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);

        LOGGER.info("Completed Processing of Product Packages");
        productDataStore = null;
        return matchedCriteriaSet;
    }

    protected boolean isValueIsValid(String value) {
        return true;
    }

    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForProductPackage(value.trim());
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
        LOGGER.info("Registering existing Package values of product");
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
                     updateReferenceTable(externalProductId, ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE,
                     valueToRegister, criteriaValues);
                }
            }
        }
        LOGGER.info("Completed existing Package values of product");

        return false;
    }

    /* (non-Javadoc)
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#getCriteriaSet(java.lang.String, com.asi.service.product.client.vo.ProductDetail, com.asi.service.product.client.vo.ProductCriteriaSets, int)
     */
    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

}
