# GCP Microservices Deployment Guide

## Step-by-Step Deployment (Cloud Shell)

### 1. Upload Project to Cloud Shell
- Go to **console.cloud.google.com**
- Open **Cloud Shell**
- Upload entire **BookManagement** folder

### 2. Set Project and Enable APIs
```bash
gcloud config set project YOUR_PROJECT_ID
gcloud services enable appengine.googleapis.com
```

### 3. Deploy Services in Order

**Deploy Eureka Server First:**
```bash
cd BookManagement/eureka-server
mvn clean package
gcloud app deploy src/main/appengine/app.yaml
```

**Deploy User Service:**
```bash
cd ../user-service
mvn clean package
gcloud app deploy src/main/appengine/app.yaml
```

**Deploy Book Service:**
```bash
cd ../book-service
mvn clean package
gcloud app deploy src/main/appengine/app.yaml
```

**Deploy Rental Service:**
```bash
cd ../rental-service
mvn clean package
gcloud app deploy src/main/appengine/app.yaml
```

**Deploy API Gateway (Default Service):**
```bash
cd ../api-gateway
mvn clean package
gcloud app deploy src/main/appengine/app.yaml
```

## Service URLs After Deployment:
- **API Gateway**: https://YOUR_PROJECT_ID.appspot.com
- **Eureka Server**: https://eureka-server-dot-YOUR_PROJECT_ID.appspot.com
- **User Service**: https://user-service-dot-YOUR_PROJECT_ID.appspot.com
- **Book Service**: https://book-service-dot-YOUR_PROJECT_ID.appspot.com
- **Rental Service**: https://rental-service-dot-YOUR_PROJECT_ID.appspot.com

## API Endpoints:
All requests go through API Gateway:
- `GET https://YOUR_PROJECT_ID.appspot.com/api/users`
- `POST https://YOUR_PROJECT_ID.appspot.com/api/users`
- `GET https://YOUR_PROJECT_ID.appspot.com/api/books`
- `POST https://YOUR_PROJECT_ID.appspot.com/api/books`
- `POST https://YOUR_PROJECT_ID.appspot.com/api/rentals/borrow`

## Notes:
- Each service runs on port 8080 in App Engine
- Services communicate via App Engine URLs
- Eureka handles service discovery
- API Gateway routes all external requests