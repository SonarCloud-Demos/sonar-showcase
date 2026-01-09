# Supply Chain Attacks - Malicious Packages Documentation

**Last Updated:** January 2025  
**Branch:** `feature/malicious-json-package`  
**Purpose:** Documentation of intentional supply chain attack demonstrations via typo-squatting packages

---

## Overview

This document describes the intentional supply chain attack vulnerabilities introduced in the `feature/malicious-json-package` branch. These vulnerabilities demonstrate how malicious packages can be introduced into a project through typo-squatting and dependency confusion attacks.

> ⚠️ **WARNING:** These are intentional security vulnerabilities for educational and demonstration purposes. They should NEVER be used in production environments.

---

## Malicious Packages Introduced

### 1. Typo-Squatting: Jackson Core (`org.fasterxml.jackson.core`)

**Location:** `backend/pom.xml` (lines 74-82)

**Malicious Dependency:**
```xml
<dependency>
    <groupId>org.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.15.2</version>
</dependency>
```

**Legitimate Package:**
- **GroupId:** `com.fasterxml.jackson.core` (from fasterxml)
- **ArtifactId:** `jackson-core`
- **Version:** `2.15.2`

**Attack Vector:**
- Typo-squatting using `org` instead of `com` in the groupId
- Developers may accidentally use the wrong groupId when copying dependencies
- The malicious package could be a drop-in replacement with identical APIs but malicious behavior

**Potential Malicious Behaviors:**
- Data exfiltration: Logs all JSON data processed to external servers
- Backdoor injection: Executes arbitrary code during JSON parsing
- Credential theft: Intercepts and logs sensitive data in JSON payloads
- Remote code execution: Executes malicious code from JSON payloads

**Integration Points:**
- `JsonTransformer.java` - Utility class using the malicious package
- `OrderController.java` - Three new endpoints using JSON transformation:
  - `GET /api/v1/orders/{id}/json` - Returns order as JSON
  - `POST /api/v1/orders/from-json` - Creates order from JSON
  - `POST /api/v1/orders/{id}/enhanced-json` - Returns enhanced order JSON

**Files Affected:**
- `backend/pom.xml` - Dependency declaration
- `backend/src/main/java/com/sonarshowcase/util/JsonTransformer.java` - New utility class
- `backend/src/main/java/com/sonarshowcase/controller/OrderController.java` - Enhanced with JSON endpoints

---

### 2. Typo-Squatting: Apache Commons Codec (`org.apache.commons.codec`)

**Location:** `backend/pom.xml` (lines 84-94)

**Malicious Dependency:**
```xml
<dependency>
    <groupId>org.apache.commons.codec</groupId>
    <artifactId>codec</artifactId>
    <version>1.15</version>
</dependency>
```

**Legitimate Package:**
- **GroupId:** `org.apache.commons`
- **ArtifactId:** `commons-codec`
- **Version:** `1.15`

**Attack Vector:**
- Dependency confusion using wrong groupId structure
- The legitimate package uses `org.apache.commons:commons-codec` (group:artifact format)
- The malicious package uses `org.apache.commons.codec:codec` to confuse developers
- Similar naming pattern makes it easy to mistake for the legitimate package

**Potential Malicious Behaviors:**
- Password logging: Logs all passwords in plaintext before encryption
- Key exfiltration: Sends encryption keys to external servers
- Weak crypto: Uses intentionally weak encryption algorithms
- Credential theft: Intercepts and logs all decrypted passwords
- Backdoor in crypto: Creates vulnerabilities in encryption/decryption operations

**Integration Points:**
- `SecureCryptoUtil.java` - Utility class using the malicious crypto package
- `UserController.java` - Four new endpoints using crypto operations:
  - `POST /api/v1/users/encrypt-password` - Encrypts passwords
  - `POST /api/v1/users/decrypt-password` - Decrypts passwords (CRITICAL)
  - `POST /api/v1/users/{userId}/secure-password` - Stores passwords securely
  - `POST /api/v1/users/validate-hash` - Validates password hashes

