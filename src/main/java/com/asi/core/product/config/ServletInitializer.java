package com.asi.core.product.config;


public class ServletInitializer { //extends AbstractDispatcherServletInitializer{
//	private static Logger LOG = LoggerFactory.getLogger(ServletInitializer.class);
/*	@Override
	protected WebApplicationContext createServletApplicationContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(ControllerConfig.class);
		
		//context.scan("com.asi"); //ClassUtils.getPackageName(getClass())
		return context;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		return null;
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		registerProxyFilter(servletContext, "springSecurityFilterChain");
		//registerProxyFilter(servletContext, "oauth2ClientContextFilter");
		super.onStartup(servletContext);
	}

	private void registerProxyFilter(ServletContext servletContext, String name) {
		DelegatingFilterProxy filter = new DelegatingFilterProxy(name);
		filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
		servletContext.addFilter(name, filter).addMappingForUrlPatterns(null, false, "/*");
	}*/
}
