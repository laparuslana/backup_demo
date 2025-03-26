package com.example.demo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "backup_settings")
public class BackupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "day")
    private String day;

    @Column(name = "time")
    private String time;

    @Column(name = "cluster_server")
    private String clusterServer;

    @Column(name = "db_server")
    private String dbServer;

    @Column(name = "db_user")
    private String dbUser;

    @Column(name = "db_password")
    private String dbPassword;

    @Column(name = "backup_location")
    private String backupLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(String clusterServer) {
        this.clusterServer = clusterServer;
    }

    public String getDbServer() {
        return dbServer;
    }

    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation) {
        this.backupLocation = backupLocation;
    }
}
