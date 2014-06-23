package com.asi.core.repo.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.Batch;
import com.asi.service.product.client.vo.BatchDataSource;
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSet;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.parser.ImprintParser;
import com.asi.service.product.client.vo.parser.LookupParser;
import com.asi.service.product.client.vo.parser.ProductConfigurationsParser;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.ItemPriceDetail.PRICE_Type;
import com.asi.service.product.vo.PriceCriteria;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import javax.ws.rs.core.MediaType;



@Component
public class ProductRepo {
	private final static Logger _LOGGER = LoggerFactory
			.getLogger(ProductRepo.class);

	/**
	 * @return the productClient
	 */
	public ProductClient getProductClient() {
		return productClient;
	}

	/**
	 * @param productClient
	 *            the productClient to set
	 */
	public void setProductClient(ProductClient productClient) {
		this.productClient = productClient;
	}

	@Autowired
	@Qualifier("productServiceClient")
	ProductClient productClient;
	@Autowired
	ProductDetail productDetail;
	@Autowired LookupParser lookupsParser;
	String productImportURL;
	

	public LookupParser getLookupsParser() {
		return lookupsParser;
	}

	public void setLookupsParser(LookupParser lookupsParser) {
		this.lookupsParser = lookupsParser;
	}

	String batchProcessingURL;
	public String getProductImportURL() {
		return productImportURL;
	}

	public void setProductImportURL(String productImportURL) {
		this.productImportURL = productImportURL;
	}

	public String getBatchProcessingURL() {
		return batchProcessingURL;
	}

	public void setBatchProcessingURL(String batchProcessingURL) {
		this.batchProcessingURL = batchProcessingURL;
	}

	@Autowired
	LookupValuesClient lookupColor;
	@Autowired
	ProductConfigurationsParser productConfiguration;
	@Autowired
	ImprintParser imprintParser;
	@Autowired RestTemplate productRestTemplate;
	public ImprintParser getImprintParser() {
		return imprintParser;
	}

	public void setImprintParser(ImprintParser imprintParser) {
		this.imprintParser = imprintParser;
	}
	public ProductConfigurationsParser getProductConfiguration() {
		return productConfiguration;
	}
	public RestTemplate getProductRestTemplate() {
		return productRestTemplate;
	}

	public void setProductRestTemplate(RestTemplate productRestTemplate) {
		this.productRestTemplate = productRestTemplate;
	}
	public void setProductConfiguration(
			ProductConfigurationsParser productConfiguration) {
		this.productConfiguration = productConfiguration;
	}
	
	private Product prepairProduct(String companyID, String productID)
			throws ProductNotFoundException {
		productDetail = getProductFromService(companyID, productID);
		Product product = new Product();
		BeanUtils.copyProperties(productDetail, product);
		//product=lookupsParser.setProductConfigurations(productDetail,product);
		product=lookupsParser.setProductCategory(productDetail,product);
		product=lookupsParser.setProductServiceKeywords(productDetail,product);
		product=lookupsParser.setProductServiceDataSheet(productDetail,product);
		product=lookupsParser.setProductServiceInventoryLink(productDetail, product);
		product=lookupsParser.setProductServiceBasePriceInfo(productDetail, product);
		//product.setNewProductExpirationDate(productDetail.getn)
		
		return product;
	}

	private ProductDetail getProductFromService(String companyID,
			String productID) throws ProductNotFoundException {
		if(null !=productDetail)
			productDetail = productClient.doIt(companyID,productID);
		
		return productDetail;
		
	}	

	public Product getProductPrices(String companyID, String productID)
			throws ProductNotFoundException {

		Product product = prepairProduct(companyID, productID);

		List<PriceGrid> priceGrids = productDetail.getPriceGrids();
		List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();

		for (PriceGrid prices : priceGrids) {
			if (prices.getPriceGridSubTypeCode().equalsIgnoreCase(
					PRICE_Type.REGL.name()))
				pricesInfo.add(getBasePriceDetails(productDetail,
						ItemPriceDetail.PRICE_Type.REGL, prices, false));
		}
		if(pricesInfo.isEmpty())
			_LOGGER.error(
					"Invalid price grid id or price grid not found for company %1 product %2",
					companyID, productID);
		
		product.setItemPrice(pricesInfo);

		return product;
		
	}

