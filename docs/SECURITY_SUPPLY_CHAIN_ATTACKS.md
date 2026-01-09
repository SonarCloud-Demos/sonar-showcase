# Supply Chain Attacks - Real-World Malicious Package Documentation

**Last Updated:** January 2025  
**Branch:** `feature/malicious-json-package`  
**Purpose:** Documentation of REAL supply chain attack via typosquatting package discovered in Maven Central

---

## Overview

This document describes a **REAL supply chain attack** that was discovered in Maven Central in December 2024. The malicious package `org.fasterxml.jackson.core:jackson-databind` was a typosquatting attack that impersonated the legitimate Jackson JSON library.

> ⚠️ **WARNING:** This demonstrates a REAL malicious package that was discovered in production. This is for educational and demonstration purposes only.

> 📰 **REAL INCIDENT:** https://www.esecurityplanet.com/threats/malicious-jackson-lookalike-library-slips-into-maven-central/

---

## Real-World Malicious Package

### 1. REAL Typosquatting Attack: Jackson Databind (`org.fasterxml.jackson.core:jackson-databind`)

**Location:** `backend/pom.xml` (lines 74-82)

**Malicious Dependency (REAL ATTACK):**
```xml
<dependency>
    <groupId>org.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

**Legitimate Package:**
- **GroupId:** `com.fasterxml.jackson.core` (from fasterxml.com)
- **ArtifactId:** `jackson-databind`
- **Version:** `2.15.2` (or latest)

**Attack Vector:**
- **Typosquatting**: Uses `org` instead of `com` in groupId
- **Namespace Impersonation**: Single namespace element difference
- **Lookalike Domain**: Registered `fasterxml.org` (vs legitimate `fasterxml.com`)
- Developers may accidentally use wrong groupId when copying dependencies

**What This REAL Malicious Package Does:**
1. **Automatic Execution**: Executes automatically in Spring Boot environments
2. **C2 Communication**: Contacts command-and-control (C2) server to download payloads
3. **Cobalt Strike Beacons**: Downloads Cobalt Strike beacons (used by ransomware groups)
4. **Obfuscation**: Uses heavily scrambled code and prompt injection to evade detection
5. **Credential Theft**: Performs credential theft and lateral movement
6. **Persistence**: Establishes persistent access to compromised systems

**Real-World Impact:**
- Discovered in Maven Central (December 2024)
- Targeted Spring Boot applications specifically
- Used by ransomware groups and APT actors
- Demonstrates sophistication of modern supply chain attacks

**Integration Points:**
- `JsonTransformer.java` - Utility class using the REAL malicious package
- `OrderController.java` - Three new endpoints using JSON transformation:
  - `GET /api/v1/orders/{id}/json` - Returns order as JSON
  - `POST /api/v1/orders/from-json` - Creates order from JSON
  - `POST /api/v1/orders/{id}/enhanced-json` - Returns enhanced order JSON

**Files Affected:**
- `backend/pom.xml` - Dependency declaration
- `backend/src/main/java/com/sonarshowcase/util/JsonTransformer.java` - New utility class
- `backend/src/main/java/com/sonarshowcase/controller/OrderController.java` - Enhanced with JSON endpoints

**Note:** This malicious package may have been removed from Maven Central after discovery. If the build fails, this demonstrates why dependency verification and monitoring are critical.

---

### 2. Outdated Version: Apache Commons Codec (`commons-codec:1.13`)

**Location:** `backend/pom.xml` (lines 84-94)

**Outdated Dependency:**
```xml
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.13</version>
</dependency>
```

**Current Secure Version:**
- **GroupId:** `commons-codec`
- **ArtifactId:** `commons-codec`
- **Version:** `1.15+` (latest: 1.20.0)

**Attack Vector:**
- Using outdated dependency version
- Developers may not update dependencies regularly
- Older versions may have security issues or use weak algorithms
- Supply chain risk from not maintaining dependency hygiene

**Potential Risks:**
- Weak cryptography: Older versions may use deprecated/weak algorithms (e.g., MD5)
- Missing security patches: Outdated versions don't have latest security fixes
- Compatibility issues: May force use of insecure workarounds
- Supply chain exposure: Outdated dependencies increase attack surface

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

### 3. Dependency Confusion: Maven Compiler Plugin (`maven-compiler-plugin` npm package)

**Location:** NPM registry (JavaScript/TypeScript projects)  
**Vulnerability ID:** SNYK-JS-MAVENCOMPILERPLUGIN-3091064  
**CVSS Score:** 9.8 (Critical)  
**Reference:** https://security.snyk.io/vuln/SNYK-JS-MAVENCOMPILERPLUGIN-3091064

**Malicious Package (REAL ATTACK):**
```json
{
  "name": "maven-compiler-plugin",
  "version": "latest"
}
```

**Legitimate Package (Maven):**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
</plugin>
```

