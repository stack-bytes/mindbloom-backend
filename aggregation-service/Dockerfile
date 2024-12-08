FROM openjdk:21-jdk-slim
LABEL author="jarvx & crares"


WORKDIR /app


COPY target/aggregation-service-0.0.1-SNAPSHOT.jar  /app/aggregation-service.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/aggregation-service.jar"]