package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.transformers.ProductDataStore;

import com.asi.ext.api.service.model.BlendMaterial;
import com.asi.ext.api.service.model.Combo;
import com.asi.ext.api.service.model.Material;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.ChildCriteriaSetCodeValue;
import com.asi.service.product.client.vo.ChildCriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetCodeValues;
import com.asi.service.product.client.vo.CriteriaSetValues;
import com.asi.service.product.client.vo.ProductCriteriaSets;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.SetCodeValues;

public class ProductMateriaProcessor extends SimpleCriteriaProcessor {

    private final static Logger LOGGER                       = Logger.getLogger(ProductMateriaProcessor.class.getName());

    private int                 uniqueCriteriaSetId          = 1;
    private String              configId                     = "0";
    private int                 criteriaSetCodeValueObjectId = -1;

    /**
     * @param uniqueSetValueId
     * @param uniqueCriteriaSetId
     * @param configId
     */
    public ProductMateriaProcessor(int uniqueCriteriaSetId, String configId) {
        this.uniqueCriteriaSetId = uniqueCriteriaSetId;
        this.configId = configId;
    }

    public ProductCriteriaSets getProductMaterialCriteriaSet(List<Material> materials, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, String configId) {

        if (materials == null || materials.isEmpty()) {
            return null;
        }
        this.configId = configId;

        return getCriteriaSet(materials, existingProduct, matchedCriteriaSet, 0);
    }

