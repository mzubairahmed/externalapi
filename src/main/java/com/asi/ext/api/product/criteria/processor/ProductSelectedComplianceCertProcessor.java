package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.SelectedComplianceCerts;
import com.asi.ext.api.util.CommonUtilities;

public class ProductSelectedComplianceCertProcessor {

    private final static Logger LOGGER = Logger.getLogger(ProductSelectedComplianceCertProcessor.class.getName());

    public SelectedComplianceCerts[] getSelectedComplianceCerts(String complianceCerts, String companyId, String productId,
            Product existingProduct) {

        if (complianceCerts == null) {
            return new SelectedComplianceCerts[0];
        } else if (!CommonUtilities.isUpdateNeeded(complianceCerts)) {
            if (existingProduct != null) {
                return existingProduct.getSelectedComplianceCerts();
            } else {
                return new SelectedComplianceCerts[0];
            }
        }
        // Create / Update Compliance Cert
        
        Map<String, SelectedComplianceCerts> existingComplianceCertMap = null;
        boolean checkForExisting = false;
        if (existingProduct != null && existingProduct.getSelectedComplianceCerts() != null 
                && existingProduct.getSelectedComplianceCerts().length > 0) {
            existingComplianceCertMap = getExistingMap(existingProduct.getSelectedComplianceCerts());
            checkForExisting = (existingComplianceCertMap != null && !existingComplianceCertMap.isEmpty()); 
        }
        
        List<SelectedComplianceCerts> finalComplianceCertsList = new ArrayList<SelectedComplianceCerts>();
        String[] finalValues = CommonUtilities.getCSVValues(complianceCerts);
        for (String compCert : finalValues) {
            String complianceId = getComplianceCertId(compCert);
            SelectedComplianceCerts selectedComplianceCerts = null;
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
        return finalComplianceCertsList.toArray(new SelectedComplianceCerts[0]);
    }
    
    
    private SelectedComplianceCerts createNewComplianceCert(String companyId, String productId, String complianceCertId, String description) {
        
        SelectedComplianceCerts compCert = new SelectedComplianceCerts();
        compCert.setCompanyId(companyId);
        compCert.setProductId(productId);
        compCert.setDescription(description);
        compCert.setComplianceCertId(complianceCertId);
        
        return compCert;
    }
    
    private Map<String, SelectedComplianceCerts> getExistingMap(SelectedComplianceCerts[] existingCerts) {
        Map<String, SelectedComplianceCerts> existingMap = new HashMap<String, SelectedComplianceCerts>();
        for (SelectedComplianceCerts compCert : existingCerts) {
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