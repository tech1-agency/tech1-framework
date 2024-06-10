#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 <<-EOSQL
    CREATE DATABASE tech1_b2b_postgres_server;
EOSQL
