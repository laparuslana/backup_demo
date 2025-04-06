package com.example.demo.Controller;

import com.example.demo.Model.Backup.BackupRequest;
import com.example.demo.Model.Backup.BackupSchedule;
import com.example.demo.Model.Backup.BackupService;
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

    @PostMapping(value = "/execute", consumes = "application/json")
    public ResponseEntity<Map<String, String>> executeBackup(@RequestBody BackupRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backup started");
        backupService.startBackup(request);
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
