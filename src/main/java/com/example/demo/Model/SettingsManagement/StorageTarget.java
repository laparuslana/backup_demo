package com.example.demo.Model.SettingsManagement;

import com.example.demo.Security.JsonMapConverter;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "storage_settings")
public class StorageTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getJsonParameters() {
        return jsonParameters;
    }

    public void setJsonParameters(Map<String, String> jsonParameters) {
        this.jsonParameters = jsonParameters;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "json_params", columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> jsonParameters;
}