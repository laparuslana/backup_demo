package com.example.mainapp.Model.Backup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
}
