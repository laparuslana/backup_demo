package com.example.mainapp.Model.Backup;


import java.time.LocalDateTime;

public class BackupHistoryDTO {
    private String status;
    private LocalDateTime backup_time;
    private String database_name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBackup_time() {
        return backup_time;
    }

    public void setBackup_time(LocalDateTime backup_time) {
        this.backup_time = backup_time;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public void setDatabase_name(String database_name) {
        this.database_name = database_name;
    }

    public String getBackup_location() {
        return backup_location;
    }

    public void setBackup_location(String backup_location) {
        this.backup_location = backup_location;
    }

    public String getRetention_period() {
        return retention_period;
    }

    public void setRetention_period(String retention_period) {
        this.retention_period = retention_period;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String backup_location;
    private String retention_period;
    private String username;

    public BackupHistoryDTO(BackupHistory backup) {
        this.status = backup.getStatus();
        this.backup_time = backup.getBackup_time();
        this.database_name = backup.getDatabase_name();
        this.backup_location = backup.getBackup_location();
        this.retention_period = backup.getRetention_period();
        this.username = backup.getUser().getUsername();
    }

}
