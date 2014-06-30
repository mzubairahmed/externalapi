package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asi.service.product.client.vo.CriteriaSetRelationships;
import com.asi.service.product.client.vo.CriteriaSetValuePaths;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.Relationships;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;

public class RelationshipParser {
	
	public void mergeRelationships() {
		
	}
	
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
	 
	 public CriteriaSetRelationships[] getCriteriaSetRelationships(String productId, String parentId, String childId, String relationId) {
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
		return finalCriteriaSetRelationships.toArray(new CriteriaSetRelationships[0]);
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
			 imprintArtworkRelationship.setCriteriaSetValuePaths(artworkCriteriaSetValuePaths.toArray(new CriteriaSetValuePaths[0]));
		 } else if (minQtyCriteriaSetValuePaths != null && !minQtyCriteriaSetValuePaths.isEmpty()) { 
			 imprintMinQtyRelationship.setCriteriaSetValuePaths(minQtyCriteriaSetValuePaths.toArray(new CriteriaSetValuePaths[0]));
		 }
		 
		 List<Relationships> finalRelationships = new ArrayList<Relationships>();
		 if (imprintArtworkRelationship.getCriteriaSetValuePaths() != null 
				 && imprintArtworkRelationship.getCriteriaSetValuePaths().length > 0
				 && imprintArtworkRelationship.getCriteriaSetRelationships() != null
				 && imprintArtworkRelationship.getCriteriaSetRelationships().length > 0) {
			 finalRelationships.add(imprintArtworkRelationship);
		 }
		 
		 if (imprintMinQtyRelationship.getCriteriaSetValuePaths() != null 
				 && imprintMinQtyRelationship.getCriteriaSetValuePaths().length > 0
				 && imprintMinQtyRelationship.getCriteriaSetRelationships() != null
				 && imprintMinQtyRelationship.getCriteriaSetRelationships().length > 0) {
			 finalRelationships.add(imprintMinQtyRelationship);
		 }
		 	return finalRelationships.toArray(new Relationships[0]);
	    }

}
