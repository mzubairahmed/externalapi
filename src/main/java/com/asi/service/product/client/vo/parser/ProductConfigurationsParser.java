package com.asi.service.product.client.vo.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSet;
import com.asi.service.product.client.vo.ProductDetail;

public class ProductConfigurationsParser {
	private final static Logger _LOGGER = Logger
			.getLogger(ProductConfigurationsParser.class.getName());
	@Autowired LookupParser productLookupParser;
	
	private HashMap<String,HashMap<String, String>> criteriaSet=new HashMap<>();
	public String[] getPriceCriteria(ProductDetail productDetail,String priceGridId) {
		String[] priceCrterias=null;
		String criteriaOne="",criteria1Value="";
		String criteriaTwo="",criteria2Value="";
		String currentCriteria="";
		if(null!=productDetail && null!=priceGridId)
		{
		priceCrterias=new String[2];
		String externalId=productDetail.getExternalProductId();
		if(criteriaSet.isEmpty() && null!=externalId)
		{
			criteriaSet=setCriteriaSet(productDetail,externalId);
		}
		if(!criteriaSet.isEmpty() && null!=productDetail.getPriceGrids())
		{
			for(PriceGrid currentPricingItem:productDetail.getPriceGrids())
			{
				if(currentPricingItem.getIsBasePrice() && null!=currentPricingItem.getPricingItems())
				{
					for(PricingItem pricingItem:currentPricingItem.getPricingItems())
					{
						currentCriteria=criteriaSet.get(externalId).get(pricingItem.getCriteriaSetValueId().toString());
						if(null!=currentCriteria && priceGridId.toString().equalsIgnoreCase(pricingItem.getPriceGridId()))
						{
						if(criteriaOne.isEmpty()) {						
							criteriaOne=currentCriteria.substring(0,currentCriteria.indexOf(":"));
							criteria1Value=currentCriteria.substring(currentCriteria.indexOf(":")+1);
						}else if(criteriaTwo.isEmpty() && !criteriaOne.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":")))){
							criteriaTwo=currentCriteria.substring(0,currentCriteria.indexOf(":"));
							criteria2Value=currentCriteria.substring(currentCriteria.indexOf(":")+1);
						} else if(criteriaOne.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":"))))
						{
							criteria1Value+=","+currentCriteria.substring(currentCriteria.indexOf(":")+1);
						}
						else if(criteriaTwo.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":"))))
								{
							criteria2Value+=","+currentCriteria.substring(currentCriteria.indexOf(":")+1);
								}
						if(!criteriaOne.isEmpty() && !criteriaTwo.isEmpty() && !criteriaOne.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":"))) && !criteriaTwo.equalsIgnoreCase(currentCriteria.substring(0,currentCriteria.indexOf(":")))) {
							_LOGGER.info("InValid Price Criteria :"+currentCriteria);
						}
						}
					}
				}
			}
		}
		criteriaSet=new HashMap<>();
		priceCrterias[0]=(!criteriaOne.isEmpty())?criteriaOne+":"+criteria1Value:"";
		priceCrterias[1]=(!criteriaTwo.isEmpty())?criteriaTwo+":"+criteria2Value:"";
		}
		return priceCrterias;
	}
	private HashMap<String,HashMap<String, String>> setCriteriaSet(ProductDetail productDetails,String externalId)
	{
		HashMap<String,HashMap<String, String>> currentHashMap=new HashMap<>();
		HashMap<String, String> productCriteriSets=new HashMap<>();
		ArrayList<ProductConfiguration> productConfigurationList=(ArrayList<ProductConfiguration>) productDetails.getProductConfigurations();
		for(ProductConfiguration currentProductConfiguration: productConfigurationList){
			for(ProductCriteriaSet currentProductCriteriSet:currentProductConfiguration.getProductCriteriaSets()){
				for(CriteriaSetValue currentCriteria:currentProductCriteriSet.getCriteriaSetValues()){
					if(currentCriteria.getValue() instanceof String){
						productCriteriSets.put(currentCriteria.getID().toString(), currentCriteria.getCriteriaCode()+":"+currentCriteria.getValue().toString());
					}
					else if(currentCriteria.getValue() instanceof ArrayList){
						productCriteriSets.put(currentCriteria.getID().toString(), currentCriteria.getCriteriaCode()+":"+productLookupParser.getValueString((ArrayList<?>)currentCriteria.getValue(),currentCriteria.getCriteriaCode()));
					}
				}
			}
		}
		currentHashMap.put(externalId, productCriteriSets);
		return currentHashMap;
	}
	public LookupParser getProductLookupParser() {
		return productLookupParser;
	}
	public void setProductLookupParser(LookupParser productLookupParser) {
		this.productLookupParser = productLookupParser;
	}
}
