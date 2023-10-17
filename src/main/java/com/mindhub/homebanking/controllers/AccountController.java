package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAllAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @RequestMapping(path ="/clients/current/accounts" ,method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        //verificar que cumpla con todas las condiciones
        //si cumple las condiciones se crea una cuenta
        if(accountRepository.findByClient_email(authentication.getName()).size()>=3){
            return new ResponseEntity<>("User has 3 accounts", HttpStatus.FORBIDDEN);
        }
        //crear la nueva cuenta
        Account account = new Account(createNumberAccount(),LocalDate.now(),0.0);
        account.setClient(clientRepository.findByEmail(authentication.getName()));
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private String createNumberAccount(){
        //crea un numero aleatorio entre el 0 y el 99999999 de la forma: VIN-nnnnnnnn
        int random= (int) (Math.random() * 99999999);
        return "VIN-"+random;
    }

    @RequestMapping(path ="/clients/current/accounts")
    public List<AccountDTO> getClientAccount(Authentication authentication){
        //verificar que cumpla con todas las condiciones
        //si cumple las condiciones se crea una cuenta
        return accountRepository.findByClient_email(authentication.getName()).stream().map(AccountDTO::new).collect(Collectors.toList());
    }
}
