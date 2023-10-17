package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class RepositoriesTest {



    @Autowired

    LoanRepository loanRepository;


    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existAccount(){
        List<Account> accounts=accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void exist001Account(){

        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));

    }

    @Test
    public void exist001AccountOwner(){

        List<Client> clients=clientRepository.findAll();

        assertThat(clients, hasItem(hasProperty("accounts")));

    }

    @Test
    public void existClient(){
        List<Client> clients=clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void existMelbaClient(){

        List<Client> clients = clientRepository.findAll();

        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));

    }

    @Test
    public void existCard(){
        List<Card> cards=cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existxCard(){

        List<Card> cards = cardRepository.findAll();

        assertThat(cards, hasItem(hasProperty("number", is("1542 6345 2365 2346"))));

    }


    @Test
    public void existTransaction(){
        List<Transaction> transactions=transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void exist500Transaction(){

        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, hasItem(hasProperty("amount", equalTo(500.0))));

    }


}
