package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    public Loan findLoanByName(String name);
    public List<Loan> findAllLoans();
}
