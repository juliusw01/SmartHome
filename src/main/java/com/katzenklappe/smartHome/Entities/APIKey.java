package com.katzenklappe.smartHome.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
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
