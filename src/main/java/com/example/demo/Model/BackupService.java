package com.example.demo.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

@Service
public class BackupService {

    @Autowired
    private BackupHistoryRepository backupHistoryRepository;

    public void startBackup(BackupRequest request) {
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
                request.getClusterPassword()
        );

        executeBackupCommand(command, request.getDatabaseName(), request.getBackupLocation(), retentionPeriod);
    }

    private String buildBackupCommand(String clusterServer, String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation, String retentionPeriod, boolean clusterAdmin, String clusterUsername, String clusterPassword) {
        return String.format("bash src/main/resources/scripts/backupManually.sh %s %s %s %s %s %s %s %b %s %s",
                clusterServer, databaseName, dbServer, dbUser, dbPassword, backupLocation, retentionPeriod, clusterAdmin,
                clusterUsername != null ? clusterUsername : "",
                clusterPassword != null ? clusterPassword : "");
    }

    private void executeBackupCommand(String command, String databaseName, String backup_location, String retentionPeriod) {
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
}


