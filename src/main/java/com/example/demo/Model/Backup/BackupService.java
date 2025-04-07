package com.example.demo.Model.Backup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class BackupService {

    @Autowired
    private BackupHistoryRepository backupHistoryRepository;

    public String startBackup(BackupRequest request) {
        String retentionPeriod = request.getRetentionPeriod() != null ? request.getRetentionPeriod() : "30";

        String command = buildBackupCommand(
                request.getClusterServer(),
                request.getDatabaseName(),
                request.getDbServer(),
                request.getDbUser(),
                request.getDbPassword(),
                request.getBackupLocation(),
                retentionPeriod,
                request.isClusterAdmin(),
                request.getClusterUsername(),
                request.getClusterPassword(),
                request.getStorageType(),
                request.getStorageParams()
        );
        String storagePath;
        if (Objects.equals(request.getStorageType(), "local")) {
            storagePath = request.getBackupLocation();
        } else if (Objects.equals(request.getStorageType(), "ftp")) {
            storagePath = request.getStorageParams().get("ftpDirectory");
        } else {
            throw new IllegalArgumentException("Unsupported storage type: " + request.getStorageType());
        }

        String logs = executeBackupCommand(command, request.getDatabaseName(), storagePath, retentionPeriod);
        return "STATUS" + logs;
    }


    private String buildBackupCommand(String clusterServer, String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation, String retentionPeriod, boolean clusterAdmin, String clusterUsername, String clusterPassword, String storageType, Map<String, String> storageParams) {
        try {

            String ftpServer = storageParams != null ? storageParams.get("ftpServer") : "";
            String ftpUser = storageParams != null ? storageParams.get("ftpUser") : "";
            String ftpPassword = storageParams != null ? storageParams.get("ftpPassword") : "";
            String ftpDirectory = storageParams != null ? storageParams.get("ftpDirectory") : "";

            return String.format("bash src/main/resources/scripts/backupManually.sh %s %s %s %s %s %s %s %b %s %s %s %s %s %s %s",
                    clusterServer, databaseName, dbServer, dbUser, dbPassword, backupLocation, retentionPeriod, clusterAdmin,
                    clusterUsername, clusterPassword, storageType, ftpServer, ftpUser, ftpPassword, ftpDirectory);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while extracting storage parameters", e);
        }
    }

    private String executeBackupCommand(String command, String databaseName, String backup_location, String retentionPeriod) {
        StringBuilder output = new StringBuilder();
        String status;

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            while ((line = stdError.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                status = "Backup executed successfully!\n";
            } else {
                status = "Backup failed with exit code: " + exitCode;
            }

            logBackup(databaseName, status, backup_location, retentionPeriod);

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logBackup(databaseName, status, backup_location, retentionPeriod);
        }
        return output.toString();
    }

    private void logBackup(String databaseName, String status, String backup_location, String retentionPeriod) {
        BackupHistory backupHistory = new BackupHistory();
        backupHistory.setDatabase_name(databaseName);
        backupHistory.setStatus(status);
        backupHistory.setBackup_time(LocalDateTime.now());
        backupHistory.setBackup_location(backup_location);
        backupHistory.setRetention_period(retentionPeriod);
        backupHistoryRepository.save(backupHistory);
    }

    @Autowired
    private BackupScheduleRepository backupScheduleRepository;

    public void save(BackupSchedule schedule) {
        backupScheduleRepository.save(schedule);
    }

    public List<String> getDatabases(String dbServer, String dbUser, String dbPassword) {
        List<String> databases = new ArrayList<>();
        try {
            String[] command = {
                    "/bin/bash", "src/main/resources/scripts/retrieveDatabases.sh",
                    dbServer, dbUser, dbPassword
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                databases.add(line);
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databases;
    }
}



