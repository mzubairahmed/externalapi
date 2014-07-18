
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



public class TradeNameLookup {
	private HashMap<Integer, String> productTradeNameMap = null;
	private final static Logger _LOGGER = Logger.getLogger(TradeNameLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();
	public HashMap<Integer, String> createProductTradeNameMap(
			String tradeNameLookupUrl) {
		productTradeNameMap = new HashMap<>();
		try {
			String response = JerseyClient.invoke(new URI(tradeNameLookupUrl));
			 LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
						.parseToList(response);
	            Iterator<?> iter = json.iterator();
	            while (iter.hasNext()) {
	                @SuppressWarnings("unchecked")
	                LinkedHashMap<String, String> crntTradeNameJson = (LinkedHashMap<String, String>) iter.next();
	                productTradeNameMap.put(Integer.parseInt(crntTradeNameJson.get("Key").toString()),
	                		crntTradeNameJson.get("Value").toString());
				}	
		} catch (Exception ex) {
			_LOGGER.info("Exception while processing Product Trade Name JSON");
			productTradeNameMap = new HashMap<>();
		}
		return productTradeNameMap;
	}

}
