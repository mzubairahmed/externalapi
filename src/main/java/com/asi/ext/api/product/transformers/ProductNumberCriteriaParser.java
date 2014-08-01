package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductNumber;
import com.asi.service.product.client.vo.ProductNumberConfiguration;

public class ProductNumberCriteriaParser {
    private final static Logger LOGGER           = Logger.getLogger(ProductNumberCriteriaParser.class.getName());
    public ProductDataStore     productDataStore = new ProductDataStore();

    public ProductDetail geProductNumbers(ProductDetail product,
            List<com.asi.ext.api.service.model.ProductNumber> serviceProductNumbers, List<String> criteriaSetValueIds) {

        // Product Numbers Starts Here
        try {
            // ProductNumberCriteriaParser productNumberCriteriaParser = new ProductNumberCriteriaParser();
            List<ProductNumber> productNumberList = generateProductNumberCriterias(product, null, null);

            if (null != product.getProductNumbers() && !product.getProductNumbers().isEmpty()) {
                List<ProductNumber> productNumbers = getValidProductNumbers(product.getProductNumbers(), criteriaSetValueIds);
                product.setProductNumbers(productNumbers);

            } else {
                product.setProductNumbers(new ArrayList<ProductNumber>());
            }
        } catch (Exception ex) {
            product.setProductNumbers(new ArrayList<ProductNumber>());
            LOGGER.error("Exception occured in Product Number Criteria processing : " + ex.getMessage());
        }

        return null;
    }

    /*
     * 
     * 
     * // Product Numbers Starts Here
     * try {
     * ProductNumberCriteriaParser productNumberCriteriaParser = new ProductNumberCriteriaParser();
     * if (isSimplifiedPG && existingProduct != null) {
     * if (null != existingProduct.getProductNumbers() && !existingProduct.getProductNumbers().isEmpty()) {
     * List<ProductNumbers> productNumbers = productNumberCriteriaParser.getValidProductNumbers(
     * existingProduct.getProductNumbers(), criteriaSetValueIds, isSimplifiedPG);
     * product.setProductNumbers(productNumbers);
     * 
     * } else {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * } else if (!isSimplifiedPG && existingProduct != null) {
     * product.setProductNumbers(productNumberCriteriaParser.generateProductNumberCriterias(product,
     * existingProduct));
     * if (null != existingProduct.getProductNumbers() && !existingProduct.getProductNumbers().isEmpty()) {
     * List<ProductNumbers> productNumbers = productNumberCriteriaParser.getValidProductNumbers(
     * existingProduct.getProductNumbers(), criteriaSetValueIds, isSimplifiedPG);
     * product.setProductNumbers(productNumbers);
     * 
     * } else {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * } else {
     * product.setProductNumbers(productNumberCriteriaParser.generateProductNumberCriterias(product,
     * existingProduct));
     * if (existingProduct != null && existingProduct.getProductNumbers() != null
     * && !existingProduct.getProductNumbers().isEmpty()) {
     * List<ProductNumbers> productNumbers = productNumberCriteriaParser.getValidProductNumbers(
     * existingProduct.getProductNumbers(), criteriaSetValueIds, isSimplifiedPG);
     * product.setProductNumbers(productNumbers);
     * 
     * } else {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * // product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * } catch (Exception ex) {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * LOGGER.error("Exception occured in Product Number Criteria processing : " + ex.getMessage());
     * }
     * 
     * // Product Numbers Ends
     */
    public static String getValidProductNumberriteriaCode(String[] productNumberCriterias) {

        String temp[] = {};
        String productNumberCriteriaCode = "";
        for (String tempString : productNumberCriterias) {
            temp = tempString.split(ApplicationConstants.PRODUCT_RPBST_ELEMENT_SPLITTER_CODE);
            if (temp.length > 0) {
                if (!CommonUtilities.isValueNull(temp[0]) && CommonUtilities.isValidPriceCriteria(temp[0])) {
                    productNumberCriteriaCode = temp[0].trim().substring(0, temp[0].trim().indexOf(":"));
                    productNumberCriteriaCode += "#$"
                            + (temp[1].trim().indexOf(":") != -1 ? temp[1].trim().substring(0, temp[1].trim().indexOf(":"))
                                    : "null");
                    return productNumberCriteriaCode;
                }
            }
        }
        return null;
    }
    
