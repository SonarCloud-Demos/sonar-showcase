package com.sonarshowcase;

import com.sonarshowcase.model.User;
import com.sonarshowcase.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for UserValidator class.
 */
class UserValidatorTest {

    private UserValidator validator;
    
    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }
    
    // ==================== Email Validation Tests ====================
    
    @Test
    @DisplayName("validateEmail should return true for valid email")
    void testValidateEmail_valid() {
        assertTrue(validator.validateEmail("test@example.com"));
    }
    
    @Test
    @DisplayName("validateEmail should return true for email with subdomain")
    void testValidateEmail_withSubdomain() {
        assertTrue(validator.validateEmail("test@mail.example.com"));
    }
    
    @Test
    @DisplayName("validateEmail should return true for email with plus sign")
    void testValidateEmail_withPlusSign() {
        assertTrue(validator.validateEmail("test+tag@example.com"));
    }
    
    @Test
    @DisplayName("validateEmail should return false for null")
    void testValidateEmail_null() {
        assertFalse(validator.validateEmail(null));
    }
    
    @Test
    @DisplayName("validateEmail should return false for empty string")
    void testValidateEmail_empty() {
        assertFalse(validator.validateEmail(""));
    }
    
    @Test
    @DisplayName("validateEmail should return false for email without @")
    void testValidateEmail_noAtSign() {
        assertFalse(validator.validateEmail("testexample.com"));
    }
    
    @Test
    @DisplayName("validateEmail should return false for email starting with @")
    void testValidateEmail_startsWithAt() {
        assertFalse(validator.validateEmail("@example.com"));
    }
    
    @Test
    @DisplayName("validateEmail should return false for email ending with @")
    void testValidateEmail_endsWithAt() {
        assertFalse(validator.validateEmail("test@"));
    }
    
    @Test
    @DisplayName("validateEmail should return false for email over 255 characters")
    void testValidateEmail_tooLong() {
        String longEmail = "a".repeat(250) + "@test.com";
        assertFalse(validator.validateEmail(longEmail));
    }
    
    // ==================== Phone Validation Tests ====================
    
    @Test
    @DisplayName("validatePhone should return true for 10 digit number")
    void testValidatePhone_valid() {
        assertTrue(validator.validatePhone("1234567890"));
    }
    
    @Test
    @DisplayName("validatePhone should return true for formatted phone number")
    void testValidatePhone_formatted() {
        assertTrue(validator.validatePhone("123-456-7890"));
    }
    
    @Test
    @DisplayName("validatePhone should return true for phone with spaces")
    void testValidatePhone_withSpaces() {
        assertTrue(validator.validatePhone("123 456 7890"));
    }
    
    @Test
    @DisplayName("validatePhone should return true for phone with parentheses")
    void testValidatePhone_withParentheses() {
        assertTrue(validator.validatePhone("(123) 456-7890"));
    }
    
    @Test
    @DisplayName("validatePhone should return false for null")
    void testValidatePhone_null() {
        assertFalse(validator.validatePhone(null));
    }
    
    @Test
    @DisplayName("validatePhone should return false for empty string")
    void testValidatePhone_empty() {
        assertFalse(validator.validatePhone(""));
    }
    
    @Test
    @DisplayName("validatePhone should return false for too few digits")
    void testValidatePhone_tooFewDigits() {
        assertFalse(validator.validatePhone("123456789"));
    }
    
    @Test
    @DisplayName("validatePhone should return false for too many digits")
    void testValidatePhone_tooManyDigits() {
        assertFalse(validator.validatePhone("12345678901"));
    }
    
    // ==================== Name Validation Tests ====================
    
    @Test
    @DisplayName("validateName should return true for valid name")
    void testValidateName_valid() {
        assertTrue(validator.validateName("John"));
    }
    
    @Test
    @DisplayName("validateName should return true for name with hyphen")
    void testValidateName_withHyphen() {
        assertTrue(validator.validateName("Mary-Jane"));
    }
    
    @Test
    @DisplayName("validateName should return true for name with apostrophe")
    void testValidateName_withApostrophe() {
        assertTrue(validator.validateName("O'Connor"));
    }
    
    @Test
    @DisplayName("validateName should return true for name with space")
    void testValidateName_withSpace() {
        assertTrue(validator.validateName("John Doe"));
    }
    
    @Test
    @DisplayName("validateName should return false for null")
    void testValidateName_null() {
        assertFalse(validator.validateName(null));
    }
    
    @Test
    @DisplayName("validateName should return false for empty string")
    void testValidateName_empty() {
        assertFalse(validator.validateName(""));
    }
    
    @Test
    @DisplayName("validateName should return false for whitespace only")
    void testValidateName_whitespaceOnly() {
        assertFalse(validator.validateName("   "));
    }
    
    @Test
    @DisplayName("validateName should return false for single character")
    void testValidateName_tooShort() {
        assertFalse(validator.validateName("J"));
    }
    
    @Test
    @DisplayName("validateName should return false for name over 100 characters")
    void testValidateName_tooLong() {
        String longName = "A".repeat(101);
        assertFalse(validator.validateName(longName));
    }
    
    @Test
    @DisplayName("validateName should return false for name with numbers")
    void testValidateName_withNumbers() {
        assertFalse(validator.validateName("John123"));
    }
    
    @Test
    @DisplayName("validateName should return false for name with special characters")
    void testValidateName_withSpecialChars() {
        assertFalse(validator.validateName("John@Doe"));
    }
    
    // ==================== Address Validation Tests ====================
    
    @Test
    @DisplayName("validateAddress should return true for valid address")
    void testValidateAddress_valid() {
        assertTrue(validator.validateAddress("123 Main Street, City, State 12345"));
    }
    
    @Test
    @DisplayName("validateAddress should return false for null")
    void testValidateAddress_null() {
        assertFalse(validator.validateAddress(null));
    }
    
    @Test
    @DisplayName("validateAddress should return false for empty string")
    void testValidateAddress_empty() {
        assertFalse(validator.validateAddress(""));
    }
    
    @Test
    @DisplayName("validateAddress should return false for whitespace only")
    void testValidateAddress_whitespaceOnly() {
        assertFalse(validator.validateAddress("      "));
    }
    
    @Test
    @DisplayName("validateAddress should return false for address under 10 chars")
    void testValidateAddress_tooShort() {
        assertFalse(validator.validateAddress("123 Main"));
    }
    
    @Test
    @DisplayName("validateAddress should return false for address over 500 chars")
    void testValidateAddress_tooLong() {
        String longAddress = "A".repeat(501);
        assertFalse(validator.validateAddress(longAddress));
    }
    
    @Test
    @DisplayName("validateAddress should return true for minimum length")
    void testValidateAddress_minLength() {
        assertTrue(validator.validateAddress("1234567890"));
    }
    
    // ==================== User Validation Tests ====================
    
    @Test
    @DisplayName("validateUser should return true for valid user")
    void testValidateUser_valid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("John Doe");
        assertTrue(validator.validateUser(user));
    }
    
    @Test
    @DisplayName("validateUser should return false for null user")
    void testValidateUser_null() {
        assertFalse(validator.validateUser(null));
    }
    
    @Test
    @DisplayName("validateUser should return false for invalid email")
    void testValidateUser_invalidEmail() {
        User user = new User();
        user.setEmail("invalid");
        user.setUsername("John Doe");
        assertFalse(validator.validateUser(user));
    }
    
    @Test
    @DisplayName("validateUser should return false for invalid username")
    void testValidateUser_invalidUsername() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("J"); // Too short
        assertFalse(validator.validateUser(user));
    }
    
    @Test
    @DisplayName("validateUser should return false for null email")
    void testValidateUser_nullEmail() {
        User user = new User();
        user.setEmail(null);
        user.setUsername("John Doe");
        assertFalse(validator.validateUser(user));
    }
}

