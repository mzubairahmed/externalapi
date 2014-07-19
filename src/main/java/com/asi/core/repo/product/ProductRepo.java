package com.asi.core.repo.product;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.core.exception.ResponseNotValidException;
import com.asi.ext.api.product.transformers.ImportTransformer;
import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.Batch;
import com.asi.service.product.client.vo.BatchDataSource;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.Relationships;
import com.asi.ext.api.integration.lookup.parser.ConfigurationsParser;
import com.asi.ext.api.integration.lookup.parser.ImprintParser;
import com.asi.ext.api.integration.lookup.parser.LookupParser;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.ImprintMethod;
import com.asi.service.product.vo.Imprints;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.ItemPriceDetail.PRICE_Type;
import com.asi.service.product.vo.PriceCriteria;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;
import com.asi.service.product.vo.SizeDetails;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

// import javax.ws.rs.core.MediaType;

@Component
public class ProductRepo {
    private final static Logger _LOGGER = LoggerFactory.getLogger(ProductRepo.class);
    private ImportTransformer productTransformer = new ImportTransformer();

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
    LookupParser  lookupsParser;
    String        productImportURL;

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
    LookupValuesClient   lookupColor;
    @Autowired
    ConfigurationsParser configurationParser;
    @Autowired
    ImprintParser        imprintParser;

    public ConfigurationsParser getConfigurationParser() {
        return configurationParser;
    }

    public void setConfigurationParser(ConfigurationsParser configurationParser) {
        this.configurationParser = configurationParser;
    }

    @Autowired
    RestTemplate productRestTemplate;

    public ImprintParser getImprintParser() {
        return imprintParser;
    }

    public void setImprintParser(ImprintParser imprintParser) {
        this.imprintParser = imprintParser;
    }

    public RestTemplate getProductRestTemplate() {
        return productRestTemplate;
    }

    public void setProductRestTemplate(RestTemplate productRestTemplate) {
        this.productRestTemplate = productRestTemplate;
    }

    /**
     * This method get Products from Radar API using the companyID and XID and return back as Service Model
     * 
     * @param companyId
     * @param xid
     * @return
     */
    public com.asi.ext.api.service.model.Product getProduct(String companyId, String xid) {
        return null;
    }

    public com.asi.ext.api.service.model.Product updateProduct(String companyId, String xid,
            com.asi.ext.api.service.model.Product serviceProduct) {
        com.asi.ext.api.radar.model.Product existingRadarProduct = null;
        try {
            existingRadarProduct = productClient.getRadarProduct(companyId, serviceProduct.getExternalProductId());
        } catch (ProductNotFoundException e) {
            _LOGGER.info("Product Not found with Existing, going to create new Product");
        }
        existingRadarProduct = productTransformer.generateRadarProduct(serviceProduct, existingRadarProduct, generateBatchDataSourceId(companyId));
        return null;
    }
    
    private String generateBatchDataSourceId(String companyId) {
        // TODO : Create new Batch record
        return "14277";
    }

    private Product prepairProduct(String companyID, String productID) throws ProductNotFoundException, RestClientException,
            UnsupportedEncodingException {
        productDetail = getProductFromService(companyID, productID);
        Product product = new Product();
        BeanUtils.copyProperties(productDetail, product);
        // product=lookupsParser.setProductConfigurations(productDetail,product);
        product = lookupsParser.setProductCategory(productDetail, product);
        product = lookupsParser.setProductServiceKeywords(productDetail, product);
        product = lookupsParser.setProductServiceDataSheet(productDetail, product);
        product = lookupsParser.setProductServiceInventoryLink(productDetail, product);
        product = lookupsParser.setProductServiceBasePriceInfo(productDetail, product);
        product = lookupsParser.setProductServiceWithConfigurations(productDetail, product);

        // product.setNewProductExpirationDate(productDetail.getn)

        return product;
    }

    public ProductDetail getProductFromService(String companyID, String productID) throws ProductNotFoundException {
        if (null != productDetail) productDetail = productClient.doIt(companyID, productID);

        return productDetail;

    }

