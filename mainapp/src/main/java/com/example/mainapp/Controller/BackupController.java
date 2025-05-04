package com.example.mainapp.Controller;

import com.example.mainapp.Model.Backup.BackupRequest;
import com.example.mainapp.Model.Backup.BackupSchedule;
import com.example.mainapp.Model.Backup.BackupService;
import com.example.mainapp.Model.SettingsManagement.StorageSettingsService;
import com.example.mainapp.Model.SettingsManagement.StorageTarget;
import com.example.mainapp.Model.SettingsManagement.StorageTargetRepository;
import com.example.mainapp.Security.AesEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private StorageSettingsService storageSettingsService;

    @PostMapping(value = "/execute", consumes = "application/json")
    public ResponseEntity<Map<String, String>> executeBackup(@RequestBody BackupRequest request) throws Exception {
        Map<String, String> response = new HashMap<>();

        String type = request.getStorageType();
        String name = request.getNameSelect();

        StorageTarget target = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = storageSettingsService.getStorageParams(target);

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

    public ResponseEntity<?> saveSettings(@RequestBody Map<String, Object> request) throws Exception {
       String type = (String) request.get("type");
       String storageType = (String) request.get("storageType2");
       Long storageId = Long.valueOf((String) request.get("storageSettingId"));
       StorageTarget target = repository.findById(storageId)
               .orElseThrow(() -> new RuntimeException("Not Found"));

       Map<String, String> decrypted = storageSettingsService.getStorageParams(target);

       if ("database".equals(type)) {

           BackupSchedule backupSchedule = new BackupSchedule();
           backupSchedule.setType(type);
           backupSchedule.setDatabaseName2((String) request.get("databaseName2"));
           backupSchedule.setStorageType2(storageType);
           backupSchedule.setStorageTarget(target);
           backupSchedule.setDaysKeep2((String) request.get("daysKeep2"));

           Map<String, String> encrypted = new HashMap<>();
           Map<String, String> dbParams = new HashMap<>();
           dbParams.put("dbServer", (String) request.get("dbServer2"));
           dbParams.put("dbUser", (String) request.get("dbUser2"));
           dbParams.put("dbPassword", (String) request.get("dbPassword2"));
           for (Map.Entry<String, String> entry : dbParams.entrySet()) {
               encrypted.put(entry.getKey(), aesEncryptor.encrypt(entry.getValue()));
           }
           backupSchedule.setDbParams(encrypted);

           getScheduleParams(request, backupSchedule);

           if ("LOCAL".equals(storageType)) {
                   backupSchedule.setBackupLocation2(decrypted.get("directory"));
               } else if ("FTP".equals(storageType)) {
               getFtpEncrypted(decrypted, backupSchedule);
           }

           backupService.save(backupSchedule);

       } else if ("file".equals(type)) {

           BackupSchedule backupSchedule = new BackupSchedule();
           getScheduleParams(request, backupSchedule);

           backupSchedule.setDaysKeep2((String) request.get("daysKeep2"));
           backupSchedule.setStorageType2(storageType);
           backupSchedule.setType(type);
           backupSchedule.setStorageTarget(target);

           getFtpEncrypted(decrypted, backupSchedule);
           backupSchedule.setFolderPath((String) request.get("folderPath"));

           backupService.save(backupSchedule);
       }
        return ResponseEntity.ok(Collections.singletonMap("message", "Saved settings"));
    }

    private void getFtpEncrypted(Map<String, String> decrypted, BackupSchedule backupSchedule) throws Exception {
        Map<String, String> ftpEncrypted = new HashMap<>();
        Map<String, String> ftpParams = new HashMap<>();
        ftpParams.put("ftpServer", decrypted.get("ftp_host"));
        ftpParams.put("ftpUser", decrypted.get("ftp_user"));
        ftpParams.put("ftpPassword", decrypted.get("ftp_password"));
        ftpParams.put("ftpDirectory", decrypted.get("ftp_directory"));
        for (Map.Entry<String, String> entry : ftpParams.entrySet()) {
            ftpEncrypted.put(entry.getKey(), aesEncryptor.encrypt(entry.getValue()));
        }

        backupSchedule.setStorageParams2(ftpEncrypted);
    }

    private void getScheduleParams(@RequestBody Map<String, Object> request, BackupSchedule backupSchedule) {
        Map<String, String> scheduleParams = new HashMap<>();
        scheduleParams.put("frequency", (String) request.get("frequency"));
        scheduleParams.put("day", (String) request.get("day"));
        scheduleParams.put("time", (String) request.get("time"));
        backupSchedule.setScheduleParams(scheduleParams);
    }

    @GetMapping("/listDatabases")
    public List<String> listDatabases(@RequestParam String dbServer,
                                      @RequestParam String dbUser,
                                      @RequestParam String dbPassword) {
        return backupService.getDatabases(dbServer, dbUser, dbPassword);
        }
    }
