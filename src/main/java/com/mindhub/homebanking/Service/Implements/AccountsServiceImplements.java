package com.mindhub.homebanking.Service.Implements;

import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsServiceImplements implements AccountService {

    @Autowired
    AccountRepository accountRepository;


    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }


}
