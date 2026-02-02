# Direct GCP Deployment (No External Tools)

## Method 1: Google Cloud Shell (Recommended)

1. **Go to Google Cloud Console** (console.cloud.google.com)
2. **Open Cloud Shell** (terminal icon in top bar)
3. **Upload your project**:
   ```bash
   # Create project directory
   mkdir book-management
   cd book-management
   
   # Upload files using Cloud Shell editor or drag & drop
   ```
4. **Deploy**:
   ```bash
   # Enable App Engine
   gcloud app create --region=us-central
   
   # Deploy application
   mvn clean package appengine:deploy
   ```

## Method 2: Cloud Source Repositories

1. **Create Repository**:
   - Go to Cloud Console > Source Repositories
   - Create new repository: "book-management"

2. **Upload Code**:
   - Use web interface to upload files
   - Or clone locally and push

3. **Deploy from Cloud Shell**:
   ```bash
   gcloud source repos clone book-management
   cd book-management
   gcloud app deploy src/main/appengine/app.yaml
   ```

## Method 3: Cloud Build (Web Interface)

1. **Create Build Trigger**:
   - Go to Cloud Build > Triggers
   - Connect repository (GitHub/Bitbucket)
   - Set build configuration

2. **Auto Deploy**:
   - Push code to repository
   - Cloud Build automatically deploys

## Quick Commands for Cloud Shell:

```bash
# Enable services
gcloud services enable appengine.googleapis.com

# Create App Engine app
gcloud app create --region=us-central

# Build and deploy
mvn clean package
gcloud app deploy src/main/appengine/app.yaml

# View logs
gcloud app logs tail -s default

# Open application
gcloud app browse
```

## Access Your App:
- URL: https://PROJECT_ID.appspot.com
- H2 Console: https://PROJECT_ID.appspot.com/h2-console