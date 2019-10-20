#!/bin/bash
NETWORK_NAME=ziemniaczana_siec

SERV_IMG_NAME=ziem-serv
SERV_DOCKER_NAME=ziem-serv
SERV_IP=172.18.0.22
SERV_PORT=42069

CLI_IMG_NAME=ziem-cli
CLI_DOCKER_NAME=ziem-cli
CLI_IP=172.18.0.23
CLI_PORT=8080
SD="sudo docker"


# cleaning
echo Stopowanie
$SD network rm $NETWORK_NAME
$SD stop $SERV_DOCKER_NAME
$SD stop $CLI_DOCKER_NAME
$SD rm $SERV_DOCKER_NAME
$SD rm $CLI_DOCKER_NAME

echo tworzenie sieci
set -e
$SD network create --subnet=172.18.0.0/16 $NETWORK_NAME

# build server
$SD build -t=$SERV_IMG_NAME docker/server/

# build cli
$SD build -t=$CLI_IMG_NAME docker/client/