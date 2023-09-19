package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.util.List;

public interface CardService {
    List<Card> findAllCard();
    List<CardDTO> getAllCardDTO();
    Card findCardById(long id);
    CardDTO getCardById(long id);

    Card findCardByNumber(String number);
    CardDTO getCardByNumber(String number);
    void saveCard(Card card);
    Card generateCard(CardType cardType, CardColor cardColor);

    boolean clientHasThatCard(CardType cardType, CardColor cardColor,String emailClient);
    void deleteLogicalCard(String number);
    boolean cardBelongsToClient(String number, String email);

    boolean cardExistsByNumber(String number);

    boolean cardExistsById(long id);

    void renewCard(String number);
}
