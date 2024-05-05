package com.katzenklappe.smartHome.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katzenklappe.smartHome.Entities.Bearer;
import com.katzenklappe.smartHome.config.Secrets;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:8081")
public class AuthController {
    private static final String URL = "http://192.168.178.73:8080/auth/token";

    //private static Bearer bearer = new Bearer("",LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(11));

    //TODO: check Bearer Token from JSON file
    public static String checkBearerToken(){
        //String bearerToken = BearerToken.getBearerToken();
        if (Bearer.getExpirationDate() == null || !LocalDateTime.now().isBefore(Bearer.getExpirationDate()) || Bearer.getToken() == null){
            generateNewBearerToken();
            System.out.println("New Bearer Token generated: " + Bearer.getToken());
            System.out.println("Bearer Token has expiration date of: " + Bearer.getExpirationDate());
        }
        System.out.println("Token is still valid");
        return Bearer.getToken();
    }

    @PostMapping()
    private static void generateNewBearerToken(){
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
            Bearer.setExpirationDate(LocalDateTime.now().plusHours(44));
            String token = retractTokenFromJson(response.toString());
            Bearer.setToken(token);
            Bearer.setCreationDate(LocalDateTime.now());

            JSONObject json = new JSONObject();

            json.put("token", Bearer.getToken());
            json.put("expirationDate", Bearer.getExpirationDate().toString());
            json.put("creationDate", Bearer.getCreationDate().toString());

            FileWriter fileWriter = new FileWriter("BearerToken.json", false);
            fileWriter.write(json.toJSONString());
            fileWriter.close();

            ResponseEntity.ok().body(response.getBody());
        }catch (HttpClientErrorException e){
            // Handle client errors (4xx)
            ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }catch (HttpServerErrorException e){
            // Handle server errors (5xx)
            ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }catch (Exception e){
            // Handle other exceptions
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private static String retractTokenFromJson(String bearerJson){
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

    public static void saveBearerInJson(Bearer bearer) throws IOException {
        JSONObject json = new JSONObject();

        json.put("token", bearer.getToken());
        json.put("expirationDate", bearer.getExpirationDate().toString());
        json.put("creationDate", bearer.getCreationDate().toString());

        FileWriter fileWriter = new FileWriter("BearerToken.json", false);
        fileWriter.write(json.toJSONString());
        fileWriter.close();
    }
}
