package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository repo;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll() {

        return repo.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        //obtengo un Client por su id y obtengo su clientID (o null si no lo encuentra)
        return repo.findById(id).map(ClientDTO::new).orElse(null);
    }
}
