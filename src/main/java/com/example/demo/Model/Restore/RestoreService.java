package com.example.demo.Model.Restore;


import com.example.demo.Model.UserManagement.MyAppUser;
import com.example.demo.Model.UserManagement.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RestoreService {

    public List<String> listBackupFilesFromFtp(String host, String user, String password, String directory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/scripts/listFilesFtp.sh", host, user, password, directory);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".backup")) {
                    output.add(line.trim());
                }
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("FTP script failed with exit code: " + exitCode);
        }

        return output;
    }

    @Autowired
    private RestoreHistoryRepository restoreHistoryRepository;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

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
                restoreRequest.getRes_clusterPassword(),
                restoreRequest.getStorageParams(),
                restoreRequest.getFullPath()
        );

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        String logs = executeRestoreCommand(command, user);
        return "STATUS" + logs;
    }


    private String buildRestoreCommand(String clusterServer, String testDbName, String dbServer, String dbUser, String dbPassword, String backupFile, boolean clusterAdmin, String clusterUsername, String clusterPassword, Map<String, String> storageParams, String fullPath) {
        try {

            String ftpServer = storageParams != null ? storageParams.get("ftpServer") : "";
            String ftpUser = storageParams != null ? storageParams.get("ftpUser") : "";
            String ftpPassword = storageParams != null ? storageParams.get("ftpPassword") : "";
            String ftpDirectory = storageParams != null ? storageParams.get("ftpDirectory") : "";

            return String.format("bash src/main/resources/scripts/restoreBackup.sh %s %s %s %s %s %s %b %s %s %s %s %s %s %s",
                    clusterServer, testDbName, dbServer, dbUser, dbPassword, backupFile, clusterAdmin, clusterUsername, clusterPassword,
                    ftpServer, ftpUser, ftpPassword, ftpDirectory, fullPath);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while extracting storage parameters", e);
        }
    }


    private String executeRestoreCommand(String command, MyAppUser user) {
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

            logRestore(status, user);

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logRestore(status, user);
        }
        return output.toString();
    }

    private void logRestore(String status, MyAppUser user) {
        RestoreHistory restoreHistory = new RestoreHistory();
        restoreHistory.setStatus(status);
        restoreHistory.setRestore_time(LocalDateTime.now());
        restoreHistory.setUser(user);
        restoreHistoryRepository.save(restoreHistory);
    }
}