    public ProductCriteriaSets getCriteriaSet(List<Material> materials, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        if (materials == null || materials.isEmpty()) {
            return null;
        }
        LOGGER.info("Started Processing of Product Materials");
        // First verify and process value to desired format

        List<CriteriaSetValues> finalCriteriaSetValues = new ArrayList<>();

        boolean checkExistingElements = matchedCriteriaSet != null;

        HashMap<String, CriteriaSetValues> existingValueMap = new HashMap<String, CriteriaSetValues>();
        if (checkExistingElements) {
            existingValueMap = createTableForExistingSetValue(matchedCriteriaSet.getCriteriaSetValues());
        } else {
            matchedCriteriaSet = new ProductCriteriaSets();
            // Set Basic elements
            matchedCriteriaSet.setCriteriaSetId(String.valueOf(--uniqueCriteriaSetId));
            matchedCriteriaSet.setProductId(existingProduct.getID());
            matchedCriteriaSet.setCompanyId(existingProduct.getCompanyId());
            matchedCriteriaSet.setConfigId(this.configId);
            matchedCriteriaSet.setCriteriaCode(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE);
            matchedCriteriaSet.setIsBase(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsRequiredForOrder(ApplicationConstants.CONST_STRING_FALSE_SMALL);
            matchedCriteriaSet.setIsDefaultConfiguration(ApplicationConstants.CONST_STRING_FALSE_SMALL);
        }

        for (Material material : materials) {

            boolean hasCombo = material.getCombo() != null;
            boolean hasAliace = !CommonUtilities.isValueNull(material.getAlias());
            boolean hasBlend = (material.getBlendMaterials() != null && !material.getBlendMaterials().isEmpty());
            boolean needToCheckAgain = false;

            String setCodeValueId = getSetCodeValueId(material.getName());

            if (CommonUtilities.isValueNull(setCodeValueId)) {
                // LOG Batch Error
                continue;
            }
            CriteriaSetValues criteriaSetValue = null;

            if (checkExistingElements && !hasBlend && !hasCombo) {
                if (hasAliace) {
                    criteriaSetValue = existingValueMap.get(material.getAlias().toUpperCase() + "_" + setCodeValueId);
                } else {
                    criteriaSetValue = existingValueMap.get(material.getName().toUpperCase() + "_" + setCodeValueId);
                }
            } else if (checkExistingElements && (hasBlend || hasCombo)) {
                needToCheckAgain = true;
            }

            if (criteriaSetValue == null) {
                // If no match found in the existing list
                // Set basic properties for a criteriaSetValue
                criteriaSetValue = new CriteriaSetValues();
                criteriaSetValue.setId(String.valueOf(--uniqueSetValueId));
                criteriaSetValue.setCriteriaCode(ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE);
                if (hasBlend || hasCombo) {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LIST);
                } else {
                    criteriaSetValue.setValueTypeCode(ApplicationConstants.CONST_VALUE_TYPE_CODE_LOOK);
                }
                criteriaSetValue.setIsSubset(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setIsSetValueMeasurement(ApplicationConstants.CONST_STRING_FALSE_SMALL);
                criteriaSetValue.setCriteriaSetId(matchedCriteriaSet.getCriteriaSetId());
                if (hasAliace) {
                    criteriaSetValue.setValue(material.getAlias());
                } else {
                    criteriaSetValue.setValue(material.getName());
                }

                if (hasBlend || hasCombo) {
                    List<CriteriaSetCodeValues> criteriaSetCodeValues = new ArrayList<CriteriaSetCodeValues>();
                    if (hasCombo) {
                        criteriaSetCodeValues.addAll(getComboMaterial(material.getCombo(), criteriaSetValue.getId(),
                                setCodeValueId, existingProduct.getID(), existingProduct.getExternalProductId(), String.valueOf(criteriaSetValue.getValue())));
                    }
                    if (hasBlend) {
                        criteriaSetCodeValues.add(getBlendMaterials(material.getBlendMaterials(), criteriaSetValue.getId(),
                                setCodeValueId, existingProduct.getID(), null));
                    }
                    criteriaSetValue.setCriteriaSetCodeValues(criteriaSetCodeValues.toArray(new CriteriaSetCodeValues[0]));
                } else {
                    criteriaSetValue.setCriteriaSetCodeValues(getCriteriaSetCodeValues(setCodeValueId, criteriaSetValue.getId()));
                }
            }
            try {
                if (needToCheckAgain) {
                    String key = getKeyForComoboOrBlendMaterial(criteriaSetValue);
                    if (key != null) {
                        key = String.valueOf(criteriaSetValue.getValue()).toUpperCase() + "_" + key;
                        if (existingValueMap.get(key) != null) {
                            criteriaSetValue = existingValueMap.get(key);
                        }
                    }
                }
            } catch (Exception e) {}// Nothing to for now
            finalCriteriaSetValues.add(criteriaSetValue);
            updateReferenceTable(
                    existingProduct.getExternalProductId(),
                    ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE,
                    processSourceCriteriaValueByCriteriaCode(hasAliace ? material.getAlias() : material.getName(),
                            ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE), criteriaSetValue);

        }

        matchedCriteriaSet.setCriteriaSetValues(finalCriteriaSetValues);

        LOGGER.info("Completed Processing of Product Materials");

        return matchedCriteriaSet;
    }

    protected CriteriaSetCodeValues[] getCriteriaSetCodeValues(String setCodeValueId, String setValueId) {
        CriteriaSetCodeValues criteriaSetCodeValue = new CriteriaSetCodeValues();

        criteriaSetCodeValue.setCriteriaSetValueId(setValueId);
        criteriaSetCodeValue.setSetCodeValueId(setCodeValueId);
        criteriaSetCodeValue.setCodeValue("");
        criteriaSetCodeValue.setId(String.valueOf(--criteriaSetCodeValueObjectId));

        return new CriteriaSetCodeValues[] { criteriaSetCodeValue };
    }

    private List<CriteriaSetCodeValues> getComboMaterial(Combo materialCombo, String criteriaSetValueId,
            String parentSetCodeValueId, String productId, String xid, String parentName) {
        boolean isBlendCombo = (materialCombo.getBlendMaterials() != null && !materialCombo.getBlendMaterials().isEmpty());

        List<CriteriaSetCodeValues> setCodeValues = new ArrayList<CriteriaSetCodeValues>();

        CriteriaSetCodeValues parentCriteriaSetCodeValue = new CriteriaSetCodeValues();
        parentCriteriaSetCodeValue.setSetCodeValueId(parentSetCodeValueId);
        parentCriteriaSetCodeValue.setCodeValue("");
        parentCriteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValueId);

        if (isBlendCombo) {
            parentCriteriaSetCodeValue = getBlendMaterials(materialCombo.getBlendMaterials(), criteriaSetValueId,
                    parentSetCodeValueId, productId, parentCriteriaSetCodeValue);
            setCodeValues.add(parentCriteriaSetCodeValue);
        } else {
            parentCriteriaSetCodeValue.setId(String.valueOf(--criteriaSetCodeValueObjectId));
            setCodeValues.add(parentCriteriaSetCodeValue);
            String childCriteriaSetCodeValueId = getSetCodeValueId(materialCombo.getName());
            if (childCriteriaSetCodeValueId != null) {
                CriteriaSetCodeValues childSetCodeValue = new CriteriaSetCodeValues();
                childSetCodeValue.setCodeValue(materialCombo.getName());
                childSetCodeValue.setCriteriaSetValueId(criteriaSetValueId);
                childSetCodeValue.setSetCodeValueId(childCriteriaSetCodeValueId);
                childSetCodeValue.setId(String.valueOf(--criteriaSetCodeValueObjectId));

                setCodeValues.add(childSetCodeValue);
            } else {
                productDataStore.addErrorToBatchLogCollection(xid, ApplicationConstants.CONST_BATCH_ERR_INVALID_VALUE,
                        "Invalid combo material found for Material " + parentName);
            }
        }
        return setCodeValues;
    }

