
FROM openjdk:21-jdk-slim
LABEL author="jarvx & crares"


WORKDIR /app


COPY target/image-service-0.0.1-SNAPSHOT.jar  /app/image-service.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/image-service.jar"]