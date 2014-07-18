package com.asi.ext.api.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.asi.ext.api.util.RestAPIProperties;

public class EnvironmentConfigurator {
    private final static Logger LOGGER                   = Logger.getLogger(EnvironmentConfigurator.class.getName());

    private String              propFileLoc;
    private Properties          restApiProps             = null;

    public static boolean       isEnvironmentInitialized = false;

    public void initializeApp() {
        //

        try {
            restApiProps = PropertiesLoaderUtils.loadAllProperties(propFileLoc);
            if (restApiProps == null || restApiProps.isEmpty()) {
                LOGGER.fatal("Failed to load required environment property file, please check mule run configurations ");
                isEnvironmentInitialized = false;
                throw new RuntimeException(
                        "Failed to load required environment property file, please check mule run configurations ");
            }
        } catch (IOException e) {
            isEnvironmentInitialized = false;
            LOGGER.fatal("Failed to load required environment property file, please check mule run configurations ", e);
            throw new RuntimeException("Failed to load required environment property file, please check mule run configurations", e);
        }
        RestAPIProperties.initialize(restApiProps);
        isEnvironmentInitialized = true;
    }

    /**
     * @return the propFileLoc
     */
    @Required
    public String getPropFileLoc() {
        return propFileLoc;
    }

    /**
     * @param propFileLoc
     *            the propFileLoc to set
     */
    @Required
    public void setPropFileLoc(String propFileLoc) {
        this.propFileLoc = propFileLoc;
    }

}