	public Product getProductPrices(String companyID, String productID,
			Integer priceGridID) throws ProductNotFoundException {
	    
		productDetail = getProductFromService(companyID,productID);
		
		List<PriceGrid> priceGrids = productDetail.getPriceGrids();
		List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();
		ItemPriceDetail itemPrice;

		Product product = prepairProduct(companyID, productID);
		
		for (PriceGrid prices : priceGrids) {
			if (prices.getPriceGridSubTypeCode().equalsIgnoreCase(
					PRICE_Type.REGL.name())
					&& prices.getID().equals(priceGridID)) {
				itemPrice = getBasePriceDetails(productDetail,
						ItemPriceDetail.PRICE_Type.REGL, prices, false);
				pricesInfo.add(itemPrice);
			}
		}
		if(pricesInfo.isEmpty())
			_LOGGER.error(
					"Invalid price grid id or price grid not found for company %1 product %2 with priceGridId %3",
					companyID, productID, priceGridID);

	product.setItemPrice(pricesInfo);

		return product;

	}

	private ItemPriceDetail getBasePriceDetails(ProductDetail productDetail,
			ItemPriceDetail.PRICE_Type priceType, PriceGrid priceGrid,
			boolean setCurrency) {
    	ItemPriceDetail itemPrice = new ItemPriceDetail();
    	itemPrice.setPriceType(priceType);
        itemPrice.setPriceName(priceGrid.getDescription());
        itemPrice.setPriceIncludes(priceGrid.getPriceIncludes());
        itemPrice.setPriceUponRequest(priceGrid.getIsQUR());
        itemPrice.setIsBasePrice(String.valueOf(priceGrid.getIsBasePrice()));
		List<PriceDetail> pricesList = new ArrayList<PriceDetail>();

		for (Price p : priceGrid.getPrices()) {
			PriceDetail priceDetail = new PriceDetail();
        	priceDetail.setSequenceNumber(p.getSequenceNumber());
			priceDetail.setPrice(p.getListPrice());
			priceDetail.setQuanty(p.getQuantity());
			priceDetail.setDiscount(p.getDiscountRate()
					.getIndustryDiscountCode());
			priceDetail.setHighQuantity(p.getHighQuantity());
			priceDetail.setLowQuantity(p.getLowQuantity());
			priceDetail.setItemsPerUnit(p.getItemsPerUnit());
			priceDetail.setNetCost(p.getNetCost());
			priceDetail.setItemsPerUnitBy(p.getPriceUnit().getDisplayName());
			pricesList.add(priceDetail);
		}
        itemPrice.setProductID(productDetail.getName());
        itemPrice.setPriceDetails(pricesList);
        itemPrice.setPriceID(priceGrid.getID().toString());
		String[] basePriceCriterias = productConfiguration.getPriceCriteria(
				productDetail, priceGrid.getID());
		PriceCriteria[] priceCriterias=new PriceCriteria[basePriceCriterias.length];
		int criteriaCntr=0;
		for(String crntBasePriceCriteria:basePriceCriterias)
		{
			if(null!=crntBasePriceCriteria && !crntBasePriceCriteria.isEmpty() && !crntBasePriceCriteria.startsWith("null"))
			{
				priceCriterias[criteriaCntr]=new PriceCriteria();
				priceCriterias[criteriaCntr].setCriteriaCode(crntBasePriceCriteria.substring(0,crntBasePriceCriteria.indexOf(":")));
				priceCriterias[criteriaCntr].setValue(crntBasePriceCriteria.substring(crntBasePriceCriteria.indexOf(":")+1));
			}else{
				priceCriterias=Arrays.copyOf(priceCriterias, priceCriterias.length-1);
			}
				
			criteriaCntr++;
		}
		if(null!=priceCriterias && priceCriterias.length>0)
		{
			itemPrice.setPriceCriteria(priceCriterias);
		}
	/*	if (null != basePriceCriterias && basePriceCriterias.length > 0) {
        	itemPrice.setFirstPriceCriteria(basePriceCriterias[0]);
			if (basePriceCriterias.length > 1) {
        		itemPrice.setSecondPriceCriteria(basePriceCriterias[1]);
        	}
        	itemPrice.setPriceID(priceGrid.getID().toString());
        }*/
      return itemPrice;

	}

