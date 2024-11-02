package com.katzenklappe.smartHome.Repository;

import com.katzenklappe.smartHome.Entities.APIKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface APIKeyRepo extends JpaRepository<APIKey, Integer> {
    //Optional<APIKey> findByKey(String key);
}
