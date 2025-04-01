package com.example.demo.Controller;

import com.example.demo.Model.*;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

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

    @PostMapping(value = "/api/backup/save", consumes = "application/json")
    public ResponseEntity<?> saveSettings(@RequestBody BackupSchedule backupSchedule){
        backupService.save(backupSchedule);
        return ResponseEntity.ok(Collections.singletonMap("message", "Created user"));
    }

    @GetMapping("/api/backup/listDatabases")
    public List<String> listDatabases(@RequestParam String dbServer,
                                      @RequestParam String dbUser,
                                      @RequestParam String dbPassword) {
        return backupService.getDatabases(dbServer, dbUser, dbPassword);
        }
    }
