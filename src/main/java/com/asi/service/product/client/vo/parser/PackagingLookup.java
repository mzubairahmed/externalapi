package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;



import org.apache.log4j.Logger;

import com.asi.core.utils.JerseyClient;
import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;


public class PackagingLookup {
	private HashMap<Integer, String> productPackageMap = null;
	private final static Logger _LOGGER = Logger.getLogger(PackagingLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();
	public HashMap<Integer, String> createProductPackagingMap(
			String packagingApiUrl) {
		productPackageMap = new HashMap<>();
		try {
			String response = JerseyClient.invoke(new URI(packagingApiUrl));
			LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
					.parseToList(response);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map packagingMap = (LinkedHashMap) iter.next();
				productPackageMap.put(Integer.parseInt(packagingMap.get("Key").toString()), packagingMap.get("Value").toString());
			}
		} catch (Exception ex) {
			
			_LOGGER.info("Exception while processing Product Packaging JSON");
			productPackageMap = new HashMap<>();
		}
		return productPackageMap;
	}

}
