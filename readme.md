# Food Waste Management System

A Spring Boot application that helps reduce food waste by connecting food providers (event hosts, NGOs, restaurants, etc.) with people who can reserve and claim surplus food before it expires.

---

# Tech Stack

* Java 21
* Spring Boot 3.5.x
* Spring Data JPA
* MySQL
* Maven
* Lombok
* Jakarta Validation

---

# Features Implemented

## User Management

* Register new users
* User roles

  * USER
  * NGO
  * EVENT_HOST
* Email uniqueness validation
* Phone number uniqueness validation
* Request validation using Jakarta Bean Validation
* Custom exception handling
* Global exception handling

---

## Food Listing

* Create food listings
* Food type support

  * VEG
  * NON_VEG
* Food availability status

  * AVAILABLE
  * RESERVED
  * CLAIMED
  * EXPIRED
* Expiry time management
* Host validation before creating food listings

---

## Food Reservation (Claim)

* Reserve available food
* Verify user existence
* Verify food existence
* Validate requested quantity
* Prevent overbooking
* Automatically reduce available quantity
* Automatically reserve the entire listing when quantity becomes zero
* Reservation confirmation endpoint
* Reservation status tracking

  * RESERVED
  * CLAIMED
  * CANCELLED

---

## Automatic Reservation Expiry

* Scheduler checks expired reservations every 2 minutes
* Automatically cancels expired reservations
* Restores reserved quantity
* Makes food available again
* Batch updates using `saveAll()`

---

## Concurrency Handling (Race Condition Prevention)

While implementing the reservation module, a race condition was intentionally reproduced where two users could reserve the same food simultaneously.

Three different concurrency control strategies were implemented and tested:

* Pessimistic Locking
* Optimistic Locking
* Atomic SQL Update

The final production implementation uses **Atomic SQL Update**, where availability validation, quantity deduction, and status update are performed as a single database operation, preventing concurrent overbooking while providing better scalability for inventory-style workloads.

---

## Validation & Exception Handling

* Jakarta Bean Validation
* Global Exception Handler
* Custom Exceptions
* Clean API error responses

---

# API Endpoints

## User APIs

### Create User

```http
POST /users
```

Request

```json
{
  "name": "Ashish",
  "email": "ashish@gmail.com",
  "phone": "9876543210",
  "role": "NGO"
}
```

---

## Food Listing APIs

### Create Food Listing

```http
POST /food-listings
```

Request

```json
{
  "foodName": "Veg Biryani",
  "foodType": "VEG",
  "quantity": 100,
  "cost": 0,
  "city": "Hyderabad",
  "latitude": 17.385,
  "longitude": 78.486,
  "expiryTime": "2026-08-01T22:00:00",
  "hostId": "USER_UUID"
}
```

---

## Food Claim APIs

### Reserve Food

```http
POST /food-claims
```

Request

```json
{
  "foodId": "FOOD_UUID",
  "userId": "USER_UUID",
  "quantity": 20
}
```

### Confirm Food Claim

```http
POST /food-claims/{claimId}/confirm
```

---

# Project Structure

```text
src/main/java/com/food
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── scheduler
├── service
```

---

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

* Validates food availability
* Checks requested quantity
* Updates reservation status
* Deducts available quantity

Because these operations are executed as a single database update, race conditions caused by concurrent requests are prevented.

## JPQL Update Ordering

While implementing the Atomic SQL approach, an issue was observed where JPQL expressions inside the same `SET` clause do not reliably evaluate previously updated column values.

To ensure the correct listing status is calculated, the query updates the status expression before deducting the quantity while both expressions reference the original database values.

The reservation process is executed inside a transaction. If any operation fails, Spring automatically rolls back the transaction, ensuring database consistency.

---

# Current Status

## Version

**v0.4**

## Completed

* User Module
* Food Listing Module
* Food Reservation Module
* Reservation Confirmation
* Automatic Reservation Expiry Scheduler
* Race Condition Handling
* Atomic SQL Reservation
* DTO Validation
* Global Exception Handling
* Custom Exceptions
* MySQL Integration

---

# Upcoming Features

* JWT Authentication & Authorization
* Role-based Access Control
* Refresh Tokens
* Location-based Food Search
* Nearby Food Listings
* NGO Verification
* Notification Service
* Image Upload
* Claim History
* Dashboard APIs
* Docker Support
* Unit & Integration Testing

---

# Future Improvements

* Replace UUID references with JPA Relationships (`@ManyToOne`)
* Optimize Scheduler to avoid N+1 queries
* Add Database Indexes
* Batch Processing for Large Data Sets
* Event-driven Reservation Expiry
* Production Logging
* Monitoring & Metrics

---

# Learning Goals

This project is being developed as a real-world backend application while learning:

* Spring Boot
* REST API Design
* Spring Data JPA
* Transactions
* Concurrency Control
* Race Condition Prevention
* Database Locking Strategies
* Schedulers
* Validation
* Exception Handling
* Scalable Backend Design
* MySQL
* Clean Architecture
