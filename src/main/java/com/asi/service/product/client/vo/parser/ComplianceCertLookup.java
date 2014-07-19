package com.asi.service.product.client.vo.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.asi.core.utils.JerseyClient;
import com.asi.ext.api.integration.lookup.parser.ComplianceCertParser;
import com.asi.service.product.client.vo.SelectedComplianceCert;



public class ComplianceCertLookup {
    private final static Logger _LOGGER = Logger.getLogger(ComplianceCertLookup.class.getName());

    private static ConcurrentHashMap<String, String> complianceCertMap = new ConcurrentHashMap<>();
    private String                                   complianceCertAPI;

    private ComplianceCertParser                     complianceCertparser;
    
    public String getComplianceCertString(List<SelectedComplianceCert> complianceCerts) {
        String complianceCertFinal = "";
        if (complianceCerts != null && !complianceCerts.isEmpty()) {
            for (SelectedComplianceCert compCert : complianceCerts) {
                if (compCert != null) {
                    if (compCert.getComplianceCertId().equals(-1)) { // Other value
                        complianceCertFinal += "," + compCert.getDescription();
                    } else {
                        String temp = getComplianceCertByCode(compCert.getComplianceCertId()+"");
                        if (temp != null) {
                            complianceCertFinal += "," + temp;                            
                        }
                    }
                }
            }
        } else {
            return complianceCertFinal;
        }
        
        if (complianceCertFinal.startsWith(",")) {
            complianceCertFinal = complianceCertFinal.substring(1);
        }
        return complianceCertFinal;
    }
    
    public String getComplianceCertByCode(String code) {
        return getComplianceName(code);
    }

    private String getComplianceName(String key) {
        
        if (complianceCertMap == null || complianceCertMap.isEmpty()) {
            // Initialize map for once
            loadComplianceCerts();
            if (complianceCertMap == null) {
                return null;
            }
        }
        
        return complianceCertMap.get(key);
    }

    private synchronized void loadComplianceCerts() {
        try {
            String complianceCertResponse = JerseyClient.invoke(new URI(complianceCertAPI));
            
            if (complianceCertResponse != null) {
                complianceCertMap = complianceCertparser.createComplianceCertMap(complianceCertResponse);
            } else {
                _LOGGER.error("Unable to process compliance and cert reponse \n"+complianceCertResponse);
            }
        } catch (NullPointerException | URISyntaxException e) {
            _LOGGER.error("Exception while processing compliance and cert", e);
        } catch (Exception e) {
            _LOGGER.error("Exception while processing compliance and cert", e);
        }
    }

    /**
     * @return the complianceCertAPI
     */
    public String getComplianceCertAPI() {
        return complianceCertAPI;
    }

    /**
     * @param complianceCertAPI
     *            the complianceCertAPI to set
     */
    @Required
    public void setComplianceCertAPI(String complianceCertAPI) {
        this.complianceCertAPI = complianceCertAPI;
    }

    /**
     * @return the complianceCertparser
     */
    public ComplianceCertParser getComplianceCertparser() {
        return complianceCertparser;
    }

    /**
     * @param complianceCertparser
     *            the complianceCertparser to set
     */
    public void setComplianceCertparser(ComplianceCertParser complianceCertparser) {
        this.complianceCertparser = complianceCertparser;
    }

}