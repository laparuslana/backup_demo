package com.example.backup_service.Controller;

import com.example.backup_service.Model.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @PostMapping("/save")
    public ResponseEntity<String> saveBackupSettings(@RequestBody com.example.backup_service.Model.BackupHistory backupHistory) {
        backupService.saveBackupSettings(backupHistory);
        return ResponseEntity.ok("Backup settings saved and backup scheduled.");
    }
}

