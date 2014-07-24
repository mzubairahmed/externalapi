package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;

import com.asi.ext.api.service.model.ProductionTime;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Value;

public class ProductionTimeProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(ProductionTimeProcessor.class.getName());
    private final static String PRODUCTION_TIME     = "Production Time";

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param configId
     */
    public ProductionTimeProcessor(int uniqueSetValueId, String configId) {
        this.configId = configId;
    }

    public ProductCriteriaSets getProductionTimeCriteriaSet(List<ProductionTime> productionTimes, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {
        this.configId = configId;

        return getCriteriaSet(productionTimes, existingProduct, matchedCriteriaSet, 0);

    }

    private ProductCriteriaSets getCriteriaSet(List<ProductionTime> productionTimes, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {

        if (productionTimes == null) {
            return null;
        }

        LOGGER.info("Started Processing of Product Time of product " + productionTimes);
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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (ProductionTime productionTime : productionTimes) {
            if (productionTime == null) {
                continue;
            }
            String setCodeValueId = getSetCodeValueId(PRODUCTION_TIME);
            CriteriaSetValues criteriaSetValue = null;
            Value value = getValueForProductionTime(existingProduct.getExternalProductId(),
                    String.valueOf(productionTime.getBusinessDays()));
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
                    criteriaSetValue.setCriteriaValueDetail(productionTime.getDetails());
                    criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                    criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                    criteriaSetValue.setValue(new Value[] { value });
                }
            }
            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE,
                    String.valueOf(productionTime.getBusinessDays()), criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }
        LOGGER.info("Completed Processing of ProductTime of product " + productionTimes);
        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);
        return matchedCriteriaSet;
    }

    private Value getValueForProductionTime(String externalProductId, String productionTime) {
        if (CommonUtilities.isValidProductionTime(productionTime)) {
            String criteriaSetAttributeId = getCriteriaSetAttributeId(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
            String unitOfMeasureCode = getUnitOfMeasureCode(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE,
                    "Business days");

            if (criteriaSetAttributeId != null && unitOfMeasureCode != null) {
                Value value = new Value();
                value.setCriteriaAttributeId(criteriaSetAttributeId);
                value.setUnitOfMeasureCode(unitOfMeasureCode);
                value.setUnitValue(String.valueOf(productionTime).trim());

                return value;
            } else {
                addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
                        "One of the required attribute not found in the Production Time lookup data " + productionTime);
                return null;
            }

        } else {
            addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                    "Invalid value found for Production Time " + productionTime);
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
                existing.put(getKeyFromValue(criteriaSetValue), criteriaSetValue);
            }
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Completed createTableForExistingSetValue(), " + System.currentTimeMillis());
        }
        return existing;
    }

    @Override
    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForProductionTime(value);
    }

    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String[] processValues(String value) {
        // return CommonUtilities.removeDuplicatesFromCsv(value);
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
        LOGGER.info("Registering existing Production Time values of product");
        if (criteriaSet.getCriteriaSetValues() != null && !criteriaSet.getCriteriaSetValues().isEmpty()) {
            for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
                if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
                    String valueToRegister = null;
                    if (criteriaValues.getValue() instanceof ArrayList) {
                        valueToRegister = getUnitValueFromValue(criteriaValues.getValue(), 1);
                    } else if (criteriaValues.getValue() instanceof Value[]) {
                        Value tempValue = ((Value[]) criteriaValues.getValue())[0];
                        valueToRegister = tempValue.getUnitValue();
                    }
                    if (!CommonUtilities.isValueNull(valueToRegister)) {
                        updateReferenceTable(externalProductId, ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE,
                                valueToRegister, criteriaValues);
                    }

                }
            }
        }
        LOGGER.info("Completed existing Production Time values of product");

        return false;
    }

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

}
