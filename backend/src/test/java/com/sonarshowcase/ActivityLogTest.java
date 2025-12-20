package com.sonarshowcase;

import com.sonarshowcase.model.ActivityLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ActivityLog model to increase coverage.
 */
class ActivityLogTest {

    private ActivityLog activityLog;

    @BeforeEach
    void setUp() {
        activityLog = new ActivityLog();
        activityLog.setId(1L);
        activityLog.setUserId(100L);
        activityLog.setAction("LOGIN");
        activityLog.setDetails("User logged in successfully");
        activityLog.setTimestamp(new Date());
        activityLog.setIpAddress("192.168.1.100");
    }
    
    @Test
    @DisplayName("Should create activity log with valid fields")
    void testCreateActivityLog_withValidFields() {
        assertNotNull(activityLog);
        assertEquals(1L, activityLog.getId());
        assertEquals(100L, activityLog.getUserId());
        assertEquals("LOGIN", activityLog.getAction());
        assertEquals("User logged in successfully", activityLog.getDetails());
        assertNotNull(activityLog.getTimestamp());
        assertEquals("192.168.1.100", activityLog.getIpAddress());
    }
    
    @Test
    @DisplayName("Should set and get id")
    void testActivityLog_id() {
        activityLog.setId(2L);
        assertEquals(2L, activityLog.getId());
    }
    
    @Test
    @DisplayName("Should set and get userId")
    void testActivityLog_userId() {
        activityLog.setUserId(200L);
        assertEquals(200L, activityLog.getUserId());
    }
    
