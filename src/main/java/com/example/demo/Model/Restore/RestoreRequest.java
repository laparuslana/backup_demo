package com.example.demo.Model.Restore;


public class RestoreRequest {

    private String res_clusterServer;
    private String testDbName;
    private String restoreDbServer;
    private String restoreDbUser;
    private String restoreDbPassword;
    private String backupFile;
    private boolean res_clusterAdmin;
    private String res_clusterUsername;
    private String res_clusterPassword;



    public boolean isRes_clusterAdmin() {
        return res_clusterAdmin;
    }

    public void setRes_clusterAdmin(boolean res_clusterAdmin) {
        this.res_clusterAdmin = res_clusterAdmin;
    }

    public String getRes_clusterUsername() {
        return res_clusterUsername;
    }

    public void setRes_clusterUsername(String res_clusterUsername) {
        this.res_clusterUsername = res_clusterUsername;
    }

    public String getRes_clusterPassword() {
        return res_clusterPassword;
    }

    public void setRes_clusterPassword(String res_clusterPassword) {
        this.res_clusterPassword = res_clusterPassword;
    }


    public RestoreRequest() {
    }

    public RestoreRequest(String res_clusterServer, String testDbName, String restoreDbServer, String restoreDbUser, String restoreDbPassword, String backupFile, boolean res_clusterAdmin, String res_clusterUsername, String res_clusterPassword) {
        this.res_clusterServer = res_clusterServer;
        this.testDbName = testDbName;
        this.restoreDbServer = restoreDbServer;
        this.restoreDbUser = restoreDbUser;
        this.restoreDbPassword = restoreDbPassword;
        this.backupFile = backupFile;
        this.res_clusterAdmin = res_clusterAdmin;
        this.res_clusterUsername = res_clusterUsername;
        this.res_clusterPassword = res_clusterPassword;
    }

    public String getRes_clusterServer() {
        return res_clusterServer;
    }

    public void setRes_clusterServer(String res_clusterServer) {
        this.res_clusterServer = res_clusterServer;
    }

    public String getTestDbName() {
        return testDbName;
    }

    public void setTestDbName(String testDbName) {
        this.testDbName = testDbName;
    }

    public String getRestoreDbServer() {
        return restoreDbServer;
    }

    public void setRestoreDbServer(String restoreDbServer) {
        this.restoreDbServer = restoreDbServer;
    }

    public String getRestoreDbUser() {
        return restoreDbUser;
    }

    public void setRestoreDbUser(String restoreDbUser) {
        this.restoreDbUser = restoreDbUser;
    }

    public String getRestoreDbPassword() {
        return restoreDbPassword;
    }

    public void setRestoreDbPassword(String restoreDbPassword) {
        this.restoreDbPassword = restoreDbPassword;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }
}
