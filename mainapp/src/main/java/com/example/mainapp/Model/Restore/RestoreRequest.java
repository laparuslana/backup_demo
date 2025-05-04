package com.example.mainapp.Model.Restore;


import java.util.Map;

public class RestoreRequest {

    private String testDbName;
    private String restoreDbServer;
    private String restoreDbUser;
    private String restoreDbPassword;

    private String res_storageType;
    private String backupFile;
    private boolean res_clusterAdmin;
    private String res_clusterUsername;
    private String res_clusterPassword;

    private Map<String, String> storageParams;
    private String fullPath;

    public String getRes_nameSelect() {
        return res_nameSelect;
    }

    public void setRes_nameSelect(String res_nameSelect) {
        this.res_nameSelect = res_nameSelect;
    }

    private String res_nameSelect;

    public String getTest_database() {
        return test_database;
    }

    public void setTest_database(String test_database) {
        this.test_database = test_database;
    }

    private String test_database;

    public String getRes_storageType() {
        return res_storageType;
    }

    public void setRes_storageType(String res_storageType) {
        this.res_storageType = res_storageType;
    }

    public Map<String, String> getStorageParams() {
        return storageParams;
    }

    public void setStorageParams(Map<String, String> storageParams) {
        this.storageParams = storageParams;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }


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
