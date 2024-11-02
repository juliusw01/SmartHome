package com.katzenklappe.smartHome.test.controller;

import com.katzenklappe.smartHome.test.entities.TestEntity;
import com.katzenklappe.smartHome.test.repo.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("some/mapping")
public class TestController {

    @Autowired
    private TestRepo testRepo;

    @GetMapping("/endpoint")
    public List<TestEntity> getList(){
        return testRepo.findAll();
    }
}
