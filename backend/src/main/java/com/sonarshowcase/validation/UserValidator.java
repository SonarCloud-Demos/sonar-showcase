package com.sonarshowcase.validation;

import com.sonarshowcase.model.User;
import java.util.regex.Pattern;

/**
 * User validator with duplicated code
 * 
 * MNT-05: Duplicated validation logic (copy-pasted from OrderValidator)
 * 
 * This class provides validation methods for user data. Note that many
 * of these validation methods are duplicated in OrderValidator.
 */
public class UserValidator {

    /**
     * Default constructor for validator.
     * Creates a new instance of UserValidator.
     */
    public UserValidator() {
        // Default constructor
    }

    // MNT: Duplicated pattern (also in OrderValidator)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    // MNT: Duplicated pattern
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\d{10}$"
    );
    
    /**
     * MNT-05: Duplicated code - same logic exists in OrderValidator
     * 
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    public boolean validateEmail(String email) {
        // MNT: This exact code is duplicated in OrderValidator
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
     * @param phone The phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public boolean validatePhone(String phone) {
        // MNT: This exact code is duplicated in OrderValidator
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
     * MNT: Duplicated validation for names
     * 
     * @param name The name to validate
     * @return true if the name is valid, false otherwise
     */
    public boolean validateName(String name) {
        // MNT: Duplicated in OrderValidator.validateCustomerName
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
     * @param address The address to validate
     * @return true if the address is valid, false otherwise
     */
    public boolean validateAddress(String address) {
        // MNT: Duplicated in OrderValidator
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
     * Validates a user object
     * 
     * @param user The user to validate
     * @return true if the user is valid, false otherwise
     */
    public boolean validateUser(User user) {
        if (user == null) {
            return false;
        }
        if (!validateEmail(user.getEmail())) {
            return false;
        }
        if (!validateName(user.getUsername())) {
            return false;
        }
        return true;
    }
}

