/**
 * 
 */
package com.asi.ext.api.product.transformers;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.radar.lookup.model.PriceUnitJsonModel;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.radar.model.PriceUnit;
import com.asi.ext.api.rest.JersyClientGet;
import com.asi.ext.api.service.model.Catalog;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.JsonToLookupTableConverter;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.product.client.vo.Currency;
import com.asi.service.product.client.vo.DiscountRate;
import com.asi.service.product.client.vo.ProductMediaCitations;

/**
 * This class is used store elements for product transformation purpose and many
 * static functions which required for better processing.
 * 
 * @author Rahul K
 * 
 */
public class ProductDataStore {

    public static RestTemplate                                        lookupRestTemplate;

    private final static Logger                                       LOGGER                         = Logger.getLogger(ProductDataStore.class
                                                                                                             .getName());

    private static ConcurrentHashMap<String, Set<String>>             GLOBAL_BATCH_LOG_COLLECTION    = new ConcurrentHashMap<String, Set<String>>();
    private static ConcurrentHashMap<String, HashMap<String, String>> criteriaSetValueReferenceTable = new ConcurrentHashMap<String, HashMap<String, String>>();
    private static ConcurrentHashMap<String, String>                  sizeGroupOfProduct             = new ConcurrentHashMap<>();

    private static Map<String, CriteriaInfo>                          criteriaInfo                   = new HashMap<String, CriteriaInfo>();
    // Lookup value tables
    private static Map<String, String>                                productColorMap                = null;
    private static ConcurrentHashMap<String, String>                  categoryCodeLookupTable        = new ConcurrentHashMap<String, String>();
    private static ConcurrentHashMap<String, String>                  productTypeCodeLookupTable     = new ConcurrentHashMap<String, String>();
    private static HashMap<String, String>                            productOriginsLookupTable      = new HashMap<>();
    private static HashMap<String, String>                            productShapesLookupTable       = new HashMap<>();
    private static HashMap<String, String>                            productThemesLookupTable       = new HashMap<>();
    private static HashMap<String, String>                            productPackagesLookupTable     = new HashMap<>();
    private static HashMap<String, String>                            additionalColorLookupTable     = new HashMap<>();
    private static HashMap<String, String>                            additionalLocationLookupTable  = new HashMap<>();
    private static HashMap<String, String>                            imprintColorLookupTable        = new HashMap<>();
    private static HashMap<String, String>                            complianceCertLookupTable      = new HashMap<>();
    private static HashMap<String, String>                            safetyWarningLookupTable       = new HashMap<>();
    private static HashMap<String, String>                            prodSpecSampleLookupTable      = new HashMap<>();
    private static Map<String, String>                                immprintMethodLookupTable      = new HashMap<>();
    private static Map<String, String>                                productMaterialLookupTable     = new HashMap<>();
    private static HashMap<String, String> 							  selectedNamesLookupTable		 = new HashMap<>();
    private static HashMap<String, String>                            productionTimeLookupTable      = new HashMap<>();
    private static HashMap<String, String>                            rushTimeLookupTable            = new HashMap<>();
    private static HashMap<String, String>                            sameDayRushServiceLookupTable  = new HashMap<>();
    private static HashMap<String, String> 							  fobPointsLookupTable			 = new HashMap<>();
    
    public static Map<String, String>                                 criteriatAttributeIds          = new HashMap<>();
    public static Map<String, HashMap<String, String>>                unitOfMeasureCodes             = new HashMap<>();
    public static Map<String, HashMap<String, String>>                criteriaItemLookups            = new HashMap<>();

    public static Map<String, PriceUnitJsonModel>                     priceUnitCollection            = new HashMap<String, PriceUnitJsonModel>();
    public static Map<String, String>                                 artworkLookupTable             = new HashMap<String, String>();
    public static Map<String, String>                                 minoLookupTable                = new HashMap<String, String>();

    public static Map<String, String>                                 shippingItemLookupTable        = new HashMap<String, String>();

    private static Map<String, String>                                optionsLookupTable             = new HashMap<String, String>();
    private static Map<String, Currency>                              currencyLookupTable            = new HashMap<String, Currency>();
    private static Map<String, DiscountRate>                          discountLookupTable            = new HashMap<String, DiscountRate>();

    public static LinkedList<LinkedHashMap>							  sizelookupsResponse		= null;
    public static LinkedList<LinkedHashMap>                           sizeElementsResponse           = null;
    
    public ProductDataStore() {

        if (GLOBAL_BATCH_LOG_COLLECTION == null) {
            GLOBAL_BATCH_LOG_COLLECTION = new ConcurrentHashMap<String, Set<String>>();
        }

        if (criteriaSetValueReferenceTable == null) {
            criteriaSetValueReferenceTable = new ConcurrentHashMap<String, HashMap<String, String>>();
        }
    }

    public void removeEntryFromCriteriaReferenceTable(String externalProductId) {
        try {
            criteriaSetValueReferenceTable.remove(externalProductId.trim());
        } catch (Exception e) {
            // TODO : Exception logic
        }
    }

    public void updateCriteriaSetValueReferenceTable(String extId, String criteriaCode, String value, String crtSetValueId) {
        if (criteriaSetValueReferenceTable == null || criteriaSetValueReferenceTable.isEmpty()) {
            criteriaSetValueReferenceTable = new ConcurrentHashMap<String, HashMap<String, String>>();
        }

        if (criteriaSetValueReferenceTable.get(extId.trim()) == null) {
            criteriaSetValueReferenceTable.put(extId.trim(), new HashMap<String, String>());
        }

        criteriaSetValueReferenceTable.get(extId.trim())
                .put(criteriaCode.trim() + "__" + value.toUpperCase().trim(), crtSetValueId);

        return;
    }

    public void updateCriteriaSetValueReferenceTableWithKey(String extId, String criteriaCode, String key, String crtSetValueId) {
        if (criteriaSetValueReferenceTable == null || criteriaSetValueReferenceTable.isEmpty()) {
            criteriaSetValueReferenceTable = new ConcurrentHashMap<String, HashMap<String, String>>();
        }

        if (criteriaSetValueReferenceTable.get(extId.trim()) == null) {
            criteriaSetValueReferenceTable.put(extId.trim(), new HashMap<String, String>());
        }

        criteriaSetValueReferenceTable.get(extId.trim()).put(key, crtSetValueId);

        return;
    }

