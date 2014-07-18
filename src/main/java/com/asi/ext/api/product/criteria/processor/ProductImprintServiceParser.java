package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.product.transformers.ProductParser;
import com.asi.ext.api.radar.lookup.model.SetCodeValueJsonModel;
import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.RestAPIProperties;

public class ProductImprintServiceParser extends ProductParser {

    private static ConcurrentHashMap<String, SetCodeValueJsonModel> rushTimeSetCodeValues       = null;
    private static ConcurrentHashMap<String, SetCodeValueJsonModel> sameDayServiceSetCodeValues = null;

    public ProductCriteriaSets createCriteriaSetForRushTime(Product extProduct, Product product, String rushTimeValue,
            String criteriaCode) throws VelocityException {
        if (rushTimeValue != null) {

            if (rushTimeSetCodeValues == null || rushTimeSetCodeValues.isEmpty()) {
                String rushTimeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                        .get(ApplicationConstants.RUSH_TIME_LOOKUP));
                rushTimeSetCodeValues = jsonProcessorObj
                        .getSetCodeValuesForIndividualCriteriaCode(rushTimeWSResponse, criteriaCode);

                if (rushTimeSetCodeValues == null || rushTimeSetCodeValues.isEmpty()) {
                    // TODO : ADD batch error and return null;
                    return null;
                }
            }
            criteriaSetUniqueId--;
            boolean noRushTimeDaysFound = false;
            ProductCriteriaSets productCriteriaSet = new ProductCriteriaSets();
            productCriteriaSet.setCriteriaCode(criteriaCode);
            productCriteriaSet.setCompanyId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getCompanyId());
            productCriteriaSet.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
            productCriteriaSet.setProductId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getProductId());
            productCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            productCriteriaSet.setConfigId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getConfigId());

            if (CommonUtilities.isValueNull(rushTimeValue)) {
                rushTimeValue = ApplicationConstants.CONST_STRING_RUSH_SERVICE;
                noRushTimeDaysFound = true;
            }
            List<CriteriaSetValues> rushTimeCriteriaSetValuesList = new ArrayList<CriteriaSetValues>();
            String[] rushTimeValues = rushTimeValue.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
            for (int i = 0; i < rushTimeValues.length; i++) {
                // check RUSH Time value is integer or not
                rushTimeValues[i] = rushTimeValues[i].trim();
                if (!noRushTimeDaysFound && !CommonUtilities.isValidProductionTime(rushTimeValues[i])) {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid value found for Rush Time "
                                    + rushTimeValues[i]);
                    continue;
                }
                // Add lookup logic
                String setCodeValueId = rushTimeSetCodeValues.get(ApplicationConstants.CONST_STRING_OTHER) != null ? rushTimeSetCodeValues
                        .get(ApplicationConstants.CONST_STRING_OTHER).getId() : null; // Find from setCodeValues of Rush time
                if (setCodeValueId != null) {
                    CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
                    criteriaSetValue.setCriteriaSetId(productCriteriaSet.getCriteriaSetId());
                    criteriaSetValuesID = criteriaSetValuesID - 1;
                    criteriaSetValue.setId(criteriaSetValuesID + "");
                    criteriaSetValue.setCriteriaCode(criteriaCode);

                    criteriaSetValue.setCriteriaValueDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getCriteriaValueDetail());
                    criteriaSetValue.setIsSubset(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getIsSubset());
                    criteriaSetValue.setIsSetValueMeasurement(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                            .getCriteriaSetValues()[0].getIsSetValueMeasurement());
                    if (noRushTimeDaysFound) {
                        productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), criteriaCode,
                                processSourceCriteriaValueByCriteriaCode("Y", criteriaCode), criteriaSetValue.getId());
                    } else {
                        productDataStore
                                .updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), criteriaCode,
                                        processSourceCriteriaValueByCriteriaCode(rushTimeValues[i], criteriaCode),
                                        criteriaSetValue.getId());
                    }
                    CriteriaSetCodeValues[] criteriaSetCodeValues = new CriteriaSetCodeValues[1];

                    CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();
                    criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
                    criteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValuesID + "");

                    criteriaSetCodeValues[0] = criteriaSetCodeValue;

                    criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);

                    // Value processing

                    if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE) && !noRushTimeDaysFound) {
                        Value value = new Value();
                        value.setCriteriaAttributeId("14");
                        value.setUnitValue(rushTimeValues[i]);
                        value.setUnitOfMeasureCode("BUSI");

                        Value[] valueAry = new Value[1];
                        valueAry[0] = value;
                        criteriaSetValue.setValue(valueAry);
                    } else {
                        criteriaSetValue.setValue(rushTimeValue);
                    }
                    rushTimeCriteriaSetValuesList.add(criteriaSetValue);
                } else {
                    // TODO : Add batch error
                }
            }
            if (!rushTimeCriteriaSetValuesList.isEmpty()) {
                productCriteriaSet.setCriteriaSetValues(rushTimeCriteriaSetValuesList.toArray(new CriteriaSetValues[0]));
                return productCriteriaSet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public ProductCriteriaSets createCriteriaSetForSameDayService(Product extProduct, Product product, String sameDayRush,
            String criteriaCode) throws VelocityException {
        if (sameDayRush != null) {

            if (sameDayServiceSetCodeValues == null || sameDayServiceSetCodeValues.isEmpty()) {
                String sameDayServiceWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
                        .get(ApplicationConstants.RUSH_TIME_LOOKUP));
                sameDayServiceSetCodeValues = jsonProcessorObj.getSetCodeValuesForIndividualCriteriaCode(sameDayServiceWSResponse,
                        criteriaCode);

                if (sameDayServiceSetCodeValues == null || sameDayServiceSetCodeValues.isEmpty()) {
                    // TODO : ADD batch error and return null;
                    return null;
                }
            }
            criteriaSetUniqueId--;
            ProductCriteriaSets productCriteriaSet = new ProductCriteriaSets();
            productCriteriaSet.setCriteriaCode(criteriaCode);
            productCriteriaSet.setCompanyId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getCompanyId());
            productCriteriaSet.setCriteriaSetId(String.valueOf(criteriaSetUniqueId));
            productCriteriaSet.setProductId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getProductId());
            productCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            productCriteriaSet.setConfigId(product.getProductConfigurations()[0].getProductCriteriaSets()[0].getConfigId());

            String setCodeValueId = sameDayServiceSetCodeValues.get(ApplicationConstants.CONST_STRING_OTHER) != null ? sameDayServiceSetCodeValues
                    .get(ApplicationConstants.CONST_STRING_OTHER).getId() : null; // Find from setCodeValues of Rush time
            if (setCodeValueId != null) {
                CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setCriteriaSetId(productCriteriaSet.getCriteriaSetId());
                criteriaSetValuesID = criteriaSetValuesID - 1;
                criteriaSetValue.setId(criteriaSetValuesID + "");
                criteriaSetValue.setCriteriaCode(criteriaCode);
                productDataStore.updateCriteriaSetValueReferenceTable(product.getExternalProductId().trim(), criteriaCode, "Y",
                        criteriaSetValue.getId());

                criteriaSetValue.setCriteriaValueDetail(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getCriteriaSetValues()[0].getCriteriaValueDetail());
                criteriaSetValue.setIsSubset(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getCriteriaSetValues()[0].getIsSubset());
                criteriaSetValue.setIsSetValueMeasurement(product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                        .getCriteriaSetValues()[0].getIsSetValueMeasurement());

                CriteriaSetCodeValues[] criteriaSetCodeValues = new CriteriaSetCodeValues[1];

                CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();
                criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
                criteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValuesID + "");

                criteriaSetCodeValues[0] = criteriaSetCodeValue;

                criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_CUST);

                // Value processing
                criteriaSetValue.setValue(sameDayRush);

                productCriteriaSet.setCriteriaSetValues(new CriteriaSetValues[] { criteriaSetValue });

                return productCriteriaSet;
            } else {
                // TODO : Add batch error log about setCodeValue Id not found
                return null;
            }
        }
        return null;
    }
}
