FROM ubuntu:18.04

RUN apt-get update --fix-missing
RUN apt-get upgrade -y
RUN apt-get install openjdk-8-jdk -y


# Budowanie aplikacji
COPY .mvn .mvn
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw
RUN ./mvnw package
#Czyszczenie zbędnych plików
RUN mv target/client-0.0.1-SNAPSHOT.jar client.jar
RUN rm -rf target
RUN rm -rf src
RUN rm -rf .mvn mvnw
RUN rm pom.xml

COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
