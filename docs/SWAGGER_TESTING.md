# Swagger UI Testing Guide

**Date:** January 2025  
**Status:** ✅ Annotations Complete - Ready for Testing

---

## Summary

All API endpoints have been annotated with OpenAPI/Swagger annotations. The application is ready for Swagger UI testing.

### What Was Done

1. ✅ **Added OpenAPI annotations to all endpoints:**
   - UserController: All 12 endpoints annotated
   - OrderController: All 5 endpoints annotated
   - FileController: All 8 endpoints annotated
   - HealthController: Both endpoints annotated

2. ✅ **Enhanced annotations include:**
   - Operation summaries and descriptions
   - Security warnings for vulnerable endpoints
   - Parameter descriptions with examples
   - Response codes and descriptions
   - Request body schemas

---

## Testing Swagger UI

### Prerequisites

1. **Start PostgreSQL:**
   ```bash
   docker-compose up -d postgres
   ```

2. **Build the application:**
   ```bash
   mvn clean install
   ```

3. **Start the Spring Boot application:**
   ```bash
   cd backend
   mvn spring-boot:run
   # OR use the wrapper:
   ./mvnw spring-boot:run
   ```

### Access Swagger UI

Once the application is running, access Swagger UI at:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **OpenAPI YAML:** http://localhost:8080/v3/api-docs.yaml

### Verify All Endpoints

Swagger UI should display the following endpoint groups:

#### 1. **Users** (12 endpoints)
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `POST /api/v1/users` - Create new user
- `GET /api/v1/users/search` - Search users
- `DELETE /api/v1/users/{id}` - Delete user
- `GET /api/v1/users/login` - Login (VULNERABLE - SQL Injection)
- `GET /api/v1/users/vulnerable-search` - Vulnerable search (SQL Injection)
- `GET /api/v1/users/sorted` - Get sorted users (SQL Injection)
- `PUT /api/v1/users/{id}/password` - Update password (INSECURE)

#### 2. **Orders** (5 endpoints)
- `GET /api/v1/orders` - Get all orders
- `GET /api/v1/orders/{id}` - Get order by ID
- `GET /api/v1/orders/user/{userId}` - Get orders by user
- `POST /api/v1/orders` - Create new order
- `POST /api/v1/orders/{id}/discount` - Apply discount code

#### 3. **Files** (8 endpoints)
- `GET /api/v1/files/download` - Download file (VULNERABLE - Path Traversal)
- `GET /api/v1/files/read` - Read file (VULNERABLE - Path Traversal)
- `DELETE /api/v1/files/delete` - Delete file (VULNERABLE - Path Traversal)
- `POST /api/v1/files/extract` - Extract ZIP (VULNERABLE - Zip Slip)
- `GET /api/v1/files/profile` - Get user profile (VULNERABLE - Path Traversal)
- `GET /api/v1/files/logs` - Get logs (VULNERABLE - Path Traversal)
- `POST /api/v1/files/export` - Export data (VULNERABLE - Path Traversal)
- `GET /api/v1/files/template` - Get template (VULNERABLE - Path Traversal)

#### 4. **Health** (2 endpoints)
- `GET /api/v1/health` - Health check
- `GET /api/v1/info` - System information (INSECURE)

### Testing Checklist

- [ ] Swagger UI loads successfully
- [ ] All 4 tag groups are visible (Users, Orders, Files, Health)
- [ ] All 27 endpoints are listed
- [ ] Each endpoint shows:
  - [ ] Summary and description
  - [ ] Parameters with descriptions and examples
  - [ ] Response codes and descriptions
  - [ ] Security warnings (where applicable)
- [ ] "Try it out" functionality works
- [ ] Request/response schemas are displayed correctly

---

## Generating Documentation

### JavaDoc (Backend)

**Important:** You must build and install all modules first before generating JavaDoc.

**Step 1: Build and install all modules**
```bash
# From project root
mvn clean install -DskipTests
```

**Step 2: Generate JavaDoc**
```bash
cd backend
mvn javadoc:javadoc
```

**Step 3: View JavaDoc**
```bash
open backend/target/site/apidocs/index.html
```

**Alternative: Generate from root**
```bash
# From project root
mvn javadoc:javadoc -pl backend
```

> **Note:** If you get dependency resolution errors, see `docs/JAVADOC_GENERATION.md` for troubleshooting.

### TypeDoc (Frontend)

TypeDoc is already configured! Generate documentation:

```bash
cd frontend
npm run docs
```

**Watch mode** (auto-regenerate on changes):
```bash
npm run docs:serve
```

**View documentation:**
```bash
open frontend/target/site/typedoc/index.html
```

Output will be in: `frontend/target/site/typedoc/`

> **Note:** See `docs/TYPEDOC_GENERATION.md` for detailed instructions and configuration.

---

## Annotation Enhancements Added

### Security Warnings
All vulnerable endpoints are clearly marked with:
- 🔴 Security vulnerability indicators
- Detailed attack examples
- Security warnings in descriptions

### Parameter Documentation
- Parameter descriptions
- Example values
- Required/optional indicators

### Response Documentation
- HTTP status codes
- Response descriptions
- Error scenarios

### Request Body Documentation
- Schema definitions
- Required fields
- Example payloads

---

## Next Steps

1. **Test Swagger UI:** Start the application and verify all endpoints appear
2. **Generate JavaDoc:** Run `mvn javadoc:javadoc` in the backend directory
3. **Generate TypeDoc:** Run `npx typedoc` in the frontend directory
4. **Update TODO.md:** Mark testing tasks as complete

---

*Last Updated: January 2025*

