package com.katzenklappe.smartHome.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
@Controller
@RestController
@RequestMapping("/light")
//@CrossOrigin(origins = "http://localhost:8081")

public class LightController {
    private final String baseURL = "http://192.168.178.73:8080";
    //TODO: find baseUrl by trying 'ping smarthome01', 'ping smarthome02', etc.

    private AuthController bearer;

    public LightController(AuthController bearer) {
        this.bearer = bearer;
    }

    @GetMapping("/devices")
    public ResponseEntity <Object> getAllDevices(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearer.checkBearerToken());

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

    @PostMapping("/device/{deviceID}/switchState")
    public ResponseEntity <Object> turnLightOn(@PathVariable String deviceID){
        String capabilityId = getCapabilitiesId(deviceID);
        boolean isOn = getIsOn(deviceID);
        boolean targetState = !isOn;
        String template = """
                {
                    "type": "SetState",
                    "namespace": "core.RWE",
                    "target": "%s",
                    "params": {
                        "onState": {
                            "type": "Constant",
                            "value": %s
                        }
                    }
                }""";

        String reqBody = String.format(template, capabilityId, targetState);
        return switchState(reqBody);
    }

    public boolean getIsOn(String deviceId){
        String response = getState(deviceId).toString();

        int startIndex = response.indexOf('{');
        int endIndex = response.lastIndexOf('}');
        String jsonResponse = response.substring(startIndex, endIndex + 1);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode stateNode = jsonNode.get("state");
            JsonNode onStateNode = stateNode.get("onState");
            JsonNode valueNode =  onStateNode.get("value");

            if (valueNode != null){
                String currentState = valueNode.toString();
                if (currentState.equals("false")){
                    return false;
                }else if (currentState.equals("true")){
                    return true;
                }
            }
        } catch (Exception e) {
        System.out.println("Fehler beim Parsen des JSON: " + e.getMessage());
    }
        return false;
    }

    public ResponseEntity<Object> getState(String deviceId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearer.checkBearerToken());

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        String uri = baseURL + "/device/" + deviceId + "/capability/state";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (HttpClientErrorException e){
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

    public String getCapabilitiesId(String deviceID){
        String capability = getCapability(deviceID).toString();

        int startIndex = capability.indexOf('{');
        int endIndex = capability.lastIndexOf('}');

        String capabiltyJSON = capability.substring(startIndex, endIndex + 1);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(capabiltyJSON);

            JsonNode capabilitiesNode = jsonNode.get("capabilities");

            if (capabilitiesNode != null && capabilitiesNode.isArray()) {
                String capabilityId = capabilitiesNode.get(0).asText();

                return capabilityId;
            } else {
                System.out.println("Capabilities nicht gefunden oder kein Array.");
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Parsen des JSON: " + e.getMessage());
        }
        return null;
    }

    public ResponseEntity <Object> getCapability(String deviceID){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearer.checkBearerToken());

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        String uri = baseURL + "/device/" + deviceID;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (HttpClientErrorException e){
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

    public ResponseEntity <Object> switchState(@RequestBody Object requestBody){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearer.checkBearerToken());

        HttpEntity<Object> entity =new HttpEntity<>(requestBody, headers);

        String uri = baseURL + "/action";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            return ResponseEntity.ok().body(response.getBody());
        } catch (HttpClientErrorException e){
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
}
