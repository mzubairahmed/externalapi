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
		
		product.setItemPrices(pricesInfo);

		return product;
		
	}
	public Product getProductPrices(String companyID, String productID,Integer priceGridID)
	{
	    
		productDetail = productClient.doIt(companyID,productID);
		
		List<PriceGrid> priceGrids = productDetail.getPriceGrids();
		List<ItemPriceDetail> pricesInfo = new ArrayList<ItemPriceDetail>();

		Product product = new Product();
		BeanUtils.copyProperties(productDetail, product);
		
		for(PriceGrid prices:priceGrids)
		{
			if(prices.getPriceGridSubTypeCode().equalsIgnoreCase(PRICE_Type.REGL.name()) && prices.getID().equals(priceGridID))
				pricesInfo.add(getBasePriceDetails(productDetail,ItemPriceDetail.PRICE_Type.REGL,prices,false));
		}
		if(pricesInfo.isEmpty())
			_LOGGER.error("Invalid price grid id or price grid not found for company %1 product %2 with priceGridId %3",companyID,productID,priceGridID);

		product.setItemPrices(pricesInfo);
		
		return product;
		
	}	
    private ItemPriceDetail getBasePriceDetails(ProductDetail productDetail,ItemPriceDetail.PRICE_Type priceType,PriceGrid priceGrid, boolean setCurrency) {
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
        String[] basePriceCriterias=productConfiguration.getPriceCriteria(productDetail,priceGrid.getID());
        if(null!=basePriceCriterias && basePriceCriterias.length>0)
        {
        	itemPrices.setFirstPriceCriteria(basePriceCriterias[0]);
        	if(basePriceCriterias.length>1)
        	{
        		itemPrices.setSecondPriceCriteria(basePriceCriterias[1]);
        	}
        	itemPrices.setPriceID(priceGrid.getID().toString());
        }
      return itemPrices;
    }
    public ProductConfigurationsParser getProductConfiguration() {
		return productConfiguration;
	}
	public void setProductConfiguration(
			ProductConfigurationsParser productConfiguration) {
		this.productConfiguration = productConfiguration;
	}

}