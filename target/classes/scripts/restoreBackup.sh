#!/bin/bash

# Parameters for BAF
BAF_PATH=$1
TEST_DB_NAME=$2
DB_HOST=$3
DB_USER=$4
DB_PASSWORD=$5
BACKUP_FILE=$6
CLUSTER_ADMIN=$7
CLUSTER_USER=$8
CLUSTER_PASS=$9

FTP_SERVER="${10}"
FTP_USER="${11}"
FTP_PASSWORD="${12}"
FTP_DIRECTORY="${13}"
FULL_PATH="${14}"
STORAGE_TYPE="${15}"

LOCAL_DIR="/home/adminbs/ftp"

# Set database password for connection
if [ "$CLUSTER_ADMIN" = "true" ]; then
    echo "Cluster admin mode enabled."
    export PGPASSWORD="$CLUSTER_PASS"
else
    export PGPASSWORD="$DB_PASSWORD"
fi

# Ensure we are in the correct BAF directory
cd "$BAF_PATH" || { echo "❌ Failed to change directory to $BAF_PATH"; exit 1; }
echo "Current directory: $(pwd)"

# Check if the database exists in BAF
echo -e "\n🔍 Checking if the database exists in the BAF cluster..."
cluster=$(./rac cluster list | awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')

echo -e "\nCluster is: $cluster"
echo -e "\n📋 Currently available databases in the cluster:"
./rac infobase --cluster="$cluster" summary list | awk -F: '/name/{print $2}'

echo "We chose: $STORAGE_TYPE"

# ---- FTP RESTORE ----
if [ "$STORAGE_TYPE" = "FTP" ]; then
    echo "🌐 Connecting to FTP server: $FTP_SERVER"

    ftp -inv "$FTP_SERVER" <<EOF
user $FTP_USER $FTP_PASSWORD
lcd $LOCAL_DIR
cd $FTP_DIRECTORY
binary
get $BACKUP_FILE
bye
EOF

    if [ ! -f "$LOCAL_DIR/$BACKUP_FILE" ]; then
        echo "❌ FTP download failed: file not found"
        exit 1
    fi

    echo "✅ Backup downloaded to $LOCAL_DIR/$BACKUP_FILE"

    # Create database
    if [ "$CLUSTER_ADMIN" = "true" ]; then
        INFOBASENEW=$(./rac infobase --cluster-user="$CLUSTER_USER" --cluster-pwd="$CLUSTER_PASS" --cluster="$cluster" create \
            --create-database --name="$TEST_DB_NAME" --dbms=PostgreSQL --db-server="$DB_HOST" --db-name="$TEST_DB_NAME" \
            --locale=uk --db-user="$DB_USER" --db-pwd="$DB_PASSWORD" --license-distribution=allow | \
            awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
    else
        echo -e "\nCreating the test database in the BAF cluster..."
        INFOBASENEW=$(./rac infobase --cluster="$cluster" create \
            --create-database --name="$TEST_DB_NAME" --dbms=PostgreSQL --db-server="$DB_HOST" --db-name="$TEST_DB_NAME" \
            --locale=uk --db-user="$DB_USER" --db-pwd="$DB_PASSWORD" --license-distribution=allow | \
            awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
    fi

    if [ $? -ne 0 ]; then
        echo "❌ Failed to create database in BAF"
        exit 1
    fi

    echo "✅ Database '$TEST_DB_NAME' created in BAF"

    # Restore backup
    echo "🛠️ Restoring backup from $LOCAL_DIR/$BACKUP_FILE into $TEST_DB_NAME..."
    pg_restore -h "$DB_HOST" -p 5432 -U "$DB_USER" --clean --if-exists -d "$TEST_DB_NAME" -c "$LOCAL_DIR/$BACKUP_FILE"

    if [ $? -eq 0 ]; then
        echo "✅ Restore completed successfully into database '$TEST_DB_NAME'"
    else
        echo "❌ Restore failed"
        exit 1
    fi
fi

# ---- LOCAL RESTORE ----
if [ "$STORAGE_TYPE" = "LOCAL" ]; then
    if [ "$CLUSTER_ADMIN" = "true" ]; then
        INFOBASENEW=$(./rac infobase --cluster-user="$CLUSTER_USER" --cluster-pwd="$CLUSTER_PASS" --cluster="$cluster" create \
            --create-database --name="$TEST_DB_NAME" --dbms=PostgreSQL --db-server="$DB_HOST" --db-name="$TEST_DB_NAME" \
            --locale=uk --db-user="$DB_USER" --db-pwd="$DB_PASSWORD" --license-distribution=allow | \
            awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
    else
        echo -e "\nCreating the test database in the BAF cluster..."
        INFOBASENEW=$(./rac infobase --cluster="$cluster" create \
            --create-database --name="$TEST_DB_NAME" --dbms=PostgreSQL --db-server="$DB_HOST" --db-name="$TEST_DB_NAME" \
            --locale=uk --db-user="$DB_USER" --db-pwd="$DB_PASSWORD" --license-distribution=allow | \
            awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
    fi

    if [ $? -ne 0 ]; then
        echo "❌ Failed to create database in BAF"
        exit 1
    fi

    echo "✅ Database '$TEST_DB_NAME' created in BAF"

    echo "🛠️ Restoring local backup: $FULL_PATH into database '$TEST_DB_NAME'..."
    pg_restore -h "$DB_HOST" -p 5432 -U "$DB_USER" --clean --if-exists -d "$TEST_DB_NAME" -c "$FULL_PATH"

    if [ $? -eq 0 ]; then
        echo "✅ Restore completed successfully into database '$TEST_DB_NAME'"
    else
        echo "❌ Restore failed"
        exit 1
    fi

    # Verify available databases
    echo "📋 Available databases after restore:"
    psql -h "$DB_HOST" -p 5432 -U "$DB_USER" -lqt | cut -d \| -f 1 | sed -e 's/^[ \t]*//' | sort
fi
#!/bin/bash

# Parameters for BAF
#BAF_PATH=$1
#TEST_DB_NAME=$2
#DB_HOST=$3
#DB_USER=$4
#DB_PASSWORD=$5
#BACKUP_FILE=$6
#CLUSTER_ADMIN=$7
#CLUSTER_USER=$8
#CLUSTER_PASS=$9
#
#FTP_SERVER="${10}"
#FTP_USER="${11}"
#FTP_PASSWORD="${12}"
#FTP_DIRECTORY="${13}"
#FULL_PATH="${14}"
#STORAGE_TYPE="${15}"
#
#
#LOCAL_DIR="/home/adminbs/ftp"
#
#if [ "$CLUSTER_ADMIN" = "true" ]; then
#    echo "Cluster admin mode enabled."
#    export PGPASSWORD="$CLUSTER_PASS"
#else
#    export PGPASSWORD="$DB_PASSWORD"
#fi
#
#
## Ensure we are in the correct BAF directory
#cd "$BAF_PATH" || exit 1
#clear
#pwd
#
## Check if the database exists in BAF
#echo -e "\nChecking if the database exists in the BAF cluster..."
#cluster=$(./rac cluster list | awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
#
## List all databases in the cluster
#echo -e "\nCluster is $cluster"
#echo -e "\nCurrently available databases in the cluster: "
#./rac infobase --cluster=$cluster summary list | awk -F: '/name/{print $2}'
#
#
#echo "We chose: $STORAGE_TYPE"
#if [ "$STORAGE_TYPE" = "ftp" ]; then
#
#echo "🌐 Connecting to FTP server: $FTP_SERVER"
#ftp -inv "$FTP_SERVER" <<EOF
#user $FTP_USER $FTP_PASSWORD
#lcd $LOCAL_DIR
#cd $FTP_DIRECTORY
#binary
#get $BACKUP_FILE
#bye
#EOF
#
#if [ ! -f "$LOCAL_DIR/$BACKUP_FILE" ]; then
#    echo "❌ FTP download failed"
#    exit 1
#fi
#
#echo "Backup downloaded $LOCAL_DIR"
#
#if [ "$CLUSTER_ADMIN" = "true" ]; then
#  INFOBASENEW=$(./rac infobase --cluster-user=$CLUSTER_USER --cluster-pwd=$CLUSTER_PASS --cluster=$cluster create \
#    --create-database --name=$TEST_DB_NAME --dbms=PostgreSQL --db-server=$DB_HOST --db-name=$TEST_DB_NAME \
#    --locale=uk --db-user=$DB_USER --db-pwd=$DB_PASSWORD --license-distribution=allow | \
#    awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
#  else
#
##Create the test database in the BAF cluster
#echo -e "\nCreating the test database in the BAF cluster..."
#INFOBASENEW=$(./rac infobase --cluster=$cluster create \
#  --create-database --name=$TEST_DB_NAME --dbms=PostgreSQL --db-server=$DB_HOST --db-name=$TEST_DB_NAME \
#  --locale=uk --db-user=$DB_USER --db-pwd=$DB_PASSWORD --license-distribution=allow | \
#  awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
#fi
#if [ $? -ne 0 ]; then
#  echo "❌ Failed to create database in BAF"
#  exit 1
#fi
#
#echo "✅ Database '$TEST_DB_NAME' created in BAF"
#
#echo "Restoring FTP backup from $LOCAL_DIR/$BACKUP_FILE into $TEST_DB_NAME"
#pg_restore -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" --clean --if-exists -d "$TEST_DB_NAME" -c "$LOCAL_DIR/$BACKUP_FILE"
#
#if [ $? -eq 0 ]; then
#    echo "✅ Restore completed successfully into database '$TEST_DB_NAME'"
#else
#    echo "❌ Restore failed"
#    exit 1
#fi
#fi
#
#
#if [ "$STORAGE_TYPE" = "local" ]; then
#if [ "$CLUSTER_ADMIN" = "true" ]; then
#  INFOBASENEW=$(./rac infobase --cluster-user=$CLUSTER_USER --cluster-pwd=$CLUSTER_PASS --cluster=$cluster create \
#    --create-database --name=$TEST_DB_NAME --dbms=PostgreSQL --db-server=$DB_HOST --db-name=$TEST_DB_NAME \
#    --locale=uk --db-user=$DB_USER --db-pwd=$DB_PASSWORD --license-distribution=allow | \
#    awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
#
#  else
## Create the test database in the BAF cluster
#echo -e "\nCreating the test database in the BAF cluster..."
#INFOBASENEW=$(./rac infobase --cluster=$cluster create \
#  --create-database --name=$TEST_DB_NAME --dbms=PostgreSQL --db-server=$DB_HOST --db-name=$TEST_DB_NAME \
#  --locale=uk --db-user=$DB_USER --db-pwd=$DB_PASSWORD --license-distribution=allow | \
#  awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')
#
#if [ $? -ne 0 ]; then
#  echo "❌ Failed to create database in BAF"
#  exit 1
#fi
#fi
#echo "✅ Database '$TEST_DB_NAME' created in BAF"
#
## Restore the backup into the test database using pg_restore
#echo "Restoring backup into database '$TEST_DB_NAME'..."
#
#pg_restore -h "$DB_HOST" -p "5432" -U "$DB_USER" --clean --if-exists -d "$TEST_DB_NAME" -c "$FULL_PATH"
#
#
#if [ $? -eq 0 ]; then
#    echo "✅ Restore completed successfully into database '$TEST_DB_NAME'"
#else
#    echo "❌ Restore failed"
#    exit 1
#fi
#
## Verify the databases after restore
#echo "📋 Available databases after restore:"
#psql -h "$DB_HOST" -p "5432" -U "$DB_USER" -lqt | cut -d \| -f 1 | sed -e 's/^[ \t]*//' | sort
#
#fi
