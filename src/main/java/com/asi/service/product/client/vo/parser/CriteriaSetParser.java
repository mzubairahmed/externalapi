package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.ProductCriteriaSet;

public class CriteriaSetParser {
	@Autowired LookupParser productLookupParser;
	 private static ConcurrentHashMap<String, HashMap<String, String>> criteriaSetReference =  new ConcurrentHashMap<>();
    private void addReferenceSet(String externalProductId, String criteriaCode,
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
    public LookupParser getProductLookupParser() {
		return productLookupParser;
	}
	public void setProductLookupParser(LookupParser productLookupParser) {
		this.productLookupParser = productLookupParser;
	}
	@SuppressWarnings({ "unchecked" })
	public ConcurrentHashMap<String, HashMap<String, String>> getCriteriaSetDetailsByExternalId(String xid,List<ProductCriteriaSet> productCriteriaSetsAry)
    {
    	List<CriteriaSetValue> criteriaSetValuesList=null;
    	List<LinkedHashMap<String,String>> valueList=new ArrayList<>();
    	for(ProductCriteriaSet productCriteriaSet:productCriteriaSetsAry)
    	{
    		criteriaSetValuesList=productCriteriaSet.getCriteriaSetValues();
    		for(CriteriaSetValue currentCriteriaSetValue:criteriaSetValuesList)
    		{
    			if(currentCriteriaSetValue.getValue() instanceof String)
    			addReferenceSet(xid, currentCriteriaSetValue.getCriteriaCode(), currentCriteriaSetValue.getID(), currentCriteriaSetValue.getValue().toString());
    			else{
    				if(currentCriteriaSetValue.getValue() instanceof List){
    					valueList=(List<LinkedHashMap<String,String>>)currentCriteriaSetValue.getValue();
    					for(LinkedHashMap<String, String> valueObj:valueList){
    						addReferenceSet(xid, currentCriteriaSetValue.getCriteriaCode(), currentCriteriaSetValue.getID(), productLookupParser.getElementValue(currentCriteriaSetValue.getCriteriaCode(),String.valueOf(valueObj.get("CriteriaAttributeId")),String.valueOf(valueObj.get("UnitValue")),String.valueOf(valueObj.get("UnitOfMeasureCode"))));	
    					}
    				}
    			}
    		}	    		
    	}
    	return criteriaSetReference;
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
}