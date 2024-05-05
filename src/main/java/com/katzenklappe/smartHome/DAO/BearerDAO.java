package com.katzenklappe.smartHome.DAO;

import java.time.LocalDateTime;

public class BearerDAO {

    public String token;
    public LocalDateTime expirationDate;
    public LocalDateTime creationDate;
    //public String refreshToken;

    public BearerDAO(String token, LocalDateTime expirationDate, LocalDateTime creationDate){
        super();
        this.token = token;
        this.expirationDate = expirationDate;
        this.creationDate = creationDate;
        //this.refreshToken = refreshToken;
    }
}
