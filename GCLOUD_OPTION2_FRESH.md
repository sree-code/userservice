# Google Cloud Run - Fresh Deployment

## 🆕 Deploy a New Service (Clean Start)

### Prerequisites
1. Install Google Cloud CLI: https://cloud.google.com/sdk/docs/install
2. Authenticate: `gcloud auth login`
3. Set project: `gcloud config set project seemee-backend`

### Method A: Using gcloud CLI (Recommended)

```bash
# 1. Build the project (already done)
mvn clean package -DskipTests

# 2. Deploy to Cloud Run
gcloud run deploy userservice-api \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 1Gi \
  --cpu 1 \
  --port 8080 \
  --min-instances 0 \
  --max-instances 10 \
  --set-env-vars SPRING_PROFILES_ACTIVE=prod \
  --set-env-vars PORT=8080 \
  --set-env-vars MONGODB_URI="mongodb+srv://krishk:uh3RIF6cWPWLvFiA@cluster0.szsml.mongodb.net/SeeMee?retryWrites=true&w=majority" \
  --set-env-vars EMAIL_USERNAME="sivakreddy.k7@gmail.com" \
  --set-env-vars EMAIL_PASSWORD="eerjbdyoxsltzmqc"
```

### Method B: Using Cloud Console

1. **Go to Cloud Run**: https://console.cloud.google.com/run
2. **Create Service**: Click "CREATE SERVICE"
3. **Container Image**: 
   - Select "Source Repository"
   - Connect your GitHub repo: `sree-code/userservice`
   - Branch: `krishreddy`
4. **Service Settings**:
   - Service name: `userservice-api`
   - Region: `us-central1`
   - Authentication: Allow unauthenticated
5. **Container Settings**:
   - Port: `8080`
   - Memory: `1 GiB`
   - CPU: `1 vCPU`
6. **Environment Variables**: (same as Option 1)
7. **Advanced Settings**:
   - Request timeout: `300s`
   - Maximum requests: `80`

### 🎯 Expected Result
New service URL: `https://userservice-api-[hash].us-central1.run.app`

### 🔄 Update Your Constants
After successful deployment, update your backend URL in SeeMeeConstants.java

## ⚡ Quick Commands for PowerShell

```powershell
# Check if gcloud is installed
gcloud version

# Login to Google Cloud
gcloud auth login

# Set your project
gcloud config set project seemee-backend

# Deploy (run from your project directory)
gcloud run deploy userservice-api --source . --platform managed --region us-central1 --allow-unauthenticated --memory 1Gi --cpu 1 --port 8080 --set-env-vars SPRING_PROFILES_ACTIVE=prod,PORT=8080,MONGODB_URI="mongodb+srv://krishk:uh3RIF6cWPWLvFiA@cluster0.szsml.mongodb.net/SeeMee?retryWrites=true&w=majority",EMAIL_USERNAME="sivakreddy.k7@gmail.com",EMAIL_PASSWORD="eerjbdyoxsltzmqc"
```
