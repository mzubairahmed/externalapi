package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;

import com.asi.ext.api.service.model.Samples;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductSpecSampleProcessor extends SimpleCriteriaProcessor { // SPECIAL Processing needed

    private final static Logger LOGGER              = Logger.getLogger(ProductSpecSampleProcessor.class.getName());

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductSpecSampleProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getProductSamplesCriteriaSet(Samples samples, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {
        this.configId = configId;
        String specSample = samples.getSpecSampleAvailable() ? samples.getSpecInfo() : null;
        String prodSample = samples.getProductSampleAvailable() ? samples.getProductSampleInfo() : null;

        return getCriteriaSetForMultiple(samples, existingProduct, matchedCriteriaSet, 0);
    }

    public ProductCriteriaSets getCriteriaSetForMultiple(Samples sample, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Started processing ProductSpecSampleProcessor.getCriteriaSetForMultiple()");
        }
        LOGGER.info("Started Processing of Spec & Product sample");
        Map<String, CriteriaSetValues> existingMap = matchedCriteriaSet != null ? createTableForExistingSetValue(matchedCriteriaSet
                .getCriteriaSetValues()) : null;

        boolean checkExisting = existingMap != null;

        CriteriaSetValues productSampleCriteriaSetValue = null;
        CriteriaSetValues specSampleCriteriaSetValue = null;

        if (matchedCriteriaSet == null) {
            matchedCriteriaSet = new ProductCriteriaSets();
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getID());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        if (checkExisting) {
            productSampleCriteriaSetValue = existingMap.get("ProductSample");
            specSampleCriteriaSetValue = existingMap.get("SpecSample");
        }
        if (productSampleCriteriaSetValue == null && sample.getProductSampleAvailable()) {
            // Existing criteria value matched
            productSampleCriteriaSetValue = createProductSampleSetValue(sample.getProductSampleInfo(), matchedCriteriaSet.getCriteriaSetId());
        } else if (productSampleCriteriaSetValue != null && sample.getProductSampleAvailable()) {
            productSampleCriteriaSetValue.setValue(sample.getProductSampleInfo());
            productSampleCriteriaSetValue.setCriteriaValueDetail(sample.getProductSampleInfo() != null ? sample.getProductSampleInfo() : "");
        }
        
        if (specSampleCriteriaSetValue == null && sample.getSpecSampleAvailable()) {
            // Existing criteria value matched
            specSampleCriteriaSetValue = createSpecSampleSetValue(sample.getSpecInfo(), matchedCriteriaSet.getCriteriaSetId());
        } else if (specSampleCriteriaSetValue != null && sample.getSpecSampleAvailable()) {
            specSampleCriteriaSetValue.setValue(sample.getSpecInfo());
            specSampleCriteriaSetValue.setCriteriaValueDetail(sample.getSpecInfo() != null ? sample.getSpecInfo() : "");
        }

        

        List<CriteriaSetValues> finalValues = new ArrayList<CriteriaSetValues>();

        if (sample.getProductSampleAvailable()) {
            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE,
                    productSampleCriteriaSetValue.getCriteriaValueDetail(), productSampleCriteriaSetValue);
            finalValues.add(productSampleCriteriaSetValue);
        }
        if (sample.getSpecSampleAvailable()) {
            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE,
                    specSampleCriteriaSetValue.getCriteriaValueDetail(), specSampleCriteriaSetValue);
            finalValues.add(specSampleCriteriaSetValue);
        }
        
        if (finalValues != null && !finalValues.isEmpty()) {
            matchedCriteriaSet.setCriteriaSetValues(finalValues);
            return matchedCriteriaSet;            
        } else {
            return null;
        }

    }

    private CriteriaSetValues createSpecSampleSetValue(String specSample, String criteriaSetId) {
        specSample = CommonUtilities.isValueNull(specSample) ? "" : specSample;
        CriteriaSetValues crtValue = new CriteriaSetValues();
        crtValue.setId(String.valueOf(--uniqueSetValueId));
        crtValue.setCriteriaSetId(criteriaSetId);
        crtValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        crtValue.setCriteriaCode(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);
        crtValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        crtValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
        crtValue.setValue(specSample);
        crtValue.setCriteriaValueDetail(specSample);
        String setCodeValueId = getSetCodeValueId("Spec Sample");
        if (setCodeValueId != null) {
            crtValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, crtValue.getId()));
        }

        return crtValue;
    }

    private CriteriaSetValues createProductSampleSetValue(String prodSample, String criteriaSetId) {
        prodSample = CommonUtilities.isValueNull(prodSample) ? "" : prodSample;
        CriteriaSetValues crtValue = new CriteriaSetValues();
        crtValue.setId(String.valueOf(--uniqueSetValueId));
        crtValue.setCriteriaSetId(criteriaSetId);
        crtValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        crtValue.setCriteriaCode(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);
        crtValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        crtValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
        crtValue.setValue(prodSample);
        crtValue.setCriteriaValueDetail(prodSample);
        String setCodeValueId = getSetCodeValueId("Product Sample");
        if (setCodeValueId != null) {
            crtValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, crtValue.getId()));
        }
        return crtValue;
    }

    public ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet,
            int currentSetValueId) {
        return null;
    }

    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForProdSpecSample(value);
    }

    @Override
    protected String[] processValues(String value) {
        // Add NP Check and all
        return new String[] { value.trim() };
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        return false;
    }

    private HashMap<String, CriteriaSetValues> createTableForExistingSetValue(List<CriteriaSetValues> setValues) {
        HashMap<String, CriteriaSetValues> tempHashMap = new HashMap<>();
        String specSampleSetCodeValue = getSetCodeValueId("Spec Sample");
        String productSampleSetCodeValue = getSetCodeValueId("Product Sample");

        if (setValues != null && !setValues.isEmpty()) {
            for (CriteriaSetValues criteriaSetValue : setValues) {
                try {
                    String setCodeValue = criteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(); // Check for AIOE
                    if (specSampleSetCodeValue.equalsIgnoreCase(setCodeValue)) {
                        tempHashMap.put("SpecSample", criteriaSetValue);
                    } else if (productSampleSetCodeValue.equalsIgnoreCase(setCodeValue)) {
                        tempHashMap.put("ProductSample", criteriaSetValue);
                    } else {
                        tempHashMap.put("Other", criteriaSetValue);
                    }
                } catch (Exception e) {
                }
            }
        }

        return tempHashMap;
    }

    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

}
