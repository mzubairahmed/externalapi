package com.asi.core.repo.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.asi.service.lookup.ColorsList;
import com.asi.service.lookup.MaterialsList;
import com.asi.service.lookup.PackagesList;
import com.asi.service.lookup.SafetyWarningsList;
import com.asi.service.lookup.ShapesList;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.Category;
import com.asi.service.lookup.vo.LookupName;
import com.asi.service.lookup.vo.ThemesList;

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

	public ThemesList getAllThemes() {
		ThemesList themesList=new ThemesList();
		LookupName theme=null;
		HashMap<String, String> themesLookupTable;
		List<LookupName> themeArrayList = new ArrayList<LookupName>();
		LinkedList<?> categoryResponse = lookupRestTemplate
				.getForObject(
						RestAPIProperties
								.get(ApplicationConstants.PRODUCT_THEMES_URL),
						LinkedList.class);
		
		try {
			themesLookupTable = JsonToLookupTableConverter
					.jsonToProductThemesMap(categoryResponse);
			@SuppressWarnings("rawtypes")
			Iterator themeIterator = themesLookupTable.keySet()
					.iterator();
			while (themeIterator.hasNext()) {
				theme = new LookupName();
				theme.setName(themeIterator.next().toString());
				themeArrayList.add(theme);
				themesList.setThemes(themeArrayList);
			}
		} catch (Exception ex) {
			_LOGGER.info(ex.getMessage());
		}
		return themesList;
		
	}

	public ColorsList getAllColors() {
		ColorsList colorsList=new ColorsList();
		HashMap<String, String> colorsLookupTable;
		List<LookupName> colorsArrayList = new ArrayList<LookupName>();
		LookupName crntLookupName=null;
		try {
			colorsLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.createProductColorMap(RestAPIProperties
					.get(ApplicationConstants.COLORS_LOOKUP_URL));
			@SuppressWarnings("rawtypes")
			Iterator colorIterator = colorsLookupTable.keySet()
					.iterator();
			while (colorIterator.hasNext()) {
				crntLookupName=new LookupName();
				crntLookupName.setName(colorIterator.next().toString());
				colorsArrayList.add(crntLookupName);				
			}
			colorsList.setColors(colorsArrayList);
		} catch (Exception ex) {
			_LOGGER.info(ex.getMessage());
		}
		return colorsList;
	}

	public MaterialsList getAllMaterials() {
		MaterialsList materialsList=new MaterialsList();
		List<LookupName> materialsArrayList = new ArrayList<LookupName>();
		LookupName crntLookupName=null;
		try{
			HashMap<String, String> materialsLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.createProductMaterialMap(RestAPIProperties
					.get(ApplicationConstants.MATERIALS_LOOKUP_URL));
			@SuppressWarnings("rawtypes")
			Iterator colorIterator = materialsLookupTable.keySet()
					.iterator();
			while (colorIterator.hasNext()) {
				crntLookupName=new LookupName();
				crntLookupName.setName(colorIterator.next().toString());
				materialsArrayList.add(crntLookupName);				
			}
			materialsList.setMaterials(materialsArrayList);
		}catch(Exception ex){
			_LOGGER.info(ex.getMessage());
		}		
		return materialsList;
	}

	public ShapesList getAllShapes() {
		ShapesList shapesList=new ShapesList();
		List<LookupName> shapesArrayList = new ArrayList<LookupName>();
		LookupName crntLookupName=null;
		try{
		     LinkedList<?> productShapesResponse = lookupRestTemplate.getForObject(
                     RestAPIProperties.get(ApplicationConstants.PRODUCT_SHAPES_LOOKUP_URL), LinkedList.class);
        
			HashMap<String, String> shapesLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToProductShapesMap(productShapesResponse);
			@SuppressWarnings("rawtypes")
			Iterator shapeIterator = shapesLookupTable.keySet()
					.iterator();
			while (shapeIterator.hasNext()) {
				crntLookupName=new LookupName();
				crntLookupName.setName(shapeIterator.next().toString());
				shapesArrayList.add(crntLookupName);				
			}
			shapesList.setShapes(shapesArrayList);
		}catch(Exception ex){
			_LOGGER.info(ex.getMessage());
		}
		return shapesList;
	}
	

	public PackagesList getAllPackages() {
		PackagesList packagesList=new PackagesList();
		List<LookupName> packagesArrayList = new ArrayList<LookupName>();
		LookupName crntLookupName=null;
		try{
		LinkedList<?> productPackagesResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.PACKAGING_LOOKUP), LinkedList.class);
	            
	         HashMap<String, String> packagesLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToProductPackagesMap(productPackagesResponse);
				@SuppressWarnings("rawtypes")
				Iterator packageIterator = packagesLookupTable.keySet()
						.iterator();
				while (packageIterator.hasNext()) {
					crntLookupName=new LookupName();
					crntLookupName.setName(packageIterator.next().toString());
					packagesArrayList.add(crntLookupName);				
				}
				packagesList.setPackages(packagesArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return packagesList;
	}

	public SafetyWarningsList getSafetyWarningsList() {
		SafetyWarningsList safetyWarningsList=new SafetyWarningsList();
		List<String> safetyWarningArrayList = new ArrayList<String>();
		try{
		LinkedList<?> productSafetyWarningsResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.SAFETY_WARNINGS_LOOKUP), LinkedList.class);
	            
	         HashMap<String, String> safetyWarningsLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToSafetyWarningLookupTable(productSafetyWarningsResponse);
				@SuppressWarnings("rawtypes")
				Iterator safetyWarningIterator = safetyWarningsLookupTable.keySet()
						.iterator();
				while (safetyWarningIterator.hasNext()) {
					safetyWarningArrayList.add(safetyWarningIterator.next().toString());				
				}
				safetyWarningsList.setSafetyWarnings(safetyWarningArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return safetyWarningsList;
	}
}