    public Product getProductPrices(String companyID, String productID) throws ProductNotFoundException {

        Product product = null;
        try {
            product = prepairProduct(companyID, productID);
        } catch (RestClientException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<PriceGrid> priceGrids = productDetail.getPriceGrids();
        List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();

        for (PriceGrid prices : priceGrids) {
            if (prices.getPriceGridSubTypeCode().equalsIgnoreCase(PRICE_Type.REGL.name()))
                pricesInfo.add(getBasePriceDetails(productDetail, ItemPriceDetail.PRICE_Type.REGL, prices, false));
        }
        if (pricesInfo.isEmpty())
            _LOGGER.error("Invalid price grid id or price grid not found for company %1 product %2", companyID, productID);

        product.setItemPrice(pricesInfo);

        return product;

    }

    public Product getProductPrices(String companyID, String productID, Integer priceGridID) throws ProductNotFoundException,
            RestClientException, UnsupportedEncodingException {

        productDetail = getProductFromService(companyID, productID);

        List<PriceGrid> priceGrids = productDetail.getPriceGrids();
        List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();
        ItemPriceDetail itemPrice;

        Product product = prepairProduct(companyID, productID);

        for (PriceGrid prices : priceGrids) {
            if (prices.getPriceGridSubTypeCode().equalsIgnoreCase(PRICE_Type.REGL.name()) && prices.getID().equals(priceGridID)) {
                itemPrice = getBasePriceDetails(productDetail, ItemPriceDetail.PRICE_Type.REGL, prices, false);
                pricesInfo.add(itemPrice);
            }
        }
        if (pricesInfo.isEmpty())
            _LOGGER.error("Invalid price grid id or price grid not found for company %1 product %2 with priceGridId %3", companyID,
                    productID, priceGridID);

        product.setItemPrice(pricesInfo);

        return product;

    }

    private ItemPriceDetail getBasePriceDetails(ProductDetail productDetail, ItemPriceDetail.PRICE_Type priceType,
            PriceGrid priceGrid, boolean setCurrency) {
        ItemPriceDetail itemPrice = new ItemPriceDetail();
        itemPrice.setPriceType(priceType);
        itemPrice.setPriceName(priceGrid.getDescription());
        itemPrice.setPriceIncludes(priceGrid.getPriceIncludes());
        itemPrice.setPriceUponRequest(priceGrid.getIsQUR());
        itemPrice.setIsBasePrice(String.valueOf(priceGrid.getIsBasePrice()));
        // itemPrice.setProductNumber(priceGrid.getp);
        List<PriceDetail> pricesList = new ArrayList<PriceDetail>();

        for (Price p : priceGrid.getPrices()) {
            PriceDetail priceDetail = new PriceDetail();
            priceDetail.setSequenceNumber(p.getSequenceNumber());
            priceDetail.setPrice(p.getListPrice());
            priceDetail.setQuanty(p.getQuantity());
            priceDetail.setDiscount(p.getDiscountRate().getIndustryDiscountCode());
            priceDetail.setHighQuantity(p.getHighQuantity());
            priceDetail.setLowQuantity(p.getLowQuantity());
            priceDetail.setItemsPerUnit(p.getItemsPerUnit());
            priceDetail.setNetCost(p.getNetCost());
            priceDetail.setItemsPerUnitBy(p.getPriceUnit().getDisplayName());
            pricesList.add(priceDetail);
        }
        itemPrice.setProductID(String.valueOf(productDetail.getID()));
        itemPrice.setPriceDetails(pricesList);
        itemPrice.setPriceID(priceGrid.getID().toString());
        String[] basePriceCriterias = configurationParser.getPriceCriteria(productDetail, priceGrid.getID());
        PriceCriteria[] priceCriterias = new PriceCriteria[basePriceCriterias.length];
        int criteriaCntr = 0;
        for (String crntBasePriceCriteria : basePriceCriterias) {
            if (null != crntBasePriceCriteria && !crntBasePriceCriteria.isEmpty() && !crntBasePriceCriteria.startsWith("null")) {
                priceCriterias[criteriaCntr] = new PriceCriteria();
                priceCriterias[criteriaCntr]
                        .setCriteriaCode(crntBasePriceCriteria.substring(0, crntBasePriceCriteria.indexOf(":")));
                priceCriterias[criteriaCntr].setValue(crntBasePriceCriteria.substring(crntBasePriceCriteria.indexOf(":") + 1));
            } else {
                priceCriterias = Arrays.copyOf(priceCriterias, priceCriterias.length - 1);
            }

            criteriaCntr++;
        }
        if (null != priceCriterias && priceCriterias.length > 0) {
            itemPrice.setPriceCriteria(priceCriterias);
        }
        /*
         * if (null != basePriceCriterias && basePriceCriterias.length > 0) {
         * itemPrice.setFirstPriceCriteria(basePriceCriterias[0]);
         * if (basePriceCriterias.length > 1) {
         * itemPrice.setSecondPriceCriteria(basePriceCriterias[1]);
         * }
         * itemPrice.setPriceID(priceGrid.getID().toString());
         * }
         */
        return itemPrice;

    }

    public Product getProductImprintMethodDetails(String companyId, String xid) throws ProductNotFoundException {
        Product product = null;
        try {
            product = prepairProduct(companyId, xid);
        } catch (RestClientException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        product.setImprints(getProductImprintMethods(companyId, xid));
        return product;
    }

    public Imprints getProductImprintMethods(String companyId, String xid) throws ProductNotFoundException {
        productDetail = getProductFromService(companyId, xid);
        List<ImprintMethod> imprintMethodsList = new ArrayList<ImprintMethod>();
        		ProductConfiguration productConfiguration = productDetail
				.getProductConfigurations().get(0);
		ProductCriteriaSets imprintCriteriaSet = lookupsParser
				.getCriteriaSetBasedOnCriteriaCode(
                productConfiguration.getProductCriteriaSets(), "IMMD");
        if (null != imprintCriteriaSet) {
            List<CriteriaSetValues> criteriaSetValues = imprintCriteriaSet.getCriteriaSetValues();
            for (CriteriaSetValues criteriaSetValue : criteriaSetValues) {
                imprintMethodsList = imprintParser.getImprintMethodRelations(productDetail.getExternalProductId(),
                        Integer.parseInt(criteriaSetValue.getCriteriaSetId()), productConfiguration.getProductCriteriaSets(),
                        productDetail.getRelationships());
            }
        }
        Imprints imprints = new Imprints();
        imprints.setImprintMethod(imprintMethodsList);
        return imprints;

    }

    public Product updateProductBasePrices(String companyId, com.asi.ext.api.service.model.Product serviceProduct, String process)
            throws ProductNotFoundException, ResponseNotValidException, RestClientException, UnsupportedEncodingException {
        ProductDetail currentRadarProduct = productClient.doIt(companyId, serviceProduct.getExternalProductId());
        Product currentProduct = null;
        try {
            // Product checkProductExistance=null;
            com.asi.service.product.client.vo.Product velocityBean = new com.asi.service.product.client.vo.Product();
            velocityBean = setProductWithPriceDetails(currentProduct, currentRadarProduct);
            velocityBean = setProductWithBasicDetails(currentProduct, currentRadarProduct, velocityBean);
            // velocityBean = setProductWithProductConfigurations(currentProduct,velocityBean);
            // velocityBean.setDataSourceId("12938");
            // velocityBean = productConfiguration.setProductWithImprintDetails(currentProduct,velocityBean,lookupsParser,"IMMD");
            velocityBean.setDataSourceId(String.valueOf(getDataSourceId(currentRadarProduct)));

            // Set Hidden Variables
            velocityBean.setIsWIP("true");
            velocityBean.setShow1MediaIdIm("0");
            velocityBean.setShow1MediaIdVd("0");

            productRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            productRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "com.asi.util.json"));

            ObjectMapper mapper = new ObjectMapper();
            String productJson = null;
            try {
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                productJson = mapper.writeValueAsString(velocityBean);
                _LOGGER.info("Product Json:" + productJson);
            } catch (Exception e) {
                _LOGGER.info(e.getMessage());
            }
            HttpEntity<com.asi.service.product.client.vo.Product> requestEntity = new HttpEntity<com.asi.service.product.client.vo.Product>(
                    velocityBean, requestHeaders);
            ResponseEntity<Object> responseEntity = null;
            responseEntity = productRestTemplate.exchange(productImportURL, HttpMethod.POST, requestEntity, Object.class);
            _LOGGER.info("Product Respones Status:" + responseEntity);
        } catch (HttpClientErrorException hcerror) {
            throw new ResponseNotValidException(String.valueOf(currentProduct.getID()));
        } catch (Exception ex) {
            ex.printStackTrace();
            ProductNotFoundException exc = new ProductNotFoundException(String.valueOf(currentProduct.getID()));
            exc.setStackTrace(ex.getStackTrace());
            throw exc;
        }
        currentProduct = prepairProduct(String.valueOf(currentProduct.getCompanyId()), currentProduct.getExternalProductId());
        return currentProduct;
    }

