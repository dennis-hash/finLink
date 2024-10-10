package com.example.MCrypto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/register/admin",
                                "/api/auth/setPassword",
                                "/swagger-ui**",
                                "/swagger-ui/**",
                                "/v3/**",
                                "/images/**",
                                "/api/v1/auth/**",
                                "/api/property",
                                "/api/property/propertyId=*",
                                "/api/property/filter",
                                "/api/category",
                                "/api/handleCallback"
                        ).permitAll()
                        .requestMatchers(
                                "/api/property/propertyId={propertyId}/features/featureName={featureName}", //delete feature
                                "/api/payment/initialize", //init payments
                                "/api/payment/partnerTransactionID={partnerTransactionID}/status", //check payment status
                                "/api/property/userId={userId}/create", //create a new property
                                "/api/property/propertyId={propertyId}/update", //update property details
                                //"property/userId={userId}",
                                //"/api/property/propertyId={propertyId}/image",
                                "/api/property/propertyId={propertyId}"
                        ).hasAnyAuthority("VERIFIED USER","ADMIN")
                        .requestMatchers(
                                "/api/property/propertyId={propertyId}/approve",
                                "/api/property/propertyId={propertyId}/disapprove",
                                "/api/property/unpaid",
                                "/api/settings",
                                "/api/category/create",
                                "/api/settings/getInfo",
                                "/api/profile/getAll",
                                "/api/profile/userId={userId}/refute",
                                "/api/profile/userId={userId}/verify",
                                "/api/profile/unverified",
                                "/api/role/userId={userId}/update"
                        ).hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
