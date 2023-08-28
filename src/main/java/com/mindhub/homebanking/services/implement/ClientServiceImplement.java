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
    public ResponseEntity<Object> getClientDTO() {
        return new ResponseEntity<>(clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> getClientDTO(long id) {
        //obtengo un Client por su id y obtengo su clientID (o null si no lo encuentra)
        return new ResponseEntity<>(clientRepository.findById(id).map(ClientDTO::new).orElse(null),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> getClientDTOByEmail(String email) {
        return new ResponseEntity<>(clientRepository.findByEmail(email).map(ClientDTO::new).orElse(null),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> saveClient(String firstName, String lastName, String email, String password) {
        //verificar datos recibidos
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
        if (clientRepository.findByEmail(email).orElse(null) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }
        //si todo fue correcto crear la nueva entidad Cliente
        Client newClient =new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientRepository.save(newClient);
        //generar nuevo numero de cuenta que no este repetido
        String numberAccount="";
        do{
            numberAccount=generateNumberAccount();
        }while(accountRepository.findByNumber(numberAccount).orElse(null)!=null);
        //crear primera cuenta del cliente
        Account account = new Account(numberAccount);
        newClient.addAccount(account);
        accountRepository.save(account);
        //devolver respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private String generateNumberAccount(){
        //crea un numero aleatorio entre el 0 y el 99999999 de la forma: VIN-nnnnnnnn
        int random= (int) (Math.random() * 99999999);
        return "VIN-"+random;
    }
}
