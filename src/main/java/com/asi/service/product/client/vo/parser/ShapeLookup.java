
package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import json.IParser;
import json.JSONParserImpl;

import org.apache.log4j.Logger;

import com.asi.core.utils.JerseyClient;


public class ShapeLookup {
	private HashMap<Integer, String> productShapeMap = null;
	private final static Logger _LOGGER = Logger.getLogger(ShapeLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();

	
			public HashMap<Integer, String> createProductShapeMap(String shapeApiUrl) {
				productShapeMap = new HashMap<>();
				try {
					String response = JerseyClient.invoke(new URI(shapeApiUrl));
					 LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
								.parseToList(response);
					 Iterator<?> iter = json.iterator();// entrySet().iterator();
			            // LOGGER.info("==iterate result==");
			            while (iter.hasNext()) {
			                @SuppressWarnings("unchecked")
			                LinkedHashMap<Integer, String> crntShapeJson = (LinkedHashMap<Integer, String>) iter.next();
			                productShapeMap.put(Integer.parseInt(crntShapeJson.get("Key").toString()), crntShapeJson.get("Value").toString());
			            }
				} catch (Exception ex) {
					_LOGGER.info("Exception while processing Product Shape JSON");
					productShapeMap = new HashMap<>();
				}
				return productShapeMap;
			}
}
