# Quick Reference Guide

## 🚀 Start Commands

```bash
# Start each service in separate terminal windows

# 1. Eureka Server
cd eureka-server
mvn spring-boot:run

# 2. User Service
cd user-service
mvn spring-boot:run

# 3. Book Service
cd book-service
mvn spring-boot:run

# 4. Order Service
cd order-service
mvn spring-boot:run

# 5. API Gateway
cd api-gateway
mvn spring-boot:run

# 6. Frontend
cd frontend
npm install  # First time only
npm start
```

## 🔗 Service URLs

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend | http://localhost:3000 | User Interface |
| API Gateway | http://localhost:8080 | Single entry point |
| Eureka Dashboard | http://localhost:8761 | Service registry |
| User Service | http://localhost:8082 | Direct access (dev only) |
| Book Service | http://localhost:8081 | Direct access (dev only) |
| Order Service | http://localhost:8083 | Direct access (dev only) |

## 📡 API Endpoints (via Gateway)

### User Endpoints
```bash
# Register
POST http://localhost:8080/api/users/register
Body: {"name":"John","email":"john@test.com","password":"pass123","role":"SELLER"}

# Login
POST http://localhost:8080/api/users/login
Body: {"email":"john@test.com","password":"pass123"}

# Get User
GET http://localhost:8080/api/users/{id}

# Validate Token
GET http://localhost:8080/api/users/validate
Header: Authorization: Bearer {token}
```

### Book Endpoints
```bash
# Get All Books
GET http://localhost:8080/api/books

# Get Available Books
GET http://localhost:8080/api/books/available

# Get Books by Seller
GET http://localhost:8080/api/books/seller/{sellerId}

# Get Book by ID
GET http://localhost:8080/api/books/{id}

# Create Book
POST http://localhost:8080/api/books
Body: {"title":"Book Title","author":"Author","isbn":"123","price":29.99,"quantity":10,"sellerId":1}

# Update Book
PUT http://localhost:8080/api/books/{id}
Body: {"title":"Updated Title","author":"Author","isbn":"123","price":29.99,"quantity":10,"sellerId":1}

# Update Quantity
PUT http://localhost:8080/api/books/{id}/quantity
Body: {"quantity":5}

# Delete Book
DELETE http://localhost:8080/api/books/{id}
```

### Order Endpoints
```bash
# Purchase Book
POST http://localhost:8080/api/orders/purchase
Body: {"buyerId":2,"bookId":1,"quantity":2}

# Get Buyer Orders
GET http://localhost:8080/api/orders/buyer/{buyerId}

# Get Seller Sales
GET http://localhost:8080/api/orders/seller/{sellerId}

# Get Order by ID
GET http://localhost:8080/api/orders/{id}

# Update Order Status
PUT http://localhost:8080/api/orders/{id}/status
Body: {"status":"COMPLETED"}
```

## 🧪 Test Data

### Create Test Seller
```json
POST /api/users/register
{
  "name": "Alice Seller",
  "email": "alice@seller.com",
  "password": "seller123",
  "role": "SELLER"
}
```

### Create Test Buyer
```json
POST /api/users/register
{
  "name": "Bob Buyer",
  "email": "bob@buyer.com",
  "password": "buyer123",
  "role": "BUYER"
}
```

### Create Test Book (as Seller with ID 1)
```json
POST /api/books
{
  "title": "Spring Boot in Action",
  "author": "Craig Walls",
  "isbn": "978-1617292545",
  "price": 39.99,
  "quantity": 15,
  "sellerId": 1
}
```

### Purchase Book (as Buyer with ID 2)
```json
POST /api/orders/purchase
{
  "buyerId": 2,
  "bookId": 1,
  "quantity": 2
}
```

## 🐛 Troubleshooting

### Service won't start
```bash
# Check if port is already in use
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Mac/Linux

# Kill process using port
taskkill /PID <PID> /F        # Windows
kill -9 <PID>                 # Mac/Linux
```

### Services not registering with Eureka
- Wait 30 seconds after starting each service
- Check Eureka dashboard: http://localhost:8761
- Verify application.yml has correct eureka.client.service-url

### Frontend can't connect to backend
- Verify API Gateway is running on port 8080
- Check browser console for CORS errors
- Verify proxy in package.json points to correct URL

### Purchase fails
- Check book has sufficient quantity
- Verify buyer and book IDs exist
- Check Order Service logs for errors

## 📝 Common Tasks

### Add New Book Field
1. Update Book.java entity
2. Add getter/setter
3. Update BookController if needed
4. Update frontend form

### Add New API Endpoint
1. Add method to Controller
2. Update API Gateway routes if needed
3. Update frontend to call new endpoint

### Change Database
1. Update pom.xml with new DB driver
2. Update application.yml datasource config
3. Remove H2 dependency

## 🔐 Security Notes

⚠️ **Current Implementation (Development Only)**
- Passwords stored in plain text
- JWT secret in config file
- CORS wide open
- No input validation
- No rate limiting

✅ **For Production**
- Use BCrypt for passwords
- Store secrets in environment variables
- Restrict CORS to specific origins
- Add input validation
- Implement rate limiting
- Add HTTPS
- Use proper database (PostgreSQL/MySQL)

## 📊 Database Access

### H2 Console (if enabled)
```
URL: http://localhost:8081/h2-console
JDBC URL: jdbc:h2:mem:bookdb
Username: sa
Password: (empty)
```

## 🎯 User Roles

| Role | Can Do |
|------|--------|
| SELLER | List books, Edit own books, Delete own books, View sales |
| BUYER | Browse books, Purchase books, View order history |

## 💡 Tips

- Always start Eureka Server first
- Wait for each service to fully start before starting next
- Check Eureka dashboard to verify all services are registered
- Use browser DevTools Network tab to debug API calls
- Check service logs for detailed error messages
- Use Postman or cURL for API testing

## 📦 Build Commands

```bash
# Build all services
cd eureka-server && mvn clean package
cd user-service && mvn clean package
cd book-service && mvn clean package
cd order-service && mvn clean package
cd api-gateway && mvn clean package

# Build frontend
cd frontend && npm run build
```

## 🔄 Reset Everything

```bash
# Stop all services (Ctrl+C in each terminal)
# Restart in order:
# 1. Eureka → 2. User → 3. Book → 4. Order → 5. Gateway → 6. Frontend
```

## 📞 Support

- Check SETUP_GUIDE.md for detailed instructions
- Check PROJECT_STRUCTURE.md for architecture details
- Check IMPLEMENTATION_SUMMARY.md for what's been implemented
- Check ARCHITECTURE_DIAGRAM.md for visual diagrams
