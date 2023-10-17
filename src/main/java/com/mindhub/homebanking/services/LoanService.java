package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> findAllLoan();
    List<LoanDTO> getAllLoanDTO();

    Loan findLoanById(long id);
    LoanDTO getLoanDTOById(long id);

    boolean existsLoanById(long id);
}
