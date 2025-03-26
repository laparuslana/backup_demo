package com.example.demo.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class BackupService {

    public String startBackup(BackupRequest request) {
        String command = buildBackupCommand(
                request.getClusterServer(),
                request.getDatabaseName(),
                request.getDbServer(),
                request.getDbUser(),
                request.getDbPassword(),
                request.getBackupLocation()
        );

        return executeBackupCommand(command);
    }

    private String buildBackupCommand(String clusterServer, String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation) {
        return String.format("bash src/main/resources/scripts/backupManually.sh %s %s %s %s %s %s ",
                clusterServer, databaseName, dbServer, dbUser, dbPassword, backupLocation);
    }

    private String executeBackupCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "✅ Backup executed successfully!\n" + output;
            } else {
                return "❌ Backup failed with exit code: " + exitCode;
            }
        } catch (IOException | InterruptedException e) {
            return "❌ Error executing backup: " + e.getMessage();
        }
    }

    @Autowired
    private BackupScheduleRepository backupScheduleRepository;

    public void save(BackupSchedule schedule) {
        backupScheduleRepository.save(schedule);
    }
}


