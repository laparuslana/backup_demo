package com.example.demo.Controller;

import com.example.demo.Model.Backup.StorageSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/storage-settings")
public class StorageSettingsController {

    private final StorageSettingsService storageSettingsService;

    public StorageSettingsController(StorageSettingsService storageSettingsService) {
        this.storageSettingsService = storageSettingsService;
    }

    @GetMapping("/load")
    public ResponseEntity<?> getAllSettings() {
        try {
            return ResponseEntity.ok(storageSettingsService.loadSettings());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not load settings"));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSettings(@RequestBody Map<String, Object> settings) {
        try {
            storageSettingsService.saveSettings(settings);
            return ResponseEntity.ok(Map.of("message", "Settings saved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not save settings"));
        }
    }

    @GetMapping(params = "type")
    public ResponseEntity<?> getSettingsForType(@RequestParam String type) {
        try {
            return ResponseEntity.ok(storageSettingsService.getSettingsForType(type));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
