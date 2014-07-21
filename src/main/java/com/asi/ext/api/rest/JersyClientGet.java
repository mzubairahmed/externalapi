package com.asi.ext.api.rest;

// import java.net.URLEncoder;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.PriceGrids;
import com.asi.ext.api.radar.model.Prices;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.exception.VelocityException.ExceptionType;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.RestAPIProperties;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * JerseyClientGet class contains functionalities to connect a webservice using HTTP GET method.
 * Also includes functionalities like Conversion of JSON string to Java Object, Comparing exiting criteria values, exiting PriceList
 * etc.
 * 
 * @author Shravan, Murali Ede, Rahul
 * @version 1.5
 * 
 */
public class JersyClientGet {
    /** Logger object for debugging */
    private final static Logger     LOGGER           = Logger.getLogger(JersyClientGet.class.getName());
    private static JSONParser       parser           = null;
    private static ContainerFactory containerFactory = null;

    /**
     * Functionality of this method to check whether the given product is already exits in the database.
     * If the Product exists in database then the Product will be updated, otherwise it a new Product created in database
     * 
     * @param productId
     *            is the ID of the product for checking existence
     * @param companyId
     *            is the ID of the company were the product belongs
     * @return Existing product if a match found otherwise return null
     * @throws VelocityException
     */
    public Product getExistingProductDetails(String productId, String companyId) throws VelocityException, Exception {
        Product product = null;
        String getUrl = "";
        try {
            Client client = Client.create();
            client.setConnectTimeout(ApplicationConstants.PRODUCT_CONNECTION_TIMEOUT);
            client.setReadTimeout(ApplicationConstants.PRODUCT_SOCKET_READ_TIMEOUT);

            getUrl = RestAPIProperties.get(ApplicationConstants.REST_API_IMPORT) + "ExternalProductId="
                    + CommonUtilities.getURLEncodedValue(productId) + "&companyId=" + CommonUtilities.getURLEncodedValue(companyId);
            // getUrl = URLEncoder.encode(getUrl, "UTF-8"); // Implement this logic to encode a url.
            WebResource webResource = client.resource(getUrl);

            LOGGER.info("Checking the Product Existance :" + getUrl);
            ClientResponse response = webResource.accept("application/com.asi.util.json").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                LOGGER.info("Resp Code:" + response.getStatus());
                if (response.getStatus() == 404)
                    return product;
                else if (response.getStatus() == 500) {
                    LOGGER.error(response.getEntity(String.class));
                    throw new VelocityException("Internal Server Error encounterd while processing request",
                            ExceptionType.INTERNAL_SERVER_ERROR, null);
                } else
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            String output = response.getEntity(String.class);
            LOGGER.info(output);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(new MyNameStrategy());
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

            product = mapper.readValue(output, new TypeReference<Product>() {
            });
            LOGGER.info("Product Model JSON  : " + mapper.writeValueAsString(product));
        } catch (VelocityException ve) {
            throw new VelocityException(ve.getMessage(), ExceptionType.INTERNAL_SERVER_ERROR, null);
        } catch (ClientHandlerException e) {
            LOGGER.error("Exception occured when checking External Product Id : " + e.getMessage(), e);
            String message = String.format(
                    "Failed to get existing product ( ExternalProductId =%s , companyId=%s ) info within %s ms", productId,
                    companyId, ApplicationConstants.PRODUCT_SOCKET_READ_TIMEOUT);
            new ProductDataStore().addErrorToBatchLogCollection(productId, ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
                    message);
            throw new Exception(message);
        } catch (SocketTimeoutException e) {
            LOGGER.error("Exception occured when checking External Product Id : " + e.getMessage(), e);
            String message = String.format(
                    "Failed to get existing product ( ExternalProductId =%s , companyId=%s ) info within %s ms", productId,
                    companyId, ApplicationConstants.PRODUCT_SOCKET_READ_TIMEOUT);
            new ProductDataStore().addErrorToBatchLogCollection(productId, ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
                    message);
            throw new Exception(message);
        } catch (Exception e) {
            LOGGER.error("Exception occured when checking External Product Id : " + e.getMessage(), e);
            return null;
        }
        return product;
    }

