package com.katzenklappe.smartHome.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class APIKey {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String apiKey;
    private final LocalDateTime keyCreated;

    public APIKey(String apiKey){
        this.apiKey = apiKey;
        keyCreated = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public LocalDateTime getKeyCreated() {
        return keyCreated;
    }
}
