package com.example.demo.Model.Backup;

import com.example.demo.Model.Common.StorageTarget;
import com.example.demo.Security.JsonMapConverter;
import com.example.demo.Security.JsonNodeConverter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Base64;
import java.util.Map;

@Entity
@Table(name = "backup_settings")
public class BackupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "folder_path")
    private String folderPath;

    @Column(name = "database_name")
    private String databaseName2;

    @Column(name = "schedule_params", columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> scheduleParams;

    @Column(name = "db_params", columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> dbParams;

    @Column(name = "backup_location")
    private String backupLocation2;

    @Column(name = "retention_period")
    private String daysKeep2;

    @Column(name = "storage_params", columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> storageParams2;

    @Column(name = "storage_type", nullable = false)
    private String storageType2;

    public StorageTarget getStorageTarget() {
        return storageTarget;
    }

    public void setStorageTarget(StorageTarget storageTarget) {
        this.storageTarget = storageTarget;
    }

    public Map<String, String> getDbParams() {
        return dbParams;
    }

    public void setDbParams(Map<String, String> dbParams) {
        this.dbParams = dbParams;
    }

    public Map<String, String> getScheduleParams() {
        return scheduleParams;
    }

    public void setScheduleParams(Map<String, String> scheduleParams) {
        this.scheduleParams = scheduleParams;
    }

    @ManyToOne
    @JoinColumn(name = "storage_target_id")
    private StorageTarget storageTarget;


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

    public Map<String, String> getStorageParams2() {
        return storageParams2;
    }

    public void setStorageParams2(Map<String, String> storageParams2) {
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

    public Map<String, String> getStorageParams() {
        return storageParams2;
    }

    public void setStorageParams(Map<String, String> storageParams2) {
        this.storageParams2 = storageParams2;
    }

}