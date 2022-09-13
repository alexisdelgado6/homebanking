package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionsService {
    public Transaction saveTransaction(Transaction transaction);

    public Set<Transaction> findByDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Account account);
}
