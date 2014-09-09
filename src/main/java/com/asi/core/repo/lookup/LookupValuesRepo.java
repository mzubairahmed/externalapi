package com.asi.core.repo.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.radar.lookup.model.PriceUnitJsonModel;
import com.asi.ext.api.rest.JersyClientGet;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.JsonToLookupTableConverter;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.lookup.ArtworksList;
import com.asi.service.lookup.ColorsList;
import com.asi.service.lookup.ComplianceList;
import com.asi.service.lookup.CriteriaCodesList;
import com.asi.service.lookup.CurrencyList;
import com.asi.service.lookup.DiscountRatesList;
import com.asi.service.lookup.ImprintMethodsList;
import com.asi.service.lookup.MaterialsList;
import com.asi.service.lookup.PackagesList;
import com.asi.service.lookup.PriceModifiers;
import com.asi.service.lookup.SafetyWarningsList;
import com.asi.service.lookup.ShapesList;
import com.asi.service.lookup.UpchargeLevelsList;
import com.asi.service.lookup.UpchargeTypesList;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.ThemesList;
import com.asi.service.product.client.vo.Currency;
import com.asi.service.product.client.vo.DiscountRate;
import com.asi.service.product.client.vo.parser.UpChargeLookup;

@Component
public class LookupValuesRepo {
	public static RestTemplate lookupRestTemplate;
	private static Logger _LOGGER = LoggerFactory
			.getLogger(LookupValuesRepo.class);

