package com.asi.ext.api.integration.lookup.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.asi.ext.api.service.model.Value;

public class CriteriaSetParser {
	 private static ConcurrentHashMap<String, HashMap<String, String>> criteriaSetReference =  new ConcurrentHashMap<>();
	 private static ConcurrentHashMap<String, HashMap<String, Object>> criteriaSetValueReference =  new ConcurrentHashMap<>();
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
   //     if(null!=tempMap.get(criteriaSetValueId)){
        return tempMap.get(criteriaSetValueId);
        /*   }else{
        	return findSizesCriteriaSetById(extPrdId,criteriaSetValueId);
        }*/
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
    public Object findSizesCriteriaSetById(String extPrdId, String criteriaSetValueId) {
        if (criteriaSetValueReference == null || criteriaSetValueReference.isEmpty()) {
            return null;
        }
        HashMap<String, Object> tempMap = criteriaSetValueReference.get(extPrdId.trim());

        if (tempMap == null || tempMap.isEmpty()) {
            return null;
        }
        return tempMap.get(criteriaSetValueId);
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
	public void addSizesReferenceSet(String externalProductId,
			String criteriaCode, String criteriaSetValueId, Object valueObj) {
		Object currentValueObj=null;
		List<Value> valuesList=null;
		List<Value> tempValuesList=null;
		if(valueObj instanceof Value){
		currentValueObj=(Value)valueObj;
		((Value) currentValueObj).setCriteriaType(criteriaCode);
		}else if(valueObj instanceof List){
			valuesList=(List<Value>)valueObj;
			tempValuesList=new ArrayList<>();
			for(Value currentValue:valuesList){
				currentValue.setCriteriaType(criteriaCode);
				tempValuesList.add(currentValue);
			}
			currentValueObj=tempValuesList;
		}else
			currentValueObj=valueObj;
    	if(null!=valueObj && null!=criteriaCode)
    	{
          if (criteriaSetValueReference== null || criteriaSetValueReference.isEmpty()) {
        	  criteriaSetValueReference = new ConcurrentHashMap<String, HashMap<String, Object>>();
            }

            if (criteriaSetValueReference.get(externalProductId.trim()) == null) {
            	criteriaSetValueReference.put(externalProductId.trim(), new HashMap<String, Object>());
            }
          if(criteriaSetValueReference.get(externalProductId.trim()).get(criteriaSetValueId) == null){
        	  criteriaSetValueReference.get(externalProductId.trim()).put(criteriaSetValueId,currentValueObj);
          }
    	} else
    	{
    		return;
    	}
		
	}
	 
}
