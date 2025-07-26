#!/bin/bash

echo "Building and deploying User Service to Firebase..."

# Build the Maven project
echo "Building Maven project..."
./mvnw clean package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Maven build successful!"
    
    # Install Node.js dependencies
    echo "Installing Firebase dependencies..."
    npm install
    
    # Deploy to Firebase
    echo "Deploying to Firebase..."
    firebase deploy
    
    echo "Deployment completed!"
else
    echo "Maven build failed. Please check the errors above."
    exit 1
fi
