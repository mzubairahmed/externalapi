package com.asi.service.product.client.vo.parser;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.integration.lookup.parser.UpchargePricesParser;
import com.asi.ext.api.util.RestAPIProperties;



public class UpChargeLookup {
    private final static Logger                      _LOGGER       = Logger.getLogger(UpChargeLookup.class.getName());
    public static RestTemplate lookupRestTemplate;
    private String                                   usageLevelLookupAPI;
    private String                                   upchargeTypelookupAPI;
    
    private static ConcurrentHashMap<String, String> upchargeTypes = new ConcurrentHashMap<String, String>();
    private static ConcurrentHashMap<String, String> upchargeLevel = new ConcurrentHashMap<String, String>();

    private UpchargePricesParser                     upchargePricesParser  = new UpchargePricesParser();

    public String getUpChargeType(String upChargeType) {
        String temp = getUpChargeTypeByCode(upChargeType);

        if (temp != null) {
            return temp;
        } else {
            return "";
        }
    }

    private String getUpChargeTypeByCode(String code) {

        if (upchargeTypes == null || upchargeTypes.isEmpty()) {
            loadUpchargeTypes(upchargeTypelookupAPI);
        }
        if (upchargeTypes != null) {
            return upchargeTypes.get(code);
        } else {
            return null;
        }
    }

    public ConcurrentHashMap<String, String> getUpchargeTypes(String upchargeTypeAPI){
    	if(upchargeTypes.isEmpty()){
    		loadUpchargeTypes(upchargeTypeAPI);
    	}
    	return upchargeTypes;
    }
    
    private void loadUpchargeTypes(final String upchargeTypelookupAPI) {
        try {
        	LinkedList<?> criteriaCodesResponse = lookupRestTemplate.getForObject(
		            upchargeTypelookupAPI, LinkedList.class);
            upchargeTypes = upchargePricesParser.getUpchargeTypeCollectionFromJSON(criteriaCodesResponse);

            if (upchargeTypes != null && !upchargeTypes.isEmpty()) {
                _LOGGER.info("Building of Upcharge Types collection completed successfuly");
            } else {
                _LOGGER.info("Failed to build Upcharge Types collection");
            }
        } catch (Exception e) {
            _LOGGER.error("Exception while trying load Upcharge Types collection", e);
        }
    }

    public String getUpchargeLevel(String upchargeLevel) {
        String temp = getUpchargeLevelByCode(upchargeLevel);
        if (temp != null) {
            return temp;
        } else {
            return "";
        }
    }

    private String getUpchargeLevelByCode(String code) {
        if (upchargeLevel == null || upchargeLevel.isEmpty()) {
            loadUsageLevels(usageLevelLookupAPI);
        }
        if (upchargeLevel != null) {
            return upchargeLevel.get(code);
        } else {
            return null;
        }
    }

    private void loadUsageLevels(final String usageLevelLookupAPI) {
        try {
        	LinkedList<?> usageLevelsResponse = lookupRestTemplate.getForObject(
		            usageLevelLookupAPI, LinkedList.class);

            upchargeLevel = upchargePricesParser.getUsageLevelCollectionFromJSON(usageLevelsResponse);

            if (upchargeLevel != null && !upchargeLevel.isEmpty()) {
                _LOGGER.info("Building of UsageLevel collection completed successfuly");
            } else {
                _LOGGER.info("Failed to build UsageLevel collection");
            }
        } catch (Exception e) {
            _LOGGER.error("Exception while trying load UsageLevel collection", e);
        }
    }

    /**
     * @return the usageLevelLookupAPI
     */
    public String getUsageLevelLookupAPI() {
        return usageLevelLookupAPI;
    }

    /**
     * @param usageLevelLookupAPI the usageLevelLookupAPI to set
     */
    @Required
    public void setUsageLevelLookupAPI(String usageLevelLookupAPI) {
        this.usageLevelLookupAPI = usageLevelLookupAPI;
    }

    /**
     * @return the upchargeTypelookupAPI
     */
    public String getUpchargeTypelookupAPI() {
        return upchargeTypelookupAPI;
    }

    /**
     * @param upchargeTypelookupAPI the upchargeTypelookupAPI to set
     */
    @Required
    public void setUpchargeTypelookupAPI(String upchargeTypelookupAPI) {
        this.upchargeTypelookupAPI = upchargeTypelookupAPI;
    }

	public ConcurrentHashMap<String, String> getUpchargeLevels(
			String pricingUsagelevelLookup) {
    	if(upchargeLevel.isEmpty()){
    		loadUsageLevels(pricingUsagelevelLookup);
    	}
    	return upchargeLevel;
	}

}
