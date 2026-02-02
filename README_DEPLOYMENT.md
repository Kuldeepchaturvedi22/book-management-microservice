# Book Management System - Cloud Deployment Ready

A complete microservices-based book marketplace with Docker, Kubernetes, and CI/CD.

## 🚀 Quick Links

- **[Local Setup Guide](SETUP_GUIDE.md)** - Run locally without Docker
- **[Docker Testing](DOCKER_TESTING.md)** - Test with Docker Compose
- **[GCP Deployment](GCP_DEPLOYMENT.md)** - Deploy to Google Cloud
- **[CI/CD Setup](CICD_SETUP.md)** - Automated deployment with GitHub Actions
- **[Deployment Summary](DEPLOYMENT_SUMMARY.md)** - Complete overview

## 📦 What's Included

### Microservices
- **Eureka Server** (8761) - Service discovery
- **User Service** (8082) - Authentication & user management
- **Book Service** (8081) - Book inventory
- **Order Service** (8083) - Purchase transactions
- **API Gateway** (8080) - Single entry point
- **Frontend** (3000/80) - React UI

### Deployment Options
1. **Local Development** - Run with Maven/npm
2. **Docker Compose** - Containerized local testing
3. **GCP Kubernetes** - Production deployment
4. **CI/CD Pipeline** - Automated with GitHub Actions

## 🎯 Choose Your Path

### 1️⃣ Just Want to Test Locally?
```bash
# Start all services with Docker
docker-compose up --build

# Access at http://localhost:3000
```
📖 **Guide:** [DOCKER_TESTING.md](DOCKER_TESTING.md)

---

### 2️⃣ Want to Deploy to GCP Manually?
```bash
# Create GKE cluster
gcloud container clusters create book-management-cluster \
  --zone us-central1-a --num-nodes 3

# Deploy
kubectl apply -f k8s/
```
📖 **Guide:** [GCP_DEPLOYMENT.md](GCP_DEPLOYMENT.md)

---

### 3️⃣ Want Automated CI/CD?
```bash
# 1. Set up GCP and GitHub secrets
# 2. Push to GitHub
git push origin main

# GitHub Actions handles the rest!
```
📖 **Guide:** [CICD_SETUP.md](CICD_SETUP.md)

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Frontend (React)                      │
│                  http://EXTERNAL-IP                      │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│                   API Gateway (8080)                     │
└────────┬──────────────┬──────────────┬──────────────────┘
         │              │              │
         ▼              ▼              ▼
┌──────────────┐ ┌──────────┐ ┌──────────────┐
│ User Service │ │  Book    │ │    Order     │
│    (8082)    │ │ Service  │ │   Service    │
│              │ │  (8081)  │ │   (8083)     │
└──────────────┘ └──────────┘ └──────────────┘
         │              │              │
         └──────────────┴──────────────┘
                        │
                        ▼
              ┌──────────────────┐
              │  Eureka Server   │
              │     (8761)       │
              └──────────────────┘
```

## 📋 Prerequisites

### For Local Development
- Java 17
- Maven
- Node.js & npm

### For Docker
- Docker Desktop
- Docker Compose

### For GCP Deployment
- Google Cloud account
- gcloud CLI
- kubectl
- GitHub account (for CI/CD)

## 🚀 Quick Start

### Option 1: Docker Compose (Easiest)
```bash
docker-compose up --build
```
Access: http://localhost:3000

### Option 2: Manual Local
```bash
# Terminal 1: Eureka
cd eureka-server && mvn spring-boot:run

# Terminal 2: User Service
cd user-service && mvn spring-boot:run

# Terminal 3: Book Service
cd book-service && mvn spring-boot:run

# Terminal 4: Order Service
cd order-service && mvn spring-boot:run

