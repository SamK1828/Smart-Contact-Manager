// package com.smart.config;

// import org.springframework.beans.factory.ObjectProvider;
// import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.access.expression.SecurityExpressionHandler;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.ObjectPostProcessor;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.web.FilterInvocation;

// import jakarta.servlet.Filter;


// @Configuration
// @EnableWebSecurity
// public class MyConfig extends WebSecurityConfiguration {

//     // private final UserDetailsService userDetailsService;

//     // public MyConfig(UserDetailsService userDetailsService) {
//     //     this.userDetailsService = userDetailsService;
//     // }

//     // // This class can be used to configure web security settings
//     // @Bean
//     // public UserDetailsService getUserDetailsService() {
//     //     // Replace Object and implementation with your actual bean
//     //     return new UserDetailsServiceImpl();
//     // }
//     // @Bean
//     // public BCryptPasswordEncoder passwordEncoder() {
//     //     return new BCryptPasswordEncoder();
//     // }

//     // @Bean
//     // public DaoAuthenticationProvider authenticationProvider() {
//     //     DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//     //     daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//     //     daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//     //     return daoAuthenticationProvider;
//     // }

//     // @Override
//     // public void setFilterChainProxySecurityConfigurer(ObjectPostProcessor<Object> arg0,
//     //         ConfigurableListableBeanFactory arg1) throws Exception {
//     //     // TODO Auto-generated method stub
//     //     super.setFilterChainProxySecurityConfigurer(arg0, arg1);
//     // }

//     // @Override
//     // public Filter springSecurityFilterChain(ObjectProvider<HttpSecurity> arg0) throws Exception {
//     //     // TODO Auto-generated method stub
//     //     return super.springSecurityFilterChain(arg0);
//     // }

//     // @Override
//     // public SecurityExpressionHandler<FilterInvocation> webSecurityExpressionHandler() {
//     //     // TODO Auto-generated method stub
//     //     return super.webSecurityExpressionHandler();
//     // }

//     // //Configure Method
    
//     // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//     //     auth.authenticationProvider(authenticationProvider());
//     // }

// }
