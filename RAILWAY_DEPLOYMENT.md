# Railway Deployment Guide

## Quick Deploy to Railway (Free Tier)

1. **Create Railway Account**: Go to https://railway.app and sign up
2. **Connect GitHub**: Link your GitHub account
3. **Deploy from GitHub**: 
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your `userservice` repository
   - Railway will auto-detect it's a Java/Spring Boot app

## Environment Variables to Set in Railway:

```
SPRING_PROFILES_ACTIVE=prod
PORT=8080
MONGODB_URI=mongodb+srv://krishk:uh3RIF6cWPWLvFiA@cluster0.szsml.mongodb.net/SeeMee?retryWrites=true&w=majority
EMAIL_USERNAME=sivakreddy.k7@gmail.com
EMAIL_PASSWORD=eerjbdyoxsltzmqc
```

## Alternative: Render.com

1. Go to https://render.com
2. Connect GitHub
3. Create new "Web Service"
4. Select your repository
5. Use these settings:
   - **Environment**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/userService-0.0.1-SNAPSHOT.jar`

Your API will be available at: `https://your-app-name.railway.app` or `https://your-app-name.onrender.com`
