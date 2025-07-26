# Cloud Deployment Options for Global Access

## 🚀 Option 1: Render (Free Tier Available)
Your app already has render.yaml configured!

### Steps:
1. Push code to GitHub
2. Connect Render to your GitHub repo
3. Deploy automatically
4. Get public URL: https://your-app-name.onrender.com

### Render Configuration (render.yaml):
```yaml
services:
  - type: web
    name: userservice
    env: java
    plan: free
    buildCommand: ./mvnw clean install -DskipTests
    startCommand: java -Dserver.port=$PORT -jar target/userService-0.0.1-SNAPSHOT.jar
    envVars:
      - key: PORT
        value: 10000
      - key: SPRING_PROFILES_ACTIVE
        value: prod
```

## 🚀 Option 2: Heroku
### Steps:
1. Create Heroku account
2. Install Heroku CLI
3. Create Procfile
4. Deploy with git
5. Get public URL: https://your-app.herokuapp.com

## 🚀 Option 3: Railway
### Steps:
1. Connect GitHub to Railway
2. Auto-deploy from your repo
3. Get public URL: https://your-app.up.railway.app

## 🚀 Option 4: AWS EC2 (More Control)
### Steps:
1. Launch EC2 instance
2. Install Java and Maven
3. Deploy your JAR file
4. Configure security groups
5. Get public IP: http://your-ec2-ip:8080

## 🚀 Option 5: Google Cloud Platform
### Steps:
1. Create GCP project
2. Use App Engine or Compute Engine
3. Deploy Spring Boot app
4. Get public URL

## 🚀 Option 6: Azure App Service
### Steps:
1. Create Azure account
2. Create App Service
3. Deploy Spring Boot app
4. Get public URL

## 💰 Cost Comparison:
- **Render Free Tier**: 0$ (Limited hours)
- **Heroku Free Tier**: 0$ (Discontinued, paid plans start ~$7/month)
- **Railway**: Free tier available
- **AWS EC2**: ~$5-10/month (t3.micro)
- **GCP/Azure**: Similar pricing to AWS

## 🎯 Recommended: Render
Your project already has render.yaml configured, making Render the easiest option!
