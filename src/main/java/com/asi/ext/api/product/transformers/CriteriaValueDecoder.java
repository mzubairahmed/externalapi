package com.asi.ext.api.product.transformers;

import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.ProductCriteriaSets;

public class CriteriaValueDecoder {

    public ProductDataStore productDataStore = new ProductDataStore();

    /**
     * Gets array of option values
     * 
     * @param optionCriteriaSets
     * 
     * @throws NullPointerException
     */
    public void updateOptionCriteriaValuesToReference(String externalProductId, ProductCriteriaSets optionCriteriaSets) {
        CriteriaSetValues[] criteriaSetValues = optionCriteriaSets.getCriteriaSetValues();
        if (criteriaSetValues == null) {
            return;
        }

        for (CriteriaSetValues criteriaSetValue : criteriaSetValues) {
            if (criteriaSetValue != null) {
                String criteriaProcessedValue = "";

                if (criteriaSetValues.length > 1) {
                    // criteriaProcessedValue = (criteriaSetValue.getValue() +"") ? criteriaSetValue.getValue() :
                    // criteriaSetValue.getFormatValue();
                    criteriaProcessedValue = optionCriteriaSets.getCriteriaDetail() + ":" + criteriaSetValue.getValue();
                    productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, optionCriteriaSets.getCriteriaCode(),
                            criteriaProcessedValue, criteriaSetValue.getId());
                } else {
                    criteriaProcessedValue = (String) criteriaSetValue.getValue();
                    productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, optionCriteriaSets.getCriteriaCode(),
                            criteriaProcessedValue, criteriaSetValue.getId());
                }
            }
        }
    }

    public void updateSimpleCriteriaValuesToReference(String externalProductId, ProductCriteriaSets simpleCriteriaSets) {
        CriteriaSetValues[] criteriaSetValues = simpleCriteriaSets.getCriteriaSetValues();
        if (criteriaSetValues == null) {
            return;
        }

        if (criteriaSetValues != null && criteriaSetValues.length > 0) {
            for (CriteriaSetValues criteriaSetValue : criteriaSetValues) {
                if (criteriaSetValue != null) {
                    String processedCriteriaValue = "";
                    if (criteriaSetValue.getValue() != null && !criteriaSetValue.getValue().toString().isEmpty()) {
                        processedCriteriaValue = criteriaSetValue.getValue().toString();
                    } else if (criteriaSetValue.getFormatValue() != null && !criteriaSetValue.getFormatValue().toString().isEmpty()) {
                        processedCriteriaValue = criteriaSetValue.getFormatValue();
                    } else {
                        processedCriteriaValue = criteriaSetValue.getBaseLookupValue();
                    }

                    productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, simpleCriteriaSets.getCriteriaCode(),
                            processedCriteriaValue, criteriaSetValue.getId());

                }
            }
        }
    }
}
