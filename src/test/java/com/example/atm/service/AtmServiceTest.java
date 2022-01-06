package com.example.atm.service;

import com.example.atm.exception.AmountException;
import com.example.atm.exception.CashStorageContentException;
import com.example.atm.repository.CashRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class AtmServiceTest {

    @InjectMocks
    AtmService atmService;

    @Mock
    CashRepository cashRepository;

    @Captor
    ArgumentCaptor<Map<String,Integer>> cashStorageCaptor;

    @Test
    void testDepositOk() {
        var deposit = new HashMap<String,Integer>();
        deposit.put("1000",1);
        deposit.put("2000",2);
        deposit.put("5000",5);

        Assertions.assertEquals(30000, atmService.deposit(deposit));
        Mockito.verify(cashRepository).save(cashStorageCaptor.capture());
        Assertions.assertEquals(1, cashStorageCaptor.getValue().get("1000"));
        Assertions.assertEquals(2, cashStorageCaptor.getValue().get("2000"));
        Assertions.assertEquals(5, cashStorageCaptor.getValue().get("5000"));
    }

    @Test
    void testDepositFailOnAmount() {
        var deposit = new HashMap<String,Integer>();
        deposit.put("1001",1);

        Assertions.assertThrows(AmountException.class, () -> atmService.deposit(deposit));
    }

    @Test
    void testWithdrawalFailOnAmount() {
        Assertions.assertThrows(AmountException.class, () -> atmService.withdrawal(1));
    }

    @Test
    void testWithdrawalFailOnEmptyStorage() {
        Assertions.assertThrows(CashStorageContentException.class, () -> atmService.withdrawal(1000));
    }

    @Test
    void testWithdrawalOk() {
        var deposit = new HashMap<String,Integer>();
        deposit.put("1000",1);
        deposit.put("2000",2);
        deposit.put("5000",5);
        Mockito.when(cashRepository.findAll()).thenReturn(deposit);

        var cash = atmService.withdrawal(13000);

        Assertions.assertEquals(1, cash.get("1000"));
        Assertions.assertEquals(1, cash.get("2000"));
        Assertions.assertEquals(2, cash.get("5000"));
        Mockito.verify(cashRepository).save(cashStorageCaptor.capture());
        Assertions.assertEquals(1, cashStorageCaptor.getValue().get("2000"));
        Assertions.assertEquals(3, cashStorageCaptor.getValue().get("5000"));
    }
}
