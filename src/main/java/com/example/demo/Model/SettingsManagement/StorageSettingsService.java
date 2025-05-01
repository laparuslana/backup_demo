package com.example.demo.Model.SettingsManagement;


import com.example.demo.Security.AesEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Map<String, String> getStorageParams(StorageTarget target) throws Exception {
        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }
        return decrypted;
    }

}
