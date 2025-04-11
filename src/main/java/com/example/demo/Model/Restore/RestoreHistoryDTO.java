package com.example.demo.Model.Restore;

import java.time.LocalDateTime;

public class RestoreHistoryDTO {

private String status;

    public LocalDateTime getRestore_time() {
        return restore_time;
    }

    public void setRestore_time(LocalDateTime restore_time) {
        this.restore_time = restore_time;
    }

    private LocalDateTime restore_time;

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

private String username;
private String backup_file;

    public String getSource_database() {
        return source_database;
    }

    public void setSource_database(String source_database) {
        this.source_database = source_database;
    }

    public String getBackup_file() {
        return backup_file;
    }

    public void setBackup_file(String backup_file) {
        this.backup_file = backup_file;
    }

    private String source_database;

public RestoreHistoryDTO(RestoreHistory restore) {
    this.status = restore.getStatus();
    this.restore_time = restore.getRestore_time();
this.backup_file = restore.getBackup_file();
this.source_database = restore.getSource_database();
    this.username = restore.getUser().getUsername();
}
    }