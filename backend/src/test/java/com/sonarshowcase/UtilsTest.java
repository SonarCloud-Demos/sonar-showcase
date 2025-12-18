package com.sonarshowcase;

import com.sonarshowcase.util.Utils;
import com.sonarshowcase.util.PasswordUtil;
import com.sonarshowcase.util.TokenGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for utility classes to achieve proper test coverage.
 */
class UtilsTest {

    // ==================== Utils.doStuff tests ====================
    
    @Test
    @DisplayName("doStuff should convert input to uppercase")
    void testDoStuff_convertsToUppercase() {
        String result = Utils.doStuff("hello");
        assertEquals("HELLO", result);
    }
    
    @Test
    @DisplayName("doStuff should handle null input")
    void testDoStuff_handlesNull() {
        String result = Utils.doStuff(null);
        assertEquals("null input", result);
    }
    
    @Test
    @DisplayName("doStuff should handle empty string")
    void testDoStuff_handlesEmptyString() {
        String result = Utils.doStuff("");
        assertEquals("", result);
    }
    
    @Test
    @DisplayName("doStuff should handle already uppercase string")
    void testDoStuff_alreadyUppercase() {
        String result = Utils.doStuff("HELLO");
        assertEquals("HELLO", result);
    }
    
    @Test
    @DisplayName("doStuff should handle mixed case string")
    void testDoStuff_mixedCase() {
        String result = Utils.doStuff("HeLLo WoRLd");
        assertEquals("HELLO WORLD", result);
    }
    
    // ==================== Utils.calculate tests ====================
    
    @Test
    @DisplayName("calculate should add two positive numbers")
    void testCalculate_positiveNumbers() {
        int result = Utils.calculate(5, 3);
        assertEquals(8, result);
    }
    
    @Test
    @DisplayName("calculate should handle negative numbers")
    void testCalculate_negativeNumbers() {
        int result = Utils.calculate(-5, -3);
        assertEquals(-8, result);
    }
    
    @Test
    @DisplayName("calculate should handle zero")
    void testCalculate_withZero() {
        int result = Utils.calculate(5, 0);
        assertEquals(5, result);
    }
    
    @Test
    @DisplayName("calculate should handle large numbers")
    void testCalculate_largeNumbers() {
        int result = Utils.calculate(1000000, 2000000);
        assertEquals(3000000, result);
    }
    
    // ==================== Utils.processData tests ====================
    
    @Test
    @DisplayName("processData should repeat characters with flag true")
    void testProcessData_withFlagTrue() {
        String result = Utils.processData("abc", 6, true);
        assertEquals("abcabc", result);
    }
    
    @Test
    @DisplayName("processData should reverse with flag false")
    void testProcessData_withFlagFalse() {
        String result = Utils.processData("abc", 3, false);
        assertEquals("cba", result);
    }
    
    @Test
    @DisplayName("processData should handle empty result")
    void testProcessData_zeroLength() {
        String result = Utils.processData("abc", 0, true);
        assertEquals("", result);
    }
    
    // ==================== Utils.doSomething tests ====================
    
    @Test
    @DisplayName("doSomething should handle non-null object")
    void testDoSomething_withObject() {
        // This method prints to console, we just verify it doesn't throw
        assertDoesNotThrow(() -> Utils.doSomething("test object"));
    }
    
    @Test
    @DisplayName("doSomething should handle null object")
    void testDoSomething_withNull() {
        assertDoesNotThrow(() -> Utils.doSomething(null));
    }
    
    // ==================== Utils.getUsers tests ====================
    
