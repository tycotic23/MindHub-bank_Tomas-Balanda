package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.implement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanServiceImplement loanService;

    @Autowired
    private ClientServiceImplement clientService;

    @Autowired
    private AccountServiceImplement accountService;

    @Autowired
    private TransactionServiceImplement transactionService;

    @Autowired
    private ClientLoanServiceImplement clientLoanService;

    @RequestMapping("/loans")
    public ResponseEntity<Object> getLoans(){
        return new ResponseEntity<>(loanService.getAllLoanDTO(),HttpStatus.ACCEPTED);
    }

    /*Crear un Loan y aplicarselo al cliente*/
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        //revisar autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        //validar datos recibidos
        //revisar que los datos no esten vacios
        if(authentication.getName().isBlank()) {
            return new ResponseEntity<>("Missing client", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getToAccountNumber().isBlank()) {
            return new ResponseEntity<>("Missing number account", HttpStatus.FORBIDDEN);
        }
        //revisar que amount no sea 0 ni negativo
        if(loanApplicationDTO.getAmount()<=0) {
            return new ResponseEntity<>("Amount can't be zero", HttpStatus.FORBIDDEN);
        }
        //revisar que el tipo de préstamo exista
        if(!loanService.existsLoanById(loanApplicationDTO.getLoanId())){
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
        Loan loanType=loanService.findLoanById(loanApplicationDTO.getLoanId());
        //revisar que no exceda el monto máximo
        if(loanType.getMaxAmount()<loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("Maximum amount reached", HttpStatus.FORBIDDEN);
        }
        //validar cantidad de pagos elegidos
        if(!loanType.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payments incorrect", HttpStatus.FORBIDDEN);
        }
        //revisar que la cuenta de destino exista
        if(!accountService.existsByNumber(loanApplicationDTO.getToAccountNumber())){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }
        Account destinationAccount=accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        //revisar que el cliente exista
        if(!clientService.existsByEmail(authentication.getName())){
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
        Client client=clientService.findClientByEmail(authentication.getName());
        //revisar que la cuenta pertenezca al cliente
        if(!client.getAccounts().contains(destinationAccount)) {
            return new ResponseEntity<>("The account does not belong to the current client", HttpStatus.FORBIDDEN);
        }

        //crear nueva entidad y asociar cliente-prestamo
        ClientLoan newLoan=new ClientLoan(client,loanType,loanApplicationDTO.getAmount()*1.2,loanApplicationDTO.getPayments());
        client.addLoan(newLoan);
        loanType.addClient(newLoan);

        //crear transaccion
        Transaction creditTransaction=new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(), loanType.getName()+" loan approved", LocalDateTime.now());
        //asociar transaccion al cliente
        destinationAccount.addTransaction(creditTransaction);
        //sumar monto a la cuenta
        destinationAccount.plusBalance(loanApplicationDTO.getAmount());

        //guardar la cuenta
        accountService.saveAccount(destinationAccount);
        //guardar transaccion
        transactionService.saveTransaction(creditTransaction);
        //guardar prestamo del cliente
        clientLoanService.saveClientLoan(newLoan);

        //devolver respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
