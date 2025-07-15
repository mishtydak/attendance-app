# Stage 1: Build the application using Maven
# We use an official Maven image that has Java and Maven pre-installed.
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the entire project context
COPY . .

# ***************************************************************
# *** THIS IS THE FINAL FIX ***
# *** We are now using 'mvn' (the system-wide Maven) instead of './mvnw' (the local wrapper)
# *** This bypasses the need for the missing .mvn folder.
# ***************************************************************
RUN mvn clean install -DskipTests

# Stage 2: Create the final, lightweight production image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy only the built .jar file from the build stage
COPY --from=build /app/target/attendance-app-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]