package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;

import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.SelectedComplianceCert;

public class ProductSelectedComplianceCertProcessor {

    private final static Logger LOGGER = Logger.getLogger(ProductSelectedComplianceCertProcessor.class.getName());

    public List<SelectedComplianceCert> getSelectedComplianceCertList(List<String> complianceCertList, String companyId, String productId,
            ProductDetail existingProduct) {
        String complianceCertFinalString = null;
        if (complianceCertList != null && !complianceCertList.isEmpty()) {
            complianceCertFinalString = CommonUtilities.convertStringListToCSV(complianceCertList);
        }
        
        return getSelectedComplianceCerts(complianceCertFinalString, companyId, productId, existingProduct);
    }
    private List<SelectedComplianceCert> getSelectedComplianceCerts(String complianceCerts, String companyId, String productId,
            ProductDetail existingProduct) {

        if (CommonUtilities.isValueNull(complianceCerts)) {
            return new ArrayList<>();
        } else if (!CommonUtilities.isUpdateNeeded(complianceCerts)) {
            if (existingProduct != null) {
                return existingProduct.getSelectedComplianceCerts();
            } else {
                return new ArrayList<>();
            }
        }
        // Create / Update Compliance Cert
        
        Map<String, SelectedComplianceCert> existingComplianceCertMap = null;
        boolean checkForExisting = false;
        if (existingProduct != null && existingProduct.getSelectedComplianceCerts() != null 
                && existingProduct.getSelectedComplianceCerts().size() > 0) {
            existingComplianceCertMap = getExistingMap(existingProduct.getSelectedComplianceCerts());
            checkForExisting = (existingComplianceCertMap != null && !existingComplianceCertMap.isEmpty()); 
        }
        
        List<SelectedComplianceCert> finalComplianceCertsList = new ArrayList<SelectedComplianceCert>();
        String[] finalValues = CommonUtilities.getCSVValues(complianceCerts);
        for (String compCert : finalValues) {
            String complianceId = getComplianceCertId(compCert);
            SelectedComplianceCert selectedComplianceCerts = null;
            if (checkForExisting) {
                if (complianceId == "-1") {
                    selectedComplianceCerts = existingComplianceCertMap.get(complianceId + "_" + compCert.trim());
                } else {
                    selectedComplianceCerts = existingComplianceCertMap.get(complianceId);
                }
            }
            if (selectedComplianceCerts == null) { // create a new one
                selectedComplianceCerts= createNewComplianceCert(companyId, productId, complianceId, compCert);
            }
            
            finalComplianceCertsList.add(selectedComplianceCerts);
        }
        
        LOGGER.info("Compliance Certs Transformation Completed :" + complianceCerts);
        return finalComplianceCertsList;
    }
    
    
    private SelectedComplianceCert createNewComplianceCert(String companyId, String productId, String complianceCertId, String description) {
        
        SelectedComplianceCert compCert = new SelectedComplianceCert();
        compCert.setCompanyId(companyId);
        compCert.setProductId(productId);
        compCert.setDescription(description);
        compCert.setComplianceCertId(complianceCertId);
        compCert.setID("0");
        
        return compCert;
    }
    
    private Map<String, SelectedComplianceCert> getExistingMap(List<SelectedComplianceCert> existingCerts) {
        Map<String, SelectedComplianceCert> existingMap = new HashMap<String, SelectedComplianceCert>();
        for (SelectedComplianceCert compCert : existingCerts) {
            if ("-1".equalsIgnoreCase(compCert.getComplianceCertId())) {
                existingMap.put(compCert.getComplianceCertId() + "_"+compCert.getDescription() != null ? compCert.getDescription().trim() : "", compCert);
            } else {
                existingMap.put(compCert.getComplianceCertId(), compCert);
            }
        }
        return existingMap;
    }
    
    private String getComplianceCertId(String compliance) {
        return ProductDataStore.getComplianceCertId(compliance.trim());
    }

}