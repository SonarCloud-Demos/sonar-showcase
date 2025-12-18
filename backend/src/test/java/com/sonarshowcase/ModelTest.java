package com.sonarshowcase;

import com.sonarshowcase.model.Order;
import com.sonarshowcase.model.User;
import com.sonarshowcase.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for model classes to increase coverage.
 */
class ModelTest {

    // ==================== User Model Tests ====================
    
    @Test
    @DisplayName("User should set and get id")
    void testUser_id() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }
    
    @Test
    @DisplayName("User should set and get username")
    void testUser_username() {
        User user = new User();
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }
    
    @Test
    @DisplayName("User should set and get email")
    void testUser_email() {
        User user = new User();
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }
    
    @Test
    @DisplayName("User should set and get password")
    void testUser_password() {
        User user = new User();
        user.setPassword("secret123");
        assertEquals("secret123", user.getPassword());
    }
    
    @Test
    @DisplayName("User should set and get creditCardNumber")
    void testUser_creditCardNumber() {
        User user = new User();
        user.setCreditCardNumber("4111111111111111");
        assertEquals("4111111111111111", user.getCreditCardNumber());
    }
    
    @Test
    @DisplayName("User should set and get ssn")
    void testUser_ssn() {
        User user = new User();
        user.setSsn("123-45-6789");
        assertEquals("123-45-6789", user.getSsn());
    }
    
    @Test
    @DisplayName("User should set and get role")
    void testUser_role() {
        User user = new User();
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }
    
    @Test
    @DisplayName("User should set and get active status")
    void testUser_active() {
        User user = new User();
        user.setActive(true);
        assertTrue(user.getActive());
        
        user.setActive(false);
        assertFalse(user.getActive());
    }
    
    @Test
    @DisplayName("User should set and get createdAt")
    void testUser_createdAt() {
        User user = new User();
        Date now = new Date();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }
    
    @Test
    @DisplayName("User should set and get updatedAt")
    void testUser_updatedAt() {
        User user = new User();
        Date now = new Date();
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdatedAt());
    }
    
    @Test
    @DisplayName("User should set and get orders")
    void testUser_orders() {
        User user = new User();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        user.setOrders(orders);
        assertEquals(1, user.getOrders().size());
    }
    
    @Test
    @DisplayName("User toString should return User{}")
    void testUser_toString() {
        User user = new User();
        assertEquals("User{}", user.toString());
    }
    
    // ==================== Order Model Tests ====================
    
    @Test
    @DisplayName("Order should set and get id")
    void testOrder_id() {
        Order order = new Order();
        order.setId(1L);
        assertEquals(1L, order.getId());
    }
    
    @Test
    @DisplayName("Order should set and get orderNumber")
    void testOrder_orderNumber() {
        Order order = new Order();
        order.setOrderNumber("ORD-001");
        assertEquals("ORD-001", order.getOrderNumber());
    }
    
    @Test
    @DisplayName("Order should set and get totalAmount")
    void testOrder_totalAmount() {
        Order order = new Order();
        BigDecimal amount = new BigDecimal("99.99");
        order.setTotalAmount(amount);
        assertEquals(amount, order.getTotalAmount());
    }
    
    @Test
    @DisplayName("Order should set and get status")
    void testOrder_status() {
        Order order = new Order();
        order.setStatus("PENDING");
        assertEquals("PENDING", order.getStatus());
    }
    
    @Test
    @DisplayName("Order should set and get orderDate")
    void testOrder_orderDate() {
        Order order = new Order();
        Date now = new Date();
        order.setOrderDate(now);
        assertEquals(now, order.getOrderDate());
    }
    
    @Test
    @DisplayName("Order should set and get shippingAddress")
    void testOrder_shippingAddress() {
        Order order = new Order();
        order.setShippingAddress("123 Main St, City, State 12345");
        assertEquals("123 Main St, City, State 12345", order.getShippingAddress());
    }
    
    @Test
    @DisplayName("Order should set and get notes")
    void testOrder_notes() {
        Order order = new Order();
        order.setNotes("Please deliver in the morning");
        assertEquals("Please deliver in the morning", order.getNotes());
    }
    
    @Test
    @DisplayName("Order should set and get user")
    void testOrder_user() {
        Order order = new Order();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        order.setUser(user);
        assertEquals(user, order.getUser());
        assertEquals(1L, order.getUser().getId());
    }
    
    @Test
    @DisplayName("Order should set and get products")
    void testOrder_products() {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        order.setProducts(products);
        assertEquals(1, order.getProducts().size());
    }
    
    // ==================== Comprehensive Model Tests ====================
    
    @Test
    @DisplayName("User with all fields set")
    void testUser_allFields() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setCreditCardNumber("4111111111111111");
        user.setSsn("123-45-6789");
        user.setRole("USER");
        user.setActive(true);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setOrders(new ArrayList<>());
        
        assertNotNull(user.getId());
        assertNotNull(user.getUsername());
        assertNotNull(user.getEmail());
        assertNotNull(user.getPassword());
        assertNotNull(user.getCreditCardNumber());
        assertNotNull(user.getSsn());
        assertNotNull(user.getRole());
        assertNotNull(user.getActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotNull(user.getOrders());
    }
    
    @Test
    @DisplayName("Order with all fields set")
    void testOrder_allFields() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setTotalAmount(new BigDecimal("150.00"));
        order.setStatus("COMPLETED");
        order.setOrderDate(new Date());
        order.setShippingAddress("123 Test St");
        order.setNotes("Test notes");
        order.setUser(new User());
        order.setProducts(new ArrayList<>());
        
        assertNotNull(order.getId());
        assertNotNull(order.getOrderNumber());
        assertNotNull(order.getTotalAmount());
        assertNotNull(order.getStatus());
        assertNotNull(order.getOrderDate());
        assertNotNull(order.getShippingAddress());
        assertNotNull(order.getNotes());
        assertNotNull(order.getUser());
        assertNotNull(order.getProducts());
    }
    
    @Test
    @DisplayName("User handles null fields gracefully")
    void testUser_nullFields() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getCreditCardNumber());
        assertNull(user.getSsn());
        assertNull(user.getRole());
        assertNull(user.getActive());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertNull(user.getOrders());
    }
    
    @Test
    @DisplayName("Order handles null fields gracefully")
    void testOrder_nullFields() {
        Order order = new Order();
        assertNull(order.getId());
        assertNull(order.getOrderNumber());
        assertNull(order.getTotalAmount());
        assertNull(order.getStatus());
        assertNull(order.getOrderDate());
        assertNull(order.getShippingAddress());
        assertNull(order.getNotes());
        assertNull(order.getUser());
        assertNull(order.getProducts());
    }
}

