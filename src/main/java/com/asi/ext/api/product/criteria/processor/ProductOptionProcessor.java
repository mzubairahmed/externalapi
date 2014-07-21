/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.product.transformers.ProductParser;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.util.ApplicationConstants;

/**
 * @author Rahul K
 * 
 */
public class ProductOptionProcessor {

    private final static Logger LOGGER           = Logger.getLogger(ProductOptionProcessor.class.getName());
    private ProductDataStore    productDataStore = new ProductDataStore();

    public void registerExistingOptionValues(String externalProductId, Map<String, List<ProductCriteriaSets>> optionCriteriaGroup) {
        LOGGER.info("Registering existing Option values");
        if (optionCriteriaGroup != null && !optionCriteriaGroup.isEmpty()) {
            for (Entry<String, List<ProductCriteriaSets>> option : optionCriteriaGroup.entrySet()) {
                if (option != null && option.getValue() != null) {
                    String optionCriteriaCode = option.getKey();
                    List<ProductCriteriaSets> optionCriteriaSets = option.getValue();
                    for (ProductCriteriaSets optionCriteriaSet : optionCriteriaSets) {
                        boolean hasMultipleValues = optionCriteriaSet.getCriteriaSetValues().length > 1;
                        for (CriteriaSetValues criteriaSetValue : optionCriteriaSet.getCriteriaSetValues()) {

                            String temp = "";
                            if (hasMultipleValues) {
                                temp = ProductParser.processOptionNameForPriceCriteria(optionCriteriaCode,
                                        optionCriteriaSet.getCriteriaDetail(), String.valueOf(criteriaSetValue.getValue()));
                                productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, optionCriteriaCode, temp,
                                        criteriaSetValue.getId());
                            } else {
                                temp = ProductParser.processOptionNameForPriceCriteria(optionCriteriaCode,
                                        optionCriteriaSet.getCriteriaDetail(), String.valueOf(criteriaSetValue.getValue()));
                                productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, optionCriteriaCode, temp,
                                        criteriaSetValue.getId());
                                // This is a Work around, need to remove this once Options module completed
                                productDataStore.updateCriteriaSetValueReferenceTable(externalProductId, optionCriteriaCode,
                                        String.valueOf(criteriaSetValue.getValue()), criteriaSetValue.getId());

                            }

                        }

                    }
                }
            }
        } else if (ApplicationConstants.TRACE_ENABLED) {
            LOGGER.trace("There is nothing to register");
        }
        LOGGER.info("Completed registering existing Option values");
    }

}
