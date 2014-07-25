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

public class ProductImprintMethodProcessor {

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

}
