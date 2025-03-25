package com.example.demo.Controller;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/api")
//public class BackupController {
//
//    @Autowired
//    private BackupService backupService;
//
//    @PostMapping("/save")
//    public ResponseEntity<String> saveBackupSettings(@RequestBody com.example.backup_service.Model.BackupHistory backupHistory) {
//        backupService.saveBackupSettings(backupHistory);
//        return ResponseEntity.ok("Backup settings saved and backup scheduled.");
//    }
//}

import com.example.demo.Model.BackupService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.Model.BackupRequest;

import java.util.HashMap;
import java.util.Map;


@RestController
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping(value = "/api/backup/execute", consumes = "application/json")
    public ResponseEntity<Map<String, String>> executeBackup(@RequestBody BackupRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backup started");

        backupService.startBackup(request);
        return ResponseEntity.ok(response);
    }
}
