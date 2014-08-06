package com.asi.core.repo.product;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.integration.lookup.parser.ConfigurationsParser;
import com.asi.ext.api.integration.lookup.parser.ImprintParser;
import com.asi.ext.api.integration.lookup.parser.LookupParser;
import com.asi.ext.api.product.transformers.ImportTransformer;
import com.asi.ext.api.product.transformers.PriceGridParser;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.service.model.Catalog;
import com.asi.ext.api.service.model.Image;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.Batch;
import com.asi.service.product.client.vo.BatchDataSource;
import com.asi.service.product.client.vo.ProductDataSheet;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductInventoryLink;
import com.asi.service.product.client.vo.ProductKeywords;
import com.asi.service.product.client.vo.ProductMediaCitations;
import com.asi.service.product.client.vo.ProductMediaItems;
import com.asi.service.product.client.vo.SelectedComplianceCert;
import com.asi.service.product.client.vo.SelectedProductCategory;
import com.asi.service.product.client.vo.SelectedSafetyWarnings;
import com.asi.service.product.exception.ProductNotFoundException;
import com.asi.service.product.vo.Product;
import com.asi.service.resource.response.ExternalAPIResponse;

@Component
public class ProductRepo {
    private final static Logger _LOGGER            = LoggerFactory.getLogger(ProductRepo.class);
    private ImportTransformer   productTransformer = new ImportTransformer();

    private ProductDataStore    lookupDataStore    = new ProductDataStore();

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
    PriceGridParser priceGridParser=new PriceGridParser();
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

    public ExternalAPIResponse updateProduct(String companyId, String xid, com.asi.ext.api.service.model.Product serviceProduct) {
        ProductDetail existingRadarProduct = null;
        try {
            existingRadarProduct = productClient.getRadarProduct(companyId, serviceProduct.getExternalProductId());
        } catch (ProductNotFoundException e) {
            _LOGGER.info("Product Not found with Existing, going to create new Product");
        }
        try {
            // Doing Transformation of Service product to pure Radar object model (Core Component)
            existingRadarProduct = productTransformer.generateRadarProduct(serviceProduct, existingRadarProduct,
                    generateBatchDataSourceId(companyId), companyId);
        } catch (Exception e) {
            _LOGGER.error("Exception while generating Radar product", e);
            return productClient.convertExceptionToResponseModel(e);
        }
        // Saving product to Radar API
        ExternalAPIResponse response = productClient.saveProduct(existingRadarProduct);
        return appendErrorLogsToResponse(response, existingRadarProduct.getExternalProductId());
    }

    private ExternalAPIResponse appendErrorLogsToResponse(ExternalAPIResponse response, String xid) {
        Set<String> errors = ProductDataStore.getBatchErrors(xid);
        response.setAdditionalInfo(errors);
        return response;
    }

    private String generateBatchDataSourceId(String companyId) {
        try {
            return getDataSourceId(companyId);
        } catch (Exception e) {
            _LOGGER.error("Batch Creation failed ", e);
            return "0";
        }
    }

    @SuppressWarnings("unused")
    private Product prepairProduct(String companyID, String productID) throws ProductNotFoundException, RestClientException,
            UnsupportedEncodingException {
        productDetail = getProductFromService(companyID, productID);
        Product product = new Product();
        BeanUtils.copyProperties(productDetail, product);
        // product=lookupsParser.setProductConfigurations(productDetail,product);
        /*
         * product = lookupsParser.setProductCategory(productDetail, product);
         * product = lookupsParser.setProductServiceKeywords(productDetail, product);
         * product = lookupsParser.setProductServiceDataSheet(productDetail, product);
         * product = lookupsParser.setProductServiceInventoryLink(productDetail, product);
         * product = lookupsParser.setProductServiceBasePriceInfo(productDetail, product);
         * product = lookupsParser.setProductServiceWithConfigurations(productDetail, product);
         */
        // product.setNewProductExpirationDate(productDetail.getn)

        return product;
    }

    public ProductDetail getProductFromService(String companyID, String productID) throws ProductNotFoundException {
        productDetail = productClient.doIt(companyID, productID);

        return productDetail;

    }

