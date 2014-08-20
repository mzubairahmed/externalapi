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

    private ProductDataStore    productDatastore = new ProductDataStore();

    public List<ProductMediaItems> getProductImages(List<Image> imagesToProcess, String companyId, String productId,
            List<ProductMediaItems> existingMediaItems, String externalProductId) {
        if (imagesToProcess != null && !imagesToProcess.isEmpty()) {
            return getProductMediaItems(imagesToProcess, companyId, productId, existingMediaItems, externalProductId);
        } else {
            return new ArrayList<ProductMediaItems>();
        }
    }

    private List<ProductMediaItems> getProductMediaItems(List<Image> imagesToProcess, String companyId, String productId,
            List<ProductMediaItems> existingMediaItems, String externalProductId) {
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
                productMediaItems.setMedia(createNewMedia(image, companyId, productMediaItems.getMediaId(), externalProductId));
            } else {
                productMediaItems.setIsPrimary(image.getIsPrimary());
                productMediaItems.setMediaRank(image.getRank());
                productMediaItems.setMedia(comapreAndUpdateMediaCriteriaMatches(productMediaItems.getMedia(), image, externalProductId));
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

    private Media comapreAndUpdateMediaCriteriaMatches(Media media, Image serImage, String xid) {
        List<Configurations> mediaConfigs = serImage.getConfigurations();

        if (mediaConfigs != null && !mediaConfigs.isEmpty()) {
            List<MediaCriteriaMatches> finalMediaCriteriaMatches = new ArrayList<MediaCriteriaMatches>();

            Map<String, MediaCriteriaMatches> existingMediaCriteriaMap = new HashMap<String, MediaCriteriaMatches>();
            if (media.getMediaCriteriaMatches() != null && media.getMediaCriteriaMatches().length > 0) {
                for (MediaCriteriaMatches medMatche : media.getMediaCriteriaMatches()) {
                    existingMediaCriteriaMap.put(medMatche.getCriteriaSetValueId(), medMatche);
                }
            }
            CriteriaInfo criteriaInfo = null;

            for (Configurations config : mediaConfigs) {
                
                criteriaInfo = ProductDataStore.getCriteriaInfoByDescription(config.getCriteria(), xid);
                if (criteriaInfo != null) {
                    String criteriaSetValueId = ProductDataStore.findCriteriaSetValueIdForValue(xid, criteriaInfo.getCode(),
                            String.valueOf(config.getValue()));
                    if (criteriaSetValueId != null) {
                        MediaCriteriaMatches currentMediaCriteriaMatch = existingMediaCriteriaMap.get(criteriaSetValueId);
                        if (currentMediaCriteriaMatch == null) {
                            currentMediaCriteriaMatch = new MediaCriteriaMatches();
                            currentMediaCriteriaMatch.setCriteriaSetValueId(criteriaSetValueId);
                            currentMediaCriteriaMatch.setMediaId(media.getId());
                        }
                        finalMediaCriteriaMatches.add(currentMediaCriteriaMatch);
                    } else {
                        productDatastore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                "Criteria value specified for Media is not exist in product, Criteria : " + config.getCriteria()
                                        + "Value : " + String.valueOf(config.getValue()));
                    }
                } else {
                    productDatastore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                            "Criteria specified for Media is not valid, Criteria : " + config.getCriteria());
                }
            }
            media.setMediaCriteriaMatches(finalMediaCriteriaMatches.toArray(new MediaCriteriaMatches[0]));
        } else {
            media.setMediaCriteriaMatches(new MediaCriteriaMatches[] {});
        }
        return media;
    }

    private Media createNewMedia(Image img, String companyId, String mediaId, String externalProductId) {
        Media media = new Media();
        List<MediaCriteriaMatches> mediaCriteriaMatchesList = new ArrayList<MediaCriteriaMatches>();

        CriteriaInfo tempCriteriaInfo = new CriteriaInfo();
        media.setId(mediaId);
        media.setCompanyID(companyId);
        media.setUrl(img.getImageURL());
        media.setDescription("");
        media.setMediaTypeCode(ApplicationConstants.CONST_MEDIA_TYPE_CODE);
        media.setImageQualityCode(ApplicationConstants.CONST_IMAGE_QUALITY_CODE);
        // media.setMediaCriteriaMatches(new MediaCriteriaMatches[0]);
        if (img.getConfigurations() != null && !img.getConfigurations().isEmpty()) {
            for (Configurations currentConfig : img.getConfigurations()) {
                MediaCriteriaMatches currentMediaCriteriaMatches = new MediaCriteriaMatches();
                tempCriteriaInfo = ProductDataStore.getCriteriaInfoByDescription(currentConfig.getCriteria(), externalProductId);
                if (tempCriteriaInfo != null) {
                    String criteriaSetValueId = ProductDataStore.findCriteriaSetValueIdForValue(externalProductId,
                            tempCriteriaInfo.getCode(), String.valueOf(currentConfig.getValue()));
                    if (criteriaSetValueId != null) {
                        currentMediaCriteriaMatches.setCriteriaSetValueId(criteriaSetValueId);
                        currentMediaCriteriaMatches.setMediaId(mediaId);
                        mediaCriteriaMatchesList.add(currentMediaCriteriaMatches);
                    } else {
                        productDatastore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                                "Criteria value specified for Media is not exist in product, Criteria : " + currentConfig.getCriteria()
                                        + "Value : " + String.valueOf(currentConfig.getValue()));
                    }
                } else {
                    productDatastore.addErrorToBatchLogCollection(externalProductId, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                            "Criteria specified for Media is not valid, Criteria : " + currentConfig.getCriteria());
                }
            }
            media.setMediaCriteriaMatches(mediaCriteriaMatchesList.toArray(new MediaCriteriaMatches[0]));
        }
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
