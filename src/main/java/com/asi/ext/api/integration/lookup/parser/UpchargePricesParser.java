package com.asi.ext.api.integration.lookup.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.asi.payload.vo.ProcessProductsList;
import com.asi.util.CommonUtilities;
import com.asi.util.json.IParser;
import com.asi.util.json.JSONParserImpl;
import com.asi.vo.Price;
import com.asi.vo.PriceGrid;
import com.asi.vo.UpchargeDiscountList;
import com.asi.vo.UpchargePricesList;
import com.asi.vo.UpchargeQuantityList;

public class UpchargePricesParser {
    
    private static IParser jsonParser = new JSONParserImpl();
    
	CommonUtilities commonUtilities=new CommonUtilities();
	public ArrayList<Object> getUpChargePricesList(List<PriceGrid> priceGrids) {
		ArrayList<Object> upChargePriceFinalList=null;
		UpchargePricesList upchargePricesList=new UpchargePricesList();
		List<Price> prices=null;
		UpchargeQuantityList upchargeQuantityList=new UpchargeQuantityList();
		UpchargeDiscountList upchargeDisountList=new UpchargeDiscountList();
		for(PriceGrid priceGrid:priceGrids)
		{
			prices=priceGrid.getPrices();
			int priceCntr=1;
			if(!priceGrid.getIsBasePrice())
			{
				upChargePriceFinalList=new ArrayList<Object>();
			for(Price price:prices)
			{
				switch(priceCntr)
				{
				case 1: 
					upchargePricesList.setUP1(price.getListPrice()+"");
					upchargeQuantityList.setUQ1(price.getQuantity()+"");
					upchargeDisountList.setUD1(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 2: 
					upchargePricesList.setUP2(price.getListPrice()+"");
					upchargeQuantityList.setUQ2(price.getQuantity()+"");
					upchargeDisountList.setUD2(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 3: 
					upchargePricesList.setUP3(price.getListPrice()+"");
					upchargeQuantityList.setUQ3(price.getQuantity()+"");
					upchargeDisountList.setUD3(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 4: 
					upchargePricesList.setUP4(price.getListPrice()+"");
					upchargeQuantityList.setUQ4(price.getQuantity()+"");
					upchargeDisountList.setUD4(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 5: 
					upchargePricesList.setUP5(price.getListPrice()+"");
					upchargeQuantityList.setUQ5(price.getQuantity()+"");
					upchargeDisountList.setUD5(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 6: 
					upchargePricesList.setUP6(price.getListPrice()+"");
					upchargeQuantityList.setUQ6(price.getQuantity()+"");
					upchargeDisountList.setUD6(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 7: 
					upchargePricesList.setUP7(price.getListPrice()+"");
					upchargeQuantityList.setUQ7(price.getQuantity()+"");
					upchargeDisountList.setUD7(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 8: 
					upchargePricesList.setUP8(price.getListPrice()+"");
					upchargeQuantityList.setUQ8(price.getQuantity()+"");
					upchargeDisountList.setUD8(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 9: 
					upchargePricesList.setUP9(price.getListPrice()+"");
					upchargeQuantityList.setUQ9(price.getQuantity()+"");
					upchargeDisountList.setUD9(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				case 10: 
					upchargePricesList.setUP10(price.getListPrice()+"");
					upchargeQuantityList.setUQ10(price.getQuantity()+"");
					upchargeDisountList.setUD10(price.getDiscountRate().getIndustryDiscountCode());
					priceCntr++;
					break;
				default:
					priceCntr=1;
					break;
				}
			}
			upChargePriceFinalList.add(upchargePricesList);
			upChargePriceFinalList.add(upchargeQuantityList);
			upChargePriceFinalList.add(upchargeDisountList);
			}
		}
		return upChargePriceFinalList;
	}
	
	public ProcessProductsList setUpchargeItem(int pricingColumn,
			ProcessProductsList currentProduct, String[] listOfUPs,
			String[] listOfUQs, String[] listOfUDs, int repeatableRow) {
		UpchargePricesList pricesList=new UpchargePricesList();
		UpchargeQuantityList quantityList=new UpchargeQuantityList();
		UpchargeDiscountList discountList=new UpchargeDiscountList();
		switch(pricingColumn)
		{
		case 1: 
			pricesList
			.setUP1(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ1(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD1(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 2: 
			pricesList
			.setUP2(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ2(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD2(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 3: 
			pricesList
			.setUP3(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ3(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD3(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 4: 
			pricesList
			.setUP4(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ4(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD4(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 5: 
			pricesList
			.setUP5(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ5(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD5(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 6: 
			pricesList
			.setUP6(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ6(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD6(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 7: 
			pricesList
			.setUP7(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ7(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD7(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 8: 
			pricesList
			.setUP8(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ8(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD8(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 9: 
			pricesList
			.setUP9(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ9(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD9(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
		case 10: 
			pricesList
			.setUP10(commonUtilities.checkAndSet(listOfUPs,repeatableRow));
			quantityList
			.setUQ10(commonUtilities.checkAndSet(listOfUQs,repeatableRow));
			discountList
			.setUD10(commonUtilities.checkAndSet(listOfUDs,repeatableRow));
			break;
			default:
				break;
		}

		return currentProduct;
	}

    public ConcurrentHashMap<String, String> getUpchargeTypeCollectionFromJSON(String response) {
        ConcurrentHashMap<String, String> upchargeTypes = new ConcurrentHashMap<String, String>();
        LinkedList<?> jsonMap = (LinkedList<?>) jsonParser.parseToList(response);
        Iterator<?> iter = jsonMap.iterator();
        while (iter.hasNext()) {
            Map<?, ?> currentUpchargeType = (LinkedHashMap<?, ?>) iter.next();
            
            if (currentUpchargeType != null) {
                String key = currentUpchargeType.get("Code") != null ? (String) currentUpchargeType.get("Code") : null;
                String value = currentUpchargeType.get("DisplayName") != null ? (String) currentUpchargeType.get("DisplayName") : null;
                if (key != null && value != null) {
                    upchargeTypes.put(key, value);
                }
            }
            
        }
        return upchargeTypes;
    }

    public ConcurrentHashMap<String, String> getUsageLevelCollectionFromJSON(String response) {
        ConcurrentHashMap<String, String> usageLevels = new ConcurrentHashMap<String, String>();
        LinkedList<?> jsonMap = (LinkedList<?>) jsonParser.parseToList(response);
        Iterator<?> iter = jsonMap.iterator();
        while (iter.hasNext()) {
            Map<?, ?> currentUsageLevel = (LinkedHashMap<?, ?>) iter.next();
            
            if (currentUsageLevel != null) {
                String key = currentUsageLevel.get("Code") != null ? (String) currentUsageLevel.get("Code") : null;
                String value = currentUsageLevel.get("DisplayName") != null ? (String) currentUsageLevel.get("DisplayName") : null;
                if (key != null && value != null) {
                    usageLevels.put(key, value);
                }
            }
            
        }
        return usageLevels;
    }

}
