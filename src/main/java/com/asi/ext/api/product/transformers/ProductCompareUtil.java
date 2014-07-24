package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.asi.ext.api.radar.model.CriteriaSetRelationships;
import com.asi.ext.api.radar.model.CriteriaSetValuePaths;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.radar.model.ProductDataSheet;
import com.asi.ext.api.radar.model.ProductInventoryLink;
import com.asi.ext.api.radar.model.ProductMediaCitations;
import com.asi.ext.api.radar.model.ProductMediaItems;
import com.asi.ext.api.radar.model.Relationships;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductDetail;

/**
 * Utility class for comparing current {@link Product} elements with existing {@link Product}
 * 
 * @author Rahul K
 * @category B>Utility tools </B>
 * @see CommonUtilities
 */
public class ProductCompareUtil extends CommonUtilities {
    /**
     * Always set this COMPARE_FLAG to false when a method start processing
     */
    private static boolean         COMPARE_FLAG     = false;
    public static ProductDataStore productDataStore = new ProductDataStore();

    /**
     * Compares the current {@link ProductMediaItems} with existing {@link ProductMediaItems} and
     * updates the current ProductMediaItems with existing one.
     * 
     * @param currentProduct
     *            is the current {@link Product}
     * @param newMediaItems
     *            is the current {@link ProductMediaItems}
     * @param existingMediaItems
     *            is the existing {@link ProductMediaItems}
     * @return
     */
    public static ProductMediaItems[] compareAndUpdateProductMediaItems(Product currentProduct, ProductMediaItems[] newMediaItems,
            ProductMediaItems[] existingMediaItems) {

        if (existingMediaItems != null && existingMediaItems.length > 0) {

        } else {
            return newMediaItems;
        }

        return null;
    }

