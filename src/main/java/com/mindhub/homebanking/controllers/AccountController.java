package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> getAllAccounts(){

        return accountService.getAccountDTO();
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
            return accountService.getAccountDTO(id);
        }
        else {
            return accountService.getAccountDTO(id, authentication.getName());
        }
    }

    @RequestMapping(path ="/clients/current/accounts" ,method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        return accountService.saveAccount(authentication.getName());
    }

    @RequestMapping(path ="/clients/current/accounts")
    public ResponseEntity<Object> getClientAccount(Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        return accountService.getClientAccountDTO(authentication.getName());
    }
}
