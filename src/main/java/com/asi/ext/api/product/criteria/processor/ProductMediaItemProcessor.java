package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.radar.model.Media;
import com.asi.ext.api.radar.model.MediaCriteriaMatches;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductMediaItems;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;

public class ProductMediaItemProcessor {

    private final static Logger LOGGER = Logger.getLogger(ProductMediaItemProcessor.class.getName());
    private final static String IMAGE_SERVER_URL    =   "http://media.asicdn.com/images/jpgb/";

    public ProductMediaItems[] getProductMediaItems(String mediaItems, String companyId, String productId, Product existingProduct) {
        LOGGER.info("Started processing product images");

        if (CommonUtilities.isValueNull(mediaItems)) {
            return new ProductMediaItems[0];
        } else if (!CommonUtilities.isUpdateNeeded(mediaItems)) {
            return existingProduct != null ? existingProduct.getProductMediaItems() : new ProductMediaItems[0];
        }

        boolean checkExisting = (existingProduct != null && existingProduct.getProductMediaItems() != null && existingProduct
                .getProductMediaItems().length > 0);

        String[] finalImages = getMedialUrls(mediaItems);
        List<ProductMediaItems> processedImages = new ArrayList<ProductMediaItems>();
        Map<String, ProductMediaItems> existingMediaMap = null;

        if (checkExisting) {
            existingMediaMap = getExistingMediaItems(existingProduct.getProductMediaItems());
        }
        int mediaId = 0;
        boolean isPrimaryImageFound = false;
        for (String image : finalImages) {
            ProductMediaItems productMediaItems = null;
            if (checkExisting) {
                productMediaItems = existingMediaMap.get(image.trim().toUpperCase());
            }
            if (productMediaItems == null) {
                productMediaItems = new ProductMediaItems();
                productMediaItems.setProductId(productId);
                productMediaItems.setMediaRank(String.valueOf(processedImages.size() + 1));
                productMediaItems.setMediaId(String.valueOf(mediaId--));
                productMediaItems.setIsPrimary(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                productMediaItems.setMedia(createNewMedia(image, companyId, productMediaItems.getMediaId()));
            }
            if (String.valueOf(productMediaItems.getIsPrimary()).equalsIgnoreCase(ApplicationConstants.CONST_STRING_TRUE_SMALL)) {
                isPrimaryImageFound = true;
            }
            productMediaItems.setMediaRank(String.valueOf(processedImages.size() + 1));
            processedImages.add(productMediaItems);
        }
        if (!isPrimaryImageFound && !processedImages.isEmpty()) {
            processedImages.get(0).setIsPrimary(ApplicationConstants.CONST_STRING_TRUE_SMALL);
        }
        LOGGER.info("Completed processing product images");
        return processedImages.toArray(new ProductMediaItems[0]);
    }

    private Media createNewMedia(String url, String companyId, String mediaId) {
        Media media = new Media();

        media.setId(mediaId);
        media.setCompanyID(companyId);
        media.setUrl(url);
        media.setDescription("");
        media.setMediaTypeCode(ApplicationConstants.CONST_MEDIA_TYPE_CODE);
        media.setImageQualityCode(ApplicationConstants.CONST_IMAGE_QUALITY_CODE);
        media.setMediaCriteriaMatches(new MediaCriteriaMatches[0]);

        return media;
    }

    private Map<String, ProductMediaItems> getExistingMediaItems(ProductMediaItems[] productMediaItems) {
        Map<String, ProductMediaItems> existing = new HashMap<String, ProductMediaItems>();
        for (ProductMediaItems media : productMediaItems) {
            if (media.getMedia() != null && media.getMedia().getUrl() != null) {
                existing.put(String.valueOf(media.getMedia().getUrl()).toUpperCase(), media);
                //Generate alternative entry for same media
                existing.put(String.valueOf(getAlternateUrlForMedia(media)).toUpperCase(), media);
            }
        }
        return existing;
    }

    private String[] getMedialUrls(String values) {
        try {
            List<String> tempList = new ArrayList<String>();

            for (String temp : values.split(ApplicationConstants.CONST_STRING_COMMA_SEP)) {
                tempList.add(String.valueOf(temp).trim());
            }
            return tempList.toArray(new String[0]);
        } catch (Exception e) {
            return values.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
        }
    }
    
    private String getAlternateUrlForMedia(ProductMediaItems mediaItem) {
        String alternativeUrl = IMAGE_SERVER_URL + computeMediaPath(mediaItem.getMediaId());
        alternativeUrl += "/" + String.valueOf(mediaItem.getMediaId()) + ".jpg";        
        return alternativeUrl;
    }

    private static String computeMediaPath(String mediaId) {
        try {
            if (mediaId.length() > 4) {
                String temp = mediaId.substring(0, mediaId.length()-4);
                return temp != null ? temp + "0000" : mediaId;
            } else {
                return mediaId;
            }
        } catch (Exception e) {
            LOGGER.error("Exception while computing the alternative URL for comparison : Media ID : "+mediaId, e);
            return mediaId;
        }
    }
    
}
