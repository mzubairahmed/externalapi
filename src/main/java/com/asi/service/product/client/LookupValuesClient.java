package com.asi.service.product.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.asi.service.product.client.vo.origin.Origin;


public class LookupValuesClient {
	@Autowired RestOperations lookupRestTemplate;
	private static Logger _LOGGER = LoggerFactory.getLogger(LookupValuesClient.class);
	private String lookupColorURL;
	private String lookupSizeURL;
	private String lookupMaterialURL;
	private String lookupcriteriaAttributeURL;
	private String originLookupURL;
	private String lookupCategoryURL;
	private String lookupArtworkURL;
	private String lookupImprintURL;
	private String lookupTradeNameURL;
	private String lookupShapeURL;
	private String lookupPackageURL;
	
	public String getLookupPackageURL() {
		return lookupPackageURL;
	}

	public void setLookupPackageURL(String lookupPackageURL) {
		this.lookupPackageURL = lookupPackageURL;
	}

	public String getLookupTradeNameURL() {
		return lookupTradeNameURL;
	}

	public void setLookupTradeNameURL(String lookupTradeNameURL) {
		this.lookupTradeNameURL = lookupTradeNameURL;
	}

	public String getLookupShapeURL() {
		return lookupShapeURL;
	}

	public void setLookupShapeURL(String lookupShapeURL) {
		this.lookupShapeURL = lookupShapeURL;
	}

	public String getLookupImprintURL() {
		return lookupImprintURL;
	}

	public void setLookupImprintURL(String lookupImprintURL) {
		this.lookupImprintURL = lookupImprintURL;
	}

	public String getLookupArtworkURL() {
		return lookupArtworkURL;
	}

	public void setLookupArtworkURL(String lookupArtworkURL) {
		this.lookupArtworkURL = lookupArtworkURL;
	}



	public String getOriginLookupURL() {
		return originLookupURL;
	}



	public void setOriginLookupURL(String originLookupURL) {
		this.originLookupURL = originLookupURL;
	}

	public ArrayList<LinkedHashMap> getColor()
	 {
		return getColorFromLookup(lookupColorURL);
		
	 }

	public ArrayList<LinkedHashMap> getOrigin()
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
	public ArrayList<LinkedHashMap> getColorFromLookup(String colorLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> color = lookupRestTemplate.getForObject(colorLookupURL,ArrayList.class);
		_LOGGER.debug(colorLookupURL + "  " +  color.toString());
		return color;
	}
	
	@Cacheable(value="lookupCache",key="#sizeLookupURL")
	public ArrayList<LinkedHashMap> getSizesFromLookup(String sizeLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> sizes = lookupRestTemplate.getForObject(sizeLookupURL,ArrayList.class);
		return sizes;
	}
	@Cacheable(value="lookupCache",key="#materialLookupURL")
	public ArrayList<LinkedHashMap> getMaterialFromLookup(String materialLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> material = lookupRestTemplate.getForObject(materialLookupURL,ArrayList.class);
		return material;
	}
	@Cacheable(value="lookupCache",key="#originLookupURL")
	public ArrayList<LinkedHashMap> getOriginFromLookup(String originLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> serviceOrigin = lookupRestTemplate.getForObject(originLookupURL,ArrayList.class);
		return serviceOrigin;
	}	
	@Cacheable(value="lookupCache",key="#shapeLookupURL")
	public ArrayList<LinkedHashMap> getShapesFromLookup(String shapeLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> serviceShapes = lookupRestTemplate.getForObject(lookupShapeURL,ArrayList.class);
		return serviceShapes;
	}
	@Cacheable(value="lookupCache",key="#packagingLookupURL")
	public ArrayList<LinkedHashMap> getPackagesFromLookup(String packagingLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> servicePackages = lookupRestTemplate.getForObject(lookupPackageURL,ArrayList.class);
		return servicePackages;
	}
	@Cacheable(value="lookupCache",key="#categoryLookupURL")
	public ArrayList<LinkedHashMap<String,String>> getCategoriesFromLookup(String categoryLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<String,String>> serviceCategory=null;
		try
		{
			serviceCategory = lookupRestTemplate.getForObject(categoryLookupURL,ArrayList.class);
		} catch(RestClientException ex)
		{
			
		}
		
		return serviceCategory;
	}
	@Cacheable(value="lookupCache",key="#artwrokLookupURL")
	public ArrayList<LinkedHashMap> getArtworksFromLookup(String artwrokLookupURL)
	{
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> serviceArtwork = lookupRestTemplate.getForObject(artwrokLookupURL,ArrayList.class);
		return serviceArtwork;
	}
	public ArrayList<LinkedHashMap> getTradeNameFromLookup(
			String lookupTradeNameURL,Object srchkey) {
		ArrayList<LinkedHashMap> serviceTradeName = new ArrayList<>();
		if(null!=srchkey && !srchkey.toString().isEmpty() && !srchkey.toString().equalsIgnoreCase("null")){
			 serviceTradeName = lookupRestTemplate.getForObject(lookupTradeNameURL+srchkey,ArrayList.class);
		}else{
			 serviceTradeName = lookupRestTemplate.getForObject(lookupTradeNameURL.substring(0,lookupTradeNameURL.indexOf("?")),ArrayList.class);
		}
		return serviceTradeName;
	}

	@Cacheable(value="lookupCache",key="#imprintLookupURL")
	public ArrayList<LinkedHashMap> getImprintFromLookup(
			String imprintLookupURL) {
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap> serviceArtwork = lookupRestTemplate.getForObject(imprintLookupURL,ArrayList.class);
		return serviceArtwork;
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



	public String getLookupCategoryURL() {
		return lookupCategoryURL;
	}



	public void setLookupCategoryURL(String lookupCategoryURL) {
		this.lookupCategoryURL = lookupCategoryURL;
	}

	
	





}
