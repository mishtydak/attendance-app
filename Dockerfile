# Stage 1: Build the application using Maven
# We use an official Maven image that has Java and Maven pre-installed.
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to leverage Docker's layer caching
COPY pom.xml .

# --- THIS IS THE CORRECTED SECTION ---
# Copy the Maven wrapper scripts
COPY mvnw ./
COPY mvnw.cmd ./
# The line "COPY .mvn/ .mvn" has been removed because the folder does not exist.
# ------------------------------------

# Download all dependencies. This is cached if pom.xml doesn't change.
RUN ./mvnw dependency:go-offline

# Copy the rest of your source code
COPY src ./src

# Build the application, creating the .jar file. Skip tests for faster builds.
RUN ./mvnw clean install -DskipTests


# Stage 2: Create the final, lightweight production image
# We use an official OpenJDK image that only has Java, making it smaller and more secure.
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the .jar file that was built in the 'build' stage
COPY --from=build /app/target/attendance-app-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port that the application will run on.
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]