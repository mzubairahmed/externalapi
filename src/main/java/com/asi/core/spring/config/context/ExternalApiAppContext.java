/**
 * 
 */
package com.asi.core.spring.config.context;

import org.springframework.context.ApplicationContext;

/**
 * 
 * 
 * This class provides application-wide access to the Spring ApplicationContext.
 * 
 * The ApplicationContext is injected by the class "ExternalApiApplicationContextProvider".
 * 
 * Use this class when a class need to be loaded outside the spring managed class
 * 
 * @author Rahul K
 * 
 */
public class ExternalApiAppContext {

    private static ApplicationContext appContext;

    /**
     * @param appContext
     *            the appContext to set
     */
    public static void setAppContext(ApplicationContext appContext) {
        ExternalApiAppContext.appContext = appContext;
    }

    /**
     * This method will return the instance of class which is already loaded by Spring container.
     * Make sure proper class type and bean name are passing
     * 
     * @param beanName
     *            is the bean name which is registered in Spring container
     * @param classType
     *            is the Class type of bean. Miss match of class type and bean type cause {@link ClassCastException}
     * @return instance of bean or null if not found
     */
    public static <T> T getBean(String beanName, Class<T> classType) {
        if (appContext != null) {
            return appContext.getBean(beanName, classType);
        } else {
            // ApplicationContext not initialized
            return null;
        }
    }

    /**
     * This method will return the instance of class which is already loaded by Spring container.
     * Make sure proper class type specified
     * 
     * 
     * @param classType
     *            is the Class type of bean. Miss match of class type and bean type cause {@link ClassCastException}
     * @return instance of bean or null if not found
     */
    public static <T> T getBean(Class<T> classType) {
        if (appContext != null) {
            return appContext.getBean(classType);
        } else {
            // ApplicationContext not initialized
            return null;
        }
    }
}
