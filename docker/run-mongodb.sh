#!/usr/bin/env bash

docker-compose -f "$(pwd)"/docker-compose.mongodb.yml pull
#docker-compose -f "$(pwd)"/docker-compose.mongodb.yml up -d
docker-compose -f "$(pwd)"/docker-compose.mongodb.yml up
