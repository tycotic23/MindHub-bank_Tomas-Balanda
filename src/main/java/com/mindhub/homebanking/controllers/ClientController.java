package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientServiceImplement clientService;

    @RequestMapping("/clients")
    public ResponseEntity<Object> getAllClients() {

        return clientService.getClientDTO();
    }

    @RequestMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id) {
        return clientService.getClientDTO(id);
    }

    @RequestMapping(path="/clients",method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam String password ){
        return clientService.saveClient(firstName, lastName, email, password);
    }

    @RequestMapping("/clients/current")
    public ResponseEntity<Object> getCurrentClient(Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        return clientService.getClientDTOByEmail(authentication.getName());
    }


}
