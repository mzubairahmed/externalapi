/**
 * 
 */
package com.asi.ext.api.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.service.model.Availability;
import com.asi.ext.api.service.model.BaseValue;
import com.asi.ext.api.service.model.Capacity;
import com.asi.ext.api.service.model.Configurations;
import com.asi.ext.api.service.model.Dimension;
import com.asi.ext.api.service.model.ListValue;
import com.asi.ext.api.service.model.PriceGrid;
import com.asi.ext.api.service.model.Product;
import com.asi.ext.api.service.model.ShippingEstimate;
import com.asi.ext.api.service.model.Size;
import com.asi.ext.api.service.model.StringValue;
import com.asi.ext.api.service.model.Value;
import com.asi.ext.api.service.model.Values;
import com.asi.ext.api.service.model.Volume;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDataSheet;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;

/**
 * @author Rahul K
 * 
 */
public final class ProductParserUtil {

    private final static String PRODUCT_ID = ApplicationConstants.CONST_STRING_ZERO;
    private final static String ID         = ApplicationConstants.CONST_STRING_ZERO;

    private static Logger _LOGGER = Logger.getLogger(ProductParserUtil.class);
    
    public final static String getConfigId(List<ProductConfiguration> productConfigurations) {
        if (productConfigurations != null && productConfigurations.size() > 0) {
            return productConfigurations.get(0).getID() + "";
        } else {
            return ApplicationConstants.CONST_STRING_ZERO;
        }
    }
    
