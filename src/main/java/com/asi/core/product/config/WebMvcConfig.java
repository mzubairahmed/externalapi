package com.asi.core.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.asi.core.exception.ErrorMessageFactory;
import com.asi.core.exception.ErrorMessageHandlerExceptionResolver;
import com.asi.core.exception.HttpMediaTypeNotSupportedExceptionErrorMessageFactory;



@Configuration
//@PropertySource({"classpath:dev-environment.properties", "classpath:velocity-import.properties"})
public class WebMvcConfig extends WebMvcConfigurationSupport {
	@Bean(name="errorMessageHandlerExceptionResolver")
    public ErrorMessageHandlerExceptionResolver createErrorMessageHandlerExceptionResolver() {
		ErrorMessageHandlerExceptionResolver r =
              new ErrorMessageHandlerExceptionResolver();
		ErrorMessageFactory<?>[] errorMessageFactories = new ErrorMessageFactory<?>[1];
		HttpMediaTypeNotSupportedExceptionErrorMessageFactory hmtException =new HttpMediaTypeNotSupportedExceptionErrorMessageFactory();
		errorMessageFactories[0]=hmtException;

		r.setErrorMessageFactories(errorMessageFactories);

        return r;
    }
//	 @Bean
//	 public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//	  RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
//	  handlerMapping.setUseSuffixPatternMatch(false);
//	  handlerMapping.setUseTrailingSlashMatch(false);
//	  return handlerMapping;
//	 } 
	/*
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
		 
	@Bean
	public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
		ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
		List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
		internalResourceViewResolver.setSuffix(".jsp");
		viewResolvers.add(new org.springframework.web.servlet.view.BeanNameViewResolver());
		viewResolvers.add(internalResourceViewResolver);
		contentViewResolver.setViewResolvers(viewResolvers);
		contentViewResolver.setDefaultViews(Arrays.<View> asList(new MappingJackson2JsonView()));
		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManager.addMediaType("html", MediaType.TEXT_HTML);
		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
		
		contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
		
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
   public com.asi.service.product.client.ProductClient productServiceClient(@Value("${ws.api.product.import}")
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
   @Bean
   public com.asi.service.product.client.LookupValuesClient lookupValuesClient(@Value("${lookup.color.url}") String lookupColorURL,
		   @Value("${lookup.sizes.url}") String lookupSizeURL,
		   @Value("${lookup.material.url}") String lookupMaterialURL,
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
   
  
//
//    @Bean
//    public AccessConfirmationController accessConfirmationController(ClientDetailsService clientDetailsService, ApprovalStore approvalStore) {
//        AccessConfirmationController accessConfirmationController = new AccessConfirmationController();
//        accessConfirmationController.setClientDetailsService(clientDetailsService);
//        accessConfirmationController.setApprovalStore(approvalStore);
//        return accessConfirmationController;
//    }
//
//
//	@Bean
//	public AdminController adminController(TokenStore tokenStore, ConsumerTokenServices tokenServices,
//			AsiUserApprovalHandler userApprovalHandler) {
//		AdminController adminController = new AdminController();
//		adminController.setTokenStore(tokenStore);
//		adminController.setTokenServices(tokenServices);
//		adminController.setUserApprovalHandler(userApprovalHandler);
//		return adminController;
//	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
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
