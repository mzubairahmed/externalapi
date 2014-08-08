package com.asi.ext.api.integration.lookup.parser;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CriteriaSetParser {
	 private static ConcurrentHashMap<String, HashMap<String, String>> criteriaSetReference =  new ConcurrentHashMap<>();
	 private static ConcurrentHashMap<String, HashMap<String, String>> criteriaSetIdReference =  new ConcurrentHashMap<>();
    public void addReferenceSet(String externalProductId, String criteriaCode,
            Integer criteriaSetValueId,String value) {
    	if(null!=value && null!=criteriaCode)
    	{
          if (criteriaSetReference== null || criteriaSetReference.isEmpty()) {
              criteriaSetReference = new ConcurrentHashMap<String, HashMap<String, String>>();
            }

            if (criteriaSetReference.get(externalProductId.trim()) == null) {
                criteriaSetReference.put(externalProductId.trim(), new HashMap<String, String>());
            }
            criteriaSetReference.get(externalProductId.trim()).put(criteriaSetValueId+"",criteriaCode.trim() + "__" + value.toUpperCase().trim());
    	} else
    	{
    		return;
    	}
    }
    public void addCriteriaSetByCode(String externalProductId, String criteriaCode,
            String criteriaSetId) {
    	if(null!=criteriaSetId && null!=criteriaCode)
    	{
          if (criteriaSetIdReference== null || criteriaSetIdReference.isEmpty()) {
        	  criteriaSetIdReference = new ConcurrentHashMap<String, HashMap<String, String>>();
            }

            if (criteriaSetIdReference.get(externalProductId.trim()) == null) {
            	criteriaSetIdReference.put(externalProductId.trim(), new HashMap<String, String>());
            }
            criteriaSetIdReference.get(externalProductId.trim()).put(criteriaSetId,criteriaCode);
    	} else
    	{
    		return;
    	}
    }
    
    public String findCriteriaSetValueById(String extPrdId, String criteriaSetValueId) {
        if (criteriaSetReference == null || criteriaSetReference.isEmpty()) {
            return null;
        }
        HashMap<String, String> tempMap = criteriaSetReference.get(extPrdId.trim());

        if (tempMap == null || tempMap.isEmpty()) {
            return null;
        }
        return tempMap.get(criteriaSetValueId);
    }
    public HashMap<String, String> findCriteriaSetMapValueById(String extPrdId) {
        if (criteriaSetReference == null || criteriaSetReference.isEmpty()) {
            return null;
        }
        HashMap<String, String> tempMap = criteriaSetReference.get(extPrdId.trim());
        return tempMap;
    }
    public boolean isCriteriaExists(String criteria) {
        if (criteriaSetReference != null && criteriaSetReference.containsKey(criteria)) {
            return true;
        } else {
            return false;
        }
    }
    public void removeCriteriaReferencesByExternalId(String externalId)
    {
    	if(null!=criteriaSetReference)
    	 criteriaSetReference.remove(externalId);
    }
	public String findCriteriaBySetId(String extPrdId, String valueOf) {
		if (criteriaSetIdReference == null || criteriaSetIdReference.isEmpty()) {
            return null;
        }
        HashMap<String, String> tempMap = criteriaSetIdReference.get(extPrdId.trim());

        if (tempMap == null || tempMap.isEmpty()) {
            return null;
        }
        return tempMap.get(valueOf);
	}
}
