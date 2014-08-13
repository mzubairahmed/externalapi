package com.asi.ext.api.integration.lookup.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Availability;
import com.asi.ext.api.service.model.AvailableVariations;
import com.asi.service.product.client.vo.CriteriaSetRelationship;
import com.asi.service.product.client.vo.CriteriaSetRelationships;
import com.asi.service.product.client.vo.CriteriaSetValuePath;
import com.asi.service.product.client.vo.CriteriaSetValuePaths;
import com.asi.service.product.client.vo.Relationship;
import com.asi.service.product.client.vo.Relationships;
import com.asi.service.product.vo.ImprintMethod;

public class RelationshipParser {
	
	public static final String[] SIZE_GROUP_CRITERIACODES={"CAPS","DIMS","SABR","SAHU","SAIT","SANS","SAWI","SSNM","SVWT","SOTH"};
	private static final String[] OPTIONS_CRITERIACODES = {"SHOP","PROP","IMOP"};
	public void mergeRelationships() {
		
	}
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser();	
	 public List<CriteriaSetValuePaths> getCriteriaSetValuePaths(ImprintMethod immdMethod, String criteriaCode, String relationId, String productId) {
		 String[] values = null;
		 
		 if (criteriaCode.equalsIgnoreCase("ARTW") && immdMethod.getArtworkName() != null) {
			 values = immdMethod.getArtworkName().split(",");
		 } else if (criteriaCode.equalsIgnoreCase("MINO") && immdMethod.getMinimumOrder() != null) {
			 values = immdMethod.getMinimumOrder().split(",");
		 }
		 
		 List<CriteriaSetValuePaths> finalCriteriaSetValuePaths = new ArrayList<CriteriaSetValuePaths>();
		 if (values != null && values.length > 0) {
			 int counter = 0;
			 for (String childCrtId : values) {
				 CriteriaSetValuePaths parentCrtValuePath = new CriteriaSetValuePaths();
				 
				 parentCrtValuePath.setId(String.valueOf(--counter));
				 parentCrtValuePath.setIsParent("true");
				 parentCrtValuePath.setRelationshipId(relationId);
				 parentCrtValuePath.setCriteriaSetValueId(immdMethod.getMethodName());
				 parentCrtValuePath.setProductId(productId);
				 
				 CriteriaSetValuePaths childCrtValuePath = new CriteriaSetValuePaths();
				 
				 childCrtValuePath.setId(String.valueOf(counter));
				 childCrtValuePath.setRelationshipId(relationId);
				 childCrtValuePath.setIsParent("false");
				 childCrtValuePath.setCriteriaSetValueId(childCrtId);
				 childCrtValuePath.setProductId(productId);
				 
				 finalCriteriaSetValuePaths.add(parentCrtValuePath);
				 finalCriteriaSetValuePaths.add(childCrtValuePath);
				 
			 }
			 return finalCriteriaSetValuePaths;
		 }
		 return finalCriteriaSetValuePaths;
	 }
	 
	 public List<CriteriaSetRelationships> getCriteriaSetRelationships(String productId, String parentId, String childId, String relationId) {
		List<CriteriaSetRelationships> finalCriteriaSetRelationships = new ArrayList<CriteriaSetRelationships>();
		if (parentId != null && !parentId.isEmpty() 
				&& childId != null && !childId.isEmpty()) {
			CriteriaSetRelationships parentCriteria = new CriteriaSetRelationships();
			parentCriteria.setRelationshipId(relationId);
			parentCriteria.setIsParent("true");
			parentCriteria.setProductId(productId);
			parentCriteria.setCriteriaSetId(parentId);
			finalCriteriaSetRelationships.add(parentCriteria);
			
			CriteriaSetRelationships childCriteria = new CriteriaSetRelationships();
			childCriteria.setRelationshipId(relationId);
			childCriteria.setIsParent("false");
			childCriteria.setProductId(productId);
			childCriteria.setCriteriaSetId(childId);
			
			finalCriteriaSetRelationships.add(childCriteria);
		}
		return finalCriteriaSetRelationships;
	}
	 
