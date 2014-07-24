/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.List;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Option;

/**
 * @author Rahul K
 * 
 */
public class ProductOptionsProcessor {


    public void tryLookupData(String code) {
        code = ProductDataStore.getSetCodeValueIdForOptions("PROP");
    }
    public List<?> getOptionCriteriaSets(List<Option> options) {
        if (options == null || options.isEmpty()) {
            return null;
        }
        
        for (Option option : options) {
            
        }
        return null;
    }
    
    /*public void tempMethod() {

        // Option Type Starts Here

        if (null != optionType && !optionType.equalsIgnoreCase("null") && !optionType.equals("")
                && CommonUtilities.isUpdateNeeded(optionType, true)) {
            Map<String, List<ProductCriteriaSets>> newOptionsCriteriaSets = new HashMap<>();

            optionType = CommonUtilities.removeUpdateCharsString(optionType);
            LOGGER.info("Option Type Transformation Starts :" + optionType);
            String[] optionTypeAry = optionType.split(",");
            String[] optionValuesAry = CommonUtilities.removeUpdateCharsString(optionValues).split("\\|");
            String[] optionNamesAry = CommonUtilities.removeUpdateCharsString(optionNames).split("\\|");
            String[] reqForOrderAry = CommonUtilities.removeUpdateCharsString(reqForOrder).split("\\|");
            String[] isMultipleChoiceAllowedAry = CommonUtilities.removeUpdateCharsString(isMultipleChoiceAllowed).split("\\|");

            int optionValueCntr = 0;
            for (String crntOptionType : optionTypeAry) {
                if (crntOptionType.equalsIgnoreCase("Product Option") || crntOptionType.equalsIgnoreCase("Imprint Option")
                        || crntOptionType.equalsIgnoreCase("Shipping Option")) {
                    if (optionValuesAry.length > optionValueCntr && null != optionValuesAry[optionValueCntr]
                            && !optionValuesAry[optionValueCntr].equalsIgnoreCase("null")
                            && !optionValuesAry[optionValueCntr].isEmpty()) {
                        ProductCriteriaSets prdCriteriaSets = productParser.addCriteriaSetForCriteriaCode(existingProduct, product,
                                crntOptionType, optionValuesAry[optionValueCntr], optionNamesAry[optionValueCntr]);
                        if (prdCriteriaSets != null && CommonUtilities.isValueNull(optionNamesAry[optionValueCntr])) {
                            String temp = CommonUtilities.getValueFromCSV(optionValuesAry[optionValueCntr], 1);
                            if (!CommonUtilities.isValueNull(temp)) {
                                prdCriteriaSets.setCriteriaDetail(temp);
                            }
                        }
                        if (null != prdCriteriaSets && cntr < productCriteriaSetsAry.length) {
                            Boolean temp = CommonUtilities.getBooleanValueFromString(reqForOrderAry[optionValueCntr]);
                            if (temp != null) {
                                prdCriteriaSets.setIsRequiredForOrder(temp.toString());
                            } else {
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Req_for_order value "
                                                + reqForOrderAry[optionValueCntr] + " found for " + crntOptionType);
                            }
                            temp = CommonUtilities.getBooleanValueFromString(isMultipleChoiceAllowedAry[optionValueCntr]);

                            if (temp != null) {
                                prdCriteriaSets.setIsMultipleChoiceAllowed(temp.toString());
                            } else {
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Can_order_only_one value "
                                                + isMultipleChoiceAllowedAry[optionValueCntr] + " found for " + crntOptionType);
                            }
                            // TODO : values
                            prdCriteriaSets = ProductCompareUtil.multipleProductCriteriaSetCompareAndUpdate(product,
                                    prdCriteriaSets.getCriteriaCode(), prdCriteriaSets,
                                    optionsCriteriaSet.get(prdCriteriaSets.getCriteriaCode()), true);
                            if (newOptionsCriteriaSets.get(prdCriteriaSets.getCriteriaCode()) == null) {
                                newOptionsCriteriaSets.put(prdCriteriaSets.getCriteriaCode(), new ArrayList<ProductCriteriaSets>());
                            }
                            newOptionsCriteriaSets.get(prdCriteriaSets.getCriteriaCode()).add(prdCriteriaSets);
                            // productCriteriaSetsAry[cntr] = prdCriteriaSets;
                            cntr++;
                        } else {
                            // productCriteriaSetsAry = Arrays.copyOf(productCriteriaSetsAry,
                            // productCriteriaSetsAry.length - 1);
                            // cntr--;
                        }
                    }
                } else if (!CommonUtilities.isValueNull(crntOptionType)) {
                    LOGGER.info("Invalid Option Type" + crntOptionType);
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Option type " + crntOptionType);
                }
                optionValueCntr++;
            }
            optionsCriteriaSet = newOptionsCriteriaSets;
            LOGGER.info("Option Type Transformation End");
        } else if (CommonUtilities.isValueNull(optionType)) {
            optionsCriteriaSet = new HashMap<String, List<ProductCriteriaSets>>();
        } else if (!CommonUtilities.isUpdateNeeded(optionType)) {
            productOptionProcessor.registerExistingOptionValues(product.getExternalProductId(), optionsCriteriaSet);
        }

    }
    */
}
