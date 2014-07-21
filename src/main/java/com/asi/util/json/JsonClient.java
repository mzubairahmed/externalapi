package com.asi.util.json;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonClient {

	private JSONParser parser;
	private ContainerFactory containerFactory;
	public void init()
	{
		parser = new JSONParser();
		containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		
		
	}
	
	public LinkedList<?> parse(String jsonText)
	{
		LinkedList<?> json = null;
		try {
			json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
