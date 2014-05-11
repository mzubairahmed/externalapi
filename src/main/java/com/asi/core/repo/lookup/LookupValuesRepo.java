package com.asi.core.repo.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asi.service.lookup.vo.AsiColor;
import com.asi.service.lookup.vo.ColorHue;
import com.asi.service.lookup.vo.Colors;
import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.vo.colors.CodeValueGroup;
import com.asi.service.product.client.vo.colors.SetCodeValue;

@Component
public class LookupValuesRepo {
	@Autowired LookupValuesClient lookupClient;
	
	@SuppressWarnings("unchecked")
	public List<AsiColor> getColors()
	{
		List<AsiColor> asiColorList = new ArrayList<AsiColor>();
		ArrayList<?> colorLookupResponse = lookupClient.getColor();
		Iterator<?> iter = colorLookupResponse.iterator();
		while (iter.hasNext()) {
			Map<?, ?> crntColorJson = (LinkedHashMap<?, ?>) iter.next();
			AsiColor asiColor = new AsiColor();
			asiColor.setDisplayName((String)crntColorJson.get("DisplayName"));
			asiColor.setDescription((String)crntColorJson.get("Description"));
			
			@SuppressWarnings({ "rawtypes" })
			ArrayList<LinkedHashMap> codeValueGrps = (ArrayList<LinkedHashMap>) crntColorJson.get("CodeValueGroups");
			@SuppressWarnings("rawtypes")
			Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();
			List<Colors> colorsList = new ArrayList<Colors>();

			while (iterator.hasNext()) {
				Colors colors = new Colors();
				@SuppressWarnings({ "rawtypes" })
				Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
				if (null != codeValueGrpsMap.get("DisplayName"))
				{
					colors.setCode(codeValueGrpsMap.get("Code").toString()); 
					colors.setDisplayName(codeValueGrpsMap.get("DisplayName").toString());
					colors.setDescription(codeValueGrpsMap.get("Description").toString());
				}
				@SuppressWarnings("rawtypes")
				ArrayList finalLst = (ArrayList) codeValueGrpsMap.get("SetCodeValues");
				@SuppressWarnings("rawtypes")
				Iterator finalItr = finalLst.iterator();
				if (finalItr.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map finalMap = (LinkedHashMap) finalItr.next();
					// LOGGER.info("ID:"+finalMap.get("ID"));
					String colorID = finalMap.get("ID").toString();
					colors.setiD(colorID);
				}
				@SuppressWarnings("rawtypes")
				LinkedHashMap  colorHueMap = (LinkedHashMap) codeValueGrpsMap.get("ColorHue");
				String hueCode="";
				String hueDescription="";
				String hueDisplayName=""; 
				if(colorHueMap.containsKey("Code"))
					hueCode = colorHueMap.get("Code").toString();
				if(colorHueMap.containsKey("Code"))
					hueDescription = colorHueMap.get("Description").toString();
				if(colorHueMap.containsKey("Code"))
					hueDisplayName = colorHueMap.get("DisplayName").toString();
					ColorHue colorHue = new ColorHue();					
					colorHue.setCode(hueCode);
					colorHue.setDescription(hueDescription);
					colorHue.setDisplayName(hueDisplayName);
					colors.setColorHue(colorHue);
				
				colorsList.add(colors);
			}
			asiColor.setColor(colorsList);
			asiColorList.add(asiColor);
		}	
		

		return asiColorList;
		
	}
	@SuppressWarnings("unused")
	private List<Colors> getColorsForColor(List<CodeValueGroup> codeValueGroups)
	{
		List<Colors> colorsList = new ArrayList<Colors>();
		ListIterator<CodeValueGroup> codeValueGroupsItr = codeValueGroups.listIterator();
		while(codeValueGroupsItr.hasNext()){
			
			CodeValueGroup codeValueGroup = codeValueGroupsItr.next();
			Colors colors =getColorValues(codeValueGroup.getSetCodeValues());
			colors.setDescription(codeValueGroup.getDescription());
			colors.setDisplayName(codeValueGroup.getDisplayName());
		}
		
		return colorsList;
	}
	
	private Colors getColorValues(List<SetCodeValue> setCodeValues)
	{
		ListIterator<SetCodeValue> codeValuesItr = setCodeValues.listIterator();
		Colors colors = new Colors();
		while(codeValuesItr.hasNext()){
			SetCodeValue codeValue = codeValuesItr.next();
			colors.setiD(codeValue.getID().toString());			
		}
		return colors;
	}
}
