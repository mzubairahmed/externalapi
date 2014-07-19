package com.asi.util.json;

import java.util.LinkedList;
import java.util.Map;

public interface IParser {

	public LinkedList<?> parseToList(String jsonText);

	public Map<?, ?> parseToMap(String jsonText);

	public <T> T convertJsonToBeanCollection(String json,
			Class<?> classType);

}
