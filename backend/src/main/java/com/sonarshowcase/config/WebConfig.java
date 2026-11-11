package com.sonarshowcase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Web configuration with insecure CORS settings
 * 
 * SEC-08: CORS Wildcard - allows any origin
 * 
 * This configuration class sets up CORS (Cross-Origin Resource Sharing) settings
 * for the application. Note: The current configuration is intentionally insecure
 * for demonstration purposes.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Default constructor for Spring configuration.
     * Spring will use this constructor to create the configuration bean.
     */
    public WebConfig() {
        // Default constructor for Spring
    }

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
     * 
     * @return CorsFilter instance with permissive CORS configuration
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

    /**
     * Registers the API authentication filter so that every /api/** request
     * (apart from the explicitly public endpoints and CORS preflight) must
     * present a valid X-API-Key header before reaching any controller.
     *
     * @param apiKey the expected API key, injected from configuration
     * @return the filter registration bean
     */
    @Bean
    public FilterRegistrationBean<ApiAuthenticationFilter> apiAuthenticationFilter(
            @Value("${app.security.api-key:}") String apiKey) {
        FilterRegistrationBean<ApiAuthenticationFilter> registration =
                new FilterRegistrationBean<>(new ApiAuthenticationFilter(apiKey));
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE - 100);
        return registration;
    }

    /**
     * Default-deny authentication filter for the /api/** surface.
     *
     * Every request under /api is rejected with 401 unless it carries a valid
     * X-API-Key header matching the configured key, except for the explicitly
     * public endpoints (login, health) and CORS preflight (OPTIONS) requests.
     *
     * A custom header is used (rather than Authorization) so that the
     * intentionally vulnerable JWT demo in AuthController#verifyToken, which
     * reads the Authorization header, keeps working.
     */
    public static class ApiAuthenticationFilter extends OncePerRequestFilter {

        private static final String API_KEY_HEADER = "X-API-Key";

        private final byte[] expectedKey;

        public ApiAuthenticationFilter(String apiKey) {
            this.expectedKey = (apiKey == null || apiKey.isBlank())
                    ? null
                    : apiKey.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain filterChain) throws ServletException, IOException {
            String path = resolvePath(request);

            if (!path.equals("/api") && !path.startsWith("/api/")) {
                filterChain.doFilter(request, response);
                return;
            }

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            boolean isLogin = "POST".equalsIgnoreCase(request.getMethod()) && "/api/v1/auth/login".equals(path);
            boolean isHealth = "GET".equalsIgnoreCase(request.getMethod()) && "/api/v1/health".equals(path);
            if (isLogin || isHealth) {
                filterChain.doFilter(request, response);
                return;
            }

            String providedKey = request.getHeader(API_KEY_HEADER);
            if (expectedKey == null || providedKey == null
                    || !MessageDigest.isEqual(providedKey.getBytes(StandardCharsets.UTF_8), expectedKey)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);
        }

        private String resolvePath(HttpServletRequest request) {
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
                uri = uri.substring(contextPath.length());
            }

            StringBuilder cleaned = new StringBuilder();
            for (String segment : uri.split("/", -1)) {
                int semicolon = segment.indexOf(';');
                cleaned.append(semicolon >= 0 ? segment.substring(0, semicolon) : segment).append('/');
            }
            String normalized = cleaned.toString().replaceAll("/+", "/");
            if (normalized.length() > 1 && normalized.endsWith("/")) {
                normalized = normalized.substring(0, normalized.length() - 1);
            }
            return normalized.isEmpty() ? "/" : normalized;
        }
    }
}

