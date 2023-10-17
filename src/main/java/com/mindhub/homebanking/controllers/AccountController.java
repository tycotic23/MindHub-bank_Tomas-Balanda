package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountServiceImplement accountService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAccountDTO();
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }

    @RequestMapping(path ="/clients/current/accounts" ,method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        return accountService.saveAccount(authentication.getName());
    }

    @RequestMapping(path ="/clients/current/accounts")
    public List<AccountDTO> getClientAccount(Authentication authentication){
        return accountService.getClientAccountDTO(authentication.getName());
    }
}
