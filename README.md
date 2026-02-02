# Book Management Microservice

A minimal Spring Boot microservice for book management with two user types:
- **Consumer/Reader**: Can borrow and return books
- **BookKeeper**: Can add, edit, update, and manage books

## API Endpoints

### Books (BookKeeper operations)
- `GET /api/books` - Get all books
- `GET /api/books/available` - Get available books
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create new book
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Users
- `GET /api/users` - Get all users
- `POST /api/users` - Create new user

### Rentals (Consumer operations)
- `POST /api/rentals/borrow?userId={id}&bookId={id}&days={days}` - Borrow book
- `PUT /api/rentals/return/{rentalId}` - Return book
- `GET /api/rentals/user/{userId}` - Get user's active rentals

## Sample Data

### Create Users
```json
POST /api/users
{
  "name": "John Reader",
  "email": "john@example.com",
  "userType": "CONSUMER"
}

POST /api/users
{
  "name": "Jane Keeper",
  "email": "jane@example.com",
  "userType": "BOOKKEEPER"
}
```

### Create Books
```json
POST /api/books
{
  "title": "Spring Boot Guide",
  "author": "Tech Author",
  "isbn": "978-1234567890"
}
```

## GCP Deployment (Direct - No External Tools)

1. **Go to Google Cloud Console** (console.cloud.google.com)
2. **Open Cloud Shell** (terminal icon)
3. **Upload project files** using Cloud Shell editor
4. **Deploy:**
   ```bash
   gcloud app create --region=us-central
   mvn clean package
   gcloud app deploy src/main/appengine/app.yaml
   ```

**Alternative: Use Cloud Source Repositories**
- Upload code via web interface
- Deploy from Cloud Shell

## Local Development

```bash
mvn spring-boot:run
```

Access H2 Console: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:bookdb
- Username: sa
- Password: (empty)

## Architecture

This is a single microservice that handles:
- User management (Consumer/BookKeeper roles)
- Book inventory management
- Rental/borrowing system

The service uses:
- Spring Boot 3.2
- H2 in-memory database
- JPA/Hibernate
- REST APIs
- Docker containerization
- GCP Cloud Run deployment"# book-management-microservice" 
