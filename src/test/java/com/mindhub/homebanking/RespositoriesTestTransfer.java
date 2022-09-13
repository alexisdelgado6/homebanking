package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class RespositoriesTestTransfer {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void existTypeTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("type", is(DEBIT))));
    }
}
