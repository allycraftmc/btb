#!/usr/bin/env bash

if [ "$#" -lt 2 ]; then
  echo "usage: $0 <user>@<server> <path_server>"
  exit 1
fi

set -x

SSH_DESTINATION="$1"
BASE_PATH="$2"

RSYNC_OPTIONS="-rl --delete --mkpath --progress"
# Copy worlds
# shellcheck disable=SC2086
rsync $RSYNC_OPTIONS ./assets/lobby-world ./assets/map-world "$SSH_DESTINATION:$BASE_PATH/assets"
# Copy Plugin
# shellcheck disable=SC2086
rsync $RSYNC_OPTIONS ./build/libs/btb-1.0-SNAPSHOT.jar "$SSH_DESTINATION:$BASE_PATH/build/libs/btb-1.0-SNAPSHOT.jar"

set +x

echo "Uploaded to production server"