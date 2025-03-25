package com.example.demo;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "backup_settings")
public class ScheduledBackup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "frequency")
    private String frequency; // DAILY, WEEKLY

    @Column(name = "retention_days")
    private int retentionDays;

    @Column(name = "next_backup_time")
    private LocalDateTime nextBackupTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getNextBackupTime() {
        return nextBackupTime;
    }

    public void setNextBackupTime(LocalDateTime nextBackupTime) {
        this.nextBackupTime = nextBackupTime;
    }

    public int getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(int retentionDays) {
        this.retentionDays = retentionDays;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

// Getters and Setters
}