    private CriteriaSetCodeValues getBlendMaterials(List<BlendMaterial> blendMaterials, String criteriaSetValueId,
            String parentSetCodeValueId, String productId, CriteriaSetCodeValues parentCriteriaSetCodeValue) {

        if (parentCriteriaSetCodeValue == null) {
            parentCriteriaSetCodeValue = new CriteriaSetCodeValues();
            parentCriteriaSetCodeValue.setId(String.valueOf(--criteriaSetCodeValueObjectId));
            parentCriteriaSetCodeValue.setSetCodeValueId(parentSetCodeValueId);
            parentCriteriaSetCodeValue.setCodeValue("");
            parentCriteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValueId);
        }
        if (blendMaterials.size() > 2) {
            // TODO : Log Error more two materials not allowed
        }
        List<ChildCriteriaSetCodeValues> childCriteriaSetCodeValues = new ArrayList<ChildCriteriaSetCodeValues>();
        for (BlendMaterial bMaterial : blendMaterials) {
            String setCodeValueId = getSetCodeValueId(bMaterial.getName());
            if (setCodeValueId == null) {
                // TODO : Log Error Blend material not found
                continue;
            }
            // For more understanding please refer to Radar Response model

            ChildCriteriaSetCodeValue childCriteriaSetCodeValue = new ChildCriteriaSetCodeValue();
            childCriteriaSetCodeValue.setId(String.valueOf(--criteriaSetCodeValueObjectId));
            childCriteriaSetCodeValue.setCriteriaSetValueId(criteriaSetValueId);
            childCriteriaSetCodeValue.setCodeValue(bMaterial.getPercentage());
            childCriteriaSetCodeValue.setSetCodeValueId(setCodeValueId);

            ChildCriteriaSetCodeValues childCodeValues = new ChildCriteriaSetCodeValues();
            childCodeValues.setProductId(productId);
            childCodeValues.setParentCriteriaSetCodeValueId(parentCriteriaSetCodeValue.getId());
            childCodeValues.setChildCriteriaSetCodeValue(childCriteriaSetCodeValue);
            childCodeValues.setChildCriteriaSetCodeValueId(childCriteriaSetCodeValue.getId());

            // add elements to blend material collection
            childCriteriaSetCodeValues.add(childCodeValues);
        }

        parentCriteriaSetCodeValue.setChildCriteriaSetCodeValues(childCriteriaSetCodeValues
                .toArray(new ChildCriteriaSetCodeValues[0]));

