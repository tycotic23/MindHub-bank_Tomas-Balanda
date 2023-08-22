package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<CardDTO> getCardDTO() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public CardDTO getCardDTO(long idCard){
        return cardRepository.findById(idCard).map(CardDTO::new).orElse(null);
    }

    @Override
    public List<CardDTO> getCardDTObyClientEmail(String clientEmail) {
        return cardRepository.findByClient_email(clientEmail).stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> createClientCard(String cardType, String cardColor, String clientEmail) {
        //revisar los datos recibidos que no sean nulos o incorrectos
        if(cardType.isEmpty()) {
            return new ResponseEntity<>("Missing data: card type is empty", HttpStatus.FORBIDDEN);
        }
        if(cardColor.isEmpty()) {
            return new ResponseEntity<>("Missing data: card color is empty", HttpStatus.FORBIDDEN);
        }
        //revisar que no cuenta ya con 3 tarjetas
        if(cardRepository.findByClient_email(clientEmail).size()>=3){
            return new ResponseEntity<>("User has 3 cards", HttpStatus.FORBIDDEN);
        }
        //obtener cliente y revisar que exista
        Client client=clientRepository.findByEmail(clientEmail).orElse(null);
        if(client==null)
            return new ResponseEntity<>("User not found", HttpStatus.FORBIDDEN);
        //si se cumplen las condiciones crear entidad Card
        Card newCard=new Card(CardType.valueOf(cardType),CardColor.valueOf(cardColor));
        client.addCard(newCard);
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
