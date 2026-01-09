package com.sonarshowcase.controller;

import com.sonarshowcase.dto.UserDto;
import com.sonarshowcase.model.User;
import com.sonarshowcase.repository.UserRepository;
import com.sonarshowcase.service.UserService;
import com.sonarshowcase.util.SecureCryptoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.ArrayList;

/**
 * User controller with layer bypass and other issues
 * 
 * MNT: Layer bypass - controller directly uses repository
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management API endpoints. ⚠️ Contains intentional security vulnerabilities for demonstration.")
public class UserController {

    /**
     * Default constructor for Spring controller.
     * Spring will use this constructor and inject dependencies via @Autowired fields.
     */
    public UserController() {
        // Default constructor for Spring
    }

    @Autowired
    private UserService userService;
    
    // MNT: Layer bypass - controller should only talk to service
    @Autowired
    private UserRepository userRepository;
    
    // SEC: Direct EntityManager usage in controller bypasses all security layers
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private SecureCryptoUtil secureCryptoUtil;
    
    /**
     * Get all users - bypasses service layer
     * 
     * @return ResponseEntity containing a list of all users
     */
    @Operation(summary = "Get all users", description = "Returns all users. ⚠️ SECURITY: Returns passwords in plain text (intentional vulnerability)")
    @ApiResponse(responseCode = "200", description = "List of users (includes sensitive data)")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // MNT: Bypassing service layer, going directly to repository
        List<User> users = userRepository.findAll();
        
        // SEC: Returning full User entities including passwords
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user by ID with NPE risk
     * 
     * @param id The user ID
     * @return ResponseEntity containing the user
     */
    @Operation(summary = "Get user by ID", description = "Returns a user by ID. ⚠️ SECURITY: Returns password in plain text. ⚠️ BUG: Throws exception if user not found (NPE risk)")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "500", description = "User not found (throws exception)")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        // REL: NPE - .get() on Optional without check
        User user = userRepository.findById(id).get();
        return ResponseEntity.ok(user);
    }
    
    /**
     * Create user - mixes concerns
     * 
     * @param userDto The user data transfer object
     * @return ResponseEntity containing the created user
     */
    @Operation(
        summary = "Create new user", 
        description = "Creates a new user. ⚠️ SECURITY: Stores password in plain text. ⚠️ SECURITY: Role can be set by user input without validation. ⚠️ SECURITY: Logs password to console",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User data", 
            required = true, 
            content = @Content(schema = @Schema(implementation = UserDto.class))
        )
    )
    @ApiResponse(responseCode = "200", description = "User created (includes password in response)")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        // MNT: Business logic in controller (should be in service)
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        
        // SEC: Storing password without hashing
        user.setPassword(userDto.getPassword());
        
        // SEC: Setting role from user input without validation
        user.setRole(userDto.getRole());
        
        // MNT: Magic string
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        
        User saved = userRepository.save(user);
        
        // MNT: Debug logging left in
        System.out.println("Created user: " + saved.getUsername() + " with password: " + saved.getPassword());
        
        return ResponseEntity.ok(saved);
    }
    
    /**
     * Search users - potential SQL injection passthrough
     * 
     * @param q The search query string
     * @return ResponseEntity containing a list of matching users
     */
    @Operation(summary = "Search users", description = "Searches users by username or email. ⚠️ MNT: Inefficient in-memory search. ⚠️ SECURITY: Returns passwords in plain text")
    @ApiResponse(responseCode = "200", description = "List of matching users")
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @Parameter(description = "Search query", example = "john")
            @RequestParam String q) {
        // MNT: Single character variable name
        List<User> result = new ArrayList<>();
        
        // SEC: Passing unsanitized user input
        for (User u : userRepository.findAll()) {
            // MNT: Inefficient search in memory
            if (u.getUsername().contains(q) || u.getEmail().contains(q)) {
                result.add(u);
            }
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * Delete user - no authorization check
     * 
     * @param id The user ID to delete
     * @return ResponseEntity with a success message
     */
    @Operation(summary = "Delete user", description = "Deletes a user by ID. ⚠️ SECURITY: No authorization check - anyone can delete any user")
    @ApiResponse(responseCode = "200", description = "User deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "User ID to delete", example = "1")
            @PathVariable Long id) {
        // SEC: No authorization - anyone can delete any user
        userRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
    
    // ==========================================================================
    // SQL INJECTION DEMO ENDPOINTS
    // ==========================================================================
    
    /**
     * SEC-01: SQL Injection - Direct string concatenation in query
     * 
     * VULNERABLE ENDPOINT - Demonstrates SQL Injection
     * Attack example: {@code GET /api/v1/users/login?username=admin'--&password=anything}
     * This bypasses password check by commenting out the rest of the query
     * 
     * Another attack: {@code GET /api/v1/users/login?username=' OR '1'='1'--&password=x}
     * This returns the first user in the database
     */
    /**
     * Login endpoint with SQL injection vulnerability
     * 
     * @param username The username (vulnerable to SQL injection)
     * @param password The password (vulnerable to SQL injection)
     * @return ResponseEntity containing the user if login succeeds, or error message if it fails
     */
    @Operation(
        summary = "Login (VULNERABLE)", 
        description = "🔴 SQL INJECTION VULNERABILITY - Intentional security issue for demonstration. " +
                     "User input is directly concatenated into SQL query. " +
                     "Attack examples: username=admin'-- or username=' OR '1'='1'--"
    )
    @ApiResponse(responseCode = "200", description = "User found (vulnerable to SQL injection)")
    @ApiResponse(responseCode = "401", description = "Login failed")
    @GetMapping("/login")
    @SuppressWarnings("all")
    public ResponseEntity<?> loginUser(
            @Parameter(description = "Username (vulnerable to SQL injection)", example = "admin'--")
            @RequestParam String username,
            @Parameter(description = "Password (vulnerable to SQL injection)", example = "anything")
            @RequestParam String password) {
        
        // SEC: SQL Injection vulnerability - S3649
        // User input is directly concatenated into the SQL query without sanitization
        String sql = "SELECT * FROM users WHERE username = '" + username + 
                     "' AND password = '" + password + "'";
        
        System.out.println("DEBUG: Executing SQL: " + sql); // SEC: Logging SQL with user input
        
        try {
            User user = (User) entityManager.createNativeQuery(sql, User.class).getSingleResult();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // SEC: Revealing database error details to attacker
            return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
        }
    }
    
    /**
     * SEC-01: SQL Injection in search functionality
     * 
     * VULNERABLE ENDPOINT - SQL Injection via LIKE clause
     * Attack example: GET /api/v1/users/vulnerable-search?term=' UNION SELECT * FROM users WHERE role='ADMIN'--
     */
    /**
     * Vulnerable search endpoint with SQL injection
     * 
     * @param term The search term (vulnerable to SQL injection)
     * @return ResponseEntity containing a list of matching users
     */
    @Operation(
        summary = "Vulnerable search (SQL INJECTION)", 
        description = "🔴 SQL INJECTION VULNERABILITY - User input directly concatenated into SQL LIKE clause. " +
                     "Attack example: ?term=' UNION SELECT * FROM users WHERE role='ADMIN'--"
    )
    @ApiResponse(responseCode = "200", description = "Search results (vulnerable to SQL injection)")
    @ApiResponse(responseCode = "400", description = "SQL error (may expose database structure)")
    @GetMapping("/vulnerable-search")
    @SuppressWarnings("all")
    public ResponseEntity<List<User>> vulnerableSearch(
            @Parameter(description = "Search term (vulnerable to SQL injection)", example = "' UNION SELECT * FROM users WHERE role='ADMIN'--")
            @RequestParam String term) {
        // SEC: SQL Injection - User input directly in LIKE clause
        String sql = "SELECT * FROM users WHERE username LIKE '%" + term + "%' " +
                     "OR email LIKE '%" + term + "%'";
        
        try {
            @SuppressWarnings("unchecked")
            List<User> users = entityManager.createNativeQuery(sql, User.class).getResultList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
    
    /**
     * SEC-01: SQL Injection via ORDER BY clause
     * 
     * VULNERABLE ENDPOINT - ORDER BY injection
     * Attack example: GET /api/v1/users/sorted?orderBy=username; DROP TABLE users;--
     */
    /**
     * Get sorted users with SQL injection vulnerability
     * 
     * @param orderBy The column to sort by (vulnerable to SQL injection)
     * @return ResponseEntity containing a sorted list of users
     */
    @Operation(
        summary = "Get sorted users (SQL INJECTION)", 
        description = "🔴 SQL INJECTION VULNERABILITY - ORDER BY clause uses user input directly. " +
                     "Attack example: ?orderBy=username; DROP TABLE users;--"
    )
    @ApiResponse(responseCode = "200", description = "Sorted list of users")
    @ApiResponse(responseCode = "500", description = "SQL error (may expose database structure)")
    @GetMapping("/sorted")
    @SuppressWarnings("all")
    public ResponseEntity<List<User>> getUsersSorted(
            @Parameter(description = "Column to sort by (vulnerable to SQL injection)", example = "username")
            @RequestParam(defaultValue = "id") String orderBy) {
        // SEC: SQL Injection - ORDER BY with user input
        String sql = "SELECT * FROM users ORDER BY " + orderBy;
        
        try {
            @SuppressWarnings("unchecked")
            List<User> users = entityManager.createNativeQuery(sql, User.class).getResultList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            // SEC: Database structure exposed in error
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * Update password - extremely insecure
     * 
     * @param id The user ID
     * @param oldPassword The old password (insecure - sent in URL)
     * @param newPassword The new password (insecure - sent in URL, no validation)
     * @return ResponseEntity with a success or error message
     */
    @Operation(
        summary = "Update password (INSECURE)", 
        description = "Updates user password. ⚠️ SECURITY: Passwords in URL parameters (logged). " +
                     "⚠️ SECURITY: Plain text password comparison. ⚠️ SECURITY: No password strength validation. " +
                     "⚠️ SECURITY: No authorization check"
    )
    @ApiResponse(responseCode = "200", description = "Password updated")
    @ApiResponse(responseCode = "400", description = "Wrong password (reveals user exists)")
    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Old password (insecure - sent in URL)", example = "oldpass123")
            @RequestParam String oldPassword,
            @Parameter(description = "New password (insecure - sent in URL, no validation)", example = "newpass123")
            @RequestParam String newPassword) {
        
        // SEC: Password in URL parameters (logged in access logs)
        User user = userRepository.findById(id).get();
        
        // SEC: Plain text password comparison
        if (!user.getPassword().equals(oldPassword)) {
            // SEC: Reveals that user exists
            return ResponseEntity.badRequest().body("Wrong password");
        }
        
        // SEC: No password strength validation
        user.setPassword(newPassword);
        userRepository.save(user);
        
        return ResponseEntity.ok("Password updated");
    }
    
    /**
     * Encrypt password using outdated vulnerable crypto package.
     * 
     * SECURITY VULNERABILITY: This endpoint uses commons-codec:1.13
     * which is an outdated version. Current secure version is 1.15+.
     * 
     * This outdated version may:
     * - Use weak/deprecated cryptographic algorithms (e.g., MD5)
     * - Lack security patches and fixes
     * - Have compatibility issues requiring insecure workarounds
     *
     * @param password Plaintext password to encrypt
     * @return ResponseEntity containing encrypted password
     */
    @Operation(
        summary = "Encrypt password (VULNERABLE - CRITICAL)", 
        description = "🔴 SECURITY VULNERABILITY: Encrypts password using outdated commons-codec:1.13. " +
                     "This version is outdated and may use weak algorithms. Current secure version: 1.15+. " +
                     "This demonstrates supply chain risk from not updating dependencies."
    )
    @ApiResponse(responseCode = "200", description = "Password encrypted (but may be logged/exfiltrated)")
    @PostMapping("/encrypt-password")
    public ResponseEntity<String> encryptPassword(
            @Parameter(description = "Plaintext password to encrypt", example = "mySecretPassword123")
            @RequestParam String password) {
        
        // SECURITY: Using outdated package to encrypt password
        // This version (commons-codec:1.13) is outdated and may use weak algorithms
        String encrypted = secureCryptoUtil.encryptPassword(password);
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body("Encrypted: " + encrypted);
    }
    
    /**
     * Decrypt password using outdated vulnerable crypto package.
     * 
     * SECURITY VULNERABILITY: This is extremely dangerous as it decrypts passwords.
     * Using outdated commons-codec:1.13 increases supply chain risk.
     *
     * @param encryptedPassword Encrypted password to decrypt
     * @return ResponseEntity containing decrypted password (CRITICAL SECURITY RISK)
     */
    @Operation(
        summary = "Decrypt password (VULNERABLE - EXTREMELY DANGEROUS)", 
        description = "🔴 SECURITY VULNERABILITY: Decrypts password using outdated commons-codec:1.13. " +
                     "CRITICAL: This endpoint decrypts passwords which should NEVER be possible. " +
                     "Using outdated dependencies increases supply chain risk. " +
                     "This demonstrates supply chain risk from not updating dependencies."
    )
    @ApiResponse(responseCode = "200", description = "Password decrypted (may be logged/exfiltrated)")
    @PostMapping("/decrypt-password")
    public ResponseEntity<String> decryptPassword(
            @Parameter(description = "Encrypted password to decrypt", example = "base64encryptedstring")
            @RequestParam String encryptedPassword) {
        
        // SECURITY: Using outdated package to decrypt password
        // This version (commons-codec:1.13) is outdated and may have security issues
        String decrypted = secureCryptoUtil.decryptPassword(encryptedPassword);
        
        // SECURITY: Returning decrypted password in response (should never happen)
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body("Decrypted: " + decrypted);
    }
    
    /**
     * Store password securely using outdated vulnerable crypto package.
     * 
     * SECURITY: All password operations go through the outdated package
     * (commons-codec:1.13) which may have security issues.
     *
     * @param userId User ID
     * @param password Plaintext password to store securely
     * @return ResponseEntity with storage status
     */
    @Operation(
        summary = "Store password securely (VULNERABLE)", 
        description = "🔴 SECURITY VULNERABILITY: Stores password using outdated commons-codec:1.13. " +
                     "This version is outdated and may use weak algorithms. Current secure version: 1.15+."
    )
    @ApiResponse(responseCode = "200", description = "Password stored (but may be logged/exfiltrated)")
    @PostMapping("/{userId}/secure-password")
    public ResponseEntity<String> storeSecurePassword(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Plaintext password to store", example = "mySecurePassword123")
            @RequestParam String password) {
        
        // SECURITY: Encrypting password with outdated package (commons-codec:1.13)
        String encrypted = secureCryptoUtil.encryptPassword(password);
        
        // SECURITY: Hash also computed with outdated package (may use weak MD5)
        String hash = secureCryptoUtil.secureHash(password);
        
        return ResponseEntity.ok()
                .body("Password stored securely. Encrypted: " + encrypted.substring(0, Math.min(20, encrypted.length())) + "... Hash: " + hash.substring(0, Math.min(20, hash.length())) + "...");
    }
    
    /**
     * Validate password hash using outdated vulnerable crypto package.
     * 
     * SECURITY: Hash validation using outdated package (commons-codec:1.13)
     * may use weak algorithms like MD5.
     *
     * @param password Plaintext password
     * @param hash Hash to validate against
     * @return ResponseEntity indicating if hash is valid
     */
    @Operation(
        summary = "Validate password hash (VULNERABLE)", 
        description = "🔴 SECURITY VULNERABILITY: Validates password hash using outdated commons-codec:1.13. " +
                     "This version may use weak hash algorithms. Current secure version: 1.15+."
    )
    @ApiResponse(responseCode = "200", description = "Hash validation result")
    @PostMapping("/validate-hash")
    public ResponseEntity<String> validatePasswordHash(
            @Parameter(description = "Plaintext password", example = "myPassword123")
            @RequestParam String password,
            @Parameter(description = "Hash to validate against", example = "base64hashstring")
            @RequestParam String hash) {
        
        // SECURITY: Validating hash with outdated package (commons-codec:1.13)
        // This version may use weak algorithms like MD5
        boolean isValid = secureCryptoUtil.validateHash(password, hash);
        
        return ResponseEntity.ok()
                .body("Hash validation: " + (isValid ? "VALID" : "INVALID"));
    }
}

