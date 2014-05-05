package com.asi.core.repo.product;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.asi.service.product.client.LookupValuesClient;
import com.asi.service.product.client.ProductClient;
import com.asi.service.product.client.vo.Price;
import com.asi.service.product.client.vo.PriceGrid;
import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.vo.ItemPriceDetail;
import com.asi.service.product.vo.ItemPriceDetail.PRICE_Type;
import com.asi.service.product.vo.PriceDetail;
import com.asi.service.product.vo.Product;
import com.asi.service.product.vo.ProductConfigurationsParser;


public class ProductRepo {
	private final static Logger _LOGGER = LoggerFactory.getLogger(ProductRepo.class);
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
	@Autowired ProductConfigurationsParser productConfiguration;
	
	public Product getProductPrices(String companyID, String productID)
	{
	    
		productDetail = productClient.doIt(companyID,productID);
		Product product = new Product();
		BeanUtils.copyProperties(productDetail, product);
		
		List<PriceGrid> priceGrids = productDetail.getPriceGrids();
		List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();
		
		for(PriceGrid prices:priceGrids)
		{
			if(prices.getPriceGridSubTypeCode().equalsIgnoreCase(PRICE_Type.REGL.name()))
				pricesInfo.add(getBasePriceDetails(productDetail,ItemPriceDetail.PRICE_Type.REGL,prices,false));
		}
		if(pricesInfo.isEmpty())
			_LOGGER.error("Invalid price grid id or price grid not found for company %1 product %2",companyID,productID);
		
		product.setItemPrice(pricesInfo);

		return product;
		
	}
	public Product getProductPrices(String companyID, String productID,Integer priceGridID)
	{
	    
		productDetail = productClient.doIt(companyID,productID);
		
		List<PriceGrid> priceGrids = productDetail.getPriceGrids();
		List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();
		ItemPriceDetail itemPrice;

		Product product = new Product();
		BeanUtils.copyProperties(productDetail, product);
		
		for(PriceGrid prices:priceGrids)
		{
			if(prices.getPriceGridSubTypeCode().equalsIgnoreCase(PRICE_Type.REGL.name()) && prices.getID().equals(priceGridID))
			{
				itemPrice = getBasePriceDetails(productDetail,ItemPriceDetail.PRICE_Type.REGL,prices,false);
				pricesInfo.add(itemPrice);
			}
		}
		if(pricesInfo.isEmpty())
			_LOGGER.error("Invalid price grid id or price grid not found for company %1 product %2 with priceGridId %3",companyID,productID,priceGridID);

		product.setItemPrice(pricesInfo);
		
		return product;
		
	}	
    private ItemPriceDetail getBasePriceDetails(ProductDetail productDetail,ItemPriceDetail.PRICE_Type priceType,PriceGrid priceGrid, boolean setCurrency) {
    	ItemPriceDetail itemPrice = new ItemPriceDetail();
    	itemPrice.setPriceType(priceType);
        itemPrice.setPriceName(priceGrid.getDescription());
        itemPrice.setPriceIncludes(priceGrid.getPriceIncludes());
        itemPrice.setPriceUponRequest(priceGrid.getIsQUR());
        List<PriceDetail> pricesList = new ArrayList<PriceDetail>();
        
        for (Price p : priceGrid.getPrices()) {
        	PriceDetail priceDetail = new PriceDetail();
        	priceDetail.setSequenceNumber(p.getSequenceNumber());
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
        itemPrice.setProductID(productDetail.getName());
        itemPrice.setPriceDetails(pricesList);
        String[] basePriceCriterias=productConfiguration.getPriceCriteria(productDetail,priceGrid.getID());
        if(null!=basePriceCriterias && basePriceCriterias.length>0)
        {
        	itemPrice.setFirstPriceCriteria(basePriceCriterias[0]);
        	if(basePriceCriterias.length>1)
        	{
        		itemPrice.setSecondPriceCriteria(basePriceCriterias[1]);
        	}
        	itemPrice.setPriceID(priceGrid.getID().toString());
        }
      return itemPrice;
    }
    public ProductConfigurationsParser getProductConfiguration() {
		return productConfiguration;
	}
	public void setProductConfiguration(
			ProductConfigurationsParser productConfiguration) {
		this.productConfiguration = productConfiguration;
	}

}