    @SuppressWarnings("unchecked")
    private String getDataSourceId(String companyId) throws Exception {
        String dataSourceId = "0";
        Batch batchData = new Batch();
        batchData.setBatchId(0);
        batchData.setBatchTypeCode("IMRT");
        batchData.setStartDate(String.valueOf(new Timestamp(System.currentTimeMillis()).toString()));
        batchData.setStatus("N");
        batchData.setCompanyId(String.valueOf(companyId));
        BatchDataSource batchDataSources = new BatchDataSource();
        batchDataSources.setBatchId(0);
        batchDataSources.setId(0);
        batchDataSources.setDescription("Batch Created by External API");
        batchDataSources.setName("Batch ExtAPI");
        batchDataSources.setTypeCode("IMRT"); // TODO: Doubt full need to check with Chuck
        batchData.setBatchDataSources(new ArrayList<com.asi.service.product.client.vo.BatchDataSource>(Arrays
                .asList(batchDataSources)));
        productRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        productRestTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        LinkedHashMap<String, String> batchDetails = productRestTemplate.postForObject(batchProcessingURL, batchData,
                LinkedHashMap.class);
        String batchId = String.valueOf(batchDetails.get("BatchId"));
        _LOGGER.debug("Batch ID: " + batchId);
        if (null != batchId && !batchId.equals("0")) {
            LinkedHashMap<Object, ArrayList<LinkedHashMap<String, String>>> crntObj = productRestTemplate.getForObject(
                    batchProcessingURL + "/" + batchId, LinkedHashMap.class);
            List<LinkedHashMap<String, String>> batchDataSourceList = (ArrayList<LinkedHashMap<String, String>>) crntObj
                    .get("BatchDataSources");
            dataSourceId = String.valueOf(batchDataSourceList.get(0).get("Id"));
        }
        return dataSourceId;
    }

