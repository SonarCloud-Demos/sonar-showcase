package com.sonarshowcase;

import com.sonarshowcase.model.User;
import com.sonarshowcase.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for User model and related operations
 */
class UserServiceTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user.setRole("USER");
        user.setActive(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
    }
    
    @Test
    @DisplayName("Should create user with valid fields")
    void testCreateUser_withValidFields() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedpassword", user.getPassword());
        assertEquals("USER", user.getRole());
        assertTrue(user.getActive());
    }
    
    @Test
    @DisplayName("Should update username")
    void testUser_updateUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());
    }
    
    @Test
    @DisplayName("Should update email")
    void testUser_updateEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }
    
    @Test
    @DisplayName("Should update password")
    void testUser_updatePassword() {
        user.setPassword("newhash");
        assertEquals("newhash", user.getPassword());
    }
    
    @Test
    @DisplayName("Should update role")
    void testUser_updateRole() {
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }
    
    @Test
    @DisplayName("Should toggle active status")
    void testUser_toggleActive() {
        assertTrue(user.getActive());
        user.setActive(false);
        assertFalse(user.getActive());
        user.setActive(true);
        assertTrue(user.getActive());
    }
    
    @Test
    @DisplayName("Should handle null username")
    void testUser_nullUsername() {
        user.setUsername(null);
        assertNull(user.getUsername());
    }
    
    @Test
    @DisplayName("Should handle null email")
    void testUser_nullEmail() {
        user.setEmail(null);
        assertNull(user.getEmail());
    }
    
    @Test
    @DisplayName("Should handle empty username")
    void testUser_emptyUsername() {
        user.setUsername("");
        assertEquals("", user.getUsername());
    }
    
    @Test
    @DisplayName("Should handle empty email")
    void testUser_emptyEmail() {
        user.setEmail("");
        assertEquals("", user.getEmail());
    }
    
    @Test
    @DisplayName("Should set and get credit card number")
    void testUser_creditCardNumber() {
        user.setCreditCardNumber("4111111111111111");
        assertEquals("4111111111111111", user.getCreditCardNumber());
    }
    
    @Test
    @DisplayName("Should set and get SSN")
    void testUser_ssn() {
        user.setSsn("123-45-6789");
        assertEquals("123-45-6789", user.getSsn());
    }
    
    @Test
    @DisplayName("Should handle null credit card")
    void testUser_nullCreditCard() {
        user.setCreditCardNumber(null);
        assertNull(user.getCreditCardNumber());
    }
    
    @Test
    @DisplayName("Should handle null SSN")
    void testUser_nullSsn() {
        user.setSsn(null);
        assertNull(user.getSsn());
    }
    
    @Test
    @DisplayName("Should set and get createdAt date")
    void testUser_createdAt() {
        Date date = new Date();
        user.setCreatedAt(date);
        assertEquals(date, user.getCreatedAt());
    }
    
    @Test
    @DisplayName("Should set and get updatedAt date")
    void testUser_updatedAt() {
        Date date = new Date();
        user.setUpdatedAt(date);
        assertEquals(date, user.getUpdatedAt());
    }
    
    @Test
    @DisplayName("Should handle null dates")
    void testUser_nullDates() {
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }
    
    @Test
    @DisplayName("Should set and get orders")
    void testUser_orders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        orders.add(order1);
        
        Order order2 = new Order();
        order2.setId(2L);
        orders.add(order2);
        
        user.setOrders(orders);
        
        assertNotNull(user.getOrders());
        assertEquals(2, user.getOrders().size());
    }
    
    @Test
    @DisplayName("Should handle empty orders list")
    void testUser_emptyOrders() {
        user.setOrders(new ArrayList<>());
        assertNotNull(user.getOrders());
        assertTrue(user.getOrders().isEmpty());
    }
    
    @Test
    @DisplayName("Should handle null orders list")
    void testUser_nullOrders() {
        user.setOrders(null);
        assertNull(user.getOrders());
    }
    
    @Test
    @DisplayName("Should return User{} from toString")
    void testUser_toString() {
        String result = user.toString();
        assertEquals("User{}", result);
    }
    
    // ==================== Role-based tests ====================
    
    @Test
    @DisplayName("Should handle USER role")
    void testUser_userRole() {
        user.setRole("USER");
        assertEquals("USER", user.getRole());
    }
    
    @Test
    @DisplayName("Should handle ADMIN role")
    void testUser_adminRole() {
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }
    
    @Test
    @DisplayName("Should handle MANAGER role")
    void testUser_managerRole() {
        user.setRole("MANAGER");
        assertEquals("MANAGER", user.getRole());
    }
    
    @Test
    @DisplayName("Should handle null role")
    void testUser_nullRole() {
        user.setRole(null);
        assertNull(user.getRole());
    }
    
    // ==================== Edge cases ====================
    
    @Test
    @DisplayName("Should handle long username")
    void testUser_longUsername() {
        String longName = "a".repeat(255);
        user.setUsername(longName);
        assertEquals(longName, user.getUsername());
    }
    
    @Test
    @DisplayName("Should handle long email")
    void testUser_longEmail() {
        String longEmail = "a".repeat(200) + "@test.com";
        user.setEmail(longEmail);
        assertEquals(longEmail, user.getEmail());
    }
    
    @Test
    @DisplayName("Should handle special characters in username")
    void testUser_specialCharacters() {
        user.setUsername("user_name-123.test");
        assertEquals("user_name-123.test", user.getUsername());
    }
    
    @Test
    @DisplayName("Should handle unicode in username")
    void testUser_unicodeUsername() {
        user.setUsername("用户测试");
        assertEquals("用户测试", user.getUsername());
    }
    
    @Test
    @DisplayName("Should handle unicode in email")
    void testUser_unicodeEmail() {
        user.setEmail("test@例え.com");
        assertEquals("test@例え.com", user.getEmail());
    }
    
    @Test
    @DisplayName("Should handle null active status")
    void testUser_nullActive() {
        user.setActive(null);
        assertNull(user.getActive());
    }
    
    @Test
    @DisplayName("Should create new user with no fields set")
    void testUser_newEmptyUser() {
        User newUser = new User();
        assertNull(newUser.getId());
        assertNull(newUser.getUsername());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
        assertNull(newUser.getRole());
        assertNull(newUser.getActive());
    }
}
