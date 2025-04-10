package com.example.demo.Model.Backup;

import com.example.demo.Security.AesEncryptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Service
public class StorageSettingsService {

    @Autowired
    private AesEncryptor aesEncryptor;

    private final Path settingsFile = Paths.get("config/storage-settings.json");
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public Map<String, Object> loadSettings() throws IOException {
        if (!Files.exists(settingsFile)) {
            return new HashMap<>();
        }
        String content = Files.readString(settingsFile);
        return objectMapper.readValue(content, new TypeReference<>() {});
    }

    public void saveSettings(Map<String, Object> settings) throws Exception {
        Object rawPassword = settings.get("ftpPassword");
        String encrypted = aesEncryptor.encrypt((String) rawPassword);
        settings.put("ftpPassword", encrypted);
        Files.createDirectories(settingsFile.getParent());
        objectMapper.writeValue(settingsFile.toFile(), settings);
    }

    public Map<String, Object> getSettingsForType(String type) throws IOException {
        Map<String, Object> all = loadSettings();
        if (!all.containsKey(type)) {
            throw new IllegalArgumentException("No settings for type: " + type);
        }
        return (Map<String, Object>) all.get(type);
    }
}
