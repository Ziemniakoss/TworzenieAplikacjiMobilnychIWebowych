FROM ubuntu:18.04

RUN apt-get update  && apt-get upgrade -y
RUN apt-get install openjdk-8-jre -y
RUN apt-get install iputils-ping -y


COPY docker-entrypoint.sh docker-entrypoint.sh
RUN chmod 700 docker-entrypoint.sh
COPY client-0.0.1-SNAPSHOT.jar client.jar

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
