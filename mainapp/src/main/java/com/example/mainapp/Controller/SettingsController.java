package com.example.mainapp.Controller;


import com.example.mainapp.Model.SettingsManagement.*;
import com.example.mainapp.Model.UserManagement.MyAppUser;
import com.example.mainapp.Model.UserManagement.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    private final StorageSettingsService storageSettingsService;

    @Autowired
    private StorageTargetRepository repository;

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private BafSettingsRepository bafSettingsRepository;

    public SettingsController(StorageSettingsService storageSettingsService) {
        this.storageSettingsService = storageSettingsService;
    }

    @PostMapping("/storage-settings-save")
    public ResponseEntity<?> saveSettings(@RequestBody StorageDTO dto) {
        try {
            storageSettingsService.saveSettings(dto);
            return ResponseEntity.ok(Map.of("message", "Settings saved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not save settings"));
        }
    }

    @PostMapping(value="/baf-settings-save", consumes = "application/json")
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

    @GetMapping("/get-storage-settings")
    public List<Map<String, Object>> getStorageSettings(@RequestParam String type) {
        return repository.findAllByType(type)
                .stream()
                .map(target -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", target.getId());
                            map.put("name", target.getName());
                            return map;
                        })
                .collect(Collectors.toList());
    }
}
