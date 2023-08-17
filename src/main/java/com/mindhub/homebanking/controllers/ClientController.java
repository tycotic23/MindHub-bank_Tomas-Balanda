package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll() {

        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        //obtengo un Client por su id y obtengo su clientID (o null si no lo encuentra)
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @RequestMapping(path="/clients",method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam String password ){
        //verificar datos recibidos
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }
        //si todo fue correcto crear la nueva entidad Cliente
        Client newClient =new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientRepository.save(newClient);
        //crear primera cuenta del cliente
        Account account = new Account("VIN-"+(int) (Math.random() * 99999999), LocalDate.now(),0.0);
        account.setClient(newClient);
        accountRepository.save(account);
        //devolver respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }


}
