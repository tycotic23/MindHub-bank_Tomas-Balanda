package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> findAllLoan() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDTO> getAllLoanDTO() {
        return findAllLoan().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public Loan findLoanById(long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public LoanDTO getLoanDTOById(long id) {
        return new LoanDTO(findLoanById(id));
    }

    @Override
    public boolean existsLoanById(long id) {
        return loanRepository.existsLoanById(id);
    }
}
