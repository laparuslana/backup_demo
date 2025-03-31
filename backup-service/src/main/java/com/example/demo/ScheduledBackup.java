package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.Base64;

@Entity
@Table(name = "backup_settings")
public class ScheduledBackup {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "database_name")
        private String databaseName;

        @Column(name = "frequency")
        private String frequency;

        @Column(name = "day")
        private String day;

        @Column(name = "time")
        private LocalTime time;

        @Column(name = "cluster_server")
        private String clusterServer;

        @Column(name = "db_server")
        private String dbServer;

        @Column(name = "db_user")
        private String dbUser;

        @Column(name = "db_password")
        private String dbPassword;

        @Column(name = "backup_location")
        private String backupLocation;

        @Column(name = "retention_period")
        private String daysKeep;

        @Column(name = "cluster_admin")
        private boolean clusterAdmin;

        @Column(name = "cluster_username")
        private String clusterUsername;

        @Column(name = "cluster_password")
        private String clusterPassword;


    private String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    private String decrypt(String encryptedData) {
        return new String(Base64.getDecoder().decode(encryptedData));
    }

    public String getDaysKeep() {
            return daysKeep;
        }

        public void setDaysKeep(String daysKeep) {
            this.daysKeep = daysKeep;
        }

        public String getClusterPassword() {
            return decrypt(clusterPassword);
        }

        public String getClusterUsername() {
            return clusterUsername;
        }

        public void setClusterUsername(String clusterUsername) {
            this.clusterUsername = clusterUsername;
        }

        public boolean isClusterAdmin() {
            return clusterAdmin;
        }

        public void setClusterAdmin(boolean clusterAdmin) {
            this.clusterAdmin = clusterAdmin;
        }

        public void setDbPassword(String dbPassword) {
            this.dbPassword = encrypt(dbPassword);
        }

        public void setClusterPassword(String clusterPassword) {
            this.clusterPassword = encrypt(clusterPassword);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public LocalTime getTime() {
            return time;
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        public String getClusterServer() {
            return clusterServer;
        }

        public void setClusterServer(String clusterServer) {
            this.clusterServer = clusterServer;
        }

        public String getDbServer() {
            return dbServer;
        }

        public void setDbServer(String dbServer) {
            this.dbServer = dbServer;
        }

        public String getDbUser() {
            return dbUser;
        }

        public void setDbUser(String dbUser) {
            this.dbUser = dbUser;
        }

        public String getDbPassword() {
            return decrypt(dbPassword);
        }

        public String getBackupLocation() {
            return backupLocation;
        }

        public void setBackupLocation(String backupLocation) {
            this.backupLocation = backupLocation;
        }
    }

