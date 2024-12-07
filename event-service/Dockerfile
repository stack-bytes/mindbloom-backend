
FROM openjdk:21-jdk-slim
LABEL author="jarvx & crares"


WORKDIR /app


COPY target/event-service-0.0.1-SNAPSHOT.jar  /app/event-service.jar

EXPOSE 8082

CMD ["java", "-jar", "/app/event-service.jar"]