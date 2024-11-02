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

import static com.katzenklappe.smartHome.Services.HashService.hashString;

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

}
