package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private Set<TransactionDTO> transaction;

    private AccountType accountType;

    private boolean acountStatus;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transaction = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.accountType = account.getAccountType();
        this.acountStatus = account.isAccountStatus();
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public long getId() {
        return id;
    }

    public boolean isAcountStatus() {
        return acountStatus;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransaction() {
        return transaction;
    }
}
