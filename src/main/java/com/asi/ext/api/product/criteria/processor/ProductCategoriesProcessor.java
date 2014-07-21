/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.SelectedProductCategories;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

/**
 * @author Rahul K
 * 
 */
public class ProductCategoriesProcessor {

    List<SelectedProductCategories> adCategories = new ArrayList<SelectedProductCategories>();

    public SelectedProductCategories[] getCategories(List<String> productCategoryList, String productId, String xid,
            Product existingProduct) {
        String crntProductCategory = null;
        if (productCategoryList != null && !productCategoryList.isEmpty()) {
            crntProductCategory = CommonUtilities.convertStringListToCSV(productCategoryList);
        }
        SelectedProductCategories[] productCategories = getProductCategories(crntProductCategory, productId, xid, existingProduct);

        if (productCategories == null || productCategories.length == 0) {
            return new SelectedProductCategories[] {};
        }
        return productCategories;
    }

    private SelectedProductCategories[] getProductCategories(String categories, String productId, String externalPrdId,
            Product existingProduct) {
        if (!CommonUtilities.isUpdateNeeded(categories)) {
            if (existingProduct == null) {
                return new SelectedProductCategories[0];
            } else {
                return existingProduct.getSelectedProductCategories();
            }
        }
        ProductDataStore productDataStore = new ProductDataStore();

        Map<String, SelectedProductCategories> existingCategoriesMap = null;
        if (existingProduct != null) {
            existingCategoriesMap = getExistingProductCategories(existingProduct.getSelectedProductCategories());
        }
        boolean checkExisting = false;

        if (existingCategoriesMap != null && !existingCategoriesMap.isEmpty()) {
            checkExisting = true;
        }

        List<SelectedProductCategories> finalCategories = new ArrayList<SelectedProductCategories>();
        finalCategories.addAll(adCategories);

        for (String categ : categories.split(",")) {
            if (CommonUtilities.isValueNull(categ)) {
                continue;
            }
            String categCode = ProductDataStore.findCategoryCode(categ.trim());
            if (categCode != null) {
                SelectedProductCategories category = null;
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

        return finalCategories.toArray(new SelectedProductCategories[0]);
    }

    private SelectedProductCategories createNewCategory(String code, String productId) {

        SelectedProductCategories newCategory = new SelectedProductCategories();
        newCategory.setCode(code);
        newCategory.setIsPrimary(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        newCategory.setAdCategoryFlg(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        newCategory.setProductId(productId);

        return newCategory;
    }

    private Map<String, SelectedProductCategories> getExistingProductCategories(SelectedProductCategories[] categories) {
        Map<String, SelectedProductCategories> categoryMap = new HashMap<String, SelectedProductCategories>();
        for (SelectedProductCategories ctg : categories) {
            if (ctg != null) {
                if (ApplicationConstants.CONST_STRING_TRUE_SMALL.equalsIgnoreCase(ctg.getAdCategoryFlg())) {
                    adCategories.add(ctg);
                } else {
                    categoryMap.put(ctg.getCode(), ctg);
                }
            }
        }
        return categoryMap;
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