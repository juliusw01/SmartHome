package com.katzenklappe.smartHome.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class SmartHomeController extends Device{

    @Id
    private String hostname;

    public SmartHomeController(String hostname, String id, String name){
        super(id, name);
        this.hostname = hostname;
    }
}
