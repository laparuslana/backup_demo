#!/bin/bash

BACKUP_FILE=$2
RESTORE_PATH=$1
FTP_SERVER=$3
FTP_USER=$4
FTP_PASSWORD=$5
FTP_DIRECTORY=$6

TMP_PATH="/tmp/restore"

mkdir -p "$TMP_PATH"


echo "🌐 Connecting to FTP server: $FTP_SERVER"
ftp -inv "$FTP_SERVER" <<EOF
user $FTP_USER $FTP_PASSWORD
lcd $TMP_PATH
cd $FTP_DIRECTORY
binary
get $BACKUP_FILE
bye
EOF

if [ ! -f "$TMP_PATH/$BACKUP_FILE" ]; then
    echo "❌ FTP download failed"
    exit 1
fi

echo "Extracting archive..."
mkdir -p "$RESTORE_PATH"
tar -xzvf "$TMP_PATH/$BACKUP_FILE" -C "$RESTORE_PATH"

rm -rf "$TMP_PATH"

echo "Restore completed!"
