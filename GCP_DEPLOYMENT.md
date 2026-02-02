# GCP Kubernetes Deployment Guide

## Prerequisites

1. **Google Cloud Account** with billing enabled
2. **gcloud CLI** installed
3. **kubectl** installed
4. **Docker** installed
5. **GitHub repository** for your code

## Step 1: Set Up GCP Project

```bash
# Login to GCP
gcloud auth login

# Create new project (or use existing)
gcloud projects create book-management-prod --name="Book Management"

# Set project
gcloud config set project book-management-prod

# Enable required APIs
gcloud services enable container.googleapis.com
gcloud services enable containerregistry.googleapis.com
gcloud services enable cloudbuild.googleapis.com
```

## Step 2: Create GKE Cluster

```bash
# Create GKE cluster
gcloud container clusters create book-management-cluster \
  --zone us-central1-a \
  --num-nodes 3 \
  --machine-type e2-medium \
  --enable-autoscaling \
  --min-nodes 2 \
  --max-nodes 5

# Get credentials
gcloud container clusters get-credentials book-management-cluster --zone us-central1-a

# Verify connection
kubectl get nodes
```

## Step 3: Set Up Service Account for GitHub Actions

```bash
# Create service account
gcloud iam service-accounts create github-actions \
  --display-name="GitHub Actions"

# Grant permissions
gcloud projects add-iam-policy-binding book-management-prod \
  --member="serviceAccount:github-actions@book-management-prod.iam.gserviceaccount.com" \
  --role="roles/container.developer"

gcloud projects add-iam-policy-binding book-management-prod \
  --member="serviceAccount:github-actions@book-management-prod.iam.gserviceaccount.com" \
  --role="roles/storage.admin"

# Create and download key
gcloud iam service-accounts keys create key.json \
  --iam-account=github-actions@book-management-prod.iam.gserviceaccount.com

# Display key (copy this for GitHub secrets)
cat key.json
```

## Step 4: Configure GitHub Secrets

Go to your GitHub repository → Settings → Secrets and variables → Actions

Add these secrets:

1. **GCP_PROJECT_ID**: `book-management-prod`
2. **GCP_SA_KEY**: Paste the entire content of `key.json`

## Step 5: Update Kubernetes Manifests

Replace `PROJECT_ID` in all k8s/*.yaml files with your actual project ID:

```bash
# On Linux/Mac
sed -i 's/PROJECT_ID/book-management-prod/g' k8s/*.yaml

# On Windows (PowerShell)
Get-ChildItem k8s/*.yaml | ForEach-Object {
    (Get-Content $_) -replace 'PROJECT_ID', 'book-management-prod' | Set-Content $_
}
```

## Step 6: Manual Deployment (First Time)

```bash
# Configure Docker for GCR
gcloud auth configure-docker

# Build and push images
docker build -t gcr.io/book-management-prod/eureka-server:latest ./eureka-server
docker push gcr.io/book-management-prod/eureka-server:latest

docker build -t gcr.io/book-management-prod/user-service:latest ./user-service
docker push gcr.io/book-management-prod/user-service:latest

docker build -t gcr.io/book-management-prod/book-service:latest ./book-service
docker push gcr.io/book-management-prod/book-service:latest

docker build -t gcr.io/book-management-prod/order-service:latest ./order-service
docker push gcr.io/book-management-prod/order-service:latest

docker build -t gcr.io/book-management-prod/api-gateway:latest ./api-gateway
docker push gcr.io/book-management-prod/api-gateway:latest

docker build -t gcr.io/book-management-prod/frontend:latest ./frontend
docker push gcr.io/book-management-prod/frontend:latest

# Deploy to Kubernetes
kubectl apply -f k8s/eureka-server.yaml
sleep 30
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/book-service.yaml
kubectl apply -f k8s/order-service.yaml
sleep 20
kubectl apply -f k8s/api-gateway.yaml
sleep 10
kubectl apply -f k8s/frontend.yaml
```

## Step 7: Get Application URL

```bash
# Wait for LoadBalancer to get external IP
kubectl get service frontend -w

# Once you see EXTERNAL-IP, access your app at:
# http://<EXTERNAL-IP>
```

## Step 8: Set Up CI/CD

1. Push your code to GitHub
2. GitHub Actions will automatically:
   - Build Docker images
   - Push to Google Container Registry
   - Deploy to GKE
   - Update running pods

## Useful Commands

### Check Deployment Status
```bash
kubectl get deployments
kubectl get pods
kubectl get services
```

### View Logs
```bash
kubectl logs -f deployment/user-service
kubectl logs -f deployment/book-service
kubectl logs -f deployment/order-service
kubectl logs -f deployment/api-gateway
kubectl logs -f deployment/frontend
```

### Scale Services
```bash
kubectl scale deployment user-service --replicas=3
kubectl scale deployment book-service --replicas=3
```

### Update Deployment
```bash
kubectl rollout restart deployment/user-service
kubectl rollout status deployment/user-service
```

### Delete Everything
```bash
kubectl delete -f k8s/
```

### Delete Cluster
```bash
gcloud container clusters delete book-management-cluster --zone us-central1-a
```

## Monitoring

### View Eureka Dashboard
```bash
kubectl port-forward service/eureka-server 8761:8761
# Access: http://localhost:8761
```

### View Application Logs
```bash
# Stream logs from all pods
kubectl logs -f -l app=user-service
```

## Cost Optimization

1. **Use Preemptible Nodes** (cheaper but can be terminated):
```bash
gcloud container clusters create book-management-cluster \
  --preemptible \
  --zone us-central1-a
```

2. **Use Autopilot** (managed, pay-per-pod):
```bash
gcloud container clusters create-auto book-management-cluster \
  --region us-central1
```

3. **Set Resource Limits** in k8s manifests:
```yaml
resources:
  requests:
    memory: "256Mi"
    cpu: "250m"
  limits:
    memory: "512Mi"
    cpu: "500m"
```

## Troubleshooting

### Pods not starting
```bash
kubectl describe pod <pod-name>
kubectl logs <pod-name>
```

### Service not accessible
```bash
kubectl get endpoints
kubectl describe service <service-name>
```

### Image pull errors
```bash
# Verify GCR access
gcloud auth configure-docker
docker pull gcr.io/book-management-prod/user-service:latest
```

## Production Checklist

- [ ] Use Cloud SQL instead of H2
- [ ] Set up Cloud Monitoring
- [ ] Configure Cloud Logging
- [ ] Set up SSL/TLS certificates
- [ ] Configure Cloud Armor (DDoS protection)
- [ ] Set up backup strategy
- [ ] Configure alerts
- [ ] Set resource limits
- [ ] Enable pod autoscaling
- [ ] Set up staging environment
- [ ] Configure secrets management (Secret Manager)

## Estimated Costs

**GKE Cluster (3 e2-medium nodes):**
- ~$73/month per node
- Total: ~$219/month

**Load Balancer:**
- ~$18/month

**Container Registry:**
- ~$0.10/GB/month

**Total Estimated:** ~$240-300/month

Use GCP Pricing Calculator for accurate estimates: https://cloud.google.com/products/calculator
