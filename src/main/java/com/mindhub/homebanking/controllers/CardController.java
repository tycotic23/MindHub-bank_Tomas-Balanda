package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.implement.CardServiceImplement;
import com.mindhub.homebanking.services.implement.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardServiceImplement cardService;

    @Autowired
    private ClientServiceImplement clientService;

    @RequestMapping("/cards")
    public ResponseEntity<Object> getAllCards(){
        return new ResponseEntity<>(cardService.getAllCardDTO(),HttpStatus.ACCEPTED);
    }

    /*Retorna las tarjetas del cliente autenticado*/
    @RequestMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentClientCards(Authentication authentication){
        //revisar la autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }

        //obtener cliente
        ClientDTO currentClient= clientService.getClientDTOByEmail(authentication.getName());
        if(currentClient==null){
            return new ResponseEntity<>("User not found",HttpStatus.FORBIDDEN);
        }
        //obtener sus tarjetas
        return new ResponseEntity<>(currentClient.getCards(),HttpStatus.ACCEPTED);
    }

    /*Metodo para crear tarjetas para el cliente autenticado, siempre y cuando el cliente no tenga una*/
    @RequestMapping(path = "/clients/current/cards",method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        //revisar autenticacion
        if(authentication==null) {
            return new ResponseEntity<>("You need to login first", HttpStatus.FORBIDDEN);
        }
        //revisar los datos recibidos que no sean nulos o incorrectos
        if(cardType==null) {
            return new ResponseEntity<>("Missing data: card type is empty", HttpStatus.FORBIDDEN);
        }
        if(cardColor==null) {
            return new ResponseEntity<>("Missing data: card color is empty", HttpStatus.FORBIDDEN);
        }
        //obtener cliente
        Client currentClient= clientService.findClientByEmail(authentication.getName());
        if(currentClient==null){
            return new ResponseEntity<>("User not found",HttpStatus.FORBIDDEN);
        }
        //revisar que no tenga ya ese tipo de tarjeta (puede tener una de cada tipo y de cada color)
        if(cardService.clientHasThatCard(cardType,cardColor,authentication.getName())){
            return new ResponseEntity<>("User already has that card. You only can have one card of each type and color", HttpStatus.FORBIDDEN);
        }
        //crear tarjeta con numero unico
        Card newCard=cardService.generateCard(cardType,cardColor);
        //asociarla al cliente
        currentClient.addCard(newCard);
        //guardar tarjeta en el repositorio
        cardService.saveCard(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
