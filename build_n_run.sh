#!/bin/bash

# Skrypt odpowiedzialny za zbudowanie obrazów dockera dla klienta i serwera oraz ich uruchomienie.
# Port 8080 hosta musi być przekierowany do portu 8080 dockera serwera
# Port 80 hosta musi być przekierowany do portu 8080 dockera klienta
# Serwer musi mieć podłaczony wolumin do zapisywania na nim danych o użytjownikach i innych danych

SERWER_IMG_NAME="serwer-web"
SERWER_CON_NAME="serwer-tawim"
SERWER_PORT=8080

CLIENT_IMG_NAME="client-web"
CLIENT_CON_NAME="client-tawim"
CLIENT_PORT=80

set -e
echo Budowanie dockera serwera
#TODO
echo Budowanie dockera klienta
#TODO
echo Uruchamnie dockera serwera
#TODO
echo Uruchamnie dockera klienta
#TODO
echo Ok