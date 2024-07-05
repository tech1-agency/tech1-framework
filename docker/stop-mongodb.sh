#!/usr/bin/env bash

docker-compose -f "$(pwd)"/docker-compose.mongodb.yml down --volumes
