package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardService {
    public List<CardDTO> getCardDTO();
    public CardDTO getCardDTO(long idCard);

    public List<CardDTO> getCardDTObyClientEmail(String clientEmail);

    public ResponseEntity<Object> createClientCard(String cardType, String cardColor, String clientEmail);

}
