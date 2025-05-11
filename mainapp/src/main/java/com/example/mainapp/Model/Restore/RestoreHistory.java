package com.example.mainapp.Model.Restore;


import com.example.mainapp.Model.UserManagement.MyAppUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restore_history")
public class RestoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRestore_time() {
        return restore_time;
    }

    public void setRestore_time(LocalDateTime restore_time) {
        this.restore_time = restore_time;
    }

    @Column(name = "status")
    private String status;

    @Column(name = "restore_time")
    private LocalDateTime restore_time;

    public String getBackup_file() {
        return backup_file;
    }

    public void setBackup_file(String backup_file) {
        this.backup_file = backup_file;
    }

    public String getSource_database() {
        return source_database;
    }

    public void setSource_database(String source_database) {
        this.source_database = source_database;
    }

    @Column(name = "backup_file")
    private String backup_file;

    @Column(name = "source_database")
    private String source_database;


    public MyAppUser getUser() {
        return user;
    }

    public void setUser(MyAppUser user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private MyAppUser user;
}
