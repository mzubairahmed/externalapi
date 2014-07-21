package com.asi.ext.api.product.criteria.processor;

import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

public class SameDayServiceProcessor {

    private final static Logger LOGGER             = Logger.getLogger(SameDayServiceProcessor.class.getName());

    private int                 criteriaSetId      = -15;
    private int                 criteriaSetValueId = 0;

    /**
     * @param criteriaSetId
     * @param criteriaSetValueId
     */
    public SameDayServiceProcessor(int criteriaSetValueId) {
        this.criteriaSetValueId = criteriaSetValueId;
    }

    public ProductCriteriaSets getCriteriaSetForSameDayService(ProductCriteriaSets matchedCriteriaSet, Product product,
            String sameDayRush, String configId) throws VelocityException {
        if (matchedCriteriaSet != null) {
            if (sameDayRush.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                return null;
            } else if (sameDayRush.equalsIgnoreCase(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)
                    || CommonUtilities.getBooleanValueFromYesOrNo(sameDayRush)) {
                // TODO : Register SDRU for Price reference
                if (matchedCriteriaSet.getCriteriaSetValues() != null && matchedCriteriaSet.getCriteriaSetValues().length > 0) {
                    CriteriaSetValues setValue = matchedCriteriaSet.getCriteriaSetValues()[0];
                    new ProductDataStore().updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE, "Y", setValue.getId());
                }
                return matchedCriteriaSet;
            } else if (!CommonUtilities.getBooleanValueFromYesOrNo(sameDayRush)) {
                return null;
            }
        } else if (sameDayRush.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
                || sameDayRush.equalsIgnoreCase(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)
                || !CommonUtilities.getBooleanValueFromYesOrNo(sameDayRush)) {
            return null;
        }
        LOGGER.info("Started processing SameDayRush Service ");

        criteriaSetId--;
        ProductCriteriaSets productCriteriaSet = new ProductCriteriaSets();
        productCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
        productCriteriaSet.setCompanyId(product.getCompanyId());
        productCriteriaSet.setCriteriaSetId(String.valueOf(criteriaSetId));
        productCriteriaSet.setProductId(product.getId());
        productCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        productCriteriaSet.setConfigId(configId);

        String setCodeValueId = ProductDataStore.getSetCodeValueIdForSameDayService(sameDayRush);
        if (setCodeValueId != null) {
            CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
            criteriaSetValue.setCriteriaSetId(productCriteriaSet.getCriteriaSetId());
            criteriaSetValueId--;
            criteriaSetValue.setId(criteriaSetValueId + "");
            criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
            new ProductDataStore().updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(),
                    ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE, "Y", criteriaSetValue.getId());

            criteriaSetValue.setCriteriaValueDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                    .getCriteriaSetValues()[0].getCriteriaValueDetail());
            criteriaSetValue
                    .setIsSubset(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getCriteriaSetValues()[0]
                            .getIsSubset());
            criteriaSetValue.setIsSetValueMeasurement(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                    .getCriteriaSetValues()[0].getIsSetValueMeasurement());

            CriteriaSetCodeValues[] criteriaSetCodeValues = new CriteriaSetCodeValues[1];

            CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();
            criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
            criteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValueId + "");

            criteriaSetCodeValues[0] = criteriaSetCodeValue;

            criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
            criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);

            // Value processing
            criteriaSetValue.setValue(sameDayRush);

            productCriteriaSet.setCriteriaSetValues(new CriteriaSetValues[] { criteriaSetValue });

            return productCriteriaSet;
        }
        LOGGER.info("Completed processing SameDayRush Service ");
        return null;
    }
}
