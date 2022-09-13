package com.mindhub.homebanking.Service;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    public Card findByCardNumber(String cardNumber);
    public Card saveCard(Card card);
}
