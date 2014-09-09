package com.asi.ext.api.product.criteria.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Artwork;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductArtworkProcessor extends SimpleCriteriaProcessor {

    protected Map<String, CriteriaSetValues> existingCriteriaValueMap = new HashMap<String, CriteriaSetValues>();

    public CriteriaSetValues getArtworkCriteriaSetValue(String xid, Artwork artworks, String criteriaSetId) {
            
            CriteriaSetValues criteriaSetValue = null;
            if (existingCriteriaValueMap != null) {
                criteriaSetValue = existingCriteriaValueMap.get(artworks.getValue().toUpperCase());
            }
            // Check again, if not exist create new one, else update code value
            if (criteriaSetValue == null) {
                criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_ARTWORK_CODE);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(criteriaSetId);
                
                
                String setCodeValueId = getSetCodeValueId(artworks.getValue());
                if (CommonUtilities.isValueNull(setCodeValueId)) {
                    setCodeValueId = getSetCodeValueId(artworks.getValue(), true);
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                } 
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId(),artworks.getComments()));
                
                criteriaSetValue.setCriteriaValueDetail(artworks.getComments());
                //criteriaSetValue.getCriteriaSetCodeValues()[0].setCodeValue();
                
                criteriaSetValue.setValue(artworks.getValue());
            } else {
                criteriaSetValue.setCriteriaValueDetail(artworks.getComments());
                //criteriaSetValue.getCriteriaSetCodeValues()[0].setCodeValue(artworks.getComments()); 
            }
            
            updateReferenceTable(xid, ApplicationConstants.CONST_ARTWORK_CODE, artworks.getValue(), criteriaSetValue);
        
        return criteriaSetValue;

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
        return ProductDataStore.getArtworkSetCodeValueId(value, false);
    }

    public String getSetCodeValueId(String value, boolean checkOther) {
        // TODO Auto-generated method stub
        return ProductDataStore.getArtworkSetCodeValueId(value, checkOther);
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
    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }
}
