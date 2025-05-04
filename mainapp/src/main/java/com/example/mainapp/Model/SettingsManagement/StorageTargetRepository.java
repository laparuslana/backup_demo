package com.example.mainapp.Model.SettingsManagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageTargetRepository extends JpaRepository<StorageTarget, Long> {
    Optional<StorageTarget> findByName(String name);
    List<StorageTarget> findAllByType(String type);
}
