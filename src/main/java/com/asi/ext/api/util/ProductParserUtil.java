/**
 * 
 */
package com.asi.ext.api.util;

import java.util.List;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.service.model.Capacity;
import com.asi.ext.api.service.model.Dimension;
import com.asi.ext.api.service.model.ShippingEstimate;
import com.asi.ext.api.service.model.Size;
import com.asi.ext.api.service.model.Value;
import com.asi.ext.api.service.model.Values;
import com.asi.ext.api.service.model.Volume;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductDataSheet;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;

/**
 * @author krahul
 * 
 */
public final class ProductParserUtil {

    private final static String PRODUCT_ID = ApplicationConstants.CONST_STRING_ZERO;
    private final static String ID         = ApplicationConstants.CONST_STRING_ZERO;

    public final static String getConfigId(List<ProductConfiguration> productConfigurations) {
        if (productConfigurations != null && productConfigurations.size() > 0) {
            return productConfigurations.get(0).getID() + "";
        } else {
            return ApplicationConstants.CONST_STRING_ZERO;
        }
    }

    public static ProductInventoryLink getInventoryLink(String inventoryLink, ProductDetail existingProduct, String companyId) {
        ProductInventoryLink productInventoryLink = null;
        if (existingProduct != null) {
            productInventoryLink = existingProduct.getProductInventoryLink() != null ? existingProduct.getProductInventoryLink()
                    : new ProductInventoryLink();
            productInventoryLink.setUrl(inventoryLink);
            productInventoryLink.setProductId(String.valueOf(existingProduct.getID()));
            productInventoryLink.setCompanyId(companyId);
        } else {
            productInventoryLink = new ProductInventoryLink();
            productInventoryLink.setUrl(inventoryLink);
            productInventoryLink.setProductId(PRODUCT_ID);
            productInventoryLink.setId(ID);
            productInventoryLink.setCompanyId(companyId);
        }

        return productInventoryLink;
    }

    public static ProductDataSheet getDataSheet(String productDataSheet, ProductDetail existingProduct, String companyId) {
        ProductDataSheet prodDataSheet = new ProductDataSheet();
        if (existingProduct != null) {
            prodDataSheet = existingProduct.getProductDataSheet() != null ? existingProduct.getProductDataSheet()
                    : new ProductDataSheet();
            prodDataSheet.setUrl(productDataSheet);
            prodDataSheet.setProductId(existingProduct.getID());
            prodDataSheet.setCompanyId(companyId);
        } else {
            prodDataSheet = new ProductDataSheet();
            prodDataSheet.setUrl(productDataSheet);
            prodDataSheet.setProductId(PRODUCT_ID);
            prodDataSheet.setId(ID);
            prodDataSheet.setCompanyId(companyId);
        }
        return prodDataSheet;
    }

    public static String getCodeFromOptionType(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        if (type.equalsIgnoreCase("Product")) {
            return ApplicationConstants.CONST_PRODUCT_OPTION;
        } else if (type.equalsIgnoreCase("Imprint")) {
            return ApplicationConstants.CONST_IMPRINT_OPTION;
        } else if (type.equalsIgnoreCase("Shipping")) {
            return ApplicationConstants.CONST_SHIPPING_OPTION;
        } else {
            return null;
        }
    }

    public static Integer getCriteriaSetValueId(String xid, String criteriaCode, Object value) {
        String result = ProductDataStore.findCriteriaSetValueIdForValue(xid, criteriaCode, String.valueOf(value));
        if (result != null) {
            return Integer.parseInt(result);
        } else {
            return null;
        }

    }

    public static String getCriteriaCodeFromCriteria(String criteria) {
        CriteriaInfo criteriaInfo = ProductDataStore.getCriteriaInfoByDescription(criteria);
        return criteria != null ? criteriaInfo.getCode() : null;
    }

