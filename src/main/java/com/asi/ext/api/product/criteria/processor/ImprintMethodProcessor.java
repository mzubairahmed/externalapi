package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ImprintRelationData;
import com.asi.ext.api.service.model.Artwork;
import com.asi.ext.api.service.model.ImprintMethod;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.product.client.vo.CriteriaSetRelationship;
import com.asi.service.product.client.vo.CriteriaSetValuePath;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Relationship;

/**
 * @author Rahul K
 * 
 */
public class ImprintMethodProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER        = Logger.getLogger(ImprintMethodProcessor.class.getName());

    private final static String MINO_REL_NAME = "Imprint Method x Min Order";
    private final static String ARTW_REL_NAME = "Imprint Method x Artwork";
    
    private RelationshipProcessor relationshipProcessor = new RelationshipProcessor();

    private int                 criteiraSetId = -2000;

    public ProductDetail processImprintAndRelations(ProductDetail existingProduct,
            Map<String, ProductCriteriaSets> existingCriteriaSetMap, String configId) {
        return existingProduct;
    }

    private int relationshipId  = -1;

    private int valuePathUniqId = -1;

    /*
     * imprintMethodString = CommonUtilities.appendValue(imprintMethodString,
     * getImprintString(impMethod.getType(), impMethod.getAlias()), "|");
     * minQtyString = CommonUtilities.appendValue(minQtyString, getMINOString(impMethod.getMinimumOrder()), "|");
     * artworkString = CommonUtilities.appendValue(artworkString, getArtworkValue(impMethod.getArtwork()), "|");
     */

    public ImprintRelationData getImprintCriteriaSet(List<ImprintMethod> imprintMethods, ProductDetail exisitingProduct,
            Map<String, ProductCriteriaSets> existingCriteriaSetMap, String configId) {
        
        ImprintRelationData imprintRelationData = new ImprintRelationData();
        ProductImprintMethodProcessor imprintMethodProcessor = new ProductImprintMethodProcessor();
        ProductArtworkProcessor artworkProcessor = new ProductArtworkProcessor();
        ProductMinimumQuantityProcessor minoProcessor = new ProductMinimumQuantityProcessor();

        String immdCriteriaId = "";
        String minoCriteriaId = "";
        String artwCriteriaId = "";
        valuePathUniqId = -1;
        relationshipId = -1;

        List<CriteriaSetValuePath> artworkCriteriaValuePathList = new ArrayList<CriteriaSetValuePath>();
        List<CriteriaSetValuePath> minoCriteriaValuePathList = new ArrayList<CriteriaSetValuePath>();

        Relationship immdArtwRelationship = null;
        Relationship immdMinoRelationship = null;

        ProductCriteriaSets immdCriteriaSet = existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
        if (immdCriteriaSet != null) {
            immdCriteriaId = immdCriteriaSet.getCriteriaSetId();
            imprintMethodProcessor.generateExistingCriteriaList(immdCriteriaSet.getCriteriaSetValues());
        } else {
            immdCriteriaSet = getProductCriteriaSet(exisitingProduct, configId, ApplicationConstants.CONST_IMPRINT_METHOD_CODE,
                    String.valueOf(--criteiraSetId));
            immdCriteriaId = immdCriteriaSet.getCriteriaSetId();
        }
        ProductCriteriaSets artwCriteriaSet = existingCriteriaSetMap.get(ApplicationConstants.CONST_ARTWORK_CODE);
        if (artwCriteriaSet != null) {
            artwCriteriaId = artwCriteriaSet.getCriteriaSetId();
            artworkProcessor.generateExistingCriteriaList(artwCriteriaSet.getCriteriaSetValues());
            immdArtwRelationship = immdCriteriaSet != null ? getRelationshipBasedCriteriaIds(immdCriteriaId, artwCriteriaId,
                    exisitingProduct.getRelationships()) : null;
            if (immdArtwRelationship == null) {
                immdArtwRelationship = createRelationship(ARTW_REL_NAME, immdCriteriaId, artwCriteriaId, exisitingProduct.getID(),
                        --relationshipId);
            }
        } else {
            artwCriteriaSet = getProductCriteriaSet(exisitingProduct, configId, ApplicationConstants.CONST_ARTWORK_CODE,
                    String.valueOf(--criteiraSetId));
            artwCriteriaId = artwCriteriaSet.getCriteriaSetId();
            immdArtwRelationship = createRelationship(ARTW_REL_NAME, immdCriteriaId, artwCriteriaId, exisitingProduct.getID(),
                    --relationshipId);
        }
        ProductCriteriaSets minoCriteriaSet = existingCriteriaSetMap.get(ApplicationConstants.CONST_MINIMUM_QUANTITY);
        if (minoCriteriaSet != null) {
            minoCriteriaId = minoCriteriaSet.getCriteriaSetId();
            minoProcessor.generateExistingCriteriaList(minoCriteriaSet.getCriteriaSetValues());
            immdMinoRelationship = immdCriteriaSet != null ? getRelationshipBasedCriteriaIds(immdCriteriaId, minoCriteriaId,
                    exisitingProduct.getRelationships()) : null;
            if (immdMinoRelationship == null) {
                immdMinoRelationship = createRelationship(MINO_REL_NAME, immdCriteriaId, minoCriteriaId, exisitingProduct.getID(),
                        --relationshipId);
            }
        } else {
            minoCriteriaSet = getProductCriteriaSet(exisitingProduct, configId, ApplicationConstants.CONST_MINIMUM_QUANTITY,
                    String.valueOf(--criteiraSetId));
            minoCriteriaId = minoCriteriaSet.getCriteriaSetId();
            immdMinoRelationship = createRelationship(MINO_REL_NAME, immdCriteriaId, minoCriteriaId, exisitingProduct.getID(),
                    --relationshipId);
        }

        Map<String, CriteriaSetValues> immdCriteriaValues = new HashMap<String, CriteriaSetValues>();
        Map<String, CriteriaSetValues> artwCriteriaValues = new HashMap<String, CriteriaSetValues>();
        Map<String, CriteriaSetValues> minoCriteriaValues = new HashMap<String, CriteriaSetValues>();

        for (ImprintMethod impMethod : imprintMethods) {
            CriteriaSetValues immdCriteriaSetValue = null;
            if (impMethod.getType() != null && !impMethod.getType().isEmpty()) {
                immdCriteriaSetValue = imprintMethodProcessor.getImprintCriteriaSetValue(impMethod.getType(), impMethod.getAlias(),
                        immdCriteriaId);
                immdCriteriaValues.put(impMethod.getType().toUpperCase(), immdCriteriaSetValue);
            }
            if (immdCriteriaSetValue != null && impMethod.getMinimumOrder() != null) {
                CriteriaSetValues minoCriteriaSetValue = minoProcessor.getMinQtyCriteriaSetValue(impMethod.getMinimumOrder(),
                        minoCriteriaId);
                if (minoCriteriaSetValue != null) {
                    minoCriteriaValuePathList
                            .addAll(getValuePaths(immdCriteriaSetValue, minoCriteriaSetValue, immdMinoRelationship));
                    minoCriteriaValues.put(minoProcessor.getKey(impMethod.getMinimumOrder()), minoCriteriaSetValue);
                }
            }

            if (immdCriteriaSetValue != null && impMethod.getArtwork() != null && !impMethod.getArtwork().isEmpty()) {
                for (Artwork art : impMethod.getArtwork()) {
                    CriteriaSetValues artwCriteriaSetValue = artworkProcessor.getArtworkCriteriaSetValue(
                            exisitingProduct.getExternalProductId(), art, artwCriteriaId);
                    if (artwCriteriaSetValue != null) {
                        artwCriteriaValues.put(art.getValue().toUpperCase(), artwCriteriaSetValue);
                        artworkCriteriaValuePathList.addAll(getValuePaths(immdCriteriaSetValue, artwCriteriaSetValue,
                                immdArtwRelationship));
                    }
                }
            }

        }
        boolean imprintAvailable = false;

        if (immdCriteriaValues != null && !immdCriteriaValues.isEmpty()) {
            imprintAvailable = true;
            immdCriteriaSet.setCriteriaSetValues(new ArrayList<CriteriaSetValues>());
            immdCriteriaSet.getCriteriaSetValues().addAll(immdCriteriaValues.values());
            existingCriteriaSetMap.put(ApplicationConstants.CONST_IMPRINT_METHOD_CODE, immdCriteriaSet);

        }
        List<Relationship> relationships = exisitingProduct.getRelationships();

        if (imprintAvailable && artwCriteriaValues != null && !artwCriteriaValues.isEmpty()) {
            artwCriteriaSet.setCriteriaSetValues(new ArrayList<CriteriaSetValues>());
            artwCriteriaSet.getCriteriaSetValues().addAll(artwCriteriaValues.values());
            existingCriteriaSetMap.put(ApplicationConstants.CONST_ARTWORK_CODE, artwCriteriaSet);
            immdArtwRelationship.setCriteriaSetValuePaths(artworkCriteriaValuePathList);
            relationships = replaceRelationship(relationships, immdArtwRelationship);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_ARTWORK_CODE);
        }

        if (minoCriteriaValues != null && !minoCriteriaValues.isEmpty()) {
            minoCriteriaSet.setCriteriaSetValues(new ArrayList<CriteriaSetValues>());
            minoCriteriaSet.getCriteriaSetValues().addAll(minoCriteriaValues.values());
            existingCriteriaSetMap.put(ApplicationConstants.CONST_MINIMUM_QUANTITY, minoCriteriaSet);
            immdMinoRelationship.setCriteriaSetValuePaths(minoCriteriaValuePathList);
            relationships = replaceRelationship(relationships, immdMinoRelationship);
        }

        imprintRelationData.setExistingCriteriaSetMap(existingCriteriaSetMap);
        imprintRelationData.setRelationships(relationships);

        return imprintRelationData;

    }

    private ProductCriteriaSets getProductCriteriaSet(ProductDetail exisitingProduct, String configId, String criteriaCode,
            String criteiraSetId) {
        ProductCriteriaSets productCriteriaSets = new ProductCriteriaSets();
        productCriteriaSets.setCriteriaSetId(criteiraSetId);
        productCriteriaSets.setProductId(exisitingProduct.getID());
        productCriteriaSets.setCompanyId(exisitingProduct.getCompanyId());
        productCriteriaSets.setConfigId(configId);
        productCriteriaSets.setCriteriaCode(criteriaCode);
        productCriteriaSets.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        productCriteriaSets.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        productCriteriaSets.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);

        return productCriteriaSets;
    }

    private List<Relationship> replaceRelationship(List<Relationship> relationships, Relationship relToReplace) {
        if (relationships != null && !relationships.isEmpty()) {
            Iterator<Relationship> relItr = relationships.iterator();
            while (relItr.hasNext()) {
                Relationship temp = relItr.next();
                if (temp.getID().equals(relToReplace.getID())) {
                    relationships.remove(temp);
                    break;
                }
            }
        } else {

            relationships = new ArrayList<Relationship>();
        }
        relationships.add(relToReplace);

        return relationships;
    }

    private Relationship createRelationship(String name, String parent, String child, String productId, int relationId) {
        Integer pid = Integer.parseInt(productId);
        Integer parentId = Integer.parseInt(parent);
        Integer childId = Integer.parseInt(child);

        Relationship rel = new Relationship();
        rel.setID(relationId);
        rel.setProductId(pid);
        rel.setParentCriteriaSetId(parentId);
        rel.setName(name);
        List<CriteriaSetRelationship> relationshipGrouperList = new ArrayList<CriteriaSetRelationship>(2);

        CriteriaSetRelationship parentRel = new CriteriaSetRelationship();
        parentRel.setRelationshipId(relationshipId);
        parentRel.setIsParent(true);
        parentRel.setCriteriaSetId(parentId);
        parentRel.setProductId(pid);

        relationshipGrouperList.add(parentRel);

        CriteriaSetRelationship childRel = new CriteriaSetRelationship();
        childRel.setRelationshipId(relationshipId);
        childRel.setIsParent(false);
        childRel.setCriteriaSetId(childId);
        childRel.setProductId(pid);

        relationshipGrouperList.add(childRel);
        rel.setCriteriaSetRelationships(relationshipGrouperList);

        return rel;
    }

    public Relationship getRelationshipBasedCriteriaIds(String parentId, String childId, List<Relationship> extRelations) {
        for (Relationship rel : extRelations) {
            if (isRelationshipMatches(parentId, childId, rel.getCriteriaSetRelationships())) {
                return rel;
            }
        }
        return null;
    }

    private boolean isRelationshipMatches(String parent, String child, List<CriteriaSetRelationship> relationshipGrouperList) {
        boolean parentMatched = false;
        boolean childMatched = false;
        for (CriteriaSetRelationship crtRel : relationshipGrouperList) {
            if (crtRel.getIsParent() && String.valueOf(crtRel.getCriteriaSetId()).equalsIgnoreCase(parent)) {
                parentMatched = true;
            }
            if (!crtRel.getIsParent() && String.valueOf(crtRel.getCriteriaSetId()).equalsIgnoreCase(child)) {
                childMatched = true;
            }
        }
        if (parentMatched && childMatched) {
            return true;
        } else {
            return false;
        }
    }

    private List<CriteriaSetValuePath> getValuePaths(CriteriaSetValues parentCriteriaValue, CriteriaSetValues childCriteriaValue,
            Relationship relationship) {
        List<CriteriaSetValuePath> crtValuePaths = getNewCriteriaSetValuePaths(parentCriteriaValue.getId(), childCriteriaValue.getId(), relationship);
        
        if (relationship != null && relationship.getCriteriaSetValuePaths() != null
                && !relationship.getCriteriaSetValuePaths().isEmpty()) {
            return relationshipProcessor.compareCriteriaSetValuePaths(crtValuePaths, relationship.getCriteriaSetValuePaths(), relationship.getID());
        } else {
            return getNewCriteriaSetValuePaths(parentCriteriaValue.getId(), childCriteriaValue.getId(), relationship);
        }
    }

    private List<CriteriaSetValuePath> getNewCriteriaSetValuePaths(String parentId, String childId, Relationship rel) {
        Integer parent = Integer.parseInt(parentId);
        Integer child = Integer.parseInt(childId);

        List<CriteriaSetValuePath> crtValuePath = new ArrayList<CriteriaSetValuePath>();

        CriteriaSetValuePath parentPath = new CriteriaSetValuePath();
        parentPath.setID(--valuePathUniqId);
        parentPath.setIsParent(true);
        parentPath.setCriteriaSetValueId(parent);
        parentPath.setProductId(rel.getProductId());
        parentPath.setRelationshipId(rel.getID());

        crtValuePath.add(parentPath);

        CriteriaSetValuePath childPath = new CriteriaSetValuePath();
        childPath.setID(valuePathUniqId);
        childPath.setIsParent(false);
        childPath.setCriteriaSetValueId(child);
        childPath.setProductId(rel.getProductId());
        childPath.setRelationshipId(rel.getID());

        crtValuePath.add(childPath);

        return crtValuePath;
    }

    // Imprint Method Generation
    /*
     * existingCriteriaSetMap.put(
     * ApplicationConstants.CONST_IMPRINT_METHOD_CODE,
     * processImprintMethod(imprintMethodString, existingProduct,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_METHOD_CODE), configId));
     * 
     * existingCriteriaSetMap.put(
     * ApplicationConstants.CONST_ARTWORK_CODE,
     * processArtwork(imprintMethodString, existingProduct,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ARTWORK_CODE), configId));
     * 
     * existingCriteriaSetMap.put(
     * ApplicationConstants.CONST_MINIMUM_QUANTITY,
     * processMinimumQty(imprintMethodString, existingProduct,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_MINIMUM_QUANTITY), configId));
     */
    /*
     * private ProductCriteriaSets processArtwork(String artworks, ProductDetail existingProduct, ProductCriteriaSets
     * immdCriteriaSet,
     * String configId) {
     * LOGGER.info("Imprint Artwork Transformation Starts :" + artworks);
     * 
     * ProductCriteriaSets artwrkCriteriaSet = null;
     * try {
     * artwrkCriteriaSet = productParser.addCriteriaSetForImprint(existingProduct, existingProduct, artworks, null,
     * ApplicationConstants.CONST_ARTWORK_CODE);
     * } catch (VelocityException e) {
     * LOGGER.error("Exception while processing Artwork", e);
     * e.printStackTrace();
     * }
     * if (artwrkCriteriaSet != null && artwrkCriteriaSet.getCriteriaSetValues() != null
     * && !artwrkCriteriaSet.getCriteriaSetValues().isEmpty()) {
     * return artwrkCriteriaSet;
     * }
     * 
     * return null;
     * }
     * 
     * private ProductCriteriaSets processMinimumQty(String minQuantities, ProductDetail existingProduct,
     * ProductCriteriaSets immdCriteriaSet, String configId) {
     * 
     * LOGGER.info("Imprint Minimum Quantity Transformation Starts :" + minQuantities);
     * // Validate Min_QTY
     * String[] minQtys = minQuantities.split("\\|");
     * String tempElement = "";
     * if (minQtys != null && minQtys.length > 0) {
     * for (int i = 0; i < minQtys.length; i++) {
     * if (!CommonUtilities.isValueNull(minQtys[i])) {
     * String[] valuesArray = minQtys[i].split(":");
     * if (valuesArray != null && valuesArray.length == 2 && valuesArray[0] != null && valuesArray[1] != null) {
     * tempElement += minQtys[i] + "|";
     * } else {
     * // TODO : Add Error logging
     * 
     * tempElement += "null|";
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE + ":Invalid Min_QTY specified - " + minQtys[i];
     * 
     * }
     * } else {
     * tempElement += "null|";
     * }
     * }
     * if (tempElement != null && !tempElement.isEmpty() && tempElement.endsWith("|")) {
     * tempElement = tempElement.substring(0, tempElement.length() - 1);
     * }
     * minQuantities = tempElement;
     * }
     * ProductCriteriaSets minQtyCriteriaSet;
     * try {
     * minQtyCriteriaSet = productParser.addCriteriaSetForImprint(existingProduct, existingProduct, minQuantities, null,
     * ApplicationConstants.CONST_MINIMUM_QUANTITY);
     * if (minQtyCriteriaSet != null) {
     * return minQtyCriteriaSet;
     * }
     * } catch (VelocityException e) {
     * LOGGER.error("Exception while processing Minimum Quantity", e);
     * }
     * 
     * return null;
     * }
     * 
     * 
     * private void processImprintRelations(Map<String, ProductCriteriaSets> existingCriteriaSetMap, List<String>
     * criteriaSetValueIds,
     * List<ImprintMethod> imprintMethods, ProductDetail existingProduct, ProductDetail product) {
     * 
     * String imprintMethodString = "";
     * String minQtyString = "";
     * String artworkString = "";
     * 
     * for (ImprintMethod impMethod : imprintMethods) {
     * imprintMethodString = CommonUtilities.appendValue(imprintMethodString,
     * getImprintString(impMethod.getType(), impMethod.getAlias()), "|");
     * minQtyString = CommonUtilities.appendValue(minQtyString, getMINOString(impMethod.getMinimumOrder()), "|");
     * artworkString = CommonUtilities.appendValue(artworkString, getArtworkValueWithoutComments(impMethod.getArtwork()), "|");
     * }
     * // List<String> criteriaSetValueIds = CommonUtilities.getAllCriteriaSetValueIds(productCriteriaSetsAry);
     * // List<String> extCriteriaSetValueIds =
     * // CommonUtilities.getAllCriteriaSetValueIds(existingProduct.getProductConfigurations()[0].getProductCriteriaSets());
     * Map<String, Relationships> relationShips = new HashMap<String, Relationships>();
     * // List<Relationships> relationShips = new ArrayList<Relationships>();
     * int relationshipId = -1;
     * if (CommonUtilities.isUpdateNeeded(artworkString, false) && !artworkString.equals("")
     * && !CommonUtilities.isElementsAreNull(artworkString, "|")
     * && !imprintMethodString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && !imprintMethodString.equals("") && !artworkString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
     * LOGGER.info("Imprint Relationships Transformation Starts");
     * 
     * try {
     * Relationships relationShip = productParser.createImprintArtworkRelationShip(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ARTWORK_CODE), product, imprintMethodString,
     * artworkString, true, String.valueOf(relationshipId));
     * 
     * if (relationShip != null) {
     * // Before adding relationship we need to check for existence of this relationship
     * if (existingProduct != null) {
     * relationShip = productParser.compareAndUpdateRelationship(existingProduct.getRelationships(), relationShip,
     * ApplicationConstants.CONST_ARTWORK_CODE, productCriteriaSetsAry);
     * }
     * if (relationShip != null) {
     * relationShips.put(ApplicationConstants.CONST_ARTWORK_CODE, relationShip);
     * relationshipId--;
     * }
     * }
     * } catch (Exception e) {
     * LOGGER.error("Exception occured in Artwork RL processing, continue work : " + e.getMessage(), e);
     * }
     * }
     * 
     * if (!minQtyString.equals("") && !CommonUtilities.isElementsAreNull(minQtyString, "|")
     * && !minQtyString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL) && !minQtyString.equals("")
     * && !minQtyString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(minQtyString, true)) {
     * LOGGER.info("Imprint Relationships Transformation Starts");
     * try {
     * Relationships relationShip = productParser.createImprintArtworkRelationShip(productCriteriaSetsAry, product,
     * imprintMethodString, minQtyString, false, String.valueOf(relationshipId));
     * 
     * if (relationShip != null) {
     * if (existingProduct != null) {
     * relationShip = productParser.compareAndUpdateRelationship(existingProduct.getRelationships(), relationShip,
     * ApplicationConstants.CONST_MINIMUM_QUANTITY, productCriteriaSetsAry);
     * }
     * if (relationShip != null) {
     * relationShips.put(ApplicationConstants.CONST_MINIMUM_QUANTITY, relationShip);
     * relationshipId--;
     * }
     * }
     * } catch (Exception e) {
     * LOGGER.error("Exception occured in MinQTY RL Process, continue process : " + e.getMessage());
     * }
     * }
     * 
     * product.setRelationships(ProductCompareUtil.mergeRelationShips(relationShips, existingProduct));
     * // product.setRelationships(ProductCompareUtil.validateAllRelationships(product.getRelationships(), criteriaSetValueIds));
     * 
     * }
     * 
     * private String getArtworkValue(List<Artwork> artworkList) {
     * String artworkString = "";
     * if (artworkList == null || artworkList.isEmpty()) {
     * return "null";
     * } else {
     * for (Artwork artw : artworkList) {
     * artworkString = CommonUtilities.appendValue(artworkString, artw.getValue() + "$$$" + artw.getComments(), ",");
     * }
     * }
     * return artworkString;
     * }
     * 
     * private String getArtworkValueWithoutComments(List<Artwork> artworkList) {
     * String artworkString = "";
     * if (artworkList == null || artworkList.isEmpty()) {
     * return "null";
     * } else {
     * for (Artwork artw : artworkList) {
     * artworkString = CommonUtilities.appendValue(artworkString, artw.getValue(), ",");
     * }
     * }
     * return artworkString;
     * }
     * 
     * private String getImprintString(String value, String aliace) {
     * if (CommonUtilities.isValueNull(aliace)) {
     * return value;
     * } else {
     * return aliace + "=" + value;
     * }
     * }
     * 
     * private String getMINOString(MinimumOrder minimumOrder) {
     * if (minimumOrder == null) {
     * return "null";
     * } else {
     * return minimumOrder.getValue() + ":" + minimumOrder.getUnit();
     * }
     * }
     * 
     * 
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
