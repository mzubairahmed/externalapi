package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.asi.ext.api.integration.lookup.parser.CriteriaSetParser;
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.ProductCriteriaSet;


public class OptionLookup {
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser(); 
	public ConcurrentHashMap<String,ArrayList<String>> findOptionValueDetails(
			ConcurrentHashMap<String,ArrayList<String>> optionList,
			String criteriaCode,
			@SuppressWarnings("rawtypes") LinkedList<LinkedHashMap> optionElementsResponse,
			ProductCriteriaSet productCriteriaSet,String externalId) {
		if(optionList==null)
			optionList=new ConcurrentHashMap<>();
		String canOrderOnlyOne = "", reqForOrder = "", optionName = "", optionValue = "";
		ArrayList<CriteriaSetValue> criteriaSetValueLst = (ArrayList<CriteriaSetValue>) productCriteriaSet
				.getCriteriaSetValues();
		int cntr=0;
		ArrayList<String> tempArrayList=new ArrayList<>();
		String optionTyp="",checkOption="",crntOptionVal="";
		for (CriteriaSetValue criteriaSetValue : criteriaSetValueLst) {
			tempArrayList=new ArrayList<>();
			optionTyp=criteriaCode;
			criteriaSetParser.addReferenceSet(externalId,criteriaCode,criteriaSetValue.getID(),(criteriaSetValue.getValue() instanceof String)?productCriteriaSet.getCriteriaDetail()+":"+criteriaSetValue.getValue().toString():"");
				canOrderOnlyOne = (productCriteriaSet.getIsMultipleChoiceAllowed())?"Y":"N";
				reqForOrder = (productCriteriaSet.getIsRequiredForOrder())?"Y":"N";
			optionName = productCriteriaSet.getCriteriaDetail();
			checkOption=optionTyp+"_"+optionName;
			if (criteriaSetValue.getValue() instanceof String)
			{
				crntOptionVal=criteriaSetValue.getValue().toString();
				if(cntr==0)
				{
					optionValue=crntOptionVal;
					tempArrayList.add(0,crntOptionVal);
					tempArrayList.add(1,canOrderOnlyOne);
					tempArrayList.add(2,reqForOrder);
					optionList.put(checkOption,tempArrayList);
					//optionList.put(checkOption,(null!=optionList.get(checkOption))?optionList.get(checkOption)+","+crntOptionVal:crntOptionVal);
				}
				else
				{
					if(null!=optionList.get(checkOption))
					{
						tempArrayList.add(0, optionList.get(checkOption).get(0)+","+crntOptionVal);
						tempArrayList.add(1,canOrderOnlyOne);
						tempArrayList.add(2,reqForOrder);
						optionList.put(checkOption,tempArrayList);
					}
					else
					{
						//optionValue+="||"+crntOptionVal;
						tempArrayList.add(0, crntOptionVal);
						tempArrayList.add(1,canOrderOnlyOne);
						tempArrayList.add(2,reqForOrder);
						optionList.put(checkOption,tempArrayList);
					}
				}
				//optionValue = (cntr==0)?criteriaSetValue.getValue().toString():optionValue+"||"+criteriaSetValue.getValue().toString();
				//optionList.put(optionTyp+"_"+optionName, optionValue);
			}
			else
			{
				//optionValue = (cntr==0)?"":optionValue+"||"+"";
				tempArrayList.add(0, optionValue);
				tempArrayList.add(1,canOrderOnlyOne);
				tempArrayList.add(2,reqForOrder);
				optionList.put(optionTyp+"_"+optionName, tempArrayList);
			}
			cntr++;
			tempArrayList=null;
		}
		return optionList;
	}
}
