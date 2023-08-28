package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardService {
    public ResponseEntity<Object> getCardDTO();
    public ResponseEntity<Object> getCardDTO(long idCard);

    public ResponseEntity<Object> getCardDTObyClientEmail(String clientEmail);

    public ResponseEntity<Object> createClientCard(CardType cardType, CardColor cardColor, String clientEmail);

}
