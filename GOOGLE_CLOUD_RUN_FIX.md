# Google Cloud Run Deployment - Troubleshooting & Fix

## 🔍 Current Issue
Your service is deployed at: `https://seemee-backend-815537417392.us-central1.run.app`
But it's returning "Server Error" - likely due to configuration or startup issues.

## 🛠️ Troubleshooting Steps

### 1. Check Cloud Run Service Status
Go to [Google Cloud Console](https://console.cloud.google.com/run) and check:
- Service status and logs
- Environment variables
- Memory and CPU allocation

### 2. Common Issues & Solutions

**Issue 1: Environment Variables Missing**
Ensure these environment variables are set in Cloud Run:
```
SPRING_PROFILES_ACTIVE=prod
PORT=8080
MONGODB_URI=mongodb+srv://krishk:uh3RIF6cWPWLvFiA@cluster0.szsml.mongodb.net/SeeMee?retryWrites=true&w=majority
EMAIL_USERNAME=sivakreddy.k7@gmail.com
EMAIL_PASSWORD=eerjbdyoxsltzmqc
```

**Issue 2: Memory Allocation**
- Increase memory to at least 1GB
- Set CPU to 1 vCPU minimum

**Issue 3: Container Port**
- Ensure the container port is set to 8080
- Check that your application.properties has `server.port=8080`

### 3. Quick Redeploy Commands

```bash
# Build the application
mvn clean package -DskipTests

# Deploy to Cloud Run (if you have gcloud CLI)
gcloud run deploy seemee-backend \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 1Gi \
  --cpu 1 \
  --port 8080 \
  --set-env-vars SPRING_PROFILES_ACTIVE=prod \
  --set-env-vars MONGODB_URI="mongodb+srv://krishk:uh3RIF6cWPWLvFiA@cluster0.szsml.mongodb.net/SeeMee?retryWrites=true&w=majority" \
  --set-env-vars EMAIL_USERNAME="sivakreddy.k7@gmail.com" \
  --set-env-vars EMAIL_PASSWORD="eerjbdyoxsltzmqc"
```

## 🔄 Alternative: Fresh Deployment

If the current deployment continues to have issues, try these alternatives:

### Option A: Railway (Recommended for quick fix)
1. Go to https://railway.app
2. Connect your GitHub repo
3. Deploy automatically with zero config

### Option B: Render
1. Go to https://render.com
2. Connect GitHub and deploy
3. Free tier available

## 🌐 Frontend Configuration

Once your backend is working, update your frontend to use:
- **Backend URL**: `https://seemee-backend-815537417392.us-central1.run.app`
- **Login endpoint**: `https://seemee-backend-815537417392.us-central1.run.app/api/user/login`

## 🧪 Test Endpoints

Once fixed, test these endpoints:
- Health: `GET /health`
- Login: `POST /api/user/login`
- Register: `POST /api/user/createProfile`
- Profile: `GET /api/user/getProfile/{email}`

## 📝 Current Status
- ❌ Backend: Not responding (needs fixing)
- ✅ Frontend: Working at https://seemee-759f4.web.app
- ✅ Firebase Hosting: Documentation deployed

**Next Step**: Fix the Cloud Run deployment or deploy to Railway for a quick solution.
