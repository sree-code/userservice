@echo off

echo Building the application...
call mvn clean package -DskipTests

echo Building Docker image...
docker build -t wash-app-userservice .

echo Tagging image for Google Container Registry...
docker tag wash-app-userservice gcr.io/%GOOGLE_CLOUD_PROJECT%/wash-app-userservice:latest

echo Pushing to Google Container Registry...
docker push gcr.io/%GOOGLE_CLOUD_PROJECT%/wash-app-userservice:latest

echo Deploying new userservice to Google Cloud Run...
gcloud run deploy userservice-api ^
  --image gcr.io/%GOOGLE_CLOUD_PROJECT%/wash-app-userservice:latest ^
  --platform managed ^
  --region us-central1 ^
  --allow-unauthenticated ^
  --port 8080 ^
  --memory 512Mi ^
  --cpu 1 ^
  --max-instances 10 ^
  --set-env-vars="FIREBASE_PROJECT_ID=%GOOGLE_CLOUD_PROJECT%" ^
  --set-env-vars="FIREBASE_CREDENTIALS_JSON=%FIREBASE_CREDENTIALS_JSON%" ^
  --service-account=%SERVICE_ACCOUNT_EMAIL%

echo New userservice deployed successfully!
gcloud run services describe userservice-api --region=us-central1 --format="value(status.url)"
pause
