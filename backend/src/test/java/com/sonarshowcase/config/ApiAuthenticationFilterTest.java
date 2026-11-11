package com.sonarshowcase.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for WebConfig.ApiAuthenticationFilter - default-deny filter protecting /api/**
 */
class ApiAuthenticationFilterTest {

    @Test
    void testMissingKey_isRejected() throws Exception {
        WebConfig.ApiAuthenticationFilter filter = new WebConfig.ApiAuthenticationFilter("test-key");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertNull(chain.getRequest());
    }

    @Test
    void testCorrectKey_isAccepted() throws Exception {
        WebConfig.ApiAuthenticationFilter filter = new WebConfig.ApiAuthenticationFilter("test-key");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users");
        request.addHeader("X-API-Key", "test-key");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getRequest());
    }

    @Test
    void testWrongKey_isRejected() throws Exception {
        WebConfig.ApiAuthenticationFilter filter = new WebConfig.ApiAuthenticationFilter("test-key");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users");
        request.addHeader("X-API-Key", "wrong");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertNull(chain.getRequest());
    }

    @Test
    void testHealthEndpoint_isPublic() throws Exception {
        WebConfig.ApiAuthenticationFilter filter = new WebConfig.ApiAuthenticationFilter("test-key");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getRequest());
    }

    @Test
    void testNonApiPath_isPublic() throws Exception {
        WebConfig.ApiAuthenticationFilter filter = new WebConfig.ApiAuthenticationFilter("test-key");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getRequest());
    }

    @Test
    void testEmptyConfiguredKey_isRejected() throws Exception {
        WebConfig.ApiAuthenticationFilter filter = new WebConfig.ApiAuthenticationFilter("");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users");
        request.addHeader("X-API-Key", "");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
    }
}
