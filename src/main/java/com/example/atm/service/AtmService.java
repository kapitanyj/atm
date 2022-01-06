package com.example.atm.service;

import com.example.atm.exception.AmountException;
import com.example.atm.exception.CashStorageContentException;
import com.example.atm.repository.CashRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AtmService {

    @Autowired
    private CashRepository cashRepository;

    public int deposit(Map<String,Integer> deposit) {
        deposit.entrySet().forEach(e -> {
            if (Integer.parseInt(e.getKey()) % 1000 != 0) { throw new AmountException(); } //validate cashUnits
        });

        Map<String,Integer> cashStorage = cashRepository.findAll();
        deposit.entrySet().forEach(cashEntry ->
                cashStorage.put(
                        cashEntry.getKey(),
                        cashEntry.getValue() +
                                (cashStorage.get(cashEntry.getKey()) != null ? cashStorage.get(cashEntry.getKey()) : 0 )));
        cashRepository.save(cashStorage);

        int sum = cashStorage.entrySet().stream()
                .map(e -> Integer.parseInt(e.getKey()) * e.getValue())
                .reduce(0, Integer::sum);
        log.info("cashStorage sum: {}", sum);
        return sum;
    }

    public Map<String,Integer> withdrawal(int amount) {
        if (amount % 1000 != 0) { throw new AmountException(); } //validate

        Map<String,Integer> cashStorage = cashRepository.findAll();
        Map<String,Integer> actualCash = calculateCashUnits(amount, cashStorage);
        Map<String,Integer> newCashStorage = subtractCashUnits(actualCash, cashStorage);
        cashRepository.save(newCashStorage);
        return actualCash;
    }

    private static Map<String,Integer> calculateCashUnits(int amount, Map<String,Integer> cashStorage) {
        SortedMap<String,Integer> orderedCashStorage = new TreeMap<>(
                (o1, o2) -> Integer.compare(Integer.parseInt(o2), Integer.parseInt(o1))); //desc order
        orderedCashStorage.putAll(cashStorage);
        log.debug("orderedCashStorage: {}", orderedCashStorage);

        Map<String,Integer> actualCash = new HashMap<>();
        for (Map.Entry<String,Integer> cashEntry: orderedCashStorage.entrySet()) {
            int cashUnit = Integer.parseInt(cashEntry.getKey());
            int cashCount = cashEntry.getValue();
            while (amount >= cashUnit && cashCount > 0) {
                int actualCashCount = actualCash.get(cashEntry.getKey()) != null ? actualCash.get(cashEntry.getKey()) : 0;
                amount -= cashUnit;
                actualCash.put(cashEntry.getKey(), actualCashCount + 1);
                log.debug("amount: {}", amount);
            }
        }
        if (amount > 0) { throw new CashStorageContentException(); }
        log.info("actualCash: {}", actualCash);
        return actualCash;
    }

    private static Map<String,Integer> subtractCashUnits(Map<String,Integer> actualCash, Map<String,Integer> cashStorage) {
        return cashStorage.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> actualCash.containsKey(e.getKey()) ? e.getValue() - actualCash.get(e.getKey()) : e.getValue()));
    }

}
