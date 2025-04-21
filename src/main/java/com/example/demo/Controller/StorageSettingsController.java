package com.example.demo.Controller;

import com.example.demo.Model.Backup.BafSettings;
import com.example.demo.Model.Backup.BafSettingsRepository;
import com.example.demo.Model.Backup.StorageSettingsService;
import com.example.demo.Model.UserManagement.MyAppUser;
import com.example.demo.Model.UserManagement.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/storage-settings")
@CrossOrigin(origins = "*")
public class StorageSettingsController {

    private final StorageSettingsService storageSettingsService;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private BafSettingsRepository bafSettingsRepository;

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

    @PostMapping(value="/baf", consumes = "application/json")
    public ResponseEntity<?> saveBafSettings(@RequestBody BafSettings bafSettings) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        bafSettings.setUser(user);
        storageSettingsService.saveBafSettings(bafSettings);
        return ResponseEntity.ok(Map.of("message", "Settings saved"));

    }
    @GetMapping("/get-baf-settings")
    public ResponseEntity<?> getBafSettings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MyAppUser user = myAppUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<BafSettings> settings = bafSettingsRepository.findByUserId(user.getId());

        if (settings.isPresent()) {
            BafSettings baf = settings.get();
            Map<String, Object> response = new HashMap<>();
            response.put("bafType", baf.getBafType());
            response.put("bafPath", baf.getBafPath());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(Map.of());
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
