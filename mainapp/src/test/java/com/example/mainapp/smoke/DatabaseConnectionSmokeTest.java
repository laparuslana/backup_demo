package com.example.mainapp.smoke;

import com.example.mainapp.Model.Backup.BackupHistory;
import com.example.mainapp.Model.Backup.BackupHistoryRepository;
import com.example.mainapp.Model.UserManagement.MyAppUser;
import com.example.mainapp.Model.UserManagement.MyAppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@WithMockUser(username = "test")
public class DatabaseConnectionSmokeTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BackupHistoryRepository backupHistoryRepository;

    @Autowired
    MyAppUserRepository userRepository;

    private MyAppUser testUser;

    @BeforeEach
    public void setup() {
        Optional<MyAppUser> existingUser = userRepository.findByUsername("test");
        if (existingUser.isPresent()) {
            testUser = existingUser.get();
        } else {
            testUser = new MyAppUser();
            testUser.setUsername("test");

            testUser = userRepository.save(testUser);
        }
}

    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isClosed()).isFalse();
        }
    }

    @Test
    public void testRepositoryOperations() {
        BackupHistory history = new BackupHistory();
        history.setDatabase_name("smoke-test-db");
        history.setStatus("TEST");
        history.setBackup_location("/tmp/smoke-test");
        history.setBackup_time(LocalDateTime.now());
        history.setRetention_period("1");
        history.setUser(testUser);

        BackupHistory savedHistory = backupHistoryRepository.save(history);
        assertThat(savedHistory.getId()).isNotNull();

        Optional<BackupHistory> retrieved = backupHistoryRepository.findById(savedHistory.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getDatabase_name()).isEqualTo("smoke-test-db");

        backupHistoryRepository.deleteById(savedHistory.getId());
    }
}