    private void getProdNumMap(List<com.asi.ext.api.service.model.ProductNumber> pNumbers) {
        List<String> pnoList = new ArrayList<String>();
        int counter = 0;
        for(com.asi.ext.api.service.model.ProductNumber serPno : pNumbers) {
            
        }
    }

    @SuppressWarnings("unused")
    public ArrayList<ProductNumber> generateProductNumberCriterias(ProductDetail crntProduct,
            List<com.asi.ext.api.service.model.ProductNumber> serProductNumbers, ProductDetail existingProduct) {
        ArrayList<ProductNumber> finalProductNumbers = new ArrayList<>();
        HashMap<String, ProductNumber> crntProductNumberItems = null;
        String[] criteriaPN1list = null;/*crntProduct.getProductNumbers().get(0).getProductNumberConfigurations().get(0)
                .getCriteriaSetValueId().split(ApplicationConstants.PRICE_CRT_SPLITTER_BASECRITERIA)*/;
        String[] validCriteriaCodes = getValidCriteriaSets(criteriaPN1list);
        String firstCriteria = "";
        String secondCriteria = "";
        if (validCriteriaCodes != null && validCriteriaCodes.length > 1) {
            firstCriteria = validCriteriaCodes[0];
            secondCriteria = validCriteriaCodes[1];
            if (firstCriteria.equalsIgnoreCase(secondCriteria)) {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
        String[] productNumberValues = crntProduct.getProductNumbers().get(0).getValue()
                .split(ApplicationConstants.PRICE_CRT_SPLITTER_BASECRITERIA);
        String criteriaSetId1 = null;
        String criteriaSetId2 = null;
        if (null != existingProduct) {
            crntProductNumberItems = getExistingProductNumbersDetails(existingProduct);
        }
        if (null != criteriaPN1list && criteriaPN1list.length > 1) {
            for (int i = 0; i < criteriaPN1list.length; i++) {
                String[] criteriaPN1 = criteriaPN1list[i].split(ApplicationConstants.PRODUCT_RPBST_ELEMENT_SPLITTER_CODE);
                if (null != criteriaPN1 && criteriaPN1.length > 1
                        && !criteriaPN1[0].equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
                        && !criteriaPN1[0].contains(",") && !criteriaPN1[1].contains(",")) {
                    ProductNumber crntProductNumber = null;
                    List<ProductNumberConfiguration> productNumberConfigurationsAry = new ArrayList<>();
                    if (criteriaPN1[0].startsWith(firstCriteria)) {
                        criteriaSetId1 = getProductNumberIdByCriteria(criteriaPN1[0], crntProduct.getExternalProductId());
                    }
                    if (criteriaPN1[1].startsWith(secondCriteria)
                            && !criteriaPN1[1].equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
                        criteriaSetId2 = getProductNumberIdByCriteria(criteriaPN1[1], crntProduct.getExternalProductId());
                    }
                    if (null != existingProduct && null != crntProductNumberItems) {
                        if (null != criteriaSetId1 && null != criteriaSetId2) {
                            crntProductNumber = crntProductNumberItems.get(productNumberValues[i] + "," + criteriaSetId1 + ","
                                    + criteriaSetId2);
                        }
                        if (null != criteriaSetId1) {
                            crntProductNumber = crntProductNumberItems.get(productNumberValues[i] + "," + criteriaSetId1);
                        } else {
                            productDataStore.addErrorToBatchLogCollection(crntProduct.getExternalProductId().trim(),
                                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Product Number 1 Criteria "
                                            + criteriaPN1[0] + " is not valid");
                        }
                    }
                    if (null != crntProductNumber) {
                        finalProductNumbers.add(crntProductNumber);
                    } else {
                        if (null != criteriaSetId1) {/*
                            crntProductNumber = new ProductNumber();
                            ProductNumberConfiguration firstProductNumberConfigurations = new ProductNumberConfiguration();
                            ProductNumberConfiguration secondProductNumberConfigurations = new ProductNumberConfiguration();
                            //crntProductNumber.setId("-" + (i + 1));
                            crntProductNumber.setValue(productNumberValues[i]);
                            crntProductNumber.setProductId(crntProduct.getId());
                            firstProductNumberConfigurations.setCriteriaSetValueId(criteriaSetId1);
                            firstProductNumberConfigurations.setId("0");
                            firstProductNumberConfigurations.setProductNumberId(crntProductNumber.getId());
                            productNumberConfigurationsAry.add(firstProductNumberConfigurations);
                            if (null != criteriaSetId2) {
                                secondProductNumberConfigurations.setCriteriaSetValueId(criteriaSetId2);
                                secondProductNumberConfigurations.setId("0");
                                secondProductNumberConfigurations.setProductNumberId(crntProductNumber.getId());
                                productNumberConfigurationsAry.add(secondProductNumberConfigurations);
                            } else {
                                productDataStore.addErrorToBatchLogCollection(crntProduct.getExternalProductId().trim(),
                                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Product Number 2 Criteria "
                                                + criteriaPN1[1] + " is not valid");
                            }
                            crntProductNumber.setProductNumberConfigurations(productNumberConfigurationsAry);
                            finalProductNumbers.add(crntProductNumber);
                        */} else {
                            productDataStore.addErrorToBatchLogCollection(crntProduct.getExternalProductId().trim(),
                                    ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST, "Product Number 1 Criteria "
                                            + criteriaPN1[0] + " is not valid");
                        }

                    }
                } else if ((null != criteriaPN1 && criteriaPN1.length > 1 && !criteriaPN1[0]
                        .equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL))) {
                    if (criteriaPN1[0].contains(",")) {
                        productDataStore.addErrorToBatchLogCollection(crntProduct.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                "Invalid Product Number Criteria 1 - Should not be Multiple Values" + criteriaPN1[0]);
                        LOGGER.info("Product Number Criteria 1 Should not be Multiple Values " + criteriaPN1[0]);
                    } else if (criteriaPN1[1].contains(",")) {
                        productDataStore.addErrorToBatchLogCollection(crntProduct.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                "Invalid Product Number Criteria 2 - Should not be Multiple Values" + criteriaPN1[1]);
                        LOGGER.info("Product Number Criteria 2 Should not be Multiple Values " + criteriaPN1[1]);
                    }
                }
                criteriaSetId1 = null;
                criteriaSetId2 = null;
            }
        }
        return finalProductNumbers;
    }

    private static HashMap<String, ProductNumber> getExistingProductNumbersDetails(ProductDetail existingProduct) {
        HashMap<String, ProductNumber> existingProductNumbers = new HashMap<>();
        ArrayList<ProductNumber> existingProductNumbersList = (ArrayList<ProductNumber>) existingProduct.getProductNumbers();
        String productNumberKey1 = "";
        String productNumberKey2 = "";
        for (ProductNumber crntProductNumbers : existingProductNumbersList) {
            productNumberKey1 = crntProductNumbers.getValue();
            productNumberKey2 = crntProductNumbers.getValue();
            if (crntProductNumbers.getProductNumberConfigurations().size() > 1) {
                productNumberKey1 += "," + crntProductNumbers.getProductNumberConfigurations().get(0).getCriteriaSetValueId();
                productNumberKey1 += "," + crntProductNumbers.getProductNumberConfigurations().get(1).getCriteriaSetValueId();
                productNumberKey2 += "," + crntProductNumbers.getProductNumberConfigurations().get(1).getCriteriaSetValueId();
                productNumberKey2 += "," + crntProductNumbers.getProductNumberConfigurations().get(0).getCriteriaSetValueId();
                existingProductNumbers.put(productNumberKey1, crntProductNumbers);
                existingProductNumbers.put(productNumberKey2, crntProductNumbers);
            } else if (crntProductNumbers.getProductNumberConfigurations().size() > 0) {
                productNumberKey1 += "," + crntProductNumbers.getProductNumberConfigurations().get(0).getCriteriaSetValueId();
                existingProductNumbers.put(productNumberKey1, crntProductNumbers);
            }
        }
        return existingProductNumbers;
    }

    private static String[] getValidCriteriaSets(String[] criteriaPN1list) {
        String[] criteriaAry = new String[] { "null", "null" };
        if (null == criteriaPN1list) return null;
        for (String crntProductNumberCriteriaSet : criteriaPN1list) {
            if (null != crntProductNumberCriteriaSet) {
                String[] individualProductCritieriaSets = crntProductNumberCriteriaSet
                        .split(ApplicationConstants.PRODUCT_RPBST_ELEMENT_SPLITTER_CODE);
                if (individualProductCritieriaSets != null && individualProductCritieriaSets.length > 1) {
                    if (null != individualProductCritieriaSets[0] && individualProductCritieriaSets[0].contains(":")) {
                        criteriaAry[0] = individualProductCritieriaSets[0].substring(0,
                                individualProductCritieriaSets[0].indexOf(":"));
                        if (null != individualProductCritieriaSets[1] && individualProductCritieriaSets[1].contains(":")) {
                            criteriaAry[1] = individualProductCritieriaSets[1].substring(0,
                                    individualProductCritieriaSets[1].indexOf(":"));
                        } else {
                            criteriaAry[1] = "null";
                        }
                    }
                    return criteriaAry;
                }
            }

        }
        return null;
    }

    private String getProductNumberIdByCriteria(String criteriaSet, String externalId) {
        String[] criteriaElements = criteriaSet.split(":");
        String criteriaSetValueId = "";
        if (null != criteriaElements && criteriaElements.length > 1) {
            criteriaSetValueId = productDataStore.findCriteriaSetValueIdForValue(externalId, criteriaElements[0],
                    criteriaElements[1]);
        }
        return criteriaSetValueId;
    }

    protected List<ProductNumber> getValidProductNumbers(List<ProductNumber> existingProductNumbers,
            List<String> criteriaSetValueIds) {
        // Check existing ProductNumber configuration exists or not, if not return the current copy
        if (existingProductNumbers != null && !existingProductNumbers.isEmpty()) {

            if (true) { // if its a simplified template then validate and return the existing PNRConfiguration
                existingProductNumbers = validateProductNumberConfiguration(existingProductNumbers, criteriaSetValueIds);
                return existingProductNumbers;
            }

        } else {
            return existingProductNumbers;
        }

        return null;
    }

    private List<ProductNumber> validateProductNumberConfiguration(List<ProductNumber> productNumbers,
            List<String> criteriaSetValueIds) {
        // If any of the participating criteria set value not existing in the current set,
        // then we must ignore that particular ProductNumberConfiguration.
        List<ProductNumber> processedProductNumbers = new ArrayList<ProductNumber>();

        for (ProductNumber pNumber : productNumbers) {
            if (pNumber != null && pNumber.getProductNumberConfigurations() != null
                    && !pNumber.getProductNumberConfigurations().isEmpty()) {
                boolean isValid = true;
                for (ProductNumberConfiguration pnrConfig : pNumber.getProductNumberConfigurations()) {
                    if (!criteriaSetValueIds.contains(pnrConfig.getCriteriaSetValueId())) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    processedProductNumbers.add(pNumber);
                }
            }
        }

        return processedProductNumbers;

    }

    /**
     * Checks that the CriteriaSetValueId existing in the Criteria Collection.
     * 
     * @param configurations
     * @return true if its valid
     */
    /*
     * private boolean isValidPNRConfiguration(ProductNumberConfigurations configurations) {
     * if (configurations != null && !CommonUtilities.isValueNull(configurations.getCriteriaSetValueId())) {
     * // TODO : Check with DataStore
     * } else {
     * return false;
     * }
     * 
     * return false;
     * }
     */
}
