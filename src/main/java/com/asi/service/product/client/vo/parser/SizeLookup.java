package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

import com.asi.ext.api.integration.lookup.parser.CriteriaSetParser;
import com.asi.ext.api.service.model.Apparel;
import com.asi.ext.api.service.model.Capacity;
import com.asi.ext.api.service.model.Dimension;
import com.asi.ext.api.service.model.OtherSize;
import com.asi.ext.api.service.model.Size;
import com.asi.ext.api.service.model.Value;
import com.asi.ext.api.service.model.Values;
import com.asi.ext.api.service.model.Volume;
import com.asi.service.product.client.vo.CriteriaSetValues;



public class SizeLookup {
	private final static Logger _LOGGER = Logger.getLogger(SizeLookup.class
			.getName());
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser(); 
	/**
	 * Finds the value of a given size element from sizeElementResponses
	 * 
	 * @param elementName is the type of size value like Dimension, Weight, etc...
	 * @param sizeElementsResponse contains all the size related details 
	 * @param attribute is the value
	 * @return Criteria code of the give size value
	 */
	@SuppressWarnings("rawtypes")
	public String getSizesElementValue(String elementName,
			LinkedList<LinkedHashMap> sizeElementsResponse, String attribute) {
		attribute=attribute.trim();
		elementName=elementName.trim();
		String ElementValue = "";
		try
		{
			//LinkedList<LinkedHashMap> sizeElementsResponse=(LinkedList<LinkedHashMap>)jsonParser.parseToList(response);
			if(null!=sizeElementsResponse)
			{
			Iterator<LinkedHashMap> iterator = sizeElementsResponse.iterator();
			while (iterator.hasNext()) {
				Map sizeIndividualLookupMap = (LinkedHashMap) iterator.next();
				if (elementName.equalsIgnoreCase("id"))
				{
					if(sizeIndividualLookupMap.get("ID").toString().equals(attribute))
					{
						ElementValue=sizeIndividualLookupMap.get("DisplayName").toString();
						break;
					}					
				}else if(elementName.equalsIgnoreCase("units"))
				{
					@SuppressWarnings({ "unchecked" })
					ArrayList<LinkedHashMap> unitValuesList = (ArrayList<LinkedHashMap>) sizeIndividualLookupMap.get("UnitsOfMeasure");
					Iterator<LinkedHashMap> unitValuesiterator = unitValuesList.iterator();
					while (unitValuesiterator.hasNext()) {
						Map codeValueGrpsMap = (LinkedHashMap) unitValuesiterator.next();
						if (codeValueGrpsMap.get("Code").toString()
								.equalsIgnoreCase(attribute)) {
							ElementValue = (String) codeValueGrpsMap.get("Format");
							break;
						}
					}
				}
				if(!ElementValue.isEmpty())
					break;
			}
			}

		}
		catch(Exception Ex)
		{
			_LOGGER.error("Exception while processing Product Size Group JSON", Ex);
		}
		return ElementValue;
	}
	public Size findSizeValueDetails(Size size,String criteriaCode,@SuppressWarnings("rawtypes") LinkedList<LinkedHashMap> criteriaAttributes,
			List<CriteriaSetValues> criteriaSetValueLst,String externalProductId) {
		Dimension dimension=null;
		Capacity capacity=null;
		Apparel apparel=null;
		OtherSize other=null;
		Volume volume=null;
				if(criteriaCode.equalsIgnoreCase("DIMS"))
					dimension=new Dimension();
				else if(criteriaCode.equalsIgnoreCase("CAPS"))
					capacity=new Capacity();
				else if(criteriaCode.equalsIgnoreCase("SVWT"))
					volume=new Volume();
				else if(criteriaCode.equalsIgnoreCase("SABR") || criteriaCode.equalsIgnoreCase("SAHU") || criteriaCode.equalsIgnoreCase("SAIT") || criteriaCode.equalsIgnoreCase("SANS") || criteriaCode.equalsIgnoreCase("SAWI") || criteriaCode.equalsIgnoreCase("SSNM"))
					apparel=new Apparel();
				else if(criteriaCode.equalsIgnoreCase("SOTH"))
					other=new OtherSize();
				
		
				String sizeValue="",finalSizeValue="",delim="";
				String sizeElementValue="";
				//int noOfSizes=criteriaSetValueLst.size();
				int elementsCntr=0;
				Value valueObj;
				List<Value> valueObjList=null;
				List<String> valueStringList=null;
				List<Values> valuesList=new ArrayList<>();
				String unitOfmeasureCode="";
				String[] valueElements={};
				
				Values currentValues=null;
				for(CriteriaSetValues criteriaSetValue:criteriaSetValueLst)
				{
					int sizeCntr=0;
					currentValues=new Values();	
					if(criteriaSetValue.getValue() instanceof List)
					{
					ArrayList<?> valueList=(ArrayList<?>)criteriaSetValue.getValue();
					Iterator<?> sizeValuesItr=valueList.iterator();
					valueObjList=new ArrayList<>();
					valueStringList=new ArrayList<>();
					while(sizeValuesItr.hasNext())
					{
						LinkedHashMap<?, ?> valueMap=(LinkedHashMap<?, ?>)sizeValuesItr.next();
						unitOfmeasureCode=getSizesElementValue("UNITS", criteriaAttributes,valueMap.get("UnitOfMeasureCode").toString());
						valueObj =new Value();
						
						if(unitOfmeasureCode.equals("\"")) unitOfmeasureCode="in";
						if(unitOfmeasureCode.equals("'")) unitOfmeasureCode="ft";
						if(criteriaCode.equalsIgnoreCase("DIMS") || criteriaCode.equalsIgnoreCase("SDIM"))
						{
							if(criteriaCode.equalsIgnoreCase("DIMS")){
								
								sizeValue=getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString())+":"+valueMap.get("UnitValue")+":"
										+unitOfmeasureCode;
								criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,Integer.parseInt(criteriaSetValue.getId()),sizeValue);
								valueElements=sizeValue.split(":");
								if(valueElements.length>2){
									valueObj.setAttribute(valueElements[0]);
									valueObj.setUnit(valueElements[2]);
									valueObj.setValue(valueElements[1]);
									}
								valueObjList.add(valueObj);
							}
							else{
							sizeValue=valueMap.get("UnitValue")+":"+unitOfmeasureCode;
							criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,Integer.parseInt(criteriaSetValue.getId()),sizeValue);
							valueElements=sizeValue.split(":");
							if(valueElements.length>2){
							valueObj.setAttribute(valueElements[1]);
							valueObj.setUnit("");
							valueObj.setValue(valueElements[0]);
							}
							delim="; ";
							}
						}
						else if(criteriaCode.equalsIgnoreCase("CAPS") || criteriaCode.equalsIgnoreCase("SVWT") || criteriaCode.equalsIgnoreCase("SHWT"))
							{
							sizeValue=valueMap.get("UnitValue")+":"
									+unitOfmeasureCode;
							criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,Integer.parseInt(criteriaSetValue.getId()),sizeValue);
							if(criteriaCode.equalsIgnoreCase("SVWT")){
							valueElements=sizeValue.split(":");
							if(valueElements.length>1){
								valueObj.setUnit(valueElements[0]);
								valueObj.setValue(valueElements[1]);
							}
							valueObjList.add(valueObj);}else{
								valueStringList.add(sizeValue);
							}
							delim=": ";
							}
						else 
						{
							if(criteriaCode.equalsIgnoreCase("SAWI"))
							{
								if(getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString()).equals("Waist")) {
									sizeValue=valueMap.get("UnitValue").toString();
								}
								else if(getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString()).equals("Inseam")){
									sizeValue="x"+valueMap.get("UnitValue").toString();
								}
								
							}
							else
								if(criteriaCode.equalsIgnoreCase("SAIT")){  
									if(unitOfmeasureCode.length()==1){
										sizeValue=valueMap.get("UnitValue").toString()+unitOfmeasureCode;
									}
									else{
										sizeValue=valueMap.get("UnitValue").toString()+" "+unitOfmeasureCode;	
									}									
								}
							valueStringList.add(sizeValue);
						}
						
						if(sizeCntr!=0)
						{
							if(criteriaCode.equalsIgnoreCase("SANS")){
								sizeElementValue+="("+sizeValue.trim()+")";
							}
								else {
									sizeElementValue+=delim+sizeValue;
								}
						}
						
						else{
							sizeElementValue+=sizeValue;
							//valueObj.setValue(sizeValue);
						}
						//valueObjList.add(valueObj);
						sizeCntr++;
					}		
					
					}else
					{
							sizeValue=criteriaSetValue.getBaseLookupValue();
							if(null==sizeValue)
								sizeValue=criteriaSetValue.getFormatValue();
							valueStringList.add(sizeValue);//valueObj.setValue(sizeValue);
							sizeElementValue+=sizeValue;
						sizeCntr++;
					}
					//criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,Integer.parseInt(criteriaSetValue.getId()),sizeValue);
					if(elementsCntr!=0)
					{
						finalSizeValue=finalSizeValue+","+sizeElementValue.trim();
					}else
						finalSizeValue=sizeElementValue.trim();
					valueElements=sizeElementValue.split(":");
				/*	if(valueElements.length>2){
						valueObj.setAttribute(valueElements[0]);
						valueObj.setUnit(valueElements[2]);
						valueObj.setValue(valueElements[1]);
						}else{
							if(valueElements.length>1){
								valueObj.setUnit(valueElements[1]);
								valueObj.setValue("");
								valueObj.setValue(valueElements[0]);
								}else{
									valueObj.setValue(valueElements[0]);
								}
						}*/
					sizeElementValue="";
					elementsCntr++;
					if(criteriaCode.equalsIgnoreCase("DIMS") || criteriaCode.equalsIgnoreCase("SVWT")){
						currentValues.setValue(valueObjList);
						valuesList.add(currentValues);
					}else if(criteriaCode.equalsIgnoreCase("SAIT")){
						currentValues.setValue(valueStringList);
						valuesList.add(currentValues);
						valueStringList=new ArrayList<>();
					}
					
				}
				switch(criteriaCode){
				case "DIMS":
					dimension.setValues(valuesList);
					size.setDimension(dimension);
					break;
				case "SVWT":
					volume.setValues(valuesList);
					size.setVolume(volume);
					break;
				case "SAIT":
					apparel.setValues(valuesList);
					size.setApparel(apparel);
					break;
				default: 
					break;
				}
				//size.setValue(valueObjList);
			return size;

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

	public String findUnitOfMeasureByFormat(String format,@SuppressWarnings("rawtypes") LinkedList<LinkedHashMap> criteriaAttributes) 
	{
		
		return getSizesElementValue("UNITS", criteriaAttributes,format);
	}
}
