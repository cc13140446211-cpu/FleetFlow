# FleetFlow

FleetFlow is a backend freight management system built with Spring Boot. It is designed to manage the workflow from customer quotations and payments to freight job scheduling.

The system allows dispatchers to manage customers, prepare quotations, record payment status, and convert eligible quotations into scheduled freight jobs. It also validates driver and truck availability to prevent scheduling conflicts.

## Key Features

- Customer management
- Freight quotation creation and status management
- Quote payment status tracking
- Quote-to-job conversion
- Driver and truck assignment
- Driver and truck scheduling conflict detection
- Request validation and centralized exception handling
- Automated testing for core business logic and API validation
- Interactive API documentation with OpenAPI and Swagger UI

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Spring Boot | Backend application framework |
| Spring MVC | REST API development |
| MyBatis | Persistence and SQL mapping |
| MySQL | Relational database |
| Maven | Dependency management and build automation |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies in unit tests |
| MockMvc | Controller and HTTP-layer testing |
| OpenAPI / Swagger UI | Interactive API documentation |

## Architecture

FleetFlow follows a layered backend architecture to separate API handling, business logic, data access, and database persistence.

```text
Client / Swagger UI
        |
        | HTTP Request / Response
        v
+-------------------+
|    Controller     | ← HTTP Layer
+-------------------+
        |
        v
+-------------------+
|      Service      |← Business Logic
+-------------------+
        |
        v
+-------------------+
|      Mapper       |← Data Access
+-------------------+
        |
        | MyBatis
        v
+-------------------+
|       MySQL       |← Persistence
+-------------------+
```

## Core Business Workflow

FleetFlow manages the lifecycle of a freight request from quotation to job scheduling.

```text
Customer
   |
   v
Quote Created
(PENDING)
   |
   | Customer accepts quote
   v
Quote
(ACCEPTED)
   |
   | Payment recorded
   v
Payment
(PAID)
   |
   | Dispatcher schedules job
   v
Resource Validation
   |
   +--> Driver active and available?
   |
   +--> Truck available?
   |
   +--> Existing job for quote?
   |
   v
Job Created
(SCHEDULED)
   |
   v
Quote
(CONVERTED)
```

A freight job can only be created when the quotation has been accepted and paid. Before scheduling the job, FleetFlow validates the assigned driver and truck and checks for overlapping schedules to prevent resource conflicts.

Once the job is successfully created, the quotation is marked as `CONVERTED` to prevent the same quotation from being converted into multiple jobs.

## Database Design

FleetFlow uses MySQL as its relational database. The main entities are Customer, Employee, Truck, Quote, and Job.

```text
Customer
   |
   | 1 : N
   v
Quote
   |
   | 1 : 0..1
   v
Job
   | \
   |  \
   v   v
Driver Truck
(Employee)
```

### Main Entities

| Entity | Purpose |
|---|---|
| `customer` | Stores customer and company information |
| `employee` | Stores dispatchers and drivers |
| `truck` | Stores fleet vehicle information |
| `quote` | Stores freight quotations prepared for customers |
| `job` | Stores scheduled freight jobs and assigned resources |

### Key Relationships

- One customer can have multiple quotations.
- Each quotation belongs to one customer.
- Each quotation is prepared by a dispatcher.
- A quotation can be converted into at most one job.
- Each job is assigned to one driver and one truck.
- Each job records the dispatcher responsible for scheduling it.

The database uses primary keys and foreign keys to maintain relationships between entities. Unique and check constraints enforce important data rules, while indexes support common lookup and scheduling-conflict queries.

## API Documentation

FleetFlow provides interactive REST API documentation using OpenAPI and Swagger UI.

When the application is running locally, the Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI allows developers to inspect available endpoints, request schemas, response structures, and execute API requests directly from the browser.

### Core API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/customers` | Retrieve all customers |
| `GET` | `/api/customers/{id}` | Retrieve a customer by ID |
| `POST` | `/api/customers` | Create a new customer |
| `GET` | `/api/quotes` | Retrieve quotations |
| `GET` | `/api/quotes/{id}` | Retrieve a quotation by ID |
| `POST` | `/api/quotes` | Create a new freight quotation |
| `PATCH` | `/api/quotes/{id}/status` | Update quotation status |
| `PATCH` | `/api/quotes/{id}/payment` | Update quotation payment status |
| `POST` | `/api/quotes/{quoteId}/jobs` | Convert an eligible quotation into a scheduled job |
| `GET` | `/api/jobs` | Retrieve scheduled jobs |

