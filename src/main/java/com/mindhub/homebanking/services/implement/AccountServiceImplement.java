package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account generateAccount() {
        //generar nuevo numero de cuenta que no este repetido
        String numberAccount="";
        do{
            numberAccount=generateNumberAccount();
        }while(findByNumber(numberAccount)!=null);
        //generar nueva cuenta con fecha del momento con un numero no repetido
        return new Account(numberAccount);
    }

    @Override
    public Account findByNumber(String number){

        return accountRepository.findByNumber(number).orElse(null);
    }

    private String generateNumberAccount(){
        //crea un numero aleatorio entre el 0 y el 99999999 de la forma: VIN-nnnnnnnn
        int random= (int) (Math.random() * 99999999);
        return "VIN-"+random;
    }

    public List<Account> findAllAccount(){
        return accountRepository.findAll();
    }
    public List<AccountDTO> getAllAccountDTO(){
        return findAllAccount().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    public Account findAccountById(long id){
        return accountRepository.findById(id).orElse(null);
    }
    public AccountDTO getAccountById(long id){
        return new AccountDTO(findAccountById(id));
    }

    public boolean accountBelongsToClient(long id,String email){
        return accountRepository.existsByClient_emailAndId(email,id);
    }
    public boolean existById(long id){
        return accountRepository.existsById(id);
    }

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }


    @Override
    public void saveAccount(Account account) {
        //guardar la cuenta
        accountRepository.save(account);
    }


}
