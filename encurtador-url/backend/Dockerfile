FROM eclipse-temurin:21-jdk-alpine

LABEL maintainer="guilhermerjara@hotmail.com"

WORKDIR /app

COPY target/encurtador-url-0.0.1-SNAPSHOT.jar /app/encurtador-url.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "encurtador-url.jar"]