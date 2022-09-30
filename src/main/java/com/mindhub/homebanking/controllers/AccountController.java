package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
        @Autowired
        private AccountService accountService;

        @Autowired
        private ClientService clientService;

        @GetMapping("/account")
        public List<AccountDTO> getAccount(){
            return accountService.getAllAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
        }
        @GetMapping("/account/{id}")
        public AccountDTO getAccount(@PathVariable Long id){
            return new AccountDTO(accountService.getAccountById(id));
        }
        @PostMapping("/clients/current/accounts")
        public ResponseEntity<Object> addAccount(@RequestParam AccountType accountType, Authentication authentication){
            Client client = clientService.findByEmail(authentication.getName());
            List<Account> accountList = client.getAccounts().stream().filter(account -> account.isAccountStatus()).collect(Collectors.toList());
            if (accountList.toArray().length >=3){
                return new ResponseEntity<>("You already have three accounts.", HttpStatus.FORBIDDEN);
            }
            if (accountType == null){
                return new ResponseEntity<>("You must choose a account type", HttpStatus.FORBIDDEN);
            } else {
                Random randomNumber = new Random();
                Account account = new Account("VIN-"+randomNumber.nextInt(99999999), LocalDateTime.now(),0.00, client, accountType);
                accountService.saveAccount(account);
                return new ResponseEntity<>("Account created.", HttpStatus.CREATED);
            }
        }
        @PatchMapping("/clients/current/accounts")
        public ResponseEntity<Object> deletedAccount(@RequestParam String number, Authentication authentication){
            Client client = clientService.findByEmail(authentication.getName());
            Account account = accountService.findAccountByNumber(number);
            if (number.isEmpty()){
                return new ResponseEntity<>("You must choose an account.", HttpStatus.FORBIDDEN);
            }
            if (!client.getAccounts().contains(account)){
                return new ResponseEntity<>("The account isn't yours.", HttpStatus.FORBIDDEN);
            }
            if (client.getAccounts().toArray().length == 1){
                return new ResponseEntity<>("You can't deleted all your accounts", HttpStatus.FORBIDDEN);
            }
            if (account.getBalance() > 0){
                return new ResponseEntity<>("You can't deleted a account with money.", HttpStatus.FORBIDDEN);
            }
            account.setAccountStatus(false);
            accountService.saveAccount(account);
            return new ResponseEntity<>("The accounts was deleted sucessful.", HttpStatus.ACCEPTED);
        }
}
