package com.katzenklappe.smartHome.controller;

import com.katzenklappe.smartHome.Entities.APIKey;
import com.katzenklappe.smartHome.Repository.APIKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/auth/apikey")
public class APIKeyController {

    @Autowired
    private APIKeyRepo apiKeyRepo;

    @GetMapping("/newkey")
    public ResponseEntity<String> createNewKey(){
        byte[] random = new byte[32];
        new SecureRandom().nextBytes(random);
        String key = Base64.getEncoder().encodeToString(random);
        String hash = hashString(key);
        System.out.println("API Key hash: " + hash);
        apiKeyRepo.save(new APIKey(hash));

        return new ResponseEntity<>(key, HttpStatus.CREATED);
    }

    private static String hashString(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes){
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
