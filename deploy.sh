#!/bin/bash

echo "Building the application..."
mvn clean package -DskipTests

echo "Building Docker image..."
docker build -t wash-app-userservice .

echo "Tagging image for Google Container Registry..."
docker tag wash-app-userservice gcr.io/$GOOGLE_CLOUD_PROJECT/wash-app-userservice

echo "Pushing to Google Container Registry..."
docker push gcr.io/$GOOGLE_CLOUD_PROJECT/wash-app-userservice

echo "Deploying to Google Cloud Run..."
gcloud run deploy wash-app-userservice \
  --image gcr.io/$GOOGLE_CLOUD_PROJECT/wash-app-userservice \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars="FIREBASE_PROJECT_ID=$GOOGLE_CLOUD_PROJECT" \
  --set-env-vars="FIREBASE_CREDENTIALS_JSON=$FIREBASE_CREDENTIALS_JSON"

echo "Deployment completed!"
