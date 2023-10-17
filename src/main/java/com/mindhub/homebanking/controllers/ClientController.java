package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ClientDTO> getAllClients() {

        return clientService.getClientDTO();
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClientDTO(id);
    }

    @RequestMapping(path="/clients",method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam String password ){
        return clientService.saveClient(firstName, lastName, email, password);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return clientService.getClientDTOByEmail(authentication.getName());
    }


}
