package com.katzenklappe.smartHome.Repository;

import com.katzenklappe.smartHome.Entities.Bearer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BearerRepo extends JpaRepository<Bearer, UUID> {
}
