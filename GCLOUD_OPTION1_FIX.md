# Google Cloud Run - Fix Current Deployment

## 🎯 Step-by-Step Fix for Your Current Service

### 1. Access Google Cloud Console
Go to: https://console.cloud.google.com/run/detail/us-central1/seemee-backend

### 2. Edit Your Service
Click **"EDIT & DEPLOY NEW REVISION"**

### 3. Configure Container Settings
- **Container port**: `8080`
- **Memory**: `1 GiB` (minimum)
- **CPU**: `1 vCPU`
- **Request timeout**: `300 seconds`
- **Maximum requests per container**: `80`

### 4. Set Environment Variables
Add these exactly:
```
SPRING_PROFILES_ACTIVE = prod
PORT = 8080
MONGODB_URI = mongodb+srv://krishk:uh3RIF6cWPWLvFiA@cluster0.szsml.mongodb.net/SeeMee?retryWrites=true&w=majority
EMAIL_USERNAME = sivakreddy.k7@gmail.com
EMAIL_PASSWORD = eerjbdyoxsltzmqc
```

### 5. Security Settings
- **Authentication**: Allow unauthenticated invocations
- **Ingress**: All traffic

### 6. Deploy
Click **"DEPLOY"** and wait for completion

### 7. Test Endpoints
After deployment, test:
- https://seemee-backend-815537417392.us-central1.run.app/health
- https://seemee-backend-815537417392.us-central1.run.app/api/user/login (POST)

## 🔍 Common Issues & Solutions

**Issue 1: Still getting 500 errors?**
- Check Cloud Run logs: Go to "LOGS" tab in your service
- Look for startup errors or MongoDB connection issues

**Issue 2: Cold start timeouts?**
- Increase CPU allocation to 2 vCPU
- Set minimum instances to 1 (this costs more but eliminates cold starts)

**Issue 3: MongoDB connection fails?**
- Verify the MongoDB URI is correct
- Check if your MongoDB cluster allows connections from Google Cloud IPs
