package com.mindhub.homebanking.Service.Implements;

import com.mindhub.homebanking.Service.LoanService;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImplements implements LoanService {
    @Autowired
    private LoanRepository loanRepository;


    @Override
    public Loan findLoanByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public List<Loan> findAllLoans() {
        return loanRepository.findAll();
    }


}
