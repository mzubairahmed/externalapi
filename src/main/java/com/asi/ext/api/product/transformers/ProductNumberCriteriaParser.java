package com.asi.ext.api.product.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.service.model.Configurations;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.util.ProductParserUtil;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.ProductNumber;
import com.asi.service.product.client.vo.ProductNumberConfiguration;

public class ProductNumberCriteriaParser {
    private final static Logger LOGGER           = Logger.getLogger(ProductNumberCriteriaParser.class.getName());
    public ProductDataStore     productDataStore = new ProductDataStore();
    private static Integer      pnoConfig        = -1;

    public List<ProductNumber> generateProductNumbers(List<com.asi.ext.api.service.model.ProductNumber> serviceProductNumbers,
            ProductDetail product) {
        int pnoId = -1;
        List<ProductNumber> productNumbers = new ArrayList<ProductNumber>();
        // Load existing map
        Map<String, ProductNumber> extPnoMap = getExistingPnoMap(product.getProductNumbers());

        for (com.asi.ext.api.service.model.ProductNumber serPno : serviceProductNumbers) {
            if (serPno != null) {
                ProductNumber pNumber = extPnoMap.get(serPno.getProductNumber());
                if (pNumber == null) {
                    pNumber = new ProductNumber();
                    ;
                    pNumber.setId(--pnoId);
                }

                pNumber.setProductNumberConfigurations(getProductNumberConfiguration(serPno.getConfigurations(), pNumber,
                        product.getExternalProductId()));

                if (pNumber.getProductNumberConfigurations() != null && !pNumber.getProductNumberConfigurations().isEmpty()) {
                    pNumber.setProductId(product.getID());
                    pNumber.setValue(serPno.getProductNumber());
                    productNumbers.add(pNumber);
                }
            }
        }

        return productNumbers;
    }

    private List<ProductNumberConfiguration> getProductNumberConfiguration(List<Configurations> serPnoConfigs,
            ProductNumber productNumber, String xid) {
        List<ProductNumberConfiguration> pnoConfigurations = new ArrayList<ProductNumberConfiguration>();

        for (Configurations config : serPnoConfigs) {

            String criteriaCode = ProductParserUtil.getCriteriaCodeFromCriteria(config.getCriteria(), xid);
            if (criteriaCode != null) {
                Integer criteriaSetValueId = ProductParserUtil.getCriteriaSetValueId(xid, criteriaCode, config.getValue());
                if (criteriaSetValueId != null) {

                    ProductNumberConfiguration pnoConfig = getMatchingConfiguration(criteriaSetValueId,
                            productNumber.getProductNumberConfigurations());
                    if (pnoConfig == null) {
                        pnoConfig = new ProductNumberConfiguration();
                        pnoConfig.setProductNumberId(String.valueOf(productNumber.getId()));
                        pnoConfig.setId(--ProductNumberCriteriaParser.pnoConfig);
                        pnoConfig.setCriteriaSetValueId(criteriaSetValueId);
                    }
                    pnoConfigurations.add(pnoConfig);
                } else {
                    // TODO : Add Errors
                }
            }
        }
        return pnoConfigurations;
    }

    private ProductNumberConfiguration getMatchingConfiguration(Integer criteriaSetValueId,
            List<ProductNumberConfiguration> configurations) {
        if (configurations != null && !configurations.isEmpty()) {
            for (ProductNumberConfiguration config : configurations) {
                if (config.getCriteriaSetValueId().equals(criteriaSetValueId)) {
                    return config;
                }
            }
        }
        return null;
    }

    private Map<String, ProductNumber> getExistingPnoMap(List<ProductNumber> extPNumbers) {
        if (extPNumbers == null || extPNumbers.isEmpty()) {
            return new HashMap<String, ProductNumber>(0);
        }

        Map<String, ProductNumber> existingMap = new HashMap<String, ProductNumber>(extPNumbers.size());
        for (ProductNumber pNumber : extPNumbers) {
            existingMap.put(pNumber.getValue(), pNumber);
        }
        return existingMap;
    }

}
