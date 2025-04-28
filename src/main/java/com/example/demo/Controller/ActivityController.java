package com.example.demo.Controller;


import com.example.demo.Model.Common.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;


@RestController
@RequestMapping("/api")
public class ActivityController {

    private final ActivityChartService chartService;

    public ActivityController(ActivityChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/activity-stats")
    public List<ChartDTO> getChartStats() {
        return chartService.getActivityChart();
    }

    @GetMapping("/progress")
    public ResponseEntity<ProgressDTO> getProgress() {
        ProgressDTO progress = ProgressSession.getCurrentProgress();
        if (progress == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/logs/auto-db")
    public ResponseEntity<Map<String, String>> getAutoDbLogs() {
        return ResponseEntity.ok(readLogFile("/tmp/auto-db-cron.log"));
    }

    @GetMapping("/logs/auto-file")
    public ResponseEntity<Map<String, String>> getAutoFileLogs() {
        return ResponseEntity.ok(readLogFile("/tmp/auto-file-cron.log"));
    }

    private Map<String, String> readLogFile(String filePath) {
        Map<String, String> result = new HashMap<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                result.put("lastModified", "0");
                result.put("content", "❌ No log file found.");
                return result;
            }

            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            long lastModifiedMillis = attrs.lastModifiedTime().toMillis();

            String content = readLastLines(filePath);
                result.put("lastModified", String.valueOf(lastModifiedMillis));
                result.put("content", content);
                return result;
        } catch (IOException e) {
            result.put("lastModified", "0");
            result.put("content", "❌ Cannot read log file.");
            return result;
        }
    }
    private String readLastLines(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Deque<String> lastLines = new ArrayDeque<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (lastLines.size() == 5) {
                    lastLines.pollFirst();
                }
                lastLines.addLast(line);
            }
            List<String> fileLines = new ArrayList<>(lastLines);
            return String.join("\n", fileLines);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

