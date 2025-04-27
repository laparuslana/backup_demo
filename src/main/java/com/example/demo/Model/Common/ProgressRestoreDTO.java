package com.example.demo.Model.Common;

import java.time.LocalDateTime;

public class ProgressRestoreDTO {
    public ProgressRestoreDTO() {

    }
    public ProgressRestoreDTO(int percent, String status, String backupFile, String startTime, LocalDateTime updateTime, String sourceDatabase) {
        this.percent = percent;
        this.status = status;
        this.backupFile = backupFile;
        this.startTime = startTime;
        this.updateTime = updateTime;
        this.sourceDatabase = sourceDatabase;
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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    private int percent;
    private String status;
    private String backupFile;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String startTime;
    private LocalDateTime updateTime;

    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getBackupFile() {
        return backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }

    private String sourceDatabase;


}
