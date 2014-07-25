package com.asi.ext.api.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.util.CommonUtilities;
import com.asi.ext.api.integration.lookup.parser.LookupParser;
import com.asi.ext.api.product.transformers.ProductDataStore;
import com.asi.ext.api.util.JsonToLookupTableConverter;
import com.asi.ext.api.util.RestAPIProperties;

public class EnvironmentConfigurator implements InitializingBean {
	private final static Logger LOGGER = Logger
			.getLogger(EnvironmentConfigurator.class.getName());

    private final static String DEFAULT_ENVIRONMENT      = "stage";
    private final static String PROP_FILE_NAME           = "velocity-api.properties";

    private String              env;
    private Properties          restApiProps             = null;

	private RestTemplate lookupRestTemplate;
    public static boolean       isEnvironmentInitialized = false;

    public void initializeApp() {
        //

        try {
            if (CommonUtilities.isValueNull(env)) {
                env = DEFAULT_ENVIRONMENT;
            }
            restApiProps = PropertiesLoaderUtils.loadAllProperties(env + "/" + PROP_FILE_NAME);
            if (restApiProps == null || restApiProps.isEmpty()) {
                LOGGER.fatal("Failed to load required environment property file, please check mule run configurations ");
                isEnvironmentInitialized = false;
                throw new RuntimeException(
                        "Failed to load required environment property file, please check mule run configurations ");
            }
        } catch (IOException e) {
            isEnvironmentInitialized = false;
			LOGGER.fatal(
					"Failed to load required environment property file, please check mule run configurations ",
					e);
			throw new RuntimeException(
					"Failed to load required environment property file, please check mule run configurations",
					e);
        }
        RestAPIProperties.initialize(restApiProps);
        isEnvironmentInitialized = true;
		ProductDataStore.lookupRestTemplate = lookupRestTemplate;
		JsonToLookupTableConverter.lookupRestTemplate = lookupRestTemplate;
		LookupParser.lookupRestTemplate=lookupRestTemplate;
    }

    /**
     * @return the env
     */
    @Required
    public String getEnv() {
        return env;
    }

    /**
     * @param env
     *            the env to set
     */
    @Required
    public void setEnv(String env) {
        this.env = env;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeApp();

    }

	public RestTemplate getLookupRestTemplate() {
		return lookupRestTemplate;
	}

	@Required
	public void setLookupRestTemplate(RestTemplate lookupRestTemplate) {
		this.lookupRestTemplate = lookupRestTemplate;
	}
	
	

}
