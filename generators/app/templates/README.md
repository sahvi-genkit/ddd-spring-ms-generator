# <%= project_name %>

<%= project_description %>

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository
2. Build the project:
   ```bash
   ./mvnw clean install
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on port 8080 with the context path `<%= api_context_path %>`.

## Project Structure

The project follows Domain-Driven Design (DDD) principles and is organized into the following layers:

```
src/main/java/<%= package_dir %>/
├── Application.java              # Main application class
├── domain/                       # Domain Layer
│   ├── model/                    # Domain entities and value objects
│   │   └── BaseEntity.java       # Base entity class
│   └── repository/               # Repository interfaces
│       └── Repository.java       # Base repository interface
├── application/                  # Application Layer
│   └── service/                  # Application services
│       └── BaseService.java      # Base service class
└── infrastructure/               # Infrastructure Layer
    ├── config/                   # Configuration classes
    │   └── JpaConfig.java        # JPA configuration
    └── persistence/              # Persistence implementations
        └── JpaRepository.java    # Base JPA repository
```

### Layer Descriptions

1. **Domain Layer** (`domain/`)
   - Contains the core business logic
   - Includes domain entities, value objects, and repository interfaces
   - Independent of other layers

2. **Application Layer** (`application/`)
   - Orchestrates the flow of data to and from the domain layer
   - Contains application services that implement use cases
   - Coordinates domain objects to perform tasks

3. **Infrastructure Layer** (`infrastructure/`)
   - Provides technical capabilities to support higher layers
   - Implements interfaces defined in the domain layer
   - Contains configurations, persistence, and external service implementations

## Database

The application uses H2 as an in-memory database for development. You can access the H2 console at:
- URL: `http://localhost:8080<%= api_context_path %>/h2-console`
- JDBC URL: `jdbc:h2:mem:<%= project_artifactory_id %>db`
- Username: `sa`
- Password: (empty)

## API Documentation

Once the application is running, you can access:
- Actuator endpoints: `http://localhost:8080<%= api_context_path %>/actuator`
- Health check: `http://localhost:8080<%= api_context_path %>/actuator/health`
- H2 Console: `http://localhost:8080<%= api_context_path %>/h2-console`

## Contact

For any questions or issues, please contact: <%= email_contact %>

## 
- The generated code follows best practices:
- Proper separation of concerns
- Comprehensive error handling
- RESTful API design
- OpenAPI documentation
- Input validation
- Test coverage
- Clean code structure
