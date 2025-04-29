#!/bin/bash

DATABASE_NAME=$1
DB_SERVER=$2
DB_USER=$3
DB_PASSWORD=$4
BACKUP_DIR=$5
DAYS_TO_KEEP=$6
STORAGE_TYPE=$7
DATA=$(date +"%Y%m%d")

if [ "$STORAGE_TYPE" != "FTP" ]; then
  mkdir -p "$BACKUP_DIR"
fi
FTP_SERVER=$8
FTP_USER=$9
FTP_PASSWORD="${10}"
FTP_DIRECTORY="${11}"

if [ "$CLUSTER_ADMIN" = "true" ]; then
    echo "Cluster admin mode enabled."
    export PGPASSWORD="$CLUSTER_PASSWORD"
else
    export PGPASSWORD="$DB_PASSWORD"
fi

echo "We chose: $STORAGE_TYPE"
if [ "$STORAGE_TYPE" = "FTP" ]; then
    echo "🌐 All Parameters: $FTP_SERVER, $FTP_USER, $FTP_DIRECTORY"
    TEMP_BACKUP_DIR="/home/adminbs/backup"

    mkdir -p "$TEMP_BACKUP_DIR"

    pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "${TEMP_BACKUP_DIR}/${DATABASE_NAME}_${DATA}_ftp.backup"


ftp -inv "$FTP_SERVER" <<EOF
user $FTP_USER $FTP_PASSWORD
lcd $TEMP_BACKUP_DIR
cd $FTP_DIRECTORY
put ${DATABASE_NAME}_${DATA}_ftp.backup
bye
EOF

sleep 10
    if [ $? -eq 0 ]; then
      NOW=$(date +"%Y-%m-%d %H:%M:%S")
      echo "[$NOW] FTP upload successful ${DATABASE_NAME}_${DATA}_ftp.backup to $FTP_DIRECTORY" >> /tmp/auto-db-cron.log
    else
        echo "[$NOW] FTP upload failed ${DATABASE_NAME}_${DATA}_ftp.backup to $FTP_DIRECTORY" >> /tmp/auto-db-cron.log
        exit 1
    fi
else
  pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup"

  if [ -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup" ]; then
    NOW=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[$NOW] Backup successful ${DATABASE_NAME}_${DATA}.backup to $BACKUP_DIR" >> /tmp/auto-db-cron.log
  else
      echo "[$NOW] Error during local backup for database $DATABASE_NAME to $BACKUP_DIR" >> /tmp/auto-db-cron.log
      exit 1
  fi

  BACKUP_COUNT=$(find "$BACKUP_DIR" -type f -name "*.backup" | wc -l)
  if [ "$BACKUP_COUNT" -gt 3 ]; then
    find "$BACKUP_DIR/" -maxdepth 1 -mtime +$DAYS_TO_KEEP -name "*.backup" -type f -exec rm -rf '{}' ';'
    echo "🗑️  Old backups deleted (older than $DAYS_TO_KEEP days)"
  else
    echo "ℹ️  Keeping minimum of 3 backups"
  fi

fi

echo "✅ Backup process completed."

