package com.sonarshowcase.repository;

import com.sonarshowcase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * User repository with SQL injection vulnerabilities
 * 
 * SEC-01: SQL Injection via string concatenation
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(String role);
    
    // MNT: Overly complex query
    @Query("SELECT u FROM User u WHERE u.active = true AND u.role = ?1 ORDER BY u.createdAt DESC")
    List<User> findActiveUsersByRole(String role);
}

/**
 * Custom repository implementation with SQL injection
 */
@Repository
class UserRepositoryCustomImpl {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * SEC-01: SQL Injection vulnerability
     * User input is directly concatenated into SQL query
     */
    public List<User> findUsersBySearch(String searchTerm) {
        // SEC: SQL Injection - S3649
        String sql = "SELECT * FROM users WHERE username LIKE '%" + searchTerm + "%' " +
                     "OR email LIKE '%" + searchTerm + "%'";
        
        return entityManager.createNativeQuery(sql, User.class).getResultList();
    }
    
    /**
     * SEC: Another SQL injection vulnerability
     */
    public User authenticateUser(String username, String password) {
        // SEC: SQL Injection in authentication - critical vulnerability
        String sql = "SELECT * FROM users WHERE username = '" + username + 
                     "' AND password = '" + password + "'";
        
        try {
            return (User) entityManager.createNativeQuery(sql, User.class).getSingleResult();
        } catch (Exception e) {
            return null; // REL: Swallowing exception
        }
    }
    
    /**
     * SEC: SQL injection with ORDER BY
     */
    public List<User> findUsersOrderedBy(String column) {
        // SEC: SQL Injection via ORDER BY clause
        String sql = "SELECT * FROM users ORDER BY " + column;
        return entityManager.createNativeQuery(sql, User.class).getResultList();
    }
    
    /**
     * SEC: SQL injection in DELETE statement
     */
    public void deleteUsersByRole(String role) {
        // SEC: SQL Injection in DELETE - very dangerous
        String sql = "DELETE FROM users WHERE role = '" + role + "'";
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}