**Attack Vector:**
- **Dependency Confusion**: Malicious npm package with same name as legitimate Maven plugin
- **Cross-Ecosystem Confusion**: Targets developers working with both Maven and npm
- **Automatic Execution**: Executes malicious code when installed via npm
- **Namespace Impersonation**: Uses identical name to legitimate Maven plugin
- Developers may accidentally install npm package when they meant to use Maven plugin

**What This REAL Malicious Package Does:**
1. **Automatic Execution**: Executes malicious code on installation via npm
2. **Code Execution**: Can execute arbitrary code on the host system
3. **Data Exfiltration**: May steal sensitive information from the development environment
4. **System Compromise**: Can install backdoors or additional malware
5. **Credential Theft**: May access environment variables, API keys, and secrets
6. **Lateral Movement**: Can spread to other systems in the network

**Real-World Impact:**
- **Critical Severity**: CVSS 9.8 indicates complete system compromise potential
- **Mature Exploit**: Proof-of-concept code available, high likelihood of exploitation
- **Total Loss**: Complete loss of confidentiality, integrity, and availability
- **Arbitrary Code Execution**: Attackers can execute any code on compromised systems
- **Supply Chain Risk**: Affects entire development pipeline if installed

**Why This Attack is Dangerous:**
- **Cross-Platform Confusion**: Developers using both Maven and npm may confuse package names
- **Automatic Installation**: npm installs packages automatically, executing malicious code
- **Build Pipeline Risk**: If installed in CI/CD, affects all builds and deployments
- **Developer Machine Compromise**: Can compromise entire development environment
- **Supply Chain Propagation**: Can spread to production if not detected

**Mitigation Implemented:**
1. **Explicit Maven Plugin Configuration**: Added explicit `maven-compiler-plugin` version in `pom.xml`
2. **Repository Restrictions**: Configured Maven to only use trusted repositories (Maven Central)
3. **Checksum Verification**: Enabled checksum verification in Maven settings
4. **NPM Security Configuration**: Added `.npmrc` with security settings to prevent malicious package installation
5. **Maven Settings Security**: Created `.mvn/settings.xml` with repository restrictions
6. **Documentation**: Comprehensive documentation of the vulnerability and mitigation

**Files Affected:**
- `pom.xml` - Added explicit maven-compiler-plugin configuration
- `.npmrc` - NPM security configuration to prevent malicious package installation
- `.mvn/settings.xml` - Maven security settings with repository restrictions
- `docs/SECURITY_SUPPLY_CHAIN_ATTACKS.md` - This documentation

