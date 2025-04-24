package com.example.demo.Controller;

import com.example.demo.Model.Backup.BackupRequest;
import com.example.demo.Model.Backup.BackupSchedule;
import com.example.demo.Model.Backup.BackupService;
import com.example.demo.Model.Common.StorageSettingsService;
import com.example.demo.Model.Common.StorageTarget;
import com.example.demo.Model.Common.StorageTargetRepository;
import com.example.demo.Security.AesEncryptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @Autowired
    private StorageTargetRepository repository;

    @Autowired
    private AesEncryptor aesEncryptor;


    @PostMapping(value = "/execute", consumes = "application/json")
    public ResponseEntity<Map<String, String>> executeBackup(@RequestBody BackupRequest request) throws Exception {
        Map<String, String> response = new HashMap<>();

        String type = request.getStorageType();
        String name = request.getNameSelect();
        StorageTarget target = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }

        switch (type) {
            case "LOCAL" -> {
                request.setBackupLocation(decrypted.get("directory"));
            }
            case "FTP" -> {
                Map<String, String> ftpParams = new HashMap<>();
                ftpParams.put("ftpServer", decrypted.get("ftp_host"));
                ftpParams.put("ftpUser", decrypted.get("ftp_user"));
                ftpParams.put("ftpPassword", decrypted.get("ftp_password"));
                ftpParams.put("ftpDirectory", decrypted.get("ftp_directory"));
                request.setStorageParams(ftpParams);
            }
        }

        backupService.startBackup(request);
        response.put("message", "Backup started");
        return ResponseEntity.ok(response);
    }

   @PostMapping(value = "/save", consumes = "application/json")

    public ResponseEntity<?> saveSettings(@RequestBody BackupSchedule backupSchedule) throws Exception {
       String type = backupSchedule.getType();

       if ("database".equals(type)) {

           String storageType = backupSchedule.getStorageType2();
           String name = backupSchedule.getNameSelect2();
           StorageTarget target = repository.findByName(name)
                   .orElseThrow(() -> new RuntimeException("Not Found"));

           Map<String, String> decrypted = new HashMap<>();
           for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
               decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
           }

           if ("LOCAL".equals(storageType)) {
                   backupSchedule.setBackupLocation2(decrypted.get("directory"));
               } else if ("FTP".equals(storageType)) {
                   Map<String, String> ftpParams = new HashMap<>();
                   ftpParams.put("ftpServer", decrypted.get("ftp_host"));
                   ftpParams.put("ftpUser", decrypted.get("ftp_user"));
                   ftpParams.put("ftpPassword", decrypted.get("ftp_password"));
                   ftpParams.put("ftpDirectory", decrypted.get("ftp_directory"));
                   backupSchedule.setStorageParams2(ftpParams);
               }

           backupService.save(backupSchedule);

       } else if ("file".equals(type)) {
           String name = backupSchedule.getNameSelect2();
           StorageTarget target = repository.findByName(name)
                   .orElseThrow(() -> new RuntimeException("Not Found"));

           Map<String, String> decrypted = new HashMap<>();
           for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
               decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
           }
           Map<String, String> ftpParams = new HashMap<>();
           ftpParams.put("ftpServer", decrypted.get("ftp_host"));
           ftpParams.put("ftpUser", decrypted.get("ftp_user"));
           ftpParams.put("ftpPassword", decrypted.get("ftp_password"));
           ftpParams.put("ftpDirectory", decrypted.get("ftp_directory"));
           backupSchedule.setStorageParams2(ftpParams);

           backupService.save(backupSchedule);
       }
        return ResponseEntity.ok(Collections.singletonMap("message", "Saved settings"));
    }

    @GetMapping("/listDatabases")
    public List<String> listDatabases(@RequestParam String dbServer,
                                      @RequestParam String dbUser,
                                      @RequestParam String dbPassword) {
        return backupService.getDatabases(dbServer, dbUser, dbPassword);
        }
    }
