package com.example.atm.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class CashRepository {
    private Map<String,Integer> cashStorage;

    public Map<String,Integer> findAll() {
        if (cashStorage == null) { return new HashMap<>(); }
        return new HashMap<>(this.cashStorage);
    }

    public void save(Map<String,Integer> cashStorage) {
        this.cashStorage = cashStorage;
        log.info("new cashStorage: {}", cashStorage);
    }
}
