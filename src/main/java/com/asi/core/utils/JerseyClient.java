package com.asi.core.utils;



import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.asi.service.product.client.vo.Batch;
import com.asi.service.product.client.vo.BatchDataSource;
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
        ClientResponse response = webResource.accept("application/com.asi.util.json").get(ClientResponse.class);
        if (response.getStatus() == 200) {
            restResponse = response.getEntity(String.class);
        }
        return restResponse;
    }
    public static String getDataSourceByBatchId(String batchId) throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
    	URI uri=new URI("http://stage-espupdates.asinetwork.local/api/api/batch/"+batchId);
        String restResponse = null;
        Client client = Client.create();
        client.setConnectTimeout(5000);
        WebResource webResource = client.resource(uri);
        String responseString = webResource.accept("application/com.asi.util.json").get(String.class);
        if (null!=responseString && !responseString.isEmpty()) {
        	ObjectMapper objectMapper = new ObjectMapper();
        	objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            Batch batchObj=objectMapper.readValue(responseString, new TypeReference<Batch>() {});
        	BatchDataSource batchDataSource=batchObj.getBatchDataSources().get(0);
            restResponse = String.valueOf(batchDataSource.getId());
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

            if (response != null && (response.getStatus() == 200 ||response.getStatus() == 201)) { // OK
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
    
    public static String sendBatchRequst(URI url, AsiHttpMethod method, String body) {

        try {
            Client client = Client.create();
            client.setConnectTimeout(5000);
            WebResource resource = client.resource(url);
            String responseStr="";
            JSONObject batchResponse=null;
            if (method.equals(AsiHttpMethod.POST)) {
            	responseStr = resource.type(MediaType.APPLICATION_JSON).post(String.class, body);
            	batchResponse = new JSONObject(responseStr);
            	responseStr=batchResponse.get("BatchId").toString();
            } else if (method.equals(AsiHttpMethod.PUT)) {
            	responseStr = resource.type(MediaType.APPLICATION_JSON).put(String.class, body);
                batchResponse = new JSONObject(responseStr);
                responseStr=batchResponse.get("BatchId").toString();
            }

            if (responseStr != null) { // OK
                _LOGGER.info(responseStr);
               // restResponse = response.getEntity(String.class);                
                return responseStr;
            }  else {
                _LOGGER.debug("Server failed to process request, returned null data");
                return "0";
            }

        } catch (IllegalArgumentException iae) {
            _LOGGER.error("IllegalArugumentException while processing request, URL : " + url, iae);
        } catch (Exception e) {
            _LOGGER.error("Unhandled Exeception while trying to send PUT request to URL : " + url, e);
        }
        return "0";
    }

}