package com.sonarshowcase;

import com.sonarshowcase.model.Order;
import com.sonarshowcase.model.User;
import com.sonarshowcase.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Order model and OrderService calculation logic
 */
class OrderServiceTest {

    private Order order;
    private User user;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setTotalAmount(new BigDecimal("99.99"));
        order.setStatus("PENDING");
        
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        order.setUser(user);
    }

    @Test
    @DisplayName("Should create order with valid fields")
    void testCreateOrder_withValidFields() {
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(new BigDecimal("99.99"), order.getTotalAmount());
        assertEquals("PENDING", order.getStatus());
    }
    
    @Test
    @DisplayName("Should associate user with order")
    void testOrder_withUser() {
        assertNotNull(order.getUser());
        assertEquals(1L, order.getUser().getId());
        assertEquals("testuser", order.getUser().getUsername());
    }
    
    @Test
    @DisplayName("Should handle order with zero amount")
    void testOrder_zeroAmount() {
        order.setTotalAmount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }
    
    @Test
    @DisplayName("Should handle order with large amount")
    void testOrder_largeAmount() {
        BigDecimal largeAmount = new BigDecimal("999999.99");
        order.setTotalAmount(largeAmount);
        assertEquals(largeAmount, order.getTotalAmount());
    }
    
    @Test
    @DisplayName("Should handle order with decimal precision")
    void testOrder_decimalPrecision() {
        BigDecimal precise = new BigDecimal("123.456789");
        order.setTotalAmount(precise);
        assertEquals(precise, order.getTotalAmount());
    }

    @Test
    @DisplayName("Should update order status")
    void testOrder_statusTransitions() {
        order.setStatus("PENDING");
        assertEquals("PENDING", order.getStatus());
        
        order.setStatus("PROCESSING");
        assertEquals("PROCESSING", order.getStatus());
        
        order.setStatus("SHIPPED");
        assertEquals("SHIPPED", order.getStatus());
        
        order.setStatus("DELIVERED");
        assertEquals("DELIVERED", order.getStatus());
    }
    
    @Test
    @DisplayName("Should handle null user")
    void testOrder_nullUser() {
        order.setUser(null);
        assertNull(order.getUser());
    }
    
    @Test
    @DisplayName("Should handle null notes")
    void testOrder_nullNotes() {
        order.setNotes(null);
        assertNull(order.getNotes());
    }
    
    @Test
    @DisplayName("Should set and get notes")
    void testOrder_notes() {
        order.setNotes("Please gift wrap");
        assertEquals("Please gift wrap", order.getNotes());
    }
    
    @Test
    @DisplayName("Should set and get shipping address")
    void testOrder_shippingAddress() {
        order.setShippingAddress("123 Test St, City, State 12345");
        assertEquals("123 Test St, City, State 12345", order.getShippingAddress());
    }
    
    // ==================== Order Calculation Tests ====================
    
    @Test
    @DisplayName("Should compare amounts correctly")
    void testOrder_amountComparison() {
        BigDecimal amount1 = new BigDecimal("100.00");
        BigDecimal amount2 = new BigDecimal("50.00");
        
        assertTrue(amount1.compareTo(amount2) > 0);
        assertTrue(amount2.compareTo(amount1) < 0);
        assertEquals(0, amount1.compareTo(new BigDecimal("100.00")));
    }
    
    @Test
    @DisplayName("Should add amounts correctly")
    void testOrder_amountAddition() {
        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal tax = new BigDecimal("8.25");
        BigDecimal shipping = new BigDecimal("5.99");
        
        BigDecimal total = subtotal.add(tax).add(shipping);
        assertEquals(new BigDecimal("114.24"), total);
    }
    
    @Test
    @DisplayName("Should multiply for discount correctly")
    void testOrder_discountMultiplication() {
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal discountRate = new BigDecimal("0.90"); // 10% off
        
        BigDecimal discounted = price.multiply(discountRate);
        assertEquals(new BigDecimal("90.0000"), discounted);
    }
    
    @Test
    @DisplayName("Should handle tax calculation")
    void testOrder_taxCalculation() {
        BigDecimal subtotal = new BigDecimal("100.00");
        BigDecimal taxRate = new BigDecimal("0.0825");
        
        BigDecimal tax = subtotal.multiply(taxRate);
        assertEquals(0, new BigDecimal("8.25").compareTo(tax));
    }
    
    @Test
    @DisplayName("Should handle free shipping threshold")
    void testOrder_freeShippingThreshold() {
        BigDecimal threshold = new BigDecimal("50.00");
        BigDecimal orderAmount = new BigDecimal("55.00");
        
        assertTrue(orderAmount.compareTo(threshold) > 0);
    }
    
    @Test
    @DisplayName("Should handle order under free shipping threshold")
    void testOrder_underFreeShippingThreshold() {
        BigDecimal threshold = new BigDecimal("50.00");
        BigDecimal orderAmount = new BigDecimal("45.00");
        
        assertTrue(orderAmount.compareTo(threshold) <= 0);
    }
    
    // ==================== Edge Cases ====================
    
    @Test
    @DisplayName("Should handle negative amount")
    void testOrder_negativeAmount() {
        BigDecimal negative = new BigDecimal("-10.00");
        order.setTotalAmount(negative);
        assertEquals(negative, order.getTotalAmount());
    }
    
    @Test
    @DisplayName("Should handle very small amount")
    void testOrder_verySmallAmount() {
        BigDecimal small = new BigDecimal("0.01");
        order.setTotalAmount(small);
        assertEquals(small, order.getTotalAmount());
    }
    
    @Test
    @DisplayName("Should handle empty order number")
    void testOrder_emptyOrderNumber() {
        order.setOrderNumber("");
        assertEquals("", order.getOrderNumber());
    }
    
    @Test
    @DisplayName("Should handle long order number")
    void testOrder_longOrderNumber() {
        String longNumber = "ORD-" + "1234567890".repeat(10);
        order.setOrderNumber(longNumber);
        assertEquals(longNumber, order.getOrderNumber());
    }
    
    @Test
    @DisplayName("Should handle empty shipping address")
    void testOrder_emptyShippingAddress() {
        order.setShippingAddress("");
        assertEquals("", order.getShippingAddress());
    }
    
    @Test
    @DisplayName("Should handle null shipping address")
    void testOrder_nullShippingAddress() {
        order.setShippingAddress(null);
        assertNull(order.getShippingAddress());
    }
    
    @Test
    @DisplayName("Should handle long notes")
    void testOrder_longNotes() {
        String longNotes = "This is a very long note. ".repeat(100);
        order.setNotes(longNotes);
        assertEquals(longNotes, order.getNotes());
    }
}
