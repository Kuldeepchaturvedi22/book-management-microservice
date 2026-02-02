@echo off
echo Deploying Book Management System to GCP...

echo.
echo 1. Deploying Eureka Server...
cd eureka-server
call gcloud app deploy src/main/appengine/app.yaml --quiet
cd ..

echo.
echo 2. Deploying Book Service...
cd book-service
call gcloud app deploy src/main/appengine/app.yaml --quiet
cd ..

echo.
echo 3. Deploying API Gateway...
cd api-gateway
call gcloud app deploy src/main/appengine/app.yaml --quiet
cd ..

echo.
echo 4. Building and deploying Frontend...
cd frontend
call npm run build
echo Frontend built successfully. Deploy the 'build' folder to your preferred static hosting service.
cd ..

echo.
echo Deployment completed!
echo Check your GCP Console for service URLs.