FROM ubuntu:16.04

RUN apt-get update  && apt-get upgrade -y
RUN apt install redis-server -y
RUN apt-get install openjdk-8-jre -y

COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh
COPY server-0.0.1-SNAPSHOT.jar server.jar

EXPOSE 42069

ENTRYPOINT ["/docker-entrypoint.sh"]
