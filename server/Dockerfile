FROM ubuntu:18.04

RUN apt-get update --fix-missing
RUN apt-get upgrade -y
RUN apt install redis-server -y
RUN apt-get install openjdk-8-jdk -y

#Budowanie aplikacji
COPY .mvn .mvn
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw
RUN ./mvnw package
#Czyszczenie zbędnych plików
RUN mv target/server-0.0.1-SNAPSHOT.jar server.jar
RUN rm -rf target
RUN rm -rf src
RUN rm -rf .mvn mvnw
RUN rm pom.xml

RUN mkdir webServerFiles
COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh

EXPOSE 42069

ENTRYPOINT ["/docker-entrypoint.sh"]
