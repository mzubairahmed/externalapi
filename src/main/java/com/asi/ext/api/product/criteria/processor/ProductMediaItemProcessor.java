package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.service.model.Configurations;
import com.asi.ext.api.service.model.Image;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.product.client.vo.Media;
import com.asi.service.product.client.vo.MediaCriteriaMatches;
import com.asi.service.product.client.vo.ProductMediaItems;

public class ProductMediaItemProcessor {

    private final static Logger LOGGER           = Logger.getLogger(ProductMediaItemProcessor.class.getName());
    private final static String IMAGE_SERVER_URL = "http://media.asicdn.com/images/jpgb/";

    public List<ProductMediaItems> getProductImages(List<Image> imagesToProcess, String companyId, String productId,
            List<ProductMediaItems> existingMediaItems,String externalProductId) {
        if (imagesToProcess != null && !imagesToProcess.isEmpty()) {
            return getProductMediaItems(imagesToProcess, companyId, productId, existingMediaItems,externalProductId);
        } else {
            return new ArrayList<ProductMediaItems>();
        }
    }

    private List<ProductMediaItems> getProductMediaItems(List<Image> imagesToProcess, String companyId, String productId,
            List<ProductMediaItems> existingMediaItems,String externalProductId) {
        LOGGER.info("Started processing product images");

        boolean checkExisting = (existingMediaItems != null && !existingMediaItems.isEmpty());

        // String[] finalImages = getMedialUrls(imagesToProcess);
        List<ProductMediaItems> processedImages = new ArrayList<ProductMediaItems>();
        Map<String, ProductMediaItems> existingMediaMap = null;

        if (checkExisting) {
            existingMediaMap = getExistingMediaItems(existingMediaItems);
        }
        int mediaId = 0;
        boolean isPrimaryImageFound = false;
        for (Image image : imagesToProcess) {
            ProductMediaItems productMediaItems = null;
            if (checkExisting) {
                productMediaItems = existingMediaMap.get(String.valueOf(image.getImageURL()).trim().toUpperCase());
            }
            if (productMediaItems == null) {
                productMediaItems = new ProductMediaItems();
                productMediaItems.setProductId(productId);
                productMediaItems.setMediaRank(image.getRank());
                productMediaItems.setMediaId(String.valueOf(mediaId--));
                productMediaItems.setIsPrimary(image.getIsPrimary());
                productMediaItems.setMedia(createNewMedia(image, companyId, productMediaItems.getMediaId(),externalProductId));
            } else {
                productMediaItems.setIsPrimary(image.getIsPrimary());
                productMediaItems.setMediaRank(image.getRank());
            }
            if (productMediaItems.getIsPrimary()) {
                isPrimaryImageFound = true;
            }
            // productMediaItems.setMediaRank(String.valueOf(processedImages.size() + 1));
            processedImages.add(productMediaItems);
        }
        if (!isPrimaryImageFound && !processedImages.isEmpty()) {
            processedImages.get(0).setIsPrimary(true);
        }
        LOGGER.info("Completed processing product images");
        return processedImages;
    }

    private Media createNewMedia(Image img, String companyId, String mediaId,String externalProductId) {
        Media media = new Media();
        MediaCriteriaMatches[] mediaCriteriaMatchesAry={};
      
        CriteriaInfo tempCriteriaInfo=new CriteriaInfo();
        MediaCriteriaMatches currentMediaCriteriaMatches=null;
        media.setId(mediaId);
        media.setCompanyID(companyId);
        media.setUrl(img.getImageURL());
        media.setDescription("");
        media.setMediaTypeCode(ApplicationConstants.CONST_MEDIA_TYPE_CODE);
        media.setImageQualityCode(ApplicationConstants.CONST_IMAGE_QUALITY_CODE);
        //media.setMediaCriteriaMatches(new MediaCriteriaMatches[0]);
        mediaCriteriaMatchesAry=new MediaCriteriaMatches[img.getConfigurations().size()];
        int configId=0;
        for(Configurations currentConfig:img.getConfigurations()){
        	currentMediaCriteriaMatches=new MediaCriteriaMatches();
        	tempCriteriaInfo=ProductDataStore.getCriteriaInfoByDescription(currentConfig.getCriteria());
        	currentMediaCriteriaMatches.setCriteriaSetValueId(ProductDataStore.findCriteriaSetValueIdForValue(externalProductId,tempCriteriaInfo.getCode(),currentConfig.getValue().toString()));
        	currentMediaCriteriaMatches.setMediaId(mediaId);
        	mediaCriteriaMatchesAry[configId]=currentMediaCriteriaMatches;
        	configId++;
        }
        media.setMediaCriteriaMatches(mediaCriteriaMatchesAry);
        return media;
    }

    private Map<String, ProductMediaItems> getExistingMediaItems(List<ProductMediaItems> productMediaItems) {
        Map<String, ProductMediaItems> existing = new HashMap<String, ProductMediaItems>();
        for (ProductMediaItems media : productMediaItems) {
            if (media.getMedia() != null && media.getMedia().getUrl() != null) {
                existing.put(String.valueOf(media.getMedia().getUrl()).toUpperCase(), media);
                // Generate alternative entry for same media
                existing.put(String.valueOf(getAlternateUrlForMedia(media)).toUpperCase(), media);
            }
        }
        return existing;
    }

    /*
     * private String[] getMedialUrls(String values) {
     * try {
     * List<String> tempList = new ArrayList<String>();
     * 
     * for (String temp : values.split(ApplicationConstants.CONST_STRING_COMMA_SEP)) {
     * tempList.add(String.valueOf(temp).trim());
     * }
     * return tempList.toArray(new String[0]);
     * } catch (Exception e) {
     * return values.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
     * }
     * }
     */

    private String getAlternateUrlForMedia(ProductMediaItems mediaItem) {
        String alternativeUrl = IMAGE_SERVER_URL + computeMediaPath(mediaItem.getMediaId());
        alternativeUrl += "/" + String.valueOf(mediaItem.getMediaId()) + ".jpg";
        return alternativeUrl;
    }

    private static String computeMediaPath(String mediaId) {
        try {
            if (mediaId.length() > 4) {
                String temp = mediaId.substring(0, mediaId.length() - 4);
                return temp != null ? temp + "0000" : mediaId;
            } else {
                return mediaId;
            }
        } catch (Exception e) {
            LOGGER.error("Exception while computing the alternative URL for comparison : Media ID : " + mediaId, e);
            return mediaId;
        }
    }

}
