
# Build project and generate .jar file with maven
FROM maven:3.9.7-eclipse-temurin-22-alpine AS build

COPY . .


RUN mvn clean package


# Run project


FROM openjdk:21-slim


WORKDIR /app


COPY --from=build target/connected-minds-api-0.0.1-SNAPSHOT.jar /app/application.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Djava.security.egd=file:/dev/./urandom", "application.jar"]