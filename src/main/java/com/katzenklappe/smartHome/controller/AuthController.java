package com.katzenklappe.smartHome.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katzenklappe.smartHome.config.BearerToken;
import com.katzenklappe.smartHome.config.Secrets;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8081")
public class AuthController {
    private static final String URL = "http://192.168.178.73:8080/auth/token";

    public static String getBearerToken(){
        String bearerToken = BearerToken.getBearerToken();
        if (BearerToken.getExpirationDate() == null || !LocalDateTime.now().isBefore(BearerToken.getExpirationDate()) || bearerToken == null){
            String bearerJson = generateNewBearerToken().toString();
            BearerToken.setExpirationDate(LocalDateTime.now().plusHours(44));
            bearerToken = retractBearerFromJson(bearerJson);
            BearerToken.setBearerToken(bearerToken);
            System.out.println("New Bearer Token generated: " + BearerToken.getBearerToken());
            System.out.println("Bearer Token has expiration date of: " + BearerToken.getExpirationDate());
        }

        return bearerToken;
    }

    @PostMapping()
    private static ResponseEntity<String> generateNewBearerToken(){
        String username = Secrets.getUSERNAME();
        String password = Secrets.getPASSWORD();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic Y2xpZW50SWQ6Y2xpZW50UGFzcw==");

        String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"grant_type\": \"password\" }";

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok().body(response.getBody());
        }catch (HttpClientErrorException e){
            // Handle client errors (4xx)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }catch (HttpServerErrorException e){
            // Handle server errors (5xx)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }catch (Exception e){
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private static String retractBearerFromJson(String bearerJson){
        int startIndex = bearerJson.indexOf('{');
        int endIndex = bearerJson.lastIndexOf('}');

        String subString = bearerJson.substring(startIndex, endIndex + 1);
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode jsonNode = objectMapper.readTree(subString);
            JsonNode bearerNode = jsonNode.get("access_token");
            String tmp = bearerNode.toString();
            return tmp.substring(tmp.indexOf('"')+1, tmp.lastIndexOf('"'));
        } catch (Exception e) {
            System.out.println("Fehler beim Parsen des JSON: " + e.getMessage());
        }

        return null;
    }
}
