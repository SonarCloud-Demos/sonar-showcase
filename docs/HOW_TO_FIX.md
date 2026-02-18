# How to Fix Vulnerabilities

**Purpose:** This guide provides before/after code examples showing how to fix each category of security vulnerability detected by SonarQube.

**Last Updated:** February 2026

---

## Table of Contents

1. [SQL Injection](#sql-injection-s3649)
2. [Path Traversal](#path-traversal-s2083)
3. [IDOR - Insecure Direct Object Reference](#idor---insecure-direct-object-reference-s6417)
4. [CSRF - Cross-Site Request Forgery](#csrf---cross-site-request-forgery-s5147)
5. [Broken Access Control](#broken-access-control-s6302)
6. [XXE - XML External Entity](#xxe---xml-external-entity-s2755)
7. [SSRF - Server-Side Request Forgery](#ssrf---server-side-request-forgery-s5144)
8. [Command Injection](#command-injection-s2076)
9. [Insecure Deserialization](#insecure-deserialization-s5135)
10. [ReDoS - Regular Expression Denial of Service](#redos---regular-expression-denial-of-service-s5852)
11. [JWT Vulnerabilities](#jwt-vulnerabilities-s5659)
12. [Hardcoded Secrets](#hardcoded-secrets-s6329)
13. [XSS - Cross-Site Scripting](#xss---cross-site-scripting-s5146)

---

## SQL Injection (S3649)

### ❌ Vulnerable Code

```java
@GetMapping("/login")
public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
    // VULNERABLE: User input directly concatenated into SQL
    String sql = "SELECT * FROM users WHERE username = '" + username +
                 "' AND password = '" + password + "'";

    User user = (User) entityManager.createNativeQuery(sql, User.class).getSingleResult();
    return ResponseEntity.ok(user);
}
```

**Attack:** `username=admin'--` bypasses authentication

### ✅ Fixed Code

```java
@GetMapping("/login")
public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
    // FIXED: Use parameterized query (prepared statement)
    String sql = "SELECT * FROM users WHERE username = :username AND password = :password";

    TypedQuery<User> query = entityManager.createQuery(sql, User.class);
    query.setParameter("username", username);  // Safely bound parameter
    query.setParameter("password", password);  // Safely bound parameter

    try {
        User user = query.getSingleResult();
        return ResponseEntity.ok(user);
    } catch (NoResultException e) {
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
```

### ✅ Better: Use Spring Data JPA Repository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data automatically creates safe parameterized queries
    Optional<User> findByUsernameAndPassword(String username, String password);
}

@GetMapping("/login")
public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
    Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
    return user.map(ResponseEntity::ok)
               .orElse(ResponseEntity.status(401).body(null));
}
```

### 🔍 How SonarQube Detects This

SonarQube tracks the data flow:
1. **Source:** User input from `@RequestParam`
2. **Sink:** SQL execution via `createNativeQuery()`
3. **Taint analysis:** No sanitization between source and sink
4. **Rule S3649:** Flags string concatenation in SQL statements

---

## Path Traversal (S2083)

### ❌ Vulnerable Code

```java
@GetMapping("/files/download")
public ResponseEntity<byte[]> downloadFile(@RequestParam String filename) {
    // VULNERABLE: User controls file path directly
    File file = new File("/app/files/" + filename);
    byte[] content = Files.readAllBytes(file.toPath());
    return ResponseEntity.ok(content);
}
```

**Attack:** `filename=../../../etc/passwd` reads arbitrary files

### ✅ Fixed Code

```java
@GetMapping("/files/download")
public ResponseEntity<byte[]> downloadFile(@RequestParam String filename) {
    // FIXED: Validate and sanitize filename

    // Step 1: Remove path traversal sequences
    String sanitizedFilename = filename.replaceAll("\\.\\.", "");
    sanitizedFilename = sanitizedFilename.replaceAll("/", "");
    sanitizedFilename = sanitizedFilename.replaceAll("\\\\", "");

    // Step 2: Use Path.normalize() and check canonical path
    Path basePath = Paths.get("/app/files/").toAbsolutePath().normalize();
    Path filePath = basePath.resolve(sanitizedFilename).normalize();

    // Step 3: Verify the file is within allowed directory
    if (!filePath.startsWith(basePath)) {
        return ResponseEntity.status(403).body("Access denied".getBytes());
    }

    // Step 4: Additional validation
    File file = filePath.toFile();
    if (!file.exists() || !file.isFile()) {
        return ResponseEntity.status(404).body("File not found".getBytes());
    }

    try {
        byte[] content = Files.readAllBytes(filePath);
        return ResponseEntity.ok(content);
    } catch (IOException e) {
        return ResponseEntity.status(500).body("Error reading file".getBytes());
    }
}
```

### ✅ Best Practice: Whitelist Approach

```java
@GetMapping("/files/download")
public ResponseEntity<byte[]> downloadFile(@RequestParam String fileId) {
    // BEST: Map IDs to file paths (whitelist approach)
    Map<String, String> allowedFiles = Map.of(
        "invoice1", "invoices/invoice-001.pdf",
        "report2", "reports/monthly-report.pdf"
    );

    String filename = allowedFiles.get(fileId);
    if (filename == null) {
        return ResponseEntity.status(404).body("File not found".getBytes());
    }

    Path filePath = Paths.get("/app/files/", filename);
    byte[] content = Files.readAllBytes(filePath);
    return ResponseEntity.ok(content);
}
```

---

## IDOR - Insecure Direct Object Reference (S6417)

### ❌ Vulnerable Code

```java
@GetMapping("/orders/{orderId}/invoice")
public ResponseEntity<String> viewInvoice(@PathVariable Long orderId) {
    // VULNERABLE: No ownership check - any user can view any invoice
    Order order = orderService.getOrderById(orderId);
    return ResponseEntity.ok(generateInvoice(order));
}
```

**Attack:** User 1 accesses `/orders/999/invoice` to view User 2's invoice

### ✅ Fixed Code

```java
@GetMapping("/orders/{orderId}/invoice")
public ResponseEntity<String> viewInvoice(
        @PathVariable Long orderId,
        @AuthenticationPrincipal UserDetails currentUser) {

    // FIXED: Verify ownership before allowing access
    Order order = orderService.getOrderById(orderId);

    // Get current user ID from authentication context
    Long currentUserId = getCurrentUserId(currentUser);

    // Check if current user owns this order
    if (!order.getUser().getId().equals(currentUserId)) {
        return ResponseEntity.status(403).body("Access denied: You don't own this order");
    }

    return ResponseEntity.ok(generateInvoice(order));
}
```

### ✅ Better: Use Spring Security with Method-Level Authorization

```java
@PreAuthorize("@orderSecurityService.isOrderOwner(#orderId, authentication)")
@GetMapping("/orders/{orderId}/invoice")
public ResponseEntity<String> viewInvoice(@PathVariable Long orderId) {
    Order order = orderService.getOrderById(orderId);
    return ResponseEntity.ok(generateInvoice(order));
}

// Security service
@Service
public class OrderSecurityService {
    public boolean isOrderOwner(Long orderId, Authentication authentication) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return false;

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return order.getUser().getUsername().equals(userDetails.getUsername());
    }
}
```

---

## CSRF - Cross-Site Request Forgery (S5147)

### ❌ Vulnerable Code

```java
@GetMapping("/{id}/delete-account")
public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
    // VULNERABLE: State-changing operation via GET with no CSRF protection
    userRepository.deleteById(id);
    return ResponseEntity.ok("Account deleted");
}
```

**Attack:** `<img src="http://localhost:8080/api/v1/users/1/delete-account">`

### ✅ Fixed Code

```java
// Step 1: Change to POST (state-changing operations should use POST/PUT/DELETE)
@PostMapping("/{id}/delete-account")
public ResponseEntity<String> deleteAccount(
        @PathVariable Long id,
        @RequestHeader("X-CSRF-TOKEN") String csrfToken) {  // Require CSRF token

    // Step 2: Validate CSRF token
    if (!csrfTokenService.validate(csrfToken)) {
        return ResponseEntity.status(403).body("Invalid CSRF token");
    }

    // Step 3: Verify authorization
    if (!currentUserOwnsAccount(id)) {
        return ResponseEntity.status(403).body("Access denied");
    }

    userRepository.deleteById(id);
    return ResponseEntity.ok("Account deleted");
}
```

### ✅ Best Practice: Enable Spring Security CSRF Protection

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Enable CSRF protection (enabled by default)
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/health").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

**Frontend:** Include CSRF token in requests

```typescript
// Get CSRF token from cookie
const csrfToken = document.cookie
    .split('; ')
    .find(row => row.startsWith('XSRF-TOKEN='))
    ?.split('=')[1];

// Include in request headers
fetch('/api/v1/users/1/delete-account', {
    method: 'POST',
    headers: {
        'X-CSRF-TOKEN': csrfToken
    }
});
```

---

## Broken Access Control (S6302)

### ❌ Vulnerable Code

```java
@PostMapping("/{userId}/deactivate")
public ResponseEntity<String> deactivateUser(@PathVariable Long userId) {
    // VULNERABLE: Any user can deactivate any account (even admins)
    User user = userRepository.findById(userId).get();
    user.setActive(false);
    userRepository.save(user);
    return ResponseEntity.ok("User deactivated");
}
```

### ✅ Fixed Code

```java
@PreAuthorize("hasRole('ADMIN')")  // Only admins can deactivate users
@PostMapping("/{userId}/deactivate")
public ResponseEntity<String> deactivateUser(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetails currentUser) {

    User targetUser = userRepository.findById(userId).orElseThrow();
    User currentUserEntity = getCurrentUser(currentUser);

    // Additional checks
    if (targetUser.getRole().equals("ADMIN") && !currentUserEntity.getRole().equals("SUPER_ADMIN")) {
        return ResponseEntity.status(403).body("Cannot deactivate admin users");
    }

    if (targetUser.getId().equals(currentUserEntity.getId())) {
        return ResponseEntity.status(400).body("Cannot deactivate your own account");
    }

    targetUser.setActive(false);
    userRepository.save(targetUser);

    // Audit log
    auditLogService.log("USER_DEACTIVATED", currentUserEntity, targetUser);

    return ResponseEntity.ok("User deactivated");
}
```

---

## XXE - XML External Entity (S2755)

### ❌ Vulnerable Code

```java
@PostMapping("/xml/parse")
public ResponseEntity<String> parseXml(@RequestBody String xml) {
    // VULNERABLE: Default XML parser allows XXE
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new InputSource(new StringReader(xml)));
    return ResponseEntity.ok("Parsed");
}
```

**Attack:**
```xml
<?xml version="1.0"?>
<!DOCTYPE foo [<!ENTITY xxe SYSTEM "file:///etc/passwd">]>
<data>&xxe;</data>
```

### ✅ Fixed Code

```java
@PostMapping("/xml/parse")
public ResponseEntity<String> parseXml(@RequestBody String xml) {
    try {
        // FIXED: Disable external entities and DTDs
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Disable external DTDs
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        // Disable external entities
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        // Disable external DTDs as well
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        // Make parser namespace aware
        factory.setNamespaceAware(true);

        // Disable XInclude
        factory.setXIncludeAware(false);

        // Disable expand entity references
        factory.setExpandEntityReferences(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        return ResponseEntity.ok("Parsed safely");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Invalid XML");
    }
}
```

---

## SSRF - Server-Side Request Forgery (S5144)

### ❌ Vulnerable Code

```java
@GetMapping("/proxy/fetch")
public ResponseEntity<String> fetchUrl(@RequestParam String url) {
    // VULNERABLE: User-controlled URL can access internal services
    RestTemplate restTemplate = new RestTemplate();
    String content = restTemplate.getForObject(url, String.class);
    return ResponseEntity.ok(content);
}
```

**Attack:** `url=http://169.254.169.254/latest/meta-data/iam/security-credentials/`

### ✅ Fixed Code

```java
@GetMapping("/proxy/fetch")
public ResponseEntity<String> fetchUrl(@RequestParam String url) {
    try {
        // FIXED: Validate and whitelist allowed URLs
        URL parsedUrl = new URL(url);

        // Whitelist allowed domains
        List<String> allowedDomains = List.of("api.example.com", "cdn.example.com");
        if (!allowedDomains.contains(parsedUrl.getHost())) {
            return ResponseEntity.status(403).body("Domain not allowed");
        }

        // Block private IP ranges
        InetAddress address = InetAddress.getByName(parsedUrl.getHost());
        if (address.isSiteLocalAddress() || address.isLoopbackAddress() ||
            address.isLinkLocalAddress()) {
            return ResponseEntity.status(403).body("Private IP addresses not allowed");
        }

        // Only allow HTTPS
        if (!"https".equals(parsedUrl.getProtocol())) {
            return ResponseEntity.status(403).body("Only HTTPS allowed");
        }

        RestTemplate restTemplate = new RestTemplate();
        String content = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(content);

    } catch (MalformedURLException | UnknownHostException e) {
        return ResponseEntity.badRequest().body("Invalid URL");
    }
}
```

---

## Command Injection (S2076)

### ❌ Vulnerable Code

```java
@GetMapping("/system/ping")
public ResponseEntity<String> ping(@RequestParam String host) {
    // VULNERABLE: User input directly in system command
    String command = "ping -c 3 " + host;
    Process process = Runtime.getRuntime().exec(command);
    return ResponseEntity.ok("Ping executed");
}
```

**Attack:** `host=google.com; cat /etc/passwd`

### ✅ Fixed Code

```java
@GetMapping("/system/ping")
public ResponseEntity<String> ping(@RequestParam String host) {
    // FIXED: Validate input and use parameterized execution

    // Step 1: Validate hostname format
    if (!host.matches("^[a-zA-Z0-9.-]+$")) {
        return ResponseEntity.badRequest().body("Invalid hostname");
    }

    // Step 2: Use ProcessBuilder with separate arguments (NOT shell execution)
    ProcessBuilder processBuilder = new ProcessBuilder("ping", "-c", "3", host);

    // Step 3: Disable shell interpretation
    // ProcessBuilder does NOT invoke shell by default (good!)

    try {
        Process process = processBuilder.start();

        // Read output safely
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream())
        );
        String result = reader.lines().collect(Collectors.joining("\n"));

        process.waitFor(5, TimeUnit.SECONDS);

        return ResponseEntity.ok(result);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error executing ping");
    }
}
```

---

## Hardcoded Secrets (S6329)

### ❌ Vulnerable Code

```java
@Service
public class PaymentService {
    // VULNERABLE: Hardcoded API keys
    private static final String STRIPE_SECRET_KEY = "sk_live_abc123def456";
    private static final String PAYPAL_CLIENT_SECRET = "xyz789secret";

    public boolean processPayment(String cardNumber) {
        // Use hardcoded secrets...
    }
}
```

### ✅ Fixed Code

```java
@Service
public class PaymentService {
    // FIXED: Load from environment variables or secrets management
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${paypal.client.secret}")
    private String paypalClientSecret;

    public boolean processPayment(String cardNumber) {
        // Use externalized secrets...
    }
}
```

**application.properties:**
```properties
# Load from environment variables
stripe.secret.key=${STRIPE_SECRET_KEY}
paypal.client.secret=${PAYPAL_CLIENT_SECRET}
```

**Environment variables (set in deployment):**
```bash
export STRIPE_SECRET_KEY=sk_live_real_secret_key
export PAYPAL_CLIENT_SECRET=real_paypal_secret
```

### ✅ Best Practice: Use Secrets Management

```java
@Configuration
public class SecretsConfig {

    @Bean
    public AWSSecretsManager secretsManager() {
        return AWSSecretsManagerClientBuilder.standard()
            .withRegion("us-east-1")
            .build();
    }

    @Bean
    public String stripeSecretKey(AWSSecretsManager secretsManager) {
        GetSecretValueRequest request = new GetSecretValueRequest()
            .withSecretId("stripe/secret-key");
        GetSecretValueResult result = secretsManager.getSecretValue(request);
        return result.getSecretString();
    }
}
```

---

## XSS - Cross-Site Scripting (S5146)

### ❌ Vulnerable Code (React)

```typescript
function CommentDisplay({ comment }: Props) {
    // VULNERABLE: Renders unsanitized HTML
    return (
        <div dangerouslySetInnerHTML={{ __html: comment.htmlContent }} />
    );
}
```

**Attack:** `htmlContent: "<img src=x onerror='alert(document.cookie)'>"`

### ✅ Fixed Code

```typescript
import DOMPurify from 'dompurify';

function CommentDisplay({ comment }: Props) {
    // FIXED: Sanitize HTML before rendering
    const sanitizedContent = DOMPurify.sanitize(comment.htmlContent, {
        ALLOWED_TAGS: ['b', 'i', 'em', 'strong', 'a', 'p'],
        ALLOWED_ATTR: ['href']
    });

    return (
        <div dangerouslySetInnerHTML={{ __html: sanitizedContent }} />
    );
}
```

### ✅ Best Practice: Avoid `dangerouslySetInnerHTML`

```typescript
function CommentDisplay({ comment }: Props) {
    // BEST: Use React's built-in escaping (no dangerouslySetInnerHTML)
    return (
        <div>
            <p>{comment.content}</p>  {/* Automatically escaped by React */}
        </div>
    );
}
```

---

## General Best Practices

### 1. Input Validation

```java
// Always validate user input
public void validateEmail(String email) {
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        throw new IllegalArgumentException("Invalid email");
    }
}
```

### 2. Output Encoding

```java
// Encode output based on context (HTML, JavaScript, URL)
import org.owasp.encoder.Encode;

String userInput = request.getParameter("name");
String htmlOutput = Encode.forHtml(userInput);
String jsOutput = Encode.forJavaScript(userInput);
```

### 3. Principle of Least Privilege

```java
// Grant minimum necessary permissions
@PreAuthorize("hasRole('USER')")  // Not ADMIN for user operations
public ResponseEntity<User> updateProfile() {
    // ...
}
```

### 4. Defense in Depth

```java
// Multiple layers of security
public void processPayment(Order order, User user) {
    // Layer 1: Authentication
    if (!isAuthenticated(user)) throw new SecurityException();

    // Layer 2: Authorization
    if (!user.getId().equals(order.getUserId())) throw new SecurityException();

    // Layer 3: Input validation
    if (order.getAmount().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException();

    // Layer 4: Business rules
    if (!user.hasEnoughBalance(order.getAmount())) throw new InsufficientFundsException();

    // Process...
}
```

---

## Testing Your Fixes

### Unit Tests

```java
@Test
public void testSqlInjectionPrevented() {
    String maliciousInput = "admin'--";

    // Should NOT authenticate
    ResponseEntity<?> response = controller.loginUser(maliciousInput, "any");

    assertEquals(401, response.getStatusCodeValue());
}
```

### Integration Tests

```java
@Test
public void testCSRFProtection() {
    // Request without CSRF token should be rejected
    ResponseEntity<String> response = restTemplate.postForEntity(
        "/api/v1/users/1/delete-account",
        null,
        String.class
    );

    assertEquals(403, response.getStatusCodeValue());
}
```

---

## Additional Resources

- **OWASP Top 10:** https://owasp.org/www-project-top-ten/
- **OWASP Cheat Sheets:** https://cheatsheetseries.owasp.org/
- **SonarQube Rules:** https://rules.sonarsource.com/
- **Spring Security Docs:** https://spring.io/projects/spring-security

---

**Remember:** Security is not a one-time fix. Continuously review, test, and update your security measures.
