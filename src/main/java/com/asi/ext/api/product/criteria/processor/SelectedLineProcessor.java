/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.SelectedLineNames;

/**
 * @author krahul
 * 
 */
public class SelectedLineProcessor {

    private final static Logger LOGGER           = Logger.getLogger(SelectedLineProcessor.class.getName());

    private ProductDataStore    productDataStore = new ProductDataStore();

    public List<SelectedLineNames> getSelectedLines(List<String> selectedLines, ProductDetail product, String authToken) {
        LOGGER.info("Started processing selected line names");
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Selected names to process : " + selectedLines);
        }
        Map<String, SelectedLineNames> extLineNames = getExistingLineNames(product.getSelectedLineNames());
        List<SelectedLineNames> finalLineNames = new ArrayList<SelectedLineNames>();
        for (String lineName : selectedLines) {
            String lineId = getLineId(lineName, authToken);

            if (lineId != null) {
                SelectedLineNames sLineName = extLineNames.get(lineId);
                if (sLineName == null) {
                    sLineName = getSelectedLineName(lineName, lineId, product.getID(), product.getCompanyId());
                }
                finalLineNames.add(sLineName);
            } else {
                productDataStore.addErrorToBatchLogCollection(product.getExternalProductId(),
                        ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE, "Given selected line name " + lineName
                                + " doesn't exist in lookup");
            }
        }
        LOGGER.info("Completed processing selected line names");
        return finalLineNames;
    }

    private Map<String, SelectedLineNames> getExistingLineNames(List<SelectedLineNames> lineNames) {
        if (lineNames == null || lineNames.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, SelectedLineNames> existingLineNames = new HashMap<String, SelectedLineNames>(lineNames.size());
        for (SelectedLineNames line : lineNames) {
            existingLineNames.put(line.getId(), line);
        }
        return existingLineNames;
    }

    private SelectedLineNames getSelectedLineName(String selectedLine, String lineId, String productId, String companyId) {
        SelectedLineNames lineName = new SelectedLineNames();
        lineName.setId(lineId);
        lineName.setMarketSegmentCode(ApplicationConstants.CONST_MARKET_SEGMENT_CODE);
        lineName.setProductId(productId);
        lineName.setName(selectedLine);

        return lineName;
    }

    private String getLineId(String selectedLine, String authToken) {
        return ProductDataStore.getSetCodeValueIdForSelectedLineName(selectedLine, authToken);
    }
}
