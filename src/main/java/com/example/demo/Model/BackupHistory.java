package com.example.demo.Model;

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

    @Column(name = "retention_period", nullable = false)
    private String retention_period;

    public String getRetention_period() {
        return retention_period;
    }

    public void setRetention_period(String retention_period) {
        this.retention_period = retention_period;
    }

    public String getBackup_location() {
        return backup_location;
    }

    public void setBackup_location(String backup_location) {
        this.backup_location = backup_location;
    }

    @Column(name = "storage_path")
    private String backup_location;

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
