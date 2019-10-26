#!/bin/bash
source docker_config

D=docker

echo Stopowanie > /dev/null
bash stop.sh
echo Usuwanie kontenerów
$D rm -f $SERV_DOCKER_NAME > /dev/null
$D rm -f $CLI_DOCKER_NAME > /dev/null
echo Usuwanie sieci
$D network rm $NETWORK_NAME > /dev/null
echo Usuwanie obrazów
$D image rm -f $SERV_IMG_NAME > /dev/null
$D image rm -f $CLI_IMG_NAME > /dev/null
echo Ok
