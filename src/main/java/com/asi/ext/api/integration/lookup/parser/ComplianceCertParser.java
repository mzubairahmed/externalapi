package com.asi.ext.api.integration.lookup.parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;

public class ComplianceCertParser {
    private final static Logger _LOGGER = Logger.getLogger(ComplianceCertParser.class.getName());
    
    private static IParser jsonParser = new JSONParserImpl();
    
    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<String, String> createComplianceCertMap(String source) {
        
        ConcurrentHashMap<String, String> complianceCertMap = new ConcurrentHashMap<>();
        try {
            LinkedList<?> tempList = jsonParser.parseToList(source);
            if (tempList != null && !tempList.isEmpty()) {
                Iterator<?> itr = tempList.iterator();
               while(itr.hasNext()) {
                   try {
                       Map<String, String> map = (Map<String, String>) itr.next();
                        if (map != null) {
                            String key = String.valueOf(map.get("Key"));
                            String value = map.get("Value");
                            if (key != null && value != null) {
                                complianceCertMap.put(key, value);
                            }
                        }
                   } catch (Exception e) {
                       _LOGGER.error("Exception while parsing compliance and cert JSON to Map", e);
                   } // Added for getting as much as possible values
                }
            }
        } catch (Exception e) {
            _LOGGER.error("Exception while parsing compliance and cert JSON to Map", e);
        }
        
        return complianceCertMap;
    }

}