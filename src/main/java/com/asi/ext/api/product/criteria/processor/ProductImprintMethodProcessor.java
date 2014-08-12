package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asi.ext.api.product.transformers.ProductDataStore;

import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductImprintMethodProcessor extends SimpleCriteriaProcessor {

    protected Map<String, CriteriaSetValues> existingCriteriaValueMap = new HashMap<String, CriteriaSetValues>();

    public CriteriaSetValues getImprintCriteriaSetValue(String imprintName, String aliace, String criteriaSetId) {
        
        CriteriaSetValues criteriaSetValue = existingCriteriaValueMap.get(imprintName);
        String setCodeValueId = null;
        boolean customValue = false;
        if (criteriaSetValue == null) {
            setCodeValueId = ProductDataStore.getSetCodeValueIdForImmdMethod(imprintName);
            if (CommonUtilities.isValueNull(setCodeValueId)) {
                customValue = true;
                setCodeValueId = ProductDataStore.getSetCodeValueIdForImmdMethod(ApplicationConstants.CONST_STRING_OTHER);
            }
            criteriaSetValue = new CriteriaSetValues();

            criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
            criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
            if (customValue) { 
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
            } else {
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
            }
            criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setCriteriaSetId(criteriaSetId);
            criteriaSetValue.setCriteriaValueDetail(aliace);
            criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
            criteriaSetValue.setValue(aliace);
        } else {
            criteriaSetValue.setValue(aliace);
            criteriaSetValue.setCriteriaValueDetail(aliace);
        }

        return criteriaSetValue;

    }

    protected void generateExistingCriteriaList(List<CriteriaSetValues> criteriaSetValues) {
        existingCriteriaValueMap = getExistingCriteriaSetValues(criteriaSetValues);
    }

    public ProductCriteriaSets compareAndUpdateImprintMethod(ProductCriteriaSets currentImpMethodSet,
            ProductCriteriaSets extImpMethodSet, String configId, String productId) {

        List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<CriteriaSetValues>();

        String criteriaSetId = extImpMethodSet != null ? extImpMethodSet.getCriteriaSetId() : currentImpMethodSet
                .getCriteriaSetId();
        if (extImpMethodSet != null) {
            Map<String, CriteriaSetValues> existingCriteriaValueMap = getExistingCriteriaSetValues(extImpMethodSet
                    .getCriteriaSetValues());
            for (CriteriaSetValues criteriaSetValue : currentImpMethodSet.getCriteriaSetValues()) {
                CriteriaSetValues temp = null;
                if (!existingCriteriaValueMap.isEmpty()) {
                    temp = existingCriteriaValueMap.get(String.valueOf(criteriaSetValue.getValue()).trim());
                }

                if (temp == null) {
                    criteriaSetValue.setCriteriaSetId(criteriaSetId);
                    finalCriteriaSetValues.add(criteriaSetValue);
                } else {
                    finalCriteriaSetValues.add(temp);
                }
            }
        }
        if (finalCriteriaSetValues != null && !finalCriteriaSetValues.isEmpty()) {
            currentImpMethodSet.setCriteriaSetValues(finalCriteriaSetValues);
        }
        currentImpMethodSet.setConfigId(configId);
        currentImpMethodSet.setProductId(productId);
        currentImpMethodSet.setCriteriaSetId(criteriaSetId);
        return currentImpMethodSet;
    }

    private Map<String, CriteriaSetValues> getExistingCriteriaSetValues(List<CriteriaSetValues> criteriaSetValues) {
        Map<String, CriteriaSetValues> finalCriteriaValueMap = new HashMap<String, CriteriaSetValues>();
        for (CriteriaSetValues criteriaValue : criteriaSetValues) {
            if (criteriaValue != null) {
                String key = CommonUtilities.getValueFromCriteriaSet(criteriaValue);
                if (key == null && criteriaValue.getCriteriaSetCodeValues() != null
                        && criteriaValue.getCriteriaSetCodeValues().length != 0) {
                    key = reverseLookup(criteriaValue.getCriteriaSetCodeValues()[0].getSetCodeValueId());
                }
                finalCriteriaValueMap.put(key, criteriaValue);
            }
        }
        return finalCriteriaValueMap;
    }

    private String reverseLookup(String setCodeValueId) {
        return ProductDataStore.reverseLookupFindAttribute(setCodeValueId, ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
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
        return null;
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

}
