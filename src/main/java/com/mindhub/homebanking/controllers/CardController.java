package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.services.implement.CardServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardServiceImplement cardService;

    @RequestMapping("/cards")
    public ResponseEntity<Object> getAllCards(){
        return cardService.getCardDTO();
    }

    @RequestMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentClientCards(Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        return cardService.getCardDTObyClientEmail(authentication.getName());
    }

    @RequestMapping(path = "/clients/current/cards",method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        return cardService.createClientCard(cardType,cardColor,authentication.getName());
    }

}
