package com.example.demo.Model.Common;

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

    @Autowired
    private BafSettingsRepository bafSettingsRepository;

    public void saveBafSettings(BafSettings bafSettings) {
        bafSettingsRepository.save(bafSettings);
    }

    @Autowired
    private StorageTargetRepository repository;

    public void saveSettings(StorageDTO dto) throws Exception {
        Map<String, String> encrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : dto.getJsonParameters().entrySet()) {
            encrypted.put(entry.getKey(), aesEncryptor.encrypt(entry.getValue()));
        }

        StorageTarget entity = new StorageTarget();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setJsonParameters(encrypted);

        repository.save(entity);
    }

    public Map<String, String> getStorageParams(String name) throws Exception {
        StorageTarget target = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }
        return decrypted;
    }

}
