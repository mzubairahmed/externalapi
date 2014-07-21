package com.asi.core.product.config;



//@Configuration
//@EnableWebSecurity
public class SecurityConfiguration{
//	extends WebSecurityConfigurerAdapter {
//
//
// protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("customer1").password("customer1").roles("USER").and().withUser("asi")
//                .password("asi").roles("USER");
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
//    }
//
////    @Override
////    @Bean
////    public AuthenticationManager authenticationManagerBean() throws Exception {
////        return super.authenticationManagerBean();
////    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // @formatter:off
//                 http
//            .authorizeRequests().antMatchers("/api/product/**").permitAll().and()
//            .authorizeRequests()
//                .anyRequest().hasRole("USER").and().csrf().disable();
//                 
///*                .and()
//            .exceptionHandling()
//                .accessDeniedPage("/login.jsp?authorization_error=true")
//                .and()
//            // TODO: put CSRF protection back into this endpoint
//            .csrf()
//                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
//            .logout()
//                .logoutSuccessUrl("/index.jsp")
//                .logoutUrl("/logout.do")
//                .and()
//            .formLogin()
//                    .usernameParameter("j_username")
//                    .passwordParameter("j_password")
//                    .failureUrl("/login.jsp?authentication_error=true")
//                    .loginPage("/login.jsp")
//                    .loginProcessingUrl("/login.do");*/
//        // @formatter:on
//    }
}
