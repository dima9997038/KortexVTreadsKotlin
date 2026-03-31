FROM eclipse-temurin:17-jdk AS builder

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

FROM eclipse-temurin:17-jre

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8761

HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=5 \
  CMD curl -f http://localhost:8761/ || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]