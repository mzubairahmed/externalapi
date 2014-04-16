package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.LookupValuesClient;

public class LookupParser {
	

	@Autowired LookupValuesClient lookupClient;
	
	
	
	/**
     * 
     * Gets the parsed size data (in Map format) from sizeResponse JSON
     * 
     * @param sizesWSResponse is the JSON data which contains the size details, criteria details etc.. 
     * @param attribute is the string to lookup for existence  
     * @param DimensionType is the type of dimension
     */
	@SuppressWarnings("rawtypes")
	public HashMap getSizesResponse(String DimensionType,String attribute) {
		DimensionType=DimensionType.trim();
		attribute=attribute.trim();
		HashMap returnValue = null;
		ArrayList<LinkedHashMap> criteriaAttributeList=lookupClient.getCriteriaAttributesFromLookup(lookupClient.getLookupcriteriaAttributeURL().toString());
		for(LinkedHashMap currentCriteriaAttribute: criteriaAttributeList)
		{
			//LinkedHashMap sizesData = (LinkedHashMap) iter.next();
			if (attribute.equalsIgnoreCase(currentCriteriaAttribute.get("ID")
					.toString())
					&& DimensionType.equalsIgnoreCase(currentCriteriaAttribute
							.get("CriteriaCode").toString())) {
				returnValue = currentCriteriaAttribute;
				break;
			}
		}
		return returnValue;
	}

	private String getSizesElementValue(String criteriaCode,
			String criteriaAttributeId, String unitValue,
			String unitOfMeasureCode) {
		String elementValue="";
		String elementName="";
		String elementUnit="";
		@SuppressWarnings("rawtypes")
		HashMap sourceMap=getSizesResponse(criteriaCode, criteriaAttributeId);
		elementName=sourceMap.get("DisplayName").toString();
		if(null!=sourceMap && null!=sourceMap.get("UnitsOfMeasure") && !sourceMap.get("UnitsOfMeasure").toString().isEmpty())
		{
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> unitOfMeasures = (ArrayList<LinkedHashMap>) sourceMap.get("UnitsOfMeasure");
		for(@SuppressWarnings("rawtypes") Map codeValueGrpsMap :unitOfMeasures) {
			// LOGGER.info(codeValueGrpsMap.toString());
			if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(unitOfMeasureCode)) {
				elementUnit = (String) codeValueGrpsMap.get("Format");
			}
		}
		}
		elementValue=!elementName.isEmpty()?elementName+":"+unitValue+":"+elementUnit:"";
		return elementValue;
	}

	public LookupValuesClient getLookupClient() {
		return lookupClient;
	}

	public void setLookupClient(LookupValuesClient lookupClient) {
		this.lookupClient = lookupClient;
	}

	@SuppressWarnings("rawtypes")
	public String getValueString(ArrayList<?> value,String criteriaCode) {
		String valueStr="";
		String currentValue="";
		LinkedHashMap curntHashMap=null;
		if(null!=value){
		for(Object currentObj:value)
		{
			curntHashMap =(LinkedHashMap)currentObj;
			if(criteriaCode.equalsIgnoreCase("DIMS")){
				currentValue=getSizesElementValue(criteriaCode,curntHashMap.get("CriteriaAttributeId").toString(),curntHashMap.get("UnitValue").toString(),curntHashMap.get("UnitOfMeasureCode").toString());
			} else {
				currentValue=getElementValue(criteriaCode,curntHashMap.get("CriteriaAttributeId").toString(),curntHashMap.get("UnitValue").toString(),curntHashMap.get("UnitOfMeasureCode").toString());
			}
			valueStr=valueStr.equals("")?currentValue:valueStr+";"+currentValue;
			currentValue="";
		}
		}
		return valueStr;
	}

	private String getElementValue(String criteriaCode,String criteriaAttributeId,String unitValue, String unitOfMeasureCode) {
		String elementUnit="";
		String elementValue="";
		@SuppressWarnings("rawtypes")
		HashMap sourceMap=getSizesResponse(criteriaCode, criteriaAttributeId);
		if(null!=sourceMap && null!=sourceMap.get("UnitsOfMeasure") && !sourceMap.get("UnitsOfMeasure").toString().isEmpty())
		{
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<LinkedHashMap> unitOfMeasures = (ArrayList<LinkedHashMap>) sourceMap.get("UnitsOfMeasure");
		for(@SuppressWarnings("rawtypes") Map codeValueGrpsMap :unitOfMeasures) {
			if (codeValueGrpsMap.get("Code").toString().equalsIgnoreCase(unitOfMeasureCode)) {
				elementUnit = (String) codeValueGrpsMap.get("Format");
			}
		}
		}
		elementValue=(!unitValue.isEmpty() && !elementUnit.isEmpty())?unitValue+":"+elementUnit:"";
		return elementValue;
	}

	
	
}
