package com.asi.ext.api.product.transformers;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.asi.ext.api.exception.VelocityException;
import com.asi.ext.api.exception.VelocityException.ExceptionType;
import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.RestAPIProperties;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * JerseyClientPost class contains methods which is used connect Velocity REST API using HTTP POST.
 * JerseyClientPost class also contain methods like getLookupsResponse(String), checkCurrentOrgin(String), etc..
 * 
 * @author Shravan Kumar, Murali Ede, Rahul K
 * @version 1.5
 * 
 */
public class JerseyClientPost {
    private final static Logger LOGGER = Logger.getLogger(JerseyClientPost.class.getName());

    public static void main(String[] args) {

        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://local-espupdates.asicentral.com/api/api/lookup/criteria_attributes");
            String response = webResource.get(String.class);
            // LOGGER.info("Output from Server .... \n");
            // LOGGER.info(response);
            JsonProcessor jsonProcessorObj = new JsonProcessor();
            // LOGGER.info("Final Criteria Code:"+jsonProcessorObj.checkValueKeyPair(response,"U.S.A."));
            // JerseyClientPost jcPost=new JerseyClientPost();
            // LOGGER.info(jsonProcessorObj.getSizesResponse(response,"Length"));
            LOGGER.info("Sizes Check"
                    + jsonProcessorObj.getSizesElementValue(
                            "CRITERIASETID",
                            jsonProcessorObj.getSizesResponse(response, "Capacity", ApplicationConstants.CONST_SIZE_GROUP_CAPACITY),
                            "\"")); // 200137759
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the origin (country ) of the product using originValue.
     * 
     * @param orginValue
     *            is the value of origin
     * @return ID of the origin
     */
    public String checkCurrentOrigin(String orginValue) {
        String criteriaCode = null;
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(RestAPIProperties.get(ApplicationConstants.ORIGIN_LOOKUP_URL));
            String response = webResource.get(String.class);
            JsonProcessor jsonProcessorObj = new JsonProcessor();
            LinkedList<?> orginsList = jsonProcessorObj.getAllOrigins(response);
            Iterator<?> iter = orginsList.iterator();// entrySet().iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
                if (orginValue.equals(crntValue.get("Value"))) criteriaCode = crntValue.get("Key");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return criteriaCode;
    }

    /**
     * Gets the key value pair of a orgin value
     * 
     * @param originValue
     *            is the origin to search
     * @return origin value
     */
    public String getKeyValue(String originValue) {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(RestAPIProperties.get(ApplicationConstants.ORIGIN_LOOKUP_URL));
            String response = webResource.get(String.class);
            JsonProcessor jsonProcessorObj = new JsonProcessor();
            originValue = jsonProcessorObj
                    .checkValueKeyPair(response, originValue, ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return originValue;
    }

    /**
     * Gets Color criteria details of a give color value,
     * First will send a HTTP Request to lookup API of Colors then from the response we find the actual color criteria details
     * 
     * @param colorValue
     *            is the color we need to find
     * @return Color criteria details
     */
    public String getColorKeyValue(String colorValue) {
        try {
            colorValue = colorValue.trim();
            LOGGER.info("In Jersy Client" + colorValue);
            Client client = Client.create();
            WebResource webResource = client.resource(RestAPIProperties.get(ApplicationConstants.COLORS_LOOKUP_URL));
            String response = webResource.get(String.class);
            // LOGGER.info("Output from Server .... \n");
            // LOGGER.info(response);
            JsonProcessor jsonProcessorObj = new JsonProcessor();
            colorValue = jsonProcessorObj.checkColorValueKeyPair(response, colorValue);
            // LOGGER.info("Final Criteria Code:"+jsonProcessorObj.checkValueKeyPair(response,originValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colorValue;
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
    public String getLookupsResponse(String url) throws VelocityException {
        String response = ApplicationConstants.CONST_STRING_NULL_CAP;
        try {
            Client client = Client.create();
            LOGGER.info("WS Call :" + url);
            WebResource webResource = client.resource(url);
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

    /**
     * Send HTTP POST request to a given URL and the data set to the body part of HTTP POST request
     * 
     * @param URL
     *            is the resource / location
     * @param data
     *            is the entity to send
     * @return response from the resource in {@linkplain String}
     * @throws VelocityException
     */
    public String doPostRequest(String URL, String data) throws VelocityException {
        if (URL != null && !URL.isEmpty()) {
            String response = sendRequest(URL, data, MediaType.APPLICATION_JSON);

            if (response == null || response.isEmpty()) {
                LOGGER.info("POST method returned NULL or Empty data");
            }
            LOGGER.info("Response recived : " + response);
            return response;

        } else {
            LOGGER.info("URL Cannot be null URL : " + URL);
            return null;
        }
    }

    /**
     * Send HTTP POST request to a given URL and the data set to the body part of HTTP POST request and set content-type as well
     * 
     * @param resource
     *            is the resource / location
     * @param data
     *            is the entity to send
     * @param contentType
     *            is the mediaType of the request
     * @return response from the resource in {@linkplain String}
     * @throws VelocityException
     */
    private final String sendRequest(String resource, String data, String contentType) throws VelocityException {

        try {
            Client client = Client.create();

            WebResource webResource = client.resource(resource);
            ClientResponse clientResponse = webResource.type(contentType).entity(data).post(ClientResponse.class);

            if (clientResponse.getStatus() == 500) {
                LOGGER.info(resource + " is not reachable");
                throw new VelocityException(resource + " is not reachable", ExceptionType.INTERNAL_SERVER_ERROR, null);
            } else if (clientResponse.getStatus() == 404) {
                LOGGER.info("Invalid URL / requested resource not found on the server. Status Code : " + clientResponse.getStatus());
            }

            String response = clientResponse.getEntity(String.class);

            return response;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid URL supplied : " + resource + "\nException : " + e.getMessage());
            throw new VelocityException("Invalid URL supplied : " + resource + "\nException : " + e.getMessage(),
                    ExceptionType.INVALID_URL, e);
        } catch (Exception e) {
            LOGGER.error("Unhandled Exception occured while processing the request, Reason/Cause : " + e.getMessage());
            throw new VelocityException("Unhandled Exception occured while processing the request, Reason/Cause : "
                    + e.getMessage(), e);
        }
    }
}
