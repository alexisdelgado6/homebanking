package com.mindhub.homebanking.Service;


import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    public List<Account> getAllAccounts();
    public Account getAccountById(Long id);
    public Account saveAccount(Account account);
    public Account findAccountByNumber(String number);

}
