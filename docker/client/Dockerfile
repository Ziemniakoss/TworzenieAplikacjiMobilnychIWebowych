FROM ubuntu:18.04

RUN apt-get update  && apt-get upgrade -y
RUN apt-get install openjdk-8-jre -y

COPY client.jar client.jar
COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
