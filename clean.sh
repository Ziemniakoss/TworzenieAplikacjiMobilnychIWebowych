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

echo Stopowanie > /dev/null
$SD network rm $NETWORK_NAME > /dev/null
$SD stop $SERV_DOCKER_NAME > /dev/null
$SD stop $CLI_DOCKER_NAME > /dev/null
$SD rm -f $SERV_DOCKER_NAME > /dev/null
$SD rm -f $CLI_DOCKER_NAME > /dev/null
$SD image rm -f $SERV_IMG_NAME > /dev/null
$SD image rm -f $CLI_IMG_NAME > /dev/null
