A Spring Boot RESTful API for managing personal tasks with secure authentication via Keycloak , JWT validation, and role-based access control.

This application allows users to:

Create, Read, Update, and Delete their own tasks
Authenticate using Keycloak (OAuth2 + JWT)
Enforce ownership checks at the service layer

🧰Prerequisites
Before running the app, ensure you have:
**Java 24
Maven 
Docker (for running Keycloak)**
MySQL

a. Keycloak Setup Instructions
1. Start Keycloak with Docker
   sudo docker run -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.2.3 start-dev

2. Open Admin Console
    http://localhost:8080
Log in as admin.

3. Create Realm: task-manager-realm
   Go to Add realm
   Name: task-manager-realm
   Click Create
4. Create Client: task-manager-app
   Go to Clients > Create client
   Client ID : task-manager-app
   Access Type : confidential
   Root URL
   http://localhost:8081
   Home URL
   http://localhost:8081
   Valid redirect URIs
   http://localhost:8081/*
   Valid post logout redirect URIs
   http://localhost:8081

5. Create Role: user-role
   Go to Roles > Add role
   Name: user-role
   Save

6. Create User
   Go to Users > Add user
   Set username (user)
   Set password under Credentials tab

7. Assign Role to User
   Go to Role Mappings
   Under Available Roles , select user and click Add selected

/-----------------------/

b. Build and Run Instructions
Clone the Repository
git clone git@github.com:A7medAlmubarak/secure-task-manager-api.git
cd secure-task-manager-api

Build the App : ./mvnw clean package

/-----------------------/

c. Obtain JWT Token from Keycloak
Use this command to get a token:
curl -X POST http://localhost:8080/realms/task-manager-realm/protocol/openid-connect/token \   
-H "Content-Type: application/x-www-form-urlencoded" \
-d "client_id=task-manager-app" \
-d "username=user" \
-d "password=password" \
-d "grant_type=password" \"
/-----------------------/

d. Design Decisions & Justifications
i. Authorization Logic (User Owns Task Check)
We enforce task ownership at the service layer , not using @PreAuthorize("hasRole('user') or similar method-level security annotations.
Why?
Explicit logic : We query the database directly using findByIdAndOwnerId(...) which clearly shows that only tasks belonging to the current user can be accessed.
Centralized business logic : Keeps ownership logic in one place (service) rather than scattering it across multiple layers.

ii. Keycloak Integration (JWT Validation & User ID Extraction)
Spring Security is configured as an OAuth2 Resource Server .

We use:
A custom JwtAuthConverter to extract roles from realm_access.roles claim and map them to ROLE_user, etc.
We also extract the user ID from the JWT using:
Authentication.getName()

iii. Challenges Encountered & Resolved
Problem:
Users could access other users’ tasks.

Solution:
Implemented ownership check in service layer using:
Optional<Task> findByIdAndOwnerId(Long id, String ownerId);
Ensured all operations verify ownership before proceeding.

/-----------------------/

e. Domain-Driven Design (DDD) Approach
The application follows a lightweight Domain-Driven Design (DDD) approach to model the core functionality of task management. 
This helps separate business logic from infrastructure concerns and keeps the system scalable and maintainable.

secure-task-manager-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.task/
│   │   │       ├── TaskApplication.java
│   │   │       │
│   │   │       ├── application/
│   │   │       │   ├── service/
│   │   │       │   │   └── TaskService.java
│   │   │       │   ├── exception/task/
│   │   │       │   └── usecase/
│   │   │       │
│   │   │       ├── domain/
│   │   │       │   ├── entity/
│   │   │       │   │   └── Task.java
│   │   │       │   └── Interface/
│   │   │       │
│   │   │       ├── infrastructure/
│   │   │       │   ├── config/
│   │   │       │   │   ├── LoggingConfig.java
│   │   │       │   │   └── CacheConfig.java
│   │   │       │   ├── database/
│   │   │       │   │   └── dao/TaskRepository.java
│   │   │       │   ├── logging/
│   │   │       │   └── security/
│   │   │       │       ├── JwtAuthConverter.java
│   │   │       │       └── SecurityConfig.java
│   │   │       │
│   │   │       └── presentation/
│   │   │           ├── dto/
│   │   │           │   ├── request/
│   │   │           │   │   └── TaskRequestDTO.java
│   │   │           │   └── response/
│   │   │           │       └── TaskResponseDTO.java
│   │   │           └── controller/
│   │   │           │   └── TaskController.java
│   │   │           └── utils/
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │
│   └── test/
│
├── pom.xml
└── README.md