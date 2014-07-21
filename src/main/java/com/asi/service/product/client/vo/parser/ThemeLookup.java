
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



public class ThemeLookup {
	private HashMap<Integer, String> productThemeMap = null;

	private final static Logger _LOGGER = Logger.getLogger(ThemeLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();

	public HashMap<Integer, String> createProductThemeMap(
			String themeApiUrl) {
		productThemeMap = new HashMap<>();
		try {
			String response = JerseyClient.invoke(new URI(themeApiUrl));
			LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
					.parseToList(response);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				Map<?, ?> crntThemeJson = (LinkedHashMap<?, ?>) iter.next();
				@SuppressWarnings({ "rawtypes", "unchecked" })
				LinkedList<LinkedHashMap> setCodeValues = (LinkedList<LinkedHashMap>) crntThemeJson
						.get("SetCodeValues");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = setCodeValues.iterator();
				while (iterator.hasNext()) {
					@SuppressWarnings({ "rawtypes" })
					Map setCodeValueGrpsMap = (LinkedHashMap) iterator.next();
			
					productThemeMap.put(Integer.parseInt(setCodeValueGrpsMap.get(
							"ID").toString()), setCodeValueGrpsMap.get("CodeValue")
							.toString());
				}
			}
		} catch (Exception ex) {
			_LOGGER.info("Exception while processing Product Theme JSON");
			productThemeMap = new HashMap<>();
		}
		return productThemeMap;
	}
}
