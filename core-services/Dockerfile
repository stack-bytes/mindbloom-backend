
FROM openjdk:21-jdk-slim
LABEL author="jarvx & crares"


WORKDIR /app


COPY target/core-services-0.0.1-SNAPSHOT.jar  /app/core-services.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/core-services.jar"]