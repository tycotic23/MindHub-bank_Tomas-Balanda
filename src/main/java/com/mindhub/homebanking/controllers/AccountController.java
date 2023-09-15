package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountServiceImplement accountService;

    @Autowired
    private ClientServiceImplement clientService;

    @RequestMapping("/accounts")
    public ResponseEntity<Object> getAllAccounts(Authentication authentication){
        if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
            return new ResponseEntity<>(accountService.getAllAccountDTO(),HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Only for admin", HttpStatus.FORBIDDEN);
    }

    @RequestMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        //revisar autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        //revisar que exista la cuenta
        if(!accountService.existById(id)){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"))) {
            return new ResponseEntity<>(accountService.getAccountById(id),HttpStatus.ACCEPTED);
        }
        else {
            //revisar que la cuenta solicitada pertenezca al cliente autenticado

            if(!accountService.accountBelongsToClient(id,authentication.getName())){
                return new ResponseEntity<>("The account doesn't belong to the authenticated user",HttpStatus.FORBIDDEN);
            }
            //conseguir y devolver la cuenta solicitada
            return new ResponseEntity<>(accountService.getAccountById(id),HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        //revisar autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        //obtener cliente y sus cuentas (no es un DTO porque lo modifico al agregarle su nueva cuenta)
        //antes revisar que el cliente exista
        if(!clientService.existsByEmail(authentication.getName())){
            return new ResponseEntity<>("User not found", HttpStatus.FORBIDDEN);
        }
        Client currentClient=clientService.findClientByEmail(authentication.getName());
        //verificar que cumpla con todas las condiciones
        if(currentClient.getAccounts().size()>=3){
            return new ResponseEntity<>("User has 3 accounts", HttpStatus.FORBIDDEN);
        }
        //generar cuenta
        Account newAccount=accountService.generateAccount();
        //añadirla al cliente
        currentClient.addAccount(newAccount);
        //guardarla
        accountService.saveAccount(newAccount);
        //respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path ="/clients/current/accounts")
    public ResponseEntity<Object> getClientAccount(Authentication authentication){
        //revisar autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        //obtener cliente con sus cuentas
        return new ResponseEntity<>(clientService.getClientDTOByEmail(authentication.getName()).getAccounts(), HttpStatus.ACCEPTED);
    }
}