    public static String getSizeValuesFromSize(Size size, String sizeCriteriaCode) {
        if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)) {
            return getDimensionValues(size.getDimension());
        } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) {
            return getCapacityValues(size.getCapacity());
        } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)) {
            return getVolumeValues(size.getVolume());
        }  else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)) {
            return getCSVSizesFromSizeModel(size.getOther().getValues());
        } else if (sizeCriteriaCode.equalsIgnoreCase("SABR") 
                || sizeCriteriaCode.equalsIgnoreCase("SAHU") 
                || sizeCriteriaCode.equalsIgnoreCase("SAIT") 
                || sizeCriteriaCode.equalsIgnoreCase("SANS") 
                || sizeCriteriaCode.equalsIgnoreCase("SAWI") 
                || sizeCriteriaCode.equalsIgnoreCase("SSNM")) {
            return getCSVSizesFromSizeModel(size.getApparel().getValues());
        }
        return "null";
    }

    private static String getDimensionValues(Dimension dims) {
        String dimValues = "";
        for (Values individualDim : dims.getValues()) {
            String individualSize = "";
            for (Value v : individualDim.getValue()) {
                if (!individualSize.isEmpty()) {
                    individualSize = CommonUtilities.appendValue(individualSize, "", ";");
                }
                individualSize = CommonUtilities.appendValue(individualSize, v.getAttribute(), "");
                individualSize = CommonUtilities.appendValue(individualSize, v.getValue(), ":");
                individualSize = CommonUtilities.appendValue(individualSize, v.getUnit(), ":");
            }

            if (!dimValues.isEmpty()) {
                dimValues = CommonUtilities.appendValue(dimValues, individualSize, ",");
            } else {
                dimValues = individualSize;
            }
        }
        if (dimValues.endsWith(",")) {
            dimValues = dimValues.substring(0, dimValues.length() - 1);
        }
        return dimValues;
    }

    private static String getCapacityValues(Capacity caps) {
        String capsValues = "";
        for (Value v : caps.getValues()) {
            if (!capsValues.isEmpty()) {
                capsValues = CommonUtilities.appendValue(capsValues, "", ",");
            }
            capsValues = CommonUtilities.appendValue(capsValues, v.getValue(), "");
            capsValues = CommonUtilities.appendValue(capsValues, v.getUnit(), ":");
        }

        return capsValues;
    }

    private static String getVolumeValues(Volume vols) {
        String volumeValues = "";
        for (Values individualVol : vols.getValues()) {
            String individualSize = "";
            for (Value v : individualVol.getValue()) {
                if (!individualSize.isEmpty()) {
                    individualSize = CommonUtilities.appendValue(individualSize, "", ";");
                }
                individualSize = CommonUtilities.appendValue(individualSize, v.getValue(), ":");
                individualSize = CommonUtilities.appendValue(individualSize, v.getUnit(), "");
            }

            if (!volumeValues.isEmpty()) {
                volumeValues = CommonUtilities.appendValue(volumeValues, individualSize, ",");
            } else {
                volumeValues = individualSize;
            }
        }
        if (volumeValues.endsWith(",")) {
            volumeValues = volumeValues.substring(0, volumeValues.length() - 1);
        }
        return volumeValues;
    }

    private static String getCSVSizesFromSizeModel(List<Value> values) {
        String finalValues = "";
        for (Value v : values) {
            if (finalValues.isEmpty()) {
                finalValues = v.getValue();
            } else {
                finalValues = CommonUtilities.appendValue(finalValues, v.getValue(), ",");
            }
        }

        return finalValues;
    }
    
    public static String getShippingDimension(ShippingEstimate shippingEstimate) {
        String shippingDimension = "";
        if (shippingEstimate != null && shippingEstimate.getDimensions() != null) {
            shippingDimension = CommonUtilities.appendValue(shippingDimension,shippingEstimate.getDimensions().getLength() , "");
            shippingDimension = CommonUtilities.appendValue(shippingDimension,shippingEstimate.getDimensions().getLengthUnit() , ":");
            shippingDimension = CommonUtilities.appendValue(shippingDimension,shippingEstimate.getDimensions().getWidth() , ";");
            shippingDimension = CommonUtilities.appendValue(shippingDimension,shippingEstimate.getDimensions().getWidthUnit() , ":");
            shippingDimension = CommonUtilities.appendValue(shippingDimension,shippingEstimate.getDimensions().getHeight() , ";");
            shippingDimension = CommonUtilities.appendValue(shippingDimension,shippingEstimate.getDimensions().getHeightUnit() , ":");
            return shippingDimension;
        } else {
            return "null";
        }
    }
    
    public static String getShippingWeight(ShippingEstimate shippingEstimate) {
        String shippingWeight = "";
        if (shippingEstimate != null && shippingEstimate.getWeight() != null) {
            shippingWeight = CommonUtilities.appendValue(shippingWeight,shippingEstimate.getWeight().getValue() , "");
            shippingWeight = CommonUtilities.appendValue(shippingWeight,shippingEstimate.getWeight().getUnit() , ":");
            return shippingWeight;
        } else {
            return "null";
        }
    }
    
    public static String getShippingItem(ShippingEstimate shippingEstimate) {
        String numberOfItems = "";
        if (shippingEstimate != null && shippingEstimate.getNumberOfItems() != null) {
            numberOfItems = CommonUtilities.appendValue(numberOfItems,shippingEstimate.getNumberOfItems().getValue() , "");
            numberOfItems = CommonUtilities.appendValue(numberOfItems,shippingEstimate.getNumberOfItems().getUnit() , ":");
            return numberOfItems;
        } else {
            return "null";
        }
    }
    /*
     * private SelectedProductCategories[] getProductCategories(List<String> productCategories) {
     * String categories = CommonUtilities.convertStringListToCSV(productCategories);
     * if (!CommonUtilities.isValueNull(categories)) {
     * SelectedProductCategories category = new SelectedProductCategories();
     * category.setCode(categories);
     * category.setProductId(PRODUCT_ID);
     * return new SelectedProductCategories[] { category };
     * } else {
     * return new SelectedProductCategories[] {};
     * }
     * }
     * 
     * private SelectedComplianceCerts[] getProductComplianceCerts(List<String> productComplianceCerts) {
     * String complianceCerts = CommonUtilities.convertStringListToCSV(productComplianceCerts);
     * if (!CommonUtilities.isValueNull(complianceCerts)) {
     * SelectedComplianceCerts selectedCompliance = new SelectedComplianceCerts();
     * selectedCompliance.setId(ID);
     * selectedCompliance.setProductId(PRODUCT_ID);
     * selectedCompliance.setComplianceCertId(complianceCerts);
     * return new SelectedComplianceCerts[] { selectedCompliance };
     * } else {
     * return new SelectedComplianceCerts[] {};
     * }
     * }
     * 
     * private SelectedSafetyWarnings[] getSelectedSafetyWarnings(List<String> selectedSafetyWarnings) {
     * String safetyWarnings = CommonUtilities.convertStringListToCSV(selectedSafetyWarnings);
     * if (!CommonUtilities.isValueNull(safetyWarnings)) {
     * SelectedSafetyWarnings safetyWarning = new SelectedSafetyWarnings();
     * safetyWarning.setCode(safetyWarnings);
     * return new SelectedSafetyWarnings[] { safetyWarning };
     * } else {
     * return new SelectedSafetyWarnings[] {};
     * }
     * }
     */

}
