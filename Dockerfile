# Step 1: Build stage
FROM openjdk:17-jdk-slim as build

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# Step 2: Run stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /app/target/fundmate-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render prefers 8080)
EXPOSE 8080

# Run with custom JVM options
ENTRYPOINT ["java", "-Xms64m", "-Xmx192m", "-Xss256k", "-XX:MaxMetaspaceSize=64m", "-jar", "app.jar"]
