package com.asi.service.product.client.vo.parser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SampleLookup {

	public String findSampleTypeFromSet(@SuppressWarnings("rawtypes") LinkedList<LinkedHashMap> samplesResponse,
			String setCodeValueId) {
		String SampleType="";
		Iterator<?> iter = samplesResponse.iterator();
		while (iter.hasNext()) {
			Map<?, ?> crntSampleJson = (LinkedHashMap<?, ?>) iter.next();
			if(crntSampleJson.get("Code").toString().equalsIgnoreCase("SMPL"))
			{
			@SuppressWarnings({ "rawtypes", "unchecked" })
			LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntSampleJson
					.get("CodeValueGroups");
			@SuppressWarnings("rawtypes")
			Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();
			while(iterator.hasNext())
			{
				Map<?, ?> codeValueGrpsMap = (LinkedHashMap<?, ?>) iterator.next();
				@SuppressWarnings("rawtypes")
				List finalLst = (LinkedList) codeValueGrpsMap
						.get("SetCodeValues");
				@SuppressWarnings("rawtypes")
				Iterator finalItr = finalLst.iterator();
				while (finalItr.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map finalMap = (LinkedHashMap) finalItr.next();
					// LOGGER.info("ID:"+finalMap.get("ID"));
					if(finalMap.get("ID")
							.toString().equalsIgnoreCase(setCodeValueId))
						SampleType = finalMap.get("CodeValue").toString();
				}
			}
			}
		}
		return SampleType;
	}

}
