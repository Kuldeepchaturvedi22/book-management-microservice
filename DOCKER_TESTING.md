# Local Docker Testing Guide

Test your Docker setup locally before deploying to GCP.

## Prerequisites

- Docker Desktop installed
- Docker Compose installed

## Quick Start

### 1. Build and Run All Services

```bash
# Build and start all services
docker-compose up --build

# Or run in detached mode
docker-compose up -d --build
```

### 2. Access the Application

- **Frontend**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **User Service**: http://localhost:8082
- **Book Service**: http://localhost:8081
- **Order Service**: http://localhost:8083

### 3. Stop Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## Individual Service Testing

### Build Individual Images

```bash
# Eureka Server
docker build -t eureka-server:local ./eureka-server

# User Service
docker build -t user-service:local ./user-service

# Book Service
docker build -t book-service:local ./book-service

# Order Service
docker build -t order-service:local ./order-service

# API Gateway
docker build -t api-gateway:local ./api-gateway

# Frontend
docker build -t frontend:local ./frontend
```

### Run Individual Containers

```bash
# Create network
docker network create book-network

# Run Eureka Server
docker run -d --name eureka-server --network book-network -p 8761:8761 eureka-server:local

# Run User Service
docker run -d --name user-service --network book-network -p 8082:8082 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/ \
  user-service:local

# Run Book Service
docker run -d --name book-service --network book-network -p 8081:8081 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/ \
  book-service:local

# Run Order Service
docker run -d --name order-service --network book-network -p 8083:8083 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/ \
  order-service:local

# Run API Gateway
docker run -d --name api-gateway --network book-network -p 8080:8080 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/ \
  api-gateway:local

# Run Frontend
docker run -d --name frontend --network book-network -p 3000:80 frontend:local
```

## Useful Docker Commands

### View Running Containers
```bash
docker ps
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service
docker logs -f user-service
```

### Execute Commands in Container
```bash
docker exec -it user-service sh
```

### View Container Stats
```bash
docker stats
```

### Clean Up

```bash
# Remove all stopped containers
docker container prune

# Remove all unused images
docker image prune -a

# Remove all unused volumes
docker volume prune

# Remove everything
docker system prune -a --volumes
```

## Testing the Application

### 1. Health Check
```bash
# Check if all services are up
curl http://localhost:8761  # Eureka
curl http://localhost:8080/actuator/health  # Gateway
curl http://localhost:8082/actuator/health  # User Service
curl http://localhost:8081/actuator/health  # Book Service
curl http://localhost:8083/actuator/health  # Order Service
```

### 2. Test API Endpoints
```bash
# Register user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@test.com","password":"test123","role":"SELLER"}'

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123"}'

# Create book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Book","author":"Author","isbn":"123","price":29.99,"quantity":10,"sellerId":1}'

# Get books
curl http://localhost:8080/api/books
```

## Troubleshooting

### Services not connecting
```bash
# Check network
docker network inspect book-network

# Check if Eureka is running
docker logs eureka-server

# Restart services
docker-compose restart
```

### Port already in use
```bash
# Find process using port
netstat -ano | findstr :8080  # Windows
lsof -i :8080  # Mac/Linux

# Kill process or change port in docker-compose.yml
```

### Image build fails
```bash
# Clean build cache
docker builder prune

# Rebuild without cache
docker-compose build --no-cache
```

### Container crashes
```bash
# View logs
docker logs <container-name>

# Check container status
docker inspect <container-name>
```

## Performance Testing

### Load Testing with Apache Bench
```bash
# Install Apache Bench
# Windows: Download from Apache website
# Mac: brew install httpd
# Linux: sudo apt-get install apache2-utils

# Test API Gateway
ab -n 1000 -c 10 http://localhost:8080/api/books

# Test with POST
ab -n 100 -c 5 -p data.json -T application/json http://localhost:8080/api/users/login
```

## Docker Compose Commands Reference

```bash
# Start services
docker-compose up

# Start in background
docker-compose up -d

# Build images
docker-compose build

# Build and start
docker-compose up --build

# Stop services
docker-compose stop

# Start stopped services
docker-compose start

# Restart services
docker-compose restart

# Remove containers
docker-compose down

# View logs
docker-compose logs

# Follow logs
docker-compose logs -f

# Scale service
docker-compose up -d --scale user-service=3

# List containers
docker-compose ps

# Execute command
docker-compose exec user-service sh
```

## Next Steps

Once local Docker testing is successful:
1. Push code to GitHub
2. Follow GCP_DEPLOYMENT.md for cloud deployment
3. GitHub Actions will handle CI/CD automatically