    /**
     * Converts JSON string {@linkplain com.mule.velocity.bean.Value} object
     * 
     * @param valueData
     *            is the JSON string to convert
     * @return {@linkplain com.mule.velocity.bean.Value} or null
     */
    public Value getValueObject(String valueData) {
        Value valueObj = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            valueObj = mapper.readValue(valueData, new TypeReference<Value>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueObj;
    }

    /**
     * Finds all the setCodeValuesId from the given Product and return back as a {@link HashMap} of
     * CriteriaSetValues, where key is CriteriaCode and value is the array of {@link CriteriaSetValues}
     * 
     * @param product
     *            is the product contains all the criteria set
     * @return {@link HashMap} of CriteriaSetValues, where key is CriteriaCode and
     *         value is the array of {@link CriteriaSetValues}
     */
    public HashMap<String, CriteriaSetValues[]> getAllSetCodeValueIds(Product product) {
        HashMap<String, CriteriaSetValues[]> finalCriteraHashMap = new HashMap<String, CriteriaSetValues[]>();
        if (null != product) {
            CriteriaSetValues setCodeValuesAry[] = null;
            ProductCriteriaSets productCriteriaSets = null;
            ProductCriteriaSets[] productCriteriaSetsAry = {};
            if (product.getProductConfigurations().length > 0)
                productCriteriaSetsAry = product.getProductConfigurations()[0].getProductCriteriaSets();
            CriteriaSetValues[] criteriaSetValuesAry = null;
            CriteriaSetValues criteriaSetValues = null;
            // - CriteriaSetCodeValues criteriaSetCodeValue=null;

            for (int productCriteriaSetsCntr = 0; productCriteriaSetsCntr < productCriteriaSetsAry.length; productCriteriaSetsCntr++) {
                productCriteriaSets = productCriteriaSetsAry[productCriteriaSetsCntr];
                // System.out.println(productCriteriaSets.getCriteriaCode());
                criteriaSetValuesAry = productCriteriaSets.getCriteriaSetValues();
                setCodeValuesAry = new CriteriaSetValues[criteriaSetValuesAry.length];
                for (int criteriaSetCntr = 0; criteriaSetCntr < criteriaSetValuesAry.length; criteriaSetCntr++) {
                    criteriaSetValues = criteriaSetValuesAry[criteriaSetCntr];
                    if (criteriaSetValues.getCriteriaSetCodeValues().length > 0)
                    // - criteriaSetCodeValue=criteriaSetValues.getCriteriaSetCodeValues()[0];
                    // setCodeValuesAry[criteriaSetCntr]=criteriaSetCodeValue.getSetCodeValueId();
                        setCodeValuesAry[criteriaSetCntr] = criteriaSetValues;
                    // -LOGGER.info(":"+(null!=criteriaSetCodeValue?criteriaSetCodeValue.getSetCodeValueId():" criteriaSetCodeValue is Null"));
                }
                finalCriteraHashMap.put(productCriteriaSets.getCriteriaCode(), setCodeValuesAry);
            }
        }
        return finalCriteraHashMap;
    }

    /**
     * Get all productCriteriaSets of a specific criteria code in a Product
     * 
     * @param product
     *            is the Product which contains te criteriaSets
     * @param criteriaCode
     *            is the code of criteria which we need to find
     * @return Array of productCriteriaSet
     */
    public ProductCriteriaSets[] getAllProductCriteriasets(Product product, String criteriaCode) {
        ProductCriteriaSets[] finalProductCriteriaSets = {};
        ProductCriteriaSets[] listProductCriteriaSets = product.getProductConfigurations()[0].getProductCriteriaSets();
        ProductCriteriaSets crntProductCriteriaSets = null;
        int cntr = 0;
        finalProductCriteriaSets = new ProductCriteriaSets[listProductCriteriaSets.length];
        for (int productCriteriaSetsCntr = 0; productCriteriaSetsCntr < listProductCriteriaSets.length; productCriteriaSetsCntr++) {
            crntProductCriteriaSets = listProductCriteriaSets[productCriteriaSetsCntr];
            if (crntProductCriteriaSets.getCriteriaCode().equalsIgnoreCase(criteriaCode)) {
                finalProductCriteriaSets[cntr] = new ProductCriteriaSets();
                finalProductCriteriaSets[cntr] = crntProductCriteriaSets;
                cntr++;
            } else
                finalProductCriteriaSets = (ProductCriteriaSets[]) Arrays.copyOf(finalProductCriteriaSets,
                        finalProductCriteriaSets.length - 1);
        }
        return finalProductCriteriaSets;
    }

    /**
     * Checks whether the criteria code is already exists in the existing product
     * 
     * @param existingCriteriaSetCodeValues
     *            is the existingCriteria Set code values
     * @param curntCriteria
     *            is the criteria to check
     * @param existingProduct
     *            is the existing product
     * @return {@linkplain CriteriaSetValues} if a match found otherwise <code>null</code> will be returned.
     */
    public CriteriaSetValues IsExistingCriteriaCode(CriteriaSetValues[] existingCriteriaSetCodeValues, String curntCriteria,
            Product existingProduct) {
        // boolean blnFlag=false;
        CriteriaSetValues criteriaSetValue = null;
        CriteriaSetValues crntCriteriaSet = null;
        CriteriaSetCodeValues criteriaSetCodeValue = null;
        // CriteriaSetCodeValues[] criteriaSetCodeValues=null;
        for (int i = 0; null != existingCriteriaSetCodeValues && i < existingCriteriaSetCodeValues.length; i++) {
            crntCriteriaSet = existingCriteriaSetCodeValues[i];
            if (null != crntCriteriaSet && crntCriteriaSet.getCriteriaSetCodeValues().length > 0) {
                criteriaSetCodeValue = crntCriteriaSet.getCriteriaSetCodeValues()[0];
                // criteriaSetCodeValue.setId("0");
                // criteriaSetCodeValues=new CriteriaSetCodeValues[0];
                // criteriaSetCodeValues[0]=criteriaSetCodeValue;
                if (curntCriteria.equalsIgnoreCase(criteriaSetCodeValue.getSetCodeValueId())
                        && !(crntCriteriaSet.getValue() instanceof ArrayList || crntCriteriaSet.getValue() instanceof Value[])) {
                    // blnFlag=true;
                    criteriaSetValue = crntCriteriaSet;
                }
            }
        }

        return criteriaSetValue;
    }

    /**
     * Gets existing price list from existing product in {@linkplain String} format with a special char for splitting
     * 
     * @param existingProduct
     *            is the existing product which contains a price list
     * @return {@linkplain String}[] of existing price list or null
     */
    public String[] getExistingPricesList(Product existingProduct) {

        String[] pricesList = null;

        if (null != existingProduct && null != existingProduct.getPriceGrids() && existingProduct.getPriceGrids().length > 0) {
            PriceGrids priceGrids = existingProduct.getPriceGrids()[0];
            Prices[] pricesAry = priceGrids.getPrices();
            Prices currentPrice = new Prices();
            Double price;
            pricesList = new String[pricesAry.length];
            for (int i = 0; i < pricesAry.length; i++) {
                currentPrice = pricesAry[i];
                price = (double) Math.round(Double.parseDouble(currentPrice.getListPrice()));
                pricesList[i] = price.intValue() + "$" + currentPrice.getQuantity() + "$"
                        + currentPrice.getDiscountRate().getIndustryDiscountCode();
            }
        }
        return pricesList;
    }

    /**
     * Check the given price is already exits in the price list
     * 
     * @param listOfExistingPrices
     *            is the existing price list
     * @param checkCurrentWithExistingPrices
     * @param sourceProduct
     *            is the product
     * @return Existing {@linkplain Prices} if exists otherwise <code>null</code>
     */
    public Prices IsExistingPrice(String[] listOfExistingPrices, String checkCurrentWithExistingPrices, Product sourceProduct) {
        // boolean existanceFlag=false;
        Prices pricesSet = null;
        String currentPriceSet = "";
        String currentPrice = "";
        String currentQuantity = "";
        String currentDiscountCode = "";
        String existingPrice = "", existingQuantity = "", existingDiscoutCode = "";
        String[] priceElement = checkCurrentWithExistingPrices.split("\\$");
        for (int i = 0; i < priceElement.length; i++) {
            if (i == 0)
                currentPrice = priceElement[i];
            else if (i == 1)
                currentQuantity = priceElement[i];
            else if (i == 2) currentDiscountCode = priceElement[i];
        }
        if (!currentPrice.equals("") && !currentQuantity.equals("") && !currentDiscountCode.equals("")) {
            Prices[] pricesAry = null;

            for (int cntr = 0; cntr < listOfExistingPrices.length; cntr++) {
                currentPriceSet = listOfExistingPrices[cntr];
                if (null != checkCurrentWithExistingPrices && checkCurrentWithExistingPrices.equalsIgnoreCase(currentPriceSet)) {
                    // existanceFlag=true;
                    pricesAry = sourceProduct.getPriceGrids()[0].getPrices();
                    for (int priceCntr = 0; priceCntr < pricesAry.length; priceCntr++) {
                        existingPrice = pricesAry[priceCntr].getListPrice();
                        existingPrice = existingPrice.substring(0, existingPrice.indexOf("."));
                        existingQuantity = pricesAry[priceCntr].getQuantity();
                        existingDiscoutCode = pricesAry[priceCntr].getDiscountRate().getCode().substring(0, 1);
                        if (currentPrice.equals(existingPrice) && currentQuantity.equals(existingQuantity)
                                && currentDiscountCode.equalsIgnoreCase(existingDiscoutCode)) {
                            pricesSet = sourceProduct.getPriceGrids()[0].getPrices()[priceCntr];
                            return pricesSet;
                        }
                    }

                }
            }
        }
        return pricesSet;
    }

    /**
     * Set HTTP request to the given URI and returns backs the response in {@linkplain String} format
     * 
     * @param url
     *            is the resource to send request
     * @return response in {@linkplain String}
     * @throws VelocityException
     * 
     */
    public static String getLookupsResponse(String url) throws VelocityException {
        String response = ApplicationConstants.CONST_STRING_NULL_CAP;
        try {
            Client client = Client.create();
            LOGGER.info("WS Call :" + url);
            WebResource webResource = client.resource(url);// "http://stage-espupdates.asicentral.com/api/api/lookup/origins"
            ClientResponse clientResponse = webResource.accept("application/com.asi.util.json").get(ClientResponse.class);
            if (clientResponse.getStatus() == 500) {
                LOGGER.info(url + " is not reachable");
                throw new VelocityException(url + " is not reachable", ExceptionType.INTERNAL_SERVER_ERROR, null);
            } else if (clientResponse.getStatus() == 404) {
                LOGGER.info("Invalid URL / requested resource not found on the server. Status Code : " + clientResponse.getStatus());
            } else
                response = webResource.get(String.class);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid URL supplied : " + url + "\nException : " + e.getMessage());
            throw new VelocityException("Invalid URL supplied : " + url + "\nException : " + e.getMessage(),
                    ExceptionType.INVALID_URL, e);
        } catch (Exception e) {
            LOGGER.error("Unhandled Exception occured while processing the request, Reason/Cause : " + e.getMessage());
            throw new VelocityException("Unhandled Exception occured while processing the request, Reason/Cause = "
                    + e.getMessage(), e);
        }
        return response;
    }

    public static LinkedList<?> parseToList(String jsonText) {

        LinkedList<?> json = null;
        try {
            if (parser == null) {
                parser = new JSONParser();
            }
            if (containerFactory == null) {
                createContainerFatory();
            }
            json = (LinkedList<?>) parser.parse(jsonText, containerFactory);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e.getCause());
        }

        return json;

    }

    public static void createContainerFatory() {

        containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };

    }
}
