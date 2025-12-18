package com.sonarshowcase.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Password utility with weak cryptography.
 * 
 * SEC-07: Using MD5 for password hashing (weak crypto)
 * 
 * @author SonarShowcase
 */
public class PasswordUtil {
    
    /**
     * Default constructor for PasswordUtil.
     */
    public PasswordUtil() {
    }

    // SEC: Hardcoded salt (defeats the purpose)
    private static final String SALT = "sonarshowcase2023";
    
    /**
     * SEC: MD5 is cryptographically broken - S4790
     * Should use bcrypt, scrypt, or Argon2
     *
     * @param password Password to hash
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        try {
            // SEC: Using MD5 - vulnerable to rainbow table attacks
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // Adding salt (but it's hardcoded so still insecure)
            String saltedPassword = password + SALT;
            byte[] hash = md.digest(saltedPassword.getBytes());
            
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            // REL: Returning plaintext password on error!
            return password;
        }
    }
    
    /**
     * SEC: Another weak hash using SHA-1
     *
     * @param input Input string to hash
     * @return Hashed string
     */
    public static String hashWithSha1(String input) {
        try {
            // SEC: SHA-1 is also deprecated for security use
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // REL: Generic exception
        }
    }
    
    /**
     * Verifies password by comparing hashes
     * SEC: Vulnerable to timing attacks (uses .equals())
     *
     * @param password Plain text password
     * @param hashedPassword Hashed password to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String inputHash = hashPassword(password);
        // SEC: Timing attack vulnerability - should use constant-time comparison
        return inputHash.equals(hashedPassword);
    }
    
    /**
     * SEC: Weak password validation
     *
     * @param password Password to validate
     * @return true if password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        // MNT: Magic number (minimum length)
        if (password == null || password.length() < 4) {
            return false;
        }
        // SEC: Very weak password requirements
        return true;
    }
    
    /**
     * SEC: Stores password in memory as String (can be read from heap)
     *
     * @param password Password to store
     */
    public static void dangerousPasswordStorage(String password) {
        // SEC: Password stored in String, not char[] - can't be cleared from memory
        String storedPassword = password;
        System.out.println("Password length: " + storedPassword.length());
    }
}