    @Test
    @DisplayName("getUsers should return empty list")
    void testGetUsers_returnsEmptyList() {
        List<String> result = Utils.getUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    // ==================== Utils.processItems tests ====================
    
    @Test
    @DisplayName("processItems should process all items")
    void testProcessItems_withItems() {
        List<String> items = Arrays.asList("item1", "item2", "item3");
        assertDoesNotThrow(() -> Utils.processItems(items));
    }
    
    @Test
    @DisplayName("processItems should handle empty list")
    void testProcessItems_emptyList() {
        List<String> items = new ArrayList<>();
        assertDoesNotThrow(() -> Utils.processItems(items));
    }
    
    // ==================== Utils.isPositive tests ====================
    
    @Test
    @DisplayName("isPositive should return true for positive numbers")
    void testIsPositive_positiveNumber() {
        assertTrue(Utils.isPositive(5));
        assertTrue(Utils.isPositive(1));
        assertTrue(Utils.isPositive(100));
    }
    
    @Test
    @DisplayName("isPositive should return false for zero")
    void testIsPositive_zero() {
        assertFalse(Utils.isPositive(0));
    }
    
    @Test
    @DisplayName("isPositive should return false for negative numbers")
    void testIsPositive_negativeNumbers() {
        assertFalse(Utils.isPositive(-1));
        assertFalse(Utils.isPositive(-100));
    }
    
    // ==================== Utils.formatName tests ====================
    
    @Test
    @DisplayName("formatName1 should format first and last name")
    void testFormatName1_validNames() {
        String result = Utils.formatName1("John", "Doe");
        assertEquals("John Doe", result);
    }
    
    @Test
    @DisplayName("formatName1 should handle null first name")
    void testFormatName1_nullFirst() {
        String result = Utils.formatName1(null, "Doe");
        assertEquals(" Doe", result);
    }
    
    @Test
    @DisplayName("formatName1 should handle null last name")
    void testFormatName1_nullLast() {
        String result = Utils.formatName1("John", null);
        assertEquals("John ", result);
    }
    
    @Test
    @DisplayName("formatName1 should handle both null")
    void testFormatName1_bothNull() {
        String result = Utils.formatName1(null, null);
        assertEquals(" ", result);
    }
    
    @Test
    @DisplayName("formatName1 should trim whitespace")
    void testFormatName1_withWhitespace() {
        String result = Utils.formatName1("  John  ", "  Doe  ");
        assertEquals("John Doe", result);
    }
    
    @Test
    @DisplayName("formatName2 should format first and last name")
    void testFormatName2_validNames() {
        String result = Utils.formatName2("Jane", "Smith");
        assertEquals("Jane Smith", result);
    }
    
    @Test
    @DisplayName("formatName2 should handle null values")
    void testFormatName2_nullValues() {
        String result = Utils.formatName2(null, null);
        assertEquals(" ", result);
    }
    
    @Test
    @DisplayName("formatFullName should format first and last name")
    void testFormatFullName_validNames() {
        String result = Utils.formatFullName("Alice", "Johnson");
        assertEquals("Alice Johnson", result);
    }
    
    @Test
    @DisplayName("formatFullName should handle null values")
    void testFormatFullName_nullValues() {
        String result = Utils.formatFullName(null, "Johnson");
        assertEquals(" Johnson", result);
    }
    
    // ==================== PasswordUtil tests ====================
    
    @Test
    @DisplayName("hashPassword should return non-null hash")
    void testHashPassword_returnsHash() {
        String hash = PasswordUtil.hashPassword("password123");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }
    
    @Test
    @DisplayName("hashPassword should return consistent hash for same input")
    void testHashPassword_consistentHash() {
        String hash1 = PasswordUtil.hashPassword("test123");
        String hash2 = PasswordUtil.hashPassword("test123");
        assertEquals(hash1, hash2);
    }
    
    @Test
    @DisplayName("hashPassword should return different hash for different input")
    void testHashPassword_differentHashes() {
        String hash1 = PasswordUtil.hashPassword("password1");
        String hash2 = PasswordUtil.hashPassword("password2");
        assertNotEquals(hash1, hash2);
    }
    
    @Test
    @DisplayName("hashWithSha1 should return non-null hash")
    void testHashWithSha1_returnsHash() {
        String hash = PasswordUtil.hashWithSha1("test");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }
    
    @Test
    @DisplayName("hashWithSha1 should return consistent hash")
    void testHashWithSha1_consistentHash() {
        String hash1 = PasswordUtil.hashWithSha1("test");
        String hash2 = PasswordUtil.hashWithSha1("test");
        assertEquals(hash1, hash2);
    }
    
    @Test
    @DisplayName("verifyPassword should return true for matching password")
    void testVerifyPassword_matching() {
        String hashedPassword = PasswordUtil.hashPassword("mypassword");
        assertTrue(PasswordUtil.verifyPassword("mypassword", hashedPassword));
    }
    
    @Test
    @DisplayName("verifyPassword should return false for non-matching password")
    void testVerifyPassword_notMatching() {
        String hashedPassword = PasswordUtil.hashPassword("mypassword");
        assertFalse(PasswordUtil.verifyPassword("wrongpassword", hashedPassword));
    }
    
    @Test
    @DisplayName("isValidPassword should return false for null")
    void testIsValidPassword_null() {
        assertFalse(PasswordUtil.isValidPassword(null));
    }
    
    @Test
    @DisplayName("isValidPassword should return false for short password")
    void testIsValidPassword_tooShort() {
        assertFalse(PasswordUtil.isValidPassword("abc"));
    }
    
    @Test
    @DisplayName("isValidPassword should return true for valid password")
    void testIsValidPassword_valid() {
        assertTrue(PasswordUtil.isValidPassword("password123"));
    }
    
    @Test
    @DisplayName("isValidPassword should return true for minimum length")
    void testIsValidPassword_minLength() {
        assertTrue(PasswordUtil.isValidPassword("abcd"));
    }
    
    @Test
    @DisplayName("dangerousPasswordStorage should not throw")
    void testDangerousPasswordStorage() {
        assertDoesNotThrow(() -> PasswordUtil.dangerousPasswordStorage("test"));
    }
    
    // ==================== TokenGenerator tests ====================
    
    @Test
    @DisplayName("generateToken should return non-null token")
    void testGenerateToken_returnsToken() {
        String token = TokenGenerator.generateToken();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    
    @Test
    @DisplayName("generateToken should return 32 character token")
    void testGenerateToken_correctLength() {
        String token = TokenGenerator.generateToken();
        assertEquals(32, token.length());
    }
    
    @Test
    @DisplayName("generateToken should return hex string")
    void testGenerateToken_hexFormat() {
        String token = TokenGenerator.generateToken();
        assertTrue(token.matches("[0-9a-f]+"));
    }
    
    @Test
    @DisplayName("generateSessionId should return non-null session id")
    void testGenerateSessionId_returnsSessionId() {
        String sessionId = TokenGenerator.generateSessionId();
        assertNotNull(sessionId);
        assertFalse(sessionId.isEmpty());
    }
    
    @Test
    @DisplayName("generateApiKey should include userId")
    void testGenerateApiKey_includesUserId() {
        String apiKey = TokenGenerator.generateApiKey("user123");
        assertTrue(apiKey.startsWith("API-user123-"));
    }
    
    @Test
    @DisplayName("generateApiKey should have correct format")
    void testGenerateApiKey_correctFormat() {
        String apiKey = TokenGenerator.generateApiKey("testuser");
        assertTrue(apiKey.matches("API-testuser-\\d+"));
    }
    
    @Test
    @DisplayName("generatePasswordResetToken should return 6 digit token")
    void testGeneratePasswordResetToken_sixDigits() {
        String token = TokenGenerator.generatePasswordResetToken();
        assertNotNull(token);
        assertEquals(6, token.length());
        assertTrue(token.matches("\\d{6}"));
    }
    
    @Test
    @DisplayName("getTokenSecret should return secret")
    void testGetTokenSecret_returnsSecret() {
        String secret = TokenGenerator.getTokenSecret();
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
    }
    
    @Test
    @DisplayName("generateUUID should return 16 character uppercase string")
    void testGenerateUUID_correctFormat() {
        String uuid = TokenGenerator.generateUUID();
        assertNotNull(uuid);
        assertEquals(16, uuid.length());
        assertTrue(uuid.matches("[A-F0-9]+"));
    }
    
    @Test
    @DisplayName("generateUUID should return unique values")
    void testGenerateUUID_unique() {
        String uuid1 = TokenGenerator.generateUUID();
        String uuid2 = TokenGenerator.generateUUID();
        assertNotEquals(uuid1, uuid2);
    }
}