	 public Relationships[] createImprintArtworkRelationShip(List<ImprintMethod> imprintMethods, String productId, String immdCriteriaSetId, String artworkCriteriaId, String minQtyCriteriaId) {
		 
		 Relationships imprintArtworkRelationship = new Relationships();
		 List<CriteriaSetValuePaths> artworkCriteriaSetValuePaths =  new ArrayList<CriteriaSetValuePaths>();
		 imprintArtworkRelationship.setId("-1");
		 imprintArtworkRelationship.setName("Imprint Method x Artwork");
		 imprintArtworkRelationship.setProductId(productId);
		 imprintArtworkRelationship.setParentCriteriaSetId(immdCriteriaSetId);
		 imprintArtworkRelationship.setCriteriaSetRelationships(getCriteriaSetRelationships(productId, immdCriteriaSetId, artworkCriteriaId, imprintArtworkRelationship.getId()));
		 
		 Relationships imprintMinQtyRelationship = new Relationships();
		 List<CriteriaSetValuePaths> minQtyCriteriaSetValuePaths =  new ArrayList<CriteriaSetValuePaths>();
		 imprintMinQtyRelationship.setId("-2");
		 imprintMinQtyRelationship.setName("IImprint Method x Min Order");
		 imprintMinQtyRelationship.setProductId(productId);
		 imprintMinQtyRelationship.setParentCriteriaSetId(immdCriteriaSetId);
		 imprintArtworkRelationship.setCriteriaSetRelationships(getCriteriaSetRelationships(productId, immdCriteriaSetId, minQtyCriteriaId, imprintMinQtyRelationship.getId()));
		 
		 for (ImprintMethod imprint : imprintMethods) {
			 if (imprint.getMethodName() != null && !imprint.getMethodName().trim().isEmpty() && !imprint.getMethodName().trim().equalsIgnoreCase("0")) {
				 if (imprint.getArtworkName() != null && !imprint.getArtworkName().trim().isEmpty() && !imprint.getArtworkName().trim().equalsIgnoreCase("0")) {
					 artworkCriteriaSetValuePaths.addAll(getCriteriaSetValuePaths(imprint, "ARTW", imprintArtworkRelationship.getId(),productId));
				 } 
				 if (imprint.getMinimumOrder() != null && !imprint.getMinimumOrder().trim().isEmpty() && !imprint.getMinimumOrder().trim().equalsIgnoreCase("0")) {
					 minQtyCriteriaSetValuePaths.addAll(getCriteriaSetValuePaths(imprint, "MINO", imprintMinQtyRelationship.getId(),productId));
				 }
			 }
		 }
		 
		 if (artworkCriteriaSetValuePaths != null && !artworkCriteriaSetValuePaths.isEmpty()) {
			 imprintArtworkRelationship.setCriteriaSetValuePaths(artworkCriteriaSetValuePaths);
		 } else if (minQtyCriteriaSetValuePaths != null && !minQtyCriteriaSetValuePaths.isEmpty()) { 
			 imprintMinQtyRelationship.setCriteriaSetValuePaths(minQtyCriteriaSetValuePaths);
		 }
		 
		 List<Relationships> finalRelationships = new ArrayList<Relationships>();
		 if (imprintArtworkRelationship.getCriteriaSetValuePaths() != null 
				 && imprintArtworkRelationship.getCriteriaSetValuePaths().size() > 0
				 && imprintArtworkRelationship.getCriteriaSetRelationships() != null
				 && imprintArtworkRelationship.getCriteriaSetRelationships().size() > 0) {
			 finalRelationships.add(imprintArtworkRelationship);
		 }
		 
		 if (imprintMinQtyRelationship.getCriteriaSetValuePaths() != null 
				 && imprintMinQtyRelationship.getCriteriaSetValuePaths().size() > 0
				 && imprintMinQtyRelationship.getCriteriaSetRelationships() != null
				 && imprintMinQtyRelationship.getCriteriaSetRelationships().size() > 0) {
			 finalRelationships.add(imprintMinQtyRelationship);
		 }
		 	return finalRelationships.toArray(new Relationships[0]);
	    }

