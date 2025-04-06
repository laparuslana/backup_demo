package com.example.demo.Model.Restore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

@Service
public class RestoreService {

    @Autowired
    private RestoreHistoryRepository restoreHistoryRepository;

    public String restore(RestoreRequest restoreRequest) {
        String command = buildRestoreCommand(
                restoreRequest.getRes_clusterServer(),
                restoreRequest.getTestDbName(),
                restoreRequest.getRestoreDbServer(),
                restoreRequest.getRestoreDbUser(),
                restoreRequest.getRestoreDbPassword(),
                restoreRequest.getBackupFile(),
                restoreRequest.isRes_clusterAdmin(),
                restoreRequest.getRes_clusterUsername(),
                restoreRequest.getRes_clusterPassword()
        );

        String logs = executeRestoreCommand(command);
        return "STATUS" + logs;
    }


    private String buildRestoreCommand(String clusterServer, String testDbName, String dbServer, String dbUser, String dbPassword, String backupFile, boolean clusterAdmin, String clusterUsername, String clusterPassword) {
        return String.format("bash src/main/resources/scripts/restoreBackup.sh %s %s %s %s %s %s %b %s %s",
                clusterServer, testDbName, dbServer, dbUser, dbPassword, backupFile, clusterAdmin, clusterUsername, clusterPassword);
    }


    private String executeRestoreCommand(String command) {
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
                status = "Restore executed successfully!\n";
            } else {
                status = "Restore failed with exit code: " + exitCode;
            }

            logRestore(status);

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logRestore(status);
        }
        return output.toString();
    }

    private void logRestore(String status) {
        RestoreHistory restoreHistory = new RestoreHistory();
        restoreHistory.setStatus(status);
        restoreHistory.setRestore_time(LocalDateTime.now());
        restoreHistoryRepository.save(restoreHistory);
    }
}