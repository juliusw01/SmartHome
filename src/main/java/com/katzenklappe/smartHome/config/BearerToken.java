package com.katzenklappe.smartHome.config;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class BearerToken {

    private static String bearerToken;
    private static LocalDateTime expirationDate;

    public static String getBearerToken(){
        return bearerToken;
    }

    public static void setBearerToken(String bearerToken){
        BearerToken.bearerToken = bearerToken;
    }

    public static LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public static void setExpirationDate(LocalDateTime expirationDate) {
        BearerToken.expirationDate = expirationDate;
    }
}
