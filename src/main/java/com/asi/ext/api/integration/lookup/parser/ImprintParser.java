package com.asi.ext.api.integration.lookup.parser;

import java.util.HashMap;
import java.util.List;

import com.asi.service.product.client.vo.CriteriaSetValuePath;

public class ImprintParser {
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser(); 
	public void updateCriteriaValuePathsByParent(String externalId,
			List<CriteriaSetValuePath> criteriaSetValuePaths) {
		List<CriteriaSetValuePath> relationCriteriaSetValuePaths=criteriaSetValuePaths;
		for(CriteriaSetValuePath criteriaSetValuePath: criteriaSetValuePaths)
		{
			if(criteriaSetValuePath.getIsParent().toString().equals("true"))
			{
				for(CriteriaSetValuePath relationCriteria:relationCriteriaSetValuePaths)
				{
					if(relationCriteria.getIsParent().toString().equals("false")&&relationCriteria.getID().toString().equalsIgnoreCase(criteriaSetValuePath.getID().toString()))
					{
						if(null!=LookupParser.imprintRelationMap.get(criteriaSetValuePath.getCriteriaSetValueId()+""))
						{
							LookupParser.imprintRelationMap.put(criteriaSetValuePath.getCriteriaSetValueId()+"",LookupParser.imprintRelationMap.get(criteriaSetValuePath.getCriteriaSetValueId().toString())+","+relationCriteria.getCriteriaSetValueId());
						}
						else
						LookupParser.imprintRelationMap.put(criteriaSetValuePath.getCriteriaSetValueId()+"",relationCriteria.getCriteriaSetValueId()+"");
					}
				}				
			}
		}
		//return imprintMethodsList;
	}

	public String[] getImprintRelations(String[] relationValueAry,String externalId) {
		String[] minArtwork=new String[]{"",""};
		String imprintRelation="";
		String[] relationCriteraSet=null;
		for(int relationCntr=0;relationCntr<relationValueAry.length;relationCntr++)
		{
			imprintRelation=criteriaSetParser.findCriteriaSetMapValueById(externalId).get(relationValueAry[relationCntr]);
			if(null!=imprintRelation)
			{
			relationCriteraSet=imprintRelation.split("__");
			if(null!=relationCriteraSet && relationCriteraSet.length==2)
			{
				if(relationCriteraSet[0].equalsIgnoreCase("MINO"))
				{
					minArtwork[0]=relationCriteraSet[1];
				}
				else if(relationCriteraSet[0].equalsIgnoreCase("ARTW"))
				{
					if(relationCntr==0)
						minArtwork[1]=relationCriteraSet[1];
					else
					{
						if(minArtwork[1].isEmpty())
							minArtwork[1]=relationCriteraSet[1];
						else
							minArtwork[1]+=","+relationCriteraSet[1];
					}
				}
			}
			}
		}
		return minArtwork;
	}

	public boolean checkImprintMethod(String imprintMethod,String externalId) {
		HashMap<String,String> imprintMethodReference=criteriaSetParser.findCriteriaSetMapValueById(externalId);
		String srchValue="",criteriaValue="";
		if(null!=imprintMethodReference && !imprintMethodReference.isEmpty())
		{
		for(String criteriaDetails : imprintMethodReference.keySet()) {
			criteriaValue=imprintMethodReference.get(criteriaDetails);
			if(criteriaValue.startsWith("IMMD"))
			{
				srchValue=criteriaValue.substring(criteriaValue.indexOf("__")+2);
				if(srchValue.equalsIgnoreCase(imprintMethod))
					return true;
			}
		}
		}
		return false;
	}
}
