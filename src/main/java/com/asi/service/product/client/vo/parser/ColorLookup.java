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



public class ColorLookup {
	private HashMap<String, String> productColorMap = null;
	private final static Logger _LOGGER = Logger.getLogger(ColorLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();

	
	public HashMap<String, String> createProductColorMap(final String colorLookupUrl) {
		try {
			productColorMap = new HashMap<String, String>();
			String currentColorKey = "";
			String currentColorValue = "";
			String response = JerseyClient.invoke(new URI(colorLookupUrl));
			LinkedList<?> json = (LinkedList<?>) jsonParser
					.parseToList(response);
			Iterator<?> iter = json.iterator();
			while (iter.hasNext()) {
				Map<?, ?> crntColorJson = (LinkedHashMap<?, ?>) iter.next();
				@SuppressWarnings({ "rawtypes", "unchecked" })
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntColorJson
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					currentColorKey="";currentColorValue="";
					@SuppressWarnings({ "rawtypes" })
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					if (null != codeValueGrpsMap.get("DisplayName"))
						currentColorValue = codeValueGrpsMap.get("DisplayName")
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
						currentColorKey = finalMap.get("ID").toString();
						productColorMap.put(currentColorKey, currentColorValue);
					}
				}
			}
			
		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Color JSON", e);
			productColorMap = new HashMap<String, String>();
		}
		return productColorMap;

	}

}
