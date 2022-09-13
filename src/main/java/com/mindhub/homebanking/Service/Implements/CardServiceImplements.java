package com.mindhub.homebanking.Service.Implements;

import com.mindhub.homebanking.Service.CardService;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplements implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findByCardNumber(String cardNumber) {
        return cardRepository.findBycardNumber(cardNumber);
    }

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
}
