package com.example.mainapp.Model.Backup;

import com.example.mainapp.Model.UserManagement.MyAppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
  @Override
  List<BackupHistory> findAll();
    
  @Override
    BackupHistory save(BackupHistory backupHistory);
   
    List<BackupHistory> findByUser(MyAppUser user);
}
