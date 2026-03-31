FROM openjdk:25-ea-jdk-slim AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew && sed -i 's/\r$//' gradlew   # fix Windows line endings

COPY build.gradle.kts settings.gradle.kts .
COPY src src

RUN ./gradlew bootJar

FROM openjdk:25-ea-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]