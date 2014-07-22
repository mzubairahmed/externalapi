package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public abstract class SimpleCriteriaProcessor {

    protected static int          uniqueSetValueId = -12;

    protected Map<String, String> resourceUrls     = new HashMap<String, String>();
    private ProductDataStore      productDataStore = new ProductDataStore();

    protected abstract ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet, int currentSetValueId);

    public abstract String getSetCodeValueId(String value);

    protected abstract boolean isValueIsValid(String value);

    protected abstract String[] processValues(String value);

    protected abstract boolean updateCriteriaSet(String value);

    protected void updateReferenceTable(String externalProductId, String criteriaCode, String value,
            com.asi.service.product.client.vo.CriteriaSetValues criteriaSetValue) {
        productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, criteriaCode, value, criteriaSetValue.getId());
    }

    public String processSourceCriteriaValueByCriteriaCode(String sourceValue, String criteriaCode) {
        if (!CommonUtilities.isValueNull(sourceValue)) {
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE)) {
                if (sourceValue.contains("\\|")) {

                }
                sourceValue = sourceValue.replace("|", "-").trim();
                return sourceValue;
            } else if (sourceValue.contains("=")) {
                String elements[] = sourceValue.split("=");
                return elements.length > 1 ? elements[1] : elements[0];
            } else {
                return sourceValue.trim();
            }
        } else {
            return sourceValue;
        }
    }

    protected void addErrorToBatchLogCollection(String externalPrdId, String batchErrCode, String message) {
        productDataStore.addErrorToBatchLogCollection(externalPrdId, batchErrCode, message);
    }

    protected boolean updateNeeded(ProductCriteriaSets criteriaSet, String value) {
        if (criteriaSet == null && CommonUtilities.isValueNull(value)) {
            return false;
        } else if (CommonUtilities.isValueNull(value)) {
            return false;
        } else {
            return true;
        }
    }

    protected CriteriaSetCodeValues[] getCriteriaSetCodeValues(String setCodeValueId, String setValueId) {
        CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();

        criteriaSetCodeValue.setCriteriaSetValueId(setValueId);
        criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
        criteriaSetCodeValue.setCodeValue("");
        criteriaSetCodeValue.setId(ApplicationConstants.CONST_STRING_ZERO);

        return new CriteriaSetCodeValues[] { criteriaSetCodeValue };
    }

    @SuppressWarnings("unchecked")
    protected String getKeyFromValue(CriteriaSetValues value) {
        String finalKeyForValue = "";
        if (value.getValue() instanceof String) {
            finalKeyForValue = String.valueOf(value).toUpperCase();
        } else if (value.getValue() instanceof ArrayList) {
            finalKeyForValue = getKeyValueFromValueMap((List<HashMap<String, String>>) value.getValue());
        } else if (value.getValue() instanceof Value[]) {
            finalKeyForValue = getKeyValueFromValueArray((Value[]) value.getValue());
        }
        return finalKeyForValue;
    }

    @SuppressWarnings("unchecked")
    protected String getKeyFromValue(Object value) {
        String finalKeyForValue = "";
        if (value instanceof String) {
            finalKeyForValue = String.valueOf(value).toUpperCase();
        } else if (value instanceof ArrayList) {
            finalKeyForValue = getKeyValueFromValueMap((List<HashMap<String, String>>) value);
        } else if (value instanceof Value[]) {
            finalKeyForValue = getKeyValueFromValueArray((Value[]) value);
        } else if (value instanceof Value) {
            finalKeyForValue = getKeyValueFromValue((Value) value);
        }
        return finalKeyForValue;
    }

    private String getKeyValueFromValueArray(Value[] values) {
        String finalValue = "";
        for (Value v : values) {
            if (v != null) {
                if (finalValue == "") {
                    finalValue = String.valueOf(v.getCriteriaAttributeId()) + "===" + String.valueOf(v.getUnitValue()) + "==="
                            + String.valueOf(v.getUnitOfMeasureCode());
                } else {
                    finalValue += "++" + String.valueOf(v.getCriteriaAttributeId()) + "===" + String.valueOf(v.getUnitValue())
                            + "===" + String.valueOf(v.getUnitOfMeasureCode());
                }
            }
        }
        return finalValue.toUpperCase();
    }

    private String getKeyValueFromValue(Value v) {
        String finalValue = "";
        if (v != null) {
            if (finalValue == "") {
                finalValue = String.valueOf(v.getCriteriaAttributeId()) + "===" + String.valueOf(v.getUnitValue()) + "==="
                        + String.valueOf(v.getUnitOfMeasureCode());
            }
        }
        return finalValue.toUpperCase();
    }

    private String getKeyValueFromValueMap(List<HashMap<String, String>> values) {
        String finalValue = "";
        for (HashMap<String, String> v : values) {
            if (v != null) {
                if (finalValue == "") {
                    finalValue = String.valueOf(v.get("CriteriaAttributeId")) + "===" + String.valueOf(v.get("UnitValue")) + "==="
                            + String.valueOf(v.get("UnitOfMeasureCode"));
                } else {
                    finalValue += "++" + String.valueOf(v.get("CriteriaAttributeId")) + "===" + String.valueOf(v.get("UnitValue"))
                            + "===" + String.valueOf(v.get("UnitOfMeasureCode"));
                }
            }
        }
        return finalValue.toUpperCase();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getUnitValueFromValue(Object valueObject, Integer index) {
        if (valueObject == null) {
            return null;
        } else if (valueObject instanceof Value) {
            return ((Value) valueObject).getUnitValue();
        } else if (valueObject instanceof Value[]) {
            if (index != null) {
                Value[] tempValues = (Value[]) valueObject;
                if (index > tempValues.length) {
                    return null;
                }
                index = index == null ? 1 : index;
                int counter = 0;
                for (Value v : tempValues) {
                    if (index == ++counter) {
                        return v.getUnitValue();
                    }
                }
            }
        } else if (valueObject instanceof ArrayList) {
            ArrayList<LinkedHashMap<?, ?>> tempArrayList = (ArrayList) valueObject;
            if (index > tempArrayList.size()) {
                return null;
            }

            index = index == null ? 1 : index;
            int counter = 0;
            for (LinkedHashMap<?, ?> tempMap : tempArrayList) {
                if (index == ++counter) {
                    return String.valueOf(tempMap.get("UnitValue"));
                }
            }
        }
        return null;
    }

    protected String getCriteriaSetAttributeId(String criteriaCode) {
        return ProductDataStore.getCriteriaSetAttributeId(criteriaCode);
    }

    protected String getUnitOfMeasureCode(String criteriaCode, String unit) {
        return ProductDataStore.getUnitOfMeasureCode(criteriaCode, unit.trim());
    }

    protected String doReverseLookup(String setCodeValueId, String criteriaCode) {
        return ProductDataStore.reverseLookupFindAttribute(setCodeValueId, criteriaCode);
    }

    // public abstract boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId);
}
