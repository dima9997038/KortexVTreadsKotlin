
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN apt-get update && apt-get install -y dos2unix && \
    dos2unix gradlew && \
    chmod +x gradlew && \
    rm -rf /var/lib/apt/lists/*

RUN ./gradlew dependencies --no-daemon


COPY src src


RUN ./gradlew bootJar --no-daemon -x test


FROM eclipse-temurin:25-jre

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]