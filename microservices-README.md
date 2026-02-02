# Book Management Microservices Architecture

## Architecture Overview
- **Eureka Server** (8761) - Service Discovery
- **API Gateway** (8080) - Single entry point
- **User Service** (8081) - User management
- **Book Service** (8082) - Book inventory
- **Rental Service** (8083) - Book borrowing/returning

## Running the Microservices

### Option 1: Individual Services
```bash
# 1. Start Eureka Server
cd eureka-server
mvn spring-boot:run

# 2. Start User Service
cd user-service
mvn spring-boot:run

# 3. Start Book Service
cd book-service
mvn spring-boot:run

# 4. Start Rental Service
cd rental-service
mvn spring-boot:run

# 5. Start API Gateway
cd api-gateway
mvn spring-boot:run
```



## API Endpoints (via Gateway - Port 8080)

### Users
- `GET /api/users` - Get all users
- `POST /api/users` - Create user

### Books
- `GET /api/books` - Get all books
- `GET /api/books/available` - Get available books
- `POST /api/books` - Create book
- `PUT /api/books/{id}` - Update book

### Rentals
- `POST /api/rentals/borrow?userId={id}&bookId={id}&days={days}` - Borrow book
- `PUT /api/rentals/return/{rentalId}` - Return book
- `GET /api/rentals/user/{userId}` - Get user rentals

## Service URLs (Direct Access)
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Book Service**: http://localhost:8082
- **Rental Service**: http://localhost:8083

## Microservice Features
- **Service Discovery**: Eureka for automatic service registration
- **Load Balancing**: Gateway routes to available instances
- **Inter-Service Communication**: Feign clients
- **Circuit Breaker**: Built-in resilience patterns
- **Centralized Routing**: Single API Gateway entry point

## Testing the System
1. Create users via `/api/users`
2. Add books via `/api/books`
3. Borrow books via `/api/rentals/borrow`
4. Return books via `/api/rentals/return`

All requests go through the API Gateway at port 8080.