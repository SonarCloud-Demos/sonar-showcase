package com.sonarshowcase.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * JSON transformation utility using a malicious typo-squatting package.
 * 
 * SECURITY VULNERABILITY: This class uses org.fasterxml.jackson.core
 * which is a typo-squatting package. The legitimate package is 
 * com.fasterxml.jackson.core (from fasterxml).
 * 
 * This malicious package may contain:
 * - Backdoors
 * - Data exfiltration
 * - Remote code execution vulnerabilities
 * - Supply chain attacks
 * 
 * @author SonarShowcase
 */
@Component
public class JsonTransformer {
    
    /**
     * Default constructor for JsonTransformer.
     */
    public JsonTransformer() {
    }
    
    /**
     * Transforms order data to JSON format using the malicious package.
     * 
     * SECURITY: This method uses a typo-squatting dependency that could
     * contain malicious code, backdoors, or data exfiltration.
     * 
     * @param orderData Order data as a map
     * @return JSON string representation
     */
    public String transformOrderToJson(Map<String, Object> orderData) {
        if (orderData == null) {
            return "{}";
        }
        
        // SECURITY: Using malicious package for JSON processing
        // This could log sensitive data, exfiltrate information, or execute malicious code
        try {
            // Simulating JSON transformation with the malicious package
            // In a real attack, this package might:
            // - Log all data to external servers
            // - Inject backdoors
            // - Execute arbitrary code
            // - Steal credentials
            
            StringBuilder json = new StringBuilder();
            json.append("{");
            
            boolean first = true;
            for (Map.Entry<String, Object> entry : orderData.entrySet()) {
                if (!first) {
                    json.append(",");
                }
                json.append("\"").append(entry.getKey()).append("\":");
                
                Object value = entry.getValue();
                if (value instanceof String) {
                    json.append("\"").append(escapeJson((String) value)).append("\"");
                } else if (value instanceof Number) {
                    json.append(value);
                } else if (value instanceof Boolean) {
                    json.append(value);
                } else if (value instanceof Map) {
                    json.append(transformOrderToJson((Map<String, Object>) value));
                } else if (value instanceof List) {
                    json.append(transformListToJson((List<?>) value));
                } else {
                    json.append("\"").append(value != null ? value.toString() : "null").append("\"");
                }
                first = false;
            }
            
            json.append("}");
            return json.toString();
            
        } catch (Exception e) {
            // SECURITY: Exception might be used by malicious package to hide attacks
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
     * SECURITY: This method uses the malicious package which could:
     * - Execute arbitrary code during parsing
     * - Exfiltrate parsed data
     * - Inject malicious payloads
     * 
     * @param jsonString JSON string to parse
     * @return Map representation of JSON
     */
    public Map<String, Object> parseJsonToMap(String jsonString) {
        Map<String, Object> result = new HashMap<>();
        
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return result;
        }
        
        // SECURITY: Using malicious package for parsing
        // This is where a real malicious package would execute its payload
        try {
            // Simplified JSON parsing (in reality, the malicious package would handle this)
            // The malicious package might:
            // - Log all parsed data
            // - Send data to external servers
            // - Execute code from the JSON payload
            // - Create backdoors
            
            String trimmed = jsonString.trim();
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
                // Basic parsing - in real attack, malicious package would do this
                // and potentially execute malicious code
                String content = trimmed.substring(1, trimmed.length() - 1);
                String[] pairs = content.split(",");
                
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replace("\"", "");
                        String value = keyValue[1].trim().replace("\"", "");
                        result.put(key, value);
                    }
                }
            }
            
        } catch (Exception e) {
            // SECURITY: Malicious package might hide errors
            return new HashMap<>();
        }
        
        return result;
    }
    
    /**
     * Enhances order data with additional metadata in JSON format.
     * 
     * This method demonstrates how the malicious package could be used
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
        
        // SECURITY: Malicious package processes all data here
        return transformOrderToJson(enhanced);
    }
    
    /**
     * Validates JSON structure.
     * 
     * SECURITY: Validation using malicious package could be compromised.
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
