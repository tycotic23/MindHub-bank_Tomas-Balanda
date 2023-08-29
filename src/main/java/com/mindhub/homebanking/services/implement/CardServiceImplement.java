package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ResponseEntity<Object> getCardDTO() {
        return new ResponseEntity<>(cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> getCardDTO(long idCard){
        return new ResponseEntity<>(cardRepository.findById(idCard).map(CardDTO::new).orElse(null),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> getCardDTObyClientEmail(String clientEmail) {
        //ver que el cliente autenticado exista
        Client client=clientRepository.findByEmail(clientEmail).orElse(null);
        if(client==null){
            return new ResponseEntity<>("User not found",HttpStatus.FORBIDDEN);
        }
        //devolver tarjetas del cliente
        return new ResponseEntity<>(client.getCards().stream().map(CardDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
        //return new ResponseEntity<>(cardRepository.findByClient_email(clientEmail).stream().map(CardDTO::new).collect(Collectors.toList()),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Object> createClientCard(CardType cardType, CardColor cardColor, String clientEmail) {
        //revisar los datos recibidos que no sean nulos o incorrectos
        if(cardType==null) {
            return new ResponseEntity<>("Missing data: card type is empty", HttpStatus.FORBIDDEN);
        }
        if(cardColor==null) {
            return new ResponseEntity<>("Missing data: card color is empty", HttpStatus.FORBIDDEN);
        }
        //revisar que no cuenta ya con 3 tarjetas
        /*if(cardRepository.findByClient_email(clientEmail).size()>=3){
            return new ResponseEntity<>("User has 3 cards", HttpStatus.FORBIDDEN);
        }*/
        //revisar que no tenga ya ese tipo de tarjeta (puede tener una de cada tipo y de cada color)
        if(cardRepository.existsByClient_emailAndTypeAndColor(clientEmail,cardType,cardColor)){
            return new ResponseEntity<>("User already has that card. You only can have one card of each type and color", HttpStatus.FORBIDDEN);
        }
        //obtener cliente y revisar que exista
        Client client=clientRepository.findByEmail(clientEmail).orElse(null);
        if(client==null) {
            return new ResponseEntity<>("User not found", HttpStatus.FORBIDDEN);
        }
        //crear numero de tarjeta unico
        String newCardNumber="";
        do{
            newCardNumber=generateCardNumber();
        }while(cardRepository.findByNumber(newCardNumber).orElse(null)!=null);
        //si se cumplen las condiciones crear entidad Card
        Card newCard=new Card(newCardNumber,cardType,cardColor);//CardType.valueOf(cardType),CardColor.valueOf(cardColor));
        client.addCard(newCard);
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private String generateCardNumber(){
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
