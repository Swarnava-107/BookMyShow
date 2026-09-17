# 🎬 BookMyShow – Movie Ticket Booking Backend

A **full-fledged backend application for a movie ticket booking platform**, developed using **Java and Spring Boot**. The project provides RESTful APIs for managing movies, theatres, shows, seats, users, and ticket bookings.

The application follows a **layered backend architecture** and uses **Spring Data JPA and Hibernate** for persistence with **MySQL** as the relational database.

---

## 🚀 Project Overview

The goal of this project is to build a backend system that handles the core workflow of an online movie ticket booking platform.

The application manages the complete relationship between:

**Users → Movies → Theatres → Shows → Seats → Bookings**

Users can discover available shows, view seat availability, select seats, and book movie tickets through REST APIs.

---

## ✨ Key Features

* 🎥 **Movie Management**

  * Add and retrieve movie information.
  * Maintain movie-related data for scheduled shows.

* 🏢 **Theatre Management**

  * Manage theatre information.
  * Configure theatre seats and seating information.

* 🕐 **Show Management**

  * Create and manage movie shows.
  * Associate movies with theatres and show timings.
  * Search available shows.

* 💺 **Seat Management**

  * Maintain theatre seats.
  * Track seat availability for individual shows.
  * Manage show-specific seat information.

* 🎟️ **Ticket Booking**

  * Select available seats for a particular show.
  * Book movie tickets for registered users.
  * Maintain booking and ticket information.

* 👤 **User Management**

  * Create and retrieve user information.
  * Associate users with their bookings.

* 🗄️ **Database Persistence**

  * MySQL-based relational database.
  * Hibernate/JPA entity mapping.
  * Spring Data JPA repositories for database operations.

* ⚠️ **Exception Handling**

  * Centralized handling of application-level exceptions.
  * Meaningful responses for invalid operations and booking failures.

---

## 🏗️ Architecture

The project follows a **Controller → Service → Repository** layered architecture.

```text
                    Client
                      │
                      ▼
              ┌───────────────┐
              │   Controller  │
              │  REST APIs    │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │    Service    │
              │ Business Logic│
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │  Repository   │
              │ Spring Data   │
              │     JPA       │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │    MySQL      │
              │   Database    │
              └───────────────┘
```

This separation keeps **API handling, business logic, and database operations independent**, making the application easier to maintain and extend.

---

## 🛠️ Technology Stack

| Technology          | Purpose                                    |
| ------------------- | ------------------------------------------ |
| **Java**            | Core programming language                  |
| **Spring Boot**     | Backend application framework              |
| **Spring MVC**      | REST API development                       |
| **Spring Data JPA** | Database access and repository abstraction |
| **Hibernate**       | ORM and entity management                  |
| **MySQL**           | Relational database                        |
| **Maven**           | Dependency and build management            |

---

## 🗃️ Core Domain Model

The backend is built around several interconnected entities:

```text
User
 │
 └──────────► Booking

Movie
 │
 └──────────► Show
                 │
                 ├──────────► Theatre
                 │
                 └──────────► Show Seats
                                  │
                                  └──► Seat

Booking
 │
 └──────────► Show / Seats / User
```

The entity relationships are mapped using **JPA/Hibernate annotations**, allowing the Java domain model to be persisted directly into the MySQL database.

---

## 🔄 Booking Workflow

The core booking flow is:

```text
1. User
   ↓
2. Select Movie
   ↓
3. Select Theatre / Show
   ↓
4. Check Available Seats
   ↓
5. Select Seats
   ↓
6. Create Booking
   ↓
7. Persist Booking & Seat Information
```

This workflow demonstrates the implementation of real-world backend business logic rather than simple CRUD operations.

---

## 🔌 REST API Modules

The application exposes REST APIs organized around different resources.

### User APIs

Responsible for user-related operations.

### Movie APIs

Responsible for creating and retrieving movie information.

### Theatre APIs

Responsible for theatre-related operations.

### Show APIs

Responsible for creating and searching movie shows.

### Booking APIs

Responsible for handling ticket booking and seat selection.

---

## ⚙️ Getting Started

### Prerequisites

Make sure the following are installed:

* Java JDK
* Maven
* MySQL
* Git

### 1. Clone the repository

```bash
git clone https://github.com/Swarnava-107/BookMyShow.git
cd BookMyShow
```

### 2. Configure MySQL

Create a MySQL database for the application.

```sql
CREATE DATABASE bookmyshow_db;
```

Update your database configuration in:

```text
src/main/resources/application.properties
```

Configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookmyshow_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The backend will start on the configured Spring Boot port.

---

## 🧪 Testing APIs

The REST APIs can be tested using tools such as:

* Postman
* Swagger
* IntelliJ IDEA HTTP Client
* cURL

Example workflow:

```text
Create User
    ↓
Create Movie
    ↓
Create Theatre
    ↓
Configure Seats
    ↓
Create Show
    ↓
Check Show Availability
    ↓
Select Seats
    ↓
Book Ticket
```

---

## 📌 What This Project Demonstrates

This project demonstrates practical experience with:

* RESTful API development
* Spring Boot application development
* Layered architecture
* Object-Relational Mapping
* Hibernate/JPA entity relationships
* Spring Data JPA repositories
* MySQL database design
* Backend business logic
* Entity relationship management
* Movie/show/seat booking workflows
* Exception handling
* Maven-based project management

---

## 🔮 Future Enhancements

Potential improvements for the system include:

* Spring Security and JWT-based authentication
* Role-based authorization
* Payment gateway integration
* Redis-based seat locking
* Concurrent booking protection
* Multiple screens per theatre
* Booking cancellation and refund workflows
* Email/SMS booking notifications
* Docker-based deployment
* API documentation with Swagger/OpenAPI

---

## 👨‍💻 Author

**Swarnava Samanta**

GitHub:
https://github.com/Swarnava-107

---

## ⭐ Project

If you find this project useful or interesting, consider giving the repository a ⭐.
