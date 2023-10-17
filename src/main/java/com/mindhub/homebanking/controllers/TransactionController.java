package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;
import com.mindhub.homebanking.services.implement.TransactionServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionServiceImplement transactionService;

    @Autowired
    private AccountServiceImplement accountService;


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam double amount, @RequestParam String description){
        //revisar autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        //verificar que los parametros no esten vacios
        if(Double.isNaN(amount)){
            return new ResponseEntity<>("Incorrect amount", HttpStatus.FORBIDDEN);
        }
        if(fromAccountNumber.isBlank()){
            return new ResponseEntity<>("Missing source account",HttpStatus.FORBIDDEN);
        }
        if(toAccountNumber.isBlank()){
            return new ResponseEntity<>("Missing destination account",HttpStatus.FORBIDDEN);
        }
        //no debe ser la misma cuenta la de origen y la de destino
        if(fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("The accounts must be different",HttpStatus.FORBIDDEN);
        }
        //verificar que las cuentas existan
        if(!accountService.existsByNumber(fromAccountNumber)){
            return new ResponseEntity<>("Source account not found",HttpStatus.FORBIDDEN);
        }
        if(!accountService.existsByNumber(toAccountNumber)){
            return new ResponseEntity<>("Destination account not found",HttpStatus.FORBIDDEN);
        }
        Account sourceAccount =accountService.findByNumber(fromAccountNumber);
        Account destinationAccount=accountService.findByNumber(toAccountNumber);
        //verificar que la cuenta de origen le pertenezca al cliente autenticado
        if(!sourceAccount.getClient().getEmail().equals(authentication.getName())){
            return new ResponseEntity<>("Source account must be yours",HttpStatus.FORBIDDEN);
        }
        //verificar el monto no sea cero o menor
        if(amount<=0){
            return new ResponseEntity<>("Incorrect amount",HttpStatus.FORBIDDEN);
        }
        //verificar que tenga dinero suficiente
        if(sourceAccount.getBalance()<amount){
            return new ResponseEntity<>("Insufficient funds",HttpStatus.FORBIDDEN);
        }
        //crear transacciones y asociarlos a sus respectivas cuentas
        Transaction debitTransaction=new Transaction(TransactionType.DEBIT,-amount,description+destinationAccount.getNumber(), LocalDateTime.now());
        Transaction creditTransaction=new Transaction(TransactionType.CREDIT,amount,description+sourceAccount.getNumber(), LocalDateTime.now());
        sourceAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);
        //modificar balances de las cuentas
        sourceAccount.minusBalance(amount);
        destinationAccount.plusBalance(amount);
        //guardar nuevamente las cuentas
        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);
        //guardar transacciones
        //el siguiente codigo comentado se uso para probar el @Transactional forzando el error
        /*Transaction tran=new Transaction();
        tran.setAccount(new Account());
        transactionService.saveTransaction(tran);*/
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
