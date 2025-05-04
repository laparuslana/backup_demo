package com.example.mainapp.Controller;


import com.example.mainapp.Model.Backup.BackupHistory;
import com.example.mainapp.Model.Backup.BackupHistoryRepository;
import com.example.mainapp.Model.Restore.RestoreHistoryDTO;
import com.example.mainapp.Model.Restore.RestoreHistoryRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import com.example.mainapp.Model.Backup.BackupHistoryDTO;


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
    public List<BackupHistoryDTO> getBackupHistory() {
        return repository.findAll()
                .stream()
                .map(BackupHistoryDTO::new)
                .collect(Collectors.toList());
    }

    private final RestoreHistoryRepository restoreRepository;

    @GetMapping("/restore")
    public List<RestoreHistoryDTO> getRestoreHistory() {

        return restoreRepository.findAll()
                .stream()
                .map(RestoreHistoryDTO::new)
                .collect(Collectors.toList());
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
