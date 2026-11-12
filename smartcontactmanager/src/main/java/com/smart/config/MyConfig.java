package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MyConfig {
    @Bean
    AuthenticationManager authenticationManager(
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    SecurityFilterChain applicationSecurity(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        return http
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/about", "/signup", "/do_register", "/login", "/css/**", "/js/**",
                                "/img/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/do_login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/user/dashboard", true)
                        .failureUrl("/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new LegacyAwarePasswordEncoder();
    }
}
