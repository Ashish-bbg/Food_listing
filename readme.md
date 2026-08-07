# Food Waste Management System

A Spring Boot application that helps reduce food waste by connecting food providers (event hosts, NGOs, restaurants, etc.) with people who can reserve and claim surplus food before it expires.

---

# Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Maven
- Lombok
- Jakarta Validation

---

# Features Implemented

## User Management

- Register new users
- User roles
  - USER
  - NGO
  - EVENT_HOST

- Email uniqueness validation
- Phone number uniqueness validation
- Request validation using Jakarta Bean Validation
- Custom exception handling
- Global exception handling

---

## Food Listing

- Create food listings
- Food type support
  - VEG
  - NON_VEG

- Food availability status
  - AVAILABLE
  - RESERVED
  - CLAIMED
  - EXPIRED

- Expiry time management
- Automatically associates the authenticated user as the food listing owner

---

## Food Reservation (Claim)

- Reserve available food
- Verify user existence
- Verify food existence
- Validate requested quantity
- Prevent overbooking
- Automatically reduce available quantity
- Automatically reserve the entire listing when quantity becomes zero
- Reservation confirmation endpoint
- Reservation status tracking
  - RESERVED
  - CLAIMED
  - CANCELLED
- Claim ownership authorization
- Expiration validation
- Cancellation + quantity restoration

---

## Automatic Reservation Expiry

- Scheduler checks expired reservations every 2 minutes
- Automatically cancels expired reservations
- Restores reserved quantity
- Makes food available again
- Batch updates using `saveAll()`

---

## Concurrency Handling (Race Condition Prevention)

While implementing the reservation module, a race condition was intentionally reproduced where two users could reserve the same food simultaneously.

Three different concurrency control strategies were implemented and tested:

- Pessimistic Locking
- Optimistic Locking
- Atomic SQL Update

The final production implementation uses **Atomic SQL Update**, where availability validation, quantity deduction, and status update are performed as a single database operation, preventing concurrent overbooking while providing better scalability for inventory-style workloads.

---

## Validation & Exception Handling

- Jakarta Bean Validation
- Global Exception Handler
- Custom Exceptions
- Clean API error responses

## Authentication & Authorization

- User Registration
- User Login
- Stateless JWT Authentication
- Custom UserDetails implementation
- Password Encryption using BCryptPasswordEncoder
- Custom JWT Authentication Filter
- AuthenticationManager & DaoAuthenticationProvider
- SecurityContext-based user identification
- Ownership validation using authenticated user
- AuthenticationEntryPoint for Unauthorized Requests (401)
- Custom AccessDeniedHandler for Forbidden Requests (403)
- Token Validation
- Invalid Token Handling
- Expired Token Handling

## Role-Based Access Control

Role-based authorization using Spring Security.

Example:

- Only authenticated users can access protected APIs.
- Food listing owners can update or delete only their own listings.
- Unauthorized access returns proper HTTP status codes.

---

# API Endpoints

## Authentication

### Register

POST /auth/register

### Login

POST /auth/login

### Food Listing

#### Create

POST /food-listings

#### Get All

GET /food-listings

#### Get By ID

GET /food-listings/{id}

#### Update

PUT /food-listings/{id}

#### Delete

DELETE /food-listings/{id}

### Food claim

GET /food-claims/my

GET /food-claims/{id}

PATCH /food-claims/{id}/cancel

---

## Authentication Request Examples

### Create User / Register User

```http
POST /auth/register
```

Request

```json
{
  "name": "username",
  "email": "username@gmail.com",
  "password": "Test@123",
  "role": "USER",
  "phone": "9459578392"
}
```

Supported values:

- USER
- NGO
- EVENT_HOST

### Login User

```http
POST /auth/login
```

Request

```json
{
  "email": "username@gmail.com",
  "password": "Test@123"
}
```

## Food Listing APIs

### Create Food Listing

```http
POST /food-listings
```

Request

```json
{
  "foodName": "Noodles",
  "foodType": "VEG",
  "quantity": 10,
  "cost": 25,
  "city": "Hyderabad",
  "latitude": 17.385,
  "longitude": 78.486,
  "expiryTime": "2026-08-10T22:00:00"
}
```

### Get All Food Listings

```http
GET /food-listings
```

### Get Food Listing By ID

```http
GET /food-listings/{id}
```

Example: `(d8caaaf5-c398-41bb-a532-fdbc59198906)`

### Delete Food Listing

```http
DELETE /food-listings/{id}
```

Example: `(d8caaaf5-c398-41bb-a532-fdbc59198906)`

### Update Food Listing

```http
PUT /food-listings/{id}
```

Example: `(d8caaaf5-c398-41bb-a532-fdbc59198906)`

Request

```json
{
  "foodName": "Chai pani",
  "foodType": "VEG",
  "quantity": 100,
  "cost": 5,
  "city": "Hyderabad",
  "latitude": 17.385,
  "longitude": 78.486,
  "expiryTime": "2026-08-10T22:00:00"
}
```

## Food Claim APIs

### Claim Food

```http
POST /food-claims
```

Request

```json
{
  "foodId": "2447b307-b473-46ba-9496-efbf28a5f98a",
  "quantity": 10
}
```

### Confirm Food Claim

