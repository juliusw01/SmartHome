package com.katzenklappe.smartHome.Entities;

import java.lang.annotation.Documented;
import java.time.LocalDateTime;


public class Bearer {

    private static String token;
    private static LocalDateTime expirationDate;
    private static LocalDateTime creationDate;

    //private String refreshToken; TODO: extract refresh Token from and use it instead of always generating new Bearer Tokens


    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Bearer.token = token;
    }

    public static LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public static void setExpirationDate(LocalDateTime expirationDate) {
        Bearer.expirationDate = expirationDate;
    }

    public static LocalDateTime getCreationDate() {
        return creationDate;
    }

    public static void setCreationDate(LocalDateTime creationDate) {
        Bearer.creationDate = creationDate;
    }

}
