package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import json.IParser;
import json.JSONParserImpl;

import org.apache.log4j.Logger;

import com.asi.core.utils.JerseyClient;


public class MaterialLookup {
	private HashMap<Integer, String> productMaterialMap = null;
	private final static Logger _LOGGER = Logger.getLogger(ColorLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();

	
	public HashMap<Integer, String> createProductMaterialMap(final String colorLookupUrl) {
		try {
			productMaterialMap = new HashMap<Integer, String>();
			int currentMaterialKey = 0;
			String currentMaterialValue = "";
			String response = JerseyClient.invoke(new URI(colorLookupUrl));
			LinkedList<?> json = (LinkedList<?>) jsonParser
					.parseToList(response);
			Iterator<?> iter = json.iterator();
			while (iter.hasNext()) {
				Map<?, ?> crntMaterialJson = (LinkedHashMap<?, ?>) iter.next();
				@SuppressWarnings({ "rawtypes", "unchecked" })
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntMaterialJson
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					currentMaterialKey=0;currentMaterialValue="";
					@SuppressWarnings({ "rawtypes" })
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					if (null != codeValueGrpsMap.get("DisplayName"))
						currentMaterialValue = codeValueGrpsMap.get("DisplayName")
								.toString();
					@SuppressWarnings("rawtypes")
					List finalLst = (LinkedList) codeValueGrpsMap
							.get("SetCodeValues");
					@SuppressWarnings("rawtypes")
					Iterator finalItr = finalLst.iterator();
					if (finalItr.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map finalMap = (LinkedHashMap) finalItr.next();
						// LOGGER.info("ID:"+finalMap.get("ID"));
						currentMaterialKey = Integer.parseInt(finalMap.get("ID")
								.toString());
						if(currentMaterialValue.contains("-"))
						{
							currentMaterialValue=currentMaterialValue.substring(currentMaterialValue.indexOf("-")+1);
						}
						productMaterialMap.put(currentMaterialKey, currentMaterialValue);
					}
				}
			}
			
		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Material JSON", e);
			productMaterialMap = new HashMap<Integer, String>();
		}
		return productMaterialMap;

	}

}
