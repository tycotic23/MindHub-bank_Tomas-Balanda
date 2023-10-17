package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientServiceImplement clientService;

    @Autowired
    private AccountServiceImplement accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public ResponseEntity<Object> getAllClients() {

        return new ResponseEntity<>(clientService.getAllClientDTO(),HttpStatus.ACCEPTED);
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id) {
        ClientDTO client= clientService.getClientDTOById(id);
        if(client==null){
            return new ResponseEntity<>("Client not found",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(client,HttpStatus.ACCEPTED);
    }

    @RequestMapping("/clients/current")
    public ResponseEntity<Object> getCurrentClient(Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        ClientDTO client= clientService.getClientDTOByEmail(authentication.getName());
        if(client==null){
            return new ResponseEntity<>("Client not found",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(client,HttpStatus.ACCEPTED);
    }

    @RequestMapping(path="/clients",method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam String password ){
        //validacion de datos
        if (firstName.isBlank()) {

            return new ResponseEntity<>("Missing data: Please complete the first name", HttpStatus.FORBIDDEN);

        }
        if (lastName.isBlank()) {

            return new ResponseEntity<>("Missing data: Please complete the last name", HttpStatus.FORBIDDEN);

        }
        if (email.isBlank()) {

            return new ResponseEntity<>("Missing data: Please complete the email", HttpStatus.FORBIDDEN);

        }
        if (password.isBlank()) {

            return new ResponseEntity<>("Missing data: Please complete the password", HttpStatus.FORBIDDEN);

        }
        if (clientService.getClientDTOByEmail(email)!=null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }
        //creacion del objeto y guardado en su repositorio
        Client client = new Client(firstName, lastName, email,passwordEncoder.encode(password));
        clientService.saveClient(client);
        //crear cuenta y enlazarla al cliente
        Account account = accountService.generateAccount(); //new Account(numberAccount);
        client.addAccount(account);
        accountService.saveAccount(account);
        //devolver respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




}