**Prevention Best Practices:**
1. **Always Use Explicit Versions**: Pin exact versions for all plugins and dependencies
2. **Verify Package Sources**: Always verify package names match official documentation
3. **Separate Ecosystems**: Be aware of cross-ecosystem confusion (Maven vs npm)
4. **Repository Restrictions**: Only use trusted repositories (Maven Central, npm official registry)
5. **Checksum Verification**: Enable checksum verification in build tools
6. **Dependency Scanning**: Use automated tools (Snyk, OWASP Dependency-Check) to detect malicious packages
7. **Regular Audits**: Regularly audit dependencies for suspicious packages
8. **CI/CD Security**: Scan dependencies in CI/CD pipelines before deployment

---

## Attack Scenarios

### Scenario 1: REAL Attack - Automatic Execution and C2 Communication

**REAL Attack Flow (as discovered in December 2024):**
1. Developer accidentally adds `org.fasterxml.jackson.core:jackson-databind` (typosquatting package)
2. Package is included in Spring Boot application
3. **Automatic Execution**: Malicious package executes automatically in Spring Boot environment
4. **C2 Communication**: Package contacts command-and-control server
5. **Payload Delivery**: Downloads Cobalt Strike beacons tailored to host OS
6. **Obfuscation**: Uses scrambled code and prompt injection to evade detection
7. **Credential Theft**: Performs credential theft and lateral movement
8. **Persistence**: Establishes persistent access for ransomware/APT groups

**REAL Impact:**
- Complete system compromise
- Ransomware deployment capability
- Credential theft and lateral movement
- Data exfiltration at scale
- Regulatory compliance failures (GDPR, PCI-DSS)
- Reputation damage

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

### Scenario 4: Dependency Confusion - Maven Compiler Plugin (SNYK-JS-MAVENCOMPILERPLUGIN-3091064)

