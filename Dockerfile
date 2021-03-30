FROM openjdk:8-jre-alpine
EXPOSE 8090

WORKDIR /app

RUN mkdir massa
COPY src/main/resources/massa ./massa

COPY target/teste-gft-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-Dspring.profiles.active=container", "-jar", "teste-gft-0.0.1-SNAPSHOT.jar"]