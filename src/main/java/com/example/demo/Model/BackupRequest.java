package com.example.demo.Model;

public class BackupRequest {
    private String clusterServer;
    private String databaseName;
    private String dbServer;
    private String dbUser;
    private String dbPassword;
    private String backupLocation;
//    private String daysKeep;

    public BackupRequest() {}

    public BackupRequest(String clusterServer, String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation) {
        this.clusterServer = clusterServer;
        this.databaseName = databaseName;
        this.dbServer = dbServer;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.backupLocation = backupLocation;
//        this.daysKeep = daysKeep;
    }

    // Getters and Setters
    public String getClusterServer() { return clusterServer; }
    public void setClusterServer(String clusterServer) { this.clusterServer = clusterServer; }

    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

    public String getDbServer() { return dbServer; }
    public void setDbServer(String dbServer) { this.dbServer = dbServer; }

    public String getDbUser() { return dbUser; }
    public void setDbUser(String dbUser) { this.dbUser = dbUser; }

    public String getDbPassword() { return dbPassword; }
    public void setDbPassword(String dbPassword) { this.dbPassword = dbPassword; }

    public String getBackupLocation() { return backupLocation; }
    public void setBackupLocation(String backupLocation) { this.backupLocation = backupLocation; }
//
//    public String getDaysKeep() { return daysKeep; }
//    public void setDaysKeep(String daysKeep) { this.daysKeep = daysKeep; }
}

