package com.asi.core.product.config;

import org.springframework.beans.factory.annotation.Autowired;

//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class MethodSecurityConfig {
//extends GlobalMethodSecurityConfiguration {
    @Autowired
    private SecurityConfiguration securityConfig;
    
/*    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }*/
}
