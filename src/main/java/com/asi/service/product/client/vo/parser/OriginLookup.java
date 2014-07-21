package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;



import org.apache.log4j.Logger;

import com.asi.core.utils.JerseyClient;
import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;



public class OriginLookup {
	private HashMap<String, String> productOriginMap = null;
	private final static Logger _LOGGER = Logger.getLogger(OriginLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();

	
	public HashMap<String, String> createProductOriginMap(String originLookupUrl) {
		productOriginMap = new HashMap<>();
		try {
			String response = JerseyClient.invoke(new URI(originLookupUrl));
			 LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
						.parseToList(response);
	            Iterator<?> iter = json.iterator();// entrySet().iterator();
	            // LOGGER.info("==iterate result==");
	            while (iter.hasNext()) {
	                @SuppressWarnings("unchecked")
	                LinkedHashMap<String, String> crntOriginJson = (LinkedHashMap<String, String>) iter.next();
			
			
					productOriginMap.put(String.valueOf(crntOriginJson.get("ID")).trim(),
							crntOriginJson.get("CodeValue").toString());
				}
		
		} catch (Exception ex) {
			_LOGGER.info("Exception while processing Product Origin JSON");
			productOriginMap = new HashMap<>();
		}
		return productOriginMap;
	}
}
