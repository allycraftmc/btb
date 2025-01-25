#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 1 ]; then
  echo "usage: $0 <path-to-world>"
  exit 1
fi

if [ ! -d "$1" ]; then
  echo "Could not find $1"
  exit 1
fi

du -sh "$1"

read -p "Are you sure to clean this world? [Y/N]" -n 1 -r
echo

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
  echo "Aborting"
  exit 1
fi

set -x

# delete empty chunks
mcaselector --mode delete --world "$1" --query "Palette = \"air\""

# delete player related data
rm -rf "$1/advancements"
rm -rf "$1/stats"
rm -rf "$1/playerdata"
rm -rf "$1/data"
rm -f "$1/level.dat_old"

# remove bukkit datapack and btb datapack after mounted in docker
rm -rf "$1/datapacks"

set +x
