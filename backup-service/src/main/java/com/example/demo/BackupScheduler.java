package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class BackupScheduler {

    @Autowired
    private BackupRepository backupScheduleRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Scheduled(fixedRate = 60000)
    public void scheduleBackups() {
        System.out.println("Checking schedule");
        List<ScheduledBackup> schedules = backupScheduleRepository.findAll();

        for (ScheduledBackup schedule : schedules) {
            scheduleBackup(schedule);
        }
    }

    private void scheduleBackup(ScheduledBackup schedule) {
        String cronExpression = convertToCron(schedule.getFrequency(), schedule.getDay(), schedule.getTime());

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable backupTask = () -> executeBackup(schedule);

        long initialDelay = calculateInitialDelay(schedule.getDay(), schedule.getTime());
        long period = calculatePeriod(schedule.getFrequency());

        scheduler.scheduleAtFixedRate(backupTask, initialDelay, period, TimeUnit.MILLISECONDS);
        System.out.println("Task " + schedule.getDatabaseName() + " с cron: " + cronExpression);

        try {
            Process process = Runtime.getRuntime().exec("crontab -l");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder existingCronJobs = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                existingCronJobs.append(line).append("\n");
            }

            String cronJob = String.format("%s /bin/bash backup-service/src/main/resources/scripts/backupAuto.sh %s %s %s",
                    cronExpression, schedule.getDatabaseName(), schedule.getDbServer(), schedule.getBackupLocation());

            existingCronJobs.append(cronJob).append("\n");

            Files.write(Paths.get("/tmp/my_cron_jobs"), existingCronJobs.toString().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            Runtime.getRuntime().exec("crontab /tmp/my_cron_jobs");

            System.out.println("Backup task scheduled: " + cronJob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long calculateInitialDelay(String day, LocalTime time) {
        LocalDate now = LocalDate.now();

        DayOfWeek targetDay;
        if (day == null || day.isBlank()) {
            targetDay = now.getDayOfWeek();
        } else {
            try {
                targetDay = DayOfWeek.valueOf(day.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid day value: " + day, e);
            }
        }

        LocalDate targetDate = now.with(TemporalAdjusters.nextOrSame(targetDay));

        LocalDateTime scheduledTime = LocalDateTime.of(targetDate, time);
        LocalDateTime currentTime = LocalDateTime.now();

        long delay = Duration.between(currentTime, scheduledTime).toSeconds();
        return delay > 0 ? delay : 0;
    }

    public static long calculatePeriod(String frequency) {
        switch (frequency.toLowerCase()) {
            case "hourly":
                return 3600;
            case "daily":
                return 86400;
            case "weekly":
                return 604800;
            default:
                throw new IllegalArgumentException("Unsupported frequency: " + frequency);
        }
    }
    private void executeBackup(ScheduledBackup schedule) {

        JsonNode storageParams = schedule.getStorageParams();
        String ftpServer = storageParams != null ? storageParams.get("ftpServer").asText() : "";
        String ftpUser = storageParams != null ? storageParams.get("ftpUser").asText() : "";
        String ftpPassword = storageParams != null ? storageParams.get("ftpPassword").asText() : "";
        String ftpDirectory = storageParams != null ? storageParams.get("ftpDirectory").asText() : "";

        String command = String.format(
                "bash backup-service/src/main/resources/scripts/backupAuto.sh %s %s %s %s %s %s %s %b %s %s %s %s %s %s %s",
                schedule.getClusterServer(), schedule.getDatabaseName(), schedule.getDbServer(), schedule.getDbUser(), schedule.getDbPassword(), schedule.getBackupLocation(), schedule.getDaysKeep(),
                schedule.isClusterAdmin(),
                schedule.getClusterUsername(),
                schedule.getClusterPassword(),
                schedule.getStorageType(), ftpServer, ftpUser, ftpPassword, ftpDirectory
        );

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

            logBackup(schedule.getDatabaseName(), status, schedule.getBackupLocation(), schedule.getDaysKeep());

        } catch (IOException | InterruptedException e) {
            status = "❌ Error executing backup: " + e.getMessage();
            logBackup(schedule.getDatabaseName(), status, schedule.getBackupLocation(), schedule.getDaysKeep());
        }
        }

    private void logBackup(String databaseName, String status, String backup_location, String retention_days) {
        HistoryTask backupHistory = new HistoryTask();
        backupHistory.setDatabase_name(databaseName);
        backupHistory.setStatus(status);
        backupHistory.setBackup_time(LocalDateTime.now());
        backupHistory.setBackup_location(backup_location);
        backupHistory.setRetention_period(retention_days);
        historyRepository.save(backupHistory);
    }

    private String convertToCron(String frequency, String day, LocalTime time) {
        System.out.println("DEBUG: " + time);
        String timestr = time.format(DateTimeFormatter.ofPattern("mm:HH"));
        String[] timeParts = timestr.split(":");
        String minutes = timeParts[0];
        String hours = timeParts[1];

        System.out.println("DEBUG: " + hours + minutes);
        switch (frequency) {
            case "daily":
                return String.format("%s %s * * *", minutes, hours);
            case "weekly":
                return String.format("%s %s * * %s", minutes, hours, convertDayToCron(day));
            case "monthly":
                return String.format("%s %s %s * *", minutes, hours, day);
            default:
                throw new IllegalArgumentException("Unknown frequency: " + frequency);
        }
    }

    private String convertDayToCron(String day) {
        Map<String, String> daysMap = Map.of(
                "Monday", "1",
                "Tuesday", "2",
                "Wednesday", "3",
                "Thursday", "4",
                "Friday", "5",
                "Saturday", "6",
                "Sunday", "7"
        );
        return daysMap.getOrDefault(day, "*");
    }
}

