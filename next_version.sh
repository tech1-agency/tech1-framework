#!/usr/bin/env bash

MAVEN_NEW_VERSION_DEFAULT="2.6.0-SNAPSHOT"

read -p "Provide new maven version or press enter for defined version '$MAVEN_NEW_VERSION_DEFAULT': " MAVEN_NEW_VERSION_USER_INPUT

MAVEN_NEW_VERSION="${MAVEN_NEW_VERSION_USER_INPUT:-$MAVEN_NEW_VERSION_DEFAULT}"

echo "==========================================================================================="
echo "Parameter: MAVEN_NEW_VERSION = $MAVEN_NEW_VERSION"
echo "==========================================================================================="

mvn versions:set -DnewVersion="${MAVEN_NEW_VERSION}" -DgenerateBackupPoms=false
