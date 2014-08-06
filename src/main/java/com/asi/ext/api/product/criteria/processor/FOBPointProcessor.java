/**
 * 
 */
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

/**
 * @author krahul
 * 
 */
public class FOBPointProcessor extends SimpleCriteriaProcessor {

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    private String companyId = null;
    private final static Logger LOGGER              = Logger.getLogger(FOBPointProcessor.class.getName());

    public FOBPointProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getFOBPCriteriaSet(List<String> fobPoints, ProductDetail product,
            ProductCriteriaSets matchedCriteriaSet, String configId) {
        this.configId = configId;
        this.companyId = product.getCompanyId();
        return getCriteriaSet(CommonUtilities.convertStringListToCSV(fobPoints), product, matchedCriteriaSet, 0);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#getCriteriaSet(java.lang.String,
     * com.asi.service.product.client.vo.ProductDetail, com.asi.service.product.client.vo.ProductCriteriaSets, int)
     */
    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {

        if (!updateNeeded(matchedCriteriaSet, values)) {
            return null;
        }
        LOGGER.info("Started Processing of Product FOB Points");
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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_CRITERIA_CODE_FOBP);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (String value : finalValues) {
            String setCodeValueId = getSetCodeValueId(value);

            if (CommonUtilities.isValueNull(setCodeValueId)) {
                // LOG Batch Error
                addErrorToBatchLogCollection(existingProduct.getExternalProductId(),
                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "FOB Point " + value
                                + " dosen't exists in lookup values");
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
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_CRITERIA_CODE_FOBP);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                criteriaSetValue.setValue(value);
            }

            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_CRITERIA_CODE_FOBP, value, criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);
        LOGGER.info("Completed Processing of FOB Points");

        return matchedCriteriaSet;
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
    
    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#getSetCodeValueId(java.lang.String)
     */
    @Override
    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForFobPoints(value, companyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#isValueIsValid(java.lang.String)
     */
    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#processValues(java.lang.String)
     */
    @Override
    protected String[] processValues(String value) {
        return CommonUtilities.getOriginalCSVValues(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.asi.ext.api.product.criteria.processor.SimpleCriteriaProcessor#updateCriteriaSet(java.lang.String)
     */
    @Override
    protected boolean updateCriteriaSet(String value) {
        // TODO Auto-generated method stub
        return false;
    }

}
