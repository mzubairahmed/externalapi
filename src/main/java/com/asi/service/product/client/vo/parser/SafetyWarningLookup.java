package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import json.IParser;
import json.JSONParserImpl;

import org.apache.log4j.Logger;

import com.asi.core.utils.JerseyClient;



public class SafetyWarningLookup {
	private HashMap<String, String> productWarningsMap = null;
	private final static Logger _LOGGER = Logger.getLogger(SafetyWarningLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();
	public HashMap<String, String> createProductWarningMap(
			String safetyWarningUrl) {
		productWarningsMap = new HashMap<>();
		try {
			String response = JerseyClient.invoke(new URI(safetyWarningUrl));
			LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
					.parseToList(response);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map warningsMap = (LinkedHashMap) iter.next();
				productWarningsMap.put(warningsMap.get("Key").toString(), warningsMap.get("Value").toString());
			}
		} catch (Exception ex) {
			
			_LOGGER.info("Exception while processing Product Safety Warnings JSON");
			productWarningsMap = new HashMap<>();
		}
		return productWarningsMap;
	}
}
