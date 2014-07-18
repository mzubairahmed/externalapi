package com.asi.ext.api.tools;

import java.util.List;

import com.asi.ext.api.radar.model.CriteriaSetCodeValues;
import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Media;
import com.asi.ext.api.radar.model.PriceGrids;
import com.asi.ext.api.radar.model.Prices;
import com.asi.ext.api.radar.model.PricingItems;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductConfigurations;
import com.asi.ext.api.radar.model.ProductCriteriaSets;
import com.asi.ext.api.radar.model.ProductDataSheet;
import com.asi.ext.api.radar.model.ProductInventoryLink;
import com.asi.ext.api.radar.model.ProductKeywords;
import com.asi.ext.api.radar.model.ProductMediaItems;
import com.asi.ext.api.radar.model.SelectedComplianceCerts;
import com.asi.ext.api.radar.model.SelectedProductCategories;
import com.asi.ext.api.radar.model.SelectedSafetyWarnings;
import com.asi.ext.api.radar.model.SetCodeValues;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

public class ProductConverter {

    private final String COMPANY_ID    = "9207";
    private final String DATASOURCE_ID = "14236";

    public Product constructRadarProductModel(com.asi.ext.api.service.model.Product serviceProduct) {

        // Set Basic properties
        Product finalProduct = new Product();
        finalProduct.setId(ID);
        finalProduct.setCompanyId(COMPANY_ID);
        finalProduct.setDataSourceId(DATASOURCE_ID);
        finalProduct.setName(serviceProduct.getName());
        finalProduct.setExternalProductId(serviceProduct.getExternalProductId());
        finalProduct.setAsiProdNo(serviceProduct.getAsiProdNo());
        finalProduct.setDescription(serviceProduct.getDescription());
        finalProduct.setSummary(serviceProduct.getSummary());
        finalProduct.setShipperBillsByCode(serviceProduct.getShipperBillsBy());
        // Object Types
        finalProduct.setProductInventoryLink(getProductInventoryLink(serviceProduct.getProductInventoryLink()));
        finalProduct.setProductDataSheet(getProductDataSheet(serviceProduct.getProductDataSheet()));
        // List Elements
        finalProduct.setProductKeywords(getProductKeywords(serviceProduct.getProductKeywords()));
        finalProduct.setSelectedProductCategories(getProductCategories(serviceProduct.getCategories()));
        finalProduct.setSelectedComplianceCerts(getProductComplianceCerts(serviceProduct.getComplianceCerts()));
        finalProduct.setSelectedSafetyWarnings(getSelectedSafetyWarnings(serviceProduct.getSafetyWarnings()));

        
        // Temporary Product preparations
        ProductConfigurations configurations =  new ProductConfigurations();
        configurations.setProductId(PRODUCT_ID);
        configurations.setIsDefault("true");
        
        ProductCriteriaSets criteriaSets = new ProductCriteriaSets();
        CriteriaSetValues criteriaSetValue = new CriteriaSetValues();
        CriteriaSetCodeValues criteriaSetCodeValues = new CriteriaSetCodeValues();
        
        criteriaSetValue.setCriteriaSetCodeValues(new CriteriaSetCodeValues[]{criteriaSetCodeValues});
        criteriaSets.setCriteriaSetValues(new CriteriaSetValues[]{criteriaSetValue});
        
        PriceGrids priceGrids = new PriceGrids();
        Prices price = new Prices();
        priceGrids.setPrices(new Prices[]{price});
        PricingItems pricingItems = new PricingItems();
        priceGrids.setPricingItems(new PricingItems[]{pricingItems});
                
        finalProduct.setPriceGrids(new PriceGrids[]{priceGrids});
        
        ProductMediaItems mediaItems = new ProductMediaItems();
        mediaItems.setMedia(new Media());
        finalProduct.setProductMediaItems(new ProductMediaItems[]{mediaItems});
        /*product.getProductConfigurations()[0].getProductCriteriaSets()[0]
                .getCriteriaSetValues()[0].getCriteriaSetCodeValues()[0].getSetCodeValueId();*/
        
        configurations.setProductCriteriaSets(new ProductCriteriaSets[]{criteriaSets});
        finalProduct.setProductConfigurations(new ProductConfigurations[]{configurations});
        /*
         * finalProduct.
         * finalProduct.
         */

        return finalProduct;
    }

    public com.asi.ext.api.service.model.Product constructServiceProductModel(Product radarProdModel) {
        return null;
    }

    private final String PRODUCT_ID = ApplicationConstants.CONST_STRING_ZERO;
    private final String ID         = ApplicationConstants.CONST_STRING_ZERO;

    private ProductInventoryLink getProductInventoryLink(String inventoryLink) {
        ProductInventoryLink productInventoryLink = new ProductInventoryLink();
        productInventoryLink.setUrl(inventoryLink);
        productInventoryLink.setProductId(PRODUCT_ID);
        productInventoryLink.setId(ID);

        return productInventoryLink;
    }

    private ProductDataSheet getProductDataSheet(String productDataSheet) {
        ProductDataSheet prodDataSheet = new ProductDataSheet();
        prodDataSheet.setUrl(productDataSheet);
        prodDataSheet.setProductId(PRODUCT_ID);
        prodDataSheet.setId(ID);

        return prodDataSheet;
    }

    private ProductKeywords[] getProductKeywords(List<String> productKeywords) {
        String keywords = CommonUtilities.convertStringListToCSV(productKeywords);
        if (!CommonUtilities.isValueNull(keywords)) {
            ProductKeywords keyword = new ProductKeywords();
            keyword.setId(ID);
            keyword.setProductId(PRODUCT_ID);
            keyword.setValue(keywords);
            return new ProductKeywords[] { keyword };
        } else {
            return new ProductKeywords[] {};
        }
    }

    private SelectedProductCategories[] getProductCategories(List<String> productCategories) {
        String categories = CommonUtilities.convertStringListToCSV(productCategories);
        if (!CommonUtilities.isValueNull(categories)) {
            SelectedProductCategories category = new SelectedProductCategories();
            category.setCode(categories);
            category.setProductId(PRODUCT_ID);
            return new SelectedProductCategories[] { category };
        } else {
            return new SelectedProductCategories[] {};
        }
    }

    private SelectedComplianceCerts[] getProductComplianceCerts(List<String> productComplianceCerts) {
        String complianceCerts = CommonUtilities.convertStringListToCSV(productComplianceCerts);
        if (!CommonUtilities.isValueNull(complianceCerts)) {
            SelectedComplianceCerts selectedCompliance = new SelectedComplianceCerts();
            selectedCompliance.setId(ID);
            selectedCompliance.setProductId(PRODUCT_ID);
            selectedCompliance.setComplianceCertId(complianceCerts);
            return new SelectedComplianceCerts[] { selectedCompliance };
        } else {
            return new SelectedComplianceCerts[] {};
        }
    }

    private SelectedSafetyWarnings[] getSelectedSafetyWarnings(List<String> selectedSafetyWarnings) {
        String safetyWarnings = CommonUtilities.convertStringListToCSV(selectedSafetyWarnings);
        if (!CommonUtilities.isValueNull(safetyWarnings)) {
            SelectedSafetyWarnings safetyWarning = new SelectedSafetyWarnings();
            safetyWarning.setCode(safetyWarnings);
            return new SelectedSafetyWarnings[] { safetyWarning };
        } else {
            return new SelectedSafetyWarnings[] {};
        }
    }

}
