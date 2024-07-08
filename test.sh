#!/usr/bin/env bash

FILE_PATH=".github/workflows/main.yml"
DOCKER_COMPOSE_MONGODB_PATH="docker/docker-compose.mongodb.yml"

echo "================================================================================================================="
MAJOR_VERSION_NUMBER=$(grep "DOCKER_VERSION:" "$FILE_PATH" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | awk -F '[.-]' '{print $1}')
MINOR_VERSION_NUMBER=$(grep "DOCKER_VERSION:" "$FILE_PATH" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | awk -F '[.-]' '{print $2}')
PATCH_VERSION_NUMBER=$(grep "DOCKER_VERSION:" "$FILE_PATH" | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | awk -F '[.-]' '{print $3}')
CURRENT_RELEASE_CHANGELOG_VERSION="$MAJOR_VERSION_NUMBER.$MINOR_VERSION_NUMBER.$PATCH_VERSION_NUMBER"

echo "CURRENT_RELEASE_CHANGELOG_VERSION: $CURRENT_RELEASE_CHANGELOG_VERSION"

NEW_CONTENT="ghcr.io\/tech1-io\/tech1-framework-iam-server:$CURRENT_RELEASE_CHANGELOG_VERSION"
NEW_CONTENT3="\timage: ghcr.io\/tech1-io\/tech1-framework-iam-server:3.0.2"
NEW_CONTENT2="    image: ghcr.io\/tech1-io\/tech1-framework-iam-server:$CURRENT_RELEASE_CHANGELOG_VERSION"

# Use sed to replace line 3 with the new content
sed -i '' '3s/.*/'"$NEW_CONTENT2"'/' "$DOCKER_COMPOSE_MONGODB_PATH"

#sed -i '' 's/image: ghcr.io\/tech1-io\/tech1-framework-iam-server:[0-9.]\+/image: ghcr.io\/tech1-io\/tech1-framework-iam-server:'"$NEW_TAG"'/' "$FILE_PATH"

echo "================================================================================================================="

