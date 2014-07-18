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



public class ImprintLookup {
	private HashMap<Integer, String> imprintMethodMap = null;
	private HashMap<Integer, String> imprintArtworkMap = null;
	private final static Logger _LOGGER = Logger.getLogger(ImprintLookup.class
			.getName());
	private static IParser jsonParser = new JSONParserImpl();
	public HashMap<Integer, String> createImprintMethodMap(
			String imprintUrl) {
		imprintMethodMap = new HashMap<>();
		try {
			String response = JerseyClient.invoke(new URI(imprintUrl));
			LinkedList<?> json = (LinkedList<?>) (LinkedList<?>) jsonParser
					.parseToList(response);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map imprintMthdMap = (LinkedHashMap) iter.next();
				imprintMethodMap.put(Integer.parseInt(imprintMthdMap.get("ID").toString()), imprintMthdMap.get("CodeValue").toString());
			}
		} catch (Exception ex) {
			
			_LOGGER.info("Exception while processing Product Imprint Method JSON");
			imprintMethodMap = new HashMap<>();
		}
		return imprintMethodMap;
	}
	public HashMap<Integer, String> createImprintArtworkMap(
			String imprintArtworkUrl) {
		try {
			imprintArtworkMap = new HashMap<Integer, String>();
			String response = JerseyClient.invoke(new URI(imprintArtworkUrl));
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
					@SuppressWarnings({ "rawtypes" })
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					@SuppressWarnings("rawtypes")
					List finalLst = (LinkedList) codeValueGrpsMap
							.get("SetCodeValues");
					@SuppressWarnings("rawtypes")
					Iterator finalItr = finalLst.iterator();
					while (finalItr.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map finalMap = (LinkedHashMap) finalItr.next();
						imprintArtworkMap.put(Integer.parseInt(finalMap.get("ID")
								.toString()), finalMap.get("CodeValue").toString());
					}
				}
			}
			
		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Imprint Artwork JSON", e);
			imprintArtworkMap = new HashMap<Integer, String>();
		}
		return imprintArtworkMap;
	}

}
