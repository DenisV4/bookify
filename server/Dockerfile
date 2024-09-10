# Build
FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . /app/
RUN gradle build -x test

# Run
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/server.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app/server.jar"]