**REAL Attack Flow:**
1. Developer working on project with both Maven and npm dependencies
2. Developer accidentally runs `npm install maven-compiler-plugin` (thinking it's needed)
3. **Automatic Execution**: Malicious npm package executes on installation
4. **Code Execution**: Malicious code runs with full system privileges
5. **Credential Theft**: Steals environment variables, API keys, SSH keys
6. **Backdoor Installation**: Installs persistent backdoor for long-term access
7. **CI/CD Compromise**: If installed in CI/CD, affects all builds and deployments
8. **Production Risk**: Malicious code may be deployed to production

**REAL Impact:**
- **CVSS 9.8 (Critical)**: Complete system compromise
- **Arbitrary Code Execution**: Attackers can execute any code
- **Total Loss**: Complete loss of confidentiality, integrity, and availability
- **Supply Chain Compromise**: Entire development pipeline at risk
- **Production Deployment**: Malicious code may reach production systems
- **Data Exfiltration**: Sensitive data can be stolen
- **Regulatory Compliance**: GDPR, PCI-DSS violations
- **Reputation Damage**: Loss of customer trust

**Why This is Particularly Dangerous:**
- **Cross-Ecosystem Confusion**: Easy to confuse Maven plugin with npm package
- **Automatic Execution**: npm packages execute code automatically on install
- **Developer Machine Access**: Compromises entire development environment
- **Build Pipeline Risk**: Can affect all CI/CD builds if installed there
- **Silent Execution**: May go undetected until significant damage is done

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
   - Use dependency lock files (Maven: `maven-dependency-plugin`, npm: `package-lock.json`)
   - Regularly update and review dependencies
   - **Explicit Plugin Configuration**: Always explicitly configure Maven plugins with versions

2. **Verify Package Sources:**
   - Always use official Maven Central repositories
   - Always use official npm registry (registry.npmjs.org)
   - Verify package groupId/package name matches official documentation
   - Check package maintainer identity and reputation
   - **Cross-Ecosystem Awareness**: Be aware of Maven vs npm package name confusion

3. **Implement Security Policies:**
   - Require security review for new dependencies
   - Use automated dependency scanning in CI/CD (Snyk, OWASP Dependency-Check)
   - Block packages from untrusted sources
   - **Repository Restrictions**: Configure Maven and npm to only use trusted repositories
   - **Checksum Verification**: Enable checksum verification in Maven settings

4. **Monitor and Audit:**
   - Regular security audits of dependencies
   - Monitor for known vulnerabilities (CVE database, Snyk)
   - Track and review all dependency changes
   - **Automated Scanning**: Run `npm audit` and Maven dependency checks regularly

5. **Configuration Security:**
   - Use `.npmrc` with security settings (audit, strict-ssl, registry restrictions)
   - Use Maven `settings.xml` with repository mirrors and checksum policies
   - Block external repositories by default
   - Require HTTPS for all repository access

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
   <!-- Remove this REAL malicious package from backend/pom.xml -->
   <dependency>
       <groupId>org.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
   </dependency>
   ```

2. **Use Legitimate Package:**
   ```xml
   <!-- Use legitimate Jackson (note: 'com' not 'org') -->
   <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
       <version>2.15.2</version>
   </dependency>
   ```

3. **Best Practices to Prevent Similar Attacks:**
   - **Verify GroupIds**: Always verify package groupIds match official documentation
   - **Check Domains**: Verify maintainer domains (fasterxml.com vs fasterxml.org)
   - **Dependency Scanning**: Use automated tools to detect suspicious packages
   - **Version Pinning**: Pin exact versions and verify checksums
   - **Regular Audits**: Regularly audit dependencies for suspicious packages
   - **Monitor Downloads**: Check package download counts and maintainer reputation
   - **Cross-Ecosystem Awareness**: Be aware of Maven vs npm package name confusion
   - **Explicit Plugin Configuration**: Always explicitly configure Maven plugins with versions
   - **Repository Restrictions**: Configure Maven and npm to only use trusted repositories
   - **Checksum Verification**: Enable checksum verification in Maven settings
   - **NPM Security**: Use `.npmrc` with security settings (audit, strict-ssl)
   - **Maven Security**: Use `settings.xml` with repository mirrors and checksum policies

4. **Remove or Refactor Code:**
   - Remove `JsonTransformer.java` or refactor to use legitimate package
   - Remove `SecureCryptoUtil.java` or refactor to use legitimate package
   - Remove or secure the vulnerable endpoints

---

## References

### Real-World Incident
- **[eSecurity Planet - Malicious Jackson Lookalike Library](https://www.esecurityplanet.com/threats/malicious-jackson-lookalike-library-slips-into-maven-central/)** - Original article about this REAL attack
- **[Aikido - Maven Central Jackson Typosquatting](https://www.aikido.dev/blog/maven-central-jackson-typosquatting-malware)** - Technical analysis
- **[Cybersecurity News - Maven Central Infiltration](https://cybersecuritynews.com/hackers-infiltrated-maven-central/)** - Additional coverage

### Maven Compiler Plugin Dependency Confusion
- **[Snyk - SNYK-JS-MAVENCOMPILERPLUGIN-3091064](https://security.snyk.io/vuln/SNYK-JS-MAVENCOMPILERPLUGIN-3091064)** - Official vulnerability report
- **[OWASP Dependency Confusion](https://owasp.org/www-community/vulnerabilities/Dependency_Confusion)** - General dependency confusion attacks
- **[Maven Central Repository](https://central.sonatype.com/)** - Official Maven repository
- **[NPM Official Registry](https://www.npmjs.com/)** - Official npm registry

### General Supply Chain Security
- [OWASP Dependency Confusion](https://owasp.org/www-community/vulnerabilities/Dependency_Confusion)
- [NPM Typo-Squatting](https://snyk.io/blog/typosquatting-attacks/)
- [Maven Central Repository](https://central.sonatype.com/)
- [OWASP Dependency-Check](https://owasp.org/www-project-dependency-check/)
- [Snyk - Dependency Confusion Attacks](https://snyk.io/blog/dependency-confusion-attacks/)

---

*This documentation is part of an educational demonstration of supply chain attacks. These vulnerabilities are intentional and should never be used in production environments.*
