package com.example.atm.controller;

import com.example.atm.service.AtmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class AtmController {

    @Autowired
    private AtmService atmService;

    @PostMapping(value = "/api/Withdrawal", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> withdrawal(@RequestBody int amount) {
        log.info("withdrawal {}", amount);
        return atmService.withdrawal(amount);
    }

    @PostMapping(value = "/api/Deposit", produces = MediaType.APPLICATION_JSON_VALUE)
    Integer deposit(@RequestBody Map<String, Integer> deposit) {
        log.info("deposit {}", deposit);
        return atmService.deposit(deposit);
    }
}
