package com.sonarshowcase;

import com.sonarshowcase.controller.AuthController;
import com.sonarshowcase.model.User;
import com.sonarshowcase.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Tests for AuthController - intentionally vulnerable JWT controller
 */
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    private AuthController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AuthController(userRepository);
    }

    private User activeUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setActive(true);
        return user;
    }

    @Test
    void testLogin_validCredentials() {
        String username = "testuser";
        String password = "testpass";
        when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(activeUser(username, password, "USER")));

        ResponseEntity<Map<String, String>> response = controller.login(username, password);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("token"));
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    void testLogin_emptyUsername() {
        String username = "";
        String password = "testpass";

        ResponseEntity<Map<String, String>> response = controller.login(username, password);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
    }

    @Test
    void testLogin_emptyPassword() {
        String username = "testuser";
        String password = "";

        ResponseEntity<Map<String, String>> response = controller.login(username, password);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void testLogin_bothEmpty() {
        String username = "";
        String password = "";

        ResponseEntity<Map<String, String>> response = controller.login(username, password);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void testVerifyToken_validToken() {
        // First login to get a valid token
        when(userRepository.findByUsername("user"))
            .thenReturn(Optional.of(activeUser("user", "pass", "USER")));
        ResponseEntity<Map<String, String>> loginResponse = controller.login("user", "pass");
        String token = "Bearer " + loginResponse.getBody().get("token");

        ResponseEntity<Map<String, String>> response = controller.verifyToken(token);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testVerifyToken_withoutBearerPrefix() {
        // Get a token
        when(userRepository.findByUsername("user"))
            .thenReturn(Optional.of(activeUser("user", "pass", "USER")));
        ResponseEntity<Map<String, String>> loginResponse = controller.login("user", "pass");
        String token = loginResponse.getBody().get("token");

        ResponseEntity<Map<String, String>> response = controller.verifyToken(token);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testVerifyToken_invalidToken() {
        String invalidToken = "invalid.token.here";

        ResponseEntity<Map<String, String>> response = controller.verifyToken(invalidToken);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void testVerifyToken_emptyToken() {
        String emptyToken = "";

        ResponseEntity<Map<String, String>> response = controller.verifyToken(emptyToken);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void testVerifyToken_noneAlgorithm() {
        // Create a token with "none" algorithm (vulnerability demonstration)
        String header = "{\"alg\":\"none\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"admin\",\"role\":\"admin\"}";

        String encodedHeader = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString(header.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        String encodedPayload = java.util.Base64.getUrlEncoder().withoutPadding()
            .encodeToString(payload.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        String noneToken = encodedHeader + "." + encodedPayload + ".";

        ResponseEntity<Map<String, String>> response = controller.verifyToken(noneToken);

        // Should accept "none" algorithm (vulnerability)
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().get("message").contains("none"));
    }

    @Test
    void testLogin_specialCharacters() {
        String username = "user@domain.com";
        String password = "p@$$w0rd!";
        when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(activeUser(username, password, "USER")));

        ResponseEntity<Map<String, String>> response = controller.login(username, password);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody().get("token"));
    }

    @Test
    void testLogin_unknownUser_rejected() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = controller.login("ghost", "whatever");

        assertEquals(401, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("error"));
        assertFalse(response.getBody().containsKey("token"));
    }

    @Test
    void testLogin_wrongPassword_rejected() {
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(activeUser("testuser", "correct", "USER")));

        ResponseEntity<Map<String, String>> response = controller.login("testuser", "wrong");

        assertEquals(401, response.getStatusCode().value());
        assertFalse(response.getBody().containsKey("token"));
    }

    @Test
    void testLogin_inactiveUser_rejected() {
        User inactiveUser = activeUser("testuser", "testpass", "USER");
        inactiveUser.setActive(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(inactiveUser));

        ResponseEntity<Map<String, String>> response = controller.login("testuser", "testpass");

        assertEquals(401, response.getStatusCode().value());
        assertFalse(response.getBody().containsKey("token"));
    }

    @Test
    void testLogin_claimsFromStoredRecord() {
        when(userRepository.findByUsername("adminuser"))
            .thenReturn(Optional.of(activeUser("adminuser", "adminpass", "ADMIN")));

        ResponseEntity<Map<String, String>> response = controller.login("adminuser", "adminpass");

        assertEquals(200, response.getStatusCode().value());
        String token = response.getBody().get("token");
        String[] parts = token.split("\\.");
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

        assertTrue(payloadJson.contains("\"role\":\"ADMIN\""));
        assertTrue(payloadJson.contains("adminuser"));
    }
}
