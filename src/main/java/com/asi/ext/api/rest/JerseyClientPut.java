package com.asi.ext.api.rest;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Class which provides the services for sending a HTTP PUT request 
 *
 *@author Rahul, Shravan, Murali
 *@version 1.5
 *@category Connectivity
 */
public class JerseyClientPut {

    private final static Logger LOGGER     = Logger.getLogger(JerseyClientPut.class);

    private final Client        restClient = Client.create();

    /**
     *  This is the function which actually send HTTP PUT request to the server 
     * @param URL is the location / service (API) to connect
     * @param requestData is the data which we need to send to the API
     * @param contentType is the MediaType
     * @return Response data in {@linkplain java.lang.String} format
     */
    private String sendRequest(final String URL, String requestData, String contentType) {

        try {
            WebResource resource = restClient.resource(URL);

            String response = resource.type(MediaType.APPLICATION_JSON).put(String.class, requestData);

            if (response != null) {
                LOGGER.info("Recived Response from server , response : " + response);
            } else {
                LOGGER.debug("Server returned empty resopnse , response : " + response);
            }
            return response;

        } catch (IllegalArgumentException iae) {
            LOGGER.error("IllegalArugumentException while processing request, URL : " + URL);
        } catch (Exception e) {
            LOGGER.error("Unhandled Exeception while trying to send PUT request to URL : " + URL);
        }
        return null;
    }

    /**
     * Used to send HTTP PUT request
     * @param URL is the address of API/Service
     * @param data is the content we need to send
     * @return response of Http request in {@linkplain java.lang.String} format
     */
    public String doPutRequest(String URL, String data) {

        if (URL != null || !data.isEmpty()) {
            String response = sendRequest(URL, data, MediaType.APPLICATION_JSON);

            if (response != null) {
                // Successfully completed HTTP PUT request, returning data
                return response;
            } else {
                // Something went wrong while processing PUT request, returning NULL
            }
        } else {
            // handle this in VelocityException
            LOGGER.error("Request cannot be completed beacuse URL or data cannot be null");
        }
        return null;
    }

}
