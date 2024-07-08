#!/usr/bin/env bash

PREFIX="[NextSnapshot]"
FILE_PATH=".github/workflows/main.yml"
CHANGELOG_PATH="CHANGELOG.md"
GITHUB_ACTION_MAIN_WORKFLOW=".github/workflows/main.yml"

echo "================================================================================================================="
echo "$PREFIX GitHub Action, DOCKER_VERSION started"

MAJOR_VERSION_NUMBER=$(grep "DOCKER_VERSION:" "$FILE_PATH" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | awk -F '[.-]' '{print $1}')
MINOR_VERSION_NUMBER=$(grep "DOCKER_VERSION:" "$FILE_PATH" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | awk -F '[.-]' '{print $2}')
PATCH_VERSION_NUMBER=$(grep "DOCKER_VERSION:" "$FILE_PATH" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | awk -F '[.-]' '{print $3}')
CURRENT_RELEASE_CHANGELOG_VERSION="$MAJOR_VERSION_NUMBER.$MINOR_VERSION_NUMBER.$PATCH_VERSION_NUMBER"

echo "CURRENT_RELEASE_CHANGELOG_VERSION: $CURRENT_RELEASE_CHANGELOG_VERSION"

echo -e "### Changelog $NEXT_RELEASE_CHANGELOG_VERSION\n— TBD" > "$CHANGELOG_PATH"

echo "$PREFIX GitHub Action, DOCKER_VERSION has been completed"
echo "================================================================================================================="

