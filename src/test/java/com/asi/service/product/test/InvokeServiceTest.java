package com.asi.service.product.test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.asi.service.product.vo.Product;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-config-test.xml")
public class InvokeServiceTest {

	private static String url = "http://localhost:8080/productService/api/product/491276/prices/00-1069";
	//550023590";
	@Rule
	public ServerRunning serverRunning = ServerRunning.isNotRunning();

	@Test
	public void testConnectThroughClientAppJson() throws Exception {
		RestTemplate template = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_JSON}));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Product> result = template.exchange(url,HttpMethod.GET, new HttpEntity<String>(requestHeaders), Product.class);
	System.out.println("status Code:"+result.getStatusCode());
		assertEquals(result.getStatusCode(),HttpStatus.ACCEPTED);
		
	}
/*	@Test
	public void testConnectThroughClientAppXML() throws Exception {
		RestTemplate template = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		//requestHeaders.setAccept(Arrays.asList(new MediaType[] {MediaType.APPLICATION_XML}));
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<Product> result = template.exchange(url,HttpMethod.GET, new HttpEntity<String>(requestHeaders), Product.class);
		
		assertEquals(result.getStatusCode(),HttpStatus.ACCEPTED);
		
	}*/

}
