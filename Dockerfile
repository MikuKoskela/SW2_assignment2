# Use lightweight Java runtime
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built jar
COPY target/*.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]