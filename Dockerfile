# Stage 1: Build Java 21 application using Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build production jar skipping tests (tests run in CI pipeline)
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment using JDK/JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy compiled jar and runner script
COPY --from=builder /app/target/credit-simulator.jar /app/target/credit-simulator.jar
COPY credit_simulator /app/credit_simulator
COPY file_inputs.txt /app/file_inputs.txt

# Ensure executable permissions for runner script
RUN chmod +x /app/credit_simulator

# Entrypoint to run credit_simulator
ENTRYPOINT ["/app/credit_simulator"]
