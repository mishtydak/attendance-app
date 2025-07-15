# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the entire project context
COPY . .

# Build the application directly. This is simpler and more robust.
# It will download dependencies and compile in one step.
RUN ./mvnw clean install -DskipTests

# Stage 2: Create the final, lightweight production image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy only the built .jar file from the build stage
COPY --from=build /app/target/attendance-app-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]