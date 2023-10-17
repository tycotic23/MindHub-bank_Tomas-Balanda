package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    void saveAccount(Account account);
    Account generateAccount();

    Account findByNumber(String number);
    List<Account> findAllAccount();
    List<AccountDTO> getAllAccountDTO();

    Account findAccountById(long id);
    AccountDTO getAccountById(long id);
    boolean accountBelongsToClient(long id, String email);
    boolean existById(long id);
}
