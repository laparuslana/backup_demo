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

public RestoreHistoryDTO(RestoreHistory restore) {
    this.status = restore.getStatus();
    this.restore_time = restore.getRestore_time();

    this.username = restore.getUser().getUsername();
}
    }