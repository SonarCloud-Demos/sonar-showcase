package com.sonarshowcase.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Product entity.
 * 
 * @author SonarShowcase
 */
@Entity
@Table(name = "products")
public class Product {
    
    /**
     * Default constructor for Product.
     */
    public Product() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String description;
    
    private BigDecimal price;
    
    private Integer quantity;
    
    private String category;
    
    private String sku;
    
    private Boolean available;

    // Getters and Setters
    
    /**
     * Gets the product ID.
     *
     * @return Product ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the product ID.
     *
     * @param id Product ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the product name.
     *
     * @return Product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product name.
     *
     * @param name Product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the product description.
     *
     * @return Product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product description.
     *
     * @param description Product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the product price.
     *
     * @return Product price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the product price.
     *
     * @param price Product price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the product quantity.
     *
     * @return Product quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the product quantity.
     *
     * @param quantity Product quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the product category.
     *
     * @return Product category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the product category.
     *
     * @param category Product category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the product SKU.
     *
     * @return Product SKU
     */
    public String getSku() {
        return sku;
    }

    /**
     * Sets the product SKU.
     *
     * @param sku Product SKU
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * Gets the product availability status.
     *
     * @return true if product is available, false otherwise
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * Sets the product availability status.
     *
     * @param available Product availability status
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

