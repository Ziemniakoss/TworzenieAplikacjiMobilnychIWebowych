#!/bin/bash
source docker_config

echo Wyłączanie kontenerów
docker stop  $CLI_DOCKER_NAME
docker stop  $SERV_DOCKER_NAME
echo Kontenery wyłączone