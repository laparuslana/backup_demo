package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.Base64;

@Entity
@Table(name = "backup_settings")
public class ScheduledBackup {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "type", nullable = false)
        private String type;

        @Column(name = "folder_path")
        private String folderPath;

        @Column(name = "database_name")
        private String databaseName;

        @Column(name = "frequency")
        private String frequency;

        @Column(name = "day")
        private String day;

        @Column(name = "time")
        private LocalTime time;

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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public JsonNode getStorageParams() {
        return storageParams;
    }

    public void setStorageParams(JsonNode storageParams) {
        this.storageParams = storageParams;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    @Column(name = "storage_params", columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode storageParams;

    @Column(name = "storage_type", nullable = false)
    private String storageType;


    private String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    private String decrypt(String encryptedData) {
        if (encryptedData == null) {
            return "";
        }
        return new String(Base64.getDecoder().decode(encryptedData));
    }

    public String getDaysKeep() {
            return daysKeep;
        }

        public void setDaysKeep(String daysKeep) {
            this.daysKeep = daysKeep;
        }

        public void setDbPassword(String dbPassword) {
            this.dbPassword = encrypt(dbPassword);
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