    public com.asi.ext.api.service.model.Product getServiceProduct(String companyId, String xid) {
        com.asi.ext.api.service.model.Product serviceProduct = null;
        try {
            productDetail = getProductFromService(companyId, xid);
            // serviceProduct=prepairServiceProduct();
            if (null != productDetail) {
                serviceProduct = new com.asi.ext.api.service.model.Product();
                BeanUtils.copyProperties(productDetail, serviceProduct);
                serviceProduct.setShipperBillsBy(productDetail.getShipperBillsByCode());
                serviceProduct = setBasicProductDetails(productDetail, serviceProduct);
                serviceProduct=configurationParser.setProductWithConfigurations(productDetail, serviceProduct);
                serviceProduct=priceGridParser.setProductWithPriceGrids(productDetail,serviceProduct);
/*                List<com.asi.ext.api.service.model.PriceGrid> priceGridList = new ArrayList<>();
                serviceProduct.setPriceGrids(priceGridList);*/
                // serviceProduct.setName(productDetail.getName());
            }

        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return serviceProduct;
    }

    private com.asi.ext.api.service.model.Product setBasicProductDetails(ProductDetail radProduct,
            com.asi.ext.api.service.model.Product serviceProduct) {
        // Selected Safety Warnings
        List<SelectedSafetyWarnings> safetyWarningsList = radProduct.getSelectedSafetyWarnings();
        List<String> finalSafetyWrngs = new ArrayList<>();
        for (SelectedSafetyWarnings currentSafetyWrng : safetyWarningsList) {
            finalSafetyWrngs.add(lookupDataStore.getSelectedSafetyWarningNameByCode(currentSafetyWrng.getCode()));
        }
        serviceProduct.setSafetyWarnings(finalSafetyWrngs);

        // Status Code
        serviceProduct.setStatusCode(radProduct.getStatusCode().equalsIgnoreCase("ACTV") ? "Active" : "In Active");

        // Compliance certs
        List<SelectedComplianceCert> complianceCertsList = radProduct.getSelectedComplianceCerts();
        List<String> finalComplianceCerts = new ArrayList<>();
        for (SelectedComplianceCert currentCompliance : complianceCertsList) {
        	if(currentCompliance.getComplianceCertId().equals("-1")){
        		finalComplianceCerts.add(currentCompliance.getDescription());
        	}else{            
        		finalComplianceCerts.add(lookupDataStore.getComplianceCertNameById(String.valueOf(currentCompliance.getComplianceCertId())));
        	}
        }
        serviceProduct.setComplianceCerts(finalComplianceCerts);

        // Keywords
        List<ProductKeywords> productKeywordsList = radProduct.getProductKeywords();
        List<String> finalKeywords = new ArrayList<>();
        for (ProductKeywords currentKeyword : productKeywordsList) {
            finalKeywords.add(currentKeyword.getValue());
        }
        serviceProduct.setProductKeywords(finalKeywords);

        // Categories
        List<SelectedProductCategory> productCategoriesList = radProduct.getSelectedProductCategories();
        List<String> finalCategoriesList = new ArrayList<>();
        for (SelectedProductCategory currentCategory : productCategoriesList) {
            finalCategoriesList.add(lookupDataStore.findCategoryNameByCode(currentCategory.getCode()));
        }
        serviceProduct.setCategories(finalCategoriesList);

        // Inventory Link
        ProductInventoryLink inventoryList = radProduct.getProductInventoryLink();
        if (null != inventoryList) serviceProduct.setProductInventoryLink(inventoryList.getUrl());

        // Data Sheet
        ProductDataSheet prodDatasheet = radProduct.getProductDataSheet();
        if (null != prodDatasheet) serviceProduct.setProductDataSheet(prodDatasheet.getUrl());

        // Same Day Rush
        if (null != radProduct.getSameDayRushFlag() && radProduct.getSameDayRushFlag().equalsIgnoreCase("U")) {
            serviceProduct.setSameDayRushOffered(true);
        } else
            serviceProduct.setSameDayRushOffered(false);

        // Product Type Code
        if(null!=radProduct.getProductTypeCode() && !radProduct.getProductTypeCode().trim().isEmpty()){
        serviceProduct.setProductType(lookupDataStore.findProdTypeNameByCode(radProduct.getProductTypeCode()));	
        }else{
        	serviceProduct.setProductType(ApplicationConstants.CONST_STRING_EMPTY);
        }
        
        // Imaging
        if(null!=radProduct.getProductMediaItems() && radProduct.getProductMediaItems().size()>0){
        	List<Image> imagesList=new ArrayList<>();
        	Image currentImage=null;
        	for(ProductMediaItems currentProductMediaItems:radProduct.getProductMediaItems()){
        		currentImage=new Image();
        		currentImage.setRank(currentProductMediaItems.getMediaRank());
        		currentImage.setIsPrimary(currentProductMediaItems.getIsPrimary());
        		currentImage.setImageURL(currentProductMediaItems.getMedia().getUrl());
        		imagesList.add(currentImage);
        	}
        	serviceProduct.setImages(imagesList);
        }

        // Catalog
        if(null!=radProduct.getProductMediaCitations() && radProduct.getProductMediaCitations().size()>0){
        	List<Catalog> catalogsList=new ArrayList<>();
        	Catalog catalog=null;
        	for(ProductMediaCitations currentMediaCitation:radProduct.getProductMediaCitations()){
        		catalog=new Catalog();
        		catalogsList.add(ProductDataStore.getMediaCitationById(currentMediaCitation.getMediaCitationId(), currentMediaCitation.getProductMediaCitationReferences().get(0).getMediaCitationReferenceId(), radProduct.getCompanyId()));
        	}        	
        	serviceProduct.setCatalogs(catalogsList);
        }

        // Misclenious
        serviceProduct.setDistributorOnly(radProduct.getIncludeAppOfferList());
        serviceProduct.setDistributorOnlyComments(radProduct.getDistributorComments());
        serviceProduct.setProductDisclaimer(radProduct.getDisclaimer());
        serviceProduct.setAdditionalProductInfo(radProduct.getAdditionalInfo());
        serviceProduct.setAdditionalShippingInfo(radProduct.getAdditionalShippingInfo());
        serviceProduct.setPriceConfirmedThru(radProduct.getPriceConfirmationDate());
        serviceProduct.setCanOrderLessThanMimimum(String.valueOf(radProduct.getIsOrderLessThanMinimumAllowed()));
        
        
        return serviceProduct;
    }

    public ProductDataStore getLookupDataStore() {
        return lookupDataStore;
    }

    public void setLookupDataStore(ProductDataStore lookupDataStore) {
        this.lookupDataStore = lookupDataStore;
    }
    /*
     * private String getDataSourceId(Integer companyId) {
     * 
     * return null; }
     */
}