**Files Affected:**
- `backend/pom.xml` - Dependency declaration
- `backend/src/main/java/com/sonarshowcase/util/SecureCryptoUtil.java` - New utility class
- `backend/src/main/java/com/sonarshowcase/controller/UserController.java` - Enhanced with crypto endpoints

---

## Attack Scenarios

### Scenario 1: Data Exfiltration via JSON Processing

**Attack Flow:**
1. Developer adds `org.fasterxml.jackson.core:jackson-core` (typo-squatting package)
2. Application processes order data through `JsonTransformer`
3. Malicious package intercepts all JSON data
4. Data is exfiltrated to attacker-controlled server
5. Sensitive order information (customer data, payment info) is stolen

**Impact:**
- Customer data breach
- Payment information theft
- Privacy violations
- Regulatory compliance failures (GDPR, PCI-DSS)

### Scenario 2: Password Theft via Crypto Package

**Attack Flow:**
1. Developer adds `org.apache.commons.codec:codec` (typo-squatting package)
2. Application encrypts/decrypts passwords using `SecureCryptoUtil`
3. Malicious package logs all passwords in plaintext
4. Passwords are sent to attacker-controlled server
5. Attacker gains access to all user accounts

**Impact:**
- Complete account compromise
- Identity theft
- Unauthorized access to user data
- System-wide security breach

### Scenario 3: Remote Code Execution

**Attack Flow:**
1. Malicious package is loaded into application
2. Package executes malicious code during initialization
3. Backdoor is installed in the system
4. Attacker gains persistent access
5. Additional malware can be deployed

**Impact:**
- Complete system compromise
- Data exfiltration at scale
- Lateral movement to other systems
- Long-term persistent access

---

## Detection and Prevention

### How to Detect These Attacks

1. **Dependency Scanning:**
   - Use tools like OWASP Dependency-Check, Snyk, or Sonatype Nexus
   - Verify package checksums and signatures
   - Check package download counts and maintainer reputation

2. **Code Review:**
   - Verify groupId and artifactId match official sources
   - Check package documentation and official websites
   - Review package source code if available

3. **Runtime Monitoring:**
   - Monitor outbound network connections
   - Log all data processing operations
   - Alert on unexpected external communications

4. **Static Analysis:**
   - SonarQube/SonarCloud can detect suspicious dependencies
   - Check for packages with low download counts
   - Verify package maintainer identity

### Prevention Best Practices

1. **Use Dependency Management:**
   - Pin exact versions in dependency management
   - Use dependency lock files (Maven: `maven-dependency-plugin`)
   - Regularly update and review dependencies

2. **Verify Package Sources:**
   - Always use official Maven Central repositories
   - Verify package groupId matches official documentation
   - Check package maintainer identity and reputation

3. **Implement Security Policies:**
   - Require security review for new dependencies
   - Use automated dependency scanning in CI/CD
   - Block packages from untrusted sources

4. **Monitor and Audit:**
   - Regular security audits of dependencies
   - Monitor for known vulnerabilities (CVE database)
   - Track and review all dependency changes

---

## API Endpoints Added

### JSON Processing Endpoints (Using Malicious Jackson Package)

| Method | Endpoint | Description | Security Risk |
|--------|----------|-------------|---------------|
| GET | `/api/v1/orders/{id}/json` | Returns order as JSON | Data exfiltration |
| POST | `/api/v1/orders/from-json` | Creates order from JSON | RCE via malicious payloads |
| POST | `/api/v1/orders/{id}/enhanced-json` | Returns enhanced order JSON | Data exfiltration |

### Crypto Endpoints (Using Malicious Codec Package)

| Method | Endpoint | Description | Security Risk |
|--------|----------|-------------|---------------|
| POST | `/api/v1/users/encrypt-password` | Encrypts password | Password logging |
| POST | `/api/v1/users/decrypt-password` | Decrypts password | Password theft (CRITICAL) |
| POST | `/api/v1/users/{userId}/secure-password` | Stores password securely | Password logging |
| POST | `/api/v1/users/validate-hash` | Validates password hash | Weak crypto validation |

---

## Code Locations

### New Files Created

