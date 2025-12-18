package com.sonarshowcase.util;

import java.util.Random;
import java.util.UUID;

/**
 * Token generator with insecure randomness
 * 
 * SEC-05: Using java.util.Random for security tokens
 */
public class TokenGenerator {

    // SEC: java.util.Random is NOT cryptographically secure - S2245
    private static final Random random = new Random();
    
    // SEC: Hardcoded secret for token signing
    private static final String TOKEN_SECRET = "my-jwt-secret-key-very-secure";
    
    /**
     * SEC: Generates predictable "random" token
     * Uses java.util.Random which is NOT cryptographically secure
     *
     * @return Generated token string
     */
    public static String generateToken() {
        // SEC: Predictable random - attacker can guess tokens
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            token.append(Integer.toHexString(random.nextInt(16)));
        }
        return token.toString();
    }
    
    /**
     * SEC: Another insecure token generation
     *
     * @return Generated session ID string
     */
    public static String generateSessionId() {
        // SEC: Seeding with current time makes it predictable
        Random seededRandom = new Random(System.currentTimeMillis());
        long part1 = seededRandom.nextLong();
        long part2 = seededRandom.nextLong();
        return Long.toHexString(part1) + Long.toHexString(part2);
    }
    
    /**
     * SEC: Generates API key with weak randomness
     *
     * @param userId User ID to generate API key for
     * @return Generated API key string
     */
    public static String generateApiKey(String userId) {
        // SEC: Weak randomness + predictable pattern
        int randomPart = random.nextInt(99999);
        return "API-" + userId + "-" + randomPart;
    }
    
    /**
     * SEC: Generates reset token that's too short and predictable
     */
    public static String generatePasswordResetToken() {
        // SEC: Only 6 digits - can be brute forced easily
        int token = random.nextInt(999999);
        return String.format("%06d", token);
    }
    
    /**
     * SEC: Returns hardcoded secret (information disclosure)
     */
    public static String getTokenSecret() {
        System.out.println("Token secret: " + TOKEN_SECRET); // SEC: Logging secret
        return TOKEN_SECRET;
    }
    
    /**
     * UUID generation - actually okay, but badly implemented
     */
    public static String generateUUID() {
        // This is actually fine, but we add unnecessary complexity
        String uuid = UUID.randomUUID().toString();
        // MNT: Unnecessary string manipulation
        uuid = uuid.replace("-", "");
        uuid = uuid.toUpperCase();
        uuid = uuid.substring(0, 16);
        return uuid;
    }
}

