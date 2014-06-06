package com.asi.core.repo.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asi.service.lookup.vo.AsiColor;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.Category;
import com.asi.service.lookup.vo.ColorHue;
import com.asi.service.lookup.vo.Colors;
import com.asi.service.lookup.vo.Size;
import com.asi.service.lookup.vo.SizeInfo;
import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.vo.colors.CodeValueGroup;
import com.asi.service.product.client.vo.colors.SetCodeValue;
import com.asi.service.product.vo.Constrain;
import com.asi.service.product.vo.CountryOfOrigin;
import com.asi.service.product.vo.OriginOfCountries;

@Component
public class LookupValuesRepo {
	@Autowired LookupValuesClient lookupClient;
	private static Logger _LOGGER = LoggerFactory.getLogger(LookupValuesRepo.class);
	@SuppressWarnings("unchecked")
	public List<AsiColor> getColors()
	{
		List<AsiColor> asiColorList = new ArrayList<AsiColor>();
		ArrayList<?> colorLookupResponse = lookupClient.getColorFromLookup(lookupClient.getLookupColorURL());
		Iterator<?> iter = colorLookupResponse.iterator();
		while (iter.hasNext()) {
			Map<?, ?> crntColorJson = (LinkedHashMap<?, ?>) iter.next();
			AsiColor asiColor = new AsiColor();
			asiColor.setDisplayName((String)crntColorJson.get("DisplayName"));
			asiColor.setDescription((String)crntColorJson.get("Description"));
			if(crntColorJson.containsKey("CodeValueGroups"))
			{
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
	
	public OriginOfCountries getOrigin()
	{
		List<?> serviceOrigin = lookupClient.getOriginFromLookup(lookupClient.getOriginLookupURL());
		List<CountryOfOrigin> countryOfOrigin = new ArrayList<CountryOfOrigin>();
		Iterator<LinkedHashMap> origins = (Iterator<LinkedHashMap>) serviceOrigin.iterator();
		while (origins.hasNext()) {
			Map<?, ?> origin = (LinkedHashMap<?, ?>) origins.next();
			CountryOfOrigin country = new CountryOfOrigin();
			country.setId(origin.get("ID").toString());
			country.setCountryOfOrigin(origin.get("CodeValue").toString());
			countryOfOrigin.add(country);
		}
		OriginOfCountries originofCountries = new OriginOfCountries();
		originofCountries.setOriginOfCountryList(countryOfOrigin);
		return originofCountries;
	}
	
	public SizeInfo getSizeInfo()
	{
		SizeInfo sizeInfo = new SizeInfo();
		List<Size> sizes= new ArrayList<Size>();
		ArrayList<LinkedHashMap> sizesFromService = lookupClient.getSizesFromLookup(lookupClient.getLookupSizeURL());
		Iterator<?> sizeIterator=  sizesFromService.iterator();
		while(sizeIterator.hasNext())
		{
			
			Map<?, ?> sizeMap = (LinkedHashMap<?, ?>)sizeIterator.next();
			Size size = new Size();
			size = makeSize(sizeMap,size);
			ArrayList<?> codeValueGroupsList = (ArrayList<?>) sizeMap.get("CodeValueGroups");
			Iterator<LinkedHashMap> codeValueGroupsIterator  = (Iterator<LinkedHashMap>) codeValueGroupsList.iterator();
			while(codeValueGroupsIterator.hasNext())
			{
				Map<?, ?> codeValueGroupsMap = (LinkedHashMap<?, ?>)codeValueGroupsIterator.next();
				List<Size> childSizes= new ArrayList<Size>();
				Size childSize = new Size();
				childSize = makeSize(codeValueGroupsMap,childSize);
				if(codeValueGroupsMap.containsKey("SetCodeValues"))
				{
					Map<?,?> setCodeValuesMap = (Map<?,?>) sizeMap.get("SetCodeValues");
					Size actualSize = makeSize(setCodeValuesMap,childSize);
				}
				
				childSizes.add(childSize);
				
				size.setSizeList(childSizes);
				
			}
			sizes.add(size);
			
		}
		sizeInfo.setSizes(sizes);
		return sizeInfo;
	}
	private Size makeSize(Map<?,?> sizeMap,Size size)
	{
		if(sizeMap.containsKey("ID"))
			size.setId((String) sizeMap.get("ID"));
		if(sizeMap.containsKey("Code"))
			size.setSizeCode((String) sizeMap.get("Code"));
		if(sizeMap.containsKey("Description"))
			size.setDescription((String) sizeMap.get("Description"));
		if(sizeMap.containsKey("CodeValue") && (size.getDescription().isEmpty() || null==size.getDescription()))
			size.setDescription((String) sizeMap.get("CodeValue"));
		if(sizeMap.containsKey("DisplayName"))
			size.setDisplayText((String) sizeMap.get("DisplayName"));
		
		Constrain constrain = new Constrain();
		if(sizeMap.containsKey("IsAllowBasePrice"))
			constrain.setAllowBasePrice((Boolean) sizeMap.get("IsAllowBasePrice"));		
		if(sizeMap.containsKey("IsAllowUpcharge"))
			constrain.setAllowUpcharge((Boolean)sizeMap.get("IsAllowUpcharge"));
		if(sizeMap.containsKey("IsProductNumberAssignmentAllowed"))
			constrain.setProductNumberAssignmentAllowed((Boolean)sizeMap.get("IsProductNumberAssignmentAllowed"));
		if(sizeMap.containsKey("IsMediaAssignmentAllowed"))
			constrain.setMediaAssignmentAllowed((Boolean)sizeMap.get("IsMediaAssignmentAllowed"));
		if(sizeMap.containsKey("IsFlag"))
			constrain.setFlag((Boolean)sizeMap.get("IsFlag"));
		size.setConstrains(constrain);
		return size;
	}
	@SuppressWarnings({ "unchecked" })
	public CategoriesList getAllCategories() {
		CategoriesList categoriesList = new CategoriesList();
		Category category=null;
		List<Category> categoryArrayList= new ArrayList<Category>();
		LinkedHashMap<String,String> currentHashMap=new LinkedHashMap<>();
		ArrayList<LinkedHashMap<String,String>> categoriesFromService = lookupClient.getCategoriesFromLookup(lookupClient.getLookupCategoryURL());
		Iterator<?> categoriesIterator=  categoriesFromService.iterator();
		while(categoriesIterator.hasNext())
		{
			currentHashMap=(LinkedHashMap)categoriesIterator.next();
			category=new Category();
			if(currentHashMap.containsKey("Code"))
				category.setCode(currentHashMap.get("Code"));
			if(currentHashMap.containsKey("Description"))
				category.setDescription(currentHashMap.get("Description"));
			if(currentHashMap.containsKey("Name"))
				category.setName(currentHashMap.get("Name"));
			if(currentHashMap.containsKey("DisplayName"))
				category.setDisplayName(currentHashMap.get("DisplayName"));
			if(currentHashMap.containsKey("ParentCategoryCode"))
				category.setParentCategoryCode(currentHashMap.get("ParentCategoryCode"));
			if(currentHashMap.containsKey("ProductTypeCode"))
				category.setProductTypeCode(currentHashMap.get("ProductTypeCode"));
			if(currentHashMap.containsKey("IsProductTypeSpecific"))
				category.setIsProductTypeSpecific(String.valueOf(currentHashMap.get("IsProductTypeSpecific")));
			if(currentHashMap.containsKey("IsAllowsAssign"))
				category.setIsAllowsAssign(String.valueOf(currentHashMap.get("IsAllowsAssign")));
			if(currentHashMap.containsKey("IsParent"))
				category.setIsParent(String.valueOf(currentHashMap.get("IsParent")));
			if(currentHashMap.containsKey("IsPrimary"))
				category.setIsPrimary(String.valueOf(currentHashMap.get("IsPrimary")));
			categoryArrayList.add(category);			
		}
		categoriesList.setSizes(categoryArrayList);
		return categoriesList;
	}
}
