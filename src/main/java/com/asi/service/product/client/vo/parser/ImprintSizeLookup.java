package com.asi.service.product.client.vo.parser;

import java.util.List;

import com.asi.ext.api.integration.lookup.parser.CriteriaSetParser;
import com.asi.service.product.client.vo.CriteriaSetValue;



public class ImprintSizeLookup {
	private CriteriaSetParser criteriaSetParser=new CriteriaSetParser(); 
	public String[] findImprintSizeDetails(
			List<CriteriaSetValue> criteriaSetValues,String externalId) {
		String[] imprintDetails=new String[2];
		String imprintSize="",imprintLocation="";
		String crntImprint="";
		String[] imprintItems=null;
		int crntImprintCntr=0;
		for(CriteriaSetValue criteriaSetValue: criteriaSetValues)
		{
			String strValue=criteriaSetValue.getFormatValue();
			if(null==strValue || strValue.isEmpty()){
				strValue=criteriaSetValue.getValue() != null ? criteriaSetValue.getValue().toString() : criteriaSetValue.getBaseLookupValue();
			}
			criteriaSetParser.addReferenceSet(externalId,criteriaSetValue.getCriteriaCode(),criteriaSetValue.getID(),strValue);
			crntImprint=criteriaSetValue.getValue().toString();
			imprintItems=crntImprint.split("\\|");
			if(imprintItems.length>0)
			{
			if(crntImprintCntr==0)
			{
				if(imprintItems.length==1)
				{
				imprintSize=imprintItems[0];
				}
				else
				{
				imprintSize=imprintItems[0].trim();
				imprintLocation=imprintItems[1].trim();
				}
			}else
			{
				if(imprintItems.length==1)
				{
				imprintSize=imprintSize+","+imprintItems[0].trim();
				}
				else
				{
				imprintSize=imprintSize+","+imprintItems[0].trim();
				imprintLocation=imprintLocation+","+imprintItems[1].trim();
				}
			}
			}
			crntImprintCntr++;
		}
		imprintDetails[0]=imprintSize;
		imprintDetails[1]=imprintLocation;
		return imprintDetails;
	}

}