	public Product getProductImprintMethodDetails(String companyId, String xid)
			throws ProductNotFoundException {
		Product product = prepairProduct(companyId, xid);
		product.setImprints(getProductImprintMethods(companyId, xid));
		return product;
	}
	
	public Imprints getProductImprintMethods(String companyId, String xid)
			throws ProductNotFoundException {
		productDetail = getProductFromService(companyId,xid);
		List<ImprintMethod> imprintMethodsList = new ArrayList<ImprintMethod>();
		ProductConfiguration productConfiguration = productDetail
				.getProductConfigurations().get(0);
		ProductCriteriaSet imprintCriteriaSet = imprintParser
				.getCriteriaSetBasedOnCriteriaCode(
						productConfiguration.getProductCriteriaSets(), "IMMD");
		if(null!=imprintCriteriaSet){
			List<CriteriaSetValue> criteriaSetValues = imprintCriteriaSet
					.getCriteriaSetValues();
			for (CriteriaSetValue criteriaSetValue : criteriaSetValues) {
				imprintMethodsList = imprintParser.getImprintMethodRelations(
						productDetail.getExternalProductId(),
						criteriaSetValue.getCriteriaSetId(),
						productConfiguration.getProductCriteriaSets(),
						productDetail.getRelationships());
			}
		}
		Imprints imprints = new Imprints();
		imprints.setImprintMethod(imprintMethodsList);
		return imprints;
		
	}

	public Product updateProductBasePrices(Product currentProduct,String requestType)
			throws Exception {
	//	ProductDetail velocityBean = new ProductDetail();
		com.asi.service.product.client.vo.Product velocityBean=new com.asi.service.product.client.vo.Product();
		velocityBean = setProductWithPriceDetails(currentProduct);
		velocityBean = setProductWithBasicDetails(currentProduct,velocityBean);
		velocityBean = setProductWithProductConfigurations(currentProduct,velocityBean);
		//velocityBean.setDataSourceId("12938");
		velocityBean.setDataSourceId(String.valueOf(getDataSourceId(currentProduct)));
		productRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		productRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application","json"));
		
		ObjectMapper mapper = new ObjectMapper();
		String productJson = null;
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			productJson = mapper.writeValueAsString(velocityBean);
			_LOGGER.info("Product Json:"+productJson);
		} catch (Exception e) {
			_LOGGER.info(e.getMessage());
		}
		HttpEntity<com.asi.service.product.client.vo.Product> requestEntity = new HttpEntity<com.asi.service.product.client.vo.Product>(velocityBean, requestHeaders);
		ResponseEntity<Object> responseEntity=null;
		if(requestType.equalsIgnoreCase("update")){
			responseEntity = productRestTemplate.exchange(productImportURL, HttpMethod.POST, requestEntity, Object.class);
		}else{
			responseEntity = productRestTemplate.exchange(productImportURL, HttpMethod.PUT, requestEntity, Object.class);
		}
		//Client        restClient = Client.create();

	//	WebResource resource = restClient.resource(productImportURL);

