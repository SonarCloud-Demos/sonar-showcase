package com.sonarshowcase;

import com.sonarshowcase.model.Order;
import com.sonarshowcase.validation.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for OrderValidator class.
 */
class OrderValidatorTest {

    private OrderValidator validator;
    
    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
    }
    
    // ==================== Email Validation Tests ====================
    
    @Test
    @DisplayName("validateEmail should return true for valid email")
    void testValidateEmail_valid() {
        assertTrue(validator.validateEmail("customer@example.com"));
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
    @DisplayName("validateEmail should return false for email over 255 chars")
    void testValidateEmail_tooLong() {
        String longEmail = "a".repeat(250) + "@test.com";
        assertFalse(validator.validateEmail(longEmail));
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
    
    // ==================== Phone Validation Tests ====================
    
    @Test
    @DisplayName("validatePhone should return true for 10 digit number")
    void testValidatePhone_valid() {
        assertTrue(validator.validatePhone("1234567890"));
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
    @DisplayName("validatePhone should return false for wrong length")
    void testValidatePhone_wrongLength() {
        assertFalse(validator.validatePhone("123"));
    }
    
    // ==================== Customer Name Validation Tests ====================
    
    @Test
    @DisplayName("validateCustomerName should return true for valid name")
    void testValidateCustomerName_valid() {
        assertTrue(validator.validateCustomerName("John Smith"));
    }
    
    @Test
    @DisplayName("validateCustomerName should return true for hyphenated name")
    void testValidateCustomerName_hyphenated() {
        assertTrue(validator.validateCustomerName("Mary-Jane Watson"));
    }
    
    @Test
    @DisplayName("validateCustomerName should return false for null")
    void testValidateCustomerName_null() {
        assertFalse(validator.validateCustomerName(null));
    }
    
    @Test
    @DisplayName("validateCustomerName should return false for empty string")
    void testValidateCustomerName_empty() {
        assertFalse(validator.validateCustomerName(""));
    }
    
    @Test
    @DisplayName("validateCustomerName should return false for whitespace only")
    void testValidateCustomerName_whitespaceOnly() {
        assertFalse(validator.validateCustomerName("   "));
    }
    
    @Test
    @DisplayName("validateCustomerName should return false for single char")
    void testValidateCustomerName_tooShort() {
        assertFalse(validator.validateCustomerName("J"));
    }
    
    @Test
    @DisplayName("validateCustomerName should return false for name over 100 chars")
    void testValidateCustomerName_tooLong() {
        assertFalse(validator.validateCustomerName("A".repeat(101)));
    }
    
    @Test
    @DisplayName("validateCustomerName should return false for name with numbers")
    void testValidateCustomerName_withNumbers() {
        assertFalse(validator.validateCustomerName("John123"));
    }
    
    // ==================== Shipping Address Validation Tests ====================
    
    @Test
    @DisplayName("validateShippingAddress should return true for valid address")
    void testValidateShippingAddress_valid() {
        assertTrue(validator.validateShippingAddress("123 Main Street, City, State 12345"));
    }
    
    @Test
    @DisplayName("validateShippingAddress should return false for null")
    void testValidateShippingAddress_null() {
        assertFalse(validator.validateShippingAddress(null));
    }
    
    @Test
    @DisplayName("validateShippingAddress should return false for empty string")
    void testValidateShippingAddress_empty() {
        assertFalse(validator.validateShippingAddress(""));
    }
    
    @Test
    @DisplayName("validateShippingAddress should return false for whitespace only")
    void testValidateShippingAddress_whitespaceOnly() {
        assertFalse(validator.validateShippingAddress("      "));
    }
    
    @Test
    @DisplayName("validateShippingAddress should return false for address under 10 chars")
    void testValidateShippingAddress_tooShort() {
        assertFalse(validator.validateShippingAddress("123 Main"));
    }
    
    @Test
    @DisplayName("validateShippingAddress should return false for address over 500 chars")
    void testValidateShippingAddress_tooLong() {
        assertFalse(validator.validateShippingAddress("A".repeat(501)));
    }
    
    // ==================== Zip Code Validation Tests ====================
    
    @Test
    @DisplayName("validateZipCode should return true for 5 digit zip")
    void testValidateZipCode_fiveDigit() {
        assertTrue(validator.validateZipCode("12345"));
    }
    
    @Test
    @DisplayName("validateZipCode should return true for zip+4 format")
    void testValidateZipCode_zipPlusFour() {
        assertTrue(validator.validateZipCode("12345-6789"));
    }
    
    @Test
    @DisplayName("validateZipCode should return false for null")
    void testValidateZipCode_null() {
        assertFalse(validator.validateZipCode(null));
    }
    
    @Test
    @DisplayName("validateZipCode should return false for wrong length")
    void testValidateZipCode_wrongLength() {
        assertFalse(validator.validateZipCode("1234"));
    }
    
    @Test
    @DisplayName("validateZipCode should return false for invalid format")
    void testValidateZipCode_invalidFormat() {
        assertFalse(validator.validateZipCode("ABCDE"));
    }
    
    // ==================== Order Validation Tests ====================
    
    @Test
    @DisplayName("validateOrder should return true for valid order")
    void testValidateOrder_valid() {
        Order order = new Order();
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setShippingAddress("123 Main Street, City, State 12345");
        assertTrue(validator.validateOrder(order));
    }
    
    @Test
    @DisplayName("validateOrder should return false for null order")
    void testValidateOrder_null() {
        assertFalse(validator.validateOrder(null));
    }
    
    @Test
    @DisplayName("validateOrder should return false for null total amount")
    void testValidateOrder_nullAmount() {
        Order order = new Order();
        order.setTotalAmount(null);
        order.setShippingAddress("123 Main Street, City, State 12345");
        assertFalse(validator.validateOrder(order));
    }
    
    @Test
    @DisplayName("validateOrder should return false for zero total amount")
    void testValidateOrder_zeroAmount() {
        Order order = new Order();
        order.setTotalAmount(BigDecimal.ZERO);
        order.setShippingAddress("123 Main Street, City, State 12345");
        assertFalse(validator.validateOrder(order));
    }
    
    @Test
    @DisplayName("validateOrder should return false for negative total amount")
    void testValidateOrder_negativeAmount() {
        Order order = new Order();
        order.setTotalAmount(new BigDecimal("-10.00"));
        order.setShippingAddress("123 Main Street, City, State 12345");
        assertFalse(validator.validateOrder(order));
    }
    
    @Test
    @DisplayName("validateOrder should return false for invalid shipping address")
    void testValidateOrder_invalidAddress() {
        Order order = new Order();
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setShippingAddress("short");
        assertFalse(validator.validateOrder(order));
    }
}

