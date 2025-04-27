package com.example.demo.Model.Restore;


import com.example.demo.Model.Common.*;
import com.example.demo.Model.UserManagement.MyAppUser;
import com.example.demo.Model.UserManagement.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RestoreService {

    @Autowired
    private BafSettingsRepository bafSettingsRepository;

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

    public List<String> listArchiveFilesFromFtp(String host, String user, String password, String directory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/scripts/listArchivesFtp.sh", host, user, password, directory);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".tar.gz")) {
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

        public String deleteTest(String testDb, String server, String user, String password) throws IOException, InterruptedException {
            StringBuilder output = new StringBuilder();
            String status;

            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/scripts/delete.sh", testDb, server, user, password);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                status = "✅ Test database deleted successfully!";
            } else {
                status = "❌ Delete failed with exit code: " + exitCode;
            }

            return status;
        }

    public String switchDb(String clusterAd, String clusterUser, String clusterPass, String infobase) throws IOException, InterruptedException {
        StringBuilder output = new StringBuilder();
        String status;
        String bafPath = "";
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<BafSettings> settings = bafSettingsRepository.findByUserId(user.getId());

        if (settings.isPresent()) {
            BafSettings baf = settings.get();
            bafPath = baf.getBafPath();
        }
        ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/scripts/switch.sh", bafPath, clusterAd, clusterUser, clusterPass, infobase);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            status = "✅ Switch successfully!";
        } else {
            status = "❌ Delete failed with exit code: " + exitCode;
        }

        return status;
    }

    @Autowired
    private RestoreHistoryRepository restoreHistoryRepository;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    public String restore(RestoreRequest restoreRequest) {
        String bafPath = "";
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<BafSettings> settings = bafSettingsRepository.findByUserId(user.getId());

        if (settings.isPresent()) {
            BafSettings baf = settings.get();
            bafPath = baf.getBafPath();
        }

        String command = buildRestoreCommand(
                bafPath,
                restoreRequest.getTestDbName(),
                restoreRequest.getRestoreDbServer(),
                restoreRequest.getRestoreDbUser(),
                restoreRequest.getRestoreDbPassword(),
                restoreRequest.getBackupFile(),
                restoreRequest.isRes_clusterAdmin(),
                restoreRequest.getRes_clusterUsername(),
                restoreRequest.getRes_clusterPassword(),
                restoreRequest.getStorageParams(),
                restoreRequest.getFullPath(),
                restoreRequest.getRes_storageType()
        );

        String backupFileName = restoreRequest.getBackupFile();

        String sourceDatabase = backupFileName.contains("_") ?
                backupFileName.substring(0, backupFileName.indexOf('_')) : "unknown";

        String logs = executeRestoreCommand(command, user, backupFileName, sourceDatabase);
        return "STATUS" + logs;
    }


    private String buildRestoreCommand(String bafPath, String testDbName, String dbServer, String dbUser, String dbPassword, String backupFile, boolean clusterAdmin, String clusterUsername, String clusterPassword, Map<String, String> storageParams, String fullPath, String storageType) {
        try {

            String ftpServer = storageParams != null ? storageParams.get("ftpServer") : null;
            String ftpUser = storageParams != null ? storageParams.get("ftpUser") : null;
            String ftpPassword = storageParams != null ? storageParams.get("ftpPassword") : null;
            String ftpDirectory = storageParams != null ? storageParams.get("ftpDirectory") : null;

            return String.format("bash src/main/resources/scripts/restoreBackup.sh %s %s %s %s %s %s %b %s %s %s %s %s %s %s %s",
                    bafPath, testDbName, dbServer, dbUser, dbPassword, backupFile, clusterAdmin, clusterUsername, clusterPassword,
                    ftpServer, ftpUser, ftpPassword, ftpDirectory, fullPath, storageType);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while extracting storage parameters", e);
        }
    }


    private String executeRestoreCommand(String command, MyAppUser user, String backupFile, String sourceDatabase) {
        LocalDateTime initStartTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = initStartTime.format(formatter);
        //ProgressSession.setRestoreProgress(new ProgressRestoreDTO(0, "Backup...", backupFile, startTime, LocalDateTime.now(), sourceDatabase));

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

            logRestore(status, user, backupFile, sourceDatabase);
           // ProgressSession.setRestoreProgress(new ProgressRestoreDTO(100, "Done", backupFile, startTime, LocalDateTime.now(), sourceDatabase));

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logRestore(status, user, backupFile, sourceDatabase);
           // ProgressSession.setRestoreProgress(new ProgressRestoreDTO(0, "Fail", backupFile, startTime, LocalDateTime.now(), sourceDatabase));
        }
        return output.toString();
    }

    private void logRestore(String status, MyAppUser user, String backupFile, String sourceDatabase) {
        RestoreHistory restoreHistory = new RestoreHistory();
        restoreHistory.setStatus(status);
        restoreHistory.setRestore_time(LocalDateTime.now());
        restoreHistory.setUser(user);
        restoreHistory.setBackup_file(backupFile);
        restoreHistory.setSource_database(sourceDatabase);
        restoreHistoryRepository.save(restoreHistory);
    }

    public List<String> getInfobases(String clusterAd, String clusterUser, String clusterPass) {
        List<String> databases = new ArrayList<>();
        String bafPath = "";
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<BafSettings> settings = bafSettingsRepository.findByUserId(user.getId());

        if (settings.isPresent()) {
            BafSettings baf = settings.get();
            bafPath = baf.getBafPath();
        }
        try {
            String[] command = {
                    "/bin/bash", "src/main/resources/scripts/listInfobases.sh",
                    bafPath, clusterAd, clusterUser, clusterPass
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

    public String restoreFileDb(String restorePath, String restoreFile, String ftpHost, String ftpUser, String ftpPassword, String ftpDirectory) {
      String command = String.format("bash src/main/resources/scripts/restoreFileDb.sh %s %s %s %s %s %s",
                restorePath, restoreFile, ftpHost, ftpUser, ftpPassword, ftpDirectory);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        String sourceDatabase = restoreFile.contains("_") ?
                restoreFile.substring(0, restoreFile.indexOf('_')) : "unknown";

        String logs = executeRestoreFileCommand(command, user, restoreFile, sourceDatabase);
        return "STATUS" + logs;
    }

    private String executeRestoreFileCommand(String command, MyAppUser user, String backupFile, String sourceDatabase) {
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

            logRestore(status, user, backupFile, sourceDatabase);

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logRestore(status, user, backupFile, sourceDatabase);
        }
        return output.toString();
    }
}