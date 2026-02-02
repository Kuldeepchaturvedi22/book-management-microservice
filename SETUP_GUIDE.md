# Book Management Microservices System

A complete microservices-based book marketplace where **Sellers** can list books and **Buyers** can browse and purchase them.

## 🏗️ Architecture

```
Frontend (React - Port 3000)
    ↓
API Gateway (Port 8080)
    ↓
├── User Service (Port 8082) - Authentication & User Management
├── Book Service (Port 8081) - Book Listings & Inventory
└── Order Service (Port 8083) - Purchase Transactions
    ↓
Eureka Server (Port 8761) - Service Discovery
```

## 🚀 Quick Start

### 1. Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Wait until you see "Started EurekaServerApplication" (http://localhost:8761)

### 2. Start User Service
```bash
cd user-service
mvn spring-boot:run
```
Wait for "Started UserServiceApplication" (http://localhost:8082)

### 3. Start Book Service
```bash
cd book-service
mvn spring-boot:run
```
Wait for "Started BookServiceApplication" (http://localhost:8081)

### 4. Start Order Service
```bash
cd order-service
mvn spring-boot:run
```
Wait for "Started OrderServiceApplication" (http://localhost:8083)

### 5. Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```
Wait for "Started ApiGatewayApplication" (http://localhost:8080)

### 6. Start Frontend
```bash
cd frontend
npm install
npm start
```
Opens at http://localhost:3000

## 📱 How to Use

### For Sellers:
1. **Register** as SELLER
2. **Login** with your credentials
3. **List books** with title, author, ISBN, price, and quantity
4. **Manage inventory** - edit or delete your books
5. **View sales** - see all orders for your books

### For Buyers:
1. **Register** as BUYER
2. **Login** with your credentials
3. **Browse available books** in the marketplace
4. **Add to cart** and adjust quantities
5. **Purchase books** - stock updates automatically
6. **View order history**

## 🔌 API Endpoints

### User Service (via /api/users)
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Login user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/validate` - Validate JWT token

### Book Service (via /api/books)
- `GET /api/books` - Get all books
- `GET /api/books/available` - Get available books
- `GET /api/books/seller/{sellerId}` - Get books by seller
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create new book (Seller)
- `PUT /api/books/{id}` - Update book (Seller)
- `PUT /api/books/{id}/quantity` - Update stock quantity
- `DELETE /api/books/{id}` - Delete book (Seller)

### Order Service (via /api/orders)
- `POST /api/orders/purchase` - Create purchase order
- `GET /api/orders/buyer/{buyerId}` - Get buyer's orders
- `GET /api/orders/seller/{sellerId}` - Get seller's sales
- `GET /api/orders/{id}` - Get order by ID
- `PUT /api/orders/{id}/status` - Update order status

## 🧪 Testing the System

### Test Scenario 1: Complete Flow
1. Register as SELLER (email: seller@test.com, password: test123)
2. Login and add a book:
   - Title: "Spring Boot Guide"
   - Author: "John Doe"
   - ISBN: "978-1234567890"
   - Price: 29.99
   - Quantity: 10

3. Logout and register as BUYER (email: buyer@test.com, password: test123)
4. Login and browse books
5. Add book to cart and purchase
6. Check your order history

7. Logout and login as SELLER again
8. View your sales and see the order

### Test with cURL

**Register Seller:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Seller","email":"seller@test.com","password":"test123","role":"SELLER"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"seller@test.com","password":"test123"}'
```

**Add Book (use sellerId from login response):**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring Boot","author":"John Doe","isbn":"978-1234567890","price":29.99,"quantity":10,"sellerId":1}'
```

**Purchase Book (as buyer with buyerId):**
```bash
curl -X POST http://localhost:8080/api/orders/purchase \
  -H "Content-Type: application/json" \
  -d '{"buyerId":2,"bookId":1,"quantity":2}'
```

## 🛠️ Technology Stack

**Backend:**
- Spring Boot 3.2.0
- Spring Cloud (Eureka, Gateway)
- Spring Data JPA
- H2 Database (in-memory)
- JWT Authentication
- WebClient (inter-service communication)

**Frontend:**
- React 18
- Axios
- CSS3

## 📊 Service Ports

| Service | Port |
|---------|------|
| Eureka Server | 8761 |
| API Gateway | 8080 |
| Book Service | 8081 |
| User Service | 8082 |
| Order Service | 8083 |
| Frontend | 3000 |

## 🔐 Security

- JWT-based authentication
- Token stored in localStorage
- Password stored as plain text (⚠️ Use bcrypt in production!)
- CORS enabled for development

## 📝 Notes

- All services use H2 in-memory database (data resets on restart)
- Services must start in order: Eureka → Microservices → Gateway → Frontend
- Wait for each service to fully start before starting the next
- Check Eureka dashboard at http://localhost:8761 to verify all services are registered

## 🐛 Troubleshooting

**Services not connecting:**
- Ensure Eureka Server is running first
- Check all services are registered at http://localhost:8761
- Wait 30 seconds after starting each service

**Frontend can't connect:**
- Verify API Gateway is running on port 8080
- Check browser console for errors
- Ensure proxy is set in package.json

**Purchase fails:**
- Verify book has sufficient quantity
- Check buyer and book IDs are correct
- Ensure Order Service can reach Book Service

## 🎯 Next Steps for Production

1. Add Spring Security with bcrypt password hashing
2. Replace H2 with PostgreSQL/MySQL
3. Add API documentation with Swagger
4. Implement proper error handling
5. Add logging and monitoring
6. Add unit and integration tests
7. Implement rate limiting
8. Add Redis for caching
9. Deploy to cloud (AWS, GCP, Azure)
10. Add CI/CD pipeline