	public CategoriesList getAllCategories() {
		CategoriesList categoriesList = new CategoriesList();
		ConcurrentHashMap<String, String> categoryCodeLookupTable;
		List<String> categoryArrayList = new ArrayList<>();
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
				categoryArrayList.add(categoryIterator.next().toString());
			}
			categoriesList.setCategories(categoryArrayList);
		} catch (Exception ex) {
			_LOGGER.info(ex.getMessage());
		}
		return categoriesList;
	}

	public ThemesList getAllThemes() {
		ThemesList themesList=new ThemesList();
		HashMap<String, String> themesLookupTable;
		List<String> themeArrayList = new ArrayList<String>();
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
				themeArrayList.add(themeIterator.next().toString());
			}
			themesList.setThemes(themeArrayList);
		} catch (Exception ex) {
			_LOGGER.info(ex.getMessage());
		}
		return themesList;
		
	}

	public ColorsList getAllColors() {
		ColorsList colorsList=new ColorsList();
		HashMap<String, String> colorsLookupTable;
		List<String> colorsArrayList = new ArrayList<>();
		try {
			colorsLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.createProductColorMap(RestAPIProperties
					.get(ApplicationConstants.COLORS_LOOKUP_URL));
			@SuppressWarnings("rawtypes")
			Iterator colorIterator = colorsLookupTable.keySet()
					.iterator();
			while (colorIterator.hasNext()) {
				colorsArrayList.add(colorIterator.next().toString());				
			}
			colorsList.setColors(colorsArrayList);
		} catch (Exception ex) {
			_LOGGER.info(ex.getMessage());
		}
		return colorsList;
	}

	public MaterialsList getAllMaterials() {
		MaterialsList materialsList=new MaterialsList();
		List<String> materialsArrayList = new ArrayList<>();
		try{
			HashMap<String, String> materialsLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.createProductMaterialMap(RestAPIProperties
					.get(ApplicationConstants.MATERIALS_LOOKUP_URL));
			@SuppressWarnings("rawtypes")
			Iterator materialIterator = materialsLookupTable.keySet()
					.iterator();
			while (materialIterator.hasNext()) {
				materialsArrayList.add(materialIterator.next().toString());				
			}
			materialsList.setMaterials(materialsArrayList);
		}catch(Exception ex){
			_LOGGER.info(ex.getMessage());
		}		
		return materialsList;
	}

	public ShapesList getAllShapes() {
		ShapesList shapesList=new ShapesList();
		List<String> shapesArrayList = new ArrayList<>();
		try{
		     LinkedList<?> productShapesResponse = lookupRestTemplate.getForObject(
                     RestAPIProperties.get(ApplicationConstants.PRODUCT_SHAPES_LOOKUP_URL), LinkedList.class);
        
			HashMap<String, String> shapesLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToProductShapesMap(productShapesResponse);
			@SuppressWarnings("rawtypes")
			Iterator shapeIterator = shapesLookupTable.keySet()
					.iterator();
			while (shapeIterator.hasNext()) {
				shapesArrayList.add(shapeIterator.next().toString());				
			}
			shapesList.setShapes(shapesArrayList);
		}catch(Exception ex){
			_LOGGER.info(ex.getMessage());
		}
		return shapesList;
	}
	

	public PackagesList getAllPackages() {
		PackagesList packagesList=new PackagesList();
		List<String> packagesArrayList = new ArrayList<String>();
		try{
		LinkedList<?> productPackagesResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.PACKAGING_LOOKUP), LinkedList.class);
	            
	         HashMap<String, String> packagesLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToProductPackagesMap(productPackagesResponse);
				@SuppressWarnings("rawtypes")
				Iterator packageIterator = packagesLookupTable.keySet()
						.iterator();
				while (packageIterator.hasNext()) {
					packagesArrayList.add(packageIterator.next().toString());				
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
	
	public ImprintMethodsList getImprintMethodsList() {
		ImprintMethodsList imprintMethodsList=new ImprintMethodsList();
		List<String> imprintMethodArrayList = new ArrayList<String>();
		try{
		LinkedList<?> imprintMethodsResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.IMPRINT_LOOKUP_URL), LinkedList.class);
	            
	         HashMap<String, String> imprintMethodsLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToImprintMethodLookupTable(imprintMethodsResponse);
				@SuppressWarnings("rawtypes")
				Iterator imprintMethodIterator = imprintMethodsLookupTable.keySet()
						.iterator();
				while (imprintMethodIterator.hasNext()) {
					imprintMethodArrayList.add(imprintMethodIterator.next().toString());				
				}
				imprintMethodsList.setImprintMethods(imprintMethodArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return imprintMethodsList;
	}

	public ArtworksList getArtworksList() {
		ArtworksList artworksList=new ArtworksList();
		List<String> artworkArrayList = new ArrayList<>();
		try{
		LinkedList<?> artworksResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.IMPRINT_ARTWORK_LOOKUP_URL), LinkedList.class);
	            
	         HashMap<String, String> artworksLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToArtworkLookupTable(artworksResponse);
				@SuppressWarnings("rawtypes")
				Iterator artworkIterator = artworksLookupTable.keySet()
						.iterator();
				while (artworkIterator.hasNext()) {
					artworkArrayList.add(artworkIterator.next().toString());				
				}
				artworksList.setArtworks(artworkArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return artworksList;
	}

	public ComplianceList getComplianceList() {
		ComplianceList complianceList=new ComplianceList();
		List<String> complianceArrayList = new ArrayList<>();
		try{
		LinkedList<?> compliancesResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.PRODUCT_COMPLIANCECERTS_LOOKUP), LinkedList.class);
	            
	         HashMap<String, String> complianceLookupTable = (HashMap<String, String>) JsonToLookupTableConverter.jsonToComplianceCertLookupTable(compliancesResponse);
				@SuppressWarnings("rawtypes")
				Iterator complianceIterator = complianceLookupTable.keySet()
						.iterator();
				while (complianceIterator.hasNext()) {
					complianceArrayList.add(complianceIterator.next().toString());				
				}
				complianceList.setCompliances(complianceArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return complianceList;
	}
	public DiscountRatesList getDiscountList() {
		DiscountRatesList discountList=new DiscountRatesList();
		List<String> discountArrayList = new ArrayList<>();
		try{
		LinkedList<?> discountsResponse = lookupRestTemplate.getForObject(
	            RestAPIProperties.get(ApplicationConstants.DISCOUNT_RATES_LOOKUP_URL), LinkedList.class);
	            
	         HashMap<String, DiscountRate> discountLookupTable = (HashMap<String,DiscountRate>) JsonToLookupTableConverter.jsonToDiscountLookupTable(discountsResponse);
				@SuppressWarnings("rawtypes")
				Iterator discountIterator = discountLookupTable.keySet()
						.iterator();
				while (discountIterator.hasNext()) {
					discountArrayList.add((discountLookupTable.get(discountIterator.next())).getCode());	
					discountIterator.remove();
				}
				discountList.setDiscountRates(discountArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return discountList;
	}
	public CurrencyList getCurrenciesList() {
		CurrencyList currencyList=new CurrencyList();
		List<String> currencyArrayList = new ArrayList<>();
		try{
		 String currenciesResponse = JersyClientGet.getLookupsResponse(RestAPIProperties
                 .get(ApplicationConstants.CURRENCIES_LOOKUP_URL));
	         Map<String, Currency> currencyLookupTable = (Map<String, Currency>) JsonToLookupTableConverter.jsonToCurrencyLookupTable(currenciesResponse);
				@SuppressWarnings("rawtypes")
				Iterator currencyIterator = currencyLookupTable.keySet()
						.iterator();
				while (currencyIterator.hasNext()) {
					currencyArrayList.add((currencyLookupTable.get(currencyIterator.next())).getName());
				}
				currencyList.setCurrencies(currencyArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return currencyList;
	}

	public CriteriaCodesList getCriteriaCodesList() {
		CriteriaCodesList criteriaCodesList=new CriteriaCodesList();
		List<String> criteriaCodesArrayList = new ArrayList<>();
		try{
			LinkedList<?> criteriaCodesResponse = lookupRestTemplate.getForObject(
		            RestAPIProperties.get(ApplicationConstants.CRITERIA_INFO_URL), LinkedList.class);
	         ConcurrentHashMap<String, String> criteriaCodesLookupTable = (ConcurrentHashMap<String, String>) JsonToLookupTableConverter.jsonToProductCriteriaListLookupTable(criteriaCodesResponse);
				@SuppressWarnings("rawtypes")
				Iterator criteriaCodeIterator = criteriaCodesLookupTable.keySet()
						.iterator();
				while (criteriaCodeIterator.hasNext()) {
					criteriaCodesArrayList.add(criteriaCodeIterator.next().toString());
				}
				criteriaCodesList.setCriteriaCodes(criteriaCodesArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return criteriaCodesList;
	}

	public UpchargeTypesList getUpchargeTypes() {
		UpchargeTypesList upchargeTypesList=new UpchargeTypesList();
		UpChargeLookup upchargeLookup=new UpChargeLookup();
		List<String> upchargesArrayList = new ArrayList<>();
		try{
	         ConcurrentHashMap<String, String> upchargesLookupTable = upchargeLookup.getUpchargeTypes(ApplicationConstants.PRICING_SUBTYPECODE_LOOKUP);
				@SuppressWarnings("rawtypes")
				Iterator upchargeIterator = upchargesLookupTable.keySet()
						.iterator();
				while (upchargeIterator.hasNext()) {
					upchargesArrayList.add(upchargesLookupTable.get(upchargeIterator.next().toString()));
				}
				upchargeTypesList.setUpchargeTypes(upchargesArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return upchargeTypesList;
	}
	
	public UpchargeLevelsList getUpchargeLevels() {
		UpchargeLevelsList upchargeLevelsList=new UpchargeLevelsList();
		UpChargeLookup upchargeLookup=new UpChargeLookup();
		List<String> upchargesArrayList = new ArrayList<>();
		try{
	         ConcurrentHashMap<String, String> upchargesLookupTable = upchargeLookup.getUpchargeLevels(ApplicationConstants.PRICING_USAGELEVEL_LOOKUP);
				@SuppressWarnings("rawtypes")
				Iterator upchargeIterator = upchargesLookupTable.keySet()
						.iterator();
				while (upchargeIterator.hasNext()) {
					upchargesArrayList.add(upchargesLookupTable.get(upchargeIterator.next().toString()));
				}
				upchargeLevelsList.setUpchargeLevels(upchargesArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return upchargeLevelsList;
	}
	
	public PriceModifiers getPriceModifiers() {
		PriceModifiers priceModifiers=new PriceModifiers();
		List<String> priceUnitArrayList=new ArrayList<>();
		try{
			LinkedList<?> priceUnitsResponse = lookupRestTemplate.getForObject(
		            RestAPIProperties.get(ApplicationConstants.PRICE_UNIT_LOOKUP_URL), LinkedList.class);
		         Map<String, PriceUnitJsonModel > priceUnitLookupTable = (Map<String, PriceUnitJsonModel>) JsonToLookupTableConverter.jsonToPriceUnitLookupTable(priceUnitsResponse);
					@SuppressWarnings("rawtypes")
				Iterator priceUnitIterator = priceUnitLookupTable.keySet()
						.iterator();
				while (priceUnitIterator.hasNext()) {
					
					priceUnitArrayList.add(priceUnitLookupTable.get(priceUnitIterator.next().toString()).getDisplayName());
				}
				priceModifiers.setPricemodifiers(priceUnitArrayList);
			}catch(Exception ex){
				_LOGGER.info(ex.getMessage());
			}
	         return priceModifiers;
	}
	
}
