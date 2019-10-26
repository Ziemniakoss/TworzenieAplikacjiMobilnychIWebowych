#!/bin/bash
source docker_config

docker start  $SERV_DOCKER_NAME
docker start  $CLI_DOCKER_NAME

#Odpalanie przeglądarki
if command -v python3 &>/dev/null; then
    python3 -mwebbrowser http://localhost:8080
else
    echo Strona działą pod adresem localhost:8080
fi