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

    public String startBackup(BackupRequest request) {
        String command = buildBackupCommand(
                request.getClusterServer(),
                request.getDatabaseName(),
                request.getDbServer(),
                request.getDbUser(),
                request.getDbPassword(),
                request.getBackupLocation()
        );

        return executeBackupCommand(command, request.getDatabaseName(), request.getBackupLocation());
    }

    private String buildBackupCommand(String clusterServer, String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation) {
        return String.format("bash src/main/resources/scripts/backupManually.sh %s %s %s %s %s %s ",
                clusterServer, databaseName, dbServer, dbUser, dbPassword, backupLocation);
    }

    private String executeBackupCommand(String command, String databaseName, String backup_location) {
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

            logBackup(databaseName, status, backup_location);
            return status + "/n" + output;

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logBackup(databaseName, status, backup_location);
            return status;
        }
    }
 private void logBackup(String databaseName, String status, String backup_location) {
        BackupHistory backupHistory = new BackupHistory();
        backupHistory.setDatabase_name(databaseName);
        backupHistory.setStatus(status);
        backupHistory.setBackup_time(LocalDateTime.now());
        backupHistory.setBackup_location(backup_location);
        backupHistoryRepository.save(backupHistory);
 }

    @Autowired
    private BackupScheduleRepository backupScheduleRepository;

    public void save(BackupSchedule schedule) {
        backupScheduleRepository.save(schedule);
    }
}


