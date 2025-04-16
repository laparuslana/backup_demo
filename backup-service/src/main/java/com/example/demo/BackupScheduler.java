package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
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

        @Scheduled(fixedRate = 60000)
        public void scheduleBackups() {
            System.out.println("Checking schedule");
            List<ScheduledBackup> schedules = backupScheduleRepository.findAll();

            for (ScheduledBackup schedule : schedules) {
                installCronJob(schedule);
            }
        }

        private void installCronJob(ScheduledBackup schedule) {
            try {
                String cronExpression = convertToCron(schedule.getFrequency(), schedule.getDay(), schedule.getTime());

                String projectRoot = Paths.get("").toAbsolutePath().toString();
                String scriptPath = projectRoot + "/backup-service/src/main/resources/scripts/backupAuto.sh";

                String cronJob = String.format("%s /bin/bash %s %s %s %s %s %s %s %s %s %s %s %s >> /tmp/auto-cron.log 2>&1",
                        cronExpression,
                        scriptPath,
                        schedule.getDatabaseName(),
                        schedule.getDbServer(),
                        schedule.getDbUser(),
                        schedule.getDbPassword(),
                        schedule.getBackupLocation(),
                        schedule.getDaysKeep(),
                        schedule.getStorageType(),
                        getJson(schedule.getStorageParams(), "ftpServer"),
                        getJson(schedule.getStorageParams(), "ftpUser"),
                        getJson(schedule.getStorageParams(), "ftpPassword"),
                        getJson(schedule.getStorageParams(), "ftpDirectory")
                );

                Process getCron = new ProcessBuilder("crontab", "-l").start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(getCron.getInputStream()));
                StringBuilder currentJobs = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    currentJobs.append(line).append("\n");
                    System.out.println("✅ Cron job installed: " + cronJob);
                }
                getCron.waitFor();

                if (currentJobs.toString().contains(cronJob)) {
                    System.out.println("⏩ Cron job already exists");
                    return;
                }

                currentJobs.append(cronJob).append("\n");

                Path tempCron = Files.createTempFile("my_cron", ".tmp");
                Files.write(tempCron, currentJobs.toString().getBytes());

                Process setCron = new ProcessBuilder("crontab", tempCron.toString()).start();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(setCron.getInputStream()));
                while ((line = errorReader.readLine()) != null) {
                    System.out.println("CRONTAB OUTPUT: " + line);
                }
                int exitCode = setCron.waitFor();
                System.out.println("CRONTAB INSTALL EXIT CODE: " + exitCode);

                Files.deleteIfExists(tempCron);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


            private String getJson(JsonNode node, String key) {
            return (node != null && node.has(key)) ? node.get(key).asText() : "";
        }

        private String convertToCron(String frequency, String day, LocalTime time) {
            String timeStr = time.format(DateTimeFormatter.ofPattern("mm HH"));
            String[] timeParts = timeStr.split(" ");
            String minutes = timeParts[0];
            String hours = timeParts[1];

            switch (frequency.toLowerCase()) {
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
                    "Sunday", "0"
            );
            return daysMap.getOrDefault(day, "*");
        }
}

