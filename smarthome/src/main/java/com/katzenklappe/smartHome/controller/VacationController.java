package com.katzenklappe.smartHome.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpResponse;

@RestController()
@RequestMapping("/api")
@Slf4j
public class VacationController {

    @PostMapping("/go-on-vacation")
    public ResponseEntity<String> runScript(){
        try {
            String dockerApiUrl = "http://localhost:2375/v1.43/containers/create"; // Docker-API Endpoint
            String requestBody = """
                {
                  "Image": "vacationmode",
                  "Cmd": ["bash", "/usr/local/bin/VacationMode.bash"]
                }
                """;

            HttpHeaders header = new HttpHeaders();
            header.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(requestBody, header);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(dockerApiUrl, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start container");
        }
    }











    @PostMapping("end-vacation")
    public ResponseEntity<String> endScript(){
        try{
            Process process = new ProcessBuilder("docker", "stop", "vacationmode").start();
            process.waitFor();
            log.info("Vacation mode stopped");
            return ResponseEntity.status(HttpStatus.OK).body("Vacation mode stopped");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to stop vacationmode.bash");
        }
    }

}
