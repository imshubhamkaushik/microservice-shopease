# multi-stage
# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the Spring Boot fat JAR (skip tests for faster build)
RUN mvn -B clean package -DskipTests

# ---------- Runtime Stage ----------
FROM gcr.io/distroless/java17:nonroot
WORKDIR /app

# Copy only the JAR from build stage
COPY --from=build /app/target/user-service-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]
