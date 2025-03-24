package com.example.backup_service.Model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
}
