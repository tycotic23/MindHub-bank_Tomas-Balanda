package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class,args);
	}
/*
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
		return (args -> {

			Transaction tran1=new Transaction(TransactionType.CREDIT,500.0,"compra de helado", LocalDateTime.now());
			Transaction tran2=new Transaction(TransactionType.DEBIT,-1500.0,"compra de helado", LocalDateTime.now());
			Transaction tran3=new Transaction(TransactionType.CREDIT,1500.0,"compra de helado", LocalDateTime.now());
			Transaction tran4=new Transaction(TransactionType.CREDIT,12500.0,"compra de helado", LocalDateTime.now());
			Transaction tran5=new Transaction(TransactionType.DEBIT,-5040.0,"compra de helado", LocalDateTime.now());
			Transaction tran6=new Transaction(TransactionType.CREDIT,250.0,"compra de helado", LocalDateTime.now());

			Account account1=new Account("VIN001", LocalDate.now(),5000.0);
			Account account2=new Account("VIN002", LocalDate.now().plusDays(1),7500.0);
			Account account3=new Account("VIN003", LocalDate.now(),15000.0);
			Account account4=new Account("VIN004", LocalDate.now().minusDays(2),77500.0);

            Card card1=new Card(CardType.DEBIT,CardColor.GOLD,"1542 6345 2365 2346",LocalDate.now(),LocalDate.now().plusYears(5),(short)329);
			Card card2=new Card(CardType.CREDIT,CardColor.TITANIUM,"4723 8873 4473 4572",LocalDate.now(),LocalDate.now().plusYears(5),(short)891);
			Card card3=new Card(CardType.CREDIT,CardColor.SILVER,"8236 7612 1623 7253",LocalDate.now(),LocalDate.now().plusYears(5),(short)567);


            Loan loan1=new Loan("Hipotecario",500000.0, List.of(12,24,36,48,60));
            Loan loan2=new Loan("Personal",100000.0, List.of(6,12,24));
            Loan loan3=new Loan("Automotriz",300000.0, List.of(6,12,24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			account1.addTransaction(tran1);
			account3.addTransaction(tran2);
			account3.addTransaction(tran3);
			account4.addTransaction(tran4);
			account2.addTransaction(tran5);
			account1.addTransaction(tran6);

			Client client1=new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("1234"));
			Client client2=new Client("Mirta","Digiacomo","mirta@mindhub.com", passwordEncoder.encode("tito"));
            Client client3 = new Client ("admin","admin","admin@admin.com", passwordEncoder.encode("admin"));



			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			ClientLoan cl1=new ClientLoan(client1,loan1,400000.0,60);
			ClientLoan cl2=new ClientLoan(client1,loan2,50000.0,12);
			ClientLoan cl3=new ClientLoan(client2,loan2,100000.0,24);
			ClientLoan cl4=new ClientLoan(client2,loan3,200000.0,36);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);



			clientRepository.save(client1);
            clientRepository.save(client2);
            clientRepository.save(client3);

            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(tran1);
			transactionRepository.save(tran2);
			transactionRepository.save(tran3);
			transactionRepository.save(tran4);
			transactionRepository.save(tran5);
			transactionRepository.save(tran6);

			clientLoanRepository.save(cl1);
			clientLoanRepository.save(cl2);
			clientLoanRepository.save(cl3);
			clientLoanRepository.save(cl4);

		});
	}*/
}
