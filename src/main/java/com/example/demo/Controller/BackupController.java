package com.example.demo.Controller;

import com.example.demo.Model.Backup.BackupRequest;
import com.example.demo.Model.Backup.BackupSchedule;
import com.example.demo.Model.Backup.BackupService;
import com.example.demo.Model.Backup.StorageSettingsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;
import java.util.List;


@RestController
@RequestMapping("/api/backup")
@CrossOrigin(origins = "*")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService, StorageSettingsService storageSettingsService) {
        this.backupService = backupService;
        this.storageSettingsService = storageSettingsService;
    }

    private final StorageSettingsService storageSettingsService;

    @PostMapping(value = "/execute", consumes = "application/json")
    public ResponseEntity<Map<String, String>> executeBackup(@RequestBody BackupRequest request) throws IOException {
        Map<String, String> response = new HashMap<>();

        Map<String, Object> storageSettings = storageSettingsService.getSettingsForType(request.getStorageType());

        if ("local".equals(request.getStorageType())) {
            request.setBackupLocation((String) storageSettings.get("backupLocation"));
        } else if ("ftp".equals(request.getStorageType())) {
            Map<String, String> ftpParams = new HashMap<>();
            ftpParams.put("ftpServer", (String) storageSettings.get("ftpServer"));
            ftpParams.put("ftpUser", (String) storageSettings.get("ftpUser"));
            ftpParams.put("ftpPassword", (String) storageSettings.get("ftpPassword"));
            ftpParams.put("ftpDirectory", (String) storageSettings.get("ftpDirectory"));
            request.setStorageParams(ftpParams);
        }
        System.out.println("Loaded storage settings: " + storageSettings);
        System.out.println("Resolved storagePath: " + request.getBackupLocation());

        backupService.startBackup(request);
        response.put("message", "Backup started");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/save", consumes = "application/json")
    public ResponseEntity<?> saveSettings(@RequestBody BackupSchedule backupSchedule){
        backupService.save(backupSchedule);
        return ResponseEntity.ok(Collections.singletonMap("message", "Saved settings"));
    }

    @GetMapping("/listDatabases")
    public List<String> listDatabases(@RequestParam String dbServer,
                                      @RequestParam String dbUser,
                                      @RequestParam String dbPassword) {
        return backupService.getDatabases(dbServer, dbUser, dbPassword);
        }
    }
