package com.example.demo.Model.UserManagement;

import com.example.demo.Model.Backup.BackupHistory;
import com.example.demo.Model.Restore.RestoreHistory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "my_app_user")
public class MyAppUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    public List<BackupHistory> getBackupHistories() {
        return backupHistories;
    }

    public void setBackupHistories(List<BackupHistory> backupHistories) {
        this.backupHistories = backupHistories;
    }

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<BackupHistory> backupHistories = new ArrayList<>();

    public List<RestoreHistory> getRestoreHistories() {
        return restoreHistories;
    }

    public void setRestoreHistories(List<RestoreHistory> restoreHistories) {
        this.restoreHistories = restoreHistories;
    }

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<RestoreHistory> restoreHistories = new ArrayList<>();

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
