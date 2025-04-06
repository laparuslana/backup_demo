package com.example.demo.Model.Restore;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "restore_history")
public class RestoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

}
