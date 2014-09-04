package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.asi.ext.api.product.criteria.processor.AdditionalColorProcessor;
import com.asi.ext.api.product.criteria.processor.AdditionalLocationProcessor;
import com.asi.ext.api.product.criteria.processor.FOBPointProcessor;
import com.asi.ext.api.product.criteria.processor.ImprintMethodProcessor;
import com.asi.ext.api.product.criteria.processor.ProductAvailabilityProcessor;
import com.asi.ext.api.product.criteria.processor.ProductCategoriesProcessor;
import com.asi.ext.api.product.criteria.processor.ProductColorProcessor;
import com.asi.ext.api.product.criteria.processor.ProductImprintColorProcessor;
import com.asi.ext.api.product.criteria.processor.ProductImprintSizeAndLocationProcessor;
import com.asi.ext.api.product.criteria.processor.ProductKeywordProcessor;
import com.asi.ext.api.product.criteria.processor.ProductMateriaProcessor;
import com.asi.ext.api.product.criteria.processor.ProductMediaItemProcessor;
import com.asi.ext.api.product.criteria.processor.ProductOptionsProcessor;
import com.asi.ext.api.product.criteria.processor.ProductOriginProcessor;
import com.asi.ext.api.product.criteria.processor.ProductPackageProcessor;
import com.asi.ext.api.product.criteria.processor.ProductSelectedComplianceCertProcessor;
import com.asi.ext.api.product.criteria.processor.ProductSelectedSafetyWarningProcessor;
import com.asi.ext.api.product.criteria.processor.ProductShapeProcessor;
import com.asi.ext.api.product.criteria.processor.ProductSizeGroupProcessor;
import com.asi.ext.api.product.criteria.processor.ProductSpecSampleProcessor;
import com.asi.ext.api.product.criteria.processor.ProductThemeProcessor;
import com.asi.ext.api.product.criteria.processor.ProductTradeNameProcessor;
import com.asi.ext.api.product.criteria.processor.ProductionTimeProcessor;
import com.asi.ext.api.product.criteria.processor.RushTimeProcessor;
import com.asi.ext.api.product.criteria.processor.SelectedLineProcessor;
import com.asi.ext.api.service.model.Image;
import com.asi.ext.api.service.model.PriceGrid;
import com.asi.ext.api.service.model.Product;
import com.asi.ext.api.service.model.ProductConfigurations;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.ProductParserUtil;
import com.asi.service.product.client.vo.ProductConfiguration;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductMediaCitations;
import com.asi.service.product.client.vo.ProductMediaItems;
import com.asi.service.product.client.vo.ProductNumber;
import com.asi.service.product.exception.InvalidProductException;

/**
 * ImportTransformer consist logic for processing each individual Product,
 * and convert each product json to actual {@linkplain com.mule.velocity.bean.Product} object.
 * All lookup and value processing are done by {@link #transformMessage(MuleMessage, String)}
 * 
 * @see AbstractMessageTransformer
 * 
 */
public class ImportTransformer {

    private ProductCriteriaSets[]                  productCriteriaSetsAry      = null;

    private PriceGridParser                        priceGridParser             = new PriceGridParser();
    private ProductDataStore                       productDataStore            = new ProductDataStore();

    private ProductSelectedSafetyWarningProcessor  safetyWarningProcessor      = new ProductSelectedSafetyWarningProcessor();
    private ProductKeywordProcessor                keywordProcessor            = new ProductKeywordProcessor();
    // private ProductCategoriesProcessor categoryProcessor = new ProductCategoriesProcessor();
    private ProductSelectedComplianceCertProcessor complianceCertProcessor     = new ProductSelectedComplianceCertProcessor();
    // Product Media related
    private ProductMediaItemProcessor              productImageProcessor       = new ProductMediaItemProcessor();
    // Product configuration related
    private ProductOriginProcessor                 originProcessor             = new ProductOriginProcessor(-101, "0");
    private ProductColorProcessor                  colorProcessor              = new ProductColorProcessor(-201, "0");
    private ProductMateriaProcessor                materialProcessor           = new ProductMateriaProcessor(-301, "0");
    private ProductShapeProcessor                  shapeProcessor              = new ProductShapeProcessor(-401, "0");
    private ProductThemeProcessor                  themeProcessor              = new ProductThemeProcessor(-501, "0");
    private ProductTradeNameProcessor              tradeNameProcessor          = new ProductTradeNameProcessor(-601, "0");
    private ProductImprintColorProcessor           imprintColorProcessor       = new ProductImprintColorProcessor(-701, "0");
    private ProductImprintSizeAndLocationProcessor imszProcessor               = new ProductImprintSizeAndLocationProcessor(-801,
                                                                                       "0");
    private ProductPackageProcessor                packagingProcessor          = new ProductPackageProcessor(-901, "0");
    private ProductionTimeProcessor                productionTimeProcessor     = new ProductionTimeProcessor(-1001, "0");
    private RushTimeProcessor                      rushTimeProcessor           = new RushTimeProcessor(-1101, "0");
    
    private AdditionalColorProcessor               additionalColorProcessor    = new AdditionalColorProcessor(-1201, "0");
    private AdditionalLocationProcessor            additionalLocationProcessor = new AdditionalLocationProcessor(-1301, "0");
    private ProductSpecSampleProcessor             specSampleProcessor         = new ProductSpecSampleProcessor(-1401, "0");
    private ProductOptionsProcessor                optionsProcessor            = new ProductOptionsProcessor(-1501, "0");
    private ImprintMethodProcessor                 imprintMethodProcessor      = new ImprintMethodProcessor();                       // 1601,
    private ProductNumberCriteriaParser            productNumberProcessor      = new ProductNumberCriteriaParser();
    private CatalogCriteriaParser                  catalogCriteriaProcessor    = new CatalogCriteriaParser();

    private FOBPointProcessor                      fobPointProcessor           = new FOBPointProcessor(1901, "0");
    private SelectedLineProcessor                  selectedLineProcessor       = new SelectedLineProcessor();
    private ProductSizeGroupProcessor              sizeProcessor               = new ProductSizeGroupProcessor("-2001");

    private ProductAvailabilityProcessor           availabilityProcessor       = new ProductAvailabilityProcessor();

    private final static Logger                    LOGGER                      = Logger.getLogger(ImportTransformer.class.getName());

    public ProductDetail generateRadarProduct(com.asi.ext.api.service.model.Product serviceProduct, ProductDetail existingRadarModel, String authToken)
            throws InvalidProductException {
        LOGGER.info("Started processing product conversion");
        long processingTime = System.currentTimeMillis(); // For evaluating application performance

        boolean isNewProduct = existingRadarModel == null;
        ProductDetail productToSave = null;

        String configId = "0";
        String productId = "0";
        String companyId = "0";
        String xid = serviceProduct.getExternalProductId();
        // list for product configurations that are already processed for RADAR API and need to figure out the brokenout feature...
        List<ProductConfiguration> productConfigurations;

        if (!isNewProduct) {
            productId = existingRadarModel.getID();
            companyId = existingRadarModel.getCompanyId();
            configId = ProductParserUtil.getConfigId(existingRadarModel.getProductConfigurations());
            productToSave = existingRadarModel;
        } else {
            productToSave = new ProductDetail();
            productToSave.setID(productId);
            productToSave.setCompanyId(companyId);
            productToSave.setExternalProductId(serviceProduct.getExternalProductId());
            // Issue: VELOEXTAPI-230
            // these fields are set for new product case on temporary basis
            // as of now RADAR API doesn't include this default implementation
            // so we have to provide it.
            productToSave.setProductLockedFlag("false");
            productToSave.setChangeProductReasonCode("NONE");
            productToSave.setShow1MediaIdIm(0);
            productToSave.setShow1MediaIdVd(0);
            productToSave.setLocationCode("WIPD");
            productToSave.setSameDayRushFlag("U");
            productToSave.setRushServiceFlag("U");
            productToSave.setWorkflowStatusCode("INPR");
            productToSave.setWorkflowStatusStateCode("INCP");
            // LMIN Process
            // productToSave.setIsOrderLessThanMinimumAllowed()
        }
        productToSave.setIsShippableInPlainBox(serviceProduct.isCanShipInPlainBox());
        // DataSourceId
        // productToSave.setDataSourceId(dataSourceId);
        // Direct Elements
        productToSave.setName(serviceProduct.getName());
        productToSave.setAsiProdNo(serviceProduct.getAsiProdNo());
        productToSave.setDescription(serviceProduct.getDescription());
        productToSave.setSummary(serviceProduct.getSummary());

        productToSave.setShipperBillsByCode(ProductParserUtil.getShippersBillsBy(serviceProduct.getShipperBillsBy()));
        if (productToSave.getShipperBillsByCode() == null) {
            productToSave.setShipperBillsByCode("");
            productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                    "Invalid value provided for ShippersBillsBy : " + serviceProduct.getShipperBillsBy());
        }

        productToSave.setDisclaimer(serviceProduct.getProductDisclaimer());
        productToSave.setAdditionalInfo(serviceProduct.getAdditionalProductInfo());
        productToSave.setDistributorComments(serviceProduct.getDistributorOnlyComments());
        productToSave.setAdditionalShippingInfo(serviceProduct.getAdditionalShippingInfo());
        productToSave.setSameDayRushFlag("true");

        // Object Type Elements
        productToSave.setProductInventoryLink(ProductParserUtil.getInventoryLink(serviceProduct.getProductInventoryLink(),
                existingRadarModel, companyId));
        productToSave.setProductDataSheet(ProductParserUtil.getDataSheet(serviceProduct.getProductDataSheet(), existingRadarModel,
                companyId));

        // Check for required validations of Product
        if (validateProductRequirement(serviceProduct, existingRadarModel)) {
            LOGGER.info("Product is valid for save");
        }

        // Basic Collections
        // Process Keyword
        productToSave.setProductKeywords(keywordProcessor.getProductKeywords(serviceProduct.getProductKeywords(),
                existingRadarModel, true));

        productToSave.setSelectedProductCategories(new ProductCategoriesProcessor().getCategories(serviceProduct.getCategories(),
                productId, xid, existingRadarModel));

        // Safety Warning Start
        productToSave.setSelectedSafetyWarnings(safetyWarningProcessor.getSafetyWarnings(serviceProduct.getSafetyWarnings(), xid,
                productId, existingRadarModel));

        // Compliance Cert Processing
        productToSave.setSelectedComplianceCerts(complianceCertProcessor.getSelectedComplianceCertList(
                serviceProduct.getComplianceCerts(), companyId, productId, existingRadarModel));

        Map<String, ProductCriteriaSets> existingCriteriaSetMap = new HashMap<>();
        Map<String, List<ProductCriteriaSets>> optionsCriteriaSet = new HashMap<>();
        // Get all existing criteria set from productConfiguartions
        if (!isNewProduct) {
            existingCriteriaSetMap = ProductCompareUtil.getExistingProductCriteriaSets(productToSave.getProductConfigurations(),
                    true);
            optionsCriteriaSet = ProductCompareUtil.getOptionCriteriaSets(productToSave.getProductConfigurations());
        }

