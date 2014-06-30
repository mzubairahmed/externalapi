package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.vo.CriteriaSetValuePath;
import com.asi.service.product.client.vo.ProductCriteriaSet;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.Relationship;
import com.asi.service.product.vo.ImprintMethod;

public class ImprintParser {
	@Autowired CriteriaSetParser criteriaLookupParser;
	@Autowired LookupParser lookupsParser;
	public static ConcurrentHashMap<String, String> imprintRelationMap = null;
	 /**
     * Find a criteriaSet from the productCriteria set array based on the criteria code
     * 
     * @param productCriteriaSetsAry
     *            is the array contains all criteria set of the product
     * @param criteriaCode
     *            is the criteria code of the criteriaSet to find
     * @return the matched {@linkplain ProductCriteriaSets } or null
     */
    public ProductCriteriaSets getCriteriaSetBasedOnCriteriaCode(List<ProductCriteriaSets> productCriteriaSetsAry, String criteriaCode) {
        for (ProductCriteriaSets currentProductCriteriaSet: productCriteriaSetsAry)
        	{
        		if (null != currentProductCriteriaSet && currentProductCriteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode.trim()))
        			return currentProductCriteriaSet;
        	}
        return null;
    }

	public List<ImprintMethod> getImprintMethodRelations(String xid,Integer imprintCriteriaSetId,
			List<ProductCriteriaSets> productCriteriaSets, List<Relationship> relationshipList) {
		List<ImprintMethod> imprintMethodList=new ArrayList<>();
		ImprintMethod imprintMethod=null;
	/*	String currentCriteriaSet=null;
		String criteriaCode="";
		String unitValue="";*/
		String critieraValue="";
		imprintRelationMap=new ConcurrentHashMap<>();
		ConcurrentHashMap<String, HashMap<String, String>> currentCriteriaSetIds=criteriaLookupParser.getCriteriaSetDetailsByExternalId(xid, productCriteriaSets);
		if(null!=currentCriteriaSetIds){
				for(Relationship currentRelationship:relationshipList){
					updateCriteriaValuePathsByParent(xid,currentRelationship.getCriteriaSetValuePaths());
				}		
				if(null!=imprintRelationMap && !imprintRelationMap.isEmpty()){
					String[] imprintRelations=null;
					String crntRelationCode="";
					for(String crntImprintMethod : imprintRelationMap.keySet()) {
						imprintMethod=new ImprintMethod();
						imprintRelations=imprintRelationMap.get(crntImprintMethod).split(",");
						crntImprintMethod=criteriaLookupParser.findCriteriaSetValueById(xid,crntImprintMethod);
						imprintMethod.setMethodName(crntImprintMethod.substring(crntImprintMethod.indexOf("__")+2));
						for(String crntRelation:imprintRelations)
						{
							crntRelation=criteriaLookupParser.findCriteriaSetValueById(xid,crntRelation);
							crntRelationCode=crntRelation.substring(0,crntRelation.indexOf("__"));
							if(crntRelationCode.equalsIgnoreCase("MINO")){
								/*unitValue=crntRelation.substring(crntRelation.lastIndexOf(":")+1);
								imprintMethod.setUnit(unitValue);
								critieraValue=crntRelation.substring(crntRelation.indexOf(":")+1);
								if(critieraValue.contains(":"))*/
								imprintMethod.setMinimumOrder(crntRelation.substring(crntRelation.indexOf("__")+2));
								//System.out.println("");
							}else{
								critieraValue=crntRelation.substring(crntRelation.indexOf("__")+2);
								if(null!=imprintMethod.getArtworkName() && !imprintMethod.getArtworkName().isEmpty())
									imprintMethod.setArtworkName(imprintMethod.getArtworkName()+","+lookupsParser.getArtworkNameByCode(critieraValue));
								else
										imprintMethod.setArtworkName(lookupsParser.getArtworkNameByCode(critieraValue));
							}
						}
						imprintMethodList.add(imprintMethod);
				}
				}
			}
		return imprintMethodList;
	}
	public void updateCriteriaValuePathsByParent(String externalId,
			List<CriteriaSetValuePath> criteriaSetValuePaths) {
		List<CriteriaSetValuePath> relationCriteriaSetValuePaths=criteriaSetValuePaths;
		for(CriteriaSetValuePath criteriaSetValuePath: criteriaSetValuePaths)
		{
			if(null!=criteriaSetValuePath.getIsParent() && criteriaSetValuePath.getIsParent().toString().equals("true"))
			{
				for(CriteriaSetValuePath relationCriteria:relationCriteriaSetValuePaths)
				{
					if(relationCriteria.getIsParent().toString().equals("false")&&relationCriteria.getID().toString().equalsIgnoreCase(criteriaSetValuePath.getID().toString()))
					{
						if(null!=imprintRelationMap.get(criteriaSetValuePath.getCriteriaSetValueId()+""))
						{
							imprintRelationMap.put(criteriaSetValuePath.getCriteriaSetValueId()+"",imprintRelationMap.get(criteriaSetValuePath.getCriteriaSetValueId().toString())+","+relationCriteria.getCriteriaSetValueId());
						}
						else
						imprintRelationMap.put(criteriaSetValuePath.getCriteriaSetValueId()+"",relationCriteria.getCriteriaSetValueId()+"");
					}
				}				
			}
		}
		//return imprintMethodsList;
	}
	public CriteriaSetParser getCriteriaLookupParser() {
		return criteriaLookupParser;
	}

	public void setCriteriaLookupParser(CriteriaSetParser criteriaLookupParser) {
		this.criteriaLookupParser = criteriaLookupParser;
	}
	public LookupParser getLookupsParser() {
		return lookupsParser;
	}

	public void setLookupsParser(LookupParser lookupsParser) {
		this.lookupsParser = lookupsParser;
	}
}
