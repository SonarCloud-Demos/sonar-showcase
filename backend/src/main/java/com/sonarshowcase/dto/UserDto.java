package com.sonarshowcase.dto;

import java.util.Date;

/**
 * User DTO - exposes sensitive data
 * 
 * SEC-09: Password and sensitive fields exposed in API response
 * 
 * Data transfer object for user information. Note: This DTO intentionally
 * exposes sensitive data for demonstration purposes and should not be used
 * in production environments.
 */
public class UserDto {

    /**
     * Default constructor for DTO.
     * Creates a new instance of UserDto.
     */
    public UserDto() {
        // Default constructor
    }
    
    private Long id;
    private String username;
    private String email;
    
    // SEC: Password should NEVER be in a DTO response
    private String password;
    
    // SEC: Sensitive financial data exposed
    private String creditCardNumber;
    private String cvv;
    private String ssn;
    
    private String role;
    private Boolean active;
    private Date createdAt;
    
    // SEC: Internal system fields exposed
    private String internalNotes;
    private String adminComments;

    // MNT: No validation, no builder pattern, just raw getters/setters
    
    /**
     * Gets the user ID
     * 
     * @return The user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user ID
     * 
     * @param id The user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     * 
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address
     * 
     * @param email The email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * 
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the credit card number
     * 
     * @return The credit card number
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Sets the credit card number
     * 
     * @param creditCardNumber The credit card number
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Gets the CVV
     * 
     * @return The CVV
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * Sets the CVV
     * 
     * @param cvv The CVV
     */
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * Gets the SSN
     * 
     * @return The SSN
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Sets the SSN
     * 
     * @param ssn The SSN
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * Gets the user role
     * 
     * @return The user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user role
     * 
     * @param role The user role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the active status
     * 
     * @return The active status
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active status
     * 
     * @param active The active status
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Gets the creation date
     * 
     * @return The creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date
     * 
     * @param createdAt The creation date
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the internal notes
     * 
     * @return The internal notes
     */
    public String getInternalNotes() {
        return internalNotes;
    }

    /**
     * Sets the internal notes
     * 
     * @param internalNotes The internal notes
     */
    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    /**
     * Gets the admin comments
     * 
     * @return The admin comments
     */
    public String getAdminComments() {
        return adminComments;
    }

    /**
     * Sets the admin comments
     * 
     * @param adminComments The admin comments
     */
    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }
}

