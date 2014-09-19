package com.asi.ext.api.integration.lookup.parser;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.asi.core.utils.JerseyClient;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.service.model.Artwork;
import com.asi.ext.api.service.model.ImprintMethod;
import com.asi.ext.api.service.model.MinimumOrder;
import com.asi.ext.api.service.model.Option;
import com.asi.ext.api.service.model.Size;
import com.asi.ext.api.service.model.StringValue;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.RestAPIProperties;
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductNumber;
import com.asi.service.product.client.vo.ProductNumberConfiguration;
import com.asi.service.product.client.vo.Relationship;
import com.asi.service.product.client.vo.SelectedLineNames;
import com.asi.service.product.client.vo.parser.ColorLookup;
import com.asi.service.product.client.vo.parser.ImprintLookup;
import com.asi.service.product.client.vo.parser.ImprintSizeLookup;
import com.asi.service.product.client.vo.parser.MaterialLookup;
import com.asi.service.product.client.vo.parser.OptionLookup;
import com.asi.service.product.client.vo.parser.OriginLookup;
import com.asi.service.product.client.vo.parser.PackagingLookup;
import com.asi.service.product.client.vo.parser.ShapeLookup;
import com.asi.service.product.client.vo.parser.SizeLookup;
import com.asi.service.product.client.vo.parser.ThemeLookup;
import com.asi.service.product.client.vo.parser.TradeNameLookup;
import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;

