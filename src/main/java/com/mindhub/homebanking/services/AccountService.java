package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    public List<AccountDTO> getAccountDTO();
    public AccountDTO getAccountDTO(long id);
    public ResponseEntity<Object> saveAccount(String clientEmail);

    public List<AccountDTO> getClientAccountDTO(String clientEmail);
}
