package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;

public class ProductSizeGroupProcessor extends SimpleCriteriaProcessor {
    private final static Logger LOGGER                   = Logger.getLogger(ProductSizeGroupProcessor.class.getName());

    public final String[]       SIZE_GROUP_CRITERIACODES = { "CAPS", "DIMS", "SABR", "SAHU", "SAIT", "SANS", "SAWI", "SSNM",
            "SVWT", "SOTH"                              };

    private String              companyId                = "0";
    private String              productId                = "0";
    private String              configId                 = "0";

    /**
     * @param companyId
     * @param productId
     * @param configId
     */
    public ProductSizeGroupProcessor(String companyId, String productId, String configId) {
        this.companyId = companyId;
        this.productId = productId;
        this.configId = configId;
    }

    @Override
    public ProductCriteriaSets getCriteriaSet(String values, Product existingProduct, ProductCriteriaSets matchedCriteriaSet,
            int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

    public ProductCriteriaSets compareAndUpdateSizeGroup(Product existingProduct, ProductCriteriaSets newlyCreatedCriteriaSet,
            ProductCriteriaSets existingCriteriaSet) {
        if (newlyCreatedCriteriaSet == null) {
            return null;
        }
        if (existingCriteriaSet != null) {
            Map<String, CriteriaSetValues> existingMap = createExistingSizesCollection(existingCriteriaSet.getCriteriaSetValues(),
                    existingCriteriaSet.getCriteriaCode());
            List<CriteriaSetValues> finalValues = new ArrayList<CriteriaSetValues>();

            if (!existingMap.isEmpty()) {
                for (CriteriaSetValues criteriaSetValue : newlyCreatedCriteriaSet.getCriteriaSetValues()) {
                    String key = newlyCreatedCriteriaSet.getCriteriaCode() + "_" + getKeyFromValue(criteriaSetValue);
                    if (existingMap.containsKey(key)) {
                        finalValues.add(existingMap.get(key));
                        findSizeValueDetails(criteriaSetValue.getCriteriaCode(), getSizeElementResponse(), existingMap.get(key), existingProduct.getExternalProductId());
                    } else {
                        criteriaSetValue.setCriteriaSetId(existingCriteriaSet.getCriteriaSetId());
                        finalValues.add(criteriaSetValue);
                    }
                }
                existingCriteriaSet.setCriteriaSetValues(finalValues.toArray(new CriteriaSetValues[0]));

                return existingCriteriaSet;
            } else {
                // TODO : Update few attributes of newly created set
                return syncProductCriteriaSet(newlyCreatedCriteriaSet);
            }
        } else if (existingProduct != null) {
            // Set productId and CompanyId and ConfigId;
            newlyCreatedCriteriaSet.setProductId(existingProduct.getId());
            newlyCreatedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            newlyCreatedCriteriaSet.setConfigId(this.configId);
            return newlyCreatedCriteriaSet;
        } else {
            newlyCreatedCriteriaSet.setProductId(this.productId);
            newlyCreatedCriteriaSet.setCompanyId(this.companyId);
            newlyCreatedCriteriaSet.setConfigId(this.configId);
            return newlyCreatedCriteriaSet;
        }
    }

    public Map<String, CriteriaSetValues> createExistingSizesCollection(CriteriaSetValues[] existingCriteriaSetValues,
            String criteriaCode) {
        Map<String, CriteriaSetValues> existing = new HashMap<String, CriteriaSetValues>();
        for (CriteriaSetValues criteriaSetValue : existingCriteriaSetValues) {
            if (criteriaSetValue != null) {
                existing.put(criteriaCode + "_" + getKeyFromValue(criteriaSetValue), criteriaSetValue);
            }
        }
        return existing;
    }

    public ProductCriteriaSets syncProductCriteriaSet(ProductCriteriaSets criteriaSet) {
        criteriaSet.setProductId(this.productId);
        criteriaSet.setCompanyId(this.companyId);
        criteriaSet.setConfigId(this.configId);

        return criteriaSet;
    }

    protected ProductCriteriaSets getProductCriteriaSetForSizeGroup(Map<String, ProductCriteriaSets> criteriaSets) {
        ProductCriteriaSets criteriaSet = null;
        for (String code : SIZE_GROUP_CRITERIACODES) {
            criteriaSet = criteriaSets.get(code);
            if (criteriaSet != null) {
                return criteriaSet;
            }
        }
        return criteriaSet;
    }

    public Map<String, ProductCriteriaSets> removeSizeRelatedCriteriaSetFromExisting(
            Map<String, ProductCriteriaSets> existingCriteriaCollection) {

        try {
            for (String code : SIZE_GROUP_CRITERIACODES) {
                if (existingCriteriaCollection.remove(code) != null) {
                    break;
                }
            }
        } catch (Exception e) {
            // Nothing to do on exception
        }
        return existingCriteriaCollection;
    }

    public ProductCriteriaSets getSizeRelatedCriteriaSetFromExisting(Map<String, ProductCriteriaSets> existingCriteriaCollection) {

        try {
            List<String> sizeCriteriaCodes = Arrays.asList(SIZE_GROUP_CRITERIACODES);
            for (ProductCriteriaSets criteriaSets : existingCriteriaCollection.values()) {
                if (criteriaSets != null) {
                    if (sizeCriteriaCodes.contains(criteriaSets.getCriteriaCode())) {
                        return criteriaSets;
                    }
                }
            }

        } catch (Exception e) {
            // Nothing to do with exception
        }
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

    public boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId) {
        if (criteriaSet == null) {
            return false;
        }
        LOGGER.info("Registering existing Size values of product");
        if (criteriaSet.getCriteriaSetValues() != null && criteriaSet.getCriteriaSetValues().length > 0) {
            for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
                if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
                    findSizeValueDetails(criteriaSet.getCriteriaCode(), getSizeElementResponse(), criteriaValues, externalProductId);
                }
            }
        }
        LOGGER.info("Completed existing Size values of product");

