package com.example.demo.Model.Backup;


import com.example.demo.Model.Restore.RestoreHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivityChartService {

    private final BackupHistoryRepository backupRepo;
    private final RestoreHistoryRepository restoreRepo;

    public ActivityChartService(BackupHistoryRepository backupRepo, RestoreHistoryRepository restoreRepo) {
        this.backupRepo = backupRepo;
        this.restoreRepo = restoreRepo;
    }

    public List<ChartDTO> getActivityChart() {
        LocalDate today = LocalDate.now();

        Map<LocalDate, Long> backupMap = backupRepo.findAll().stream()
                .filter(b -> b.getBackup_time()!= null)
                .filter(b -> b.getBackup_time().toLocalDate().isAfter(today.minusDays(7)))
                .collect(Collectors.groupingBy(
                        b -> b.getBackup_time().toLocalDate(),
                        Collectors.counting()
                ));

        Map<LocalDate, Long> restoreMap = restoreRepo.findAll().stream()
                .filter(r -> r.getRestore_time() != null)
                .filter(r -> r.getRestore_time().toLocalDate().isAfter(today.minusDays(7)))
                .collect(Collectors.groupingBy(
                        r -> r.getRestore_time().toLocalDate(),
                        Collectors.counting()
                ));

        List<ChartDTO> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            int backupCount = backupMap.getOrDefault(date, 0L).intValue();
            int restoreCount = restoreMap.getOrDefault(date, 0L).intValue();
            result.add(new ChartDTO(date, backupCount, restoreCount));
        }

        return result;
    }
}