        // Imprint, Artwork, Minimum Quantity processing
        ImprintRelationData imprintRelationData = null;
        if (serviceProduct != null && serviceProduct.getProductConfigurations().getImprintMethods() != null
                && !serviceProduct.getProductConfigurations().getImprintMethods().isEmpty()) {
            imprintRelationData = imprintMethodProcessor.getImprintCriteriaSet(serviceProduct.getProductConfigurations()
                    .getImprintMethods(), productToSave, existingCriteriaSetMap, configId);

            existingCriteriaSetMap = imprintRelationData.getExistingCriteriaSetMap();
            productToSave.setRelationships(imprintRelationData.getRelationships());
        } else {
            existingCriteriaSetMap.put(ApplicationConstants.CONST_IMPRINT_METHOD_CODE, null);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_ARTWORK_CODE, null);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_MINIMUM_QUANTITY, null);
        }

        // Product, Imprint, Shipping Options processing
        optionsCriteriaSet = processProductOptions(optionsCriteriaSet, productToSave, serviceProduct.getProductConfigurations(),
                configId);

        // FOB points processing
        if (serviceProduct.getFobPoints() != null && !serviceProduct.getFobPoints().isEmpty()) {
            ProductCriteriaSets tempCriteriaSet = fobPointProcessor.getFOBPCriteriaSet(serviceProduct.getFobPoints(),
                    productToSave, existingCriteriaSetMap.get(ApplicationConstants.CONST_CRITERIA_CODE_FOBP), configId, authToken);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_CRITERIA_CODE_FOBP, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_CRITERIA_CODE_FOBP);
        }

        // Selected Line name processing
        if (serviceProduct.getLineNames() != null && !serviceProduct.getLineNames().isEmpty()) {
            productToSave
                    .setSelectedLineNames(selectedLineProcessor.getSelectedLines(serviceProduct.getLineNames(), productToSave, authToken));
        } else {
            productToSave.setSelectedLineNames(null);
        }
        // Process Product Configurations

        productToSave.setProductConfigurations(processProductConfigurations(configId, existingCriteriaSetMap, optionsCriteriaSet,
                serviceProduct.getProductConfigurations(), productToSave, isNewProduct));

        // Product Media Item processing
        productToSave.setProductMediaItems(getProductMediaItems(companyId, productId, serviceProduct.getImages(),
                productToSave.getProductMediaItems(), serviceProduct.getExternalProductId()));

        // Process Breakout Configurations...
        if (!StringUtils.isEmpty(serviceProduct.getProductBreakoutBy())) {

            if (serviceProduct.getProductBreakoutBy().equalsIgnoreCase("Product Number")) {
                productToSave.setIsProductNumberBreakout(true);
                productConfigurations = productToSave.getProductConfigurations();
                if (productConfigurations != null && !productConfigurations.isEmpty()) {
                    for (ProductConfiguration configuration : productConfigurations) {
                        for (ProductCriteriaSets criteriaSet : configuration.getProductCriteriaSets()) {
                            criteriaSet.setIsBrokenOutOn("false");
                        }
                    }
                }
            } else {

                String criteriaCode = ProductParserUtil.getCriteriaCodeFromCriteria(serviceProduct.getProductBreakoutBy(),
                        productToSave.getExternalProductId());
                if (!StringUtils.isEmpty(criteriaCode)) {
                    productToSave.setIsProductNumberBreakout(false);
                    productConfigurations = productToSave.getProductConfigurations();
                    if (productConfigurations != null && !productConfigurations.isEmpty()) {
                        for (ProductConfiguration configuration : productConfigurations) {
                            for (ProductCriteriaSets criteriaSet : configuration.getProductCriteriaSets()) {
                                if (criteriaSet.getCriteriaCode().equalsIgnoreCase(criteriaCode)) {
                                    criteriaSet.setIsBrokenOutOn("true");
                                } else {
                                    criteriaSet.setIsBrokenOutOn("false");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            productConfigurations = productToSave.getProductConfigurations();
            if (productConfigurations != null && !productConfigurations.isEmpty()) {
                for (ProductConfiguration configuration : productConfigurations) {
                    for (ProductCriteriaSets criteriaSet : configuration.getProductCriteriaSets()) {
                        criteriaSet.setIsBrokenOutOn("false");
                    }
                }
            }
            productToSave.setIsProductNumberBreakout(false);
        }

        if (serviceProduct != null) {
            productToSave.setIsPriceBreakoutFlag(serviceProduct.isBreakOutByPrice());
        }

        // PriceGrid processing
        productToSave.setPriceGrids(getPriceGrids(serviceProduct.getPriceGrids(), productToSave));
        // Product Number Processing
        if (!isProductNumberAssociatedWithPriceGrid(xid)) {
            productToSave.setProductNumbers(getProductNumbers(serviceProduct, productToSave));
        }

        // Product Catalogs
        productToSave.setProductMediaCitations(getProductMediaCitations(serviceProduct, productToSave, authToken));

        // Product Availability processing
        productToSave.setRelationships(availabilityProcessor.getProductAvailabilities(productToSave,
                serviceProduct.getAvailability(), existingCriteriaSetMap, optionsCriteriaSet));
        LOGGER.info("Completed convertion of service product to Radar model, time taken for convertion : "
                + (System.currentTimeMillis() - processingTime) + " ms");
        // Return product model
        return productToSave;
    }

    private List<ProductMediaItems> getProductMediaItems(String companyId, String productId, List<Image> serviceImages,
            List<ProductMediaItems> existingMediaItems, String externalProductId) {
        return productImageProcessor.getProductImages(serviceImages, companyId, productId, existingMediaItems, externalProductId);
    }

    private List<ProductConfiguration> processProductConfigurations(String configId,
            Map<String, ProductCriteriaSets> existingCriteriaSetMap, Map<String, List<ProductCriteriaSets>> optionsCriteriaSet,
            com.asi.ext.api.service.model.ProductConfigurations serviceProdConfigs, ProductDetail rdrProduct, boolean isNewProduct) {
        List<ProductConfiguration> updatedProductConfigurationList = new ArrayList<>();

        if (serviceProdConfigs == null) {
            return new ArrayList<ProductConfiguration>();
        }

        ProductCriteriaSets tempCriteriaSet = null;

        // Product Origin processing

        if (serviceProdConfigs.getOrigins() != null && !serviceProdConfigs.getOrigins().isEmpty()) {
            tempCriteriaSet = originProcessor.getOriginCriteriaSet(serviceProdConfigs.getOrigins(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE);
        }

        // Product Package Processing
        if (serviceProdConfigs.getPackaging() != null && !serviceProdConfigs.getPackaging().isEmpty()) {
            tempCriteriaSet = packagingProcessor.getPackageCriteriaSet(serviceProdConfigs.getPackaging(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE);
        }

        // Product Shape Processing
        if (serviceProdConfigs.getShapes() != null && !serviceProdConfigs.getShapes().isEmpty()) {
            tempCriteriaSet = shapeProcessor.getShapeCriteriaSet(serviceProdConfigs.getShapes(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE);
        }

        // Product Theme Processing
        if (serviceProdConfigs.getThemes() != null && !serviceProdConfigs.getThemes().isEmpty()) {
            tempCriteriaSet = themeProcessor.getThemeCriteriaSet(serviceProdConfigs.getThemes(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_THEME_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_THEME_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_THEME_CRITERIA_CODE);
        }

        // Product TradeName Processing
        if (serviceProdConfigs.getTradeNames() != null && !serviceProdConfigs.getTradeNames().isEmpty()) {
            tempCriteriaSet = tradeNameProcessor.getTradenames(serviceProdConfigs.getTradeNames(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_TRADE_NAME_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_TRADE_NAME_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_TRADE_NAME_CODE);
        }

        // Imprint Color Processing
        if (serviceProdConfigs.getImprintColors() != null) {
            tempCriteriaSet = imprintColorProcessor.getImprintColorCriteriaSet(serviceProdConfigs.getImprintColors(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE);
        }

        // Product Additional Color Processing
        if (serviceProdConfigs.getAdditionalColors() != null && !serviceProdConfigs.getAdditionalColors().isEmpty()) {
            tempCriteriaSet = additionalColorProcessor.getAdditionalColorCriteriaSet(serviceProdConfigs.getAdditionalColors(),
                    rdrProduct, existingCriteriaSetMap.get(ApplicationConstants.CONST_ADDITIONAL_COLOR), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_ADDITIONAL_COLOR, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_ADDITIONAL_COLOR);
        }

        // Product Additional Location Processing
        if (serviceProdConfigs.getAdditionalLocations() != null && !serviceProdConfigs.getAdditionalLocations().isEmpty()) {
            tempCriteriaSet = additionalLocationProcessor.getAdditionalLocationCriteriaSet(
                    serviceProdConfigs.getAdditionalLocations(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_ADDITIONAL_LOCATION), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_ADDITIONAL_LOCATION, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_ADDITIONAL_LOCATION);
        }

        // Product Color Processing
        if (serviceProdConfigs.getColors() != null && !serviceProdConfigs.getColors().isEmpty()) {
            tempCriteriaSet = colorProcessor.getProductColorCriteriaSet(serviceProdConfigs.getColors(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_COLORS_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_COLORS_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_COLORS_CRITERIA_CODE);
        }

        // Product Material Processing
        if (serviceProdConfigs.getMaterials() != null && !serviceProdConfigs.getMaterials().isEmpty()) {
            tempCriteriaSet = materialProcessor.getProductMaterialCriteriaSet(serviceProdConfigs.getMaterials(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE);
        }

        // Imprint Size and Location Processing
        if (serviceProdConfigs.getImprintSizeLocations() != null && !serviceProdConfigs.getImprintSizeLocations().isEmpty()) {
            tempCriteriaSet = imszProcessor.getProductImprintSizeAndLocationCriteriaSet(
                    serviceProdConfigs.getImprintSizeLocations(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_IMPRINT_SIZE_CRITERIA_CODE);
        }

        // Product Samples and Spec Sample Processing
        if (serviceProdConfigs.getSamples() != null && !serviceProdConfigs.getSamples().isNull()) {
            tempCriteriaSet = specSampleProcessor.getProductSamplesCriteriaSet(serviceProdConfigs.getSamples(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE);
        }

        // RUSH Time Processing

        if (serviceProdConfigs.getRushTime() != null && serviceProdConfigs.getRushTime().isAvailable()) {
            tempCriteriaSet = rushTimeProcessor.getRushTimeCriteriaSet(serviceProdConfigs.getRushTime(), rdrProduct,
                    existingCriteriaSetMap.get(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE);
        }
        
        // Same Day Rush Time Processing..
        if (serviceProdConfigs.getSameDayRush() != null && serviceProdConfigs.getSameDayRush().isAvailable()) {
//        	tempCriteriaSet = sameDayServiceProcessor.getCriteriaSetForSameDayService(null, , serviceProdConfigs.getSameDayRush().getDetails(), configId);
        			
            tempCriteriaSet = rushTimeProcessor.getSameDayRushTimeCriteriaSet(serviceProdConfigs.getSameDayRush(), rdrProduct, existingCriteriaSetMap.get(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
        }
        

        // Product Time Processing
        if (serviceProdConfigs.getProductionTime() != null) {
            tempCriteriaSet = productionTimeProcessor.getProductionTimeCriteriaSet(serviceProdConfigs.getProductionTime(),
                    rdrProduct, existingCriteriaSetMap.get(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE), configId);
            existingCriteriaSetMap.put(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE, tempCriteriaSet);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE);
        }

        // Product Size processing
        if (!sizeProcessor.isSizeNull(serviceProdConfigs.getSizes())) {
            existingCriteriaSetMap = sizeProcessor.getProductCriteriaSet(serviceProdConfigs.getSizes(), rdrProduct,
                    existingCriteriaSetMap, configId);
        } else {
            existingCriteriaSetMap = sizeProcessor.removeSizeRelatedCriteriaSetFromExisting(existingCriteriaSetMap);
        }
        // Product Size processing
        if (serviceProdConfigs.getShippingEstimates() != null) {
            existingCriteriaSetMap = sizeProcessor.processShippingItem(rdrProduct, existingCriteriaSetMap, configId,
                    serviceProdConfigs.getShippingEstimates(), -2501);
        } else {
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);
            existingCriteriaSetMap.remove(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
        }

        // Merge all updated ProductCriteriaSets into product configuration and set back to list
        ProductConfiguration updatedProductConfiguration = new ProductConfiguration();
        updatedProductConfiguration.setConfigId(configId);
        updatedProductConfiguration.setID(configId);
        updatedProductConfiguration.setIsDefault(true);
        updatedProductConfiguration.setProductId(rdrProduct.getID());
        updatedProductConfiguration.setProductCriteriaSets(ProductCompareUtil.getFinalProductCriteriaSets(existingCriteriaSetMap,
                optionsCriteriaSet, productCriteriaSetsAry));

        updatedProductConfigurationList.add(updatedProductConfiguration);

        return updatedProductConfigurationList;
    }

    private Map<String, List<ProductCriteriaSets>> processProductOptions(Map<String, List<ProductCriteriaSets>> optionsCriteriaSet,
            ProductDetail rdrProduct, ProductConfigurations serviceProdConfigs, String configId) {
        // Product options processing
        if (serviceProdConfigs != null && serviceProdConfigs.getOptions() != null && !serviceProdConfigs.getOptions().isEmpty()) {
            optionsCriteriaSet = optionsProcessor.getOptionCriteriaSets(serviceProdConfigs.getOptions(), rdrProduct, configId,
                    optionsCriteriaSet);
        } else {
            optionsCriteriaSet = null;
        }

        return optionsCriteriaSet;
    }

    private List<ProductNumber> getProductNumbers(Product serviceProduct, ProductDetail product) {
        if (serviceProduct.getProductNumbers() != null && !serviceProduct.getProductNumbers().isEmpty()) {
            return productNumberProcessor.generateProductNumbers(serviceProduct.getProductNumbers(), product);
        } else {
            return new ArrayList<ProductNumber>();
        }
    }

    private List<ProductMediaCitations> getProductMediaCitations(Product serviceProduct, ProductDetail product, String authToken) {
        if (serviceProduct.getCatalogs() != null && !serviceProduct.getCatalogs().isEmpty()) {
            return catalogCriteriaProcessor.prepareProductCatalog(serviceProduct.getCatalogs(), product, authToken);
        } else {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unused")
    private void processImprintMethodRelations() {

    }

    private boolean validateProductRequirement(Product serProduct, ProductDetail extProduct) throws InvalidProductException {
        if (extProduct != null && extProduct.getWorkflowStatusCode() != null
                && extProduct.getWorkflowStatusCode().equalsIgnoreCase(ApplicationConstants.CONST_PROD_UNDER_REVIEW)) {
            productDataStore.addErrorToBatchLogCollection(serProduct.getExternalProductId(),
                    ApplicationConstants.CONST_BATCH_ERR_GENERIC_ERROR, "Product is under review by ASI and cannot accept changes");
            throw new InvalidProductException(serProduct.getExternalProductId(), "Product cannot be saved, validation failed");
        }

        if (!isProductHasValidProductNumber(serProduct)) {
            throw new InvalidProductException(serProduct.getExternalProductId(), "Product cannot be saved, validation failed");
        }

        return true;
    }

    public boolean isProductHasValidProductNumber(Product serProduct) {
        boolean pnoFound = false;
        if (serProduct.getPriceGrids() != null && !serProduct.getPriceGrids().isEmpty()) {
            for (PriceGrid pg : serProduct.getPriceGrids()) {
                if (pg != null && !CommonUtilities.isValueNull(pg.getProductNumber())) {
                    pnoFound = true;
                    ProductDataStore.productNumberAssociation.put(serProduct.getExternalProductId(), "PRCG");
                    break;
                }
            }
        }
        if (serProduct.getProductNumbers() != null && !serProduct.getProductNumbers().isEmpty()) {
            if (pnoFound) { // Already configured in PriceGrid Level
                productDataStore.addErrorToBatchLogCollection(serProduct.getExternalProductId(),
                        ApplicationConstants.CONST_BATCH_ERR_GENERIC_ERROR,
                        "ProductNumber is configured in PriceGrid and Criteria Level");

                return false;
            } else {
                ProductDataStore.productNumberAssociation.put(serProduct.getExternalProductId(), "PNOCRT");
                return true;
            }
        }
        return true;
    }

    private List<com.asi.service.product.client.vo.PriceGrid> getPriceGrids(List<PriceGrid> priceGrids, ProductDetail product) {
        if (priceGrids != null && !priceGrids.isEmpty()) {
            return priceGridParser.getPriceGrids(priceGrids, product);
        } else {
            return new ArrayList<>();
        }

    }

    private boolean isProductNumberAssociatedWithPriceGrid(String xid) {
        if (ProductDataStore.productNumberAssociation.get(xid) != null) {
            return ProductDataStore.productNumberAssociation.get(xid).equalsIgnoreCase("PRCG");
        } else {
            return false;
        }
    }

    /*
     * private boolean isProductNumberAssociatedWithCriteria(String xid) {
     * if (ProductDataStore.productNumberAssociation.get(xid) != null) {
     * return ProductDataStore.productNumberAssociation.get(xid).equalsIgnoreCase("PNOCRT");
     * } else {
     * return false;
     * }
     * }
     */

    /*
     * public Object transformMessage(Object muleMessage, String arg1) {
     * 
     * boolean isSimplifiedTemplate = false;
     * // CommonUtilities.isSimplifiedTemplate(String.valueOf(muleMessage.getProperty("TemplateType",
     * // PropertyScope.SESSION)));
     * boolean isNewProduct = true;
     * 
     * ProductDataStore productDataStore = new ProductDataStore();
     * ProductSizeGroupProcessor productSizeGroupProcessor = null;
     * 
     * String batchErrorLogs = "";// muleMessage.getProperty("batchErrorLog", PropertyScope.SESSION);
     * 
     * Product product = null;
     * try {
     * Map<String, ProductCriteriaSets> existingCriteriaSetMap = new HashMap<>();
     * Map<String, List<ProductCriteriaSets>> optionsCriteriaSet = new HashMap<>();
     * 
     * String configId = "0";
     * String productId = "0";
     * String companyId = "0";
     * 
     * String currentMsg = "";// muleMessage.getPayloadAsString();
     * // Convert JSON String Product Model
     * ObjectMapper mapper = new ObjectMapper();
     * mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
     * mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
     * // mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
     * 
     * product = mapper.readValue(currentMsg, Product.class);
     * // Updating Product
     * if (null != product.getExternalProductId() && !product.getExternalProductId().equals("") && null != product.getName()
     * && !product.getName().equals("") && !CommonUtilities.isValueNull(product.getDescription())) {
     * // Register current Product for Batch Error Logging and Reference tables
     * productDataStore.registerProductForBatch(product.getExternalProductId());
     * 
     * if (null == product.getDescription()) {
     * LOGGER.info(product.getName() + " Product Description Should not be Null ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product Description is a required field";
     * } else if (product.getDescription().equals("")) {
     * LOGGER.info(product.getName() + " Product Description Should not be Empty ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product Description is a required field";
     * }
     * 
     * LOGGER.info(product.getName() + " is satisfied minimum requirements");
     * 
     * Product existingProduct = jerseyClient.getExistingProductDetails(product.getExternalProductId(),
     * product.getCompanyId());
     * 
     * if (null != existingProduct) {
     * existingProduct.setDataSourceId(product.getDataSourceId());
     * try {
     * LOGGER.info(product.getName() + " Product Exist with its Id :" + existingProduct.getId());
     * productId = existingProduct.getId();
     * companyId = existingProduct.getCompanyId();
     * product.setId(productId);
     * 
     * isNewProduct = false;
     * 
     * // Creating existing Criteria Map
     * if (existingProduct.getProductConfigurations() != null
     * && existingProduct.getProductConfigurations().length > 0) {
     * configId = existingProduct.getProductConfigurations()[0].getId();
     * if (existingProduct.getProductConfigurations()[0].getProductCriteriaSets() != null) {
     * 
     * existingCriteriaSetMap = ProductCompareUtil.getExistingProductCriteriaSets(
     * existingProduct.getProductConfigurations()[0].getProductCriteriaSets(), true);
     * 
     * optionsCriteriaSet = ProductCompareUtil.getOptionCriteriaSets(existingProduct
     * .getProductConfigurations()[0].getProductCriteriaSets());
     * 
     * }
     * }
     * existingProduct.setName(CommonUtilities.checkAndUpdate(product.getName(), existingProduct.getName()));
     * existingProduct.setDescription(CommonUtilities.checkAndUpdate(product.getDescription(),
     * existingProduct.getDescription()));
     * existingProduct.setSummary(CommonUtilities.checkAndUpdate(product.getSummary(),
     * existingProduct.getSummary()));
     * existingProduct.setDisclaimer(CommonUtilities.checkAndUpdate(product.getDisclaimer(),
     * existingProduct.getDisclaimer()));
     * existingProduct.setDistributorComments(CommonUtilities.checkAndUpdate(product.getDistributorComments(),
     * existingProduct.getDistributorComments()));
     * existingProduct.setAsiProdNo(CommonUtilities.checkAndUpdate(product.getAsiProdNo(),
     * existingProduct.getAsiProdNo()));
     * existingProduct.setAdditionalInfo(CommonUtilities.checkAndUpdate(product.getAdditionalInfo(),
     * existingProduct.getAdditionalInfo()));
     * 
     * existingProduct.setDataSourceId(product.getDataSourceId());
     * 
     * 
     * product.setIsOrderLessThanMinimumAllowed(commonUtils.parseBooleanValue(product.
     * getIsOrderLessThanMinimumAllowed()));
     * product.setIsAvailableUnimprinted(commonUtils.parseBooleanValue(product.getIsAvailableUnimprinted()));
     * product.setIsPersonalizationAvailable(commonUtils.parseBooleanValue(product.getIsPersonalizationAvailable(
     * )));
     * // TODO : Correct Prod Num
     * product.setAsiProdNo(commonUtils.truncate(product.getAsiProdNo(), 14));
     * 
     * 
     * // TODO : Check about the need of these lines
     * product.getPriceGrids()[0].setProductId(productId);
     * product.getProductConfigurations()[0].setProductId(productId);
     * product.getProductConfigurations()[0].getProductCriteriaSets()[0].setProductId(existingProduct.getId());
     * 
     * } catch (Exception e) {
     * LOGGER.error("Found some problem while getting data from existing product, message " + e.getMessage());
     * }
     * } else {
     * if (!CommonUtilities.isValidForNewProduct(product.getName())) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
     * ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD, "Product name is required field");
     * product.setName("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getDescription())) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
     * ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD, "Product Description cannot be empty");
     * product.setDescription("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getSummary())) {
     * product.setSummary("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getDisclaimer())) {
     * product.setDisclaimer("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getDistributorComments())) {
     * product.setDistributorComments("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getAsiProdNo())) {
     * product.setAsiProdNo("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getAdditionalInfo())) {
     * product.setAdditionalInfo("");
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getExcludeAppOfferList())) {
     * product.setExcludeAppOfferList("");
     * }
     * 
     * }
     * 
     * if (null != existingProduct && existingProduct.getId().equals("0")) {
     * LOGGER.error("Got Status Code 500 Exception ");
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
     * ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR,
     * "Internal server exception while fetching existing product info");
     * // muleMessage.setPayload(null);
     * } else {
     * 
     * String currentOrigin = "";
     * String productColor = "";
     * String productMaterial = "";
     * String productShape = "";
     * String productThemes = "";
     * String imprintMethods = "";
     * String imprintArtwork = "";
     * String imprintColor = "";
     * String imprintSize = "";
     * String sizeGroups = null;
     * String srcCriteriaSetCodeValue = "";
     * String packaging = "";
     * String productionTime = "";
     * String rushTime = "";
     * String shippingItems = "";
     * String optionType = "";
     * String optionValues = "";
     * String additionalColor = "";
     * String additionalLocation = "";
     * String productSample = "";
     * String specSample = "";
     * String shippingDimensions = "";
     * String shippingWeight = "";
     * String sizeValues = null;
     * String tradeNames = null;
     * String minQuantity = "";
     * 
     * // Option Names
     * String optionNames = product.getProductConfigurations()[0].getProductCriteriaSets()[0].getDescription();
     * String reqForOrder = product.getProductConfigurations()[0].getProductCriteriaSets()[0].getIsRequiredForOrder();
     * product.getProductConfigurations()[0].getProductCriteriaSets()[0].setIsRequiredForOrder("false");
     * String isMultipleChoiceAllowed = product.getProductConfigurations()[0].getProductCriteriaSets()[0]
     * .getIsMultipleChoiceAllowed();
     * 
     * product.getProductConfigurations()[0].getProductCriteriaSets()[0].setIsMultipleChoiceAllowed("false");
     * 
     * product.getProductConfigurations()[0].getProductCriteriaSets()[0].setDescription("");
     * srcCriteriaSetCodeValue = product.getProductConfigurations()[0].getProductCriteriaSets()[0]
     * .getCriteriaSetValues()[0].getCriteriaSetCodeValues()[0].getSetCodeValueId();
     * 
     * String[] criteriaCodesAry = srcCriteriaSetCodeValue.split(ApplicationConstants.PRODUCT_CRT_SPLITTER_CODE);
     * 
     * if (product.getDescription().length() > ApplicationConstants.PRD_DESCRIPTION_MAX_LENGTH) {
     * product.setDescription(product.getDescription().trim()
     * .substring(0, ApplicationConstants.PRD_DESCRIPTION_MAX_LENGTH));
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Product Description exceeded the max length "
     * + ApplicationConstants.PRD_DESCRIPTION_MAX_LENGTH);
     * }
     * 
     * // Confirmed_Thru_Date processing
     * if (!CommonUtilities.isValueNull(product.getPriceConfirmationDate())) {
     * if (CommonUtilities.isUpdateNeeded(product.getPriceConfirmationDate(), true)) {
     * try {
     * product.setPriceConfirmationDate(CommonUtilities.formatDateValue(product.getPriceConfirmationDate()));
     * } catch (Exception e) {
     * LOGGER.warn("Invalid date format found for Confirmed_Thru_Date "
     * + product.getPriceConfirmationDate());
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid date format found for Confirmed_Thru_Date " + product.getPriceConfirmationDate());
     * product.setPriceConfirmationDate(existingProduct != null ? existingProduct
     * .getPriceConfirmationDate() : "");
     * }
     * } else if (existingProduct != null) {
     * product.setPriceConfirmationDate(existingProduct.getPriceConfirmationDate());
     * } else {
     * product.setPriceConfirmationDate(null);
     * }
     * } else {
     * product.setPriceConfirmationDate(null);
     * }
     * 
     * if (!isNewProduct) {
     * existingProduct.setPriceConfirmationDate(product.getPriceConfirmationDate());
     * }
     * // DONT_MAKE_ACTIVE Processing
     * if (!CommonUtilities.isValueNull(product.getStatusCode())) {
     * if (CommonUtilities.isUpdateNeeded(product.getStatusCode())
     * && CommonUtilities.getBooleanValueFromYesOrNo(product.getStatusCode())) {
     * // muleMessage.setProperty("publishNeeded", false, PropertyScope.SESSION);
     * product.setStatusCode("");
     * } else {
     * product.setStatusCode("");
     * // muleMessage.setProperty("publishNeeded", true, PropertyScope.SESSION);
     * }
     * product.setStatusCode("");
     * } else {
     * product.setStatusCode("");
     * // muleMessage.setProperty("publishNeeded", true, PropertyScope.SESSION);
     * }
     * 
     * criteriasCount = criteriaCodesAry.length;
     * productCriteriaSetsAry = new ProductCriteriaSets[criteriasCount * 10];
     * int cntr = 0;
     * productSizeGroupProcessor = new ProductSizeGroupProcessor(companyId, productId, configId);
     * for (int i = 0; i < criteriasCount; i++) {
     * if (i == 0) {
     * currentOrigin = criteriaCodesAry[i];
     * 
     * ProductOriginProcessor originProcessor = new ProductOriginProcessor(-101, configId);
     * if (CommonUtilities.isUpdateNeeded(currentOrigin)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductCriteriaSets originCriteriaSet = originProcessor.getCriteriaSet(currentOrigin,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE, originCriteriaSet);
     * } else {
     * originProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ORIGIN_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * 
     * } else if (i == 1) {
     * productColor = criteriaCodesAry[i];
     * 
     * ProductColorProcessor colorProcessor = new ProductColorProcessor(-201, configId);
     * if (CommonUtilities.isUpdateNeeded(productColor)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductCriteriaSets colorCriteriaSet = colorProcessor.getCriteriaSet(productColor,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_COLORS_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_COLORS_CRITERIA_CODE, colorCriteriaSet);
     * } else {
     * colorProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_COLORS_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 2) {
     * productMaterial = criteriaCodesAry[i];
     * 
     * ProductMateriaProcessor materialProcessor = new ProductMateriaProcessor(-301, configId);
     * if (CommonUtilities.isUpdateNeeded(productMaterial)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductCriteriaSets materialCriteriaSet = materialProcessor.getCriteriaSet(productMaterial,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE, materialCriteriaSet);
     * } else {
     * materialProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 3) {
     * productShape = criteriaCodesAry[i];
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductShapeProcessor shapeProcessor = new ProductShapeProcessor(-401, configId);
     * if (CommonUtilities.isUpdateNeeded(productShape)) {
     * ProductCriteriaSets shapeCriteriaSet = shapeProcessor.getCriteriaSet(productShape,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE, shapeCriteriaSet);
     * } else {
     * shapeProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_SHAPE_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 4) {
     * productThemes = criteriaCodesAry[i];
     * 
     * if (CommonUtilities.isUpdateNeeded(productThemes)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductThemeProcessor themeProcessor = new ProductThemeProcessor(-501, configId);
     * ProductCriteriaSets themeCriteriaSet = themeProcessor.getCriteriaSet(productThemes,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_THEME_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_THEME_CRITERIA_CODE, themeCriteriaSet);
     * }
     * 
     * } else if (i == 5) {
     * // Sizes - Dimensions (DIMS)
     * sizeGroups = CommonUtilities.checkAndFixCSVValues(criteriaCodesAry[i]);
     * } else if (i == 6) {
     * sizeValues = CommonUtilities.checkAndFixCSVValues(criteriaCodesAry[i]);
     * } else if (i == 7) {
     * imprintMethods = criteriaCodesAry[i];
     * } else if (i == 8) {
     * imprintArtwork = CommonUtilities.checkAndFixCSVValues(criteriaCodesAry[i]);
     * } else if (i == 9) {
     * tradeNames = criteriaCodesAry[i];
     * ProductTradeNameProcessor tradeNameProcessor = new ProductTradeNameProcessor(-601, configId);
     * if (CommonUtilities.isUpdateNeeded(tradeNames)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductCriteriaSets tradeNameCriteriaSet = tradeNameProcessor.getCriteriaSet(tradeNames,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_TRADE_NAME_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_TRADE_NAME_CODE, tradeNameCriteriaSet);
     * } else {
     * tradeNameProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_TRADE_NAME_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 10) {
     * imprintColor = criteriaCodesAry[i];
     * ProductImprintColorProcessor imprintColorProcessor = new ProductImprintColorProcessor(-701, configId);
     * if (CommonUtilities.isUpdateNeeded(imprintColor)) {
     * 
     * ProductCriteriaSets imprintCriteriaSet = imprintColorProcessor.getCriteriaSet(imprintColor,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE,
     * imprintCriteriaSet);
     * imprintColorProcessor = null; // Making object eligible for GC
     * } else {
     * imprintColorProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_COLOR_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 11) {
     * imprintSize = criteriaCodesAry[i];
     * new ProductImprintSizeAndLocationProcessor(-801, configId); // TODO : Complete Imprint Size and location
     * if (CommonUtilities.isUpdateNeeded(imprintSize) && !imprintSize.trim().equals("")
     * && !imprintSize.equalsIgnoreCase("null|null")) {
     * LOGGER.info("Imprint Size(s) Transformation Start :" + imprintSize);
     * productCriteriaSets = productParser.addCriteriaSetForCriteriaCode(existingProduct, product, "IMSZ",
     * imprintSize, emptyDescription);
     * if (null != productCriteriaSets && cntr < productCriteriaSetsAry.length) {
     * productCriteriaSets.setCompanyId(product.getCompanyId());
     * productCriteriaSetsAry[cntr] = productCriteriaSets;
     * cntr++;
     * } else {
     * productCriteriaSetsAry = Arrays.copyOf(productCriteriaSetsAry,
     * productCriteriaSetsAry.length - 1);
     * }
     * LOGGER.info("Imprint Size(s) Transformation End");
     * }
     * } else if (i == 12) {
     * packaging = criteriaCodesAry[i];
     * 
     * ProductPackageProcessor packagingProcessor = new ProductPackageProcessor(-901, configId);
     * if (CommonUtilities.isUpdateNeeded(packaging)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductCriteriaSets packagingCriteriaSet = packagingProcessor.getCriteriaSet(packaging,
     * !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE, packagingCriteriaSet);
     * } else {
     * packagingProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_PACKAGE_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 13) {
     * productionTime = criteriaCodesAry[i];
     * ProductionTimeProcessor productionTimeProcessor = new ProductionTimeProcessor(-1001, configId);
     * if (CommonUtilities.isUpdateNeeded(productionTime)) {
     * ProductCriteriaSets productionTimeCriteriaSet = productionTimeProcessor.getCriteriaSet(
     * productionTime, !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE,
     * productionTimeCriteriaSet);
     * } else {
     * productionTimeProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_PRODUCTION_TIME_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * } else if (i == 14) {
     * rushTime = criteriaCodesAry[i];
     * RushTimeProcessor rushTimeProcessor = new RushTimeProcessor(-1101, configId);
     * ProductCriteriaSets rushTimeCriteriaSet = rushTimeProcessor.getCriteriaSetForRushService(
     * product.getRushServiceFlag(), !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE, rushTimeCriteriaSet);
     * if (CommonUtilities.isUpdateNeeded(rushTime)) {
     * LOGGER.info("Rush Time Transformation Starts :" + rushTime);
     * 
     * rushTimeCriteriaSet = rushTimeProcessor.getCriteriaSet(rushTime, !isNewProduct ? existingProduct
     * : product, existingCriteriaSetMap.get(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE),
     * 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE, rushTimeCriteriaSet);
     * } else {
     * rushTimeProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_RUSH_TIME_CRITERIA_CODE),
     * product.getExternalProductId());
     * }
     * LOGGER.info("Rush Time Transformation Ends");
     * 
     * } else if (i == 15) {
     * shippingItems = criteriaCodesAry[i];
     * if (CommonUtilities.isValueNull(shippingItems)) {
     * 
     * existingCriteriaSetMap.remove(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
     * 
     * } else if (CommonUtilities.isUpdateNeeded(shippingItems, true) && !shippingItems.trim().equals("")
     * && !shippingItems.equalsIgnoreCase("null")) {
     * LOGGER.info("Shipping Items Transformation Starts :" + shippingItems);
     * if (shippingItems.split(":").length == 2) {
     * productCriteriaSets = productParser.addCriteriaSetForCriteriaCode(existingProduct, product,
     * "SHES", shippingItems, emptyDescription);
     * if (null != productCriteriaSets && cntr < productCriteriaSetsAry.length) {
     * productCriteriaSets.setCompanyId(product.getCompanyId());
     * productCriteriaSetsAry[cntr] = productCriteriaSets;
     * cntr++;
     * } else {
     * productCriteriaSetsAry = Arrays.copyOf(productCriteriaSetsAry,
     * productCriteriaSetsAry.length - 1);
     * }
     * // Compare and update ShippingItems
     * ProductCriteriaSets finalCriteriaSet = existingCriteriaSetMap
     * .get(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE);
     * finalCriteriaSet = productSizeGroupProcessor.compareAndUpdateSizeGroup(existingProduct,
     * productCriteriaSets, finalCriteriaSet);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_SHIPPING_ITEM_CRITERIA_CODE,
     * finalCriteriaSet);
     * 
     * } else {
     * LOGGER.info("Invalid shipping item value");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE + ":Invalid shipping item value "
     * + shippingItems;
     * }
     * LOGGER.info("Shipping Items Transformation End");
     * }
     * } else if (i == 16) {
     * shippingDimensions = CommonUtilities.checkAndFixCSVValues(criteriaCodesAry[i]);
     * } else if (i == 17) {
     * shippingWeight = criteriaCodesAry[i];
     * } else if (i == 18) {
     * optionType = criteriaCodesAry[i];
     * } else if (i == 19) {
     * optionValues = CommonUtilities.checkAndFixCSVValues(criteriaCodesAry[i]);
     * } else if (i == 20) {
     * additionalColor = criteriaCodesAry[i];
     * AdditionalColorProcessor additionalColorProcessor = new AdditionalColorProcessor(-1201, configId);
     * if (CommonUtilities.isUpdateNeeded(additionalColor)) {
     * ProductCriteriaSets additionalColorCriteriaSet = additionalColorProcessor.getCriteriaSet(
     * additionalColor, !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ADDITIONAL_COLOR), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_ADDITIONAL_COLOR, additionalColorCriteriaSet);
     * } else {
     * additionalColorProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ADDITIONAL_COLOR),
     * product.getExternalProductId());
     * }
     * 
     * } else if (i == 21) {
     * additionalLocation = criteriaCodesAry[i];
     * AdditionalLocationProcessor additionalLocationProcessor = new AdditionalLocationProcessor(-1301,
     * configId);
     * if (CommonUtilities.isUpdateNeeded(additionalLocation)) {
     * // ALWAYS WITH THIS CONSTRUCTOR
     * ProductCriteriaSets additionalLocationCriteriaSet = additionalLocationProcessor.getCriteriaSet(
     * additionalLocation, !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ADDITIONAL_LOCATION), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_ADDITIONAL_LOCATION,
     * additionalLocationCriteriaSet);
     * } else {
     * additionalLocationProcessor.registerExistingValuesForReference(
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_ADDITIONAL_LOCATION),
     * product.getExternalProductId());
     * }
     * } else if (i == 22) {
     * productSample = criteriaCodesAry[i];
     * } else if (i == 23) {
     * specSample = criteriaCodesAry[i];
     * } else if (i == 24) {
     * minQuantity = criteriaCodesAry[i];
     * }
     * }
     * 
     * // Option Type Starts Here
     * 
     * if (null != optionType && !optionType.equalsIgnoreCase("null") && !optionType.equals("")
     * && CommonUtilities.isUpdateNeeded(optionType, true)) {
     * Map<String, List<ProductCriteriaSets>> newOptionsCriteriaSets = new HashMap<>();
     * 
     * optionType = CommonUtilities.removeUpdateCharsString(optionType);
     * LOGGER.info("Option Type Transformation Starts :" + optionType);
     * String[] optionTypeAry = optionType.split(",");
     * String[] optionValuesAry = CommonUtilities.removeUpdateCharsString(optionValues).split("\\|");
     * String[] optionNamesAry = CommonUtilities.removeUpdateCharsString(optionNames).split("\\|");
     * String[] reqForOrderAry = CommonUtilities.removeUpdateCharsString(reqForOrder).split("\\|");
     * String[] isMultipleChoiceAllowedAry = CommonUtilities.removeUpdateCharsString(isMultipleChoiceAllowed)
     * .split("\\|");
     * 
     * int optionValueCntr = 0;
     * for (String crntOptionType : optionTypeAry) {
     * if (crntOptionType.equalsIgnoreCase("Product Option")
     * || crntOptionType.equalsIgnoreCase("Imprint Option")
     * || crntOptionType.equalsIgnoreCase("Shipping Option")) {
     * if (optionValuesAry.length > optionValueCntr && null != optionValuesAry[optionValueCntr]
     * && !optionValuesAry[optionValueCntr].equalsIgnoreCase("null")
     * && !optionValuesAry[optionValueCntr].isEmpty()) {
     * ProductCriteriaSets prdCriteriaSets = productParser.addCriteriaSetForCriteriaCode(
     * existingProduct, product, crntOptionType, optionValuesAry[optionValueCntr],
     * optionNamesAry[optionValueCntr]);
     * if (prdCriteriaSets != null && CommonUtilities.isValueNull(optionNamesAry[optionValueCntr])) {
     * String temp = CommonUtilities.getValueFromCSV(optionValuesAry[optionValueCntr], 1);
     * if (!CommonUtilities.isValueNull(temp)) {
     * prdCriteriaSets.setCriteriaDetail(temp);
     * }
     * }
     * if (null != prdCriteriaSets && cntr < productCriteriaSetsAry.length) {
     * Boolean temp = CommonUtilities.getBooleanValueFromString(reqForOrderAry[optionValueCntr]);
     * if (temp != null) {
     * prdCriteriaSets.setIsRequiredForOrder(temp.toString());
     * } else {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid Req_for_order value " + reqForOrderAry[optionValueCntr]
     * + " found for " + crntOptionType);
     * }
     * temp = CommonUtilities
     * .getBooleanValueFromString(isMultipleChoiceAllowedAry[optionValueCntr]);
     * 
     * if (temp != null) {
     * prdCriteriaSets.setIsMultipleChoiceAllowed(temp.toString());
     * } else {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid Can_order_only_one value "
     * + isMultipleChoiceAllowedAry[optionValueCntr] + " found for "
     * + crntOptionType);
     * }
     * // TODO : values
     * prdCriteriaSets = ProductCompareUtil.multipleProductCriteriaSetCompareAndUpdate(product,
     * prdCriteriaSets.getCriteriaCode(), prdCriteriaSets,
     * optionsCriteriaSet.get(prdCriteriaSets.getCriteriaCode()), true);
     * if (newOptionsCriteriaSets.get(prdCriteriaSets.getCriteriaCode()) == null) {
     * newOptionsCriteriaSets.put(prdCriteriaSets.getCriteriaCode(),
     * new ArrayList<ProductCriteriaSets>());
     * }
     * newOptionsCriteriaSets.get(prdCriteriaSets.getCriteriaCode()).add(prdCriteriaSets);
     * // productCriteriaSetsAry[cntr] = prdCriteriaSets;
     * cntr++;
     * } else {
     * // productCriteriaSetsAry = Arrays.copyOf(productCriteriaSetsAry,
     * // productCriteriaSetsAry.length - 1);
     * // cntr--;
     * }
     * }
     * } else if (!CommonUtilities.isValueNull(crntOptionType)) {
     * LOGGER.info("Invalid Option Type" + crntOptionType);
     * productDataStore
     * .addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Option type "
     * + crntOptionType);
     * }
     * optionValueCntr++;
     * }
     * optionsCriteriaSet = newOptionsCriteriaSets;
     * LOGGER.info("Option Type Transformation End");
     * } else if (CommonUtilities.isValueNull(optionType)) {
     * optionsCriteriaSet = new HashMap<String, List<ProductCriteriaSets>>();
     * } else if (!CommonUtilities.isUpdateNeeded(optionType)) {
     * productOptionProcessor.registerExistingOptionValues(product.getExternalProductId(), optionsCriteriaSet);
     * }
     * 
     * // Product Sample and Spec Sample
     * ProductSpecSampleProcessor specSampleProcessor = new ProductSpecSampleProcessor(-1401, configId);
     * ProductCriteriaSets specSampleCriteriaSet = specSampleProcessor.getCriteriaSetForMultiple(specSample,
     * productSample, !isNewProduct ? existingProduct : product,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE), 0);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_PRODUCT_SAMPLE_CRITERIA_CODE, specSampleCriteriaSet);
     * 
     * 
     * Same Day RUSH Service Processing
     * 
     * 
     * if (!String.valueOf(product.getSameDayRushFlag()).equalsIgnoreCase(
     * ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
     * SameDayServiceProcessor sameDayServiceProcessor = new SameDayServiceProcessor(-1501);
     * ProductCriteriaSets sameDayCriteriaSets = existingCriteriaSetMap
     * .get(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE);
     * sameDayCriteriaSets = sameDayServiceProcessor.getCriteriaSetForSameDayService(sameDayCriteriaSets,
     * !isNewProduct ? existingProduct : product, String.valueOf(product.getSameDayRushFlag()), configId);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_STRING_SAME_DAY_RUSH_SERVICE, sameDayCriteriaSets);
     * }
     * 
     * 
     * Less_Than_Minimum Order Processing
     * 
     * if (!CommonUtilities.isValueNull(product.getIsOrderLessThanMinimumAllowed())
     * && CommonUtilities.getBooleanValueFromYesOrNo(product.getIsOrderLessThanMinimumAllowed())) {
     * product.setIsOrderLessThanMinimumAllowed(CommonUtilities.getBooleanValueFromYesOrNo(product
     * .getIsOrderLessThanMinimumAllowed()) + "");
     * ProductCriteriaSets productCriteriaSet = priceGridParser.getLessThanMinimumCriteriaSet(product);
     * if (productCriteriaSet != null) {
     * productCriteriaSetsAry[cntr++] = productCriteriaSet;
     * }
     * } else {
     * product.setIsOrderLessThanMinimumAllowed(CommonUtilities.getBooleanValueFromYesOrNo(product
     * .getIsOrderLessThanMinimumAllowed()) + "");
     * }
     * 
     * // Dimensions Parsing Starts Here
     * try {
     * if (!CommonUtilities.isUpdateNeeded(sizeGroups) || !CommonUtilities.isUpdateNeeded(sizeValues)) {
     * // No need to change anything
     * // Get size group related CriteriaSet
     * ProductCriteriaSets sizeCriteriaSet = productSizeGroupProcessor
     * .getSizeRelatedCriteriaSetFromExisting(existingCriteriaSetMap);
     * if (sizeCriteriaSet != null) {
     * productSizeGroupProcessor.registerExistingValuesForReference(sizeCriteriaSet,
     * product.getExternalProductId());
     * }
     * } else if (CommonUtilities.isValueNull(sizeGroups) || CommonUtilities.isValueNull(sizeValues)) {
     * // Remove from existing collection
     * existingCriteriaSetMap = productSizeGroupProcessor
     * .removeSizeRelatedCriteriaSetFromExisting(existingCriteriaSetMap);
     * } else if (null != sizeGroups && !sizeGroups.trim().equals("")
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
     * LOGGER.info("SizeGroups Transformation Starts :" + sizeGroups);
     * ProductCriteriaSets sizeCriteriaSets = null;
     * if (!sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_DIMENSION)
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_CAPACITY)
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_VOLUME_WEIGHT)) {
     * if (!sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM)
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_INFANT_TODDLER)
     * && !sizeGroups
     * .equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_DRESS_SHIRT_SIZES)
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_PANTS_SIZES)
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_BRA_SIZES)
     * && !sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_STANDARD_NUMBERED))
     * sizeGroups = ApplicationConstants.CONST_SIZE_OTHER_CODE;
     * if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_APPAREL_HOSIERY_UNIFORM))
     * sizeValues = sizeValues.toUpperCase();
     * if (null != sizeValues && !sizeValues.trim().equals("")) {
     * // cntr = productCriteriaSetsAry.length + 1;
     * sizeCriteriaSets = productParser.addCriteriaSetForCriteriaCode(existingProduct, product,
     * sizeGroups, sizeValues, emptyDescription);
     * }
     * } else if (!sizeGroups.equals("")) {
     * String[] tmpSizeValuesAry = sizeValues.split(":");
     * if (sizeGroups.equalsIgnoreCase(ApplicationConstants.CONST_STRING_DIMENSION)
     * && tmpSizeValuesAry.length > 3) {
     * sizeCriteriaSets = productParser.addCriteriaSetForSizes(sizeValues, existingProduct, product,
     * sizeGroups, productCriteriaSetsAry);
     * } else if (tmpSizeValuesAry.length > 1) {
     * sizeCriteriaSets = productParser.addCriteriaSetForSizes(sizeValues, existingProduct, product,
     * sizeGroups, productCriteriaSetsAry);
     * }
     * 
     * }
     * if (sizeCriteriaSets != null) {
     * String criteriaCode = sizeCriteriaSets.getCriteriaCode();
     * ProductCriteriaSets exisitingCriteriaSet = existingCriteriaSetMap.get(criteriaCode);
     * if (exisitingCriteriaSet == null) {
     * existingCriteriaSetMap = productSizeGroupProcessor
     * .removeSizeRelatedCriteriaSetFromExisting(existingCriteriaSetMap);
     * exisitingCriteriaSet = productSizeGroupProcessor.compareAndUpdateSizeGroup(existingProduct,
     * sizeCriteriaSets, exisitingCriteriaSet);
     * } else {
     * exisitingCriteriaSet = productSizeGroupProcessor.compareAndUpdateSizeGroup(existingProduct,
     * sizeCriteriaSets, exisitingCriteriaSet);
     * }
     * if (exisitingCriteriaSet != null) {
     * existingCriteriaSetMap.put(criteriaCode, exisitingCriteriaSet);
     * }
     * }
     * LOGGER.info("SizeGroups Transformation Ends");
     * }
     * } catch (Exception e) {
     * LOGGER.error("Excepiton while processing SIZE CriteriaSetValues", e);
     * }
     * // Shipping Dimensions and Weight
     * String[] dimnsAry = {};
     * if (CommonUtilities.isValueNull(shippingDimensions)) {
     * // Remove Previous SDIM from the criteria set
     * existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION);
     * 
     * } else if (null != shippingDimensions && !shippingDimensions.trim().equals("")
     * && !shippingDimensions.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(shippingDimensions, true)) {
     * LOGGER.info("Shipping Dimensions Transformation Starts :" + shippingDimensions);
     * // cntr = productCriteriaSetsAry.length + 1;
     * dimnsAry = shippingDimensions.split(";");
     * ProductCriteriaSets productCriteriaSet = null;
     * if (dimnsAry.length == 3) {
     * productCriteriaSet = productParser.addCriteriaSetForSizes(shippingDimensions, existingProduct, product,
     * "ShippingDimension", productCriteriaSetsAry);
     * if (productCriteriaSet != null) {
     * productCriteriaSetsAry[cntr++] = productCriteriaSet;
     * }
     * } else {
     * LOGGER.info("Invalid Shipping Dimension");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE + ":Invalid Shipping Dimension, value "
     * + shippingDimensions;
     * }
     * // compare and update
     * productCriteriaSet = productSizeGroupProcessor.compareAndUpdateSizeGroup(existingProduct,
     * productCriteriaSet,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION));
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_DIMENSION, productCriteriaSet);
     * LOGGER.info("Shipping Dimensions Transformation Ends");
     * }
     * 
     * if (CommonUtilities.isValueNull(shippingWeight)) {
     * existingCriteriaSetMap.remove(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT);
     * } else if (null != shippingWeight && !shippingWeight.equals("")
     * && !shippingWeight.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(shippingWeight, true)) {
     * LOGGER.info("Shipping Weight Transformation Starts :" + shippingWeight);
     * dimnsAry = shippingWeight.split(":");
     * if (dimnsAry.length > 1) {
     * ProductCriteriaSets productCriteriaSet = productParser.addCriteriaSetForSizes(shippingWeight,
     * existingProduct, product, "ShippingWeight", productCriteriaSetsAry);
     * if (productCriteriaSet != null) {
     * productCriteriaSet = productSizeGroupProcessor.compareAndUpdateSizeGroup(existingProduct,
     * productCriteriaSet,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT));
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_WEIGHT,
     * productCriteriaSet);
     * productCriteriaSetsAry[cntr++] = productCriteriaSet;
     * }
     * } else {
     * LOGGER.info("Invalid Shipping Weight");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE + ":Invalid Shipping weight, value "
     * + shippingWeight;
     * }
     * 
     * LOGGER.info("Shipping Weight Transformation Ends");
     * 
     * }
     * if (CommonUtilities.isValueNull(imprintMethods)
     * || imprintMethods.toUpperCase().startsWith(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
     * // remove all imprint related elements from criteria set, because imprint method is the parent
     * existingCriteriaSetMap.remove(ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
     * existingCriteriaSetMap.remove(ApplicationConstants.CONST_MINIMUM_QUANTITY);
     * existingCriteriaSetMap.remove(ApplicationConstants.CONST_ARTWORK_CODE);
     * 
     * } else if (CommonUtilities.isUpdateNeeded(imprintMethods, true)) {
     * imprintMethods = CommonUtilities.removeUpdateCharsString(String.valueOf(imprintMethods));
     * imprintArtwork = CommonUtilities.removeUpdateCharsString(String.valueOf(imprintArtwork));
     * minQuantity = CommonUtilities.removeUpdateCharsString(String.valueOf(minQuantity));
     * if (!imprintMethods.equals("")
     * && !imprintMethods.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(imprintMethods, true)) {
     * LOGGER.info("Imprint Methods Transformation Starts :" + imprintMethods);
     * if (imprintMethods.contains("Personalization")) {
     * imprintMethods = imprintMethods.replace("Personalization", ApplicationConstants.CONST_STRING_OTHER
     * + "=Personalization");
     * }
     * if (imprintMethods.contains("Unimprinted")) {
     * imprintMethods = imprintMethods.replace("Unimprinted", ApplicationConstants.CONST_STRING_OTHER
     * + "=Unimprinted");
     * }
     * String tempImprintMethod = imprintMethods;
     * if (CommonUtilities.getBooleanValueFromYesOrNo(product.getIsPersonalizationAvailable())) {
     * tempImprintMethod += ",Personalization";
     * }
     * if (CommonUtilities.getBooleanValueFromYesOrNo(product.getIsAvailableUnimprinted())) {
     * tempImprintMethod += ",Unimprinted";
     * }
     * if (isSimplifiedTemplate && existingProduct != null
     * && existingProduct.getProductConfigurations() != null
     * && existingProduct.getProductConfigurations().length > 0) {
     * ProductCriteriaSets temp = productParser.getCriteriaSetBasedOnCriteriaCode(
     * existingProduct.getProductConfigurations()[0].getProductCriteriaSets(),
     * ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
     * tempImprintMethod = ProductCompareUtil.correctImprintMethodForSimplifiedTemplate(tempImprintMethod,
     * temp);
     * }
     * ProductCriteriaSets imprintCriteriaSets = productParser.addCriteriaSetForImprint(existingProduct,
     * product, tempImprintMethod, productCriteriaSetsAry,
     * ApplicationConstants.CONST_IMPRINT_METHOD_CODE);
     * if (imprintCriteriaSets != null) {
     * imprintCriteriaSets = productImprintMethodProcessor.compareAndUpdateImprintMethod(
     * imprintCriteriaSets,
     * existingCriteriaSetMap.get(ApplicationConstants.CONST_IMPRINT_METHOD_CODE), configId,
     * productId);
     * existingCriteriaSetMap.put(ApplicationConstants.CONST_IMPRINT_METHOD_CODE, imprintCriteriaSets);
     * // productCriteriaSetsAry[cntr++] = imprintCriteriaSets;
     * 
     * }
     * LOGGER.info("Imprint Methods Transformation Ends");
     * 
     * } else if (!CommonUtilities.isUpdateNeeded(imprintMethods)) {
     * if (existingProduct != null && existingProduct.getProductConfigurations().length > 0) {
     * Map<String, Object> tempMap = productParser.updateProductCriteriaSetArray(productCriteriaSetsAry,
     * cntr, ApplicationConstants.CONST_IMPRINT_METHOD_CODE, existingProduct);
     * productCriteriaSetsAry = (ProductCriteriaSets[]) tempMap.get("CriteriaSet");
     * cntr = (int) tempMap.get("Counter");
     * }
     * }
     * if (!imprintArtwork.equals("")
     * && !imprintArtwork.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(imprintArtwork, true)) {
     * LOGGER.info("Imprint Artwork Transformation Starts :" + imprintArtwork);
     * 
     * ProductCriteriaSets artwrkCriteriaSet = productParser.addCriteriaSetForImprint(existingProduct,
     * product, imprintArtwork, productCriteriaSetsAry, ApplicationConstants.CONST_ARTWORK_CODE);
     * if (artwrkCriteriaSet != null && artwrkCriteriaSet.getCriteriaSetValues() != null
     * && artwrkCriteriaSet.getCriteriaSetValues().length > 0) {
     * productCriteriaSetsAry[cntr++] = artwrkCriteriaSet;
     * }
     * } else if (!CommonUtilities.isUpdateNeeded(imprintArtwork)) {
     * if (existingProduct != null && existingProduct.getProductConfigurations().length > 0) {
     * Map<String, Object> tempMap = productParser.updateProductCriteriaSetArray(productCriteriaSetsAry,
     * cntr, ApplicationConstants.CONST_ARTWORK_CODE, existingProduct);
     * productCriteriaSetsAry = (ProductCriteriaSets[]) tempMap.get("CriteriaSet");
     * cntr = (int) tempMap.get("Counter");
     * }
     * }
     * 
     * if (!minQuantity.equals("") && !minQuantity.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(minQuantity, true)) {
     * LOGGER.info("Imprint Minimum Quantity Transformation Starts :" + minQuantity);
     * // Validate Min_QTY
     * String[] minQtys = minQuantity.split("\\|");
     * String tempElement = "";
     * if (minQtys != null && minQtys.length > 0) {
     * for (int i = 0; i < minQtys.length; i++) {
     * if (!CommonUtilities.isValueNull(minQtys[i])) {
     * String[] valuesArray = minQtys[i].split(":");
     * if (valuesArray != null && valuesArray.length == 2 && valuesArray[0] != null
     * && valuesArray[1] != null) {
     * tempElement += minQtys[i] + "|";
     * } else {
     * tempElement += "null|";
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE
     * + ":Invalid Min_QTY specified - " + minQtys[i];
     * }
     * } else {
     * tempElement += "null|";
     * }
     * }
     * if (tempElement != null && !tempElement.isEmpty() && tempElement.endsWith("|")) {
     * tempElement = tempElement.substring(0, tempElement.length() - 1);
     * }
     * minQuantity = tempElement;
     * }
     * ProductCriteriaSets minQtyCriteriaSet = productParser.addCriteriaSetForImprint(existingProduct,
     * product, minQuantity, productCriteriaSetsAry, ApplicationConstants.CONST_MINIMUM_QUANTITY);
     * if (minQtyCriteriaSet != null) {
     * productCriteriaSetsAry[cntr++] = minQtyCriteriaSet;
     * }
     * 
     * } else if (!CommonUtilities.isUpdateNeeded(minQuantity)) {
     * if (existingProduct != null && existingProduct.getProductConfigurations().length > 0) {
     * Map<String, Object> tempMap = productParser.updateProductCriteriaSetArray(productCriteriaSetsAry,
     * cntr, ApplicationConstants.CONST_MINIMUM_QUANTITY, existingProduct);
     * productCriteriaSetsAry = (ProductCriteriaSets[]) tempMap.get("CriteriaSet");
     * cntr = (int) tempMap.get("Counter");
     * }
     * }
     * }
     * try {
     * if (existingProduct != null && existingProduct.getProductConfigurations() != null
     * && existingProduct.getProductConfigurations().length > 0) {
     * ProductCriteriaSets[] productCriteriaSetTemp = productParser.getCriteriaSetsBasedOnCriteriaCode(
     * existingProduct.getProductConfigurations()[0].getProductCriteriaSets(),
     * ApplicationConstants.CONST_CRITERIA_CODE_LNNM);
     * if (productCriteriaSetTemp != null && productCriteriaSetTemp.length > 0) {
     * for (ProductCriteriaSets prodCrtSet : productCriteriaSetTemp) {
     * if (prodCrtSet != null) {
     * productCriteriaSetsAry[cntr++] = prodCrtSet;
     * }
     * }
     * }
     * // FOB Point registration
     * productCriteriaSetTemp = productParser.getCriteriaSetsBasedOnCriteriaCode(
     * existingProduct.getProductConfigurations()[0].getProductCriteriaSets(),
     * ApplicationConstants.CONST_CRITERIA_CODE_FOBP);
     * if (productCriteriaSetTemp != null && productCriteriaSetTemp.length > 0) {
     * int counter = 0;
     * for (ProductCriteriaSets prodCrtSet : productCriteriaSetTemp) {
     * ProductCompareUtil.registerFobPoints(product.getExternalProductId(),
     * productCriteriaSetTemp[counter++]);
     * if (prodCrtSet != null) {
     * productCriteriaSetsAry[cntr++] = prodCrtSet;
     * }
     * }
     * }
     * }
     * } catch (Exception e) {
     * // TODO : Add some catching
     * LOGGER.error("Exception while processing either FOB points or LNNM", e);
     * }
     * 
     * try {
     * productCriteriaSetsAry = ProductCompareUtil.getFinalProductCriteriaSets(existingCriteriaSetMap,
     * optionsCriteriaSet, productCriteriaSetsAry);
     * if (isNewProduct) {
     * product.getProductConfigurations()[0].setProductCriteriaSets(productCriteriaSetsAry);
     * } else {
     * if (existingProduct.getProductConfigurations() != null
     * && existingProduct.getProductConfigurations().length > 0) {
     * existingProduct.getProductConfigurations()[0].setProductCriteriaSets(productCriteriaSetsAry);
     * } else {
     * ProductConfigurations productConfigurations = new ProductConfigurations();
     * productConfigurations.setProductId(productId);
     * productConfigurations.setId(configId);
     * productConfigurations.setIsDefault(ApplicationConstants.CONST_STRING_TRUE_SMALL);
     * productConfigurations.setProductCriteriaSets(productCriteriaSetsAry);
     * 
     * existingProduct.setProductConfigurations(new ProductConfigurations[] { productConfigurations });
     * }
     * // For Temporary purpose
     * product.getProductConfigurations()[0].setProductCriteriaSets(productCriteriaSetsAry);
     * }
     * } catch (Exception e) {
     * LOGGER.error("Exception while finalizing ProductCriteriaSet processing", e);
     * }
     * 
     * if (CommonUtilities.isUpdateNeeded(product.getIsShippableInPlainBox())) {
     * if (isNewProduct) {
     * product.setIsShippableInPlainBox(CommonUtilities.parseBooleanValue(product.getIsShippableInPlainBox()));
     * } else {
     * existingProduct.setIsShippableInPlainBox(CommonUtilities.parseBooleanValue(product
     * .getIsShippableInPlainBox()));
     * }
     * } else if (!CommonUtilities.isUpdateNeeded(product.getIsShippableInPlainBox())) {
     * if (existingProduct != null) {
     * product.setIsShippableInPlainBox(existingProduct.getIsShippableInPlainBox());
     * } else {
     * product.setIsShippableInPlainBox("false");
     * }
     * }
     * if (!CommonUtilities.isValidForNewProduct(product.getIsAvailableUnimprinted())) {
     * if (existingProduct != null) {
     * product.setIsAvailableUnimprinted(existingProduct.getIsAvailableUnimprinted());
     * } else {
     * product.setIsAvailableUnimprinted("false");
     * }
     * } else {
     * product.setIsAvailableUnimprinted("false");
     * }
     * 
     * if (!CommonUtilities.isValidForNewProduct(product.getIsPersonalizationAvailable())) {
     * if (!isNewProduct) {
     * existingProduct.setIsPersonalizationAvailable(existingProduct.getIsPersonalizationAvailable());
     * } else {
     * product.setIsPersonalizationAvailable("false");
     * }
     * } else {
     * product.setIsPersonalizationAvailable("false");
     * }
     * 
     * if (!CommonUtilities.isUpdateNeeded(product.getShipperBillsByCode())) {
     * if (!isNewProduct) {
     * existingProduct.setShipperBillsByCode(existingProduct.getShipperBillsByCode());
     * } else {
     * product.setShipperBillsByCode(null);
     * }
     * } else if (CommonUtilities.isWordExistsInReservedWordGroup(product.getShipperBillsByCode(),
     * ApplicationConstants.CONST_STRING_GRP_SHIP_BILL_BY, false)) {
     * if (!isNewProduct) {
     * existingProduct.setShipperBillsByCode(commonUtils.parseWeightValue(product.getShipperBillsByCode()));
     * } else {
     * product.setShipperBillsByCode(commonUtils.parseWeightValue(product.getShipperBillsByCode()));
     * }
     * 
     * } else if (product.getShipperBillsByCode() != null && !product.getShipperBillsByCode().trim().isEmpty()
     * && !product.getShipperBillsByCode().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)) {
     * LOGGER.info("Invalid ShipperBillsByCode : " + product.getShipperBillsByCode());
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid ShipperBillsByCode");
     * 
     * if (!isNewProduct) {
     * product.setShipperBillsByCode(existingProduct.getShipperBillsByCode());
     * } else {
     * product.setShipperBillsByCode(null);
     * }
     * }
     * 
     * if (!CommonUtilities.isUpdateNeeded(product.getAddtionalShippingInfo())) {
     * if (existingProduct != null) {
     * product.setAddtionalShippingInfo(existingProduct.getAddtionalShippingInfo());
     * } else {
     * product.setAddtionalShippingInfo("");
     * }
     * } else if (CommonUtilities.isValueNull(product.getAddtionalShippingInfo())) {
     * if (!isNewProduct) {
     * existingProduct.setAddtionalShippingInfo("");
     * } else {
     * product.setAddtionalShippingInfo("");
     * }
     * } else {
     * if (!isNewProduct) {
     * existingProduct.setAddtionalShippingInfo(product.getAddtionalShippingInfo());
     * }
     * }
     * 
     * List<String> criteriaSetValueIds = CommonUtilities.getAllCriteriaSetValueIds(productCriteriaSetsAry);
     * 
     * Map<String, Relationships> relationShips = new HashMap<String, Relationships>();
     * Relationships[] tempExtRelations = existingProduct != null ? existingProduct.getRelationships() : null;
     * int relationshipId = -1;
     * if (CommonUtilities.isUpdateNeeded(imprintArtwork, false) && !imprintArtwork.equals("")
     * && !CommonUtilities.isElementsAreNull(imprintArtwork, "|")
     * && !imprintMethods.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && !imprintMethods.equals("")
     * && !imprintArtwork.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
     * LOGGER.info("Imprint Relationships Transformation Starts");
     * 
     * try {
     * Relationships relationShip = productParser.createImprintArtworkRelationShip(productCriteriaSetsAry,
     * product, imprintMethods, imprintArtwork, true, String.valueOf(relationshipId));
     * 
     * if (relationShip != null) {
     * // Before adding relationship we need to check for existence of this relationship
     * if (existingProduct != null) {
     * relationShip = productParser.compareAndUpdateRelationship(tempExtRelations, relationShip,
     * ApplicationConstants.CONST_ARTWORK_CODE, productCriteriaSetsAry);
     * }
     * if (relationShip != null) {
     * relationShips.put(ApplicationConstants.CONST_ARTWORK_CODE, relationShip);
     * relationshipId--;
     * }
     * }
     * } catch (Exception e) {
     * LOGGER.error("Exception occured in Artwork RL processing, continue work : " + e.getMessage(), e);
     * }
     * } else if (!CommonUtilities.isUpdateNeeded(imprintArtwork, false) && existingProduct != null) {
     * try {
     * Relationships relationShip = new ProductCompareUtil().getValidRelationshipForCriteriaCodes(
     * productCriteriaSetsAry, existingProduct.getRelationships(),
     * ApplicationConstants.CONST_IMPRINT_METHOD_CODE, ApplicationConstants.CONST_ARTWORK_CODE,
     * criteriaSetValueIds);
     * 
     * if (relationShip != null) {
     * relationShips.put(ApplicationConstants.CONST_ARTWORK_CODE, relationShip);
     * relationshipId--;
     * }
     * } catch (Exception e) {
     * LOGGER.error(
     * "Exception occured in Artwork RL processing in Simplified Template, continue work : "
     * + e.getMessage(), e);
     * }
     * }
     * 
     * if (!minQuantity.equals("") && !CommonUtilities.isElementsAreNull(minQuantity, "|")
     * && !minQuantity.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && !minQuantity.equals("")
     * && !minQuantity.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
     * && CommonUtilities.isUpdateNeeded(minQuantity, true)) {
     * LOGGER.info("Imprint Relationships Transformation Starts");
     * try {
     * Relationships relationShip = productParser.createImprintArtworkRelationShip(productCriteriaSetsAry,
     * product, imprintMethods, minQuantity, false, String.valueOf(relationshipId));
     * 
     * if (relationShip != null) {
     * if (existingProduct != null) {
     * relationShip = productParser.compareAndUpdateRelationship(tempExtRelations, relationShip,
     * ApplicationConstants.CONST_MINIMUM_QUANTITY, productCriteriaSetsAry);
     * }
     * if (relationShip != null) {
     * relationShips.put(ApplicationConstants.CONST_MINIMUM_QUANTITY, relationShip);
     * relationshipId--;
     * }
     * }
     * } catch (Exception e) {
     * LOGGER.error("Exception occured in MinQTY RL Process, continue process : " + e.getMessage());
     * }
     * } else if (!CommonUtilities.isUpdateNeeded(minQuantity, true) && existingProduct != null) {
     * try {
     * Relationships relationShip = new ProductCompareUtil().getValidRelationshipForCriteriaCodes(
     * productCriteriaSetsAry, existingProduct.getRelationships(),
     * ApplicationConstants.CONST_IMPRINT_METHOD_CODE, ApplicationConstants.CONST_MINIMUM_QUANTITY,
     * criteriaSetValueIds);
     * 
     * if (relationShip != null) {
     * relationShips.put(ApplicationConstants.CONST_MINIMUM_QUANTITY, relationShip);
     * relationshipId--;
     * }
     * } catch (Exception e) {
     * LOGGER.error(
     * "Exception occured in Minimum Quantity RL processing in Simplified Template, continue work : "
     * + e.getMessage(), e);
     * }
     * }
     * 
     * product.setRelationships(ProductCompareUtil.mergeRelationShips(relationShips, existingProduct));
     * // Prices Lookup Starts Here
     * String price = "", quantity = "", discount = "";
     * price = product.getPriceGrids()[0].getPrices()[0].getListPrice();
     * quantity = product.getPriceGrids()[0].getPrices()[0].getQuantity();
     * discount = product.getPriceGrids()[0].getPrices()[0].getDiscountRate().getCode();
     * boolean isSimplifiedPG = CommonUtilities.getBooleanValueFromString(product.getPriceGrids()[0].getIsBasePrice());
     * PriceGrids[] priceGridsLst = {};
     * if (!price.equals("") && !quantity.equals("") && !discount.equals("")) {
     * 
     * try {
     * PriceGridParser priceGridParser = new PriceGridParser();
     * 
     * LOGGER.info("Prices Transformation Starts :" + price);
     * boolean isValidPGFound = false;
     * String[] priceType = price.split("@");
     * // int maxNumberOfPGrdidFound = PriceGridParser.findNumberOfPGridAvailable(priceType);
     * int numberOfValidBasePriceGrids = PriceGridParser.findPGridCountByType(priceType, true);
     * String[] quantityType = quantity.split("@");
     * String[] discountType = discount.split("@");
     * 
     * priceGridsLst = new PriceGrids[priceType.length];
     * 
     * String[] actualPriceGrids = product.getPriceGrids()[0].getIsQUR().split(":");
     * String actualCurrency = PriceGridParser.getCurrency(product.getPriceGrids()[0].getCurrency().getCode());
     * // String pgSubType = "REGL";
     * String pgUsageLevel = ApplicationConstants.CONST_STRING_NONE_CAP;
     * String[] pricingSubTypeCode = product.getPriceGrids()[0].getPriceGridSubTypeCode().split("\\|");
     * String[] pricingUsageLevel = product.getPriceGrids()[0].getUsageLevelCode().split("\\|");
     * String[] priceCriteriaAry = product.getPriceGrids()[0].getPricingItems()[0].getCriteriaSetValueId()
     * .split("\\$\\$\\$");
     * 
     * String[] priceCriteria2Ary = product.getPriceGrids()[0].getPricingItems()[0].getDescription().split(
     * "\\$\\$\\$");
     * String[] temp = PriceGridParser.getValidPriceCriteriaCode(priceCriteriaAry, priceCriteria2Ary, true)
     * .split("\\#\\$");
     * 
     * String basePriceCriteria1Code = temp[0];
     * String basePriceCriteria2Code = temp[1];
     * Set<String> optionGroupCriteriaSet = new HashSet<>();
     * if (PriceGridParser.isOptionGroup(basePriceCriteria1Code)
     * | PriceGridParser.isOptionGroup(basePriceCriteria2Code)) {
     * // Create combination for list option group
     * optionGroupCriteriaSet = priceGridParser.getOptionGroupPriceCriterias(priceCriteriaAry,
     * priceCriteria2Ary);
     * }
     * temp = PriceGridParser.getValidPriceCriteriaCode(priceCriteriaAry, priceCriteria2Ary, false).split(
     * "\\#\\$");
     * 
     * String upchargeCriteria1Code = temp[0];
     * String upchargeCriteria2Code = temp[1];
     * 
     * List<String> priceCriteriaValuesList = new ArrayList<String>();
     * 
     * String[] priceIncludes = product.getPriceGrids()[0].getPriceIncludes().split(
     * ApplicationConstants.PRODUCT_RPBST_ELEMENT_SPLITTER_CODE);
     * boolean processPrices = true;
     * String[] basePrices = product.getPriceGrids()[0].getDescription().split("\\$");
     * int priceGridRowCount = 0;
     * for (int priceTypeCntr = 0; priceTypeCntr < priceType.length; priceTypeCntr++) {
     * PriceGrids tempPriceGrids = null;
     * price = priceType[priceTypeCntr];
     * quantity = quantityType[priceTypeCntr];
     * if (priceTypeCntr % 2 == 0) {
     * priceGridRowCount++;
     * }
     * String quantityChk = quantity.replaceAll(ApplicationConstants.CONST_STRING_NULL_SMALL, "");
     * discount = discountType[priceTypeCntr];
     * if (commonUtils.isThatValidInteger(quantityChk)) {
     * try {
     * tempPriceGrids = productParser.getPriceGridsByPriceType(null, product, price, quantity,
     * discount, actualCurrency,
     * CommonUtilities.parseBooleanValue(actualPriceGrids[priceTypeCntr]),
     * basePrices[priceTypeCntr], (priceTypeCntr % 2 == 0), priceGridRowCount);
     * } catch (Exception e) {
     * tempPriceGrids = null;
     * LOGGER.error("PriceGrid Processing failed ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR
     * + ":PriceGrid processing failed";
     * 
     * }
     * } else {
     * processPrices = false;
     * LOGGER.warn("Quantity Should be Integer Value");
     * }
     * if (null != tempPriceGrids) {
     * if (!CommonUtilities.isValueNull(priceIncludes[priceTypeCntr])) {
     * tempPriceGrids.setPriceIncludes(priceIncludes[priceTypeCntr]);
     * } else {
     * tempPriceGrids.setPriceIncludes("");
     * }
     * tempPriceGrids.setIsQUR(CommonUtilities.parseBooleanValue(actualPriceGrids[priceTypeCntr]));
     * 
     * if (priceTypeCntr % 2 == 0) {
     * tempPriceGrids.setPriceGridSubTypeCode(PriceGridUtil.getPriceGridSubTypeCode(
     * priceCriteriaAry[priceTypeCntr], priceCriteria2Ary[priceTypeCntr], true));
     * String priceGridDescription = "";
     * // priceCriteriaAry[priceTypeCntr] will be in this format<criteriaCode>:<criteriaCodevalues
     * // separated by ,>
     * List<PricingItems> pricingItemsList = new ArrayList<PricingItems>();
     * try {
     * if (priceCriteriaValuesList.contains(PriceGridParser.getPriceCriteriaCombinationString(
     * basePriceCriteria1Code, priceCriteriaAry[priceTypeCntr],
     * basePriceCriteria2Code, priceCriteria2Ary[priceTypeCntr]))) {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
     * "A PriceGrid already exists with the given Criteria "
     * + CommonUtilities.getStringValue(priceCriteriaAry[priceTypeCntr]));
     * continue;
     * } else {
     * pricingItemsList = priceGridParser.generatePricingItemsForCriteria(
     * priceCriteriaAry[priceTypeCntr], product, tempPriceGrids,
     * basePriceCriteria1Code, true);
     * if (!pricingItemsList.isEmpty()) {
     * priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
     * basePriceCriteria1Code, priceCriteriaAry[priceTypeCntr],
     * basePriceCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
     * } else if (pricingItemsList.isEmpty()
     * && numberOfValidBasePriceGrids > 2
     * && ApplicationConstants.CONST_STRING_FALSE_SMALL
     * .equalsIgnoreCase(tempPriceGrids.getIsQUR())
     * && CommonUtilities.isValueNull(priceCriteriaAry[priceTypeCntr])) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId()
     * .trim(), ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
     * "PriceCriteria cannot be empty if product has multiple PriceGrid ");
     * continue;
     * }
     * }
     * if (!CommonUtilities.isValueNull(priceCriteriaAry[priceTypeCntr])
     * && pricingItemsList.isEmpty()) {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid Base Price Criteria 1 " + priceCriteriaAry[priceTypeCntr]);
     * continue;
     * }
     * } catch (AmbiguousPriceCriteriaException apre) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
     * continue;
     * }
     * 
     * if (pricingItemsList != null && !pricingItemsList.isEmpty()
     * && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * priceGridDescription = PriceGridParser.getValidCriteriaValues();
     * }
     * List<PricingItems> pricingItemList2 = new ArrayList<PricingItems>();
     * try {
     * if (!CommonUtilities.isValueNull(basePriceCriteria1Code)
     * && basePriceCriteria1Code.equalsIgnoreCase(basePriceCriteria2Code)) {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
     * "Invalid PriceCriteria "
     * + CommonUtilities.getStringValue(priceCriteria2Ary[priceTypeCntr]));
     * } else {
     * pricingItemList2 = priceGridParser.generatePricingItemsForCriteria(
     * priceCriteria2Ary[priceTypeCntr], product, tempPriceGrids,
     * basePriceCriteria2Code, true);
     * if (!pricingItemList2.isEmpty()) {
     * priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
     * basePriceCriteria1Code, priceCriteriaAry[priceTypeCntr],
     * basePriceCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
     * }
     * }
     * if (!CommonUtilities.isValueNull(priceCriteria2Ary[priceTypeCntr])
     * && pricingItemsList.isEmpty()) {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid Base Price Criteria 2 " + priceCriteria2Ary[priceTypeCntr]);
     * continue;
     * }
     * } catch (AmbiguousPriceCriteriaException apre) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
     * continue;
     * }
     * 
     * if (pricingItemList2 != null && !pricingItemList2.isEmpty()
     * && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * priceGridDescription = priceGridDescription.isEmpty() ? PriceGridParser
     * .getValidCriteriaValues() : priceGridDescription + ", "
     * + PriceGridParser.getValidCriteriaValues();
     * }
     * 
     * pricingItemsList.addAll(pricingItemList2);
     * 
     * if (!CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * tempPriceGrids.setDescription(basePrices[priceTypeCntr]);
     * } else if (!CommonUtilities.isValueNull(priceGridDescription)
     * && !pricingItemsList.isEmpty()) {
     * tempPriceGrids.setDescription(priceGridDescription);
     * } else if (!pricingItemsList.isEmpty() && CommonUtilities.isValueNull(priceGridDescription)
     * && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * tempPriceGrids.setDescription(product.getName());
     * }
     * 
     * if (!pricingItemsList.isEmpty()) {
     * tempPriceGrids.setPricingItems(pricingItemsList.toArray(new PricingItems[0]));
     * } else if (pricingItemsList != null && pricingItemsList.isEmpty() && isValidPGFound
     * && tempPriceGrids != null) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "PriceGrid without a valid price criteria");
     * tempPriceGrids = null;
     * } else {
     * tempPriceGrids.setPricingItems(new PricingItems[] {});
     * }
     * 
     * } else if (priceTypeCntr % 2 == 1) {
     * if (CommonUtilities.isValueNull(upchargeCriteria1Code)) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_UP_CRT,
     * "Upcharge criteria 1 is required for creating Upcharge PriceGrid");
     * continue;
     * }
     * tempPriceGrids.setPriceGridSubTypeCode(PriceGridUtil.getPriceGridSubTypeCode(
     * priceCriteriaAry[priceTypeCntr], priceCriteria2Ary[priceTypeCntr], false));
     * String tempCriteriaCode = priceGridParser
     * .getCriteriaCodeFromPriceCriteria(priceCriteriaAry[priceTypeCntr]);
     * if (!CommonUtilities.isValueNull(basePriceCriteria1Code)
     * && basePriceCriteria1Code.equalsIgnoreCase(tempCriteriaCode)) {
     * if (PriceGridParser.isOptionGroup(tempCriteriaCode)) {
     * if (priceGridParser.isOptionCriteriaAlreadyConfigured(
     * priceCriteriaAry[priceTypeCntr], optionGroupCriteriaSet)) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId()
     * .trim(), ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid Upcharge Price Criteria. Base Price Criteria ("
     * + basePriceCriteria1Code + ") and Upcharge criteria ("
     * + tempCriteriaCode + ") should be unique by value for Options");
     * continue;
     * }
     * } else {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Base Price Criteria ("
     * + basePriceCriteria1Code + ") and Upcharge criteria ("
     * + upchargeCriteria1Code + ") cannot be same");
     * continue;
     * }
     * }
     * tempPriceGrids.setIsBasePrice("false");
     * if (null == pricingSubTypeCodeWSResponse) {
     * pricingSubTypeCodeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
     * .get(ApplicationConstants.PRICING_SUBTYPECODE_LOOKUP));
     * }
     * String pricingSubTypeCodeValue = jsonProcessorObj.checkPricingCode(
     * pricingSubTypeCodeWSResponse, pricingSubTypeCode[priceTypeCntr]);
     * tempPriceGrids.setPriceGridSubTypeCode(pricingSubTypeCodeValue);
     * if (null == pricingUsageLevelCodeWSResponse) {
     * pricingUsageLevelCodeWSResponse = orgnCall.getLookupsResponse(RestAPIProperties
     * .get(ApplicationConstants.PRICING_USAGELEVEL_LOOKUP));
     * }
     * String pricingUsageLevelValue = jsonProcessorObj.checkPricingCode(
     * pricingUsageLevelCodeWSResponse, pricingUsageLevel[priceTypeCntr]);
     * tempPriceGrids.setUsageLevelCode(pricingUsageLevelValue);
     * 
     * // priceCriteriaAry[priceTypeCntr] will be in this format<criteriaCode>:<criteriaCodevalues
     * // separated by ,>
     * 
     * String priceGridDescription = "";
     * List<PricingItems> pricingItemsList = new ArrayList<PricingItems>();
     * try {
     * 
     * pricingItemsList = priceGridParser.generatePricingItemsForCriteria(
     * priceCriteriaAry[priceTypeCntr], product, tempPriceGrids,
     * upchargeCriteria1Code, false);
     * if (!pricingItemsList.isEmpty()) {
     * priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
     * upchargeCriteria1Code, priceCriteriaAry[priceTypeCntr],
     * upchargeCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
     * }
     * }
     * 
     * if (!CommonUtilities.isValueNull(priceCriteriaAry[priceTypeCntr])
     * && pricingItemsList.isEmpty()) {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid UpCharge Criteria 1 " + priceCriteriaAry[priceTypeCntr]);
     * continue;
     * }
     * } catch (AmbiguousPriceCriteriaException apre) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
     * continue;
     * }
     * if (pricingItemsList != null && !pricingItemsList.isEmpty()
     * && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * priceGridDescription = PriceGridParser.getValidCriteriaValues();
     * }
     * 
     * List<PricingItems> pricingItemList2 = new ArrayList<PricingItems>();
     * ;
     * try {
     * 
     * pricingItemList2 = priceGridParser.generatePricingItemsForCriteria(
     * priceCriteria2Ary[priceTypeCntr], product, tempPriceGrids,
     * upchargeCriteria2Code, false);
     * if (!pricingItemList2.isEmpty()) {
     * priceCriteriaValuesList.add(PriceGridParser.getPriceCriteriaCombinationString(
     * upchargeCriteria1Code, priceCriteriaAry[priceTypeCntr],
     * upchargeCriteria2Code, priceCriteria2Ary[priceTypeCntr]));
     * }
     * }
     * if (!CommonUtilities.isValueNull(priceCriteria2Ary[priceTypeCntr])
     * && pricingItemsList.isEmpty()) {
     * productDataStore.addErrorToBatchLogCollection(
     * product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
     * "Invalid UpCharge Criteria 2 " + priceCriteria2Ary[priceTypeCntr]);
     * continue;
     * }
     * } catch (AmbiguousPriceCriteriaException apre) {
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, apre.getMessage());
     * continue;
     * }
     * 
     * if (pricingItemList2 != null && !pricingItemList2.isEmpty()
     * && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * priceGridDescription = priceGridDescription.isEmpty() ? PriceGridParser
     * .getValidCriteriaValues() : priceGridDescription + ", "
     * + PriceGridParser.getValidCriteriaValues();
     * }
     * 
     * pricingItemsList.addAll(pricingItemList2);
     * if (!CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * tempPriceGrids.setDescription(basePrices[priceTypeCntr]);
     * } else if (!CommonUtilities.isValueNull(priceGridDescription)
     * && !pricingItemsList.isEmpty()) {
     * tempPriceGrids.setDescription(priceGridDescription);
     * } else if (!pricingItemsList.isEmpty() && CommonUtilities.isValueNull(priceGridDescription)
     * && CommonUtilities.isValueNull(basePrices[priceTypeCntr])) {
     * tempPriceGrids.setDescription(product.getName());
     * }
     * 
     * if (!pricingItemsList.isEmpty()) {
     * tempPriceGrids.setPricingItems(pricingItemsList.toArray(new PricingItems[0]));
     * } else {
     * tempPriceGrids.setPricingItems(new PricingItems[] {});
     * }
     * }
     * if (tempPriceGrids != null && CommonUtilities.isValueNull(tempPriceGrids.getDescription())) {
     * tempPriceGrids.setDescription(product.getName());
     * }
     * }
     * if (tempPriceGrids != null) {
     * isValidPGFound = true;
     * }
     * if (processPrices)
     * priceGridsLst[priceTypeCntr] = tempPriceGrids;
     * else
     * priceGridsLst = new PriceGrids[] {};
     * }
     * 
     * if (processPrices && null != priceGridsLst[1])
     * priceGridsLst[1].setIsBasePrice(ApplicationConstants.CONST_STRING_FALSE_SMALL);
     * } catch (Exception e) {
     * LOGGER.error("PriceGrid processing failed", e);
     * String message = String
     * .format("Exception while processing PriceGrid for Product %s with Product External ProductId %s, ErrorCode  = %s",
     * product.getName(), product.getExternalProductId(),
     * VelocityImportExceptionCodes.PG_EXCEPTION_UNKOWN);
     * productDataStore.addErrorToBatchLogCollection(product.getExternalProductId().trim(),
     * ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR, message);
     * }
     * List<PriceGrids> filterdPriceGrids = new ArrayList<PriceGrids>();
     * int priceGridSequence = 0;
     * for (int priceGridcntr = 0; priceGridcntr < priceGridsLst.length; priceGridcntr++) {
     * if (priceGridsLst[priceGridcntr] != null) {
     * priceGridsLst[priceGridcntr].setDisplaySequence(++priceGridSequence + "");
     * filterdPriceGrids.add(priceGridsLst[priceGridcntr]);
     * }
     * }
     * 
     * if (isSimplifiedTemplate && existingProduct != null) {
     * filterdPriceGrids.addAll(priceGridParser.getUpchargePrices(existingProduct.getPriceGrids(),
     * criteriaSetValueIds));
     * }
     * if (filterdPriceGrids != null && filterdPriceGrids.size() > 0) {
     * priceGridsLst = filterdPriceGrids.toArray(new PriceGrids[0]);
     * } else {
     * priceGridsLst = new PriceGrids[] {};
     * }
     * 
     * LOGGER.info("Prices Transformation End");
     * }
     * 
     * // Comparing Current PriceGrid with the existing priceGrid
     * 
     * if (existingProduct != null && existingProduct.getPriceGrids() != null
     * && existingProduct.getPriceGrids().length > 0 && priceGridsLst != null && priceGridsLst.length > 0) {
     * PriceGrids[] existingPriceGrids = existingProduct.getPriceGrids();
     * // PriceGrids newPriceGrid = new PriceGrids();
     * 
     * for (int existingPGCount = 0; existingPGCount < existingPriceGrids.length; existingPGCount++) {
     * PriceGrids[] currentPriceGrids = priceGridsLst;
     * PriceGrids existingPGrid = existingPriceGrids[existingPGCount];
     * for (int currentPGCount = 0; currentPGCount < currentPriceGrids.length; currentPGCount++) {
     * PriceGrids currentPGrid = currentPriceGrids[currentPGCount];
     * if (isSimplifiedTemplate
     * && currentPGrid.getIsBasePrice().equalsIgnoreCase(
     * ApplicationConstants.CONST_STRING_FALSE_SMALL)) {
     * priceGridsLst[currentPGCount] = currentPGrid;
     * continue;
     * } else if (existingPGrid.getIsBasePrice().equalsIgnoreCase(currentPGrid.getIsBasePrice())
     * && existingPGrid.getIsQUR().equalsIgnoreCase(currentPGrid.getIsQUR())
     * && existingPGrid.getDescription().equalsIgnoreCase(currentPGrid.getDescription())) {
     * // BasePriceGrid Manipulation
     * 
     * currentPGrid.setId(existingPGrid.getId());
     * currentPGrid.setDisplaySequence(existingPGrid.getDisplaySequence());
     * currentPGrid.setProductId(existingPGrid.getProductId());
     * // Set more elements if needed
     * // Currency Manipulation
     * if (currentPGrid.getCurrency() != null
     * && currentPGrid.getCurrency().getCode()
     * .equalsIgnoreCase(existingPGrid.getCurrency().getCode())) {
     * currentPGrid.setCurrency(existingPGrid.getCurrency());
     * }
     * // Price[] Manipulation
     * if (currentPGrid.getPrices() != null && currentPGrid.getPrices().length > 0
     * && existingPGrid.getPrices() != null && existingPGrid.getPrices().length > 0) {
     * Prices[] curPrices = currentPGrid.getPrices();
     * Prices[] extPrices = existingPGrid.getPrices();
     * // Now we need to iterate over each price and compare with existing one
     * for (int extPriceCount = 0; extPriceCount < extPrices.length; extPriceCount++) {
     * 
     * for (int curPriceCount = 0; curPriceCount < curPrices.length; curPriceCount++) {
     * 
     * if (PriceGridParser.isPricesEqual(extPrices[extPriceCount],
     * curPrices[curPriceCount])) {
     * curPrices[curPriceCount] = extPrices[extPriceCount];
     * break;
     * }
     * }
     * }
     * 
     * for (int tempCount = 0; tempCount < curPrices.length; tempCount++) {
     * curPrices[tempCount].setPriceGridId(currentPGrid.getId());
     * }
     * currentPGrid.setPrices(curPrices);
     * // Now check Pricing Items
     * if (currentPGrid.getPricingItems() != null && currentPGrid.getPricingItems().length > 0
     * && existingPGrid.getPricingItems() != null
     * && existingPGrid.getPricingItems().length > 0) {
     * PricingItems[] curPricingItems = currentPGrid.getPricingItems();
     * PricingItems[] exPricingItems = existingPGrid.getPricingItems();
     * 
     * for (int curPRICount = 0; curPRICount < curPricingItems.length; curPRICount++) {
     * PricingItems curPricingItem = curPricingItems[curPRICount];
     * currentPGrid.getPricingItems()[curPRICount].setPriceGridId(currentPGrid.getId());
     * for (int extPRICount = 0; extPRICount < exPricingItems.length; extPRICount++) {
     * PricingItems exPricingItem = exPricingItems[extPRICount];
     * if (curPricingItem != null
     * && exPricingItem != null
     * && curPricingItem.getCriteriaSetValueId().equalsIgnoreCase(
     * exPricingItem.getCriteriaSetValueId())) {
     * currentPGrid.getPricingItems()[curPRICount] = exPricingItem;
     * break;
     * }
     * }
     * }
     * } else if (currentPGrid.getPricingItems() != null
     * && (existingPGrid.getPricingItems() == null || existingPGrid.getPricingItems().length == 0)) {
     * for (int i = 0; i < currentPGrid.getPricingItems().length; i++) {
     * currentPGrid.getPricingItems()[i].setPriceGridId(currentPGrid.getId());
     * }
     * }
     * 
     * }
     * currentPGrid.setId(existingPGrid.getId());
     * priceGridsLst[currentPGCount] = currentPGrid;
     * break;
     * } else if (existingPGrid.getIsBasePrice().equalsIgnoreCase(currentPGrid.getIsBasePrice())
     * && !Boolean.valueOf(existingPGrid.getIsBasePrice())
     * && existingPGrid.getIsQUR().equalsIgnoreCase(currentPGrid.getIsQUR())
     * && Boolean.valueOf(existingPGrid.getIsQUR())
     * && existingPGrid.getDescription().equalsIgnoreCase(currentPGrid.getDescription())) {
     * // UpChargePriceGrid Manipulation
     * 
     * }
     * }
     * 
     * }
     * 
     * }
     * 
     * if (!isNewProduct) {
     * existingProduct.setPriceGrids(priceGridsLst);
     * } else {
     * product.setPriceGrids(priceGridsLst);
     * }
     * // Price Grid Ends
     * 
     * // Product Numbers Starts Here
     * try {
     * ProductNumberCriteriaParser productNumberCriteriaParser = new ProductNumberCriteriaParser();
     * if (isSimplifiedPG && existingProduct != null) {
     * if (null != existingProduct.getProductNumbers() && !existingProduct.getProductNumbers().isEmpty()) {
     * List<ProductNumbers> productNumbers = productNumberCriteriaParser.getValidProductNumbers(
     * existingProduct.getProductNumbers(), criteriaSetValueIds, isSimplifiedPG);
     * product.setProductNumbers(productNumbers);
     * 
     * } else {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * } else if (!isSimplifiedPG && existingProduct != null) {
     * product.setProductNumbers(productNumberCriteriaParser.generateProductNumberCriterias(product,
     * existingProduct));
     * if (null != existingProduct.getProductNumbers() && !existingProduct.getProductNumbers().isEmpty()) {
     * List<ProductNumbers> productNumbers = productNumberCriteriaParser.getValidProductNumbers(
     * existingProduct.getProductNumbers(), criteriaSetValueIds, isSimplifiedPG);
     * product.setProductNumbers(productNumbers);
     * 
     * } else {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * } else {
     * product.setProductNumbers(productNumberCriteriaParser.generateProductNumberCriterias(product,
     * existingProduct));
     * if (existingProduct != null && existingProduct.getProductNumbers() != null
     * && !existingProduct.getProductNumbers().isEmpty()) {
     * List<ProductNumbers> productNumbers = productNumberCriteriaParser.getValidProductNumbers(
     * existingProduct.getProductNumbers(), criteriaSetValueIds, isSimplifiedPG);
     * product.setProductNumbers(productNumbers);
     * 
     * } else {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * // product.setProductNumbers(new ArrayList<ProductNumbers>());
     * }
     * } catch (Exception ex) {
     * product.setProductNumbers(new ArrayList<ProductNumbers>());
     * LOGGER.error("Exception occured in Product Number Criteria processing : " + ex.getMessage());
     * }
     * 
     * // Product Numbers Ends
     * 
     * // RushTime Flag && Same_Day_Rush Processing
     * if (!CommonUtilities.isValueNull(product.getRushServiceFlag())
     * && CommonUtilities.isValueYesOrNo(product.getRushServiceFlag())) {
     * product.setRushServiceFlag(String.valueOf(product.getRushServiceFlag().charAt(0)));
     * } else {
     * product.setRushServiceFlag(null);
     * }
     * 
     * product.getProductConfigurations()[0].setConfigId(configId);
     * if (existingProduct != null && existingProduct.getProductConfigurations() != null
     * && existingProduct.getProductConfigurations().length > 0) {
     * product.getProductConfigurations()[0].setId(existingProduct.getProductConfigurations()[0].getId());
     * }
     * product.getProductConfigurations()[0].setProductCriteriaSets(productCriteriaSetsAry);
     * 
     * // Catalog Name
     * if (existingProduct != null && existingProduct.getProductMediaCitations() != null) {
     * product.setProductMediaCitations(existingProduct.getProductMediaCitations());
     * } else {
     * product.setProductMediaCitations(new ProductMediaCitations[] {});
     * }
     * 
     * // Media Items Start here
     * ProductMediaItems[] productMediaItems = product.getProductMediaItems();
     * String mediaUrl = productMediaItems[0].getMedia().getUrl();
     * if (isNewProduct) {
     * product.setProductMediaItems(productImageProcessor.getProductMediaItems(mediaUrl, product.getCompanyId(),
     * product.getId(), existingProduct));
     * } else {
     * existingProduct.setProductMediaItems(productImageProcessor.getProductMediaItems(mediaUrl,
     * existingProduct.getCompanyId(), existingProduct.getId(), existingProduct));
     * }
     * // Media Items ends here
     * LOGGER.info("Processing InventoryLink and ProductDataSheet");
     * if (isNewProduct) {
     * product.setProductInventoryLink(ProductCompareUtil.compareAndUpdateInventoryLink(product, existingProduct));
     * product.setProductDataSheet(ProductCompareUtil.compareAndUpdateProductDatasheet(product, existingProduct));
     * } else {
     * existingProduct.setProductInventoryLink(ProductCompareUtil.compareAndUpdateInventoryLink(product,
     * existingProduct));
     * existingProduct.setProductDataSheet(ProductCompareUtil.compareAndUpdateProductDatasheet(product,
     * existingProduct));
     * }
     * LOGGER.info("Completed Processing InventoryLink and ProductDataSheet");
     * 
     * LOGGER.info("Compliance Certs Transformation Starts :"
     * + product.getSelectedComplianceCerts()[0].getComplianceCertId());
     * 
     * if (isNewProduct) {
     * product.setSelectedComplianceCerts(complianceCertProcessor.getSelectedComplianceCerts(
     * product.getSelectedComplianceCerts()[0].getComplianceCertId(), product.getCompanyId(),
     * product.getId(), existingProduct));
     * } else {
     * existingProduct.setSelectedComplianceCerts(complianceCertProcessor.getSelectedComplianceCerts(
     * product.getSelectedComplianceCerts()[0].getComplianceCertId(), product.getCompanyId(),
     * product.getId(), existingProduct));
     * }
     * 
     * LOGGER.info("Compliance Certs Transformation Completed ");
     * 
     * 
     * LOGGER.info("Started Processing product keywords");
     * if (isNewProduct) {
     * product.setProductKeywords(ProductCompareUtil.comapreAndUpdateKeywords(product.getProductKeywords(),
     * existingProduct));
     * } else {
     * existingProduct.setProductKeywords(ProductCompareUtil.comapreAndUpdateKeywords(
     * product.getProductKeywords(), existingProduct));
     * }
     * 
     * LOGGER.info("Completed product keywords");
     * 
     * LOGGER.info("Started Processing Product Categories");
     * if (isNewProduct) {
     * product.setSelectedProductCategories(ProductCompareUtil.compareAndUpdateCategories(
     * product.getSelectedProductCategories(), product, existingProduct));
     * } else {
     * existingProduct.setSelectedProductCategories(ProductCompareUtil.compareAndUpdateCategories(
     * product.getSelectedProductCategories(), product, existingProduct));
     * }
     * LOGGER.info("Completed Processing Product Categories");
     * 
     * 
     * if (isNewProduct) {
     * currentMsg = JsonProcessor.convertBeanToJson(product);
     * } else {
     * currentMsg = JsonProcessor.convertBeanToJson(existingProduct);
     * }
     * LOGGER.info("Product At Str: " + currentMsg);
     * // Removing elements from reference table which are belongs to this externalProductId from reference table
     * productDataStore.removeEntryFromCriteriaReferenceTable(product.getExternalProductId().trim());
     * // muleMessage.setProperty("XID", product.getExternalProductId(), PropertyScope.SESSION); // DO NOT REMOVE THIS
     * // muleMessage.setPayload(currentMsg);
     * // muleMessage.setPayload(currentMsg);
     * // muleMessage.setPayload(NullPayload.getInstance());
     * 
     * String numberOfRecordFailedToProcess = String.valueOf(muleMessage.getProperty("recordFailedToProcess",
     * PropertyScope.SESSION));
     * muleMessage.setProperty("recordFailedToProcess",
     * String.valueOf(Integer.parseInt(numberOfRecordFailedToProcess) - 1), PropertyScope.SESSION);
     * 
     * 
     * }
     * 
     * } // Xid check
     * else {
     * if (null == product.getExternalProductId()) {
     * LOGGER.warn("External Id Should not be Null ");
     * batchErrorLogs += "$batch:" + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":External Id is required field";
     * } else if (product.getExternalProductId().equals("")) {
     * LOGGER.info("External Id Should not be Empty ");
     * batchErrorLogs += "$batch:" + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":External Id is required field";
     * }
     * 
     * if (null == product.getName()) {
     * LOGGER.info("Product Name Should not be Null ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product_Name is a required field";
     * } else if (product.getName().equals("")) {
     * LOGGER.info("Product Name Should not be Empty ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product_Name is a required field";
     * }
     * if (null == product.getDescription()) {
     * LOGGER.info(product.getName() + " Product Description Should not be Null ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product Description is a required field";
     * } else if (product.getDescription().equals("")) {
     * LOGGER.info(product.getName() + " Product Description Should not be Empty ");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product Description is a required field";
     * }
     * 
     * if (product.getCompanyId() == null || product.getCompanyId().isEmpty()) {
     * LOGGER.info(product.getName() + "Product Company Id not be null");
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":"
     * + ApplicationConstants.CONST_BATCH_ERR_REQ_FIELD + ":Product Company Id not be null";
     * }
     * LOGGER.info("External Id is Empty for the Product Name " + product.getName() + ", Product Number - "
     * + product.getId());
     * // Removing elements from reference table which are belongs to this externalProductId from reference table
     * productDataStore.removeEntryFromCriteriaReferenceTable(product.getExternalProductId().trim());
     * 
     * batchErrorLogs = ProductDataStore.updateBatchError(product.getExternalProductId(), batchErrorLogs);
     * // muleMessage.setPayload(null);
     * }
     * } catch (VelocityException ve) {
     * LOGGER.error("Velocity Exception occured while processing product", ve);
     * // from GLOBAL_BATCH_LOG_COLLECTION.remove(product.getExternalProductId().trim());
     * // Removing elements from reference table which are belongs to this externalProductId from reference table
     * productDataStore.removeEntryFromCriteriaReferenceTable(product.getExternalProductId().trim());
     * 
     * 
     * if (ve.getExceptionType() != null && ExceptionType.INTERNAL_SERVER_ERROR.equals(ve.getExceptionType())) {
     * String errors = muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION) !=
     * ApplicationConstants.CONST_STRING_NULL_SMALL ? ""
     * : muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION).toString();
     * errors += "$ERR-CODE-500:Ext-PRD-" + product.getExternalProductId() + ":" + ve.getMessage();
     * 
     * muleMessage.setProperty("INT_SER_ERR", errors, PropertyScope.SESSION);
     * } else {
     * String errors = muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION) !=
     * ApplicationConstants.CONST_STRING_NULL_SMALL ? ""
     * : muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION).toString();
     * 
     * errors += "$ERR-CODE-500:Ext-PRD-" + product.getExternalProductId() + ":" + ve.getMessage();
     * muleMessage.setProperty("INT_SER_ERR", errors, PropertyScope.SESSION);
     * }
     * batchErrorLogs += "$Ext-" + product.getExternalProductId() + ":" + ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR
     * + ":" + ve.getMessage();
     * batchErrorLogs = ProductDataStore.updateBatchError(product.getExternalProductId(), batchErrorLogs);
     * muleMessage.setPayload(null);
     * 
     * } catch (Exception ex) {
     * LOGGER.error("Exception occured while processing product", ex);
     * 
     * // Removing elements from reference table which are belongs to this externalProductId from reference table
     * productDataStore.removeEntryFromCriteriaReferenceTable(product.getExternalProductId().trim());
     * 
     * batchErrorLogs = ProductDataStore.updateBatchError(product.getExternalProductId(), batchErrorLogs);
     * batchErrorLogs += "$error:" + ApplicationConstants.CONST_BATCH_ERR_GENERIC_PLHDR + ":" + ex.getMessage();
     * 
     * String errors = muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION) !=
     * ApplicationConstants.CONST_STRING_NULL_SMALL ? ""
     * : muleMessage.getProperty("INT_SER_ERR", PropertyScope.SESSION).toString();
     * errors += "$ERROR:" + ex.getMessage();
     * muleMessage.setProperty("INT_SER_ERR", errors, PropertyScope.SESSION);
     * muleMessage.setPayload(null);
     * 
     * }
     * 
     * batchErrorLogs = ProductDataStore.updateBatchError(product.getExternalProductId(), batchErrorLogs);
     * // muleMessage.setProperty("batchErrorLog", batchErrorLogs, PropertyScope.SESSION);
     * 
     * return muleMessage;
     * }
     */
    // function for temporary processing , will be removed once null processing completed
    public static String correctxsStringData(String value) {
        if (value != null) {
            return value.replaceAll(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR, "null");
        }
        return value;
    }
}