    /*
     * private com.asi.service.product.client.vo.Product setProductWithProductConfigurations(
     * Product currentProduct, com.asi.service.product.client.vo.Product velocityBean) {
     * BeanUtils.copyProperties(currentProduct, velocityBean);
     * // BeanUtils.copyProperties(currentProduct.getProductConfigurations(), velocityBean.getProductConfigurations());
     * 
     * //velocityBean.setProductConfigurations(productConfiguration.transformProductConfiguration(currentProduct.
     * getProductConfigurations()));
     * return velocityBean;
     * }
     */

    @SuppressWarnings("unchecked")
    private String getDataSourceId(ProductDetail currentProduct) throws Exception {
        String dataSourceId = "0";
        Batch batchData = new Batch();
        batchData.setBatchId(0);
        batchData.setBatchTypeCode("IMRT");
        batchData.setStartDate(String.valueOf(new Timestamp(System.currentTimeMillis()).toString()));
        batchData.setStatus("N");
        batchData.setCompanyId(String.valueOf(currentProduct.getCompanyId()));
        BatchDataSource batchDataSources = new BatchDataSource();
        batchDataSources.setBatchId(0);
        batchDataSources.setId(0);
        batchDataSources.setDescription("Batch Created by API");
        batchDataSources.setName("ASIF");
        batchDataSources.setTypeCode("IMRT");
        batchData.setBatchDataSources(new ArrayList<com.asi.service.product.client.vo.BatchDataSource>(Arrays
                .asList(batchDataSources)));
        productRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        productRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        LinkedHashMap<String, String> batchDetails = productRestTemplate.postForObject(batchProcessingURL, batchData,
                LinkedHashMap.class);
        String batchId = String.valueOf(batchDetails.get("BatchId"));
        if (null != batchId && !batchId.equals("0")) {
            LinkedHashMap<Object, ArrayList<LinkedHashMap<String, String>>> crntObj = productRestTemplate.getForObject(
                    batchProcessingURL + "/" + batchId, LinkedHashMap.class);
            List<LinkedHashMap<String, String>> batchDataSourceList = (ArrayList<LinkedHashMap<String, String>>) crntObj
                    .get("BatchDataSources");
            dataSourceId = String.valueOf(batchDataSourceList.get(0).get("Id"));
        }
        return dataSourceId;
    }