        return false;
    }

    @SuppressWarnings("rawtypes")
    private void findSizeValueDetails(String criteriaCode, LinkedList<LinkedHashMap> criteriaAttributes,
            CriteriaSetValues criteriaSetValue, String externalProductId) {
        // String[] stringAry=new String[2];

        if (criteriaCode.equalsIgnoreCase("DIMS"))
            LOGGER.info("Found Size Group Dimension");
        else if (criteriaCode.equalsIgnoreCase("CAPS"))
            LOGGER.info("Found Size Group Capacity");
        else if (criteriaCode.equalsIgnoreCase("SVWT"))
            LOGGER.info("Found Size Group Volume/Weight");
        else if (criteriaCode.equalsIgnoreCase("SABR"))
            LOGGER.info("Found Size Group Apparel - Bra Sizes");
        else if (criteriaCode.equalsIgnoreCase("SAHU"))
            LOGGER.info("Found Size Group Apparel - Hosiery/Uniform Sizes");
        else if (criteriaCode.equalsIgnoreCase("SAIT"))
            LOGGER.info("Found Size Group Apparel - Infant & Toddler");
        else if (criteriaCode.equalsIgnoreCase("SANS"))
            LOGGER.info("Found Size Group Apparel - Dress Shirt Sizes");
        else if (criteriaCode.equalsIgnoreCase("SAWI"))
            LOGGER.info("Found Size Group Apparel - Pants Sizes");
        else if (criteriaCode.equalsIgnoreCase("SSNM"))
            LOGGER.info("Found Size Group Standard & Numbered");
        else if (criteriaCode.equalsIgnoreCase("SOTH")) LOGGER.info("Found Size Group Other");

        String sizeValue = "", finalSizeValue = "", delim = "";
        String sizeElementValue = "";
        // int noOfSizes=criteriaSetValueLst.size();
        int elementsCntr = 0;
        String unitOfmeasureCode = "";
        int sizeCntr = 0;
        if (criteriaSetValue.getValue() instanceof List) {
            ArrayList<?> valueList = (ArrayList<?>) criteriaSetValue.getValue();
            Iterator<?> sizeValuesItr = valueList.iterator();

            while (sizeValuesItr.hasNext()) {
                LinkedHashMap<?, ?> valueMap = (LinkedHashMap<?, ?>) sizeValuesItr.next();
                unitOfmeasureCode = getSizesElementValue("UNITS", criteriaAttributes, valueMap.get("UnitOfMeasureCode").toString());
                if (unitOfmeasureCode.equals("\"")) unitOfmeasureCode = "in";
                if (unitOfmeasureCode.equals("'")) unitOfmeasureCode = "ft";
                if (criteriaCode.equalsIgnoreCase("DIMS") || criteriaCode.equalsIgnoreCase("SDIM")) {
                    if (criteriaCode.equalsIgnoreCase("DIMS"))
                        sizeValue = getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString())
                                + ":" + valueMap.get("UnitValue") + ":" + unitOfmeasureCode;
                    else
                        sizeValue = valueMap.get("UnitValue") + ":" + unitOfmeasureCode;
                    delim = "; ";
                } else if (criteriaCode.equalsIgnoreCase("CAPS") || criteriaCode.equalsIgnoreCase("SVWT")
                        || criteriaCode.equalsIgnoreCase("SHWT")) {
                    sizeValue = valueMap.get("UnitValue") + ":" + unitOfmeasureCode;
                    delim = ": ";
                } else {
                    if (criteriaCode.equalsIgnoreCase("SAWI")) {
                        if (getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString()).equals(
                                "Waist"))
                            sizeValue = valueMap.get("UnitValue").toString();
                        else if (getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString())
                                .equals("Inseam")) sizeValue = "x" + valueMap.get("UnitValue").toString();
                    } else if (criteriaCode.equalsIgnoreCase("SAIT")) {
                        if (unitOfmeasureCode.length() == 1) {
                            sizeValue = valueMap.get("UnitValue").toString() + unitOfmeasureCode;
                        } else {
                            sizeValue = valueMap.get("UnitValue").toString() + " " + unitOfmeasureCode;
                        }
                    }
                }

                if (sizeCntr != 0) {
                    if (criteriaCode.equalsIgnoreCase("SANS")) {
                        sizeElementValue += "(" + sizeValue.trim() + ")";
                    } else {
                        sizeElementValue += delim + sizeValue;
                    }
                } else {
                    sizeElementValue += sizeValue;
                }
                
                sizeCntr++;
            }
            // updateReferenceTable(existingProduct.getExternalProductId(), ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE,
            // value, criteriaSetValue);
            updateReferenceTable(externalProductId, criteriaCode, sizeElementValue, criteriaSetValue);
            // criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,criteriaSetValue.getID(),sizeElementValue);
        } else {
            sizeValue = criteriaSetValue.getBaseLookupValue();
            if (null == sizeValue) {
                sizeValue = criteriaSetValue.getFormatValue();
            }
            updateReferenceTable(externalProductId, criteriaCode, sizeElementValue, criteriaSetValue);
            // criteriaSetParser.addReferenceSet(externalProductId,criteriaSetValue.getCriteriaCode(),criteriaSetValue.getID(),sizeValue);
            sizeElementValue += sizeValue;
            sizeCntr++;
        }
        if (elementsCntr != 0) {
            finalSizeValue = finalSizeValue + "," + sizeElementValue.trim();
        } else {
            finalSizeValue = sizeElementValue.trim();
        }
        sizeElementValue = "";
    }

    /**
     * Finds the value of a given size element from sizeElementResponses
     * 
     * @param elementName
     *            is the type of size value like Dimension, Weight, etc...
     * @param sizeElementsResponse
     *            contains all the size related details
     * @param attribute
     *            is the value
     * @return Criteria code of the give size value
     */
    @SuppressWarnings("rawtypes")
    public String getSizesElementValue(String elementName, LinkedList<LinkedHashMap> sizeElementsResponse, String attribute) {
        attribute = attribute.trim();
        elementName = elementName.trim();
        String ElementValue = "";
        try {
            // LinkedList<LinkedHashMap> sizeElementsResponse=(LinkedList<LinkedHashMap>)jsonParser.parseToList(response);
            if (null != sizeElementsResponse) {
                Iterator<LinkedHashMap> iterator = sizeElementsResponse.iterator();
                while (iterator.hasNext()) {
                    Map sizeIndividualLookupMap = (LinkedHashMap) iterator.next();
                    if (elementName.equalsIgnoreCase("id")) {
                        if (sizeIndividualLookupMap.get("ID").toString().equals(attribute)) {
                            ElementValue = sizeIndividualLookupMap.get("DisplayName").toString();
                            break;
                        }
                    } else if (elementName.equalsIgnoreCase("units")) {
                        @SuppressWarnings({ "unchecked" })
                        LinkedList<LinkedHashMap> unitValuesList = (LinkedList<LinkedHashMap>) sizeIndividualLookupMap
                                .get("UnitsOfMeasure");
                        Iterator<LinkedHashMap> unitValuesiterator = unitValuesList.iterator();
                        while (unitValuesiterator.hasNext()) {
                            Map codeValueGrpsMap = (LinkedHashMap) unitValuesiterator.next();
                            if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(attribute)) {
                                ElementValue = (String) codeValueGrpsMap.get("Format");
                                break;
                            }
                        }
                    }
                    if (!ElementValue.isEmpty()) break;
                }
            }

        } catch (Exception Ex) {
            LOGGER.error("Exception while processing Product Size Group JSON", Ex);
        }
        return ElementValue;
    }

    @SuppressWarnings("rawtypes")
    public LinkedList<LinkedHashMap> getSizeElementResponse() {
        return ProductDataStore.getLookupResponse();
    }

}
