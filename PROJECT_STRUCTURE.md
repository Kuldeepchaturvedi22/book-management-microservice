# Project Structure

## Complete Microservices Architecture

```
BookManagement/
│
├── eureka-server/                    # Service Discovery (Port 8761)
│   ├── src/main/java/com/eureka/
│   │   └── EurekaServerApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── user-service/                     # User Management & Auth (Port 8082)
│   ├── src/main/java/com/userservice/
│   │   ├── User.java                 # Entity: id, name, email, password, role
│   │   ├── UserRepository.java       # JPA Repository
│   │   ├── UserController.java       # REST API: register, login, validate
│   │   ├── JwtUtil.java              # JWT token generation/validation
│   │   └── UserServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.yml           # JWT secret, DB config
│   └── pom.xml
│
├── book-service/                     # Book Inventory (Port 8081)
│   ├── src/main/java/com/bookservice/
│   │   ├── Book.java                 # Entity: id, title, author, isbn, price, quantity, sellerId, status
│   │   ├── BookRepository.java       # JPA Repository with custom queries
│   │   ├── BookController.java       # REST API: CRUD + seller/available filters
│   │   ├── SwaggerConfig.java        # API documentation
│   │   └── BookServiceApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── order-service/                    # Order Processing (Port 8083)
│   ├── src/main/java/com/orderservice/
│   │   ├── Order.java                # Entity: id, buyerId, bookId, sellerId, quantity, totalPrice, status, orderDate
│   │   ├── OrderRepository.java      # JPA Repository
│   │   ├── OrderController.java      # REST API: purchase, buyer/seller orders
│   │   └── OrderServiceApplication.java  # WebClient for inter-service calls
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── api-gateway/                      # API Gateway (Port 8080)
│   ├── src/main/java/com/gateway/
│   │   └── ApiGatewayApplication.java
│   ├── src/main/resources/
│   │   └── application.yml           # Routes: /api/users, /api/books, /api/orders
│   └── pom.xml
│
├── frontend/                         # React Frontend (Port 3000)
│   ├── src/
│   │   ├── components/
│   │   │   ├── Login.js              # Login/Register component
│   │   │   ├── SellerDashboard.js    # Seller: list books, view sales
│   │   │   └── BuyerDashboard.js     # Buyer: browse, purchase, view orders
│   │   ├── App.js                    # Main app with auth routing
│   │   ├── App.css                   # Styling
│   │   └── index.js
│   ├── package.json                  # Dependencies: react, axios
│   └── public/
│
├── start-all.bat                     # Windows startup script
├── SETUP_GUIDE.md                    # Complete setup instructions
└── README.md                         # Project overview
```

## Data Flow

### User Registration/Login Flow
```
Frontend → API Gateway → User Service → H2 Database
                              ↓
                         JWT Token
                              ↓
                         Frontend (localStorage)
```

### Book Listing Flow (Seller)
```
Seller Dashboard → API Gateway → Book Service → H2 Database
                                      ↓
                              Book with sellerId
```

### Purchase Flow (Buyer)
```
Buyer Dashboard → API Gateway → Order Service
                                      ↓
                              WebClient Call
                                      ↓
                              Book Service (check stock)
                                      ↓
                              Update quantity
                                      ↓
                              Create Order → H2 Database
```

## Database Schema

### User Service (userdb)
```sql
users
├── id (PK)
├── name
├── email (unique)
├── password
└── role (SELLER/BUYER)
```

### Book Service (bookdb)
```sql
books
├── id (PK)
├── title
├── author
├── isbn
├── price
├── quantity
├── seller_id (FK → users.id)
└── status (AVAILABLE/SOLD_OUT)
```

### Order Service (orderdb)
```sql
orders
├── id (PK)
├── buyer_id (FK → users.id)
├── book_id (FK → books.id)
├── seller_id (FK → users.id)
├── quantity
├── total_price
├── status (PENDING/COMPLETED/CANCELLED)
└── order_date
```

## API Routes (via API Gateway)

### User Service Routes
- POST /api/users/register
- POST /api/users/login
- GET /api/users/{id}
- GET /api/users/validate

### Book Service Routes
- GET /api/books
- GET /api/books/available
- GET /api/books/seller/{sellerId}
- GET /api/books/{id}
- POST /api/books
- PUT /api/books/{id}
- PUT /api/books/{id}/quantity
- DELETE /api/books/{id}

### Order Service Routes
- POST /api/orders/purchase
- GET /api/orders/buyer/{buyerId}
- GET /api/orders/seller/{sellerId}
- GET /api/orders/{id}
- PUT /api/orders/{id}/status

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Microservices | Spring Boot 3.2 |
| Database | H2 (in-memory) |
| ORM | Spring Data JPA |
| Authentication | JWT (jjwt 0.11.5) |
| Inter-service Communication | WebClient (Spring WebFlux) |
| Frontend | React 18 |
| HTTP Client | Axios |
| Build Tool | Maven |
| Java Version | 17 |

## Key Features

✅ Microservices architecture with service discovery
✅ JWT-based authentication
✅ Role-based access (Seller/Buyer)
✅ Real-time inventory management
✅ Inter-service communication
✅ RESTful APIs
✅ Responsive React UI
✅ Shopping cart functionality
✅ Order tracking
✅ Sales dashboard for sellers
