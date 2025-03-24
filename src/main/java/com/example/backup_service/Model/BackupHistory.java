package com.example.backup_service.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup_history")
public class BackupHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "backup_time")
    private LocalDateTime backup_time;

    @Column(name = "database_name")
    private String database_name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
