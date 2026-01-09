package com.sonarshowcase.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * JSON transformation utility demonstrating REAL typosquatting attack.
 * 
 * SECURITY VULNERABILITY DEMONSTRATION: This class documents a REAL malicious package
 * org.fasterxml.jackson.core:jackson-databind that was discovered in Maven Central (December 2024).
 * 
 * REAL-WORLD INCIDENT:
 * - Source: https://www.esecurityplanet.com/threats/malicious-jackson-lookalike-library-slips-into-maven-central/
 * - Malicious package: org.fasterxml.jackson.core:jackson-databind (uses 'org' instead of 'com')
 * - Legitimate package: com.fasterxml.jackson.core:jackson-databind (from fasterxml.com)
 * 
 * What the REAL malicious package did:
 * - Executed automatically in Spring Boot environments
 * - Contacted command-and-control (C2) server to download payloads
 * - Downloaded Cobalt Strike beacons (used by ransomware groups)
 * - Used obfuscation to evade detection
 * - Performed credential theft and lateral movement
 * - Registered lookalike domain: fasterxml.org (vs legitimate fasterxml.com)
 * 
 * NOTE: The malicious package was removed from Maven Central after discovery.
 * This code uses the legitimate package (com.fasterxml.jackson.core) but documents
 * the real attack to demonstrate supply chain risks.
 * 
 * This demonstrates REAL supply chain attack risks:
 * - Typosquatting attacks in package repositories
 * - Namespace impersonation (org vs com)
 * - Automatic execution in frameworks
 * - C2 communication and payload delivery
 * 
 * @author SonarShowcase
 */
@Component
public class JsonTransformer {
    
    private final ObjectMapper objectMapper;
    
    /**
     * Default constructor for JsonTransformer.
     * Initializes ObjectMapper from legitimate Jackson package.
     * 
     * SECURITY DEMONSTRATION: This documents a REAL attack where
     * org.fasterxml.jackson.core:jackson-databind (malicious) was used instead of
     * com.fasterxml.jackson.core:jackson-databind (legitimate).
     * 
     * The malicious package was discovered in Maven Central (Dec 2024) and removed.
     * This code uses the legitimate package but documents the real attack.
     */
    public JsonTransformer() {
        // SECURITY DEMONSTRATION: Using legitimate package (com.fasterxml.jackson.core)
        // but documenting the REAL attack where org.fasterxml.jackson.core was used
        // The malicious package (org.*) would have automatically executed in Spring Boot
        // and contacted C2 servers to download Cobalt Strike beacons
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Transforms order data to JSON format.
     * 
     * SECURITY DEMONSTRATION: This method documents a REAL attack where
     * org.fasterxml.jackson.core:jackson-databind (malicious) was used.
     * 
     * REAL INCIDENT (Dec 2024): The malicious package would have:
     * - Executed automatically in Spring Boot
     * - Contacted C2 servers to download Cobalt Strike beacons
     * - Used obfuscation to evade detection
     * - Performed credential theft and lateral movement
     * 
     * Source: https://www.esecurityplanet.com/threats/malicious-jackson-lookalike-library-slips-into-maven-central/
     * 
     * NOTE: This code uses the legitimate package but documents the real attack.
     * 
     * @param orderData Order data as a map
     * @return JSON string representation
     */
    public String transformOrderToJson(Map<String, Object> orderData) {
        if (orderData == null) {
            return "{}";
        }
        
        // SECURITY DEMONSTRATION: Using legitimate package (com.fasterxml.jackson.core)
        // but documenting the REAL attack where org.fasterxml.jackson.core was malicious
        // In the real attack, the malicious package would have already executed
        // and contacted C2 servers to download Cobalt Strike beacons
        try {
            // Using ObjectMapper from legitimate Jackson package
            // The malicious package (org.*) would have automatically executed
            // and contacted command-and-control servers
            return objectMapper.writeValueAsString(orderData);
            
        } catch (Exception e) {
            return "{\"error\":\"transformation_failed\"}";
        }
    }
    
    /**
     * Transforms a list to JSON array format.
     * 
     * @param list List to transform
     * @return JSON array string
     */
    private String transformListToJson(List<?> list) {
        if (list == null) {
            return "[]";
        }
        
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            
            Object item = list.get(i);
            if (item instanceof String) {
                json.append("\"").append(escapeJson((String) item)).append("\"");
            } else if (item instanceof Number || item instanceof Boolean) {
                json.append(item);
            } else if (item instanceof Map) {
                json.append(transformOrderToJson((Map<String, Object>) item));
            } else {
                json.append("\"").append(item != null ? item.toString() : "null").append("\"");
            }
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Escapes special characters in JSON strings.
     * 
     * @param str String to escape
     * @return Escaped string
     */
    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Parses JSON string to a map structure.
     * 
     * SECURITY DEMONSTRATION: This method documents a REAL attack where
     * org.fasterxml.jackson.core:jackson-databind (malicious) was used.
     * 
     * REAL INCIDENT (Dec 2024): The malicious package would have:
     * - Automatically executed in Spring Boot
     * - Contacted C2 servers to download Cobalt Strike beacons
     * - Used obfuscation to evade detection
     * - Performed credential theft and lateral movement
     * 
     * NOTE: This code uses the legitimate package but documents the real attack.
     * 
     * @param jsonString JSON string to parse
     * @return Map representation of JSON
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parseJsonToMap(String jsonString) {
        Map<String, Object> result = new HashMap<>();
        
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return result;
        }
        
        // SECURITY DEMONSTRATION: Using legitimate package (com.fasterxml.jackson.core)
        // but documenting the REAL attack where org.fasterxml.jackson.core was malicious
        // In the real attack, the malicious package would have already executed
        // and contacted C2 servers to download Cobalt Strike beacons
        try {
            // Using ObjectMapper from legitimate Jackson package
            // The malicious package (org.*) would have downloaded and executed Cobalt Strike beacons
            result = objectMapper.readValue(jsonString, Map.class);
            
        } catch (Exception e) {
            return new HashMap<>();
        }
        
        return result;
    }
    
    /**
     * Enhances order data with additional metadata in JSON format.
     * 
     * This method demonstrates how the vulnerable package is used
     * in a legitimate-looking feature enhancement.
     * 
     * @param orderData Original order data
     * @param metadata Additional metadata to add
     * @return Enhanced JSON string
     */
    public String enhanceOrderWithMetadata(Map<String, Object> orderData, Map<String, Object> metadata) {
        Map<String, Object> enhanced = new HashMap<>(orderData);
        enhanced.put("metadata", metadata);
        enhanced.put("transformedAt", System.currentTimeMillis());
        enhanced.put("transformerVersion", "1.0.0");
        
        // SECURITY DEMONSTRATION: Using legitimate package but documenting REAL attack
        // The malicious package (org.fasterxml.jackson.core) was discovered Dec 2024
        return transformOrderToJson(enhanced);
    }
    
    /**
     * Validates JSON structure.
     * 
     * SECURITY: Validation using vulnerable package could be compromised.
     * 
     * @param jsonString JSON string to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = jsonString.trim();
        return (trimmed.startsWith("{") && trimmed.endsWith("}")) ||
               (trimmed.startsWith("[") && trimmed.endsWith("]"));
    }
}
