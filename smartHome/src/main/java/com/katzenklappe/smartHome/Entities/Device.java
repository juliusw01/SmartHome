package com.katzenklappe.smartHome.Entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Device {

    @Id
    private String id;
    private String name;

}
