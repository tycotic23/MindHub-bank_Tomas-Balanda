package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public ResponseEntity<Object> getAccountDTO() {
        return new ResponseEntity<>(accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> getAccountDTO(long id) {
        return new ResponseEntity<>( accountRepository.findById(id).map(AccountDTO::new).orElse(null),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> saveAccount(String clientEmail) {
        //verificar que cumpla con todas las condiciones
        if(accountRepository.findByClient_email(clientEmail).size()>=3){
            return new ResponseEntity<>("User has 3 accounts", HttpStatus.FORBIDDEN);
        }
        //obtener el cliente y revisar que exista
        Client client=clientRepository.findByEmail(clientEmail).orElse(null);
        if(client==null) {
            return new ResponseEntity<>("User not found", HttpStatus.FORBIDDEN);
        }
        //si cumple las condiciones se crea una cuenta
        //generar nuevo numero de cuenta que no este repetido
        String numberAccount="";
        do{
            numberAccount=generateNumberAccount();
        }while(accountRepository.findByNumber(numberAccount).orElse(null)!=null);
        //crear la nueva cuenta
        Account account = new Account(numberAccount);
        //añadirla al cliente en cuestion
        client.addAccount(account);
        //guardar la cuenta
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getClientAccountDTO(String clientEmail) {
        //verificar que cumpla con todas las condiciones
        //si cumple las condiciones se crea una cuenta
        return new ResponseEntity<>(accountRepository.findByClient_email(clientEmail).stream().map(AccountDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
    }

    private String generateNumberAccount(){
        //crea un numero aleatorio entre el 0 y el 99999999 de la forma: VIN-nnnnnnnn
        int random= (int) (Math.random() * 99999999);
        return "VIN-"+random;
    }
}
