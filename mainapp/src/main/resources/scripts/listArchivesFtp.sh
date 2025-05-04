#!/bin/bash

FTP_HOST=$1
FTP_USER=$2
FTP_PASS=$3
FTP_DIR=$4

ftp -inv "$FTP_HOST" <<EOF | awk '{print $NF}' | grep '\.tar.gz$' || true
user $FTP_USER $FTP_PASS
cd $FTP_DIR
ls
bye
EOF
