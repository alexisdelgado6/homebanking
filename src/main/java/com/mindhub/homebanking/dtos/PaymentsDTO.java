package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;

public class PaymentsDTO {
    private long id;

    private String accountNumber;

    private String cardHolder;

    private String number;

    private Double amount;

    private Integer cvv;

    private LocalDate thruDate;

    private String description;

    public PaymentsDTO() {
    }

    public PaymentsDTO(Card card, Transaction transaction, Account account) {
        this.accountNumber = account.getNumber();
        this.cardHolder = card.getCardHolder();
        this.number = card.getCardNumber();
        this.amount = transaction.getAmount();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.description = transaction.getDescription();
    }

    public long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getDescription() {
        return description;
    }
}
