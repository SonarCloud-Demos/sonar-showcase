package com.sonarshowcase.validation;

import com.sonarshowcase.model.Order;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Order validator with duplicated code.
 * 
 * MNT-05: Duplicated validation logic (copy-pasted from UserValidator)
 * 
 * @author SonarShowcase
 */
public class OrderValidator {
    
    /**
     * Default constructor for OrderValidator.
     */
    public OrderValidator() {
    }

    // MNT: Duplicated pattern (also in UserValidator)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    // MNT: Duplicated pattern
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\d{10}$"
    );
    
    /**
     * MNT-05: Duplicated code - same logic exists in UserValidator
     *
     * @param email Email address to validate
     * @return true if email is valid, false otherwise
     */
    public boolean validateEmail(String email) {
        // MNT: This exact code is duplicated in UserValidator
        if (email == null) {
            return false;
        }
        if (email.isEmpty()) {
            return false;
        }
        if (email.length() > 255) {
            return false;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
        if (email.startsWith("@")) {
            return false;
        }
        if (email.endsWith("@")) {
            return false;
        }
        return true;
    }
    
    /**
     * MNT: Duplicated validation logic
     *
     * @param phone Phone number to validate
     * @return true if phone is valid, false otherwise
     */
    public boolean validatePhone(String phone) {
        // MNT: This exact code is duplicated in UserValidator
        if (phone == null) {
            return false;
        }
        if (phone.isEmpty()) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        if (cleanPhone.length() != 10) {
            return false;
        }
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            return false;
        }
        return true;
    }
    
    /**
     * MNT: Duplicated validation for customer names
     *
     * @param name Customer name to validate
     * @return true if name is valid, false otherwise
     */
    public boolean validateCustomerName(String name) {
        // MNT: Duplicated from UserValidator.validateName
        if (name == null) {
            return false;
        }
        if (name.trim().isEmpty()) {
            return false;
        }
        if (name.length() < 2) {
            return false;
        }
        if (name.length() > 100) {
            return false;
        }
        if (!name.matches("[a-zA-Z\\s'-]+")) {
            return false;
        }
        return true;
    }
    
    /**
     * MNT: Duplicated address validation
     *
     * @param address Shipping address to validate
     * @return true if address is valid, false otherwise
     */
    public boolean validateShippingAddress(String address) {
        // MNT: Duplicated in UserValidator.validateAddress
        if (address == null) {
            return false;
        }
        if (address.trim().isEmpty()) {
            return false;
        }
        if (address.length() < 10) {
            return false;
        }
        if (address.length() > 500) {
            return false;
        }
        return true;
    }
    
    /**
     * Validates an order
     *
     * @param order Order to validate
     * @return true if order is valid, false otherwise
     */
    public boolean validateOrder(Order order) {
        if (order == null) {
            return false;
        }
        if (order.getTotalAmount() == null) {
            return false;
        }
        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (!validateShippingAddress(order.getShippingAddress())) {
            return false;
        }
        return true;
    }
    
    /**
     * MNT: Duplicated ZIP code validation
     *
     * @param zip ZIP code to validate
     * @return true if ZIP code is valid, false otherwise
     */
    public boolean validateZipCode(String zip) {
        if (zip == null) {
            return false;
        }
        if (zip.length() != 5 && zip.length() != 10) {
            return false;
        }
        if (!zip.matches("\\d{5}(-\\d{4})?")) {
            return false;
        }
        return true;
    }
}

