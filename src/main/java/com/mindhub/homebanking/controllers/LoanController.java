package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.services.implement.LoanServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanServiceImplement loanService;

    @RequestMapping("/loans")
    public ResponseEntity<Object> getLoans(){
        return loanService.getAllLoans();
    }

    @RequestMapping(path = "/loans",method = RequestMethod.POST)
    public ResponseEntity<Object> addLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        return loanService.applyLoanToClient(authentication.getName(),loanApplicationDTO);
    }
}