    public static boolean checkRequiredFields(Product product, Product existingProduct) {
        boolean isValidForSaving = true;
        String externalProductId = product.getExternalProductId();
        // Check Product Name

        if (isValueNull(product.getName())) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Name cannot be empty");
            isValidForSaving = false;
        } else if (existingProduct != null && !isUpdateNeeded(product.getName()) && isValueNull(existingProduct.getName())) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Name cannot be empty");
            isValidForSaving = false;
        } else if (existingProduct == null && !isUpdateNeeded(product.getName())) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Name cannot be empty");
            isValidForSaving = false;
        }
        // Check Product Description
        if (isValueNull(product.getDescription())) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Description cannot be empty");
            isValidForSaving = false;
        } else if (existingProduct != null && !isUpdateNeeded(product.getDescription())
                && isValueNull(existingProduct.getDescription())) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Description cannot be empty");
            isValidForSaving = false;
        } else if (existingProduct == null && !isUpdateNeeded(product.getDescription())) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Description cannot be empty");
            isValidForSaving = false;
        }
        // Check product categories
        if (product.getSelectedProductCategories() == null || product.getSelectedProductCategories().length == 0) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Categories cannot be empty");
        } else if (isValueNull(product.getSelectedProductCategories()[0].getCode()) && existingProduct == null) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Categories cannot be empty");
            isValidForSaving = false;
        } else if (!isUpdateNeeded(product.getSelectedProductCategories()[0].getCode()) && existingProduct == null) {
            productDataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD,
                    "Product Categories cannot be empty");
            isValidForSaving = false;
        }
        return isValidForSaving;
    }

    public static ProductMediaCitations[] comapareAndUpdateProductMediaCitations(Product product,
            ProductMediaCitations[] newMediaCitations, ProductMediaCitations[] existingMediaCitations) {
        return null;
    }

    public static ProductCriteriaSets[] comapreAndUpdateProductCriteriaSet(String criteriaCode,
            ProductCriteriaSets[] currentProductCriteriaSet, ProductCriteriaSets[] existingProductCriteriaSets) {

        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)) {
            // Multiple CriteriaSetProcessing

        } else {
            // Normal CriteriaSet processing

        }
        return null;
    }

    public static com.asi.service.product.client.vo.ProductCriteriaSets multipleProductCriteriaSetCompareAndUpdate(ProductDetail product, String criteriaCode,
            com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSet, List<com.asi.service.product.client.vo.ProductCriteriaSets> existingProductCriteriaSets,
            boolean requiredValueComaprison) {
        if (existingProductCriteriaSets != null && !existingProductCriteriaSets.isEmpty()) {
            return multipleProductCriteriaSetsCompareAndUpdate(product, criteriaCode, currentProductCriteriaSet,
                    existingProductCriteriaSets, requiredValueComaprison);
        } else {
            return currentProductCriteriaSet;
        }
    }

    public static com.asi.service.product.client.vo.ProductCriteriaSets multipleProductCriteriaSetsCompareAndUpdate(ProductDetail product, String criteriaCode,
            com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSet, List<com.asi.service.product.client.vo.ProductCriteriaSets> existingProductCriteriaSets,
            boolean requiredValueComaprison) {
        COMPARE_FLAG = false;
        if (currentProductCriteriaSet != null && !currentProductCriteriaSet.getCriteriaSetValues().isEmpty()
                && existingProductCriteriaSets != null && !existingProductCriteriaSets.isEmpty()) {

            /*if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
                return compareAndUpdateArtworkCriteriaSet(product.getExternalProductId(), currentProductCriteriaSet,
                        existingProductCriteriaSets[0]);
            }*/
            for (com.asi.service.product.client.vo.ProductCriteriaSets extCtiteriaSet : existingProductCriteriaSets) {
                if (extCtiteriaSet.getCriteriaSetValues() != null
                        && !extCtiteriaSet.getCriteriaSetValues().isEmpty()) {
                    String optionName = currentProductCriteriaSet.getCriteriaDetail();
                    String optionDescription = currentProductCriteriaSet.getDescription();
                    currentProductCriteriaSet = compareAndUpdateSingleCriteriaSetWithExisting(product, criteriaCode,
                            currentProductCriteriaSet, extCtiteriaSet, requiredValueComaprison);
                    if (COMPARE_FLAG) {
                        if (!optionName.trim().equalsIgnoreCase(extCtiteriaSet.getCriteriaDetail().trim())) {
                            currentProductCriteriaSet.setCriteriaDetail(optionName);
                        }
                        if (!optionDescription.trim().equalsIgnoreCase(extCtiteriaSet.getDescription().trim())) {
                            currentProductCriteriaSet.setDescription(optionDescription);
                        }
                        return currentProductCriteriaSet;
                    }
                }
            }
        } else {
            return currentProductCriteriaSet;
        }

        return currentProductCriteriaSet;
    }

    @SuppressWarnings("unused")
    private static ProductCriteriaSets compareAndUpdateArtworkCriteriaSet(String externalPrductId,
            ProductCriteriaSets currentArtworkCrtCets, ProductCriteriaSets extArtworkCrtCets) {
        CriteriaSetValues[] currentCriteriaSetValues = currentArtworkCrtCets.getCriteriaSetValues();
        CriteriaSetValues[] extCriteriaSetValues = extArtworkCrtCets.getCriteriaSetValues();
        List<String> tempList = new ArrayList<>();
        boolean isAnyMatchFound = false;
        for (int i = 0; i < currentCriteriaSetValues.length; i++) {
            int freequency = 0;
            if (currentCriteriaSetValues[i] == null || currentCriteriaSetValues[i].getValue() == null) {
                continue;
            }

            if (!tempList.isEmpty() && tempList.contains(currentCriteriaSetValues[i].getValue().toString())) {
                freequency = Collections.frequency(tempList, currentCriteriaSetValues[i].getValue());
            }

            if (currentCriteriaSetValues[i].getCriteriaSetCodeValues() != null
                    && currentCriteriaSetValues[i].getCriteriaSetCodeValues().length > 0
                    && currentCriteriaSetValues[i].getCriteriaSetCodeValues()[0].getSetCodeValueId() != null) {

                CriteriaSetValues criteriaSetValue = getCriteriaSetValueFromExistingList(extCriteriaSetValues, freequency,
                        currentCriteriaSetValues[i].getValue().toString(),
                        currentCriteriaSetValues[i].getCriteriaSetCodeValues()[0].getSetCodeValueId());
                if (criteriaSetValue != null) {
                    isAnyMatchFound = true;
                    productDataStore.updateCriteriaReferenceValueTableById(externalPrductId, criteriaSetValue.getCriteriaCode(),
                            currentCriteriaSetValues[i].getId(), criteriaSetValue.getId());
                    currentCriteriaSetValues[i] = criteriaSetValue;
                    currentArtworkCrtCets.setCriteriaSetId(extArtworkCrtCets.getCriteriaSetId());
                    tempList.add(criteriaSetValue.getValue().toString());
                } else if (isAnyMatchFound) {
                    currentArtworkCrtCets.setCriteriaSetId(extArtworkCrtCets.getCriteriaSetId());
                    tempList.add(currentCriteriaSetValues[i].getValue().toString());
                    currentCriteriaSetValues[i].setCriteriaSetId(extArtworkCrtCets.getCriteriaSetId());
                }

            }
        }
        currentArtworkCrtCets.setCriteriaSetValues(currentCriteriaSetValues);
        return currentArtworkCrtCets;
    }

    private static CriteriaSetValues getCriteriaSetValueFromExistingList(CriteriaSetValues[] extCriteriaSetValues, int fromPos,
            String value, String setCodeValueId) {

        if (extCriteriaSetValues != null && extCriteriaSetValues.length > 0 && value != null && setCodeValueId != null) {
            int count = 0;
            for (int i = 0; i < extCriteriaSetValues.length; i++) {
                if (extCriteriaSetValues[i] != null
                        && extCriteriaSetValues[i].getValue() != null
                        && extCriteriaSetValues[i].getValue().equals(value)
                        && extCriteriaSetValues[i].getCriteriaSetCodeValues() != null
                        && extCriteriaSetValues[i].getCriteriaSetCodeValues().length > 0
                        && extCriteriaSetValues[i].getCriteriaSetCodeValues()[0].getSetCodeValueId() != null
                        && extCriteriaSetValues[i].getCriteriaSetCodeValues()[0].getSetCodeValueId().equalsIgnoreCase(
                                setCodeValueId)) {

                    if (count == fromPos) {
                        return extCriteriaSetValues[i];
                    }
                    count++;
                }
            }
        }
        return null;
    }

    private static com.asi.service.product.client.vo.ProductCriteriaSets compareAndUpdateSingleCriteriaSetWithExisting(ProductDetail product, String criteriaCode,
            com.asi.service.product.client.vo.ProductCriteriaSets currentProductCriteriaSet, com.asi.service.product.client.vo.ProductCriteriaSets existingProductCriteriaSet,
            boolean requiredValueComaprison) {
        COMPARE_FLAG = false;
        if (!String.valueOf(currentProductCriteriaSet.getCriteriaDetail()).equalsIgnoreCase(
                String.valueOf(existingProductCriteriaSet.getCriteriaDetail()))) {
            return currentProductCriteriaSet;
        }
        List<com.asi.service.product.client.vo.CriteriaSetValues> crntCriteriaSetValues = currentProductCriteriaSet.getCriteriaSetValues();
        List<com.asi.service.product.client.vo.CriteriaSetValues> existCriteriaSetValues = existingProductCriteriaSet.getCriteriaSetValues();

        crntCriteriaSetValues = compareTwoCriteriaSetValueArray(product, criteriaCode, crntCriteriaSetValues,
                existCriteriaSetValues, requiredValueComaprison);

        if (COMPARE_FLAG) { // Matched and updated result
            existingProductCriteriaSet.setIsRequiredForOrder(currentProductCriteriaSet.getIsRequiredForOrder());
            existingProductCriteriaSet.setIsMultipleChoiceAllowed(currentProductCriteriaSet.getIsMultipleChoiceAllowed());

            currentProductCriteriaSet = existingProductCriteriaSet;
            currentProductCriteriaSet.setCriteriaSetValues(crntCriteriaSetValues);

            return currentProductCriteriaSet; // TODO Assign operation
        } else {
            return currentProductCriteriaSet;
        }

    }

    private static List<com.asi.service.product.client.vo.CriteriaSetValues> compareTwoCriteriaSetValueArray(ProductDetail product, String criteriaCode,
            List<com.asi.service.product.client.vo.CriteriaSetValues> currentCriteriaSetValues, List<com.asi.service.product.client.vo.CriteriaSetValues> existCriteriaSetValues,
            boolean requiredValueComaprison) {
        COMPARE_FLAG = false;
        String tempCriteriaId = null;
        List<com.asi.service.product.client.vo.CriteriaSetValues> finalValuesList = new ArrayList<>();
        for (com.asi.service.product.client.vo.CriteriaSetValues crntCrtValue : currentCriteriaSetValues) {
            
            if (crntCrtValue.getCriteriaSetCodeValues() != null
                    && crntCrtValue.getCriteriaSetCodeValues().length > 0) {

                for (com.asi.service.product.client.vo.CriteriaSetValues extCrValue : existCriteriaSetValues) {

                    if (extCrValue.getCriteriaSetCodeValues() != null
                            && extCrValue.getCriteriaSetCodeValues().length > 0) {
                        String existSetCodeValueId = extCrValue.getCriteriaSetCodeValues()[0]
                                .getSetCodeValueId();

                        if (crntCrtValue.getCriteriaSetCodeValues()[0].getSetCodeValueId()
                                .equals(existSetCodeValueId)) {

                            if (requiredValueComaprison) {
                                // TODO : Write a function to compare values, because Value can be Object or String better logic is,
                                // call function based on instance type
                                if (compareValuesByString(crntCrtValue,
                                        extCrValue, criteriaCode)) {
                                    tempCriteriaId = extCrValue.getCriteriaSetId();
                                    productDataStore.updateCriteriaReferenceValueTableById(product.getExternalProductId(),
                                            criteriaCode, crntCrtValue.getId(),
                                            extCrValue.getId());
                                    crntCrtValue = extCrValue;
                                    finalValuesList.add(extCrValue);
                                    COMPARE_FLAG = true;
                                    break;
                                }
                            } else {
                                // crntCrtValue.setCriteriaSetId(existCriteriaSet.getCriteriaSetId());
                                COMPARE_FLAG = true;
                                tempCriteriaId = extCrValue.getCriteriaSetId();
                                productDataStore.updateCriteriaReferenceValueTableById(product.getExternalProductId(),
                                        criteriaCode, crntCrtValue.getId(),
                                        extCrValue.getId());
                                crntCrtValue = extCrValue;
                                finalValuesList.add(extCrValue);
                                break;
                            }
                        }
                        
                    }

                }
                if (!COMPARE_FLAG) {
                    finalValuesList.add(crntCrtValue);
                }
            }
        }

        if (COMPARE_FLAG) { // which mean if at least one match was found then the entire new data belongs to exisitng criteriaSet
            for (int crntCritiaSetValsCntr = 0; crntCritiaSetValsCntr < finalValuesList.size(); crntCritiaSetValsCntr++) {
                finalValuesList.get(crntCritiaSetValsCntr).setCriteriaSetId(tempCriteriaId);
            }
        }
        return finalValuesList;
    }

    public void registerExitingCriteriaValuesToReferenceTable(String externalProductId, String criteriaCode,
            ProductCriteriaSets[] productCriteriaSets) {
        CriteriaValueDecoder criteriaValueDecoder = new CriteriaValueDecoder();
        if (productCriteriaSets != null && productCriteriaSets.length > 0 && !isValueNull(criteriaCode)) {
            for (int i = 0; i < productCriteriaSets.length; i++) {

                if (productCriteriaSets[i] != null) {
                    if (ProductParser.isOptionGroup(criteriaCode)) {
                        criteriaValueDecoder.updateOptionCriteriaValuesToReference(externalProductId, productCriteriaSets[i]);
                    } else if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_TRADE_NAME_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE)
                            || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE)) {
                        criteriaValueDecoder.updateSimpleCriteriaValuesToReference(externalProductId, productCriteriaSets[i]);
                    }
                }
            }
        }
    }

    public Relationships[] compareAndUpdateRelationships(Relationships[] currentRelationships, Relationships[] existingRelationships) {
        // We need to skip IMMD X ARTW and IMMD X MINO relationship here because we already corrected it,
        // Because there are other relationships also available in this relationship array
        // We need to validate those element as well otherwise save may get Failed
        List<Relationships> currentRelationshiList = currentRelationships != null ? Arrays.asList(currentRelationships)
                : new ArrayList<Relationships>();
        if (existingRelationships != null && existingRelationships.length > 0) {
            // Iterate each relationships
            for (Relationships relationship : existingRelationships) {
                // Null check and Skip IMMD X ARTW and IMMD X MINO relationship
                if (relationship != null && !relationship.getName().equalsIgnoreCase("Imprint Method x Artwork")
                        && !relationship.getName().equalsIgnoreCase("Imprint Method x Min Order")) {
                    /*
                     * if (validateRelationship(relationship)) {
                     * currentRelationshiList.add(relationship);
                     * }
                     */
                }

            }
        }
        return currentRelationshiList.toArray(new Relationships[0]);
    }

    public Relationships getValidRelationshipForCriteriaCodes(ProductCriteriaSets[] productCriteriaSets,
            Relationships[] relationships, String criteriaCode1, String criteriaCode2, List<String> criteriaSetValueIds) {
        if (productCriteriaSets.length < 0) {
            return null;
        }
        Relationships relationships2 = getRelationshipBasedOnCriteriaCodes(relationships, criteriaCode1, criteriaCode2,
                productCriteriaSets);

        if (relationships2 != null) {
            relationships2 = validateAndFixRelationship(relationships2, criteriaSetValueIds);
            if (relationships2 != null) {
                return relationships2;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    protected Relationships getRelationshipBasedOnCriteriaCodes(Relationships[] relationships, String parentCriteriaCode,
            String childCriteriaCode, ProductCriteriaSets[] productCriteriaSets) {

        if (relationships == null || productCriteriaSets == null || productCriteriaSets.length < 0) {
            return null;
        }
        ProductParser productParser = new ProductParser();
        String parentId = null;
        String childId = null;

        ProductCriteriaSets parentCriteriaSet = productParser.getCriteriaSetBasedOnCriteriaCode(productCriteriaSets,
                parentCriteriaCode);
        if (parentCriteriaSet != null) {
            parentId = parentCriteriaSet.getCriteriaSetId();
        }
        if (parentId == null || parentId.isEmpty()) {
            return null;
        }
        ProductCriteriaSets childCriteriaSet = productParser.getCriteriaSetBasedOnCriteriaCode(productCriteriaSets,
                childCriteriaCode);
        if (childCriteriaSet != null) {
            childId = childCriteriaSet.getCriteriaSetId();
        }
        if (childId == null || childId.isEmpty()) {
            return null;
        }

        return getRelationshipByIds(relationships, parentId, childId);
    }

    private Relationships getRelationshipByIds(Relationships[] relationships, String primaryId, String secondaryId) {
        Relationships matchedRelationships = null;
        if (relationships != null && relationships.length > 0) {

            for (Relationships relationship : relationships) {
                boolean parentMatched = false;
                boolean childMatched = false;

                if (relationship != null) {
                    CriteriaSetRelationships[] criteriaSetRelationships = relationship.getCriteriaSetRelationships();
                    if (criteriaSetRelationships != null && criteriaSetRelationships.length == 2) {
                        for (CriteriaSetRelationships crtSetRlship : criteriaSetRelationships) {
                            if (crtSetRlship != null && crtSetRlship.getIsParent().equalsIgnoreCase("true")) {
                                parentMatched = crtSetRlship.getCriteriaSetId().equalsIgnoreCase(primaryId);
                            } else if (crtSetRlship != null && crtSetRlship.getIsParent().equalsIgnoreCase("false")) {
                                childMatched = crtSetRlship.getCriteriaSetId().equalsIgnoreCase(secondaryId);
                            }
                        }
                    }
                    if (parentMatched && childMatched) {
                        return relationship;
                    }
                }
            }
        }
        return matchedRelationships;
    }

    public static Relationships[] validateAllRelationships(Relationships[] relationships, List<String> criteriaSetValueIds) {
        List<Relationships> finalRelationships = new ArrayList<Relationships>();
        for (Relationships rel : relationships) {
            Relationships temp = validateAndFixRelationship(rel, criteriaSetValueIds);
            if (temp != null) {
                finalRelationships.add(temp);
            }
        }
        return finalRelationships.toArray(new Relationships[0]);
    }

    private static Relationships validateAndFixRelationship(Relationships relationship, List<String> criteriaSetValueIds) {
        // If any of the criteriaSetValuePath not exists in the referenceTable we ignore that relation
        List<CriteriaSetValuePaths> finalCriteriaSetValuePaths = new ArrayList<CriteriaSetValuePaths>();
        if (relationship.getCriteriaSetValuePaths() != null && relationship.getCriteriaSetValuePaths().length > 0) {
            CriteriaSetValuePaths[] criteriaSetValuePaths = relationship.getCriteriaSetValuePaths();
            for (CriteriaSetValuePaths criteriaPath : criteriaSetValuePaths) {
                if (criteriaPath != null) {
                    if (!criteriaSetValueIds.contains(criteriaPath.getCriteriaSetValueId())) {
                        // Invalid Criteria setValue path
                    } else {
                        finalCriteriaSetValuePaths.add(criteriaPath);
                    }
                } else {
                    return null;
                }
            }
            if (!finalCriteriaSetValuePaths.isEmpty()) {
                relationship.setCriteriaSetValuePaths(finalCriteriaSetValuePaths.toArray(new CriteriaSetValuePaths[0]));
                return relationship;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * protected static Product syncProductWithExisting(Product currentProduct, Product existingProduct) {
     * if (existingProduct != null) {
     * existingProduct.setDescription(CommonUtilities.checkAndUpdate(currentProduct.getDescription(),
     * existingProduct.getDescription()));
     * existingProduct.setSummary(CommonUtilities.checkAndUpdate(currentProduct.getSummary(), existingProduct.getSummary()));
     * existingProduct.setDisclaimer(CommonUtilities.checkAndUpdate(currentProduct.getDisclaimer(),
     * existingProduct.getDisclaimer()));
     * existingProduct.setDistributorComments(CommonUtilities.checkAndUpdate(currentProduct.getDistributorComments(),
     * existingProduct.getDistributorComments()));
     * 
     * existingProduct.setIsOrderLessThanMinimumAllowed(CommonUtilities.parseBooleanValue(existingProduct.
     * getIsOrderLessThanMinimumAllowed()));
     * existingProduct.setIsAvailableUnimprinted(existingProduct.getIsAvailableUnimprinted());
     * existingProduct.setIsPersonalizationAvailable(existingProduct.getIsPersonalizationAvailable());
     * // TODO : Correct Prod Num
     * existingProduct.setAsiProdNo(CommonUtilities.checkAndUpdate(CommonUtilities.truncate(currentProduct.getAsiProdNo(), 14),
     * existingProduct.getAsiProdNo()));
     * 
     * 
     * } else {
     * // Update values
     * currentProduct.setDescription(CommonUtilities.checkAndUpdate(currentProduct.getDescription()));
     * currentProduct.setSummary(CommonUtilities.checkAndUpdate(currentProduct.getSummary()));
     * currentProduct.setDisclaimer(CommonUtilities.checkAndUpdate(currentProduct.getDisclaimer()));
     * currentProduct.setDistributorComments(CommonUtilities.checkAndUpdate(currentProduct.getDistributorComments()));
     * 
     * currentProduct.setIsOrderLessThanMinimumAllowed(CommonUtilities.checkAndUpdate(CommonUtilities.parseBooleanValue(currentProduct
     * .getIsOrderLessThanMinimumAllowed())));
     * currentProduct.setIsAvailableUnimprinted(CommonUtilities.checkAndUpdate(CommonUtilities.parseBooleanValue(currentProduct.
     * getIsAvailableUnimprinted())));
     * currentProduct.setIsPersonalizationAvailable(CommonUtilities.checkAndUpdate(CommonUtilities.parseBooleanValue(currentProduct.
     * getIsPersonalizationAvailable())));
     * // TODO : Correct Prod Num
     * currentProduct.setAsiProdNo(CommonUtilities.checkAndUpdate(CommonUtilities.truncate(currentProduct.getAsiProdNo(), 14)));
     * 
     * 
     * }
     * 
     * 
     * 
     * 
     * if (!CommonUtilities.isUpdateNeeded(product.getShipperBillsByCode())) {
     * if (existingProduct != null) {
     * product.setShipperBillsByCode(existingProduct.getShipperBillsByCode());
     * } else {
     * product.setShipperBillsByCode(null);
     * }
     * } else if (CommonUtilities.isWordExistsInReservedWordGroup(product.getShipperBillsByCode(),
     * ApplicationConstants.CONST_STRING_GRP_SHIP_BILL_BY, false)) {
     * product.setShipperBillsByCode(commonUtils.parseWeightValue(product.getShipperBillsByCode()));
     * } else if (product.getShipperBillsByCode() != null && !product.getShipperBillsByCode().trim().isEmpty()
     * && !product.getShipperBillsByCode().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)) {
     * LOGGER.info("Invalid ShipperBillsByCode : " + product.getShipperBillsByCode());
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE + ":Invalid ShipperBillsByCode";
     * product.setShipperBillsByCode(null);
     * }
     * if (!CommonUtilities.isUpdateNeeded(product.getAddtionalShippingInfo())) {
     * if (existingProduct != null) {
     * product.setAddtionalShippingInfo(existingProduct.getAddtionalShippingInfo());
     * } else {
     * product.setAddtionalShippingInfo("");
     * }
     * }
     * 
     * currentProduct = existingProduct != null ? existingProduct : currentProduct;
     * return currentProduct;
     * }
     */

    public static Product comapreAndUpdateDirectElements(Product product, Product existingProduct) {

        product.setChangeProductReasonCode(existingProduct.getChangeProductReasonCode());
        product.setChildChanged(existingProduct.getChildChanged());
        product.setExcludeAppOfferList(existingProduct.getExcludeAppOfferList());
        product.setFullColorProcessFlag(existingProduct.getFullColorProcessFlag());
        product.setIsCustomProduct(existingProduct.getIsCustomProduct());
        product.setIsDirty(existingProduct.getIsDirty());
        product.setIsNullo(existingProduct.getIsNullo());
        product.setIsPriceBreakoutFlag(existingProduct.getIsPriceBreakoutFlag());
        product.setIsProductNumberBreakout(existingProduct.getIsProductNumberBreakout());
        product.setIsWIP(existingProduct.getIsWIP());
        product.setIncludeAppOfferList(existingProduct.getIncludeAppOfferList());
        product.setLocationCode(existingProduct.getLocationCode());
        product.setNewProductFlag(existingProduct.getNewProductFlag());
        product.setNLevelConnectFlag(existingProduct.getNLevelConnectFlag());
        product.setOriginalProductId(existingProduct.getOriginalProductId());
        product.setProductTypeCode(existingProduct.getProductTypeCode());
        product.setProductLockedFlag(existingProduct.getProductLockedFlag());
        product.setProofProdGroupId(existingProduct.getProofProdGroupId());
        product.setProofPageNo(existingProduct.getProofPageNo());
        product.setProofSubIssueId(existingProduct.getProofSubIssueId());
        product.setShow1ProdNo(existingProduct.getShow1ProdNo());
        product.setShow1PriceGridId(existingProduct.getShow1PriceGridId());

        product.setShow1MediaIdIm(existingProduct.getShow1MediaIdIm());
        product.setShow1MediaIdVd(existingProduct.getShow1MediaIdVd());
        product.setStatusCode(existingProduct.getStatusCode());
        product.setVirtualProductFlag(existingProduct.getVirtualProductFlag());
        product.setVisibleForAllUsersFlag(existingProduct.getVisibleForAllUsersFlag());
        product.setWorkflowStatusCode(existingProduct.getWorkflowStatusCode());
        product.setWorkflowStatusStateCode(existingProduct.getWorkflowStatusStateCode());

        return product;
    }

    protected static ProductInventoryLink compareAndUpdateInventoryLink(Product currentProduct, Product existingProduct) {
        ProductInventoryLink currentProdLink = currentProduct.getProductInventoryLink();
        if (existingProduct == null) {
            // Check url is null or not
            if (currentProdLink != null && !isValueNull(currentProdLink.getUrl()) && isUpdateNeeded(currentProdLink.getUrl(), true)) {
                // Verify CompanyID is present if not get it from current product
                if (isValueNull(currentProdLink.getCompanyId())) {
                    currentProdLink.setCompanyId(currentProdLink.getCompanyId());
                    currentProdLink.setProductId("0");
                }
                return currentProdLink;
            } else {
                currentProdLink = new ProductInventoryLink();
                currentProdLink.setCompanyId(currentProdLink.getCompanyId());
                currentProdLink.setProductId("0");
            }
        } else {
            ProductInventoryLink extProdLink = existingProduct.getProductInventoryLink();
            if (!isUpdateNeeded(currentProdLink.getUrl())) {
                currentProdLink = extProdLink;
            } else if (extProdLink != null && !"0".equalsIgnoreCase(extProdLink.getId()) && !isValueNull(extProdLink.getUrl())) {
                // Now compare current element with existing
                if (extProdLink.getUrl().trim().equalsIgnoreCase(currentProdLink.getUrl())) {
                    return extProdLink;
                } else if (isValueNull(currentProdLink.getUrl())) {
                    return null;
                } else {
                    currentProdLink.setProductId(existingProduct.getId());
                    return currentProdLink;
                }
            } else {
                currentProdLink.setProductId(existingProduct.getId());
                return currentProdLink;
            }
        }
        return currentProdLink;
    }

    protected static ProductDataSheet compareAndUpdateProductDatasheet(Product currentProduct, Product existingProduct) {
        ProductDataSheet currentProdDatasheet = currentProduct.getProductDataSheet();
        if (existingProduct == null) {
            // Check url is null or not
            if (currentProdDatasheet != null && !isValueNull(currentProdDatasheet.getUrl())
                    && isUpdateNeeded(currentProdDatasheet.getUrl(), true)) {
                // Verify CompanyID is present if not get it from current product
                if (isValueNull(currentProdDatasheet.getCompanyId())) {
                    currentProdDatasheet.setCompanyId(currentProdDatasheet.getCompanyId());
                    currentProdDatasheet.setProductId("0");
                }
                return currentProdDatasheet;
            } else {
                currentProdDatasheet = new ProductDataSheet();
                currentProdDatasheet.setCompanyId(currentProdDatasheet.getCompanyId());
                currentProdDatasheet.setProductId("0");
            }
        } else {
            ProductDataSheet extProdDatasheet = existingProduct.getProductDataSheet();
            if (!isUpdateNeeded(currentProdDatasheet.getUrl())) {
                currentProdDatasheet = extProdDatasheet;
            } else if (extProdDatasheet != null && !"0".equalsIgnoreCase(extProdDatasheet.getId())
                    && !isValueNull(extProdDatasheet.getUrl())) {
                // Now compare current element with existing
                if (extProdDatasheet.getUrl().trim().equalsIgnoreCase(currentProdDatasheet.getUrl())) {
                    return extProdDatasheet;
                } else if (isValueNull(currentProdDatasheet.getUrl())) {
                    return null;
                } else {
                    currentProdDatasheet.setProductId(existingProduct.getId());
                    return currentProdDatasheet;
                }
            } else {
                currentProdDatasheet.setProductId(existingProduct.getId());
                return currentProdDatasheet;
            }
        }
        return currentProdDatasheet;
    }

    /*
     * public static ProductKeywords[] comapreAndUpdateKeywords(ProductKeywords[] currentProductKeywords, Product existingProduct) {
     * 
     * if (currentProductKeywords != null && currentProductKeywords.length > 0) {
     * ProductKeywordProcessor productKeywordProcessor = new ProductKeywordProcessor();
     * String value = currentProductKeywords[0].getValue();
     * if (value != null && value.trim().startsWith(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
     * // updateNeeded = false;
     * return existingProduct != null ? existingProduct.getProductKeywords() : new ProductKeywords[0];
     * } else if (isValueNull(value)) {
     * return new ProductKeywords[0];
     * } else {
     * return productKeywordProcessor.getProductKeywords(filterDuplicates(value.split(",")), existingProduct, true);
     * }
     * }
     * return new ProductKeywords[0];
     * }
     */

    /*
     * public static SelectedProductCategories[] getCategories(List<String> productCategoryList, String productId,
     * String xid, Product existingProduct) {
     * String crntProductCategory = null;
     * if (productCategoryList != null && !productCategoryList.isEmpty()) {
     * crntProductCategory = convertStringListToCSV(productCategoryList);
     * }
     * SelectedProductCategories[] productCategories = new ProductCategoriesProcessor().getProductCategories(crntProductCategory,
     * productId, xid, existingProduct);
     * 
     * if (productCategories == null || productCategories.length == 0) {
     * return new SelectedProductCategories[] {};
     * }
     * return productCategories;
     * }
     */

    public static Map<String, ProductCriteriaSets> getExistingProductCriteriaSets(ProductCriteriaSets[] productCrteriaSets) {
        Map<String, ProductCriteriaSets> productCrteriaSetMap = new HashMap<>();
        for (ProductCriteriaSets criteriaSet : productCrteriaSets) {
            if (criteriaSet != null) {
                productCrteriaSetMap.put(criteriaSet.getCriteriaCode().trim(), criteriaSet);
            }
        }
        return productCrteriaSetMap;
    }

    public static Map<String, com.asi.service.product.client.vo.ProductCriteriaSets> getExistingProductCriteriaSets(
            List<ProductConfiguration> productConfigs, boolean skipOptions) {
        Map<String, com.asi.service.product.client.vo.ProductCriteriaSets> productCrteriaSetMap = new HashMap<>();
        if (productConfigs == null || productConfigs.isEmpty()) {
            return productCrteriaSetMap;
        }
        for (ProductConfiguration config : productConfigs) {
            if (config != null && config.getProductCriteriaSets() != null && !config.getProductCriteriaSets().isEmpty()) {
                for (com.asi.service.product.client.vo.ProductCriteriaSets criteriaSet : config.getProductCriteriaSets()) {
                    if (criteriaSet != null && !isOptionGroup(criteriaSet.getCriteriaCode())) {
                        productCrteriaSetMap.put(criteriaSet.getCriteriaCode().trim(), criteriaSet);
                    }
                }
            }
        }
        return productCrteriaSetMap;
    }

    public static Map<String, List<com.asi.service.product.client.vo.ProductCriteriaSets>> getOptionCriteriaSets(
            List<ProductConfiguration> productConfigs) {
        Map<String, List<com.asi.service.product.client.vo.ProductCriteriaSets>> options = new HashMap<String, List<com.asi.service.product.client.vo.ProductCriteriaSets>>();

        if (productConfigs == null || productConfigs.isEmpty()) {
            return options;
        }
        for (ProductConfiguration config : productConfigs) {
            if (config != null && config.getProductCriteriaSets() != null && !config.getProductCriteriaSets().isEmpty()) {
                for (com.asi.service.product.client.vo.ProductCriteriaSets criteriaSet : config.getProductCriteriaSets()) {
                    if (criteriaSet != null && isOptionGroup(criteriaSet.getCriteriaCode())) {

                        if (options.get(criteriaSet.getCriteriaCode()) == null) {
                            options.put(criteriaSet.getCriteriaCode(),
                                    new ArrayList<com.asi.service.product.client.vo.ProductCriteriaSets>());
                        }
                        options.get(criteriaSet.getCriteriaCode()).add(criteriaSet);
                    }
                }
            }
        }
        return options;
    }

    protected static Relationships[] mergeRelationShips1(Map<String, Relationships> currentRelationships, Product existingProduct) {
        List<Relationships> finalRelationships = new ArrayList<Relationships>();
        if (existingProduct != null && existingProduct.getRelationships() != null && existingProduct.getRelationships().length > 0) {
            Relationships artworkRelationShip = currentRelationships.get(ApplicationConstants.CONST_ARTWORK_CODE);
            Relationships minQtyRelationShip = currentRelationships.get(ApplicationConstants.CONST_MINIMUM_QUANTITY);

            for (Relationships relationship : existingProduct.getRelationships()) {
                if (artworkRelationShip != null && relationship.getId().equalsIgnoreCase(artworkRelationShip.getId())) {
                    finalRelationships.add(artworkRelationShip);
                } else if (minQtyRelationShip != null && relationship.getId().equalsIgnoreCase(minQtyRelationShip.getId())) {
                    finalRelationships.add(minQtyRelationShip);
                } else {
                    finalRelationships.add(relationship);
                }
            }
            return finalRelationships.toArray(new Relationships[0]);
        } else if (currentRelationships != null && !currentRelationships.isEmpty()) {
            return getRelationshipsFromCollection(currentRelationships.values());
        } else {
            return new Relationships[] {};
        }
    }

    private static Relationships[] getRelationshipsFromCollection1(Collection<Relationships> relationShips) {
        List<Relationships> finalRelationships = new ArrayList<Relationships>();
        for (Iterator<Relationships> iterator = relationShips.iterator(); iterator.hasNext();) {
            Relationships relationships = (Relationships) iterator.next();
            if (relationShips != null) {
                finalRelationships.add(relationships);
            }
        }

        return finalRelationships.toArray(new Relationships[0]);
    }

    protected static Relationships[] mergeRelationShips(Map<String, Relationships> currentRelationships, Product existingProduct) {
        List<Relationships> finalRelationships = new ArrayList<Relationships>();
        if (existingProduct != null && existingProduct.getRelationships() != null && existingProduct.getRelationships().length > 0) {
            Relationships artworkRelationShip = currentRelationships.get(ApplicationConstants.CONST_ARTWORK_CODE);
            Relationships minQtyRelationShip = currentRelationships.get(ApplicationConstants.CONST_MINIMUM_QUANTITY);

            for (Relationships relationship : existingProduct.getRelationships()) {
                if (artworkRelationShip != null && relationship.getId().equalsIgnoreCase(artworkRelationShip.getId())) {
                    finalRelationships.add(artworkRelationShip);
                } else if (minQtyRelationShip != null && relationship.getId().equalsIgnoreCase(minQtyRelationShip.getId())) {
                    finalRelationships.add(minQtyRelationShip);
                } else {
                    finalRelationships.add(relationship);
                }
            }
            return finalRelationships.toArray(new Relationships[0]);
        } else if (currentRelationships != null && !currentRelationships.isEmpty()) {
            return getRelationshipsFromCollection(currentRelationships.values());
        } else {
            return new Relationships[] {};
        }
    }

    private static Relationships[] getRelationshipsFromCollection(Collection<Relationships> relationShips) {
        List<Relationships> finalRelationships = new ArrayList<Relationships>();
        for (Iterator<Relationships> iterator = relationShips.iterator(); iterator.hasNext();) {
            Relationships relationships = (Relationships) iterator.next();
            if (relationShips != null) {
                finalRelationships.add(relationships);
            }
        }

        return finalRelationships.toArray(new Relationships[0]);
    }

    public static String correctImprintMethodForSimplifiedTemplate(String currentImpMethod, ProductCriteriaSets immdCriteriaSet) {
        if (immdCriteriaSet == null) {
            return currentImpMethod;
        }
        String setCodeValueForPersonalization = ProductDataStore.getSetCodeValueIdForImmdMethod("Personalization");
        String setCodeValueForUnImprinted = ProductDataStore.getSetCodeValueIdForImmdMethod("Unimprinted");
        for (CriteriaSetValues criteriaSetValue : immdCriteriaSet.getCriteriaSetValues()) {
            if (criteriaSetValue != null) {
                if (criteriaSetValue.getCriteriaSetCodeValues() != null && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
                    String temp = criteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId();
                    if (setCodeValueForPersonalization.equalsIgnoreCase(temp)) {
                        currentImpMethod = CommonUtilities.appendValue(currentImpMethod, "Personalization",
                                ApplicationConstants.CONST_STRING_COMMA_SEP);
                    } else if (setCodeValueForUnImprinted.equalsIgnoreCase(temp)) {
                        currentImpMethod = CommonUtilities.appendValue(currentImpMethod, "Unimprinted",
                                ApplicationConstants.CONST_STRING_COMMA_SEP);
                    }
                }
            }
        }
        return currentImpMethod;
    }

    public static void registerFobPoints(String externalProductId, ProductCriteriaSets fobCriteriaSet) {
        CriteriaSetValues[] fobCriteriaValues = fobCriteriaSet.getCriteriaSetValues();
        for (CriteriaSetValues criteriaValue : fobCriteriaValues) {
            if (criteriaValue != null) {
                String temp = CommonUtilities.isValueNull(criteriaValue.getBaseLookupValue()) ? String.valueOf(criteriaValue
                        .getValue()) : criteriaValue.getBaseLookupValue();
                temp = CommonUtilities.removeSpaces(temp);
                productDataStore.updateCriteriaSetValueReferenceTable(externalProductId,
                        ApplicationConstants.CONST_CRITERIA_CODE_FOBP, String.valueOf(temp).trim(), criteriaValue.getId());
            }
        }
    }

    /**
     * Compares CriteriaValue1 with CriteriaValue2 based on CriteriaCode
     * 
     * @param criteriaValue1
     * @param criteriaValue2
     * @param criteriaCode
     * @return true if both are equal otherwise false
     */
    public static boolean compareValuesByString(com.asi.service.product.client.vo.CriteriaSetValues criteriaValue1, com.asi.service.product.client.vo.CriteriaSetValues criteriaValue2,
            String criteriaCode) {
        String value1 = isValueNull(String.valueOf(criteriaValue1.getValue()).trim()) ? String.valueOf(criteriaValue1
                .getBaseLookupValue()) : String.valueOf(criteriaValue1.getValue());

        String value2 = isValueNull(String.valueOf(criteriaValue2.getValue()).trim()) ? String.valueOf(criteriaValue2
                .getBaseLookupValue()) : String.valueOf(criteriaValue2.getValue());

        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE) && (isValueNull(value1) || isValueNull(value2))) {
            return true;
        }
        return value1.trim().equalsIgnoreCase(value2.trim());
    }

    public static List<com.asi.service.product.client.vo.ProductCriteriaSets> getFinalProductCriteriaSets(
            Map<String, com.asi.service.product.client.vo.ProductCriteriaSets> simpleCriteriaSets,
            Map<String, List<com.asi.service.product.client.vo.ProductCriteriaSets>> optionGroups,
            com.asi.service.product.client.vo.ProductCriteriaSets[] otherCriteriaSets) {
        List<com.asi.service.product.client.vo.ProductCriteriaSets> finalList = new ArrayList<>();

        if (simpleCriteriaSets != null && !simpleCriteriaSets.isEmpty()) {
            Collection<com.asi.service.product.client.vo.ProductCriteriaSets> criteriaSets = simpleCriteriaSets.values();
            for (Iterator<com.asi.service.product.client.vo.ProductCriteriaSets> iterator = criteriaSets.iterator(); iterator
                    .hasNext();) {
                com.asi.service.product.client.vo.ProductCriteriaSets productCriteriaSets = (com.asi.service.product.client.vo.ProductCriteriaSets) iterator
                        .next();
                if (productCriteriaSets != null) {
                    finalList.add(productCriteriaSets);
                }
            }
        }
        if (optionGroups != null && !optionGroups.isEmpty()) {
            Collection<List<com.asi.service.product.client.vo.ProductCriteriaSets>> options = optionGroups.values();
            for (Iterator<List<com.asi.service.product.client.vo.ProductCriteriaSets>> iterator = options.iterator(); iterator
                    .hasNext();) {
                List<com.asi.service.product.client.vo.ProductCriteriaSets> list = (List<com.asi.service.product.client.vo.ProductCriteriaSets>) iterator
                        .next();
                if (list != null && !list.isEmpty()) {
                    for (com.asi.service.product.client.vo.ProductCriteriaSets productCriteriaSets : list) {
                        finalList.add(productCriteriaSets);
                    }
                }
            }
        }

        if (otherCriteriaSets != null && otherCriteriaSets.length > 0) {
            for (com.asi.service.product.client.vo.ProductCriteriaSets productCriteriaSets : otherCriteriaSets) {
                if (productCriteriaSets != null) {
                    finalList.add(productCriteriaSets);
                }
            }
        }

        return finalList;
    }
}
