package com.sonarshowcase.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * ActivityLog entity - tracks user activities and actions
 * 
 * Represents an activity log entry that records user actions in the system.
 * This entity is used for audit trails and activity tracking.
 */
@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    /**
     * Default constructor for JPA entity.
     * JPA requires a no-argument constructor for entity classes.
     */
    public ActivityLog() {
        // Default constructor required by JPA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action")
    private String action;

    @Column(name = "details")
    private String details;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "ip_address")
    private String ipAddress;

    // Getters and Setters

    /**
     * Gets the activity log ID
     * 
     * @return The activity log ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the activity log ID
     * 
     * @param id The activity log ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the user ID associated with this activity
     * 
     * @return The user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with this activity
     * 
     * @param userId The user ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the action performed
     * 
     * @return The action
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action performed
     * 
     * @param action The action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Gets the details of the activity
     * 
     * @return The details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets the details of the activity
     * 
     * @param details The details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Gets the timestamp of the activity
     * 
     * @return The timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the activity
     * 
     * @param timestamp The timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the IP address from which the activity originated
     * 
     * @return The IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the IP address from which the activity originated
     * 
     * @param ipAddress The IP address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "ActivityLog{id=" + id + ", userId=" + userId + ", action='" + action + "'}";
    }
}

