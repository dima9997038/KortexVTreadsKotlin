# ==================== Build Stage ====================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

# Fix line endings + make gradlew executable
RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix gradlew && \
    chmod +x gradlew && \
    rm -rf /var/lib/apt/lists/*

# Download dependencies (Gradle will now auto-download Java 17 toolchain if needed)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the jar
RUN ./gradlew bootJar --no-daemon -x test

# ==================== Runtime Stage ====================
FROM eclipse-temurin:17-jre

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8888

# Health check for Config Server
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=5 \
  CMD curl -f http://localhost:8888/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]