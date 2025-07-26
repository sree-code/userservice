#!/bin/bash
# Railway build and start script for Spring Boot
set -e

echo "Building Spring Boot application..."
./mvnw clean package -DskipTests

echo "Starting Spring Boot application..."
java -jar target/userService-0.0.1-SNAPSHOT.jar
