# Food Waste Management System

A Spring Boot application that helps reduce food waste by connecting food providers (NGOs, event hosts, restaurants, etc.) with people who can claim surplus food.

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Data JPA
- MySQL
- Maven
- Lombok
- Jakarta Validation

## Features Implemented

### User Registration Module

- Create new users
- User roles:
  - USER
  - NGO
  - EVENT_HOST

- Email uniqueness validation
- Phone number uniqueness validation
- Request validation using Jakarta Validation
- Custom exception handling
- Global exception handling
- MySQL database integration

### API Endpoints

#### Create User

POST `/users`

Request:

```json
{
  "name": "Ashish",
  "email": "ashish@gmail.com",
  "phone": "9876543210",
  "role": "NGO"
}
```

Response:

```text
User created Successfully
```

## Project Structure

```text
src/main/java/com/food
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── service
```

## Upcoming Features

- Food Listing Module
- Food Claim Module
- Location-based Search
- Food Availability Alerts
- NGO Verification
- Authentication & Authorization
- Notification Service

## Current Status

Version: v0.1

Completed:

- User Entity
- User Repository
- User Service
- User Controller
- DTO Validation
- Custom Exceptions
- Global Exception Handler

Work in Progress:

- Food Listing Module

```

```
