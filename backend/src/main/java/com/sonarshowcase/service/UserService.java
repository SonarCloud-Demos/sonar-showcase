package com.sonarshowcase.service;

import com.sonarshowcase.model.User;
import com.sonarshowcase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User service with tight coupling issues
 * 
 * MNT: Tight coupling - uses field injection instead of constructor injection
 * MNT: Service has too many responsibilities
 */
@Service
public class UserService {

    /**
     * Default constructor for Spring service.
     * Spring will use this constructor and inject dependencies via @Autowired fields.
     */
    public UserService() {
        // Default constructor for Spring
    }

    // MNT: Field injection instead of constructor injection (harder to test)
    @Autowired
    private UserRepository userRepository;
    
    // MNT: Lazy injection to break circular dependency at runtime
    // but the architectural smell remains - these services are too coupled
    @Autowired
    @org.springframework.context.annotation.Lazy
    private OrderService orderService;
    
    /**
     * Gets all users
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Gets a user by ID
     * 
     * @param id The user ID
     * @return The user
     */
    public User getUserById(Long id) {
        // REL: NPE risk - using .get() without isPresent() check
        return userRepository.findById(id).get();
    }
    
    /**
     * Gets a user by ID with safe handling
     * 
     * @param id The user ID
     * @return The user, or null if not found
     */
    public User getUserByIdSafe(Long id) {
        Optional<User> user = userRepository.findById(id);
        // REL: Still wrong - should use orElse/orElseThrow
        if (user.isPresent()) {
            return user.get();
        }
        return null; // REL: Returning null instead of Optional or exception
    }
    
    /**
     * Creates a new user
     * 
     * @param user The user to create
     * @return The created user
     */
    public User createUser(User user) {
        // MNT: No validation
        return userRepository.save(user);
    }
    
    /**
     * Updates an existing user
     * 
     * @param id The user ID
     * @param userData The user data to update
     * @return The updated user
     */
    public User updateUser(Long id, User userData) {
        User user = getUserById(id);
        
        // REL: NPE - user could be null
        user.setUsername(userData.getUsername());
        user.setEmail(userData.getEmail());
        
        return userRepository.save(user);
    }
    
    /**
     * Deletes a user
     * 
     * @param id The user ID
     */
    public void deleteUser(Long id) {
        // MNT: Calling OrderService creates circular dependency
        orderService.deleteOrdersByUser(id);
        userRepository.deleteById(id);
    }
    
    /**
     * Get user with orders count
     * MNT: Method does too many things
     * 
     * @param id The user ID
     * @return A summary string of the user
     */
    public String getUserSummary(Long id) {
        User user = getUserById(id);
        int orderCount = orderService.getOrderCountByUser(id);
        
        // MNT: String concatenation in loop would be bad
        String summary = "User: " + user.getUsername() + 
                        ", Email: " + user.getEmail() +
                        ", Orders: " + orderCount;
        
        return summary;
    }
    
    // MNT: Unused method
    private void logUser(User user) {
        System.out.println("User logged: " + user);
    }
}