        return parentCriteriaSetCodeValue;
    }

    private String getKeyForComoboOrBlendMaterial(CriteriaSetValues criteriaValue) {
        String key = null;
        if (criteriaValue != null) {
            key = ""; // avoid null check again
            CriteriaSetCodeValues[] setCodeValues = criteriaValue.getCriteriaSetCodeValues();
            for (CriteriaSetCodeValues setCodeValue : setCodeValues) {
                key = CommonUtilities.appendValue(key, setCodeValue.getSetCodeValueId(), "+");
                if (setCodeValue.getChildCriteriaSetCodeValues() != null && setCodeValue.getChildCriteriaSetCodeValues().length > 0) {
                    for (ChildCriteriaSetCodeValues childCriteriaSetCodeValue : setCodeValue.getChildCriteriaSetCodeValues()) {
                        if (childCriteriaSetCodeValue != null && childCriteriaSetCodeValue.getChildCriteriaSetCodeValue() != null) {
                            key = CommonUtilities.appendValue(key, childCriteriaSetCodeValue.getChildCriteriaSetCodeValue().getSetCodeValueId(), "+");                            
                        }
                    }
                }
            }
        }
        if (key != null && key.endsWith("+")) {
            key = key.substring(0, key.length() - 1);
        }
        return key;
    }
    protected boolean isValueIsValid(String value) {
        // For color no need to validate values
        return true;
    }

    public String getSetCodeValueId(String value) {
        // return ProductDataStore.getSetCodeValueIdForProductMaterial(value);
        return ProductDataStore.getMaterialSetCodeValueId(value);
    }

    @Override
    protected String[] processValues(String value) {
        // Add NP Check and all
        return CommonUtilities.trimArrayValues(value.split(ApplicationConstants.CONST_STRING_COMMA_SEP));
    }

    @Override
    protected boolean updateCriteriaSet(String value) {
        // if (value != null )
        return false;
    }

    private HashMap<String, CriteriaSetValues> createTableForExistingSetValue(List<CriteriaSetValues> setValues) {
        HashMap<String, CriteriaSetValues> tempHashMap = new HashMap<>();

        if (setValues != null && !setValues.isEmpty()) {
            for (CriteriaSetValues criteriaSetValue : setValues) {
                if (criteriaSetValue.getCriteriaSetCodeValues() != null && criteriaSetValue.getCriteriaSetCodeValues().length > 0) {
                    String setCodeValue = criteriaSetValue.getCriteriaSetCodeValues()[0].getSetCodeValueId(); // Check for AIOE
                    tempHashMap.put(String.valueOf(criteriaSetValue.getValue()).toUpperCase() + "_" + setCodeValue,
                            criteriaSetValue);
                    // Workaround for Blend and Combo checking
                    try {
                        tempHashMap.put(String.valueOf(criteriaSetValue.getValue()).toUpperCase() + "_" + getKeyForComoboOrBlendMaterial(criteriaSetValue), criteriaSetValue);
                    } catch (Exception e){}
                }
            }
        }

        return tempHashMap;
    }

    public boolean registerExistingValuesForReference(ProductCriteriaSets criteriaSet, String externalProductId) {
        if (criteriaSet == null) {
            return false;
        }
        LOGGER.info("Registering existing product Material values");
        if (criteriaSet.getCriteriaSetValues() != null && !criteriaSet.getCriteriaSetValues().isEmpty()) {
            for (CriteriaSetValues criteriaValues : criteriaSet.getCriteriaSetValues()) {
                if (criteriaValues.getCriteriaSetCodeValues().length != 0) {
                    String valueToRegister = null;
                    if (CommonUtilities.isValueNull(String.valueOf(criteriaValues.getValue()))) {
                        valueToRegister = doReverseLookup(criteriaValues.getCriteriaSetCodeValues()[0].getSetCodeValueId(),
                                criteriaSet.getCriteriaCode());
                    } else {
                        valueToRegister = String.valueOf(criteriaValues.getValue());
                    }
                    updateReferenceTable(externalProductId, ApplicationConstants.CONST_MATERIALS_CRITERIA_CODE, valueToRegister,
                            criteriaValues);
                }
            }
        }
        LOGGER.info("Completed registering existing product Material values");

        return false;
    }

    @Override
    protected ProductCriteriaSets getCriteriaSet(String values, ProductDetail existingProduct,
            ProductCriteriaSets matchedCriteriaSet, int currentSetValueId) {
        // TODO Auto-generated method stub
        return null;
    }

}
