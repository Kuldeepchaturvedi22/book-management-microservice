# CI/CD Setup Guide with GitHub Actions

Complete guide to set up automated deployment to GCP using GitHub Actions.

## Overview

**Workflow:**
```
Code Push → GitHub → GitHub Actions → Build Docker Images → Push to GCR → Deploy to GKE
```

## Prerequisites

- [x] GitHub account
- [x] GCP account with billing enabled
- [x] gcloud CLI installed
- [x] kubectl installed

## Step-by-Step Setup

### 1. Create GitHub Repository

```bash
# Initialize git (if not already done)
cd BookManagement
git init

# Create .gitignore
echo "target/
node_modules/
.env
*.log
.DS_Store
key.json" > .gitignore

# Add all files
git add .
git commit -m "Initial commit with Docker and K8s setup"

# Create repository on GitHub (via web interface)
# Then push:
git remote add origin https://github.com/YOUR_USERNAME/book-management.git
git branch -M main
git push -u origin main
```

### 2. Set Up GCP Project

```bash
# Login to GCP
gcloud auth login

# Create project
export PROJECT_ID="book-management-prod"
gcloud projects create $PROJECT_ID --name="Book Management"

# Set as default project
gcloud config set project $PROJECT_ID

# Enable billing (must be done via console)
# Go to: https://console.cloud.google.com/billing

# Enable required APIs
gcloud services enable container.googleapis.com
gcloud services enable containerregistry.googleapis.com
gcloud services enable cloudbuild.googleapis.com
gcloud services enable compute.googleapis.com
```

### 3. Create GKE Cluster

```bash
# Create cluster
gcloud container clusters create book-management-cluster \
  --zone us-central1-a \
  --num-nodes 3 \
  --machine-type e2-medium \
  --enable-autoscaling \
  --min-nodes 2 \
  --max-nodes 5 \
  --disk-size 20 \
  --disk-type pd-standard

# This takes 5-10 minutes...

# Get credentials
gcloud container clusters get-credentials book-management-cluster \
  --zone us-central1-a

# Verify
kubectl get nodes
```

### 4. Create Service Account for GitHub Actions

```bash
# Create service account
gcloud iam service-accounts create github-actions \
  --display-name="GitHub Actions CI/CD"

# Get service account email
export SA_EMAIL="github-actions@${PROJECT_ID}.iam.gserviceaccount.com"

# Grant necessary permissions
gcloud projects add-iam-policy-binding $PROJECT_ID \
  --member="serviceAccount:${SA_EMAIL}" \
  --role="roles/container.developer"

gcloud projects add-iam-policy-binding $PROJECT_ID \
  --member="serviceAccount:${SA_EMAIL}" \
  --role="roles/storage.admin"

gcloud projects add-iam-policy-binding $PROJECT_ID \
  --member="serviceAccount:${SA_EMAIL}" \
  --role="roles/container.clusterAdmin"

# Create and download key
gcloud iam service-accounts keys create github-actions-key.json \
  --iam-account=$SA_EMAIL

# Display key content (you'll need this for GitHub)
cat github-actions-key.json
```

### 5. Configure GitHub Secrets

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**

Add these secrets:

| Secret Name | Value |
|-------------|-------|
| `GCP_PROJECT_ID` | Your project ID (e.g., `book-management-prod`) |
| `GCP_SA_KEY` | Entire content of `github-actions-key.json` file |

**Important:** Copy the ENTIRE JSON content including `{` and `}`

### 6. Update Kubernetes Manifests

