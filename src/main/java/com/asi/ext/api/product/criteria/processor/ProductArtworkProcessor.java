package com.asi.ext.api.product.criteria.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Artwork;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductArtworkProcessor extends SimpleCriteriaProcessor {

    protected Map<String, CriteriaSetValues> existingCriteriaValueMap = new HashMap<String, CriteriaSetValues>();

    public CriteriaSetValues getArtworkCriteriaSetValue(Artwork artworks, String criteriaSetId) {
        return null;

    }

    protected void generateExistingCriteriaList(List<CriteriaSetValues> criteriaSetValues) {
        existingCriteriaValueMap = getExistingCriteriaSetValues(criteriaSetValues);
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
        return ProductDataStore.reverseLookupFindAttribute(setCodeValueId, ApplicationConstants.CONST_ARTWORK_CODE);
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
