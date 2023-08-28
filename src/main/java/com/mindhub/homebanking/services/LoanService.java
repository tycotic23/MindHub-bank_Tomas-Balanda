package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanService {
    public ResponseEntity<Object> getAllLoans();
    public ResponseEntity<Object> applyLoanToClient(String clientEmail, LoanApplicationDTO loanApplicationDTO);
}
