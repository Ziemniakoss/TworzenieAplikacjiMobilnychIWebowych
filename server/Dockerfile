FROM ubuntu:16.04

RUN apt-get update --fix-missing
RUN apt-get upgrade -y
RUN apt install redis-server -y
RUN apt-get install openjdk-8-jdk -y


COPY .mvn .mvn
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw
COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh
RUN ./mvnw package

EXPOSE 42069

ENTRYPOINT ["/docker-entrypoint.sh"]
