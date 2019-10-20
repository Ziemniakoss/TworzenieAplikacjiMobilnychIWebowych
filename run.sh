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


$SD run --net $NETWORK_NAME --ip $SERV_IP -idt --name=$SERV_DOCKER_NAME $SERV_IMG_NAME
$SD run --net $NETWORK_NAME --ip $CLI_IP -itd -p $CLI_PORT:$CLI_PORT  --name=$CLI_DOCKER_NAME $CLI_IMG_NAME