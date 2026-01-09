# Changelog - Supply Chain Attack Demonstration

**Branch:** `feature/malicious-json-package`  
**Date:** January 2025

---

## Summary

This changelog documents the introduction of intentional supply chain attack vulnerabilities via typo-squatting packages. These vulnerabilities are for educational and demonstration purposes only.

---

## Changes Made

### 1. Malicious Dependencies Added

#### 1.1 Typo-Squatting: Jackson Core
- **File:** `backend/pom.xml`
- **Malicious Package:** `org.fasterxml.jackson.core:jackson-core:2.15.2`
- **Legitimate Package:** `com.fasterxml.jackson.core:jackson-core:2.15.2`
- **Attack Type:** Typo-squatting (wrong groupId: `org` instead of `com`)

#### 1.2 Typo-Squatting: Apache Commons Codec
- **File:** `backend/pom.xml`
- **Malicious Package:** `org.apache.commons.codec:codec:1.15`
- **Legitimate Package:** `org.apache.commons:commons-codec:1.15`
- **Attack Type:** Dependency confusion (wrong groupId structure)

### 2. New Utility Classes

#### 2.1 JsonTransformer
- **File:** `backend/src/main/java/com/sonarshowcase/util/JsonTransformer.java`
- **Purpose:** JSON transformation utility using malicious Jackson package
- **Methods:**
  - `transformOrderToJson()` - Converts order data to JSON
  - `parseJsonToMap()` - Parses JSON strings
  - `enhanceOrderWithMetadata()` - Adds metadata to orders
  - `isValidJson()` - Validates JSON structure

#### 2.2 SecureCryptoUtil
- **File:** `backend/src/main/java/com/sonarshowcase/util/SecureCryptoUtil.java`
- **Purpose:** Crypto operations utility using malicious codec package
- **Methods:**
  - `encrypt()` / `decrypt()` - Encryption/decryption operations
  - `secureHash()` - Hash generation
  - `validateHash()` - Hash validation
  - `encryptPassword()` / `decryptPassword()` - Password-specific operations

### 3. Enhanced Controllers

#### 3.1 OrderController Enhancements
- **File:** `backend/src/main/java/com/sonarshowcase/controller/OrderController.java`
- **New Endpoints:**
  - `GET /api/v1/orders/{id}/json` - Returns order as JSON
  - `POST /api/v1/orders/from-json` - Creates order from JSON
  - `POST /api/v1/orders/{id}/enhanced-json` - Returns enhanced order JSON

#### 3.2 UserController Enhancements
- **File:** `backend/src/main/java/com/sonarshowcase/controller/UserController.java`
- **New Endpoints:**
  - `POST /api/v1/users/encrypt-password` - Encrypts passwords
  - `POST /api/v1/users/decrypt-password` - Decrypts passwords (CRITICAL)
  - `POST /api/v1/users/{userId}/secure-password` - Stores passwords securely
  - `POST /api/v1/users/validate-hash` - Validates password hashes

### 4. Documentation Updates

#### 4.1 New Documentation Files
- **`docs/SECURITY_SUPPLY_CHAIN_ATTACKS.md`** - Comprehensive documentation of supply chain attacks
  - Attack scenarios
  - Detection and prevention methods
  - Remediation steps
  - API endpoint documentation

#### 4.2 Updated Documentation Files
- **`README.md`**
  - Added supply chain attack section
  - Updated vulnerable endpoints table
  - Added new endpoints to API reference
  - Added reference to security documentation

- **`docs/API_REFERENCE_CARD.md`**
  - Added supply chain attack endpoints
  - Updated security warnings

- **`docs/TODO.md`**
  - Added completed tasks for supply chain attack work
  - Added remaining tasks for testing

- **`docs/DOCUMENTATION_LOCATIONS.md`**
  - Added reference to security documentation

---

## Security Impact

### High Risk Endpoints
1. **`POST /api/v1/users/decrypt-password`** - CRITICAL
   - Decrypts passwords using malicious package
   - Could log all passwords in plaintext
   - Risk: Complete account compromise

2. **`POST /api/v1/orders/from-json`** - HIGH
   - Parses user-controlled JSON with malicious package
   - Risk: Remote code execution

3. **`POST /api/v1/users/encrypt-password`** - HIGH
   - Encrypts passwords using malicious package
   - Risk: Password logging and exfiltration

### Medium Risk Endpoints
- `GET /api/v1/orders/{id}/json` - Data exfiltration risk
- `POST /api/v1/orders/{id}/enhanced-json` - Data exfiltration risk
- `POST /api/v1/users/{userId}/secure-password` - Password logging risk
- `POST /api/v1/users/validate-hash` - Weak crypto validation

---

## Testing

### Manual Testing
All new endpoints can be tested using curl commands (see `docs/SECURITY_SUPPLY_CHAIN_ATTACKS.md`).

### SonarCloud Analysis
These malicious packages should be detected by SonarCloud through:
- Dependency analysis
- Security hotspots
- Code analysis of security warnings

---

## Remediation

To remove these vulnerabilities:

1. Remove malicious dependencies from `backend/pom.xml`
2. Replace with legitimate packages:
   - `com.fasterxml.jackson.core:jackson-core:2.15.2`
   - `org.apache.commons:commons-codec:1.15`
3. Refactor or remove utility classes using malicious packages
4. Remove or secure vulnerable endpoints

See `docs/SECURITY_SUPPLY_CHAIN_ATTACKS.md` for detailed remediation steps.

---

## Files Changed

### New Files
- `backend/src/main/java/com/sonarshowcase/util/JsonTransformer.java`
- `backend/src/main/java/com/sonarshowcase/util/SecureCryptoUtil.java`
- `docs/SECURITY_SUPPLY_CHAIN_ATTACKS.md`
- `docs/CHANGELOG_SUPPLY_CHAIN.md` (this file)

### Modified Files
- `backend/pom.xml` - Added malicious dependencies
- `backend/src/main/java/com/sonarshowcase/controller/OrderController.java` - Added JSON endpoints
- `backend/src/main/java/com/sonarshowcase/controller/UserController.java` - Added crypto endpoints
- `README.md` - Updated with supply chain attack information
- `docs/API_REFERENCE_CARD.md` - Added new endpoints
- `docs/TODO.md` - Updated task list
- `docs/DOCUMENTATION_LOCATIONS.md` - Added security docs reference

---

## Next Steps

1. Test all new endpoints with running application
2. Verify SonarCloud detects malicious packages
3. Document any additional findings
4. Consider adding automated dependency scanning to CI/CD

---

*This changelog documents intentional security vulnerabilities for educational purposes. These should never be used in production environments.*
