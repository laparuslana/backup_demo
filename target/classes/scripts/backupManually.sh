#!/bin/bash

CLUSTER_SERVER=$1
DATABASE_NAME=$2
DB_SERVER=$3
DB_USER=$4
DB_PASSWORD=$5
BACKUP_DIR=$6
DAYS_TO_KEEP=$7
DATA=$(date +"%Y%m%d")

mkdir -p "$BACKUP_DIR"

PGPASSWORD="$DB_PASSWORD" pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup"

if [ -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup" ]; then
    echo "✅ Backup successful: $DATABASE_NAME_$DATA.backup"
else
    echo "❌ Error during backup for database: $DATABASE_NAME"
    exit 1
fi

find "$BACKUP_DIR/" -maxdepth 1 -mtime +$DAYS_TO_KEEP -name "*.backup" -type f -exec rm -rf '{}' ';'

ls -l "$BACKUP_DIR/"

echo "✅ Backup process completed."
