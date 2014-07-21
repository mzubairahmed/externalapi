package com.asi.ext.api.integration.lookup.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.asi.ext.api.util.CommonUtilities;
import com.asi.service.product.client.vo.BasePriceDetails;
import com.asi.service.product.client.vo.DiscountList;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.PricesList;
import com.asi.service.product.client.vo.PricingItem;
import com.asi.service.product.client.vo.QuantityList;
import com.asi.service.product.client.vo.UpChargePriceDetails;
import com.asi.service.product.client.vo.parser.UpChargeLookup;

public class PricesParser {
    private CriteriaSetParser criteriaSetParser=new CriteriaSetParser(); 
    private ArrayList<String> rushCriteriaGroup=new ArrayList<String>(Arrays.asList("RUSH","PRTM","SDRU"));
CommonUtilities commonUtilities=new CommonUtilities();
    public List<Object> getPricesList(List<PriceGrid> priceGrids,String externalProductId) {
        ArrayList<Object> priceFinalList=null;
        PricesList pricesList=new PricesList();
        List<Price> prices=null;
        QuantityList quantityList=new QuantityList();
        int noOfPriceGrids=0;
        DiscountList disountList=new DiscountList();
        
        for (PriceGrid priceGrid : priceGrids) {
            prices=priceGrid.getPrices();
            int priceCntr=1;
            if (priceGrid.getIsBasePrice()) {
                priceFinalList=new ArrayList<Object>();
                for (Price price : prices) {
                    switch (priceCntr) {
                case 1: 
                            pricesList.setP1((noOfPriceGrids == 0) ? price.getListPrice() + "" : pricesList.getP1() + "||"
                                    + price.getListPrice());
                            quantityList.setQ1((noOfPriceGrids == 0) ? price.getQuantity() + "" : quantityList.getQ1() + "||"
                                    + price.getQuantity());
                            disountList.setD1((noOfPriceGrids == 0) ? price.getDiscountRate().getIndustryDiscountCode()
                                    : disountList.getD1() + "||" + price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 2: 
                    pricesList.setP2(price.getListPrice()+"");
                    quantityList.setQ2(price.getQuantity()+"");
                    disountList.setD2(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 3: 
                    pricesList.setP3(price.getListPrice()+"");
                    quantityList.setQ3(price.getQuantity()+"");
                    disountList.setD3(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 4: 
                    pricesList.setP4(price.getListPrice()+"");
                    quantityList.setQ4(price.getQuantity()+"");
                    disountList.setD4(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 5: 
                    pricesList.setP5(price.getListPrice()+"");
                    quantityList.setQ5(price.getQuantity()+"");
                    disountList.setD5(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 6: 
                    pricesList.setP6(price.getListPrice()+"");
                    quantityList.setQ6(price.getQuantity()+"");
                    disountList.setD6(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 7: 
                    pricesList.setP7(price.getListPrice()+"");
                    quantityList.setQ7(price.getQuantity()+"");
                    disountList.setD7(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 8: 
                    pricesList.setP8(price.getListPrice()+"");
                    quantityList.setQ8(price.getQuantity()+"");
                    disountList.setD8(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 9: 
                    pricesList.setP9(price.getListPrice()+"");
                    quantityList.setQ9(price.getQuantity()+"");
                    disountList.setD9(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                case 10: 
                    pricesList.setP10(price.getListPrice()+"");
                    quantityList.setQ10(price.getQuantity()+"");
                    disountList.setD10(price.getDiscountRate().getIndustryDiscountCode());
                    priceCntr++;
                    break;
                default:
                    priceCntr=1;
                    break;
                }
            }
            priceFinalList.add(pricesList);
            priceFinalList.add(quantityList);
            priceFinalList.add(disountList);
            }
            noOfPriceGrids++;
        }
        return priceFinalList;
    }

 /*   public ProcessProductsList setPricingItem(int pricingColumn, ProcessProductsList tempProduct, String[] listOfPs,
            String[] listOfQs, String[] listOfDs, int repeatableRow) {
        PricesList pricesList=new PricesList();
        QuantityList quantityList=new QuantityList();
        DiscountList discountList=new DiscountList();
        switch (pricingColumn) {
        case 1: 
                pricesList.setP1(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ1(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD1(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 2: 
                pricesList.setP2(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ2(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD2(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 3: 
                pricesList.setP3(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ3(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD3(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 4: 
                pricesList.setP4(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ4(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD4(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 5: 
                pricesList.setP5(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ5(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD5(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 6: 
                pricesList.setP6(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ6(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD6(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 7: 
                pricesList.setP7(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ7(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD7(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 8: 
                pricesList.setP8(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ8(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD8(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 9: 
                pricesList.setP9(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ9(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD9(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
        case 10: 
                pricesList.setP10(commonUtilities.checkAndSet(listOfPs, repeatableRow));
                quantityList.setQ10(commonUtilities.checkAndSet(listOfQs, repeatableRow));
                discountList.setD10(commonUtilities.checkAndSet(listOfDs, repeatableRow));
            break;
            default:
                break;
        }
        return tempProduct;
    }
*/
    public String formatCriteriaValue(String srcString, String criteriaCode) {
        if (srcString != null) {
            if (criteriaCode != null && criteriaCode.equalsIgnoreCase("IMSZ")) {
			srcString=srcString.replaceAll("\\|", "-");
            } else if (criteriaCode != null && criteriaCode.equalsIgnoreCase("FOBP") && srcString.contains(",")) {
                srcString = "\"" + srcString + "\"";
		}
	}
	return srcString;
}

    public String[] getPriceCriteria(String externalProductId, List<PricingItem> pricingItems) {
		String criteriaSet1 = "";
		String criteriaSet2 = "";
		String[] criteriaSets = new String[] { "", "" };
		String criteriaCode = "";
		String temp = "", temp1 = "";
		String[] criteriaItems = null;
		int criteriaCntr = 0;
		for (PricingItem priceItem : pricingItems) {
			if (criteriaCntr == 0) {
                temp = criteriaSetParser.findCriteriaSetValueById(externalProductId, priceItem.getCriteriaSetValueId());
				if (null != temp) {
					criteriaItems = temp.split("__");
					if (criteriaItems.length > 1) {
						criteriaCode = criteriaItems[0];
						criteriaSet1 = criteriaCode + ":" + formatCriteriaValue(criteriaItems[1],criteriaCode);
					}
				}
			} else {
				// criteriaSet1=temp;
                temp = criteriaSetParser.findCriteriaSetValueById(externalProductId, priceItem.getCriteriaSetValueId());
				if (null != temp) {
					criteriaItems = temp.split("__");
					if (criteriaItems.length > 1) {
						if (criteriaItems[0].equalsIgnoreCase(criteriaCode)) {
                            criteriaSet1 = criteriaSet1 + "," + formatCriteriaValue(criteriaItems[1], criteriaItems[0]);
						} else if (temp1.equals("")) {
                            criteriaSet2 = criteriaItems[0] + ":" + formatCriteriaValue(criteriaItems[1], criteriaItems[0]);
							temp1 = criteriaItems[0];
					  } else {
                            criteriaSet2 = criteriaSet2 + "," + formatCriteriaValue(criteriaItems[1], criteriaItems[0]);
               	}
					}
				}
			}
			criteriaCntr++;
		}
		criteriaSets[0] = criteriaSet1;
		criteriaSets[1] = criteriaSet2;
		return criteriaSets;
	}
    
    public BasePriceDetails getBasePriceDetails(String externalProductId, PriceGrid priceGrid, boolean setCurrency,
            String firstCriteria, String secondCriteria) {
        BasePriceDetails basePriceDetails = new BasePriceDetails();
        basePriceDetails.setBasePriceName(priceGrid.getDescription());
        basePriceDetails.setPriceIncludes(priceGrid.getPriceIncludes());
        if (priceGrid.getIsQUR()) {
            basePriceDetails.setQUR("Y");
        } else {
            basePriceDetails.setQUR("");
        }
        List<String> pricesList = new ArrayList<String>();
        List<String> quantityList = new ArrayList<String>();
        List<String> discountList = new ArrayList<String>();
        for (Price p : priceGrid.getPrices()) {
            pricesList.add(p.getListPrice()+"");
            if(null!=p.getPriceUnitName() && !p.getPriceUnitName().isEmpty() && !p.getPriceUnitName().equalsIgnoreCase("piece"))            {
            	quantityList.add(p.getQuantity()+":"+p.getPriceUnitName()+":"+p.getItemsPerUnit());
            }else{
            	quantityList.add(p.getQuantity()+"");
            }
            discountList.add(p.getDiscountRate().getIndustryDiscountCode());
        }
        basePriceDetails.setPrices(pricesList);
        basePriceDetails.setQuantities(quantityList);
        basePriceDetails.setDiscounts(discountList);
        if (setCurrency && priceGrid.getCurrency() != null) {
            basePriceDetails.setCurrency(priceGrid.getCurrency().getCode());
        }
        
        // TODO : Call the function 
        String[] temp = getPriceCriteria(externalProductId, priceGrid.getPricingItems());
        String tempCode ="";
        if (temp != null && temp.length == 2) {
        	if (!CommonUtilities.isValueNull(temp[0]) && !CommonUtilities.isValueNull(firstCriteria)) {
        		if(rushCriteriaGroup.contains(firstCriteria) && !rushCriteriaGroup.contains(temp[0]) && rushCriteriaGroup.contains(temp[1])){
        			tempCode = temp[0];
        			temp[0] = temp[1];
        			temp[1] = tempCode;
        		}else if(rushCriteriaGroup.contains(secondCriteria) && rushCriteriaGroup.contains(temp[0]))
        		{
        				tempCode = temp[0];
            			temp[0] = temp[1];
            			temp[1] = tempCode;
        		}else if (!temp[0].startsWith(firstCriteria) && !rushCriteriaGroup.contains(firstCriteria)) {
        			tempCode = temp[0];
        			temp[0] = temp[1];
        			temp[1] = tempCode;
        		}
        	}
            basePriceDetails.setBasePriceCriteria1(CommonUtilities.isValueNull(temp[0]) ? "" : temp[0]);
            basePriceDetails.setBasePriceCriteria2(CommonUtilities.isValueNull(temp[1]) ? "" : temp[1]);
        }
        return basePriceDetails;
    }
	
    public UpChargePriceDetails getUpChargePriceDetails(String externalProductId,PriceGrid priceGrid, UpChargeLookup upChargeLookup) {
        
        UpChargePriceDetails upChargePriceDetails = new UpChargePriceDetails();
        upChargePriceDetails.setUpChargeName(priceGrid.getDescription());
        
        if (priceGrid.getIsQUR()) {
            upChargePriceDetails.setuQUR("Y");
        } else {
            upChargePriceDetails.setuQUR("");
        }
        List<String> pricesList = new ArrayList<String>();
        List<String> quantityList = new ArrayList<String>();
        List<String> discountList = new ArrayList<String>();
        for (Price p : priceGrid.getPrices()) {
            pricesList.add(p.getListPrice()+"");
            quantityList.add(p.getQuantity()+"");
            discountList.add(p.getDiscountRate().getIndustryDiscountCode());
        }
        upChargePriceDetails.setPrices(pricesList);
        upChargePriceDetails.setQuantities(quantityList);
        upChargePriceDetails.setDiscounts(discountList);
        
        String[] temp = getPriceCriteria(externalProductId, priceGrid.getPricingItems());
        if (temp != null && temp.length == 2) {
            upChargePriceDetails.setUpChargeCriteria1(CommonUtilities.isValueNull(temp[0]) ? "" : temp[0]);
            upChargePriceDetails.setUpChargeCriteria2(CommonUtilities.isValueNull(temp[1]) ? "" : temp[1]);
        }
        
        if (upChargeLookup != null) {
            upChargePriceDetails.setUpchargeType(upChargeLookup.getUpChargeType(priceGrid.getPriceGridSubTypeCode()));
            upChargePriceDetails.setUpchargeLevel(upChargeLookup.getUpchargeLevel(priceGrid.getUsageLevelCode()));
        }
        return upChargePriceDetails;
    }
    
}
