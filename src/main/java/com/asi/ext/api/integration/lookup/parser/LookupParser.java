package com.asi.ext.api.integration.lookup.parser;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.asi.core.utils.JerseyClient;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.Value;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.ProductConfigurationsList;
import com.asi.service.product.client.vo.ProductCriteriaSet;
import com.asi.service.product.client.vo.parser.ColorLookup;
import com.asi.service.product.client.vo.parser.ImprintLookup;
import com.asi.service.product.client.vo.parser.ImprintSizeLookup;
import com.asi.service.product.client.vo.parser.MaterialLookup;
import com.asi.service.product.client.vo.parser.OptionLookup;
import com.asi.service.product.client.vo.parser.OriginLookup;
import com.asi.service.product.client.vo.parser.PackagingLookup;
import com.asi.service.product.client.vo.parser.SampleLookup;
import com.asi.service.product.client.vo.parser.ShapeLookup;
import com.asi.service.product.client.vo.parser.SizeLookup;
import com.asi.service.product.client.vo.parser.ThemeLookup;
import com.asi.service.product.client.vo.parser.TradeNameLookup;
import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;

public class LookupParser {
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser(); 
	private HashMap<String, String> productColorMap = null;
	private HashMap<Integer, String> productShapeMap = null;
	private HashMap<Integer, String> productMaterialMap = null;
	//private HashMap<Integer, String> additionalColorMap = null;
	private HashMap<Integer, String> productThemeMap = null;
	private HashMap<Integer, String> productPackageMap=null;
	private HashMap<Integer, String> productTradeNameMap = null;
	private HashMap<Integer, String> imprintMethodsMap = null;
	private HashMap<Integer, String> imprintArtworkMap = null;
	public static ConcurrentHashMap<String, String> imprintRelationMap = null;
	private HashMap<String, String> productWarningMap=null;
	@SuppressWarnings("rawtypes")
	public static  HashMap<String,String>  SamplesElementsResponse=null;
	@SuppressWarnings("rawtypes")
	public static LinkedList<LinkedHashMap> sizeElementsResponse = null;
	@SuppressWarnings("rawtypes")
	public static LinkedList<LinkedHashMap> sizesCriteriaWSResponse = null;
	@SuppressWarnings("rawtypes")	
	public static LinkedList<LinkedHashMap> optionElementsResponse =null;
	private static IParser jsonParser = new JSONParserImpl();
	private HashMap<String, String> productOriginMap = null;
	private final static Logger _LOGGER = Logger
			.getLogger(TradeNameLookup.class.getName());
	private ProductDataStore productDataStore=new ProductDataStore();
	private String serverURL;
	private String[] inValidImprintMethods={"PERSONALIZATION","UNIMPRINTED"};
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
		}  else if (criteriaCode.equalsIgnoreCase("THEM")) {
			if (productThemeMap == null || productThemeMap.isEmpty()) {
				ThemeLookup themeLookup = new ThemeLookup();
				productThemeMap = themeLookup.createProductThemeMap(serverURL
						+ "/api/api/lookup/themes");
			}
			return productThemeMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("PCKG")) {
			if (productPackageMap == null || productPackageMap.isEmpty()) {
				PackagingLookup packagingLookup = new PackagingLookup();
				productPackageMap = packagingLookup.createProductPackagingMap(serverURL
						+ "/api/api/lookup/packaging");
			}
			return productPackageMap.get(setCodeValueId);
		}else if (criteriaCode.equalsIgnoreCase("TDNM") && null!=formatValue 
				&& StringUtils.isNotBlank(formatValue.toString())) {
			TradeNameLookup tradeNameLookup = new TradeNameLookup();
			try {
				formatValue = URLEncoder.encode(formatValue.toString().trim(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				_LOGGER.info("Exception Occured while URL Encoding");
			}
			productTradeNameMap = tradeNameLookup
					.createProductTradeNameMap(serverURL
							+ "/api/api/lookup/trade_names?q=" + formatValue);
			return productTradeNameMap.get(setCodeValueId);
		} else if (criteriaCode.equalsIgnoreCase("IMMD")
				&& setCodeValueId!=0) {
				if (imprintMethodsMap == null || imprintMethodsMap.isEmpty()) {
				ImprintLookup imprintLookup = new ImprintLookup();
				imprintMethodsMap = imprintLookup.createImprintMethodMap(serverURL
						+ "/api/api/lookup/imprint_methods");
			}
			String returnVal=imprintMethodsMap.get(setCodeValueId);
			if(returnVal.equalsIgnoreCase("other"))
			{
				returnVal=formatValue.toString();
			}
			return returnVal;
		}else if (criteriaCode.equalsIgnoreCase("MINO")) {
			if(formatValue instanceof Value)
			{
				return getValueInText(formatValue);
			}
			else
			return "";
		}else if (criteriaCode.equalsIgnoreCase("ARTW")) {
			if (imprintArtworkMap == null || imprintArtworkMap.isEmpty()) {
				ImprintLookup imprintLookup = new ImprintLookup();
				imprintArtworkMap = imprintLookup.createImprintArtworkMap(serverURL
						+ "/api/api/lookup/criteria?code=IMPR");
			}
			return imprintArtworkMap.get(setCodeValueId);
		}
			return "";

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ProductConfigurationsList findSizeValueDetails(ProductConfigurationsList productConfigurationsList,String criteriaCode,
			List<CriteriaSetValue> criteriaSetValueLst,String externalProductId) {
		SizeLookup sizeLookup = new SizeLookup();
		String response;
		try {
			if(sizeElementsResponse==null)
			{
				response = JerseyClient.invoke(new URI(serverURL
						+ "/api/api/lookup/criteria_attributes"));
				sizeElementsResponse = (LinkedList<LinkedHashMap>) jsonParser
						.parseToList(response);
			}
			
		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Size Group JSON - size value parsing",
					e);
		}
		return sizeLookup.findSizeValueDetails(productConfigurationsList,criteriaCode,
				sizeElementsResponse, criteriaSetValueLst,externalProductId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConcurrentHashMap<String,ArrayList<String>> findOptionValueDetails(ConcurrentHashMap<String,ArrayList<String>> optionsList,String criteriaCode,
			ProductCriteriaSet productCriteriaSet,String externalProductId) {
		OptionLookup optionLookup=new OptionLookup();
		String response;
		try {
			if(optionElementsResponse==null)
			{
				response = JerseyClient.invoke(new URI(serverURL
						+ "/api/api/lookup/criteria?code=PROD"));
				optionElementsResponse = (LinkedList<LinkedHashMap>) jsonParser
						.parseToList(response);
			}
			
		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Option parsing",
					e);
		}
		return optionLookup.findOptionValueDetails(optionsList,criteriaCode,
				optionElementsResponse, productCriteriaSet,externalProductId);
	}

	public String[] findImprintSizeValue(
			List<CriteriaSetValue> criteriaSetValues, String externalId) {
		ImprintSizeLookup imprintColorLookup=new ImprintSizeLookup();
		return imprintColorLookup.findImprintSizeDetails(criteriaSetValues,externalId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String findSampleType(String setCodeValueId) {
		SampleLookup sampleLookup=new SampleLookup();
		
		try {
			if(SamplesElementsResponse==null)
			{
				SamplesElementsResponse=productDataStore.getSamplesList();
				
				/*String response = JerseyClient.invoke(new URI(serverURL
						+ "/api/api/lookup/criteria?code=IMPR"));
				SamplesElementsResponse = (LinkedList<LinkedHashMap>) jsonParser
						.parseToList(response);*/
			}
			
		} catch (Exception e) {
			_LOGGER.error("Exception while processing Product Option parsing",
					e);
		}
		return  CommonUtilities.getKeysByValueGen(SamplesElementsResponse, setCodeValueId);
	}

	public String getTimeText(Object valueAry) {
		String timeValue="";
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> valueAryList=(ArrayList<LinkedHashMap>) valueAry;
		LinkedHashMap<?, ?> crntValueObj=null;
		for(int valueCntr=0;null!=valueAryList && valueCntr<valueAryList.size();valueCntr++)
		{
			crntValueObj=valueAryList.get(valueCntr);
			if(valueCntr==0)
				timeValue=crntValueObj.get("UnitValue").toString();
			else
				timeValue+=", "+crntValueObj.get("UnitValue").toString();
		}
		return timeValue;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getShippingItems(Object value) {
		String shippingItems="";
		SizeLookup sizeLookup=new SizeLookup();
		ArrayList<LinkedHashMap> valueAryList=(ArrayList<LinkedHashMap>) value;
		LinkedHashMap<?, ?> crntValueObj=null;
		crntValueObj=valueAryList.get(0);
		shippingItems=crntValueObj.get("UnitValue").toString();
		String response;
		if(sizeElementsResponse==null)
		{
			try {
				response = JerseyClient.invoke(new URI(serverURL
						+ "/api/api/lookup/criteria_attributes"));
				sizeElementsResponse = (LinkedList<LinkedHashMap>) jsonParser
						.parseToList(response);
			} catch (URISyntaxException e) {
				_LOGGER.error("Exception while processing shipping Items",e);
			}
		}
		return (shippingItems.isEmpty())?"":shippingItems+":"+sizeLookup.findUnitOfMeasureByFormat(crntValueObj.get("UnitOfMeasureCode").toString(), sizeElementsResponse);
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
	                _LOGGER.error("Failed to process keyword : "+keyword, e); // catch added only to skip this value
	            }
	        }
	    }
	    if (finalKeywordString.startsWith(",")) {
	        finalKeywordString = finalKeywordString.substring(1);
	    }
	    return finalKeywordString;
	}

/*	public ProcessProductsList updateImprintMethod(
			ProcessProductsList processProductLst) {
		imprintRelationMap=new ConcurrentHashMap<>();
		String currentImprintMethod=processProductLst.getProductConfigurationsList().getImprintMethod();
		ArrayList<String> imrintMethodsList=new ArrayList<>();
		if(null!=currentImprintMethod && currentImprintMethod.contains("||"))
		{
			currentImprintMethod=currentImprintMethod.toLowerCase();
			imrintMethodsList=new ArrayList<String>(Arrays.asList(currentImprintMethod.split("\\|\\|")));
		}
		ArrayList<Relationship> relationShipList=(ArrayList<Relationship>) processProductLst.getProduct().getRelationships();
		ImprintParser imprintParser=new ImprintParser();
		if(null!=relationShipList && relationShipList.size()>0)
		{
		String imprintMethod="",minQty="",artwork="",sold_unimprintedFlag="",personalizationFlag="";
		int imprintCntr=0;
		for(Relationship crntRelationship:relationShipList)
		{
		imprintParser.updateCriteriaValuePathsByParent(processProductLst.getProduct().getExternalProductId(),crntRelationship.getCriteriaSetValuePaths());
		}
		String imprintMethodKey="",relationValue="",tempImprintMethod="";
		String[] individualRelations=null;
		String[] relationValueAry=null,tempRelationValueAry=null;
		for(@SuppressWarnings("rawtypes") Map.Entry relationEntry : imprintRelationMap.entrySet())
		{
			imprintMethodKey=relationEntry.getKey().toString();
			relationValue=relationEntry.getValue().toString();
			if(null!=relationValue)
			{
				individualRelations=relationValue.split(",");
			if(imprintCntr==0)
			{
				imprintMethod=criteriaSetParser.findCriteriaSetMapValueById(processProductLst.getProduct().getExternalProductId()).get(imprintMethodKey);
				if(null!=imprintMethod && imprintMethod.substring(0, imprintMethod.indexOf("__")).equalsIgnoreCase("IMMD") && !ArrayUtils.contains(inValidImprintMethods,imprintMethod.substring(imprintMethod.indexOf("__")).toUpperCase()))
				{
					imprintMethod=imprintMethod.substring(imprintMethod.indexOf("__")+2);	
					if(!imrintMethodsList.isEmpty() && imrintMethodsList.contains(imprintMethod.toLowerCase())) imrintMethodsList.remove(imprintMethod.toLowerCase());
					relationValueAry=imprintParser.getImprintRelations(individualRelations,processProductLst.getProduct().getExternalProductId());
					if(null!=relationValueAry && relationValueAry.length==2)
					{
						minQty=(null==relationValueAry[0] || relationValueAry[0].equalsIgnoreCase("null"))?"":relationValueAry[0];
						artwork=(null==relationValueAry[1] || relationValueAry[1].equalsIgnoreCase("null"))?"":relationValueAry[1];
					}
				}else
				{
					imprintMethod="";
					imprintCntr=-1;
				}
			}
			else{//
				tempImprintMethod=criteriaSetParser.findCriteriaSetMapValueById(processProductLst.getProduct().getExternalProductId()).get(imprintMethodKey);
				if(null!=tempImprintMethod && tempImprintMethod.substring(0, tempImprintMethod.indexOf("__")).equalsIgnoreCase("IMMD") && !ArrayUtils.contains(inValidImprintMethods,tempImprintMethod.substring(tempImprintMethod.indexOf("__")).toUpperCase()))
				{
				tempImprintMethod=tempImprintMethod.substring(tempImprintMethod.indexOf("__")+2);
				if(imrintMethodsList.contains(tempImprintMethod.toLowerCase())) imrintMethodsList.remove(tempImprintMethod.toLowerCase());
				imprintMethod+="||"+tempImprintMethod;
				tempRelationValueAry=imprintParser.getImprintRelations(individualRelations,processProductLst.getProduct().getExternalProductId());
				if(tempRelationValueAry.length==2)
				{
					minQty+="||"+((null==tempRelationValueAry[0] || tempRelationValueAry[0].equalsIgnoreCase("null"))?"":tempRelationValueAry[0]);
					artwork+="||"+((null==tempRelationValueAry[1] || tempRelationValueAry[1].equalsIgnoreCase("null"))?"":tempRelationValueAry[1]);
				}
				}else {
					tempImprintMethod="";
					imprintCntr--;
				}
			}
			
			}
					imprintCntr++;
		}
		int cntr=0;
		if(!imrintMethodsList.isEmpty())
		{
			for(String impMeth:imrintMethodsList)
			{
				if(!ArrayUtils.contains(inValidImprintMethods,impMeth.toUpperCase()))
				{
				if(null!=imprintMethod && null!=impMeth && !imprintMethod.equals("")) 
					imprintMethod+="||"+impMeth.toUpperCase();
				else
				{
					if(cntr==0)
						imprintMethod=impMeth.toUpperCase();
					else
						imprintMethod+="||"+impMeth.toUpperCase();
				}				
				cntr++;
				}
			}
		}
		if(imprintParser.checkImprintMethod("Unimprinted",processProductLst.getProduct().getExternalProductId())) sold_unimprintedFlag="Y";
		if(imprintParser.checkImprintMethod("Personalization",processProductLst.getProduct().getExternalProductId())) personalizationFlag="Y";	
		if(null!=processProductLst.getProductConfigurationsList())
		{
		if(null!=relationValueAry && relationValueAry.length==2)
		{
		processProductLst.getProductConfigurationsList().setMinimumQuantity(minQty);
		processProductLst.getProductConfigurationsList().setArtwork(artwork);
		}
		processProductLst.getProductConfigurationsList().setPersonalizationAvailable(personalizationFlag);
		processProductLst.getProductConfigurationsList().setSolidUnimprinted(sold_unimprintedFlag);
		processProductLst.getProductConfigurationsList().setImprintMethod(imprintMethod);
		}
		} else if (currentImprintMethod != null && !currentImprintMethod.isEmpty()) {
		    String[] temp = currentImprintMethod.split("\\|\\|");
		    if (temp != null && temp.length > 0) { 
		        imrintMethodsList= new ArrayList<String>();
		        for (int i = 0; i < temp.length; i++) {
		            String imprintMethod = temp[i];
		            if (imprintMethod != null && !imprintMethod.isEmpty() && isImprintMethod(imprintMethod)) {
		                imrintMethodsList.add(imprintMethod);
		            }		            
		        }
		        String imprintMethod = "";
		        for (String impMethod : imrintMethodsList) {
		            imprintMethod =  imprintMethod + "||" + impMethod;
		        }
		        if (imprintMethod.startsWith("||")) {
		            imprintMethod = imprintMethod.substring(2);
		        }
		        processProductLst.getProductConfigurationsList().setImprintMethod(imprintMethod);
		        if(imprintParser.checkImprintMethod("Unimprinted",processProductLst.getProduct().getExternalProductId())){
		            processProductLst.getProductConfigurationsList().setSolidUnimprinted("Y");
		        }
		        if(imprintParser.checkImprintMethod("Personalization",processProductLst.getProduct().getExternalProductId())) {
		            processProductLst.getProductConfigurationsList().setPersonalizationAvailable("Y");
		        }
		    }
		    
		    
		}
		return processProductLst;
	}
	*/
	private boolean isImprintMethod(String method) {
	    if (StringUtils.containsIgnoreCase(method.trim(), "Unimprinted") || StringUtils.containsIgnoreCase(method.trim(), "Personalization")) {
	        return false;
	    } else {
	        return true;
	    }
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getValueInText(Object value) {
		String valueText=null;
		
		if(value instanceof List)
		{
		ArrayList<?> valueList=(ArrayList<?>)value;
		Iterator<?> sizeValuesItr=valueList.iterator();
		SizeLookup sizeLookup=new SizeLookup();
		while(sizeValuesItr.hasNext())
		{
			LinkedHashMap<?, ?> valueMap=(LinkedHashMap<?, ?>)sizeValuesItr.next();
		 if (null == sizesCriteriaWSResponse) {
             try {
				sizesCriteriaWSResponse = (LinkedList<LinkedHashMap>) jsonParser
							.parseToList(JerseyClient.invoke(new URI(serverURL
									+ "/api/api/lookup/criteria_attributes")));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
      
		valueText=valueMap.get("UnitValue").toString()+":"+sizeLookup.getSizesElementValue("UNITS", sizesCriteriaWSResponse,valueMap.get("UnitOfMeasureCode").toString());
		}
		return 	valueText;	
		}
		else
		return null;
	}

	/*public String updateSafetyWarnings(
			ProcessProductsList processProductLst) {
		ArrayList<SelectedSafetyWarnings> safetyWarningsList=(ArrayList<SelectedSafetyWarnings>) processProductLst.getProduct().getSelectedSafetyWarnings();
		String safetyWarning="";
		int cntr=0;
		for(SelectedSafetyWarnings crntSafetyWarning:safetyWarningsList)
		{
			if (productWarningMap == null || productWarningMap.isEmpty()) {
				SafetyWarningLookup safetyWarningLookup=new SafetyWarningLookup();
				productWarningMap = safetyWarningLookup.createProductWarningMap(serverURL
						+ "/api/api/lookup/safetywarnings");
			}
			if(cntr==0)
				safetyWarning=productWarningMap.get(crntSafetyWarning.getCode());
			else
				safetyWarning+=","+productWarningMap.get(crntSafetyWarning.getCode());
			 cntr++;
		}		
		return safetyWarning;
	}*/
	
	public ProductConfigurationsList setOptionList(
			ProductConfigurationsList productConfigurationsList,
			ConcurrentHashMap<String, ArrayList<String>> optionList) {
		  String[] optionDetails=null;
		  int optionCntr=0;
		  String optionName="",optionType="",optionValue="",crntOptionType="";
		  String canOrderOnlyOne="",reqForOrder="";
		  ArrayList<String> optionAryList=null;
          for(String optionKey:optionList.keySet())
          {
          	if(optionKey.contains("_"))
          	{
          		optionDetails=optionKey.split("_");
          		crntOptionType=optionDetails[0];
          		optionAryList=optionList.get(optionKey);
          		crntOptionType=(crntOptionType.equalsIgnoreCase("PROP"))?"Product Option":crntOptionType.equalsIgnoreCase("SHOP")?"Shipping Option":crntOptionType.equalsIgnoreCase("IMOP")?"Imprint Option":"";
          		if(null!=optionAryList && optionDetails.length>1 && optionAryList.size()==3)
          		{
	          		if(optionCntr==0)
	          		{
	          			optionType=crntOptionType;
	          			optionName=optionDetails[1];
	          			optionValue=optionAryList.get(0);
	          			canOrderOnlyOne=optionAryList.get(1);
	          			reqForOrder=optionAryList.get(2);
	          		}
	          		else
	          		{
	          			optionType+="||"+crntOptionType;
	          			optionName+="||"+optionDetails[1];
	          			optionValue+="||"+optionAryList.get(0);
	          			canOrderOnlyOne+="||"+optionAryList.get(1);
	          			reqForOrder+="||"+optionAryList.get(2);
	          		}
          		}
          	}
          	optionCntr++;
          }
          productConfigurationsList.setOptionName(optionName);
          productConfigurationsList.setOptionType(optionType);
          productConfigurationsList.setOptionValue(optionValue);
          productConfigurationsList.setCanOrderOnlyOne(canOrderOnlyOne);
          productConfigurationsList.setReqForOrder(reqForOrder);
		return productConfigurationsList;
	}
}
