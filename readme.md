# Food Waste Management System

A Spring Boot application that helps reduce food waste by connecting food providers (event hosts, NGOs, restaurants, etc.) with people who can reserve and claim surplus food before it expires.

---

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Data JPA
- MySQL
- Maven
- Lombok
- Jakarta Validation

---

## Features Implemented

### User Management

- Register new users
- User roles
  - USER
  - NGO
  - EVENT_HOST

- Email uniqueness validation
- Phone number uniqueness validation
- Request validation using Jakarta Validation
- Custom exception handling
- Global exception handling

### Food Listing

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
- Host validation before creating listings

### Food Reservation (Claim)

- Reserve available food
- Verify user existence
- Verify food existence
- Check available quantity
- Prevent overbooking
- Automatically reduce available quantity
- Automatically reserve entire listing when quantity becomes zero
- Reservation confirmation endpoint
- Reservation status tracking
  - RESERVED
  - CLAIMED
  - CANCELLED

### Automatic Reservation Expiry

- Scheduler checks expired reservations every 2 minutes
- Automatically cancels expired reservations
- Restores food quantity
- Makes food available again
- Batch updates using `saveAll()`

### Validation & Exception Handling

- Jakarta Bean Validation
- Global Exception Handler
- Custom Exceptions
- Clean API error responses

---

## API Endpoints

### User APIs

#### Create User

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

### Food Listing APIs

#### Create Food Listing

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

### Food Claim APIs

#### Reserve Food

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

#### Confirm Food Claim

```http
POST /food-claims/{claimId}/confirm
```

---

## Project Structure

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

## Current Workflow

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
Food Quantity Reduced
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

## Current Status

### Version

**v0.3**

### Completed

- User Module
- Food Listing Module
- Food Reservation Module
- Reservation Confirmation
- Automatic Reservation Expiry Scheduler
- DTO Validation
- Global Exception Handling
- Custom Exceptions
- MySQL Integration

---

## Upcoming Features

- JWT Authentication & Authorization
- Role-based Access Control
- Location-based Food Search
- Nearby Food Listings
- NGO Verification
- Notifications
- Image Upload
- Claim History
- Dashboard APIs
- Docker Support
- Unit & Integration Testing

---

## Future Improvements

- Replace UUID references with JPA Relationships (`@ManyToOne`)
- Optimize Scheduler to avoid N+1 queries
- Add Database Indexes
- Batch Processing for Large Data Sets
- Event-driven Reservation Expiry
- Production Logging
- Monitoring & Metrics

---

## Learning Goals

This project is being developed as a real-world backend application while learning Spring Boot, REST APIs, JPA, transactions, validation, schedulers, exception handling, and scalable backend design principles.
