package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Service.CardService;
import com.mindhub.homebanking.Service.ClientService;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
    @RequestMapping("/api")
    public class CardController {

        @Autowired
        private CardService cardService;

        @Autowired
        private ClientService clientService;

        @PostMapping(value = "/clients/current/cards")
        public ResponseEntity<Object>createCard(
                @RequestParam CardColor cardColor, @RequestParam CardType cardType,
                Authentication authentication
                ){
                Client client = clientService.findByEmail(authentication.getName());
                List<Card> cardListActive = client.getCards().stream().filter(card -> card.isCardStatus()).collect(Collectors.toList());
                if (cardColor == null || cardType == null){
                    return new ResponseEntity<>("You must choose the card color or type", HttpStatus.FORBIDDEN);
                }
                if (cardListActive.stream().filter(card -> card.getCardType().equals(cardType)).filter(card -> card.getCardColor().equals(cardColor)).count()>0){
                    return new ResponseEntity<>("You only can have one cards of the same color.", HttpStatus.FORBIDDEN);
                }
                if (cardListActive.stream().filter(card -> card.getCardType().equals (cardType)).count() >=3){
                    return new ResponseEntity<>("You only can have three cards of the same type.", HttpStatus.FORBIDDEN);
                } else {
                    int cvvRandom = CardUtils.getCcvRandomNumber();
                    String cardNumber = CardUtils.getRandomCardNumber();
                    while (cardService.findByCardNumber(cardNumber)!=null){
                        cardNumber = CardUtils.getRandomCardNumber();
                    };
                    Card card = new Card(client.getFirstName()+" "+client.getLastName(),cardType,cardColor,cardNumber,cvvRandom,LocalDate.now().plusYears(5),LocalDate.now(), client, true);
                    cardService.saveCard(card);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
        }
        @PatchMapping("/clients/current/cards")
        public ResponseEntity<Object>disableCard(
                @RequestParam String cardNumber, Authentication authentication){
            Client client = clientService.findByEmail(authentication.getName());
            Card card = cardService.findByCardNumber(cardNumber);
            if(cardNumber.isEmpty()){
                return new ResponseEntity<>("Please select a card", HttpStatus.FORBIDDEN);
            }
            if (!client.getCards().contains(card)) {
                return new ResponseEntity<>("The card isn't yours", HttpStatus.FORBIDDEN);
            }
            card.setCardStatus(false);
            cardService.saveCard(card);
            return new ResponseEntity<>("The card is disable", HttpStatus.ACCEPTED);
        }
    }
