package com.katzenklappe.smartHome.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.katzenklappe.smartHome.Entities.Bearer;
import com.katzenklappe.smartHome.Repository.BearerRepo;
import com.katzenklappe.smartHome.config.Secrets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:8081")
public class AuthController {

    @Autowired
    BearerRepo bearerRepo;

    private static final String URL = "http://192.168.178.73:8080/auth/token";

    @PostMapping
    public String checkBearerToken(){
        //String bearerToken = BearerToken.getBearerToken();
        if (Bearer.exists){
            List<Bearer> bearerList = bearerRepo.findAll();
            Bearer currentBearer = bearerList.get(0);
            if (currentBearer.getExpirationDate() == null || !LocalDateTime.now().isBefore(currentBearer.getExpirationDate()) || currentBearer.getToken() == null){
                Bearer newBearer = generateNewBearerToken();
                assert newBearer != null : "Bearer Token is null";
                bearerRepo.save(newBearer);
                return newBearer.getToken();
            }else {
                return currentBearer.getToken();
            }
        }else {
            Bearer newBearer = generateNewBearerToken();
            assert newBearer != null : "Bearer Token is null";
            bearerRepo.save(newBearer);
            return newBearer.getToken();
        }

    }

    private Bearer generateNewBearerToken(){
        if (Bearer.exists){
            bearerRepo.deleteAll();
        }
        Bearer newBearer = new Bearer();
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

            newBearer.setExpirationDate(LocalDateTime.now().plusHours(44));
            String token = retractTokenFromJson(response.toString());
            newBearer.setToken(token);
            newBearer.setCreationDate(LocalDateTime.now());

            System.out.println(newBearer.getToken());
            //bearerRepo.save(newBearer);
            System.out.println(Bearer.exists);
            Bearer.exists = true;
            ResponseEntity.ok().body(response.getBody());
            System.out.println("generateNewBearerToken: " + newBearer.getToken());
            return newBearer;

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
        return null;
    }

    private String retractTokenFromJson(String bearerJson){
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
