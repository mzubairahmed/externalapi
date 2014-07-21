package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.SelectedSafetyWarnings;

public class ProductSelectedSafetyWarningProcessor {

    private final static Logger LOGGER = Logger.getLogger(ProductSelectedSafetyWarningProcessor.class.getName());

    public List<SelectedSafetyWarnings> getSafetyWarnings(List<String> safetyWarnings, String xid, String productId,
            ProductDetail existingProduct) {
        String finalSafetyWarning = CommonUtilities.convertStringListToCSV(safetyWarnings);

        return getSelectedSafetyWarnings(finalSafetyWarning, xid, productId, existingProduct);
    }

    private List<SelectedSafetyWarnings> getSelectedSafetyWarnings(String safetyWarnings, String externalProductId,
            String productId, ProductDetail existingProduct) {

        LOGGER.info("Started ProductSelectedSafetyWarningProcessor.getSelectedSafetyWarnings(), SFW " + safetyWarnings);

        boolean checkExisting = false;

        if (CommonUtilities.isValueNull(safetyWarnings)) {
            return new ArrayList<>();
        } else if (CommonUtilities.isUpdateNeeded(safetyWarnings)) {
            if (existingProduct != null && existingProduct.getSelectedSafetyWarnings() != null
                    && existingProduct.getSelectedSafetyWarnings().size() > 0) {
                checkExisting = true;
            }
        } else if (!CommonUtilities.isUpdateNeeded(safetyWarnings)) {
            return existingProduct != null ? existingProduct.getSelectedSafetyWarnings() : new ArrayList<SelectedSafetyWarnings>();
        }

        Map<String, SelectedSafetyWarnings> existingMap = null;

        if (checkExisting) {
            existingMap = getExistingSafetyWarningMap(existingProduct.getSelectedSafetyWarnings());
        }
        ProductDataStore productDataStore = new ProductDataStore();
        String[] finalSafetyWarnings = getValues(safetyWarnings);
        List<SelectedSafetyWarnings> processedSftWarning = new ArrayList<SelectedSafetyWarnings>();

        for (String warning : finalSafetyWarnings) {
            SelectedSafetyWarnings selectedSafetyWarning = null;
            String code = getSafetyWarningCode(warning);
            if (code == null) {
                productDataStore.addErrorToBatchLogCollection(externalProductId,
                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Invalid Safety warning " + warning);
                continue;
            }
            if (checkExisting) {
                selectedSafetyWarning = existingMap.get(code);
            }
            if (selectedSafetyWarning == null) {
                selectedSafetyWarning = createNewSafetyWarning(productId, code);
            }

            processedSftWarning.add(selectedSafetyWarning);
        }

        LOGGER.info("Completed ProductSelectedSafetyWarningProcessor.getSelectedSafetyWarnings(), SFW " + safetyWarnings);
        return processedSftWarning;
    }

    private String getSafetyWarningCode(String warning) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Processing ProductSelectedSafetyWarningProcessor.getSafetyWarningCode(), SFW " + warning);
        }
        return ProductDataStore.getSelectedSafetyWarningCode(warning.trim());
    }

    private SelectedSafetyWarnings createNewSafetyWarning(String productId, String safetyWarningCode) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Started ProductSelectedSafetyWarningProcessor.createNewSafetyWarning(), SFCode " + safetyWarningCode);
        }
        SelectedSafetyWarnings warning = new SelectedSafetyWarnings();

        warning.setCode(safetyWarningCode);
        warning.setMarketSegmentCode(ApplicationConstants.CONST_MARKET_SEGMENT_CODE);
        warning.setProductId(productId);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Completed ProductSelectedSafetyWarningProcessor.createNewSafetyWarning(), SFCode " + warning);
        }
        return warning;
    }

    private Map<String, SelectedSafetyWarnings> getExistingSafetyWarningMap(List<SelectedSafetyWarnings> existingSafetyWarnings) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Started ProductSelectedSafetyWarningProcessor.getExistingSafetyWarningMap(), " + existingSafetyWarnings);
        }

        Map<String, SelectedSafetyWarnings> existing = new HashMap<String, SelectedSafetyWarnings>();

        for (SelectedSafetyWarnings warning : existingSafetyWarnings) {
            try {
                existing.put(warning.getCode().trim(), warning);
            } catch (Exception e) {
                LOGGER.error("Exception while processing SelectedSaftly warning : " + warning, e);
            }
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Completed ProductSelectedSafetyWarningProcessor.getExistingSafetyWarningMap(), " + existing);
        }
        return existing;
    }

    private String[] getValues(String safetyValue) {
        return CommonUtilities.filterDuplicates(safetyValue.split(ApplicationConstants.CONST_STRING_COMMA_SEP));
    }
}