Replace `PROJECT_ID` with your actual project ID in all k8s/*.yaml files:

**Windows PowerShell:**
```powershell
$projectId = "book-management-prod"
Get-ChildItem k8s\*.yaml | ForEach-Object {
    (Get-Content $_) -replace 'PROJECT_ID', $projectId | Set-Content $_
}
```

**Linux/Mac:**
```bash
export PROJECT_ID="book-management-prod"
sed -i "s/PROJECT_ID/$PROJECT_ID/g" k8s/*.yaml
```

### 7. Test GitHub Actions Workflow

```bash
# Commit and push changes
git add .
git commit -m "Add CI/CD configuration"
git push origin main
```

**Monitor the workflow:**
1. Go to your GitHub repository
2. Click **Actions** tab
3. Watch the workflow run

### 8. Get Application URL

Once deployment completes:

```bash
# Wait for LoadBalancer to get external IP
kubectl get service frontend -w

# Or get it directly
kubectl get service frontend -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

Access your application at: `http://<EXTERNAL-IP>`

## Workflow Explanation

### What Happens on Each Push:

1. **Checkout Code** - Downloads your repository
2. **Set up JDK 17** - Installs Java for building Spring Boot apps
3. **Authenticate to GCP** - Uses service account key
4. **Configure Docker** - Sets up Docker to push to GCR
5. **Build Images** - Builds Docker images for all services
6. **Push to GCR** - Pushes images to Google Container Registry
7. **Deploy to GKE** - Updates Kubernetes deployments
8. **Rolling Update** - Gradually replaces old pods with new ones

### Deployment Order:

```
1. Eureka Server (wait 30s)
2. User, Book, Order Services (parallel, wait 20s)
3. API Gateway (wait 10s)
4. Frontend
```

## Customizing the Workflow

### Change Deployment Trigger

Edit `.github/workflows/deploy.yml`:

```yaml
# Deploy only on main branch
on:
  push:
    branches: [ main ]

# Deploy on tags
on:
  push:
    tags:
      - 'v*'

# Manual trigger
on:
  workflow_dispatch:
```

### Add Testing Stage

```yaml
- name: Run Tests
  run: |
    cd user-service
    mvn test
    cd ../book-service
    mvn test
```

### Add Staging Environment

```yaml
- name: Deploy to Staging
  if: github.ref == 'refs/heads/develop'
  run: |
    kubectl apply -f k8s/ --namespace=staging
```

## Monitoring Deployments

### View Workflow Logs
- GitHub → Actions → Click on workflow run

### View Kubernetes Logs
```bash
# All pods
kubectl get pods

# Specific service logs
kubectl logs -f deployment/user-service

# Previous deployment logs
kubectl logs -f deployment/user-service --previous
```

### Check Deployment Status
```bash
kubectl get deployments
kubectl rollout status deployment/user-service
kubectl rollout history deployment/user-service
```

## Rollback

### Via kubectl
```bash
# Rollback to previous version
kubectl rollout undo deployment/user-service

# Rollback to specific revision
kubectl rollout undo deployment/user-service --to-revision=2
```

### Via GitHub Actions
1. Revert the commit
2. Push to trigger new deployment

## Troubleshooting

### Workflow Fails at Authentication
- Verify `GCP_SA_KEY` secret is correct JSON
- Check service account has required permissions

### Image Push Fails
- Verify Container Registry API is enabled
- Check service account has `storage.admin` role

### Deployment Fails
- Check pod logs: `kubectl logs <pod-name>`
- Describe pod: `kubectl describe pod <pod-name>`
- Verify images exist in GCR

### Services Not Connecting
- Check Eureka dashboard: `kubectl port-forward service/eureka-server 8761:8761`
- Verify service names match in k8s manifests
- Check network policies

## Best Practices

### 1. Use Separate Environments
```bash
# Create namespaces
kubectl create namespace staging
kubectl create namespace production

# Deploy to specific namespace
kubectl apply -f k8s/ --namespace=staging
```

### 2. Use Image Tags
```yaml
# In workflow
docker build -t gcr.io/$PROJECT_ID/user-service:$GITHUB_SHA
docker build -t gcr.io/$PROJECT_ID/user-service:latest
```

### 3. Add Health Checks
```yaml
# In k8s manifests
livenessProbe:
  httpGet:
    path: /actuator/health
    port: 8082
  initialDelaySeconds: 60
  periodSeconds: 10
```

### 4. Set Resource Limits
```yaml
resources:
  requests:
    memory: "256Mi"
    cpu: "250m"
  limits:
    memory: "512Mi"
    cpu: "500m"
```

### 5. Use Secrets for Sensitive Data
```bash
# Create secret
kubectl create secret generic jwt-secret \
  --from-literal=secret=your-secret-key

# Use in deployment
env:
- name: JWT_SECRET
  valueFrom:
    secretKeyRef:
      name: jwt-secret
      key: secret
```

## Cost Optimization

### 1. Use Preemptible Nodes
```bash
gcloud container node-pools create preemptible-pool \
  --cluster=book-management-cluster \
  --zone=us-central1-a \
  --preemptible \
  --num-nodes=2
```

### 2. Enable Cluster Autoscaling
```bash
gcloud container clusters update book-management-cluster \
  --enable-autoscaling \
  --min-nodes=1 \
  --max-nodes=5 \
  --zone=us-central1-a
```

### 3. Set Pod Autoscaling
```bash
kubectl autoscale deployment user-service \
  --cpu-percent=70 \
  --min=2 \
  --max=10
```

## Security Checklist

- [ ] Use private GKE cluster
- [ ] Enable network policies
- [ ] Use Workload Identity
- [ ] Scan images for vulnerabilities
- [ ] Use Secret Manager for secrets
- [ ] Enable audit logging
- [ ] Set up Cloud Armor
- [ ] Use SSL/TLS certificates
- [ ] Implement RBAC
- [ ] Regular security updates

## Next Steps

1. ✅ Set up monitoring (Cloud Monitoring)
2. ✅ Configure alerts
3. ✅ Set up Cloud SQL for production database
4. ✅ Add SSL certificate
5. ✅ Configure custom domain
6. ✅ Set up backup strategy
7. ✅ Implement blue-green deployment
8. ✅ Add integration tests to CI/CD
9. ✅ Set up staging environment
10. ✅ Configure log aggregation

## Support

- **GCP Documentation**: https://cloud.google.com/kubernetes-engine/docs
- **GitHub Actions**: https://docs.github.com/en/actions
- **kubectl Reference**: https://kubernetes.io/docs/reference/kubectl/

## Cleanup

### Delete Everything
```bash
# Delete cluster
gcloud container clusters delete book-management-cluster --zone us-central1-a

# Delete images
gcloud container images list
gcloud container images delete gcr.io/$PROJECT_ID/user-service:latest

# Delete service account
gcloud iam service-accounts delete $SA_EMAIL

# Delete project (careful!)
gcloud projects delete $PROJECT_ID
```