public class LookupParser {
	private CriteriaSetParser criteriaSetParser = new CriteriaSetParser();
	public static RestTemplate lookupRestTemplate;
	private HashMap<String, String> productColorMap = null;
	private HashMap<Integer, String> productShapeMap = null;
	private HashMap<Integer, String> productMaterialMap = null;
	// private HashMap<Integer, String> additionalColorMap = null;
	private HashMap<Integer, String> productThemeMap = null;
	private HashMap<Integer, String> productPackageMap = null;
	private HashMap<Integer, String> productTradeNameMap = null;
	private HashMap<Integer, String> imprintMethodsMap = null;
	private HashMap<Integer, String> imprintArtworkMap = null;
	public static ConcurrentHashMap<String, String> imprintRelationMap = null;
	// private HashMap<String, String> productWarningMap=null;
	public static HashMap<String, String> SamplesElementsResponse = null;
	@SuppressWarnings("rawtypes")
	public static LinkedList<LinkedHashMap> sizeElementsResponse = null;
	@SuppressWarnings("rawtypes")
	public static LinkedList<LinkedHashMap> sizesCriteriaWSResponse = null;
	@SuppressWarnings("rawtypes")
	public static LinkedList<LinkedHashMap> optionElementsResponse = null;
	private static IParser jsonParser = new JSONParserImpl();
	private HashMap<String, String> productOriginMap = null;
	private final static Logger _LOGGER = Logger.getLogger(LookupParser.class
			.getName());
	private ProductDataStore productDataStore = new ProductDataStore();
	private String serverURL;
//	private String[] inValidImprintMethods = { "PERSONALIZATION", "UNIMPRINTED" };

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String findBySetCodeValueId(int setCodeValueId, String criteriaCode,
			Object formatValue) {
		if (criteriaCode.equals("PRCL")) {
			if (productColorMap == null || productColorMap.isEmpty()) {

				ColorLookup colorLookup = new ColorLookup();
				productColorMap = colorLookup.createProductColorMap(serverURL
						+ "/api/api/lookup/colors");
			}
			return productColorMap.get(String.valueOf(setCodeValueId));
		} else if (criteriaCode.equalsIgnoreCase("ORGN")) {
			if (productOriginMap == null || productOriginMap.isEmpty()) {
				OriginLookup originLookup = new OriginLookup();
				productOriginMap = originLookup
						.createProductOriginMap(serverURL
								+ "/api/api/lookup/origins");
			}
			return productOriginMap.get(String.valueOf(setCodeValueId));
		} else if (criteriaCode.equalsIgnoreCase("MTRL")) {
			MaterialLookup materialLookup = new MaterialLookup();
			if (productMaterialMap == null || productMaterialMap.isEmpty())
				productMaterialMap = materialLookup
						.createProductMaterialMap(serverURL
								+ "/api/api/lookup/materials");
			return productMaterialMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("SHAP")) {
			if (productShapeMap == null || productShapeMap.isEmpty()) {
				ShapeLookup shapeLookup = new ShapeLookup();
				productShapeMap = shapeLookup.createProductShapeMap(serverURL
						+ "/api/api/lookup/shapes");
			}
			return productShapeMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("THEM")) {
			if (productThemeMap == null || productThemeMap.isEmpty()) {
				ThemeLookup themeLookup = new ThemeLookup();
				productThemeMap = themeLookup.createProductThemeMap(serverURL
						+ "/api/api/lookup/themes");
			}
			return productThemeMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("PCKG")) {
			if (productPackageMap == null || productPackageMap.isEmpty()) {
				PackagingLookup packagingLookup = new PackagingLookup();
				productPackageMap = packagingLookup
						.createProductPackagingMap(serverURL
								+ "/api/api/lookup/packaging");
			}
			return productPackageMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("TDNM") && null != formatValue
				&& StringUtils.isNotBlank(formatValue.toString())) {
			TradeNameLookup tradeNameLookup = new TradeNameLookup();
			try {
				formatValue = URLEncoder.encode(formatValue.toString().trim(),
						"UTF-8");
			} catch (UnsupportedEncodingException e) {
				_LOGGER.info("Exception Occured while URL Encoding");
			}
			productTradeNameMap = tradeNameLookup
					.createProductTradeNameMap(serverURL
							+ "/api/api/lookup/trade_names?q=" + formatValue);
			return productTradeNameMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("IMMD") && setCodeValueId != 0) {
			if (imprintMethodsMap == null || imprintMethodsMap.isEmpty()) {
				ImprintLookup imprintLookup = new ImprintLookup();
				imprintMethodsMap = imprintLookup
						.createImprintMethodMap(serverURL
								+ "/api/api/lookup/imprint_methods");
			}
			String returnVal = imprintMethodsMap.get(setCodeValueId);
			if (returnVal.equalsIgnoreCase("other")) {
				returnVal = formatValue.toString();
			}
			return returnVal;
		} else if (criteriaCode.equalsIgnoreCase("MINO")) {
			if (formatValue instanceof Value) {
				return getValueInText(formatValue);
			} else
				return "";
		} else if (criteriaCode.equalsIgnoreCase("ARTW")) {
			if (imprintArtworkMap == null || imprintArtworkMap.isEmpty()) {
				ImprintLookup imprintLookup = new ImprintLookup();
				imprintArtworkMap = imprintLookup
						.createImprintArtworkMap(serverURL
								+ "/api/api/lookup/criteria?code=IMPR");
			}
			return imprintArtworkMap.get(setCodeValueId);
		}
		return "";

	}

