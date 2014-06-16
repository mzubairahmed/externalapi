package com.asi.service.product.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asi.service.product.client.vo.ProductDetail;
import com.asi.service.product.client.vo.parser.ProductConfigurationsParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-config-test.xml")
public class ProductConfigurationsParserTest {
	private final static Logger _LOGGER = Logger
			.getLogger(ProductConfigurationsParserTest.class.getName());
	@Autowired ProductConfigurationsParser productConfigurationsParser;
	ProductDetail testProduct=getProductDetails();
	private ProductDetail getProductDetails()
	{
		ProductDetail productDetail=null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			productDetail = mapper.readValue(this.getClass().getResource("/testproduct.json"), ProductDetail.class);
			_LOGGER.info("External product Id:"+productDetail.getExternalProductId());
		} catch (Exception e) {
			_LOGGER.info(e.getMessage());
		}
		return productDetail;
	}
	@Test
	public void testGetPriceCriteriaNullCheck()
	{
		Assert.assertNull(productConfigurationsParser.getPriceCriteria(null, null));
	}
	@Test
	public void testGetPriceCriteriaInvalidPriceGridId()
	{
		Assert.assertArrayEquals(new String[]{"",""}, productConfigurationsParser.getPriceCriteria(testProduct, "801456944"));
	}
	@Test
	public void testGetPriceCriteriaValidPriceGridId()
	{
		Assert.assertArrayEquals(new String[]{"MTRL:Other Fabric,Blend","IMMD:Unimprinted,Personalization"}, productConfigurationsParser.getPriceCriteria(testProduct, "801456966"));
	}

	
}
