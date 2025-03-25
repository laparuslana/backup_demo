package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api/backup")
//public class BackupController {
//
//    @Autowired
//    private BackupScheduler backupScheduler;
//
//    @PostMapping("/start")
//    public ResponseEntity<String> startBackup(@RequestBody ScheduledBackup scheduledBackup) {
//        backupScheduler.scheduleBackup(scheduledBackup);
//        return ResponseEntity.ok("Backup scheduled successfully.");
//    }
//}
//
