package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> findAllCard() {
        return cardRepository.findAll();
    }

    @Override
    public List<CardDTO> getAllCardDTO() {
        return findAllCard().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public Card findCardById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public CardDTO getCardById(long id) {
        return new CardDTO(findCardById(id));
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card generateCard(CardType cardType, CardColor cardColor) {
        //crear numero de tarjeta unico
        String newCardNumber="";
        do{
            newCardNumber=generateCardNumber();
        }while(cardRepository.findByNumber(newCardNumber).orElse(null)!=null);
        //si se cumplen las condiciones crear entidad Card
        return new Card(newCardNumber,cardType,cardColor);
    }

    public boolean clientHasThatCard(CardType cardType, CardColor cardColor,String emailClient){
        return cardRepository.existsByClient_emailAndTypeAndColor(emailClient,cardType,cardColor);
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