    public static String findCriteriaSetValueIdForValue(String extPrdId, String criteriaCode, String value) {
        if (criteriaSetValueReferenceTable == null || criteriaSetValueReferenceTable.isEmpty()) {
            return null;
        }
        HashMap<String, String> tempMap = criteriaSetValueReferenceTable.get(extPrdId.trim());

        if (tempMap == null || tempMap.isEmpty()) {
            return null;
        }
        // If criteriaSetValueId is null then no criteria referenced
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_CRITERIA_CODE_FOBP)) {
            value = CommonUtilities.removeSpaces(value);
        }
        if(value.contains("MEDIUM")){
        	value=value.substring(value.indexOf(" ")).trim();
        }
        String criteriaSetValueId = tempMap.get(criteriaCode.trim() + "__" + value.toUpperCase().trim());

        return criteriaSetValueId;
    }

    public void updateCriteriaReferenceValueTableById(String extId, String criteriaCode, String oldId, String newId) {
        if (criteriaSetValueReferenceTable == null || criteriaSetValueReferenceTable.isEmpty()) {
            return;
        }

        List<String> keys = CommonUtilities.getKeysByValue(criteriaSetValueReferenceTable.get(extId.trim()), oldId);
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                if (key != null && !key.isEmpty()) {
                    updateCriteriaSetValueReferenceTableWithKey(extId, criteriaCode, key, newId);
                }
            }
        }

        return;
    }
    
    public void registerSizeGroupOfProduct(String sizeGroup, String xid) {
        if (sizeGroupOfProduct == null) {
            sizeGroupOfProduct = new ConcurrentHashMap<String, String>();
        }
        sizeGroupOfProduct.put(xid.toUpperCase(), sizeGroup.toUpperCase());
    }
    
    public static String getSizeCriteriaCodeForProduct(String xid) {
        if (sizeGroupOfProduct != null) {
            return sizeGroupOfProduct.get(xid.toUpperCase());
        }
        return null;
    }

    /**
     * 
     * @param product
     * @param originalBatchError
     * @return
     */
    public synchronized static String updateBatchError(String externalProductId, String originalBatchError) {

        if (GLOBAL_BATCH_LOG_COLLECTION.containsKey(externalProductId.trim())) {
            Set<String> batchErrors = GLOBAL_BATCH_LOG_COLLECTION.get(externalProductId.trim());
            if (batchErrors != null && !batchErrors.isEmpty()) {
                for (String msg : batchErrors) {
                    if (!CommonUtilities.isValueNull(msg)) {
                        originalBatchError += msg;
                    }
                }
            }
            GLOBAL_BATCH_LOG_COLLECTION.remove(externalProductId.trim());
        }
        GLOBAL_BATCH_LOG_COLLECTION.remove(externalProductId.trim());

        return originalBatchError;
    }

    public synchronized static Set<String> getBatchErrors(String xid) {
        if (GLOBAL_BATCH_LOG_COLLECTION.containsKey(xid.trim())) {
            Set<String> batchErrors = GLOBAL_BATCH_LOG_COLLECTION.get(xid.trim());
            return batchErrors;
        }
        return new HashSet<String>();
    }

    /**
     * Adds error messages or validation messages to batch error collection,
     * this collection will be processed by batch processor and many tools for
     * manipulating batch errors.
     * 
     * <B>ExternalId </B> and <B>BatchErrorCode</B> are cannot be null, if any
     * of these two values are empty then function will not add error message to
     * batch log
     * 
     * @param externalPrdId
     *            is the ExternalId of the Product
     * @param batchErrCode
     *            is code of the error occurred
     * @param message
     *            is the information related to the error
     */
    public void addErrorToBatchLogCollection(String externalPrdId, String batchErrCode, String message) {
        if (!CommonUtilities.isValueNull(externalPrdId)) {
            externalPrdId = externalPrdId.trim();
            if (GLOBAL_BATCH_LOG_COLLECTION == null) {
                GLOBAL_BATCH_LOG_COLLECTION = new ConcurrentHashMap<String, Set<String>>();
                GLOBAL_BATCH_LOG_COLLECTION.put(externalPrdId, new HashSet<String>());
            } else if (!GLOBAL_BATCH_LOG_COLLECTION.containsKey(externalPrdId)) {
                GLOBAL_BATCH_LOG_COLLECTION.put(externalPrdId, new HashSet<String>());
            }

            GLOBAL_BATCH_LOG_COLLECTION.get(externalPrdId).add(message);
            LOGGER.info("Added batch error log for ExtID : " + externalPrdId + ", message : " + message);
        }
    }

    /**
     * Adds error messages or validation messages to batch error collection,
     * this collection will be processed by batch processor and many tools for
     * manipulating batch errors.
     * 
     * <B>ExternalId </B> and <B>BatchErrorCode</B> are cannot be null, if any
     * of these two values are empty then function will not add error message to
     * batch log
     * 
     * @param externalPrdId
     *            is the ExternalId of the Product
     * @param batchErrCode
     *            is code of the error occurred
     * @param message
     *            is the information related to the error
     */
    public static void addUnhandledErrorToBatchLogCollection(String externalPrdId, String batchErrCode, String message) {
        if (!CommonUtilities.isValueNull(externalPrdId)) {
            externalPrdId = externalPrdId.trim();
            if (GLOBAL_BATCH_LOG_COLLECTION == null) {
                GLOBAL_BATCH_LOG_COLLECTION = new ConcurrentHashMap<String, Set<String>>();
                GLOBAL_BATCH_LOG_COLLECTION.put(externalPrdId, new HashSet<String>());
            } else if (!GLOBAL_BATCH_LOG_COLLECTION.containsKey(externalPrdId)) {
                GLOBAL_BATCH_LOG_COLLECTION.put(externalPrdId, new HashSet<String>());
            }

            String batchMessage = "$Ext-" + externalPrdId + ":" + batchErrCode + ":" + message.replaceAll(":", "=");

            GLOBAL_BATCH_LOG_COLLECTION.get(externalPrdId).add(batchMessage);
            LOGGER.info("Added batch error log for ExtID : " + externalPrdId + ", message : " + message);
        }
    }

    public void registerProductForBatch(String externalProductId) {
        if (GLOBAL_BATCH_LOG_COLLECTION.get(externalProductId.trim()) != null) {
            GLOBAL_BATCH_LOG_COLLECTION.put(externalProductId.trim(), new HashSet<String>());
        }
    }

    //
    public static ConcurrentHashMap<String, String> setProductTypeCodes() {
        try {
            LinkedList<?> prodTypecodeResponse = lookupRestTemplate.getForObject(
                    RestAPIProperties.get(ApplicationConstants.PRODUCT_TYPECODE_LOOKUP_URL), LinkedList.class);
            if (prodTypecodeResponse == null || prodTypecodeResponse.size() <= 0) {
                // Report error to API that we are not able to fetch data for
                // Product Type Code
                // throw new
                // VelocityException("Unable to get response from Product Type Code API",
                // null);
                LOGGER.error("Product Type Code Lookup API returned null response");
                // TODO : Batch Error
                return null;
            } else {
                productTypeCodeLookupTable = JsonToLookupTableConverter.jsonToProductTypeCodeLookupTable(prodTypecodeResponse);
                if (productTypeCodeLookupTable == null) {
                    return null; // TODO : LOG Batch Error
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while fetching/processing product Type Code lookup data", e);
            return null;
        }
        return productTypeCodeLookupTable;
    }

    public static String findProdTypeCodeByName(String prodTypeName) { // throws
        // VelocityException
        // {
        if (productTypeCodeLookupTable == null || productTypeCodeLookupTable.isEmpty()) {
            // Create Product Type Code Lookup table
            productTypeCodeLookupTable = setProductTypeCodes();
        }
        return productTypeCodeLookupTable.get(prodTypeName.toUpperCase());

    }

    public String findProdTypeNameByCode(String typeCode) { // throws
        // VelocityException
        // {
        if (productTypeCodeLookupTable == null || productTypeCodeLookupTable.isEmpty()) {
            // Create Category Lookup table
            productTypeCodeLookupTable = setProductTypeCodes();
        }
        return CommonUtilities.getKeysByValueGen(productTypeCodeLookupTable, typeCode);

    }

    public static ConcurrentHashMap<String, String> setProductCategories() {
        try {
            LinkedList<?> categoryResponse = lookupRestTemplate.getForObject(
                    RestAPIProperties.get(ApplicationConstants.PRODUCT_CATEGORIES_LOOKUP_URL), LinkedList.class);
            if (categoryResponse == null || categoryResponse.isEmpty()) {
                // Report error to API that we are not able to fetch data for
                // Categories
                // throw new
                // VelocityException("Unable to get response from Category API",
                // null);
                LOGGER.error("Category Lookup API returned null response");
                // TODO : Batch Error
                return null;
            } else {
                categoryCodeLookupTable = JsonToLookupTableConverter.jsonToProductCategoryLookupTable(categoryResponse);
                if (categoryCodeLookupTable == null) {
                    return null; // TODO : LOG Batch Error
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while fetching/processing category lookup data", e);
            return null;
        }
        return categoryCodeLookupTable;
    }

    public static String findCategoryCode(String category) { // throws
                                                             // VelocityException
                                                             // {
        if (categoryCodeLookupTable == null || categoryCodeLookupTable.isEmpty()) {
            // Create Category Lookup table
            categoryCodeLookupTable = setProductCategories();
        }
        return categoryCodeLookupTable.get(category.toUpperCase());

    }

    public String findCategoryNameByCode(String category) { // throws
                                                            // VelocityException
                                                            // {
        if (categoryCodeLookupTable == null || categoryCodeLookupTable.isEmpty()) {
            // Create Category Lookup table
            categoryCodeLookupTable = setProductCategories();
        }
        return CommonUtilities.getKeysByValueGen(categoryCodeLookupTable, category);

    }

    /**
     * Search for a particular <b>Color Value</b> in given JSON string. If a
     * match found then we find the corresponding Group and find the matched ID
     * for the Color
     * 
     * @param jsonText
     *            is the JSON string contains all the lookup data
     * @param srchValue
     *            is the Color we need to search
     * @return ID of given color from lookup values or NULL
     */
    public static String getSetCodeValueIdForProductColor(String srchValue) {
        return getColorSetCodeValueId(srchValue);
    }

    private static String getColorSetCodeValueId(String colorName) {
        String setCodeValueId = ApplicationConstants.CONST_STRING_NULL_CAP;
        colorName = colorName.trim().toUpperCase();
        try {

            if (productColorMap == null || productColorMap.isEmpty()) {
                productColorMap = JsonToLookupTableConverter.createProductColorMap(RestAPIProperties
                        .get(ApplicationConstants.COLORS_LOOKUP_URL));
            }

            if (ProductDataStore.productColorMap.isEmpty()) {
                return ApplicationConstants.CONST_STRING_NULL_CAP;
            } else {
                setCodeValueId = ApplicationConstants.CONST_STRING_NULL_CAP;
                String key = "";
                // First checking for Direct match
                key = String.valueOf(colorName).trim();
                setCodeValueId = ProductDataStore.productColorMap.get(key.toUpperCase());
                if (!CommonUtilities.isValueNull(setCodeValueId)) {
                    return setCodeValueId;
                }
                // If match not found, try with Medium + <Space> + Color Name
                key = "Medium " + String.valueOf(colorName).trim();
                setCodeValueId = ProductDataStore.productColorMap.get(key.toUpperCase());
                if (!CommonUtilities.isValueNull(setCodeValueId)) {
                    return setCodeValueId;
                }
                // If match not found, try with Color Name + <Space> + Metal
                key = String.valueOf(colorName).trim() + " Metal";
                setCodeValueId = ProductDataStore.productColorMap.get(key.toUpperCase());
                if (!CommonUtilities.isValueNull(setCodeValueId)) {
                    return setCodeValueId;
                }
                // Last try to get Other Group
                key = ApplicationConstants.CONST_STRING_UNCLASSIFIED_OTHER.toUpperCase();
                setCodeValueId = ProductDataStore.productColorMap.get(key.toUpperCase());
                if (!CommonUtilities.isValueNull(setCodeValueId)) {
                    return setCodeValueId;
                } else {
                    return ApplicationConstants.CONST_STRING_NULL_CAP;
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception occured while finding color setcodeValue id ", e);
        }
        return setCodeValueId;
    }

    public static String getMaterialSetCodeValueId(String materialName) {
        String setCodeValueId = ApplicationConstants.CONST_STRING_NULL_CAP;
        materialName = materialName.trim().toUpperCase();
        try {

            if (productMaterialLookupTable == null || productMaterialLookupTable.isEmpty()) {
                productMaterialLookupTable = JsonToLookupTableConverter.createProductMaterialMap(RestAPIProperties
                        .get(ApplicationConstants.MATERIALS_LOOKUP_URL));
            }

            if (ProductDataStore.productMaterialLookupTable.isEmpty()) {
                return ApplicationConstants.CONST_STRING_NULL_CAP;
            } else {
                setCodeValueId = ApplicationConstants.CONST_STRING_NULL_CAP;
                String key = "";
                // First checking for Direct match
                key = String.valueOf(materialName).trim();
                setCodeValueId = ProductDataStore.productMaterialLookupTable.get(key.toUpperCase());
                if (!CommonUtilities.isValueNull(setCodeValueId)) {
                    return setCodeValueId;
                }
                /*
                 * // If match not found, try with Medium + <Space> + Color Name
                 * key = "Medium " + String.valueOf(materialName).trim();
                 * setCodeValueId = ProductDataStore.productColorMap.get(key.toUpperCase());
                 * if (!CommonUtilities.isValueNull(setCodeValueId)) {
                 * return setCodeValueId;
                 * }
                 * // If match not found, try with Color Name + <Space> + Metal
                 * key = String.valueOf(colorName).trim() + " Metal";
                 * setCodeValueId = ProductDataStore.productColorMap.get(key.toUpperCase());
                 * if (!CommonUtilities.isValueNull(setCodeValueId)) {
                 * return setCodeValueId;
                 * }
                 */
                // Last try to get Other Group
                key = ApplicationConstants.CONST_STRING_OTHER.toUpperCase();
                setCodeValueId = ProductDataStore.productMaterialLookupTable.get(key.toUpperCase());
                if (!CommonUtilities.isValueNull(setCodeValueId)) {
                    return setCodeValueId;
                } else {
                    return ApplicationConstants.CONST_STRING_NULL_CAP;
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception occured while finding color setcodeValue id ", e);
        }
        return setCodeValueId;
    }

    public static String getSetCodeValueIdForProductOrigin(String origin) {
        if (productOriginsLookupTable == null || productOriginsLookupTable.isEmpty()) {
            // Create Category Lookup table
            try {
                /*
                 * String productOriginResponse = JersyClientGet
                 * .getLookupsResponse(RestAPIProperties
                 * .get(ApplicationConstants.ORIGIN_LOOKUP_URL));
                 */
                LinkedList<?> productOriginResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.ORIGIN_LOOKUP_URL), LinkedList.class);

                if (productOriginResponse == null || productOriginResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for Origin
                    // throw new
                    // VelocityException("Unable to get response from Origin API",
                    // null);
                    LOGGER.error("Origin Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    productOriginsLookupTable = JsonToLookupTableConverter.jsonToProductOriginMap(productOriginResponse);
                    if (productOriginsLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing origin lookup data", e);
                return null;
            }
        }

        return productOriginsLookupTable.get(origin.toUpperCase());
    }

    public static String getSetCodeValueIdForProductShape(String shape) {
        if (productShapesLookupTable == null || productShapesLookupTable.isEmpty()) {
            // Create Product shapes Lookup table
            try {

                LinkedList<?> productShapesResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.PRODUCT_SHAPES_LOOKUP_URL), LinkedList.class);
                if (productShapesResponse == null || productShapesResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for Origin
                    // throw new
                    // VelocityException("Unable to get response from Shapes API",
                    // null);
                    LOGGER.error("ProductShapes Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    productShapesLookupTable = JsonToLookupTableConverter.jsonToProductShapesMap(productShapesResponse);
                    if (productShapesLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing shapes lookup data", e);
                return null;
            }
        }

        return productShapesLookupTable.get(shape.toUpperCase());
    }

    // TODO : Change to Lookuptable HIGH Priority
/*    public static String getSetCodeValueIdForProductMaterial(String value) {
        if (null == materialWSResponse) {
            try {
            	
                materialWSResponse = JersyClientGet.getLookupsResponse(RestAPIProperties
                        .get(ApplicationConstants.MATERIALS_LOOKUP_URL));
            } catch (VelocityException e) {
                LOGGER.error("ProductShapes Lookup API returned null response");
                // TODO : Batch Error
                return null;
            }
        }
        return JsonToLookupTableConverter.checkMaterialValueKeyPair(materialWSResponse, value.trim());
    }
*/
    public static String getSetCodeValueIdForProductTheme(String theme) {
        if (productThemesLookupTable == null || productThemesLookupTable.isEmpty()) {
            // Create Product themes Lookup table
            try {
                LinkedList<?> productThemesResponse = lookupRestTemplate.getForObject(RestAPIProperties
                        .get(ApplicationConstants.PRODUCT_THEMES_URL), LinkedList.class);
                
             
                if (productThemesResponse == null || productThemesResponse.isEmpty()) {
                    // throw new
                    // VelocityException("Unable to get response from themes API",
                    // null);
                    // Report error to API that we are not able to fetch data
                    // for themes
                    LOGGER.error("Product Themes Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    productThemesLookupTable = JsonToLookupTableConverter.jsonToProductThemesMap(productThemesResponse);
                    if (productThemesLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing Themes lookup data", e);
                return null;
            }
        }

        return productThemesLookupTable.get(theme.toUpperCase());
    }

    // Always need to call API
    public static String getSetCodeValueIdForProductTradeName(String tradeName) {
        try {
            String lookupUrl = RestAPIProperties.get(ApplicationConstants.PRODUCT_TRADENAMES_LOOKUP);
            lookupUrl += CommonUtilities.getURLEncodedValue(tradeName);
          
            LinkedList<?> productTradeNameResponse = lookupRestTemplate.getForObject(new URI(lookupUrl), LinkedList.class);
            if (productTradeNameResponse == null || productTradeNameResponse.isEmpty()) {
                // Report error to API that we are not able to fetch data for
                // Origin
                // throw new
                // VelocityException("Unable to get response from tradename API",
                // null);
                LOGGER.error("Product Tradename Lookup API returned null response");
                // TODO : Batch Error
                return null;
            } else {
                return JsonToLookupTableConverter.getSetCodeValueForTradeName(productTradeNameResponse, tradeName);
            }
        } catch (Exception e) {
            LOGGER.error("Product Tradename Lookup API failed");
            return null;
        }
    }

    public static String getSetCodeValueIdForProductPackage(String packaging) {
        if (productPackagesLookupTable == null || productPackagesLookupTable.isEmpty()) {
            // Create Product packages Lookup table
            try {
                LinkedList<?> productPackagesResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.PACKAGING_LOOKUP), LinkedList.class);
                if (productPackagesResponse == null || productPackagesResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for packages
                    // throw new
                    // VelocityException("Unable to get response from packages API",
                    // null);
                    LOGGER.error("Product packages Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    productPackagesLookupTable = JsonToLookupTableConverter.jsonToProductPackagesMap(productPackagesResponse);
                    if (productThemesLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing packages lookup data", e);
                return null;
            }
        }

        String setCodeValueId = productPackagesLookupTable.get(packaging.toUpperCase());
        if (CommonUtilities.isValueNull(setCodeValueId)) {
            setCodeValueId = productPackagesLookupTable.get(ApplicationConstants.CONST_STRING_CUSTOM.toUpperCase());
        }

        return setCodeValueId;
    }

    public static String getSetCodeValueIdForAdditionalColor(String additionalColor) {
        if (additionalColorLookupTable == null || additionalColorLookupTable.isEmpty()) {
            // Create Product additional color Lookup table
            try {

                LinkedList<?> additionalColorResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP), LinkedList.class);
                if (additionalColorResponse == null || additionalColorResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for additional color
                    // throw new
                    // VelocityException("Unable to get response from additional color API",
                    // null);
                    LOGGER.error("Product additional color Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    additionalColorLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(additionalColorResponse,
                            ApplicationConstants.CONST_ADDITIONAL_COLOR);
                    if (additionalColorLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing additional color lookup data", e);
                return null;
            }
        }

        return additionalColorLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());
    }

    public static String getSetCodeValueIdForAdditionalLocation(String trim) {
        if (additionalLocationLookupTable == null || additionalLocationLookupTable.isEmpty()) {
            // Create Product additional location Lookup table
            try {
                LinkedList<?> additionalLocationResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.ADDITIONAL_LOCATION_LOOKUP), LinkedList.class);

                if (additionalLocationResponse == null || additionalLocationResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for additional location
                    // throw new
                    // VelocityException("Unable to get response from additional location API",
                    // null);
                    LOGGER.error("Product additional location Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    additionalLocationLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(
                            additionalLocationResponse, ApplicationConstants.CONST_ADDITIONAL_LOCATION);
                    if (additionalLocationLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing additional location lookup data", e);
                return null;
            }
        }

        return additionalLocationLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());
    }

    public static String getSetCodeValueIdForImprintColor(String imprintColor) {
        if (imprintColorLookupTable == null || imprintColorLookupTable.isEmpty()) {
            // Create Product imprint color Lookup table
            try {

                LinkedList<?> imprintColorResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.IMPRINT_COLOR_LOOKUP), LinkedList.class);
                if (imprintColorResponse == null || imprintColorResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for imprint color
                    // throw new
                    // VelocityException("Unable to get response from imprint color API",
                    // null);
                    LOGGER.error("Product imprint color Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    imprintColorLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(imprintColorResponse,
                            ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
                    if (imprintColorLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing imprint color lookup data", e);
                return null;
            }
        }

        return imprintColorLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());
    }

    public static String getComplianceCertId(String compliance) {

        if (complianceCertLookupTable == null || complianceCertLookupTable.isEmpty()) {
            // Create Product Compliance Certificate Lookup table
            complianceCertLookupTable = getComplianceCertData();
        }
        String complianceId = complianceCertLookupTable.get(compliance.toUpperCase());

        return complianceId != null ? complianceId : "-1";
    }

    public static HashMap<String, String> getComplianceCertData() {
        try {
            LinkedList<?> complianceCertResponse = lookupRestTemplate.getForObject(
                    RestAPIProperties.get(ApplicationConstants.PRODUCT_COMPLIANCECERTS_LOOKUP), LinkedList.class);
            if (complianceCertResponse == null || complianceCertResponse.isEmpty()) {
                // Report error to API that we are not able to fetch data for
                // Compliance Certificate
                // throw new
                // VelocityException("Unable to get response from  Compliance Certificate API",
                // null);
                LOGGER.error("Product Compliance Certificate Lookup API returned null response");
                // TODO : Batch Error
                return null;
            } else {
                complianceCertLookupTable = JsonToLookupTableConverter.jsonToComplianceCertLookupTable(complianceCertResponse);
                if (complianceCertLookupTable == null) {
                    return null; // TODO : LOG Batch Error
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while fetching/processing Compliance Certificate lookup data", e);
            return null;
        }
        return complianceCertLookupTable;
    }

    public String getComplianceCertNameById(String compliance) {

        if (complianceCertLookupTable == null || complianceCertLookupTable.isEmpty()) {
            // Create Product Compliance Certificate Lookup table
            complianceCertLookupTable = getComplianceCertData();
        }
        return CommonUtilities.getKeysByValueGen(complianceCertLookupTable, compliance);
    }

    public static HashMap<String, String> setSelectedSafetyWarnings() {
        try {
            LinkedList<?> safetyWarningResponse = lookupRestTemplate.getForObject(
                    RestAPIProperties.get(ApplicationConstants.SAFETY_WARNINGS_LOOKUP), LinkedList.class);

            if (safetyWarningResponse == null || safetyWarningResponse.isEmpty()) {
                // Report error to API that we are not able to fetch data for
                // Selected Safety warning
                // throw new
                // VelocityException("Unable to get response from  Selected Safety warning API",
                // null);
                LOGGER.error("Product Selected Safety warning Lookup API returned null response");
                // TODO : Batch Error
                return null;
            } else {
                safetyWarningLookupTable = JsonToLookupTableConverter.jsonToSafetyWarningLookupTable(safetyWarningResponse);
                if (safetyWarningLookupTable == null) {
                    return null; // TODO : LOG Batch Error
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while fetching/processing Selected Safety warning lookup data", e);
            return null;
        }
        return safetyWarningLookupTable;
    }

    public static String getSelectedSafetyWarningCode(String safetywarning) {
        if (safetyWarningLookupTable == null || safetyWarningLookupTable.isEmpty()) {
            // Create Product Selected Safety warning Lookup table
            safetyWarningLookupTable = setSelectedSafetyWarnings();
        }

        return safetyWarningLookupTable.get(safetywarning.toUpperCase());
    }

    public String getSelectedSafetyWarningNameByCode(String safetywarning) {
        if (safetyWarningLookupTable == null || safetyWarningLookupTable.isEmpty()) {
            // Create Product Selected Safety warning Lookup table
            safetyWarningLookupTable = setSelectedSafetyWarnings();
        }
        return CommonUtilities.getKeysByValueGen(safetyWarningLookupTable, safetywarning);
    }

    public static String getSetCodeValueIdForProdSpecSample(String value) {
        if (prodSpecSampleLookupTable == null || prodSpecSampleLookupTable.isEmpty()) {
            try {
                LinkedList<?> prodSpecSampleJson = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.ADDITIONAL_COLOR_LOOKUP), LinkedList.class);
                if (prodSpecSampleJson == null || prodSpecSampleJson.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for Product Spec Sample
                    // throw new
                    // VelocityException("Unable to get response from  Product Spec Sample API",
                    // null);
                    LOGGER.error("Product Spec Sample Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    prodSpecSampleLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(prodSpecSampleJson,
                            ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing Product Spec Sample lookup data", e);
                return null;
            }
        }
        return prodSpecSampleLookupTable.get(value.toUpperCase().trim());
    }

    public static String getSetCodeValueIdForImmdMethod(String method) {
        if (immprintMethodLookupTable == null || immprintMethodLookupTable.isEmpty()) {
            try {
                LinkedList<?> imprintMethodResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.IMPRINT_LOOKUP_URL), LinkedList.class);
                if (imprintMethodResponse == null || imprintMethodResponse.isEmpty()) {
                    LOGGER.error("ImprintMethod Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    immprintMethodLookupTable = JsonToLookupTableConverter.jsonToImprintMethodLookupTable(imprintMethodResponse);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing imprint method lookup data", e);
                return null;
            }
        }

        return immprintMethodLookupTable.get(String.valueOf(method.toUpperCase()).trim());
    }

    public static String getSetCodeValueIdForProductionTime(String value) {
        if (productionTimeLookupTable == null || productionTimeLookupTable.isEmpty()) {
            try {
                LinkedList<?> productionTimeResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.RUSH_TIME_LOOKUP), LinkedList.class);
                if (productionTimeResponse == null || productionTimeResponse.isEmpty()) {
                    LOGGER.error("ProductionTime Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    productionTimeLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(productionTimeResponse,
                            ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing ProductionTime lookup data", e);
                return null;
            }
        }

        return productionTimeLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());

    }

    public static String getSetCodeValueIdForRushTime(String value) {
        if (rushTimeLookupTable == null || rushTimeLookupTable.isEmpty()) {
            try {
                LinkedList<?> productionTimeResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.RUSH_TIME_LOOKUP), LinkedList.class);
                if (productionTimeResponse == null || productionTimeResponse.isEmpty()) {
                    LOGGER.error("RushTime Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    rushTimeLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(productionTimeResponse,
                            ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing RushTime lookup data", e);
                return null;
            }
        }

        return rushTimeLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());

    }

    public static String getSetCodeValueIdForSameDayService(String value) {
        if (sameDayRushServiceLookupTable == null || sameDayRushServiceLookupTable.isEmpty()) {
            try {
                LinkedList<?> sameDayRushResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.RUSH_TIME_LOOKUP), LinkedList.class);
                if (sameDayRushResponse == null || sameDayRushResponse.isEmpty()) {
                    LOGGER.error("SameDayRushService Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    sameDayRushServiceLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(sameDayRushResponse,
                            ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing SameDayRushService lookup data", e);
                return null;
            }
        }

        return sameDayRushServiceLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());

    }

    private static boolean CRITERIA_ATTRIBUTES_LOADED = false;

    private static boolean loadAllCriteriaSetAttributes() {
        LOGGER.info("Loading criteriaSet Attribute values");
        try {
            LinkedList<?> criteriaAttributeWSResponse = lookupRestTemplate.getForObject(RestAPIProperties
                    .get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL), LinkedList.class);
            if (criteriaAttributeWSResponse == null || criteriaAttributeWSResponse.isEmpty()) {
                LOGGER.error("Failed to load CriteriaAttribute values from Lookup API, API returned null response");
                // TODO : Batch Error
                return false;
            }
            criteriatAttributeIds = JsonToLookupTableConverter.generateCriteriaSetAttributeTable(criteriaAttributeWSResponse);
            unitOfMeasureCodes = JsonToLookupTableConverter.generateUnitOfMeasureTable(criteriaAttributeWSResponse);
            criteriaItemLookups = JsonToLookupTableConverter.generateCriteriaItemTable(criteriaAttributeWSResponse);
            CRITERIA_ATTRIBUTES_LOADED = true;
        } catch (Exception e) {
            LOGGER.error("Exception while processing criteriaset attributes", e);
            CRITERIA_ATTRIBUTES_LOADED = false;
        }
        LOGGER.info("Finished loading CriteriaSet Attribute values");
        return true;
    }

    private static boolean loadCriteriaSetAttributes() {
        if (!CRITERIA_ATTRIBUTES_LOADED) {
            return loadAllCriteriaSetAttributes();
        }
        return true;
    }

    public static String getCriteriaSetAttributeId(String criteriaCode) {
        return loadCriteriaSetAttributes() ? criteriatAttributeIds.get(criteriaCode) : null;
    }

    public static String getUnitOfMeasureCode(String criteriaCode, String unit) {
        if (loadCriteriaSetAttributes()) {
            HashMap<String, String> unitOfMeasures = unitOfMeasureCodes.get(criteriaCode);
            if (unitOfMeasures != null && !unitOfMeasures.isEmpty()) {
                return unitOfMeasures.get(unit.toUpperCase());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Find the PriceUnit object of the given price unit. PriceUnit will find
     * the element from the lookup values
     * 
     * @param priceUnit
     *            is the price unit we need to find
     * @return matched {@linkplain PriceUnit} or default {@linkplain PriceUnit}
     */
    public static com.asi.service.product.client.vo.PriceUnit getPriceUnit(String priceUnit) {
        try {
            if (ProductDataStore.priceUnitCollection == null || ProductDataStore.priceUnitCollection.isEmpty()) {
                ProductDataStore.priceUnitCollection = new HashMap<String, PriceUnitJsonModel>();
                LinkedList<?> responseList = lookupRestTemplate.getForObject(RestAPIProperties
                        .get(ApplicationConstants.PRICE_UNIT_LOOKUP_URL),LinkedList.class);
                
                priceUnitCollection = JsonToLookupTableConverter.jsonToPriceUnitLookupTable(responseList);
            }
            PriceUnitJsonModel priceUnitJsonModel = ProductDataStore.priceUnitCollection.get(String.valueOf(priceUnit)
                    .toUpperCase());
            if (priceUnitJsonModel != null) {
                return priceUnitJsonModel;
            } else {
                priceUnitJsonModel = ProductDataStore.priceUnitCollection
                        .get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());
                return priceUnitJsonModel != null ? priceUnitJsonModel : new PriceUnitJsonModel(
                        ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID, ApplicationConstants.CONST_STRING_PIECE,
                        ApplicationConstants.CONST_STRING_PIECE, "0");
            }
            // return new
            // PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID,
            // ApplicationConstants.CONST_STRING_PIECE,
            // ApplicationConstants.CONST_STRING_PIECE, "0");
        } catch (Exception e) {
            // TODO Log error message && add error to batch
            return new PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID,
                    ApplicationConstants.CONST_STRING_PIECE, ApplicationConstants.CONST_STRING_PIECE, "0");
        }
    }
  public static boolean isOtherPriceUnit(String priceUnit)
  {
	  boolean otherpriceUnitChk=false;
	  com.asi.service.product.client.vo.PriceUnit curentPriceUnit=getPriceUnit(priceUnit);
	  if(curentPriceUnit.getDisplayName().equalsIgnoreCase("other"))
		  otherpriceUnitChk=true;
	  
	  return otherpriceUnitChk;
  }
    public static String getArtworkSetCodeValueId(String artworkName, boolean checkOther) {

        if (artworkLookupTable == null || artworkLookupTable.isEmpty()) {
            try {
                LinkedList<?> artworkResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.IMPRINT_ARTWORK_LOOKUP_URL), LinkedList.class);
                if (artworkResponse == null || artworkResponse.isEmpty()) {
                    LOGGER.error("Artwork Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    artworkLookupTable = JsonToLookupTableConverter.jsonToArtworkLookupTable(artworkResponse);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing Artwork lookup data", e);
                return null;
            }
        }
        String setCodeValueId = null;
        if (checkOther) {
            setCodeValueId = artworkLookupTable.get(String.valueOf(artworkName).toUpperCase());
            if (setCodeValueId == null) {
                setCodeValueId = artworkLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());
            }
        } else {
            setCodeValueId = artworkLookupTable.get(String.valueOf(artworkName).toUpperCase());
        }
        return setCodeValueId;
    }

    public static String getMinQtySetCodeValueId(String minQty, boolean checkOther) {

        if (minoLookupTable == null || minoLookupTable.isEmpty()) {
            try {
                LinkedList<?> minQtyResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.IMPRINT_ARTWORK_LOOKUP_URL), LinkedList.class);
                if (minQtyResponse == null || minQtyResponse.isEmpty()) {
                    LOGGER.error("Min QTY Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    minoLookupTable = JsonToLookupTableConverter.jsonToMinQtyLookupTable(minQtyResponse);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing Min QTY lookup data", e);
                return null;
            }
        }
        String setCodeValueId = null;
        if (checkOther) {
            setCodeValueId = minoLookupTable.get(String.valueOf(minQty).toUpperCase());
            if (setCodeValueId == null) {
                setCodeValueId = minoLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());
            }
        } else {
            setCodeValueId = minoLookupTable.get(String.valueOf(minQty).toUpperCase());
        }
        return setCodeValueId;
    }

    public static String reverseLookupFindAttribute(String setCodeValueId, String criteriaCode) {
        return reverseLookupFindAttribute(setCodeValueId, criteriaCode, null);
    }
    /**
     * This method will search for the particular item in the Lookup table of
     * given criteria code using the SetCodeValueId. Its called reverse lookup
     * and used to decode SetCodeValue of a CriteriaSetValue element.
     * 
     * TODO : Not Completed
     * 
     * @param setCodeValueId
     * @param criteriaCode
     * @return value matches with the SetCodeValueId or null
     */
    public static String reverseLookupFindAttribute(String setCodeValueId, String criteriaCode, String companyId) {

        if (CommonUtilities.isValueNull(criteriaCode) || CommonUtilities.isValueNull(setCodeValueId)) {
            return null;
        }
        String value = null;

        if (ApplicationConstants.CONST_COLORS_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
            if (productColorMap == null || productColorMap.isEmpty()) {
                getColorSetCodeValueId("Black");
            }
            String finalValue = CommonUtilities.getKeysByValueGen(productColorMap, setCodeValueId);
            return finalValue;
        } else if (ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
            if (productMaterialLookupTable == null || productMaterialLookupTable.isEmpty()) {
                // getSetCodeValueIdForProductMaterial("Blend");
                getMaterialSetCodeValueId("Blend");
            }
            return CommonUtilities.getKeysByValueGen(productMaterialLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
            if (productOriginsLookupTable == null || productOriginsLookupTable.isEmpty()) {
                getSetCodeValueIdForProductOrigin("USA");
            }
            return CommonUtilities.getKeysByValueGen(productOriginsLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_SHAPE_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
            if (productShapesLookupTable == null || productShapesLookupTable.isEmpty()) {
                getSetCodeValueIdForProductShape("safe");
            }
            return CommonUtilities.getKeysByValueGen(productShapesLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_THEME_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_TRADE_NAME_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
            if (imprintColorLookupTable == null || imprintColorLookupTable.isEmpty()) {
                getSetCodeValueIdForImprintColor("red");
            }
            return CommonUtilities.getKeysByValueGen(imprintColorLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {
            if (productPackagesLookupTable == null || productPackagesLookupTable.isEmpty()) {
                getSetCodeValueIdForProductPackage("Pouch");
            }
            return CommonUtilities.getKeysByValueGen(productPackagesLookupTable, setCodeValueId);

        } else if (ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_LESS_THAN_MIN_CRT_CODE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_IMPRINT_METHOD_CODE.equalsIgnoreCase(criteriaCode)) {
            if (immprintMethodLookupTable == null || immprintMethodLookupTable.isEmpty()) {
                getSetCodeValueIdForImmdMethod("Pad Print");
            }
            return CommonUtilities.getKeysByValueGen(immprintMethodLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_ARTWORK_CODE.equalsIgnoreCase(criteriaCode)) {
            if (artworkLookupTable == null || artworkLookupTable.isEmpty()) {
                getArtworkSetCodeValueId("artw", true);
            }
            return CommonUtilities.getKeysByValueGen(artworkLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_MINIMUM_QUANTITY.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_CAPACITY.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_DIMENSION.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_PRODUCT_OPTION.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_SHIPPING_OPTION.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_IMPRINT_OPTION.equalsIgnoreCase(criteriaCode)) {

        } else if (ApplicationConstants.CONST_ADDITIONAL_LOCATION.equalsIgnoreCase(criteriaCode)) {
            if (additionalLocationLookupTable == null || additionalLocationLookupTable.isEmpty()) {
                getSetCodeValueIdForAdditionalLocation("Red");
            }
            return CommonUtilities.getKeysByValueGen(additionalLocationLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_ADDITIONAL_COLOR.equalsIgnoreCase(criteriaCode)) {
            if (additionalColorLookupTable == null || additionalColorLookupTable.isEmpty()) {
                getSetCodeValueIdForAdditionalColor("Red");
            }
            return CommonUtilities.getKeysByValueGen(additionalColorLookupTable, setCodeValueId);
        }  else if (ApplicationConstants.CONST_CRITERIA_CODE_LNNM.equalsIgnoreCase(criteriaCode)) {
            getSetCodeValueIdForSelectedLineName("LNNM", companyId);
            
            return CommonUtilities.getKeysByValueGen(selectedNamesLookupTable, setCodeValueId);
        } else if (ApplicationConstants.CONST_CRITERIA_CODE_FOBP.equalsIgnoreCase(criteriaCode)) {
            getSetCodeValueIdForFobPoints("FOBP", companyId);
            
            return CommonUtilities.getKeysByValueGen(fobPointsLookupTable, setCodeValueId);
        }

        return value;
    }

    public static String getSetCodeValueIdForSelectedLineName(String value, String companyId) {
    	 
        try {

            LinkedList<?> selectedNamesResponse = lookupRestTemplate.getForObject(
                    RestAPIProperties.get(ApplicationConstants.SELECTED_LINES_LOOKUP) + companyId, LinkedList.class);
            if (selectedNamesResponse == null || selectedNamesResponse.isEmpty()) {
                // Report error to API that we are not able to fetch data
                // for additional color
                // throw new
                // VelocityException("Unable to get response from additional color API",
                // null);
                LOGGER.error("Product Selected Names Lookup API returned null response");
                // TODO : Batch Error
                return null;
            } else {
                selectedNamesLookupTable = JsonToLookupTableConverter.jsonToSelectedLinesNamesLookupTable(selectedNamesResponse);
                if (selectedNamesLookupTable == null) {
                    return null; // TODO : LOG Batch Error
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while fetching/processing Selected Line Names lookup data", e);
            return null;
        }
    	 return selectedNamesLookupTable.get(value);    		
	}
    public static String getSetCodeValueIdForFobPoints(String value, String companyId) {
            // Create FOBPoint Lookup table
            try {

                LinkedList<?> fobPointsResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.FOBP_POINTS_LOOKUP)+companyId, LinkedList.class);
                if (fobPointsResponse == null || fobPointsResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for fob points throw new
                    // VelocityException("Unable to get response from fob points API",
                    // null);
                    LOGGER.error("FOBPoint Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                	fobPointsLookupTable = JsonToLookupTableConverter.jsonToFOBPointookupTable(fobPointsResponse);
                    if (fobPointsLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing FOBPoint lookup data", e);
                return null;
            }
            return fobPointsLookupTable.get(value);   		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static LinkedList<LinkedHashMap> getLookupResponse() {
        String response;
        try {
            if (sizeElementsResponse == null) {
                response = JersyClientGet.getLookupsResponse(RestAPIProperties.get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL));
                sizeElementsResponse = (LinkedList<LinkedHashMap>) JersyClientGet.parseToList(response);
            }

        } catch (Exception e) {
            LOGGER.error("Exception while processing Product Size Group JSON - size value parsing", e);
        }
        return sizeElementsResponse;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	    private static LinkedList<LinkedHashMap> getSizeLookupResponse() {
	        try {
	            if (sizelookupsResponse == null) {
	            	sizelookupsResponse = lookupRestTemplate.getForObject(RestAPIProperties.get(ApplicationConstants.SIZES_LOOKUP_URL),LinkedList.class);
	               // sizeElementsResponse = (LinkedList<LinkedHashMap>) JersyClientGet.parseToList(response);
	            }
	 
	        } catch (Exception e) {
	            LOGGER.error("Exception while processing Product Size Group JSON - size value parsing", e);
	        }
	        return sizelookupsResponse;
	    }
    public static CriteriaInfo getCriteriaInfoForCriteriaCode(String code) {
        if (criteriaInfo == null || criteriaInfo.isEmpty()) {
            return loadCriteriaInformations() ? criteriaInfo.get(code) : null;
        } else {
            return criteriaInfo.get(code);
        }
    }
    
    public static CriteriaInfo getCriteriaInfoByDescription(String description, String xid) {
        if (criteriaInfo == null || criteriaInfo.isEmpty()) {
            loadCriteriaInformations();
        } 
        if (!description.equalsIgnoreCase("Sizes")) {
            for (Map.Entry<String, CriteriaInfo> crtInfo : criteriaInfo.entrySet()) {
                if (crtInfo.getValue().getDescription().equalsIgnoreCase(description)) {
                    return crtInfo.getValue();
                }
            }
        } else {
            String criteriaCode = getSizeCriteriaCodeForProduct(xid);
            if (criteriaCode == null) {
                for (Map.Entry<String, CriteriaInfo> crtInfo : criteriaInfo.entrySet()) {
                    if (crtInfo.getValue().getDescription().equalsIgnoreCase(description)) {
                        return crtInfo.getValue();
                    }
                }
            } else {
                return criteriaInfo.get(criteriaCode);
            }
        }
        return null;
    }
    

    
    private static boolean loadCriteriaInformations() {
        try {
            LinkedList<?> wsResponse = lookupRestTemplate.getForObject(RestAPIProperties.get(ApplicationConstants.CRITERIA_INFO_URL),LinkedList.class);
            if (wsResponse != null) {
                criteriaInfo = JsonToLookupTableConverter.createCriteriaInfoLookup(wsResponse);
                return (criteriaInfo != null && !criteriaInfo.isEmpty());
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Exception while processing Criteria value list informations", e);
            return false;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String getSetCodeValueIdForOptions(String optionType) {
        if (optionsLookupTable == null || optionsLookupTable.isEmpty()) {
            // Create Options Lookup table
            try {

                LinkedList<LinkedHashMap> producOptionResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.OPTION_PRODUCT_LOOKUP), LinkedList.class);
                if (producOptionResponse != null && !producOptionResponse.isEmpty()) {
                    LinkedList<LinkedHashMap> shippingOptionResponse = lookupRestTemplate.getForObject(
                            RestAPIProperties.get(ApplicationConstants.OPTION_SHIPPING_LOOKUP), LinkedList.class);
                    if (shippingOptionResponse != null && !shippingOptionResponse.isEmpty()) {
                        producOptionResponse.addAll(shippingOptionResponse);
                        LinkedList<LinkedHashMap> imprintOptionResponse = lookupRestTemplate.getForObject(
                                RestAPIProperties.get(ApplicationConstants.OPTION_IMPRINT_LOOKUP), LinkedList.class);
                        if (imprintOptionResponse != null && !imprintOptionResponse.isEmpty()) {
                            producOptionResponse.addAll(imprintOptionResponse);
                        } else {
                            producOptionResponse = null;
                        }
                    } else {
                        producOptionResponse = null;
                    }
                }

                if (producOptionResponse == null || producOptionResponse.isEmpty()) {
                    // Report error to API that we are not able to fetch data
                    // for Options
                    // throw new
                    // VelocityException("Unable to get response from Options API",
                    // null);
                    LOGGER.error("One of the options Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    optionsLookupTable = JsonToLookupTableConverter.jsonToOptionLookupMap(producOptionResponse);
                    if (optionsLookupTable == null) {
                        return null; // TODO : LOG Batch Error
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing option lookup data", e);
                return null;
            }
        }
        return optionsLookupTable.get(optionType);
    }

    public static Currency getCurrencyForCode(String code, boolean getDefult) {
        try {
            if (currencyLookupTable == null || currencyLookupTable.isEmpty()) {
                currencyLookupTable = new HashMap<String, Currency>();
                String response = JersyClientGet.getLookupsResponse(RestAPIProperties
                        .get(ApplicationConstants.CURRENCIES_LOOKUP_URL));

                currencyLookupTable = JsonToLookupTableConverter.jsonToCurrencyLookupTable(response);
            }
            Currency currencyJsonModel = currencyLookupTable.get(String.valueOf(code).toUpperCase());
            if (currencyJsonModel == null && getDefult) {
                return currencyLookupTable.get(String.valueOf("USD").toUpperCase());
            } else {
                return currencyJsonModel;
            }
            // return new
            // PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID,
            // ApplicationConstants.CONST_STRING_PIECE,
            // ApplicationConstants.CONST_STRING_PIECE, "0");
        } catch (Exception e) {
            LOGGER.error("Exception while modeling currencies", e);
            return null;
        }
    }

    public RestTemplate getLookupRestTemplate() {
        return lookupRestTemplate;
    }

    public void setLookupRestTemplate(RestTemplate lookupRestTemplate) {
        this.lookupRestTemplate = lookupRestTemplate;
    }

    public HashMap<String, String> getSamplesList() {
        if (prodSpecSampleLookupTable == null || prodSpecSampleLookupTable.isEmpty()) {
            getSetCodeValueIdForProdSpecSample("spec");
        }
        return prodSpecSampleLookupTable;
    }

    public static DiscountRate getDiscountRate(String discountCode, boolean getDefault) {
        try {
            if (discountLookupTable == null || discountLookupTable.isEmpty()) {
                discountLookupTable = new HashMap<String, DiscountRate>();
                LinkedList<?> responseList = lookupRestTemplate.getForObject((RestAPIProperties
                        .get(ApplicationConstants.DISCOUNT_RATES_LOOKUP_URL)),LinkedList.class);

                discountLookupTable = JsonToLookupTableConverter.jsonToDiscountLookupTable(responseList);
            }
            DiscountRate discountRate = discountLookupTable.get(String.valueOf(discountCode).toUpperCase());
            if (discountRate == null && getDefault) {
                return discountLookupTable.get(String.valueOf("Z").toUpperCase());
            } else {
                return discountRate;
            }
            // return new
            // PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID,
            // ApplicationConstants.CONST_STRING_PIECE,
            // ApplicationConstants.CONST_STRING_PIECE, "0");
        } catch (Exception e) {
            LOGGER.error("Exception while modeling currencies", e);
            return null;
        }
    }

    public static String getSetCodeValueIdForMinQTY() {
        // TODO Auto-generated method stub
        return null;
    }

	public static Catalog getMediaCitationById(String mediaCitationId,
			String mediaCitationReferenceId,String companyId) {
        LinkedList<?> responseList = lookupRestTemplate.getForObject((RestAPIProperties
                .get(ApplicationConstants.PRODUCT_MEDIA_CITATION)+companyId),LinkedList.class);
        Catalog currentCatalogs= JsonToLookupTableConverter.jsonToCatalogs(responseList,mediaCitationId,mediaCitationReferenceId);
	return currentCatalogs;
	}
		
	public static ProductMediaCitations getMediaCitationsByName(String productId, String catalogName, String catalogPageNumber, String companyId) {

		String URL = RestAPIProperties.get(ApplicationConstants.PRODUCT_MEDIA_CITATION) + companyId;
		LinkedList<?> responseList = lookupRestTemplate.getForObject(URL, LinkedList.class);
		return JsonToLookupTableConverter.jsonToMediaCitation(responseList, productId, catalogName, catalogPageNumber);
	}
	
	public static String getSetCodeValueIdForShippingItem(String value) {
        if (shippingItemLookupTable == null || shippingItemLookupTable.isEmpty()) {
            try {
                LinkedList<?> shippingItemResponse = lookupRestTemplate.getForObject(
                        RestAPIProperties.get(ApplicationConstants.PRODUCT_SHIPPING_ITEM_LOOKUP), LinkedList.class);
                if (shippingItemResponse == null || shippingItemResponse.isEmpty()) {
                    LOGGER.error("Shipping Item Lookup API returned null response");
                    // TODO : Batch Error
                    return null;
                } else {
                    shippingItemLookupTable = JsonToLookupTableConverter.jsonToProductCustomLookupTable(shippingItemResponse,
                            ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while fetching/processing Shipping Item lookup data", e);
                return null;
            }
        }

        return shippingItemLookupTable.get(ApplicationConstants.CONST_STRING_OTHER.toUpperCase());

    }

    /**
     * @param xid
     */
    public static synchronized void doCleanUp(String xid) {
        // Clean Criteria SetValue Reference Table
        if (criteriaSetValueReferenceTable != null) {
            criteriaSetValueReferenceTable.remove(xid);
        }
        // Clean Size Group Table
        if (sizeGroupOfProduct != null) {
            sizeGroupOfProduct.remove(xid);
        }
        // Clean Error logs of product
        if (GLOBAL_BATCH_LOG_COLLECTION != null) {
            GLOBAL_BATCH_LOG_COLLECTION.remove(xid);
        }
        
    }
}