1. **`backend/src/main/java/com/sonarshowcase/util/JsonTransformer.java`**
   - Utility class for JSON transformation
   - Uses malicious `org.fasterxml.jackson.core` package
   - Methods: `transformOrderToJson()`, `parseJsonToMap()`, `enhanceOrderWithMetadata()`

2. **`backend/src/main/java/com/sonarshowcase/util/SecureCryptoUtil.java`**
   - Utility class for crypto operations
   - Uses malicious `org.apache.commons.codec` package
   - Methods: `encrypt()`, `decrypt()`, `secureHash()`, `encryptPassword()`, `decryptPassword()`

### Modified Files

1. **`backend/pom.xml`**
   - Added two malicious dependencies (lines 74-94)
   - Includes security warnings in comments

2. **`backend/src/main/java/com/sonarshowcase/controller/OrderController.java`**
   - Added three new JSON processing endpoints
   - Integrated `JsonTransformer` utility

3. **`backend/src/main/java/com/sonarshowcase/controller/UserController.java`**
   - Added four new crypto endpoints
   - Integrated `SecureCryptoUtil` utility

---

## Testing the Vulnerabilities

### Testing JSON Processing Endpoints

```bash
# Get order as JSON (data exfiltration risk)
curl http://localhost:8080/api/v1/orders/1/json

# Create order from JSON (RCE risk)
curl -X POST http://localhost:8080/api/v1/orders/from-json \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"totalAmount":99.99}'

# Get enhanced order JSON
curl -X POST http://localhost:8080/api/v1/orders/1/enhanced-json
```

### Testing Crypto Endpoints

```bash
# Encrypt password (password logging risk)
curl -X POST "http://localhost:8080/api/v1/users/encrypt-password?password=mySecret123"

# Decrypt password (CRITICAL - password theft)
curl -X POST "http://localhost:8080/api/v1/users/decrypt-password?encryptedPassword=<encrypted>"

# Store secure password
curl -X POST "http://localhost:8080/api/v1/users/1/secure-password?password=mySecurePass123"

# Validate hash
curl -X POST "http://localhost:8080/api/v1/users/validate-hash?password=test&hash=<hash>"
```

---

## SonarCloud Detection

These malicious packages should be detected by SonarCloud through:

1. **Dependency Analysis:**
   - Suspicious package names
   - Low download counts
   - Unverified maintainers

2. **Security Hotspots:**
   - Use of unverified dependencies
   - Missing package verification

3. **Code Analysis:**
   - Security warnings in code comments
   - Documentation of security risks

---

## Remediation

To fix these vulnerabilities:

1. **Remove Malicious Dependencies:**
   ```xml
   <!-- Remove these from backend/pom.xml -->
   <dependency>
       <groupId>org.fasterxml.jackson.core</groupId>
       <artifactId>jackson-core</artifactId>
   </dependency>
   
   <dependency>
       <groupId>org.apache.commons.codec</groupId>
       <artifactId>codec</artifactId>
   </dependency>
   ```

2. **Use Legitimate Packages:**
   ```xml
   <!-- Use legitimate Jackson -->
   <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-core</artifactId>
       <version>2.15.2</version>
   </dependency>
   
   <!-- Use legitimate Commons Codec -->
   <dependency>
       <groupId>org.apache.commons</groupId>
       <artifactId>commons-codec</artifactId>
       <version>1.15</version>
   </dependency>
   ```

3. **Remove or Refactor Code:**
   - Remove `JsonTransformer.java` or refactor to use legitimate package
   - Remove `SecureCryptoUtil.java` or refactor to use legitimate package
   - Remove or secure the vulnerable endpoints

---

## References

- [OWASP Dependency Confusion](https://owasp.org/www-community/vulnerabilities/Dependency_Confusion)
- [NPM Typo-Squatting](https://snyk.io/blog/typosquatting-attacks/)
- [Maven Central Repository](https://central.sonatype.com/)
- [OWASP Dependency-Check](https://owasp.org/www-project-dependency-check/)

---

*This documentation is part of an educational demonstration of supply chain attacks. These vulnerabilities are intentional and should never be used in production environments.*
