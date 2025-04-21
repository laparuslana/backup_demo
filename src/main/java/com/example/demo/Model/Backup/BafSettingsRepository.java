package com.example.demo.Model.Backup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BafSettingsRepository extends JpaRepository<BafSettings, Long> {
    Optional<BafSettings> findByUserId(Long userId);
}
