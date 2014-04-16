package com.asi.core.repo.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.ItemPriceDetail.PRICE_Type;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;


public class ProductRepo {
	private final static Logger _LOGGER = Logger
			.getLogger(ProductRepo.class.getName());
	/**
	 * @return the productClient
	 */
	public ProductClient getProductClient() {
		return productClient;
	}
	/**
	 * @param productClient the productClient to set
	 */
	public void setProductClient(ProductClient productClient) {
		this.productClient = productClient;
	}
	@Autowired  @Qualifier("productServiceClient") ProductClient productClient;
	@Autowired ProductDetail productDetail;
	@Autowired LookupValuesClient lookupColor;
	
	public Product getProductPrices(String companyID, Integer productID)
	{
	    
		productDetail = productClient.doIt(companyID,productID);
		Product product = new Product();
		BeanUtils.copyProperties(productDetail, product);
		
		List<PriceGrid> priceGrids = productDetail.getPriceGrids();
		List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();
		
		for(PriceGrid prices:priceGrids)
		{
			if(prices.getPriceGridSubTypeCode().equalsIgnoreCase(PRICE_Type.REGL.name()))
				pricesInfo.add(getBasePriceDetails(ItemPriceDetail.PRICE_Type.REGL,prices,false));
		}
		product.setItemPrices(pricesInfo);

		_LOGGER.debug("Color " + lookupColor.getColorFromLookup(lookupColor.getLookupColorURL()).toString());
		_LOGGER.debug("Sizes " + lookupColor.getSizesFromLookup(lookupColor.getLookupSizeURL()).toString());
		_LOGGER.debug("Material " + lookupColor.getMaterialFromLookup(lookupColor.getLookupMaterialURL()).toString());
		
		return product;
		
	}
    private ItemPriceDetail getBasePriceDetails(ItemPriceDetail.PRICE_Type priceType,PriceGrid priceGrid, boolean setCurrency) {
    	ItemPriceDetail itemPrices = new ItemPriceDetail();
    	itemPrices.setPriceType(priceType);
        itemPrices.setPriceName(priceGrid.getDescription());
        itemPrices.setPriceIncludes(priceGrid.getPriceIncludes());
        itemPrices.setPriceUponRequest(priceGrid.getIsQUR());
        List<PriceDetail> pricesList = new ArrayList<PriceDetail>();
        
        for (Price p : priceGrid.getPrices()) {
        	PriceDetail priceDetail = new PriceDetail();
        	
        	priceDetail.setPrice(p.getListPrice());
            priceDetail.setQuanty(p.getQuantity());
            priceDetail.setDiscount(p.getDiscountRate().getIndustryDiscountCode());
            priceDetail.setHighQuantity(p.getHighQuantity());
            priceDetail.setLowQuantity(p.getLowQuantity());
            priceDetail.setItemsPerUnit(p.getItemsPerUnit());
            priceDetail.setNetCost(p.getNetCost());
            priceDetail.setItemsPerUnitBy(p.getPriceUnit().getDisplayName());
            pricesList.add(priceDetail);
        }
        itemPrices.setPriceDetails(pricesList);
        

        return itemPrices;
    }

}