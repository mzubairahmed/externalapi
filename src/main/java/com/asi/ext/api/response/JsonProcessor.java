package com.asi.ext.api.response;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.asi.ext.api.rest.JersyClientGet;
import com.asi.ext.api.radar.model.Currency;
import com.asi.ext.api.radar.model.PriceUnit;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.lookup.model.PriceUnitJsonModel;
import com.asi.ext.api.radar.lookup.model.SetCodeValueJsonModel;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.RestAPIProperties;

/**
 * JsonProcessor contains function which related to JSON processing and Lookups. Methods including
 *
 *<ul><li><b>JSON Processing </b>
 * <ul>  
 *  <li> JSON String to Map 
 *  <li> Map to JSON String
 *  <li> JSON String to Object
 *  <li> Object to JSON String 
 *  <li> JSON String to Bean Class instance
 *  <li> Bean Class to JSON String 
 * </ul>
 * <li> <b>Lookup (Searching Key/Value Pair)</b>
 *  <ul>
 *      <li>Color key/value pair searching
 *      <li> Orgin key/value pair searching
 *      <li> Currency key/value pair searching
 *      <li> Shape key/value pair searching
 *      <li> Size key/value pair searching
 *      <li> Imprint key/value pair searching
 *      <li> Price code checking
 *      <li> etc...
 *  </ul>
 *</ul>
 *
 * 
 * @author Shravan Kumar, Murali Ede, Rahul K
 * @category JSON and Lookup
 * @version 1.5
 */
public class JsonProcessor {
    /**
     * LOGGER variable to use print logs for debugging purpose
     */
	private final static Logger LOGGER = Logger.getLogger(JsonProcessor.class .getName());
	public static boolean isCustomValue    = false;
	
	public static Product convertJsonToProduct(String productJson) {
	    try {
	        ObjectMapper mapper = new ObjectMapper();
    	    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            
            Product product = mapper.readValue(productJson, Product.class);
            
            return product;
	    } catch (Exception e) {
	        LOGGER.error("Exception while converting product com.asi.util.json to Product model ", e);
	        return null;
	    }
	}
	
	/**
	 * Gets all orgins from given JSON string
	 * 
	 * @param jsonText contains data in JSON string format which we need to convert a {@linkplain LinkedList} of Orgins
	 * @return a {@linkplain LinkedList} which contains all orgins or null if jsonText is not parsable
	 */
	public LinkedList<?> getAllOrigins(String jsonText) {
		LinkedList<?> finalList = null;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			finalList = (LinkedList<?>) parser
					.parse(jsonText, containerFactory);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return finalList;
	}