    public static String getShippersBillsBy(String shipperBillsBy) {
        if(!StringUtils.isEmpty(shipperBillsBy)) {
            switch (shipperBillsBy.toLowerCase()) {
            case "size of the package":
                shipperBillsBy="SIZE";
                break;
            case "weight of the package":
                shipperBillsBy="WEIG";
                break;
            case "weight and size of the package":
                shipperBillsBy="WSIZ";
                break;
            default:
                shipperBillsBy = null;
                break;
            }
            return shipperBillsBy;
        } else {
            return "";
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

    public static String getCriteriaCodeFromCriteria(String criteria, String xid) {
        if (criteria == null) {
            return null;
        }
        CriteriaInfo criteriaInfo = ProductDataStore.getCriteriaInfoByDescription(criteria, xid);
        return criteriaInfo != null ? criteriaInfo.getCode() : null;
    }

    public static String getCriteriaNameFromCriteriaCode(String criteria) {
        if (criteria == null) {
            return null;
        }
        CriteriaInfo criteriaInfo = ProductDataStore.getCriteriaInfoForCriteriaCode(criteria);
        return criteria != null ? criteriaInfo.getDescription() : null;
    }

    public static String getSizeValuesFromSize(Size size, String sizeCriteriaCode) {
        if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)) {
        	return getDimensionValues(size.getDimension());
        } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) {
            return getCapacityValues(size.getCapacity());
        } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)) {
            return getVolumeValues(size.getVolume());
        } else if (sizeCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)) {
            return getCSVSizesFromSizeModel(size.getOther().getValues());
        } else if (sizeCriteriaCode.equalsIgnoreCase("SABR") || sizeCriteriaCode.equalsIgnoreCase("SAHU")
                || sizeCriteriaCode.equalsIgnoreCase("SAIT") || sizeCriteriaCode.equalsIgnoreCase("SANS")
                || sizeCriteriaCode.equalsIgnoreCase("SAWI") || sizeCriteriaCode.equalsIgnoreCase("SSNM")) {
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
                individualSize = CommonUtilities.appendValue(individualSize, v.getUnit(), ":");
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
            shippingDimension = CommonUtilities.appendValue(shippingDimension, shippingEstimate.getDimensions().getLength(), "");
            shippingDimension = CommonUtilities.appendValue(shippingDimension, shippingEstimate.getDimensions().getLengthUnit(),
                    ":");
            shippingDimension = CommonUtilities.appendValue(shippingDimension, shippingEstimate.getDimensions().getWidth(), ";");
            shippingDimension = CommonUtilities
                    .appendValue(shippingDimension, shippingEstimate.getDimensions().getWidthUnit(), ":");
            shippingDimension = CommonUtilities.appendValue(shippingDimension, shippingEstimate.getDimensions().getHeight(), ";");
            shippingDimension = CommonUtilities.appendValue(shippingDimension, shippingEstimate.getDimensions().getHeightUnit(),
                    ":");
            return shippingDimension;
        } else {
            return "null";
        }
    }

    public static String getShippingWeight(ShippingEstimate shippingEstimate) {
        String shippingWeight = "";
        if (shippingEstimate != null && shippingEstimate.getWeight() != null) {
            shippingWeight = CommonUtilities.appendValue(shippingWeight, ((Value)shippingEstimate.getWeight()).getValue(), "");
            shippingWeight = CommonUtilities.appendValue(shippingWeight, ((Value)shippingEstimate.getWeight()).getUnit(), ":");
            return shippingWeight;
        } else {
            return "null";
        }
    }

    public static String getShippingItem(ShippingEstimate shippingEstimate) {
        String numberOfItems = "";
        if (shippingEstimate != null && shippingEstimate.getNumberOfItems() != null) {
            numberOfItems = CommonUtilities.appendValue(numberOfItems, ((Value)shippingEstimate.getNumberOfItems()).getValue(), "");
            numberOfItems = CommonUtilities.appendValue(numberOfItems, ((Value)shippingEstimate.getNumberOfItems()).getUnit(), ":");
            return numberOfItems;
        } else {
            return "null";
        }
    }

    public static boolean isOptionGroup(String criteriaCode) {
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidAvailabilty(Availability availability, boolean isOptionGroup) {

        if (availability != null) {
            // Check both criteria present or not
            if (!CommonUtilities.isValueNull(availability.getParentCriteria())
                    && !CommonUtilities.isValueNull(availability.getChildCriteria())) {
                // now check the equality of Criteria
                if (availability.getParentCriteria().trim().equalsIgnoreCase(availability.getChildCriteria())) {
                    // The only case criteria code can be equal is for Options, but then its OptionName cannot be equal
                    if (!CommonUtilities.isValueNull(availability.getParentOptionName())
                            && !CommonUtilities.isValueNull(availability.getChildOptionName())) {
                        if (!availability.getParentOptionName().trim().equalsIgnoreCase(availability.getChildOptionName().trim())) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static ProductCriteriaSets getOptionCriteriaSet(String code, String optName,
            Map<String, List<ProductCriteriaSets>> optionsCriteriaSet) {
        if (optionsCriteriaSet == null || optionsCriteriaSet.isEmpty()) {
            return null;
        }
        List<ProductCriteriaSets> criteriaSets = optionsCriteriaSet.get(code);
        if (criteriaSets != null) {
            boolean checkOptionName = CommonUtilities.isValueNull(optName);
            for (ProductCriteriaSets criteriaSet : criteriaSets) {
                if (!checkOptionName) {
                    if (String.valueOf(criteriaSet.getCriteriaDetail()).equalsIgnoreCase(optName)) {
                        return criteriaSet;
                    }
                } else {
                    return criteriaSet;
                }
            }
        } else {
            return null;
        }

        return null;
    }

    public static String getRelationNameBasedOnCodes(String parent, String child, Availability availability) {
        String name = getCriteriaNameFromCriteriaCode(parent);
        if (CommonUtilities.isValueNull(name)) {
            name = availability.getParentCriteria();
        }
        if (CommonUtilities.isOptionGroup(parent) && !CommonUtilities.isValueNull(availability.getParentOptionName())) {
            name += ":" + availability.getParentOptionName();
        }
        name += " x ";
        String temp = getCriteriaNameFromCriteriaCode(child);
        if (CommonUtilities.isValueNull(temp)) {
            name += availability.getChildCriteria();
        } else {
            name += temp;
        }
        if (CommonUtilities.isOptionGroup(child) && !CommonUtilities.isValueNull(availability.getChildOptionName())) {
            name += ":" + availability.getChildOptionName();
        }

        return name;
    }

    public static boolean isSizeGroup(String criteriaCode) {
        return ApplicationConstants.SIZE_GROUP_CRITERIACODES.contains(criteriaCode);
    }

    public static String getCriteriaSetValueIdBaseOnValueType(String xid, String criteriaCode, BaseValue value) {
        Object criteriaSetValueId = null;
       if (value instanceof StringValue) {
            criteriaSetValueId = getCriteriaSetValueId(xid, criteriaCode, ((StringValue) value).getValue());
            return criteriaSetValueId != null ? String.valueOf(criteriaSetValueId) : null;
        } else if (isSizeGroup(criteriaCode)) {
            String valueToSearch = getSizeModelFromObject(criteriaCode, value);
    //        valueToSearch=transformValue(valueToSearch,criteriaCode);
            // Temporary Fix for checking criteria related to sizes
            if(null!=valueToSearch && valueToSearch.contains("=")){
            	valueToSearch=valueToSearch.substring(valueToSearch.indexOf("=")+1);
            	if(valueToSearch.endsWith("}"))
            	valueToSearch=valueToSearch.substring(0,valueToSearch.length()-1);
            }
            criteriaSetValueId = getCriteriaSetValueId(xid, criteriaCode, valueToSearch);
            return criteriaSetValueId != null ? String.valueOf(criteriaSetValueId) : null;
        }
        return null;
    }

    
	@SuppressWarnings("unchecked")
    private static String getSizeModelFromObject(String criteriaCode, Object value) {
        String valueToSearch = null;
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_DIMENSION)) {
        	String finalValue = "";
        	try {
            	if(value instanceof List) {
	                List<Map<?, ?>> dimValuesList = (List<Map<?, ?>>) value;
	                for (Map<?, ?> dimValue : dimValuesList) {
	                    if (dimValue.get("Attribute") != null) {
	                        finalValue = CommonUtilities.appendValue(finalValue, dimValue.get("Attribute") + ":" + dimValue.get("Value") + ":" + dimValue.get("Unit"), ";");
	                    } 
	                }
            	} else if(value instanceof ListValue) {
            		ListValue listValue = (ListValue) value;
            		List<Value> dimsValues = listValue.getValue();
            		for(Value dimsValue : dimsValues) {
            			finalValue += dimsValue.getAttribute() + ":" + dimsValue.getValue() + ":" + dimsValue.getUnit() + ";";
            		}
            		finalValue = finalValue.substring(0, finalValue.lastIndexOf(";"));
            		
            	}
                valueToSearch = finalValue;
        	} catch (Exception e) {
            	_LOGGER.error(e.getMessage(), e);
                return null;
            }
        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY)) {
            try {
            	if(value instanceof LinkedHashMap){
            		return String.valueOf(((LinkedHashMap) value).get("Value")+":"+((LinkedHashMap) value).get("Unit"));
            	}else if(value instanceof Value){
            		return String.valueOf(((Value) value).getValue()+":"+(((Value) value).getUnit()));
            	}
            	else{
            		
                List<Map<?, ?>> values = (List<Map<?, ?>>) value;
                for (Map<?, ?> v : values) {
                    valueToSearch = v.get("Value") + ":" + v.get("Unit");
                }
            	}
                return valueToSearch;
            } catch (Exception e) {
                return null;
            }
        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI)) {

            try {
            	if(value instanceof LinkedHashMap){
            		return String.valueOf(value);
            	} else if(value instanceof Value) {
            		Value singleValue = (Value) value;
            		valueToSearch = singleValue.getValue() + ":" + singleValue.getUnit();
            	} else {
                List<?> volumes = (List<?>) value;
                if (volumes != null && !volumes.isEmpty()) {
                    List<Map<?, ?>> values = (List<Map<?, ?>>) volumes.get(0);
                    for (Map<?, ?> v : values) {
                        valueToSearch = v.get("Value") + ":" + v.get("Unit");
                    }
                    return valueToSearch;
                }
            	}
            } catch (Exception e) {
                return null;
            }

        } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SIZE_OTHER_CODE)) {
            try {
            	if(value instanceof LinkedHashMap){
            		return String.valueOf(value);
            	}else if(value instanceof Value) {
            		Value singleValue = (Value) value;
            		valueToSearch = singleValue.getValue();
            	}else{
                List<?> otherSizeValues = (List<?>) value;
                for (Object val : otherSizeValues) {
                    return String.valueOf(val);
                }}
            } catch (Exception e) {
                return null;
            }
        } else if (criteriaCode.equalsIgnoreCase("SABR") || criteriaCode.equalsIgnoreCase("SAHU")
                || criteriaCode.equalsIgnoreCase("SAIT") || criteriaCode.equalsIgnoreCase("SANS")
                || criteriaCode.equalsIgnoreCase("SAWI") || criteriaCode.equalsIgnoreCase("SSNM")) {
            
            try {
            	if(value instanceof LinkedHashMap){
            		return String.valueOf(value);
            	}else{
                List<?> apparelValues = (List<?>) value;
                for (Object val : apparelValues) {
                    return String.valueOf(val);
                }
            	}
            } catch (Exception e) {
                return null;
            }
        }
        // criteriaSetValueId = getCriteriaSetValueId(xid, criteriaCode, valueToSearch);
        return valueToSearch;
    }
    
    
    public boolean isProductHasValidProductNumber(Product serProduct) {
        boolean pnoFound = false;
        if (serProduct.getPriceGrids() != null && !serProduct.getPriceGrids().isEmpty()) {
            for (PriceGrid pg : serProduct.getPriceGrids()) {
                if (pg != null && !CommonUtilities.isValueNull(pg.getProductNumber())) {
                    pnoFound = true;
                    break;
                }
            }
        }
        if (serProduct.getProductNumbers() != null && !serProduct.getProductNumbers().isEmpty()) {
            if (pnoFound) { // Already configured in PriceGrid Level
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
    
    public static Object getCriteriaValueFromConfig(Configurations config, String criteriaCode) {
        
        if (CommonUtilities.isValueNull(config.getOptionName())) {
            return config.getValue();
        } else if (isOptionGroup(criteriaCode) && !CommonUtilities.isValueNull(config.getOptionName())){
            return config.getOptionName() + ":" + String.valueOf(config.getValue());
        } else {
            return config.getValue();
        }
    }

}
