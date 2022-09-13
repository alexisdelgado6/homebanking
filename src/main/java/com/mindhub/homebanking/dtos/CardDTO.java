package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;


import java.time.LocalDate;

public class CardDTO {

    private long id;

    private String cardHolder;

    private CardType cardType;

    private CardColor cardColor;

    private String cardNumber;

    private Integer cvv;

    private LocalDate thruDate;

    private LocalDate fromDate;

    private boolean cardStatus;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cardStatus = card.isCardStatus();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Integer getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public boolean isCardStatus() {
        return cardStatus;
    }
}
