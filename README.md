# SmartBookingPlatform

SmartBookingPlatform is a **microservices-based booking platform** built
using **Spring Boot and Spring Cloud**. The system demonstrates a modern
backend architecture with **service discovery, API gateway routing, JWT
authentication, and load testing**.

The project was designed as a learning and architecture exercise to
explore distributed systems concepts commonly used in production
environments.

------------------------------------------------------------------------

# Architecture Overview

The platform follows a **microservices architecture**, where each
service is responsible for a specific business capability.

Services included:

  -----------------------------------------------------------------------
  Service                             Responsibility
  ----------------------------------- -----------------------------------
  API Gateway                         Single entry point for clients,
                                      routing and authentication

  Auth Service                        User registration and JWT
                                      authentication

  Catalog Service                     Management of service offers and
                                      resources

  Booking Service                     Booking creation and availability

  Discovery Service                   Service registry using Netflix
                                      Eureka
  -----------------------------------------------------------------------

All services communicate via **REST APIs**.

------------------------------------------------------------------------

# System Architecture

```
Client 
|
v API Gateway (Spring Cloud Gateway) 
|
|------------------------------------------|
|                     |                    | 
Auth Service     Catalog Service     Booking Service 
\                                         /
 \                                       /
  --------Eureka Discovery Server-------
```

The **API Gateway** acts as the entry point, forwarding requests to the
appropriate services while enforcing authentication and routing.

All services register with the **Eureka Discovery Server**.
Service locations are resolved dynamically via service discovery.

------------------------------------------------------------------------

# Technologies Used

## Backend

-   Java 21
-   Spring Boot
-   Spring Web
-   Spring Data JPA
-   Spring Security

## Microservices Infrastructure

-   Spring Cloud Gateway
-   Netflix Eureka (Service Discovery)
-   OpenFeign

## Security

-   JWT Authentication
-   Spring Security

## Data

-   PostgreSQL
-   Hibernate / JPA

## Messaging

-   RabbitMQ (used for log/event streaming)

## Testing

-   JUnit
-   WebTestClient
-   Mockito
-   k6 (load testing)

## Documentation

-   Swagger / OpenAPI

------------------------------------------------------------------------

# API Gateway Routing

  Route                 Service
  --------------------- -----------------
  /api/auth/\*\*        Auth Service
  /api/offers/\*\*      Catalog Service
  /api/resources/\*\*   Catalog Service
  /api/bookings/\*\*    Booking Service

All protected routes require a **JWT token**, issued by the **Auth
Service**.

------------------------------------------------------------------------

# Authentication Flow

1.  Client registers using `/api/auth/register`
2.  Auth Service validates credentials and returns a JWT token
3.  Client sends requests with:

Authorization: Bearer `<JWT_TOKEN>`{=html}

4.  API Gateway validates the token and routes the request.

------------------------------------------------------------------------

# API Documentation

Swagger UI is available for each service:

  Service           URL
  ----------------- ---------------------------------------
  Auth Service      http://localhost:8081/swagger-ui.html
  Catalog Service   http://localhost:8082/swagger-ui.html
  Booking Service   http://localhost:8083/swagger-ui.html

------------------------------------------------------------------------

# Running the Platform

Start the platform:

./start-platform.sh

This script will: 1. Start infrastructure using Docker 2. Launch all
microservices 3. Wait for services to become available

Gateway URL:

http://localhost:8080

------------------------------------------------------------------------

Stop the platform:

./stop-platform.sh

This terminates all running services and containers.

------------------------------------------------------------------------

# Load Testing

Load testing was performed using **k6**.

Test scenarios:

Authentication Baseline Test\
Simulates concurrent user registration and login.

Catalog Load Test\
Simulates multiple users querying service offers.

Booking Flow Test\
Simulates authentication, offer retrieval, and booking creation.

Example command:

k6 run k6-tests/catalog-load-test.js

------------------------------------------------------------------------

# Project Structure

SmartBookingPlatform_V1

api-gateway/ auth-service/ catalog-service/ booking-service/
discovery-service/

k6-tests/

start-platform.sh stop-platform.sh docker-compose.yaml

Each service follows a layered architecture:

controller\
service\
repository\
model\
dto

------------------------------------------------------------------------

# Design Principles

-   Service autonomy
-   Loose coupling via APIs
-   Stateless authentication with JWT
-   Centralized entry through API Gateway
-   Independent scalability of services

------------------------------------------------------------------------

# Known Limitations (V1)

This version focuses on architecture fundamentals.

Limitations include:

-   No Kubernetes orchestration
-   No centralized logging
-   No distributed tracing
-   No CI/CD pipeline
-   Limited resilience patterns

------------------------------------------------------------------------

# Future Improvements (V2)

Planned improvements:

-   Docker images for all services
-   Kubernetes deployment
-   Observability stack (Prometheus + Grafana)
-   Distributed tracing
-   CI/CD pipeline
-   Resilience patterns (retry, circuit breaker)

------------------------------------------------------------------------

# Purpose of the Project

This project explores real-world backend architecture concepts:

-   Microservices architecture
-   API Gateway design
-   Authentication and security
-   Service discovery
-   Load testing and performance evaluation

The goal is to understand the **trade-offs and operational concerns of
distributed systems**.
