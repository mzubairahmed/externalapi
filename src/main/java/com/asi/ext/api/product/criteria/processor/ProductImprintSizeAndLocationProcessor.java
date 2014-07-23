package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.service.model.ImprintSizeLocation;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductImprintSizeAndLocationProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(ProductImprintSizeAndLocationProcessor.class.getName());

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductImprintSizeAndLocationProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getProductImprintSizeAndLocationCriteriaSet(List<ImprintSizeLocation> imprintSizeAndLocations,
            ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet, String configId) {

        if (imprintSizeAndLocations == null || imprintSizeAndLocations.isEmpty()) {
            return null;
        }
        this.configId = configId;

        return getCriteriaSet(getImprintSizeLocationString(imprintSizeAndLocations), existingProduct, matchedCriteriaSet, 0);
    }

    private String getImprintSizeLocationString(List<ImprintSizeLocation> imprintSizeLocations) {
        String finalImprintSizeAndLocation = "";
        for (ImprintSizeLocation imsz : imprintSizeLocations) {
            finalImprintSizeAndLocation = finalImprintSizeAndLocation.isEmpty() ? finalImprintSizeAndLocation + imsz.getSize()
                    + "|" + imsz.getLocation() : finalImprintSizeAndLocation + "," + imsz.getSize() + "|" + imsz.getLocation();
        }
        return finalImprintSizeAndLocation;
    }

    @Override
    public ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct, ProductCriteriaSets matchedCriteriaSet,
            int currentSetValueId) {

        if (!updateNeeded(matchedCriteriaSet, values)) {
            return null;
        }

        LOGGER.info("Started processing imprint size and location");
        // First verify and process value to desired format

        if (!isValueIsValid(values)) {
            return null;
        }

        String[] finalValues = processValues(values);
        List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<>();

        boolean checkExistingElements = matchedCriteriaSet != null;

        Map<String, CriteriaSetValues> existingValueMap = new HashMap<String, CriteriaSetValues>();
        if (checkExistingElements) {
            existingValueMap = createTableForExistingSetValue(matchedCriteriaSet.getCriteriaSetValues());
        } else {
            matchedCriteriaSet = new ProductCriteriaSets();
            // Set Basic elements
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getID());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(this.configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (String value : finalValues) {
            String setCodeValueId = getSetCodeValueId(value);

            if (CommonUtilities.isValueNull(setCodeValueId)) {
                addErrorToBatchLogCollection(existingProduct.getExternalProductId(),
                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Shape Value " + value
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
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                criteriaSetValue.setValue(value);
            }

            updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE,
                    value, criteriaSetValue);

            finalCriteriaSetValues.add(criteriaSetValue);
        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);

        LOGGER.info("Completed processing imprint size and location");
        return matchedCriteriaSet;

    }

    @Override
    public String getSetCodeValueId(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    private Map<String, CriteriaSetValues> createTableForExistingSetValue(List<CriteriaSetValues> existingCriteriaSetValues) {
        Map<String, CriteriaSetValues> existing = new HashMap<String, CriteriaSetValues>();
        try {
            for (CriteriaSetValues setValues : existingCriteriaSetValues) {
                existing.put(String.valueOf(setValues.getValue()), setValues);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while processing existing imprint size value , " + e.getMessage());
        }
        return existing;
    }

    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String[] processValues(String value) {

        /*if (value.contains(",")) {
            // x1,x2|y1,y2
            String[] imprintAttributes = value.split("\\|");
            String[] imprintSizes = imprintAttributes.length > 0 ? imprintAttributes[0].split(",") : new String[] {};
            String[] imprintLocation = imprintAttributes.length > 1 ? imprintAttributes[1].split(",") : new String[] {};
            // if the array length is not matching then make it equal
            if (imprintSizes.length != imprintLocation.length) {
                if (imprintSizes.length > imprintLocation.length) {

                    String[] tempArray = new String[imprintSizes.length];
                    Arrays.fill(tempArray, "");
                    System.arraycopy(imprintLocation, 0, tempArray, 0, imprintLocation.length);

                    imprintLocation = tempArray;
                } else if (imprintLocation.length > imprintSizes.length) {
                    String[] tempArray = new String[imprintLocation.length];
                    Arrays.fill(tempArray, "");
                    System.arraycopy(imprintSizes, 0, tempArray, 0, imprintSizes.length);

                    imprintSizes = tempArray;
                }
            }
            if (imprintSizes.length == imprintLocation.length) {
                value = "";
                for (int imprintCntr = 0; imprintCntr < imprintSizes.length; imprintCntr++) {
                    if (imprintCntr < imprintSizes.length - 1)
                        value += imprintSizes[imprintCntr] + "|" + imprintLocation[imprintCntr] + ",";
                    else
                        value += imprintSizes[imprintCntr] + "|" + imprintLocation[imprintCntr];
                }
            }

        }*/
        return value.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // TODO Auto-generated method stub
        return false;
    }

}
