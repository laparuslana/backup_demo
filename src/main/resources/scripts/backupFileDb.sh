#!/bin/bash

SOURCE_DIR=$1
DATA=$(date +"%Y%m%d")
FTP_SERVER=$2
FTP_USER=$3
FTP_PASSWORD=$4
FTP_DIRECTORY=$5

ARCHIVE_NAME="backup_$DATA.tar.gz"
ARCHIVE_PATH="/tmp/$ARCHIVE_NAME"

echo "Creating archive..."
tar -czf "$ARCHIVE_PATH" -C "$(dirname "$SOURCE_DIR")" "$(basename "$SOURCE_DIR")"

if [ $? -ne 0 ]; then
        echo "Failed to create the archive"
        exit 2
fi

echo "🌐 Connecting to FTP server: $FTP_SERVER"
ftp -inv "$FTP_SERVER" <<EOF
user $FTP_USER $FTP_PASSWORD
lcd /tmp
cd $FTP_DIRECTORY
put $ARCHIVE_NAME
bye
EOF

echo "FTP executed"
    if [ $? -eq 0 ]; then
        echo "✅ FTP upload successful"
    else
        echo "❌ FTP upload failed"
        exit 1
    fi

rm -f "$ARCHIVE_PATH"