	@SuppressWarnings({ "unchecked" })
	public Size findSizeValueDetails(Size size, String criteriaCode,
			List<CriteriaSetValues> criteriaSetValueLst,
			String externalProductId) {
		SizeLookup sizeLookup = new SizeLookup();
		try {
			if (sizeElementsResponse == null) {
				sizeElementsResponse = lookupRestTemplate
						.getForObject(
								RestAPIProperties
										.get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL),
								LinkedList.class);
			}

		} catch (Exception e) {
			_LOGGER.error(
					"Exception while processing Product Size Group JSON - size value parsing",
					e);
		}
		return sizeLookup.findSizeValueDetails(size, criteriaCode,
				sizeElementsResponse, criteriaSetValueLst, externalProductId);
	}
	@SuppressWarnings({ "unchecked" })
	public List<com.asi.ext.api.service.model.Value> findSizeValueListDetails(com.asi.ext.api.service.model.Value size, String criteriaCode,
			Object srcValueObj,
			String externalProductId) {
		SizeLookup sizeLookup = new SizeLookup();
		try {
			if (sizeElementsResponse == null) {
				sizeElementsResponse = lookupRestTemplate
						.getForObject(
								RestAPIProperties
										.get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL),
								LinkedList.class);
			}

		} catch (Exception e) {
			_LOGGER.error(
					"Exception while processing Product Size Group JSON - size value parsing",
					e);
		}
		return sizeLookup.findSizeValueListDetails(size, criteriaCode,
				sizeElementsResponse, srcValueObj, externalProductId);
	}

	@SuppressWarnings({ "unchecked" })
	public ConcurrentHashMap<String, ArrayList<String>> findOptionValueDetails(
			ConcurrentHashMap<String, ArrayList<String>> optionsList,
			String criteriaCode, ProductCriteriaSets productCriteriaSet,
			String externalProductId) {
		OptionLookup optionLookup = new OptionLookup();
		try {
			if (optionElementsResponse == null) {
				optionElementsResponse = lookupRestTemplate
						.getForObject(
								RestAPIProperties
										.get(ApplicationConstants.OPTION_PRODUCT_LOOKUP),
								LinkedList.class);
			}

		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Option parsing",
					e);
		}
		return optionLookup.findOptionValueDetails(optionsList, criteriaCode,
				optionElementsResponse, productCriteriaSet, externalProductId);
	}

	public String[] findImprintSizeValue(
			List<CriteriaSetValue> criteriaSetValues, String externalId) {
		ImprintSizeLookup imprintColorLookup = new ImprintSizeLookup();
		return imprintColorLookup.findImprintSizeDetails(criteriaSetValues,
				externalId);
	}

	public String findSampleType(String setCodeValueId) {

		try {
			if (SamplesElementsResponse == null) {
				SamplesElementsResponse = productDataStore.getSamplesList();
			}

		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Option parsing",
					e);
		}
		return CommonUtilities.getKeysByValueGen(SamplesElementsResponse,
				setCodeValueId);
	}

	public String getTimeText(Object valueAry) {
		String timeValue = "";
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> valueAryList = (ArrayList<LinkedHashMap>) valueAry;
		LinkedHashMap<?, ?> crntValueObj = null;
		for (int valueCntr = 0; null != valueAryList
				&& valueCntr < valueAryList.size(); valueCntr++) {
			crntValueObj = valueAryList.get(valueCntr);
			if (valueCntr == 0)
				timeValue = crntValueObj.get("UnitValue").toString();
			else
				timeValue += ", " + crntValueObj.get("UnitValue").toString();
		}
		return timeValue;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getTimeWithUnitsInText(Object valueAry) {
		String timeValue = "";
		ArrayList<LinkedHashMap> valueAryList = (ArrayList<LinkedHashMap>) valueAry;
		SizeLookup sizeLookup = new SizeLookup();
		if (null == sizesCriteriaWSResponse) {
			try {
				sizesCriteriaWSResponse = (LinkedList<LinkedHashMap>) lookupRestTemplate
						.getForObject(
								RestAPIProperties
										.get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL),
								LinkedList.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LinkedHashMap<?, ?> crntValueObj = null;
		for (int valueCntr = 0; null != valueAryList
				&& valueCntr < valueAryList.size(); valueCntr++) {
			crntValueObj = valueAryList.get(valueCntr);
			if (valueCntr == 0)
				timeValue = crntValueObj.get("UnitValue").toString() +" "+ sizeLookup.getSizesElementValue("UNITS",
						sizesCriteriaWSResponse,
						crntValueObj.get("UnitOfMeasureCode").toString());
			else
				timeValue += ", " + crntValueObj.get("UnitValue").toString();
		}
		return timeValue;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getShippingItems(Object value) {
		String shippingItems = "";
		SizeLookup sizeLookup = new SizeLookup();
		ArrayList<LinkedHashMap> valueAryList = (ArrayList<LinkedHashMap>) value;
		LinkedHashMap<?, ?> crntValueObj = null;
		crntValueObj = valueAryList.get(0);
		shippingItems = crntValueObj.get("UnitValue").toString();
		String response;
		if (sizeElementsResponse == null) {
			try {
				response = JerseyClient.invoke(new URI(serverURL
						+ "/api/api/lookup/criteria_attributes"));
				sizeElementsResponse = (LinkedList<LinkedHashMap>) jsonParser
						.parseToList(response);
			} catch (URISyntaxException e) {
				_LOGGER.error("Exception while processing shipping Items", e);
			}
		}
		return (shippingItems.isEmpty()) ? "" : shippingItems
				+ ":"
				+ sizeLookup.findUnitOfMeasureByFormat(
						crntValueObj.get("UnitOfMeasureCode").toString(),
						sizeElementsResponse);
	}

	@SuppressWarnings("unchecked")
	public String getProductKeywordString(List<Object> productKeywords) {
		String finalKeywordString = "";
		if (productKeywords != null && !productKeywords.isEmpty()) {
			for (Object keyword : productKeywords) {
				try {
					if (keyword != null) {
						LinkedHashMap<String, String> keywordMap = (LinkedHashMap<String, String>) keyword;
						finalKeywordString += "," + keywordMap.get("Value");
					}
				} catch (Exception e) {
					_LOGGER.error("Failed to process keyword : " + keyword, e); // catch
																				// added
																				// only
																				// to
																				// skip
																				// this
																				// value
				}
			}
		}
		if (finalKeywordString.startsWith(",")) {
			finalKeywordString = finalKeywordString.substring(1);
		}
		return finalKeywordString;
	}

	public List<ImprintMethod> setServiceImprintMethods(
			ProductDetail productDetail, String imprintMethods,
			CriteriaSetParser criteriaSetParserCurnt) {
		ImprintMethod serviceImprintMethod = null;
		List<ImprintMethod> imprintMethodList = new ArrayList<>();
		Artwork imprntArtwork = null;
		Artwork tempImprntArtwork = null;
		MinimumOrder imprintMinOrder = null;
		List<Artwork> artworkList=new ArrayList<>();
		imprintRelationMap = new ConcurrentHashMap<>();
		// String
		// currentImprintMethod=processProductLst.getProductConfigurationsList().getImprintMethod();
		ArrayList<String> imrintMethodsList = new ArrayList<>();
		if (null != imprintMethods){
			if(imprintMethods.contains("||")) {
			//imprintMethods = imprintMethods.toLowerCase();
			imrintMethodsList = new ArrayList<String>(
					Arrays.asList(imprintMethods.split("\\|\\|")));
		}else{
			imrintMethodsList.add(imprintMethods);
			}
		}
		ArrayList<Relationship> relationShipList = (ArrayList<Relationship>) productDetail
				.getRelationships();
		ImprintParser imprintParser = new ImprintParser();
		if (null != relationShipList && relationShipList.size() > 0) {
			String imprintMethod = "", minQty = "", artwork = "",tempArtwork="";// ,sold_unimprintedFlag="",personalizationFlag="";
			int imprintCntr = 0;
			for (Relationship crntRelationship : relationShipList) {
				imprintParser.updateCriteriaValuePathsByParent(
						productDetail.getExternalProductId(),
						crntRelationship.getCriteriaSetValuePaths());
			}
			String imprintMethodKey = "", relationValue = "", tempImprintMethod = "";
			String[] individualRelations = null;
			String[] relationValueAry = null, tempRelationValueAry = null;
			String minQtyStr = "";
			String[] artworkValueAry = {};
			for (@SuppressWarnings("rawtypes")
			Map.Entry relationEntry : imprintRelationMap.entrySet()) {
				serviceImprintMethod = new ImprintMethod();
				imprntArtwork = new Artwork();
				artworkList=new ArrayList<>();
				imprintMinOrder = new MinimumOrder();
				imprintMethodKey = relationEntry.getKey().toString();
				relationValue = relationEntry.getValue().toString();
				if (null != relationValue) {
					individualRelations = relationValue.split(",");
					if (imprintCntr == 0) {
						imprintMethod = criteriaSetParserCurnt
								.findCriteriaSetMapValueById(
										productDetail.getExternalProductId())
								.get(imprintMethodKey);
						if (null != imprintMethod
								&& imprintMethod.substring(0,
										imprintMethod.indexOf("__"))
										.equalsIgnoreCase("IMMD")) //&& !ArrayUtils.contains(inValidImprintMethods,imprintMethod.substring(imprintMethod.indexOf("__")).toUpperCase())
									{
							imprintMethod = imprintMethod
									.substring(imprintMethod.indexOf("__") + 2);
							if (!imrintMethodsList.isEmpty()
									&& imrintMethodsList.contains(imprintMethod))
								imrintMethodsList.remove(imprintMethod);
							relationValueAry = imprintParser
									.getImprintRelations(individualRelations,
											productDetail
													.getExternalProductId());
							if (null != relationValueAry
									&& relationValueAry.length == 2) {
								if (imprintMethod.contains(":")) {
									serviceImprintMethod
											.setType(imprintMethod.substring(0,
													imprintMethod.indexOf(":")));
									serviceImprintMethod.setAlias(imprintMethod
											.substring(imprintMethod
													.indexOf(":") + 1));
								} else {
									serviceImprintMethod.setType(imprintMethod);
									serviceImprintMethod
											.setAlias(imprintMethod);
								}
								minQty = (null == relationValueAry[0] || relationValueAry[0]
										.equalsIgnoreCase("null")) ? ""
										: relationValueAry[0];
								artwork = (null == relationValueAry[1] || relationValueAry[1]
										.equalsIgnoreCase("null")) ? ""
										: relationValueAry[1];
								imprntArtwork.setValue(artwork);
								if (minQty.contains(":")) {
									imprintMinOrder.setValue(minQty.substring(0,
											minQty.indexOf(":")));
									imprintMinOrder
											.setUnit(minQty.substring(minQty
													.indexOf(":") + 1));
								} else {
									imprintMinOrder = null;
								}
							}
						} else {
							imprintMethod = "";
							imprintCntr = -1;
						}
					} else {//
						tempImprintMethod = criteriaSetParserCurnt
								.findCriteriaSetMapValueById(
										productDetail.getExternalProductId())
								.get(imprintMethodKey);
						if (null != tempImprintMethod
								&& tempImprintMethod.substring(0,
										tempImprintMethod.indexOf("__"))
										.equalsIgnoreCase("IMMD")
								) {//&& !ArrayUtils.contains(inValidImprintMethods,	tempImprintMethod.substring(tempImprintMethod.indexOf("__")).toUpperCase())
							tempImprintMethod = tempImprintMethod
									.substring(tempImprintMethod.indexOf("__") + 2);
							if (imrintMethodsList.contains(tempImprintMethod))
								imrintMethodsList.remove(tempImprintMethod);
							imprintMethod += "||" + tempImprintMethod;
							tempRelationValueAry = imprintParser
									.getImprintRelations(individualRelations,
											productDetail
													.getExternalProductId());
							if (tempRelationValueAry.length == 2) {
								if (tempImprintMethod.contains(":")) {
									serviceImprintMethod
											.setType(tempImprintMethod
													.substring(
															0,
															tempImprintMethod
																	.indexOf(":")));
									serviceImprintMethod
											.setAlias(tempImprintMethod
													.substring(tempImprintMethod
															.indexOf(":") + 1));
								} else {
									serviceImprintMethod
											.setType(tempImprintMethod);
									serviceImprintMethod
											.setAlias(tempImprintMethod);
								}
								minQtyStr = ((null == tempRelationValueAry[0] || tempRelationValueAry[0]
										.equalsIgnoreCase("null")) ? ""
										: tempRelationValueAry[0]);
								minQty += "||" + minQtyStr;
								artwork += "||"
										+ ((null == tempRelationValueAry[1] || tempRelationValueAry[1]
												.equalsIgnoreCase("null")) ? ""
												: tempRelationValueAry[1]);
								imprntArtwork
										.setValue(((null == tempRelationValueAry[1] || tempRelationValueAry[1]
												.equalsIgnoreCase("null")) ? ""
												: tempRelationValueAry[1]));
								if (minQtyStr.contains(":")) {
									imprintMinOrder.setValue(minQtyStr
											.substring(0,
													minQtyStr.indexOf(":")));
									imprintMinOrder
											.setUnit(minQtyStr
													.substring(minQtyStr
															.indexOf(":") + 1));
								} else {
									imprintMinOrder = null;
								}
							}
						} else {
							tempImprintMethod = "";
							imprintCntr--;
						}
					}
					if (null != imprntArtwork
							&& null != imprntArtwork.getValue()
							&& imprntArtwork.getValue().contains(",")) {
						artworkValueAry = imprntArtwork.getValue().split(",");
						for (String currentArtwork : artworkValueAry) {
							tempImprntArtwork = new Artwork();
							if(currentArtwork.contains(":")){
								tempImprntArtwork.setValue(currentArtwork.substring(0,
										currentArtwork.indexOf(":")));
								if(!currentArtwork.trim().endsWith(":"))
								tempImprntArtwork.setComments(currentArtwork
													.substring(currentArtwork
															.indexOf(":") + 1));
								artworkList.add(tempImprntArtwork);
							}
							
						}
					}else if(null != imprntArtwork && null != imprntArtwork.getValue()){
						tempImprntArtwork = new Artwork();
						tempArtwork=imprntArtwork.getValue();
						if(tempArtwork.contains(":")){ 
						tempImprntArtwork.setValue(tempArtwork.substring(0,
								tempArtwork.indexOf(":")));
						if(!tempArtwork.trim().endsWith(":"))
						tempImprntArtwork.setComments(tempArtwork
											.substring(tempArtwork
													.indexOf(":") + 1));
						artworkList.add(tempImprntArtwork);
						}
							}						
					if(artworkList.size()>0)
					serviceImprintMethod.setArtwork(artworkList);
					serviceImprintMethod.setMinimumOrder(imprintMinOrder);
					if(serviceImprintMethod.getType()!=null)
					imprintMethodList.add(serviceImprintMethod);
				}
				imprintCntr++;
			}
			int cntr = 0;
			
			if (!imrintMethodsList.isEmpty()) {
				for (String impMeth : imrintMethodsList) {
					serviceImprintMethod=new ImprintMethod();
			//		if (!ArrayUtils.contains(inValidImprintMethods,	impMeth.toUpperCase())) {
						if (null != imprintMethod && null != impMeth
								&& !imprintMethod.equals("")){
							imprintMethod += "||" + impMeth.toUpperCase();							
						}
						else {
							if (cntr == 0)
								imprintMethod = impMeth.toUpperCase();
							else
								imprintMethod += "||" + impMeth.toUpperCase();
						}
						if (impMeth.contains(":")) {
							serviceImprintMethod
									.setType(impMeth.substring(0,
											impMeth.indexOf(":")).toUpperCase());
							serviceImprintMethod.setAlias(impMeth
									.substring(impMeth
											.indexOf(":") + 1));
						} else {
						serviceImprintMethod.setType(impMeth.toUpperCase());
						serviceImprintMethod.setAlias(serviceImprintMethod.getType());
						serviceImprintMethod.setMinimumOrder(null);
						}
						imprintMethodList.add(serviceImprintMethod);
						cntr++;
					//}
				}
			}

		} else if (imprintMethods != null && !imprintMethods.isEmpty()) {
			String[] temp = imprintMethods.split("\\|\\|");
			if (temp != null && temp.length > 0) {
				imrintMethodsList = new ArrayList<String>();
				for (int i = 0; i < temp.length; i++) {
					String imprintMethod = temp[i];
					if (imprintMethod != null && !imprintMethod.isEmpty()) { // &&
						serviceImprintMethod=new ImprintMethod();												// isImprintMethod(imprintMethod)
						if (imprintMethod.contains(":")) {
							serviceImprintMethod
									.setType(imprintMethod.substring(0,
											imprintMethod.indexOf(":")).toUpperCase());
							serviceImprintMethod.setAlias(imprintMethod
									.substring(imprintMethod
											.indexOf(":") + 1));
						} else {
						serviceImprintMethod.setType(imprintMethod.toUpperCase());
						serviceImprintMethod.setAlias(serviceImprintMethod.getType());
						serviceImprintMethod.setMinimumOrder(null);
						}
					
						imprintMethodList.add(serviceImprintMethod);
					}
				}
				String imprintMethod = "";
				for (String impMethod : imrintMethodsList) {
					imprintMethod = imprintMethod + "||" + impMethod;
				}
				if (imprintMethod.startsWith("||")) {
					imprintMethod = imprintMethod.substring(2);
				}
			}

		}
		return imprintMethodList;
	}

	public List<com.asi.ext.api.service.model.ProductNumber> setSeriviceProductWithProductNumbers(
			ProductDetail productDetail) {
		// ArrayList<Relationship> relationShipList=(ArrayList<Relationship>)
		// productDetail.getRelationships();
		List<com.asi.ext.api.service.model.ProductNumber> productNumberList = new ArrayList<>();
		com.asi.ext.api.service.model.ProductNumber currentServiceProductNumber = null;
		com.asi.ext.api.service.model.Configurations currentCriteria = null;
		List<com.asi.ext.api.service.model.Configurations> criteriaList = null;
		String currentCriteriaSetvalueId = "";
		// String tempValuePathId="";
		List<ProductNumber> productNumbers = productDetail.getProductNumbers();
		String tempCriteria = "";
		CriteriaInfo criteriaInfo = null;
		
		if (null != productNumbers && productNumbers.size() > 0) {

			for (ProductNumber crntProductNumber : productNumbers) {

				currentServiceProductNumber = new com.asi.ext.api.service.model.ProductNumber();
				currentServiceProductNumber.setProductNumber(crntProductNumber
						.getValue());
				// currentCriteria.setType(String.valueOf(crntProductNumber.getProductNumberConfigurations().get(0).getCriteriaSetValueId()),
				// crntProductNumber.getValue());
				criteriaList = new ArrayList<>();
				for (ProductNumberConfiguration productNumberConfig : crntProductNumber
						.getProductNumberConfigurations()) {
					currentCriteria = new com.asi.ext.api.service.model.Configurations();
					currentCriteriaSetvalueId = String
							.valueOf(productNumberConfig
									.getCriteriaSetValueId());
					tempCriteria = criteriaSetParser.findCriteriaSetValueById(
							productDetail.getExternalProductId(),
							currentCriteriaSetvalueId);
					if (null != tempCriteria) {
						criteriaInfo = ProductDataStore
								.getCriteriaInfoForCriteriaCode(tempCriteria
										.substring(0, tempCriteria.indexOf("_")));
						currentCriteria.setCriteria(criteriaInfo
								.getDescription());
						String tempValue=tempCriteria
						        .substring(tempCriteria.indexOf("__") + 2);
						      if(tempValue.contains(":")){
						       //currentCriteria.setValue(tempValue.substring(tempValue.indexOf(":")+1));
						    	  currentCriteria.setValue(new StringValue(tempValue.substring(tempValue.indexOf(":")+1)));
						      }else{
						       currentCriteria.setValue(new StringValue(tempValue));
						      }
					}
					if(null!=currentCriteria.getCriteria()) criteriaList.add(currentCriteria);
				}
				if(criteriaList.size()>0 && null==crntProductNumber.getPriceGridId()) {
					currentServiceProductNumber.setConfigurations(criteriaList);
					productNumberList.add(currentServiceProductNumber);
				}				
			}

		}
		if(productNumberList.size()>0)
		return productNumberList;
		else
			return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getValueInText(Object value) {
		String valueText = null;

		if (value instanceof List) {
			ArrayList<?> valueList = (ArrayList<?>) value;
			Iterator<?> sizeValuesItr = valueList.iterator();
			SizeLookup sizeLookup = new SizeLookup();
			while (sizeValuesItr.hasNext()) {
				LinkedHashMap<?, ?> valueMap = (LinkedHashMap<?, ?>) sizeValuesItr
						.next();
				if (null == sizesCriteriaWSResponse) {
					try {
						sizesCriteriaWSResponse = (LinkedList<LinkedHashMap>) lookupRestTemplate
								.getForObject(
										RestAPIProperties
												.get(ApplicationConstants.SIZES_CRITERIA_LOOKUP_URL),
										LinkedList.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				valueText = valueMap.get("UnitValue").toString()
						+ ":"
						+ sizeLookup.getSizesElementValue("UNITS",
								sizesCriteriaWSResponse,
								valueMap.get("UnitOfMeasureCode").toString());
			}
			return valueText;
		} else
			return null;
	}

	public com.asi.ext.api.service.model.ProductConfigurations setOptionList(
			com.asi.ext.api.service.model.ProductConfigurations serviceConfigurations,
			ConcurrentHashMap<String, ArrayList<String>> optionList) {
		List<Option> optionsList = new ArrayList<>();
		Option currentOption = null;
		String[] optionDetails = null;
		String crntOptionType = "";

		String[] optionValueAry = {};
		ArrayList<String> optionAryList = null;
		for (String optionKey : optionList.keySet()) {
			if (optionKey.contains("_")) {
				optionDetails = optionKey.split("_");
				crntOptionType = optionDetails[0];
				optionAryList = optionList.get(optionKey);
				crntOptionType = (crntOptionType.equalsIgnoreCase("PROP")) ? "Product"
						: crntOptionType.equalsIgnoreCase("SHOP") ? "Shipping"
								: crntOptionType.equalsIgnoreCase("IMOP") ? "Imprint"
										: "";
				if (null != optionAryList && optionDetails.length > 1
						&& optionAryList.size() == 4) {
					currentOption = new Option();
					currentOption.setOptionType(crntOptionType);
					currentOption.setName(optionDetails[1]);
					optionValueAry = optionAryList.get(0).split(",");
					for (String currentOptValue : optionValueAry) {
						currentOption.getValues().add(currentOptValue);
					}
					currentOption.setCanOnlyOrderOne(Boolean
							.valueOf((optionAryList.get(1).equalsIgnoreCase("Y")?"true":"false")));
					currentOption.setRequiredForOrder(Boolean
							.valueOf((optionAryList.get(2).equalsIgnoreCase("Y")?"true":"false")));
					currentOption
							.setAdditionalInformation(optionAryList.get(3));

				}
				if (null != currentOption)
					optionsList.add(currentOption);
				currentOption = null;
			}
		}
		serviceConfigurations.setOptions(optionsList);
		return serviceConfigurations;
	}

	public List<String> setServiceProductLineNames(ProductDetail productDetail,String authToken) {
		List<String> lineNamesList = new ArrayList<>();
		List<SelectedLineNames> selectedNamesList = productDetail
				.getSelectedLineNames();
		for (SelectedLineNames crntSelectedLineName : selectedNamesList) {
			if (null != ProductDataStore.getSetCodeValueIdForSelectedLineName(
					crntSelectedLineName.getName(),
					authToken))
				lineNamesList.add(crntSelectedLineName.getName());
		}
		return lineNamesList;
	}

}
