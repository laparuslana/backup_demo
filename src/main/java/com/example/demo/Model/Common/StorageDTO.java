package com.example.demo.Model.Common;

import java.util.Map;

public class StorageDTO {
    private String name;
    private String type;
    private Map<String, String> jsonParameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getJsonParameters() {
        return jsonParameters;
    }

    public void setJsonParameters(Map<String, String> jsonParameters) {
        this.jsonParameters = jsonParameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StorageDTO(String name, String type, Map<String, String> jsonParameters) {
        this.name = name;
        this.type = type;
        this.jsonParameters = jsonParameters;
    }
}
