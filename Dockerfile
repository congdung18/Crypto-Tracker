# Build stage
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# Copy maven wrapper and pom.xml first to cache dependencies
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

# Download and cache maven dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code and build the application
COPY src src
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/CryptoTracking-0.0.1-SNAPSHOT.jar app.jar

# Expose server port
EXPOSE 8088

# Run Spring Boot jar
ENTRYPOINT ["java", "-jar", "app.jar"]
