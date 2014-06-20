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
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.Currency;
import com.asi.service.product.client.vo.DiscountRate;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSet;
import com.asi.service.product.client.vo.ProductDataSheet;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;
import com.asi.service.product.client.vo.SelectedProductCategory;
import com.asi.service.product.client.vo.batch.Batch;
import com.asi.service.product.client.vo.batch.BatchDataSource;
import com.asi.service.product.client.vo.parser.ImprintParser;
import com.asi.service.product.client.vo.parser.LookupParser;
import com.asi.service.product.client.vo.parser.ProductConfigurationsParser;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.ItemPriceDetail.PRICE_Type;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;



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
		product=lookupsParser.setProductCategory(productDetail,product);
		product=lookupsParser.setProductServiceKeywords(productDetail,product);
		product=lookupsParser.setProductServiceDataSheet(productDetail,product);
		product=lookupsParser.setProductServiceInventoryLink(productDetail, product);
		product=lookupsParser.setProductServiceBasePriceInfo(productDetail, product);
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
		String[] basePriceCriterias = productConfiguration.getPriceCriteria(
				productDetail, priceGrid.getID());
		if (null != basePriceCriterias && basePriceCriterias.length > 0) {
        	itemPrice.setFirstPriceCriteria(basePriceCriterias[0]);
			if (basePriceCriterias.length > 1) {
        		itemPrice.setSecondPriceCriteria(basePriceCriterias[1]);
        	}
        	itemPrice.setPriceID(priceGrid.getID().toString());
        }
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

	public Product updateProductBasePrices(Product currentProduct)
			throws Exception {
		ProductDetail velocityBean = new ProductDetail();
		velocityBean = setProductWithPriceDetails(currentProduct);
		//velocityBean.setDataSourceId("12938");
		velocityBean.setDataSourceId(getDataSourceId(currentProduct));
		productRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		productRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application","json"));
		HttpEntity<ProductDetail> requestEntity = new HttpEntity<ProductDetail>(velocityBean, requestHeaders);
		ResponseEntity<String> responseEntity = productRestTemplate.exchange(productImportURL, HttpMethod.POST, requestEntity, String.class);
		String result = String.valueOf(responseEntity.getStatusCode().value());
		_LOGGER.info("Product Respones Status:" + result);
		currentProduct=prepairProduct(String.valueOf(currentProduct.getCompanyId()),currentProduct.getExternalProductId());
		return currentProduct;
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
		batchData.setBatchDataSources(new ArrayList<BatchDataSource>(Arrays.asList(batchDataSources)));
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

	private ProductDetail setProductWithPriceDetails(
			Product srcProduct) {
		
		ProductDetail productToUpdate = new ProductDetail();
		productToUpdate.setID(String.valueOf(srcProduct.getID()));
		productToUpdate.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		productToUpdate.setName(srcProduct.getName());
		productToUpdate.setDescription(srcProduct.getDescription());
		productToUpdate.setSummary(String.valueOf(srcProduct.getSummary()));
		productToUpdate.setDataSourceId(srcProduct.getDataSourceId());		
		productToUpdate.setExternalProductId(srcProduct.getExternalProductId());	
		// Product DataSheet
		ProductDataSheet productDataSheet = new ProductDataSheet();
		productDataSheet.setProductId(String.valueOf(srcProduct.getID()));
		productDataSheet
				.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		productDataSheet.setID("0");		
		if(null!=srcProduct.getProductDataSheet())
		{
			productDataSheet.setUrl(srcProduct.getProductDataSheet().getUrl());
		}		
		productToUpdate.setProductDataSheet(productDataSheet);

		//Product Categories
		String sProductCategory=srcProduct.getCategory();
		
		if(null!=sProductCategory ||  (!StringUtils.isEmpty(sProductCategory)))
		{			
			String[] arrayProductCtgrs=sProductCategory.split(",");
			if(null!=arrayProductCtgrs && arrayProductCtgrs.length>0)
			{
				SelectedProductCategory productCategory = null;
				for(String crntCategory:arrayProductCtgrs)
				{
					productCategory = new SelectedProductCategory();
					crntCategory=lookupsParser.getCategoryCodeByName(crntCategory.trim());
					if(null!=crntCategory)
					{
						productCategory.setCode(crntCategory);
						productCategory.setProductId(String.valueOf(srcProduct.getID()));
						productCategory.setIsPrimary(Boolean.FALSE);
						productCategory.setAdCategoryFlg(Boolean.FALSE);
					}
					productToUpdate.getSelectedProductCategories().add(productCategory);
				}			
			}
		}
		
		
		// Product Keywords
		productToUpdate=lookupsParser.setProductKeyWords(productToUpdate,srcProduct);

		// Product Inventory Link
		ProductInventoryLink productInventoryLink = new ProductInventoryLink();
		productInventoryLink.setCompanyId(String.valueOf(srcProduct
				.getCompanyId()));
		productInventoryLink.setProductId(String.valueOf(srcProduct.getID()));
		productInventoryLink.setID("0");
		if(null!=srcProduct.getProductInventoryLink())
			productInventoryLink.setUrl(srcProduct.getProductInventoryLink().getUrl());
		productToUpdate.setProductInventoryLink(productInventoryLink);

		// Price Details
		if (srcProduct.getItemPrice().size() == 0) 
		{
			PriceGrid priceGrid = getQURPriceGrid(srcProduct);
			productToUpdate.getPriceGrids().add(priceGrid);
		}
		else
		{
			productToUpdate.setPriceGrids(setPriceDetails(srcProduct));
		}
		
		return productToUpdate;
	}

	private List<PriceGrid> setPriceDetails(Product srcProduct) {
		
		List<PriceGrid> pricegridList = new ArrayList<PriceGrid>();
		PriceGrid crntPriceGrids=null;
		Price price=null;
		DiscountRate discount=null;
		for(ItemPriceDetail crntItemPrice:srcProduct.getItemPrice())	
		{
			crntPriceGrids=new PriceGrid();
			crntPriceGrids.setID(crntItemPrice.getPriceID());
			crntPriceGrids.setProductId(crntItemPrice.getProductID());
			if(crntItemPrice.getPriceType().toString().equals("REGL"))
			{
				crntPriceGrids.setIsBasePrice(Boolean.TRUE);
				crntPriceGrids.setPriceGridSubTypeCode(crntItemPrice.getPriceType().toString());
			}
			if(!crntItemPrice.getPriceDetails().isEmpty() && crntItemPrice.getPriceDetails().size()>0)
			{
				crntPriceGrids.setIsQUR(Boolean.FALSE);
			}
			crntPriceGrids.setUsageLevelCode("NONE");
			crntPriceGrids.setPriceIncludes(crntItemPrice.getPriceIncludes());
			crntPriceGrids.setCurrency(setCurrency(1,0));
			

			for(PriceDetail priceDetail:crntItemPrice.getPriceDetails())
			{
				price=new Price();
				price.setPriceGridId(crntItemPrice.getPriceID());
				price.setQuantity(priceDetail.getQuanty());
				price.setListPrice(priceDetail.getPrice());
				price.setNetCost(priceDetail.getNetCost());
				price.setItemsPerUnit(priceDetail.getItemsPerUnit());
				discount=new DiscountRate();
				discount.setIndustryDiscountCode(priceDetail.getDiscount());
				discount.setCode(priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase());
				price.setDiscountRate(discount);
				price.setSequenceNumber(priceDetail.getSequenceNumber());
				crntPriceGrids.getPrices().add(price);
			}
			pricegridList.add(crntPriceGrids);
		}
		return pricegridList;
	}

	private PriceGrid getQURPriceGrid(Product crntProduct) {
		PriceGrid qurPriceGrid = new PriceGrid();
		qurPriceGrid.setID("0");
		qurPriceGrid.setProductId(String.valueOf(crntProduct.getID()));
		qurPriceGrid.setIsQUR(Boolean.TRUE);
		qurPriceGrid.setIsBasePrice(Boolean.TRUE);
		qurPriceGrid.setPriceGridSubTypeCode("REGL");
		qurPriceGrid.setUsageLevelCode("NONE");
		qurPriceGrid.setIsRange(Boolean.FALSE);
		qurPriceGrid.setIsSpecial(Boolean.FALSE);
		qurPriceGrid.setDisplaySequence(1);
		qurPriceGrid.setIsCopy(Boolean.FALSE);
		qurPriceGrid.setCurrency(setCurrency(1,0));
		return qurPriceGrid;
	}
	private Currency setCurrency(int displaySequence,int number)
	{
		Currency currency = new Currency();
		currency.setCode("USD");
		currency.setName("US Dollar");
		currency.setNumber(number);
		currency.setASIDisplaySymbol("$");
		currency.setISODisplaySymbol("$");
		currency.setIsISO(Boolean.TRUE);
		currency.setIsActive(Boolean.TRUE);
		currency.setDisplaySequence(displaySequence);
		
		return currency;
	}

	/*
	 * private String getDataSourceId(Integer companyId) {
	 * 
	 * return null; }
	 */
}