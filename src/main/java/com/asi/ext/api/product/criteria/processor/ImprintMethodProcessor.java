package com.asi.ext.api.product.criteria.processor;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.product.transformers.ProductParser;
import com.asi.ext.api.service.model.Artwork;
import com.asi.ext.api.service.model.ImprintMethod;
import com.asi.ext.api.service.model.MinimumOrder;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;

/**
 * @author Rahul K
 * 
 */
public class ImprintMethodProcessor extends SimpleCriteriaProcessor {

    private final static Logger           LOGGER                        = Logger.getLogger(ImprintMethodProcessor.class.getName());

    private ProductParser                 productParser                 = new ProductParser();
    private ProductImprintMethodProcessor productImprintMethodProcessor = new ProductImprintMethodProcessor();

    public ProductDetail processImprintAndRelations(ProductDetail existingProduct,
            Map<String, ProductCriteriaSets> existingCriteriaSetMap, String configId) {
        return existingProduct;
    }

    public Map<String, ProductCriteriaSets> getImprintCriteriaSet(List<ImprintMethod> imprintMethods,
            ProductDetail existingProduct, Map<String, ProductCriteriaSets> existingCriteriaSetMap, String configId) {
        String imprintMethodString = "";
        String minQtyString = "";
        String artworkString = "";

        for (ImprintMethod impMethod : imprintMethods) {
            imprintMethodString = CommonUtilities.appendValue(imprintMethodString,
                    getImprintString(impMethod.getType(), impMethod.getAlias()), "|");
            minQtyString = CommonUtilities.appendValue(minQtyString, getMINOString(impMethod.getMinimumOrder()), "|");
            artworkString = CommonUtilities.appendValue(artworkString, getArtworkValue(impMethod.getArtwork()), "|");
        }

        // Imprint Method Generation
        existingCriteriaSetMap.put(
                ApplicationConstants.CONST_IMPRINT_METHOD_CODE,
                processImprintMethod(imprintMethodString, existingProduct,
                        existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_METHOD_CODE), configId));

        existingCriteriaSetMap.put(
                ApplicationConstants.CONST_ARTWORK_CODE,
                processArtwork(imprintMethodString, existingProduct,
                        existingCriteriaSetMap.get(ApplicationConstants.CONST_ARTWORK_CODE), configId));

        existingCriteriaSetMap.put(
                ApplicationConstants.CONST_MINIMUM_QUANTITY,
                processMinimumQty(imprintMethodString, existingProduct,
                        existingCriteriaSetMap.get(ApplicationConstants.CONST_MINIMUM_QUANTITY), configId));