    private com.asi.service.product.client.vo.Product setProductWithPriceDetails(Product srcProduct,
            ProductDetail currentProductDetails) throws RestClientException, UnsupportedEncodingException {

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
        /*
         * productDataSheet.setProductId(String.valueOf(srcProduct.getID()));
         * productDataSheet
         * .setCompanyId(String.valueOf(srcProduct.getCompanyId()));
         * productDataSheet.setId("0");
         */
        if (null != srcProduct.getProductDataSheet()) {
            productDataSheet.setUrl(srcProduct.getProductDataSheet().getUrl());
        }
        productToUpdate.setProductDataSheet(productDataSheet);
        // Product Color
        if (null != srcProduct.getColor() && !srcProduct.getColor().isEmpty()) {
            String productColor = srcProduct.getColor();
            // productToUpdate=productConfiguration.transformProductColors(productColor);
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "PRCL", productColor);
        }
        // Product Material
        if (null != srcProduct.getMaterial() && !srcProduct.getMaterial().isEmpty()) {
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "MTRL", srcProduct.getMaterial());
        }
        // Product Origin
        if (null != srcProduct.getOrigin() && !srcProduct.getOrigin().isEmpty()) {
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "ORGN", srcProduct.getOrigin());
        }
        // Product Packaging
        if (null != srcProduct.getPackages() && !srcProduct.getPackages().isEmpty()) {
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "PCKG", srcProduct.getPackages());
        }
        // Product Shapes
        if (null != srcProduct.getShape() && !srcProduct.getShape().isEmpty()) {
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "SHAP", srcProduct.getShape());
        }

        // Product TradeName
        if (null != srcProduct.getTradeName() && !srcProduct.getTradeName().isEmpty()) {
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "TDNM", srcProduct.getTradeName());
        }
        // Imprint Processing
        if (null != srcProduct.getImprints() && srcProduct.getImprints().getImprintMethod().size() > 0) {
			int imprintCntr=0,artworkCntr=0,minCntr=0;
            String finalImprintMethod = "";
			String finalArtworks="";
			String finalMinOrders="";
            for (ImprintMethod currentImprintMethod : srcProduct.getImprints().getImprintMethod()) {
                finalImprintMethod = (imprintCntr == 0) ? currentImprintMethod.getMethodName() : finalImprintMethod + ","
                        + currentImprintMethod.getMethodName();
				if(null!=currentImprintMethod.getArtworkName() && !currentImprintMethod.getArtworkName().isEmpty()){
				if(artworkCntr==0){
					finalArtworks=currentImprintMethod.getArtworkName();
					artworkCntr++;
				}else{
					finalArtworks=finalArtworks+","+currentImprintMethod.getArtworkName();
				}
            }
				if(null!=currentImprintMethod.getMinimumOrder() && !currentImprintMethod.getMinimumOrder().isEmpty()){
					if(minCntr==0){
						finalMinOrders=currentImprintMethod.getMinimumOrder();
						minCntr++;
					}else{
						finalMinOrders=finalMinOrders+","+currentImprintMethod.getMinimumOrder();
					}
					}					
				imprintCntr++;				
			}
            productToUpdate = configurationParser.setProductWithProductConfigurations(srcProduct, currentProductDetails,
                    productToUpdate, lookupsParser, "IMMD", finalImprintMethod);
			productToUpdate=configurationParser.setProductWithProductConfigurations(srcProduct,currentProductDetails,productToUpdate,lookupsParser,"ARTW",finalArtworks);
			productToUpdate=configurationParser.setProductWithProductConfigurations(srcProduct,currentProductDetails,productToUpdate,lookupsParser,"MINO",finalMinOrders);
			productToUpdate=imprintParser.setImprintRelations(srcProduct.getImprints().getImprintMethod(),currentProductDetails,productToUpdate);
        }
        // Size Processing
        SizeDetails sizeDetails = srcProduct.getSize();
        if (null != sizeDetails && null != sizeDetails.getGroupName()) {
            if (sizeDetails.getGroupName().contains("Apparel") || sizeDetails.getGroupName().contains("Other")
                    || sizeDetails.getGroupName().contains("Standard") || sizeDetails.getGroupName().contains("Volume")) {
                productToUpdate = configurationParser.setProductWithSizeApperalConfigurations(srcProduct, currentProductDetails,
                        productToUpdate, lookupsParser, sizeDetails.getGroupName(), sizeDetails.getSizeValue());
            } else {
                productToUpdate = configurationParser.setProductWithSizeConfigurations(srcProduct, currentProductDetails,
                        productToUpdate, lookupsParser, sizeDetails.getGroupName(), sizeDetails.getSizeValue());
            }
        }
        // Product Category
        String sProductCategory = srcProduct.getCategory();
        if (null != sProductCategory || (!StringUtils.isEmpty(sProductCategory))) {
            String[] arrayProductCtgrs = sProductCategory.split(",");
            int productCtrgyCntr = 0;
            if (null != arrayProductCtgrs && arrayProductCtgrs.length > 0) {
                com.asi.service.product.client.vo.SelectedProductCategories productCategory = null;
                com.asi.service.product.client.vo.SelectedProductCategories[] productCategoryAry = new com.asi.service.product.client.vo.SelectedProductCategories[arrayProductCtgrs.length];
                for (String crntCategory : arrayProductCtgrs) {
                    productCategory = new com.asi.service.product.client.vo.SelectedProductCategories();
                    crntCategory = lookupsParser.getCategoryCodeByName(crntCategory.trim());
                    if (null != crntCategory) {
                        productCategory.setCode(crntCategory);
                        productCategory.setProductId(String.valueOf(srcProduct.getID()));
                        productCategory.setIsPrimary(String.valueOf(Boolean.FALSE));
                        productCategory.setAdCategoryFlg(String.valueOf(Boolean.FALSE));
                    }
                    productCategoryAry[productCtrgyCntr] = productCategory;
                }
                productToUpdate.setSelectedProductCategories(productCategoryAry);
            }
        }
        // Product Keywords
        productToUpdate = lookupsParser.setProductKeyWords(productToUpdate, srcProduct);
        if (null == productToUpdate.getRelationships()) {
			productToUpdate.setRelationships(new ArrayList<Relationships>());
        }
        // Product Inventory Link
        com.asi.service.product.client.vo.ProductInventoryLink productInventoryLink = new com.asi.service.product.client.vo.ProductInventoryLink();
        productInventoryLink.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
        productInventoryLink.setProductId(String.valueOf(srcProduct.getID()));
        productInventoryLink.setId("0");
        if (null != srcProduct.getProductInventoryLink())
            productInventoryLink.setUrl(srcProduct.getProductInventoryLink().getUrl());
        productToUpdate.setProductInventoryLink(productInventoryLink);

        // Price Details
        if (srcProduct.getItemPrice().size() == 0) {
            com.asi.service.product.client.vo.PriceGrids priceGrid = getQURPriceGrid(srcProduct);
            productToUpdate.setPriceGrids(new com.asi.service.product.client.vo.PriceGrids[] { priceGrid });
        } else {
            productToUpdate.setPriceGrids(setPriceDetails(srcProduct));
        }

        return productToUpdate;
    }

    private com.asi.service.product.client.vo.Product setProductWithBasicDetails(Product srcProduct,
            ProductDetail currentProductDetails, com.asi.service.product.client.vo.Product currentProduct) {
        BeanUtils.copyProperties(srcProduct, currentProduct);
        // Product DataSheet
        com.asi.service.product.client.vo.ProductDataSheet productDataSheet = new com.asi.service.product.client.vo.ProductDataSheet();
        /*
         * productDataSheet.setProductId(String.valueOf(srcProduct.getID()));
         * productDataSheet
         * .setCompanyId(String.valueOf(srcProduct.getCompanyId()));
         * productDataSheet.setId("0");
         */
        if (null != srcProduct.getProductDataSheet()) {
            productDataSheet.setUrl(srcProduct.getProductDataSheet().getUrl());
        }
        currentProduct.setProductDataSheet(productDataSheet);
        // Product Category
        com.asi.service.product.client.vo.SelectedProductCategories[] productCategoriesLst = null;
        com.asi.service.product.client.vo.SelectedProductCategories productCategories = null;
        String productCategory = srcProduct.getCategory();
		if(null!=productCategory && !productCategory.isEmpty()){
        String[] productCtgrs = productCategory.split(",");
        int productCategoryCntr = 0;
        if (null != productCtgrs && productCtgrs.length > 0) {
            productCategoriesLst = new com.asi.service.product.client.vo.SelectedProductCategories[productCtgrs.length];
            for (String crntCategory : productCtgrs) {
                productCategories = new com.asi.service.product.client.vo.SelectedProductCategories();
                crntCategory = lookupsParser.getCategoryCodeByName(crntCategory.trim());
                if (null != crntCategory) {
                    productCategories.setCode(crntCategory);
                    productCategories.setProductId(String.valueOf(srcProduct.getID()));
                    productCategories.setIsPrimary("false");
                    productCategories.setAdCategoryFlg("false");
                }
                productCategoriesLst[productCategoryCntr] = productCategories;
                productCategoryCntr++;
            }
        }

        currentProduct.setSelectedProductCategories(productCategoriesLst);
		}
			
        // Product Keywords
        currentProduct = lookupsParser.setProductKeyWords(currentProduct, srcProduct);
        // Product Inventory Link
        com.asi.service.product.client.vo.ProductInventoryLink productInventoryLink = new com.asi.service.product.client.vo.ProductInventoryLink();
        productInventoryLink.setCompanyId(String.valueOf(srcProduct.getCompanyId()));
        productInventoryLink.setProductId(String.valueOf(srcProduct.getID()));
        productInventoryLink.setId("0");
        if (null != srcProduct.getProductInventoryLink())
            productInventoryLink.setUrl(srcProduct.getProductInventoryLink().getUrl());
        currentProduct.setProductInventoryLink(productInventoryLink);
        return currentProduct;
    }

    private com.asi.service.product.client.vo.PriceGrids[] setPriceDetails(Product srcProduct) {
        com.asi.service.product.client.vo.PriceGrids[] pricegridList = {};
        com.asi.service.product.client.vo.PriceGrids crntPriceGrids = null;
        com.asi.service.product.client.vo.Currency currency = null;
        com.asi.service.product.client.vo.Prices[] pricesList = {};
        com.asi.service.product.client.vo.Prices prices = null;
        com.asi.service.product.client.vo.DiscountRate discount = null;
        int priceGridCntr = 0;
        if (null != srcProduct.getItemPrice() && srcProduct.getItemPrice().size() > 0) {
            pricegridList = new com.asi.service.product.client.vo.PriceGrids[srcProduct.getItemPrice().size()];
            for (ItemPriceDetail crntItemPrice : srcProduct.getItemPrice()) {
                crntPriceGrids = new com.asi.service.product.client.vo.PriceGrids();
                crntPriceGrids.setId(crntItemPrice.getPriceID());
                crntPriceGrids.setProductId(String.valueOf(srcProduct.getID()));
                crntPriceGrids.setDescription(crntItemPrice.getPriceName());
                if (null != crntItemPrice.getPriceType() && crntItemPrice.getPriceType().toString().equals("REGL")) {
                    crntPriceGrids.setIsBasePrice("true");
                    crntPriceGrids.setPriceGridSubTypeCode(crntItemPrice.getPriceType().toString());
                }
                /*
                 * if(!crntItemPrice.getPriceDetails().isEmpty() && crntItemPrice.getPriceDetails().size()>0)
                 * {
                 * crntPriceGrids.setIsQUR("false");
                 * }
                 */
                crntPriceGrids.setIsQUR(String.valueOf(crntItemPrice.getPriceUponRequest()));
                crntPriceGrids.setUsageLevelCode("NONE");
                crntPriceGrids.setPriceIncludes(crntItemPrice.getPriceIncludes());
                currency = new com.asi.service.product.client.vo.Currency();
                currency.setCode("USD");
                currency.setName("US Dollar");
                currency.setiSODisplaySymbol("$");
                currency.setIsISO("true");
                currency.setIsActive("true");
                crntPriceGrids.setCurrency(currency);
                int pricesCntr = 0;
                pricesList = new com.asi.service.product.client.vo.Prices[crntItemPrice.getPriceDetails().size()];
                for (PriceDetail priceDetail : crntItemPrice.getPriceDetails()) {
                    prices = new com.asi.service.product.client.vo.Prices();
                    prices.setPriceGridId(crntItemPrice.getPriceID());
                    prices.setQuantity(String.valueOf(priceDetail.getQuanty()));
                    prices.setListPrice(String.valueOf(priceDetail.getPrice()));
                    prices.setNetCost(String.valueOf(priceDetail.getNetCost()));
                    prices.setItemsPerUnit(String.valueOf(priceDetail.getItemsPerUnit()));
                    prices.setItemsPerUnit(String.valueOf(priceDetail.getItemsPerUnit()));
                    discount = new com.asi.service.product.client.vo.DiscountRate();
                    discount.setIndustryDiscountCode(priceDetail.getDiscount());
                    discount.setCode(priceDetail.getDiscount().toUpperCase() + priceDetail.getDiscount().toUpperCase()
                            + priceDetail.getDiscount().toUpperCase() + priceDetail.getDiscount().toUpperCase());
                    prices.setDiscountRate(discount);
                    prices.setSequenceNumber(String.valueOf(priceDetail.getSequenceNumber()));
                    pricesList[pricesCntr++] = prices;
                }
                crntPriceGrids.setPrices(pricesList);
                pricegridList[priceGridCntr++] = crntPriceGrids;
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
        qurPriceGrid.setCurrency(setCurrency(1, 0));
        return qurPriceGrid;
    }

    private com.asi.service.product.client.vo.Currency setCurrency(int displaySequence, int number) {
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

	public com.asi.ext.api.service.model.Product getServiceProduct(
			String companyId, String xid) {
		com.asi.ext.api.service.model.Product serviceProduct=null;
		try {
			ProductDetail currentProductDetails = getProductFromService(companyId, xid);
			//serviceProduct=prepairServiceProduct();
			if(null!=currentProductDetails){
				serviceProduct=new com.asi.ext.api.service.model.Product();
				serviceProduct.setName(currentProductDetails.getName());
			}
			
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return serviceProduct;
	}

    /*
     * private String getDataSourceId(Integer companyId) {
     * 
     * return null; }
     */
}