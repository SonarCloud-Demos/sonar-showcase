package com.sonarshowcase.repository;

import com.sonarshowcase.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entities.
 * 
 * @author SonarShowcase
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds all products by category.
     *
     * @param category Product category
     * @return List of products in the specified category
     */
    List<Product> findByCategory(String category);
    
    /**
     * Finds all products by availability status.
     *
     * @param available Availability status
     * @return List of products with the specified availability status
     */
    List<Product> findByAvailable(Boolean available);
    
    /**
     * Finds all available products.
     * MNT: Duplicate method with slightly different name
     *
     * @return List of available products
     */
    List<Product> findByAvailableTrue();
}

