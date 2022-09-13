package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Service.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    public double amountToPay;

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @Transactional
    @PostMapping(value = "/loans")
    public ResponseEntity<Object> newLoan(
            @RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication
            ){
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findAccountByNumber(loanApplicationDTO.getAccountNumberDestiny());
        Loan loan = loanService.findLoanByName(loanApplicationDTO.getName());
        if (loanApplicationDTO.getAccountNumberDestiny().isEmpty() || loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Missing data.", HttpStatus.FORBIDDEN);
        }
        if (loan == null){
            return new ResponseEntity<>("Loan doesn't exist",HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("You are exceeding the max amount.", HttpStatus.FORBIDDEN);
        }
        if(client.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan()==loan).count()>0){
            return new ResponseEntity<>("You already have this loan", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Number of payments is not available.", HttpStatus.FORBIDDEN);
        }
        if (account == null){
            return new ResponseEntity<>("The account you are trying to get the loan doesn't exist.", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The account isn't yours", HttpStatus.FORBIDDEN);
        }
        else {
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), client, loan);
            switch (loan.getName()){
                case "Personal":
                    switch (clientLoan.getPayments()){
                        case 6: clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                            break;
                        case 12: clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                            break;
                        case 24: clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                            break;
                    }
                    break;
                case "Mortgage":
                    switch (clientLoan.getPayments()){
                        case 12: clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                            break;
                        case 24: clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                            break;
                        case 36: clientLoan.setAmount(clientLoan.getAmount() * 1.45);
                            break;
                        case 48: clientLoan.setAmount(clientLoan.getAmount() * 1.50);
                            break;
                        case 60: clientLoan.setAmount(clientLoan.getAmount() * 1.55);
                            break;
                    }
                    break;
                case "Car":
                    switch (clientLoan.getPayments()) {
                        case 6:
                            clientLoan.setAmount(clientLoan.getAmount() * 1.20);
                            break;
                        case 12:
                            clientLoan.setAmount(clientLoan.getAmount() * 1.30);
                            break;
                        case 24:
                            clientLoan.setAmount(clientLoan.getAmount() * 1.40);
                            break;
                        case 36:
                            clientLoan.setAmount(clientLoan.getAmount() * 1.45);
                            break;
                    }
                    break;
                default:
                    break;
            }
            clientLoanService.saveClientLoan(clientLoan);
        }

            Transaction transaction = new Transaction(account, TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " "+ "loan approved", LocalDateTime.now());
            transactionsService.saveTransaction(transaction);
            account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
            accountService.saveAccount(account);
            return new ResponseEntity<>("loan sucessful", HttpStatus.CREATED);
        }
    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.findAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }
}
