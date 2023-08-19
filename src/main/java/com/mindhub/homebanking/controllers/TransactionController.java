package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @RequestMapping(path = "/transactions",method = RequestMethod.POST)
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestParam double amount, @RequestParam String description, @RequestParam String numberFrom, @RequestParam String numberTo){
        //verificar que los parametros no esten vacios
        if(Double.isNaN(amount)){
            return new ResponseEntity<>("Incorrect amount",HttpStatus.FORBIDDEN);
        }
        if(description.isEmpty()){
            return new ResponseEntity<>("Missing description",HttpStatus.FORBIDDEN);
        }
        if(numberFrom.isEmpty()){
            return new ResponseEntity<>("Missing source account",HttpStatus.FORBIDDEN);
        }
        if(numberTo.isEmpty()){
            return new ResponseEntity<>("Missing destination account",HttpStatus.FORBIDDEN);
        }
        //no debe ser la misma cuenta la de origen y la de destino
        if(numberFrom.equals(numberTo)){
            return new ResponseEntity<>("The accounts must be different",HttpStatus.FORBIDDEN);
        }
        //verificar que las cuentas existan
        Account sourceAccount =accountRepository.findByNumber(numberFrom);
        Account destinationAccount=accountRepository.findByNumber(numberTo);
        if(sourceAccount==null){
            return new ResponseEntity<>("Source account not found",HttpStatus.FORBIDDEN);
        }
        if(destinationAccount==null){
            return new ResponseEntity<>("Destination account not found",HttpStatus.FORBIDDEN);
        }
        //verificar que la cuenta de origen le pertenezca al cliente autenticado
        if(!sourceAccount.getClient().getEmail().equals(authentication.getName())){
            return new ResponseEntity<>("Source account must be yours",HttpStatus.FORBIDDEN);
        }
        //verificar que tenga dinero suficiente
        if(sourceAccount.getBalance()<amount){
            return new ResponseEntity<>("Insufficient funds",HttpStatus.FORBIDDEN);
        }

        //crear transacciones y asociarlos a sus respectivas cuentas
        Transaction debitTransaction=new Transaction(TransactionType.DEBIT,-amount,description, LocalDateTime.now());
        Transaction creditTransaction=new Transaction(TransactionType.CREDIT,amount,description, LocalDateTime.now());
        sourceAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);
        //modificar balances de las cuentas
        sourceAccount.minusBalance(amount);
        destinationAccount.plusBalance(amount);
        //guardar nuevamente las cuentas
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        //guardar transacciones
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
