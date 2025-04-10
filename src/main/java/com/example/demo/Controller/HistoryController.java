package com.example.demo.Controller;

import com.example.demo.Model.Backup.BackupHistory;
import com.example.demo.Model.Backup.BackupHistoryRepository;
import com.example.demo.Model.Restore.RestoreHistory;
import com.example.demo.Model.Restore.RestoreHistoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/req/history")
@CrossOrigin(origins = "*")
public class HistoryController {
    private final BackupHistoryRepository repository;

    public HistoryController(BackupHistoryRepository repository, RestoreHistoryRepository restoreRepository) {

        this.repository = repository;
        this.restoreRepository = restoreRepository;
    }

    @GetMapping("/backup")
    public List<BackupHistory> getBackupHistory() {
        return repository.findAll();
    }

    private final RestoreHistoryRepository restoreRepository;

    @GetMapping("/restore")
    public List<RestoreHistory> getRestoreHistory() {
        return restoreRepository.findAll();
    }

    @GetMapping("/download")
    public void downloadBackupHistory(HttpServletResponse response) throws IOException {
        List<BackupHistory> backups = repository.findAll();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=backup_history.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Status,Date,Database Name, Backup Location, Retention Period, User");

        for (BackupHistory backup : backups) {
            writer.println(backup.getStatus()+ "," + backup.getBackup_time() + "," + backup.getDatabase_name() + "," + backup.getBackup_location() + "," + backup.getRetention_period() + "," + backup.getUser());
        }

        writer.flush();
        writer.close();
    }


}
