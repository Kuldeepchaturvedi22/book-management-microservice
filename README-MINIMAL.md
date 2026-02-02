# Book Management System - GCP Free Tier Optimized

A minimal microservices-based book management system optimized for GCP free tier deployment.

## Architecture

- **Eureka Server** (Port 8761) - Service discovery
- **Book Service** (Port 8080) - CRUD operations for books
- **API Gateway** (Port 8080) - Routes requests to services
- **React Frontend** - User interface

## Features

- Add new books
- View all books
- Edit existing books
- Delete books
- Responsive web interface

## Local Development

### Prerequisites
- Java 17
- Node.js 16+
- Maven

### Running Locally

1. **Start Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Start Book Service**
   ```bash
   cd book-service
   mvn spring-boot:run
   ```

3. **Start API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

4. **Start Frontend**
   ```bash
   cd frontend
   npm install
   npm start
   ```

Access the application at `http://localhost:3000`

## GCP Deployment

### Prerequisites
- GCP Account with billing enabled
- Google Cloud SDK installed
- Project created in GCP Console

### Deploy to GCP

1. **Set your GCP project**
   ```bash
   gcloud config set project YOUR_PROJECT_ID
   ```

2. **Run deployment script**
   ```bash
   deploy-gcp-minimal.bat
   ```

### Memory Optimization Features

- **F1 instance class** - Minimal memory footprint
- **JVM heap limited to 128MB**
- **Disabled unnecessary Spring Boot features**
- **H2 in-memory database** - No external database needed
- **Single instance scaling** - Reduces resource usage

## API Endpoints

### Book Service
- `GET /books` - Get all books
- `GET /books/{id}` - Get book by ID
- `POST /books` - Create new book
- `PUT /books/{id}` - Update book
- `DELETE /books/{id}` - Delete book

## Cost Optimization

This setup is designed to run within GCP's free tier limits:
- Uses F1 micro instances (free tier eligible)
- Minimal memory allocation
- Auto-scaling from 0 to 1 instance
- No external databases or storage

## Troubleshooting

### Common Issues

1. **Out of Memory Errors**
   - Increase heap size in app.yaml if needed
   - Monitor memory usage in GCP Console

2. **Service Discovery Issues**
   - Ensure Eureka Server is deployed first
   - Check service URLs in application.yml

3. **Frontend API Calls Failing**
   - Update proxy configuration in package.json
   - Check CORS settings in BookController

## Project Structure

```
BookManagement/
├── eureka-server/          # Service discovery
├── book-service/           # Book CRUD operations
├── api-gateway/            # Request routing
├── frontend/               # React UI
└── deploy-gcp-minimal.bat  # Deployment script
```