package com.example.backup_service.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BackupService {

    @Autowired
    private BackupHistoryRepository backupHistoryRepository;

    private final RestTemplate restTemplate;

    public BackupService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void saveBackupSettings(BackupHistory backupHistory) {
        backupHistoryRepository.save(backupHistory);

        // Send an HTTP request to Backup Service to start scheduling
        String backupServiceUrl = "http://localhost:8083/api/backup/start";

        try {
            restTemplate.postForEntity(backupServiceUrl, backupHistory, String.class);
        } catch (Exception e) {
            System.out.println("Backup Service is not available: " + e.getMessage());
        }
    }
}

