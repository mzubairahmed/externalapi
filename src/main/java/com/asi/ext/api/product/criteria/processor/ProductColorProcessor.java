package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Color;
import com.asi.ext.api.service.model.Combo;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductColorProcessor extends SimpleCriteriaProcessor {

    private int                 uniqueCriteriaSetId = -1;
    private String              configId            = "0";

    private final static Logger LOGGER              = Logger.getLogger(ProductColorProcessor.class.getName());

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductColorProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getProductColorCriteriaSet(List<Color> colors, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {

        if (colors == null || colors.isEmpty()) {
            return null;
        }
        this.configId = configId;

        return getCriteriaSet(colors, existingProduct, matchedCriteriaSet, 0);
    }

    public ProductCriteriaSets getCriteriaSet(List<Color> values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {

        LOGGER.info("Started Processing of Product Colors");
        // First verify and process value to desired format

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
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (Color color : values) {

            String setCodeValueId = getSetCodeValueId(color.getName());
            if (CommonUtilities.isValueNull(setCodeValueId)) {
                // LOG Batch Error
                continue;
            }
            boolean hasCombo = (color.getCombos() != null && !color.getCombos().isEmpty());
            boolean hasAliace = !CommonUtilities.isValueNull(color.getAlias());
            String keyToSearch = null;
            if (hasCombo && !hasAliace) {
                keyToSearch = getSearchKeyForComboColor(color);
            } else {
                keyToSearch = hasAliace ? color.getAlias() : color.getName();
            }
            CriteriaSetValues criteriaSetValue = null;

            if (checkExistingElements) {

                criteriaSetValue = existingValueMap.get(keyToSearch.toUpperCase() + "_" + setCodeValueId);
            }
            if (criteriaSetValue == null) {
                // If no match found in the existing list
                // Set basic properties for a criteriaSetValue
                criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
                if (hasCombo) {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LIST);
                } else {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                }
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                CriteriaSetCodeValues[] setCodeValues = null;
                if (hasCombo) {
                    setCodeValues = getCriteriaSetCodeValuesForCombo(setCodeValueId, criteriaSetValue.getId(), color.getCombos());
                    // criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValuesForCombo(setCodeValueId,
                    // criteriaSetValue.getId(), color.getCombos()));
                } else {
                    setCodeValues = getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId());
                    // criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId,
                    // criteriaSetValue.getId()));

                }
                if (!CommonUtilities.isValueNull(color.getRGBHex()) && setCodeValues != null && setCodeValues.length > 0) {
                    setCodeValues[0].setCodeValue(color.getRGBHex());
                }
                criteriaSetValue.setCriteriaSetCodeValues(setCodeValues);

                if (hasAliace) {
                    criteriaSetValue.setValue(color.getAlias());
                } else {
                    criteriaSetValue.setValue(color.getName());
                }
            } else {
                if (hasCombo) {
                    criteriaSetValue = updateComboValues(color, criteriaSetValue, setCodeValueId);
                } else if (criteriaSetValue.getCriteriaSetCodeValues() != null && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
                    criteriaSetValue.getCriteriaSetCodeValues()[0].setCodeValue(color.getRGBHex());
                }
            }

            updateReferenceTable(
                    existingProduct.getExternalProductId(),
                    ApplicationConstants.CONST_COLORS_CRITERIA_CODE,
                    processSourceCriteriaValueByCriteriaCode(hasAliace ? color.getAlias() : color.getName(),
                            ApplicationConstants.CONST_COLORS_CRITERIA_CODE), criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);

        LOGGER.info("Completed Processing of Product Colors");

        return matchedCriteriaSet;
    }

    protected CriteriaSetCodeValues[] getCriteriaSetCodeValuesForCombo(String setCodeValueId, String setValueId,
            List<Combo> comboColors) {
        List<CriteriaSetCodeValues> setCodeValues = new ArrayList<CriteriaSetCodeValues>();

        CriteriaSetCodeValues primarySetCodeValue = new CriteriaSetCodeValues();
        primarySetCodeValue.setCriteriaSetValueId(setValueId);
        primarySetCodeValue.setSetCodeValueId(setCodeValueId);
        primarySetCodeValue.setCodeValue("");
        primarySetCodeValue.setCodeValueDetail("main");
        primarySetCodeValue.setId(ApplicationConstants.CONST_STRING_ZERO);

        setCodeValues.add(primarySetCodeValue);

        if (comboColors != null && !comboColors.isEmpty()) {
            for (Combo combo : comboColors) {
                String childColorSetCodeValId = getSetCodeValueId(combo.getName());
                if (childColorSetCodeValId == null) {
                    // TODO LOG ERROR
                    continue;
                }
                CriteriaSetCodeValues childSetCodeValue = new CriteriaSetCodeValues();
                childSetCodeValue.setCriteriaSetValueId(setValueId);
                childSetCodeValue.setSetCodeValueId(childColorSetCodeValId);
                childSetCodeValue.setCodeValue(combo.getRgbhex());
                childSetCodeValue.setCodeValueDetail(combo.getType());
                childSetCodeValue.setId(ApplicationConstants.CONST_STRING_ZERO);
                setCodeValues.add(childSetCodeValue);
            }
        }

        return setCodeValues.toArray(new CriteriaSetCodeValues[0]);
    }

    private CriteriaSetValues updateComboValues(Color color, CriteriaSetValues criteriaSetValue, String parentSetCodeValueId) {
        if (criteriaSetValue.getCriteriaSetCodeValues() != null && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
            List<CriteriaSetCodeValues> setCodeValueList = new ArrayList<CriteriaSetCodeValues>();
            CriteriaSetCodeValues parentSetCodeValue = null;
            
            for (Combo combo : color.getCombos()) {
                boolean matchFound = false;
                String setCodeValueIdForCombo = getSetCodeValueId(combo.getName());
                for (CriteriaSetCodeValues comboSetCodeValue : criteriaSetValue.getCriteriaSetCodeValues()) {
                    if (comboSetCodeValue.getSetCodeValueId().equalsIgnoreCase(parentSetCodeValueId) && parentSetCodeValue == null) {
                        parentSetCodeValue = comboSetCodeValue;
                        comboSetCodeValue.setCodeValue(color.getRGBHex());
                        setCodeValueList.add(comboSetCodeValue);
                    } else if (comboSetCodeValue.getSetCodeValueId().equalsIgnoreCase(setCodeValueIdForCombo)) {
                        comboSetCodeValue.setCodeValueDetail(combo.getType());
                        comboSetCodeValue.setCodeValue(combo.getRgbhex());
                        setCodeValueList.add(comboSetCodeValue);
                        matchFound = true;
                    }
                }
                if (!matchFound) {
                    setCodeValueList.add(getCriteriaSetCodeValue(setCodeValueIdForCombo, criteriaSetValue.getCriteriaSetId(), combo.getRgbhex(), combo.getType()));
                }
            }
            criteriaSetValue.setCriteriaSetCodeValues(setCodeValueList.toArray(new CriteriaSetCodeValues[0]));
        }
        return criteriaSetValue;
    }

    private String getSearchKeyForComboColor(Color color) {
        return color.getAlias();
    }

    protected boolean isValueIsValid(String value) {
        // For color no need to validate values
        return true;
    }

    public String getSetCodeValueId(String value) {
        return ProductDataStore.getSetCodeValueIdForProductColor(value.trim());
    }

    @Override
    protected String[] processValues(String value) {
        // Add NP Check and all
        return CommonUtilities.trimArrayValues(value.split(ApplicationConstants.CONST_STRING_COMMA_SEP));
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // if (value != null )
        return false;
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

    public boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId) {
        if (criteriaSet == null) {
            return false;
        }
        LOGGER.info("Registering existing product color values");
        if (criteriaSet.getCriteriaSetValues() != null && !criteriaSet.getCriteriaSetValues().isEmpty()) {
            for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
                if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
                    String valueToRegister = null;
                    if (CommonUtilities.isValueNull(String.valueOf(criteriaValues.getValue()))) {
                        valueToRegister = doReverseLookup(criteriaValues.getCriteriaSetCodeValues()[0].getSetCodeValueId(),
                                criteriaSet.getCriteriaCode());
                    } else {
                        valueToRegister = String.valueOf(criteriaValues.getValue());
                    }
                    updateReferenceTable(externalProductId, ApplicationConstants.CONST_COLORS_CRITERIA_CODE, valueToRegister,
                            criteriaValues);
                }
            }
        }
        LOGGER.info("Completed registering existing product color values");

        return false;
    }

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

}
