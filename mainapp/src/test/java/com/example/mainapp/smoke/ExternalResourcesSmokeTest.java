package com.example.mainapp.smoke;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ExternalResourcesSmokeTest {

    @Value("${ftp.host}")
    private String ftpHost;

    @Value("${ftp.port}")
    private int ftpPort;

    @Value("${ftp.directory}")
    private String ftpDirectory;

    @Value("${ftp.username}")
    private String ftpUsername;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Test
    public void testFtpServerConnection() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setConnectTimeout(5000);

            ftpClient.connect(ftpHost, ftpPort);

            int replyCode = ftpClient.getReplyCode();
            assertThat(FTPReply.isPositiveCompletion(replyCode)).isTrue();

            boolean loginSuccess = ftpClient.login(ftpUsername, ftpPassword);
            assertThat(loginSuccess).isTrue();

            ftpClient.logout();
        } catch (IOException e) {
            fail("FTP server is not accessible: " + e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }

    @Test
    public void testFtpFileUpload() {
        FTPClient ftpClient = new FTPClient();
        try {

            System.out.println("Connecting to FTP server: " + ftpHost + ":" + ftpPort);
            ftpClient.connect(ftpHost, ftpPort);

            System.out.println("Logging in with username: " + ftpUsername);
            boolean login = ftpClient.login(ftpUsername, ftpPassword);
            if (!login) {
                fail("Could not login to FTP server");
                return;
            }
            System.out.println("Login successful");

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setConnectTimeout(10000);

            if (!ftpDirectory.isEmpty()) {
                System.out.println("Checking/creating directory: " + ftpDirectory);

                ftpClient.changeWorkingDirectory(ftpDirectory);
                }

            String currentDir = ftpClient.printWorkingDirectory();
            System.out.println("Current working directory: " + currentDir);

            File fileToUpload = new File("src/test/resources/test_backup.sql");

            System.out.println("Using file: " + fileToUpload.getAbsolutePath() + ", size: " + fileToUpload.length() + " bytes");

            assertThat(fileToUpload.exists()).isTrue();

            String remoteFileName = "backup-test-" + System.currentTimeMillis() + ".sql";
            System.out.println("Uploading to remote file: " + remoteFileName + " in directory: " + currentDir);

            try (FileInputStream fis = new FileInputStream(fileToUpload)) {
                boolean uploaded = ftpClient.storeFile(remoteFileName, fis);
                System.out.println("Upload result: " + uploaded);

                assertThat(uploaded).isTrue();

                boolean deleted = ftpClient.deleteFile(remoteFileName);
                System.out.println("File deletion result: " + deleted);
                assertThat(deleted).isTrue();
            }


            ftpClient.logout();
        } catch (IOException e) {
            fail("FTP file upload test failed: " + e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {

                }
            }
        }
    }
}