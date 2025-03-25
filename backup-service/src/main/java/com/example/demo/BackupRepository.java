package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<ScheduledBackup, Long> {
}
