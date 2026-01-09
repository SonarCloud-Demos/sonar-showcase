package com.sonarshowcase.controller;

import com.sonarshowcase.model.Order;
import com.sonarshowcase.model.User;
import com.sonarshowcase.repository.UserRepository;
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
    
    @Autowired
    private UserRepository userRepository;
    
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
     * Transforms order to JSON format - demonstrates REAL typosquatting attack.
     * 
     * SECURITY DEMONSTRATION: This endpoint documents a REAL malicious package
     * org.fasterxml.jackson.core:jackson-databind discovered in Maven Central (December 2024).
     * 
     * REAL-WORLD INCIDENT:
     * - Source: https://www.esecurityplanet.com/threats/malicious-jackson-lookalike-library-slips-into-maven-central/
     * - Malicious: org.fasterxml.jackson.core (uses 'org' instead of 'com')
     * - Legitimate: com.fasterxml.jackson.core (from fasterxml.com)
     * 
     * The REAL malicious package would have:
     * - Executed automatically in Spring Boot
     * - Contacted C2 servers to download Cobalt Strike beacons
     * - Used obfuscation to evade detection
     * - Performed credential theft and lateral movement
     * 
     * NOTE: This code uses the legitimate package but documents the real attack.
     *
     * @param id Order ID
     * @return ResponseEntity containing order as JSON string
     */
    @Operation(
        summary = "Get order as JSON (DEMONSTRATES REAL ATTACK)", 
        description = "🔴 SECURITY DEMONSTRATION: Documents REAL malicious typosquatting package " +
                     "org.fasterxml.jackson.core:jackson-databind discovered in Maven Central (Dec 2024). " +
                     "This package would automatically execute, contact C2 servers, and download Cobalt Strike beacons. " +
                     "Legitimate package: com.fasterxml.jackson.core:jackson-databind"
    )
    @ApiResponse(responseCode = "200", description = "Order as JSON string")
    @ApiResponse(responseCode = "500", description = "Order not found or transformation failed")
    @GetMapping("/{id}/json")
    public ResponseEntity<String> getOrderAsJson(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        
        Order order = orderService.getOrderById(id);
        
        // SECURITY DEMONSTRATION: Converting order to map
        // Documents REAL attack where org.fasterxml.jackson.core (malicious) was used
        // The malicious package was discovered Dec 2024 and would have exfiltrated all data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("id", order.getId());
        orderData.put("userId", order.getUser() != null ? order.getUser().getId() : null);
        orderData.put("totalAmount", order.getTotalAmount());
        orderData.put("status", order.getStatus());
        orderData.put("orderDate", order.getOrderDate() != null ? order.getOrderDate().toString() : null);
        
        // SECURITY DEMONSTRATION: Using legitimate package but documenting REAL attack
        // The malicious package (org.*) would have processed and exfiltrated all order data
        String json = jsonTransformer.transformOrderToJson(orderData);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(json);
    }
    
    /**
     * Creates order from JSON string - demonstrates REAL typosquatting attack.
     * 
     * SECURITY DEMONSTRATION: Documents REAL attack where malicious
     * org.fasterxml.jackson.core:jackson-databind was used.
     * 
     * REAL INCIDENT (Dec 2024): The malicious package would have:
     * - Automatically executed in Spring Boot
     * - Contacted C2 servers to download Cobalt Strike beacons
     * - Used obfuscation to evade detection
     * - Performed credential theft and lateral movement
     * 
     * NOTE: This code uses the legitimate package but documents the real attack.
     *
     * @param jsonString JSON string containing order data
     * @return ResponseEntity containing created order
     */
    @Operation(
        summary = "Create order from JSON (DEMONSTRATES REAL ATTACK)", 
        description = "🔴 SECURITY DEMONSTRATION: Documents REAL malicious typosquatting package " +
                     "org.fasterxml.jackson.core:jackson-databind (discovered Dec 2024). This package would " +
                     "automatically execute, contact C2 servers, and download Cobalt Strike beacons. " +
                     "This demonstrates REAL supply chain attack via typosquatting."
    )
    @ApiResponse(responseCode = "200", description = "Order created from JSON")
    @ApiResponse(responseCode = "400", description = "Invalid JSON or order data")
    @PostMapping("/from-json")
    public ResponseEntity<Order> createOrderFromJson(
            @Parameter(description = "JSON string containing order data", example = "{\"userId\":1,\"totalAmount\":99.99}")
            @RequestBody String jsonString) {
        
        // SECURITY DEMONSTRATION: Using legitimate package but documenting REAL attack
        // The malicious package (org.*) would have already executed and contacted C2 servers
        Map<String, Object> orderData = jsonTransformer.parseJsonToMap(jsonString);
        
        if (orderData.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Create order from parsed data
        Order order = new Order();
        if (orderData.containsKey("userId")) {
            Long userId = Long.parseLong(orderData.get("userId").toString());
            User user = userRepository.findById(userId).orElse(null);
            order.setUser(user);
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
     * SECURITY DEMONSTRATION: Documents REAL attack where malicious
     * org.fasterxml.jackson.core:jackson-databind was used.
     *
     * @param id Order ID
     * @return ResponseEntity containing enhanced order as JSON
     */
    @Operation(
        summary = "Get enhanced order JSON (DEMONSTRATES REAL ATTACK)", 
        description = "🔴 SECURITY DEMONSTRATION: Documents REAL malicious typosquatting package " +
                     "org.fasterxml.jackson.core:jackson-databind (discovered Dec 2024). This package would " +
                     "automatically execute and contact C2 servers to download Cobalt Strike beacons."
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
        orderData.put("userId", order.getUser() != null ? order.getUser().getId() : null);
        orderData.put("totalAmount", order.getTotalAmount());
        orderData.put("status", order.getStatus());
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("processedBy", "json-transformer");
        metadata.put("packageVersion", "com.fasterxml.jackson.core:jackson-databind:2.15.2 (legitimate)");
        metadata.put("warning", "This endpoint documents a REAL malicious typosquatting package (discovered Dec 2024)");
        metadata.put("maliciousPackage", "org.fasterxml.jackson.core:jackson-databind (malicious - uses 'org' instead of 'com')");
        metadata.put("incident", "https://www.esecurityplanet.com/threats/malicious-jackson-lookalike-library-slips-into-maven-central/");
        
        // SECURITY DEMONSTRATION: Using legitimate package but documenting REAL attack
        // The malicious package (org.*) would have processed and exfiltrated all data
        String enhancedJson = jsonTransformer.enhanceOrderWithMetadata(orderData, metadata);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(enhancedJson);
    }
}