        return existingCriteriaSetMap;

    }

    private ProductCriteriaSets processImprintMethod(String imprintMethods, ProductDetail existingProduct,
            ProductCriteriaSets immdCriteriaSet, String configId) {
        LOGGER.info("Imprint Methods Transformation Starts :" + imprintMethods);

        ProductCriteriaSets imprintCriteriaSets = null;
        try {
            imprintCriteriaSets = productParser.addCriteriaSetForImprint(existingProduct, existingProduct, imprintMethods, null,
                    ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
        } catch (VelocityException e) {
            LOGGER.error("Exception while processing Imprint Method", e);
        }
        if (imprintCriteriaSets != null) {
            imprintCriteriaSets = productImprintMethodProcessor.compareAndUpdateImprintMethod(imprintCriteriaSets, immdCriteriaSet,
                    configId, existingProduct.getID());
            return imprintCriteriaSets;
        }
        LOGGER.info("Imprint Methods Transformation Ends");

        return imprintCriteriaSets;
    }

    private ProductCriteriaSets processArtwork(String artworks, ProductDetail existingProduct, ProductCriteriaSets immdCriteriaSet,
            String configId) {
        LOGGER.info("Imprint Artwork Transformation Starts :" + artworks);

        ProductCriteriaSets artwrkCriteriaSet = null;
        try {
            artwrkCriteriaSet = productParser.addCriteriaSetForImprint(existingProduct, existingProduct, artworks, null,
                    ApplicationConstants.CONST_ARTWORK_CODE);
        } catch (VelocityException e) {
            LOGGER.error("Exception while processing Artwork", e);
            e.printStackTrace();
        }
        if (artwrkCriteriaSet != null && artwrkCriteriaSet.getCriteriaSetValues() != null
                && !artwrkCriteriaSet.getCriteriaSetValues().isEmpty()) {
            return artwrkCriteriaSet;
        }

        return null;
    }

    private ProductCriteriaSets processMinimumQty(String minQuantities, ProductDetail existingProduct,
            ProductCriteriaSets immdCriteriaSet, String configId) {

        LOGGER.info("Imprint Minimum Quantity Transformation Starts :" + minQuantities);
        // Validate Min_QTY
        String[] minQtys = minQuantities.split("\\|");
        String tempElement = "";
        if (minQtys != null && minQtys.length > 0) {
            for (int i = 0; i < minQtys.length; i++) {
                if (!CommonUtilities.isValueNull(minQtys[i])) {
                    String[] valuesArray = minQtys[i].split(":");
                    if (valuesArray != null && valuesArray.length == 2 && valuesArray[0] != null && valuesArray[1] != null) {
                        tempElement += minQtys[i] + "|";
                    } else {
                        // TODO : Add Error logging
                        /*
                         * tempElement += "null|";
                         * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
                         * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE + ":Invalid Min_QTY specified - " + minQtys[i];
                         */
                    }
                } else {
                    tempElement += "null|";
                }
            }
            if (tempElement != null && !tempElement.isEmpty() && tempElement.endsWith("|")) {
                tempElement = tempElement.substring(0, tempElement.length() - 1);
            }
            minQuantities = tempElement;
        }
        ProductCriteriaSets minQtyCriteriaSet;
        try {
            minQtyCriteriaSet = productParser.addCriteriaSetForImprint(existingProduct, existingProduct, minQuantities, null,
                    ApplicationConstants.CONST_MINIMUM_QUANTITY);
            if (minQtyCriteriaSet != null) {
                return minQtyCriteriaSet;
            }
        } catch (VelocityException e) {
            LOGGER.error("Exception while processing Minimum Quantity", e);
        }

        return null;
    }
/*
    private void processImprintRelations(Map<String, ProductCriteriaSets> existingCriteriaSetMap, List<String> criteriaSetValueIds,
            List<ImprintMethod> imprintMethods, ProductDetail existingProduct, ProductDetail product) {

        String imprintMethodString = "";
        String minQtyString = "";
        String artworkString = "";

        for (ImprintMethod impMethod : imprintMethods) {
            imprintMethodString = CommonUtilities.appendValue(imprintMethodString,
                    getImprintString(impMethod.getType(), impMethod.getAlias()), "|");
            minQtyString = CommonUtilities.appendValue(minQtyString, getMINOString(impMethod.getMinimumOrder()), "|");
            artworkString = CommonUtilities.appendValue(artworkString, getArtworkValueWithoutComments(impMethod.getArtwork()), "|");
        }
        // List<String> criteriaSetValueIds = CommonUtilities.getAllCriteriaSetValueIds(productCriteriaSetsAry);
        // List<String> extCriteriaSetValueIds =
        // CommonUtilities.getAllCriteriaSetValueIds(existingProduct.getProductConfigurations()[0].getProductCriteriaSets());
        Map<String, Relationships> relationShips = new HashMap<String, Relationships>();
        // List<Relationships> relationShips = new ArrayList<Relationships>();
        int relationshipId = -1;
        if (CommonUtilities.isUpdateNeeded(artworkString, false) && !artworkString.equals("")
                && !CommonUtilities.isElementsAreNull(artworkString, "|")
                && !imprintMethodString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
                && !imprintMethodString.equals("") && !artworkString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
            LOGGER.info("Imprint Relationships Transformation Starts");

            try {
                Relationships relationShip = productParser.createImprintArtworkRelationShip(
                        existingCriteriaSetMap.get(ApplicationConstants.CONST_ARTWORK_CODE), product, imprintMethodString,
                        artworkString, true, String.valueOf(relationshipId));

                if (relationShip != null) {
                    // Before adding relationship we need to check for existence of this relationship
                    if (existingProduct != null) {
                        relationShip = productParser.compareAndUpdateRelationship(existingProduct.getRelationships(), relationShip,
                                ApplicationConstants.CONST_ARTWORK_CODE, productCriteriaSetsAry);
                    }
                    if (relationShip != null) {
                        relationShips.put(ApplicationConstants.CONST_ARTWORK_CODE, relationShip);
                        relationshipId--;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception occured in Artwork RL processing, continue work : " + e.getMessage(), e);
            }
        }

        if (!minQtyString.equals("") && !CommonUtilities.isElementsAreNull(minQtyString, "|")
                && !minQtyString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL) && !minQtyString.equals("")
                && !minQtyString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
                && CommonUtilities.isUpdateNeeded(minQtyString, true)) {
            LOGGER.info("Imprint Relationships Transformation Starts");
            try {
                Relationships relationShip = productParser.createImprintArtworkRelationShip(productCriteriaSetsAry, product,
                        imprintMethodString, minQtyString, false, String.valueOf(relationshipId));

                if (relationShip != null) {
                    if (existingProduct != null) {
                        relationShip = productParser.compareAndUpdateRelationship(existingProduct.getRelationships(), relationShip,
                                ApplicationConstants.CONST_MINIMUM_QUANTITY, productCriteriaSetsAry);
                    }
                    if (relationShip != null) {
                        relationShips.put(ApplicationConstants.CONST_MINIMUM_QUANTITY, relationShip);
                        relationshipId--;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Exception occured in MinQTY RL Process, continue process : " + e.getMessage());
            }
        } 

        product.setRelationships(ProductCompareUtil.mergeRelationShips(relationShips, existingProduct));
        // product.setRelationships(ProductCompareUtil.validateAllRelationships(product.getRelationships(), criteriaSetValueIds));

    }
*/
    private String getArtworkValue(List<Artwork> artworkList) {
        String artworkString = "";
        if (artworkList == null || artworkList.isEmpty()) {
            return "null";
        } else {
            for (Artwork artw : artworkList) {
                artworkString = CommonUtilities.appendValue(artworkString, artw.getValue() + "$$$" + artw.getComments(), ",");
            }
        }
        return artworkString;
    }

    private String getArtworkValueWithoutComments(List<Artwork> artworkList) {
        String artworkString = "";
        if (artworkList == null || artworkList.isEmpty()) {
            return "null";
        } else {
            for (Artwork artw : artworkList) {
                artworkString = CommonUtilities.appendValue(artworkString, artw.getValue(), ",");
            }
        }
        return artworkString;
    }

    private String getImprintString(String value, String aliace) {
        if (CommonUtilities.isValueNull(aliace)) {
            return value;
        } else {
            return aliace + "=" + value;
        }
    }

    private String getMINOString(MinimumOrder minimumOrder) {
        if (minimumOrder == null) {
            return "null";
        } else {
            return minimumOrder.getValue() + ":" + minimumOrder.getUnit();
        }
    }
    /*
     * public Map<String, ProductCriteriaSets> generateImprintMethodCriteriaSet(List<ImprintMethod> imprintMethods,
     * Map<String, ProductCriteriaSets> imprintMethodCriteriaSet, ProductDetail existingProduct, String configId,
     * int criteriaSetId) {
     * 
     * if (imprintMethods == null || imprintMethods.isEmpty()) {
     * return null;
     * } else {
     * ProductCriteriaSets immdCriteriaSet = imprintMethodCriteriaSet.get(ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
     * ProductCriteriaSets artwCriteriaSet = imprintMethodCriteriaSet.get(ApplicationConstants.CONST_ARTWORK_CODE);
     * ProductCriteriaSets minoCriteriaSet = imprintMethodCriteriaSet.get(ApplicationConstants.CONST_MINIMUM_QUANTITY);
     * 
     * List<CriteriaSetValues> immdCrtValues = new ArrayList<CriteriaSetValues>();
     * List<CriteriaSetValues> artwCrtValues = new ArrayList<CriteriaSetValues>();
     * List<CriteriaSetValues> minoCrtValues = new ArrayList<CriteriaSetValues>();
     * 
     * for (ImprintMethod impMethod : imprintMethods) {
     * if (impMethod != null && !CommonUtilities.isValueNull(impMethod.getType())) {
     * immdCriteriaSet = immdCriteriaSet != null ? immdCriteriaSet : getCriteriaSetModel(
     * ApplicationConstants.CONST_IMPRINT_METHOD_CODE, existingProduct.getCompanyId(), configId,
     * existingProduct.getID(), String.valueOf(--criteriaSetId));
     * 
     * 
     * } else {
     * // TODO : Log Error
     * }
     * }
     * }
     * 
     * return null;
     * }
     * 
     * public CriteriaSetValues getImprintCriteriaSetValue(String value, String aliace, String criteriaSetId,
     * List<CriteriaSetValues> existingValues) {
     * if (value.contains("=")) {
     * 
     * }
     * String setCodeValueId = ProductDataStore.getSetCodeValueIdForImmdMethod(value);
     * if (setCodeValueId == null) {
     * setCodeValueId = ProductDataStore.getSetCodeValueIdForImmdMethod(ApplicationConstants.CONST_STRING_OTHER);
     * }
     * return null;
     * }
     * 
     * private CriteriaSetValues checkForExisting(String criteriaCode, String value, String aliace, List<CriteriaSetValues>
     * valueList) {
     * 
     * for (CriteriaSetValues criteriaValue : valueList) {
     * if (String.valueOf(criteriaValue).equalsIgnoreCase(value) || String.valueOf(criteriaValue)) {
     * criteriaValue.set
     * }
     * }
     * return null;
     * }
     * 
     * public ProductCriteriaSets getCriteriaSetModel(String criteriaCode, String companyId, String configId, String productId,
     * String criteriaSetId) {
     * ProductCriteriaSets criteriaSet = new ProductCriteriaSets();
     * criteriaSet.setCompanyId(companyId);
     * criteriaSet.setProductId(productId);
     * criteriaSet.setConfigId(configId);
     * criteriaSet.setCriteriaSetId(criteriaSetId);
     * 
     * return criteriaSet;
     * }
     */

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

}
