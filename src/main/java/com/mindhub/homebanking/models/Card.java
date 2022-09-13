package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private long id;

        @ManyToOne (fetch = FetchType.EAGER)
        @JoinColumn(name = "client_id")
        private Client client;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name="account_id")
        private Account account;

        private String cardHolder;

        private CardType cardType;

        private CardColor cardColor;

        private String cardNumber;

        private Integer cvv;

        private LocalDate thruDate;

        private LocalDate fromDate;

        private boolean cardStatus = true;

        public Card() {
        }

//        public Card(Client client, CardType cardType, CardColor cardColor, String cardNumber, Integer cvv, LocalDate thruDate, LocalDate fromDate, boolean cardStatus) {
//                this.cardHolder = (client.getFirstName() + " "+ client.getLastName());
//                this.cardType = cardType;
//                this.cardColor = cardColor;
//                this.cardNumber = cardNumber;
//                this.cvv = cvv;
//                this.thruDate = thruDate;
//                this.fromDate = fromDate;
//                this.cardStatus = cardStatus;
//        }

        public Card(String cardHolder, CardType cardType, CardColor cardColor, String cardNumber, Integer cvv, LocalDate thruDate, LocalDate fromDate, Client client, boolean cardStatus) {
                this.cardHolder = cardHolder;
                this.cardType = cardType;
                this.cardColor = cardColor;
                this.cardNumber = cardNumber;
                this.cvv = cvv;
                this.thruDate = thruDate;
                this.fromDate = fromDate;
                this.client = client;
                this.cardStatus = cardStatus;
        }

        public Card(Client client, String cardHolder, CardType cardType, CardColor cardColor, String cardNumber, Integer cvv, LocalDate thruDate, LocalDate fromDate, boolean cardStatus, Account account) {
                this.client = client;
                this.cardHolder = cardHolder;
                this.cardType = cardType;
                this.cardColor = cardColor;
                this.cardNumber = cardNumber;
                this.cvv = cvv;
                this.thruDate = thruDate;
                this.fromDate = fromDate;
                this.cardStatus = cardStatus;
        }

        public Account getAccount() {
                return account;
        }

        public void setAccount(Account account) {
                this.account = account;
        }

        public long getId() {
                return id;
        }

        public Client getClient() {
                return client;
        }

        public void setClient(Client client) {
                this.client = client;
        }

        public CardType getCardType() {
                return cardType;
        }

        public void setCardType(CardType cardType) {
                this.cardType = cardType;
        }

        public CardColor getCardColor() {
                return cardColor;
        }

        public void setCardColor(CardColor cardColor) {
                this.cardColor = cardColor;
        }

        public String getCardNumber() {
                return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
                this.cardNumber = cardNumber;
        }

        public Integer getCvv() {
                return cvv;
        }

        public void setCvv(Integer cvv) {
                this.cvv = cvv;
        }

        public LocalDate getThruDate() {
                return thruDate;
        }

        public void setThruDate(LocalDate thruDate) {
                this.thruDate = thruDate;
        }

        public LocalDate getFromDate() {
                return fromDate;
        }

        public void setFromDate(LocalDate fromDate) {
                this.fromDate = fromDate;
        }

        public String getCardHolder() {
                return cardHolder;
        }

        public void setCardHolder(String cardHolder) {
                this.cardHolder = cardHolder;
        }

        public boolean isCardStatus() {
                return cardStatus;
        }

        public void setCardStatus(boolean cardStatus) {
                this.cardStatus = cardStatus;
        }
}
