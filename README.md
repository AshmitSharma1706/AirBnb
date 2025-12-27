A scalable Airbnb-like backend system built using Spring Boot and RESTful architecture.
This project demonstrates real-world backend engineering practices including:
  a. MVC layered architecture.
  b. Scalable RESTful API endpoints.
  c. ORM(Object-Relational Mapping) and pagination to support high-performance operations. 
  d. JWT token and refresh token security.
  e. Authentication and Authorization, RBAC(Role based access control) and Password encoding.
  f. Session management.
  g. Database design and relationship mapping.
  h. Swagger API documentation.
  i. AWS cloud deployement with Elastic Beanstock, EC2 instance and AWS RDS(Relational Database Service) with Postgres cloud database.
  j. AWS CI/CD pipline
  k. Postman for testing API endpoints.
  l. Strip payment gateway.
  m. Scalability considerations.

**Tech Stack**
  a. Language: Java
  b. Framework: Spring Boot
  c. API Style: RESTful APIs
  d. ORM: JPA / Hibernate
  e. Database: PostgreSQL
  f. Security: JWT-based Authentication
  g. API Documentation: Swagger (OpenAPI 3)
  h. Build Tool: Maven and Postman
  i. Cloud: AWS

**System Architecture Overview**
  The application follows a Layered Architecture to ensure clean separation of concerns and long-term maintainability.
  ┌──────────────────┐
  │     Client       │  (Frontend / Postman / Swagger UI)
  └────────┬─────────┘
           │ HTTP Requests
           ▼
  ┌──────────────────┐
  │   Controller     │  (REST APIs)
  └────────┬─────────┘
           │
           ▼
  ┌──────────────────┐
  │   Service        │  (Business Logic)
  └────────┬─────────┘
           │
           ▼
  ┌──────────────────┐
  │  Repository      │  (JPA / Hibernate)
  └────────┬─────────┘
           │
           ▼
  ┌──────────────────┐
  │ PostgreSQL DB    │
  └──────────────────┘

**Layer Responsibilities**

  Layer	                |             Responsibility
  ----------------------|----------------------------------------------------
  Controller	        |             Handles HTTP requests and responses
  Service	            |             Business rules, validations, workflows
  Repository	        |             Database operations using JPA
  Security	            |             JWT filters & authorization
  Swagger	            |             Interactive API documentation


**Authentication & Security**

  a. Stateless JWT token and refresh token based authentication.
  b. JWT token generated on login.
  c. Token required for secured endpoints.

**Swagger (OpenAPI) API Documentation**
  Swagger is integrated to provide interactive and self-documented APIs.

  a. Swagger UI URL
    After running the application, access: http://localhost:8080/api/v1/swagger-ui.html
                                           or
                                           http://localhost:8080/api/v1/swagger-ui/index.html
  b. OpenAPI JSON
     http://localhost:8080/api/v1/v3/api-docs

**Benefits of Swagger**

  a. Interactive API testing
  b. Clear request/response models
  c. Easy onboarding for frontend & QA teams
  d. Reduces manual documentation effort

**Clone Repository**
  git clone https://github.com/AshmitSharma1706/AirBnb.git
  cd AirBnb  

**Database Setup**
  CREATE DATABASE AirBnb;

**Configure application.properties**

  DB Config
    spring.datasource.url=jdbc:postgresql://${DB_HOST_URL:localhost}:5432/${DB_NAME:AirBnb}
    spring.datasource.username=${DB_USERNAME:postgres}
    spring.datasource.password=${DB_PASSWORD:admin}
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    server.servlet.context-path=/api/v1
    server.port=${PORT:8080}

**Docker**
  docker pull ashmitsharma317/airbnb-app:1.0.0
  docker run -p 8080:8080 ashmitsharma317/airbnb-app:1.0.0


**Setup & Run Instructions**
  
Prerequisites
  a. Java 17+
  b. Maven 3+
  c. PostgreSQL
  d. Git
  e. Docker

**Scalability Considerations**

  Stateless APIs → Horizontal scaling with load balancers
  JWT Auth → No session dependency
  Database Indexing → Faster queries
  Modular Design → Easy migration to microservices
  API Versioning → /api/v1 for backward compatibility
  Caching (Future) → Redis for hot data

**Future Enhancements**

  Redis caching
  Review & rating system
  Advanced search filters
  Kubernetes deployment

 **Author**

  Ashmit Sharma
  Backend Developer | Java | Spring Boot | AWS Cloud | Docker
  GitHub: https://github.com/AshmitSharma1706  
