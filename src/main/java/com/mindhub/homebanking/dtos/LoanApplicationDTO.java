package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanApplicationDTO {
    private long id;
    private double amount;
    private Integer payments;
    private String accountNumberDestiny;
    private String name;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(ClientLoan clientLoan, Account account, Loan loan) {
        this.id = loan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.accountNumberDestiny = account.getNumber();
        this.name = loan.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountNumberDestiny() {
        return accountNumberDestiny;
    }
}


