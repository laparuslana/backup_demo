### Data backup system for accounting applications of the BAF platform.

This is a specialized software solution for performing both manual and automated backups and restores of data from the BAS application on Linux.

## Table of Contents

- [Requirements](#requirements)
- [System Structure](#system-structure)
- [Installation](#installation)
- [Uninstallation](#uninstallation)

## Requirements

- Linux server (recommended: Ubuntu 20.04+)
- Installed `MariaDB`
- Installed `Java 17+`
- `systemd` (for service management)
- `tar`, `bash`, `dpkg`

---

## System Structure
- `mainapp.jar`  Main application 
- `backup-service.jar` Module for scheduled backups
- `init-db.sql`  SQL script for initializing the database 
- `backup_baf.sql` SQL dump of the schema
- `setup.sh` Script to configure application properties

---

## Installation
1. .deb package can be requested with email to laparuslana@gmail.com
2. **Install .deb package:**
   ```bash
   sudo dpkg -i business_backup.deb
   sudo apt-get install -f
3. **Run init-db.sql**
   1. Enter yout root password if MariaDB not newly installed
   2. Enter `backup_baf` as application's database
   3. Enter DB user and password for application 
4. **Run script setup.sh** 

## Uninstallation
To uninstall run the following commands:
   ```bash
      sudo systemctl stop mainapp
      sudo systemctl disable mainapp
      sudo rm /etc/systemd/system/mainapp.service
      sudo systemctl stop backup-service
      sudo systemctl disable backup-service
      sudo rm /etc/systemd/system/backup-service.service
      sudo rm -rf /opt/myapp
      sudo rm -rf /opt/backup-service

// if Mariadb was installed for this application 
      sudo apt purge --remove mariadb-server mariadb-client mariadb-common mariadb-* -y
      sudo systemctl daemon-reload



