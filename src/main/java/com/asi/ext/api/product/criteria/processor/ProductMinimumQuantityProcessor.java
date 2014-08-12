package com.asi.ext.api.product.criteria.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.MinimumOrder;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Value;

public class ProductMinimumQuantityProcessor extends SimpleCriteriaProcessor {

    protected Map<String, CriteriaSetValues> existingCriteriaValueMap = new HashMap<String, CriteriaSetValues>();

    public CriteriaSetValues getMinQtyCriteriaSetValue(MinimumOrder minQty, String criteriaSetId) {
        CriteriaSetValues criteriaSetValue = existingCriteriaValueMap.get(getKey(minQty));
        String setCodeValueId = null;
        if (criteriaSetValue == null) {
            setCodeValueId = ProductDataStore.getMinQtySetCodeValueId(ApplicationConstants.CONST_STRING_OTHER, true);
            criteriaSetValue = new CriteriaSetValues();

            criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
            criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_MINIMUM_QUANTITY);
            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
            criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setCriteriaSetId(criteriaSetId);
            criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
            criteriaSetValue.setValue(getValueObject(minQty));
        }

        return criteriaSetValue;
    }

    protected void generateExistingCriteriaList(List<CriteriaSetValues> criteriaSetValues) {
        existingCriteriaValueMap = getExistingCriteriaSetValues(criteriaSetValues);
    }

    private Value[] getValueObject(MinimumOrder mino) {
        Value value = new Value();

        value.setUnitValue(mino.getValue());
        value.setUnitOfMeasureCode(getUnitOfMeasureCode(ApplicationConstants.CONST_MINIMUM_QUANTITY, mino.getUnit()));
        value.setCriteriaAttributeId(getCriteriaSetAttributeId(ApplicationConstants.CONST_MINIMUM_QUANTITY));
        // String uom = getUnitOfMeasureCode(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE, "Business days");
        return new Value[] { value };
    }

    protected String getKey(MinimumOrder mino) {
        return mino.getValue() + "===" + mino.getValue();
    }

    private Map<String, CriteriaSetValues> getExistingCriteriaSetValues(List<CriteriaSetValues> criteriaSetValues) {
        Map<String, CriteriaSetValues> finalCriteriaValueMap = new HashMap<String, CriteriaSetValues>();
        for (CriteriaSetValues criteriaValue : criteriaSetValues) {
            if (criteriaValue != null) {
                String key = getKeyFromValue(criteriaValue.getValue());
                finalCriteriaValueMap.put(key, criteriaValue);
            }
        }
        return finalCriteriaValueMap;
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
