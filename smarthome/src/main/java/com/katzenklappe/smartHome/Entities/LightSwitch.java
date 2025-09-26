package com.katzenklappe.smartHome.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
//@AllArgsConstructor
public class LightSwitch extends Device{

    @Id
    private String switchState;

    public LightSwitch(String switchState, String id, String name){
        super(id, name);
        this.switchState = switchState;

    }
}
