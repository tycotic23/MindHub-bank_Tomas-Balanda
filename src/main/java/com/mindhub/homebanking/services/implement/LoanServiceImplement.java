package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public ResponseEntity<Object> getAllLoans() {
        return new ResponseEntity<>(loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> applyLoanToClient(String clientEmail, LoanApplicationDTO loanApplicationDTO) {
        //revisar que los datos no esten vacios
        if(clientEmail.isBlank()) {
            return new ResponseEntity<>("Missing client", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getToAccountNumber().isBlank()) {
            return new ResponseEntity<>("Missing number account", HttpStatus.FORBIDDEN);
        }
        //revisar que amount no sea 0 ni negativo
        if(loanApplicationDTO.getAmount()<=0) {
            return new ResponseEntity<>("Amount can't be zero", HttpStatus.FORBIDDEN);
        }
        //revisar que payments no sea 0
        if(loanApplicationDTO.getPayments()==0) {
            return new ResponseEntity<>("Payments can't be zero", HttpStatus.FORBIDDEN);
        }
        //revisar que el tipo de préstamo exista
        Loan loanType=loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);
        if(loanType==null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
        //revisar que no exceda el monto máximo
        if(loanType.getMaxAmount()<loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("Maximum amount reached", HttpStatus.FORBIDDEN);
        }
        //validar cantidad de pagos elegidos
        if(!loanType.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payments incorrect", HttpStatus.FORBIDDEN);
        }
        //revisar que la cuenta exista
        Account destinationAccount=accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber()).orElse(null);
        if(destinationAccount==null) {
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }
        //revisar que el cliente exista
        Client client=clientRepository.findByEmail(clientEmail).orElse(null);
        if(client==null) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
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
        accountRepository.save(destinationAccount);
        //guardar transaccion
        transactionRepository.save(creditTransaction);
        //guardar prestamo del cliente
        clientLoanRepository.save(newLoan);
        //devolver respuesta
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
