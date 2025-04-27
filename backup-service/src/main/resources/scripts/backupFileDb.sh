#!/bin/bash

SOURCE_DIR=$1
DAYS_TO_KEEP=$2
FTP_SERVER=$3
FTP_USER=$4
FTP_PASSWORD=$5
FTP_DIRECTORY=$6
DATA=$(date +"%Y%m%d")

SOURCE_BASENAME=$(basename "$SOURCE_DIR")
ARCHIVE_NAME="${SOURCE_BASENAME}_backup_${DATA}.tar.gz"
ARCHIVE_PATH="/home/adminbs/archive"

mkdir -p "$ARCHIVE_PATH"

echo "Creating archive..."
tar -czf "$ARCHIVE_PATH/$ARCHIVE_NAME" -C "$(dirname "$SOURCE_DIR")" "$(basename "$SOURCE_DIR")"

if [ $? -ne 0 ]; then
        echo "Failed to create the archive"
        exit 2
fi

echo "🌐 Connecting to FTP server: $FTP_SERVER"
ftp -inv "$FTP_SERVER" <<EOF
user $FTP_USER $FTP_PASSWORD
lcd $ARCHIVE_PATH
cd $FTP_DIRECTORY
put $ARCHIVE_NAME
bye
EOF

echo "FTP executed"
    if [ $? -eq 0 ]; then
      NOW=$(date +"%Y-%m-%d %H:%M:%S")
      echo "[$NOW] FTP upload successful $ARCHIVE_NAME to $FTP_DIRECTORY" >> /tmp/auto-file-cron.log
    else
        echo "[$NOW] FTP upload failed $ARCHIVE_NAME to $FTP_DIRECTORY" >> /tmp/auto-file-cron.log
        exit 1
    fi

BACKUP_COUNT=$(find "$ARCHIVE_PATH" -type f -name "*.tar.gz" | wc -l)
  if [ "$BACKUP_COUNT" -gt 3 ]; then
    find "$ARCHIVE_PATH/" -maxdepth 1 -mtime +$DAYS_TO_KEEP -name "*.tar.gz" -type f -exec rm -rf '{}' ';'
    echo "🗑️  Old backups deleted (older than $DAYS_TO_KEEP days)"
  else
    echo "ℹ️  Keeping minimum of 3 backups"
  fi

