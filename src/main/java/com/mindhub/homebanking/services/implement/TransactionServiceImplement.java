package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Override
    @Transactional
    public ResponseEntity<Object> saveTransaction(String clientEmail, String fromAccountNumber, String toAccountNumber, double amount, String description) {
        //verificar que los parametros no esten vacios
        if(Double.isNaN(amount)){
            return new ResponseEntity<>("Incorrect amount", HttpStatus.FORBIDDEN);
        }
       /* if(description.isBlank()){
            return new ResponseEntity<>("Missing description",HttpStatus.FORBIDDEN);
        }*/
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
        Account sourceAccount =accountRepository.findByNumber(fromAccountNumber).orElse(null);
        Account destinationAccount=accountRepository.findByNumber(toAccountNumber).orElse(null);
        if(sourceAccount==null){
            return new ResponseEntity<>("Source account not found",HttpStatus.FORBIDDEN);
        }
        if(destinationAccount==null){
            return new ResponseEntity<>("Destination account not found",HttpStatus.FORBIDDEN);
        }
        //verificar que la cuenta de origen le pertenezca al cliente autenticado
        if(!sourceAccount.getClient().getEmail().equals(clientEmail)){
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
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        //guardar transacciones
        //el siguiente codigo comentado se uso para probar el @Transactional forzando el error
        /*Transaction tran=new Transaction();
        tran.setAccount(new Account());
        transactionRepository.save(tran);*/
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
