#!/usr/bin/env bash

METHOD=maven
PORT=8484
SPRING_BOOT_PROFILE=dev
SPRING_BOOT_CONFIG_LOCATION=classpath:application.yml,classpath:application-dev.yml
JASYPT_PASSWORD=JJEPTECH1
JVM_ARGUMENTS="-Xms512m -Xmx2g --add-opens=java.base/java.time=ALL-UNNAMED --add-opens=java.base/java.math=ALL-UNNAMED"

echo "================================================================================================================="
echo "PostgreSQL init [Started]"
echo "Create database 'tech1_b2b_postgres_server' if not exist"
echo "================================================================================================================="

docker run --rm --network tech1-network jbergknoff/postgresql-client postgresql://postgres:postgres@tech1-postgres:5432/postgres -c "CREATE DATABASE tech1_b2b_postgres_server"

echo "================================================================================================================="
echo "PostgreSQL init [Completed]"
echo "================================================================================================================="

java-run-spring-boot-dev-profile-v4.sh $METHOD $PORT $SPRING_BOOT_PROFILE $SPRING_BOOT_CONFIG_LOCATION $JASYPT_PASSWORD "$JVM_ARGUMENTS"
