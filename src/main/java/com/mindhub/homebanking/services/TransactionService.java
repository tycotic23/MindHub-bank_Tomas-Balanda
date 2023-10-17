package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;

public interface TransactionService {
    public ResponseEntity<Object> saveTransaction(String clientEmail,String fromAccountNumber,String toAccountNumber,double amount,String description);
}
