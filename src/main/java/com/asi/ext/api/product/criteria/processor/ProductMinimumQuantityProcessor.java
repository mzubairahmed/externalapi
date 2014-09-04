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

    public CriteriaSetValues getMinQtyCriteriaSetValue(String xid, MinimumOrder minQty, String criteriaSetId) {
            String setCodeValueId = null;
        
            setCodeValueId = ProductDataStore.getMinQtySetCodeValueId(ApplicationConstants.CONST_STRING_OTHER, true);
            CriteriaSetValues criteriaSetValue = new CriteriaSetValues();

            criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
            criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_MINIMUM_QUANTITY);
            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
            criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setCriteriaSetId(criteriaSetId);
            criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
            criteriaSetValue.setValue(getValueObject(minQty));
        
            CriteriaSetValues tempCriteriaSetValue = existingCriteriaValueMap.get(getKeyFromValue(criteriaSetValue.getValue()));
            if (tempCriteriaSetValue != null) {
                updateReferenceTable(xid, ApplicationConstants.CONST_MINIMUM_QUANTITY, minQty.getValue() + " "+ minQty.getUnit(), tempCriteriaSetValue);
                return tempCriteriaSetValue;
            }
            updateReferenceTable(xid, ApplicationConstants.CONST_MINIMUM_QUANTITY, minQty.getValue() + " "+ minQty.getUnit(), criteriaSetValue);
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
        return String.valueOf(mino.getValue()) + "===" + String.valueOf(mino.getUnit());
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }
}
