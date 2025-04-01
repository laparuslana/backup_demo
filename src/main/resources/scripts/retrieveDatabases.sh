#!/bin/bash

# Get arguments from command line
HOST=$1
USER=$2
PASSWORD=$3
PORT="5432"
DBNAME="postgres"  # Default database to connect to (postgres is often used)

# Export password to avoid password prompt
export PGPASSWORD=$PASSWORD

# Run the query to get the list of databases
psql -h $HOST -p $PORT -U $USER -d $DBNAME -c "SELECT datname FROM pg_database WHERE datistemplate = false;" | \
    awk 'NR > 2 {print $1}'  # Filter out the first two lines (headers)

# Optionally, unset the password variable after use
unset PGPASSWORD