    /**
     * Search for a particular <b>Value</b> in given JSON string. 
     * If a match found the return it corresponding <b>Key (criteria code) </b>
     * 
     * @param jsonText contains data in JSON string format
     * @param srchValue is the value we need to search in the JSON
     * @return criteriaCode of the matched Value or <b>NULL</b> if no match found
     */
	public String checkValueKeyPair(String jsonText, String srchValue, String criteriaSetCode) {
		String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
				if (crntValue != null) {
    				if (!srchValue.equals("")&&srchValue.trim().equalsIgnoreCase(crntValue.get(ApplicationConstants.CONST_STRING_VALUE)))
    					{
    					criteriaCode = String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY));
    					JsonProcessor.isCustomValue = false;
    					break;
    					}
    				else if (criteriaSetCode.equalsIgnoreCase(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE) &&  crntValue.get(ApplicationConstants.CONST_STRING_VALUE) != null && crntValue.get(ApplicationConstants.CONST_STRING_VALUE).equalsIgnoreCase(ApplicationConstants.CONST_STRING_CUSTOM)) {
    				    criteriaCode=String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY));
    				    JsonProcessor.isCustomValue = true;
    				} else 
    					if(crntValue.get(ApplicationConstants.CONST_STRING_VALUE) != null && crntValue.get(ApplicationConstants.CONST_STRING_VALUE).equalsIgnoreCase(ApplicationConstants.CONST_STRING_OTHER)) {
    						criteriaCode=String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY));
    						JsonProcessor.isCustomValue = true;
    				}
				}
			}
			//LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return criteriaCode;
	}
	
	/**
     * Search for a particular <b>Value</b> in given JSON string. 
     * If a match found the return it corresponding <b>Key (criteria code) </b>
     * 
     * @param jsonText contains data in JSON string format
     * @param srchValue is the value we need to search in the JSON
     * @return criteriaCode of the matched Value in Long format or <b>NULL</b> if no match found
     */
	public String checkLongKeyPair(String jsonText, String srchValue) {
		String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, ?> crntValue = (LinkedHashMap<String, ?>) iter
						.next();
				if (!srchValue.equals("")&&srchValue.trim().equalsIgnoreCase(crntValue.get(ApplicationConstants.CONST_STRING_VALUE).toString()))
					{
					criteriaCode = String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY));
					break;
					}
				else
					if(crntValue.get(ApplicationConstants.CONST_STRING_VALUE).toString().equalsIgnoreCase(ApplicationConstants.CONST_STRING_OTHER))
						criteriaCode=String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY));
			}
			//LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return criteriaCode;
	}
	/**
	 * Search for a particular <b>Color Value</b> in given JSON string. 
     * If a match found then we find the corresponding Group and find the matched ID for the Color
	 * @param jsonText is the JSON string contains all the lookup data
	 * @param srchValue is the Color we need to search 
	 * @return ID of given color from lookup values or NULL
	 */
	public String checkColorValueKeyPair(String jsonText, String srchValue) {
	    //return getColorSetCodeValueId(srchValue);
	    return null;
	}
	
	ProductDataStore dataStore = new ProductDataStore();
	
    /**
     * Search for a particular <b>Currency</b> in given JSON string. 
     * If a match found then we return parsed {@linkplain Currency} object with required values set
     * Currency search criteria must be in Currency Code ( USD, EUR, INR, etc...) format
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Color we need to search 
     * @return {@link Currency} or Empty {@link Currency} object
     */
	public Currency checkCurrencyValueKeyPair(String externalProductId, String jsonText, String srchValue) { 
		// LOGGER.info("JsonTxt"+jsonText);
		// String currncyCode="NON";
		if(srchValue.trim().isEmpty() 
		        || srchValue.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL) 
		        || srchValue.trim().equalsIgnoreCase("NON")) {
		    //ProductParser.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD, "Currency code empty for Pricing");
		    srchValue="USD";
		}
		Currency currency = new Currency();
		// String returnValue="NON";
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		boolean matchFound = false;
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();

			while (iter.hasNext()) {
				@SuppressWarnings({  "rawtypes" })
				LinkedHashMap crntValue = (LinkedHashMap) iter.next();
				// LOGGER.info.println(crntValue.get("Name")+"==iterate result=="+crntValue);
				// srchValue="NON";
				if (srchValue.equalsIgnoreCase(crntValue.get("Code").toString())) {
					currency.setCode(crntValue.get("Code").toString());
					currency.setName(crntValue.get("Name").toString());
					currency.setIsActive(crntValue.get("IsActive").toString());
					return currency;
				}
			}
			if (!matchFound) {
			    LOGGER.warn("Ext-PRD-ID : "+externalProductId+", Invalid Currency Code " +srchValue+" for Pricing");
			    dataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Currency Code " +srchValue+" for Pricing");
			}
			// LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
			return currency;
		} catch (ParseException pe) {
			LOGGER.error("Exception while processing Currency JSON : \n"+jsonText, pe);
		}
		return currency;
	}
	public String checkCurrencyValueKeyPair(String externalProductId, LinkedList<?> jsonList, String srchValue) { 
		// LOGGER.info("JsonTxt"+jsonText);
		 String currncyCode="NON";
		if(srchValue.trim().isEmpty() 
		        || srchValue.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL) 
		        || srchValue.trim().equalsIgnoreCase("NON")) {
		    //ProductParser.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD, "Currency code empty for Pricing");
		    srchValue="USD";
		}
		Currency currency = new Currency();
		// String returnValue="NON";
		
		boolean matchFound = false;
		try {
		
			Iterator<?> iter = jsonList.iterator();// entrySet().iterator();

			while (iter.hasNext()) {
				@SuppressWarnings({  "rawtypes" })
				LinkedHashMap crntValue = (LinkedHashMap) iter.next();
				// LOGGER.info.println(crntValue.get("Name")+"==iterate result=="+crntValue);
				// srchValue="NON";
				if (srchValue.equalsIgnoreCase(crntValue.get("Code").toString())) {
					currncyCode=crntValue.get("Code").toString();
					/*currency.setCode(crntValue.get("Code").toString());
					currency.setName(crntValue.get("Name").toString());
					currency.setIsActive(crntValue.get("IsActive").toString());*/
					return crntValue.get("Code").toString();
				}
			}
			if (!matchFound) {
			    LOGGER.warn("Ext-PRD-ID : "+externalProductId+", Invalid Currency Code " +srchValue+" for Pricing");
			    dataStore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Currency Code " +srchValue+" for Pricing");
			}
			// LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
			return currncyCode;
		} catch (Exception pe) {
			LOGGER.error("Exception while processing Currency JSON : \n"+jsonList, pe);
		}
		return currncyCode;
	}
    /**
     * Search for a particular <b>Discount Value</b> in given JSON string. 
     * If a match found then we find the corresponding Discount Group then return data in a format like this <br /> 
     * srchValue#{Code}#{DiscountPercentage}#{IndustryDiscountCode}#{Description}
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Discount value we need to search 
     * @return the discount code and its details in a special format srchValue#{Code}#{DiscountPercentage}#{IndustryDiscountCode}#{Description}
     */
	public String checkDiscountValueKeyPair(String jsonText, String srchValue, String externalPrdId, boolean isBasePrice) {
	    String tempDiscountValue = srchValue;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			boolean validDiscountCode = false;
			String temp = "";
			while (iter.hasNext()) {
				@SuppressWarnings({  "rawtypes" })
				LinkedHashMap crntValue = (LinkedHashMap) iter.next();
				// LOGGER.info(crntValue.get("Name")+"==iterate result=="+crntValue);
				// srchValue="NON";
				srchValue = "";
				if (tempDiscountValue.trim().toUpperCase().equalsIgnoreCase(crntValue.get(
						"IndustryDiscountCode").toString())) {
					srchValue = crntValue.get("Code").toString();
					srchValue += "#"
							+ crntValue.get("DiscountPercent").toString();
					srchValue += "#"
							+ crntValue.get("IndustryDiscountCode").toString();
					srchValue += "#" + crntValue.get("Description").toString();
					// LOGGER.info("in loop"+srchValue);
					// "Description":"Discount","DiscountPercent":0.70,"IndustryDiscountCode":"L"
					validDiscountCode = true;
					break;
				}
				else
					if( !isBasePrice && crntValue.get(
						"IndustryDiscountCode").toString().equalsIgnoreCase("z"))
					{
						temp = crntValue.get("Code").toString();
						temp += "#"
								+ crntValue.get("DiscountPercent").toString();
						temp += "#"
								+ crntValue.get("IndustryDiscountCode").toString();
						temp += "#" + crntValue.get("Description").toString();
					}
			}
			// LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
			if (!validDiscountCode) {
			    dataStore.addErrorToBatchLogCollection(externalPrdId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid discount code "+tempDiscountValue);
			    return temp;
			}
			return srchValue;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return srchValue;
	}
    /**
     * Search for a particular <b>Product Category</b> in given JSON string. 
     * If a match found then we find the corresponding Group and find the matched Criteria ID for the <b>Category</b>
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Category need to search 
     * @return ID of given Category from lookup values or NULL
     */
	public String checkProductCategoryKeyPair(String jsonText, String srchValue) {
		String returnValue = ApplicationConstants.CONST_STRING_NULL_CAP;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();

			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				LinkedHashMap crntValue = (LinkedHashMap) iter.next();
				// LOGGER.info(crntValue.get("Name")+"==iterate result=="+crntValue);
				// srchValue="NON";
				srchValue=srchValue.trim();
				if (srchValue
						.equalsIgnoreCase(crntValue.get("Name").toString())) {
					returnValue = crntValue.get("Code").toString();
					// LOGGER.info("in loop"+srchValue);
					return returnValue;
				}
			}
			// LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
			return returnValue;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return returnValue;
	}
    /**
     * Search for a particular <b>Material</b> Criteria Code in given JSON string. 
     * If a match found then we find the corresponding Group and find the matched Criteria ID for the <b>Material</b>
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Material name need to search 
     * @return criteria code ID of given Material from lookup values or NULL
     */
	public String checkMaterialValueKeyPair(String jsonText, String srchValue) {
		String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
		srchValue = srchValue.toLowerCase();
		srchValue = srchValue.substring(0, 1).toUpperCase()
				+ srchValue.substring(1);
		//srchValue = "Fabric-" + srchValue;
		// LOGGER.info("Search Value in Json Processor"+srchValue);
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			String otherCriteriaValue=ApplicationConstants.CONST_STRING_OTHER;
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			boolean nextCriteriaCodeCheck=true;
			while (nextCriteriaCodeCheck && iter.hasNext()) {
				Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
				// LOGGER.info("INitial List"+crntValue);
				if (String.valueOf(crntValue.get("DisplayName")).trim().equalsIgnoreCase(srchValue.trim())) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

					
						// LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
						@SuppressWarnings("rawtypes")
						List finalLst = (LinkedList) codeValueGrpsMap
								.get("SetCodeValues");
						@SuppressWarnings("rawtypes")
						Iterator finalItr = finalLst.iterator();
						if (finalItr.hasNext()) {
							@SuppressWarnings("rawtypes")
							Map finalMap = (LinkedHashMap) finalItr.next();
							// LOGGER.info("ID:"+finalMap.get("ID"));
							criteriaCode = String.valueOf(finalMap.get("ID"));
							nextCriteriaCodeCheck=false;
						}
					}
			
				}
				else if(crntValue.get("DisplayName").toString()
						.equalsIgnoreCase(otherCriteriaValue))
				
				{
					@SuppressWarnings({ "rawtypes", "unchecked" })
					LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
							.get("CodeValueGroups");
					@SuppressWarnings("rawtypes")
					Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

					while (iterator.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

						
							// LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
							@SuppressWarnings("rawtypes")
							List finalLst = (LinkedList) codeValueGrpsMap
									.get("SetCodeValues");
							@SuppressWarnings("rawtypes")
							Iterator finalItr = finalLst.iterator();
							if (finalItr.hasNext()) {
								@SuppressWarnings("rawtypes")
								Map finalMap = (LinkedHashMap) finalItr.next();
								// LOGGER.info("ID:"+finalMap.get("ID"));
								otherCriteriaValue = String.valueOf(finalMap.get("ID"));
							}
						}
				}
			}
			if(criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL))
				criteriaCode=otherCriteriaValue;
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		return criteriaCode;
	}

    /**
     * Search for a particular <b>Shape Value</b> in given JSON string. 
     * If a match found then we find the corresponding Group and find the matched ID for the Shape
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Shape value need to search 
     * @return Key of given Shape value from lookup values or NULL
     */
	public String checkShapeValueKeyPair(String jsonText, String productShape) {
		String returnValue = ApplicationConstants.CONST_STRING_NULL_CAP;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter
						.next();
				if (productShape.trim().equalsIgnoreCase(crntValue.get(ApplicationConstants.CONST_STRING_VALUE).toString()))
					returnValue = String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY));
			}
			// LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return returnValue;
	}
    /**
     * Search for a particular <b>Theme Value</b> in given JSON string. 
     * If a match found then we find the corresponding Group and find the matched ID for the Theme
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Theme value need to search 
     * @return Key of given Theme value from lookup values or NULL
     */
	public String checkThemeValueKeyPair(String themesWSResponse,
			String crntProdctTheme) {
		{
			String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
			// crntProdctTheme=srchValue.toLowerCase();
			// srchValue=srchValue.substring(0,1).toUpperCase()+srchValue.substring(1);
			// srchValue="Medium "+srchValue;
			// LOGGER.info("Search Value in Json Processor"+srchValue);
			JSONParser parser = new JSONParser();
			ContainerFactory containerFactory = new ContainerFactory() {
				public List<?> creatArrayContainer() {
					return new LinkedList<Object>();
				}

				public Map<?, ?> createObjectContainer() {
					return new LinkedHashMap<Object, Object>();
				}
			};
			try {
				LinkedList<?> json = (LinkedList<?>) parser.parse(
						themesWSResponse, containerFactory);
				Iterator<?> iter = json.iterator();// entrySet().iterator();
				// LOGGER.info("==iterate result==");
				boolean nxtCriteriaCheck=true;
			//	String otherCriteriaCode="NONE";
				while (nxtCriteriaCheck && iter.hasNext()) {
					Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
					// LOGGER.info("INitial List"+crntValue);
					@SuppressWarnings({ "rawtypes", "unchecked" })
					LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
							.get("SetCodeValues");
					@SuppressWarnings("rawtypes")
					Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

					while (iterator.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

						if (codeValueGrpsMap.get("CodeValue").toString()
								.equalsIgnoreCase(crntProdctTheme.trim())) {
							// LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
							criteriaCode = String.valueOf(codeValueGrpsMap.get("ID"));
							nxtCriteriaCheck=false;
						}
						
							
					}
				}
			} catch (Exception pe) {
				pe.printStackTrace();
			}
			return criteriaCode;
		}
	}
    /**
     * Search for a particular <b>Product Keyword</b> in given JSON string. 
     * If a keyword match found then we find the criteria Code for the Product Keyword
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the Keyword of Product value need to search 
     * @return CriteriaCode of given Product Keyword value from lookup values or NULL
     */
	public String checkProductKeywordsKeyPair(String jsonText, String srchValue) {
		String returnValue = ApplicationConstants.CONST_STRING_NULL_CAP;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();

			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				LinkedHashMap crntValue = (LinkedHashMap) iter.next();
				// LOGGER.info(crntValue.get("Name")+"==iterate result=="+crntValue);
				// srchValue="NON";
				if (srchValue
						.equalsIgnoreCase(crntValue.get("Name").toString())) {
					returnValue = crntValue.get("Code").toString();
					// LOGGER.info("in loop"+srchValue);
				}
			}
			// LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
			return returnValue;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return returnValue;
	}

	/**
     * 
     * Gets the parsed size data (in Map format) from sizeResponse JSON
     * 
     * @param sizesWSResponse is the JSON data which contains the size details, criteria details etc.. 
     * @param attribute is the string to lookup for existence  
     * @param DimensionType is the type of dimension
     */
	@SuppressWarnings("rawtypes")
	public HashMap getSizesResponse(String sizesWSResponse, String attribute,String DimensionType) {
		DimensionType=DimensionType.trim();
		attribute=attribute.trim();
		HashMap returnValue = null;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(sizesWSResponse,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();

			while (iter.hasNext()) {
				LinkedHashMap sizesData = (LinkedHashMap) iter.next();
				if(attribute.contains("Apparel") || attribute.equalsIgnoreCase("Size-Other"))
				{
					if (sizesData.get("DisplayName")
							.toString().contains(attribute) 
							&& DimensionType.equalsIgnoreCase(sizesData
									.get("Code").toString())) {
						returnValue = sizesData;
					}
				}
				else if (attribute.equalsIgnoreCase(sizesData.get("DisplayName")
						.toString())
						&& DimensionType.equalsIgnoreCase(sizesData
								.get("CriteriaCode").toString())) {
					returnValue = sizesData;
					// LOGGER.info("in loop"+returnValue);
				}
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * Finds the value of a given size element from sizeElementResponses
	 * 
	 * @param elementName is the type of size value like Dimension, Weight, etc...
	 * @param sizeElementsResponse contains all the size related details 
	 * @param attribute is the value
	 * @return Criteria code of the give size value
	 */
	public String getSizesElementValue(String elementName,
			@SuppressWarnings("rawtypes") HashMap sizeElementsResponse, String attribute) {
		attribute=attribute.trim();
		elementName=elementName.trim();
		String ElementValue = "";
		try
		{
			
			if (null!=sizeElementsResponse && elementName.equalsIgnoreCase("Units")) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) sizeElementsResponse
						.get("UnitsOfMeasure");
				// LOGGER.info("Units of Measure: "+codeValueGrps);
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					// LOGGER.info(codeValueGrpsMap.toString());
					if (codeValueGrpsMap.get("Format").toString()
							.equalsIgnoreCase(attribute)) {
						ElementValue = (String) codeValueGrpsMap.get("Code");
					}
				}
			} else if (null!=sizeElementsResponse && elementName.equalsIgnoreCase("id")) {
				ElementValue = sizeElementsResponse.get("ID").toString();
			}else
				if(null!=sizeElementsResponse &&  elementName.equalsIgnoreCase("CRITERIASETID"))
				{
				//	LOGGER.info(sizeElementsResponse);
					@SuppressWarnings("unused")
					String criteriaItem=sizeElementsResponse.get("CriteriaItem").toString();
					@SuppressWarnings("rawtypes")
					LinkedHashMap criteriaItemGrps = (LinkedHashMap) sizeElementsResponse
							.get("CriteriaItem");
				//	 LOGGER.info("Units of Measure: "+criteriaItemGrps);
					
				 @SuppressWarnings("rawtypes")
				List finalLst = (LinkedList) criteriaItemGrps.get("CodeValueGroups");
				@SuppressWarnings({ "rawtypes", "unchecked" })
				Iterator<LinkedHashMap> iterator = finalLst.iterator();
				boolean gotElementValue=false;
					while (iterator.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					//	 LOGGER.info("Before Loop:"+codeValueGrpsMap.toString());
						 @SuppressWarnings("rawtypes")
						LinkedList setCodeValues=(LinkedList)codeValueGrpsMap.get("SetCodeValues");
						 @SuppressWarnings("rawtypes")
						Iterator setCodeValuesItr=setCodeValues.iterator();
						 while(setCodeValuesItr.hasNext())
						 {
							 @SuppressWarnings("rawtypes")
							LinkedHashMap finalSetCode=(LinkedHashMap)setCodeValuesItr.next();							 
							 if(finalSetCode.get("CodeValue").toString().contains(ApplicationConstants.CONST_STRING_OTHER))
							 {
								// LOGGER.info(finalSetCode);
								 ElementValue=String.valueOf(finalSetCode.get("ID"));
								 gotElementValue=true;
								 break;
							 }
							 if(gotElementValue)
								 break;
						 }
						
						 if(gotElementValue)
							 break;
					
				}
				}
				else if(elementName.equalsIgnoreCase("CODEVALUE"))
				{
					
				
				}

		}
		catch(Exception Ex)
		{
			Ex.printStackTrace();
		}
		return ElementValue;
	}

	/**
	 * Find the criteria code of a Give Size key by looking up into sizeWSResponse ( which contains all the size related informations ). 
	 * @param sizesWSResponse is the map data which contains all the information about size/units
	 * @param curntCriteria 
	 * @param sizeGroup
	 * @return
	 */
	public String checkSizesKeyValuePair(String sizesWSResponse,
			String curntCriteria,String sizeGroup) {
		String criteriaCode=ApplicationConstants.CONST_STRING_NULL_CAP;
		sizeGroup=sizeGroup.trim();
		curntCriteria=curntCriteria.trim();
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(
					sizesWSResponse, containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
				// LOGGER.info("INitial List"+crntValue);
				if(crntValue.get("Code").equals(sizeGroup))
				{
					
				@SuppressWarnings({ "rawtypes", "unchecked" })
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					@SuppressWarnings({ "unchecked", "rawtypes" })
					LinkedList<LinkedHashMap> setCodeValueGrps = (LinkedList<LinkedHashMap>) codeValueGrpsMap
							.get("SetCodeValues");
					@SuppressWarnings("rawtypes")
					Iterator<LinkedHashMap> iterator1 = setCodeValueGrps.iterator();
					while(iterator1.hasNext())
					{
						@SuppressWarnings("rawtypes")
						Map finalCodeValueMap=iterator1.next();
					//	LOGGER.info();
					if (finalCodeValueMap.get("CodeValue").toString()
							.equalsIgnoreCase(curntCriteria)) {
						// LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
						criteriaCode = String.valueOf(finalCodeValueMap.get("ID"));
					}
					}
				}
				}
			}
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		return criteriaCode;

	}
    /**
	 * Find the criteria code of a Give Size key by looking up into sizeWSResponse ( which contains all the size related informations ). 
	 * @param sizesWSResponse is the map data which contains all the information about size/units
	 * @param curntCriteria 
	 * @param sizeGroup
	 * @return
	 */
	public String checkOtherSizesKeyValuePair(String sizesWSResponse,
			String curntCriteria,String sizeGroup) {
		String criteriaCode=ApplicationConstants.CONST_STRING_NULL_CAP;
		sizeGroup=sizeGroup.trim();
		curntCriteria=curntCriteria.trim();
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(
					sizesWSResponse, containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
				// LOGGER.info("INitial List"+crntValue);
				if(crntValue.get("Code").equals(sizeGroup))
				{
					
				@SuppressWarnings({ "rawtypes", "unchecked" })
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
					@SuppressWarnings({ "unchecked", "rawtypes" })
					LinkedList<LinkedHashMap> setCodeValueGrps = (LinkedList<LinkedHashMap>) codeValueGrpsMap
							.get("SetCodeValues");
					@SuppressWarnings("rawtypes")
					Iterator<LinkedHashMap> iterator1 = setCodeValueGrps.iterator();
					while(iterator1.hasNext())
					{
						@SuppressWarnings("rawtypes")
						Map finalCodeValueMap=iterator1.next();
					//	LOGGER.info();
					if (finalCodeValueMap.get("CodeValue").toString()
							.contains(ApplicationConstants.CONST_STRING_OTHER)) {
						// LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
						criteriaCode = String.valueOf(finalCodeValueMap.get("ID"));
					}
					}
				}
				}
			}
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		return criteriaCode;

	}
    /**
     * Search for a particular <b>Imprint Value</b> in given JSON string. 
     * If a imprint value match found then we find the criteria Code for that Imprint Value
     * @param jsonText is the JSON string contains all the lookup data
     * @param srchValue is the ImprintValue value need to search 
     * @return CriteriaCode of given Imprint value value from lookup values or NULL
     */
	public String checkImprintValueKeyPair(String jsonText, String srchValue) {
		String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// System.out.println("==iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings({ "rawtypes" })
				LinkedHashMap crntValue = (LinkedHashMap) iter
						.next();
				if (srchValue.trim().equalsIgnoreCase(crntValue.get("CodeValue").toString()))
					criteriaCode = String.valueOf(crntValue.get("ID"));
			}
			// System.out.println(JSONValue.toJSONString(com.asi.util.json));
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return criteriaCode;
	}
	/**
	 * Find a ImprintArtwork key value pair from the product criteria JSON based on criteriaCode and searchValue
	 * 
	 * @param jsonText is the JSON data which contains all the key value pair information in JSON format
	 * @param srchValue is the searchCriteria
	 * @param criteriaCode is the criteriaCode of ImprintArtwork
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String checkImprintArtWorkValueKeyPair(String jsonText, String srchValue,String criteriaCode) {
		//String criteriaCode = "NULL";
		srchValue = srchValue.toLowerCase();
		srchValue = srchValue.substring(0, 1).toUpperCase()
				+ srchValue.substring(1);
		//srchValue = "Fabric-" + srchValue;
		 LOGGER.info("Search Value in Json Processor "+srchValue);
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
				// LOGGER.info("INitial List"+crntValue);
				if(crntValue.get("Code").toString().equalsIgnoreCase(criteriaCode.trim()))
				{
				@SuppressWarnings("rawtypes")
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

					
						@SuppressWarnings("rawtypes")
						List finalLst = (LinkedList) codeValueGrpsMap
								.get("SetCodeValues");
						@SuppressWarnings("rawtypes")
						Iterator finalItr = finalLst.iterator();
						while (finalItr.hasNext()) {
							@SuppressWarnings("rawtypes")
							Map finalMap = (LinkedHashMap) finalItr.next();
							if(finalMap.get("CodeValue").toString().equalsIgnoreCase(srchValue.trim()))
							criteriaCode = String.valueOf(finalMap.get("ID"));
							// LOGGER.info("ID:"+finalMap.get("ID"));
						}
					
					//break;
				}
				//break;
			}
			}
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		return criteriaCode;
	}

	/**
	 * Check mediaCitation ID in the mediaCitaion lookup response. If a ID match found will return the corresponding criteria code 
	 * @param mediaCitationsWSResponse is the mediaCitation response we received from mediaCitation lookup.
	 * @param mediaCitationId is the mediaCitationId have to lookup
	 * @return criteriaCode of give mediaCitaion
	 */
	public String checkMediaCitationId(String mediaCitationsWSResponse,
			String mediaCitationId) {
	    String citationId=ApplicationConstants.CONST_STRING_NULL_CAP;
		String citationReferenceId=ApplicationConstants.CONST_STRING_NULL_CAP;
		String[] mediaCitationArray = new String [2];
		boolean isURL = false;
		if (mediaCitationId != null && !mediaCitationId.trim().isEmpty() 
		        && !mediaCitationId.trim().toLowerCase().startsWith(ApplicationConstants.CONST_STRING_HTTP) && mediaCitationId.contains(":")) {
		    mediaCitationArray[0] = mediaCitationId.split(":")[0];
		    mediaCitationArray[1] = mediaCitationId.split(":")[1];
		} else if (mediaCitationId.toLowerCase().startsWith(ApplicationConstants.CONST_STRING_HTTP) 
		        || mediaCitationId.toLowerCase().startsWith(ApplicationConstants.CONST_STRING_WWW)) {
		    mediaCitationArray[0] = mediaCitationId.split(":")[0] +":"+ mediaCitationId.split(":")[1];
		    isURL = true;
		} else {
		    mediaCitationArray[0] = mediaCitationId;
            mediaCitationArray[1] = null;
		}
		// LOGGER.info("Search Value in Json Processor"+srchValue);
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(mediaCitationsWSResponse,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
				// LOGGER.info("INitial List"+crntValue);
				if(crntValue.get("Name").toString().equalsIgnoreCase(mediaCitationArray[0].trim()) || (isURL && crntValue.get("Url").toString().equalsIgnoreCase(mediaCitationArray[0].trim())))
				{
					citationId=String.valueOf(crntValue.get("ID"));
					if (mediaCitationArray[1] == null) {
					    break;
					}
					@SuppressWarnings("rawtypes")
					List mediaCitationReferences = (LinkedList) crntValue
							.get("MediaCitationReferences");
					@SuppressWarnings("rawtypes")
					Iterator finalItr = mediaCitationReferences.iterator();
					while(finalItr.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map finalMap = (LinkedHashMap) finalItr.next();
						// LOGGER.info("ID:"+finalMap.get("ID"));
						if ( finalMap.get("Number").equals(mediaCitationArray[1]))
						{
						    citationReferenceId = String.valueOf(finalMap.get("ID"));
						    break;
						}
					}
					break;
					//citationReferenceId=mediaCitationReferences.get("ID");
				}
			
			}
		}catch(Exception ex)
		{
			//LOGGER.error(ex);
			ex.printStackTrace();
		}
	
		
		return citationId+":"+citationReferenceId;
	}
	/**
	 * Find the criteria code for a particular pricing subtype, criteria code is searched in the
	 * pricingSubType response
	 * @param pricingSubTypeCodeWSResponse is webservice response received from the lookup API
	 * @param pricingSubTypeCode is the sub type code of the price
	 * @return Criteria code of the price
	 */
	public String checkPricingCode(String pricingSubTypeCodeWSResponse,
			String pricingSubTypeCode) {
			String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
			JSONParser parser = new JSONParser();
			ContainerFactory containerFactory = new ContainerFactory() {
				public List<?> creatArrayContainer() {
					return new LinkedList<Object>();
				}

				public Map<?, ?> createObjectContainer() {
					return new LinkedHashMap<Object, Object>();
				}
			};
			try {
				LinkedList<?> json = (LinkedList<?>) parser.parse(pricingSubTypeCodeWSResponse,
						containerFactory);
				Iterator<?> iter = json.iterator();// entrySet().iterator();
				// LOGGER.info("==iterate result==");
				while (iter.hasNext()) {
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter
							.next();
					if (!pricingSubTypeCode.equals("") && crntValue.get("DisplayName").contains(pricingSubTypeCode.trim()))
					{
						criteriaCode = String.valueOf(crntValue.get("Code"));
						break;
					}else if(crntValue.get("DisplayName").contains(ApplicationConstants.CONST_STRING_OTHER))
						criteriaCode=String.valueOf(crntValue.get("Code"));
					
				}
				
				//LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
	
		return criteriaCode;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public ConcurrentHashMap<String, SetCodeValueJsonModel> getSetCodeValuesForIndividualCriteriaCode(String jsonText, String criteriaCode) {
	    ConcurrentHashMap<String, SetCodeValueJsonModel> setCodeValueCollection = null;
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };
        try {
            LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText, containerFactory);
            Iterator<?> iter = json.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if(crntValue.get("Code").toString().equalsIgnoreCase(criteriaCode.trim()))
                {
                LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                while (iterator.hasNext()) {
                    Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
                    List finalLst = (LinkedList) codeValueGrpsMap.get("SetCodeValues");
                    setCodeValueCollection = convertSetCodeValueMapToObject(finalLst);
                }
            }
            }
        } catch (Exception pe) {
            LOGGER.error("Exception occured while processing RUSH Time SetCodeValue JSON", pe);
        }
        return setCodeValueCollection;
    }
	
	@SuppressWarnings("rawtypes")
    private ConcurrentHashMap convertSetCodeValueMapToObject(List setCodeValuesList) {
	    ConcurrentHashMap<String, SetCodeValueJsonModel> setCodeValueCollection = new ConcurrentHashMap<String, SetCodeValueJsonModel>();
        Iterator finalItr = setCodeValuesList.iterator();
        while (finalItr.hasNext()) {
            Map finalMap = (LinkedHashMap) finalItr.next();
            if (finalMap != null && !finalMap.isEmpty()) {
                SetCodeValueJsonModel setCodeValueJsonModel = new SetCodeValueJsonModel();
                setCodeValueJsonModel.setCodeValue(String.valueOf(finalMap.get("CodeValue")));
                setCodeValueJsonModel.setId(String.valueOf(finalMap.get("ID")));
                setCodeValueJsonModel.setDisplaySequence(String.valueOf(finalMap.get("DisplaySequence")));
                setCodeValueJsonModel.setSupplierSpecific(Boolean.parseBoolean(String.valueOf(finalMap.get("IsSupplierSpecific"))));
                setCodeValueCollection.put(setCodeValueJsonModel.getCodeValue(), setCodeValueJsonModel);
            }
        }
	    return setCodeValueCollection;
	}
	
 /**
     * 
     * Converts JSON to Bean Object, converted uses JsonAttribute value to map columns if it exits 
     * else try to use the fieldName and type
     * 
     * 
     * @param com.asi.util.json the JSON need to be converted to bean 
     * @param classType the type JSON need be to converted
     * @return new type {@linkplain T} derived from the JSON
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertJsonToBean(String json, Class<?> classType) {

        if (json != null && !json.trim().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (T) mapper.readValue(json, classType);
            } catch (JsonParseException e) {
                LOGGER.error("JSONParseException while deserializing jsonData to Object, ClassType : "
                        + classType.getCanonicalName() + ", JsonData : " + json + " Exception : " + e.getMessage());
            } catch (JsonMappingException e) {
                LOGGER.error("JsonMappingException while deserializing jsonData to Object, ClassType : "
                        + classType.getCanonicalName() + ", JsonData : " + json + " Exception : " + e.getMessage());
            } catch (IOException e) {
                LOGGER.error("IOException while deserializing jsonData to Object, ClassType : " + classType.getCanonicalName()
                        + ", JsonData : " + json + " Exception : " + e.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Creates collection of Specific classType from Json. Default collection implementation is {@link List}
     * @param com.asi.util.json is the com.asi.util.json to process
     * @param classType Generic type of the collection which needs to be returned
     * @return {@link List} of elements of the classType specified
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertJsonToBeanCollection(String json, Class<?> classType) {

        if (json != null && !json.trim().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (T) mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, classType));
            } catch (JsonParseException e) {
                LOGGER.error("JSONParseException while deserializing jsonData to Object, ClassType : "
                        + classType.getCanonicalName() + ", JsonData : " + json + " Exception : " + e.getMessage());
            } catch (JsonMappingException e) {
                LOGGER.error("JsonMappingException while deserializing jsonData to Object, ClassType : "
                        + classType.getCanonicalName() + ", JsonData : " + json + " Exception : " + e.getMessage());
            } catch (IOException e) {
                LOGGER.error("IOException while deserializing jsonData to Object, ClassType : " + classType.getCanonicalName()
                        + ", JsonData : " + json + " Exception : " + e.getMessage());
            }
        } else {
            return null;
        }
        return null;
    }
    /**
     * Converts a Bean into JSON string format 
     * @param bean is the Object which is need to be converted to JSON
     * @return converted string JSON data
     */
    public static String convertBeanToJson(Object bean) {

        ObjectMapper mapper = new ObjectMapper();
        if (bean != null) {
            
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            String parsedResult = null;
            try {
                parsedResult = mapper.writeValueAsString(bean);
            } catch (JsonGenerationException e) {
                LOGGER.error("Unable to Generate JSON from Batch", e);
            } catch (JsonMappingException e) {
                LOGGER.error("Exception while generating JSON from batch data", e);
            } catch (IOException e) {
                LOGGER.error("IOException while BatchData to JSON", e);
            }
            return parsedResult;
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public String checkOptionsKeyValueKeyPair(String jsonText, String srchValue,String criteriaCode) {
		//String criteriaCode = "NULL";
		srchValue = srchValue.toLowerCase();
		srchValue = srchValue.substring(0, 1).toUpperCase()
				+ srchValue.substring(1);
		//srchValue = "Fabric-" + srchValue;
		 LOGGER.info("Search Value in Json Processor"+srchValue);
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List<?> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<?, ?> createObjectContainer() {
				return new LinkedHashMap<Object, Object>();
			}
		};
		try {
			LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
					containerFactory);
			Iterator<?> iter = json.iterator();// entrySet().iterator();
			// LOGGER.info("==iterate result==");
			while (iter.hasNext()) {
				Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
				// LOGGER.info("INitial List"+crntValue);
				if(crntValue.get("Code").toString().equalsIgnoreCase(criteriaCode.trim()))
				{
				@SuppressWarnings("rawtypes")
				LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue
						.get("CodeValueGroups");
				@SuppressWarnings("rawtypes")
				Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

					
						@SuppressWarnings("rawtypes")
						List finalLst = (LinkedList) codeValueGrpsMap
								.get("SetCodeValues");
						@SuppressWarnings("rawtypes")
						Iterator finalItr = finalLst.iterator();
						while (finalItr.hasNext()) {
							@SuppressWarnings("rawtypes")
							Map finalMap = (LinkedHashMap) finalItr.next();
							//if(finalMap.get("CodeValue").toString().equalsIgnoreCase(srchValue.trim()))
							criteriaCode = String.valueOf(finalMap.get("ID"));
							// LOGGER.info("ID:"+finalMap.get("ID"));
						}
					
					//break;
				}
				//break;
			}
			}
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		return criteriaCode;
	}

    public String checkValueKeyPairForOrign(String jsonText, String srchValue) {
        String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };
        try {
            LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText,
                    containerFactory);
            Iterator<?> iter = json.iterator();// entrySet().iterator();
            // LOGGER.info("==iterate result==");
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
                if (crntValue != null) {
                    if (!srchValue.equals("")&&srchValue.trim().equalsIgnoreCase(crntValue.get(ApplicationConstants.CONST_STRING_CODE_VALUE)))
                        {
                            criteriaCode = String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_ID_CAP));
                            break;
                        }
                    /*else
                        if(crntValue.get(ApplicationConstants.CONST_STRING_CODE_VALUE) != null && crntValue.get(ApplicationConstants.CONST_STRING_CODE_VALUE).equalsIgnoreCase(ApplicationConstants.CONST_STRING_OTHER))
                            criteriaCode=String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_ID_CAP));*/
                }
            }
            //LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return criteriaCode;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, String> createColorLookupMap(String colorLookupJson) {
        Map<String, String> colorJsonLookupTable = new HashMap<String, String>();
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };
        
        try {
            LinkedList<?> json = (LinkedList<?>) parser.parse(colorLookupJson, containerFactory);
            colorJsonLookupTable = new HashMap<String, String>(json.size() * 2);
            Iterator<?> iter = json.iterator();
            while (iter.hasNext()) {
                try {
                    LinkedHashMap<?, ?> crntValue = (LinkedHashMap<String, String>) iter.next();
                    if (crntValue != null) {
                        LinkedList<LinkedHashMap<?, ?>> codeValueGroups = (LinkedList<LinkedHashMap<?, ?>>) crntValue.get("CodeValueGroups");
                        if (codeValueGroups != null && !codeValueGroups.isEmpty()) {
                            Iterator<LinkedHashMap<?, ?>> codeValueGrpItr = codeValueGroups.iterator();
                            while (codeValueGrpItr.hasNext()) {
                                LinkedHashMap<?,?> codeValueGroup = (LinkedHashMap<?,?>) codeValueGrpItr.next();
                                LinkedList<LinkedHashMap<?, ?>> setCodeValues = (LinkedList<LinkedHashMap<?, ?>>) codeValueGroup.get("SetCodeValues");
                                if (setCodeValues != null && !setCodeValues.isEmpty()) {
                                    Iterator<LinkedHashMap<?, ?>> setCodeValuesItr = setCodeValues.iterator();
                                    while (setCodeValuesItr.hasNext()) {
                                        LinkedHashMap<?,?> setCodeValueMap = (LinkedHashMap<?,?>) setCodeValuesItr.next();
                                        if (setCodeValueMap != null && !setCodeValueMap.isEmpty()) {
                                            String key = String.valueOf(setCodeValueMap.get(ApplicationConstants.CONST_STRING_CODE_VALUE)).trim();
                                            String value = String.valueOf(setCodeValueMap.get(ApplicationConstants.CONST_STRING_ID_CAP)).trim(); 
                                            colorJsonLookupTable.put(key.toUpperCase(), value);
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception while processing color com.asi.util.json model ",e);
                }
            }
            //LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return colorJsonLookupTable;
    }
    /**
     * Find the PriceUnit object of the given price unit. PriceUnit will find the element from the lookup values
     * @param priceUnit is the price unit we need to find
     * @return matched {@linkplain PriceUnit} or default {@linkplain PriceUnit}
     */
    public static PriceUnit getPriceUnitId(String priceUnit) {
        try {
            if (ProductDataStore.priceUnitCollection == null || ProductDataStore.priceUnitCollection.isEmpty()) {
                ProductDataStore.priceUnitCollection = new HashMap<String, PriceUnitJsonModel>();
                String response = JersyClientGet.getLookupsResponse(RestAPIProperties.get(ApplicationConstants.PRICE_UNIT_LOOKUP_URL));
                
                List<LinkedHashMap<String, String>> priceUnitJsonModelList = convertJsonToBean(response, List.class);
                
                if (priceUnitJsonModelList != null && !priceUnitJsonModelList.isEmpty()) {
                    for (LinkedHashMap<String, String> priceUnitJModel : priceUnitJsonModelList) {
                        PriceUnitJsonModel pUntiJModel = new PriceUnitJsonModel();
                        pUntiJModel.setDescription(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_DESCRIPTION)));
                        pUntiJModel.setID(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_ID_CAP)));
                        pUntiJModel.setDisplayName(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_DISPLAY_NAME)));
                        pUntiJModel.setItemsPerUnit(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_ITEMS_PER_UNIT)));
                        ProductDataStore.priceUnitCollection.put(priceUnitJModel.get(ApplicationConstants.CONST_STRING_DISPLAY_NAME), pUntiJModel);
                    }
                }
            }
            PriceUnitJsonModel priceUnitJsonModel = ProductDataStore.priceUnitCollection.get(priceUnit);
            if (priceUnitJsonModel != null) {
                return priceUnitJsonModel;
            } else {
                priceUnitJsonModel = ProductDataStore.priceUnitCollection.get(ApplicationConstants.CONST_STRING_PIECE);
                return priceUnitJsonModel != null ? priceUnitJsonModel: new PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID, ApplicationConstants.CONST_STRING_PIECE, ApplicationConstants.CONST_STRING_PIECE, "0");
            }
            //return new PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID, ApplicationConstants.CONST_STRING_PIECE, ApplicationConstants.CONST_STRING_PIECE, "0");
        } catch (Exception e) {
            // TODO Log error message && add error to batch
            return new PriceUnitJsonModel(ApplicationConstants.CONST_STRING_PRICE_UNIT_DEFAULT_ID, ApplicationConstants.CONST_STRING_PIECE, ApplicationConstants.CONST_STRING_PIECE, "0");
        }
    }
    
    public static Set<String> getErrorMessageFromJson(String json) {
        
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };
        Set<String> errorMessages = new HashSet<String>();
        try {
            LinkedHashMap<?, ?> errorMessageMap = (LinkedHashMap<?, ?>) parser.parse(json, containerFactory);
            if (errorMessageMap != null && !errorMessageMap.isEmpty()) {
                Iterator<?> jsonListItr = errorMessageMap.values().iterator();
                while (jsonListItr.hasNext()) {
                    String message =  String.valueOf(jsonListItr.next());
                    if (!CommonUtilities.isValueNull(message)) {
                        errorMessages.add(message);
                    }
                }
            } else {
                return errorMessages;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return errorMessages;
    }
    
}
