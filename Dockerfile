FROM openjdk:21
RUN mkdir -p deploy
WORKDIR /deploy
COPY ./build/libs/WeER_backend-0.0.1-SNAPSHOT.jar api.jar
ENTRYPOINT ["java","-jar","/deploy/api.jar"]