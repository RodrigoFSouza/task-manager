FROM gradle:7.6.0-jdk17-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle* ./
RUN gradle build --no-daemon --parallel --stacktrace || return 0

COPY . .

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080