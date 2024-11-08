FROM openjdk:21

RUN mkdir -p deploy
WORKDIR /deploy

COPY build/libs/WeER_backend-0.0.1-SNAPSHOT.jar WeER_backend.jar

ENTRYPOINT ["java","-jar","/deploy/WeER_backend.jar"]