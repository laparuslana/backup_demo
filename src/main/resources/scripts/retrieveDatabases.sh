#!/bin/bash

HOST=$1
USER=$2
PASSWORD=$3
PORT="5432"
DBNAME="postgres"

export PGPASSWORD=$PASSWORD

psql -h $HOST -p $PORT -U $USER -d $DBNAME -c "SELECT datname FROM pg_database WHERE datistemplate = false;" | \
    awk 'NR > 2 {print $1}'

unset PGPASSWORD