	public List<Availability> getAvailabilityByRelationships(
			List<Relationship> relationships,String extPrdId) {
		List<Availability> availabilityList=null;
		List<AvailableVariations> availabileVariationsList=null;
		AvailableVariations availableVariations=null;
		Availability availability=null;
		String tempCriteria="";
		String criteriaValue="";
		int tempId=0;
		boolean isParentOptionCriteria=false;
		boolean isChildOptionCriteria=false;
		String tempCriteriaCode="";
		String optionValue=null;
	
		if(null!=relationships && relationships.size()>0){
			availabilityList=new ArrayList<>();	
			
		for(Relationship currentRelationship:relationships){
			availability=new Availability();
			if(null!=currentRelationship.getCriteriaSetRelationships() && currentRelationship.getCriteriaSetRelationships().size()>0){
				for(CriteriaSetRelationship currentCriteriaRelationship:currentRelationship.getCriteriaSetRelationships()){
					tempCriteria=criteriaSetParser.findCriteriaBySetId(extPrdId,String.valueOf(currentCriteriaRelationship.getCriteriaSetId()));
				if(null!=tempCriteria){
					if(tempCriteria.contains(":")){
						tempCriteriaCode=tempCriteria.substring(0,tempCriteria.indexOf(":"));
					}else{
						tempCriteriaCode=tempCriteria;
					}
					if(Arrays.asList(SIZE_GROUP_CRITERIACODES).contains(tempCriteriaCode))
						{
						tempCriteria="Size";
						}
					else if(Arrays.asList(OPTIONS_CRITERIACODES).contains(tempCriteriaCode)){
						optionValue=tempCriteria.substring(tempCriteria.indexOf(":")+1);
						//tempCriteria=tempCriteriaCode;						
						}
					if(currentCriteriaRelationship.getIsParent()){
						if(Arrays.asList(OPTIONS_CRITERIACODES).contains(tempCriteriaCode))		isParentOptionCriteria=true;	
						if(Arrays.asList(SIZE_GROUP_CRITERIACODES).contains(tempCriteriaCode)){
							availability.setParentCriteria(tempCriteria);
						}else{
							if(isParentOptionCriteria){
								optionValue=tempCriteria.substring(tempCriteria.indexOf(":")+1);
								tempCriteria=tempCriteria.substring(0,tempCriteria.indexOf(":"));
								}
							availability.setParentCriteria(ProductDataStore.getCriteriaInfoForCriteriaCode(tempCriteria).getDescription());
						}
						availability.setParentOptionName(optionValue);
					}else{
						if(Arrays.asList(OPTIONS_CRITERIACODES).contains(tempCriteriaCode))		isChildOptionCriteria=true;
						if(Arrays.asList(SIZE_GROUP_CRITERIACODES).contains(tempCriteriaCode)){
							availability.setChildCriteria(tempCriteria);
						}else{
							if(isChildOptionCriteria){
							optionValue=tempCriteria.substring(tempCriteria.indexOf(":")+1);
							tempCriteria=tempCriteria.substring(0,tempCriteria.indexOf(":"));
							}
							availability.setChildCriteria(ProductDataStore.getCriteriaInfoForCriteriaCode(tempCriteria).getDescription());
						}
						availability.setChildOptionName(optionValue);
					
				}
				availabileVariationsList=new ArrayList<>();
				List<CriteriaSetValuePath> tempCriteriaSetValuePaths=currentRelationship.getCriteriaSetValuePaths();	
				for(CriteriaSetValuePath currentCriteriaSetValuePath:currentRelationship.getCriteriaSetValuePaths()){
					availableVariations=new AvailableVariations();
					if(!currentCriteriaSetValuePath.getIsParent() ){
						criteriaValue=criteriaSetParser.findCriteriaSetValueById(extPrdId,String.valueOf(currentCriteriaSetValuePath.getCriteriaSetValueId()));
						if(isChildOptionCriteria){
							
							tempCriteria=criteriaValue.substring(criteriaValue.indexOf("__")+2);
							availableVariations.setChildValue(tempCriteria.substring(tempCriteria.indexOf(":")+2));
						}else{
							if(null==criteriaValue){
								availableVariations.setChildValue(criteriaSetParser.findSizesCriteriaSetById(extPrdId, String.valueOf(currentCriteriaSetValuePath.getCriteriaSetValueId())));
							}else{
								if(criteriaValue.indexOf("__")+3<criteriaValue.length())
								availableVariations.setChildValue(criteriaValue.substring(criteriaValue.indexOf("__")+2));
							}
						}
						tempId=currentCriteriaSetValuePath.getID();
						for(CriteriaSetValuePath pairingCriteriaSetPath:tempCriteriaSetValuePaths){
							if(tempId==pairingCriteriaSetPath.getID() && pairingCriteriaSetPath.getIsParent()){
								criteriaValue=criteriaSetParser.findCriteriaSetValueById(extPrdId,String.valueOf(pairingCriteriaSetPath.getCriteriaSetValueId()));
								if(isParentOptionCriteria){
									tempCriteria=criteriaValue.substring(criteriaValue.indexOf("__")+2);
									availableVariations.setParentValue(tempCriteria.substring(tempCriteria.indexOf(":")+2));
								}else{
									availableVariations.setParentValue(criteriaValue.substring(criteriaValue.indexOf("__")+2));
								}
								availabileVariationsList.add(availableVariations);
							}
						}
					}
					
				}}
				availability.setAvailableVariations(availabileVariationsList);
				}
			}			
			availabilityList.add(availability);
		}		
		}		
		return availabilityList;
	}

}
