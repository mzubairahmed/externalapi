/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.SelectedProductCategory;

/**
 * @author Rahul K
 * 
 */
public class ProductCategoriesProcessor {

    List<SelectedProductCategory> adCategories = new ArrayList<SelectedProductCategory>();

    public List<SelectedProductCategory> getCategories(List<String> productCategoryList, String productId, String xid,
            ProductDetail existingProduct) {
        String crntProductCategory = null;
        if (productCategoryList != null && !productCategoryList.isEmpty()) {
            crntProductCategory = CommonUtilities.convertStringListToCSV(productCategoryList);
        } else {
            return (existingProduct != null && !existingProduct.getSelectedProductCategories().isEmpty()) ? getAdCategories(existingProduct
                    .getSelectedProductCategories()) : new ArrayList<SelectedProductCategory>();
        }
        List<SelectedProductCategory> productCategories = getProductCategories(crntProductCategory, productId, xid, existingProduct);

        if (productCategories == null || productCategories.size() == 0) {
            return new ArrayList<SelectedProductCategory>();
        }
        return productCategories;
    }

    private List<SelectedProductCategory> getProductCategories(String categories, String productId, String externalPrdId,
            ProductDetail existingProduct) {
        if (!CommonUtilities.isUpdateNeeded(categories)) {
            if (existingProduct == null) {
                return new ArrayList<SelectedProductCategory>();
            } else {
                return existingProduct.getSelectedProductCategories();
            }
        }
        ProductDataStore productDataStore = new ProductDataStore();

        Map<String, SelectedProductCategory> existingCategoriesMap = null;
        if (existingProduct != null) {
            existingCategoriesMap = getExistingProductCategories(existingProduct.getSelectedProductCategories());
        }
        boolean checkExisting = false;

        if (existingCategoriesMap != null && !existingCategoriesMap.isEmpty()) {
            checkExisting = true;
        }

        List<SelectedProductCategory> finalCategories = new ArrayList<SelectedProductCategory>();
        finalCategories.addAll(adCategories);

        for (String categ : categories.split(",")) {
            if (CommonUtilities.isValueNull(categ)) {
                continue;
            }
            String categCode = ProductDataStore.findCategoryCode(categ.trim());
            if (categCode != null) {
                SelectedProductCategory category = null;
                if (checkExisting) {
                    category = existingCategoriesMap.get(categCode.toUpperCase());
                }
                if (category == null) { // Existing table not found data, create new one
                    category = createNewCategory(categCode, productId);
                }

                finalCategories.add(category);

            } else {
                // LOG BATCH Invalid Category
                productDataStore.addErrorToBatchLogCollection(externalPrdId,
                        ApplicationConstants.CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST,
                        "Invalid product category specified, category " + categ);
            }
        }

        return finalCategories;
    }

    private SelectedProductCategory createNewCategory(String code, String productId) {

        SelectedProductCategory newCategory = new SelectedProductCategory();
        newCategory.setCode(code);
        newCategory.setIsPrimary(false);
        newCategory.setAdCategoryFlg(false);
        newCategory.setProductId(productId);

        return newCategory;
    }

    private Map<String, SelectedProductCategory> getExistingProductCategories(List<SelectedProductCategory> categories) {
        Map<String, SelectedProductCategory> categoryMap = new HashMap<String, SelectedProductCategory>();
        for (SelectedProductCategory ctg : categories) {
            if (ctg != null) {
                if (ctg.getAdCategoryFlg()) {
                    adCategories.add(ctg);
                } else {
                    categoryMap.put(ctg.getCode(), ctg);
                }
            }
        }
        return categoryMap;
    }

    private List<SelectedProductCategory> getAdCategories(List<SelectedProductCategory> categories) {
        adCategories = new ArrayList<SelectedProductCategory>();
        if (categories == null) {
            return adCategories;
        }
        for (SelectedProductCategory ctg : categories) {
            if (ctg != null) {
                if (ctg.getAdCategoryFlg()) {
                    adCategories.add(ctg);
                }
            }
        }
        return adCategories;
    }

    private String getCategoryCode(String category) {
        if (category != null) {
            category = category.trim();
        } else {
            return null;
        }
        return ProductDataStore.findCategoryCode(category);
    }
}