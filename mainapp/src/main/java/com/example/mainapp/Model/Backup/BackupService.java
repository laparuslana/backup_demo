package com.example.mainapp.Model.Backup;


import com.example.mainapp.Model.SettingsManagement.ProgressDTO;
import com.example.mainapp.Model.SettingsManagement.ProgressSession;
import com.example.mainapp.Model.UserManagement.MyAppUser;
import com.example.mainapp.Model.UserManagement.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class BackupService {

    @Autowired
    private BackupHistoryRepository backupHistoryRepository;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    public String startBackup(BackupRequest request) {
        String retentionPeriod = request.getRetentionPeriod() != null ? request.getRetentionPeriod() : "30";

        String command = buildBackupCommand(
                request.getDatabaseName(),
                request.getDbServer(),
                request.getDbUser(),
                request.getDbPassword(),
                request.getBackupLocation(),
                retentionPeriod,
                request.getStorageType(),
                request.getStorageParams()
        );
        String storagePath;
        if (Objects.equals(request.getStorageType(), "LOCAL")) {
            storagePath = request.getBackupLocation();
        } else if (Objects.equals(request.getStorageType(), "FTP")) {
            storagePath = request.getStorageParams().get("ftpDirectory");
        } else {
            throw new IllegalArgumentException("Непідтримуваний тип сховища: " + request.getStorageType());
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено: " + username));

        String logs = executeBackupCommand(command, request.getDatabaseName(), storagePath, retentionPeriod, user);
        return "STATUS" + logs;
    }


    private String buildBackupCommand(String databaseName, String dbServer, String dbUser, String dbPassword, String backupLocation, String retentionPeriod, String storageType, Map<String, String> storageParams) {
        try {

            String ftpServer = storageParams != null ? storageParams.get("ftpServer") : null;
            String ftpUser = storageParams != null ? storageParams.get("ftpUser") : null;
            String ftpPassword = storageParams != null ? storageParams.get("ftpPassword") : null;
            String ftpDirectory = storageParams != null ? storageParams.get("ftpDirectory") : null;

            return String.format("bash /opt/myapp/scripts/backupManually.sh %s %s %s %s %s %s %s %s %s %s %s",
                    databaseName, dbServer, dbUser, dbPassword, backupLocation, retentionPeriod, storageType, ftpServer, ftpUser, ftpPassword, ftpDirectory);
        } catch (Exception e) {
            throw new IllegalArgumentException("Помилка під час вилучення параметрів сховища", e);
        }
    }

    private String executeBackupCommand(String command, String databaseName, String backup_location, String retentionPeriod, MyAppUser myAppUserId) {
        LocalDateTime initStartTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = initStartTime.format(formatter);

        ProgressSession.setProgress(new ProgressDTO(0, "Початок копіювання...", databaseName, startTime, LocalDateTime.now(), backup_location));

        StringBuilder output = new StringBuilder();
        String status;

        int[] simulatedProgress = {0};
        boolean[] hasRealProgress = {false};
        boolean[] shouldStop = {false};

        try {
            Process process = Runtime.getRuntime().exec(command);

            Thread progressThread = new Thread(() -> {
                while (!shouldStop[0] && simulatedProgress[0] < 90) {
                    try {
                        Thread.sleep(2000);
                        if (!hasRealProgress[0] && !shouldStop[0]) {
                            synchronized (simulatedProgress) {
                                simulatedProgress[0] += 2;
                                ProgressSession.setProgress(new ProgressDTO(
                                        simulatedProgress[0],
                                        "Обробка... (оцінка)",
                                        databaseName,
                                        startTime,
                                        LocalDateTime.now(),
                                        backup_location
                                ));
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            progressThread.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            Thread stdoutThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");

                        if (line.startsWith("[PROGRESS]")) {
                            try {
                                String[] parts = line.split(" ", 3);
                                int percent = Integer.parseInt(parts[1]);
                                synchronized (simulatedProgress) {
                                    hasRealProgress[0] = true;
                                    simulatedProgress[0] = percent;
                                }
                                ProgressSession.setProgress(new ProgressDTO(
                                        percent,
                                        "Обробка...",
                                        databaseName,
                                        startTime,
                                        LocalDateTime.now(),
                                        backup_location
                                ));
                            } catch (Exception e) {
                                System.err.println("Failed to parse progress: " + e.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading stdout: " + e.getMessage());
                }
            });
            stdoutThread.start();

            Thread stderrThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = stdError.readLine()) != null) {
                        output.append("Помилка: ").append(line).append("\n");
                        synchronized (simulatedProgress) {
                            ProgressSession.setProgress(new ProgressDTO(
                                    simulatedProgress[0],
                                    "❌ Помилка: " + line,
                                    databaseName,
                                    startTime,
                                    LocalDateTime.now(),
                                    backup_location
                            ));
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading stderr: " + e.getMessage());
                }
            });
            stderrThread.start();

            int exitCode = process.waitFor();

            shouldStop[0] = true;
            progressThread.interrupt();

            try {
                stdoutThread.join(5000);
                stderrThread.join(5000);
                progressThread.join(1000);
            } catch (InterruptedException e) {

            }

            if (exitCode == 0) {
                status = "Успішно!\n";
                ProgressSession.setProgress(new ProgressDTO(100, "✅ Готово", databaseName, startTime, LocalDateTime.now(), backup_location));
            } else {
                status = "Не вдалося: " + exitCode;
                ProgressSession.setProgress(new ProgressDTO(0, "❌ Не вдалося", databaseName, startTime, LocalDateTime.now(), backup_location));
            }

            logBackup(databaseName, status, backup_location, retentionPeriod, myAppUserId);

        } catch (IOException | InterruptedException e) {
            status = "❌ Помилка: " + e.getMessage();
            logBackup(databaseName, status, backup_location, retentionPeriod, myAppUserId);
            ProgressSession.setProgress(new ProgressDTO(0, "❌ Невдача", databaseName, startTime, LocalDateTime.now(), backup_location));
        }

        return output.toString();
    }

//    private String executeBackupCommand(String command, String databaseName, String backup_location, String retentionPeriod, MyAppUser myAppUserId) {
//        LocalDateTime initStartTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String startTime = initStartTime.format(formatter);
//        ProgressSession.setProgress(new ProgressDTO(0, "Копіювання...", databaseName, startTime, LocalDateTime.now(), backup_location));
//
//        StringBuilder output = new StringBuilder();
//        String status;
//
//        try {
//            Process process = Runtime.getRuntime().exec(command);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//
//                if (line.startsWith("[PROGRESS]")) {
//                    try {
//                        String[] parts = line.split(" ", 3);
//                        int percent = Integer.parseInt(parts[1]);
//                        ProgressSession.setProgress(new ProgressDTO(percent, "Обробка...", databaseName, startTime, LocalDateTime.now(), backup_location));
//
//                        Thread.sleep(30000);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//
//            while ((line = stdError.readLine()) != null) {
//                output.append("Помилка: ").append(line).append("\n");
//                ProgressSession.setProgress(new ProgressDTO(0, "❌ Помилка: " + line, databaseName, startTime, LocalDateTime.now(), backup_location));
//            }
//
//
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                status = "Успішно!\n";
//            } else {
//                status = "Не вдалося: " + exitCode;
//            }
//
//            logBackup(databaseName, status, backup_location, retentionPeriod, myAppUserId);
//            ProgressSession.setProgress(new ProgressDTO(100, "Готово", databaseName, startTime, LocalDateTime.now(), backup_location));
//
//
//        } catch (IOException | InterruptedException e) {
//            status = "❌ Помилка: " + e.getMessage();
//            logBackup(databaseName, status, backup_location, retentionPeriod, myAppUserId);
//            ProgressSession.setProgress(new ProgressDTO(0, "Невдача", databaseName, startTime, LocalDateTime.now(), backup_location));
//        }
//
//        return output.toString();
//    }

    private void logBackup(String databaseName, String status, String backup_location, String retentionPeriod, MyAppUser myAppUserId) {
        BackupHistory backupHistory = new BackupHistory();
        backupHistory.setDatabase_name(databaseName);
        backupHistory.setStatus(status);
        backupHistory.setBackup_time(LocalDateTime.now());
        backupHistory.setBackup_location(backup_location);
        backupHistory.setRetention_period(retentionPeriod);
        backupHistory.setUser(myAppUserId);
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
                    "/bin/bash", "/opt/myapp/scripts/retrieveDatabases.sh",
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



