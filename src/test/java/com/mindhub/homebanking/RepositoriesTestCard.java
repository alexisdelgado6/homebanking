package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTestCard {

    @Autowired
    CardRepository cardRepository;

    @Test
    public void existCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existCardHolder(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cardHolder", is("Melba Styles"))));
    }
}
