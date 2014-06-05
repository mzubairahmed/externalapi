package com.asi.core.repo.product;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.asi.core.utils.JerseyClient;
import com.asi.core.utils.JerseyClient.AsiHttpMethod;
import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.CriteriaSetValue;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSet;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.parser.ImprintParser;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.ItemPriceDetail.PRICE_Type;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;
import com.asi.service.product.vo.ProductConfigurationsParser;
import com.asi.velocity.bean.Batch;
import com.asi.velocity.bean.BatchDataSource;
import com.asi.velocity.bean.Currency;
import com.asi.velocity.bean.DiscountRate;
import com.asi.velocity.bean.PriceGrids;
import com.asi.velocity.bean.Prices;
import com.asi.velocity.bean.PricingItems;
import com.asi.velocity.bean.ProductDataSheet;
import com.asi.velocity.bean.ProductInventoryLink;
import com.asi.velocity.bean.SelectedProductCategories;



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
	@Autowired
	LookupValuesClient lookupColor;
	@Autowired
	ProductConfigurationsParser productConfiguration;
	@Autowired
	ImprintParser imprintParser;
	
	public ImprintParser getImprintParser() {
		return imprintParser;
	}

	public void setImprintParser(ImprintParser imprintParser) {
		this.imprintParser = imprintParser;
	}
	public ProductConfigurationsParser getProductConfiguration() {
		return productConfiguration;
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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		com.asi.velocity.bean.Product velocityBean = new com.asi.velocity.bean.Product();
		velocityBean = setProductWithPriceDetails(currentProduct);
		velocityBean.setDataSourceId(getDataSourceId(currentProduct));
		String productDetails = mapper.writeValueAsString(velocityBean);
		boolean batchFinalizeStatus = JerseyClient
				.sendRequst(
						new URI(
								"http://stage-espupdates.asicentral.com/api/api/ProductImport"),
						AsiHttpMethod.POST, productDetails);
		_LOGGER.info("Batch Final Status:" + batchFinalizeStatus);
		// return
		// prepairProduct(String.valueOf(currentProduct.getCompanyId()),currentProduct.getExternalProductId());
		return currentProduct;
	}

	private String getDataSourceId(Product currentProduct) throws Exception {
		String dataSourceId = "0";
		Batch batchData = new Batch();
		batchData.setBatchId(0);
		batchData.setBatchTypeCode("IMRT");
	
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-ddTmm:ss:ms");//2014-06-03T21:45:43.013
	//String date = sdf.format() ); 
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
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String batchDetails = mapper.writeValueAsString(batchData);
		String batchId = JerseyClient.sendBatchRequst(new URI(
				"http://stage-espupdates.asicentral.com/api/api/batch"),
				AsiHttpMethod.POST, batchDetails);
		_LOGGER.info("batch Id Created:"+batchId);
		if(null!=batchId && !batchId.equals("0"))
		{
			// Get Data Source Id Based on Batch Id
			dataSourceId=JerseyClient.getDataSourceByBatchId(batchId);
		}
		return dataSourceId;
	}

	private com.asi.velocity.bean.Product setProductWithPriceDetails(
			Product srcProduct) {
		// String dataSourceId=getDataSourceId(srcProduct.getCompanyId());
		com.asi.velocity.bean.Product currentProduct = new com.asi.velocity.bean.Product();
		currentProduct.setId(String.valueOf(srcProduct.getID()));
		currentProduct.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		currentProduct.setName(srcProduct.getName());
		currentProduct.setDescription(srcProduct.getDescription());
		currentProduct.setSummary(String.valueOf(srcProduct.getSummary()));
		currentProduct.setDataSourceId(srcProduct.getDataSourceId());
		currentProduct.setExternalProductId(srcProduct.getExternalProductId());
		// Product DataSheet
		ProductDataSheet productDataSheet = new ProductDataSheet();
		productDataSheet.setProductId(String.valueOf(srcProduct.getID()));
		productDataSheet
				.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
		productDataSheet.setId("0");
		currentProduct.setProductDataSheet(productDataSheet);
		// Product Category
		List<SelectedProductCategories> productCategoriesLst = new ArrayList<>();
		SelectedProductCategories productCategories = new SelectedProductCategories();
		productCategories.setCode("B07449903");
		productCategories.setProductId(String.valueOf(srcProduct.getID()));
		productCategories.setIsPrimary("false");
		productCategories.setAdCategoryFlg("false");
		productCategoriesLst.add(productCategories);
		currentProduct
				.setSelectedProductCategories(new SelectedProductCategories[] { productCategories });
		// Product Inventory Link
		ProductInventoryLink productInventoryLink = new ProductInventoryLink();
		productInventoryLink.setCompanyId(String.valueOf(srcProduct
				.getCompanyId()));
		productInventoryLink.setProductId(String.valueOf(srcProduct.getID()));
		productInventoryLink.setId("0");
		currentProduct.setProductInventoryLink(productInventoryLink);

		// Price Details
		PriceGrids[] pricegridList = new PriceGrids[] {};
		if (srcProduct.getItemPrice().size() == 0) {
			pricegridList = new PriceGrids[1];
			PriceGrids priceGrid = getQURPriceGrid(srcProduct);
			pricegridList[0] = priceGrid;
			currentProduct.setPriceGrids(pricegridList);
		}else
		{
			pricegridList=setPriceDetails(srcProduct);
			if(null!=pricegridList && pricegridList.length!=0)
			currentProduct.setPriceGrids(pricegridList);
		}
		return currentProduct;
	}

	private PriceGrids[] setPriceDetails(Product srcProduct) {
		PriceGrids[] pricegridList = new PriceGrids[srcProduct.getItemPrice().size()];
		int priceGridCntr=0;
		PriceGrids crntPriceGrids=null;
		Currency currency=null;
		Prices[] pricesList=null;
		Prices prices=null;
		DiscountRate discount=null;
		int pricesCntr=0;
		for(ItemPriceDetail crntItemPrice:srcProduct.getItemPrice())	
		{
			crntPriceGrids=new PriceGrids();
			crntPriceGrids.setId(crntItemPrice.getPriceID());
			crntPriceGrids.setProductId(crntItemPrice.getProductID());
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
			currency = new Currency();
			currency.setCode("USD");
			currency.setName("US Dollar");
			currency.setiSODisplaySymbol("$");
			currency.setIsISO("true");
			currency.setIsActive("true");
			crntPriceGrids.setCurrency(currency);
			pricesList=new Prices[crntItemPrice.getPriceDetails().size()];
			for(PriceDetail priceDetail:crntItemPrice.getPriceDetails())
			{
				prices=new Prices();
				prices.setPriceGridId(crntItemPrice.getPriceID());
				prices.setQuantity(String.valueOf(priceDetail.getQuanty()));
				prices.setListPrice(String.valueOf(priceDetail.getPrice()));
				prices.setNetCost(String.valueOf(priceDetail.getNetCost()));
				prices.setItemsPerUnit(String.valueOf(priceDetail.getItemsPerUnit()));
				prices.setItemsPerUnit(String.valueOf(priceDetail.getItemsPerUnit()));
				discount=new DiscountRate();
				discount.setIndustryDiscountCode(priceDetail.getDiscount());
				discount.setCode(priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase()+priceDetail.getDiscount().toUpperCase());
				prices.setDiscountRate(discount);
				prices.setSequenceNumber(String.valueOf(priceDetail.getSequenceNumber()));
				pricesList[pricesCntr]=prices;
				//prices.setd
				pricesCntr++;
			}
			crntPriceGrids.setPrices(pricesList);
			pricegridList[priceGridCntr]=crntPriceGrids;
		}
		return pricegridList;
	}

	private PriceGrids getQURPriceGrid(Product crntProduct) {
		PriceGrids qurPriceGrid = new PriceGrids();
		qurPriceGrid.setId("0");
		qurPriceGrid.setProductId(String.valueOf(crntProduct.getID()));
		qurPriceGrid.setIsQUR("true");
		qurPriceGrid.setIsBasePrice("true");
		qurPriceGrid.setPriceGridSubTypeCode("REGL");
		qurPriceGrid.setUsageLevelCode("NONE");
		// qurPriceGrid.setDescription("")
		qurPriceGrid.setIsRange("false");
		qurPriceGrid.setIsSpecial("false");
		// qurPriceGrid.setPriceIncludes": "",
		qurPriceGrid.setDisplaySequence("1");
		qurPriceGrid.setIsCopy("false");
		Currency currency = new Currency();
		currency.setCode("USD");
		currency.setName("US Dollar");
		currency.setNumber("0");
		currency.setaSIDisplaySymbol("$");
		currency.setiSODisplaySymbol("$");
		currency.setIsISO("true");
		currency.setIsActive("true");
		currency.setDisplaySequence("1");
		qurPriceGrid.setCurrency(currency);
		PricingItems[] pricingItems = new PricingItems[] {};
		Prices[] prices = new Prices[] {};
		qurPriceGrid.setPricingItems(pricingItems);
		qurPriceGrid.setPrices(prices);
		return qurPriceGrid;
	}

	/*
	 * private String getDataSourceId(Integer companyId) {
	 * 
	 * return null; }
	 */
}