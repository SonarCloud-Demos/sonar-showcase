package com.sonarshowcase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration with insecure CORS settings
 * 
 * SEC-08: CORS Wildcard - allows any origin
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * SEC: Wildcard CORS - allows any origin to access API
     * This is a major security vulnerability
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // SEC: S5122 - Wildcard CORS
                .allowedMethods("*")  // SEC: Allows all HTTP methods
                .allowedHeaders("*")  // SEC: Allows all headers
                .exposedHeaders("*")
                .allowCredentials(false); // Can't use true with wildcard
    }
    
    /**
     * Alternative CORS filter with same issues
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // SEC: Even more permissive CORS
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

