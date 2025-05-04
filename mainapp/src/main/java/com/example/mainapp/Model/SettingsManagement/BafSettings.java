package com.example.mainapp.Model.SettingsManagement;


import com.example.mainapp.Model.UserManagement.MyAppUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "baf_settings")
public class BafSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBafType() {
        return bafType;
    }

    public void setBafType(String bafType) {
        this.bafType = bafType;
    }

    public String getBafPath() {
        return bafPath;
    }

    public void setBafPath(String bafPath) {
        this.bafPath = bafPath;
    }

    public MyAppUser getUser() {
        return user;
    }

    public void setUser(MyAppUser user) {
        this.user = user;
    }

    @Column(name = "baf_type")
    private String bafType;

    @Column(name = "baf_path")
    private String bafPath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private MyAppUser user;
}
