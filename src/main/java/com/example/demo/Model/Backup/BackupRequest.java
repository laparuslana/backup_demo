package com.example.demo.Model.Backup;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class BackupRequest {
    private String clusterServer;
    private String databaseName;
    private String dbServer;
    private String dbUser;
    private String dbPassword;
    private String backupLocation;
    private String retentionPeriod;
    private boolean clusterAdmin;
    private String clusterUsername;
    private String clusterPassword;
    private String storageType;

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

    public boolean isClusterAdmin() {
        return clusterAdmin;
    }

    public void setClusterAdmin(boolean clusterAdmin) {
        this.clusterAdmin = clusterAdmin;
    }

    public String getClusterUsername() {
        return clusterUsername;
    }

    public void setClusterUsername(String clusterUsername) {
        this.clusterUsername = clusterUsername;
    }

    public String getClusterPassword() {
        return clusterPassword;
    }

    public void setClusterPassword(String clusterPassword) {
        this.clusterPassword = clusterPassword;
    }



    // Getters and Setters
    public String getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(String clusterServer) {
        this.clusterServer = clusterServer;
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

