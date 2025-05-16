package com.example.mainapp.smoke;

import com.example.mainapp.Model.Backup.BackupRequest;
import com.example.mainapp.Model.Backup.BackupService;
import org.springframework.context.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@WithMockUser(username = "test")
public class ServicesSmokeTest {

    @Autowired
    private BackupService backupService;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testBackupServiceAvailability() {
        assertThat(backupService).isNotNull();
    }

    @Test
    public void testAllRequiredBeansLoaded() {
        assertThat(context.containsBean("backupService")).isTrue();
        assertThat(context.containsBean("backupController")).isTrue();

        assertThat(context.containsBean("backupHistoryRepository")).isTrue();
        assertThat(context.containsBean("backupScheduleRepository")).isTrue();
    }

    @Test
    public void testBackupServiceCanProcessRequest() {
        BackupRequest request = new BackupRequest();
        request.setDatabaseName("test-db");
        request.setStorageType("LOCAL");
        request.setBackupLocation(System.getProperty("java.io.tmpdir"));
        request.setDbServer("localhost");
        request.setDbUser("postgres");
        request.setDbPassword("postgres");
        request.setRetentionPeriod("7");

        String result = backupService.startBackup(request);
        assertThat(result).isNotNull();
    }
}