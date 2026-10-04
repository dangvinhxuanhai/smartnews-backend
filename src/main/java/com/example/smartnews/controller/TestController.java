package com.example.smartnews.controller;

import com.example.smartnews.entity.SystemAccount;
import com.example.smartnews.repository.SystemAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private SystemAccountRepository repo;

    @GetMapping("/accounts")
    public List<SystemAccount> getAll(){
        return repo.findAll();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Protected API";
    }
}
