#!/bin/bash

CLUSTER_SERVER=$1
TEST_DB_NAME=$2
DB_HOST=$3
DB_USER=$4
DB_PASSWORD=$5
BACKUP_FILE=$6
CLUSTER_ADMIN=$7
CLUSTER_USER=$8
CLUSTER_PASS=$9

if [ "$CLUSTER_ADMIN" = "true" ]; then
    echo "Cluster admin mode enabled."
    export PGPASSWORD="$CLUSTER_PASS"
else
    export PGPASSWORD="$DB_PASSWORD"
fi
#export PGPASSWORD="postgres"
#dropdb -h "localhost" -p "5432" -U "postgres" "test_base"

echo "Creating test database: $TEST_DB_NAME"
createdb -h "$DB_HOST" -p 5432 -U "$DB_USER" "$TEST_DB_NAME"
if [ $? -ne 0 ]; then
    echo "❌ Failed to create database"
    exit 1
fi

echo "Database '$TEST_DB_NAME' created successfully"

echo "📋 Available databases after creation:"
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -lqt | cut -d \| -f 1 | sed -e 's/^[ \t]*//' | sort

echo "Restoring backup from $BACKUP_FILE into $TEST_DB_NAME"
pg_restore -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" --clean --if-exists -d "$TEST_DB_NAME" -c "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Restore completed successfully into database '$TEST_DB_NAME'"
else
    echo "❌ Restore failed"
    exit 1
fi