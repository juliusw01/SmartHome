package com.katzenklappe.smartHome.Repository;

import com.katzenklappe.smartHome.Entities.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface APIKeyRepo extends JpaRepository<APIKey, Integer> {
    Optional<APIKey> findByApiKey(String apiKey);
}
