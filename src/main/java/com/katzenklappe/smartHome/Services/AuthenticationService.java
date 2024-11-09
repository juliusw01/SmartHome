package com.katzenklappe.smartHome.Services;

import com.katzenklappe.smartHome.Entities.APIKey;
import com.katzenklappe.smartHome.Repository.APIKeyRepo;
import com.katzenklappe.smartHome.config.ApiKeyAuthentication;
import com.katzenklappe.smartHome.config.Secrets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

import static com.katzenklappe.smartHome.Services.HashService.hashString;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private APIKeyRepo apiKeyRepo;


    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    private static final String INIT_TOKEN = Secrets.getAUTH_TOKEN();


    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey.equals(INIT_TOKEN)){
            log.info("Request from " + request.getRemoteAddr() + " with valid API Key");
            return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
        }
        try {
            //TODO: Application starts with empty repos
            System.out.println("Trying to find key...");
            APIKey key = apiKeyRepo.findByApiKey(apiKey).get();
            final String AUTH_TOKEN = key.getApiKey();
            System.out.println("Auth Token: " + AUTH_TOKEN);
            String requestHash = hashString(apiKey);
            System.out.println("request Hash: " + requestHash);
            if (!requestHash.equals(AUTH_TOKEN)) {
                throw new BadCredentialsException("Invalid API Key");
            }
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("API Key not found");
        }
        log.info("Request from " + request.getRemoteAddr() + " with valid API Key");
        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
