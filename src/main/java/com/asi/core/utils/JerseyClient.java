package com.asi.core.utils;



import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public final class JerseyClient {

    private final static Logger _LOGGER = Logger.getLogger(JerseyClient.class.getName());

    public enum AsiHttpMethod {
        GET, POST, PUT, DELETE, PATCH
    }

    public static String invoke(URI uri) {
        String restResponse = null;
        Client client = Client.create();
        client.setConnectTimeout(5000);
        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
        if (response.getStatus() == 200) {
            restResponse = response.getEntity(String.class);
        }
        return restResponse;
    }

    public static boolean sendRequst(URI url, AsiHttpMethod method, String body) {

        try {
            Client client = Client.create();
            client.setConnectTimeout(5000);
            WebResource resource = client.resource(url);

            ClientResponse response = null;
            if (method.equals(AsiHttpMethod.POST)) {
                response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, body);
            } else if (method.equals(AsiHttpMethod.PUT)) {
                response = resource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, body);
            }

            if (response != null && response.getStatus() == 200) { // OK
                _LOGGER.info(response);
                return true;
            } else if(response != null) {
                _LOGGER.debug("Server failed to process request, status : "+ response.getStatus() + " , Message : "+response);
                return false;
            } else {
                _LOGGER.debug("Server failed to process request, returned null data");
                return false;
            }

        } catch (IllegalArgumentException iae) {
            _LOGGER.error("IllegalArugumentException while processing request, URL : " + url, iae);
        } catch (Exception e) {
            _LOGGER.error("Unhandled Exeception while trying to send PUT request to URL : " + url, e);
        }
        return false;
    }

}