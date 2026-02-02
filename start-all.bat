@echo off
echo ========================================
echo Book Management Microservices Startup
echo ========================================
echo.

echo Starting Eureka Server (Port 8761)...
start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 30

echo Starting User Service (Port 8082)...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"
timeout /t 20

echo Starting Book Service (Port 8081)...
start "Book Service" cmd /k "cd book-service && mvn spring-boot:run"
timeout /t 20

echo Starting Order Service (Port 8083)...
start "Order Service" cmd /k "cd order-service && mvn spring-boot:run"
timeout /t 20

echo Starting API Gateway (Port 8080)...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
timeout /t 20

echo Starting Frontend (Port 3000)...
start "Frontend" cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo All services are starting!
echo ========================================
echo.
echo Check these URLs:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - Frontend: http://localhost:3000
echo.
echo Press any key to exit this window...
pause > nul
