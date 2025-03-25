package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BackupScheduler {

    @Autowired
    private BackupRepository backupRepository;

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void checkAndExecuteBackups() {
        List<ScheduledBackup> backups = backupRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (ScheduledBackup backup : backups) {
            if (backup.getNextBackupTime().isBefore(now)) {
                executeBackup(backup);
                backup.setNextBackupTime(calculateNextBackupTime(backup.getFrequency()));
                backupRepository.save(backup);
            }
        }
    }

    private void executeBackup(ScheduledBackup backup) {
        try {
            // Replace with actual backup command
            String command = "mysqldump -u root -pYourPassword " + backup.getDatabaseName() + " > /backups/" + backup.getDatabaseName() + ".sql";
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            process.waitFor();
            System.out.println("Backup completed for database: " + backup.getDatabaseName());
        } catch (Exception e) {
            System.err.println("Backup failed: " + e.getMessage());
        }
    }

    private LocalDateTime calculateNextBackupTime(String frequency) {
        LocalDateTime now = LocalDateTime.now();
        switch (frequency) {
            case "DAILY":
                return now.plusDays(1);
            case "WEEKLY":
                return now.plusWeeks(1);
            default:
                return now.plusHours(24);
        }
    }
}
