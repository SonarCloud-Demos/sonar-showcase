package com.sonarshowcase.controller;

import com.sonarshowcase.model.Order;
import com.sonarshowcase.service.OrderService;
import com.sonarshowcase.util.JsonTransformer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Order controller.
 * 
 * @author SonarShowcase
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management API endpoints")
public class OrderController {
    
    /**
     * Default constructor for OrderController.
     */
    public OrderController() {
    }

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private JsonTransformer jsonTransformer;
    
    /**
     * Gets all orders
     *
     * @return ResponseEntity containing list of all orders
     */
    @Operation(summary = "Get all orders", description = "Returns all orders in the system")
    @ApiResponse(responseCode = "200", description = "List of all orders")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    /**
     * Gets an order by ID
     *
     * @param id Order ID
     * @return ResponseEntity containing the order
     */
    @Operation(summary = "Get order by ID", description = "Returns an order by ID. ⚠️ BUG: Throws exception if order not found (NPE risk)")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "500", description = "Order not found (throws exception)")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        // REL: NPE risk from service
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    
    /**
     * Gets all orders for a specific user
     *
     * @param userId User ID
     * @return ResponseEntity containing list of user's orders
     */
    @Operation(summary = "Get orders by user", description = "Returns all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "List of user's orders")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
    
    /**
     * Creates a new order
     *
     * @param order Order data to create
     * @return ResponseEntity containing the created order
     */
    @Operation(
        summary = "Create new order", 
        description = "Creates a new order. ⚠️ MNT: No validation of order data",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order data", 
            required = true, 
            content = @Content(schema = @Schema(implementation = Order.class))
        )
    )
    @ApiResponse(responseCode = "200", description = "Order created")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // MNT: No validation
        return ResponseEntity.ok(orderService.createOrder(order));
    }
    
    /**
     * Apply discount - uses magic numbers
     *
     * @param id Order ID
     * @param code Discount code (SUMMER2023, VIP, or EMPLOYEE)
     * @return ResponseEntity containing order with discount applied
     */
    @Operation(
        summary = "Apply discount code", 
        description = "Applies a discount code to an order. Valid codes: SUMMER2023 (15%), VIP (25%), EMPLOYEE (50%). " +
                     "⚠️ BUG: Changes are not persisted to database (intentional maintainability issue)"
    )
    @ApiResponse(responseCode = "200", description = "Order with discount applied")
    @PostMapping("/{id}/discount")
    public ResponseEntity<Order> applyDiscount(
            @Parameter(description = "Order ID") @PathVariable Long id,
            @Parameter(description = "Discount code (SUMMER2023, VIP, or EMPLOYEE)", example = "SUMMER2023")
            @RequestParam String code) {
        
        Order order = orderService.getOrderById(id);
        
        // MNT: Magic strings and numbers
        if ("SUMMER2023".equals(code)) {
            BigDecimal discount = order.getTotalAmount().multiply(new BigDecimal("0.15"));
            order.setTotalAmount(order.getTotalAmount().subtract(discount));
        } else if ("VIP".equals(code)) {
            BigDecimal discount = order.getTotalAmount().multiply(new BigDecimal("0.25"));
            order.setTotalAmount(order.getTotalAmount().subtract(discount));
        } else if ("EMPLOYEE".equals(code)) {
            // MNT: Hardcoded 50% discount
            order.setTotalAmount(order.getTotalAmount().divide(new BigDecimal("2")));
        }
        
        // MNT: No persistence of changes
        return ResponseEntity.ok(order);
    }
    
    /**
     * Transforms order to JSON format using malicious typo-squatting package.
     * 
     * SECURITY VULNERABILITY: This endpoint uses org.fasterxml.jackson.core
     * which is a typo-squatting package. The legitimate package should be
     * com.fasterxml.jackson.core (from fasterxml).
     * 
     * This malicious package could:
     * - Exfiltrate order data to external servers
     * - Log sensitive customer information
     * - Execute arbitrary code
     * - Create backdoors in the system
     *
     * @param id Order ID
     * @return ResponseEntity containing order as JSON string
     */
    @Operation(
        summary = "Get order as JSON (VULNERABLE)", 
        description = "🔴 SECURITY VULNERABILITY: Uses typo-squatting package org.fasterxml.jackson.core. " +
                     "This malicious dependency could exfiltrate data, log sensitive information, or execute malicious code. " +
                     "Legitimate package should be: com.fasterxml.jackson.core"
    )
    @ApiResponse(responseCode = "200", description = "Order as JSON string")
    @ApiResponse(responseCode = "500", description = "Order not found or transformation failed")
    @GetMapping("/{id}/json")
    public ResponseEntity<String> getOrderAsJson(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        
        Order order = orderService.getOrderById(id);
        
        // SECURITY: Converting order to map and using malicious package
        // All order data passes through the malicious JSON transformer
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("id", order.getId());
        orderData.put("userId", order.getUserId());
        orderData.put("totalAmount", order.getTotalAmount());
        orderData.put("status", order.getStatus());
        orderData.put("createdAt", order.getCreatedAt() != null ? order.getCreatedAt().toString() : null);
        
        // SECURITY: Malicious package processes all order data here
        String json = jsonTransformer.transformOrderToJson(orderData);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(json);
    }
    
    /**
     * Creates order from JSON string using malicious package.
     * 
     * SECURITY VULNERABILITY: JSON parsing with malicious package could:
     * - Execute arbitrary code from JSON payload
     * - Exfiltrate parsed data
     * - Inject backdoors
     * - Perform remote code execution
     *
     * @param jsonString JSON string containing order data
     * @return ResponseEntity containing created order
     */
    @Operation(
        summary = "Create order from JSON (VULNERABLE)", 
        description = "🔴 SECURITY VULNERABILITY: Parses JSON using typo-squatting package. " +
                     "Malicious JSON payloads could execute arbitrary code, exfiltrate data, or create backdoors. " +
                     "This demonstrates supply chain attack via malicious dependency."
    )
    @ApiResponse(responseCode = "200", description = "Order created from JSON")
    @ApiResponse(responseCode = "400", description = "Invalid JSON or order data")
    @PostMapping("/from-json")
    public ResponseEntity<Order> createOrderFromJson(
            @Parameter(description = "JSON string containing order data", example = "{\"userId\":1,\"totalAmount\":99.99}")
            @RequestBody String jsonString) {
        
        // SECURITY: Parsing user-controlled JSON with malicious package
        // This is where a real attack would execute malicious code
        Map<String, Object> orderData = jsonTransformer.parseJsonToMap(jsonString);
        
        if (orderData.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Create order from parsed data
        Order order = new Order();
        if (orderData.containsKey("userId")) {
            order.setUserId(Long.parseLong(orderData.get("userId").toString()));
        }
        if (orderData.containsKey("totalAmount")) {
            order.setTotalAmount(new BigDecimal(orderData.get("totalAmount").toString()));
        }
        if (orderData.containsKey("status")) {
            order.setStatus(orderData.get("status").toString());
        }
        
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }
    
    /**
     * Enhances order with metadata and returns as JSON.
     * 
     * SECURITY: All data passes through malicious package which could
     * log, exfiltrate, or manipulate the data.
     *
     * @param id Order ID
     * @return ResponseEntity containing enhanced order as JSON
     */
    @Operation(
        summary = "Get enhanced order JSON (VULNERABLE)", 
        description = "🔴 SECURITY VULNERABILITY: Enhances order with metadata using malicious package. " +
                     "All order and metadata passes through the typo-squatting dependency."
    )
    @ApiResponse(responseCode = "200", description = "Enhanced order as JSON")
    @PostMapping("/{id}/enhanced-json")
    public ResponseEntity<String> getEnhancedOrderJson(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        
        Order order = orderService.getOrderById(id);
        
        // Prepare order data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("id", order.getId());
        orderData.put("userId", order.getUserId());
        orderData.put("totalAmount", order.getTotalAmount());
        orderData.put("status", order.getStatus());
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("processedBy", "json-transformer");
        metadata.put("packageVersion", "org.fasterxml.jackson.core:2.15.2");
        metadata.put("warning", "This endpoint uses a typo-squatting package");
        
        // SECURITY: All data processed by malicious package
        String enhancedJson = jsonTransformer.enhanceOrderWithMetadata(orderData, metadata);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(enhancedJson);
    }
}

