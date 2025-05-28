# Configuration Guide

This document provides a detailed explanation of the configuration options available in `config.json` for generating Spring Boot microservices.

## Table of Contents
1. [Project Configuration](#project-configuration)
2. [Entity Configuration](#entity-configuration)
   - [Fields](#fields)
   - [Validations](#validations)
   - [CRUD Operations](#crud-operations)
   - [Custom Endpoints](#custom-endpoints)
   - [Security](#security)
3. [Client Configuration](#client-configuration)
4. [Validation Types](#validation-types)
5. [Response Types](#response-types)
6. [Pagination and Sorting](#pagination-and-sorting)
7. [Error Handling](#error-handling)
8. [Testing Configuration](#testing-configuration)

## Project Configuration

The root level configuration defines the basic project settings:

```json
{
  "projectName": "Order Service",           // Name of the microservice
  "projectDescription": "Microservice for managing orders",  // Description of the service
  "projectArtifactoryId": "order-service",  // Artifact ID for Maven/Gradle
  "basePackageName": "com.example.orders",  // Base Java package name
  "apiContextPath": "/api/v1",             // Base API path
  "emailContact": "dev@example.com"        // Contact email for API documentation
}
```

### Field Descriptions
- `projectName`: Display name of the service, used in documentation and logs
- `projectDescription`: Detailed description of the service's purpose
- `projectArtifactoryId`: Maven/Gradle artifact identifier
- `basePackageName`: Root Java package name (e.g., `com.example.orders`)
- `apiContextPath`: Base URL path for all API endpoints
- `emailContact`: Contact information for API documentation

## Entity Configuration

Entities represent the domain models in your microservice. Each entity can have fields, validations, CRUD operations, and custom endpoints.

### Fields

Fields define the properties of an entity:

```json
{
  "name": "Order",
  "fields": [
    {
      "type": "String",           // Java data type
      "name": "orderNumber",      // Field name
      "required": true,           // Whether the field is mandatory
      "description": "Unique order number",  // Field description
      "validations": [...]        // Validation rules
    }
  ]
}
```

#### Supported Field Types
- `String`: Text data
- `Integer`: Whole numbers
- `Long`: Large whole numbers
- `Double`: Decimal numbers
- `BigDecimal`: Precise decimal numbers (recommended for monetary values)
- `Boolean`: True/false values
- `LocalDate`: Date without time
- `LocalDateTime`: Date with time
- `LocalTime`: Time without date
- `Enum`: Custom enumerated types

### Validations

Validations ensure data integrity and business rules. Each validation can have specific parameters and error messages:

```json
"validations": [
  {
    "type": "NotBlank",
    "message": "Order number is required"
  },
  {
    "type": "Size",
    "min": 5,
    "max": 20,
    "message": "Order number must be between 5 and 20 characters"
  },
  {
    "type": "Pattern",
    "regexp": "^[A-Z0-9-]+$",
    "message": "Order number must contain only uppercase letters, numbers, and hyphens"
  }
]
```

### CRUD Operations

CRUD (Create, Read, Update, Delete) operations can be enabled/disabled and configured for each entity:

```json
"crudOperations": {
  "enabled": true,
  "operations": {
    "getAll": {
      "enabled": true,
      "path": "",
      "summary": "Get all orders",
      "description": "Retrieves a paginated list of all orders",
      "responses": {
        "200": {
          "description": "Successfully retrieved orders",
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/Order"
                }
              }
            }
          }
        }
      }
    }
  }
}
```

#### Available CRUD Operations
- `getAll`: Retrieve all entities (with pagination)
- `getById`: Retrieve a single entity by ID
- `create`: Create a new entity
- `update`: Update an existing entity
- `delete`: Delete an entity

Each operation can be configured with:
- `enabled`: Whether the operation is available
- `path`: URL path for the operation
- `summary`: Short description
- `description`: Detailed description
- `responses`: HTTP response configurations

### Custom Endpoints

Custom endpoints allow you to define additional API operations beyond CRUD:

```json
"endpoints": [
  {
    "name": "searchOrders",
    "method": "GET",
    "path": "/search",
    "summary": "Search orders",
    "description": "Search orders by various criteria",
    "returnType": "ResponseEntity<Page<Order>>",
    "parameters": [...],
    "responses": [...]
  }
]
```

#### Endpoint Configuration Options
- `name`: Method name in the controller
- `method`: HTTP method (GET, POST, PUT, DELETE)
- `path`: URL path
- `summary`: Short description
- `description`: Detailed description
- `returnType`: Java return type
- `parameters`: Input parameters
- `responses`: HTTP response configurations

#### Parameter Types
- `path`: URL path parameters
- `query`: Query string parameters
- `body`: Request body parameters
- `header`: HTTP header parameters

### Security

Security can be configured at both entity and endpoint levels:

```json
{
  "security": {
    "default": {
      "required": true,
      "roles": ["ORDER_ADMIN"]
    },
    "operations": {
      "getAll": {
        "roles": ["ORDER_READ"]
      },
      "getById": {
        "roles": ["ORDER_READ"]
      },
      "create": {
        "roles": ["ORDER_CREATE"]
      },
      "update": {
        "roles": ["ORDER_UPDATE"]
      },
      "delete": {
        "roles": ["ORDER_DELETE"]
      }
    }
  }
}
```

### Security Configuration Options
- `default`: Default security settings for the entity
  - `required`: Whether authentication is required
  - `roles`: List of required roles
- `operations`: Security settings for specific CRUD operations
  - Each operation can have its own set of required roles

## Client Configuration

Clients define external service integrations. Each client can have multiple endpoints, and each endpoint can have its own request and response objects.

### Client Request/Response Objects

Request and response objects are defined as part of the client configuration. These objects follow the same structure as entity fields:

```json
"requestTypes": {
  "PaymentRequest": {
    "fields": [
      {
        "type": "String",
        "name": "orderId",
        "required": true,
        "description": "Order identifier",
        "validations": [
          {
            "type": "NotBlank",
            "message": "Order ID is required"
          }
        ]
      },
      {
        "type": "BigDecimal",
        "name": "amount",
        "required": true,
        "description": "Payment amount",
        "validations": [
          {
            "type": "NotNull",
            "message": "Amount is required"
          },
          {
            "type": "DecimalMin",
            "value": "0.01",
            "message": "Amount must be greater than 0"
          }
        ]
      },
      {
        "type": "String",
        "name": "currency",
        "required": true,
        "description": "Payment currency",
        "validations": [
          {
            "type": "NotBlank",
            "message": "Currency is required"
          },
          {
            "type": "Pattern",
            "regexp": "^[A-Z]{3}$",
            "message": "Currency must be a 3-letter code"
          }
        ]
      }
    ]
  }
},
"responseTypes": {
  "PaymentResponse": {
    "fields": [
      {
        "type": "String",
        "name": "paymentId",
        "required": true,
        "description": "Payment identifier"
      },
      {
        "type": "String",
        "name": "status",
        "required": true,
        "description": "Payment status",
        "validations": [
          {
            "type": "Pattern",
            "regexp": "^(SUCCESS|FAILED|PENDING)$",
            "message": "Invalid payment status"
          }
        ]
      },
      {
        "type": "LocalDateTime",
        "name": "processedAt",
        "required": true,
        "description": "Payment processing timestamp"
      }
    ]
  },
  "PaymentStatus": {
    "fields": [
      {
        "type": "String",
        "name": "paymentId",
        "required": true,
        "description": "Payment identifier"
      },
      {
        "type": "String",
        "name": "status",
        "required": true,
        "description": "Current payment status"
      },
      {
        "type": "String",
        "name": "message",
        "required": false,
        "description": "Additional status information"
      }
    ]
  }
}
```

### Complete Client Example

Here's a complete example of a client configuration with request/response objects:

```json
{
  "name": "Payment",
  "baseUrl": "http://payment-service:8080",
  "connectTimeout": 5000,
  "readTimeout": 5000,
  "requestTypes": {
    "PaymentRequest": {
      "fields": [
        {
          "type": "String",
          "name": "orderId",
          "required": true,
          "description": "Order identifier",
          "validations": [
            {
              "type": "NotBlank",
              "message": "Order ID is required"
            }
          ]
        },
        {
          "type": "BigDecimal",
          "name": "amount",
          "required": true,
          "description": "Payment amount",
          "validations": [
            {
              "type": "NotNull",
              "message": "Amount is required"
            },
            {
              "type": "DecimalMin",
              "value": "0.01",
              "message": "Amount must be greater than 0"
            }
          ]
        }
      ]
    },
    "InventoryCheckRequest": {
      "fields": [
        {
          "type": "String",
          "name": "productId",
          "required": true,
          "description": "Product identifier"
        },
        {
          "type": "Integer",
          "name": "quantity",
          "required": true,
          "description": "Requested quantity",
          "validations": [
            {
              "type": "Min",
              "value": 1,
              "message": "Quantity must be at least 1"
            }
          ]
        }
      ]
    }
  },
  "responseTypes": {
    "PaymentResponse": {
      "fields": [
        {
          "type": "String",
          "name": "paymentId",
          "required": true,
          "description": "Payment identifier"
        },
        {
          "type": "String",
          "name": "status",
          "required": true,
          "description": "Payment status"
        }
      ]
    },
    "PaymentStatus": {
      "fields": [
        {
          "type": "String",
          "name": "paymentId",
          "required": true,
          "description": "Payment identifier"
        },
        {
          "type": "String",
          "name": "status",
          "required": true,
          "description": "Current payment status"
        }
      ]
    },
    "InventoryCheckResponse": {
      "fields": [
        {
          "type": "String",
          "name": "productId",
          "required": true,
          "description": "Product identifier"
        },
        {
          "type": "Boolean",
          "name": "available",
          "required": true,
          "description": "Whether the product is available"
        },
        {
          "type": "Integer",
          "name": "availableQuantity",
          "required": true,
          "description": "Available quantity"
        }
      ]
    }
  },
  "endpoints": [
    {
      "name": "processPayment",
      "path": "/payments",
      "method": "POST",
      "requestType": "PaymentRequest",
      "responseType": "PaymentResponse",
      "retryable": true,
      "timeout": 10000,
      "description": "Process a payment for an order"
    },
    {
      "name": "getPaymentStatus",
      "path": "/payments/{paymentId}",
      "method": "GET",
      "responseType": "PaymentStatus",
      "retryable": true,
      "timeout": 5000,
      "description": "Get the status of a payment"
    },
    {
      "name": "checkInventory",
      "path": "/inventory/check",
      "method": "POST",
      "requestType": "InventoryCheckRequest",
      "responseType": "InventoryCheckResponse",
      "retryable": true,
      "timeout": 3000,
      "description": "Check product availability"
    }
  ]
}
```

### Client Configuration Best Practices

1. **Request/Response Objects**
   - Define clear and specific request/response types
   - Include proper validation rules
   - Use appropriate data types
   - Add meaningful descriptions
   - Consider versioning for long-lived APIs

2. **Endpoint Configuration**
   - Set appropriate timeouts per endpoint
   - Enable retries only for idempotent operations
   - Use meaningful endpoint names
   - Include proper error handling
   - Document all parameters and responses

3. **Error Handling**
   - Define expected error responses
   - Include proper error codes
   - Add meaningful error messages
   - Consider retry strategies
   - Handle timeouts appropriately

4. **Security**
   - Use HTTPS for external services
   - Include authentication headers
   - Consider API keys or tokens
   - Handle sensitive data properly
   - Follow security best practices

## Validation Types

### Common Validations
- `NotBlank`: String must not be empty or whitespace
- `NotNull`: Field must not be null
- `Size`: String/collection size constraints
- `Min`: Minimum numeric value
- `Max`: Maximum numeric value
- `DecimalMin`: Minimum decimal value
- `DecimalMax`: Maximum decimal value
- `Digits`: Number of digits (integer and fraction)
- `Pattern`: Regular expression pattern
- `Email`: Valid email format
- `Past`: Date must be in the past
- `PastOrPresent`: Date must be in the past or present
- `Future`: Date must be in the future
- `FutureOrPresent`: Date must be in the future or present

## Response Types

### Common Response Types
- `ResponseEntity<T>`: Spring's response wrapper
- `Page<T>`: Paginated results
- `List<T>`: Collection of items
- `T`: Single item
- `Void`: No content

### HTTP Status Codes
- `200`: Success
- `201`: Created
- `204`: No Content
- `400`: Bad Request
- `401`: Unauthorized
- `403`: Forbidden
- `404`: Not Found
- `409`: Conflict
- `500`: Internal Server Error

## Best Practices

1. **Field Types**
   - Use `BigDecimal` for monetary values
   - Use appropriate date/time types
   - Use enums for fixed sets of values

2. **Validations**
   - Always provide meaningful error messages
   - Use appropriate validation types
   - Consider business rules in validations

3. **CRUD Operations**
   - Enable only needed operations
   - Provide clear descriptions
   - Define proper response types

4. **Custom Endpoints**
   - Use meaningful names
   - Document parameters and responses
   - Consider pagination for list endpoints

5. **Client Configuration**
   - Set appropriate timeouts
   - Enable retries for idempotent operations
   - Use meaningful endpoint names

## Example Configurations

### Basic Entity
```json
{
  "name": "Product",
  "fields": [
    {
      "type": "String",
      "name": "name",
      "required": true,
      "validations": [
        {
          "type": "NotBlank",
          "message": "Product name is required"
        }
      ]
    }
  ],
  "crudOperations": {
    "enabled": true
  }
}
```

### Complex Entity with Custom Endpoint
```json
{
  "name": "Order",
  "fields": [...],
  "crudOperations": {
    "enabled": true,
    "operations": {...}
  },
  "endpoints": [
    {
      "name": "searchOrders",
      "method": "GET",
      "path": "/search",
      "parameters": [...],
      "responses": [...]
    }
  ]
}
```

### External Client
```json
{
  "name": "Payment",
  "baseUrl": "http://payment-service:8080",
  "endpoints": [
    {
      "name": "processPayment",
      "path": "/payments",
      "method": "POST",
      "retryable": true
    }
  ]
}
```

## Pagination and Sorting

The generator supports pagination and sorting for list operations. This is configured through endpoint parameters:

```json
{
  "parameters": [
    {
      "name": "page",
      "type": "Integer",
      "in": "query",
      "required": false,
      "description": "Page number (0-based)",
      "default": 0,
      "validations": [
        {
          "type": "Min",
          "value": 0,
          "message": "Page number must be 0 or greater"
        }
      ]
    },
    {
      "name": "size",
      "type": "Integer",
      "in": "query",
      "required": false,
      "description": "Number of items per page",
      "default": 20,
      "validations": [
        {
          "type": "Min",
          "value": 1,
          "message": "Page size must be at least 1"
        },
        {
          "type": "Max",
          "value": 100,
          "message": "Page size cannot exceed 100"
        }
      ]
    },
    {
      "name": "sort",
      "type": "String",
      "in": "query",
      "required": false,
      "description": "Sort criteria (e.g., 'orderDate,desc')",
      "validations": [
        {
          "type": "Pattern",
          "regexp": "^[a-zA-Z]+(,[a-zA-Z]+)*(,asc|,desc)?$",
          "message": "Sort must be in format: field1,field2,desc"
        }
      ]
    }
  ]
}
```

### Pagination Response Schema

The paginated response includes metadata about the current page and total results:

```json
{
  "content": [
    // Array of items
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 100,
  "totalPages": 5,
  "last": false,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 20,
  "first": true,
  "empty": false
}
```

## Error Handling

The generator provides standardized error responses through the `ErrorResponse` schema:

```json
{
  "components": {
    "schemas": {
      "ErrorResponse": {
        "type": "object",
        "properties": {
          "timestamp": {
            "type": "string",
            "format": "date-time"
          },
          "status": {
            "type": "integer",
            "format": "int32"
          },
          "error": {
            "type": "string"
          },
          "message": {
            "type": "string"
          },
          "path": {
            "type": "string"
          }
        }
      }
    }
  }
}
```

### Common Error Scenarios
- 400 Bad Request: Invalid input data
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Insufficient permissions
- 404 Not Found: Resource not found
- 409 Conflict: Resource conflict (e.g., duplicate entry)
- 500 Internal Server Error: Unexpected server error

## Testing Configuration

The generator creates comprehensive test suites for each layer:

### Controller Tests
- Uses `@WebMvcTest` for testing REST endpoints
- Mocks service layer dependencies
- Tests all CRUD operations
- Validates response status codes and content
- Tests security constraints

### Service Tests
- Uses `@ExtendWith(MockitoExtension.class)` for unit testing
- Mocks repository layer
- Tests business logic
- Validates transaction boundaries
- Tests error scenarios

### Repository Tests
- Uses `@DataJpaTest` for integration testing
- Tests database operations
- Validates entity mappings
- Tests custom queries
- Tests pagination and sorting

### Test Utilities
- Test data builders
- Mock data generators
- Common assertions
- Security test helpers

### Best Practices
1. Use meaningful test names (e.g., `shouldCreateOrderWhenValidDataProvided`)
2. Follow given/when/then structure
3. Test both success and failure scenarios
4. Use appropriate test annotations
5. Mock external dependencies
6. Validate all response aspects
7. Test security constraints
8. Use test data builders
9. Keep tests independent
10. Follow AAA pattern (Arrange, Act, Assert) 