      //  String response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(String.class, productJson);

		
		//String responseEntity = productRestTemplate.postForObject(productImportURL, requestEntity, String.class);
		//String result = String.valueOf(responseEntity.getStatusCode().value());
		_LOGGER.info("Product Respones Status:" + responseEntity);
		//currentProduct=prepairProduct(String.valueOf(currentProduct.getCompanyId()),currentProduct.getExternalProductId());
		return currentProduct;
	}

	private com.asi.service.product.client.vo.Product setProductWithProductConfigurations(
			Product currentProduct, com.asi.service.product.client.vo.Product velocityBean) {
		BeanUtils.copyProperties(currentProduct, velocityBean);
	//	BeanUtils.copyProperties(currentProduct.getProductConfigurations(), velocityBean.getProductConfigurations());
		//velocityBean.setProductConfigurations(Arrays.asList(currentProduct.getProductConfigurations()));
		return velocityBean;
	}

	@SuppressWarnings("unchecked")
	private String getDataSourceId(Product currentProduct) throws Exception {
		String dataSourceId = "0";
		Batch batchData = new Batch();
		batchData.setBatchId(0);
		batchData.setBatchTypeCode("IMRT");
		batchData.setStartDate(String.valueOf(new Timestamp(System.currentTimeMillis()).toString()));
		batchData.setStatus("N");
		batchData.setCompanyId(String.valueOf(currentProduct.getCompanyId()));
		BatchDataSource batchDataSources=new BatchDataSource();
		batchDataSources.setBatchId(0);
		batchDataSources.setId(0);
		batchDataSources.setDescription("Batch Created by API");
		batchDataSources.setName("ASIF");
		batchDataSources.setTypeCode("IMRT");
		batchData.setBatchDataSources(new ArrayList<com.asi.service.product.client.vo.BatchDataSource>(Arrays.asList(batchDataSources)));
		productRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		productRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		LinkedHashMap<String, String> batchDetails=productRestTemplate.postForObject(batchProcessingURL, batchData, LinkedHashMap.class);
		String batchId=String.valueOf(batchDetails.get("BatchId"));
		if(null!=batchId && !batchId.equals("0"))
		{
			LinkedHashMap<Object, ArrayList<LinkedHashMap<String,String>>> crntObj=productRestTemplate.getForObject(batchProcessingURL+"/"+batchId,LinkedHashMap.class);
			List<LinkedHashMap<String,String>> batchDataSourceList=(ArrayList<LinkedHashMap<String,String>>)crntObj.get("BatchDataSources");
		   	dataSourceId = String.valueOf(batchDataSourceList.get(0).get("Id"));
		}
		return dataSourceId;
	}

	private com.asi.service.product.client.vo.Product setProductWithPriceDetails(
			Product srcProduct) {

		com.asi.service.product.client.vo.Product productToUpdate = new com.asi.service.product.client.vo.Product();
		productToUpdate.setId(String.valueOf(srcProduct.getID()));
		productToUpdate.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		productToUpdate.setName(srcProduct.getName());
		productToUpdate.setDescription(srcProduct.getDescription());
		productToUpdate.setSummary(String.valueOf(srcProduct.getSummary()));
		productToUpdate.setDataSourceId(srcProduct.getDataSourceId());		
		productToUpdate.setExternalProductId(srcProduct.getExternalProductId());	
		// Product DataSheet
		com.asi.service.product.client.vo.ProductDataSheet productDataSheet = new com.asi.service.product.client.vo.ProductDataSheet();
		productDataSheet.setProductId(String.valueOf(srcProduct.getID()));
		productDataSheet
				.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		productDataSheet.setId("0");		
		if(null!=srcProduct.getProductDataSheet())
		{
			productDataSheet.setUrl(srcProduct.getProductDataSheet().getUrl());
		}		
		productToUpdate.setProductDataSheet(productDataSheet);

		// Product Category
String sProductCategory=srcProduct.getCategory();
		
		if(null!=sProductCategory ||  (!StringUtils.isEmpty(sProductCategory)))
		{			
			String[] arrayProductCtgrs=sProductCategory.split(",");
			int productCtrgyCntr=0;
			if(null!=arrayProductCtgrs && arrayProductCtgrs.length>0)
			{
				com.asi.service.product.client.vo.SelectedProductCategories productCategory = null;
				com.asi.service.product.client.vo.SelectedProductCategories[] productCategoryAry = new com.asi.service.product.client.vo.SelectedProductCategories[arrayProductCtgrs.length];
				for(String crntCategory:arrayProductCtgrs)
				{
					productCategory = new com.asi.service.product.client.vo.SelectedProductCategories();
					crntCategory=lookupsParser.getCategoryCodeByName(crntCategory.trim());
					if(null!=crntCategory)
					{
						productCategory.setCode(crntCategory);
						productCategory.setProductId(String.valueOf(srcProduct.getID()));
						productCategory.setIsPrimary(String.valueOf(Boolean.FALSE));
						productCategory.setAdCategoryFlg(String.valueOf(Boolean.FALSE));
					}
					productCategoryAry[productCtrgyCntr]=productCategory;
				}			
				productToUpdate.setSelectedProductCategories(productCategoryAry);
			}
		}
		// Product Keywords
		productToUpdate=lookupsParser.setProductKeyWords(productToUpdate,srcProduct);

		// Product Inventory Link
		com.asi.service.product.client.vo.ProductInventoryLink productInventoryLink = new com.asi.service.product.client.vo.ProductInventoryLink();
		productInventoryLink.setCompanyId(String.valueOf(srcProduct
				.getCompanyId()));
		productInventoryLink.setProductId(String.valueOf(srcProduct.getID()));
		productInventoryLink.setId("0");
		if(null!=srcProduct.getProductInventoryLink())
			productInventoryLink.setUrl(srcProduct.getProductInventoryLink().getUrl());
		productToUpdate.setProductInventoryLink(productInventoryLink);

		// Price Details
		if (srcProduct.getItemPrice().size() == 0) 
		{
			com.asi.service.product.client.vo.PriceGrids priceGrid = getQURPriceGrid(srcProduct);
			productToUpdate.setPriceGrids(new com.asi.service.product.client.vo.PriceGrids[]{priceGrid});
		}
		else
		{
			productToUpdate.setPriceGrids(setPriceDetails(srcProduct));
		}
		
		return productToUpdate;
	}
	private com.asi.service.product.client.vo.Product setProductWithBasicDetails(
			Product srcProduct,com.asi.service.product.client.vo.Product currentProduct) {
		BeanUtils.copyProperties(srcProduct, currentProduct);
		// Product DataSheet
		com.asi.service.product.client.vo.ProductDataSheet productDataSheet = new com.asi.service.product.client.vo.ProductDataSheet();
		productDataSheet.setProductId(String.valueOf(srcProduct.getID()));
		productDataSheet
				.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		productDataSheet.setId("0");
		if(null!=srcProduct.getProductDataSheet())
		{
			productDataSheet.setUrl(srcProduct.getProductDataSheet().getUrl());
		}		
		currentProduct.setProductDataSheet(productDataSheet);
		// Product Category
		com.asi.service.product.client.vo.SelectedProductCategories[] productCategoriesLst = null;
		com.asi.service.product.client.vo.SelectedProductCategories productCategories = null;
		String productCategory=srcProduct.getCategory();
		String[] productCtgrs=productCategory.split(",");
		int productCategoryCntr=0;
		if(null!=productCtgrs && productCtgrs.length>0)
		{
			productCategoriesLst=new com.asi.service.product.client.vo.SelectedProductCategories[productCtgrs.length];
			for(String crntCategory:productCtgrs)
			{
				productCategories = new com.asi.service.product.client.vo.SelectedProductCategories();
				crntCategory=lookupsParser.getCategoryCodeByName(crntCategory.trim());
				if(null!=crntCategory)
				{
					productCategories.setCode(crntCategory);
					productCategories.setProductId(String.valueOf(srcProduct.getID()));
					productCategories.setIsPrimary("false");
					productCategories.setAdCategoryFlg("false");
				}
				productCategoriesLst[productCategoryCntr]=productCategories;
				productCategoryCntr++;
			}			
		}
		
			currentProduct
			.setSelectedProductCategories(productCategoriesLst);
		// Product Keywords
		currentProduct=lookupsParser.setProductKeyWords(currentProduct,srcProduct);
		// Product Inventory Link
		com.asi.service.product.client.vo.ProductInventoryLink productInventoryLink = new com.asi.service.product.client.vo.ProductInventoryLink();
		productInventoryLink.setCompanyId(String.valueOf(srcProduct
				.getCompanyId()));
		productInventoryLink.setProductId(String.valueOf(srcProduct.getID()));
		productInventoryLink.setId("0");
		if(null!=srcProduct.getProductInventoryLink())
			productInventoryLink.setUrl(srcProduct.getProductInventoryLink().getUrl());
		currentProduct.setProductInventoryLink(productInventoryLink);
		return currentProduct;
	}
	private com.asi.service.product.client.vo.PriceGrids[] setPriceDetails(Product srcProduct) {
		com.asi.service.product.client.vo.PriceGrids[] pricegridList ={};
		com.asi.service.product.client.vo.PriceGrids crntPriceGrids=null;
		com.asi.service.product.client.vo.Currency currency=null;
		com.asi.service.product.client.vo.Prices[] pricesList={};
		com.asi.service.product.client.vo.Prices prices=null;
		com.asi.service.product.client.vo.DiscountRate discount=null;
		int priceGridCntr=0;
		if(null!=srcProduct.getItemPrice() && srcProduct.getItemPrice().size()>0)
		{
		pricegridList=new com.asi.service.product.client.vo.PriceGrids[srcProduct.getItemPrice().size()];
		for(ItemPriceDetail crntItemPrice:srcProduct.getItemPrice())	
		{
			crntPriceGrids=new com.asi.service.product.client.vo.PriceGrids();
			crntPriceGrids.setId(crntItemPrice.getPriceID());
			crntPriceGrids.setProductId(String.valueOf(srcProduct.getID()));
			crntPriceGrids.setDescription(crntItemPrice.getPriceName());
			if(crntItemPrice.getPriceType().toString().equals("REGL"))
			{
				crntPriceGrids.setIsBasePrice("true");
				crntPriceGrids.setPriceGridSubTypeCode(crntItemPrice.getPriceType().toString());
			}
			if(!crntItemPrice.getPriceDetails().isEmpty() && crntItemPrice.getPriceDetails().size()>0)
			{
				crntPriceGrids.setIsQUR("false");
			}
			crntPriceGrids.setUsageLevelCode("NONE");
			crntPriceGrids.setPriceIncludes(crntItemPrice.getPriceIncludes());
			currency = new com.asi.service.product.client.vo.Currency();
			currency.setCode("USD");
			currency.setName("US Dollar");
			currency.setiSODisplaySymbol("$");
			currency.setIsISO("true");
			currency.setIsActive("true");
			crntPriceGrids.setCurrency(currency);
			int pricesCntr=0;
			pricesList=new com.asi.service.product.client.vo.Prices[crntItemPrice.getPriceDetails().size()];
			for(PriceDetail priceDetail:crntItemPrice.getPriceDetails())
			{
				prices=new com.asi.service.product.client.vo.Prices();
				prices.setPriceGridId(crntItemPrice.getPriceID());
				prices.setQuantity(String.valueOf(priceDetail.getQuanty()));
				prices.setListPrice(String.valueOf(priceDetail.getPrice()));
				prices.setNetCost(String.valueOf(priceDetail.getNetCost()));
				prices.setItemsPerUnit(String.valueOf(priceDetail.getItemsPerUnit()));
				prices.setItemsPerUnit(String.valueOf(priceDetail.getItemsPerUnit()));
				discount=new com.asi.service.product.client.vo.DiscountRate();
				discount.setIndustryDiscountCode(priceDetail.getDiscount());
				discount.setCode(priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase());
				prices.setDiscountRate(discount);
				prices.setSequenceNumber(String.valueOf(priceDetail.getSequenceNumber()));
				pricesList[pricesCntr++]=prices;
			}
			crntPriceGrids.setPrices(pricesList);
			pricegridList[priceGridCntr++]=crntPriceGrids;
		}
		}
		return pricegridList;
	}

	private com.asi.service.product.client.vo.PriceGrids getQURPriceGrid(Product crntProduct) {
		com.asi.service.product.client.vo.PriceGrids qurPriceGrid = new com.asi.service.product.client.vo.PriceGrids();
		qurPriceGrid.setId("0");
		qurPriceGrid.setProductId(String.valueOf(crntProduct.getID()));
		qurPriceGrid.setIsQUR(String.valueOf(Boolean.TRUE));
		qurPriceGrid.setIsBasePrice(String.valueOf(Boolean.TRUE));
		qurPriceGrid.setPriceGridSubTypeCode("REGL");
		qurPriceGrid.setUsageLevelCode("NONE");
		qurPriceGrid.setIsRange(String.valueOf(Boolean.FALSE));
		qurPriceGrid.setIsSpecial(String.valueOf(Boolean.FALSE));
		qurPriceGrid.setDisplaySequence("1");
		qurPriceGrid.setIsCopy(String.valueOf(Boolean.FALSE));
		qurPriceGrid.setCurrency(setCurrency(1,0));
		return qurPriceGrid;
	}
	private com.asi.service.product.client.vo.Currency setCurrency(int displaySequence,int number)
	{
		com.asi.service.product.client.vo.Currency currency = new com.asi.service.product.client.vo.Currency();
		currency.setCode("USD");
		currency.setName("US Dollar");
		currency.setNumber(String.valueOf(number));
		currency.setaSIDisplaySymbol("$");
		currency.setaSIDisplaySymbol("$");
		currency.setIsISO(String.valueOf(Boolean.TRUE));
		currency.setIsActive(String.valueOf(Boolean.TRUE));
		currency.setDisplaySequence(String.valueOf(displaySequence));
		
		return currency;
	}

	/*
	 * private String getDataSourceId(Integer companyId) {
	 * 
	 * return null; }
	 */
}