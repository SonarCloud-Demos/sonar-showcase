package com.sonarshowcase.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice test for {@link SupplyChainScaController} — uses a benign payload only (no JNDI strings).
 */
@WebMvcTest(controllers = SupplyChainScaController.class, properties = "app.security.api-key=test-api-key")
class SupplyChainScaControllerTest {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String TEST_API_KEY = "test-api-key";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void log4jEchoAcceptsBenignPlainText() throws Exception {
        mockMvc.perform(
                        post("/api/v1/sca-demo/log4j-echo")
                                .header(API_KEY_HEADER, TEST_API_KEY)
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("logged"));
    }

    @Test
    void log4jEchoEmptyBodyReturnsOk() throws Exception {
        mockMvc.perform(post("/api/v1/sca-demo/log4j-echo")
                        .header(API_KEY_HEADER, TEST_API_KEY)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("logged"));
    }
}
