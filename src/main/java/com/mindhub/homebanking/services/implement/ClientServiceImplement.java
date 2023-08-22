package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public List<ClientDTO> getClientDTO() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientDTO(long id) {
        //obtengo un Client por su id y obtengo su clientID (o null si no lo encuentra)
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @Override
    public ClientDTO getClientDTOByEmail(String email) {
        return clientRepository.findByEmail(email).map(ClientDTO::new).orElse(null);
    }

    @Override
    public ResponseEntity<Object> saveClient(String firstName, String lastName, String email, String password) {
        //verificar datos recibidos
        if (firstName.isEmpty()) {

            return new ResponseEntity<>("Missing data: Please complete the first name", HttpStatus.FORBIDDEN);

        }
        if (lastName.isEmpty()) {

            return new ResponseEntity<>("Missing data: Please complete the last name", HttpStatus.FORBIDDEN);

        }
        if (email.isEmpty()) {

            return new ResponseEntity<>("Missing data: Please complete the email", HttpStatus.FORBIDDEN);

        }
        if (password.isEmpty()) {

            return new ResponseEntity<>("Missing data: Please complete the password", HttpStatus.FORBIDDEN);

        }
        if (clientRepository.findByEmail(email).orElse(null) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }
        //si todo fue correcto crear la nueva entidad Cliente
        Client newClient =new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientRepository.save(newClient);
        //crear primera cuenta del cliente
        Account account = new Account();
        newClient.addAccount(account);
        accountRepository.save(account);
        //devolver respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
