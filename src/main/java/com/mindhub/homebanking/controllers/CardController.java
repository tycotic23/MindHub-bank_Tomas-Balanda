package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    public List<CardDTO> getAllCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentClientCards(Authentication authentication){
        return cardRepository.findByClient_email(authentication.getName()).stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @RequestMapping(path = "/clients/current/cards",method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam String cardType, @RequestParam String cardColor, Authentication authentication){
        //revisar los datos recibidos que no sean nulos o incorrectos
        if(cardType.isEmpty()) {
            return new ResponseEntity<>("Missing data: card type is empty", HttpStatus.FORBIDDEN);
        }
        if(cardColor.isEmpty()) {
            return new ResponseEntity<>("Missing data: card color is empty", HttpStatus.FORBIDDEN);
        }
        //revisar que no cuenta ya con 3 tarjetas
        if(cardRepository.findByClient_email(authentication.getName()).size()>=3){
            return new ResponseEntity<>("User has 3 cards", HttpStatus.FORBIDDEN);
        }
        //si se cumplen las condiciones crear entidad Card
        Card newCard=new Card(CardType.valueOf(cardType),CardColor.valueOf(cardColor),generateNumber(), LocalDate.now(),LocalDate.now().plusYears(5),generateCvv());
        newCard.setClient(clientRepository.findByEmail(authentication.getName()));
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private short generateCvv(){
        return (short) (Math.random() * 999);
    }

    private String generateNumber(){
        DecimalFormat format=new DecimalFormat("0000");
        String number="";
        for(int i=0;i<4;i++){
            number += format.format((int)(Math.random() * 9999));
            if(i!=3){
                number+="-";
            }
        }
        return number;
    }

}
