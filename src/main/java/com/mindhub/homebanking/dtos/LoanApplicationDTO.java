package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long loanId;
    private int payments;
    private double amount;
    private String toAccountNumber;


    public LoanApplicationDTO(){}
    public LoanApplicationDTO(long loan_id, int payments, double amount, String accountDestination) {
        this.loanId = loan_id;
        this.payments = payments;
        this.amount = amount;
        this.toAccountNumber = accountDestination;
    }

    public long getLoanId() {
        return loanId;
    }

    public int getPayments() {
        return payments;
    }

    public double getAmount() {
        return amount;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
