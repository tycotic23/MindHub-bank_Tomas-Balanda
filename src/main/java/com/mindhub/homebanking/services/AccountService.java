package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    public ResponseEntity<Object> getAccountDTO();
    public ResponseEntity<Object> getAccountDTO(long id);
    public ResponseEntity<Object> saveAccount(String clientEmail);

    public ResponseEntity<Object> getClientAccountDTO(String clientEmail);
}
