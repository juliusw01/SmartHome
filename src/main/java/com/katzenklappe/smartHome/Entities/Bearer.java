package com.katzenklappe.smartHome.Entities;

import jakarta.persistence.*;

import java.lang.annotation.Documented;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Bearer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Lob
    private String token;
    private LocalDateTime expirationDate;
    private LocalDateTime creationDate;

    public static boolean exists = false;

    //private String refreshToken; TODO: extract refresh Token from JSON and use it instead of always generating new Bearer Tokens


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public UUID getId() {
        return id;
    }
}
