package com.asi.ext.api.util;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * {@link RestAPIProperties} contains configuration details of VelocityImport project, including 
 *  project properties for setting up the environment also loading all the required URLs for lookup and other services.
 *  <b>Modifying any functionalities may affect the entire project and can breaks the functionalities.</b> 
 *  <br />
 *  In this class we have some static initializers which will ensure that the all the required elements loaded before it gets called
 *  
 *  @author Rahul K, Shravan Kumar, Murali Ede
 *  @version 1.5
 *  @category Configuration
 */
public class RestAPIProperties {
	private final static Logger LOGGER = Logger
			.getLogger(RestAPIProperties.class);

	private static Properties restAPIProps;
	/**
	 * 
	 * Find the property value from the environment specific file
	 * 
	 * @param key is the property we need to lookup from the property file
	 * @return value of the property if exists otherwise null
	 */
	public static String get(String key){
		if (restAPIProps != null){
			return restAPIProps.getProperty(key).trim();
		}else{
			LOGGER.error("Propert file not loaded, returning null value");
			return null;
		}
	}
	
    public static void initialize(Properties restApiProps2) {
        restAPIProps = restApiProps2;
        
    }
}