# Terminal 5: Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 6: Frontend
cd frontend && npm start
```

## 🎨 Features

### For Sellers
- ✅ Register and login
- ✅ List books with price and quantity
- ✅ Manage inventory
- ✅ View sales and revenue

### For Buyers
- ✅ Browse available books
- ✅ Shopping cart
- ✅ Purchase books
- ✅ Order history

### System
- ✅ JWT authentication
- ✅ Microservices architecture
- ✅ Service discovery
- ✅ API Gateway
- ✅ Docker containerization
- ✅ Kubernetes orchestration
- ✅ CI/CD with GitHub Actions

## 📊 Project Structure

```
BookManagement/
├── .github/workflows/     # GitHub Actions CI/CD
├── k8s/                   # Kubernetes manifests
├── eureka-server/         # Service discovery
├── user-service/          # User management
├── book-service/          # Book inventory
├── order-service/         # Order processing
├── api-gateway/           # API Gateway
├── frontend/              # React UI
├── docker-compose.yml     # Local Docker setup
└── Documentation files
```

## 📚 Documentation

| File | Description |
|------|-------------|
| [SETUP_GUIDE.md](SETUP_GUIDE.md) | Local development setup |
| [DOCKER_TESTING.md](DOCKER_TESTING.md) | Docker Compose testing |
| [GCP_DEPLOYMENT.md](GCP_DEPLOYMENT.md) | Google Cloud deployment |
| [CICD_SETUP.md](CICD_SETUP.md) | CI/CD configuration |
| [DEPLOYMENT_SUMMARY.md](DEPLOYMENT_SUMMARY.md) | Complete overview |
| [QUICK_REFERENCE.md](QUICK_REFERENCE.md) | API endpoints & commands |
| [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) | Architecture details |
| [TESTING_CHECKLIST.md](TESTING_CHECKLIST.md) | Testing guide |

## 🔧 Technology Stack

**Backend:**
- Spring Boot 3.2
- Spring Cloud (Eureka, Gateway)
- Spring Data JPA
- JWT Authentication
- H2 Database (dev) / Cloud SQL (prod)

**Frontend:**
- React 18
- Axios
- CSS3

**DevOps:**
- Docker
- Kubernetes
- Google Kubernetes Engine (GKE)
- GitHub Actions
- Google Container Registry

## 💰 Cost Estimate (GCP)

- **GKE Cluster**: ~$220/month (3 nodes)
- **Load Balancer**: ~$18/month
- **Container Registry**: ~$1/month
- **Total**: ~$240-300/month

**Optimization:** Use preemptible nodes or GKE Autopilot to reduce costs.

## 🔐 Security Notes

**Current (Development):**
- H2 in-memory database
- Plain text passwords
- JWT secret in config

**Production Recommendations:**
- Use Cloud SQL
- Implement bcrypt
- Use Secret Manager
- Enable SSL/TLS
- Restrict CORS
- Use private GKE cluster

## 🐛 Troubleshooting

### Docker Issues
```bash
docker-compose logs -f
docker system prune -a
```

### Kubernetes Issues
```bash
kubectl get pods
kubectl logs POD_NAME
kubectl describe pod POD_NAME
```

### GitHub Actions Issues
- Check Actions tab in GitHub
- Verify secrets are set
- Check service account permissions

## 📞 Support

- **Issues**: Create GitHub issue
- **Documentation**: Check guides in repository
- **GCP Support**: https://cloud.google.com/support

## 🎓 Learning Resources

- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [GKE Documentation](https://cloud.google.com/kubernetes-engine/docs)
- [GitHub Actions](https://docs.github.com/en/actions)

## 📝 License

This project is for educational purposes.

## 🙏 Acknowledgments

Built with Spring Boot, React, Docker, and Kubernetes.

---

## 🚀 Ready to Deploy?

1. **Test Locally**: `docker-compose up`
2. **Read Guide**: Choose your deployment path
3. **Deploy**: Follow the guide
4. **Enjoy**: Your app is live!

**Need Help?** Check the documentation files or create an issue.
