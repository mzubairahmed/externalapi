package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

public class RushTimeProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(RushTimeProcessor.class.getName());

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param configId
     */
    public RushTimeProcessor(int uniqueSetValueId, String configId) {
        this.configId = configId;
    }

    public ProductCriteriaSets getCriteriaSetForRushService(String rushServiceFlag, Product existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentCriteriaId) {
        boolean rushService = false;
        CriteriaSetValues rushServiceCriteriaSetValue = null;
        if (matchedCriteriaSet != null) {
            rushServiceCriteriaSetValue = getExisitingRushService(matchedCriteriaSet.getCriteriaSetValues());
            if (rushServiceCriteriaSetValue != null && !CommonUtilities.isUpdateNeeded(rushServiceFlag)) {
                rushService = CommonUtilities.getBooleanValueFromYesOrNo(rushServiceFlag);
            } else if (rushServiceCriteriaSetValue != null && CommonUtilities.isUpdateNeeded(rushServiceFlag)) {
                rushService = CommonUtilities.getBooleanValueFromYesOrNo(rushServiceFlag);
            } else if (rushServiceCriteriaSetValue == null && CommonUtilities.isUpdateNeeded(rushServiceFlag)) {
                rushService = CommonUtilities.getBooleanValueFromYesOrNo(rushServiceFlag);
            }
        } else {
            rushService = CommonUtilities.getBooleanValueFromYesOrNo(rushServiceFlag);
        }
        if (matchedCriteriaSet == null && rushService) {
            matchedCriteriaSet = new ProductCriteriaSets();
            // Set Basic elements
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getId());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(this.configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        if (rushServiceCriteriaSetValue == null && rushService) {
            // If no match found in the existing list
            // Set basic properties for a criteriaSetValue
            String setCodeValueId = getSetCodeValueId(ApplicationConstants.CONST_STRING_RUSH_SERVICE);
            rushServiceCriteriaSetValue = new CriteriaSetValues();
            rushServiceCriteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
            rushServiceCriteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
            rushServiceCriteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
            rushServiceCriteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            rushServiceCriteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            rushServiceCriteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
            rushServiceCriteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId,
                    String.valueOf(--uniqueSetValueId)));
            rushServiceCriteriaSetValue.setValue(ApplicationConstants.CONST_STRING_RUSH_SERVICE);

        } else if (rushServiceCriteriaSetValue != null && !rushService) { // Remove existing value
            // remove existing value for rush service from criteria setValue
            List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<CriteriaSetValues>();
            for (CriteriaSetValues criteriaSetValue : matchedCriteriaSet.getCriteriaSetValues()) {
                if (!criteriaSetValue.getCriteriaSetId().equalsIgnoreCase(rushServiceCriteriaSetValue.getCriteriaSetId())) {
                    finalCriteriaSetValues.add(criteriaSetValue);
                }
            }
            matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues.toArray(new CriteriaSetValues[0]));
        }
        return matchedCriteriaSet;
    }

    private CriteriaSetValues getExisitingRushService(CriteriaSetValues[] existingCriteriaSetValues) {
        if (existingCriteriaSetValues != null && existingCriteriaSetValues.length > 0) {
            for (CriteriaSetValues criteriaSetValue : existingCriteriaSetValues) {
                // For rush service criteria set value will be a string type otherwise it will be Value[]
                if (criteriaSetValue != null && criteriaSetValue.getValue() instanceof String) {
                    return criteriaSetValue;
                }
            }
        }
        return null;
    }

    @Override
    public ProductCriteriaSets getCriteriaSet(String values, Product existingProduct, ProductCriteriaSets matchedCriteriaSet,
            int currentSetValueId) {
        CriteriaSetValues rushService = null;
        if (!updateNeeded(matchedCriteriaSet, values)) {
            if (matchedCriteriaSet != null) {
                rushService = getExisitingRushService(matchedCriteriaSet.getCriteriaSetValues());
                if (rushService != null) {
                    matchedCriteriaSet.setCriteriaSetValues(new CriteriaSetValues[]{rushService});
                } else {
                   return null;
                }
            }
            return null;
        }

        LOGGER.info("Started Processing of Rush Time values of product " + values);
        String[] finalValues = processValues(values);
        List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<>();

        boolean checkExistingElements = matchedCriteriaSet != null;

        HashMap<String, CriteriaSetValues> existingValueMap = new HashMap<String, CriteriaSetValues>();
        if (checkExistingElements) {
            existingValueMap = createTableForExistingSetValue(matchedCriteriaSet.getCriteriaSetValues());
            if (rushService == null) {
                rushService = getExisitingRushService(matchedCriteriaSet.getCriteriaSetValues());               
            }
        } else {
            matchedCriteriaSet = new ProductCriteriaSets();
            // Set Basic elements
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getId());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(this.configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (String rushTime : finalValues) {
            if (rushTime != null) {
                rushTime = rushTime.trim();
            }
            String setCodeValueId = getSetCodeValueId(rushTime);
            CriteriaSetValues criteriaSetValue = null;
            Value value = getValueForRushTime(existingProduct.getExternalProductId(), rushTime);
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
                    criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);
                    criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                    criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                    criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                    criteriaSetValue.setValue(new Value[] { value });
                }
            }
            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE,
                    rushTime, criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }
        LOGGER.info("Completed Processing of ProductTime of product " + values);
        if (rushService != null) {
            finalCriteriaSetValues.add(rushService);
        }
        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues.toArray(new CriteriaSetValues[0]));
        return matchedCriteriaSet;
    }

    private Value getValueForRushTime(String externalProductId, String rushTime) {
        if (CommonUtilities.isValidProductionTime(rushTime)) {
            String criteriaSetAttributeId = getCriteriaSetAttributeId(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
            String unitOfMeasureCode = getUnitOfMeasureCode(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE, "Business days");

            if (criteriaSetAttributeId != null && unitOfMeasureCode != null) {
                Value value = new Value();
                value.setCriteriaAttributeId(criteriaSetAttributeId);
                value.setUnitOfMeasureCode(unitOfMeasureCode);
                value.setUnitValue(String.valueOf(rushTime).trim());

                return value;
            } else {
                addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
                        "One of the required attribute not found in the Rush Time lookup data " + rushTime);
                return null;
            }

        } else {
            addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                    "Invalid value found for Rush Time " + rushTime);
        }
        return null;
    }

    private HashMap<String, CriteriaSetValues> createTableForExistingSetValue(CriteriaSetValues[] existingCriteriaSetValues) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Started createTableForExistingSetValue(), " + System.currentTimeMillis());
        }
        HashMap<String, CriteriaSetValues> existing = new HashMap<String, CriteriaSetValues>(existingCriteriaSetValues.length);

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
        return ProductDataStore.getSetCodeValueIdForRushTime(value);
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
        if (criteriaSet.getCriteriaSetValues() != null && criteriaSet.getCriteriaSetValues().length > 0) {
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

}
