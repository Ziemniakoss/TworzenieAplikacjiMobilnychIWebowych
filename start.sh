#!/bin/bash
# budowanie
cd client
./mvnw install
cd ../server
./mvnw install
cd ..
( cd docker ; docker-compose up --build)