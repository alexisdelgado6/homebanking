package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;

    private boolean accountStatus;

    public Account(){}

//    public Account(String number, LocalDateTime creationDate, double balance, AccountType accountType){
//        this.number = number;
//        this.creationDate = creationDate;
//        this.balance = balance;
//        this.accountType = accountType;
//    }

    public Account(String number, LocalDateTime creationDate, double balance, Client client, AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.accountType = accountType;
        this.accountStatus = true;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }
}
