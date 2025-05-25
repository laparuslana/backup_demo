#!/bin/bash

DATABASE_NAME=$1
DB_SERVER=$2
DB_USER=$3
DB_PASSWORD=$4
BACKUP_DIR=$5
DAYS_TO_KEEP=$6
STORAGE_TYPE=$7
DATA=$(date +"%Y%m%d")
FTP_SERVER=$8
FTP_USER=$9
FTP_PASSWORD="${10}"
FTP_DIRECTORY="${11}"

export PGPASSWORD="$DB_PASSWORD"

echo "[PROGRESS] 5 Starting backup process..."

if [ "$STORAGE_TYPE" != "FTP" ] && [ "$STORAGE_TYPE" != "ftp" ]; then
  mkdir -p "$BACKUP_DIR"
fi

echo "[PROGRESS] 10 Validated storage type and paths."

if [ "$STORAGE_TYPE" = "ftp" ] || [ "$STORAGE_TYPE" = "FTP" ]; then
    echo "[PROGRESS] 20 Starting pg_dump for FTP..."
    TEMP_BACKUP_DIR="/home/adminbs/backup"
    mkdir -p "$TEMP_BACKUP_DIR"

    pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "${TEMP_BACKUP_DIR}/${DATABASE_NAME}_${DATA}_ftp.backup"
    if [ $? -ne 0 ]; then
        echo "[PROGRESS] 0 Backup failed during pg_dump for FTP"
        exit 1
    fi

    echo "[PROGRESS] 50 Uploading backup to FTP..."
    ftp -inv "$FTP_SERVER" <<EOF
user $FTP_USER $FTP_PASSWORD
lcd $TEMP_BACKUP_DIR
cd $FTP_DIRECTORY
put ${DATABASE_NAME}_${DATA}_ftp.backup
bye
EOF
    if [ $? -eq 0 ]; then
        echo "[PROGRESS] 80 FTP upload successful"
    else
        echo "[PROGRESS] 0 FTP upload failed"
        exit 1
    fi
else
    echo "[PROGRESS] 20 Starting local pg_dump..."
    pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup"
    if [ ! -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup" ]; then
        echo "[PROGRESS] 0 Error during local backup"
        exit 1
    fi
    echo "[PROGRESS] 70 Local pg_dump completed"

    BACKUP_COUNT=$(find "$BACKUP_DIR" -type f -name "*.backup" | wc -l)
    if [ "$BACKUP_COUNT" -gt 3 ]; then
        find "$BACKUP_DIR/" -maxdepth 1 -mtime +$DAYS_TO_KEEP -name "*.backup" -type f -exec rm -rf '{}' ';'
        echo "[PROGRESS] 85 Old backups deleted"
    else
        echo "[PROGRESS] 85 Minimum backup count maintained"
    fi
fi

ls -l "$BACKUP_DIR/" > /dev/null 2>&1

#echo "[PROGRESS] 100 Backup process completed"

#!/bin/bash
#
#DATABASE_NAME=$1
#DB_SERVER=$2
#DB_USER=$3
#DB_PASSWORD=$4
#BACKUP_DIR=$5
#DAYS_TO_KEEP=$6
#STORAGE_TYPE=$7
#DATA=$(date +"%Y%m%d")
#
#if [ "$STORAGE_TYPE" != "FTP" ]; then
#  mkdir -p "$BACKUP_DIR"
#fi
#FTP_SERVER=$8
#FTP_USER=$9
#FTP_PASSWORD="${10}"
#FTP_DIRECTORY="${11}"
#
#echo "FTP Server: $FTP_SERVER"
#echo "FTP User: $FTP_USER"
#echo "FTP Password: $FTP_PASSWORD"
#echo "FTP Directory: $FTP_DIRECTORY"
#
#
#export PGPASSWORD="$DB_PASSWORD"
#
#echo "We chose: $STORAGE_TYPE"
#if [ "$STORAGE_TYPE" = "ftp" ]; then
#    echo "🌐 All Parameters: $FTP_SERVER, $FTP_USER, $FTP_DIRECTORY"
#    TEMP_BACKUP_DIR="/home/adminbs/backup"
#
#    mkdir -p "$TEMP_BACKUP_DIR"
#
#    pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "${TEMP_BACKUP_DIR}/${DATABASE_NAME}_${DATA}_ftp.backup"
#
#   # sleep 10
#
#echo "🌐 Connecting to FTP server: $FTP_SERVER"
#ftp -inv "$FTP_SERVER" <<EOF
#user $FTP_USER $FTP_PASSWORD
#lcd $TEMP_BACKUP_DIR
#cd $FTP_DIRECTORY
#put ${DATABASE_NAME}_${DATA}_ftp.backup
#bye
#EOF
#
#sleep 10
#echo "FTP executed"
#    if [ $? -eq 0 ]; then
#        echo "✅ FTP upload successful"
#    else
#        echo "❌ FTP upload failed"
#        exit 1
#    fi
#else
#  echo "Backup locally $BACKUP_DIR"
#  pg_dump -h "$DB_SERVER" -p 5432 -U "$DB_USER" "$DATABASE_NAME" -c -Fc -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup"
#
#  if [ -f "$BACKUP_DIR/$DATABASE_NAME"_"$DATA.backup" ]; then
#      echo "✅ Backup successful: $DATABASE_NAME_$DATA.backup"
#  else
#      echo "❌ Error during local backup for database: $DATABASE_NAME"
#      exit 1
#  fi
#
#  BACKUP_COUNT=$(find "$BACKUP_DIR" -type f -name "*.backup" | wc -l)
#  if [ "$BACKUP_COUNT" -gt 3 ]; then
#    find "$BACKUP_DIR/" -maxdepth 1 -mtime +$DAYS_TO_KEEP -name "*.backup" -type f -exec rm -rf '{}' ';'
#    echo "🗑️  Old backups deleted (older than $DAYS_TO_KEEP days)"
#  else
#    echo "ℹ️  Keeping minimum of 3 backups"
#  fi
#
#fi
#
#ls -l "$BACKUP_DIR/"
#
#echo "✅ Backup process completed."
#

