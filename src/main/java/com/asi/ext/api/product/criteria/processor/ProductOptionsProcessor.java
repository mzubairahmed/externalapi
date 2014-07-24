/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductCompareUtil;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.product.transformers.ProductParser;
import com.asi.ext.api.service.model.Option;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.ProductParserUtil;
import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

/**
 * @author Rahul K
 * 
 */
public class ProductOptionsProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER              = Logger.getLogger(ProductOptionsProcessor.class.getName());

    private int                 uniqueCriteriaSetId = 1;
    private String              configId            = "0";

    private ProductDataStore    productDataStore    = new ProductDataStore();

    /**
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductOptionsProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public Map<String, List<ProductCriteriaSets>> getOptionCriteriaSets(List<Option> options, ProductDetail existingProduct, String configId,
            Map<String, List<ProductCriteriaSets>> optionsCriteriaSet) {
        if (options == null || options.isEmpty()) {
            return null;
        }
        this.configId = configId;
        Map<String, List<ProductCriteriaSets>> newOptionsCriteriaSets = new HashMap<>();
        for (Option option : options) {
            ProductCriteriaSets optionCriteriaSet = new ProductCriteriaSets();
            String criteriaCode = ProductParserUtil.getCodeFromOptionType(option.getOptionType());
            if (criteriaCode == null) {
                LOGGER.info("Invalid Option Type " + option.getOptionType());
                productDataStore.addErrorToBatchLogCollection(existingProduct.getExternalProductId().trim(),
                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Option type " + option.getOptionType());
                continue;
            }
            String setCodeValueId = ProductDataStore.getSetCodeValueIdForOptions(criteriaCode);
            if (setCodeValueId == null) {
                // TODO : Log Error
                continue;
            }
            optionCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            optionCriteriaSet.setProductId(existingProduct.getID());
            optionCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            optionCriteriaSet.setCriteriaDetail(option.getName());
            optionCriteriaSet.setDescription(option.getAdditionalInformation());
            optionCriteriaSet.setConfigId(configId);
            optionCriteriaSet.setCriteriaCode(criteriaCode);
            optionCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            optionCriteriaSet.setIsRequiredForOrder(option.getRequiredForOrder() + "");
            optionCriteriaSet.setIsMultipleChoiceAllowed(option.getCanOnlyOrderOne() + "");
            optionCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);

            // Value Processing

            for (String value : option.getValues()) {
                CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaCode(criteriaCode);
                criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(optionCriteriaSet.getCriteriaSetId());
                criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                criteriaSetValue.setValue(value);
                if (optionCriteriaSet.getCriteriaSetValues() == null) {
                    optionCriteriaSet.setCriteriaSetValues(new ArrayList<CriteriaSetValues>());
                }
                optionCriteriaSet.getCriteriaSetValues().add(criteriaSetValue);
            }
            try {
                optionCriteriaSet = ProductCompareUtil.multipleProductCriteriaSetCompareAndUpdate(existingProduct, criteriaCode,
                        optionCriteriaSet, optionsCriteriaSet.get(criteriaCode), true);
            } catch (Exception e) {
                LOGGER.error("Exception while comparing Option criteria set", e);
            }
            if (newOptionsCriteriaSets.get(criteriaCode) == null) {
                newOptionsCriteriaSets.put(criteriaCode, new ArrayList<ProductCriteriaSets>());
            }
            newOptionsCriteriaSets.get(criteriaCode).add(optionCriteriaSet);
        }
        if (newOptionsCriteriaSets != null) {
            try {
                registerExistingOptionValues(existingProduct.getExternalProductId(), newOptionsCriteriaSets);
            } catch (Exception e) {
                LOGGER.error("Exception while registering values to datastore", e);
            }
        }
        return newOptionsCriteriaSets;
    }

    protected CriteriaSetCodeValues[] getCriteriaSetCodeValues(String setCodeValueId, String setValueId) {
        CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();

        criteriaSetCodeValue.setCriteriaSetValueId(setValueId);
        criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
        criteriaSetCodeValue.setCodeValue("");
        criteriaSetCodeValue.setId(ApplicationConstants.CONST_STRING_ZERO);

        return new CriteriaSetCodeValues[] { criteriaSetCodeValue };
    }

    public void registerExistingOptionValues(String externalProductId, Map<String, List<ProductCriteriaSets>> optionCriteriaGroup) {
        LOGGER.info("Registering existing Option values");
        if (optionCriteriaGroup != null && !optionCriteriaGroup.isEmpty()) {
            for (Entry<String, List<ProductCriteriaSets>> option : optionCriteriaGroup.entrySet()) {
                if (option != null && option.getValue() != null) {
                    String optionCriteriaCode = option.getKey();
                    List<ProductCriteriaSets> optionCriteriaSets = option.getValue();
                    for (ProductCriteriaSets optionCriteriaSet : optionCriteriaSets) {
                        boolean hasMultipleValues = optionCriteriaSet.getCriteriaSetValues().size() > 1;
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

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSetCodeValueId(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean isValueIsValid(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected String[] processValues(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * public void tempMethod() {
     * 
     * // Option Type Starts Here
     * 
     * if (null != optionType && !optionType.equalsIgnoreCase("null") && !optionType.equals("")
     * && CommonUtilities.isUpdateNeeded(optionType, true)) {
     * Map<String, List<ProductCriteriaSets>> newOptionsCriteriaSets = new HashMap<>();
     * 
     * optionType = CommonUtilities.removeUpdateCharsString(optionType);
     * LOGGER.info("Option Type Transformation Starts :" + optionType);
     * String[] optionTypeAry = optionType.split(",");
     * String[] optionValuesAry = CommonUtilities.removeUpdateCharsString(optionValues).split("\\|");
     * String[] optionNamesAry = CommonUtilities.removeUpdateCharsString(optionNames).split("\\|");
     * String[] reqForOrderAry = CommonUtilities.removeUpdateCharsString(reqForOrder).split("\\|");
     * String[] isMultipleChoiceAllowedAry = CommonUtilities.removeUpdateCharsString(isMultipleChoiceAllowed).split("\\|");
     * 
     * int optionValueCntr = 0;
     * for (String crntOptionType : optionTypeAry) {
     * if (crntOptionType.equalsIgnoreCase("Product Option") || crntOptionType.equalsIgnoreCase("Imprint Option")
     * || crntOptionType.equalsIgnoreCase("Shipping Option")) {
     * if (optionValuesAry.length > optionValueCntr && null != optionValuesAry[optionValueCntr]
     * && !optionValuesAry[optionValueCntr].equalsIgnoreCase("null")
     * && !optionValuesAry[optionValueCntr].isEmpty()) {
     * ProductCriteriaSets prdCriteriaSets = productParser.addCriteriaSetForCriteriaCode(existingProduct, product,
     * crntOptionType, optionValuesAry[optionValueCntr], optionNamesAry[optionValueCntr]);
     * if (prdCriteriaSets != null && CommonUtilities.isValueNull(optionNamesAry[optionValueCntr])) {
     * String temp = CommonUtilities.getValueFromCSV(optionValuesAry[optionValueCntr], 1);
     * if (!CommonUtilities.isValueNull(temp)) {
     * prdCriteriaSets.setCriteriaDetail(temp);
     * }
     * }
     * if (null != prdCriteriaSets && cntr < productCriteriaSetsAry.length) {
     * Boolean temp = CommonUtilities.getBooleanValueFromString(reqForOrderAry[optionValueCntr]);
     * if (temp != null) {
     * prdCriteriaSets.setIsRequiredForOrder(temp.toString());
     * } else {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Req_for_order value "
     * + reqForOrderAry[optionValueCntr] + " found for " + crntOptionType);
     * }
     * temp = CommonUtilities.getBooleanValueFromString(isMultipleChoiceAllowedAry[optionValueCntr]);
     * 
     * if (temp != null) {
     * prdCriteriaSets.setIsMultipleChoiceAllowed(temp.toString());
     * } else {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Can_order_only_one value "
     * + isMultipleChoiceAllowedAry[optionValueCntr] + " found for " + crntOptionType);
     * }
     * // TODO : values
     * prdCriteriaSets = ProductCompareUtil.multipleProductCriteriaSetCompareAndUpdate(product,
     * prdCriteriaSets.getCriteriaCode(), prdCriteriaSets,
     * optionsCriteriaSet.get(prdCriteriaSets.getCriteriaCode()), true);
     * if (newOptionsCriteriaSets.get(prdCriteriaSets.getCriteriaCode()) == null) {
     * newOptionsCriteriaSets.put(prdCriteriaSets.getCriteriaCode(), new ArrayList<ProductCriteriaSets>());
     * }
     * newOptionsCriteriaSets.get(prdCriteriaSets.getCriteriaCode()).add(prdCriteriaSets);
     * // productCriteriaSetsAry[cntr] = prdCriteriaSets;
     * cntr++;
     * } else {
     * // productCriteriaSetsAry = Arrays.copyOf(productCriteriaSetsAry,
     * // productCriteriaSetsAry.length - 1);
     * // cntr--;
     * }
     * }
     * } else if (!CommonUtilities.isValueNull(crntOptionType)) {
     * LOGGER.info("Invalid Option Type" + crntOptionType);
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Option type " + crntOptionType);
     * }
     * optionValueCntr++;
     * }
     * optionsCriteriaSet = newOptionsCriteriaSets;
     * LOGGER.info("Option Type Transformation End");
     * } else if (CommonUtilities.isValueNull(optionType)) {
     * optionsCriteriaSet = new HashMap<String, List<ProductCriteriaSets>>();
     * } else if (!CommonUtilities.isUpdateNeeded(optionType)) {
     * productOptionProcessor.registerExistingOptionValues(product.getExternalProductId(), optionsCriteriaSet);
     * }
     * 
     * }
     */
}
