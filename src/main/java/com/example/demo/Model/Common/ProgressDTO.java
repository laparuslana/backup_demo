package com.example.demo.Model.Common;

import java.time.LocalDateTime;

public class ProgressDTO {
    public ProgressDTO() {

    }
    public ProgressDTO(int percent, String status, String databaseName, String startTime, LocalDateTime updateTime, String backupLocation) {
        this.percent = percent;
        this.status = status;
        this.databaseName = databaseName;
        this.startTime = startTime;
        this.updateTime = updateTime;
        this.backupLocation = backupLocation;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    private int percent;
    private String status;
    private String databaseName;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation) {
        this.backupLocation = backupLocation;
    }

    private String startTime;
    private LocalDateTime updateTime;
    private String backupLocation;


}
