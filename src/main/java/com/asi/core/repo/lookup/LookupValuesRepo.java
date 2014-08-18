package com.asi.core.repo.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.JsonToLookupTableConverter;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.Category;

@Component
public class LookupValuesRepo {
	public static RestTemplate lookupRestTemplate;
	private static Logger _LOGGER = LoggerFactory
			.getLogger(LookupValuesRepo.class);

	public CategoriesList getAllCategories() {
		CategoriesList categoriesList = new CategoriesList();
		Category category = null;
		ConcurrentHashMap<String, String> categoryCodeLookupTable;
		List<Category> categoryArrayList = new ArrayList<Category>();
		LinkedList<?> categoryResponse = lookupRestTemplate
				.getForObject(
						RestAPIProperties
								.get(ApplicationConstants.PRODUCT_CATEGORIES_LOOKUP_URL),
						LinkedList.class);
		try {
			categoryCodeLookupTable = JsonToLookupTableConverter
					.jsonToProductCategoryLookupTable(categoryResponse);
			@SuppressWarnings("rawtypes")
			Iterator categoryIterator = categoryCodeLookupTable.keySet()
					.iterator();
			while (categoryIterator.hasNext()) {
				category = new Category();
				category.setName(categoryIterator.next().toString());
				categoryArrayList.add(category);
				categoriesList.setCategories(categoryArrayList);
			}
		} catch (Exception ex) {
			_LOGGER.info(ex.getMessage());
		}
		return categoriesList;
	}

}
