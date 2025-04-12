#!/bin/bash

# Parameters for BAF
#/opt/1cv8/i386/8.3.25.1374
BAF_PATH=$1
CLUSTER_ADMIN=$2
CLUSTER_USER=$3
CLUSTER_PASS=$4
SOURCE_DB=$5
INFOBASE=$6

# Ensure we are in the correct BAF directory
cd "$BAF_PATH" || exit 1
clear
pwd

# Check if the database exists in BAF
echo -e "\nChecking if the database exists in the BAF cluster..."
cluster=$(./rac cluster list | awk '{FS=":"; RS="-"} {print $3}' | sed '2,$d')

# List all databases in the cluster
echo -e "\nCluster is $cluster"


echo -e "\n Now remove Web publication und restart Apache \n "
$BAF_PATH/webinst -delete -apache24 -wsdir $SOURCE_DB -dir "/var/www/html/$SOURCE_DB" \
-connstr "Srvr="127.0.0.1";Ref="$SOURCE_DB";" -confPath /etc/apache2/conf-enabled/$SOURCE_DB.conf|\
rm -R /var/www/html/$SOURCE_DB|rm /etc/apache2/conf-enabled/$SOURCE_DB.conf
systemctl restart apache2.service
echo -e "\n Web publication delete!"

sleep 5

if [ "$CLUSTER_ADMIN" = "true" ]; then
  $BAF_PATH/rac infobase --cluster-user=$CLUSTER_USER --cluster-pwd=$CLUSTER_PASS --cluster=$cluster drop --infobase=$INFOBASE
else
  $BAF_PATH/rac infobase --cluster=$cluster drop --infobase=$INFOBASE
fi