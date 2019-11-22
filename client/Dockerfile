FROM ubuntu:18.04

RUN apt-get update --fix-missing
RUN apt-get upgrade -y
RUN apt-get install openjdk-8-jdk -y
RUN apt-get install iputils-ping -y


COPY .mvn .mvn
COPY src src
COPY pom.xml pom.xml
COPY mvnw mvnw
COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh
RUN ./mvnw package

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
