package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductNumberConfigurations;
import com.asi.ext.api.radar.model.ProductNumbers;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

public class ProductNumberCriteriaParser {
    private final static Logger _LOGGER          = Logger.getLogger(ProductNumberCriteriaParser.class.getName());
    public ProductDataStore     productDataStore = new ProductDataStore();

    public static String getValidProductNumberriteriaCode(String[] productNumberCriterias) {
        /*
         * ArrayList<String> productNumberCriteria1List=new ArrayList();
         * ArrayList<String> productNumberCriteria2List=new ArrayList();
         */
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

    public ArrayList<ProductNumbers> generateProductNumberCriterias(Product crntProduct, Product existingProduct) {
        ArrayList<ProductNumbers> finalProductNumbers = new ArrayList<>();
        HashMap<String, ProductNumbers> crntProductNumberItems = null;
        String[] criteriaPN1list = crntProduct.getProductNumbers().get(0).getProductNumberConfigurations().get(0)
                .getCriteriaSetValueId().split(ApplicationConstants.PRICE_CRT_SPLITTER_BASECRITERIA);
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
                    ProductNumbers crntProductNumber = null;
                    List<ProductNumberConfigurations> productNumberConfigurationsAry = new ArrayList<>();
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
                        if (null != criteriaSetId1) {
                            crntProductNumber = new ProductNumbers();
                            ProductNumberConfigurations firstProductNumberConfigurations = new ProductNumberConfigurations();
                            ProductNumberConfigurations secondProductNumberConfigurations = new ProductNumberConfigurations();
                            crntProductNumber.setId("-" + (i + 1));
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
                        } else {
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
                        _LOGGER.info("Product Number Criteria 1 Should not be Multiple Values " + criteriaPN1[0]);
                    } else if (criteriaPN1[1].contains(",")) {
                        productDataStore.addErrorToBatchLogCollection(crntProduct.getExternalProductId().trim(),
                                ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                "Invalid Product Number Criteria 2 - Should not be Multiple Values" + criteriaPN1[1]);
                        _LOGGER.info("Product Number Criteria 2 Should not be Multiple Values " + criteriaPN1[1]);
                    }
                }
                criteriaSetId1 = null;
                criteriaSetId2 = null;
            }
        }
        return finalProductNumbers;
    }

    private static HashMap<String, ProductNumbers> getExistingProductNumbersDetails(Product existingProduct) {
        HashMap<String, ProductNumbers> existingProductNumbers = new HashMap<>();
        ArrayList<ProductNumbers> existingProductNumbersList = (ArrayList<ProductNumbers>) existingProduct.getProductNumbers();
        String productNumberKey1 = "";
        String productNumberKey2 = "";
        for (ProductNumbers crntProductNumbers : existingProductNumbersList) {
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

    protected List<ProductNumbers> getValidProductNumbers(List<ProductNumbers> existingProductNumbers,
            List<String> criteriaSetValueIds, boolean isSimplifiedTemplate) {
        // Check existing ProductNumber configuration exists or not, if not return the current copy
        if (existingProductNumbers != null && !existingProductNumbers.isEmpty()) {

            if (isSimplifiedTemplate) { // if its a simplified template then validate and return the existing PNRConfiguration
                existingProductNumbers = validateProductNumberConfiguration(existingProductNumbers, criteriaSetValueIds);
                return existingProductNumbers;
            }

        } else {
            return existingProductNumbers;
        }

        return null;
    }

    private List<ProductNumbers> validateProductNumberConfiguration(List<ProductNumbers> productNumbers,
            List<String> criteriaSetValueIds) {
        // If any of the participating criteria set value not existing in the current set,
        // then we must ignore that particular ProductNumberConfiguration.
        List<ProductNumbers> processedProductNumbers = new ArrayList<ProductNumbers>();

        for (ProductNumbers pNumber : productNumbers) {
            if (pNumber != null && pNumber.getProductNumberConfigurations() != null
                    && !pNumber.getProductNumberConfigurations().isEmpty()) {
                boolean isValid = true;
                for (ProductNumberConfigurations pnrConfig : pNumber.getProductNumberConfigurations()) {
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
    private boolean isValidPNRConfiguration(ProductNumberConfigurations configurations) {
        if (configurations != null && !CommonUtilities.isValueNull(configurations.getCriteriaSetValueId())) {
            // TODO : Check with DataStore
        } else {
            return false;
        }

        return false;
    }*/
}
