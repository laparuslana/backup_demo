package com.example.demo.Controller;


import com.example.demo.Model.Common.StorageSettingsService;
import com.example.demo.Model.Restore.RestoreRequest;
import com.example.demo.Model.Restore.RestoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/restore")
@CrossOrigin(origins = "*")
public class RestoreController {

    private final RestoreService restoreService;

    private final StorageSettingsService storageSettingsService;


    public RestoreController(RestoreService restoreService, StorageSettingsService storageSettingsService) {
        this.restoreService = restoreService;
        this.storageSettingsService = storageSettingsService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listBackupFiles(@RequestParam String type) throws Exception {
        Map<String, Object> config = storageSettingsService.getSettingsForType(type);

        if ("local".equals(type)) {
            String dirPath = (String) config.get("backupLocation");

            try (Stream<Path> paths = Files.list(Paths.get(dirPath))) {
                List<String> fileNames = paths
                        .filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .filter(name -> name.endsWith(".backup"))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(fileNames);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonList("Error: " + e.getMessage()));
            }

        } else if ("ftp".equals(type)) {
            String ftpHost = (String) config.get("ftpServer");
            String ftpUser = (String) config.get("ftpUser");
            String ftpPassword = (String) config.get("ftpPassword");
            String ftpDir = (String) config.get("ftpDirectory");

            try {
                List<String> ftpFiles = restoreService.listBackupFilesFromFtp(ftpHost, ftpUser, ftpPassword, ftpDir);
                return ResponseEntity.ok(ftpFiles);
            } catch (IOException | InterruptedException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonList("Error: " + e.getMessage()));
            }
        }

        return ResponseEntity.badRequest().body(Collections.singletonList("Unknown storage type"));
    }


    @GetMapping("/archive")
    public ResponseEntity<List<String>> listArchiveFiles(@RequestParam String type) throws Exception {
        Map<String, Object> config = storageSettingsService.getSettingsForType(type);

            String ftpHost = (String) config.get("ftpServer");
            String ftpUser = (String) config.get("ftpUser");
            String ftpPassword = (String) config.get("ftpPassword");
            String ftpDir = (String) config.get("ftpDirectory");

            try {
                List<String> ftpFiles = restoreService.listArchiveFilesFromFtp(ftpHost, ftpUser, ftpPassword, ftpDir);
                return ResponseEntity.ok(ftpFiles);
            } catch (IOException | InterruptedException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonList("Error: " + e.getMessage()));
            }

    }


    @PostMapping(value = "/backup", consumes = "application/json")
    public ResponseEntity<Map<String, String>> restoreBackup(@RequestBody RestoreRequest restoreRequest) throws Exception {
        Map<String, String> response = new HashMap<>();

        String type = restoreRequest.getRes_storageType();
        String fileName = restoreRequest.getBackupFile();

        Map<String, Object> config = storageSettingsService.getSettingsForType(type);

        String fullPath = "";
        if ("local".equals(type)) {
            String localPath = (String) config.get("backupLocation");
            fullPath = Paths.get(localPath, fileName).toString();
        } else if ("ftp".equals(type)) {
            String ftpDirectory = (String) config.get("ftpDirectory");

            Map<String, String> ftpParams = new HashMap<>();
            ftpParams.put("ftpServer", (String) config.get("ftpServer"));
            ftpParams.put("ftpUser", (String) config.get("ftpUser"));
            ftpParams.put("ftpPassword", (String) config.get("ftpPassword"));
            ftpParams.put("ftpDirectory", ftpDirectory);
            restoreRequest.setStorageParams(ftpParams);
        } else {
            throw new IllegalArgumentException("Unknown storage type: " + type);
        }

        restoreRequest.setFullPath(fullPath);

        System.out.println(restoreRequest.getFullPath());
        System.out.println(restoreRequest.getStorageParams());
        System.out.println(restoreService.restore(restoreRequest));
        response.put("message", "Restore done");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/manage", produces = "application/json")
    public ResponseEntity<Map<String, String>> deleteTest(@RequestParam String testDb,
                                                          @RequestParam String server,
                                                          @RequestParam String user,
                                                          @RequestParam String password) throws IOException, InterruptedException {
        Map<String, String> response = new HashMap<>();

        String result = restoreService.deleteTest(testDb, server, user, password);
        response.put("message", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/file", produces = "application/json")
    public ResponseEntity<Map<String, String>> restoreFileDb(@RequestParam String type,
                                                          @RequestParam String restorePath,
                                                          @RequestParam String restoreFile) throws IOException, InterruptedException {
        Map<String, String> response = new HashMap<>();

        Map<String, Object> config = storageSettingsService.getSettingsForType(type);

        String ftpHost = (String) config.get("ftpServer");
        String ftpUser = (String) config.get("ftpUser");
        String ftpPassword = (String) config.get("ftpPassword");
        String ftpDir = (String) config.get("ftpDirectory");

        String result = restoreService.restoreFileDb(restorePath, restoreFile, ftpHost, ftpUser, ftpPassword, ftpDir);
        response.put("message", result);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/switch", produces = "application/json")
    public ResponseEntity<Map<String, String>> switchDB(@RequestParam String clusterAd,
                                                        @RequestParam String clusterUser,
                                                        @RequestParam String clusterPass,
                                                        @RequestParam String infobase) throws IOException, InterruptedException {
        Map<String, String> response = new HashMap<>();

        String result = restoreService.switchDb(clusterAd, clusterUser, clusterPass, infobase);
        response.put("message", result);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/listInfobases")
    public List<String> listDatabases(@RequestParam String clusterAd,
                                      @RequestParam String clusterUser,
                                      @RequestParam String clusterPass) {
        return restoreService.getInfobases(clusterAd, clusterUser, clusterPass);
    }
}
