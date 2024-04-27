package com.katzenklappe.smartHome.controller;


import com.katzenklappe.smartHome.DTO.LightDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
@Controller
@RestController
@RequestMapping("/light")

public class LightController {
    private static final String BEARER_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhbGwiLCJleHAiOjE3MTQzOTI3MjksImlhdCI6MTcxNDIxOTkyOSwiaXNzIjoiOTE0MTAwMDYwMzQyIiwianRpIjoiM2I5ODI1NTlmYmRiNGI2Yjk2NGFhY2FlZWJiMDk2NGQiLCJzdWIiOiJhZG1pbiIsInVzZXJfcGVybWlzc2lvbnMiOiJGRkZGRkZGRkZGRkZGRkZGIiwiZGV2aWNlIjoiOTE0MTAwMDYwMzQyIn0=.o7AtGe6zsBavhJyVzwvpSQsgam9MaNEIedE3ik8G5Vt007XVKJChEN0zf2AyL25Fp02p6nfE/4BZttxVaUW6inSQLzMnMcWSGuaQcY+TaUsQeeIOUjMSK+z9pYiC/woebz3FZaptc55RMrMWUupT+39bpNMM6IlWwlPlKpoEW44=";
    private final String baseURL = "http://192.168.178.73:8080";

    @RequestMapping("/hello")
    public String hello(){
        return "HelloWorld";
    }

    @GetMapping("/devices")
    public ResponseEntity <Object> getAllDevices(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + BEARER_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String uri = baseURL + "/device";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (HttpClientErrorException e) {
            // Handle client errors (4xx)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            // Handle server errors (5xx)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/device/{id}/on")
    public ResponseEntity <Object> turnLightOn(@PathVariable String id, @RequestBody LightDTO lightDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + BEARER_TOKEN);

        HttpEntity<String> entity =new HttpEntity<>(headers);

        String uri = baseURL + "/action";
        RestTemplate restTemplate = new RestTemplate();



        return new ResponseEntity<>("Das hat geklappt", HttpStatus.OK);
    }

    /*
    @GetMapping("/devices")
    public String getAllDevices(){
        String uri = "http://192.168.178.73:8080/device";
        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }
     */
}
