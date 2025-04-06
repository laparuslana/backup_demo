package com.example.demo.Controller;

import com.example.demo.Model.Backup.BackupHistory;
import com.example.demo.Model.Backup.BackupHistoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/req/backup-history")
@CrossOrigin(origins = "*")
public class BackupHistoryController {
    private final BackupHistoryRepository repository;

    public BackupHistoryController(BackupHistoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<BackupHistory> getBackupHistory() {
        return repository.findAll();
    }

    @GetMapping("/download")
    public void downloadBackupHistory(HttpServletResponse response) throws IOException {
        List<BackupHistory> backups = repository.findAll();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=backup_history.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Status,Date,Database Name, Backup Location, Retention Period");

        for (BackupHistory backup : backups) {
            writer.println(backup.getStatus()+ "," + backup.getBackup_time() + "," + backup.getDatabase_name() + "," + backup.getBackup_location() + "," + backup.getRetention_period());
        }

        writer.flush();
        writer.close();
    }


}
