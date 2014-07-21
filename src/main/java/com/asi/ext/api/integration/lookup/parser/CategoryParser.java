package com.asi.ext.api.integration.lookup.parser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;

public class CategoryParser {
    
    private static IParser jsonParser = new JSONParserImpl();

    public ConcurrentHashMap<String, String> getCategoryCollectionFromJSON(String categoryJson) {
        
        ConcurrentHashMap<String, String> categories = new ConcurrentHashMap<String, String>();
        LinkedList<?> jsonMap = (LinkedList<?>) jsonParser.parseToList(categoryJson);
        Iterator<?> iter = jsonMap.iterator();
        while (iter.hasNext()) {
            Map<?, ?> currentCategory = (LinkedHashMap<?, ?>) iter.next();
            
            if (currentCategory != null) {
                String key = currentCategory.get("Code") != null ? (String) currentCategory.get("Code") : null;
                String value = currentCategory.get("Name") != null ? (String) currentCategory.get("Name") : null;
                if (key != null && value != null) {
                    categories.put(key, value);
                }
            }
            
        }
        return categories;
    }
}
