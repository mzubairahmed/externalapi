package com.asi.service.product.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestOperations;

import com.asi.service.product.client.vo.colors.Color;
import com.asi.service.product.client.vo.criteria.Criterium;
import com.asi.service.product.client.vo.material.Material;
import com.asi.service.product.client.vo.origin.Origin;


public class LookupValuesClient {
	@Autowired RestOperations lookupRestTemplate;
	private static Logger _LOGGER = LoggerFactory.getLogger(LookupValuesClient.class);
	private String lookupColorURL;
	private String lookupSizeURL;
	private String lookupMaterialURL;
	private String lookupcriteriaAttributeURL;
	private String originLookupURL;
	
	public String getOriginLookupURL() {
		return originLookupURL;
	}



	public void setOriginLookupURL(String originLookupURL) {
		this.originLookupURL = originLookupURL;
	}
	public ArrayList<Color> getColor()
	 {
		return getColorFromLookup(lookupColorURL);
		
	 }
	
	public ArrayList<Origin> getOrigin()
	{
		return getOriginFromLookup(originLookupURL);
	}
	
	

	@SuppressWarnings("rawtypes")
	@Cacheable(value="lookupCache",key="#lookupcriteriaAttributeURL")
	public ArrayList<LinkedHashMap> getCriteriaAttributesFromLookup(String lookupcriteriaAttributeURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> criteriaAttribute = lookupRestTemplate.getForObject(lookupcriteriaAttributeURL,ArrayList.class);
		return criteriaAttribute;
	}
	@Cacheable(value="lookupCache",key="#colorLookupURL")
	public ArrayList<Color> getColorFromLookup(String colorLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Color> color = lookupRestTemplate.getForObject(colorLookupURL,ArrayList.class);
		_LOGGER.debug(colorLookupURL + "  " +  color.toString());
		return color;
	}
	
	@Cacheable(value="lookupCache",key="#sizeLookupURL")
	public ArrayList<Criterium> getSizesFromLookup(String sizeLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Criterium> sizes = lookupRestTemplate.getForObject(sizeLookupURL,ArrayList.class);
		return sizes;
	}
	@Cacheable(value="lookupCache",key="#materialLookupURL")
	public ArrayList<Material> getMaterialFromLookup(String materialLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Material> material = lookupRestTemplate.getForObject(materialLookupURL,ArrayList.class);
		return material;
	}
	@Cacheable(value="lookupCache",key="#originLookupURL")
	public ArrayList<Origin> getOriginFromLookup(String originLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Origin> serviceOrigin = lookupRestTemplate.getForObject(originLookupURL,ArrayList.class);
		return serviceOrigin;
	}	

	/**
	 * @return the lookupColorURL
	 */
	public String getLookupColorURL() {
		return lookupColorURL;
	}


	/**
	 * @param lookupColorURL the lookupColorURL to set
	 */
	public void setLookupColorURL(String lookupColorURL) {
		this.lookupColorURL = lookupColorURL;
	}


	/**
	 * @param lookupRestTemplate the lookupRestTemplate to set
	 */
	public void setLookupRestTemplate(RestOperations lookupRestTemplate) {
		this.lookupRestTemplate = lookupRestTemplate;
	}

	/**
	 * @return the lookupSizeURL
	 */
	public String getLookupSizeURL() {
		return lookupSizeURL;
	}

	/**
	 * @param lookupSizeURL the lookupSizeURL to set
	 */
	public void setLookupSizeURL(String lookupSizeURL) {
		this.lookupSizeURL = lookupSizeURL;
	}

	/**
	 * @return the lookupMaterialURL
	 */
	public String getLookupMaterialURL() {
		return lookupMaterialURL;
	}

	/**
	 * @param lookupMaterialURL the lookupMaterialURL to set
	 */
	public void setLookupMaterialURL(String lookupMaterialURL) {
		this.lookupMaterialURL = lookupMaterialURL;
	}
	
	public String getLookupcriteriaAttributeURL() {
		return lookupcriteriaAttributeURL;
	}

	public void setLookupcriteriaAttributeURL(String lookupcriteriaAttributeURL) {
		this.lookupcriteriaAttributeURL = lookupcriteriaAttributeURL;
	}
}
