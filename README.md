![image](https://github.com/user-attachments/assets/2f893586-6c5b-4a0d-9d9e-a4535bf608c7)

Project Overview
This project is a microservices-based application designed to manage user registration and authentication with the following key functionalities:

User Service: Handles CRUD operations for user management, storing data in a MySQL database, and communicates with the Auth Service for OTP-based registration.
Auth Service: Manages user registration by sending OTPs via email and verifying them to activate accounts.
Eureka Server: Acts as a service registry for service discovery, allowing services to locate each other dynamically.
Config Server: Provides centralized configuration management using a Git repository, with Vault integration for securing sensitive data (e.g., database credentials, SMTP settings).

1. User Service
  - Purpose: Manages CRUD operations (Create, Read, Update, Delete) for user details (e.g., id, email, password, isVerified).
  - Database: MySQL
  - Communication: Integrates with Auth Service for OTP-based registration.
  - Why MySQL?
      - MySQL is a widely used, open-source relational database that supports the structured data needs of the User Service (e.g., user tables with columns like id, email, etc.).
      It’s well-supported by Spring Data JPA, simplifying CRUD operations.
  - Why Spring Data JPA?
      - Provides an abstraction over raw JDBC, making it easier to define entities (User) and repositories (UserRepository) with minimal boilerplate code.
      Supports hibernate.ddl-auto: update, which automatically creates or updates the database schema based on the entity definition.
  - Why Communication with Auth Service?
      - OTP-based registration requires coordination between services. The User Service saves user details only after the Auth Service verifies the OTP, ensuring a secure registration flow.

2. Auth Service
 - Purpose: Handles user registration by sending OTPs to email and verifying them to activate accounts.
 - Dependencies: Spring Web, Spring Boot Starter Mail.
 - Communication: Registers with Eureka for discovery and uses Config Server for configuration.
 - Why Spring Boot Starter Mail?
    - Simplifies email sending using JavaMailSender, enabling OTP delivery via SMTP (e.g., Gmail).
    Supports secure email transmission with TLS/SSL, aligning with security best practices.
 - Why OTP-Based Registration?
    - Adds a layer of security by requiring email verification, preventing unauthorized account creation.
    Common in modern applications for user validation.

3. Eureka Server
 - Purpose: Acts as a service registry for service discovery.
 - Configuration: Runs on port 8761, disables self-registration.
 - Why Eureka?
    - Eureka, part of Spring Cloud Netflix, provides a robust service discovery mechanism, allowing services (User Service, Auth Service) to register and locate each other dynamically.
    Essential in a microservices architecture where services are distributed and may scale independently.
    Disabling register-with-eureka and fetch-registry prevents the server from registering itself, keeping it a dedicated registry.

4. Config Server
 - Purpose: Centralizes configuration from a Git repository, integrating with Vault for secure sensitive data.
 - Configuration Files: user-service.yml, auth-service.yml, eureka-server.yml.
 - Why Config Server?
    - Centralizes configuration management, making it easier to update settings (e.g., ports, database URLs) across all services without redeployment.
    Uses a Git repository for version control, ensuring configuration history and rollback capability.
    Integrates with Vault to securely manage sensitive data, reducing the risk of hardcoding credentials.
 - Why Vault?
    - Secures sensitive information (e.g., DB_USERNAME, DB_PASSWORD, MAIL_USERNAME, MAIL_PASSWORD) using a secrets manager.
    Prevents exposure in configuration files or environment variables, enhancing security.

Step 0:  Prerequisites
  1. Java 17+: Required for Spring Boot 3.x compatibility and modern features.
  2. Maven: Standard build tool for Java projects, managing dependencies efficiently.
  3. MySQL: Needed for the local database, installed and configured manually.
  4. Git: Version control for Config Server configurations.
  5. Vault: Used to secure sensitive data, installed locally.

Step 1: 
  - Setup Eureka Server
    Implementation: Created a Spring Boot app named eureka-server, added the Eureka Server dependency, and annotated the main class with @EnableEurekaServer. Configured it to run on port 8761 with application.yml.
    Why: Establishes a service registry, critical for microservices to discover each other dynamically.

Step 2:
 - Setup Config Server
    Implementation: Created a config-server project, added the Config Server dependency, and set it up to pull configurations from a local Git repository (e.g., C:\config-repo). Integrated Vault for secure data retrieval in bootstrap.yml.
    Why: Centralizes config management and secures sensitive data, reducing manual updates and security risks.

Step 3:
  - Setup User Service
  Implementation: Defined the User entity, UserRepository, and UserController with CRUD endpoints. Created a UserService class to handle business logic and communication with the Auth Service, and moved the RestTemplate bean to AppConfig to fix a circular dependency.
  Why: Provides a RESTful interface for user management, leveraging JPA for database interaction and ensuring proper service communication.

Step 4: 
  - Setup Auth Service
  Implementation: Implemented OtpService for email OTPs and AuthController for registration/verification. Configured Gmail SMTP settings in application.yml, addressing the UnknownHostException by ensuring network connectivity and using an App Password.
  Why: Secures registration with email verification, using Spring Mail for SMTP integration.

Step 5: 
  - Setup Vault
    Implementation: Installed Vault locally, started it in dev mode, enabled the KV secrets engine, and stored sensitive credentials (e.g., Gmail and MySQL credentials).
    Why: Enhances security by managing secrets externally, avoiding hardcoding, and integrates with Config Server for seamless retrieval.

Step 6:
  - Run the Project
    Implementation: Started MySQL, then ran each service using mvn spring-boot:run in the order: Eureka Server, Config Server, User Service, Auth Service. Tested endpoints with curl or Postman.
    Why: Validates the entire architecture, ensuring end-to-end functionality (e.g., OTP sending, user creation).

Component?
- Spring Boot: Enables rapid development with embedded servers and auto-configuration.
- Spring Cloud: Provides microservices features (Eureka, Config Server, Vault integration).
- MySQL: A reliable relational database for user data, hosted locally to avoid costs.
- Vault: Secures sensitive data, critical for production-like security in development.
- Git: Offers version control for configurations, aligning with Config Server’s design.

This is the link of Config Repo https://github.com/Deepoo2000/config-repo
