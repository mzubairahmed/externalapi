/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Availability;
import com.asi.ext.api.service.model.AvailableVariations;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.ProductParserUtil;
import com.asi.service.product.client.vo.CriteriaSetValuePath;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Relationship;

/**
 * @author Rahul K
 * 
 */
public class ProductAvailabilityProcessor {

    private final static Logger   LOGGER           = Logger.getLogger(ProductAvailabilityProcessor.class.getName());

    private RelationshipProcessor relProcessor     = new RelationshipProcessor();

    private ProductDataStore      productDataStore = new ProductDataStore();

    public List<Relationship> getProductAvailabilities(ProductDetail product, List<Availability> productAvailability,
            Map<String, ProductCriteriaSets> criteriaSetMap, Map<String, List<ProductCriteriaSets>> optionsCriteriaSet) {
        int relationId = -4; // Starting with -4
        int uniqPathId = -20;
        List<Relationship> processedRelations = new ArrayList<Relationship>();
        // Iterate each availability and create individual relationships
        if (productAvailability != null && !productAvailability.isEmpty()) {
            for (Availability availability : productAvailability) {

                String parentCriteriaCode = ProductParserUtil.getCriteriaCodeFromCriteria(availability.getParentCriteria(),
                        product.getExternalProductId());
                String childCriteriaCode = ProductParserUtil.getCriteriaCodeFromCriteria(availability.getChildCriteria(),
                        product.getExternalProductId());

                // do validations
                if (!CommonUtilities.isValueNull(parentCriteriaCode) && !CommonUtilities.isValueNull(childCriteriaCode)) {
                    if(parentCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_METHOD_CODE) 
                            && (childCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE) | childCriteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY))) {
                        continue;
                    }
                    if (ProductParserUtil.isValidAvailabilty(availability, ProductParserUtil.isOptionGroup(parentCriteriaCode))) {
                        ProductCriteriaSets parentCrtSet = getCriteriaSet(parentCriteriaCode,
                                ProductParserUtil.isOptionGroup(parentCriteriaCode), availability.getParentOptionName(),
                                criteriaSetMap, optionsCriteriaSet);
                        if (parentCrtSet != null) {
                            ProductCriteriaSets childCrtSet = getCriteriaSet(childCriteriaCode,
                                    ProductParserUtil.isOptionGroup(childCriteriaCode), availability.getChildOptionName(),
                                    criteriaSetMap, optionsCriteriaSet);

                            if (childCrtSet != null) {
                                if (!parentCrtSet.getCriteriaSetId().equalsIgnoreCase(childCrtSet.getCriteriaSetId())) {
                                    // Basic validations completed now create relationship with parent and child
                                    Relationship relationship = relProcessor.createRelationship(ProductParserUtil
                                            .getRelationNameBasedOnCodes(parentCriteriaCode, childCriteriaCode, availability),
                                            parentCrtSet.getCriteriaSetId(), childCrtSet.getCriteriaSetId(), product.getID(),
                                            --relationId);
                                    if (availability.getAvailableVariations() != null
                                            && !availability.getAvailableVariations().isEmpty()) {
                                        List<CriteriaSetValuePath> crtSetValuePaths = new ArrayList<>();
                                        // Create CriteriaSetValuePath combinations for each variation

                                        for (AvailableVariations variation : availability.getAvailableVariations()) {
                                            // if any variations not valid then getCriteriaSetValuePaths() function will log
                                            // appropriate error
                                            // no need to worry about that
                                            crtSetValuePaths.addAll(getCriteriaSetValuePaths(product.getExternalProductId(),
                                                    parentCriteriaCode, childCriteriaCode, variation, relationship.getID(),
                                                    --uniqPathId, product.getID()));
                                        }
                                        relationship.setCriteriaSetValuePaths(crtSetValuePaths);
                                    }
                                    // If there is no availability variations, which means those availability cannot be ordered
                                    // finally compare this relationship and fix ids
                                    processedRelations.add(relProcessor.compareAndUpdateRelationShip(relationship,
                                            parentCrtSet.getCriteriaSetId(), childCrtSet.getCriteriaSetId(),
                                            product.getRelationships()));
                                } else {
                                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                            "Availability : Parent Criteria " + availability.getParentCriteria()
                                                    + " and Child Criteria " + availability.getChildCriteria()
                                                    + " cannot be the same");
                                }
                            } else {
                                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                        "Child Criteria " + availability.getChildCriteria() + "not found for availability");
                            }
                        } else {
                            productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                    ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                    "Parent Criteria " + availability.getParentCriteria() + "not found for availability");
                            // TODO: productDataStore.addErrorToBatchLog("Parent Criteria not found for availability");
                        }

                    } else {
                        productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Product Availability Configuration ");
                        // TODO: productDataStore.addErrorToBatchLog("Invalid Product Availability Configuration ");
                    }
                } else {
                    productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                            ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Product Availability Configuration");
                    // TODO: productDataStore.addErrorToBatchLog("Criteria cannot be empty for availability");
                }
            }
        }
        // add IMMD x ARTW and IMMD x MINO relation back

        processedRelations = includeImprintRelationsIfExists(processedRelations, product.getRelationships(), criteriaSetMap);

        LOGGER.info("Started Processing product availability");
        return processedRelations;

    }

    private ProductCriteriaSets getCriteriaSet(String criteriaCode, boolean isOptGroup, String optNamee,
            Map<String, ProductCriteriaSets> criteriaSetMap, Map<String, List<ProductCriteriaSets>> optionsCriteriaSet) {

        if (isOptGroup) {
            return ProductParserUtil.getOptionCriteriaSet(criteriaCode, optNamee, optionsCriteriaSet);
        } else {
            return criteriaSetMap.get(criteriaCode);
        }
    }

    private List<CriteriaSetValuePath> getCriteriaSetValuePaths(String xid, String parentCode, String childCode,
            AvailableVariations variation, int relationId, int uniqPathId, String pid) {
        String parentSetValueId = ProductParserUtil.getCriteriaSetValueIdBaseOnValueType(xid, parentCode,
                variation.getParentValue());
        if (parentSetValueId != null) {
            String childSetValueId = ProductParserUtil.getCriteriaSetValueIdBaseOnValueType(xid, childCode,
                    variation.getChildValue());
            if (childSetValueId != null) {
                return relProcessor.getNewCriteriaSetValuePaths(parentSetValueId, childSetValueId, relationId, pid, uniqPathId);
            } else {
                productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                        "Availability Child variation " + variation.getChildValue() + " not found");
            }
        } else {
            productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                    "Availability Parent variation " + variation.getParentValue() + " not found");
        }
        return new ArrayList<>();
    }

    private List<Relationship> includeImprintRelationsIfExists(List<Relationship> updatedRels, List<Relationship> extRels,
            Map<String, ProductCriteriaSets> criteriaSetMap) {
        ProductCriteriaSets parentCriteriaSet = criteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
        if (parentCriteriaSet != null) {
            String parentId = parentCriteriaSet.getCriteriaSetId();
            ProductCriteriaSets childCriteriaSet = criteriaSetMap.get(ApplicationConstants.CONST_ARTWORK_CODE); // Artwork
                                                                                                                // processing
            if (childCriteriaSet != null) {
                Relationship immdXartw = relProcessor.getRelationshipBasedCriteriaIds(parentId,
                        childCriteriaSet.getCriteriaSetId(), extRels);
                if (immdXartw != null) {
                    updatedRels.add(immdXartw);
                }
            }
            childCriteriaSet = criteriaSetMap.get(ApplicationConstants.CONST_MINIMUM_QUANTITY); // MIN_QTY processing
            if (childCriteriaSet != null) {
                Relationship immdXmino = relProcessor.getRelationshipBasedCriteriaIds(parentId,
                        childCriteriaSet.getCriteriaSetId(), extRels);
                if (immdXmino != null) {
                    updatedRels.add(immdXmino);
                }
            }
        } else {
            return updatedRels;
        }
        return updatedRels;
    }
}
