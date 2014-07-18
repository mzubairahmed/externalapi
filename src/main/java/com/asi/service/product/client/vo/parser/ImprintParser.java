package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.vo.CriteriaSetRelationships;
import com.asi.service.product.client.vo.CriteriaSetValuePath;
import com.asi.service.product.client.vo.CriteriaSetValuePaths;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Relationship;
import com.asi.service.product.client.vo.Relationships;
import com.asi.service.product.vo.ImprintMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImprintParser {
	@Autowired CriteriaSetParser criteriaLookupParser;
	@Autowired LookupParser lookupsParser;
	public static ConcurrentHashMap<String, String> imprintRelationMap = null;
	private int criteriaSetValuePathId=-1;
   

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
	public com.asi.service.product.client.vo.Product setImprintRelations(
			List<ImprintMethod> imprintMethodList,
			ProductDetail currentProductDetails,
			com.asi.service.product.client.vo.Product productToUpdate) {
		List<Relationships> relationshipsList=new ArrayList<>();
		//ConcurrentHashMap<String, HashMap<String, String>> currentCriteriaSetIds=criteriaLookupParser.getCriteriaSetDetailsByExternalId();
		ProductCriteriaSets imprintCriteriaSet=lookupsParser.getCriteriaSetBasedOnCriteriaCode(productToUpdate.getProductConfigurations()[0].getProductCriteriaSets(), "IMMD");
		ProductCriteriaSets artworkCriteriaSet=lookupsParser.getCriteriaSetBasedOnCriteriaCode(productToUpdate.getProductConfigurations()[0].getProductCriteriaSets(), "ARTW");
		ProductCriteriaSets minOrderCriteriaSet=lookupsParser.getCriteriaSetBasedOnCriteriaCode(productToUpdate.getProductConfigurations()[0].getProductCriteriaSets(), "MINO");
		String imprintCriteriaSetId="",artworkCriteriaSetId="",minOrderSetId="";
		boolean isArtwork=false;
		int relationshipId=-1;
		criteriaSetValuePathId--;
		for(ImprintMethod currentImprintMethod:imprintMethodList){
			Relationships individualRelationShip = new Relationships();
			individualRelationShip.setId(String.valueOf(relationshipId));
	        if (isArtwork) {
	        	individualRelationShip.setName("Imprint Method x Artwork");
	        } else {
	        	individualRelationShip.setName("Imprint Method x Min Order");
	        }
	        individualRelationShip.setProductId(String.valueOf(currentProductDetails.getID()));
		// Iterate through all user given imprint Methods
		// Check its related artwork and Min order existence with current product details, if not exist then add
			imprintCriteriaSetId=criteriaLookupParser.getCriteriaSetId(productToUpdate.getExternalProductId(), productToUpdate.getProductConfigurations()[0].getProductCriteriaSets(),"IMMD",currentImprintMethod.getMethodName());
			//artworkCriteriaSetId=criteriaLookupParser.getCriteriaSetId(productToUpdate.getExternalProductId(), productToUpdate.getProductConfigurations()[0].getProductCriteriaSets(),"ARTW",currentImprintMethod.getMethodName());
			minOrderSetId=criteriaLookupParser.getCriteriaSetId(productToUpdate.getExternalProductId(), productToUpdate.getProductConfigurations()[0].getProductCriteriaSets(),"MINO",currentImprintMethod.getMinimumOrder());
			individualRelationShip=createImprintRelationShip(individualRelationShip,imprintCriteriaSet.getCriteriaSetId(),minOrderCriteriaSet.getCriteriaSetId(),imprintCriteriaSetId,minOrderSetId,relationshipId,isArtwork,String.valueOf(currentProductDetails.getID()));
			relationshipsList.add(individualRelationShip);
			relationshipId--;
		}
		productToUpdate.setRelationships(relationshipsList);
		return productToUpdate;
	}
	private Relationships createImprintRelationShip(Relationships relationships,String relationshipImprintId,String relationshipSetId,String imprintCriteriaSetValueId,String relationCriteriaSetValueId,int relationshipId,boolean isArtwork,String productId)
	{
		
		List<CriteriaSetRelationships> criteriaSetRelationshipsList=new ArrayList<>();
		CriteriaSetRelationships imprintCriteriaSetRelationship=new CriteriaSetRelationships();
		CriteriaSetRelationships relationCriteriaSetRelationship=new CriteriaSetRelationships();
		List<CriteriaSetValuePaths> criteriaSetValuePaths=new ArrayList<>();
		CriteriaSetValuePaths imprintCriteriaSetValuePaths=new CriteriaSetValuePaths();
		CriteriaSetValuePaths relationCriteriaSetValuePaths=new CriteriaSetValuePaths();
	    relationships.setParentCriteriaSetId(relationshipImprintId);
	    imprintCriteriaSetRelationship.setIsParent("true");
	    imprintCriteriaSetRelationship.setRelationshipId(String.valueOf(relationshipId));
	    imprintCriteriaSetRelationship.setProductId(productId);
	    imprintCriteriaSetRelationship.setCriteriaSetId(relationshipImprintId);
        criteriaSetRelationshipsList.add(imprintCriteriaSetRelationship);
        relationCriteriaSetRelationship = new CriteriaSetRelationships();
        relationCriteriaSetRelationship.setProductId(productId);
        relationCriteriaSetRelationship.setRelationshipId(String.valueOf(relationshipId));
        relationCriteriaSetRelationship.setCriteriaSetId(relationshipSetId);
        relationCriteriaSetRelationship.setIsParent("false");
        criteriaSetRelationshipsList.add(relationCriteriaSetRelationship);
        relationships.setCriteriaSetRelationships(criteriaSetRelationshipsList);
        
        imprintCriteriaSetValuePaths.setRelationshipId(String.valueOf(relationshipId));
        imprintCriteriaSetValuePaths.setIsParent("true");
        imprintCriteriaSetValuePaths.setProductId(productId);
        imprintCriteriaSetValuePaths.setCriteriaSetValueId(imprintCriteriaSetValueId);
        imprintCriteriaSetValuePaths.setId(String.valueOf(criteriaSetValuePathId));

        criteriaSetValuePaths.add(imprintCriteriaSetValuePaths);   
        
        relationCriteriaSetValuePaths.setRelationshipId(String.valueOf(relationshipId));
        relationCriteriaSetValuePaths.setIsParent("false");
        relationCriteriaSetValuePaths.setProductId(productId);
        relationCriteriaSetValuePaths.setCriteriaSetValueId(relationCriteriaSetValueId);
        relationCriteriaSetValuePaths.setId(String.valueOf(criteriaSetValuePathId));
        criteriaSetValuePaths.add(relationCriteriaSetValuePaths); 
        relationships.setCriteriaSetValuePaths(criteriaSetValuePaths);
		return relationships;
	}	
}
