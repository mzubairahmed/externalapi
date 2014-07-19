package com.asi.util.json;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONParserImpl implements IParser {

	private IParserFactory jsonParserFactory = null;
	private final static Logger _LOGGER = Logger.getLogger(JSONParserImpl.class.getName());
	public JSONParserImpl()
	{

		jsonParserFactory = new JSONParserFactory();
		jsonParserFactory.createContainerFatory();
		jsonParserFactory.creatParser();
		
	}
	@Override
	public LinkedList<?> parseToList(String jsonText) {

		LinkedList<?> json = null;
		try {
			JSONParser parser= (JSONParser) jsonParserFactory.getParser();
		    ContainerFactory containerFactory = (ContainerFactory) jsonParserFactory.getContainerFatory();
			json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
		} catch (ParseException e) {
			_LOGGER.error(e.getMessage(),e.getCause());
		}

		return json;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> parseToMap(String jsonText) {

		Map<?,?> json=null;
		try {
			JSONParser parser= (JSONParser) jsonParserFactory.getParser();
		    ContainerFactory containerFactory = (ContainerFactory) jsonParserFactory.getContainerFatory();
			json = (LinkedHashMap<Object, Object>) parser.parse(jsonText,
					containerFactory);
		} catch (ParseException e) {
			_LOGGER.error(e.getMessage(),e.getCause());
		}
		
		return json;
	}
	
	 @SuppressWarnings("unchecked")
	    public <T> T convertJsonToBeanCollection(String json, Class<?> classType) {

	        if (json != null && !json.trim().isEmpty()) {
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	            try {
	                return (T) mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, classType));
	            } catch (JsonParseException e) {
	                _LOGGER.error("JSONParseException while deserializing jsonData to Object, ClassType : "
	                        + classType.getCanonicalName() + ", JsonData : " + json + " Exception : " + e.getMessage());
	            } catch (JsonMappingException e) {
	                _LOGGER.error("JsonMappingException while deserializing jsonData to Object, ClassType : "
	                        + classType.getCanonicalName() + ", JsonData : " + json + " Exception : " + e.getMessage());
	            } catch (IOException e) {
	                _LOGGER.error("IOException while deserializing jsonData to Object, ClassType : " + classType.getCanonicalName()
	                        + ", JsonData : " + json + " Exception : " + e.getMessage());
	            }
	        } else {
	            return null;
	        }
	        return null;
	    }

}
