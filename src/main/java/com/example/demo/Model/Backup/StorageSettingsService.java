package com.example.demo.Model.Backup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Service
public class StorageSettingsService {

    private final Path settingsFile = Paths.get("config/storage-settings.json");
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public Map<String, Object> loadSettings() throws IOException {
        if (!Files.exists(settingsFile)) {
            return new HashMap<>();
        }
        String content = Files.readString(settingsFile);
        return objectMapper.readValue(content, new TypeReference<>() {});
    }

    public void saveSettings(Map<String, Object> settings) throws IOException {
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
