package com.example.demo.Model.Backup;

import com.example.demo.Security.JsonNodeConverter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Base64;

@Entity
@Table(name = "backup_settings")
public class BackupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "folder_path", nullable = true)
    private String folderPath;

    @Column(name = "database_name")
    private String databaseName2;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "day")
    private String day;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "db_server")
    private String dbServer2;

    @Column(name = "db_user")
    private String dbUser2;

    @Column(name = "db_password")
    private String dbPassword2;

    @Column(name = "backup_location")
    private String backupLocation2;

    @Column(name = "retention_period")
    private String daysKeep2;


    @Column(name = "storage_params", columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode storageParams2;

    @Column(name = "storage_type", nullable = false)
    private String storageType2;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getStorageType2() {
        return storageType2;
    }

    public void setStorageType2(String storageType2) {
        this.storageType2 = storageType2;
    }

    public JsonNode getStorageParams2() {
        return storageParams2;
    }

    public void setStorageParams2(JsonNode storageParams2) {
        this.storageParams2 = storageParams2;
    }

    private String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    private String decrypt(String encryptedData) {
        return new String(Base64.getDecoder().decode(encryptedData));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseName2() {
        return databaseName2;
    }

    public void setDatabaseName2(String databaseName2) {
        this.databaseName2 = databaseName2;
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDbServer2() {
        return dbServer2;
    }

    public void setDbServer2(String dbServer2) {
        this.dbServer2 = dbServer2;
    }

    public String getDbUser2() {
        return dbUser2;
    }

    public void setDbUser2(String dbUser2) {
        this.dbUser2 = dbUser2;
    }

    public String getDbPassword2() {
        return dbPassword2;
    }

    public void setDbPassword2(String dbPassword2) {
        this.dbPassword2 = encrypt(dbPassword2);
    }

    public String getBackupLocation2() {
        return backupLocation2;
    }

    public void setBackupLocation2(String backupLocation2) {
        this.backupLocation2 = backupLocation2;
    }

    public String getDaysKeep2() {
        return daysKeep2;
    }

    public void setDaysKeep2(String daysKeep2) {
        this.daysKeep2 = daysKeep2;
    }

    public JsonNode getStorageParams() {
        return storageParams2;
    }

    public void setStorageParams(JsonNode storageParams2) {
        this.storageParams2 = storageParams2;
    }



}