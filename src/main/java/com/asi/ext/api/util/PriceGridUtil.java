/**
 * 
 */
package com.asi.ext.api.util;

import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.product.transformers.ProductDataStore;

/**
 * @author krahul
 * 
 */
public class PriceGridUtil extends CommonUtilities {

    public static String getPriceUnitFromQuantity(String quantity) {
        if (!isValueNull(quantity)) {
            String[] quantityElements = quantity.split(":");
            if (quantityElements != null && quantityElements.length > 1) {
                return quantityElements[1];
            } else if (quantityElements != null && quantityElements.length == 1) {
                return ApplicationConstants.CONST_STRING_PIECE;
            } else {
                return ApplicationConstants.CONST_STRING_PIECE;
            }
        } else {
            return ApplicationConstants.CONST_STRING_PIECE;
        }
    }

    public static String getItemsPerUnitFromQuantity(String quantiy) {
        if (!isValueNull(quantiy)) {
            String[] quantityElements = quantiy.split(":");
            if (quantityElements != null && quantityElements.length > 2) {
                return quantityElements[2];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String[] getQuantityElements(String quantity) {
        if (!isValueNull(quantity)) {
            return quantity.split(":");
        } else {
            return null;
        }
    }

    public static boolean isCriteriaCodeValidForPriceGrid(String criteriaCode, boolean isBasePrice) {
        if (CommonUtilities.isValueNull(criteriaCode)) {
            return true;
        }
        if (isBasePrice) {
            return ApplicationConstants.BASE_PRICE_CRITERIA_SET.contains(criteriaCode.trim().toUpperCase());
        } else {
            return ApplicationConstants.UPCHARGE_PRICE_CRITERIA_SET.contains(criteriaCode.trim().toUpperCase());
        }
    }

    public static String getPriceGridErrorMessage(String message, boolean isBasePrice, int pGridNumber) {
        String errorMessage = isBasePrice ? "Base PriceGrid " : "Upcharge PriceGrid ";
        errorMessage += pGridNumber;
        errorMessage += " : " + message;
        return errorMessage;
    }

    /**
     * PRS Group = CriteriaCode IN (PRTM, RUSH, SDRU )
     * 
     * @param criteriaCode
     *            is the criteriaCode to check
     * @return true if the criteria code belongs to PRS group
     */
    public static boolean isCriteriaInPRSGroup(String criteriaCode) {
        if (String.valueOf(criteriaCode).equals(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE)
                || String.valueOf(criteriaCode).equals(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE)
                || String.valueOf(criteriaCode).equals(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE)) {
            return true;
        }
        return false;
    }

    public static boolean relaxPriceValidation(String[] quantities) {
        /*
         * for (String quantity : quantities) {
         * if (!isValueNull(quantity)) {
         * String priceUnit = getPriceUnitFromQuantity(quantity);
         * if (!priceUnit.equalsIgnoreCase(ApplicationConstants.CONST_STRING_PIECE)) {
         * return true;
         * }
         * }
         * }
         */
        return true;
    }

    public static boolean isValidQuantity(String quantity) {
        boolean isValid = false;
        String[] quantityElements = getQuantityElements(quantity);
        if (quantityElements != null) {
            if (quantityElements.length > 0) {
                isValid = CommonUtilities.isInteger(quantityElements[0], true);
            }
            if (quantityElements.length > 2) {
                isValid = (CommonUtilities.isInteger(quantityElements[2], true) && !String.valueOf(quantityElements[2])
                        .equalsIgnoreCase("0"));
            }
        } else {
            return isValid;
        }
        return isValid;
    }

    public static String getPriceCriteriaCode(String priceCriteria) {
        String criteriaCode = "null";
        if (priceCriteria != null) {
            if (priceCriteria.indexOf(":") != -1) {
                criteriaCode = priceCriteria.trim().substring(0, priceCriteria.indexOf(":"));
            }
        }
        return criteriaCode;
    }

    public static String getPriceGridSubTypeCode(String criteria1, String criteria2, boolean isBasePrice) {
        String criteriaCode1 = getPriceCriteriaCode(criteria1);
        String criteriaCode2 = getPriceCriteriaCode(criteria2);
        if (!isValueNull(criteriaCode1) && isValueNull(criteriaCode2)) {
            return getPriceGridSubTypeCode(criteriaCode1, isBasePrice); // Check lookup for SubTypeCode
        } else {
            return getPriceGridSubTypeCode(null, isBasePrice); // Get Default value
        }

    }

    public static String getPriceGridSubTypeCode(String criteriaCode, boolean basePrice) {
        if (isValueNull(criteriaCode) || basePrice) {
            return basePrice ? ApplicationConstants.CONST_BASE_PRICE_GRID_CODE
                    : ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
        }
        CriteriaInfo info = ProductDataStore.getCriteriaInfoForCriteriaCode(criteriaCode);
        if (info != null) {
            return info.getDefaultPriceGridSubTypeCode();
        } else {
            return basePrice ? ApplicationConstants.CONST_BASE_PRICE_GRID_CODE
                    : ApplicationConstants.CONST_UPCHARGE_PRICE_GRID_CODE;
        }

    }
}
