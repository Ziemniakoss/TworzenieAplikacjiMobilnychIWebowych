#!/bin/bash
source docker_config

D="docker"
TOTAL_STAEPS=8

# cleaning
STEP=1
echo $STEP/$TOTAL_STAEPS Zatrzymywanie działających dockerów
$D stop $SERV_DOCKER_NAME
$D stop $CLI_DOCKER_NAME
((STEP++))

echo $STEP/$TOTAL_STAEPS Usuwanie konetenerów
$D rm $SERV_DOCKER_NAME
$D rm $CLI_DOCKER_NAME
((STEP++))

echo $STEP/$TOTAL_STAEPS Usuwanie sieci
$D network rm $NETWORK_NAME
((STEP++))

set -e
echo $STEP/$TOTAL_STAEPS Tworzenie sieci
$D network create --subnet=172.18.0.0/16 $NETWORK_NAME
((STEP++))

# build server and cli image
echo $STEP/$TOTAL_STAEPS Tworzenie obrazu serwera
$D build -t=$SERV_IMG_NAME docker/server/
((STEP++))
echo $STEP/$TOTAL_STAEPS Tworzenie obrazu klienta
$D build  -t=$CLI_IMG_NAME docker/client/
((STEP++))

#Tworzenie konetenerów
$D create --net $NETWORK_NAME --hostname server --ip $SERV_IP -it --name=$SERV_DOCKER_NAME $SERV_IMG_NAME
$D create --net $NETWORK_NAME --ip $CLI_IP -it -p $CLI_PORT:$CLI_PORT  --name=$CLI_DOCKER_NAME $CLI_IMG_NAME