```http
POST /food-claims/eb192331-d0ce-49cf-8475-7f9a7489ba50/confirm
```

Request

```json
{}
```

### Get My Food Claim

```http
GET /food-claims/my
```

Request

```json
{}
```

```http
GET /food-claims/{id}
```

Request

```json
{}
```

```http
PATCH /food-claims/{id}/cancel
```

Request

```json
{}
```

### Update Food Listing

Listing owners can update only their own food listings.

### Delete Food Listing

Listing owners can delete only their own food listings.

### Confirm Food Claim

```http
POST /food-claims/{claimId}/confirm
```

## Common HTTP Responses

| Code | Meaning                              |
| ---- | ------------------------------------ |
| 200  | Success                              |
| 201  | Created                              |
| 400  | Validation Failed                    |
| 401  | Unauthorized (Missing/Invalid JWT)   |
| 403  | Forbidden (Insufficient Permissions) |
| 404  | Resource Not Found                   |

---

# Project Structure

```text
src/main/java/com/food
├── controller
├── configuration
├── dto
├── ├── request
├── └── response
├── entity
├── enums
├── exception
├── repository
├── security
├── └── exception
├── └── JwtAuthenticationFilter
├── └── JwtService
├── └── CustomUserDetails
├── scheduler
├── service
```

---

## Application Flow

### Authentication Flow

Every protected request first passes through Spring Security's filter chain where the JWT is validated before reaching the controller.

```text
          Client
              │
              ▼
          JWT Token
              │
              ▼
          Security Filter Chain
              │
              ▼
          JWT Authentication Filter
              │
              ▼
          Validate JWT
              │
              ▼
          Load User Details
              │
              ▼
          SecurityContext
              │
              ▼
          Controller
```

### Food Listing Flow

```text
      Authenticated User
              │
              ▼
      Create Food Listing
              │
              ▼
      Food Saved
              │
              ▼
      Get All Listings
              │
              ▼
      Update Own Listing
              │
              ▼
      Delete Own Listing
```

# Current Workflow

```text
User Registration
        │
        ▼
Food Listing Created
        │
        ▼
User Reserves Food
        │
        ▼
Atomic SQL Reservation
(Check Availability + Update Quantity + Update Status)
        │
        ▼
Food Reserved
        │
        ├──────────────► User Confirms Claim
        │                     │
        │                     ▼
        │               Status = CLAIMED
        │
        └──────────────► Reservation Expires
                              │
                              ▼
                     Scheduler Cancels Reservation
                              │
                              ▼
                    Food Quantity Restored
```

---

# Repository Branches

This repository includes separate branches demonstrating different concurrency control strategies:

| Branch                | Description                                       |
| --------------------- | ------------------------------------------------- |
| `main`                | Production implementation using Atomic SQL Update |
| `atomic-sql-update`   | Atomic SQL based reservation                      |
| `optimistic-locking`  | Optimistic locking using `@Version`               |
| `pessimistic-locking` | Database row locking using `PESSIMISTIC_WRITE`    |

Each implementation was tested by reproducing concurrent reservation requests to compare how each strategy handles race conditions.

---

# Technical Notes

## Atomic SQL Reservation

Food reservation is implemented using a single JPQL update statement that:

- Validates food availability
- Checks requested quantity
- Updates reservation status
- Deducts available quantity

Because these operations are executed as a single database update, race conditions caused by concurrent requests are prevented.

## JPQL Update Ordering

While implementing the Atomic SQL approach, an issue was observed where JPQL expressions inside the same `SET` clause do not reliably evaluate previously updated column values.

To ensure the correct listing status is calculated, the query updates the status expression before deducting the quantity while both expressions reference the original database values.

The reservation process is executed inside a transaction. If any operation fails, Spring automatically rolls back the transaction, ensuring database consistency.

---

# Current Status

## Version

**v0.5**

## Completed

- User Registration
- User Login
- JWT Authentication
- Role-Based Authorization
- Food Listing CRUD
- Ownership Validation
- Custom Authentication Handlers
- Food Reservation Module
- Reservation Confirmation
- Automatic Reservation Expiry Scheduler
- Race Condition Handling
- Atomic SQL Reservation
- DTO Validation
- Global Exception Handling
- Custom Exceptions
- MySQL Integration

---

# Upcoming Features

- Refresh Token Support
- User Profile APIs
- Pagination & Sorting
- Search & Filtering
- Nearby Food Listings (Location-Based Search)
- NGO Verification Workflow
- Email Notifications
- Image Upload (Cloudinary/S3)
- Admin Dashboard APIs
- Docker & Docker Compose
- Unit Testing (JUnit + Mockito)
- Integration Testing
- Swagger/OpenAPI Documentation

---

# Future Improvements

- Replace UUID references with JPA Relationships (`@ManyToOne`)
- Optimize Scheduler to avoid N+1 queries
- Add Database Indexes
- Batch Processing for Large Data Sets
- Event-driven Reservation Expiry
- Production Logging
- Monitoring & Metrics

---

# Learning Goals

This project is being built to gain hands-on experience with:

- Spring Boot
- Spring Security
- JWT Authentication
- REST API Design
- Spring Data JPA
- MySQL
- Exception Handling
- Clean Architecture
- Backend Best Practices
- Scalable Application Design