    @Test
    @DisplayName("Should set and get action")
    void testActivityLog_action() {
        activityLog.setAction("PROFILE_UPDATE");
        assertEquals("PROFILE_UPDATE", activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should set and get details")
    void testActivityLog_details() {
        activityLog.setDetails("User updated profile");
        assertEquals("User updated profile", activityLog.getDetails());
    }
    
    @Test
    @DisplayName("Should set and get timestamp")
    void testActivityLog_timestamp() {
        Date date = new Date();
        activityLog.setTimestamp(date);
        assertEquals(date, activityLog.getTimestamp());
    }
    
    @Test
    @DisplayName("Should set and get IP address")
    void testActivityLog_ipAddress() {
        activityLog.setIpAddress("10.0.0.1");
        assertEquals("10.0.0.1", activityLog.getIpAddress());
    }
    
    @Test
    @DisplayName("Should handle null userId")
    void testActivityLog_nullUserId() {
        activityLog.setUserId(null);
        assertNull(activityLog.getUserId());
    }
    
    @Test
    @DisplayName("Should handle null action")
    void testActivityLog_nullAction() {
        activityLog.setAction(null);
        assertNull(activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle null details")
    void testActivityLog_nullDetails() {
        activityLog.setDetails(null);
        assertNull(activityLog.getDetails());
    }
    
    @Test
    @DisplayName("Should handle null timestamp")
    void testActivityLog_nullTimestamp() {
        activityLog.setTimestamp(null);
        assertNull(activityLog.getTimestamp());
    }
    
    @Test
    @DisplayName("Should handle null IP address")
    void testActivityLog_nullIpAddress() {
        activityLog.setIpAddress(null);
        assertNull(activityLog.getIpAddress());
    }
    
    @Test
    @DisplayName("Should handle empty action")
    void testActivityLog_emptyAction() {
        activityLog.setAction("");
        assertEquals("", activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle empty details")
    void testActivityLog_emptyDetails() {
        activityLog.setDetails("");
        assertEquals("", activityLog.getDetails());
    }
    
    @Test
    @DisplayName("Should handle empty IP address")
    void testActivityLog_emptyIpAddress() {
        activityLog.setIpAddress("");
        assertEquals("", activityLog.getIpAddress());
    }
    
    // ==================== Action Type Tests ====================
    
    @Test
    @DisplayName("Should handle LOGIN action")
    void testActivityLog_loginAction() {
        activityLog.setAction("LOGIN");
        assertEquals("LOGIN", activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle PROFILE_UPDATE action")
    void testActivityLog_profileUpdateAction() {
        activityLog.setAction("PROFILE_UPDATE");
        assertEquals("PROFILE_UPDATE", activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle ORDER_CREATE action")
    void testActivityLog_orderCreateAction() {
        activityLog.setAction("ORDER_CREATE");
        assertEquals("ORDER_CREATE", activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle ADMIN_ACTION action")
    void testActivityLog_adminAction() {
        activityLog.setAction("ADMIN_ACTION");
        assertEquals("ADMIN_ACTION", activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle PASSWORD_CHANGE action")
    void testActivityLog_passwordChangeAction() {
        activityLog.setAction("PASSWORD_CHANGE");
        assertEquals("PASSWORD_CHANGE", activityLog.getAction());
    }
    
    // ==================== Edge Cases ====================
    
    @Test
    @DisplayName("Should handle long action string")
    void testActivityLog_longAction() {
        String longAction = "A".repeat(255);
        activityLog.setAction(longAction);
        assertEquals(longAction, activityLog.getAction());
    }
    
    @Test
    @DisplayName("Should handle long details string")
    void testActivityLog_longDetails() {
        String longDetails = "This is a very long details string. ".repeat(100);
        activityLog.setDetails(longDetails);
        assertEquals(longDetails, activityLog.getDetails());
    }
    
    @Test
    @DisplayName("Should handle special characters in details")
    void testActivityLog_specialCharacters() {
        activityLog.setDetails("User action: <script>alert('xss')</script>");
        assertEquals("User action: <script>alert('xss')</script>", activityLog.getDetails());
    }
    
    @Test
    @DisplayName("Should handle IPv6 address")
    void testActivityLog_ipv6Address() {
        activityLog.setIpAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", activityLog.getIpAddress());
    }
    
    @Test
    @DisplayName("Should handle unicode in details")
    void testActivityLog_unicodeDetails() {
        activityLog.setDetails("用户操作：登录系统");
        assertEquals("用户操作：登录系统", activityLog.getDetails());
    }
    
    @Test
    @DisplayName("Should create new activity log with no fields set")
    void testActivityLog_newEmptyLog() {
        ActivityLog newLog = new ActivityLog();
        assertNull(newLog.getId());
        assertNull(newLog.getUserId());
        assertNull(newLog.getAction());
        assertNull(newLog.getDetails());
        assertNull(newLog.getTimestamp());
        assertNull(newLog.getIpAddress());
    }
    
    @Test
    @DisplayName("Should return proper toString format")
    void testActivityLog_toString() {
        activityLog.setId(1L);
        activityLog.setUserId(100L);
        activityLog.setAction("LOGIN");
        String result = activityLog.toString();
        assertTrue(result.contains("ActivityLog"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("userId=100"));
        assertTrue(result.contains("action='LOGIN'"));
    }
    
    @Test
    @DisplayName("Should handle all fields set")
    void testActivityLog_allFields() {
        ActivityLog log = new ActivityLog();
        log.setId(999L);
        log.setUserId(888L);
        log.setAction("TEST_ACTION");
        log.setDetails("Test details");
        Date date = new Date();
        log.setTimestamp(date);
        log.setIpAddress("127.0.0.1");
        
        assertNotNull(log.getId());
        assertNotNull(log.getUserId());
        assertNotNull(log.getAction());
        assertNotNull(log.getDetails());
        assertNotNull(log.getTimestamp());
        assertNotNull(log.getIpAddress());
    }
    
    @Test
    @DisplayName("Should handle zero userId")
    void testActivityLog_zeroUserId() {
        activityLog.setUserId(0L);
        assertEquals(0L, activityLog.getUserId());
    }
    
    @Test
    @DisplayName("Should handle negative userId")
    void testActivityLog_negativeUserId() {
        activityLog.setUserId(-1L);
        assertEquals(-1L, activityLog.getUserId());
    }
    
    @Test
    @DisplayName("Should handle very large userId")
    void testActivityLog_largeUserId() {
        activityLog.setUserId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, activityLog.getUserId());
    }
}

