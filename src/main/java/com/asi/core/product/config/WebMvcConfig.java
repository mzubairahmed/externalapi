package com.asi.core.product.config;




public class WebMvcConfig{
//	extends WebMvcConfigurerAdapter {

	/*
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
		 
	@Bean
	public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
		ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
		contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
		contentViewResolver.setDefaultViews(Arrays.<View> asList(new MappingJackson2JsonView()));
		return contentViewResolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	    
 @Bean
   public com.asi.service.product.client.vo.ProductDetail productDetail()
   {
	   com.asi.service.product.client.vo.ProductDetail prod = new com.asi.service.product.client.vo.ProductDetail();
	   return prod;
	   
   }
  
   @Bean
   public com.asi.service.product.client.ProductClient productServiceClient(@Value("${ws_api_product_import}")
	String productSearchUrl, @Qualifier("restTemplate") RestOperations restTemplate)
   {
	   com.asi.service.product.client.ProductClient productClient = new com.asi.service.product.client.ProductClient();
	   
	   productClient.setProductSearchUrl(productSearchUrl);
	   return productClient;
   }
   @Bean
   public ProductRepo productRepo(@Qualifier("productServiceClient") ProductClient productClient)
   {
	   ProductRepo productRepo = new ProductRepo();
	   productRepo.setProductClient(productClient);
	   return productRepo;
   }
   
   public com.asi.service.product.client.LookupValuesClient lookupValuesClient(@Value("${lookup_color_url}") String lookupColorURL,
		   @Value("${lookup_sizes_url}") String lookupSizeURL,
		   @Value("${lookup_material_url}") String lookupMaterialURL,
		   @Qualifier("restTemplate")  RestOperations lookupRestTemplate)
	{
	   com.asi.service.product.client.LookupValuesClient lookupClient = new com.asi.service.product.client.LookupValuesClient();
	   lookupClient.setLookupColorURL(lookupColorURL);
	   lookupClient.setLookupMaterialURL(lookupMaterialURL);
	   lookupClient.setLookupSizeURL(lookupSizeURL);
	   lookupClient.setLookupRestTemplate(lookupRestTemplate);
	   return lookupClient;
	}
   
   
   //productSearchService public ProductSearchService productSearchService(@Value("${tes}")
   
  

    @Bean
    public AccessConfirmationController accessConfirmationController(ClientDetailsService clientDetailsService, ApprovalStore approvalStore) {
        AccessConfirmationController accessConfirmationController = new AccessConfirmationController();
        accessConfirmationController.setClientDetailsService(clientDetailsService);
        accessConfirmationController.setApprovalStore(approvalStore);
        return accessConfirmationController;
    }


	@Bean
	public AdminController adminController(TokenStore tokenStore, ConsumerTokenServices tokenServices,
			AsiUserApprovalHandler userApprovalHandler) {
		AdminController adminController = new AdminController();
		adminController.setTokenStore(tokenStore);
		adminController.setTokenServices(tokenServices);
		adminController.setUserApprovalHandler(userApprovalHandler);
		return adminController;
	}


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new BufferedImageHttpMessageConverter());
	}
	protected static class ResourceConfiguration {
		@Bean
		 public org.springframework.web.client.RestTemplate restTemplate()
		{
			org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
			org.springframework.http.converter.json.MappingJackson2HttpMessageConverter jsonConverter = new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter();
			jsonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
			org.springframework.http.converter.xml.SourceHttpMessageConverter<?> xmlConverter = new org.springframework.http.converter.xml.SourceHttpMessageConverter<>();
			xmlConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_XML));
			restTemplate.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(jsonConverter, xmlConverter));
			return restTemplate;
		}
	}
	*/
}
