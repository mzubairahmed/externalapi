/**
 * 
 */
package com.asi.ext.api.util;

import java.util.List;

import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductConfigurations;
import com.asi.ext.api.radar.model.ProductDataSheet;
import com.asi.ext.api.radar.model.ProductInventoryLink;
import com.asi.ext.api.radar.model.ProductKeywords;
import com.asi.ext.api.radar.model.SelectedComplianceCerts;
import com.asi.ext.api.radar.model.SelectedProductCategories;
import com.asi.ext.api.radar.model.SelectedSafetyWarnings;

/**
 * @author krahul
 * 
 */
public final class ProductParserUtil {

    private final static String PRODUCT_ID = ApplicationConstants.CONST_STRING_ZERO;
    private final static String ID         = ApplicationConstants.CONST_STRING_ZERO;

    public final static String getConfigId(ProductConfigurations[] productConfigurations) {
        if (productConfigurations != null && productConfigurations.length > 0) {
            return productConfigurations[0].getId();
        } else {
            return ApplicationConstants.CONST_STRING_ZERO;
        }
    }

    public static ProductInventoryLink getInventoryLink(String inventoryLink, Product existingProduct, String companyId) {
        ProductInventoryLink productInventoryLink = null;
        if (existingProduct != null) {
            productInventoryLink = existingProduct.getProductInventoryLink() != null ? existingProduct.getProductInventoryLink()
                    : new ProductInventoryLink();
            productInventoryLink.setUrl(inventoryLink);
            productInventoryLink.setProductId(existingProduct.getId());
            productInventoryLink.setCompanyId(companyId);
        } else {
            productInventoryLink = new ProductInventoryLink();
            productInventoryLink.setUrl(inventoryLink);
            productInventoryLink.setProductId(PRODUCT_ID);
            productInventoryLink.setId(ID);
            productInventoryLink.setCompanyId(companyId);
        }

        return productInventoryLink;
    }

    public static ProductDataSheet getDataSheet(String productDataSheet, Product existingProduct, String companyId) {
        ProductDataSheet prodDataSheet = new ProductDataSheet();
        if (existingProduct != null) {
            prodDataSheet = existingProduct.getProductDataSheet() != null ? existingProduct.getProductDataSheet()
                    : new ProductDataSheet();
            prodDataSheet.setUrl(productDataSheet);
            prodDataSheet.setProductId(existingProduct.getId());
            prodDataSheet.setCompanyId(companyId);
        } else {
            prodDataSheet = new ProductDataSheet();
            prodDataSheet.setUrl(productDataSheet);
            prodDataSheet.setProductId(PRODUCT_ID);
            prodDataSheet.setId(ID);
            prodDataSheet.setCompanyId(companyId);
        }
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
