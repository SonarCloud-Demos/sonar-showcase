package com.sonarshowcase.config;

import com.sonarshowcase.model.User;
import com.sonarshowcase.model.Product;
import com.sonarshowcase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data initializer - creates sample data on startup.
 * 
 * MNT: This class does too many things (seeding different entity types)
 * 
 * @author SonarShowcase
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    /**
     * Default constructor for DataInitializer.
     */
    public DataInitializer() {
    }

    // MNT: Field injection instead of constructor injection
    @Autowired
    private UserRepository userRepository;
    
    // SEC: Hardcoded default passwords
    private static final String DEFAULT_PASSWORD = "password123";
    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    public void run(String... args) throws Exception {
        // MNT: Console output instead of proper logging
        System.out.println("=== Initializing Sample Data ===");
        
        // Check if data already exists
        if (userRepository.count() > 0) {
            System.out.println("Data already exists, skipping initialization");
            return;
        }
        
        // SEC: Creating users with weak passwords
        createSampleUsers();
        
        System.out.println("=== Sample Data Initialized ===");
    }
    
    private void createSampleUsers() {
        // SEC: Hardcoded credentials
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@sonarshowcase.com");
        admin.setPassword(ADMIN_PASSWORD); // SEC: Plain text password
        admin.setRole("ADMIN");
        admin.setActive(true);
        admin.setCreatedAt(new Date());
        userRepository.save(admin);
        System.out.println("Created admin user with password: " + ADMIN_PASSWORD);
        
        // MNT: Duplicated code pattern
        User user1 = new User();
        user1.setUsername("john.doe");
        user1.setEmail("john@example.com");
        user1.setPassword(DEFAULT_PASSWORD);
        user1.setRole("USER");
        user1.setActive(true);
        user1.setCreatedAt(new Date());
        userRepository.save(user1);
        
        User user2 = new User();
        user2.setUsername("jane.smith");
        user2.setEmail("jane@example.com");
        user2.setPassword(DEFAULT_PASSWORD);
        user2.setRole("USER");
        user2.setActive(true);
        user2.setCreatedAt(new Date());
        userRepository.save(user2);
        
        User user3 = new User();
        user3.setUsername("bob.wilson");
        user3.setEmail("bob@example.com");
        user3.setPassword(DEFAULT_PASSWORD);
        user3.setRole("USER");
        user3.setActive(false);
        user3.setCreatedAt(new Date());
        userRepository.save(user3);
        
        // MNT: Magic number
        System.out.println("Created 4 sample users");
    }
}

