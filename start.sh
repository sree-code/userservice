#!/bin/bash
# Railway build script for Spring Boot
mvn clean package -DskipTests
java -jar target/userService-0.0.1-SNAPSHOT.jar
