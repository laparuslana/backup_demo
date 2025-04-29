package com.example.demo.Controller;


import com.example.demo.Model.SettingsManagement.StorageTarget;
import com.example.demo.Model.SettingsManagement.StorageTargetRepository;
import com.example.demo.Model.Restore.RestoreRequest;
import com.example.demo.Model.Restore.RestoreService;
import com.example.demo.Security.AesEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RestoreController(RestoreService restoreService) {
        this.restoreService = restoreService;
    }
    @Autowired
    private StorageTargetRepository repository;

    @Autowired
    private AesEncryptor aesEncryptor;

    @GetMapping("/list")
    public ResponseEntity<List<String>> listBackupFiles(@RequestParam String type,
                                                        @RequestParam String nameSelect) throws Exception {
        StorageTarget target = repository.findByName(nameSelect)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }

        if ("LOCAL".equals(type)) {
            String dirPath = decrypted.get("directory");
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
        } else if ("FTP".equals(type)) {
            String ftpHost = decrypted.get("ftp_host");
            String ftpUser = decrypted.get("ftp_user");
            String ftpPassword = decrypted.get("ftp_password");
            String ftpDir = decrypted.get("ftp_directory");

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
    public ResponseEntity<List<String>> listArchiveFiles(@RequestParam String nameSelect) throws Exception {
        StorageTarget target = repository.findByName(nameSelect)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }

        String ftpHost = decrypted.get("ftp_host");
        String ftpUser = decrypted.get("ftp_user");
        String ftpPassword = decrypted.get("ftp_password");
        String ftpDir = decrypted.get("ftp_directory");

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

        String name = restoreRequest.getRes_nameSelect();
        StorageTarget target = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }

        String fullPath = "";
        if ("LOCAL".equals(type)) {
            String localPath = decrypted.get("directory");
            fullPath = Paths.get(localPath, fileName).toString();
        } else if ("FTP".equals(type)) {
            Map<String, String> ftpParams = new HashMap<>();
            ftpParams.put("ftpServer", decrypted.get("ftp_host"));
            ftpParams.put("ftpUser", decrypted.get("ftp_user"));
            ftpParams.put("ftpPassword", decrypted.get("ftp_password"));
            ftpParams.put("ftpDirectory", decrypted.get("ftp_directory"));
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
    public ResponseEntity<Map<String, String>> restoreFileDb(@RequestParam String restorePath,
                                                          @RequestParam String restoreFile,
                                                             @RequestParam String name) throws Exception {
        Map<String, String> response = new HashMap<>();

        StorageTarget target = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Map<String, String> decrypted = new HashMap<>();
        for (Map.Entry<String, String> entry : target.getJsonParameters().entrySet()) {
            decrypted.put(entry.getKey(), aesEncryptor.decrypt(entry.getValue()));
        }

        String ftpHost = decrypted.get("ftp_host");
        String ftpUser = decrypted.get("ftp_user");
        String ftpPassword = decrypted.get("ftp_password");
        String ftpDir = decrypted.get("ftp_directory");


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
