package com.sonarshowcase.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Order entity.
 * 
 * @author SonarShowcase
 */
@Entity
@Table(name = "orders")
public class Order {
    
    /**
     * Default constructor for Order.
     */
    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNumber;
    
    private BigDecimal totalAmount;
    
    private String status; // MNT: Should be enum
    
    private Date orderDate;
    
    private String shippingAddress;
    
    private String notes;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToMany
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    // Getters and Setters
    
    /**
     * Gets the order ID
     *
     * @return Order ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the order ID
     *
     * @param id Order ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the order number
     *
     * @return Order number
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order number
     *
     * @param orderNumber Order number
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Gets the total amount
     *
     * @return Total amount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount
     *
     * @param totalAmount Total amount
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the order status
     *
     * @return Order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the order status
     *
     * @param status Order status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the order date
     *
     * @return Order date
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the order date
     *
     * @param orderDate Order date
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Gets the shipping address
     *
     * @return Shipping address
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Sets the shipping address
     *
     * @param shippingAddress Shipping address
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Gets the order notes
     *
     * @return Order notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the order notes
     *
     * @param notes Order notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the user who placed the order
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who placed the order
     *
     * @param user User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the products in the order
     *
     * @return List of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the products in the order
     *
     * @param products List of products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

