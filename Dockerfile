# Use a lightweight OpenJDK image
FROM openjdk:21-slim

# Set the working directory
WORKDIR /app

# Copy JAR file, but fail gracefully if it doesn’t exist
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]