package com.asi.ext.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.radar.lookup.model.PriceUnitJsonModel;
import com.asi.ext.api.radar.model.CriteriaInfo;
import com.asi.ext.api.response.JsonProcessor;
import com.asi.ext.api.service.model.Catalog;
import com.asi.service.product.client.vo.Currency;
import com.asi.service.product.client.vo.DiscountRate;
import com.asi.service.product.client.vo.MediaCitationReference;
import com.asi.service.product.client.vo.ProductMediaCitationReferences;
import com.asi.service.product.client.vo.ProductMediaCitations;

public final class JsonToLookupTableConverter {

    private final static Logger LOGGER = Logger.getLogger(JsonToLookupTableConverter.class.getName());

    public static RestTemplate  lookupRestTemplate;

    @SuppressWarnings("rawtypes")
    public static ConcurrentHashMap<String, String> jsonToProductCategoryLookupTable(LinkedList<?> jsonList) {
        ConcurrentHashMap<String, String> categoryLookupData = new ConcurrentHashMap<String, String>();

        try {
            Iterator<?> iter = jsonList.iterator();
            categoryLookupData = new ConcurrentHashMap<String, String>(jsonList.size() + 2);
            while (iter.hasNext()) {
                try {
                    LinkedHashMap crntValue = (LinkedHashMap) iter.next();
                    categoryLookupData.put(String.valueOf(crntValue.get("Name")).toUpperCase(),
                            String.valueOf(crntValue.get("Code")));
                } catch (Exception e) {
                } // Collecting maximum elements
            }
            return categoryLookupData;
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return categoryLookupData;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> createColorLookupMap(LinkedList<?> colorLookupJsonList) {
        Map<String, String> colorJsonLookupTable = new HashMap<String, String>();

        try {
            colorJsonLookupTable = new HashMap<String, String>(colorLookupJsonList.size() * 2);
            Iterator<?> iter = colorLookupJsonList.iterator();
            while (iter.hasNext()) {
                try {
                    LinkedHashMap<?, ?> crntValue = (LinkedHashMap<String, String>) iter.next();
                    if (crntValue != null) {
                        ArrayList<LinkedHashMap<?, ?>> codeValueGroups = (ArrayList<LinkedHashMap<?, ?>>) crntValue
                                .get("CodeValueGroups");
                        if (codeValueGroups != null && !codeValueGroups.isEmpty()) {
                            Iterator<LinkedHashMap<?, ?>> codeValueGrpItr = codeValueGroups.iterator();
                            while (codeValueGrpItr.hasNext()) {
                                LinkedHashMap<?, ?> codeValueGroup = (LinkedHashMap<?, ?>) codeValueGrpItr.next();
                                ArrayList<LinkedHashMap<?, ?>> setCodeValues = (ArrayList<LinkedHashMap<?, ?>>) codeValueGroup
                                        .get("SetCodeValues");
                                if (setCodeValues != null && !setCodeValues.isEmpty()) {
                                    Iterator<LinkedHashMap<?, ?>> setCodeValuesItr = setCodeValues.iterator();
                                    while (setCodeValuesItr.hasNext()) {
                                        LinkedHashMap<?, ?> setCodeValueMap = (LinkedHashMap<?, ?>) setCodeValuesItr.next();
                                        if (setCodeValueMap != null && !setCodeValueMap.isEmpty()) {
                                            String key = String.valueOf(
                                                    setCodeValueMap.get(ApplicationConstants.CONST_STRING_CODE_VALUE)).trim();
                                            String value = String.valueOf(
                                                    setCodeValueMap.get(ApplicationConstants.CONST_STRING_ID_CAP)).trim();
                                            colorJsonLookupTable.put(key.toUpperCase(), value);
                                        }
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception while processing color com.asi.util.json model ", e);
                }
            }
            // LOGGER.info(JSONValue.toJSONString(com.asi.util.json));
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return colorJsonLookupTable;
    }

    /*
     * public static HashMap<String, Map<String, SetCodeValueJsonModel>> createProductColorMap(final String COLOR_API_URL) {
     * HashMap<String, Map<String, SetCodeValueJsonModel>> productColorMap = new HashMap<>();
     * try {
     * String response = JersyClientGet.getLookupsResponse(COLOR_API_URL);
     * List<ProductColorJsonModel> productColorList = JsonProcessor.convertJsonToBeanCollection(response,
     * ProductColorJsonModel.class);
     * if (productColorList != null && !productColorList.isEmpty()) {
     * productColorMap = new HashMap<>();
     * for (ProductColorJsonModel prodColor : productColorList) {
     * String colorName = prodColor.getDisplayName() != null ? prodColor.getDisplayName().toUpperCase() : prodColor
     * .getDisplayName();
     * HashMap<String, SetCodeValueJsonModel> color = new HashMap<>();
     * for (CodeValueGroupJsonModel codeValueGroup : prodColor.getCodeValueGroups()) {
     * if (codeValueGroup != null && codeValueGroup.getSetCodeValues() != null
     * && !codeValueGroup.getSetCodeValues().isEmpty()) {
     * String key = codeValueGroup.getDisplayName() != null ? codeValueGroup.getDisplayName().toUpperCase()
     * : codeValueGroup.getDescription() != null ? codeValueGroup.getDescription().toUpperCase()
     * : "null";
     * color.put(key, codeValueGroup.getSetCodeValues().get(0));
     * }
     * }
     * productColorMap.put(colorName, color);
     * }
     * } else {
     * productColorMap = new HashMap<>();
     * }
     * } catch (Exception e) {
     * LOGGER.error("Exception while processing Product Color JSON", e);
     * productColorMap = new HashMap<>();
     * }
     * 
     * return productColorMap;
     * }
     */

    public static Map<String, String> createProductColorMap(final String COLOR_API_URL) {
        try {
            // String response = JersyClientGet.getLookupsResponse(COLOR_API_URL);
            LinkedList<?> response = lookupRestTemplate.getForObject(COLOR_API_URL, LinkedList.class);
            return createColorLookupMap(response);

        } catch (Exception e) {
            LOGGER.error("Exception while processing Product Color JSON", e);
            return new HashMap<String, String>();
        }

    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, String> jsonToProductOriginMap(LinkedList<?> originsList) {

        HashMap<String, String> originMap = new HashMap<String, String>();
        // This is a workaround requested by Radar Team, this need to be there until they clean up DB
        HashMap<String, String> originTempMap = new HashMap<String, String>(); // Store is StandardValueFlag: "N" elements only
        try {
            originMap = new HashMap<String, String>(originsList.size());
            Iterator<?> iter = originsList.iterator();// entrySet().iterator();
            while (iter.hasNext()) {
                LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
                if (crntValue != null) {
                    try {
                        if (!String.valueOf(crntValue.get("StandardValueFlag")).toLowerCase().equalsIgnoreCase("N")) { 
                            originMap.put(crntValue.get("CodeValue").toUpperCase(), String.valueOf(crntValue.get("ID")));
                        } else {
                            originTempMap.put(crntValue.get("CodeValue").toUpperCase(), String.valueOf(crntValue.get("ID")));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while proceesing Origin Json Value conversion", e);
        }
        // now merge the elements those are not in actual map, this is a workaround for Radar DB problem
        // 
        if (originMap.isEmpty()) {
            return originTempMap;
        } else if (!originTempMap.isEmpty()) {
            originTempMap.keySet().removeAll(originMap.keySet()); // Removing elements which are already present in actual map
            if (originTempMap != null && !originTempMap.isEmpty()) {
                originMap.putAll(originTempMap); // now add all remaining elements are need to be added to the exact map
            }
        } 
        return originMap;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, String> jsonToProductShapesMap(LinkedList<?> jsonList) {
        HashMap<String, String> shapesMap = new HashMap<String, String>();
        try {

            shapesMap = new HashMap<String, String>(jsonList.size());

            Iterator<?> iter = jsonList.iterator();
            while (iter.hasNext()) {
                LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
                if (crntValue != null) {
                    try {
                        shapesMap.put(crntValue.get("Value").toUpperCase(), crntValue.get("Key"));
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while proceesing Shapes Json Value conversion", e);
        }
        return shapesMap;
    }

    /**
     * Search for a particular <b>Material</b> Criteria Code in given JSON string.
     * If a match found then we find the corresponding Group and find the matched Criteria ID for the <b>Material</b>
     * 
     * @param jsonText
     *            is the JSON string contains all the lookup data
     * @param srchValue
     *            is the Material name need to search
     * @return criteria code ID of given Material from lookup values or NULL
     */
    public static String checkMaterialValueKeyPair(String jsonText, String srchValue) {
        String criteriaCode = ApplicationConstants.CONST_STRING_NULL_CAP;
        srchValue = srchValue.toLowerCase();
        srchValue = srchValue.substring(0, 1).toUpperCase() + srchValue.substring(1);
        // srchValue = "Fabric-" + srchValue;
        // LOGGER.info("Search Value in Json Processor"+srchValue);
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List<?> creatArrayContainer() {
                return new LinkedList<Object>();
            }

            public Map<?, ?> createObjectContainer() {
                return new LinkedHashMap<Object, Object>();
            }
        };
        try {
            LinkedList<?> json = (LinkedList<?>) parser.parse(jsonText, containerFactory);
            String otherCriteriaValue = ApplicationConstants.CONST_STRING_OTHER;
            Iterator<?> iter = json.iterator();// entrySet().iterator();
            // LOGGER.info("==iterate result==");
            boolean nextCriteriaCodeCheck = true;
            while (nextCriteriaCodeCheck && iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                // LOGGER.info("INitial List"+crntValue);
                if (crntValue.get("DisplayName").toString().trim().equalsIgnoreCase(srchValue.trim())) {
                    @SuppressWarnings({ "rawtypes", "unchecked" })
                    LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                    @SuppressWarnings("rawtypes")
                    Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                    while (iterator.hasNext()) {
                        @SuppressWarnings("rawtypes")
                        Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

                        // LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
                        @SuppressWarnings("rawtypes")
                        List finalLst = (LinkedList) codeValueGrpsMap.get("SetCodeValues");
                        @SuppressWarnings("rawtypes")
                        Iterator finalItr = finalLst.iterator();
                        if (finalItr.hasNext()) {
                            @SuppressWarnings("rawtypes")
                            Map finalMap = (LinkedHashMap) finalItr.next();
                            // LOGGER.info("ID:"+finalMap.get("ID"));
                            criteriaCode = finalMap.get("ID").toString();
                            nextCriteriaCodeCheck = false;
                        }
                    }

                } else if (crntValue.get("DisplayName").toString().equalsIgnoreCase(otherCriteriaValue))

                {
                    @SuppressWarnings({ "rawtypes", "unchecked" })
                    LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                    @SuppressWarnings("rawtypes")
                    Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                    while (iterator.hasNext()) {
                        @SuppressWarnings("rawtypes")
                        Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

                        // LOGGER.info("[]"+codeValueGrpsMap.get("SetCodeValues"));
                        @SuppressWarnings("rawtypes")
                        List finalLst = (LinkedList) codeValueGrpsMap.get("SetCodeValues");
                        @SuppressWarnings("rawtypes")
                        Iterator finalItr = finalLst.iterator();
                        if (finalItr.hasNext()) {
                            @SuppressWarnings("rawtypes")
                            Map finalMap = (LinkedHashMap) finalItr.next();
                            // LOGGER.info("ID:"+finalMap.get("ID"));
                            otherCriteriaValue = finalMap.get("ID").toString();
                        }
                    }
                }
            }
            if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) criteriaCode = otherCriteriaValue;
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return criteriaCode;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static HashMap<String, String> jsonToProductThemesMap(LinkedList<?> productThemesResponse) {
 
        HashMap<String, String> productThemesLookupTable = new HashMap<String, String>();
        try {
            Iterator<?> iter = productThemesResponse.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                LinkedList<LinkedHashMap> codeValueGrps = (LinkedList<LinkedHashMap>) crntValue.get("SetCodeValues");
                Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                while (iterator.hasNext()) {
                    Map codeValueGrpsMap = (LinkedHashMap) iterator.next();
                    try {
                        productThemesLookupTable.put(String.valueOf(codeValueGrpsMap.get("CodeValue")).toUpperCase(),
                                String.valueOf(codeValueGrpsMap.get("ID")));
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception pe) {
        }
        return productThemesLookupTable;
    }

    @SuppressWarnings("unchecked")
    public static String getSetCodeValueForTradeName(LinkedList<?> jsonList, String tradeName) {
        String setCodeValueId = null;
        try {
            Iterator<?> iter = jsonList.iterator();
            while (iter.hasNext()) {
                LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
                if (crntValue != null) {
                    if (tradeName.equalsIgnoreCase(crntValue.get(ApplicationConstants.CONST_STRING_VALUE))) {
                        setCodeValueId = crntValue.get(ApplicationConstants.CONST_STRING_KEY).toString();
                        break;
                    }
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return setCodeValueId;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, String> jsonToProductPackagesMap(LinkedList<?> productPackagesList) {
        HashMap<String, String> packagesMap = new HashMap<>();
        try {
            Iterator<?> iter = productPackagesList.iterator();// entrySet().iterator();
            packagesMap = new HashMap<>(productPackagesList.size());
            while (iter.hasNext()) {
                LinkedHashMap<String, String> crntValue = (LinkedHashMap<String, String>) iter.next();
                if (crntValue != null) {
                    try {
                        packagesMap.put(String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_VALUE)).toUpperCase(),
                                String.valueOf(crntValue.get(ApplicationConstants.CONST_STRING_KEY)));
                    } catch (Exception e) {
                    } // read Maximum data

                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return packagesMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static HashMap<String, String> jsonToProductCustomLookupTable(LinkedList<?> wsResponseList, String criteriaCode) {

        HashMap<String, String> customLookupTable = new HashMap<>();
        try {
            Iterator<?> iter = wsResponseList.iterator();// entrySet().iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (criteriaCode.equalsIgnoreCase(String.valueOf(crntValue.get("Code")))) {
                    ArrayList<LinkedHashMap> codeValueGrps = (ArrayList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                    Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                    while (iterator.hasNext()) {
                        Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

                        ArrayList finalLst = (ArrayList) codeValueGrpsMap.get("SetCodeValues");
                        Iterator finalItr = finalLst.iterator();
                        while (finalItr.hasNext()) {
                            Map finalMap = (LinkedHashMap) finalItr.next();
                            customLookupTable.put(String.valueOf(finalMap.get("CodeValue")).toUpperCase(),
                                    String.valueOf(finalMap.get("ID")));
                        }
                    }
                    break;
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return customLookupTable;
    }
    public static HashMap<String, String> jsonToSelectedLinesNamesLookupTable(LinkedList<?> jsonList) {

        HashMap<String, String> selectedLineNamesLookupTable = new HashMap<>();
        try {
        	selectedLineNamesLookupTable = new HashMap<>(jsonList.size());
            Iterator<?> iter = jsonList.iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, ?> crntValue = (LinkedHashMap<String, ?>) iter.next();
                try {
                	selectedLineNamesLookupTable.put(String.valueOf(crntValue.get("Name")), String.valueOf(crntValue.get("ID")).toUpperCase());
                } catch (Exception e) {
                    // Trying to get maximum data so no exception need to process now
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return selectedLineNamesLookupTable;
    }

    public static HashMap<String, String> jsonToComplianceCertLookupTable(LinkedList<?> jsonList) {

        HashMap<String, String> complianceCertLookupTable = new HashMap<>();
        try {
            complianceCertLookupTable = new HashMap<>(jsonList.size());
            Iterator<?> iter = jsonList.iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, ?> crntValue = (LinkedHashMap<String, ?>) iter.next();
                try {
                    complianceCertLookupTable.put(String.valueOf(crntValue.get("Value")).toUpperCase(),
                            String.valueOf(crntValue.get("Key")));
                } catch (Exception e) {
                    // Trying to get maximum data so no exception need to process now
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return complianceCertLookupTable;
    }

    public static HashMap<String, String> jsonToSafetyWarningLookupTable(LinkedList<?> jsonList) {
        HashMap<String, String> safetywarningLookupTable = new HashMap<>();
        try {
            safetywarningLookupTable = new HashMap<>(jsonList.size());
            Iterator<?> iter = jsonList.iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, ?> crntValue = (LinkedHashMap<String, ?>) iter.next();
                try {
                    safetywarningLookupTable.put(String.valueOf(crntValue.get("Value")).toUpperCase(),
                            String.valueOf(crntValue.get("Key")));
                } catch (Exception e) {
                    // Trying to get maximum data so no exception need to process now
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return safetywarningLookupTable;
    }

    public static HashMap<String, String> jsonToProdSpecSampleLookupTable(String prodSpecSampleJson) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, String> jsonToImprintMethodLookupTable(LinkedList<?> imprintMethodList) {

        Map<String, String> imprintMethodMap = new HashMap<String, String>();
        try {
            imprintMethodMap = new HashMap<String, String>(imprintMethodList.size()); // for performance
            Iterator<?> iter = imprintMethodList.iterator();
            while (iter.hasNext()) {
                LinkedHashMap crntValue = (LinkedHashMap) iter.next();
                if (crntValue.get("CodeValue") != null) {
                    imprintMethodMap.put(String.valueOf(crntValue.get("CodeValue")).toUpperCase().trim(),
                            String.valueOf(crntValue.get("ID")));
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return imprintMethodMap;
    }

    public static Map<String, PriceUnitJsonModel> jsonToPriceUnitLookupTable(LinkedList<?> jsonList) {
        Map<String, PriceUnitJsonModel> priceUnitMap = new HashMap<String, PriceUnitJsonModel>();
        try {
            List<LinkedHashMap<String, String>> priceUnitJsonModelList = (List<LinkedHashMap<String, String>>) jsonList;//JsonProcessor.convertJsonToBean(jsonList, List.class);

            if (priceUnitJsonModelList != null && !priceUnitJsonModelList.isEmpty()) {
                for (LinkedHashMap<String, String> priceUnitJModel : priceUnitJsonModelList) {
                    PriceUnitJsonModel pUntiJModel = new PriceUnitJsonModel();
                    pUntiJModel.setDescription(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_DESCRIPTION)));
                    pUntiJModel.setID(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_ID_CAP)));
                    pUntiJModel.setDisplayName(String.valueOf(priceUnitJModel.get(ApplicationConstants.CONST_STRING_DISPLAY_NAME)));
                    pUntiJModel.setItemsPerUnit(String.valueOf(priceUnitJModel
                            .get(ApplicationConstants.CONST_STRING_ITEMS_PER_UNIT)));
                    priceUnitMap.put(String.valueOf(priceUnitJModel.get("DisplayName")).toUpperCase(), pUntiJModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return priceUnitMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, String> jsonToArtworkLookupTable(LinkedList<?> jsonDataList) {

        Map<String, String> artworkLookupTable = new HashMap<String, String>();
        try {
            Iterator<?> iter = jsonDataList.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (crntValue.get("Code").toString().equalsIgnoreCase(ApplicationConstants.CONST_ARTWORK_CODE)) {
                    ArrayList<LinkedHashMap> codeValueGrps = (ArrayList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                    Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                    while (iterator.hasNext()) {
                        Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

                        List finalLst = (ArrayList) codeValueGrpsMap.get("SetCodeValues");
                        Iterator finalItr = finalLst.iterator();
                        while (finalItr.hasNext()) {
                            try {
                                Map finalMap = (LinkedHashMap) finalItr.next();
                                if (finalMap != null) {
                                    artworkLookupTable.put(String.valueOf(finalMap.get("CodeValue")).toUpperCase(),
                                            String.valueOf(finalMap.get("ID")));
                                }
                            } catch (Exception e) {

                            }

                        }

                        // break;
                    }
                    // break;
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return artworkLookupTable;

    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, String> jsonToMinQtyLookupTable(LinkedList<?> jsonDataList) {

        Map<String, String> artworkLookupTable = new HashMap<String, String>();
        try {
            Iterator<?> iter = jsonDataList.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (crntValue.get("Code").toString().equalsIgnoreCase(ApplicationConstants.CONST_MINIMUM_QUANTITY)) {
                    ArrayList<LinkedHashMap> codeValueGrps = (ArrayList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                    Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                    while (iterator.hasNext()) {
                        Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

                        List finalLst = (ArrayList) codeValueGrpsMap.get("SetCodeValues");
                        Iterator finalItr = finalLst.iterator();
                        while (finalItr.hasNext()) {
                            try {
                                Map finalMap = (LinkedHashMap) finalItr.next();
                                if (finalMap != null) {
                                    artworkLookupTable.put(String.valueOf(finalMap.get("CodeValue")).toUpperCase(),
                                            String.valueOf(finalMap.get("ID")));
                                }
                            } catch (Exception e) {

                            }

                        }

                        // break;
                    }
                    // break;
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return artworkLookupTable;

    }

    public static Map<String, String> generateCriteriaSetAttributeTable(LinkedList<?> jsonDataList) {

        Map<String, String> criteriaSetAttributeLookupTable = new HashMap<String, String>();
        try {
            Iterator<?> iter = jsonDataList.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (crntValue != null && !crntValue.isEmpty()) {
                    criteriaSetAttributeLookupTable.put(String.valueOf(crntValue.get("CriteriaCode")),
                            String.valueOf(crntValue.get("ID")));
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return criteriaSetAttributeLookupTable;

    }

    public static Map<String, HashMap<String, String>> generateUnitOfMeasureTable(LinkedList<?> jsonDataList) {
     
        Map<String, HashMap<String, String>> unitOfMeasureMap = new HashMap<String, HashMap<String, String>>();
        try {
            Iterator<?> iter = jsonDataList.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (crntValue != null && !crntValue.isEmpty()) {
                    String criteriaCode = String.valueOf(crntValue.get("CriteriaCode"));
                    HashMap<String, String> criteriaSetAttributeMap = new HashMap<String, String>();
                    List<?> unitsOfMeasureList = (List<?>) crntValue.get("UnitsOfMeasure");
                    if (unitsOfMeasureList != null) {
                        Iterator<?> unitOfMeasureItr = unitsOfMeasureList.iterator();
                        while (unitOfMeasureItr.hasNext()) {
                            Map<?, ?> unitsOfMeasures = (LinkedHashMap<?, ?>) unitOfMeasureItr.next();
                            if (unitsOfMeasures != null) {
                                criteriaSetAttributeMap.put(String.valueOf(unitsOfMeasures.get("Format")).toUpperCase(),
                                        String.valueOf(unitsOfMeasures.get("Code")));
                            }
                        }
                        unitOfMeasureMap.put(criteriaCode, criteriaSetAttributeMap);
                    }
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return unitOfMeasureMap;
    }

    public static Map<String, HashMap<String, String>> generateCriteriaItemTable(LinkedList<?> jsonDataList) {
        Map<String, HashMap<String, String>> criteriaItemsMap = new HashMap<String, HashMap<String, String>>();
        try {
            Iterator<?> iter = jsonDataList.iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (crntValue != null && !crntValue.isEmpty()) {
                    String criteriaCode = String.valueOf(crntValue.get("CriteriaCode"));
                    Map<?, ?> criteriaItemMap = (HashMap<?, ?>) crntValue.get("CriteriaItem");
                    if (criteriaItemMap != null) {

                        HashMap<String, String> criteriaItem = new HashMap<String, String>();
                        List<?> codeValueGroupList = (List<?>) criteriaItemMap.get("CodeValueGroups");
                        if (codeValueGroupList != null) {
                            Iterator<?> codeValueGroupItr = codeValueGroupList.iterator();
                            while (codeValueGroupItr.hasNext()) {
                                Map<?, ?> codeValueGroup = (LinkedHashMap<?, ?>) codeValueGroupItr.next();
                                if (codeValueGroup != null && !codeValueGroup.isEmpty()) {
                                    List<?> setCodeValueList = (List<?>) codeValueGroup.get("SetCodeValues");
                                    Iterator<?> setCodeValueIter = setCodeValueList.iterator();
                                    while (setCodeValueIter.hasNext()) {
                                        Map<?, ?> setCodeValue = (LinkedHashMap<?, ?>) setCodeValueIter.next();
                                        criteriaItem.put(String.valueOf(setCodeValue.get("CodeValue")),
                                                String.valueOf(setCodeValue.get("ID")));
                                    }
                                }
                            }
                        }
                        criteriaItemsMap.put(criteriaCode, criteriaItem);
                    }
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return criteriaItemsMap;
    }

    public static Map<String, CriteriaInfo> createCriteriaInfoLookup(LinkedList<?> wsResponseList) {
        Map<String, LinkedHashMap> criteriaLinkedHashMap = new HashMap<String, LinkedHashMap>();
        Map<String, CriteriaInfo> criteriaInfoMap = new HashMap<String, CriteriaInfo>();
        List<LinkedHashMap> criteriaInfoList = (List<LinkedHashMap>) wsResponseList;//JsonProcessor.convertJsonToBeanCollection(wsResponse, CriteriaInfo.class);
        CriteriaInfo info=null;
        if (criteriaInfoList != null) {
        	criteriaLinkedHashMap = new HashMap<String, LinkedHashMap>(criteriaInfoList.size());
            for (LinkedHashMap infoMap : criteriaInfoList) {
            	info=new CriteriaInfo();
            	info.setCode(infoMap.get("Code").toString());
            	//info.setCriteriaTypeCode(infoMap.get("CriteriaTypeCode").toString());
            //	info.setCriteriaTypeDescription(infoMap.get("CriteriaTypeDescription").toString());
            	if(infoMap.containsKey("DefaultPriceGridSubTypeCode"))
            	info.setDefaultPriceGridSubTypeCode(infoMap.get("DefaultPriceGridSubTypeCode").toString());
            	//info.setCriteriaTypeDisplayName(infoMap.get("DisplayName").toString()); 
            	info.setDescription(infoMap.get("Description").toString());
                criteriaInfoMap.put(info.getCode(), info);
            }
        }
        return criteriaInfoMap;
    }

    @SuppressWarnings("rawtypes")
    public static ConcurrentHashMap<String, String> jsonToProductTypeCodeLookupTable(LinkedList<?> prodTypecodeResponse) {
        ConcurrentHashMap<String, String> typeCodeLookupData = null;
        try {
            LinkedList<?> json = prodTypecodeResponse;
            Iterator<?> iter = json.iterator();
            typeCodeLookupData = new ConcurrentHashMap<String, String>(json.size() + 2);
            while (iter.hasNext()) {
                try {
                    LinkedHashMap crntValue = (LinkedHashMap) iter.next();
                    typeCodeLookupData.put(String.valueOf(crntValue.get("DisplayName")).toUpperCase(),
                            String.valueOf(crntValue.get("Code")));
                } catch (Exception e) {
                } // Collecting maximum elements
            }
            return typeCodeLookupData;
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return typeCodeLookupData;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, String> jsonToOptionLookupMap(LinkedList<LinkedHashMap> producOptionResponse) {
        Map<String, String> optionsLookupTable = new HashMap<>();
        try {
            LinkedList<?> json = producOptionResponse;
            Iterator<?> iter = json.iterator();// entrySet().iterator();
            while (iter.hasNext()) {
                Map<?, ?> crntValue = (LinkedHashMap<?, ?>) iter.next();
                if (CommonUtilities.isOptionGroup(String.valueOf(crntValue.get("Code")))) {
                    ArrayList<LinkedHashMap> codeValueGrps = (ArrayList<LinkedHashMap>) crntValue.get("CodeValueGroups");
                    Iterator<LinkedHashMap> iterator = codeValueGrps.iterator();

                    while (iterator.hasNext()) {
                        Map codeValueGrpsMap = (LinkedHashMap) iterator.next();

                        List finalLst = (ArrayList) codeValueGrpsMap.get("SetCodeValues");
                        Iterator finalItr = finalLst.iterator();
                        while (finalItr.hasNext()) {
                            Map finalMap = (LinkedHashMap) finalItr.next();
                            optionsLookupTable.put(String.valueOf(crntValue.get("Code")), String.valueOf(finalMap.get("ID")));
                            
                        }
                    }
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        
        return optionsLookupTable;
    }

	public static Map<String, String> createProductMaterialMap(final String MATERIAL_API_URL) {
		  try {
	            LinkedList<?> response = lookupRestTemplate.getForObject(MATERIAL_API_URL, LinkedList.class);
	            return createColorLookupMap(response);

	        } catch (Exception e) {
	            LOGGER.error("Exception while processing Product Color JSON", e);
	            return new HashMap<String, String>();
	        }

	}

    public static Map<String, Currency> jsonToCurrencyLookupTable(String response) {
        Map<String, Currency> currencyMap = new HashMap<String, Currency>();
        try {
            List<LinkedHashMap<String, String>> currencyJsonModelList = JsonProcessor.convertJsonToBean(response, List.class);

            if (currencyJsonModelList != null && !currencyJsonModelList.isEmpty()) {
                for (LinkedHashMap<String, String> currencyJModel : currencyJsonModelList) {
                    Currency currency = new Currency();
                    currency.setCode(String.valueOf(currencyJModel.get("Code")));
                    currency.setName(String.valueOf(currencyJModel.get("Name")));
                    currency.setNumber(String.valueOf(currencyJModel.get("Number")));
                    currencyMap.put(String.valueOf(currencyJModel.get("Code")).toUpperCase(), currency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyMap;
    }

    public static Map<String, DiscountRate> jsonToDiscountLookupTable(LinkedList<?> responseList) {
        Map<String, DiscountRate> currencyMap = new HashMap<String, DiscountRate>();
        try {
            List<LinkedHashMap<String, String>> currencyJsonModelList = (List<LinkedHashMap<String, String>>) responseList;//JsonProcessor.convertJsonToBean(response, List.class);
            
            if (currencyJsonModelList != null && !currencyJsonModelList.isEmpty()) {
                for (LinkedHashMap<String, String> discountJModel : currencyJsonModelList) {
                    DiscountRate currency = new DiscountRate();
                    currency.setCode(String.valueOf(discountJModel.get("Code")));
                    currency.setDescription(String.valueOf(discountJModel.get("Description")));
                    currency.setDiscountPercent(String.valueOf(discountJModel.get("DiscountPercent")));
                    currency.setIndustryDiscountCode(String.valueOf(discountJModel.get("IndustryDiscountCode")));
                    currencyMap.put(String.valueOf(discountJModel.get("IndustryDiscountCode")).toUpperCase(), currency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencyMap;
    }

	public static ConcurrentHashMap<String, String> jsonToProductCriteriaListLookupTable(
			LinkedList<?> prodCriteriaListResponse) {

		
		
		 ConcurrentHashMap<String, String> criteriaCodeLookupData = null;
	        try {
	            LinkedList<?> json = prodCriteriaListResponse;
	            Iterator<?> iter = json.iterator();
	            criteriaCodeLookupData = new ConcurrentHashMap<String, String>(json.size() + 2);
	            while (iter.hasNext()) {
	                try {
	                    LinkedHashMap crntValue = (LinkedHashMap) iter.next();
	                    criteriaCodeLookupData.put(String.valueOf(crntValue.get("Description")).toUpperCase(),
	                            String.valueOf(crntValue.get("Code")));
	                } catch (Exception e) {
	                } // Collecting maximum elements
	            }
	            return criteriaCodeLookupData;
	        } catch (Exception pe) {
	            pe.printStackTrace();
	        }
	        return criteriaCodeLookupData;		
	}

    public static HashMap<String, String> jsonToFOBPointookupTable(LinkedList<?> fobPoints) {

        HashMap<String, String> fobPointsLookupTable = new HashMap<>();
        try {
            fobPointsLookupTable = new HashMap<>(fobPoints.size());
            Iterator<?> iter = fobPoints.iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, ?> crntValue = (LinkedHashMap<String, ?>) iter.next();
                try {
                    fobPointsLookupTable.put(String.valueOf(crntValue.get("Name")), String.valueOf(crntValue.get("ID")).toUpperCase());
                } catch (Exception e) {
                    // Trying to get maximum data so no exception need to process now
                }
            }
        } catch (Exception pe) {
            pe.printStackTrace();
        }
        return fobPointsLookupTable;
    }

	@SuppressWarnings("unchecked")
	public static Catalog jsonToCatalogs(LinkedList<?> responseList,String mediaCitationId,String mediaCitationReferenceId) {
		Catalog returnCatalog=new Catalog();
		try {
            Iterator<?> iter = responseList.iterator();
            LinkedHashMap crntValue=null;
            ArrayList<LinkedHashMap> productCitationReferences=null;
            while(iter.hasNext()){
            	    crntValue = (LinkedHashMap) iter.next();
            	    if(crntValue.get("ID").toString().equals(mediaCitationId)){
                    returnCatalog.setCatalogName(crntValue.get("Name").toString());
                    productCitationReferences = (ArrayList<LinkedHashMap>) crntValue.get("MediaCitationReferences");
                    for(LinkedHashMap citationReference:productCitationReferences){
                    		if(mediaCitationReferenceId.equals(citationReference.get("ID").toString()))
                    			returnCatalog.setCatalogPage(citationReference.get("Number").toString());
                    	}                                                    	
            	    }
            }            
	    } catch (Exception pe) {
            pe.printStackTrace();
        }		
		return returnCatalog;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ProductMediaCitations jsonToMediaCitation(LinkedList<?> responseList, String productID, String catalogName, String catalogPageNumber) {

		ProductMediaCitations mediaCitation = new ProductMediaCitations();
		List<ProductMediaCitationReferences> productMediaCitationReferences;
		ProductMediaCitationReferences productMediaCitationReference;
		MediaCitationReference mediaCitationReference;
		LinkedHashMap current;
        ArrayList<LinkedHashMap> productCitationReferences;
        
        Integer newMediaCitationID = -1;
        Integer newMediaCitationReferenceID = -1;
		
		try {

			for(Iterator<?> iter = responseList.iterator(); iter.hasNext(); ) {

            	current = (LinkedHashMap) iter.next();
            	
            	if(current.get("Name").toString().equalsIgnoreCase(catalogName)) {
            		
            		mediaCitation.setId(String.valueOf(newMediaCitationID));
            		mediaCitation.setProductId(productID);
            		mediaCitation.setMediaCitationId(current.get("ID").toString());
            		mediaCitation.setIsInitMediaCitation("false");
            		
            		productCitationReferences = (ArrayList<LinkedHashMap>) current.get("MediaCitationReferences");

            		if(productCitationReferences != null && !productCitationReferences.isEmpty()) {
            			boolean pageNumberFound = false;
            			productMediaCitationReferences = new ArrayList<ProductMediaCitationReferences>();

                		productMediaCitationReference = new ProductMediaCitationReferences();
                		productMediaCitationReference.setProductId(productID);
                		
                		productMediaCitationReference.setMediaCitationId(mediaCitation.getMediaCitationId());
                		productMediaCitationReference.setIsPrimary("false");
                		
                		mediaCitationReference = new MediaCitationReference();

            			for(LinkedHashMap citationReference : productCitationReferences) {

            				pageNumberFound = catalogPageNumber.equals(citationReference.get("Number").toString());
                    		if(pageNumberFound) {
                    			productMediaCitationReference.setMediaCitationReferenceId(citationReference.get("ID").toString());
                        		mediaCitationReference.setId(productMediaCitationReference.getMediaCitationReferenceId());
                        		if(!StringUtils.isEmpty(citationReference.get("Sequence"))) {
                        			mediaCitationReference.setSequence(citationReference.get("Sequence").toString());
                        		}
	                    		break;
	                    	}
	                    }
            			
            			if(!pageNumberFound) {
                    		mediaCitationReference.setId(String.valueOf(--newMediaCitationReferenceID));
                    		productMediaCitationReference.setMediaCitationReferenceId(String.valueOf(newMediaCitationReferenceID));
            			}
            			
                		mediaCitationReference.setNumber(catalogPageNumber);
                		mediaCitationReference.setMediaCitationId(productMediaCitationReference.getMediaCitationId());
                		productMediaCitationReference.setMediaCitationReference(mediaCitationReference);
                		
                		productMediaCitationReferences.add(productMediaCitationReference);
            			
            			mediaCitation.setProductMediaCitationReferences(productMediaCitationReferences);
            			
            		}
            		break;
            	}
            }
		} catch (Exception pe) {
			pe.printStackTrace();
		}		
		return mediaCitation;
	}


}
