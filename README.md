# Javaughn's Capstone Project
### Grid Dynamics

### About
My project is a spring boot application,
creating the backend of a doctor application

* [User Stories](./Capstone%20User%20Stories.pdf)
* [Api Specification](./Capstone-Api-Specification.yml)


## Stages
- Api Design - Design the api's to be implemented in the project
- Database Design - Design the database schema for the project. Using flyway and Docker Compose
- Api Implementation - Implement the basic logic for the api's
- Validation & Exception Handling - Handle the exceptions and validate the inputs
- Security
- Testing



## Microservices and Components

The project comprises the following microservices and components:

- **user-service:**
  Service dedicated to managing user-related operations.

- **appointment-service:**
  Service responsible for handling appointment scheduling and management.

- **medical-record-service:**
  Service that manages medical record information for patients.

- **expense-service:**
  Service for tracking and managing expenses within the system.

- **discovery:**
  Service discovery mechanism to facilitate communication between microservices.

- **config-server:**
  Centralized configuration server for managing configurations across microservices.

- **api-gateway:**
  API Gateway responsible for routing requests and handling cross-cutting concerns.
    - Port: 9000
