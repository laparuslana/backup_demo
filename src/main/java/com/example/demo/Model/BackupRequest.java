package com.example.demo.Model;

import com.fasterxml.jackson.databind.JsonNode;

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
    private JsonNode storageParams;


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


    public BackupRequest() {
    }

    public BackupRequest(String clusterServer, String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation, String retentionPeriod, boolean clusterAdmin, String clusterUsername, String clusterPassword) {
        this.clusterServer = clusterServer;
        this.databaseName = databaseName;
        this.dbServer = dbServer;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.backupLocation = backupLocation;
        this.retentionPeriod = retentionPeriod;
        this.clusterAdmin = clusterAdmin;
        this.clusterUsername = clusterUsername;
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

