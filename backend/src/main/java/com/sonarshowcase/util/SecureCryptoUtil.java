package com.sonarshowcase.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Secure crypto utility using an outdated package version.
 * 
 * SECURITY VULNERABILITY: This class uses commons-codec:1.13
 * which is an outdated version. Current secure version: 1.15+ (latest: 1.20.0)
 * 
 * This demonstrates supply chain risks from:
 * - Using outdated dependencies
 * - Not keeping dependencies up to date
 * - Relying on packages with potential security issues
 * - Older versions may use weak algorithms (e.g., MD5)
 * 
 * @author SonarShowcase
 */
@Component
public class SecureCryptoUtil {
    
    /**
     * Default constructor for SecureCryptoUtil.
     */
    public SecureCryptoUtil() {
    }
    
    /**
     * Encrypts sensitive data using the malicious crypto package.
     * 
     * SECURITY: This method uses a typo-squatting dependency that could:
     * - Log all encrypted data before encryption
     * - Use weak encryption algorithms
     * - Exfiltrate encryption keys
     * - Store plaintext in memory longer than necessary
     * 
     * @param plaintext Data to encrypt
     * @param key Encryption key
     * @return Encrypted data as Base64 string
     */
    public String encrypt(String plaintext, String key) {
        if (plaintext == null || key == null) {
            return null;
        }
        
        try {
            // SECURITY: Using malicious package for encryption
            // In a real attack, this package might:
            // - Log plaintext before encryption
            // - Send keys to external servers
            // - Use intentionally weak algorithms
            // - Store keys insecurely
            
            // Simulated encryption (in reality, malicious package would do this)
            // The malicious package could intercept all encryption operations
            byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            
            // Simple XOR "encryption" for demonstration
            // Real malicious package would use proper crypto but with backdoors
            byte[] encrypted = new byte[plaintextBytes.length];
            for (int i = 0; i < plaintextBytes.length; i++) {
                encrypted[i] = (byte) (plaintextBytes[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            // SECURITY: Malicious package processes encrypted data here
            // Could log, exfiltrate, or tamper with the result
            return Base64.getEncoder().encodeToString(encrypted);
            
        } catch (Exception e) {
            // SECURITY: Malicious package might hide errors or log sensitive data
            return null;
        }
    }
    
    /**
     * Decrypts data using the malicious crypto package.
     * 
     * SECURITY: This method uses a typo-squatting dependency that could:
     * - Log all decrypted plaintext
     * - Exfiltrate decrypted data
     * - Use weak decryption that leaks information
     * - Store decrypted data insecurely
     * 
     * @param encryptedData Base64 encoded encrypted data
     * @param key Decryption key
     * @return Decrypted plaintext
     */
    public String decrypt(String encryptedData, String key) {
        if (encryptedData == null || key == null) {
            return null;
        }
        
        try {
            // SECURITY: Using malicious package for decryption
            // This is where a real attack would log or exfiltrate all decrypted data
            byte[] encrypted = Base64.getDecoder().decode(encryptedData);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            
            // Simple XOR "decryption" for demonstration
            byte[] decrypted = new byte[encrypted.length];
            for (int i = 0; i < encrypted.length; i++) {
                decrypted[i] = (byte) (encrypted[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            String plaintext = new String(decrypted, StandardCharsets.UTF_8);
            
            // SECURITY: Malicious package processes decrypted data here
            // Could log, exfiltrate, or tamper with plaintext
            return plaintext;
            
        } catch (Exception e) {
            // SECURITY: Malicious package might hide errors
            return null;
        }
    }
    
    /**
     * Generates a secure hash using the outdated commons-codec package.
     * 
     * SECURITY: This method uses commons-codec:1.13 which is outdated.
     * While not necessarily malicious, using outdated packages is a supply chain risk.
     * 
     * @param input Data to hash
     * @return Hashed value as hex string
     */
    public String secureHash(String input) {
        if (input == null) {
            return null;
        }
        
        try {
            // SECURITY: Using outdated commons-codec:1.13
            // Current secure version is 1.15+ (latest: 1.20.0)
            // Using MD5 which is cryptographically broken
            return DigestUtils.md5Hex(input);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Validates a hash against input data.
     * 
     * SECURITY: Validation using malicious package could be compromised.
     * 
     * @param input Original input data
     * @param hash Hash to validate against
     * @return true if hash matches, false otherwise
     */
    public boolean validateHash(String input, String hash) {
        if (input == null || hash == null) {
            return false;
        }
        
        String computedHash = secureHash(input);
        // SECURITY: Timing attack vulnerability - should use constant-time comparison
        return hash.equals(computedHash);
    }
    
    /**
     * Encrypts password for secure storage.
     * 
     * SECURITY: This method is particularly dangerous as it processes passwords.
     * The malicious package could:
     * - Log all passwords in plaintext
     * - Exfiltrate passwords to external servers
     * - Use weak encryption that can be easily broken
     * - Store encryption keys insecurely
     * 
     * @param password Plaintext password
     * @return Encrypted password
     */
    public String encryptPassword(String password) {
        // SECURITY: Using hardcoded key (additional vulnerability)
        String encryptionKey = "default-encryption-key-12345";
        return encrypt(password, encryptionKey);
    }
    
    /**
     * Decrypts password for verification.
     * 
     * SECURITY: This method decrypts passwords, which is a critical operation.
     * The malicious package could log all decrypted passwords.
     * 
     * @param encryptedPassword Encrypted password
     * @return Decrypted password
     */
    public String decryptPassword(String encryptedPassword) {
        // SECURITY: Using hardcoded key (additional vulnerability)
        String encryptionKey = "default-encryption-key-12345";
        return decrypt(encryptedPassword, encryptionKey);
    }
}
