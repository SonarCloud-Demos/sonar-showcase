package com.sonarshowcase.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User entity - anemic domain model (all logic in services)
 * 
 * Represents a user in the system with authentication and profile information.
 * This is an anemic domain model where business logic is handled in service layers.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Default constructor for JPA entity.
     * JPA requires a no-argument constructor for entity classes.
     */
    public User() {
        // Default constructor required by JPA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    
    private String email;
    
    // SEC: Storing password in plain text (no @JsonIgnore)
    private String password;
    
    // SEC: Storing credit card info
    private String creditCardNumber;
    
    private String ssn; // SEC: Storing SSN
    
    private String role;
    
    private Boolean active;
    
    private Date createdAt;
    
    private Date updatedAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    // MNT: Massive getter/setter boilerplate (anemic domain model)
    
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
     * Gets the last update date
     * 
     * @return The last update date
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update date
     * 
     * @param updatedAt The last update date
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the list of orders
     * 
     * @return The list of orders
     */
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Sets the list of orders
     * 
     * @param orders The list of orders
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    // MNT: Empty toString
    @Override
    public String toString() {
        return "User{}";
    }
}

