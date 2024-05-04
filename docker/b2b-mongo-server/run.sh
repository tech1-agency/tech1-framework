#!/usr/bin/env bash

docker-compose -f "$(pwd)"/docker-compose.docker.yml pull
#docker-compose -f "$(pwd)"/docker-compose.docker.yml up -d
docker-compose -f "$(pwd)"/docker-compose.docker.yml up