## Testing

FleetFlow includes automated tests for core business rules and API validation.

### Service Unit Tests

JUnit 5 and Mockito are used to test service-layer business logic in isolation.

The mapper dependencies are mocked so that unit tests do not require a real MySQL database. This allows each test to focus on a specific business rule and control the behaviour of external dependencies.

Current service test coverage includes:

- Rejecting job creation when a quotation does not exist
- Rejecting job creation when a quotation has not been paid
- Preventing multiple jobs from being created from the same quotation
- Preventing driver scheduling conflicts
- Preventing truck scheduling conflicts
- Validating job pickup and drop-off times
- Successfully converting an eligible quotation into a freight job
- Validating quotation status transitions
- Validating quotation payment rules

Example test flow:

```text
JobService
    |
    | depends on
    v
Mock QuoteMapper
Mock EmployeeMapper
Mock TruckMapper
Mock JobMapper
    |
    v
Business rule is tested without accessing MySQL
```

### Controller Tests

MockMvc is used to test the HTTP and validation layer.

These tests verify that incoming JSON requests are correctly processed by Spring MVC and that invalid request data produces appropriate HTTP responses.

Examples include:

- Missing required fields → `400 Bad Request`
- Invalid email format → `400 Bad Request`
- Blank customer name → `400 Bad Request`
- Valid customer request → successful response

### Running Tests

Run all automated tests with:

```bash
./mvnw test
```

A successful test run should complete with no failures or errors.

## Project Structure

```text
src/
├── main/
│   ├── java/com/yuhan/fleetflow/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   │   └── request/
│   │   ├── exception/
│   │   ├── mapper/
│   │   ├── model/
│   │   └── service/
│   │
│   └── resources/
│
└── test/
    └── java/com/yuhan/fleetflow/
        ├── service/
        └── controller/

database/
├── schema.sql
└── sample-data.sql

pom.xml
README.md
```

### Package Responsibilities

- `controller` — REST API endpoints and HTTP request handling
- `service` — Core business logic and workflow coordination
- `mapper` — MyBatis-based database access
- `model` — Domain models used by the application
- `dto` — Data Transfer Objects used for API input
- `exception` — Custom exceptions and centralized error handling
- `config` — Application configuration such as OpenAPI metadata

## Getting Started

### Prerequisites

Make sure the following tools are installed:

- Java 23
- MySQL 8
- Git

Maven installation is optional because the project includes the Maven Wrapper.

### 1. Clone the Repository

```bash
git clone <repository-url>
cd FleetFlow
```

### 2. Create the Database

Log in to MySQL:

```bash
mysql -u root -p
```

Create the database:

```sql
CREATE DATABASE fleetflow;
```

Exit MySQL:

```sql
exit;
```

### 3. Create Database Tables

Run the schema file:

```bash
mysql -u root -p fleetflow < database/schema.sql
```

### 4. Load Sample Data

```bash
mysql -u root -p fleetflow < database/sample-data.sql
```

The sample data includes customers, dispatchers, drivers, and trucks for local development and testing.

### 5. Configure Database Connection

Configure the database connection in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fleetflow
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

Do not commit real database passwords or other secrets to the repository.

### 6. Run the Application

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend will run at:

```text
http://localhost:8080
```

### 7. Open Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger UI can be used to inspect and manually test the REST API.

### 8. Run Automated Tests

```bash
./mvnw test
```

## Future Improvements

FleetFlow is currently focused on the backend workflow for freight quotation and job scheduling.

Possible future improvements include:

- Build a web frontend for dispatcher operations
- Add authentication and role-based authorization
- Add customer-facing quotation and job tracking features
- Introduce a dedicated payment entity for payment history, deposits, and refunds
- Add more comprehensive integration testing
- Add CI using GitHub Actions
- Containerize the application using Docker
- Deploy the backend and database to a cloud environment
- Improve concurrency control for resource scheduling in a multi-instance deployment