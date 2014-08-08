/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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

    private final static Logger   LOGGER       = Logger.getLogger(ProductAvailabilityProcessor.class.getName());

    private RelationshipProcessor relProcessor = new RelationshipProcessor();

    public List<Relationship> getProductAvailabilities(ProductDetail product, List<Availability> productAvailability,
            Map<String, ProductCriteriaSets> criteriaSetMap, Map<String, List<ProductCriteriaSets>> optionsCriteriaSet) {
        LOGGER.info("Started Processing product availability");
        long timeStarted = System.currentTimeMillis();
        int relationId = -4; // Starting with -4
        int uniqPathId = -20;
        List<Relationship> processedRelations = new ArrayList<Relationship>();
        // Iterate each availability and create individual relationships
        for (Availability availability : productAvailability) {

            String parentCriteriaCode = ProductParserUtil.getCriteriaCodeFromCriteria(availability.getParentCriteria());
            String childCriteriaCode = ProductParserUtil.getCriteriaCodeFromCriteria(availability.getChildCriteria());

            // do validations
            if (!CommonUtilities.isValueNull(parentCriteriaCode) && !CommonUtilities.isValueNull(childCriteriaCode)) {
                if (ProductParserUtil.isValidAvailabilty(availability, ProductParserUtil.isOptionGroup(parentCriteriaCode))) {
                    ProductCriteriaSets parentCrtSet = getCriteriaSet(parentCriteriaCode,
                            ProductParserUtil.isOptionGroup(parentCriteriaCode), availability.getParentOptionName(),
                            criteriaSetMap, optionsCriteriaSet);
                    if (parentCrtSet != null) {
                        ProductCriteriaSets childCrtSet = getCriteriaSet(childCriteriaCode,
                                ProductParserUtil.isOptionGroup(childCriteriaCode), availability.getChildOptionName(),
                                criteriaSetMap, optionsCriteriaSet);

                        if (childCrtSet != null) {
                            // Basic validations completed now create relationship with parent and child
                            Relationship relationship = relProcessor.createRelationship(
                                    ProductParserUtil.getRelationNameBasedOnCodes(parentCriteriaCode, childCriteriaCode),
                                    parentCrtSet.getCriteriaSetId(), childCrtSet.getCriteriaSetId(), product.getID(), --relationId);
                            if (availability.getAvailableVariations() != null && !availability.getAvailableVariations().isEmpty()) {
                                List<CriteriaSetValuePath> crtSetValuePaths = new ArrayList<>();
                                // Create CriteriaSetValuePath combinations for each variation
                                System.out.println();
                                for (AvailableVariations variation : availability.getAvailableVariations()) {
                                    // if any variations not valid then getCriteriaSetValuePaths function will log error
                                    // no need to worry about that
                                    System.out.println();
                                    crtSetValuePaths.addAll(getCriteriaSetValuePaths(product.getExternalProductId(),
                                            parentCriteriaCode, childCriteriaCode, variation, relationship.getID(), --uniqPathId,
                                            product.getID()));
                                }
                                relationship.setCriteriaSetValuePaths(crtSetValuePaths);
                            }
                            // If there is no availability variations, which means those availability cannot be ordered
                            // finally compare this relationship and fix ids
                            processedRelations.add(relProcessor.compareAndUpdateRelationShip(relationship,
                                    parentCrtSet.getCriteriaSetId(), childCrtSet.getCriteriaSetId(), product.getRelationships()));

                        } else {
                            // TODO: productDataStore.addErrorToBatchLog("Child Criteria " + + "not found for availability");
                        }
                    } else {
                        // TODO: productDataStore.addErrorToBatchLog("Parent Criteria not found for availability");
                    }

                } else {
                    // TODO: productDataStore.addErrorToBatchLog("Invalid Product Availability Configuration ");
                }
            } else {
                // TODO: productDataStore.addErrorToBatchLog("Criteria cannot be empty for availability");
            }
        }
        // add IMMD x ARTW and IMMD x MINO relation back

        processedRelations = includeImprintRelationsIfExists(processedRelations, product.getRelationships());

        System.out.println("Total time taken for processing Availability is " + (System.currentTimeMillis() - timeStarted) + " ms");

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
                // TODO: productDataStore.addErrorToBatchLog("Availability Child variation " + variation.getChildValue() +
                // " not found");
            }
        } else {
            // TODO: productDataStore.addErrorToBatchLog("Availability Parent variation " + variation.getParentValue() +
            // " not found");
        }
        return new ArrayList<>();
    }

    private List<Relationship> includeImprintRelationsIfExists(List<Relationship> updatedRels, List<Relationship> extRels) {

        Relationship immdXartw = relProcessor.getRelationshipBasedCriteriaIds(ApplicationConstants.CONST_IMPRINT_METHOD_CODE,
                ApplicationConstants.CONST_ARTWORK_CODE, extRels);
        if (immdXartw != null) {
            updatedRels.add(immdXartw);
        }

        Relationship immdXmino = relProcessor.getRelationshipBasedCriteriaIds(ApplicationConstants.CONST_IMPRINT_METHOD_CODE,
                ApplicationConstants.CONST_MINIMUM_QUANTITY, extRels);
        if (immdXmino != null) {
            updatedRels.add(immdXartw);
        }
        return updatedRels;
    }
}
