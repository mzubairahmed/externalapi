package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.RushTime;
import com.asi.ext.api.service.model.RushTimeValue;
import com.asi.ext.api.service.model.SameDayRush;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Value;

public class RushTimeProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(RushTimeProcessor.class.getName());

    private final static String RUSH_SERVICE        = "Rush_Service";

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param configId
     */
    public RushTimeProcessor(int uniqueSetValueId, String configId) {
        this.uniqueCriteriaSetId = uniqueSetValueId;
        this.configId = configId;
    }

    public ProductCriteriaSets getRushTimeCriteriaSet(RushTime rushTime, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {

        this.configId = configId;
        if (rushTime != null && rushTime.isAvailable() && rushTime.getRushTimeValues() != null
                && !rushTime.getRushTimeValues().isEmpty()) {
            return getCriteriaSetForRushTimes(rushTime.getRushTimeValues(), existingProduct, matchedCriteriaSet, 0);
        } else if (rushTime != null && rushTime.isAvailable()
                && (rushTime.getRushTimeValues() == null || rushTime.getRushTimeValues().isEmpty())) {
            rushTime.setRushTimeValues(new ArrayList<RushTimeValue>());
            RushTimeValue rTimeValue = new RushTimeValue();
            rTimeValue.setBusinessDays(ApplicationConstants.CONST_STRING_RUSH_SERVICE);
            rTimeValue.setDetails("");
            rushTime.getRushTimeValues().add(rTimeValue);
            return getCriteriaSetForRushTimes(rushTime.getRushTimeValues(), existingProduct, matchedCriteriaSet, 0);
        } else {
            return null;
        }
    }

    public ProductCriteriaSets getSameDayRushTimeCriteriaSet(SameDayRush sameDayRush, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet, String configId) {

        this.configId = configId;
        if (sameDayRush != null && sameDayRush.isAvailable()) {
            return getCriteriaSetForSameDayRush(sameDayRush, existingProduct, matchedCriteriaSet, 0);
        } else {
            return null;
        }
    }
    
    public ProductCriteriaSets getCriteriaSetForSameDayRush(SameDayRush sameDayRush, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet, int currentCriteriaId) {
    	
        LOGGER.info("Started Processing of Same Day Rush of product " + sameDayRush);

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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }
        
        String setCodeValueId = getSetCodeValueIdForSDRU("");
        CriteriaSetValues criteriaSetValue = null;
//        Object value = getValueForRushTime(existingProduct.getExternalProductId(), sameDayRush.getBusinessDays());
        String value = ApplicationConstants.CONST_STRING_SAME_DAY_SERVICE;

        String key = getKeyFromValue(value);
        if (checkExistingElements) {
            criteriaSetValue = existingValueMap.get(key);
        }

        if (criteriaSetValue == null) {
            // If no match found in the existing list
            // Set basic properties for a criteriaSetValue
            criteriaSetValue = new CriteriaSetValues();
            criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
            criteriaSetValue.setCriteriaValueDetail(sameDayRush.getDetails());
            criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
            criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
            criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
            criteriaSetValue.setValue(value);
        } else {
            criteriaSetValue.setCriteriaValueDetail(sameDayRush.getDetails());
        }

//        if (sameDayRush.getBusinessDays().equalsIgnoreCase(ApplicationConstants.CONST_STRING_RUSH_SERVICE)) {
//            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE,
//                    String.valueOf(sameDayRush.getBusinessDays()), criteriaSetValue);
//        } else {
//            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE,
//                    String.valueOf(rushTime.getBusinessDays()) + " business days", criteriaSetValue);
//        }
        updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE,
                ApplicationConstants.CONST_STRING_SAME_DAY_SERVICE, criteriaSetValue);
        finalCriteriaSetValues.add(criteriaSetValue);

        if (!finalCriteriaSetValues.isEmpty()) {
            matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);
        }

        LOGGER.info("Completed Processing of Same Day Rush Time of product " + sameDayRush);

        return matchedCriteriaSet;

    }

    
    public ProductCriteriaSets getCriteriaSetForRushTimes(List<RushTimeValue> rushTimes, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentCriteriaId) {

        LOGGER.info("Started Processing of Rush Time values of product " + rushTimes);
        // String[] finalValues = processValues(values);
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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (RushTimeValue rushTime : rushTimes) {
            if (rushTime == null) {
                continue;
            }
            String setCodeValueId = getSetCodeValueId(RUSH_SERVICE);
            CriteriaSetValues criteriaSetValue = null;
            Object value = getValueForRushTime(existingProduct.getExternalProductId(), rushTime.getBusinessDays());

            if (value == null) {
                continue;
            } else {

                String key = getKeyFromValue(value);
                if (checkExistingElements) {
                    criteriaSetValue = existingValueMap.get(key);
                }

                if (criteriaSetValue == null) {
                    // If no match found in the existing list
                    // Set basic properties for a criteriaSetValue
                    criteriaSetValue = new CriteriaSetValues();
                    criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                    criteriaSetValue.setCriteriaValueDetail(rushTime.getDetails());
                    criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                    criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                    if (value instanceof String) {
                        criteriaSetValue.setValue(value);
                    } else {
                        criteriaSetValue.setValue(new Value[] { (Value) value });                        
                    }
                } else {
                    criteriaSetValue.setCriteriaValueDetail(rushTime.getDetails());
                }
            }
            if (rushTime.getBusinessDays().equalsIgnoreCase(ApplicationConstants.CONST_STRING_RUSH_SERVICE)) {
                updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE,
                        String.valueOf(rushTime.getBusinessDays()), criteriaSetValue);
            } else {
                updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE,
                        String.valueOf(rushTime.getBusinessDays()) + " business days", criteriaSetValue);
            }

            finalCriteriaSetValues.add(criteriaSetValue);
        }
        if (!finalCriteriaSetValues.isEmpty()) {
            matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);
        }
        LOGGER.info("Completed Processing of ProductTime of product " + rushTimes);

        return matchedCriteriaSet;
    }

    private Object getValueForRushTime(String externalProductId, String rushTime) {
        if (!String.valueOf(rushTime).equalsIgnoreCase(ApplicationConstants.CONST_STRING_RUSH_SERVICE)) {
            String criteriaSetAttributeId = getCriteriaSetAttributeId(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
            String unitOfMeasureCode = getUnitOfMeasureCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE, "Business days");

            if (criteriaSetAttributeId != null && unitOfMeasureCode != null) {
                Value value = new Value();
                value.setCriteriaAttributeId(criteriaSetAttributeId);
                value.setUnitOfMeasureCode(unitOfMeasureCode);
                value.setUnitValue(rushTime);

                return value;
            } else {
                addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
                        "One of the required attribute not found in the Rush Time lookup data " + rushTime);
                return null;
            }

        } else if (rushTime.equalsIgnoreCase(ApplicationConstants.CONST_STRING_RUSH_SERVICE)) {
            return ApplicationConstants.CONST_STRING_RUSH_SERVICE;
        } else {
            addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                    "Invalid value found for Rush Time " + rushTime);
        }
        return null;
    }

    private HashMap<String, CriteriaSetValues> createTableForExistingSetValue(List<CriteriaSetValues> existingCriteriaSetValues) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Started createTableForExistingSetValue(), " + System.currentTimeMillis());
        }
        HashMap<String, CriteriaSetValues> existing = new HashMap<String, CriteriaSetValues>(existingCriteriaSetValues.size());

        for (CriteriaSetValues criteriaSetValue : existingCriteriaSetValues) {
            if (criteriaSetValue != null) {
                existing.put(getKeyFromValue(criteriaSetValue.getValue()), criteriaSetValue);
            }
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Completed createTableForExistingSetValue(), " + System.currentTimeMillis());
        }
        return existing;
    }

    @Override
    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForRushTime(value);
    }

    public String getSetCodeValueIdForSDRU(String value) {
        return ProductDataStore.getSetCodeValueIdForSameDayService(value);
    }

    
    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String[] processValues(String value) {
        return CommonUtilities.getValuesFromCsv(value);
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId) {
        if (criteriaSet == null) {
            return false;
        }
        LOGGER.info("Registering existing Rush Time values of product");
        if (criteriaSet.getCriteriaSetValues() != null && !criteriaSet.getCriteriaSetValues().isEmpty()) {
            for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
                if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
                    String valueToRegister = null;
                    if (criteriaValues.getValue() instanceof ArrayList) {
                        valueToRegister = getUnitValueFromValue(criteriaValues.getValue(), 1);
                    } else if (criteriaValues.getValue() instanceof Value[]) {
                        Value tempValue = ((Value[]) criteriaValues.getValue())[0];
                        valueToRegister = tempValue.getUnitValue();
                    } else if (criteriaValues.getValue() instanceof String) {
                        valueToRegister = String.valueOf(criteriaValues.getValue());
                    }
                    if (!CommonUtilities.isValueNull(valueToRegister)) {
                        updateReferenceTable(externalProductId, ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE,
                                valueToRegister, criteriaValues);
                    }

                }
            }
        }
        LOGGER.info("Completed existing Rush Time values of product");

        return false;
    }

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

}
