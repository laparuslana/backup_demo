#!/bin/bash

# Parameters for BAF
# /opt/1cv8/i386/8.3.25.1374
BAF_PATH=$1
CLUSTER_ADMIN=$2
CLUSTER_USER=$3
CLUSTER_PASS=$4

# Ensure we are in the correct BAF directory
cd "$BAF_PATH" || exit 1
clear
pwd

# Check if the database exists in BAF
cluster=$(./rac cluster list | awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')

if [ "$CLUSTER_ADMIN" = "true" ]; then
    ./rac infobase --cluster-user="$CLUSTER_USER" --cluster-pwd="$CLUSTER_PASS" --cluster="$cluster" summary list \
    | sed -n 's/^[[:space:]]*infobase[[:space:]]*:[[:space:]]*//p' \
    | paste -d';' - - \
    | while IFS=';' read -r uuid; do
        read -r name
        echo "$uuid;$name"
      done < <(
        ./rac infobase --cluster-user="$CLUSTER_USER" --cluster-pwd="$CLUSTER_PASS" --cluster="$cluster" summary list \
        | sed -n -e 's/^[[:space:]]*infobase[[:space:]]*:[[:space:]]*//p' -e 's/^[[:space:]]*name[[:space:]]*:[[:space:]]*//p'
      )
else
    ./rac infobase --cluster="$cluster" summary list \
    | sed -n -e 's/^[[:space:]]*infobase[[:space:]]*:[[:space:]]*//p' -e 's/^[[:space:]]*name[[:space:]]*:[[:space:]]*//p' \
    | paste -d';' - - \
    | while IFS=';' read -r uuid name; do
        echo "$uuid;$name"
      done
fi
