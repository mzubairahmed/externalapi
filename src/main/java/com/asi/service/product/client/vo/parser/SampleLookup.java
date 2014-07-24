package com.asi.service.product.client.vo.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SampleLookup {

	public String findSampleTypeFromSet(@SuppressWarnings("rawtypes") HashMap<String,String> samplesResponse,
			String setCodeValueId) {
		String SampleType="";
		Iterator itr = samplesResponse.entrySet().iterator();
	    while (itr.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itr.next();
	        if(pairs.getKey().toString().equalsIgnoreCase(setCodeValueId)){
	        	SampleType=pairs.getValue().toString();
	        }
	    }
	    
		return SampleType;
	}

}
