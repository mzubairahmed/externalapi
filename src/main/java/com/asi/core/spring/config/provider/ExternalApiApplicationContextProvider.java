/**
 * 
 */
package com.asi.core.spring.config.provider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.asi.core.spring.config.context.ExternalApiAppContext;

/**
 * @author Rahul K
 * 
 */
public class ExternalApiApplicationContextProvider implements ApplicationContextAware {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        ExternalApiAppContext.setAppContext(appContext);

    }

}
