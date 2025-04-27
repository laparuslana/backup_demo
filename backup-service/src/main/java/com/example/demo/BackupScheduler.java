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
import java.util.HashMap;
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
        private AesEncryptor aesEncryptor;

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
                Map<String, String> scheduleParams = schedule.getScheduleParams();
                String cronExpression = convertToCron(scheduleParams.get("frequency"), scheduleParams.get("day"), scheduleParams.get("time"));
                String projectRoot = Paths.get("").toAbsolutePath().toString();

                String cronJob;

                Map<String, String> decryptedMap = new HashMap<>();
                for (Map.Entry<String, String> entry : schedule.getDbParams().entrySet()) {
                    decryptedMap.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));

                }

                Map<String, String> decryptedFtp = new HashMap<>();
                if (schedule.getStorageParams2() == null) {
                    decryptedFtp.put("", "");
                } else {
                    for (Map.Entry<String, String> entry : schedule.getStorageParams2().entrySet()) {
                        decryptedFtp.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
                    }
                }
                    if ("database".equalsIgnoreCase(schedule.getType())) {
                        String scriptPath = projectRoot + "/backup-service/src/main/resources/scripts/backupAuto.sh";

                        cronJob = String.format("%s /bin/bash %s %s %s %s %s %s %s %s %s %s %s %s",
                                cronExpression,
                                scriptPath,
                                schedule.getDatabaseName2(),
                                decryptedMap.get("dbServer"),
                                decryptedMap.get("dbUser"),
                                decryptedMap.get("dbPassword"),
                                schedule.getBackupLocation2(),
                                schedule.getDaysKeep2(),
                                schedule.getStorageType2(),
                                decryptedFtp.get("ftpServer"),
                                decryptedFtp.get("ftpUser"),
                                decryptedFtp.get("ftpPassword"),
                                decryptedFtp.get("ftpDirectory")
                        );
                    } else if ("file".equalsIgnoreCase(schedule.getType())) {
                        String scriptPath = projectRoot + "/backup-service/src/main/resources/scripts/backupFileDb.sh";

                        cronJob = String.format("%s /bin/bash %s %s %s %s %s %s %s",
                                cronExpression,
                                scriptPath,
                                schedule.getFolderPath(),
                                schedule.getDaysKeep2(),
                                decryptedFtp.get("ftpServer"),
                                decryptedFtp.get("ftpUser"),
                                decryptedFtp.get("ftpPassword"),
                                decryptedFtp.get("ftpDirectory")
                        );
                    } else {
                        System.out.println("❌ Unknown backup type: " + schedule.getType());
                        return;
                    }


                    Process getCron = new ProcessBuilder("crontab", "-l").start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getCron.getInputStream()));
                    StringBuilder currentJobs = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        currentJobs.append(line).append("\n");
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
                    System.out.println("✅ Cron job installed: " + cronJob);
                } catch(Exception e){
                    throw new RuntimeException(e);
                }
        }

        private String convertToCron(String frequency, String day, String time) {
            String[] timeParts = time.split(":");
            String minutes = timeParts[1];
            String hours = timeParts[0];

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

