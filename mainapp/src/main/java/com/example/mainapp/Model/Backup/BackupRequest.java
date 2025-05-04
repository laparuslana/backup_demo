package com.example.mainapp.Model.Backup;


import java.util.Map;

public class BackupRequest {

    private String databaseName;
    private String dbServer;
    private String dbUser;
    private String dbPassword;
    private String backupLocation;
    private String retentionPeriod;
    private String storageType;

    public String getNameSelect() {
        return nameSelect;
    }

    public void setNameSelect(String nameSelect) {
        this.nameSelect = nameSelect;
    }

    private String nameSelect;

    public Map<String, String> getStorageParams() {
        return storageParams;
    }

    public void setStorageParams(Map<String, String> storageParams) {
        this.storageParams = storageParams;
    }

    private Map<String, String> storageParams;


    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getRetentionPeriod() {
        return retentionPeriod;
    }

    public void setRetentionPeriod(String retentionPeriod) {
        this.retentionPeriod = retentionPeriod;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
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
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation) {
        this.backupLocation = backupLocation;
    }
}

