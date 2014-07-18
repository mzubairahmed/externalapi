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
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.ProductConfigurationsList;



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
					LinkedList<LinkedHashMap> unitValuesList = (LinkedList<LinkedHashMap>) sizeIndividualLookupMap.get("UnitsOfMeasure");
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
	public ProductConfigurationsList findSizeValueDetails(ProductConfigurationsList productConfigurationsList,String criteriaCode,@SuppressWarnings("rawtypes") LinkedList<LinkedHashMap> criteriaAttributes,
			List<CriteriaSetValue> criteriaSetValueLst,String externalProductId) {
		//String[] stringAry=new String[2];
	
			if(criteriaCode.equalsIgnoreCase("DIMS"))
				productConfigurationsList.setSizeGroup("Dimension");
			else if(criteriaCode.equalsIgnoreCase("CAPS"))
				productConfigurationsList.setSizeGroup("Capacity");
			else if(criteriaCode.equalsIgnoreCase("SVWT"))
				productConfigurationsList.setSizeGroup("Volume/Weight");
			else if(criteriaCode.equalsIgnoreCase("SABR"))
				productConfigurationsList.setSizeGroup("Apparel - Bra Sizes");
			else if(criteriaCode.equalsIgnoreCase("SAHU"))
				productConfigurationsList.setSizeGroup("Apparel - Hosiery/Uniform Sizes");
			else if(criteriaCode.equalsIgnoreCase("SAIT"))
				productConfigurationsList.setSizeGroup("Apparel - Infant & Toddler");
			else if(criteriaCode.equalsIgnoreCase("SANS"))
				productConfigurationsList.setSizeGroup("Apparel - Dress Shirt Sizes");
			else if(criteriaCode.equalsIgnoreCase("SAWI"))
				productConfigurationsList.setSizeGroup("Apparel - Pants Sizes");
			else if(criteriaCode.equalsIgnoreCase("SSNM"))
				productConfigurationsList.setSizeGroup("Standard & Numbered");
			else if(criteriaCode.equalsIgnoreCase("SOTH"))
				productConfigurationsList.setSizeGroup("Other");
			
	
			String sizeValue="",finalSizeValue="",delim="";
			String sizeElementValue="";
			//int noOfSizes=criteriaSetValueLst.size();
			int elementsCntr=0;
			String unitOfmeasureCode="";
			for(CriteriaSetValue criteriaSetValue:criteriaSetValueLst)
			{
				int sizeCntr=0;
				if(criteriaSetValue.getValue() instanceof List)
				{
				ArrayList<?> valueList=(ArrayList<?>)criteriaSetValue.getValue();
				Iterator<?> sizeValuesItr=valueList.iterator();
				
				while(sizeValuesItr.hasNext())
				{
					LinkedHashMap<?, ?> valueMap=(LinkedHashMap<?, ?>)sizeValuesItr.next();
					unitOfmeasureCode=getSizesElementValue("UNITS", criteriaAttributes,valueMap.get("UnitOfMeasureCode").toString());
					if(unitOfmeasureCode.equals("\"")) unitOfmeasureCode="in";
					if(unitOfmeasureCode.equals("'")) unitOfmeasureCode="ft";
					if(criteriaCode.equalsIgnoreCase("DIMS") || criteriaCode.equalsIgnoreCase("SDIM"))
					{
						if(criteriaCode.equalsIgnoreCase("DIMS"))
						sizeValue=getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString())+":"+valueMap.get("UnitValue")+":"
								+unitOfmeasureCode;
						else
						sizeValue=valueMap.get("UnitValue")+":"+unitOfmeasureCode;
						delim="; ";
					}
					else if(criteriaCode.equalsIgnoreCase("CAPS") || criteriaCode.equalsIgnoreCase("SVWT") || criteriaCode.equalsIgnoreCase("SHWT"))
						{
						sizeValue=valueMap.get("UnitValue")+":"
								+unitOfmeasureCode;
						delim=": ";
						}
					else 
					{
						if(criteriaCode.equalsIgnoreCase("SAWI"))
						{
							if(getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString()).equals("Waist")) 
								sizeValue=valueMap.get("UnitValue").toString();
							else if(getSizesElementValue("ID", criteriaAttributes, valueMap.get("CriteriaAttributeId").toString()).equals("Inseam")) sizeValue="x"+valueMap.get("UnitValue").toString();
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
					}
					
					if(sizeCntr!=0)
					{
						if(criteriaCode.equalsIgnoreCase("SANS"))
							sizeElementValue+="("+sizeValue.trim()+")";
							else sizeElementValue+=delim+sizeValue;
					}
					
					else
						sizeElementValue+=sizeValue;
					sizeCntr++;
				}		
				criteriaSetParser.addReferenceSet(externalProductId,criteriaCode,criteriaSetValue.getID(),sizeElementValue);
				}else
				{
						sizeValue=criteriaSetValue.getBaseLookupValue();
						if(null==sizeValue)
							sizeValue=criteriaSetValue.getFormatValue();
						criteriaSetParser.addReferenceSet(externalProductId,criteriaSetValue.getCriteriaCode(),criteriaSetValue.getID(),sizeValue);
						sizeElementValue+=sizeValue;
					sizeCntr++;
				}
				if(elementsCntr!=0)
				{
					finalSizeValue=finalSizeValue+","+sizeElementValue.trim();
				}else
					finalSizeValue=sizeElementValue.trim();
				sizeElementValue="";
				elementsCntr++;
			}
			if(criteriaCode.equalsIgnoreCase("SHWT"))
				productConfigurationsList.setShippingWeight(finalSizeValue);
			else if(criteriaCode.equalsIgnoreCase("SDIM"))
				productConfigurationsList.setShippingDimension(finalSizeValue);
			else 
				productConfigurationsList.setSizeValue(finalSizeValue);	
		return productConfigurationsList;
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
