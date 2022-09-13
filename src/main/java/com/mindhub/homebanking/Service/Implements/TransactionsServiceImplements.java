package com.mindhub.homebanking.Service.Implements;

import com.mindhub.homebanking.Service.TransactionsService;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionsServiceImplements implements TransactionsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> findByDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Account account) {
        return transactionRepository.findByDateBetween(fromDate, toDate).stream().filter(transaction -> transaction.getAccount()==account).collect(Collectors.toSet());
    }


}
